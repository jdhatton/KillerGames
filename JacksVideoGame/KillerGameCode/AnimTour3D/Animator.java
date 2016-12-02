
// Animator.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* Animator maintains a schedule of animations (in animSchedule)
   which it processes every timeDelay ms. The processing of an
   animation usually involves executing a movment or rotation operation on 
   the sprite (bob), and updating its pose.

   The KeyBehavior object calls varous methods in Animator in response
   to key presses. The methods add animation sequences to the 
   scheduler.

   The viewpoint is set here, based on the TourSprite's position.
   As the sprite moves, the viewpoint is also moved. 
*/

import java.util.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import java.text.DecimalFormat;


public class Animator extends Behavior
{
  // sprite animation constants
  private final static double MOVERATE = 0.3;
  private final static double ROTATE_AMT = Math.PI / 16.0;

  private final static double HEIGHT = 2.0;    // height of viewer
  private final static double ZOFFSET = 8.0;   // distance from sprite
  private final static double ZSTEP = 1.0;     // viewer's zoom step


  /* Each sprite operation is represented by a sequence of animations
     which can be added to the schedule and processed. 

     Each animation sequence must end with "stand", or seqCount 
     will not know it is over.

     The animation names in a sequence must be the same as the 
     3ds filenames for the sprite's poses.
  */
  private final static String forwards[] = {"walk1", "walk2", "stand"};
  private final static String backwards[] = {"rev1", "rev2", "stand"};
  private final static String rotClock[] = {"rotClock", "stand"};
  private final static String rotCounterClock[] = {"rotCC", "stand"};
  private final static String moveLeft[] = {"mleft", "stand"};
  private final static String moveRight[] = {"mright", "stand"};

  private final static String active[] = {"toggle", "stand"};
                // toggle has no pose, since it switches off the sprite
  private final static String punch[] = 
		{"punch1", "punch1", "punch2", "punch2", "stand"};


  /* MAX_SEQS is the maximum number of animations sequences that 
     can appear in the schedule at any one time. This
     prevents too large a delay before starting an animation.
  */
  private final static int MAX_SEQS = 4;


  private WakeupCondition timeDelay;

  private TransformGroup viewerTG;  // the viewpoint TG
  private Transform3D t3d, toMove;  // used to affect viewerTG;

  private AnimSprite3D bob;   // the sprite
  private Point3d bobPosn;    // its last position
  private boolean isActive;   // whether the sprite is active

  // ArrayList to store animation sequences that need to be processed
  private ArrayList animSchedule;
  private int seqCount;   // number of animation sequences in the schedule

  private DecimalFormat df;   // for simpler output during debugging



  public Animator(int td, AnimSprite3D b, TransformGroup vTG)
  { 
    df = new DecimalFormat("0.###");  // 3 dp

    timeDelay = new WakeupOnElapsedTime(td);

    animSchedule = new ArrayList();   // initialise schedule
    seqCount = 0;

    bob = b;
    bobPosn = bob.getCurrLoc();   // start location for sprite
    viewerTG = vTG;
    isActive = true;
    t3d = new Transform3D();
    toMove = new Transform3D();
    setViewer();
  } // end of Animator()


  private void setViewer()
  /* Position the viewpoint so it is offset by ZOFFSET from the
     sprite along the z-axis, and is at height HEIGHT. 
  */
  { bobPosn = bob.getCurrLoc();   // start location for bob
    viewerTG.getTransform( t3d );
    // args are: viewer posn, where looking, up direction
    t3d.lookAt( new Point3d(bobPosn.x, HEIGHT, bobPosn.z + ZOFFSET), 
				new Point3d(bobPosn.x, HEIGHT, bobPosn.z), 
				new Vector3d(0,1,0));
    t3d.invert();
    viewerTG.setTransform(t3d);
  }  // end of setViewer()



  public void initialize()
  { wakeupOn( timeDelay ); }


  public void processStimulus( Enumeration criteria )
  { // ignore criteria
    String anim = getNextAnim();
    if (anim != null)
      doAnimation(anim);
    wakeupOn( timeDelay );
  } // end of processStimulus()


  private void doAnimation(String anim)
  /* An animation (anim) is processed in 2 ways:
      * it is used to execute a transformation on the sprite
      * it is used to change the sprite's pose

     Also, this method checks if the user's viewpoint needs
     updating after the change to the sprite.
  */
  { // System.out.println("processing :" + anim);
    // System.out.println("  new animSchedule :" + animSchedule);

    /* Carry out a transformation on the sprite.
       Note: "stand", "punch1", "punch2" have no transformations
    */
    if ( anim.equals("walk1") || anim.equals("walk2"))   // forward
      bob.moveBy(0.0, MOVERATE/2);      // half a step
    else if ( anim.equals("rev1") || anim.equals("rev2"))  // back
      bob.moveBy(0.0, -MOVERATE/2);     // half a step
    else if (anim.equals("rotClock"))
      bob.doRotateY(-ROTATE_AMT);       // clockwise rot
    else if (anim.equals("rotCC"))
      bob.doRotateY(ROTATE_AMT);        // counter-clockwise rot
    else if (anim.equals("mleft"))      // move left
      bob.moveBy(-MOVERATE,0.0);    
    else if (anim.equals("mright"))     // move right
      bob.moveBy(MOVERATE,0.0);
    else if (anim.equals("toggle")) {
      isActive = !isActive;    // toggle activity
      bob.setActive(isActive);
    }

    // update the sprite's pose, except for "toggle"
    if (!anim.equals("toggle"))
      bob.setPose(anim); 

    viewerMove();    // update the user's viewpoint
  }  // end of doAnimation()


  // ---------------------- viewer controls ---------------------

  private void viewerMove()
  // Updates the viewpoint by the translation change of the sprite
  {
    Point3d newLoc = bob.getCurrLoc();
    // printTuple(newLoc, "newLoc");
    Vector3d trans = new Vector3d( newLoc.x - bobPosn.x,
								0, newLoc.z - bobPosn.z);
    viewerTG.getTransform( t3d );
    toMove.setTranslation(trans);  // overwrites previous translation
    t3d.mul(toMove);
    viewerTG.setTransform(t3d);

    bobPosn = newLoc;   // save for next time
  }  // end of viewerMove()


  public void shiftInViewer()
  {  shiftViewer(-ZSTEP); }   // move viewer negatively on z-axis

  public void shiftOutViewer()
  {  shiftViewer(ZSTEP); }


  private void shiftViewer(double zDist)
  // move the viewer inwards or outwards
  {
    Vector3d trans = new Vector3d(0,0,zDist);
    viewerTG.getTransform( t3d );
    toMove.setTranslation(trans);  // overwrites previous translation
    t3d.mul(toMove);
    viewerTG.setTransform(t3d);
  }  // end of viewerMove()


  // -------------- manipulate animSchedule arraylist -------------------
  /* These methods are synchronized since it is possible that 
     processStimulus() may call getNextAnim() at the same time that
     addAnims() is being called.
  */


  synchronized private void addAnims(String ims[])
  { 
    if (seqCount < MAX_SEQS) {  // not too many animation sequences
      for(int i=0; i < ims.length; i++)
        animSchedule.add(ims[i]);
      // System.out.println("added to animSchedule :" + animSchedule);
      seqCount++;
      // System.out.println("seqCount :" + seqCount);
    }
  }

  synchronized private String getNextAnim()
  { if (animSchedule.isEmpty())
      return null;
    else {
      String anim = (String) animSchedule.remove(0);
      if (anim.equals("stand"))   // end of a sequence
         seqCount--;
      return anim;
    }
  }


  // -----  sprite commands --> animation schedule additions ----------
  /* The methods here are the visible interface for controlling the sprite.
     Each call is translated into the addition of animation sequences to 
     animSchedule.
  */

  // moves
  public void moveForward()
  { addAnims(forwards); }

  public void moveBackward()
  { addAnims(backwards); }

  public void moveLeft()
  { addAnims(moveLeft); }

  public void moveRight()
  { addAnims(moveRight); }

  // rotations around y-axis
  public void rotClock()
  { addAnims(rotClock); }

  public void rotCounterClock()
  { addAnims(rotCounterClock); }


  public void punch()
  { addAnims(punch); }

  public void toggleActive()
  { addAnims(active); }


  // ----------------- debugging methods ------------------------------

  private void printTuple(Tuple3d t, String id)
  // used for debugging
  {
    System.out.println(id + " x: " + df.format(t.x) + 
				", " + id + " y: " + df.format(t.y) +
				", " + id + " z: " + df.format(t.z));
  }  // end of printTuple()


}  // end of Animator class
