
// WrapTour3D.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* Usual checkboard world but with:
       * extra scenery and obstacles
           - the scenery is loaded with PropManager objects
           - the obstacles cannot be passed through by the sprites
       * a touring 3D sprite controlled from the keyboard
       * an autonomous chasing alien hand
       * a view point that moves with the sprite (a 3rd person camera)
       * textured background
       * full screen (type 'q' or <enter> to exit)
*/

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;

import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.geometry.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.image.TextureLoader;


// import com.tornadolabs.j3dtree.*;    // for displaying the scene graph


public class WrapTour3D extends JPanel
// Holds the 3D canvas where the loaded image is displayed
{
  private static final int PWIDTH = 512;   // size of panel
  private static final int PHEIGHT = 512; 

  private static final int BOUNDSIZE = 100;  // larger than world


  private SimpleUniverse su;
  private BranchGroup sceneBG;
  private BoundingSphere bounds;   // for environment nodes

  private JFrame win;   // required at quit time

  private Obstacles obs;
  private TourSprite bob;   // the tourist

  // private Java3dTree j3dTree;   // frame to hold tree display


  public WrapTour3D(String tourFnm, JFrame jf)
  // construct the 3D canvas
  {
    win = jf;
    setLayout( new BorderLayout() );
    setOpaque( false );

    // setPreferredSize( new Dimension(PWIDTH, PHEIGHT));
    setPreferredSize( Toolkit.getDefaultToolkit().getScreenSize() );  // full screen

    GraphicsConfiguration config =
					SimpleUniverse.getPreferredConfiguration();
    Canvas3D canvas3D = new Canvas3D(config);
    add("Center", canvas3D);

    canvas3D.setFocusable(true);
    canvas3D.requestFocus();    // the canvas now has focus, so receives key events

	canvas3D.addKeyListener( new KeyAdapter() {
	// listen for esc, q, end, ctrl-c on the canvas to
	// allow a convenient exit from the full screen configuration
       public void keyPressed(KeyEvent e)
       { int keyCode = e.getKeyCode();
         if ((keyCode == KeyEvent.VK_ESCAPE) || (keyCode == KeyEvent.VK_Q) ||
             (keyCode == KeyEvent.VK_END) ||
             ((keyCode == KeyEvent.VK_C) && e.isControlDown()) ) {
           win.dispose();
           System.exit(0);    // exit() alone isn't sufficient most of the time
         }
       }
     });

    su = new SimpleUniverse(canvas3D);

    // j3dTree = new Java3dTree();   // create a display tree for the SG

    createSceneGraph(tourFnm);
    su.addBranchGraph( sceneBG );

	// j3dTree.updateNodes( su );    // build the tree display window

  } // end of WrapTour3D()


  private void createSceneGraph(String tourFnm) 
  // initilise the scene
  { 
    sceneBG = new BranchGroup();
    bounds = new BoundingSphere(new Point3d(0,0,0), BOUNDSIZE); 

    lightScene();         // add the lights
    addBackground();      // add the sky
    sceneBG.addChild( new CheckerFloor().getBG() );  // add the floor

    makeScenery(tourFnm);      // add scenery and obstacles
    addTourist();              // add the user-controlled 3D sprite

    addAlien();                // add self-guided alien 3D sprite

	// j3dTree.recursiveApplyCapability( sceneBG );   // set capabilities for tree display

    sceneBG.compile();   // fix the scene
  } // end of createSceneGraph()



  private void lightScene()
  /* One ambient light, 2 directional lights */
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
  // a blue sky with clouds backdrop using a scaled texture
  {
    TextureLoader bgTexture = new TextureLoader("models/bigSky.jpg", null);
	Background back = new Background(bgTexture.getImage());
    back.setImageScaleMode(Background.SCALE_FIT_MAX);
    // back.setImageScaleMode(Background.SCALE_REPEAT);   // tiling approach
    back.setApplicationBounds( bounds );

    sceneBG.addChild( back );
  } // end of addBackground() v.2



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



  // --------------------- tourist ---------------------------


  private void addTourist()
  /* The TourSprite is a robot. The sprite and its control behaviour
     are created at he same time. The TouristControls object affects
     the view point, so its TG is passed to it.
  */
  {
    bob = new TourSprite("Coolrobo.3ds", obs);   // sprite
    bob.setPosition(2.0, 1.0);  
    sceneBG.addChild( bob.getTG() );

    ViewingPlatform vp = su.getViewingPlatform();
    TransformGroup viewerTG = vp.getViewPlatformTransform();   // view point TG

    TouristControls tcs = new TouristControls(bob, viewerTG);   // sprite's controls
    tcs.setSchedulingBounds( bounds );
    sceneBG.addChild( tcs );
  } // end of addTourist()


  // -------------------------- alien ---------------------------

  private void addAlien()
  /* The alien is a hand. It's position is updated periodically by
      calls from a TimeBehavior object. The alien's behaviour is to
      try to catch the TourSprite.
  */
  { AlienSprite al = 
       new AlienSprite("hand1.obj", obs, bob);   // alien
    al.setPosition(-6.0, -6.0);
    sceneBG.addChild( al.getTG() );

    TimeBehavior alienTimer = new TimeBehavior(500, al);  // alien's controls
    alienTimer.setSchedulingBounds( bounds );
    sceneBG.addChild( alienTimer );
  } // end of addAlien()


} // end of WrapTour3D class