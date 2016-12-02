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
import java.util.Vector;

public class TornadoRotation extends RotationInterpolator
{
	Vector	m_Text3DVector = null;


	public TornadoRotation( Alpha alpha, TransformGroup target, Transform3D axisOfRotation, float minimumAngle, float maximumAngle )
	{
		super( alpha, target, axisOfRotation, minimumAngle, maximumAngle );

		m_Text3DVector = new Vector( );
	}

	public void addTornadoText3D( TornadoText3D text3D )
	{
		m_Text3DVector.add( text3D );
	}

	public void processStimulus( java.util.Enumeration criteria )
	{
		super.processStimulus( criteria );

		/*
		for( int n = 0; n < m_Text3DVector.size(); n++ )
		{
		((TornadoText3D) m_Text3DVector.get( n )).onFrameUpdate();
		}
		*/
	}
}
