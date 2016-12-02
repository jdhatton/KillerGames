
// PreyBehavior.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
// Sirinart Sakarin, March 2003, s4210315@calvin.coe.psu.ac.th

/* PreyBehavior maintains its own BoidsList of Boids, _and_ a
   reference to the predator BoidsList. The list is obtained 
   via a reference to the PredatorBehavior object.

   PreyBehavior initialises its BoidsList of Boids and the 
   BranchGroup of Boid BranchGroup nodes.

   PreyBehaviour maintains one extra velocity rule: seePredators()
    - if a predator is near then flee
   This method is called by each PreyBoid object.

*/

import java.util.*;
import javax.media.j3d.*;
import javax.vecmath.*;

  
public class PreyBehavior extends FlockBehavior
{
  // scaling factor for the velocity calculated by seePredators() rule
  private final static float FLEE_WEIGHT = 0.2f;
  
  private BoidsList predsList;
  private PredatorBehavior predBeh;

  // used for repeated calculations
  private Vector3f avoidPred = new Vector3f();
  private Vector3f distFrom = new Vector3f();


  public PreyBehavior(int numBoids, Obstacles obs)
  {
    super(numBoids);
    System.out.println("Num. Prey: " + numBoids);
    createBoids(numBoids, obs);
  }


  private void createBoids(int numBoids, Obstacles obs)
  /* Initialise the Boidslist of PreyBoid objects and add
     the TransformGroups for these boids to boidsBG.
     Boid is a subclass of TransformGroup so can be added to boidsBG
     directly.
  */
  { // preyBoids can be detached from the scene
	boidsBG.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE); 
	boidsBG.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);

    PreyBoid pb; 
	for(int i=0; i < numBoids; i++){
      pb = new PreyBoid(obs, this); 
	  boidsBG.addChild(pb);   // add to BranchGroup
	  boidsList.add(pb);      // add to BoidsList
    }
    boidsBG.addChild(this);   // store the prey behaviour with its BG
  } // end of createBoids()


  public void setPredBeh(PredatorBehavior pb)
  // store the predator's behavior
  { predBeh = pb; }
  


  public Vector3f seePredators(Vector3f boidPos)
  /* If the boid is close to a predator, then it moves in the opposite
     direction by a scaled amount.
     This is an extra velocity rule, used by each PreyBoid by
     calling doVelocityRules()
  */
  { predsList = predBeh.getBoidsList();  // refer to pred list
    avoidPred.set(0,0,0);  // reset
    Vector3f predPos;
    PredatorBoid b;

    int i = 0;
	while((b = (PredatorBoid)predsList.getBoid(i)) != null) {
      distFrom.set(boidPos);
      predPos = b.getBoidPos();
      distFrom.sub(predPos);
	  if(distFrom.length() < PROXIMITY) {   // is the pred. boid close?
        avoidPred.set(distFrom);
        avoidPred.scale(FLEE_WEIGHT);
        break;
      }
      i++;
	}
	return avoidPred;
  } // end of seePredators()



  public void eatBoid(int i)
  /* PreyBoid 'i' is to be eaten. This causes it to be
     detached from the scene and removed from its BoidsList.
     Called by eatFood() in PredatorBehavior. */
  { 
     ((PreyBoid)boidsList.getBoid(i)).boidDetach(); 
	 boidsList.removeBoid(i);
  }


} // end of PreyBehavior class