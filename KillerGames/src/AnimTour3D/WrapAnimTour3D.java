
// WrapAnimTour3D.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* Usual checkboard world but with an animated
   touring 3D sprite.
*/

import javax.swing.*;
import java.awt.*;
import java.io.*;

import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.geometry.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.behaviors.vp.*;
import com.sun.j3d.utils.image.TextureLoader;

// import com.tornadolabs.j3dtree.*;    // for displaying the scene graph


public class WrapAnimTour3D extends JPanel
// Holds the 3D canvas where the loaded image is displayed
{
  private static final int PWIDTH = 512;   // size of panel
  private static final int PHEIGHT = 512; 

  private SimpleUniverse su;
  private BranchGroup sceneBG;
  private BoundingSphere bounds;   // for environment nodes

  // private Java3dTree j3dTree;   // frame to hold tree display


  public WrapAnimTour3D()
  // construct the 3D canvas
  {
    setLayout( new BorderLayout() );
    setOpaque( false );
    setPreferredSize( new Dimension(PWIDTH, PHEIGHT));

    GraphicsConfiguration config =
					SimpleUniverse.getPreferredConfiguration();
    Canvas3D canvas3D = new Canvas3D(config);
    add("Center", canvas3D);
    canvas3D.setFocusable(true);
    canvas3D.requestFocus();
    su = new SimpleUniverse(canvas3D);

    // j3dTree = new Java3dTree();   // create a display tree for the SG

    createSceneGraph();
    su.addBranchGraph( sceneBG );

	// j3dTree.updateNodes( su );    // build the tree display window
  } // end of WrapAnimTour3D()


  private void createSceneGraph() 
  // initilise the scene
  { sceneBG = new BranchGroup();
    bounds = new BoundingSphere(new Point3d(0,0,0), 100); 

    lightScene();         // add the lights
    addBackground();      // add the sky
    sceneBG.addChild( new CheckerFloor().getBG() );  // add the floor

    addTourist();         // add the user-controlled animated 3D sprite

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
  } // end of addBackground()



  // --------------------- tourist ---------------------------


  private void addTourist()
  /*  The tourist (bob) is represented by multiple models in
      its AnimSprite3D object. 

      Key input is caught by KeyBehavior and passed to the
      Animator object for processing.

      Animator add animation sequences to an animation schedule.
      An animation is carried out on bob every 20 ms, which usually
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
 

} // end of WrapAnimTour3D class