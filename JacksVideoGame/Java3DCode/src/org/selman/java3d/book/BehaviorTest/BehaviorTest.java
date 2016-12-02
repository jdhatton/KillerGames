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

package org.selman.java3d.book.behaviortest;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;

import javax.media.j3d.*;
import javax.vecmath.*;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.image.TextureLoader;

import org.selman.java3d.book.common.*;

/**
* Creates five sample behaviors and applies them
* to an object in a scene:<p>
* 1. Object Size - displays the size of the geometry in the object
* 2. Explode - explodes the geometry after 10 seconds
* 3. Stretch - allows the geometry to be stretched using the spacebar (simple physics)
* 4. Bounds - displays the bounds of the object
* 5. FPS - displays frames-per-seconds rendered
*/
public class BehaviorTest extends Java3dApplet implements ExplosionListener, ActionListener
{
	private static int 				m_kWidth = 350;
	private static int 				m_kHeight = 400;

	private RotationInterpolator	m_RotationInterpolator = null;
	private StretchBehavior 		m_StretchBehavior = null;
	private ObjectSizeBehavior 		m_SizeBehavior = null;
	private ExplodeBehavior			m_ExplodeBehavior = null;

	private FpsBehavior				m_FpsBehavior = null;
	private BoundsBehavior			m_BoundsBehavior = null;

	public BehaviorTest( )
	{
		initJava3d( );

		Panel controlPanel = new Panel( );

		Button rotateButton = new Button( "Rotate" );
		rotateButton.addActionListener( this );
		controlPanel.add( rotateButton );

		Button objSizeButton = new Button( "Object Size" );
		objSizeButton.addActionListener( this );
		controlPanel.add( objSizeButton );

		Button explodeButton = new Button( "Explode" );
		explodeButton.addActionListener( this );
		controlPanel.add( explodeButton );

		Button stretchButton = new Button( "Stretch" );
		stretchButton.addActionListener( this );
		controlPanel.add( stretchButton );

		Button boundsButton = new Button( "Bounds" );
		boundsButton.addActionListener( this );
		controlPanel.add( boundsButton );

		Button fpsButton = new Button( "FPS" );
		fpsButton.addActionListener( this );
		controlPanel.add( fpsButton );

		add( controlPanel, BorderLayout.SOUTH );
	}

	// handle event from the GUI components we created
	public void actionPerformed( ActionEvent event )
	{
		if( event.getActionCommand( ).equals( "Object Size" ) != false )
			m_SizeBehavior.setEnable( !m_SizeBehavior.getEnable( ) );

		else if( event.getActionCommand( ).equals( "Explode" ) != false )
			m_ExplodeBehavior.setEnable( !m_ExplodeBehavior.getEnable( ) );

		else if( event.getActionCommand( ).equals( "Stretch" ) != false )
			m_StretchBehavior.setEnable( !m_StretchBehavior.getEnable( ) );

		else if( event.getActionCommand( ).equals( "Rotate" ) != false )
			m_RotationInterpolator.setEnable( !m_RotationInterpolator.getEnable( ) );

		else if( event.getActionCommand( ).equals( "Bounds" ) != false )
			m_BoundsBehavior.setEnable( !m_BoundsBehavior.getEnable( ) );

		else if( event.getActionCommand( ).equals( "FPS" ) != false )
			m_FpsBehavior.setEnable( !m_FpsBehavior.getEnable( ) );
	}

	protected Background createBackground( )
	{
		return null;
	}

	protected BranchGroup createSceneBranchGroup( )
	{
		BranchGroup objRoot = super.createSceneBranchGroup( );

		// create a TransformGroup to rotate the hand
		TransformGroup objTrans = new TransformGroup( );
		objTrans.setCapability( TransformGroup.ALLOW_TRANSFORM_WRITE );
		objTrans.setCapability( TransformGroup.ALLOW_TRANSFORM_READ );

		// create a RotationInterpolator behavior to rotate the hand
		Transform3D yAxis = new Transform3D( );
		Alpha rotationAlpha = new Alpha( -1, Alpha.INCREASING_ENABLE,
			0, 0,
			4000, 0, 0,
			0, 0, 0 );

		m_RotationInterpolator = new RotationInterpolator( rotationAlpha, objTrans, yAxis, 0.0f, (float) Math.PI*2.0f );
		m_RotationInterpolator.setSchedulingBounds( createApplicationBounds( ) );
		objTrans.addChild( m_RotationInterpolator );

		// create an Appearance and Material
		Appearance app = new Appearance( );

		TextureLoader tex = new TextureLoader( "earth.jpg", this );
		app.setTexture( tex.getTexture( ) );

		Sphere sphere = new Sphere( 3, Primitive.GENERATE_NORMALS | Primitive.GENERATE_TEXTURE_COORDS, 32, app );

		// connect the scenegraph
		objTrans.addChild( sphere );
		objRoot.addChild( objTrans );

		m_FpsBehavior = new FpsBehavior( );
		m_FpsBehavior.setSchedulingBounds( getApplicationBounds( ) );
		objRoot.addChild( m_FpsBehavior );

		m_BoundsBehavior = new BoundsBehavior( sphere );
		m_BoundsBehavior.setSchedulingBounds( getApplicationBounds( ) );
		m_BoundsBehavior.addBehaviorToParentGroup( objTrans );

		m_StretchBehavior = new StretchBehavior( (GeometryArray) sphere.getShape( ).getGeometry( ) );
		m_StretchBehavior.setSchedulingBounds( getApplicationBounds( ) );				
		objRoot.addChild( m_StretchBehavior );
		m_StretchBehavior.setEnable( false );

		m_SizeBehavior = new ObjectSizeBehavior( (GeometryArray) sphere.getShape( ).getGeometry( ) );
		m_SizeBehavior.setSchedulingBounds( getApplicationBounds( ) );				
		objRoot.addChild( m_SizeBehavior );
		m_SizeBehavior.setEnable( false );

		m_ExplodeBehavior = new ExplodeBehavior( sphere.getShape( ), 10000, 20, this );
		m_ExplodeBehavior.setSchedulingBounds( getApplicationBounds( ) );				
		objRoot.addChild( m_ExplodeBehavior );

		return objRoot;
	}

	public WakeupCondition onExplosionFinished( ExplodeBehavior explodeBehavior, Shape3D shape3D )
	{
		System.out.println( "Explosion Finished." );
		return explodeBehavior.restart( shape3D, 10000, 20, this );
	}

	public static void main( String[] args )
	{
		BehaviorTest behaviorTest = new BehaviorTest( );
		behaviorTest.saveCommandLineArguments( args );

		new MainFrame( behaviorTest, m_kWidth, m_kHeight );
	}
}
