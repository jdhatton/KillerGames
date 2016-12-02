
// ClientInfo.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* Details for a single client and methods for receiving and
   sending strings to the client.

   Since client input is non-blocking then the arrival of data
   does not mean that it comprises a complete message. 

   The ongoing message is maintained in inBuffer until
   a '\n' is detected and then the complete string is returned.
*/


import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.*;
// import java.util.*;


public class ClientInfo
{
  private static final int BUFSIZ = 1024;  // max size of a message

  private SocketChannel channel;
  private SelectScoreServer ss;
  private ByteBuffer inBuffer;

  private Charset charset;     // for decoding bytes --> string
  private CharsetDecoder decoder;


  public ClientInfo(SocketChannel chan, SelectScoreServer ss)
  {
    channel = chan;
    this.ss = ss;
    inBuffer = ByteBuffer.allocateDirect(BUFSIZ);
    inBuffer.clear();

    charset = Charset.forName("ISO-8859-1");
    decoder = charset.newDecoder();

    showClientDetails();
  } // end of ClientInfo()



  private void showClientDetails()
  // show client details; not used further
  {
    Socket sock = channel.socket();
    InetAddress iaddr = sock.getInetAddress();
    System.out.println("Client address: " + iaddr.getHostAddress() );
    System.out.println("Client name: " + iaddr.getHostName() );
    System.out.println("Client port: " + sock.getPort() );
  }  // end of showClientDetails()


  public void closeDown()
  {
    try {
      channel.close();
    }
    catch(IOException e)
    { System.out.println(e); }
  }



  public String readMessage()
  /* readMessage() is called when there are bytes waiting
     to be read, but this may not be a complete message.
     The message is stored in inBuffer until a '\n' is detected.
  */
  { 
    String inputMsg = null;
    try {
      int numBytesRead = channel.read(inBuffer);
      if (numBytesRead == -1) {     // channel has gone
        channel.close();
        ss.removeChannel(channel); // tell SelectScoreServer
      }
      else
        inputMsg = getMessage(inBuffer);
    }
    catch (IOException e)
    {  System.out.println("rm: " + e); 
       ss.removeChannel(channel); // tell SelectScoreServer
    }

    return inputMsg;
  }  // end of readMessage()



  private String getMessage(ByteBuffer buf)
  {
    String msg = null;
    int posn = buf.position();   // current buffer sizes
    int limit = buf.limit();
    // System.out.println("Position: " + posn + ";  limit: " + limit);

    buf.position(0);    // set range of bytes in buffer for translation
    buf.limit(posn);
    try {
      CharBuffer cb = decoder.decode(buf);
      msg = cb.toString();
    }
    catch(CharacterCodingException cce)
    { System.out.println( cce );  }

    System.out.println("Current msg: " + msg);
    buf.limit(limit);    // reset buffer to full range of bytes
    buf.position(posn);
    
    if (msg.endsWith("\n")) {    // we assume '\n' is the last char
      buf.clear();
      return msg;
    }
    return null;
  }  // end of getMessage()



  public boolean sendMessage(String msg)
  {
    String fullMsg = msg + "\r\n";

    ByteBuffer outBuffer = ByteBuffer.allocateDirect(BUFSIZ);
    outBuffer.clear();
    outBuffer.put( fullMsg.getBytes() );
    outBuffer.flip();

    boolean msgSent = false;
    try {
      // send the data, don't assume it goes all at once
      while( outBuffer.hasRemaining() )
        channel.write(outBuffer);
      msgSent = true;
    }
    catch(IOException e)
    { System.out.println(e);  
      ss.removeChannel(channel); // tell SelectScoreServer
    }
    
    return msgSent;
  }  // end of sendMessage()


}  // end of ClientInfo class

