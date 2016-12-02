
// OverlayCanvas.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th
/*
   The 3D Canvas includes a status line,
   displayed in red, at the top left corner.

   Current status information is obtained from
   the NetFourByFour object each time postSwap() is called.
*/

import java.awt.*;
import java.awt.geom.*;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;


public class OverlayCanvas extends Canvas3D
{
  private final static int XPOS = 5;
  private final static int YPOS = 15;
  private final static Font MSGFONT = new Font( "SansSerif", Font.BOLD, 12);

  private NetFourByFour fbf;
  private String status;


  public OverlayCanvas(GraphicsConfiguration config, NetFourByFour fbf)
  { super(config);
    this.fbf = fbf;
  }


  public void postSwap()
  /* Called by the rendering loop after completing 
     all rendering to the canvas.  */
  {
    Graphics2D g = (Graphics2D) getGraphics();
    g.setColor(Color.red);
    g.setFont( MSGFONT );

    if ((status = fbf.getStatus()) != null)  // it has a value
      g.drawString(status, XPOS, YPOS);

    // this call is made to compensate for the javaw repaint bug, ...
    Toolkit.getDefaultToolkit().sync();
  }  // end of postSwap()


  public void repaint()
  // Overriding repaint() makes the worst flickering disappear
  { Graphics2D g = (Graphics2D) getGraphics();
    paint(g);
  }

  public void paint(Graphics g)
  // paint() is overridden to compensate for the javaw repaint bug
  { super.paint(g);
    Toolkit.getDefaultToolkit().sync();
  }

} // end of OverlayCanvas class

