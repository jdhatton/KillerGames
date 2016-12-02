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

package org.selman.java3d.book.cuboidtest;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;

import javax.media.j3d.*;
import javax.vecmath.*;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.geometry.*;

import org.selman.java3d.book.common.*;

/**
* Creates a scene using the Java 3D Box and a custom Cuboid
* implementation that uses Quads for sides instead of TriArrays
*/
class CuboidTest extends Java3dApplet implements ActionListener
{
	private static int 			m_kWidth = 400;
	private static int 			m_kHeight = 400;

	public CuboidTest( )
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

	protected double getScale( )
	{
		return 0.1;
	}

	protected BranchGroup createSceneBranchGroup( )
	{
		BranchGroup objRoot = super.createSceneBranchGroup( );

		TransformGroup objTrans = new TransformGroup( );
		objTrans.setCapability( TransformGroup.ALLOW_TRANSFORM_WRITE );
		objTrans.setCapability( TransformGroup.ALLOW_TRANSFORM_READ );

		BoundingSphere bounds =	new BoundingSphere( new Point3d( 0.0,0.0,0.0 ), 100.0 );

		Transform3D yAxis = new Transform3D( );
		Alpha rotationAlpha = new Alpha( -1, Alpha.INCREASING_ENABLE,
			0, 0,
			4000, 0, 0,
			0, 0, 0 );

		RotationInterpolator rotator = new RotationInterpolator( rotationAlpha, objTrans, yAxis, 0.0f, (float) Math.PI*2.0f );		
		rotator.setSchedulingBounds( bounds );
		objTrans.addChild( rotator );

		// create an appearance
		Appearance ap = new Appearance( );

		// render as a wireframe
		PolygonAttributes polyAttrbutes = new PolygonAttributes( );
		polyAttrbutes.setPolygonMode( PolygonAttributes.POLYGON_LINE );
		polyAttrbutes.setCullFace( PolygonAttributes.CULL_NONE ) ;
		ap.setPolygonAttributes( polyAttrbutes );

		objTrans.addChild( new Cuboid( 50,30,20, ap ) );
		objTrans.addChild( new Box( 25,15,10, ap ) );

		objRoot.addChild( objTrans );

		return objRoot;
	}

	public static void main( String[] args )
	{
		CuboidTest cuboidTest = new CuboidTest( );
		cuboidTest.saveCommandLineArguments( args );

		new MainFrame( cuboidTest, m_kWidth, m_kHeight );
	}
}
