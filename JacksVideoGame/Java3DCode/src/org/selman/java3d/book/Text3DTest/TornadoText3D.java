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

package org.selman.java3d.book.text3dtest;

import javax.media.j3d.*;
import javax.vecmath.*;
import java.awt.*;


public class TornadoText3D extends Text3D
{	
	public TornadoText3D( )
	{
		super( );
		setDefaultAttributes( );
	}

	public TornadoText3D( Font3D font3D )
	{
		super( font3D );
		setDefaultAttributes( );
	}

	public TornadoText3D( Font3D font3D, java.lang.String string )
	{
		super( font3D, string );
		setDefaultAttributes( );
	}

	public TornadoText3D( Font3D font3D, java.lang.String string, Point3f position )
	{
		super( font3D,string,position );
		setDefaultAttributes( );
	}

	public TornadoText3D( Font3D font3D, java.lang.String string, Point3f position, int alignment, int path )
	{
		super( font3D,string,position,alignment,path );
		setDefaultAttributes( );
	}

	protected void setDefaultAttributes( )
	{
		setCapability( ALLOW_ALIGNMENT_READ );
		setCapability( ALLOW_ALIGNMENT_WRITE );

		setCapability( ALLOW_CHARACTER_SPACING_READ );
		setCapability( ALLOW_CHARACTER_SPACING_WRITE );

		setCapability( ALLOW_FONT3D_READ );
		setCapability( ALLOW_FONT3D_WRITE );

		setCapability( ALLOW_PATH_READ );
		setCapability( ALLOW_PATH_WRITE );

		setCapability( ALLOW_POSITION_READ );
		setCapability( ALLOW_POSITION_READ );

		setCapability( ALLOW_STRING_READ );
		setCapability( ALLOW_STRING_WRITE );
	}

	public void onFrameUpdate( )
	{
		setString( getString( ) + "." );
	}	
}
