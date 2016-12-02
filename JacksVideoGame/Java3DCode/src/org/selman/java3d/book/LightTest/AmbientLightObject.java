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

package org.selman.java3d.book.lighttest;

import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.geometry.*;

import java.util.*;
import java.awt.*;

public class AmbientLightObject extends LightObject
{		
	public AmbientLightObject( )
	{
	}

	protected Light createLight( )
	{
		return ( Light ) new AmbientLight( );
	}

	public String getName( )
	{
		return "AmbientLight";
	}


	protected int[] getCapabilities( )
	{
		return super.getCapabilities( );
	}

	public Group createGeometry( )
	{
		return super.createGeometry( );
	}

	public void addUiToPanel( Panel panel )
	{
		panel.add( new Label( "AmbientLight" ) );
		super.addUiToPanel( panel );
	}

	public void synchLightToUi( )
	{
		super.synchLightToUi( );
	}

	public void synchUiToLight( )
	{
		super.synchUiToLight( );
	}
}
