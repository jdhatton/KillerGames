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

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.awt.image.*;
import java.net.*;
import java.io.*;

import javax.media.j3d.*;
import javax.vecmath.*;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.geometry.*;

import org.selman.java3d.book.common.*;

/**
* Creates a scene which plots a custom Alpha function
* (loaded from a file). In addition the Alpha is applied
* to a rendered object in a scene.
* Note: this example does not currently run as an Applet.
*/
public class CustomAlphaTest extends Java3dApplet implements ActionListener, FileAlphaListener
{
	private static int 			m_kWidth = 600;
	private static int 			m_kHeight = 600;

	final int						m_knLoopCount = 0;

	final long						m_kTimeStep = 200;

	private FileAlpha				m_Alpha = null;

	private Vector					m_EditFieldVector = null;

	private BufferedImage		m_Image = null;

	private long					m_CurrentTime = 0;
	private float					m_CurrentValue = 0;

	// offsets and drawing information
	private int 					m_nMaxHeight = 0;
	private int 					m_nMaxWidth = 0;
	private int 					m_nInsetX = 0;
	private int 					m_nInsetY = 0;
	private int 					m_nGraphMaxWidth = 0;
	private int 					m_nGraphMaxHeight = 0;
	private int 					m_nGraphInsetX = 0;
	private int 					m_nGraphInsetY = 0;
	private double					m_ScaleX = 0;

	public CustomAlphaTest( )
	{
		try
		{
			// HACK:
			// if we are running as an Applet
			// the getWorkingDirectory() call will throw an NPE
			// as we cannot call getCodeBase until "start" has been called (below).
			m_Alpha = new FileAlpha( new URL( getWorkingDirectory( ), "values.xls" ) );
			m_Image = new BufferedImage( m_kWidth, m_kHeight, BufferedImage.TYPE_INT_RGB ); 

			buildUi( );
			initJava3d( );			
		}
		catch( Exception e )
		{
		}	
	}

	// we duplicate this code here for Applet support
	public void start( )
	{
		if( m_Alpha != null )
			return;

		try
		{
			m_Alpha = new FileAlpha( new URL( getWorkingDirectory( ), "values.xls" ) );
			m_Image = new BufferedImage( m_kWidth, m_kHeight, BufferedImage.TYPE_INT_RGB ); 

			buildUi( );
			initJava3d( );			
		}
		catch( Exception e )
		{
			e.printStackTrace( );
		}
	}

	protected void addCanvas3D( Canvas3D c3d )
	{
		Frame frame = new Frame( "Custom Alpha Test" );
       
		Panel aPanel = new Panel( );
       aPanel.setLayout( new BorderLayout() );
		aPanel.add( c3d, BorderLayout.CENTER );
       
       frame.add( aPanel );

		frame.pack( );
		frame.setSize( new Dimension( 320, 320 ) );
		frame.validate( );
		frame.setVisible( true );

		doLayout( );
	}

	protected void buildUi( )
	{
		m_EditFieldVector = new Vector( 1 );

		addField( "Loop Count", m_knLoopCount );

		addButton( "Update" );

		updateUi( );
		drawGraph( );
		repaint( );
	}

	protected BranchGroup createSceneBranchGroup( )
	{
		BranchGroup objRoot = super.createSceneBranchGroup( );

		TransformGroup objTrans = new TransformGroup( );
		objTrans.setCapability( TransformGroup.ALLOW_TRANSFORM_WRITE );
		objTrans.setCapability( TransformGroup.ALLOW_TRANSFORM_READ );

		ColorCube cube = new ColorCube( 2 );		
		objTrans.addChild( cube );

		FileAlpha fileAlpha = null;
		
		try
		{
			fileAlpha = new FileAlpha( new URL( getWorkingDirectory(), "values.xls" ), this );
			}
		catch( Exception e )
		{
			e.toString();
			}
		
		PositionInterpolator posInterpolator = new PositionInterpolator( 	fileAlpha, 
																								objTrans,
																								new Transform3D(),
																								-6,
																								6 );
		posInterpolator.setSchedulingBounds( getApplicationBounds( ) );

		objTrans.addChild( posInterpolator );
		objRoot.addChild( objTrans );

		return objRoot;
	}


	// handle event from the GUI components we created
	public void actionPerformed( ActionEvent event )
	{
		if( event.getActionCommand( ).equals( "Update" ) != false )
		{
			updateAlpha( );
			updateUi( );

			drawGraph( );
			repaint( );
		}
	}

	protected void updateAlpha( )
	{
		if( m_Alpha != null )
			m_Alpha.setLoopCount( Integer.parseInt( getField( m_knLoopCount ).getText( ) ) );
	}

	protected void updateUi( )
	{
		if( m_Alpha != null )
			getField( m_knLoopCount ).setText( String.valueOf( m_Alpha.getLoopCount( ) ) );		
	}


	long getMaxTime( )
	{
		return m_Alpha.getStopTime( );
	}

	protected void drawGraph( )
	{
		if ( m_Alpha != null )
		{
			Graphics g = m_Image.getGraphics( );

			g.setColor( Color.white );
			g.fillRect( 0, 0, m_kWidth, m_kHeight );
			g.setColor( Color.black );

			m_Alpha.setStartTime( 0 );
			long lMaxTime = getMaxTime( );

			computeDrawScale( lMaxTime );

			drawAxes( g, lMaxTime );
			drawPhases( g, lMaxTime );				
			drawAlpha( g, lMaxTime );
		}
	}

	protected TextField getField( final int nIndex )
	{
		return ( TextField ) m_EditFieldVector.get( nIndex );
	}


	protected void addButton( final String szText )
	{
		Button button = new Button( szText );
		button.addActionListener( this );
		add( button );
	}

	protected void addField( final String szText, final int nIndex )
	{
		Label label = new Label( szText );
		TextField textField = new TextField( 4 );

		m_EditFieldVector.add( nIndex, (Object) textField );

		add( label );
		add( textField );
	}


	protected void computeDrawScale( long lMaxTime )
	{
		int nWidth = getWidth( );
		int nHeight = getHeight( );

		if( nWidth > m_kWidth )
			nWidth = m_kWidth;

		if( nHeight > m_kHeight )
			nHeight = m_kHeight;

		m_nMaxHeight = (int) (nHeight * 0.7);
		m_nMaxWidth = (int) (nWidth * 0.9);

		m_nInsetX = (nWidth - m_nMaxWidth) / 2;
		m_nInsetY = (int) ((nHeight - m_nMaxHeight) / 1.1);

		m_nGraphMaxWidth = (int) (m_nMaxWidth * 0.80 );
		m_nGraphMaxHeight = (int) (m_nMaxHeight * 0.80 );

		m_nGraphInsetX = (m_nMaxWidth - m_nGraphMaxWidth) / 2;
		m_nGraphInsetY = (m_nMaxHeight - m_nGraphMaxHeight) / 2;

		m_ScaleX = ((double) m_nGraphMaxWidth) / ((double) lMaxTime);
	}

	protected void drawAreaRect( Graphics g, int x, int y, int width, int height )
	{
		g.drawRect( x, y, width, height );
	}

	protected void drawAreaString( Graphics g, int nLen, String szText, double x, double y )
	{
		if( nLen > 0 && szText.length( ) > nLen )
			szText = szText.substring( 0, nLen );

		g.drawString( szText, (int) x, (int) (m_nMaxHeight - y) );
	}

	protected void drawGraphString( Graphics g, int nLen, String szText, double x, double y )
	{
		if( nLen > 0 && szText.length( ) > nLen )
			szText = szText.substring( 0, nLen );

		g.drawString( szText, (int) (m_nGraphInsetX + x), (int) (m_nGraphInsetY + m_nGraphMaxHeight - y) );
	}

	protected void drawGraphLine( Graphics g, double x1, double y1, double x2, double y2 )
	{
		g.drawLine( (int) (m_nGraphInsetX + x1), 
			(int) (m_nGraphInsetY + m_nGraphMaxHeight - y1 ),
			(int) (m_nGraphInsetX + x2 ), 
			(int) (m_nGraphInsetY + m_nGraphMaxHeight - y2 ) );
	}

	protected void drawGraphFillCircle( Graphics g, double x1, double y1, double radius )
	{
		g.fillOval( (int) (m_nGraphInsetX + x1 - radius),
			(int) (m_nGraphInsetY + m_nGraphMaxHeight - y1 - radius), 
			(int) (radius * 2), 
			(int) (radius * 2) );
	}


	protected void drawAxes( Graphics g, long lMaxTime )
	{
		// draw the frame
		drawAreaRect( g, 0, 0, m_nMaxWidth, m_nMaxHeight );

		drawGraphString( g, -1, "Alpha vs. Time (secs)", m_nGraphMaxWidth / 2, m_nGraphMaxHeight + 20 );

		// draw the X axis
		drawGraphLine( g, 0, 0, m_nGraphMaxWidth, 0 );

		// draw the Y axis
		drawGraphLine( g, 0, 0, 0, m_nGraphMaxHeight );

		// draw the horizontal Y axis lines				
		for( double yAxisTick = 0; yAxisTick <= 1.0; yAxisTick += 0.2 )
		{
			double yTick = yAxisTick * m_nGraphMaxHeight;

			g.setColor( Color.gray );
			drawGraphLine( g, 0, yTick, m_nGraphMaxWidth, yTick );
			g.setColor( Color.black );

			drawGraphString( g, 3, "" + yAxisTick, -20, yTick );
		}
	}

	protected void drawPhases( Graphics g, long lMaxTime )
	{					
		double curTime = 0;

		g.setColor( Color.darkGray );

		int nLoop = 1;

		if( m_Alpha.getLoopCount( ) > 0 )
			nLoop = m_Alpha.getLoopCount( );

		for( int n = 0; n < nLoop; n++ )
		{
			for( int nValue = 0; nValue < m_Alpha.getNumValues( ); nValue++ )
			{
				curTime = n * m_Alpha.getMaxTime( ) + m_Alpha.getTimeForValue( nValue );

				g.setColor( Color.black );
				drawGraphString( g, -1, "" + (curTime / 1000), curTime * m_ScaleX, -20 );
				g.setColor( Color.darkGray );

				drawGraphLine( g, curTime * m_ScaleX, 0, curTime * m_ScaleX, m_nGraphMaxHeight );
			}
		}

		g.setColor( Color.black );
	}

	protected void drawAlpha( Graphics g, long lMaxTime )
	{
		g.setColor( Color.blue );

		float value = 0;

		double x1 = 0;
		double y1 = 0;
		double x2 = 0;
		double y2 = 0;

		for ( long lTime = 0; lTime <= lMaxTime; lTime += m_kTimeStep )
		{
			value = m_Alpha.value( lTime );

			x2 = lTime * m_ScaleX;
			y2 = value * m_nGraphMaxHeight;

			drawGraphLine( g, x1, y1, x2, y2 );

			x1 = x2;
			y1 = y2;
		}

		g.setColor( Color.black );
	}

	protected void drawCurrentPosition( Graphics g )
	{
		drawGraphFillCircle( g, m_nInsetX + m_CurrentTime * m_ScaleX, 
			m_CurrentValue * m_nGraphMaxHeight - m_nInsetY, 3 );
	}

	public void onFileAlphaGetValue( long ltime, float value )
	{
		m_CurrentTime = ltime % m_Alpha.getStopTime( );
		m_CurrentValue = value;

		int repaintX = (int) (m_nGraphInsetX + m_nInsetX + m_CurrentTime * m_ScaleX);
		int repaintY = (int) (m_nGraphInsetY + m_nGraphMaxHeight - (m_CurrentValue * m_nGraphMaxHeight - m_nInsetY));

		Graphics g = getGraphics( );

		if( g != null )
		{
			repaint( repaintX - 5, repaintY - 5, 10, 10 );
			drawCurrentPosition( g );
		}
	}

	public void paint( Graphics g )
	{
		super.paint( g );

		if( m_Alpha != null )
		{
			drawGraph( );
			g.drawImage( m_Image, m_nInsetX, m_nInsetY, this );
		}
	}

	public static void main( String[] args )
	{
		CustomAlphaTest alphaTest = new CustomAlphaTest( );
		new MainFrame( alphaTest, m_kWidth, m_kHeight );
	}
}
