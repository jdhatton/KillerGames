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

package org.selman.java3d.book.vrmlpickingtest;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.Hashtable;

import javax.media.j3d.*;
import javax.vecmath.*;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseZoom;
import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.picking.*;
import com.sun.j3d.loaders.vrml97.VrmlLoader;
import com.sun.j3d.loaders.Scene;

import org.selman.java3d.book.common.*;

/**
*	This example loads a VRML file, automatically computes the view point to
*	view the objects in the file, and then mouse picks. For each pick all the
*	selected components of the scene are reported (by their VRML name).
*	The VRML scene can be rotates, scaled and translated using the mouse.
*/
public class VrmlPickingTest extends Java3dApplet implements MouseListener 
{
	PickCanvas 					pickCanvas = null;

	public VrmlPickingTest( )
	{
	}

	public VrmlPickingTest( String[] args )
	{
		saveCommandLineArguments( args );
		initJava3d( );
	}

	public void start( )
	{
		if( pickCanvas == null )
			initJava3d( );
	}

	protected void addCanvas3D( Canvas3D c3d )
	{
		setLayout( new BorderLayout( ) );
		add( c3d, BorderLayout.CENTER );
		doLayout( );

		if ( m_SceneBranchGroup != null )
		{
			c3d.addMouseListener( this );

			pickCanvas = new PickCanvas( c3d, m_SceneBranchGroup );
			pickCanvas.setMode( PickTool.GEOMETRY_INTERSECT_INFO );
			pickCanvas.setTolerance( 4.0f );
		}

		c3d.setCursor( new Cursor( Cursor.HAND_CURSOR ) );
	}

	public TransformGroup[] getViewTransformGroupArray( )
	{
		TransformGroup[] tgArray = new TransformGroup[1];
		tgArray[0] = new TransformGroup( );

		Transform3D viewTrans = new Transform3D( );
		Transform3D eyeTrans = new Transform3D( );

		BoundingSphere sceneBounds = (BoundingSphere) m_SceneBranchGroup.getBounds( );

		// point the view at the center of the object
		Point3d center = new Point3d( );
		sceneBounds.getCenter( center );
		double radius = sceneBounds.getRadius( );
		Vector3d temp = new Vector3d( center );
		viewTrans.set( temp );

		// pull the eye back far enough to see the whole object
		double eyeDist = 1.4 * radius / Math.tan( Math.toRadians( 40 ) / 2.0 );
		temp.x = 0.0;
		temp.y = 0.0;
		temp.z = eyeDist;
		eyeTrans.set( temp );
		viewTrans.mul( eyeTrans );

		// set the view transform
		tgArray[0].setTransform( viewTrans );

		return tgArray;
	}	

	protected BranchGroup createSceneBranchGroup( )
	{
		BranchGroup objRoot = super.createSceneBranchGroup( );

		Bounds lightBounds = getApplicationBounds( );

		AmbientLight ambLight = new AmbientLight( true, new Color3f( 1.0f, 1.0f, 1.0f ) );
		ambLight.setInfluencingBounds( lightBounds );
		objRoot.addChild( ambLight );

		DirectionalLight headLight = new DirectionalLight( );
		headLight.setInfluencingBounds( lightBounds );
		objRoot.addChild( headLight );

		TransformGroup mouseGroup = createMouseBehaviorsGroup( );

		String vrmlFile = null;
		
		try
		{
			URL codebase = getWorkingDirectory();
			vrmlFile = codebase.toExternalForm( ) + "/VRML/BoxConeSphere.wrl";
			}
		catch( Exception e )
		{
			e.printStackTrace();
			}
				
		if ( m_szCommandLineArray != null )
		{
			switch ( m_szCommandLineArray.length )
			{
				case 0:
				break;

			case 1:
				vrmlFile = m_szCommandLineArray[0];
				break;

			default:
				System.err.println( "Usage: VrmlPickingTest [pathname|URL]" );
				System.exit( -1 );
				}		
		}
		
		BranchGroup sceneRoot = loadVrmlFile( vrmlFile );

		if ( sceneRoot != null )
			mouseGroup.addChild( sceneRoot );

		objRoot.addChild( mouseGroup );		

		return objRoot;
	}

	private TransformGroup createMouseBehaviorsGroup( )
	{
		TransformGroup examineGroup = new TransformGroup( );		
		examineGroup.setCapability( TransformGroup.ALLOW_TRANSFORM_READ );
		examineGroup.setCapability( TransformGroup.ALLOW_TRANSFORM_WRITE );

		Bounds behaviorBounds = getApplicationBounds( );

		MouseRotate mr = new MouseRotate( examineGroup );
		mr.setSchedulingBounds( behaviorBounds );
		examineGroup.addChild( mr );

		MouseTranslate mt = new MouseTranslate( examineGroup );
		mt.setSchedulingBounds( behaviorBounds );
		examineGroup.addChild( mt );

		MouseZoom mz = new MouseZoom( examineGroup );
		mz.setSchedulingBounds( behaviorBounds );
		examineGroup.addChild( mz );

		return examineGroup;
	}


	private BranchGroup loadVrmlFile( String location )
	{
		BranchGroup sceneGroup = null;
		Scene scene = null;

		VrmlLoader loader = new VrmlLoader( );

		try
		{
			URL loadUrl = new URL( location );
			try
			{
				// load the scene
				scene = loader.load( new URL( location ) );
			} 
			catch ( Exception e )
			{
				System.out.println( "Exception loading URL:" + e );
				e.printStackTrace( );
			}
		} 
		catch ( MalformedURLException badUrl )
		{
			// location may be a path name
			try 
			{
				// load the scene
				scene = loader.load( location );
			} 
			catch ( Exception e )
			{
				System.out.println( "Exception loading file from path:" + e );
				e.printStackTrace( );
			}
		}

		if (scene != null)
		{
			// get the scene group
			sceneGroup = scene.getSceneGroup( );

			sceneGroup.setCapability( BranchGroup.ALLOW_BOUNDS_READ );
			sceneGroup.setCapability( BranchGroup.ALLOW_CHILDREN_READ );

			Hashtable namedObjects = scene.getNamedObjects( );
			System.out.println( "*** Named Objects in VRML file: \n" + namedObjects );

			// recursively set the user data here
			// so we can find our objects when they are picked
			java.util.Enumeration enumValues = namedObjects.elements( );
			java.util.Enumeration enumKeys = namedObjects.keys( );

			if( enumValues != null )
			{
				while( enumValues.hasMoreElements( ) != false )
				{
					Object value = enumValues.nextElement( );
					Object key = enumKeys.nextElement( );

					recursiveSetUserData( value, key );
				}
			}
		}

		return sceneGroup;
	}	

	// method to recursively set the user data for objects in the scenegraph tree
	// we also set the capabilites on Shape3D and Morph objects required by the PickTool
	void recursiveSetUserData( Object value, Object key )
	{
		if( value instanceof SceneGraphObject != false )
		{
			// set the user data for the item
			SceneGraphObject sg = (SceneGraphObject) value;
			sg.setUserData( key );

			// recursively process group
			if( sg instanceof Group )
			{
				Group g = (Group) sg;

				// recurse on child nodes
				java.util.Enumeration enumKids = g.getAllChildren( );

				while( enumKids.hasMoreElements( ) != false )
					recursiveSetUserData( enumKids.nextElement( ), key );
			}
			else if ( sg instanceof Shape3D || sg instanceof Morph )
			{
				PickTool.setCapabilities( (Node) sg, PickTool.INTERSECT_FULL );
			}
		}
	}

	// rewritten to check for user data
	public void mouseClicked( MouseEvent e )
	{
		System.out.println( "*** MouseClick ***" );

		pickCanvas.setShapeLocation( e );
		PickResult[] results = pickCanvas.pickAllSorted( );

		if( results != null )
		{
			for (int n = 0; n < results.length; n++ )
			{
				PickResult pickResult = results[n];

				System.out.println( "Sorted PickResult " + n + ": " + pickResult );

				Node actualNode = pickResult.getObject( );

				if( actualNode.getUserData( ) != null )
				{
					System.out.println( "Sorted Object " + n + ": " + actualNode.getUserData( ) );
				}
			}
		}

		PickResult pickResult = pickCanvas.pickClosest( );

		if( pickResult != null )
		{
			System.out.println( "Closest PickResult: " + pickResult );

			Node actualNode = pickResult.getObject( );

			if( actualNode.getUserData( ) != null )
			{
				System.out.println( "Closest Object: " + actualNode.getUserData( ) );
			}
		}

	}

	public void mouseEntered( MouseEvent e )
	{
	}

	public void mouseExited( MouseEvent e )
	{
	}

	public void mousePressed( MouseEvent e )
	{
	}

	public void mouseReleased( MouseEvent e )
	{
	}


	public static void main( String[] args )
	{
	    System.out.println( "Current user.dir: " + System.getProperty( "user.dir") );

		VrmlPickingTest pickingTest = new VrmlPickingTest( args );

		new MainFrame( pickingTest, 400, 400 );
	}
}
