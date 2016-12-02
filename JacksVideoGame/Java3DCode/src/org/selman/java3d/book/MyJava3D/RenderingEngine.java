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
* Definition of the RenderingEngine interface. A RenderingEngine can
* rendering 3D geometry (described using a Java 3D GeometryArray)
* into a 2D Graphics context.
*/
public interface RenderingEngine
{
	/**
	* Adds a GeometryArray to the RenderingEngine. All GeometryArrays
   * will be rendered.
	*/
	public void addGeometry( GeometryArray geometryArray );   

	/**
	* Renders a single frame into the Graphics.
	*/
   public void render( Graphics graphics, GeometryUpdater updater );

   /**
   * Get the current Screen position used by the RenderEngine.
   */   
   public Vector3d getScreenPosition();

   /**
   * Get the current View Angle used by the RenderEngine. View angles
   * are expressed in degrees.
   */   
   public Vector3d getViewAngle();

   /**
   * Set the current View Angle used by the RenderEngine.
   */   
   public void setViewAngle( Vector3d viewAngle );

   /**
   * Get the current View Angle used by the RenderEngine. View angles
   * are expressed in degrees.
   */   
   public Vector3d getLightAngle();

   /**
   * Set the current View Angle used by the RenderEngine.
   */   
   public void setLightAngle( Vector3d angle );

   /**
   * Set the Screen size used by the RenderEngine.
   */      
   public void setScreenSize( int width, int height );

   /**
   * Set the scale used by the RenderEngine.
   */      
   public void setScale( double scale );

   /**
   * Get the scale used by the RenderEngine.
   */      
   public double getScale();
}
