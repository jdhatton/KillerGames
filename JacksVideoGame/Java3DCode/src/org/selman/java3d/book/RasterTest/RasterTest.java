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

package org.selman.java3d.book.rastertest;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;

import javax.media.j3d.*;
import javax.vecmath.*;

import com.sun.j3d.utils.applet.MainFrame;

import java.awt.image.BufferedImage;

import com.sun.j3d.utils.geometry.ColorCube;
import org.selman.java3d.book.common.*;

/**
* This example illustrates how to:<br>
* 1. Draw an image into the 3D view as a Raster object.<br>
* 2. Read the depth components of the 3D scene<br>
* 3. Dynamically update a Raster object inside the postSwap method<br>
* 4. Render a view of the depth components as a dynamic raster<br>
*/
public class RasterTest extends Java3dApplet implements ActionListener
{
	// size of the window, and hence size of the depth component array
	private static int 			m_kWidth = 400;
	private static int 			m_kHeight = 400;

	// the Raster used to store depth components
	private Raster				m_DepthRaster = null;

	// the Raster used to render an image into the 3D view
	private Raster				m_RenderRaster = null;

	// an array of integer values for the depth components
	private int[]				m_DepthData = null;


	public RasterTest( )
	{
		// create the image to be rendered using a Raster
		BufferedImage bufferedImage = new BufferedImage( 128, 128, BufferedImage.TYPE_INT_RGB );
		ImageComponent2D imageComponent2D = new ImageComponent2D( ImageComponent2D.FORMAT_RGB, bufferedImage );
		imageComponent2D.setCapability( ImageComponent.ALLOW_IMAGE_READ );
		imageComponent2D.setCapability( ImageComponent.ALLOW_SIZE_READ );

		// create the depth component to store the 3D depth values
		DepthComponentInt depthComponent = new DepthComponentInt( m_kWidth, m_kHeight );
		depthComponent.setCapability( DepthComponent.ALLOW_DATA_READ );			

		// create the Raster for the image
		m_RenderRaster = new Raster( new Point3f( 0.0f, 0.0f, 0.0f ),
			Raster.RASTER_COLOR,
			0, 0,
			bufferedImage.getWidth( ),
			bufferedImage.getHeight( ),
			imageComponent2D,
			null );

		m_RenderRaster.setCapability( Raster.ALLOW_IMAGE_WRITE );
		m_RenderRaster.setCapability( Raster.ALLOW_SIZE_READ );

		// create the Raster for the depth components
		m_DepthRaster = new Raster( new Point3f( 0.0f, 0.0f, 0.0f ),
			Raster.RASTER_DEPTH,
			0, 0,
			m_kWidth,
			m_kHeight,
			null,
			depthComponent );

		initJava3d( );
	}

	public void actionPerformed( ActionEvent event )
	{
	}

	protected Canvas3D createCanvas3D( )
	{
		// create a custom Canvas3D with postSwap overidden
		GraphicsConfigTemplate3D gc3D = new GraphicsConfigTemplate3D( );
		gc3D.setSceneAntialiasing( GraphicsConfigTemplate.PREFERRED );
		GraphicsDevice gd[] = GraphicsEnvironment.getLocalGraphicsEnvironment( ).getScreenDevices( );

		RasterCanvas3D c3d = new RasterCanvas3D( this, gd[0].getBestConfiguration( gc3D ) );
		c3d.setSize( getCanvas3dWidth( c3d ), getCanvas3dHeight( c3d ) );

		return c3d;
	}

	protected BranchGroup createSceneBranchGroup( )
	{
		// create some simple geometry (a rotating ColorCube)
		// and a Shape3D object for the Raster containing the Image
		BranchGroup objRoot = super.createSceneBranchGroup( );

		TransformGroup objTrans = new TransformGroup( );
		objTrans.setCapability( TransformGroup.ALLOW_TRANSFORM_WRITE );

		Transform3D yAxis = new Transform3D( );
		yAxis.rotX( 0.6 );
		Alpha rotationAlpha = new Alpha( -1, Alpha.INCREASING_ENABLE,
			0, 0,
			4000, 0, 0,
			0, 0, 0 );

		RotationInterpolator rotator = new RotationInterpolator( rotationAlpha, objTrans, yAxis, 0.0f, (float) Math.PI*2.0f );

		BoundingSphere bounds =	new BoundingSphere( new Point3d( 0.0,0.0,0.0 ), 100.0 );
		rotator.setSchedulingBounds( bounds );

		objTrans.addChild( rotator );

		// wrap the Raster in a Shape3D
		Shape3D shape = new Shape3D( m_RenderRaster );

		objRoot.addChild( shape );		
		objTrans.addChild( new ColorCube( 1.0 ) );

		objRoot.addChild( objTrans );

		return objRoot;
	}

	protected int getCanvas3dWidth( Canvas3D c3d )
	{
		return m_kWidth;
	}

	protected int getCanvas3dHeight( Canvas3D c3d )
	{
		return m_kHeight;
	}


	public Raster getDepthRaster( )
	{
		return m_DepthRaster;
	}


	public void updateRenderRaster( )
	{
		// takes the Depth Raster and updates the Render Raster
		// containing the image based on the depth values stored in 
		// the Depth Raster.

		// create a temporary BufferedImage for the depth components
		BufferedImage tempBufferedImage = new BufferedImage( m_DepthRaster.getDepthComponent( ).getWidth( ), 
			m_DepthRaster.getDepthComponent( ).getHeight( ), 
			BufferedImage.TYPE_INT_RGB );

		// allocate an array of ints to store the depth components from the Depth Raster
		if( m_DepthData == null )
			m_DepthData = new int[ m_DepthRaster.getDepthComponent( ).getWidth( ) * m_DepthRaster.getDepthComponent( ).getHeight( ) ];

		// copy the depth values from the Raster into the int array
		((DepthComponentInt) m_DepthRaster.getDepthComponent( )).getDepthData( m_DepthData );

		// assign the depth values to the temporary image, the integer depths will be
		// interpreted as integer rgb values.
		tempBufferedImage.setRGB( 0, 0, 	m_DepthRaster.getDepthComponent( ).getWidth( ), 
			m_DepthRaster.getDepthComponent( ).getHeight( ), 
			m_DepthData, 0, m_DepthRaster.getDepthComponent( ).getWidth( ) );

		// get a graphics device for the image
		Graphics g = tempBufferedImage.getGraphics( );
		Dimension size = new Dimension( );
		m_RenderRaster.getSize( size );

		// because the Depth Raster is a different size to the Render Raster,
		// i.e. the Depth Raster is canvas width by canvas height and the Render Raster
		// is of aritrary size, we rescale the image here.
		g.drawImage( tempBufferedImage, 0, 0, (int) size.getWidth( ), (int) size.getHeight( ), null );

		// finally, assign the scaled image to the RenderRaster
		m_RenderRaster.setImage( new ImageComponent2D( BufferedImage.TYPE_INT_RGB, tempBufferedImage ) );
	}


	public static void main( String[] args )
	{
		RasterTest rasterTest = new RasterTest( );
		rasterTest.saveCommandLineArguments( args );

		new MainFrame( rasterTest, m_kWidth, m_kHeight );
	}

	// Canvas3D overide to read the depth components of the 3D view
	// into a Raster object and notify the Applet
	public class RasterCanvas3D extends Canvas3D
	{	
		RasterTest						m_RasterTest = null;

		public RasterCanvas3D( RasterTest rasterTest, GraphicsConfiguration graphicsConfiguration )
		{
			super( graphicsConfiguration );

			m_RasterTest = rasterTest;
		}


		public void postSwap( )
		{
			super.postSwap( );
			getGraphicsContext3D( ).readRaster( m_RasterTest.getDepthRaster( ) );

			// notify the applet to update the render object 
			// used to display the depth values
			m_RasterTest.updateRenderRaster( );
		}
	}
}
