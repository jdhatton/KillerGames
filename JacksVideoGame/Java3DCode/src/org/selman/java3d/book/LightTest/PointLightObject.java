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

package org.selman.java3d.book.lighttest;

import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.geometry.*;

import java.util.*;
import java.awt.*;

public class PointLightObject extends LightObject
{		
	protected TextField				m_XPositionTextField = null;
	protected TextField				m_YPositionTextField = null;
	protected TextField				m_ZPositionTextField = null;

	protected TextField				m_ConstantAttenuationTextField = null;
	protected TextField				m_LinearAttenuationTextField = null;
	protected TextField				m_QuadraticAttenuationTextField = null;

	protected TransformGroup		m_TransformGroup = null;
	protected Sphere					m_Sphere = null;

	public PointLightObject( )
	{
	}

	protected Light createLight( )
	{
		return ( Light ) new PointLight( );
	}

	public String getName( )
	{
		return "PointLight";
	}


	protected int[] getCapabilities( )
	{
		int[] superCaps = super.getCapabilities( );

		int[] caps = 	{ 	PointLight.ALLOW_ATTENUATION_READ,
								PointLight.ALLOW_ATTENUATION_WRITE,
								PointLight.ALLOW_POSITION_READ,
								PointLight.ALLOW_POSITION_WRITE
							};

		return createCompoundArray( superCaps, caps );
	}

	public Group createGeometry( )
	{
		Point3f pos = new Point3f( );
		((PointLight) m_Light ).getPosition( pos );

		m_TransformGroup = new TransformGroup( );
		m_TransformGroup.setCapability( TransformGroup.ALLOW_TRANSFORM_WRITE );
		m_TransformGroup.setCapability( TransformGroup.ALLOW_TRANSFORM_READ );

		Transform3D t3d = new Transform3D( );
		t3d.setTranslation( new Vector3f( pos.x, pos.y, pos.z ) );
		m_TransformGroup.setTransform( t3d );

		m_Sphere = new Sphere( 0.2f, Primitive.ENABLE_APPEARANCE_MODIFY | Primitive.GENERATE_NORMALS, 16 );
		m_TransformGroup.addChild( m_Sphere );
		m_TransformGroup.addChild( super.createGeometry( ) );

		return ( Group ) m_TransformGroup;
	}

	public void addUiToPanel( Panel panel )
	{
		m_XPositionTextField = new TextField( 3 );
		m_YPositionTextField = new TextField( 3 );
		m_ZPositionTextField = new TextField( 3 );

		m_ConstantAttenuationTextField = new TextField( 3 );
		m_LinearAttenuationTextField = new TextField( 3 );
		m_QuadraticAttenuationTextField = new TextField( 3 );

		panel.add( new Label( "Position:" ) );
		panel.add( new Label( "X:" ) );
		panel.add( m_XPositionTextField );
		panel.add( new Label( "Y:" ) );
		panel.add( m_YPositionTextField );
		panel.add( new Label( "Z:" ) );
		panel.add( m_ZPositionTextField );

		panel.add( new Label( "Attenuation:" ) );
		panel.add( new Label( "Constant:" ) );
		panel.add( m_ConstantAttenuationTextField );
		panel.add( new Label( "Linear:" ) );
		panel.add( m_LinearAttenuationTextField );
		panel.add( new Label( "Quadratic:" ) );
		panel.add( m_QuadraticAttenuationTextField );

		super.addUiToPanel( panel );		
	}

	public void synchLightToUi( )
	{
		super.synchLightToUi( );

		// set some defaults if things go wrong...
		double x = 0;
		double y = 0;
		double z = 0;

		double constant = 0.01;
		double linear = 0;
		double quadratic = 0;

		try
		{		
			x = Double.valueOf( m_XPositionTextField.getText( ) ).doubleValue( );
			y = Double.valueOf( m_YPositionTextField.getText( ) ).doubleValue( );
			z = Double.valueOf( m_ZPositionTextField.getText( ) ).doubleValue( );

			constant = Double.valueOf( m_ConstantAttenuationTextField.getText( ) ).doubleValue( );
			linear = Double.valueOf( m_LinearAttenuationTextField.getText( ) ).doubleValue( );
			quadratic = Double.valueOf( m_QuadraticAttenuationTextField.getText( ) ).doubleValue( );
		}
		catch( java.lang.NumberFormatException e )
		{
			// invalid numeric input - just ignore.
		}

		((PointLight) m_Light).setPosition( (float) x, (float) y, (float) z );
		((PointLight) m_Light ).setAttenuation( (float) constant, (float) linear, (float) quadratic );

		if( m_TransformGroup != null )
		{
			Transform3D t3d = new Transform3D( );
			m_TransformGroup.getTransform( t3d );
			t3d.setTranslation( new Vector3d( x, y, z ) );
			m_TransformGroup.setTransform( t3d );
		}

		if( m_Sphere != null )
		{
			Appearance app = new Appearance( );

			Color3f objColor = new Color3f( );
			m_Light.getColor( objColor );
			Color3f black = new Color3f( 0.0f, 0.0f, 0.0f );						
			app.setMaterial( new Material( objColor, black, objColor, black, 80.0f ) );
			m_Sphere.getShape( Sphere.BODY ).setAppearance( app );
		}
	}

	public void synchUiToLight( )
	{
		super.synchUiToLight( );
	}
}
