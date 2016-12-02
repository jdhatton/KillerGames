
// PredatorBoid.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
// Sirinart Sakarin, March 2003, s4210315@calvin.coe.psu.ac.th

/* A predator has a yellow body and can get hungry.
     When near prey (and hungry) it will 'eat' it. 
     When hungry, it will head towards prey.
*/

import javax.media.j3d.*;
import javax.vecmath.*;


public class PredatorBoid extends Boid
{
  private final static Color3f yellow = new Color3f(1.0f, 1.0f,0.6f);
  private final static int HUNGER_TRIGGER = 3;   // when hunger affects behaviour

  private int hungerCount;


  public PredatorBoid(Obstacles obs, PredatorBehavior beh)
  {
	super(yellow, 1.0f, obs, beh);   // yellow boid, normal max speed
	hungerCount = 0;
  }// end of PredatorBoid()
  

  public void animateBoid()
  // extend Boid's animateBoid() with eating behaviour
  {
	hungerCount++;
	if (hungerCount > HUNGER_TRIGGER)   // time to eat
      hungerCount -= ((PredatorBehavior)beh).eatClosePrey(boidPos);
	super.animateBoid();
  } // end of animateBoid()



  protected void doVelocityRules()
  /* Extend Boid's doVelocityRules() with a rule
     for heading towards prey if hungry.
  */
  { if (hungerCount > HUNGER_TRIGGER) {  // time to eat
      Vector3f v = ((PredatorBehavior)beh).findClosePrey(boidPos);
      velChanges.add(v);
	}
    super.doVelocityRules();
  }  // end of doVelocityRules()

} // end of PredatorBoid class