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

public class TexGenComponent extends AppearanceComponent
{
	TexCoordGeneration		m_TexCoordGeneration = null;
	final float					m_PlaneFactor = 0.02f;


	public TexGenComponent( Appearance app )
	{
		super( app );
	}

	protected int[] getCapabilities( )
	{
		return new int[]
		{
			TexCoordGeneration.ALLOW_ENABLE_WRITE
		};
	}

	protected NodeComponent createComponent( )
	{
		return ( NodeComponent ) new TexCoordGeneration( TexCoordGeneration.OBJECT_LINEAR, TexCoordGeneration.TEXTURE_COORDINATE_3 );
	}

	protected void setAppearanceCapability( )
	{
		m_Appearance.setCapability( Appearance.ALLOW_TEXGEN_WRITE );
	}

	protected void assignToAppearance( )
	{
		m_TexCoordGeneration = new TexCoordGeneration( );
		m_TexCoordGeneration.duplicateNodeComponent( m_NodeComponent );
		m_Appearance.setTexCoordGeneration( m_TexCoordGeneration );
	}

	protected void assignNullToAppearance( )
	{
		m_Appearance.setTexCoordGeneration( null );
	}


	protected String getName( )
	{
		return "TexCoordGen";
	}

	protected String[] getMenuItemNames( )
	{
		return new String[]
		{
			"-",
				"Enable", 
				"-", 
				"On",
				"Off",
				"-",
				"GenMode",
				"-",
				"EYE_LINEAR",
				"OBJECT_LINEAR",
				"SPHERE_MAP",
				"-",
				"Format",
				"-",
				"TEXTURE_COORDINATE_2",
				"TEXTURE_COORDINATE_3",
				"-",
				"Plane R",
				"-",
				"R_1_0_0_0",
				"R_0_1_0_5",
				"R_0_0_1_0",
				"R_0_0_0_1",
				"-",
				"Plane S",
				"-",
				"S_1_0_0_0",
				"S_0_1_0_5",
				"S_0_0_1_0",
				"S_0_0_0_1",
				"-",
				"Plane T",
				"-",
				"T_1_0_0_0",
				"T_0_1_0_5",
				"T_0_0_1_0",
				"T_0_0_0_1",
		};
	}

	private TexCoordGeneration getTexCoordGeneration( )
	{
		return ( TexCoordGeneration ) m_NodeComponent;
	}

	public void onOn( )
	{
		getTexCoordGeneration( ).setEnable( true );
		assignToAppearance( );
	}

	public void onOff( )
	{
		getTexCoordGeneration( ).setEnable( false );
		assignToAppearance( );
	}

	public void onEYE_LINEAR( )
	{
		getTexCoordGeneration( ).setGenMode( TexCoordGeneration.EYE_LINEAR );
		assignToAppearance( );
	}

	public void onOBJECT_LINEAR( )
	{
		getTexCoordGeneration( ).setGenMode( TexCoordGeneration.OBJECT_LINEAR );
		assignToAppearance( );
	}

	public void onSPHERE_MAP( )
	{
		getTexCoordGeneration( ).setGenMode( TexCoordGeneration.SPHERE_MAP );
		assignToAppearance( );
	}

	public void onTEXTURE_COORDINATE_2( )
	{
		getTexCoordGeneration( ).setFormat( TexCoordGeneration.TEXTURE_COORDINATE_2 );
		assignToAppearance( );
	}

	public void onTEXTURE_COORDINATE_3( )
	{
		getTexCoordGeneration( ).setFormat( TexCoordGeneration.TEXTURE_COORDINATE_3 );
		assignToAppearance( );
	}

	public void onR_1_0_0_0( )
	{
		getTexCoordGeneration( ).setPlaneR( new Vector4f( m_PlaneFactor,0,0,0 ) );
		assignToAppearance( );
	}

	public void onR_0_1_0_5( )
	{
		getTexCoordGeneration( ).setPlaneR( new Vector4f( 0,m_PlaneFactor,0,0.5f ) );
		assignToAppearance( );
	}

	public void onR_0_0_1_0( )
	{
		getTexCoordGeneration( ).setPlaneR( new Vector4f( 0,0,m_PlaneFactor,0 ) );
		assignToAppearance( );
	}

	public void onR_0_0_0_1( )
	{
		getTexCoordGeneration( ).setPlaneR( new Vector4f( 0,0,0,m_PlaneFactor ) );
		assignToAppearance( );
	}

	public void onS_1_0_0_0( )
	{
		getTexCoordGeneration( ).setPlaneS( new Vector4f( m_PlaneFactor,0,0,0 ) );
		assignToAppearance( );
	}

	public void onS_0_1_0_5( )
	{
		getTexCoordGeneration( ).setPlaneS( new Vector4f( 0,m_PlaneFactor,0,0.5f ) );
		assignToAppearance( );
	}

	public void onS_0_0_1_0( )
	{
		getTexCoordGeneration( ).setPlaneS( new Vector4f( 0,0,m_PlaneFactor,0 ) );
		assignToAppearance( );
	}

	public void onS_0_0_0_1( )
	{
		getTexCoordGeneration( ).setPlaneS( new Vector4f( 0,0,0,m_PlaneFactor ) );
		assignToAppearance( );
	}

	public void onT_1_0_0_0( )
	{
		getTexCoordGeneration( ).setPlaneT( new Vector4f( m_PlaneFactor,0,0,0 ) );
		assignToAppearance( );
	}

	public void onT_0_1_0_5( )
	{
		getTexCoordGeneration( ).setPlaneT( new Vector4f( 0,m_PlaneFactor,0,0.5f ) );
		assignToAppearance( );
	}

	public void onT_0_0_1_0( )
	{
		getTexCoordGeneration( ).setPlaneT( new Vector4f( 0,0,m_PlaneFactor,0 ) );
		assignToAppearance( );
	}

	public void onT_0_0_0_1( )
	{
		getTexCoordGeneration( ).setPlaneT( new Vector4f( 0,0,0,m_PlaneFactor ) );
		assignToAppearance( );
	}
}
