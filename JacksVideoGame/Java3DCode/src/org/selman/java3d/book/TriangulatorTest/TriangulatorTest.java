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

package org.selman.java3d.book.triangulatortest;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;

import javax.media.j3d.*;
import javax.vecmath.*;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.geometry.Triangulator;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.geometry.Box;

import org.selman.java3d.book.common.*;

/**
* This example illustrates using the Java 3D Triangulator
* and NormalGenerator to triangulate a planar surface with a
* hole in it.
*/
public class TriangulatorTest extends Java3dApplet implements ActionListener
{
	private static int 			m_kWidth = 400;
	private static int 			m_kHeight = 400;

	private double[]				m_VertexArray = { 	1,1,0, //0
														0,3,0, //1
														1,5,0, //2
														2,4,0, //3
														4,5,0, //4
														3,3,0, //5
														4,2,0, //6
														4,0,0, //7
														3,0,0, //8
														2,1,0, //9
									// these are vertices for the hole
														1,3,0, //10
														2,3,0, //11
														3,2,0, //12
														3,1,0, //13
														2,2,0};//14

	public TriangulatorTest( )
	{
		initJava3d( );
	}

	public void actionPerformed( ActionEvent event )
	{
	}

	protected Background createBackground( )
	{
		return null;
	}

	protected BranchGroup createSceneBranchGroup( )
	{
		BranchGroup objRoot = super.createSceneBranchGroup( );

		TransformGroup objTrans = new TransformGroup( );
		objTrans.setCapability( TransformGroup.ALLOW_TRANSFORM_WRITE );

		Transform3D yAxis = new Transform3D( );
		Alpha rotationAlpha = new Alpha( -1, Alpha.INCREASING_ENABLE,
			0, 0,
			4000, 0, 0,
			0, 0, 0 );

		RotationInterpolator rotator = new RotationInterpolator( rotationAlpha, objTrans, yAxis, 0.0f, (float) Math.PI*2.0f );

		BoundingSphere bounds =	new BoundingSphere( new Point3d( 0.0,0.0,0.0 ), 100.0 );
		rotator.setSchedulingBounds( bounds );

		objTrans.addChild( rotator );

		// triangulate the polygon
		GeometryInfo gi = new GeometryInfo( GeometryInfo.POLYGON_ARRAY );

		gi.setCoordinates( m_VertexArray );

		int[] stripCountArray = {10,5};
		int[] countourCountArray = {stripCountArray.length};

		gi.setContourCounts( countourCountArray );
		gi.setStripCounts( stripCountArray );

		Triangulator triangulator = new Triangulator( );
		triangulator.triangulate( gi );

		NormalGenerator normalGenerator = new NormalGenerator( );
		normalGenerator.generateNormals( gi );

		// create an appearance
		Appearance ap = new Appearance( );

		// render as a wireframe
		PolygonAttributes polyAttrbutes = new PolygonAttributes( );
		polyAttrbutes.setPolygonMode( PolygonAttributes.POLYGON_LINE );
		polyAttrbutes.setCullFace( PolygonAttributes.CULL_NONE ) ;
		ap.setPolygonAttributes( polyAttrbutes );

		// add both a wireframe and a solid version
		// of the triangulated surface
		Shape3D shape1 = new Shape3D( gi.getGeometryArray( ), ap );
		Shape3D shape2 = new Shape3D( gi.getGeometryArray( ) );

		objTrans.addChild( shape1 );
		objTrans.addChild( shape2 );
		objRoot.addChild( objTrans );

		return objRoot;
	}


	public static void main( String[] args )
	{
		TriangulatorTest triTest = new TriangulatorTest( );
		triTest.saveCommandLineArguments( args );

		new MainFrame( triTest, m_kWidth, m_kHeight );
	}
}
