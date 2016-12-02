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

package org.selman.java3d.book.lighttest;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;

import javax.media.j3d.*;
import javax.vecmath.*;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.geometry.*;

import org.selman.java3d.book.common.*;

/**
* This example creates a simple scene composed
* of several spheres and a QuadArray. The scene
* is lit using 4 lights: AmbientLight, DirectionalLight,
* PointLight and SpotLight.
* Some UI is created for each light to allow the user
* to interactively modify the lights' parameters
* and view the resulting scene.
*/
public class LightTest extends Java3dApplet
{
	private static final int 			m_kWidth = 400;
	private static final int			m_kHeight = 400;

	public LightTest( )
	{
		initJava3d( );
	}

	// create a pop-up Frame to contain the
	// UI to control each light.
	protected void createLight( LightObject light, BranchGroup objRoot )
	{
		Frame frame = new Frame( light.getName( ) );
		Panel aPanel = new Panel( );
		frame.add( aPanel );		
		light.addUiToPanel( aPanel );
		frame.pack( );
		frame.setSize( new Dimension( 400, 250 ) );
		frame.validate( );
		frame.setVisible( true );

		// add the geometry that depicts the light
		// to the scenegraph
		objRoot.addChild( light.createGeometry( ) );

		// finally add the light itself to the scenegraph
		objRoot.addChild( light.getLight( ) );
	}

	// overridden to use a black background
	// so we can see the lights better
	protected Background createBackground( )
	{
		return null;
	}

	protected BranchGroup createSceneBranchGroup( )
	{
		BranchGroup objRoot = super.createSceneBranchGroup( );

		// create the 4 lights - the actual creation
		// and UI managment is delegated to an object
		// that "shadows" (no pun intended) the functionality
		// of the particular light
		createLight( new AmbientLightObject( ), objRoot );
		createLight( new PointLightObject( ), objRoot );
		createLight( new DirectionalLightObject( ), objRoot );
		createLight( new SpotLightObject( ), objRoot );

		// rotate some of the spheres in the scene
		TransformGroup objTrans = new TransformGroup( );
		objTrans.setCapability( TransformGroup.ALLOW_TRANSFORM_WRITE );
		objTrans.setCapability( TransformGroup.ALLOW_TRANSFORM_READ );

		Transform3D yAxis = new Transform3D( );
		Alpha rotationAlpha = new Alpha( -1, Alpha.INCREASING_ENABLE,
			0, 0,
			4000, 0, 0,
			0, 0, 0 );

		RotationInterpolator rotator = new RotationInterpolator( rotationAlpha, objTrans, yAxis, 0.0f, (float) Math.PI*2.0f );
		rotator.setSchedulingBounds( getApplicationBounds( ) );
		objTrans.addChild( rotator );

		// create a large sphere in the center of the
		// scene and the floor as staionary objects
		objRoot.addChild( createSphere( 0,0,0, 2 ) );
		objRoot.addChild( createFloor( ) );

		// create a smaller sphere at the corners of a cube
		final int nCubeSize = 3;
		objTrans.addChild( createSphere( nCubeSize,nCubeSize,nCubeSize, 1 ) );
		objTrans.addChild( createSphere( nCubeSize,nCubeSize,-nCubeSize, 1 ) );
		objTrans.addChild( createSphere( nCubeSize,-nCubeSize,nCubeSize, 1 ) );
		objTrans.addChild( createSphere( nCubeSize,-nCubeSize,-nCubeSize, 1 ) );
		objTrans.addChild( createSphere( -nCubeSize,nCubeSize,nCubeSize, 1 ) );
		objTrans.addChild( createSphere( -nCubeSize,nCubeSize,-nCubeSize, 1 ) );
		objTrans.addChild( createSphere( -nCubeSize,-nCubeSize,nCubeSize, 1 ) );
		objTrans.addChild( createSphere( -nCubeSize,-nCubeSize,-nCubeSize, 1 ) );

		// add some small spheres here and there to
		// make things interesting
		objRoot.addChild( createSphere( -6,-6,2, 1 ) );
		objRoot.addChild( createSphere( 8,-5,3, 1 ) );
		objRoot.addChild( createSphere( 6,7,-1, 1 ) );
		objRoot.addChild( createSphere( -5,6,-3.5f, 0.5f ) );

		objRoot.addChild( objTrans );

		return objRoot;
	}

	// creates a QuadArray and uses per-vertex
	// colors to make a black and white pattern
	protected BranchGroup createFloor( )
	{
		final int LAND_WIDTH = 12;
		final float LAND_HEIGHT = -4.0f;
		final int LAND_LENGTH = 12;

		final int nTileSize = 2;

		// calculate how many vertices we need to store all the "tiles" 
		// that compose the QuadArray.
		final int nNumTiles = ((LAND_LENGTH/nTileSize) * 2 ) * ((LAND_WIDTH/nTileSize) * 2 );
		final int nVertexCount = 4 * nNumTiles;
		Point3f[] coordArray = new Point3f[nVertexCount];
		Color3f[] colorArray = new Color3f[nVertexCount];

		// create an Appearance
		Appearance app = new Appearance( );

		// create the parent BranchGroup
		BranchGroup bg = new BranchGroup( );

		int nItem = 0;

		Color3f whiteColor = new Color3f( 1,1,1 );
		Color3f blackColor = new Color3f( 0,0,0 );

		// loop over all the tiles in the environment
		for( int x = -LAND_WIDTH; x <= LAND_WIDTH; x+=nTileSize )
		{
			for( int z = -LAND_LENGTH; z <= LAND_LENGTH; z+=nTileSize )
			{					
				// if we are not on the last row or column create a "tile"
				// and add to the QuadArray. Use CCW winding and assign texture
				// coordinates.
				if( z < LAND_LENGTH && x < LAND_WIDTH )
				{
					coordArray[nItem] = new Point3f( x, LAND_HEIGHT, z );
					colorArray[nItem++] = blackColor;

					coordArray[nItem] = new Point3f( x, LAND_HEIGHT, z + nTileSize );
					colorArray[nItem++] = whiteColor;	

					coordArray[nItem] = new Point3f( x + nTileSize, LAND_HEIGHT, z + nTileSize );
					colorArray[nItem++] = blackColor;	

					coordArray[nItem] = new Point3f( x + nTileSize, LAND_HEIGHT, z );
					colorArray[nItem++] = whiteColor;	
				}
			}
		}

		// create a GeometryInfo and generate Normal vectors
		// for the QuadArray that was populated.
		GeometryInfo gi = new GeometryInfo( GeometryInfo.QUAD_ARRAY );

		gi.setCoordinates( coordArray );
		gi.setColors( colorArray );

		NormalGenerator normalGenerator = new NormalGenerator( );
		normalGenerator.generateNormals( gi );

		// wrap the GeometryArray in a Shape3D
		Shape3D shape = new Shape3D( gi.getGeometryArray( ), app );

		// add the Shape3D to the parent BranchGroup
		bg.addChild( shape );

		return bg;
	}

	// helper method to create and position a sphere of a give size
	protected Group createSphere( float x, float y, float z, float radius )
	{
		TransformGroup tg = new TransformGroup( );
		Transform3D t3d = new Transform3D( );
		t3d.setTranslation( new Vector3d( x, y, z ) );
		tg.setTransform( t3d );

		// create an Appearance and Material
		Appearance app = new Appearance( );
		Color3f objColor = new Color3f( 1.0f, 0.7f, 0.8f );
		Color3f black = new Color3f( 0.0f, 0.0f, 0.0f );
		app.setMaterial( new Material( objColor, black, objColor, black, 80.0f ) );

		tg.addChild( new Sphere( radius, Primitive.GENERATE_NORMALS, app ) );

		return tg;
	}

	public static void main( String[] args )
	{
		LightTest lightTest = new LightTest( );
		lightTest.saveCommandLineArguments( args );

		new MainFrame( lightTest, m_kWidth, m_kHeight );
	}
}
