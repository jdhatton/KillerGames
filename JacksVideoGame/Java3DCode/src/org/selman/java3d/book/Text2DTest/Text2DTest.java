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

package org.selman.java3d.book.text2dtest;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;

import javax.media.j3d.*;
import javax.vecmath.*;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.geometry.*;
import javax.media.j3d.Billboard;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;

import org.selman.java3d.book.common.*;

/**
* Renders Java 3D 2D Text objects in a variety
* of styles.
*/
public class Text2DTest extends Java3dApplet
{
	private static int 			m_kWidth = 400;
	private static int 			m_kHeight = 400;

	public Text2DTest( )
	{
		initJava3d( );
	}

	protected BranchGroup createSceneBranchGroup( )
	{
		BranchGroup objRoot = super.createSceneBranchGroup( );

		Color3f color = new Color3f( 0.0f, 0.0f, 0.0f );
		objRoot.addChild( createText2D( "400 point 1x", color, 400, 1.0f, 6.0f ) );
		objRoot.addChild( createText2D( "150 point 3x", color, 150, 3.0f, 3.0f ) );
		objRoot.addChild( createText2D( "50 point 10x", color, 50, 10.0f, 0.0f ) );
		objRoot.addChild( createText2D( "10 point 50x", color, 10, 50.0f, -3.0f ) );
		objRoot.addChild( createText2D( "5 point 100x", color, 5, 100.0f, -6.0f ) );

		return objRoot;
	}

	TransformGroup createText2D( String szText, Color3f color, int nSize, float scale, float trans )
	{
		TransformGroup tg = new TransformGroup( );
		Transform3D t3d = new Transform3D( );
		t3d.setScale( scale );
		t3d.setTranslation( new Vector3d( -8.0,trans, 0 ) );

		tg.setTransform( t3d );		

		Text2D text2D = new Text2D( szText, color, "SansSerif", nSize, Font.PLAIN );

		tg.addChild( text2D );
		return tg;
	}

	public static void main( String[] args )
	{
		Text2DTest text2DTest = new Text2DTest( );
		text2DTest.saveCommandLineArguments( args );

		new MainFrame( text2DTest, m_kWidth, m_kHeight );
	}
}
