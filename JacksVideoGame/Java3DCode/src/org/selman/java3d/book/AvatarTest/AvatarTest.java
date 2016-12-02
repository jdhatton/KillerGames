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

package org.selman.java3d.book.avatartest;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.net.*;
import java.io.*;

import javax.media.j3d.*;
import javax.vecmath.*;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.image.TextureLoader;

import org.selman.java3d.book.common.*;
import com.tornadolabs.j3dtree.*;

import com.sun.j3d.audioengines.javasound.JavaSoundMixer;

/**
* Displays a simple driving type game scene, using texture
* mapped cubes. It assigns an Avatar to the viewer and 
* incorporates simple sounds and collision 
* detection/notification/
* <p>
* This example does not use the Java3dApplet base class
* but is based on a SimpleUniverse construction instead.
* that way we can illustrate the setPlatformGeometry call.
*/
public class AvatarTest extends Applet
{

	public BranchGroup createSceneGraph( )
	{
		BranchGroup bg = new BranchGroup( );

		TransformGroup tgRoot = addBehaviors( bg );

		createBuildings( tgRoot );
		createRoad( tgRoot );
		createLand( tgRoot );
		createCars( tgRoot );
		createBackground( bg );

		return bg;
	}

	public void createBackground( Group bg )
	{
		// add the sky backdrop
		Background back = new Background( );
		back.setApplicationBounds( getBoundingSphere( ) );
		bg.addChild( back );

		BranchGroup bgGeometry = new BranchGroup( );

		// create an appearance and assign the texture image
		Appearance app = new Appearance( );				
		Texture tex = new TextureLoader( "back.jpg", this ).getTexture( );
		app.setTexture( tex );

		Sphere sphere = new Sphere( 1.0f, Primitive.GENERATE_TEXTURE_COORDS | Primitive.GENERATE_NORMALS_INWARD, app );

		bgGeometry.addChild( sphere );
		back.setGeometry( bgGeometry );
	}


	public Group createLand( Group g )
	{	
		Land land = new Land( this, g, ComplexObject.GEOMETRY | ComplexObject.TEXTURE );
		return land.createObject( new Appearance( ), new Vector3d( 0,0,0 ), new Vector3d( 1,1,1 ), "land.jpg", null, null );
	}

	public Group createRoad( Group g )
	{	
		Road road = new Road( this, g, ComplexObject.GEOMETRY | ComplexObject.TEXTURE );
		return road.createObject( new Appearance( ), new Vector3d( 0,0,0 ), new Vector3d( 1,1,1 ), "road.jpg", null, null );
	}


	private float getRandomNumber( float basis, float random )
	{
		return basis + ( (float) Math.random( ) * random * 2 ) - (random);
	}

	public Group createBuildings( Group g )
	{
		BranchGroup bg = new BranchGroup( );

		for( int n = (int) Road.ROAD_LENGTH; n < 0; n = n + 10 )
		{
			Building building = new Building( this, bg, ComplexObject.GEOMETRY | ComplexObject.TEXTURE | ComplexObject.COLLISION );

			building.createObject( new Appearance( ), 
				new Vector3d( getRandomNumber( -4.0f, 0.25f ),
				getRandomNumber( 1.0f, 0.5f ),
				getRandomNumber( n, 0.5f ) ),
				new Vector3d( 1,1,1 ),
				"house.jpg",
				null,
				null );

			building = new Building( this, bg, ComplexObject.GEOMETRY | ComplexObject.TEXTURE | ComplexObject.COLLISION );

			building.createObject( new Appearance( ),
				new Vector3d( getRandomNumber( 4.0f, 0.25f ),
				getRandomNumber( 1.0f, 0.5f ),
				getRandomNumber( n, 0.5f ) ),
				new Vector3d( 1,1,1 ),
				"house.jpg",
				null,
				null );

		}

		g.addChild( bg );

		return bg;
	}


	public Group createCars( Group g )
	{
		BranchGroup bg = new BranchGroup( );

		for( int n = (int) Road.ROAD_LENGTH; n < 0; n = n + 10 )
		{
			Car car = new Car( this, bg, 	ComplexObject.GEOMETRY | 
				ComplexObject.TEXTURE | 
				ComplexObject.SOUND );

			car.createObject( new Appearance( ),
				new Vector3d( getRandomNumber( 0.0f, 2.0f ),
				Car.CAR_HEIGHT/2.0f,
				getRandomNumber( n, 5.0f ) ),
				new Vector3d( 1,1,1 ),
				"car0.jpg",
				"car.wav",
				"collide.wav" );
		}

		g.addChild( bg );
		return bg;
	}

	public TransformGroup addBehaviors( Group bgRoot )
	{
		// Create the transform group node and initialize it to the
		// identity.  Enable the TRANSFORM_WRITE capability so that
		// our behavior code can modify it at runtime.  Add it to the
		// root of the subgraph.
		TransformGroup objTrans = new TransformGroup( );
		objTrans.setCapability( TransformGroup.ALLOW_TRANSFORM_WRITE );

		Transform3D zAxis = new Transform3D( );
		zAxis.rotY( Math.toRadians( 90.0 ) );

		Alpha zoomAlpha = new Alpha( -1, Alpha.INCREASING_ENABLE,
			0, 0,
			20000, 0, 0,
			0, 0, 0 );

		PositionInterpolator posInt = new PositionInterpolator( zoomAlpha, objTrans, zAxis, 0, -160 );

		posInt.setSchedulingBounds( getBoundingSphere( ) );
		objTrans.addChild( posInt );

		bgRoot.addChild( objTrans );

		return objTrans;
	}

	BoundingSphere getBoundingSphere( )
	{
		return new BoundingSphere( new Point3d( 0.0,0.0,0.0 ), 400.0 );
	}

	ViewerAvatar createAvatar( )
	{				
		ViewerAvatar va = new ViewerAvatar( );
		TransformGroup tg = new TransformGroup( );

		Car car = new Car( this, tg, 	ComplexObject.GEOMETRY | 
			ComplexObject.TEXTURE | 
			ComplexObject.COLLISION | 
			ComplexObject.COLLISION_SOUND );

		car.createObject( new Appearance( ),
			new Vector3d( 0,-0.3,-0.3 ),
			new Vector3d( 0.3,0.3,1 ),
			"platform.jpg",
			null,
			"collide.wav" );

		tg.addChild( car );
		va.addChild( tg );

		return va;
	}

	public static void main( String[] args )
	{
		AvatarTest avatarTest = new AvatarTest( );

		// Create a simple scene and attach it to the virtual universe
		SimpleUniverse u = new SimpleUniverse( );

		PhysicalEnvironment physicalEnv = u.getViewer( ).getPhysicalEnvironment( );

		TransformGroup tg = u.getViewer( ).getViewingPlatform( ).getViewPlatformTransform( );

		Transform3D t3d = new Transform3D( );
		t3d.set( new Vector3f( 0,0.5f,0 ) );
		tg.setTransform( t3d );  

		CarSteering keys = new CarSteering( tg );
		keys.setSchedulingBounds( avatarTest.getBoundingSphere( ) );

		u.getViewer( ).setAvatar( avatarTest.createAvatar( ) );

		if (physicalEnv != null)
		{
			JavaSoundMixer javaSoundMixer = new JavaSoundMixer( physicalEnv );

			if (javaSoundMixer == null) 
				System.out.println( "Unable to create AudioDevice." );

			javaSoundMixer.initialize( );
		}

		// Add everthing to the scene graph - it will now be displayed.
		BranchGroup scene = avatarTest.createSceneGraph( );
		scene.addChild( keys );
		Java3dTree j3dTree = new Java3dTree( );

		j3dTree.recursiveApplyCapability( scene );

		u.addBranchGraph( scene );

		j3dTree.updateNodes( u );

		u.getViewingPlatform( ).getViewPlatform( ).setActivationRadius( 2 );
	}
}
	
