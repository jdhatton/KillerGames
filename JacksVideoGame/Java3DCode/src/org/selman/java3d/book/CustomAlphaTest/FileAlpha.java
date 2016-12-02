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

package org.selman.java3d.book.customalphatest;

import java.util.*;
import java.io.*;
import java.net.*;

import javax.media.j3d.*;
import javax.vecmath.*;

// this class defines an Alpha class that reads time/alpha 
// value pairs from a file and linearly interpolates between
// them. It illustrates creating your own Alpha class for
// interpolation.
public class FileAlpha extends Alpha
{
	// store a Vector of AlphaPairs that define 
	// the Alpha value for each time
	protected Vector					m_AlphaVector = null;

	// we override tracking of start and stop time
	protected long						m_StartTime = 0;
	protected long						m_StopTime = 0;

	// we override tracking of loop count
	protected int						m_nLoopCount = -1;

	protected FileAlphaListener	m_Listener = null;

	public FileAlpha( URL url )
	{
		this( url, null );
	}

	public FileAlpha( URL url, FileAlphaListener listener )
	{
		m_Listener = listener;

		// create the vector used to store the AlphaPairs
		m_AlphaVector = new Vector( );

		// read the AlphaPairs from the text file
		readAlphaValues( url );

		System.out.println( "Read " + getNumValues( ) + " Alpha pairs." );

		// initialize the start/stop times
		m_StartTime = System.currentTimeMillis( );
		m_StopTime = m_StartTime + getMaxTime( );
	}


	// this method is overriden to update the stop time
	public void setStartTime( long l )
	{
		m_StartTime = l;

		if( m_nLoopCount > 0 )
			m_StopTime = m_StartTime + m_nLoopCount * getMaxTime( );
		else
			m_StopTime = m_StartTime + getMaxTime( );
	}

	// overriden to return our member variable
	public long getStartTime( )
	{
		return m_StartTime;
	}

	public long getStopTime( )
	{
		return m_StopTime;
	}

	// overriden to update the stop time
	public void setLoopCount( int i )
	{
		m_nLoopCount = i;

		if( m_nLoopCount > 0 )
			m_StopTime = m_StartTime + i * getMaxTime( );
		else
			m_StopTime = m_StartTime + getMaxTime( );
	}

	// overriden to return our member variable
	public int getLoopCount( )
	{
		return m_nLoopCount;
	}

	// overriden to use our member variables
	public boolean finished( )
	{
		if( m_nLoopCount == -1 )
			return false;

		return ( System.currentTimeMillis( ) - m_StartTime > m_StopTime );
	}

	// core method override
	// returns the Alpha value for a given time
	public float value( long time )
	{
		if( time >= m_StartTime )
			return valueFromStart( time - m_StartTime );

		return 0;
	}

	// helper method to retrieve the AlphaPair
	// to the left and right of the time and
	// linearly interpolate between them.
	//
	// Note: this method could be optimized!
	protected float valueFromStart( long time )
	{			
		long modTime = time;
		float value = 0;

		if( time > getMaxTime( ) && getMaxTime( ) > 0 )
			modTime = time % getMaxTime( );

		AlphaPair leftPair = getLeftPairFromTime( modTime );
		AlphaPair rightPair = getRightPairFromTime( modTime );

		if( leftPair != null && rightPair != null )
		{
			float deltaAlpha = rightPair.getAlpha( ) - leftPair.getAlpha( );
			long deltaTime = rightPair.getTime( ) - leftPair.getTime( );
			float slope = 0;

			if( deltaTime != 0 )
				slope = deltaAlpha / deltaTime;

			long subTime = modTime - leftPair.getTime( );
			value = leftPair.getAlpha( ) + subTime * slope;

			if( m_Listener != null )
				m_Listener.onFileAlphaGetValue( time, value );
		}

		return value;
	}

	// returns the AlphaPair for a given index
	protected AlphaPair getAlphaPairForIndex( int nIndex )
	{
		if( nIndex < 0 || nIndex >= m_AlphaVector.size( ) )
			return null;

		return ( AlphaPair ) m_AlphaVector.get( nIndex );
	}

	// returns an AlphaPair immediately to the 
	// left of the given time
	protected AlphaPair getLeftPairFromTime( long time )
	{
		AlphaPair alphaPair = null;

		for( int nIndex = 0; nIndex < getNumValues( ); nIndex++ )
		{
			alphaPair = getAlphaPairForIndex( nIndex );

			if( alphaPair != null )
			{
				if( alphaPair.getTime( ) == time )
					return alphaPair;

				if( alphaPair.getTime( ) > time )
				{
					if( nIndex > 0 )
						return getAlphaPairForIndex( nIndex-1 );
					else
						break;
				}
			}
		}

		return alphaPair;
	}

	// returns an AlphaPair immediately to the 
	// right of the given time
	protected AlphaPair getRightPairFromTime( long time )
	{
		AlphaPair alphaPair = null;

		for( int nIndex = 0; nIndex < getNumValues( ); nIndex++ )
		{
			alphaPair = getAlphaPairForIndex( nIndex );

			if( alphaPair != null && alphaPair.getTime( ) >= time )
				return alphaPair;
		}

		return alphaPair;
	}

	// returns the number of AlphaPairs loaded
	public int getNumValues( )
	{
		return m_AlphaVector.size( );
	}

	// returns the time for the AlphaPair at
	// a given index
	public long getTimeForValue( int nIndex )
	{
		AlphaPair alphaPair = getAlphaPairForIndex( nIndex );

		if( alphaPair != null )
		{
			return getAlphaPairForIndex( nIndex ).getTime( );
		}

		return 0;
	}

	// returns the maximum loaded AlphaPair time
	public long getMaxTime( )
	{
		return getTimeForValue( getNumValues( )-1 );
	}


	// read the AlphaPairs from a file.
	protected void readAlphaValues( URL url )
	{		
		// allocate a temporary buffer to store the input file
		StringBuffer szBufferData = new StringBuffer( );

		try
		{
			InputStream inputStream = url.openStream( );

			int nChar = 0;

			// read the entire file into the StringBuffer
			while( true )
			{
				nChar = inputStream.read( );

				// if we have not hit the end of file 
				// add the character to the StringBuffer
				if( nChar != -1 )
					szBufferData.append( (char) nChar );
				else
					// EOF
					break;
			}

			inputStream.close( );

			// create a tokenizer to tokenize the input file at whitespace
			java.util.StringTokenizer tokenizer = new java.util.StringTokenizer( szBufferData.toString( ) );

			// keep reading time/alpha pairs until we hit EOF
			while( true )
			{
				try
				{
					long time = Long.parseLong( tokenizer.nextToken( ) );
					float alpha = Float.parseFloat( tokenizer.nextToken( ) );

					m_AlphaVector.add( new AlphaPair( time, alpha ) );
				}
				catch( Exception e )
				{
					break;
				}
			}
		}
		catch( Exception e )
		{
			System.err.println( e.toString( ) );
		}
	}
}
