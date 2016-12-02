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

package org.selman.java3d.book.boundstest;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;

import javax.media.j3d.*;
import javax.vecmath.*;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.geometry.*;
import javax.media.j3d.Billboard;

import org.selman.java3d.book.common.*;

/**
* Test harness to investigate the Bounds objects
* for a Java 3D scenegraph.
*/
public class BoundsTest extends Java3dApplet
{
	private static int 			m_kWidth = 256;
	private static int 			m_kHeight = 256;

	public BoundsTest( )
	{
		initJava3d( );
	}

	protected double getScale( )
	{
		return 0.5;
	}


	Group createColorCubes( )
	{
		Group group = new Group( );

		// defaults
		ColorCube cube1 = new ColorCube( 1.0 );
		group.addChild( cube1 );

		// explicitly set the bounds (box)
		ColorCube cube2 = new ColorCube( 2.0 );
		cube2.setBoundsAutoCompute( false );
		Bounds bounds = new BoundingBox( new Point3d( -2, -2, -2 ), new Point3d( 2, 2, 2 ) );
		cube2.setBounds( bounds );
		cube2.setCollisionBounds( bounds );		
		group.addChild( cube2 );

		// (sphere)
		ColorCube cube3 = new ColorCube( 4.0 );
		cube3.setBoundsAutoCompute( false );
		bounds = new BoundingSphere( new Point3d( 0, 0, 0 ), 4 );
		cube3.setBounds( bounds );
		cube3.setCollisionBounds( bounds );
		group.addChild( cube3 );

		// auto compute, manual collision
		ColorCube cube4 = new ColorCube( 6.0 );
		cube4.setBoundsAutoCompute( true );
		bounds = new BoundingBox( new Point3d( -10, -10, -10 ), new Point3d( 10, 10, 10 ) );
		cube4.setCollisionBounds( bounds );
		group.addChild( cube4 );

		// auto compute both
		ColorCube cube5 = new ColorCube( 6.0 );
		cube5.setBoundsAutoCompute( true );
		group.addChild( cube5 );

		return group;
	}


	protected Group createPoints( )
	{
		Group group = new Group( );

		final int kNumPoints = 200;
		final double kRadius = 10.0;
		Point3d points[] = new Point3d[kNumPoints];

		for( int n = 0; n < kNumPoints; n++ )
		{
			double randX = (java.lang.Math.random( ) * kRadius ) - kRadius/2;
			double randY = (java.lang.Math.random( ) * kRadius ) - kRadius/2;
			double randZ = (java.lang.Math.random( ) * kRadius ) - kRadius/2;

			points[n] = new Point3d( randX, randY, randZ );
		}

		PointArray pointArray = new PointArray( points.length, GeometryArray.COLOR_4 | GeometryArray.COORDINATES );
		pointArray.setCoordinates( 0, points );
		Shape3D shapePoints = new Shape3D( pointArray, new Appearance( ) );

		group.addChild( shapePoints );
		return group;
	}

	protected BranchGroup createSceneBranchGroup( )
	{
		BranchGroup objRoot = super.createSceneBranchGroup( );

		// do NOT auto compute bounds for this node
		objRoot.setBoundsAutoCompute( false );

		TransformGroup objTrans = new TransformGroup( );
		objTrans.setCapability( TransformGroup.ALLOW_TRANSFORM_WRITE );
		objTrans.setCapability( TransformGroup.ALLOW_TRANSFORM_READ );

		Transform3D yAxis = new Transform3D( );
		Alpha rotationAlpha = new Alpha( -1, Alpha.INCREASING_ENABLE,
			0, 0,
			4000, 0, 0,
			0, 0, 0 );

		RotationInterpolator rotator = new RotationInterpolator( rotationAlpha, objTrans, yAxis, 0.0f, (float) Math.PI*2.0f );

		rotator.setSchedulingBounds( createApplicationBounds( ) );
		objTrans.addChild( rotator );

		objTrans.addChild( createColorCubes( ) );
		objTrans.addChild( createPoints( ) );

		objRoot.addChild( objTrans );

		return objRoot;
	}

	public static void main( String[] args )
	{
		BoundsTest boundsTest = new BoundsTest( );
		boundsTest.saveCommandLineArguments( args );

		new MainFrame( boundsTest, m_kWidth, m_kHeight );
	}
}
