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

public class PointComponent extends AppearanceComponent
{
	public PointComponent( Appearance app )
	{
		super( app );
	}

	protected int[] getCapabilities( )
	{
		return new int[]
		{
			PointAttributes.ALLOW_ANTIALIASING_WRITE,
			PointAttributes.ALLOW_SIZE_WRITE
		};
	}

	protected NodeComponent createComponent( )
	{
		return ( NodeComponent ) new PointAttributes( );
	}

	protected void setAppearanceCapability( )
	{
		m_Appearance.setCapability( Appearance.ALLOW_POINT_ATTRIBUTES_WRITE );
	}

	protected void assignToAppearance( )
	{
		m_Appearance.setPointAttributes( (PointAttributes) m_NodeComponent );
	}

	protected void assignNullToAppearance( )
	{
		m_Appearance.setPointAttributes( null );
	}


	protected String getName( )
	{
		return "Point";
	}

	protected String[] getMenuItemNames( )
	{
		return new String[]
		{
			"-",
				"Antialiasing", 
				"-", 
				"On",
				"Off",
				"-",
				"Size",
				"-",
				"1",
				"5",
				"10" 
		};
	}

	private PointAttributes getPointAttributes( )
	{
		return ( PointAttributes ) m_NodeComponent;
	}

	public void onOn( )
	{
		getPointAttributes( ).setPointAntialiasingEnable( true );
	}

	public void onOff( )
	{
		getPointAttributes( ).setPointAntialiasingEnable( false );
	}

	public void on1( )
	{
		getPointAttributes( ).setPointSize( 1 );
	}

	public void on5( )
	{
		getPointAttributes( ).setPointSize( 5 );
	}

	public void on10( )
	{
		getPointAttributes( ).setPointSize( 10 );
	}
}
