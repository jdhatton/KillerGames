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

package org.selman.java3d.book.scenegraphtest;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;

import javax.media.j3d.*;
import javax.vecmath.*;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.geometry.*;

import org.selman.java3d.book.common.*;

/**
* This example builds a basic hierarchical model of the
* top part of a human torso (2 arms, neck and head).
* the arms are built using a hierarchy of TransformGroups
* with Cylinders used for the geometry itself.
* RotationInterpolators are used to crudely animate the model.
*/
public class ScenegraphTest extends Java3dApplet
{
	private static int 			m_kWidth = 400;
	private static int 			m_kHeight = 400;

	public ScenegraphTest( )
	{
		initJava3d( );
	}

	protected double getScale( )
	{
		return 8.0;
	}


	protected BranchGroup createSceneBranchGroup( )
	{
		BranchGroup objRoot = super.createSceneBranchGroup( );

		// create some lights for the scene
		Color3f lColor1 = new Color3f( 0.7f, 0.7f, 0.7f );
		Vector3f lDir1  = new Vector3f( -1.0f, -1.0f, -1.0f );
		Color3f alColor = new Color3f( 0.2f, 0.2f, 0.2f );

		AmbientLight aLgt = new AmbientLight( alColor );
		aLgt.setInfluencingBounds( createApplicationBounds( ) );
		DirectionalLight lgt1 = new DirectionalLight( lColor1, lDir1 );
		lgt1.setInfluencingBounds( createApplicationBounds( ) );
		objRoot.addChild( aLgt );
		objRoot.addChild( lgt1 );

		// create a rotator to spin the whole model around the Y axis
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

		// build the model itself using helper methods
		addHead( objTrans );
		objTrans.addChild( createArm( 0, 0, -Math.PI * 0.5 ) );
		objTrans.addChild( createArm( 0, Math.PI, Math.PI * 0.5 ) );

		objRoot.addChild( objTrans );

		return objRoot;
	}

	private void addHead( Group parentGroup )
	{
		// add a cylinder for the Neck
		TransformGroup tgNeck = addLimb( parentGroup, "Neck", 0.05, 0.2, 0.0, 0.0 );

		Appearance app = new Appearance( );
		Color3f black = new Color3f( 0.4f, 0.2f, 0.1f );
		Color3f objColor = new Color3f( 1, 0.8f, 0.6f );
		app.setMaterial( new Material( objColor, black, objColor, black, 90.0f ) );

		// position a Sphere for the head itself
		Sphere headSphere = new Sphere( (float) 0.12, Primitive.GENERATE_NORMALS, app );
		tgNeck.addChild( headSphere );
	}

	// creates a hierarchical model of a basic arm.
	// the arm is aligned using the three rotations: rotX, rotY, rotZ.
	// by rotating the arm we are able to create a left and a right arm
	// from the same code.
	private TransformGroup createArm( double rotX, double rotY, double rotZ )
	{
		TransformGroup tgShoulder = new TransformGroup( );
		Transform3D t3d = new Transform3D( );
		t3d.setEuler( new Vector3d( rotX, rotY, rotZ ) );
		tgShoulder.setTransform( t3d );

		TransformGroup tgParent = null;

		tgParent = addLimb( tgShoulder, "Upper Arm", 0.05, 0.5, 0.0, 0.0 );
		tgParent = addLimb( tgParent, "Lower Arm", 0.03, 0.4, Math.PI * -0.5, Math.PI * 0.8 );

		TransformGroup tgWrist = addLimb( tgParent, "Wrist", 0.03, 0.07, 0.0, Math.PI * 0.5 );
		addLimb( tgWrist, "Thumb", 0.01, 0.05, 0.0, Math.PI * 0.5 );
		addLimb( tgWrist, "Finger 1", 0.01, 0.08, 0.0, Math.PI * 0.3 );
		addLimb( tgWrist, "Finger 2", 0.01, 0.10, 0.0, Math.PI * 0.3 );
		addLimb( tgWrist, "Finger 3", 0.01, 0.08, 0.0, Math.PI * 0.3 );
		addLimb( tgWrist, "Finger 4", 0.01, 0.05, 0.0, Math.PI * 0.3 );

		return tgShoulder;
	}

	// adds a cylinder for a limb to the parentGroup.
	// optionally adds a rotator to rotate the limb between rotMin and rotMax radians.
	private TransformGroup addLimb( Group parentGroup, String szName, double radius, double length, double rotMin, double rotMax )
	{
		// create the rotator
		TransformGroup tgJoint = new TransformGroup( );
		tgJoint.setCapability( TransformGroup.ALLOW_TRANSFORM_WRITE );
		tgJoint.setCapability( TransformGroup.ALLOW_TRANSFORM_READ );

		// add a rotator if necessary
		if( rotMin != rotMax )
		{	
			Transform3D xAxis = new Transform3D( );
			xAxis.rotX( Math.PI/2.0 );
			Alpha rotationAlpha = new Alpha( -1, Alpha.INCREASING_ENABLE,
				0, 0,
				4000, 0, 0,
				0, 0, 0 );

			RotationInterpolator rotator = new RotationInterpolator( rotationAlpha, tgJoint, xAxis, (float) rotMin, (float) rotMax );
			rotator.setSchedulingBounds( createApplicationBounds( ) );
			tgJoint.addChild( rotator );
		}

		// create a cylinder using length and radius
		tgJoint.addChild( createLimb( radius, length ) );

		// create the joint (the *next* TG should 
		// be offset by the length of this limb)
		TransformGroup tgOffset = new TransformGroup( );

		Transform3D t3d = new Transform3D( );
		t3d.setTranslation( new Vector3d( 0, length, 0 ) );
		tgOffset.setTransform( t3d );

		tgJoint.addChild( tgOffset );
		parentGroup.addChild( tgJoint );

		// return the offset TG, so any child TG's will be added 
		// in the correct position.
		return tgOffset;
	}

	// creates and aligns a cylinder to represent a simple limb.
	TransformGroup createLimb( double radius, double length )
	{
		// because the cylinder is centered at 0,0,0 
		// we need to shift the cylinder so that the bottom of
		// the cylinder is at 0,0,0 and the top is at 0, length, 0

		TransformGroup tg = new TransformGroup( );
		Transform3D t3d = new Transform3D( );
		t3d.setTranslation( new Vector3d( 0, length/2, 0 ) );
		tg.setTransform( t3d );

		Appearance app = new Appearance( );
		Color3f black = new Color3f( 0.4f, 0.2f, 0.1f );
		Color3f objColor = new Color3f( 1, 0.8f, 0.6f );
		app.setMaterial( new Material( objColor, black, objColor, black, 90.0f ) );

		Cylinder cylinder = new Cylinder( (float) radius, (float) length, Primitive.GENERATE_NORMALS, app );

		tg.addChild( cylinder );
		return tg;
	}


	public static void main( String[] args )
	{
		ScenegraphTest sceneTest = new ScenegraphTest( );
		sceneTest.saveCommandLineArguments( args );

		new MainFrame( sceneTest, m_kWidth, m_kHeight );
	}
}
