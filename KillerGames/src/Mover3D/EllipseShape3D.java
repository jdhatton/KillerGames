
// EllipseShape3D.java
// Andrew Davison, July 2003, dandrew@ratree.psu.ac.th

/* An ellipse has a semi-major axis of length a,
   and semi-minor axis of length b.

   The ellipse can be represented by the equations:
     x = a cos(angle)
     y = b sin(angle)

  The radius is the semi-major axis value (a), and the
  semi-minor axis is set to be some fraction of a.
  We hardwire   b == 0.5 * radius.

*/

import javax.media.j3d.*;
import javax.vecmath.*;


public class EllipseShape3D extends LatheShape3D
{
  public EllipseShape3D(double[] xsIn, double[] ysIn,
                                Color3f darkCol, Color3f lightCol)
  { super(xsIn, ysIn, darkCol, lightCol);  }


  public EllipseShape3D(double[] xsIn, double[] ysIn, Texture tex)
  { super(xsIn, ysIn, tex);  }


  protected double zCoord(double radius, double angle)
  {  return  0.5 * radius * Math.sin(angle);  }   // b == a/2

}  // end of EllipseShape3D class