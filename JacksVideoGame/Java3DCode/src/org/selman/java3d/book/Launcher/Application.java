/**********************************************************
  Copyright (C) 2001 	Daniel Selman

  First distributed with the book "Java 3D Programming"
  by Daniel Selman and published by Manning Publications.
  http://manning.com/selman

  This program is free software; you can redistribute it and/or
  modify it under the terms of the GNU General Public License
  as published by the Free Software Foundation, version 2.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  The license can be found on the WWW at:
  http://www.fsf.org/copyleft/gpl.html

  Or by writing to:
  Free Software Foundation, Inc.,
  59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.

  Authors can be contacted at:
  Daniel Selman: daniel@selman.org

  If you make changes you think others would like, please 
  contact one of the authors or someone at the 
  www.j3d.org web site.
**************************************************************/

package org.selman.java3d.book.launcher;

import java.util.*;
import java.io.*;


/**
 * Quick-and-dirty encapsulation of an example
 * application. Also handles the mechanics of
 * Runtime.exec'ing the example to run out-
 * of-process.
 */
public class Application
{
   public static final int RUN_NEW_VM = 0;
   public static final int RUN_WITHIN_VM = 1;

   private String className;

   private static String javacPath;
   private static String installDir;

   static
   {
       // assumes the correct instance of "java" is in the path
       // add the current directory to the end of the classpath
       // should only be done once
       javacPath = "java -classpath ." + File.pathSeparator + System.getProperty( "user.dir" );
       installDir = new File( System.getProperty( "user.dir" ) ).getParent();
   }

   public Application( String className )
   {
       this.className = className;
   }

   public void run( int mode )
       throws Exception
   {
       if ( mode == RUN_NEW_VM )
       {
           File workingDirFile = new File( installDir + File.separator + "src", createPathFromClassName( className ) );
           String exec = createExecCommand( javacPath, className );

           new NativeExecute( exec, workingDirFile );
       }
       else
       {
           //
           // untested - working dir will not be correct.
           //
           // create an instance of the class
           Object obj = Class.forName( className ).newInstance();

           // invoke the main method on the instance
           obj.getClass().getMethod( "main", new Class[] {String[].class} ).
            invoke( obj, new String[] {} );
       }
   }

   public String getName()
   {
       // throw away the package name part
       int sepIndex = className.lastIndexOf( '.' );

       return className.substring( sepIndex+1, className.length() );
   }

   protected String createExecCommand( String javacPath, String className )
   {
       String exec = javacPath;
       exec += " ";

       // convert all the '.' in the className to File.separator
       exec += className;

       return exec;
   }

   protected String createPathFromClassName( String className )
   {
       // convert all the '.' in the className to File.separator
       String classFile = className.replace( '.', File.separatorChar );

       // throw away the class name part
       int sepIndex = classFile.lastIndexOf( File.separatorChar );

       return classFile.substring( 0, sepIndex+1 );
   }

   public static void setJavacPath( String newJavacPath )
   {
       javacPath = newJavacPath;
   }

   // from: http://www.javaworld.com/javaworld/jw-12-2000/jw-1229-traps.html
   class StreamGobbler extends Thread
   {
       InputStream is;
       String type;

       StreamGobbler( InputStream is, String type )
       {
           this.is = is;
           this.type = type;
       }

       public void run( )
       {
           try
           {
               InputStreamReader isr = new InputStreamReader( is );
               BufferedReader br = new BufferedReader( isr );
               String line=null;
               while ( (line = br.readLine( )) != null)
                   System.out.println( type + ">" + line );
           } catch ( IOException ioe )
           {
               ioe.printStackTrace( );
           }
       }
   }

   // from: http://www.javaworld.com/javaworld/jw-12-2000/jw-1229-traps.html
   public class NativeExecute
   {
       public NativeExecute( String cmd, File workingDirFile )
       {
           try
           {
               System.out.println( "=======================" );
               System.out.println( "EXEC: " + cmd );
               System.out.println( "CURRENT WORKING DIR: " + System.getProperty( "user.dir" ) );
               System.out.println( "NEW WORKING DIR: " + workingDirFile.getPath() );
               System.out.println( "=======================" );

               Runtime rt = Runtime.getRuntime( );

               System.setProperty( "user.dir", workingDirFile.getPath() );
               Process proc = rt.exec( cmd, null, workingDirFile );

               // any error message?
               StreamGobbler errorGobbler = new
                   StreamGobbler( proc.getErrorStream( ), "ERROR" );

               // any output?
               StreamGobbler outputGobbler = new
                   StreamGobbler( proc.getInputStream( ), "OUTPUT" );

               // kick them off
               errorGobbler.start( );
               outputGobbler.start( );

               // any error???
               int exitVal = proc.waitFor( );
               System.out.println( "Exit value: " + exitVal );
           } 
           catch ( Throwable t )
           {
               t.printStackTrace( );
           }
       }
   }
}
