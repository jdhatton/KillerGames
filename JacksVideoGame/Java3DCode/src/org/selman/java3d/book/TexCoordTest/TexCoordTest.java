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

package org.selman.java3d.book.texcoordtest;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;

import javax.media.j3d.*;
import javax.vecmath.*;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.geometry.*;

import com.sun.j3d.utils.image.*;

import org.selman.java3d.book.common.*;

/**
* This example illustrates dynamic texture coordinate generation
* using the TexCoordGeneration class. The OBJECT_LINEAR, EYE_LINEAR 
* and SPHERE_MAP modes are illustrated. The application creates a DEM
* landscape (from a sin/cos curve) and uses dynamic texture coordinate
* generation to map contours onto the landscape - either relative to its
* relative position (OBJECT_LINEAR) or absolute position (EYE_LINEAR).
* SPHERE_MAP maps the contours onto the landscape as a environment map.
* The "Rotate" and "Translate" buttons toggle two Interpolators that
* rotate and translate the entire scene respectivly.
*/
public class TexCoordTest extends Java3dApplet implements ActionListener
{
	private static int 			m_kWidth = 500;
	private static int 			m_kHeight = 400;

	// we need to track these as we modify them inside 
	// the actionPerformed method
	Appearance					m_Appearance = null;
	TexCoordGeneration			m_TexGen = null;
	PositionInterpolator 		m_PositionInterpolator = null;
	RotationInterpolator		m_RotationInterpolator = null;

	public TexCoordTest( )
	{
		initJava3d( );
	}
   
	protected void addCanvas3D( Canvas3D c3d )
	{
		setLayout( new BorderLayout( ) );
		add( c3d, BorderLayout.CENTER );

		Panel controlPanel = new Panel();
              
		// creates some GUI components so we can modify the
		// scene and texure coordinate generation at runtime
		Button eyeButton = new Button( "EYE_LINEAR" );
		eyeButton.addActionListener( this );
		controlPanel.add( eyeButton );

		Button objectButton = new Button( "OBJECT_LINEAR" );
		objectButton.addActionListener( this );
		controlPanel.add( objectButton );

		Button sphereButton = new Button( "SPHERE_MAP" );
		sphereButton.addActionListener( this );
		controlPanel.add( sphereButton );

		Button rotateButton = new Button( "Rotate" );
		rotateButton.addActionListener( this );
		controlPanel.add( rotateButton );

		Button translateButton = new Button( "Translate" );
		translateButton.addActionListener( this );
		controlPanel.add( translateButton );
       
       add( controlPanel, BorderLayout.SOUTH );
       
		doLayout( );
	}

	// overidden as we don't want a background for this example
	protected Background createBackground( )
	{
		return null;
	}

	// we are using a big landscape so we have to scale it down
	protected double getScale( )
	{
		return 0.05;
	}

	// handle event from the GUI components we created
	public void actionPerformed( ActionEvent event )
	{
		if( event.getActionCommand( ).equals( "EYE_LINEAR" ) != false )
			m_TexGen.setGenMode( TexCoordGeneration.EYE_LINEAR );

		else if( event.getActionCommand( ).equals( "OBJECT_LINEAR" ) != false )
			m_TexGen.setGenMode( TexCoordGeneration.OBJECT_LINEAR );

		else if( event.getActionCommand( ).equals( "SPHERE_MAP" ) != false )
			m_TexGen.setGenMode( TexCoordGeneration.SPHERE_MAP );

		else if( event.getActionCommand( ).equals( "Rotate" ) != false )
			m_RotationInterpolator.setEnable( !m_RotationInterpolator.getEnable( ) );

		else if( event.getActionCommand( ).equals( "Translate" ) != false )
			m_PositionInterpolator.setEnable( !m_PositionInterpolator.getEnable( ) );

		// apply any changes to the TexCoordGeneration and copy into the appearance
		TexCoordGeneration texCoordGeneration = new TexCoordGeneration( );
		texCoordGeneration = (TexCoordGeneration) m_TexGen.cloneNodeComponent( true );
		m_Appearance.setTexCoordGeneration( texCoordGeneration );
	}	

	// position the viewer in the scene
	public TransformGroup[] getViewTransformGroupArray( )
	{
		TransformGroup[] tgArray = new TransformGroup[1];
		tgArray[0] = new TransformGroup( );

		// move the camera BACK a little...
		// note that we have to invert the matrix as 
		// we are moving the viewer
		Transform3D t3d = new Transform3D( );

		t3d.rotX( 0.4 );
		t3d.setScale( getScale( ) );
		t3d.setTranslation( new Vector3d( 0.0, 0, -20.0 ) );
		t3d.invert( );
		tgArray[0].setTransform( t3d );

		return tgArray;
	}

	// we create a scene composed of two nested interpolators
	// one for translation, one for rotation that control
	// a DEM landscape created from a single Shape3D containing
	// a QuadArray.
	protected BranchGroup createSceneBranchGroup( )
	{
		BranchGroup objRoot = super.createSceneBranchGroup( );

		TransformGroup objPosition = new TransformGroup( );
		objPosition.setCapability( TransformGroup.ALLOW_TRANSFORM_WRITE );

		TransformGroup objRotate = new TransformGroup( );
		objRotate.setCapability( TransformGroup.ALLOW_TRANSFORM_WRITE );

		Transform3D axisTranslate = new Transform3D( );
		axisTranslate.rotZ( Math.toRadians( 90 ) );

		Alpha rotationAlpha = new Alpha( -1, Alpha.INCREASING_ENABLE,
			0, 0,
			6000, 0, 0,
			0, 0, 0 );

		m_PositionInterpolator = new PositionInterpolator( rotationAlpha,
			objPosition,
			axisTranslate,
			0,
			70 );

		m_PositionInterpolator.setSchedulingBounds( createApplicationBounds( ) );
		objPosition.addChild( m_PositionInterpolator );
		m_PositionInterpolator.setEnable( false );

		m_RotationInterpolator = new RotationInterpolator( rotationAlpha, 
			objRotate, 
			new Transform3D( ), 
			0.0f, 
			(float) Math.PI*2.0f );

		m_RotationInterpolator.setSchedulingBounds( getApplicationBounds( ) );
		objRotate.addChild( m_RotationInterpolator );
		m_RotationInterpolator.setEnable( true );

		TransformGroup tgLand = new TransformGroup( );
		Transform3D t3dLand = new Transform3D( );
		t3dLand.setTranslation( new Vector3d( 0, -30, 0 ) );
		tgLand.setTransform( t3dLand );

		tgLand.addChild( createDemLandscape( ) );				
		objRotate.addChild( tgLand );

		objPosition.addChild( objRotate );

		objRoot.addChild( objPosition );

		// create some lights for the scene
		Color3f lColor1 = new Color3f( 0.3f, 0.3f, 0.3f );
		Vector3f lDir1  = new Vector3f( -1.0f, -1.0f, -1.0f );
		Color3f alColor = new Color3f( 0.1f, 0.1f, 0.1f );

		AmbientLight aLgt = new AmbientLight( alColor );
		aLgt.setInfluencingBounds( getApplicationBounds( ) );
		DirectionalLight lgt1 = new DirectionalLight( lColor1, lDir1 );
		lgt1.setInfluencingBounds( getApplicationBounds( ) );

		// add the lights to the parent BranchGroup
		objRoot.addChild( aLgt );
		objRoot.addChild( lgt1 );

		return objRoot;
	}

	// create the Appearance for the landscape Shape3D
	// and initialize all the texture generation paramters
	// the yMaxHeight paramters is the maximum height of the 
	// landscape. It defines a scale factor to convert from 
	// landscape Y coordinates to texture coordinates 
	// (in the range 0 to 1).
	void createAppearance( double yMaxHeight )
	{
		// create an Appearance and assign a Material
		m_Appearance = new Appearance( );
		m_Appearance.setCapability( Appearance.ALLOW_TEXGEN_WRITE );

		Color3f black = new Color3f( 0, 0.2f, 0 );
		Color3f objColor = new Color3f( 0.1f, 0.7f, 0.2f );
		m_Appearance.setMaterial( new Material( objColor, black, objColor, black, 0.8f ) );

		// load the texture image
		TextureLoader texLoader = new TextureLoader( "stripes.gif", Texture.RGB, this );

		// clamp the coordinates in the S dimension, that way they
		// will not wrap when the calculated texture coordinate falls outside
		// the range 0 to 1. When the texture coordinate is outside this range
		// no texture coordinate will be used
		Texture tex = texLoader.getTexture( );
		tex.setBoundaryModeS( Texture.CLAMP );

		// assign the texute image to the appearance
		m_Appearance.setTexture( tex );

		// create the TexCoordGeneration object.
		// we are only using 1D texture coordinates (S) - texture coordinates
		// are calculated from a vertex's distance from the Y = 0 plane
		// the 4th parameter to the Vector4f is the distance in *texture coordinates*
		// that we shift the texture coordinates by (i.e. Y = 0 corresponds to S = 0.5)
		TexCoordGeneration texGen = new TexCoordGeneration( TexCoordGeneration.OBJECT_LINEAR,
			TexCoordGeneration.TEXTURE_COORDINATE_2,
			new Vector4f( 0, (float) (1.0/ (2 * yMaxHeight)), 0, 0.5f ),
			new Vector4f( 0,0,0,0 ),
			new Vector4f( 0,0,0,0 ) );

		// create our "non-live" TexCoordGeneration object so we
		// can update the "genMode" parameter after we go live.
		m_TexGen = (TexCoordGeneration) texGen.cloneNodeComponent( true );

		// assign the TexCoordGeneration to the Appearance
		m_Appearance.setTexCoordGeneration( texGen );

		// we just glue the texture image to the surface, we are not
		// blending or modulating the texture image based on material
		// attributes. You can experiment with MODULATE or BLEND.
		TextureAttributes texAttribs = new TextureAttributes( );
		texAttribs.setTextureMode( TextureAttributes.DECAL );
		m_Appearance.setTextureAttributes( texAttribs );
	}

	// create the Shape3D and geometry for the DEM landscape
	BranchGroup createDemLandscape( )
	{
		final double LAND_WIDTH = 200;
		final double LAND_LENGTH = 200;
		final double nTileSize = 10;
		final double yMaxHeight = LAND_WIDTH / 8;

		// calculate how many vertices we need to store all the "tiles" that compose the QuadArray.
		final int nNumTiles = (int) (((LAND_LENGTH/nTileSize) * 2 ) * ((LAND_WIDTH/nTileSize) * 2 ));
		final int nVertexCount = 4 * nNumTiles;
		Point3f[] coordArray = new Point3f[nVertexCount];
		Point2f[] texCoordArray = new Point2f[nVertexCount];

		// create the Appearance for the landscape and initialize all
		// the texture coordinate generation parameters
		createAppearance( yMaxHeight );

		// create the parent BranchGroup
		BranchGroup bg = new BranchGroup( );

		// create the geometry and populate a QuadArray
		// we use a simple sin/cos function to create an undulating surface
		// in the X/Z dimensions. Y dimension is the distance "above sea-level".
		int nItem = 0;
		double yValue0 = 0;
		double yValue1 = 0;
		double yValue2 = 0;
		double yValue3 = 0;

		final double xFactor = LAND_WIDTH / 5;
		final double zFactor = LAND_LENGTH / 3;

		// loop over all the tiles in the environment
		for( double x = -LAND_WIDTH; x <= LAND_WIDTH; x+=nTileSize )
		{
			for( double z = -LAND_LENGTH; z <= LAND_LENGTH; z+=nTileSize )
			{
				// if we are not on the last row or column create a "tile" 
				// and add to the QuadArray. Use CCW winding 
				if( z < LAND_LENGTH && x < LAND_WIDTH )
				{
					yValue0 = yMaxHeight * Math.sin( x/xFactor ) * Math.cos( z/zFactor );
					yValue1 = yMaxHeight * Math.sin( x/xFactor ) * Math.cos( (z + nTileSize)/zFactor );
					yValue2 = yMaxHeight * Math.sin( (x + nTileSize)/xFactor ) * Math.cos( (z + nTileSize )/zFactor );
					yValue3 = yMaxHeight * Math.sin( (x + nTileSize)/xFactor ) * Math.cos( z/zFactor );

					// note, we do not assign any texture coordinates!
					coordArray[nItem++] = new Point3f( (float) x, (float) yValue0, (float) z );
					coordArray[nItem++] = new Point3f( (float) x, (float) yValue1, (float) (z + nTileSize) );
					coordArray[nItem++] = new Point3f( (float) (x + nTileSize), (float) yValue2, (float) (z + nTileSize) );
					coordArray[nItem++] = new Point3f( (float) (x + nTileSize), (float) yValue3, (float) z );
				}
			}
		}

		// create a GeometryInfo and assign the coordinates
		GeometryInfo gi = new GeometryInfo( GeometryInfo.QUAD_ARRAY );
		gi.setCoordinates( coordArray );

		// generate Normal vectors for the QuadArray that was populated.
		NormalGenerator normalGenerator = new NormalGenerator( );
		normalGenerator.generateNormals( gi );

		// wrap the GeometryArray in a Shape3D
		Shape3D shape = new Shape3D( gi.getGeometryArray( ), m_Appearance );

		// add the Shape3D to a BranchGroup and return
		bg.addChild( shape );

		return bg;
	}


	public static void main( String[] args )
	{
		TexCoordTest texCoordTest = new TexCoordTest( );
		texCoordTest.saveCommandLineArguments( args );

		new MainFrame( texCoordTest, m_kWidth, m_kHeight );
	}
}
