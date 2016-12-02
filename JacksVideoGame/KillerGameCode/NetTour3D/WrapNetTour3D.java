
// WrapNetTour3D.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* Sets up a Tour3D world, but one including visitors from 
   other machines.

   New net-related code:
     * makeContact() for making contact with the TourServer;

     * closeLink() to break the link with the server and exit;

     * addVisitor() for creating distributed tourist(s),
       called by TourWatcher when the server sends it a request;

     * sendDetails() for sending sprite info to the server

  Minor differences from Tour3D:
     * no alien
     * no full-screen
     * no keyboard-driven quitting; use the close box instead
     * the frame is 500x300 rather than 512x512
     * light blue background, not a texture
     * position the tourist with command-line args
*/

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.text.DecimalFormat;

import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.geometry.*;
import javax.media.j3d.*;
import javax.vecmath.*;



public class WrapNetTour3D extends JPanel
// Holds the 3D canvas where the loaded image is displayed
{
  private static final int PWIDTH = 500;     // size of panel
  private static final int PHEIGHT = 300; 
  private static final int BOUNDSIZE = 100;  // larger than world

  private static final int PORT = 5555;      // server details
  private static final String HOST = "localhost";


  private SimpleUniverse su;
  private BranchGroup sceneBG;
  private BoundingSphere bounds;   // for environment nodes

  private Obstacles obs;
  private TourSprite bob;   // the tourist

  private Socket sock;
  private BufferedReader in;     // IO for the client
  private PrintWriter out;  
  private DecimalFormat df;   // for simpler output



  public WrapNetTour3D(String userName, String tourFnm, 
                                      double xPosn, double zPosn)
  // construct the 3D canvas
  {
    setLayout( new BorderLayout() );
    setOpaque( false );
    df = new DecimalFormat("0.####");  // 4 dp

    setPreferredSize( new Dimension(PWIDTH, PHEIGHT));
    GraphicsConfiguration config =
					SimpleUniverse.getPreferredConfiguration();
    Canvas3D canvas3D = new Canvas3D(config);
    add("Center", canvas3D);

    canvas3D.setFocusable(true);
    canvas3D.requestFocus();

    su = new SimpleUniverse(canvas3D);
    createSceneGraph(userName, tourFnm, xPosn, zPosn);

    su.addBranchGraph( sceneBG );
  } // end of WrapNetTour3D()


  void createSceneGraph(String userName, String tourFnm,
									double xPosn, double zPosn) 
  // initilise the scene
  { 
    sceneBG = new BranchGroup();
    bounds = new BoundingSphere(new Point3d(0,0,0), BOUNDSIZE); 

    // to allow clients to be added/removed from the world at run time
    sceneBG.setCapability(Group.ALLOW_CHILDREN_READ);
    sceneBG.setCapability(Group.ALLOW_CHILDREN_WRITE);
    sceneBG.setCapability(Group.ALLOW_CHILDREN_EXTEND);


    lightScene();         // add the lights
    addBackground();      // add the sky
    sceneBG.addChild( new CheckerFloor().getBG() );  // add the floor

    makeScenery(tourFnm);      // add scenery and obstacles

    makeContact();     // contact server (after Obstacles object created)

    addTourist(userName, xPosn, zPosn);      
					   // add the user-controlled 3D sprite
    sceneBG.compile();   // fix the scene
  } // end of createSceneGraph()



  private void lightScene()
  // One ambient light, 2 directional lights
  {
    Color3f white = new Color3f(1.0f, 1.0f, 1.0f);

    // Set up the ambient light
    AmbientLight ambientLightNode = new AmbientLight(white);
    ambientLightNode.setInfluencingBounds(bounds);
    sceneBG.addChild(ambientLightNode);

    // Set up the directional lights
    Vector3f light1Direction  = new Vector3f(-1.0f, -1.0f, -1.0f);
       // left, down, backwards 
    Vector3f light2Direction  = new Vector3f(1.0f, -1.0f, 1.0f);
       // right, down, forwards

    DirectionalLight light1 = 
            new DirectionalLight(white, light1Direction);
    light1.setInfluencingBounds(bounds);
    sceneBG.addChild(light1);

    DirectionalLight light2 = 
        new DirectionalLight(white, light2Direction);
    light2.setInfluencingBounds(bounds);
    sceneBG.addChild(light2);
  }  // end of lightScene()



  private void addBackground()
  // A blue sky
  { Background back = new Background();
    back.setApplicationBounds( bounds );
    back.setColor(0.17f, 0.65f, 0.92f);    // sky colour
    sceneBG.addChild( back );
  }  // end of addBackground()



  // --------------------- obstacles -----------------------

  private void makeScenery(String tourFnm)
  /* Parse the tour file which contains the names of scenery
     objects to load, and the position of obstacles.
     The format of the tour filename is:
        <object file>
        [-o (x1,z1) (x2,z2) ... ]     // obstacle coordinates
        <object file>
               :
     The scenery is loaded with PropManager objects, and we assume
     the existence of "coords" data files for thscenery models.

     The obstacles are stored in an Obstacles object, and then added
     to the scene.
  */
  { obs = new Obstacles();
    PropManager propMan;
    String tourFile = "models/"+tourFnm;
    System.out.println("Loading tour file:" + tourFile);
    try {
      BufferedReader br = new BufferedReader( new FileReader(tourFile));
      String line;
      while((line = br.readLine()) != null) {
        System.out.println(line);
        if (line.startsWith("-o")) 
          obs.store( line.substring(2).trim() );    // save obstacle info
        else {
          propMan = new PropManager(line.trim(),true);  // load scenery
          sceneBG.addChild( propMan.getTG() );    // add to world
        }
      }
      br.close();
      // System.out.println("Finished reading tour file: " + tourFile);
      // obs.print();  // for debugging
      sceneBG.addChild( obs.getObsGroup() );  // add obstacles to the scene
    } 
    catch (IOException e) 
    { System.out.println("Error reading tour file: " + tourFile);
      System.exit(1);
    }
  } // end of makeScenery()



  // --------------------- tourists ---------------------------


  private void addTourist(String userName, double xPosn, double zPosn)
  // create sprite for this client
  {
    bob = new TourSprite(userName, "Coolrobo.3ds", obs, 
								xPosn, zPosn, out);   // sprite
    sceneBG.addChild( bob.getBG() );

    ViewingPlatform vp = su.getViewingPlatform();
    TransformGroup viewerTG = vp.getViewPlatformTransform();   // view point TG

    TouristControls tcs = new TouristControls(bob, viewerTG);   // sprite's controls
    tcs.setSchedulingBounds( bounds );

    sceneBG.addChild( tcs );
  } // end of addTourist()



  public DistTourSprite addVisitor(String userName, 
						double xPosn, double zPosn, double rotRadians)
  /* Create a sprite for a visitor to the world. 
     This method is called from TourWatcher, in response to a 
     request from the TourServer.

     There were intermittent bugs with adding a new BranchGroup
     at run time. Solved (I think) by compiling the new 
     BranchGroup, and delaying a little before adding it.
  */
  {  
     DistTourSprite dtSprite = 
			new DistTourSprite(userName, "Coolrobo.3ds", obs, xPosn, zPosn);
     if (rotRadians != 0)
       dtSprite.setCurrRotation( rotRadians);

     BranchGroup sBG = dtSprite.getBG();

     sBG.compile();    // generally a good idea     
     try {
       Thread.sleep(200);    // delay a little, so world has been finished
     }
     catch(InterruptedException e) {}
     sceneBG.addChild( sBG ); 

     if (!sBG.isLive())    // just in case, but problem seems solved
       System.out.println("Visitor Sprite is NOT live");
     else
       System.out.println("Visitor Sprite is now live");

     return dtSprite;
   } // end of addVisitor()




  // ------------------------- network related --------------------------

    
  private void makeContact()
  /* Contact the server, and set up a TourWatcher to monitor the
     server. */
  {
    try {
      sock = new Socket(HOST, PORT);
      in  = new BufferedReader( 
		  		new InputStreamReader( sock.getInputStream() ) );
      out = new PrintWriter( sock.getOutputStream(), true );  // autoflush

      new TourWatcher(this, in, obs).start();    // start watching for server msgs
    }
    catch(Exception e)
    { System.out.println("No contact with server");
      System.exit(0);
    }
  }  // end of makeContact()



  public void closeLink()
  /* Called by the top-level JFrame when the close box is clicked.
     Say goodbye to server, and exit. This will kill the
     TourWatcher thread as well. */
  {
    try {
      out.println("bye");    // tell server that client is disconnecting
      sock.close();
    }
    catch(Exception e)
    {  System.out.println("Link terminated"); }

    System.exit( 0 ); 
  } // end of closeLink()



  public void sendDetails(String cliAddr, String strPort)
  /* Send details of local sprite to the client at location 
    (cliAddr, strPort).
     Msg format:  
         detailsFor cliAddr strPort xPosn zPosn rotRadians
  */
  {  Point3d currLoc = bob.getCurrLoc();
     double currRotation = bob.getCurrRotation();
     String msg = new String("detailsFor " + cliAddr + " " +
						strPort + " " + 
						df.format(currLoc.x) + " " +
						df.format(currLoc.z) + " " + 
						df.format(currRotation) );
     out.println(msg);
  }  // end of sendDetails()


} // end of WrapNetTour3D class