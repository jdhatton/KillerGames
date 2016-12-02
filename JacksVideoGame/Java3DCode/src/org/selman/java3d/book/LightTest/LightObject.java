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

import java.awt.*;
import java.awt.event.*;

import java.util.*;
import javax.swing.*;

// abstract base class that implements the 
// basic "Light" class UI functionality
public abstract class LightObject implements ActionListener, ItemListener
{
	protected Panel				m_Panel = null;
	protected JColorChooser 	m_ColorChooser = null;

	TextField					m_XTextField = null;
	TextField					m_YTextField = null;
	TextField					m_ZTextField = null;
	TextField					m_RadiusTextField = null;

	Checkbox 					m_EnableCheck = null;

	protected Light				m_Light = null;

	public LightObject( )
	{
		m_Light = createLight( );
		m_Light.setInfluencingBounds( new BoundingSphere( new Point3d( 0,0,0 ), 100 ) );

		m_ColorChooser = new JColorChooser( ); 

		int[] caps = getCapabilities( );

		if( caps != null )
		{
			for( int n = 0; n < caps.length; n++ )
				m_Light.setCapability( caps[n] );
		}
	}

	protected Light createLight( )
	{
		return null;
	}

	public Light getLight( )
	{
		return m_Light;
	}

	public String getName( )
	{
		return "Light";
	}

	protected int[] getCapabilities( )
	{
		int[] caps = new int[8];
		int nIndex = 0;

		caps[nIndex++] = Light.ALLOW_COLOR_READ;
		caps[nIndex++] = Light.ALLOW_COLOR_WRITE;
		caps[nIndex++] = Light.ALLOW_INFLUENCING_BOUNDS_READ;
		caps[nIndex++] = Light.ALLOW_INFLUENCING_BOUNDS_WRITE;
		caps[nIndex++] = Light.ALLOW_SCOPE_READ;
		caps[nIndex++] = Light.ALLOW_SCOPE_WRITE;
		caps[nIndex++] = Light.ALLOW_STATE_READ;
		caps[nIndex++] = Light.ALLOW_STATE_WRITE;

		return caps;
	}


	public Group createGeometry( )
	{
		Group g = new Group( );		
		m_Light.setUserData( this );

		return g;
	}		

	public void addUiToPanel( Panel panel )
	{
		Button colorButton = new Button( "Color" );
		colorButton.addActionListener( this );
		panel.add( colorButton );

		m_EnableCheck = new Checkbox( "Enable", true );
		m_EnableCheck.addItemListener( this );
		panel.add( m_EnableCheck );

		panel.add( new Label( "Bounds:" ) );

		panel.add( new Label( "X:" ) );
		m_XTextField = new TextField( 3 );
		panel.add( m_XTextField );

		panel.add( new Label( "Y:" ) );
		m_YTextField = new TextField( 3 );
		panel.add( m_YTextField );

		panel.add( new Label( "Z:" ) );
		m_ZTextField = new TextField( 3 );
		panel.add( m_ZTextField );

		panel.add( new Label( "Radius:" ) );
		m_RadiusTextField = new TextField( 4 );
		panel.add( m_RadiusTextField );

		Button updateButton = new Button( "Update" );
		updateButton.addActionListener( this );
		panel.add( updateButton );

		synchLightToUi( );
	}

	public void actionPerformed( ActionEvent event )
	{
		if( event.getActionCommand( ).equals( "Color" ) != false )
			OnColor( );
		else if( event.getActionCommand( ).equals( "Update" ) != false )
			synchLightToUi( );
	}

	public void itemStateChanged( ItemEvent event )
	{
		m_Light.setEnable( event.getStateChange( ) == ItemEvent.SELECTED );
	}

	protected void OnColor( )
	{
		Color rgb = m_ColorChooser.showDialog( m_Panel, 
			"Set Light Color", 
			null );

		if( rgb != null )
		{
			m_Light.setColor( new Color3f( (float) rgb.getRed( ) / 255f,
				(float) rgb.getGreen( ) / 255f,
				(float) rgb.getBlue( ) / 255f ) );
		}
	}


	public void synchLightToUi( )
	{
		m_Light.setEnable( m_EnableCheck.getState( ) );

		// set some defaults if things go wrong...
		double x = 0;
		double y = 0;
		double z = 0;

		double radius = 100;

		try
		{		
			x = Double.valueOf( m_XTextField.getText( ) ).doubleValue( );
			y = Double.valueOf( m_YTextField.getText( ) ).doubleValue( );
			z = Double.valueOf( m_ZTextField.getText( ) ).doubleValue( );

			radius = Double.valueOf( m_RadiusTextField.getText( ) ).doubleValue( );
		}
		catch( java.lang.NumberFormatException e )
		{
			// invalid numeric input - just ignore.
		}

		m_Light.setInfluencingBounds( new BoundingSphere( new Point3d( x,y,z ), radius ) );
	}

	// this method is a placeholder for some
	// code that would synchronize the UI with the
	// state of a light. This would allow the user
	// to move a light around using a MouseBehavior
	// and update the UI to display the new position. 
	// An exercise for the reader... ;-)
	public void synchUiToLight( )
	{
	}

	protected int[] createCompoundArray( int[] a1, int[] a2 )
	{
		int[] aRet = null;
		int nTotalLen = 0;
		int nLen1 = 0;
		int nLen2 = 0;

		if( a1 != null )
		{
			nTotalLen += a1.length;
			nLen1 = a1.length;
		}

		if( a2 != null )
		{
			nTotalLen += a2.length;
			nLen2 = a2.length;
		}

		aRet = new int[ nTotalLen ];

		if( a1 != null )
			System.arraycopy( a1, 0, aRet, 0, nLen1 );

		if( a2 != null )
			System.arraycopy( a2, 0, aRet, nLen1, a2.length );

		return aRet;
	}
}
