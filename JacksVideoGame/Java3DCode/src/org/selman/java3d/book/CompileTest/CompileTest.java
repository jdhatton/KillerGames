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

package org.selman.java3d.book.compiletest;

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
* Creates 1000 same-sized cubes and compiles the scene.
*/
public class CompileTest extends Java3dApplet
{
	private static int 			m_kWidth = 256;
	private static int 			m_kHeight = 256;

	public CompileTest( )
	{
		initJava3d( );
	}

	private BranchGroup createColorCubes( )
	{
		BranchGroup bg = new BranchGroup( );

		final int kNumCubes = 1000;

		for( int n = 0; n < kNumCubes; n++ )
		{
			ColorCube cube1 = new ColorCube( 1.0 );

			// switch off pickable attribute so we can compile
			cube1.setPickable( false );
			bg.addChild( cube1 );
		}

		bg.compile( );

		return bg;
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

		objRoot.addChild( objTrans );

		return objRoot;
	}

	public static void main( String[] args )
	{
		CompileTest compileTest = new CompileTest( );
		compileTest.saveCommandLineArguments( args );

		new MainFrame( compileTest, m_kWidth, m_kHeight );
	}
}
