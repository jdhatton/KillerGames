
// McDonald.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* Download an audio clip inside init(), and play it repeatedly.
   when start() is called. Mercifully, stop() will stop it when
   the page is no longer active in the browser.
*/

import java.awt.*;
import javax.swing.*;
import java.applet.AudioClip;


public class McDonald extends JApplet
{
  private AudioClip mcdClip;

  public void init() 
  {  mcdClip = getAudioClip(getCodeBase(), "mcdonald.mid");  }

  public void paint(Graphics g) 
  {  g.drawString("Old McDonald", 25, 25);  }

  public void stop() 
  {  mcdClip.stop(); }    

  public void start()
  /* A looping play (and a call to play()) always starts at
     the beginning of the clip. */
  { mcdClip.loop();  }
  
} // end of McDonald.java