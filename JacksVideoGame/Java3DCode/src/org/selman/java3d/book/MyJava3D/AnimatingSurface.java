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


/**
 * This class implements a rendering surface that
 * will repaint itself continiously using a low-priority
 * thread. 
 * <p>
 * This class is based on the Java 2D demo examples.
 */
public abstract class AnimatingSurface extends Surface implements Runnable {

	public Thread thread;

	public abstract void step( int w, int h );

	public abstract void reset( int newwidth, int newheight );

	public void start( )
	{
		if( thread == null )
		{
			thread = new Thread( this );
			thread.setPriority( Thread.MIN_PRIORITY );
			thread.start( );
		}
	}


	public synchronized void stop( )
	{
		if (thread != null)
		{
			thread.interrupt( );
		}

		thread = null;
		notifyAll( );
	}


	public void run( )
	{
		Thread me = Thread.currentThread( );

		while (thread == me && !isShowing( ) || getSize( ).width == 0)
		{
			try
			{
				thread.sleep( 200 );
			} 
			catch ( InterruptedException e )
			{ 
			}
		}

		while (thread == me)
		{
			repaint( );
		}

		thread = null;
	}
}
