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

public class PolygonComponent extends AppearanceComponent
{
	public PolygonComponent( Appearance app )
	{
		super( app );
	}

	protected int[] getCapabilities( )
	{
		return new int[]
		{
			PolygonAttributes.ALLOW_CULL_FACE_WRITE,
			PolygonAttributes.ALLOW_MODE_WRITE,
			PolygonAttributes.ALLOW_NORMAL_FLIP_WRITE,
			PolygonAttributes.ALLOW_OFFSET_WRITE
		};
	}

	protected NodeComponent createComponent( )
	{
		return ( NodeComponent ) new PolygonAttributes( );
	}

	protected void setAppearanceCapability( )
	{
		m_Appearance.setCapability( Appearance.ALLOW_POLYGON_ATTRIBUTES_WRITE );
	}

	protected void assignToAppearance( )
	{
		m_Appearance.setPolygonAttributes( (PolygonAttributes) m_NodeComponent );
	}

	protected void assignNullToAppearance( )
	{
		m_Appearance.setPolygonAttributes( null );
	}


	protected String getName( )
	{
		return "Polygon";
	}

	protected String[] getMenuItemNames( )
	{
		return new String[]
		{
			"-",
				"Cull", 
				"-", 
				"Back",
				"Front",
				"None",
				"-",
				"Mode",
				"-",
				"Fill",
				"Line",
				"Point",
				"-",
				"Normal",
				"-",
				"Flip_ON",
				"Flip_OFF",
				"-",
				"Offset",
				"-",
				"0",
				"10",
				"50",
				"200" 
		};
	}

	private PolygonAttributes getPolygonAttributes( )
	{
		return ( PolygonAttributes ) m_NodeComponent;
	}


	public void onBack( )
	{
		getPolygonAttributes( ).setCullFace( PolygonAttributes.CULL_BACK );
	}

	public void onFront( )
	{
		getPolygonAttributes( ).setCullFace( PolygonAttributes.CULL_FRONT );
	}

	public void onNone( )
	{
		getPolygonAttributes( ).setCullFace( PolygonAttributes.CULL_NONE );
	}

	public void onFill( )
	{
		getPolygonAttributes( ).setPolygonMode( PolygonAttributes.POLYGON_FILL );
	}

	public void onLine( )
	{
		getPolygonAttributes( ).setPolygonMode( PolygonAttributes.POLYGON_LINE );
	}

	public void onPoint( )
	{
		getPolygonAttributes( ).setPolygonMode( PolygonAttributes.POLYGON_POINT );
	}

	public void onFlip_ON( )
	{
		getPolygonAttributes( ).setBackFaceNormalFlip( true );
	}

	public void onFlip_OFF( )
	{
		getPolygonAttributes( ).setBackFaceNormalFlip( false );
	}

	public void on0( )
	{
		getPolygonAttributes( ).setPolygonOffset( 0 );
	}

	public void on10( )
	{
		getPolygonAttributes( ).setPolygonOffset( 10 );
	}

	public void on50( )
	{
		getPolygonAttributes( ).setPolygonOffset( 50 );
	}

	public void on200( )
	{
		getPolygonAttributes( ).setPolygonOffset( 200 );
	}
}
