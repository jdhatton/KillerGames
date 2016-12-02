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

public class TransparencyComponent extends AppearanceComponent
{
	public TransparencyComponent( Appearance app )
	{
		super( app );
	}

	protected int[] getCapabilities( )
	{
		return new int[]
		{
			TransparencyAttributes.ALLOW_MODE_WRITE,
			TransparencyAttributes.ALLOW_VALUE_WRITE
		};
	}

	protected NodeComponent createComponent( )
	{
		return ( NodeComponent ) new TransparencyAttributes( );
	}

	protected void setAppearanceCapability( )
	{
		m_Appearance.setCapability( Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_WRITE );
	}

	protected void assignToAppearance( )
	{
		m_Appearance.setTransparencyAttributes( (TransparencyAttributes) m_NodeComponent );
	}

	protected void assignNullToAppearance( )
	{
		m_Appearance.setTransparencyAttributes( null );
	}


	protected String getName( )
	{
		return "Transparency";
	}

	protected String[] getMenuItemNames( )
	{
		return new String[]
		{
			"-",
				"Transparency", 
				"-", 
				"0",
				"0point2",
				"0point5",
				"0point8",
				"1",
				"-",
				"Mode",
				"-",
				"NONE",
				"FASTEST",
				"NICEST",
				"SCREEN_DOOR",
				"BLENDED"
		};
	}

	private TransparencyAttributes getTransparencyAttributes( )
	{
		return ( TransparencyAttributes ) m_NodeComponent;
	}

	public void on0( )
	{
		getTransparencyAttributes( ).setTransparency( 0 );
	}

	public void on0point2( )
	{
		getTransparencyAttributes( ).setTransparency( 0.2f );
	}

	public void on0point5( )
	{
		getTransparencyAttributes( ).setTransparency( 0.5f );
	}

	public void on0point8( )
	{
		getTransparencyAttributes( ).setTransparency( 0.8f );
	}

	public void on1( )
	{
		getTransparencyAttributes( ).setTransparency( 1 );
	}

	public void onNONE( )
	{
		getTransparencyAttributes( ).setTransparencyMode( TransparencyAttributes.NONE );
	}

	public void onFASTEST( )
	{
		getTransparencyAttributes( ).setTransparencyMode( TransparencyAttributes.FASTEST );
	}

	public void onNICEST( )
	{
		getTransparencyAttributes( ).setTransparencyMode( TransparencyAttributes.NICEST );
	}

	public void onSCREEN_DOOR( )
	{
		getTransparencyAttributes( ).setTransparencyMode( TransparencyAttributes.SCREEN_DOOR );
	}

	public void onBLENDED( )
	{
		getTransparencyAttributes( ).setTransparencyMode( TransparencyAttributes.BLENDED );
	}
}
