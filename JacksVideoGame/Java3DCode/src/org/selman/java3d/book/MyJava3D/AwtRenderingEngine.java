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

import java.awt.*;

import javax.media.j3d.*;
import javax.vecmath.*;

import java.util.LinkedList;
import java.util.List;

/**
* Implementation of the RenderingEngine interface using AWT.
*/
public class AwtRenderingEngine implements RenderingEngine
{
	private List 					geometryList = null;

	private int						xScreenCenter = 320/2;
	private int						yScreenCenter = 240/2;

	private Vector3d				screenPosition = new Vector3d( 0, 0, 20 );
	private Vector3d				viewAngle = new Vector3d( 180, 180, 180 );
	private Vector3d				lightAngle = new Vector3d( 0, 0, 0 );

	private double 					CT;
	private double 					ST;
	private double 					CP;
	private double 					SP;

	private static final double		DEG_TO_RAD = 0.017453292;

	private static final int		NUM_POINTS = 4;
	private int[]					xCoordArray = new int[NUM_POINTS];
	private int[]					yCoordArray = new int[NUM_POINTS];
	private Point3d[]				pointArray = new Point3d[NUM_POINTS];
	private Point3d[]				projectedPointArray = new Point3d[NUM_POINTS];

	private long					frameNumber = 0;
	private static final int		POINT_WIDTH = 1;
	private static final int		POINT_HEIGHT = 1;

	private double 					modelScale = 20;
	private long					startTime = 0;

	private boolean					drawBackface = true;
	private boolean					computeIntensity = true;
	private boolean					lightRelativeToView = true;
	private Vector3d				lightAngleOffset = new Vector3d( 45, 45, 45 );

	private Vector3f[] 				normalsArray = new Vector3f[NUM_POINTS];
	private Vector3f 				light = new Vector3f( );
	private Vector3f 				surf_norm = new Vector3f( );
	private Vector3f 				view = new Vector3f( );
	private Vector3f 				temp = new Vector3f( );
   
	private static final double 	lightAmbient = 0.30;
	private static final double 	lightDiffuse = 0.50;
	private static final double 	lightSpecular = 0.20;
	private static final double 	lightGlossiness = 5.0;
	private static final int		lightMax = 255;
   

	public AwtRenderingEngine( )
	{
		geometryList = new LinkedList( );

		for( int n = 0; n < NUM_POINTS; n++ )
		{
			pointArray[n] = new Point3d( );
			projectedPointArray[n] = new Point3d( );
			normalsArray[n] = new Vector3f( );
		}

		setViewAngle( viewAngle );
		setLightAngle( lightAngle );
	}

	public void setScale( double scale )
	{
		modelScale = scale;
	}

	public double getScale( )
	{
		return modelScale;
	}

	public Vector3d getScreenPosition( )
	{
		return screenPosition;
	}

	public void setScreenSize( int width, int height )
	{
		xScreenCenter = width / 2;
		yScreenCenter = height / 2;
	}

	public void setScreenPosition( Vector3d screenPosition )
	{
		this.screenPosition = screenPosition;
	}

	public Vector3d getViewAngle( )
	{
		return viewAngle;
	}

	public void setViewAngle( Vector3d angle )
	{
		this.viewAngle = angle;
		// System.out.println( "ViewAngle: " + viewAngle );

		CT = Math.cos( DEG_TO_RAD * viewAngle.x );
		ST = Math.sin( DEG_TO_RAD * viewAngle.x );
		CP = Math.cos( DEG_TO_RAD * viewAngle.y );
		SP = Math.sin( DEG_TO_RAD * viewAngle.y );

		view.x = (float) (-CP * ST);
		view.y = (float) (-CP * CT);
		view.z = (float) SP;

		if ( lightRelativeToView != false )
		{
			lightAngle.x = viewAngle.x + lightAngleOffset.x;
			lightAngle.y = viewAngle.y + lightAngleOffset.y;
			lightAngle.z = viewAngle.z + lightAngleOffset.z;
			setLightAngle( lightAngle );
		}
	}

	public Vector3d getLightAngle( )
	{
		return lightAngle;
	}

	public void setLightAngle( Vector3d angle )
	{
	    this.lightAngle = angle;       
       //System.out.println( "LightAngle: " + lightAngle );

	    CT = Math.cos( DEG_TO_RAD * viewAngle.x );
	    ST = Math.sin( DEG_TO_RAD * viewAngle.x );
	    CP = Math.cos( DEG_TO_RAD * viewAngle.y );
	    SP = Math.sin( DEG_TO_RAD * viewAngle.y );

		light.x = (float) (Math.sin( DEG_TO_RAD * lightAngle.y ) * Math.cos( DEG_TO_RAD * lightAngle.x ));
		light.y = (float) (Math.sin( DEG_TO_RAD * lightAngle.y ) * Math.sin( DEG_TO_RAD * lightAngle.x ));
		light.z = (float) (Math.cos( DEG_TO_RAD * lightAngle.y ));
	}

	public void addGeometry( GeometryArray geometryArray )
	{
		System.out.println( "Adding GeometryArray: " + geometryArray );
		geometryList.add( geometryArray );
	}

	public void renderGeometry( Graphics graphics, GeometryUpdater updater )
	{
		GeometryArray geomArray;

		for( int n = 0; n < geometryList.size( ); n++ )
		{
			geomArray = (GeometryArray) geometryList.get( n );

			if ( geomArray instanceof LineArray )
			{
				renderLineArray( graphics, updater, geomArray );
			}
			else if ( geomArray instanceof PointArray )
			{
				renderPointArray( graphics, updater, geomArray );
			}
			else if ( geomArray instanceof QuadArray )
			{
				renderQuadArray( graphics, updater, geomArray );        
			}
			else if ( geomArray instanceof TriangleArray )
			{
				renderTriangleArray( graphics, updater, geomArray );
			}
			else
			{
				throw new UnsupportedOperationException( "Unknown geometry type: " + geomArray );
			}
		}
	}

	public void renderLineArray( Graphics graphics, GeometryUpdater updater, GeometryArray geometryArray )
	{
		for( int n = 0; n < geometryArray.getVertexCount( ); n+= 2 )
			drawLine( graphics, updater, geometryArray, n );
	}

	public void renderPointArray( Graphics graphics, GeometryUpdater updater, GeometryArray geometryArray )
	{
		for( int n = 0; n < geometryArray.getVertexCount( ); n++ )
			drawPoint( graphics, updater, geometryArray, n );
	}

	public void renderQuadArray( Graphics graphics, GeometryUpdater updater, GeometryArray geometryArray )
	{
		for( int n = 0; n < geometryArray.getVertexCount( ); n+=4 )
			drawQuad( graphics, updater, geometryArray, n );
	}

	public void renderTriangleArray( Graphics graphics, GeometryUpdater updater, GeometryArray geometryArray )
	{
		for( int n = 0; n < geometryArray.getVertexCount( ); n+=3 )
			drawTriangle( graphics, updater, geometryArray, n );
	}

	public void render( Graphics graphics, GeometryUpdater updater )
	{
		if( frameNumber == 0 )
			startTime = System.currentTimeMillis( );

		renderGeometry( graphics, updater );
		frameNumber++;

		if ( frameNumber % 500 == 0 )
		{
			System.out.println( "FPS: " + ((double) 500 / ( System.currentTimeMillis( ) - startTime)) * 1000.0 );
			startTime = System.currentTimeMillis( );
		}
	}

	public void projectPoint( Point3d input, Point3d output )
	{
		double x = screenPosition.x + input.x * CT - input.y * ST;
		double y = screenPosition.y + input.x * ST * SP + input.y * CT * SP + input.z * CP;
		double temp = viewAngle.z / (screenPosition.z + input.x * ST * CP + input.y * CT * CP - input.z * SP );

		output.x = xScreenCenter + modelScale * temp * x;
		output.y = yScreenCenter - modelScale * temp * y;
		output.z = 0;
	}

	public void drawLine( Graphics graphics, GeometryUpdater updater, GeometryArray geometryArray, int index )
	{
		for( int n = 0; n < 2; n++ )
		{
			updater.update( graphics, this, geometryArray, index+n, frameNumber );
			geometryArray.getCoordinate( index+n, pointArray[n] );
		}

		for( int n = 0; n < 2; n++ )
			projectPoint( pointArray[n], projectedPointArray[n] );

		drawLine( graphics, geometryArray, index, projectedPointArray );
	}

	public void drawLine( Graphics graphics, GeometryArray geometryArray, int index, Point3d[] pointArray )
	{
		int intensity = computeIntensity( geometryArray, index, 2 );

		if ( drawBackface || intensity >= 1 )
		{
			graphics.setColor( new Color( intensity, intensity, intensity ) );
			graphics.drawLine( (int) pointArray[0].x, (int) pointArray[0].y, (int) pointArray[1].x, (int) pointArray[1].y );
		}
	}

	public void drawQuad( Graphics graphics, GeometryUpdater updater, GeometryArray geometryArray, int index )
	{
		for( int n = 0; n < 4; n++ )
			updater.update( graphics, this, geometryArray, index+n, frameNumber );

		geometryArray.getCoordinates( index, pointArray );

		for( int n = 0; n < 4; n++ )
			projectPoint( pointArray[n], projectedPointArray[n] );

		drawQuad( graphics, geometryArray, index, projectedPointArray );
	}

	public void drawQuad( Graphics graphics, GeometryArray geometryArray, int index, Point3d[] pointArray )
	{
		drawFacet( graphics, geometryArray, index, pointArray, 4 );
	}

	private void averageVector( Vector3f returnVector, Vector3f[] vectorArray, int numPoints )
	{
		float x = 0;
		float y = 0;
		float z = 0;

		for( int n = 0; n < numPoints; n++ )
		{
			x += vectorArray[n].x;
			y += vectorArray[n].y;
			z += vectorArray[n].z;
		}

		returnVector.x = x / numPoints;
		returnVector.y = y / numPoints;
		returnVector.z = z / numPoints;
	}

	private int computeIntensity( GeometryArray geometryArray, int index, int numPoints )
	{
		int intensity = 0;

		if ( computeIntensity != false )
		{
			// if we have a normal vector compute the intensity under the lighting
			if ( (geometryArray.getVertexFormat( ) & GeometryArray.NORMALS) == GeometryArray.NORMALS )
			{
				double cos_theta;
				double cos_alpha;
				double cos_beta;

				for( int n = 0; n < numPoints; n++ )
					geometryArray.getNormal( index+n, normalsArray[n] );

				// take the average normal vector
				averageVector( surf_norm, normalsArray, numPoints );
				temp.set( view );
				temp.scale( 1.0f, surf_norm );

				cos_beta = temp.x + temp.y + temp.z;

				if ( cos_beta > 0.0 )
				{
					cos_theta = surf_norm.dot( light );

					if ( cos_theta <= 0.0 )
					{
						intensity = (int) (lightMax * lightAmbient);
					}
					else
					{
						temp.set( surf_norm );
						temp.scale( (float) cos_theta );
						temp.normalize( );
						temp.sub( light );
						temp.normalize( );

						cos_alpha = view.dot( temp );

						intensity = (int) (lightMax * ( lightAmbient + lightDiffuse * cos_theta + lightSpecular * 
							Math.pow( cos_alpha, lightGlossiness )));
					}
				}
			}
		}

		return intensity;
	}

	public void drawFacet( Graphics graphics, GeometryArray geometryArray, int index, Point3d[] pointArray, int numPoints )
	{
		int intensity = computeIntensity( geometryArray, index, numPoints );

		if ( drawBackface || intensity >= 1 )
		{       
			for( int n = 0; n < numPoints; n++ )
			{
				xCoordArray[n] = (int) pointArray[n].x;
				yCoordArray[n] = (int) pointArray[n].y;
			}

			graphics.setColor( new Color( intensity, intensity, intensity ) );
			graphics.drawPolygon( xCoordArray, yCoordArray, numPoints );
		}
	}


	public void drawPoint( Graphics graphics, GeometryUpdater updater, GeometryArray geometryArray, int index )
	{
		updater.update( graphics, this, geometryArray, index, frameNumber );
		geometryArray.getCoordinate( index, pointArray[0] );
		projectPoint( pointArray[0], projectedPointArray[0] );
		drawPoint( graphics, projectedPointArray );
	}

	public void drawPoint( Graphics graphics, Point3d[] pointArray )
	{
		graphics.drawRect( (int) pointArray[0].x, (int) pointArray[0].y, POINT_WIDTH, POINT_HEIGHT );
	}

	public void drawTriangle( Graphics graphics, GeometryUpdater updater, GeometryArray geometryArray, int index )
	{
		for( int n = 0; n < 3; n++ )
		{
			updater.update( graphics, this, geometryArray, (index+n), frameNumber );
			geometryArray.getCoordinate( (index+n), pointArray[n] );
		}

		for( int n = 0; n < 3; n++ )
			projectPoint( pointArray[n], projectedPointArray[n] );

		drawTriangle( graphics, geometryArray, index, projectedPointArray );
	}

	public void drawTriangle( Graphics graphics, GeometryArray geometryArray, int index, Point3d[] pointArray )
	{
		drawFacet( graphics, geometryArray, index, pointArray, 3 );
	}
}
