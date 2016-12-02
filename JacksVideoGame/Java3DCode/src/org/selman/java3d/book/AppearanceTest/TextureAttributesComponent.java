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

public class TextureAttributesComponent extends AppearanceComponent
{
	public TextureAttributesComponent( Appearance app )
	{
		super( app );
	}

	protected int[] getCapabilities( )
	{
		return new int[]
		{
			TextureAttributes.ALLOW_BLEND_COLOR_WRITE,
			TextureAttributes.ALLOW_MODE_WRITE,
			TextureAttributes.ALLOW_TRANSFORM_WRITE,
		};
	}

	protected NodeComponent createComponent( )
	{
		return ( NodeComponent ) new TextureAttributes( );
	}

	protected void setAppearanceCapability( )
	{
		m_Appearance.setCapability( Appearance.ALLOW_TEXTURE_ATTRIBUTES_WRITE );
	}

	protected void assignToAppearance( )
	{
		m_Appearance.setTextureAttributes( (TextureAttributes) m_NodeComponent );
	}

	protected void assignNullToAppearance( )
	{
		m_Appearance.setTextureAttributes( null );
	}


	protected String getName( )
	{
		return "TextureAttributes";
	}

	protected String[] getMenuItemNames( )
	{
		return new String[]
		{
			"-",
				"Mode", 
				"-", 
				"MODULATE",
				"DECAL",
				"BLEND",
				"REPLACE",
				"-",
				"Blend Color", 
				"-", 
				"T_White_Alpha_0point3",
				"T_Black_Alpha_0point7",
				"T_Blue_Alpha_1",
				"-",
				"Transform",
				"-",
				"0_degrees",
				"X_30_degrees",
				"Y_30_degrees",
				"Z_30_degrees",
				"-",
				"Perspective Correction",
				"-",
				"NICEST",
				"FASTEST"
		};
	}

	private TextureAttributes getTextureAttributes( )
	{
		return ( TextureAttributes ) m_NodeComponent;
	}

	public void onMODULATE( )
	{
		getTextureAttributes( ).setTextureMode( TextureAttributes.MODULATE );
	}

	public void onDECAL( )
	{
		getTextureAttributes( ).setTextureMode( TextureAttributes.DECAL );
	}

	public void onBLEND( )
	{
		getTextureAttributes( ).setTextureMode( TextureAttributes.BLEND );
	}

	public void onREPLACE( )
	{
		getTextureAttributes( ).setTextureMode( TextureAttributes.REPLACE );
	}

	public void onT_White_Alpha_0point3( )
	{
		getTextureAttributes( ).setTextureBlendColor( 1,1,1,0.3f );
	}

	public void onT_Black_Alpha_0point7( )
	{
		getTextureAttributes( ).setTextureBlendColor( 0,0,0, 0.7f );
	}

	public void onT_Blue_Alpha_1( )
	{
		getTextureAttributes( ).setTextureBlendColor( 0,0,1,1 );
	}

	public void on0_degrees( )
	{
		Transform3D t3d = new Transform3D( );
		getTextureAttributes( ).setTextureTransform( t3d );
	}

	public void onX_30_degrees( )
	{
		Transform3D t3d = new Transform3D( );
		t3d.rotX( Math.toRadians( 30 ) );
		getTextureAttributes( ).setTextureTransform( t3d );
	}

	public void onY_30_degrees( )
	{
		Transform3D t3d = new Transform3D( );
		t3d.rotY( Math.toRadians( 30 ) );
		getTextureAttributes( ).setTextureTransform( t3d );
	}

	public void onZ_30_degrees( )
	{
		Transform3D t3d = new Transform3D( );
		t3d.rotZ( Math.toRadians( 30 ) );
		getTextureAttributes( ).setTextureTransform( t3d );
	}

	public void onNICEST( )
	{
		getTextureAttributes( ).setPerspectiveCorrectionMode( TextureAttributes.NICEST );
	}

	public void onFASTEST( )
	{
		getTextureAttributes( ).setPerspectiveCorrectionMode( TextureAttributes.FASTEST );
	}
}
