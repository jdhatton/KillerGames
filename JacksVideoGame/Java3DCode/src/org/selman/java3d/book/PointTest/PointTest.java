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

package org.selman.java3d.book.pointtest;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;

import javax.media.j3d.*;
import javax.vecmath.*;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.geometry.*;

import org.selman.java3d.book.common.*;

/**
* Illustrates rendering Java 3D points in a variety of styles.
*/
public class PointTest extends Java3dApplet
{
	private static int 			m_kWidth = 400;
	private static int 			m_kHeight = 400;

	public PointTest( )
	{
		initJava3d( );
	}

	protected double getScale( )
	{
		return 1.0;
	}

	// overridden to use a black background
	// so we can see the points better
	protected Background createBackground( )
	{
		return null;
	}

	protected BranchGroup createSceneBranchGroup( )
	{
		BranchGroup objRoot = super.createSceneBranchGroup( );

		TransformGroup objTrans = new TransformGroup( );
		objTrans.setCapability( TransformGroup.ALLOW_TRANSFORM_WRITE );
		objTrans.setCapability( TransformGroup.ALLOW_TRANSFORM_READ );

		Transform3D yAxis = new Transform3D( );
		Alpha rotationAlpha = new Alpha( -1, Alpha.INCREASING_ENABLE,
			0, 0,
			14000, 0, 0,
			0, 0, 0 );

		RotationInterpolator rotator = new RotationInterpolator( rotationAlpha, objTrans, yAxis, 0.0f, (float) Math.PI*2.0f );		
		rotator.setSchedulingBounds( getApplicationBounds( ) );
		objTrans.addChild( rotator );

		Switch switchGroup = new Switch( );
		switchGroup.setCapability( Switch.ALLOW_SWITCH_WRITE );


		switchGroup.addChild( createPoints( 1, 5, false ) );
		switchGroup.addChild( createPoints( 1, 5, true ) );
		switchGroup.addChild( createPoints( 8, 10, false ) );
		switchGroup.addChild( createPoints( 8, 10, true ) );

		switchGroup.addChild( createPoints( 2, 5, false ) );
		switchGroup.addChild( createPoints( 2, 5, true ) );
		switchGroup.addChild( createPoints( 2, 20, false ) );
		switchGroup.addChild( createPoints( 2, 20, true ) );

		// create a SwitchValueInterpolator to
		// cycle through the child nodes in the Switch Node
		Alpha switchAlpha = new Alpha( -1, Alpha.INCREASING_ENABLE,
			0, 0,
			15000, 0, 0,
			0, 0, 0 );

		SwitchValueInterpolator switchInterpolator = new SwitchValueInterpolator( switchAlpha, switchGroup );
		switchInterpolator.setSchedulingBounds( getApplicationBounds( ) );
		switchInterpolator.setEnable( true );

		// WARNING: do not add the SwitchValueInterpolator to the Switch Node!
		objRoot.addChild( switchInterpolator );

		objTrans.addChild( switchGroup );
		objRoot.addChild( objTrans );

		return objRoot;
	}


	private BranchGroup createPoints( final int nPointSize, final int nNumPoints, boolean bAliased )
	{
		BranchGroup bg = new BranchGroup( );

		String szText = new String( );
		szText += ( nNumPoints + "X, Size:" + nPointSize + ", aliased: " + bAliased );

		Font3D f3d = new Font3D( new Font( "SansSerif", Font.PLAIN, 1 ), new FontExtrusion( ) );
		Text3D label3D = new Text3D( f3d, szText, new Point3f( -5,0,0 ) );
		Shape3D sh = new Shape3D( label3D );

		bg.addChild( sh );

		PointArray pointArray = new PointArray( nNumPoints * nNumPoints, GeometryArray.COORDINATES | GeometryArray.COLOR_3 );

		// create the PointArray that we will be rendering
		int nPoint = 0;
		final double factor = 1.0 / nNumPoints;

		for( int n = 0; n < nNumPoints; n++ )
		{
			for( int i = 0; i < nNumPoints; i++ )
			{
				Point3f point = new Point3f( n - nNumPoints/2, i - nNumPoints/2, 0.0f );
				pointArray.setCoordinate( nPoint, point );
				pointArray.setColor( nPoint++, new Color3f( 0.5f,
					(float) (n * factor),
					(float) (i * factor) ) );
			}
		}

		// create the material for the points
		Appearance pointApp = new Appearance( );

		// enlarge the points
		pointApp.setPointAttributes( new PointAttributes( nPointSize, bAliased ) );

		Shape3D pointShape = new Shape3D( pointArray, pointApp );

		bg.addChild( pointShape );				
		return bg;
	}


	public static void main( String[] args )
	{
		PointTest pointTest = new PointTest( );
		pointTest.saveCommandLineArguments( args );

		new MainFrame( pointTest, m_kWidth, m_kHeight );
	}
}
