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

package org.selman.java3d.book.myjava3d;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;

import javax.media.j3d.*;
import javax.vecmath.*;

import com.sun.j3d.utils.applet.MainFrame;

import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.loaders.Scene;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
* Renders a 3D shape using a 3D rendering engine
* that was written from scratch using AWT for
* graphics operations.
*/
public class MyJava3D extends JFrame
{
	private static int 				m_kWidth = 400;
	private static int 				m_kHeight = 400;
   
   private RenderingEngine			renderingEngine = new AwtRenderingEngine();
   private GeometryUpdater			geometryUpdater = new RotatingGeometryUpdater();
   private RenderingSurface		renderingSurface;

	public MyJava3D( )
	{
   	// load the object file
		Scene scene = null;
		Shape3D shape = null;

		// read in the geometry information from the data file
		ObjectFile objFileloader = new ObjectFile( ObjectFile.RESIZE );

		try
		{
			scene = objFileloader.load( "hand1.obj" );
		}
		catch ( Exception e )
		{
			scene = null;
			System.err.println( e );
		}

		if( scene == null )
			System.exit( 1 );

		// retrieve the Shape3D object from the scene
		BranchGroup branchGroup = scene.getSceneGroup( );
		shape = (Shape3D) branchGroup.getChild( 0 );

       GeometryArray geometryArray = (GeometryArray) shape.getGeometry();
      
       // add the geometry to the rendering engine...       
       renderingEngine.addGeometry( geometryArray );
       
       // create a rendering surface and bind the rendering engine
       renderingSurface = new RenderingSurface( renderingEngine, geometryUpdater );
       
       // start the rendering surface and add it to the content panel
       renderingSurface.start();
       getContentPane().add( renderingSurface );
       
    	// disable automatic close support for Swing frame.
		setDefaultCloseOperation( WindowConstants.DO_NOTHING_ON_CLOSE );
			
		// adds the window listener
		addWindowListener( 
			new WindowAdapter()
			{
				// handles the system exit window message
				public void windowClosing( WindowEvent e )
				{
					System.exit( 0 );				
               }
			}
		);
	}
   
	public static void main( String[] args )
	{
		MyJava3D myJava3D = new MyJava3D();
       myJava3D.setTitle( "MyJava3D" );
       myJava3D.setSize( 300, 300 );
       myJava3D.setVisible( true );
	}
}
