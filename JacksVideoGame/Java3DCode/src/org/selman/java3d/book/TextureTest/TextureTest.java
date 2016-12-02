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

package org.selman.java3d.book.texturetest;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.media.j3d.*;
import javax.vecmath.*;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.geometry.*;

import com.sun.j3d.utils.image.*;

import org.selman.java3d.book.common.*;

/**
* This example reads in the name of a texture image
* and texture coordinates from a file and creates
* geometry to display the texture image mapped onto
* a triangulated polygon.
*/
public class TextureTest extends Java3dApplet
{
	private static int 			m_kWidth = 600;
	private static int 			m_kHeight = 600;

	public TextureTest( )
	{
		initJava3d( );
	}

	protected int getCanvas3dWidth( Canvas3D c3d )
	{
		return m_kWidth-5;
	}

	protected int getCanvas3dHeight( Canvas3D c3d )
	{
		return m_kHeight-5;
	}

	protected BranchGroup createSceneBranchGroup( )
	{
		BranchGroup objRoot = super.createSceneBranchGroup( );

		TransformGroup objTrans = new TransformGroup( );
		objTrans.setCapability( TransformGroup.ALLOW_TRANSFORM_WRITE );
		objTrans.setCapability( TransformGroup.ALLOW_TRANSFORM_READ );

		// add an Interpolator to rotate the scene
		objTrans.addChild( createInterpolator( objTrans ) );

		// process the texture input files and add the geometry
		objTrans.addChild( createTextureGroup( "ann.txt", -5, -5, -3, false ) );
		objTrans.addChild( createTextureGroup( "daniel.txt", -5, 5, 3, false ) );
		objTrans.addChild( createTextureGroup( "ann.txt", 5, 5, 3, false ) );
		objTrans.addChild( createTextureGroup( "daniel.txt", 5, -5, -3, false ) );

		objRoot.addChild( objTrans );

		return objRoot;
	}

	// creates a TransformGroup and positions it and adds
	// the texture geometry as a child node
	protected TransformGroup createTextureGroup( String szFile, double x, double y, double z, boolean bWireframe )
	{
		TransformGroup tg = new TransformGroup( );
		Transform3D t3d = new Transform3D( );
		t3d.setTranslation( new Vector3d( x,y,z ) );
		tg.setTransform( t3d );

		Shape3D texShape = createTextureGeometry( szFile, bWireframe );

		if( texShape != null )
			tg.addChild( texShape );

		return tg;
	}

	// return a Shape3D that is a triangulated texture mapped polygon
	// based on the texture coordinates and name of texture image in the 
	// input file
	protected Shape3D createTextureGeometry( String szFile, boolean bWireframe )
	{
		// load all the texture data from the file and create the geometry coordinates
		TextureGeometryInfo texInfo = createTextureCoordinates( szFile );

		if( texInfo == null )
		{
			System.err.println( "Could not load texture info for file:" + szFile );
			return null;
		}

		// print some stats on the loaded file
		System.out.println( "Loaded File: " + szFile );
		System.out.println( "   Texture image: " + texInfo.m_szImage );
		System.out.println( "   Texture coordinates: " + texInfo.m_TexCoordArray.length );

		// create an Appearance and assign a Material
		Appearance app = new Appearance( );

		PolygonAttributes polyAttribs = null;

		// create the PolygonAttributes and attach to the Appearance,
		// note that we use CULL_NONE so that the "rear" side of the geometry
		// is visible with the applied texture image
		if( bWireframe == false )
			polyAttribs = new PolygonAttributes( PolygonAttributes.POLYGON_FILL, PolygonAttributes.CULL_NONE, 0 );
		else
			polyAttribs = new PolygonAttributes( PolygonAttributes.POLYGON_LINE, PolygonAttributes.CULL_NONE, 0 );

		app.setPolygonAttributes( polyAttribs );

		// load the texture image and assign to the appearance
		TextureLoader texLoader = new TextureLoader( texInfo.m_szImage, Texture.RGB, this );
		Texture tex = texLoader.getTexture( );
		app.setTexture( tex );

		// create a GeometryInfo for the QuadArray that was populated.
		GeometryInfo gi = new GeometryInfo( GeometryInfo.POLYGON_ARRAY );
		gi.setCoordinates( texInfo.m_CoordArray );
		gi.setTextureCoordinates( texInfo.m_TexCoordArray );

		// use the triangulator utility to triangulate the polygon
		int[] stripCountArray = {texInfo.m_CoordArray.length};
		int[] countourCountArray = {stripCountArray.length};

		gi.setContourCounts( countourCountArray );
		gi.setStripCounts( stripCountArray );

		Triangulator triangulator = new Triangulator( );
		triangulator.triangulate( gi );

		// generate normal vectors for the triangles, not
		// strictly necessary as we are not lighting the scene
		// but generally useful
		NormalGenerator normalGenerator = new NormalGenerator( );
		normalGenerator.generateNormals( gi );

		// wrap the GeometryArray in a Shape3D and assign appearance
		return new Shape3D( gi.getGeometryArray( ), app );
	}

	// handles the nitty-gritty details of loading the input
	// file and reading (in order):
	// - texture file image name
	// - size of the geometry in the X direction
	// - Y direction scale factor
	// - number of texture coordinates
	// - each texture coordinate (X Y)
	// This could all be easily accomplished using a scenegraph
	// loader but this simple code is included for reference.
	protected TextureGeometryInfo createTextureCoordinates( String szFile )
	{
		// create a simple wrapper class to package our
		// return values
		TextureGeometryInfo texInfo = new TextureGeometryInfo( );

		// allocate a temporary buffer to store the input file
		StringBuffer szBufferData = new StringBuffer( );

		float sizeGeometryX = 0;
		float factorY = 1;
		int nNumPoints = 0;
		Point2f boundsPoint = new Point2f( );

		try
		{
			// attach a reader to the input file
			FileReader fileIn = new FileReader( szFile );

			int nChar = 0;

			// read the entire file into the StringBuffer
			while( true )
			{
				nChar = fileIn.read( );

				// if we have not hit the end of file 
				// add the character to the StringBuffer
				if( nChar != -1 )
					szBufferData.append( (char) nChar );
				else
					// EOF
					break;
			}

			// create a tokenizer to tokenize the input file at whitespace
			java.util.StringTokenizer tokenizer = new java.util.StringTokenizer( szBufferData.toString( ) );

			// read the name of the texture image
			texInfo.m_szImage = tokenizer.nextToken( );

			// read the size of the generated geometry in the X dimension
			sizeGeometryX = Float.parseFloat( tokenizer.nextToken( ) );

			// read the Y scale factor
			factorY = Float.parseFloat( tokenizer.nextToken( ) );

			// read the number of texture coordinates
			nNumPoints = Integer.parseInt( tokenizer.nextToken( ) );

			// read each texture coordinate
			texInfo.m_TexCoordArray = new Point2f[nNumPoints];
			Point2f texPoint2f = null;

			for( int n = 0; n < nNumPoints; n++ )
			{
				// JAVA 3D 1.2 change - the Y coordinates 
				// have been flipped, so we have to subtract the Y coordinate from 1
				texPoint2f = new Point2f( Float.parseFloat( tokenizer.nextToken( ) ), 
					1.0f - Float.parseFloat( tokenizer.nextToken( ) ) );

				texInfo.m_TexCoordArray[n] = texPoint2f;

				// keep an eye on the extents of the texture coordinates 
				// so we can automatically center the geometry
				if( n == 0 || texPoint2f.x > boundsPoint.x )
					boundsPoint.x = texPoint2f.x;

				if( n == 0 || texPoint2f.y > boundsPoint.y )
					boundsPoint.y = texPoint2f.y;				
			}
		}
		catch( Exception e )
		{
			System.err.println( e.toString( ) );
			return null;
		}

		// build the array of coordinates
		texInfo.m_CoordArray = new Point3f[nNumPoints];

		for( int n = 0; n < nNumPoints; n++ )
		{
			// scale and center the geometry based on the texture coordinates
			texInfo.m_CoordArray[n] = new Point3f( sizeGeometryX * (texInfo.m_TexCoordArray[n].x - boundsPoint.x/2),
				factorY * sizeGeometryX * (texInfo.m_TexCoordArray[n].y - boundsPoint.y/2),
				0 );
		}

		return texInfo;
	}


	// creates a fancy RotPosPathInterpolator to spin the scene
	// between various positions and rotations.
	protected Interpolator createInterpolator( TransformGroup objTrans )
	{
		Transform3D t3d = new Transform3D( );

		float[] knots = {0.0f, 0.1f, 0.2f, 0.3f, 0.4f, 0.6f, 0.8f, 0.9f, 1.0f};
		Quat4f[] quats = new Quat4f[9];
		Point3f[] positions = new Point3f[9];

		AxisAngle4f axis = new AxisAngle4f( 1.0f,0.0f,0.0f,0.0f );
		t3d.set( axis );

		quats[0] = new Quat4f( 0.3f, 1.0f, 1.0f, 0.0f );
		quats[1] = new Quat4f( 1.0f, 0.0f, 0.0f, 0.3f );
		quats[2] = new Quat4f( 0.2f, 1.0f, 0.0f, 0.0f );
		quats[3] = new Quat4f( 0.0f, 0.2f, 1.0f, 0.0f );
		quats[4] = new Quat4f( 1.0f, 0.0f, 0.4f, 0.0f );
		quats[5] = new Quat4f( 0.0f, 1.0f, 1.0f, 0.2f );
		quats[6] = new Quat4f( 0.3f, 0.3f, 0.0f, 0.0f );
		quats[7] = new Quat4f( 1.0f, 0.0f, 1.0f, 1.0f );
		quats[8] = quats[0];

		positions[0]= new Point3f( 0.0f,  0.0f, -1.0f );
		positions[1]= new Point3f( 1.0f, -2.0f, -2.0f );
		positions[2]= new Point3f( -2.0f,  2.0f, -3.0f );
		positions[3]= new Point3f( 1.0f,  1.0f, -4.0f );
		positions[4]= new Point3f( -4.0f, -2.0f, -5.0f );
		positions[5]= new Point3f( 2.0f,  0.3f, -6.0f );
		positions[6]= new Point3f( -4.0f,  0.5f, -7.0f );
		positions[7]= new Point3f( 0.0f, -1.5f, -4.0f );
		positions[8]= positions[0];

		Alpha alpha = new Alpha( -1, Alpha.INCREASING_ENABLE, 0, 0,10000, 0, 0, 0, 0, 0 );

		RotPosPathInterpolator rotPosPath = new RotPosPathInterpolator( alpha, objTrans, t3d, knots, quats, positions );
		rotPosPath.setSchedulingBounds( createApplicationBounds( ) );

		return rotPosPath;
	}

	public static void main( String[] args )
	{
		TextureTest textureTest = new TextureTest( );
		textureTest.saveCommandLineArguments( args );

		new MainFrame( textureTest, m_kWidth, m_kHeight );
	}
}
