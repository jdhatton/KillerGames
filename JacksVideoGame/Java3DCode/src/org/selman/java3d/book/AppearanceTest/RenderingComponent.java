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

package org.selman.java3d.book.appearancetest;

import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.*;

import javax.media.j3d.*;
import javax.vecmath.*;

public class RenderingComponent extends AppearanceComponent
{
	public RenderingComponent( Appearance app )
	{
		super( app );
	}

	protected int[] getCapabilities( )
	{
		return new int[]
		{
			RenderingAttributes.ALLOW_ALPHA_TEST_FUNCTION_WRITE,
			RenderingAttributes.ALLOW_ALPHA_TEST_VALUE_WRITE,
			RenderingAttributes.ALLOW_DEPTH_ENABLE_READ
		};
	}

	protected NodeComponent createComponent( )
	{
		return ( NodeComponent ) new RenderingAttributes( );
	}

	protected void setAppearanceCapability( )
	{
		m_Appearance.setCapability( Appearance.ALLOW_RENDERING_ATTRIBUTES_WRITE );
	}

	protected void assignToAppearance( )
	{
		m_Appearance.setRenderingAttributes( (RenderingAttributes) m_NodeComponent );
	}

	protected void assignNullToAppearance( )
	{
		m_Appearance.setRenderingAttributes( null );
	}


	protected String getName( )
	{
		return "Rendering";
	}

	protected String[] getMenuItemNames( )
	{
		return new String[]
		{
			"-",
				"AlphaTest", 
				"-", 
				"ALWAYS",
				"NEVER",
				"EQUAL",
				"NOT_EQUAL",
				"LESS",
				"LESS_OR_EQUAL",
				"GREATER",
				"GREATER_OR_EQUAL" ,
				"-",
				"AlphaTest Value",
				"-",
				"0",
				"0point5",
				"1",
				"-",
				"Depth Buffer",
				"-",
				"On",
				"Off",
				"-",
				"Depth Buffer Write",
				"-",
				"Enable",
				"Disable",
		};
	}

	private RenderingAttributes getRenderingAttributes( )
	{
		return ( RenderingAttributes ) m_NodeComponent;
	}

	public void onALWAYS( )
	{
		getRenderingAttributes( ).setAlphaTestFunction( RenderingAttributes.ALWAYS );
	}

	public void onNEVER( )
	{
		getRenderingAttributes( ).setAlphaTestFunction( RenderingAttributes.NEVER );
	}

	public void onEQUAL( )
	{
		getRenderingAttributes( ).setAlphaTestFunction( RenderingAttributes.EQUAL );
	}

	public void onNOT_EQUAL( )
	{
		getRenderingAttributes( ).setAlphaTestFunction( RenderingAttributes.NOT_EQUAL );
	}

	public void onLESS( )
	{
		getRenderingAttributes( ).setAlphaTestFunction( RenderingAttributes.LESS );
	}

	public void onLESS_OR_EQUAL( )
	{
		getRenderingAttributes( ).setAlphaTestFunction( RenderingAttributes.LESS_OR_EQUAL );
	}

	public void onGREATER( )
	{
		getRenderingAttributes( ).setAlphaTestFunction( RenderingAttributes.GREATER );
	}

	public void onGREATER_OR_EQUAL( )
	{
		getRenderingAttributes( ).setAlphaTestFunction( RenderingAttributes.GREATER_OR_EQUAL );
	}

	public void on0( )
	{
		getRenderingAttributes( ).setAlphaTestValue( 0 );
	}

	public void on0point5( )
	{
		getRenderingAttributes( ).setAlphaTestValue( 0.5f );
	}

	public void on1( )
	{
		getRenderingAttributes( ).setAlphaTestValue( 1 );
	}

	public void onOn( )
	{
		getRenderingAttributes( ).setDepthBufferEnable( true );
	}

	public void onOff( )
	{
		getRenderingAttributes( ).setDepthBufferEnable( false );
	}

	public void onEnable( )
	{
		getRenderingAttributes( ).setDepthBufferWriteEnable( true );
	}

	public void onDisable( )
	{
		getRenderingAttributes( ).setDepthBufferWriteEnable( false );
	}
}
