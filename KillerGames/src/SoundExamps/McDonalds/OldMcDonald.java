
// OldMcDonald.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* Download and play an audio clip using Applet's 
   play()
*/

import java.applet.Applet;
import java.awt.*;


public class OldMcDonald extends Applet
{
  public void init() 
  {  play( getCodeBase(), "McDonald.mid");  }

  public void paint(Graphics g) 
  {  g.drawString("Older McDonald", 25, 25);  }
  
} // end of OldMcDonald.java