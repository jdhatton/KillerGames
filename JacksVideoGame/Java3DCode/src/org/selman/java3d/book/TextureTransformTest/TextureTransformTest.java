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

package org.selman.java3d.book.texturetransformtest;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;

import javax.media.j3d.*;
import javax.vecmath.*;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.image.*;
import com.sun.j3d.utils.behaviors.mouse.*;

import org.selman.java3d.book.common.*;

/**
* This example illustrates how a texture image can be 
* dynamically rotated at runtime. A MouseRotate behavior
* (use the left mouse button) controls the rotation of the 
* applied texture image
*/
public class TextureTransformTest extends Java3dApplet implements MouseBehaviorCallback
{
	private static int 			m_kWidth = 400;
	private static int 			m_kHeight = 400;

	TextureAttributes			m_TextureAttributes = null;

	public TextureTransformTest( )
	{
		initJava3d( );
	}

	protected double getScale( )
	{
		return 0.08;
	}

	// create a Box with an applied Texture image and
	// a RotationInterpolator to rotate the box
	protected BranchGroup createSceneBranchGroup( )
	{
		BranchGroup objRoot = super.createSceneBranchGroup( );

		TransformGroup objTrans = new TransformGroup( );
		objTrans.setCapability( TransformGroup.ALLOW_TRANSFORM_WRITE );
		objTrans.setCapability( TransformGroup.ALLOW_TRANSFORM_READ );

		Transform3D yAxis = new Transform3D( );
		Alpha rotationAlpha = new Alpha( -1, Alpha.INCREASING_ENABLE,
			0, 0,
			4000, 0, 0,
			0, 0, 0 );

		// create the rotation interpolator to rotate the scene
		RotationInterpolator rotator = new RotationInterpolator( rotationAlpha, objTrans, yAxis, 0.0f, (float) Math.PI*2.0f );
		rotator.setSchedulingBounds( createApplicationBounds( ) );
		objTrans.addChild( rotator );

		// create the box
		final int nScale = 50;
		Appearance app = new Appearance( );
		Box box = new Box( nScale,nScale,nScale, Primitive.GENERATE_NORMALS | Primitive.GENERATE_TEXTURE_COORDS, app );

		// load the texture image
		TextureLoader texLoader = new TextureLoader( "texture.gif", this );
		app.setTexture( texLoader.getTexture( ) );

		// set the texture attributes and ensure we
		// can write to the Transform for the texture attributes
		m_TextureAttributes = new TextureAttributes( );
		m_TextureAttributes.setCapability( TextureAttributes.ALLOW_TRANSFORM_WRITE );
		app.setTextureAttributes( m_TextureAttributes );

		// connect all the elements
		objTrans.addChild( box );				
		objRoot.addChild( objTrans );
		objRoot.addChild( createRotator( ) );

		return objRoot;
	}


	private TransformGroup createRotator( )
	{
		// create a ColorCube to illustrate the current rotation
		TransformGroup transTg = new TransformGroup( );
		Transform3D t3d = new Transform3D( );
		t3d.setTranslation( new Vector3d( -70, -70, 50 ) );
		transTg.setTransform( t3d );

		TransformGroup subTg = new TransformGroup( );
		subTg.setCapability( TransformGroup.ALLOW_TRANSFORM_WRITE );

		subTg.addChild( new ColorCube( 10.0 ) );

		// attach a MouseRotate behavior so we can
		// rotate the color cube with the left mouse button
		MouseRotate mouseRot = new MouseRotate( subTg );
		subTg.addChild( mouseRot );

		// assign a transformChanged callback as we want to be
		// notified whenever the rotation of the ColorCube
		// changed ("this" implements MouseBehaviorCallback );
		mouseRot.setupCallback( this );
		mouseRot.setSchedulingBounds( getApplicationBounds( ) );

		transTg.addChild( subTg );

		return transTg;
	}


	// this is a callback method that the MouseRotate behavior
	// calls when its Transform3D has been modified (by the user)
	public void transformChanged( int type, Transform3D transform )
	{
		// update the rotation of the TextureAttributes		
		m_TextureAttributes.setTextureTransform( transform );
	}

	public static void main( String[] args )
	{
		TextureTransformTest textureTest = new TextureTransformTest( );
		textureTest.saveCommandLineArguments( args );

		new MainFrame( textureTest, m_kWidth, m_kHeight );
	}
}
