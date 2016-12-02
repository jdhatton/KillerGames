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

package org.selman.java3d.book.behaviortest;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;

import javax.media.j3d.*;
import javax.vecmath.*;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.geometry.*;

// this class implements a simple behavior that
// calculates and prints the size of an object
// based on the vertices in its GeometryArray

public class ObjectSizeBehavior extends Behavior
{
	// the wake up condition for the behavior
	protected WakeupCondition		m_WakeupCondition = null;

	// the GeometryArray for the Shape3D that we are querying
	protected GeometryArray			m_GeometryArray = null;

	// cache some information on the model to save reallocation
	protected float[]				m_CoordinateArray = null;

		protected BoundingBox		m_BoundingBox = null;
	protected Point3d 				m_Point = null;;


	public ObjectSizeBehavior( GeometryArray geomArray )
	{
		// save the GeometryArray that we are modifying
		m_GeometryArray = geomArray;

		// set the capability bits that the behavior requires
		m_GeometryArray.setCapability( GeometryArray.ALLOW_COORDINATE_READ );
		m_GeometryArray.setCapability( GeometryArray.ALLOW_COUNT_READ );

		// allocate an array for the coordinates
		m_CoordinateArray = new float[ 3 * m_GeometryArray.getVertexCount( ) ];

		// create the BoundingBox used to 
		// calculate the size of the object
		m_BoundingBox = new BoundingBox( );

		// create a temporary point
		m_Point = new Point3d( );

		// create the WakeupCriterion for the behavior
		WakeupCriterion criterionArray[] = new WakeupCriterion[1];
		criterionArray[0] = new WakeupOnElapsedFrames( 20 );

		// save the WakeupCriterion for the behavior
		m_WakeupCondition = new WakeupOr( criterionArray );
	}

	public void initialize( )
	{
		// apply the initial WakeupCriterion
		wakeupOn( m_WakeupCondition );
	}

	public void processStimulus( java.util.Enumeration criteria )
	{				
		while( criteria.hasMoreElements( ) )
		{
			WakeupCriterion wakeUp = (WakeupCriterion) criteria.nextElement( );			

			// every N frames, recalculate the bounds 
			// for the points in the GeometryArray
			if( wakeUp instanceof WakeupOnElapsedFrames )
			{
				// get all the coordinates
				m_GeometryArray.getCoordinates( 0, m_CoordinateArray );

				// clear the old BoundingBox
				m_BoundingBox.setLower( 0,0,0 );
				m_BoundingBox.setUpper( 0,0,0 );

				// loop over every vertex and combine with the BoundingBox
				for( int n = 0; n < m_CoordinateArray.length; n+=3 )
				{
					m_Point.x = m_CoordinateArray[n];
					m_Point.y = m_CoordinateArray[n+1];
					m_Point.z = m_CoordinateArray[n+2];

					m_BoundingBox.combine( m_Point );
				}

				System.out.println( m_BoundingBox.toString( ) );
			}
		}

		// assign the next WakeUpCondition, so we are notified again
		wakeupOn( m_WakeupCondition );
	}
}
