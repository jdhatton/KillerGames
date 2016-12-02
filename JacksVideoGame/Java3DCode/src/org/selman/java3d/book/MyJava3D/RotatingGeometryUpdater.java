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
* Implementation of the GeometryUpdater interface.
* That rotates the scene by changing the viewer position
* and the scale factor for the model.
*/
public class RotatingGeometryUpdater implements GeometryUpdater
{
	long lastFrame = -1;

	public RotatingGeometryUpdater ( )
	{
	}

	public boolean update( Graphics graphics, RenderingEngine engine, GeometryArray geometry, int index, long frameNumber )
	{
		if ( lastFrame != frameNumber )
		{
       	lastFrame = frameNumber;
			Vector3d viewAngle = engine.getViewAngle( );
			viewAngle.x += 1;
			//viewAngle.y += 2;
           engine.setViewAngle( viewAngle );

           //Vector3d lightAngle = engine.getLightAngle( );
           //lightAngle.x += 5;
           //lightAngle.y += 2;
           //engine.setLightAngle( lightAngle );

			//engine.setScale( (50 * Math.sin( Math.PI * (System.currentTimeMillis() % 5000) / 5000.0 )) + 1 );
		}
       
       return false;
	}
}
