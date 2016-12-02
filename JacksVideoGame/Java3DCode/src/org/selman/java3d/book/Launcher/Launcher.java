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

import java.io.InputStreamReader;
import java.io.BufferedReader;

import java.util.List;
import java.util.ArrayList;
/**
 * Quick-and-dirty application launcher for the book examples. 
 * The Launcher displays a menu of applications, sets the working 
 * directory and Runtime.execs each example.
 * <p>
 * If you installed the examples to the "c:\dev\selman" directory
 * go to c:\dev\selman\classes and type:
 * <pre>
 * java org.selman.java3d.book.launcher.Launcher
 * </pre>
 * <p>
 * <b>
 * You need to have "java" in your path (the correct one for Java 3D!) 
 * and copy the files j3dtree.jar and vrml97.jar 
 * into your jre/lib/ext directory.
 * </b>
 * <p>
 * Please refer to the JavaDoc folder for information about each example.
 */
public class Launcher
{
   private int commandIndex = 0;
   private List applications = new ArrayList();

   public static void main( String[] args )
       throws Exception
   {
       Launcher launcher = new Launcher();
       launcher.run();
   }

   /**
    * Reads the property file for the Launcher
    * and register each example.
    */
   public Launcher()
   {
       readProperties();
   }

   protected void readProperties()
   {
       registerApplication( new Application( "org.selman.java3d.book.alphatest.AlphaTest" ) );
       registerApplication( new Application( "org.selman.java3d.book.appearancetest.AppearanceTest" ) );
       registerApplication( new Application( "org.selman.java3d.book.avatartest.AvatarTest" ) );
       registerApplication( new Application( "org.selman.java3d.book.behaviortest.BehaviorTest" ) );
       registerApplication( new Application( "org.selman.java3d.book.billboardtest.BillboardTest" ) );
       registerApplication( new Application( "org.selman.java3d.book.boundstest.BoundsTest" ) );
       registerApplication( new Application( "org.selman.java3d.book.compiletest.CompileTest" ) );
       registerApplication( new Application( "org.selman.java3d.book.cuboidtest.CuboidTest" ) );
       registerApplication( new Application( "org.selman.java3d.book.customalphatest.CustomAlphaTest" ) );
       registerApplication( new Application( "org.selman.java3d.book.hirescoordtest.HiResCoordTest" ) );
       registerApplication( new Application( "org.selman.java3d.book.immediatetest.ImmediateTest" ) );
       registerApplication( new Application( "org.selman.java3d.book.interpolatortest.InterpolatorTest" ) );
       registerApplication( new Application( "org.selman.java3d.book.keynavigatetest.KeyNavigateTest" ) );
       registerApplication( new Application( "org.selman.java3d.book.lighttest.LightTest" ) );
       registerApplication( new Application( "org.selman.java3d.book.loadertest.LoaderTest" ) );
       registerApplication( new Application( "org.selman.java3d.book.mixedtest.MixedTest" ) );
       registerApplication( new Application( "org.selman.java3d.book.mousenavigatetest.MouseNavigateTest" ) );
       registerApplication( new Application( "org.selman.java3d.book.multiview.MultiView" ) );
       registerApplication( new Application( "org.selman.java3d.book.myjava3d.MyJava3D" ) );
       registerApplication( new Application( "org.selman.java3d.book.nodestest.NodesTest" ) );
       registerApplication( new Application( "org.selman.java3d.book.pickcollisiontest.PickCollisionTest" ) );
       registerApplication( new Application( "org.selman.java3d.book.platformtest.PlatformTest" ) );
       registerApplication( new Application( "org.selman.java3d.book.pointtest.PointTest" ) );
       registerApplication( new Application( "org.selman.java3d.book.rastertest.RasterTest" ) );
       registerApplication( new Application( "org.selman.java3d.book.scenegraphtest.ScenegraphTest" ) );
       registerApplication( new Application( "org.selman.java3d.book.simpletest.SimpleTest" ) );
       registerApplication( new Application( "org.selman.java3d.book.splineinterpolatortest.SplineInterpolatorTest" ) );
       registerApplication( new Application( "org.selman.java3d.book.swingtest.SwingTest" ) );
       registerApplication( new Application( "org.selman.java3d.book.switchtest.SwitchTest" ) );
       registerApplication( new Application( "org.selman.java3d.book.texcoordtest.TexCoordTest" ) );
       registerApplication( new Application( "org.selman.java3d.book.text2dtest.Text2DTest" ) );
       registerApplication( new Application( "org.selman.java3d.book.text3dtest.Text3DTest" ) );
       registerApplication( new Application( "org.selman.java3d.book.texturetest.TextureTest" ) );
       registerApplication( new Application( "org.selman.java3d.book.texturetransformtest.TextureTransformTest" ) );
       registerApplication( new Application( "org.selman.java3d.book.triangulatortest.TriangulatorTest" ) );
       registerApplication( new Application( "org.selman.java3d.book.vrmlpickingtest.VrmlPickingTest" ) );
   }

   protected void registerApplication( Application application )
   {
       applications.add( application );
   }

   protected void printMenu()
   {
       System.out.println( "===============================================================================" );
       System.out.println( "    APPLICATION MENU" );
       System.out.println( "    - select application number and press return" );
       System.out.println( "    - 0 to quit" );
       System.out.println( "    - N for next application" );
       System.out.println( "    - P for previous application" );
       System.out.println( "===============================================================================" );

       if ( commandIndex == 0 )
       {
           System.out.println( "--> 0) QUIT" );
       }
       else
       {
           System.out.println( "    0) QUIT" );
       }

       for( int n = 0; n < applications.size(); n++ )
       {
           Application application = (Application) applications.get( n );

           if ( commandIndex-1 == n )
           {
               System.out.println( "--> " + (n + 1) + ") " + application.getName() );
           }
           else
           {
               System.out.println( "    " + (n + 1) + ") " + application.getName() );
           }
       }
   }

   public void run()
       throws Exception
   {
       // Prepare the standard input for reading lines
       BufferedReader in = 
       	new BufferedReader(new InputStreamReader(System.in));

       do
       {
           printMenu();

           String newCommand = new String();

           try
           {
               newCommand = in.readLine();
               commandIndex = Integer.parseInt( newCommand );
           }
           catch( Exception e )
           {
               if( newCommand.equalsIgnoreCase( "p" ) != false )
               {
                   commandIndex--;

                   if ( commandIndex == -1 )
                   {
                       commandIndex = applications.size();
                   }
               }
               else
               {
                   commandIndex++;

                   if ( commandIndex > applications.size() )
                   {
                       commandIndex = 1;
                   }
               }
           }

           if ( commandIndex < 0 || commandIndex > applications.size() )
           {
               throw new IllegalArgumentException( "Please enter a value between 0 and " + applications.size() );
           }

           if ( commandIndex != 0 )
           {
               try
               {
                   ((Application) applications.get( commandIndex-1 )).run( Application.RUN_NEW_VM );
               }
               catch( Exception e )
               {
                   e.printStackTrace();
               }
           }
       }
       while( commandIndex != 0 );
   }
}
