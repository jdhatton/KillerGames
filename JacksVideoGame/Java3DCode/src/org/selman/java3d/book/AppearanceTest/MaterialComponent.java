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

public class MaterialComponent extends AppearanceComponent
{
	public MaterialComponent( Appearance app )
	{
		super( app );
	}

	protected int[] getCapabilities( )
	{
		return new int[]
		{
			Material.ALLOW_COMPONENT_WRITE
		};
	}

	protected NodeComponent createComponent( )
	{
		return ( NodeComponent ) new Material( );
	}

	protected void setAppearanceCapability( )
	{
		m_Appearance.setCapability( Appearance.ALLOW_MATERIAL_WRITE );
	}

	protected void assignToAppearance( )
	{
		m_Appearance.setMaterial( (Material) m_NodeComponent );
	}

	protected void assignNullToAppearance( )
	{
		m_Appearance.setMaterial( null );
	}

	protected String getName( )
	{
		return "Material";
	}

	protected String[] getMenuItemNames( )
	{
		return new String[]
		{
			"-",
				"Ambient", 
				"-", 
				"A_White",
				"A_Black",
				"A_Blue",
				"-",
				"Diffuse",
				"-",
				"D_White",
				"D_Black",
				"D_Blue",
				"-",
				"Emissive",
				"-",
				"E_White",
				"E_Black",
				"E_Blue",
				"-",
				"Specular",
				"-",
				"S_White",
				"S_Black",
				"S_Blue",
				"-",
				"Lighting",
				"-",
				"On",
				"Off",
				"-",
				"Shininess",
				"-",
				"1",
				"70",
		};
	}

	private Material getMaterial( )
	{
		return ( Material ) m_NodeComponent;
	}	

	public void onA_White( )
	{
		getMaterial( ).setAmbientColor( 1,1,1 );
	}

	public void onA_Black( )
	{
		getMaterial( ).setAmbientColor( 0,0,0 );
	}

	public void onA_Blue( )
	{
		getMaterial( ).setAmbientColor( 0,0,1 );
	}

	public void onE_White( )
	{
		getMaterial( ).setEmissiveColor( 1,1,1 );
	}

	public void onE_Black( )
	{
		getMaterial( ).setEmissiveColor( 0,0,0 );
	}

	public void onE_Blue( )
	{
		getMaterial( ).setEmissiveColor( 0,0,1 );
	}

	public void onD_White( )
	{
		getMaterial( ).setDiffuseColor( 1,1,1 );
	}

	public void onD_Black( )
	{
		getMaterial( ).setDiffuseColor( 0,0,0 );
	}

	public void onD_Blue( )
	{
		getMaterial( ).setDiffuseColor( 0,0,1 );
	}

	public void onS_White( )
	{
		getMaterial( ).setSpecularColor( 1,1,1 );
	}

	public void onS_Black( )
	{
		getMaterial( ).setSpecularColor( 0,0,0 );
	}

	public void onS_Blue( )
	{
		getMaterial( ).setSpecularColor( 0,0,1 );
	}

	public void onOn( )
	{
		getMaterial( ).setLightingEnable( true );
	}

	public void onOff( )
	{
		getMaterial( ).setLightingEnable( false );
	}

	public void on1( )
	{
		getMaterial( ).setShininess( 1 );
	}

	public void on70( )
	{
		getMaterial( ).setShininess( 70 );
	}
}
