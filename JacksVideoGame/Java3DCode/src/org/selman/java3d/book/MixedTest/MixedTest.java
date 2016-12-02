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

package org.selman.java3d.book.mixedtest;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;

import javax.media.j3d.*;
import javax.vecmath.*;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.geometry.*;

import org.selman.java3d.book.common.*;

/**
* This example mixes rendering in immediate and
* retained (scenegraph) mode to produce a 
* composite rendered frame.
*/
public class MixedTest extends Java3dApplet
{
	private static int 			m_kWidth = 400;
	private static int 			m_kHeight = 400;

	public MixedTest( )
	{
		initJava3d( );
	}

	protected Canvas3D createCanvas3D( )
	{
		// overidden this method to create a custom
		// Canvas3D that will implement the Immediate Mode rendering
		GraphicsConfigTemplate3D gc3D = new GraphicsConfigTemplate3D( );
		gc3D.setSceneAntialiasing( GraphicsConfigTemplate.PREFERRED );
		GraphicsDevice gd[] = GraphicsEnvironment.getLocalGraphicsEnvironment( ).getScreenDevices( );

		ImmediateCanvas3D c3d = new ImmediateCanvas3D( gd[0].getBestConfiguration( gc3D ) );
		c3d.setSize( getCanvas3dWidth( c3d ), getCanvas3dHeight( c3d ) );

		return ( Canvas3D ) c3d;
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

		objTrans.addChild( new ColorCube( ) );
		objRoot.addChild( objTrans );

		return objRoot;
	}


	public static void main( String[] args )
	{
		MixedTest mixedTest = new MixedTest( );
		mixedTest.saveCommandLineArguments( args );

		new MainFrame( mixedTest, m_kWidth, m_kHeight );
	}
}
