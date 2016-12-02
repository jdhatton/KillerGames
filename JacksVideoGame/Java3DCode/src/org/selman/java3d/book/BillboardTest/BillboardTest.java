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

package org.selman.java3d.book.billboardtest;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;

import javax.media.j3d.*;
import javax.vecmath.*;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.geometry.*;
import javax.media.j3d.Billboard;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;

import org.selman.java3d.book.common.*;

/**
* Creates a simple rotating scene that includes two text billboards,
* one created to ROTATE_ABOUT_AXIS the other ROTATE_ABOUT_POINT.
*/
public class BillboardTest extends Java3dApplet
{
	private static int 			m_kWidth = 400;
	private static int 			m_kHeight = 400;

	public BillboardTest( )
	{
		initJava3d( );
	}

	protected double getScale( )
	{
		return 0.08;
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

		objTrans.addChild( createBillboard( "AXIS - 0,1,0", 
			new Point3f( -40.0f, 40.0f, 0.0f ),
			Billboard.ROTATE_ABOUT_AXIS,
			new Point3f( 0.0f, 1.0f, 0.0f ),
			bounds ) );

		objTrans.addChild( createBillboard( "POINT - 10,0,0",
			new Point3f( 40.0f, 00.0f, 0.0f ),
			Billboard.ROTATE_ABOUT_POINT,
			new Point3f( 10.0f, 0.0f, 0.0f ),
			bounds ) );

		objTrans.addChild( new ColorCube( 20.0 ) );

		objRoot.addChild( objTrans );

		return objRoot;
	}


	private TransformGroup createBillboard( String szText, Point3f locationPoint, int nMode, Point3f billboardPoint, BoundingSphere bounds )
	{
		TransformGroup subTg = new TransformGroup( );
		subTg.setCapability( TransformGroup.ALLOW_TRANSFORM_WRITE );

		Font3D f3d = new Font3D( new Font( "SansSerif", Font.PLAIN, 10 ), new FontExtrusion( ) );
		Text3D label3D = new Text3D( f3d, szText, locationPoint );

		Appearance app = new Appearance( );

		Color3f black = new Color3f( 0.1f, 0.1f, 0.1f );
		Color3f objColor = new Color3f( 0.2f, 0.2f, 0.2f );

		app.setMaterial( new Material( objColor, black, objColor, black, 90.0f ) );
		Shape3D sh = new Shape3D( label3D, app );

		subTg.addChild( sh );

		Billboard billboard = new Billboard( subTg, nMode, billboardPoint );
		billboard.setSchedulingBounds( bounds );
		subTg.addChild( billboard );

		return subTg;
	}


	public static void main( String[] args )
	{
		BillboardTest billTest = new BillboardTest( );
		billTest.saveCommandLineArguments( args );

		new MainFrame( billTest, m_kWidth, m_kHeight );		
	}
}
