
// AnimTourFS.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* The usual checkboard world and a user-controlled 3D sprite 
   (the tourist) who has an animated walking movement.

   The tourist cam move about on the XZ plane but can not 
   move off the board.

   No scenery and obstacles, but it's easy to borrow code from
   /Tour3D

   This file is a combination of AnimTour3D.java and WrapAnimTour3D,
   modified to use Full-screen exclusive mode. However, we keep
   using passive rendering by sticking with Java 3D's retained mode.

   We also illustrate how to change the bit depth with DisplayMode.

   All the coding changes are in the constructor.
   An AWT Frame is used as the top-level window. 

   This program must be called with -Dsun.java2d.noddraw=true 
    - see AnimTourFS.bat

   More details on Full-screen exclusive mode are in the Java tutorial:
      <JAVA_HOME>\tutorial\extra\fullscreen\index.html
*/

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.geometry.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.behaviors.vp.*;
import com.sun.j3d.utils.image.TextureLoader;



public class AnimTourFS
// A Frame the 3D canvas where the loaded image is displayed
{
  private GraphicsDevice gd;
  private DisplayMode origDM = null;

  private SimpleUniverse su;
  private BranchGroup sceneBG;
  private BoundingSphere bounds;   // for environment nodes

  private Frame win;   // required at quit time



  public AnimTourFS()
  // construct the 3D canvas
  {
    GraphicsConfiguration config =
          SimpleUniverse.getPreferredConfiguration();

    win = new Frame("AnimTourFS", config);   // use SU's preference
    win.setUndecorated(true) ;
	       // no menu bar, borders, etc. or Swing components
    win.setResizable(false);    // fixed size display


    GraphicsEnvironment ge =
			GraphicsEnvironment.getLocalGraphicsEnvironment();
    gd = ge.getDefaultScreenDevice();
    if (! gd.isFullScreenSupported()) {
      System.out.println("Full screen exclusive mode not supported.") ;
      System.out.println("Device = " + gd) ;
      System.exit(0) ;
    }

    Canvas3D canvas3D = new Canvas3D(config);    // setup the canvas3D
    win.add(canvas3D); 
    canvas3D.setFocusable(true);
    canvas3D.requestFocus();

	canvas3D.addKeyListener( new KeyAdapter() {
	// listen for esc, q, end, ctrl-c on the canvas to
	// allow a convenient exit from the full screen configuration
       public void keyPressed(KeyEvent e)
       { int keyCode = e.getKeyCode();
         if ((keyCode == KeyEvent.VK_ESCAPE) || (keyCode == KeyEvent.VK_Q) ||
             (keyCode == KeyEvent.VK_END) ||
             ((keyCode == KeyEvent.VK_C) && e.isControlDown()) ) {
           if (origDM != null)  // original was saved
             gd.setDisplayMode(origDM);
           gd.setFullScreenWindow(null);  // exit full-screen mode
           win.dispose();
           System.exit(0);  // exit() alone isn't sufficient most of the time
         }
       }
     });

    gd.setFullScreenWindow(win); // set full-screen exclusive mode
    if (gd.getFullScreenWindow() == null)
      System.out.println("Did not get fullscreen exclusive mode");
    else
      System.out.println("Got fullscreen exclusive mode") ;

    // Use DisplayMode to reduce the bit depth from 32 to 16.

    if (gd.isDisplayChangeSupported()) {
      origDM = gd.getDisplayMode();    // mine is 1024*768; 32 bit depth
      gd.setDisplayMode(
           new DisplayMode( origDM.getWidth(), origDM.getHeight(),
                    origDM.getBitDepth()/2,     // change to 16-bit by using /2
                    origDM.getRefreshRate()  ));
    }

    su = new SimpleUniverse(canvas3D);

    createSceneGraph();
    su.addBranchGraph( sceneBG );
  } // end of AnimTourFS()


  private void createSceneGraph() 
  // initilise the scene
  { sceneBG = new BranchGroup();
    bounds = new BoundingSphere(new Point3d(0,0,0), 100); 

    lightScene();         // add the lights
    addBackground();      // add the sky
    sceneBG.addChild( new CheckerFloor().getBG() );  // add the floor

    addTourist();         // add the user-controlled animated 3D sprite

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
  } // end of addBackground()



  // --------------------- tourist ---------------------------


  private void addTourist()
  /*  The tourist (bob) is represented by multiple models in
      its AnimSprite3D object. 

      Key input is caught by KeyBehavior and passed to the
      Animator object for processing.

      Animator add animation sequences to an animation schedule.
      An animation is carried out on bob every 75 ms, which usually
      consists of a change in position, and a change in appearance.
  */
  { // sprite
    AnimSprite3D bob = new AnimSprite3D();
    bob.setPosition(2.0, 1.0);
    sceneBG.addChild( bob.getTG() );

    // viewpoint TG
    ViewingPlatform vp = su.getViewingPlatform();
    TransformGroup viewerTG = vp.getViewPlatformTransform(); 

    // sprite's animator
    Animator animBeh = new Animator(20, bob, viewerTG); 
    animBeh.setSchedulingBounds( bounds );
    sceneBG.addChild( animBeh );

    // sprite's input keys
    KeyBehavior kb = new KeyBehavior(animBeh); 
    kb.setSchedulingBounds( bounds );
    sceneBG.addChild( kb );
  } // end of addTourist()


  // -------------------------------------------------

  public static void main(String[] args)
  { new AnimTourFS(); }


} // end of AnimTourFS class



