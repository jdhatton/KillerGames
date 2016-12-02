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

package org.selman.java3d.book.pickcollisiontest;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;

import javax.media.j3d.*;
import javax.vecmath.*;

import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.applet.MainFrame;

import org.selman.java3d.book.common.*;

/**
*	This example creates a large hollow box (out of ColorCubes, one for each
*	side of the box). Within the box, four Spheres are created. Each Sphere has
*	a behavior attached which detects collisions with the sides of the box, and
*	the other Spheres. When a collision is detected the trajectory of the Sphere
*	is reversed and the color of the Sphere changed. When a collision is not detected
*	the Sphere is advanced along its current trajectory.
*/
public class PickCollisionTest extends Java3dApplet implements ActionListener
{
	private static int 			m_kWidth = 400;
	private static int 			m_kHeight = 400;

	private static final int 	boxSize = 10;

	public PickCollisionTest( )
	{
		initJava3d( );
	}

	public void actionPerformed( ActionEvent event )
	{
	}

	protected double getScale( )
	{
		return 0.5;
	}


	// method to recursively set the user data for objects in the scenegraph tree
	// we also set the capabilites on Shape3D and Morph objects required by the PickTool
	void recursiveSetUserData( SceneGraphObject root, Object value )
	{
		root.setUserData( value );

		// recursively process group
		if( root instanceof Group )
		{
			Group g = (Group) root;

			// recurse on child nodes
			java.util.Enumeration enumKids = g.getAllChildren( );

			while( enumKids.hasMoreElements( ) != false )
				recursiveSetUserData( (SceneGraphObject) enumKids.nextElement( ), value );
		}
	}

	protected void addCube( BranchGroup bg, double x, double y, double z, double sx, double sy, double sz, String name, boolean wireframe )
	{
		// create four ColorCube objects
		TransformGroup cubeTg = new TransformGroup( );
		Transform3D t3d = new Transform3D( );
		t3d.setTranslation( new Vector3d( x, y, z ) );
		t3d.setScale( new Vector3d( sx, sy, sz ) );
		cubeTg.setTransform( t3d );
		ColorCube cube = new ColorCube( 1.0 );

		// we have to make the front face wireframe
		// or we can't see inside the box!		
		if ( wireframe )
		{
			Appearance app = new Appearance( );
			app.setPolygonAttributes( new PolygonAttributes( PolygonAttributes.POLYGON_LINE, PolygonAttributes.CULL_NONE, 0 ) );
			cube.setAppearance( app );
		}

		cubeTg.addChild( cube );
		recursiveSetUserData( cubeTg, name );

		bg.addChild( cubeTg );
	}

	protected void addSphere( BranchGroup bg, double x, double y, double z, Vector3d incVector, String name )
	{
		Appearance app = new Appearance( );

		TransformGroup sphereTg = new TransformGroup( );
		Transform3D t3d = new Transform3D( );
		t3d.setTranslation( new Vector3d( x, y, z ) );
		sphereTg.setTransform( t3d );

		sphereTg.setCapability( TransformGroup.ALLOW_TRANSFORM_WRITE );
		sphereTg.setCapability( TransformGroup.ALLOW_TRANSFORM_READ );

		sphereTg.addChild( new Sphere( 1, app ) );
		bg.addChild( sphereTg );
		recursiveSetUserData( sphereTg, name );

		// create the collision behaviour
		CollisionBehavior collisionBehavior = new CollisionBehavior( bg, sphereTg, app, new Vector3d( x,y,z ), incVector );
		collisionBehavior.setSchedulingBounds( getApplicationBounds( ) );
		bg.addChild( collisionBehavior );
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

		// create ColorCube objects, one for each side of a cube
		addCube( objRoot, 0,boxSize,0, boxSize,0.1,boxSize, "Top", false );
		addCube( objRoot, 0,-boxSize,0, boxSize,0.1,boxSize, "Bottom", false );
		addCube( objRoot, boxSize,0,0, 0.1,boxSize,boxSize, "Right", false );
		addCube( objRoot, -boxSize,0,0, 0.1,boxSize,boxSize, "Left", false );
		addCube( objRoot, 0,0,-boxSize, boxSize,boxSize,0.1, "Back", false );
		addCube( objRoot, 0,0,boxSize, boxSize,boxSize,0.1, "Front", true );

		// create the spheres
		addSphere( objRoot, 0,3,4, new Vector3d( 0.1,0.3,0.1 ), "Sphere 1" );
		addSphere( objRoot, 3,0,-2, new Vector3d( 0.4,0.1,0.2 ), "Sphere 2" );		
		addSphere( objRoot, 0,-3,0, new Vector3d( 0.2,0.2,0.6 ), "Sphere 3" );
		addSphere( objRoot, -3,0,-4, new Vector3d( 0.1,0.6,0.3 ), "Sphere 4" );

		return objRoot;
	}


	public static void main( String[] args )
	{
		PickCollisionTest pickCollisionTest = new PickCollisionTest( );
		pickCollisionTest.saveCommandLineArguments( args );

		new MainFrame( pickCollisionTest, m_kWidth, m_kHeight );
	}
}
