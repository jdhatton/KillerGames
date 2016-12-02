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

package org.selman.java3d.book.text3dtest;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;

import javax.media.j3d.*;
import javax.vecmath.*;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.geometry.*;

import org.selman.java3d.book.common.*;

/**
* Renders a Java 3D 3D Text objects with a 
* custom extrusion.
*/
public class Text3DTest extends Java3dApplet
{
	private static int 			m_kWidth = 400;
	private static int 			m_kHeight = 400;

	public Text3DTest( )
	{
		initJava3d( );
	}

	protected double getScale( )
	{
		return 0.2;
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
			4000, 0, 4000,
			0, 0, 0 );

		TornadoRotation rotator = new TornadoRotation( rotationAlpha, objTrans, yAxis, 0.0f, (float) Math.PI*2.0f );
		rotator.setSchedulingBounds( bounds );
		objTrans.addChild( rotator );

		objTrans.addChild( createText3D( rotator, "setString", 1, 10.0f, 6.0f, Text3D.PATH_RIGHT ) );
		objRoot.addChild( objTrans );

		return objRoot;
	}

	TransformGroup createText3D( TornadoRotation rotator, String szText, int nSize, float scale, float trans, int nPath )
	{
		TransformGroup tg = new TransformGroup( );
		Transform3D t3d = new Transform3D( );
		t3d.setScale( scale );
		t3d.setTranslation( new Vector3d( 0.0, trans, -10.0 ) );
		tg.setTransform( t3d );

		// use a customized FontExtrusion object to control the depth of the text
		double X1 = 0;
		double Y1 = 0;
		double X2 = 3;
		double Y2 = 0;		
		Shape extrusionShape = new java.awt.geom.Line2D.Double( X1, Y1, X2, Y2 );

		FontExtrusion fontEx = new FontExtrusion( extrusionShape ) ;

		Font3D f3d = new Font3D( new Font( "TimesRoman", Font.PLAIN, nSize ), fontEx );

		TornadoText3D text3D = new TornadoText3D( f3d, szText, new Point3f( 0.0f, 0.0f, 0.0f ), Text3D.ALIGN_CENTER, nPath );

		rotator.addTornadoText3D( text3D );

		// create an appearance
		Color3f black = new Color3f( 0.1f, 0.1f, 0.1f );
		Color3f objColor = new Color3f( 0.2f, 0.2f, 0.2f );

		Appearance app = new Appearance( );
		app.setMaterial( new Material( objColor, black, objColor, black, 90.0f ) );

		// render as a wireframe
		PolygonAttributes polyAttrbutes = new PolygonAttributes( );
		polyAttrbutes.setPolygonMode( PolygonAttributes.POLYGON_LINE );
		polyAttrbutes.setCullFace( PolygonAttributes.CULL_NONE ) ;
		app.setPolygonAttributes( polyAttrbutes );

		tg.addChild( new Shape3D( text3D, app ) );
		return tg;
	}

	public static void main( String[] args )
	{
		Text3DTest text3DTest = new Text3DTest( );
		text3DTest.saveCommandLineArguments( args );

		new MainFrame( text3DTest, m_kWidth, m_kHeight );
	}
}
