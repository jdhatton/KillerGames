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

package org.selman.java3d.book.appearancetest;

import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.*;

import javax.media.j3d.*;
import javax.vecmath.*;

public abstract class AppearanceComponent implements ActionListener
{
	protected NodeComponent				m_NodeComponent = null;
	protected Appearance					m_Appearance = null;

	public AppearanceComponent( Appearance app )
	{
		m_Appearance = app;

		m_NodeComponent = createComponent( );

		int[] capsArray = getCapabilities( );

		if( capsArray != null )
		{
			for( int n= 0; n < capsArray.length; n++ )
				m_NodeComponent.setCapability( capsArray[n] );
		}

		setAppearanceCapability( );
		assignToAppearance( );
	}

	abstract protected int[] getCapabilities( );
	abstract protected void setAppearanceCapability( );

	abstract protected NodeComponent createComponent( );
	abstract protected void assignToAppearance( );
	abstract protected void assignNullToAppearance( );

	abstract protected String getName( );
	abstract protected String[] getMenuItemNames( );

	public Menu createMenu( )
	{
		String szName = getName( );
		String[] itemArray = getMenuItemNames( );
		ActionListener listener = this;

		Menu menu = new Menu( szName );

		MenuItem menuItem = new MenuItem( "Null" );
		menuItem.addActionListener( listener );
		menu.add( menuItem );

		menuItem = new MenuItem( "Non_Null" );
		menuItem.addActionListener( listener );
		menu.add( menuItem );

		for( int n = 0; n < itemArray.length; n++ )
		{
			menuItem = new MenuItem( itemArray[n] );
			menuItem.addActionListener( listener );
			menu.add( menuItem );
		}

		return menu;
	}

	public void onNull( )
	{
		assignNullToAppearance( );
	}

	public void onNon_Null( )
	{
		assignToAppearance( );
	}

	public void actionPerformed( ActionEvent event )
	{
		// (primitive) menu command dispatch
		Class classObject = getClass( );

		Method[] methodArray = classObject.getMethods( );

		for( int n = methodArray.length-1; n >= 0; n-- )
		{
			if( ("on" + event.getActionCommand( )).equals( methodArray[n].getName( ) ) )
			{
				try
				{
					methodArray[n].invoke( this , null );
				}
				catch( InvocationTargetException ie )
				{
					System.err.println( "Warning. Menu handler threw exception: " + ie.getTargetException( ) );
				}
				catch( Exception e )
				{
					System.err.println( "Warning. Menu dispatch exception: " + e );
				}

				return;
			}
		}
	}
}
