
// ColouredTile.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

// ColouredTile creates a coloured quad array of 4 vertices.

import javax.media.j3d.*;
import javax.vecmath.*;


public class ColouredTile extends Shape3D 
{
  private static final int NUM_VERTS = 4;
  private QuadArray plane;


  public ColouredTile(Point3f p1, Point3f p2, Point3f p3, Point3f p4,
							Color3f col) 
  {
    plane = new QuadArray(NUM_VERTS, 
			GeometryArray.COORDINATES | GeometryArray.COLOR_3 );
    createGeometry(p1, p2, p3, p4, col);
    createAppearance();
  }    


  private void createGeometry(Point3f p1, Point3f p2, Point3f p3, 
								Point3f p4, Color3f col)
  { // counter-clockwise point specification
    plane.setCoordinate(0, p1);
    plane.setCoordinate(1, p2);
    plane.setCoordinate(2, p3);
    plane.setCoordinate(3, p4);

    Color3f cols[] = new Color3f[NUM_VERTS];
    for (int i=0; i < NUM_VERTS; i++)
       cols[i] = col;

    plane.setColors(0, cols);

    setGeometry(plane);
  }  // end of createGeometry()


  private void createAppearance()
  {
    Appearance app = new Appearance();

    PolygonAttributes pa = new PolygonAttributes();
    pa.setCullFace(PolygonAttributes.CULL_NONE);   
      // so can see the ColouredTile from both sides
    app.setPolygonAttributes(pa);

    setAppearance(app);
  }  // end of createAppearance()


} // end of ColouredTile class
