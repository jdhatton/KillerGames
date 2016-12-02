
// LocationBeh.java
// Thana Konglikhit, October 2003, s4310170@maliwan.psu.ac.th
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* The figure can be moved and rotated via the arrow keys:
       forward, back, left, right, up, down,
       turn clockwise and anti-clockwise

   The same arrow keys are used as in Tour3D.

   The key directions are processed from the figure's viewpoint
   (e.g. left side of figure, not left side of user.)

   The user can also enter figure commands into the text field
   of the CommandsPanel, and so mix figure and limb operations.
*/

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.media.j3d.*;
import javax.vecmath.*;


public class LocationBeh extends Behavior 
{
  // movement constants
  private final static int FWD = 0;
  private final static int BACK = 1;
  private final static int LEFT = 2;
  private final static int RIGHT = 3;
  private final static int UP = 4;
  private final static int DOWN = 5;

  private final static int CLOCK = 0;   // clockwise turn
  private final static int CCLOCK = 1;  // counter clockwise

  // key constants
  private final static int forwardKey = KeyEvent.VK_DOWN;
  private final static int backKey = KeyEvent.VK_UP;
  private final static int leftKey = KeyEvent.VK_LEFT;
  private final static int rightKey = KeyEvent.VK_RIGHT;

  private Figure figure;
  private WakeupCondition keyPress;


  public LocationBeh(Figure fig)
  { figure = fig;
    keyPress = new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED);
  }


  public void initialize()
  {  wakeupOn(keyPress);  }


  public void processStimulus(Enumeration criteria)
  {
    WakeupCriterion wakeup;
    AWTEvent[] event;

    while( criteria.hasMoreElements() ) {
      wakeup = (WakeupCriterion) criteria.nextElement();
      if( wakeup instanceof WakeupOnAWTEvent ) {
        event = ((WakeupOnAWTEvent)wakeup).getAWTEvent();
        for( int i = 0; i < event.length; i++ ) {
          if( event[i].getID() == KeyEvent.KEY_PRESSED )
            processKeyEvent((KeyEvent)event[i]);
        }
      }
    }
    wakeupOn( keyPress );
  } // end of processStimulus()


  private void processKeyEvent(KeyEvent eventKey)
  {
    int keyCode = eventKey.getKeyCode();
    // System.out.println(keyCode);    
    if( eventKey.isAltDown() )
      altMove(keyCode);
    else
      standardMove(keyCode);
  } // end of processKeyEvent()



  private void standardMove(int keycode)
  // moves figure forwards, backwards, rotates left or right
  {
    if(keycode == forwardKey)
      figure.doMove(FWD);
    else if(keycode == backKey)
      figure.doMove(BACK);
    else if(keycode == leftKey)
      figure.doRotateY(CLOCK);   // clockwise
    else if(keycode == rightKey)
      figure.doRotateY(CCLOCK);    // counter-clockwise
  } // end of standardMove()


  private void altMove(int keycode)
  // moves figure up, down, left or right
  {
    if(keycode == backKey )
      figure.doMove(UP);
    else if(keycode == forwardKey)
      figure.doMove(DOWN);
    else if(keycode == leftKey)
      figure.doMove(LEFT);
    else if(keycode == rightKey)
      figure.doMove(RIGHT);
  }  // end of altMove()


} //end of LocationBeh class