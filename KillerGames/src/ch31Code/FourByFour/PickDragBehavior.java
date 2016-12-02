
// PickDragBehavior.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* @(#)PickDragBehavior.java 1.8 01/01/11 07:32:11
 * Copyright (c) 1996-2001 Sun Microsystems, Inc. All Rights Reserved.

   Respond to mouse pick and drag events on the 3D board

   If a position was selected on the board, pass that position
   to the Board object for evaluation by calling tryPosn().

   If the mouse was dragged, then rotate the board.

   Mouse releases are also caught, so that dragging doesn't
   inadvertantly use a mouse position from an earlier drag
   that has finished.
*/

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.media.j3d.*;
import javax.vecmath.*;



public class PickDragBehavior extends Behavior 
{
   private final static Vector3d IN_VEC = new Vector3d(0.f, 0.f, -1.f);   
		// direction for picking -- into the scene

   private final static double XFACTOR = 0.02;   // for rotations
   private final static double YFACTOR = 0.02;


   private WakeupCriterion[] mouseEvents;
   private WakeupOr mouseCriterion;

   private int xPrev, yPrev;         // coords used while dragging
   private boolean isStartDrag;      // to signal the start of dragging
   private Transform3D modelTrans;   // stores the total rotation of the board
   private Transform3D transformX, transformY;   // for repeated calcs.
   private TransformGroup boardTG;   // used to move the board
   private BranchGroup bg;           // used for picking

   private Canvas3D canvas3D;
   private Board board;

   private Point3d mousePos;
   private Transform3D imWorldT3d;   // for image plate-->world transform
   private PickRay pickRay = new PickRay();
   private SceneGraphPath nodePath;


   public PickDragBehavior(Canvas3D c3D, Board bd, 
								BranchGroup bg, TransformGroup boardTG) 
   { canvas3D = c3D;
     board = bd;
     this.bg = bg; this.boardTG = boardTG;

     modelTrans = new Transform3D();
     transformX = new Transform3D();
     transformY = new Transform3D();

     mousePos = new Point3d();
     imWorldT3d = new Transform3D();

     xPrev = 0; yPrev = 0;
     isStartDrag = true;
   } // end of PickDragBehavior()


   public void initialize() 
   {
     mouseEvents = new WakeupCriterion[3];
     mouseEvents[0] = new WakeupOnAWTEvent(MouseEvent.MOUSE_DRAGGED);
     mouseEvents[1] = new WakeupOnAWTEvent(MouseEvent.MOUSE_PRESSED);
     mouseEvents[2] = new WakeupOnAWTEvent(MouseEvent.MOUSE_RELEASED);
     mouseCriterion = new WakeupOr(mouseEvents);
     wakeupOn (mouseCriterion);
   }  // end of initialize()


   public void processStimulus(Enumeration criteria) 
   {
     WakeupCriterion wakeup;
     AWTEvent[] event;
     int id;
     int xPos, yPos;
     while (criteria.hasMoreElements()) {
         wakeup = (WakeupCriterion) criteria.nextElement();
         if (wakeup instanceof WakeupOnAWTEvent) {
            event = ((WakeupOnAWTEvent)wakeup).getAWTEvent();
            for (int i=0; i<event.length; i++) { 
               xPos = ((MouseEvent)event[i]).getX();
               yPos = ((MouseEvent)event[i]).getY();
               id = event[i].getID();
               if (id == MouseEvent.MOUSE_DRAGGED)
                 processDrag(xPos, yPos);
               else if (id == MouseEvent.MOUSE_PRESSED)
                 processPress(xPos, yPos);
               else if (id == MouseEvent.MOUSE_RELEASED)
                 isStartDrag = true;   // may be end of a drag
             }
         }
      }
      wakeupOn (mouseCriterion);
   } // end of processStimulus()


  private void processDrag(int xPos, int yPos)
  /* Convert the distance dragged into a X- and Y- rotation.
     Include these in the model translation and apply to
     the top-level transformGroup boardTG.  */
  {
     if (isStartDrag)
       isStartDrag = false;
     else {  // not the start of a drag, so can calculate offset
       int dx = xPos - xPrev;                   // get distances dragged
       int dy = yPos - yPrev;
       transformX.rotX( dy * YFACTOR );         // convert to rotations
       transformY.rotY( dx * XFACTOR );
       modelTrans.mul(transformX, modelTrans);  // include with existing rotations
       modelTrans.mul(transformY, modelTrans);
       boardTG.setTransform(modelTrans);   // apply to transform group
     }

     xPrev = xPos;     // save loc so can work out drag next time
     yPrev = yPos;
  }  // end of processDrag()



  private void processPress(int xPos, int yPos)
  /* Send a pick ray into the world starting from the mouse 
     press position. Get the closest intersecting node, and
     accept the first Shape3D as the selected board position.
  */
  { canvas3D.getPixelLocationInImagePlate(xPos, yPos, mousePos);
				// get the mouse position on the image plate
    canvas3D.getImagePlateToVworld(imWorldT3d);
		        // get image plate --> world transform
    imWorldT3d.transform(mousePos);   // convert mousePos to world coords

    pickRay.set(mousePos, IN_VEC);    
                // ray starts at mouse pos, and goes straight in   

    nodePath = bg.pickClosest(pickRay); // get 1st node along pickray (and its path)
    // System.out.println("nodePath: " + nodePath);
    if (nodePath != null)
      selectedPosn(nodePath);
  }  // end of processPress()



  private void selectedPosn(SceneGraphPath np)
  /* Find selected board position, if one exists, and pass it
     to the board for processing. */
  {
    Node node = np.getObject();       // get terminal node of path
    if (node instanceof Shape3D) {    // check for shape3D
      Integer posID = (Integer) node.getUserData();   // get posn index
      if (posID != null)
        board.tryPosn( posID.intValue() );
    }
  } // end of selectedPosn()


} // end of PickDragBehavior class
