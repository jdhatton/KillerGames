
// WrapFourByFour.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* Consists of 16 poles holding 4 spheres each. The player
   clicks on a sphere to make a turn, and the sphere turns
   into a large red sphere or blue box depending on the
   player.

   The game is shown using parallel projection so the poles 
   at the 'back' do not appear smaller than those at the front --
   there is no perspective effect.

   The game uses its own mouse behaviour, defined in 
   PickDragBehaviour, to select positions on the board, and to
   rotate the board.
*/


import javax.swing.*;
import java.awt.*;

import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.geometry.*;
import javax.media.j3d.*;
import javax.vecmath.*;



public class WrapFourByFour extends JPanel
// Holds the 3D canvas where the game appears
{
  private static final int PWIDTH = 400;   // size of panel
  private static final int PHEIGHT = 400; 

  private static final int BOUNDSIZE = 100;  // larger than world

  private SimpleUniverse su;
  private BranchGroup sceneBG;
  private BoundingSphere bounds;   // for environment nodes


  public WrapFourByFour(FourByFour fbf)
  {
    setLayout( new BorderLayout() );
    setOpaque( false );
    setPreferredSize( new Dimension(PWIDTH, PHEIGHT));

    GraphicsConfiguration config =
					SimpleUniverse.getPreferredConfiguration();
    Canvas3D canvas3D = new Canvas3D(config);
    add("Center", canvas3D);
    canvas3D.setFocusable(true);     // give focus to the canvas 
    canvas3D.requestFocus();

    su = new SimpleUniverse(canvas3D);

    createSceneGraph(canvas3D, fbf);
    initUserPosition();        // set user's viewpoint
    
    su.addBranchGraph( sceneBG );
  } // end of WrapFourByFour()



  private void createSceneGraph(Canvas3D canvas3D, FourByFour fbf)
  { sceneBG = new BranchGroup();
    bounds = new BoundingSphere(new Point3d(0,0,0), BOUNDSIZE);   

    // Create the transform group which moves the game
    TransformGroup gameTG = new TransformGroup();
    gameTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
    gameTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    sceneBG.addChild(gameTG);

    lightScene();         // add the lights
    addBackground();      // add the background

    gameTG.addChild( makePoles() );       // add poles

    // posns holds the spheres/boxes which mark a player's turn
    // initially posns displays a series of small white spheres.
    Positions posns = new Positions();

    // board tracks the players moves on the game board
    Board board = new Board(posns, fbf);

    gameTG.addChild( posns.getChild() );  // add position markers

    mouseControls(canvas3D, board, gameTG);

    sceneBG.compile();   // fix the scene
  } // end of createSceneGraph()



  private void lightScene()
  // One ambient light, one directional light
  {
    Color3f white = new Color3f(1.0f, 1.0f, 1.0f);

    // create an ambient light
    AmbientLight ambLight = new AmbientLight(white);
    ambLight.setInfluencingBounds(bounds);
    sceneBG.addChild(ambLight);

    // create a directional light
    Vector3f dir = new Vector3f(-1.0f, -1.0f, -1.0f);
    DirectionalLight dirLight = new DirectionalLight(white, dir);
    dirLight.setInfluencingBounds(bounds);
    sceneBG.addChild(dirLight);
  }  // end of lightScene()


  private void addBackground()
  { 
    Background back = new Background();
    back.setApplicationBounds( bounds );
    back.setColor(0.05f, 0.05f, 0.2f);  // dark blue
    sceneBG.addChild( back );
  }  // end of addBackground()


  private void mouseControls(Canvas3D c, Board board,
								TransformGroup gameTG)
  // Create the mouse pick and drag behavior
  {
    PickDragBehavior mouseBeh =  
        new PickDragBehavior(c, board, sceneBG, gameTG);
    mouseBeh.setSchedulingBounds(bounds);
    sceneBG.addChild(mouseBeh);
  }  // end of mouseControls()



  private void initUserPosition()
  // Set the user's initial viewpoint. 
  { 
    View view = su.getViewer().getView();
    view.setProjectionPolicy(View.PARALLEL_PROJECTION);  // Use parallel projection

    // scale up and move back
    TransformGroup steerTG = 
         su.getViewingPlatform().getViewPlatformTransform();
    Transform3D t3d = new Transform3D();
    t3d.set(65.0f, new Vector3f(0.0f, 0.0f, 400.0f));
    steerTG.setTransform(t3d);
  }  // end of initUserPosition()



  // ------------------------ game poles -------------------


  private BranchGroup makePoles() 
  /* Create 16 poles (cylinders) which will each appear to 
     support 4 position markers (spheres). */
  {
     Color3f grey = new Color3f(0.25f, 0.25f, 0.25f);
     Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
     Color3f diffuseWhite = new Color3f(0.7f, 0.7f, 0.7f);
     Color3f specularWhite = new Color3f(0.9f, 0.9f, 0.9f);

     // Create the pole appearance
     Material poleMaterial =
        new Material(grey, black, diffuseWhite, specularWhite, 110.f);  // and shiny
     poleMaterial.setLightingEnable(true);
     Appearance poleApp = new Appearance();
     poleApp.setMaterial(poleMaterial);
 
     BranchGroup bg = new BranchGroup();
     float x = -30.0f;
     float z = -30.0f;

     for(int i=0; i<4; i++) {
       for(int j=0; j<4; j++) {
         Transform3D t3d = new Transform3D();
         t3d.set( new Vector3f(x, 0.0f, z) );
         TransformGroup tg = new TransformGroup(t3d);
         Cylinder cyl = new Cylinder(1.0f, 60.0f, poleApp);
         cyl.setPickable(false);  // user cannot select the poles
         tg.addChild( cyl );
         bg.addChild(tg);
         x += 20.0f;
       }
       x = -30.0f;
       z += 20.0f;
     }
     return bg;
  }  // end of makePoles()


} // end of WrapFourByFour class