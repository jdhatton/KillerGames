
// TimeBehavior.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* Update the alien sprite every timeDelay ms by calling
   its update() method.
   This class is the Java 3D version of a Timer.
*/

import java.util.Enumeration;
import javax.media.j3d.*;


public class TimeBehavior extends Behavior
{
  private WakeupCondition timeOut;
  private AlienSprite alien;

  public TimeBehavior(int timeDelay, AlienSprite as)
  { alien = as;
    timeOut = new WakeupOnElapsedTime(timeDelay);
  }

  public void initialize()
  { wakeupOn( timeOut );
  }

  public void processStimulus( Enumeration criteria )
  { // ignore criteria
    alien.update();
    wakeupOn( timeOut );
  } // end of processStimulus()


}  // end of TimeBehavior class
