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
  Lee Dixon:     leedixon@email.com

  If you make changes you think others would like, please 
  contact one of the authors or someone at the 
  www.j3d.org web site.
**************************************************************/

package org.selman.java3d.book.alphatest;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.media.j3d.*;
import javax.vecmath.*;

import com.sun.j3d.utils.applet.MainFrame;

/**
* Draws a simple plot of a parameterized Java 3D 
* Alpha Function to an AWT Window.
*/
public class AlphaTest extends Applet implements ActionListener
{
	/**
	* default size of the window
	*/
	private static int 					m_kWidth = 400;
	private static int 					m_kHeight = 400;

	/**
	* indexes into the UI components we create
	*/
	private static final int			m_knTrigger = 0;
	private static final int			m_knPhaseDelay = 1;
	private static final int			m_knIncreasingAlpha = 2;
	private static final int			m_knIncreasingRamp = 3;
	private static final int			m_knAtOne = 4;
	private static final int			m_knDecreasingAlpha = 5;
	private static final int			m_knDecreasingRamp = 6;
	private static final int			m_knAtZero = 7;
	private static final int			m_knLoopCount = 8;

	/**
	* controls how smooth a plot curve we create
	*/
	private final static long			m_kTimeStep = 10;

	/**
	* the Java 3D Alpha object we are parameterizing
	*/
	private Alpha						m_Alpha = null;

	/**
	* a Vector of Edit UI components
	*/
	Vector								m_EditFieldVector = null;

	/**
	* offsets and drawing information
	*/
	int 								m_nMaxHeight = 0;
	int 								m_nMaxWidth = 0;
	int 								m_nInsetX = 0;
	int 								m_nInsetY = 0;
	int 								m_nGraphMaxWidth = 0;
	int 								m_nGraphMaxHeight = 0;
	int 								m_nGraphInsetX = 0;
	int 								m_nGraphInsetY = 0;
	double 								m_ScaleX = 0;

	/**
	* CheckboxGroup for INCREASING/DECREASING PHASE enable
	*/
	private CheckboxGroup m_checkboxGrp;

	/**
	* Constructor
	*/
	public AlphaTest( )
	{
		m_Alpha = new Alpha( -1,
			Alpha.DECREASING_ENABLE | Alpha.INCREASING_ENABLE,
			1000,
			1000,
			5000,
			1000,
			1000,
			10000,
			2000,
			4000 );
		buildUi( );
	}

	/**
	* Creates the UI components
	*/
	protected void buildUi( )
	{
		m_EditFieldVector = new Vector( 10 );

		addField( "Trigger Time", m_knTrigger );
		addField( "Phase Delay", m_knPhaseDelay );
		addField( "Increasing Alpha", m_knIncreasingAlpha );
		addField( "Increasing Ramp", m_knIncreasingRamp );
		addField( "At One", m_knAtOne );
		addField( "Decreasing Alpha", m_knDecreasingAlpha );
		addField( "Decreasing Ramp", m_knDecreasingRamp );
		addField( "At Zero", m_knAtZero );
		addField( "Loop Count", m_knLoopCount );

		m_checkboxGrp = new CheckboxGroup( );
		add( new Checkbox( "INCREASING_ENABLE", m_checkboxGrp, false ) );
		add( new Checkbox( "DECREASING_ENABLE", m_checkboxGrp, false ) );
		add( new Checkbox( "BOTH", m_checkboxGrp, true ) );

		addButton( "Update" );

		updateUi( );
	}

	/**
	* Handle events from the GUI components we created
	*/
	public void actionPerformed( ActionEvent event )
	{
		if( event.getActionCommand( ).equals( "Update" ) != false )
		{
			updateAlpha( );
			repaint( );
		}
	}

	/**
	* Replots the Alpha function based on the contents of the UI
	*/
	protected void updateAlpha( )
	{		
		m_Alpha.setTriggerTime( Long.parseLong( getField( m_knTrigger ).getText( ) ) );	
		m_Alpha.setPhaseDelayDuration( Long.parseLong( getField( m_knPhaseDelay ).getText( ) ) );
		m_Alpha.setIncreasingAlphaDuration( Long.parseLong( getField( m_knIncreasingAlpha ).getText( ) ) );
		m_Alpha.setIncreasingAlphaRampDuration( Long.parseLong( getField( m_knIncreasingRamp ).getText( ) ) );
		m_Alpha.setAlphaAtOneDuration( Long.parseLong( getField( m_knAtOne ).getText( ) ) );
		m_Alpha.setDecreasingAlphaDuration( Long.parseLong( getField( m_knDecreasingAlpha ).getText( ) ) );
		m_Alpha.setDecreasingAlphaRampDuration( Long.parseLong( getField( m_knDecreasingRamp ).getText( ) ) );
		m_Alpha.setAlphaAtZeroDuration( Long.parseLong( getField( m_knAtZero ).getText( ) ) );
		m_Alpha.setLoopCount( Integer.parseInt( getField( m_knLoopCount ).getText( ) ) );

		Checkbox b = m_checkboxGrp.getSelectedCheckbox( );
		if ( b.getLabel( ).equals( "INCREASING_ENABLE" ) )
		{
			m_Alpha.setMode( Alpha.INCREASING_ENABLE );
		}
		else if ( b.getLabel( ).equals( "DECREASING_ENABLE" ) )
		{
			m_Alpha.setMode( Alpha.DECREASING_ENABLE );
		}
		else // "BOTH"
		{
			m_Alpha.setMode( Alpha.INCREASING_ENABLE | Alpha.DECREASING_ENABLE );
		}

	}

	/**
	* Updates the UI based on the Alpha function
	*/
	protected void updateUi( )
	{
		getField( m_knTrigger ).setText( String.valueOf( m_Alpha.getTriggerTime( ) ) );
		getField( m_knPhaseDelay ).setText( String.valueOf( m_Alpha.getPhaseDelayDuration( ) ) );
		getField( m_knIncreasingAlpha ).setText( String.valueOf( m_Alpha.getIncreasingAlphaDuration( ) ) );
		getField( m_knIncreasingRamp ).setText( String.valueOf( m_Alpha.getIncreasingAlphaRampDuration( ) ) );
		getField( m_knAtOne ).setText( String.valueOf( m_Alpha.getAlphaAtOneDuration( ) ) );
		getField( m_knDecreasingAlpha ).setText( String.valueOf( m_Alpha.getDecreasingAlphaDuration( ) ) );
		getField( m_knDecreasingRamp ).setText( String.valueOf( m_Alpha.getDecreasingAlphaRampDuration( ) ) );
		getField( m_knAtZero ).setText( String.valueOf( m_Alpha.getAlphaAtZeroDuration( ) ) );
		getField( m_knLoopCount ).setText( String.valueOf( m_Alpha.getLoopCount( ) ) );
	}

	/**
	* Helper method to add a TextField to the UI
	*/
	protected void addField( final String szText, final int nIndex )
	{
		Label label = new Label( szText );
		TextField textField = new TextField( 4 );

		m_EditFieldVector.add( nIndex, (Object) textField );

		add( label );
		add( textField );
	}

	/**
	* Retrieves a TextField with a given index
	*/
	protected TextField getField( final int nIndex )
	{
		return ( TextField ) m_EditFieldVector.get( nIndex );
	}

	/**
	* Helper method to add a Button to the UI.
	*/
	protected void addButton( final String szText )
	{
		Button button = new Button( szText );
		button.addActionListener( this );
		add( button );
	}

	/**
	* Computes the drawing scales based on the Alpha function
   * and the size of the Window.
	*/
	protected void computeDrawScale( long lMaxTime )
	{
		m_nMaxHeight = (int) (getHeight( ) * 0.7);
		m_nMaxWidth = (int) (getWidth( ) * 0.9);

		m_nInsetX = (getWidth( ) - m_nMaxWidth) / 2;
		m_nInsetY = (int) ((getHeight( ) - m_nMaxHeight) / 1.1);

		m_nGraphMaxWidth = (int) (m_nMaxWidth * 0.80 );
		m_nGraphMaxHeight = (int) (m_nMaxHeight * 0.80 );

		m_nGraphInsetX = m_nInsetX + (m_nMaxWidth - m_nGraphMaxWidth) / 2;
		m_nGraphInsetY = m_nInsetY + (m_nMaxHeight - m_nGraphMaxHeight) / 2;

		m_ScaleX = ((double) m_nGraphMaxWidth) / ((double) lMaxTime);
	}

	/**
	* Draws a rectangle into the display area for the plot.
	*/
	protected void drawAreaRect( Graphics g, int x, int y, int width, int height )
	{
		g.drawRect( m_nInsetX + x, m_nInsetY + y, width, height );
	}

	/**
	* Draws a String into the display area for the plot.
	*/
	protected void drawAreaString( Graphics g, int nLen, String szText, double x, double y )
	{
		if( nLen > 0 && szText.length( ) > nLen )
			szText = szText.substring( 0, nLen );

		g.drawString( szText, (int) (m_nInsetX + x), (int) (m_nMaxHeight + m_nInsetY - y) );
	}

	/**
	* Draws a String into the graph area for the plot.
	*/
	protected void drawGraphString( Graphics g, int nLen, String szText, double x, double y )
	{
		if( nLen > 0 && szText.length( ) > nLen )
			szText = szText.substring( 0, nLen );

		g.drawString( szText, (int) (m_nGraphInsetX + x), (int) (m_nGraphInsetY + m_nGraphMaxHeight - y) );
	}

	/**
	* Draws a Line into the graph area for the plot.
	*/
	protected void drawGraphLine( Graphics g, double x1, double y1, double x2, double y2 )
	{
		g.drawLine( (int) (m_nGraphInsetX + x1), 
			(int) (m_nGraphInsetY + m_nGraphMaxHeight - y1 ),
			(int) (m_nGraphInsetX + x2 ), 
			(int) (m_nGraphInsetY + m_nGraphMaxHeight - y2 ) );
	}

	/**
	* Draws the axes for the graph.
	*/
	protected void drawAxes( Graphics g, long lMaxTime )
	{
		drawGraphString( g, -1, "Alpha vs. Time (ms)", m_nGraphMaxWidth/2, m_nGraphMaxHeight + 20 );

		// draw the frame
		drawAreaRect( g, 0, 0, m_nMaxWidth, m_nMaxHeight );

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

	/**
	* Draws vertical lines and labels to denote the various
   * phases of the Alpha function.
	*/
	protected void drawPhases( Graphics g, long lMaxTime )
	{
		int nLoop = 1;

		if( m_Alpha.getLoopCount( ) > 0 )
			nLoop = m_Alpha.getLoopCount( );

		double curTime = 0;

		g.setColor( Color.darkGray );

		curTime += m_Alpha.getTriggerTime( );
		drawGraphLine( g, curTime * m_ScaleX, 0, curTime * m_ScaleX, m_nGraphMaxHeight );

		curTime += m_Alpha.getPhaseDelayDuration( );
		drawGraphLine( g, curTime * m_ScaleX, 0, curTime * m_ScaleX, m_nGraphMaxHeight );

		double startPhaseTime = 0;

		for( int nIteration = 0; nIteration < nLoop; nIteration++ )
		{
			startPhaseTime = curTime;

			g.setColor( Color.black );
			drawGraphString( g, -1, "" + curTime, curTime * m_ScaleX, -20 );
			g.setColor( Color.darkGray );

			if ( (m_Alpha.getMode( ) & Alpha.INCREASING_ENABLE) != 0 )
			{
				curTime += m_Alpha.getIncreasingAlphaRampDuration( );
				drawGraphLine( g, curTime * m_ScaleX, 0, curTime * m_ScaleX, m_nGraphMaxHeight );

				curTime = startPhaseTime + m_Alpha.getIncreasingAlphaDuration( ) - m_Alpha.getIncreasingAlphaRampDuration( );
				drawGraphLine( g, curTime * m_ScaleX, 0, curTime * m_ScaleX, m_nGraphMaxHeight );

				curTime = startPhaseTime + m_Alpha.getIncreasingAlphaDuration( );
				drawGraphLine( g, curTime * m_ScaleX, 0, curTime * m_ScaleX, m_nGraphMaxHeight );

				curTime += m_Alpha.getAlphaAtOneDuration( );
				drawGraphLine( g, curTime * m_ScaleX, 0, curTime * m_ScaleX, m_nGraphMaxHeight );
			}

			startPhaseTime = curTime;

			if ( (m_Alpha.getMode( ) & Alpha.DECREASING_ENABLE) != 0)
			{
				curTime += m_Alpha.getDecreasingAlphaRampDuration( );
				drawGraphLine( g, curTime * m_ScaleX, 0, curTime * m_ScaleX, m_nGraphMaxHeight );

				curTime = startPhaseTime + m_Alpha.getDecreasingAlphaDuration( ) - m_Alpha.getDecreasingAlphaRampDuration( );
				drawGraphLine( g, curTime * m_ScaleX, 0, curTime * m_ScaleX, m_nGraphMaxHeight );

				curTime = startPhaseTime + m_Alpha.getDecreasingAlphaDuration( );
				drawGraphLine( g, curTime * m_ScaleX, 0, curTime * m_ScaleX, m_nGraphMaxHeight );

				curTime += m_Alpha.getAlphaAtZeroDuration( );
				drawGraphLine( g, curTime * m_ScaleX, 0, curTime * m_ScaleX, m_nGraphMaxHeight );
			}
		}

		g.setColor( Color.black );
	}

	/**
	* Plots the Alpha function into the graph area of the plot.
	*/
	protected void drawAlpha( Graphics g, long lMaxTime )
	{
		g.setColor( Color.blue );

		float value = 0;

		m_Alpha.setStartTime( 0 );

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

	/**
	* AWT paint method that can handle dynamic
   * scale recomputation and graph redraw.
	*/
	public void paint( Graphics g )
	{
		super.paint( g );
       
		long lMaxTime = 0;
       
		if ( (m_Alpha.getMode( ) & Alpha.INCREASING_ENABLE) != 0)
		{
			lMaxTime +=
				m_Alpha.getIncreasingAlphaDuration( ) +
				m_Alpha.getAlphaAtOneDuration( );
		}
       
		if ( (m_Alpha.getMode( ) & Alpha.DECREASING_ENABLE) != 0)
		{
			lMaxTime +=
				m_Alpha.getDecreasingAlphaDuration( ) +
				m_Alpha.getAlphaAtZeroDuration( );
		}

		if( m_Alpha.getLoopCount() > 0 )
			lMaxTime *= m_Alpha.getLoopCount();

		lMaxTime += m_Alpha.getTriggerTime( );
		lMaxTime += m_Alpha.getPhaseDelayDuration( );
		computeDrawScale( lMaxTime );

		drawAxes( g, lMaxTime );
		drawPhases( g, lMaxTime );

		drawAlpha( g, lMaxTime );
	}

	/**
	* Simple main method that uses the Java 3D
   * MainFrame helper class.
	*/
	public static void main( String[] args )
	{
		AlphaTest alphaTest = new AlphaTest( );		
		new MainFrame( alphaTest, m_kWidth, m_kHeight );
	}
}
