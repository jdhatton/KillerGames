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

public class ColoringComponent extends AppearanceComponent
{
	public ColoringComponent( Appearance app )
	{
		super( app );
	}

	protected int[] getCapabilities( )
	{
		return new int[]
		{
			ColoringAttributes.ALLOW_COLOR_WRITE,
			ColoringAttributes.ALLOW_SHADE_MODEL_WRITE
		};
	}

	protected NodeComponent createComponent( )
	{
		return ( NodeComponent ) new ColoringAttributes( );
	}

	protected void setAppearanceCapability( )
	{
		m_Appearance.setCapability( Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE );
	}

	protected void assignToAppearance( )
	{
		m_Appearance.setColoringAttributes( (ColoringAttributes) m_NodeComponent );
	}

	protected void assignNullToAppearance( )
	{
		m_Appearance.setColoringAttributes( null );
	}

	protected String getName( )
	{
		return "Coloring";
	}

	protected String[] getMenuItemNames( )
	{
		return new String[]
		{
			"-",
				"Color", 
				"-", 
				"Red",
				"Green",
				"Blue",
				"-",
				"Shade Model",
				"-",
				"Fastest",
				"Nicest",
				"Flat",
				"Gouraud" 
		};
	}

	private ColoringAttributes getColoringAttributes( )
	{
		return ( ColoringAttributes ) m_NodeComponent;
	}


	public void onRed( )
	{
		getColoringAttributes( ).setColor( 1,0,0 );
	}

	public void onGreen( )
	{
		getColoringAttributes( ).setColor( 0,1,0 );
	}

	public void onBlue( )
	{
		getColoringAttributes( ).setColor( 0,0,1 );
	}

	public void onFastest( )
	{
		getColoringAttributes( ).setShadeModel( ColoringAttributes.FASTEST );
	}

	public void onNicest( )
	{
		getColoringAttributes( ).setShadeModel( ColoringAttributes.NICEST );
	}

	public void onFlat( )
	{
		getColoringAttributes( ).setShadeModel( ColoringAttributes.SHADE_FLAT );
	}

	public void onGouraud( )
	{
		getColoringAttributes( ).setShadeModel( ColoringAttributes.SHADE_GOURAUD );
	}
}
