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

package org.selman.java3d.book.immediatetest;

import javax.media.j3d.*;
import javax.vecmath.*;

public class ImmediateCanvas3D extends Canvas3D
{
	private long 					m_nRender = 0;
	private long					m_StartTime = 0;

	private static final int 		nGridSize = 50;
	private static final int 		m_kReportInterval = 50;

	private PointArray 				m_PointArray = new PointArray( nGridSize * nGridSize, GeometryArray.COORDINATES );
	private Transform3D 			m_t3d = new Transform3D( );

	private float					m_rot = 0.0f;

	ImmediateCanvas3D( java.awt.GraphicsConfiguration graphicsConfiguration )
	{
		super( graphicsConfiguration );

		// create the PointArray that we will be rendering
		int nPoint = 0;

		for( int n = 0; n < nGridSize; n++ )
		{
			for( int i = 0; i < nGridSize; i++ )
			{
				Point3f point = new Point3f( n - nGridSize/2, i - nGridSize/2, 0.0f );
				m_PointArray.setCoordinate( nPoint++, point );
			}
		}
	}	


	public void renderField( int fieldDesc )
	{
		super.renderField( fieldDesc );

		GraphicsContext3D g = getGraphicsContext3D( );

		// first time initialization
		if( m_nRender == 0 )
		{
			// set the start time
			m_StartTime = System.currentTimeMillis( );

			// add a light to the graphics context
			DirectionalLight light = new DirectionalLight( );
			light.setEnable( true );
			g.addLight( (Light) light );

			// create the material for the points
			Appearance a = new Appearance( );
			Material mat = new Material( );
			mat.setLightingEnable( true );
			mat.setAmbientColor( 0.5f, 1.0f, 1.0f );
			a.setMaterial( mat );
			a.setColoringAttributes( new ColoringAttributes( 1.0f, 0.5f, 0.5f, ColoringAttributes.NICEST ) );

			// enlarge the points
			a.setPointAttributes( new PointAttributes( 4, true ) );

			// make the appearance current in the graphics context
			g.setAppearance( a );
		}

		// set the current transformation for the graphics context
		g.setModelTransform( m_t3d );

		// finally render the PointArray
		g.draw( m_PointArray );

		// calculate and display the Frames Per Second for the
		// Immediate Mode rendering of the PointArray
		m_nRender++;

		if( (m_nRender % m_kReportInterval ) == 0 )
		{
			float fps = (float) 1000.0f / ((System.currentTimeMillis( ) - m_StartTime) / (float) m_kReportInterval);
			System.out.println( "FPS:\t" + fps );

			m_StartTime = System.currentTimeMillis( );
		}
	}	

	public void preRender( )
	{
		super.preRender( );

		// update the model transformation to rotate the PointArray 
		// about the Y axis.
		m_rot += 0.1;
		m_t3d.rotY( m_rot );

		// move the transform back so we can see the points
		m_t3d.setTranslation( new Vector3d( 0.0, 0.0, -20.0 ) );

		// scale the transformation down so we can see all of the points
		m_t3d.setScale( 0.3 );

		// force a paint (will call renderField)
		paint( getGraphics( ) );
	}	
}
