// TimeServlet.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* Return a text page to the client containing the 
   current date and time.
*/


import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.Date;
import java.text.SimpleDateFormat;


public class TimeServlet extends HttpServlet 
{
   public void doGet( HttpServletRequest request,
                      HttpServletResponse response )
      throws ServletException, IOException
   {
      // response.setContentType( "text/txt" );  // content type

      SimpleDateFormat formatter = 
			new SimpleDateFormat("d M yyyy HH:mm:ss");
      Date today = new Date();
      String todayStr = formatter.format(today);
      System.out.println("Today is: " + todayStr);

      PrintWriter output = response.getWriter();
      output.println( todayStr );  // send date & time
      output.close();
   }

} // end of TimeServlet class

