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

package org.selman.java3d.book.myjava3d;

import java.awt.*;

import javax.media.j3d.*;
import javax.vecmath.*;

/**
* Surface (JPanel) that uses a RenderingEngine
* to render a 3D scene. A GeometryUpdater is used to 
* update the scene and RenderEngine parameters.
*/
public class RenderingSurface extends AnimatingSurface
{
	RenderingEngine engine = null;
	GeometryUpdater updater = null;

	public RenderingSurface( RenderingEngine engine, GeometryUpdater updater )
	{
		this.engine = engine;
		this.updater = updater;
       
       setBackground( Color.gray );
	}

	public void render( int w, int h, Graphics2D g2 )
	{
   	engine.setScreenSize( w, h );
		engine.render( g2, updater );
	}

	public void step( int w, int h )
	{
	}

	public void reset( int newwidth, int newheight )
	{
	}
}
