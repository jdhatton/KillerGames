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

import com.sun.j3d.utils.image.*;

public class TextureComponent extends AppearanceComponent
{
	// hack, so we have an image observer when we load Textures
	public static Component			m_Component = null;
	private Texture					m_Texture = null;

	public TextureComponent( Appearance app )
	{
		super( app );
	}

	public static void setComponent( Component comp )
	{
		m_Component = comp;		
	}

	protected int[] getCapabilities( )
	{
		return new int[]
		{
			Texture.ALLOW_ENABLE_WRITE,
		};
	}

	protected NodeComponent createComponent( )
	{
		TextureLoader texLoader = new TextureLoader( "texture00.jpg", m_Component );
		ImageComponent2D image = texLoader.getImage( );
		Texture2D tex2D = new Texture2D( Texture.MULTI_LEVEL_MIPMAP, Texture.RGBA, image.getWidth( ), image.getHeight( ) );

		for( int n = 0; n <= 6; n++ )
		{
			texLoader = new TextureLoader( "texture0" + n + ".jpg", m_Component );
			tex2D.setImage( n, texLoader.getImage( ) );
		}

		return ( NodeComponent ) tex2D;
	}

	protected void setAppearanceCapability( )
	{
		m_Appearance.setCapability( Appearance.ALLOW_TEXTURE_WRITE );
	}

	protected void assignToAppearance( )
	{
		m_Texture = (Texture) new Texture2D( );		
		m_Texture = (Texture) m_NodeComponent.cloneNodeComponent( true );
		m_Appearance.setTexture( m_Texture );
	}

	protected void assignNullToAppearance( )
	{
		m_Appearance.setTexture( null );
	}


	protected String getName( )
	{
		return "Texture";
	}

	protected String[] getMenuItemNames( )
	{
		return new String[]
		{
			"-",
				"Boundary Color", 
				"-", 
				"White",
				"Black",
				"BlueTransparent",
				"-",
				"Boundary Mode S",
				"-",
				"S_CLAMP",
				"S_WRAP",
				"-",
				"Boundary Mode T",
				"-",
				"T_CLAMP",
				"T_WRAP",
				"-",
				"Enable",
				"-",
				"On",
				"Off",
				"-",
				"Image",
				"-",
				"Texture0",
				"Texture1",
				"Texture2",
				"-",
				"MagFilter",
				"-",
				"Mag_FASTEST",
				"Mag_NICEST",
				"Mag_BASE_LEVEL_POINT",
				"Mag_BASE_LEVEL_LINEAR",
				"-",
				"MinFilter",
				"-",
				"Min_FASTEST",
				"Min_NICEST",
				"Min_BASE_LEVEL_POINT",
				"Min_BASE_LEVEL_LINEAR",
				"Min_MULTI_LEVEL_POINT",
				"Min_MULTI_LEVEL_LINEAR",
				"-",
				"MipMap Mode",
				"-",
				"BASE_LEVEL",
				"MULTI_LEVEL_MIPMAP",
		};
	}

	private Texture getTexture( )
	{
		return ( Texture ) m_NodeComponent;
	}

	public void onWhite( )
	{
		getTexture( ).setBoundaryColor( 1,1,1,0 );
		assignToAppearance( );
	}

	public void onBlack( )
	{
		getTexture( ).setBoundaryColor( 0,0,0,0 );
		assignToAppearance( );
	}

	public void onBlueTransparent( )
	{
		getTexture( ).setBoundaryColor( 0,0,1,0.5f );
		assignToAppearance( );
	}

	public void onS_CLAMP( )
	{
		getTexture( ).setBoundaryModeS( Texture.CLAMP );
		assignToAppearance( );
	}

	public void onS_WRAP( )
	{
		getTexture( ).setBoundaryModeS( Texture.WRAP );
		assignToAppearance( );
	}

	public void onT_CLAMP( )
	{
		getTexture( ).setBoundaryModeT( Texture.CLAMP );
		assignToAppearance( );
	}

	public void onT_WRAP( )
	{
		getTexture( ).setBoundaryModeT( Texture.WRAP );
		assignToAppearance( );
	}

	public void onOn( )
	{
		getTexture( ).setEnable( true );
		assignToAppearance( );
	}

	public void onOff( )
	{
		getTexture( ).setEnable( false );
		assignToAppearance( );
	}

	public void onTexture0( )
	{
		TextureLoader texLoader = null;

		for( int n = 0; n <= 6; n++ )
		{
			texLoader = new TextureLoader( "texture0" + n + ".jpg", m_Component );
			getTexture( ).setImage( n, texLoader.getImage( ) );
		}

		assignToAppearance( );
	}

	public void onTexture1( )
	{
		TextureLoader texLoader = null;

		for( int n = 0; n <= 6; n++ )
		{
			texLoader = new TextureLoader( "texture1" + n + ".jpg", m_Component );
			getTexture( ).setImage( n, texLoader.getImage( ) );
		}

		assignToAppearance( );
	}

	public void onTexture2( )
	{
		TextureLoader texLoader = null;

		for( int n = 0; n <= 6; n++ )
		{
			texLoader = new TextureLoader( "texture2" + n + ".gif", m_Component );
			getTexture( ).setImage( n, texLoader.getImage( ) );
		}

		assignToAppearance( );
	}


	public void onMag_FASTEST( )
	{
		getTexture( ).setMagFilter( Texture.FASTEST );
		assignToAppearance( );
	}

	public void onMag_NICEST( )
	{
		getTexture( ).setMagFilter( Texture.NICEST );
		assignToAppearance( );
	}

	public void onMag_BASE_LEVEL_POINT( )
	{
		getTexture( ).setMagFilter( Texture.BASE_LEVEL_POINT );
		assignToAppearance( );
	}	

	public void onMag_BASE_LEVEL_LINEAR( )
	{
		getTexture( ).setMagFilter( Texture.BASE_LEVEL_LINEAR );
		assignToAppearance( );
	}

	public void onMin_FASTEST( )
	{
		getTexture( ).setMinFilter( Texture.FASTEST );
		assignToAppearance( );
	}

	public void onMin_NICEST( )
	{
		getTexture( ).setMinFilter( Texture.NICEST );
		assignToAppearance( );
	}

	public void onMin_BASE_LEVEL_POINT( )
	{
		getTexture( ).setMinFilter( Texture.BASE_LEVEL_POINT );
		assignToAppearance( );
	}	

	public void onMin_BASE_LEVEL_LINEAR( )
	{
		getTexture( ).setMinFilter( Texture.BASE_LEVEL_LINEAR );
		assignToAppearance( );
	}

	public void onMin_MULTI_LEVEL_POINT( )
	{
		getTexture( ).setMinFilter( Texture.MULTI_LEVEL_POINT );
		assignToAppearance( );
	}

	public void onMin_MULTI_LEVEL_LINEAR( )
	{
		getTexture( ).setMinFilter( Texture.MULTI_LEVEL_LINEAR );
		assignToAppearance( );
	}

	public void onBASE_LEVEL( )
	{
		getTexture( ).setMipMapMode( Texture.BASE_LEVEL );
		assignToAppearance( );
	}

	public void onMULTI_LEVEL_MIPMAP( )
	{
		getTexture( ).setMipMapMode( Texture.MULTI_LEVEL_MIPMAP );
		assignToAppearance( );
	}
}
