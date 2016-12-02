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

public class DirectionalLightObject extends LightObject
{		
	protected TextField				m_XDirectionTextField = null;
	protected TextField				m_YDirectionTextField = null;
	protected TextField				m_ZDirectionTextField = null;

	protected TransformGroup		m_TransformGroup = null;

	protected Cone					m_Cone = null;


	public DirectionalLightObject( )
	{
	}

	protected Light createLight( )
	{
		return ( Light ) new DirectionalLight( );
	}

	public String getName( )
	{
		return "DirectionalLight";
	}


	protected int[] getCapabilities( )
	{
		int[] superCaps = super.getCapabilities( );

		int[] caps = 	{ 	
								DirectionalLight.ALLOW_DIRECTION_READ,
								DirectionalLight.ALLOW_DIRECTION_WRITE,
							};

		return createCompoundArray( superCaps, caps );
	}

	public Group createGeometry( )
	{
		m_TransformGroup = new TransformGroup( );
		m_TransformGroup.setCapability( TransformGroup.ALLOW_TRANSFORM_WRITE );
		m_TransformGroup.setCapability( TransformGroup.ALLOW_TRANSFORM_READ );

		// create appearance and material for the Cone
		Appearance app = new Appearance( );

		// create the Primitive and add to the parent BranchGroup
		m_Cone = new Cone( 1, 10, Primitive.ENABLE_APPEARANCE_MODIFY | Primitive.GENERATE_NORMALS, app );
		m_TransformGroup.addChild( m_Cone );

		Group superGroup = super.createGeometry( );
		superGroup.addChild( m_TransformGroup );

		return superGroup;
	}

	public void addUiToPanel( Panel panel )
	{
		m_XDirectionTextField = new TextField( 3 );
		m_YDirectionTextField = new TextField( 3 );
		m_ZDirectionTextField = new TextField( 3 );

		panel.add( new Label( "Direction:" ) );
		panel.add( new Label( "X:" ) );
		panel.add( m_XDirectionTextField );
		panel.add( new Label( "Y:" ) );
		panel.add( m_YDirectionTextField );
		panel.add( new Label( "Z:" ) );
		panel.add( m_ZDirectionTextField );

		super.addUiToPanel( panel );		
	}

	public void synchLightToUi( )
	{
		super.synchLightToUi( );

		// set some defaults if things go wrong...
		double x = 0;
		double y = 0;
		double z = 0;

		try
		{		
			x = Double.valueOf( m_XDirectionTextField.getText( ) ).doubleValue( );
			y = Double.valueOf( m_YDirectionTextField.getText( ) ).doubleValue( );
			z = Double.valueOf( m_ZDirectionTextField.getText( ) ).doubleValue( );
		}
		catch( java.lang.NumberFormatException e )
		{
			// invalid numeric input - just ignore.
		}

		((DirectionalLight) m_Light).setDirection( (float) x, (float) y, (float) z );

		if( m_TransformGroup != null )
		{
			Vector3d coneVector = new Vector3d( 0, 1, 0 );
			Vector3d lightVector = new Vector3d( x, y, z );

			coneVector.normalize( );
			lightVector.normalize( );

			Vector3d axisVector = new Vector3d( );
			axisVector.cross( coneVector, lightVector );
			double angle  = java.lang.Math.acos( coneVector.dot( lightVector ) );

			AxisAngle4d rotAxis = new AxisAngle4d( axisVector.x, axisVector.y, axisVector.z, angle );

			Transform3D t3d = new Transform3D( );
			t3d.setRotation( rotAxis );

			m_TransformGroup.setTransform( t3d );
		}

		if( m_Cone != null )
		{
			Appearance app = new Appearance( );

			Color3f objColor = new Color3f( );
			m_Light.getColor( objColor );
			Color3f black = new Color3f( 0.0f, 0.0f, 0.0f );						
			app.setMaterial( new Material( objColor, black, objColor, black, 80.0f ) );
			m_Cone.setAppearance( app );
		}
	}

	public void synchUiToLight( )
	{
		super.synchUiToLight( );
	}
}
