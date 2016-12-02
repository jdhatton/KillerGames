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


public class ExplodeBehavior extends Behavior
{
	// the wake up condition for the behavior
	protected WakeupCondition				m_InitialWakeupCondition = null;
	protected WakeupCondition				m_FrameWakeupCondition = null;


	// the GeometryArray for the Shape3D that we are modifying
	protected Shape3D						m_Shape3D = null;
	protected GeometryArray					m_GeometryArray = null;

	protected float[]						m_CoordinateArray = null;

	protected float[]						m_OriginalCoordinateArray = null;
	protected Appearance					m_Appearance = null;

	protected TransparencyAttributes		m_TransparencyAttributes = null;

	protected int							m_nElapsedTime = 0;
	protected int							m_nNumFrames  = 0;
	protected int							m_nFrameNumber = 0;

	protected Vector3f 						m_Vector = null;

	ExplosionListener						m_Listener = null;


	public ExplodeBehavior( Shape3D shape3D, int nElapsedTime, int nNumFrames, ExplosionListener listener )
	{
		// allocate a temporary vector
		m_Vector = new Vector3f( );

		m_FrameWakeupCondition = new WakeupOnElapsedFrames( 1 );

		restart( shape3D, nElapsedTime, nNumFrames, listener );
	}

	public WakeupCondition restart( Shape3D shape3D, int nElapsedTime, int nNumFrames, ExplosionListener listener )
	{
		System.out.println( "Will explode after: " + nElapsedTime/1000 + " secs." );

		m_Shape3D = shape3D;
		m_nElapsedTime = nElapsedTime;
		m_nNumFrames = nNumFrames;
		m_nFrameNumber = 0;

		// create the WakeupCriterion for the behavior		
		m_InitialWakeupCondition = new WakeupOnElapsedTime( m_nElapsedTime );

		m_Listener = listener;

		// save the GeometryArray that we are modifying
		m_GeometryArray = (GeometryArray) m_Shape3D.getGeometry( );

		if( m_Shape3D.isLive( ) == false && m_Shape3D.isCompiled( ) == false )
		{
			// set the capability bits that the behavior requires
			m_Shape3D.setCapability( Shape3D.ALLOW_APPEARANCE_READ );
			m_Shape3D.setCapability( Shape3D.ALLOW_APPEARANCE_WRITE );

			m_Shape3D.getAppearance( ).setCapability( Appearance.ALLOW_POINT_ATTRIBUTES_WRITE );
			m_Shape3D.getAppearance( ).setCapability( Appearance.ALLOW_POLYGON_ATTRIBUTES_WRITE );
			m_Shape3D.getAppearance( ).setCapability( Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_WRITE );
			m_Shape3D.getAppearance( ).setCapability( Appearance.ALLOW_TEXTURE_WRITE );

			m_GeometryArray.setCapability( GeometryArray.ALLOW_COORDINATE_READ );
			m_GeometryArray.setCapability( GeometryArray.ALLOW_COORDINATE_WRITE );
			m_GeometryArray.setCapability( GeometryArray.ALLOW_COUNT_READ );
		}

		// make a copy of the object's original appearance
		m_Appearance = new Appearance( );
		m_Appearance = (Appearance) m_Shape3D.getAppearance( ).cloneNodeComponent( true );

		// allocate an array for the model coordinates
		m_CoordinateArray = new float[ 3 * m_GeometryArray.getVertexCount( ) ];

		// make a copy of the models original coordinates
		m_OriginalCoordinateArray = new float[ 3 * m_GeometryArray.getVertexCount( ) ];
		m_GeometryArray.getCoordinates( 0, m_OriginalCoordinateArray );

		// start (or restart) the behavior
		setEnable( true );

		return m_InitialWakeupCondition;
	}

	public void initialize( )
	{
		// apply the initial WakeupCriterion
		wakeupOn( m_InitialWakeupCondition );
	}

	public void processStimulus( java.util.Enumeration criteria )
	{
		while( criteria.hasMoreElements( ) )
		{
			WakeupCriterion wakeUp = (WakeupCriterion) criteria.nextElement( );			

			if( wakeUp instanceof WakeupOnElapsedTime )
			{
				// we are starting the explosion, apply the 
				// appearance changes we require
				PolygonAttributes polyAttribs = new PolygonAttributes( PolygonAttributes.POLYGON_POINT, PolygonAttributes.CULL_NONE, 0 );
				m_Shape3D.getAppearance( ).setPolygonAttributes( polyAttribs );

				PointAttributes pointAttribs = new PointAttributes( 3, false );
				m_Shape3D.getAppearance( ).setPointAttributes( pointAttribs );

				m_Shape3D.getAppearance( ).setTexture( null );

				m_TransparencyAttributes = new TransparencyAttributes( TransparencyAttributes.NICEST, 0 );
				m_TransparencyAttributes.setCapability( TransparencyAttributes.ALLOW_VALUE_WRITE );
				m_Shape3D.getAppearance( ).setTransparencyAttributes( m_TransparencyAttributes );
			}
			else
			{
				// we are mid explosion, modify the GeometryArray
				m_nFrameNumber++;

				m_GeometryArray.getCoordinates( 0, m_CoordinateArray );

				m_TransparencyAttributes.setTransparency( ((float) m_nFrameNumber) / ((float) m_nNumFrames) );
				m_Shape3D.getAppearance( ).setTransparencyAttributes( m_TransparencyAttributes );

				for( int n = 0; n < m_CoordinateArray.length; n+=3 )
				{
					m_Vector.x = m_CoordinateArray[n];
					m_Vector.y = m_CoordinateArray[n+1];
					m_Vector.z = m_CoordinateArray[n+2];

					m_Vector.normalize( );

					m_CoordinateArray[n] += m_Vector.x * Math.random( ) + Math.random( );
					m_CoordinateArray[n+1] += m_Vector.y * Math.random( ) + Math.random( );
					m_CoordinateArray[n+2] += m_Vector.z * Math.random( ) + Math.random( );
				}

				// assign the new coordinates
				m_GeometryArray.setCoordinates( 0, m_CoordinateArray );
			}
		}

		if( m_nFrameNumber < m_nNumFrames )
		{
			// assign the next WakeUpCondition, so we are notified again
			wakeupOn( m_FrameWakeupCondition );
		}
		else
		{
			// we are at the end of the explosion
			// reapply the original appearance and GeometryArray 
			// coordinates
			setEnable( false );
			m_Shape3D.setAppearance( m_Appearance );

			m_GeometryArray.setCoordinates( 0, m_OriginalCoordinateArray );

			m_OriginalCoordinateArray = null;
			m_GeometryArray = null;
			m_CoordinateArray = null;
			m_TransparencyAttributes = null;

			// if we have a listener notify them that we are done
			if( m_Listener != null )
				wakeupOn( m_Listener.onExplosionFinished( this, m_Shape3D ) );
		}
	}
}
