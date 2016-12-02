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

package org.selman.java3d.book.mousenavigatetest;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;

import javax.media.j3d.*;
import javax.vecmath.*;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.geometry.*;

import org.selman.java3d.book.common.*;

/**
* This example illustrates the mouse behaviors defined in the 
* org.selman.java3d.book package. These improved mouse
* behaviors:
*  - handle TransformGroups above the target TG properly <br>
*  - can be applied to any object, not just TGs <br>
*  - support interface reporting to give easy feedback on all manipulations <br>
*  - better motion speed control <br>
*  - range validation to clamp scales and translation between limits <br>
*/
public class MouseNavigateTest extends Java3dApplet implements ScaleChangeListener, RotationChangeListener, TranslationChangeListener
{
	private static int 			m_kWidth = 300;
	private static int 			m_kHeight = 400;

	// create some UI to provide feedback on all mouse
	// manipulations
	Label						m_RotationLabel = null;
	TextField					m_RotationFieldX = null;
	TextField					m_RotationFieldY = null;
	TextField					m_RotationFieldZ = null;

	Label						m_TranslationLabel = null;
	TextField					m_TranslationFieldX = null;
	TextField					m_TranslationFieldY = null;
	TextField					m_TranslationFieldZ = null;

	Label						m_ScaleLabel = null;
	TextField					m_ScaleFieldZ = null;
	TextField					m_ScaleFieldY = null;
	TextField					m_ScaleFieldX = null;


	public MouseNavigateTest( )
	{
		initJava3d( );
	}

	protected void addCanvas3D( Canvas3D c3d )
	{
   	setLayout( new BorderLayout() );
		add( c3d, BorderLayout.CENTER );
       
       Panel controlPanel = new Panel();

		// add the UI to the frame
		m_RotationLabel = new Label( "Rotation: " );
		m_RotationFieldX = new TextField( "0.00" );
		m_RotationFieldY = new TextField( "0.00" );
		m_RotationFieldZ = new TextField( "0.00" );
		controlPanel.add( m_RotationLabel );
		controlPanel.add( m_RotationFieldX );
		controlPanel.add( m_RotationFieldY );
		controlPanel.add( m_RotationFieldZ );

		m_TranslationLabel = new Label( "Translation: " );
		m_TranslationFieldX = new TextField( "0.00" );
		m_TranslationFieldY = new TextField( "0.00" );
		m_TranslationFieldZ = new TextField( "0.00" );
		controlPanel.add( m_TranslationLabel );
		controlPanel.add( m_TranslationFieldX );
		controlPanel.add( m_TranslationFieldY );
		controlPanel.add( m_TranslationFieldZ );

		m_ScaleLabel = new Label( "Scale: " );
		m_ScaleFieldX = new TextField( "0.00" );
		m_ScaleFieldY = new TextField( "0.00" );
		m_ScaleFieldZ = new TextField( "0.00" );
		controlPanel.add( m_ScaleLabel );
		controlPanel.add( m_ScaleFieldX );
		controlPanel.add( m_ScaleFieldY );
		controlPanel.add( m_ScaleFieldZ );
       
       add( controlPanel, BorderLayout.SOUTH );

		doLayout( );
	}

	protected double getScale( )
	{
		return 1.0;
	}

	// do nothing for these notifications from the mouse behaviors
	public void onStartDrag( Object target )
	{
	}
	public void onEndDrag( Object target )
	{
	}
	public void onApplyTransform( Object target )
	{
	}
	public void onAdjustTransform( Object target, int xpos, int ypos )
	{
	}

	// called by TornadoMouseRotate
	// yes, those really are Euler angles for the objects rotation
	public void onRotate( Object target, Point3d point3d )
	{
		m_RotationFieldX.setText( String.valueOf( (int) java.lang.Math.toDegrees( point3d.x ) ) );
		m_RotationFieldY.setText( String.valueOf( (int) java.lang.Math.toDegrees( point3d.y ) ) );
		m_RotationFieldZ.setText( String.valueOf( (int) java.lang.Math.toDegrees( point3d.z ) ) );
	}

	// called by TornadoMouseScale
	public void onScale( Object target, Vector3d scale )
	{
		m_ScaleFieldX.setText( String.valueOf( scale.x ) );
		m_ScaleFieldY.setText( String.valueOf( scale.y ) );
		m_ScaleFieldZ.setText( String.valueOf( scale.z ) );
	}

	// called by TornadoMouseTranslate
	public void onTranslate( Object target, Vector3d vTranslation )
	{
		m_TranslationFieldX.setText( String.valueOf( vTranslation.x ) );
		m_TranslationFieldY.setText( String.valueOf( vTranslation.y ) );
		m_TranslationFieldZ.setText( String.valueOf( vTranslation.z ) );
	}

	// we want a black background
	protected Background createBackground( )
	{
		return null;
	}


	protected BranchGroup createSceneBranchGroup( )
	{
		BranchGroup objRoot = super.createSceneBranchGroup( );

		// note that we are creating a TG *above* the TG
		// the is being controlled by the mouse behaviors.
		// The SUN mouse translate behavior would fail in this
		// instance as all movement would be in the X-Y plane
		// irrespective of any TG above the object. 
		// The TornadoMouseTranslate behavior always moves an object
		// parrallel to the image plane
		TransformGroup objTrans1 = new TransformGroup( );
		Transform3D t3d = new Transform3D( );
		objTrans1.getTransform( t3d );
		t3d.setEuler( new Vector3d( 0.9,0.8,0.3 ) );
		objTrans1.setTransform( t3d );

		TransformGroup objTrans = new TransformGroup( );
		objTrans.setCapability( TransformGroup.ALLOW_TRANSFORM_WRITE );
		objTrans.setCapability( TransformGroup.ALLOW_TRANSFORM_READ );

		// create the mouse scale behavior and set limits
		TornadoMouseScale mouseScale = new TornadoMouseScale( 5, 0.1f );
		mouseScale.setMinScale( new Point3d( 0.5,0.5,0.5 ) );
		mouseScale.setMaxScale( new Point3d( 2,2,2 ) );
		mouseScale.setObject( objTrans );
		mouseScale.setChangeListener( this );
		mouseScale.setSchedulingBounds( getApplicationBounds( ) );
		objTrans.addChild( mouseScale );

		// create the mouse rotate behavior
		TornadoMouseRotate mouseRotate = new TornadoMouseRotate( 0.001, 0.001 );
		mouseRotate.setInvert( true );
		mouseRotate.setObject( objTrans );
		mouseRotate.setChangeListener( this );
		mouseRotate.setSchedulingBounds( getApplicationBounds( ) );
		objTrans.addChild( mouseRotate );

		// create the mouse translate behavior and set limits
		TornadoMouseTranslate mouseTrans = new TornadoMouseTranslate( 0.005f );
		mouseTrans.setObject( objTrans );
		mouseTrans.setChangeListener( this );
		mouseTrans.setMinTranslate( new Point3d( -4,-4,-4 ) );
		mouseTrans.setMaxTranslate( new Point3d( 4,4,4 ) );
		mouseTrans.setSchedulingBounds( getApplicationBounds( ) );
		objTrans.addChild( mouseTrans );

		objTrans.addChild( new ColorCube( 0.5 ) );

		// create some axis for the world to show it has been rotated
		ColorCube axis = new ColorCube( 5.0 );
		Appearance app = new Appearance( );
		app.setPolygonAttributes( new PolygonAttributes( PolygonAttributes.POLYGON_LINE, PolygonAttributes.CULL_NONE, 0 ) );
		axis.setAppearance( app );		
		objTrans1.addChild( axis );

		objTrans1.addChild( objTrans );
		objRoot.addChild( objTrans1 );

		return objRoot;
	}


	public static void main( String[] args )
	{
		MouseNavigateTest mouseTest = new MouseNavigateTest( );
		mouseTest.saveCommandLineArguments( args );

		new MainFrame( mouseTest, m_kWidth, m_kHeight );
	}
}
