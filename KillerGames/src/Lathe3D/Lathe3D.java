
// Lathe3D.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* A simple basic world consisting of a checkboard floor, 
   with a red center square, and labelled XZ axes.

   Lathe shapes are placed on screen.
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class Lathe3D extends JFrame
{
  public Lathe3D() 
  {
    super("Lathe3D");
    Container c = getContentPane();
    c.setLayout( new BorderLayout() );
    WrapLathe3D w3d = new WrapLathe3D();     // panel holding the 3D canvas
    c.add(w3d, BorderLayout.CENTER);

    setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
    pack();
    setResizable(false);    // fixed size display
    setVisible(true);
  } // end of Lathe3D()


// -----------------------------------------

  public static void main(String[] args)
  { new Lathe3D(); }

} // end of Lathe3D class
