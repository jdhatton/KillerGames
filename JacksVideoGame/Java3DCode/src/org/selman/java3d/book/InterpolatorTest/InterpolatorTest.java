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

package org.selman.java3d.book.interpolatortest;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;

import javax.media.j3d.*;
import javax.vecmath.*;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.geometry.*;

import org.selman.java3d.book.common.*;

/**
* This example creates a range of Interpolators
* and switches between them using a SwitchInterpolator
*/
public class InterpolatorTest extends Java3dApplet
{
	private static int 			m_kWidth = 400;
	private static int 			m_kHeight = 400;

	public InterpolatorTest( )
	{
		initJava3d( );
	}

	// scale eveything up so that moving from 0 to 1
	// is significant
	protected double getScale( )
	{
		return 10.0;
	}

	// we want a black background
	protected Background createBackground( )
	{
		return null;
	}


	protected BranchGroup createSceneBranchGroup( )
	{
		BranchGroup objRoot = super.createSceneBranchGroup( );

		// create a root TG in case we need to scale the scene
		TransformGroup objTrans = new TransformGroup( );
		objTrans.setCapability( TransformGroup.ALLOW_TRANSFORM_WRITE );
		objTrans.setCapability( TransformGroup.ALLOW_TRANSFORM_READ );

		// create the Appearance for the Shape3D
		Appearance app = new Appearance( );

		// create a Material, modified by the ColorInterpolator
		Color3f objColor = new Color3f( 1.0f, 0.7f, 0.8f );
		Color3f black = new Color3f( 0.0f, 0.0f, 0.0f );
		Material mat = new Material( objColor, black, objColor, black, 80.0f );
		mat.setCapability( Material.ALLOW_COMPONENT_WRITE );
		app.setMaterial( mat );

		// create a TransparencyAttributes, modified by the TransparencyInterpolator
		TransparencyAttributes transparency = new TransparencyAttributes( );
		transparency.setCapability( TransparencyAttributes.ALLOW_VALUE_WRITE );
		transparency.setTransparencyMode( TransparencyAttributes.NICEST );
		app.setTransparencyAttributes( transparency );

		// create a Switch Node and set capabilities
		Switch switchNode = new Switch( );
		switchNode.setCapability( Switch.ALLOW_SWITCH_WRITE );

		// create a Alpha object for the Interpolators
		Alpha alpha = new Alpha( -1,
			Alpha.INCREASING_ENABLE | Alpha.DECREASING_ENABLE,
			500,
			100,
			5000,
			2000,
			1000,
			5000,
			2000,
			500 );

		// add each BG and Interpolator as a child of the Switch Node
		TransformGroup tg = createSharedGroup( app );
		switchNode.addChild( createBranchGroup( tg, new ColorInterpolator( alpha, app.getMaterial( ) ) ) );

		tg = createSharedGroup( app );		
		switchNode.addChild( createBranchGroup( tg, new PositionInterpolator( alpha, tg ) ) );

		tg = createSharedGroup( app );
		switchNode.addChild( createBranchGroup( tg, new RotationInterpolator( alpha, tg ) ) );

		tg = createSharedGroup( app );
		switchNode.addChild( createBranchGroup( tg, new ScaleInterpolator( alpha, tg ) ) );

		tg = createSharedGroup( app );
		switchNode.addChild( createBranchGroup( tg, new TransparencyInterpolator( alpha, app.getTransparencyAttributes( ), 0, 0.8f ) ) );

		// define the data for the RotPosScalePathInterpolator
		float[] knots = {0.0f, 0.1f, 0.2f, 0.3f, 0.4f, 0.6f, 0.8f, 0.9f, 1.0f};
		float[] scales = {0.2f, 0.5f, 0.8f, 2.3f, 5.4f, 0.6f, 0.4f, 0.2f, 0.1f};
		Quat4f[] quats = new Quat4f[9];
		Point3f[] positions = new Point3f[9];

		quats[0] = new Quat4f( 0.3f, 1.0f, 1.0f, 0.0f );
		quats[1] = new Quat4f( 1.0f, 0.0f, 0.0f, 0.3f );
		quats[2] = new Quat4f( 0.2f, 1.0f, 0.0f, 0.0f );
		quats[3] = new Quat4f( 0.0f, 0.2f, 1.0f, 0.0f );
		quats[4] = new Quat4f( 1.0f, 0.0f, 0.4f, 0.0f );
		quats[5] = new Quat4f( 0.0f, 1.0f, 1.0f, 0.2f );
		quats[6] = new Quat4f( 0.3f, 0.3f, 0.0f, 0.0f );
		quats[7] = new Quat4f( 1.0f, 0.0f, 1.0f, 1.0f );
		quats[8] = quats[0];

		positions[0]= new Point3f( 0.0f,  0.0f, -1.0f );
		positions[1]= new Point3f( 1.0f, -2.0f, -2.0f );
		positions[2]= new Point3f( -2.0f,  2.0f, -3.0f );
		positions[3]= new Point3f( 1.0f,  1.0f, -4.0f );
		positions[4]= new Point3f( -4.0f, -2.0f, -5.0f );
		positions[5]= new Point3f( 2.0f,  0.3f, -6.0f );
		positions[6]= new Point3f( -4.0f,  0.5f, -7.0f );
		positions[7]= new Point3f( 0.0f, -1.5f, -4.0f );
		positions[8]= positions[0];

		tg = createSharedGroup( app );

		// create the Interpolator
		RotPosScalePathInterpolator rotPosScalePathInterplator = new RotPosScalePathInterpolator( alpha,
			tg,
			new Transform3D( ),
			knots,
			quats,
			positions,
			scales );

		// add a BG for the Interpolator
		switchNode.addChild( createBranchGroup( tg, rotPosScalePathInterplator ) );

		// create a RandomAlpha object to control a SwitchInterpolator
		// to set the Switches active child node randomly
		RandomAlpha randomAlpha = new RandomAlpha( );

		// create the interpolator
		SwitchValueInterpolator switchInterpolator = new SwitchValueInterpolator( randomAlpha, switchNode );
		switchInterpolator.setSchedulingBounds( getApplicationBounds( ) );

		// connect the scenegraph
		objTrans.addChild( switchNode );
		objTrans.addChild( switchInterpolator );

		// Set up the global lights
		Color3f lColor1 = new Color3f( 0.7f, 0.7f, 0.7f );
		Vector3f lDir1  = new Vector3f( -1.0f, -1.0f, -1.0f );
		Color3f alColor = new Color3f( 0.2f, 0.2f, 0.2f );

		AmbientLight aLgt = new AmbientLight( alColor );
		aLgt.setInfluencingBounds( getApplicationBounds( ) );
		DirectionalLight lgt1 = new DirectionalLight( lColor1, lDir1 );
		lgt1.setInfluencingBounds( getApplicationBounds( ) );

		// add the lights
		objRoot.addChild( aLgt );
		objRoot.addChild( lgt1 );

		// connect
		objRoot.addChild( objTrans );

		return objRoot;
	}

	// creates a BG containing:
	//  - a Link to the SharedGroup
	//  - The Interpolator
	//  - a Text2D label of the name of the Interpolator
	private BranchGroup createBranchGroup( TransformGroup bgShared, Interpolator interpolator )
	{
		BranchGroup bg = new BranchGroup( );
		bg.addChild( bgShared );
		bg.addChild( interpolator );
		interpolator.setSchedulingBounds( getApplicationBounds( ) );

		// strip the package name from szClass (everything before the final ".")
		String szClass = interpolator.getClass( ).getName( );
		int nIndex = szClass.lastIndexOf( "." );

		String szTrimedClass = szClass;

		if( nIndex > -1 )
			szTrimedClass = szClass.substring( nIndex+1, szClass.length( ) );

		Text2D text = new Text2D( szTrimedClass, 
			new Color3f( 1,1,1 ), 
			"SansSerif", 20, Font.PLAIN );
		bg.addChild( text );

		return bg;
	}

	// create a SharedGroup and populate with some child nodes	
	private TransformGroup createSharedGroup( Appearance app )
	{
		TransformGroup tg = new TransformGroup( );
		tg.setCapability( TransformGroup.ALLOW_TRANSFORM_WRITE );
		tg.setCapability( TransformGroup.ALLOW_TRANSFORM_READ );

		tg.addChild( createGeometry( app, -0.5,-0.5,0 ) );
		tg.addChild( createGeometry( app, -0.5,0.5,0 ) );
		tg.addChild( createGeometry( app, 0.5,0.5,0 ) );
		tg.addChild( createGeometry( app, 0.5,-0.5,0 ) );

		return tg;
	}

	// create and position a Sphere using a parent TransformGroup
	TransformGroup createGeometry( Appearance app, double x, double y, double z )
	{
		Transform3D t3d = new Transform3D( );
		t3d.setTranslation( new Vector3d( x, y, z ) );
		TransformGroup tg = new TransformGroup( t3d );

		tg.addChild( new Sphere( 0.1f, Primitive.GENERATE_NORMALS, app ) );
		return tg;
	}	

	public static void main( String[] args )
	{
		InterpolatorTest interpolatorTest = new InterpolatorTest( );
		interpolatorTest.saveCommandLineArguments( args );

		new MainFrame( interpolatorTest, m_kWidth, m_kHeight );
	}
}
