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

package org.selman.java3d.book.switchtest;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;

import javax.media.j3d.*;
import javax.vecmath.*;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.geometry.*;

import org.selman.java3d.book.common.*;

/**
* This example creates a Switch Node and conditionally displays 
* some of the child Nodes using a logical mask, defined using
* a BitSet object.
*/
public class SwitchTest extends Java3dApplet
{
	private static final int 			m_kWidth = 400;
	private static final int 			m_kHeight = 400;

	static int							m_nLabelNumber = 0;

	public SwitchTest( )
	{
		initJava3d( );
	}

	protected BranchGroup createSceneBranchGroup( )
	{
		BranchGroup objRoot = super.createSceneBranchGroup( );

		double labelScale = 20;

		// flip this boolean to either display all
		// the child nodes or to just display the 3, 6 and 7th.
		final boolean bDisplayAll = false;

		// create the Switch Node
		int nMode = Switch.CHILD_ALL;

		if( bDisplayAll == false )
			nMode = Switch.CHILD_MASK;

		Switch switchGroup = new Switch( nMode );
		switchGroup.setCapability( Switch.ALLOW_SWITCH_WRITE );

		switchGroup.addChild( createLabel( "Child Node 1", labelScale ) );
		switchGroup.addChild( createLabel( "Child Node 2", labelScale ) );
		switchGroup.addChild( createLabel( "Child Node 3", labelScale ) );
		switchGroup.addChild( createLabel( "Child Node 4", labelScale ) );
		switchGroup.addChild( createLabel( "Child Node 5", labelScale ) );
		switchGroup.addChild( createLabel( "Child Node 6", labelScale ) );
		switchGroup.addChild( createLabel( "Child Node 7", labelScale ) );

		if( bDisplayAll == false )
		{
			java.util.BitSet visibleNodes = new java.util.BitSet( switchGroup.numChildren( ) );

			// make the third, sixth and seventh nodes visible
			visibleNodes.set( 2 );
			visibleNodes.set( 5 );
			visibleNodes.set( 6 );

			switchGroup.setChildMask( visibleNodes );
		}

		// finally add the Switch Node
		objRoot.addChild( switchGroup );

		return objRoot;
	}

	// creates a Text2D label and scales it
	// the method shifts each label created downwards.
	// Note that the labels on adjacent lines will overlap
	// so we can illustrate the function of the OrderedGroup
	TransformGroup createLabel( String szText, double scale )
	{
		Color3f colorText = new Color3f( );
		int nFontSizeText = 10;

		Text2D label3D = new Text2D( szText, colorText, "SansSerif", nFontSizeText, Font.PLAIN );

		TransformGroup tg = new TransformGroup( );
		Transform3D t3d = new Transform3D( );

		t3d.setTranslation( new Vector3d( -8,
			1.5 * (3 - m_nLabelNumber),
			0 ) );

		t3d.setScale( scale );


		tg.setTransform( t3d );
		tg.addChild( label3D );

		m_nLabelNumber++;

		return tg;
	}


	public static void main( String[] args )
	{
		SwitchTest SwitchTest = new SwitchTest( );
		SwitchTest.saveCommandLineArguments( args );

		new MainFrame( SwitchTest, m_kWidth, m_kHeight );
	}
}
