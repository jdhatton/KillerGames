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

package org.selman.java3d.book.multiview;

import java.awt.*;
import java.awt.event.*;

import javax.vecmath.*;
import javax.media.j3d.*;

import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.geometry.Box;

import org.selman.java3d.book.common.*;

/**
* Creates a new into a Java 3D scene each time the "Add View" button is pressed.
* Each view has a different scale set so you can tell them apart.
*/
public class MultiView extends Java3dApplet implements ActionListener
{	
	private static final int 	m_kWidth = 400;
	private static final int 	m_kHeight = 400;

	private static final int 	m_kCanvasSize = 100;	
	private int					m_nNumViews;

	public MultiView( )
	{	
		m_nNumViews = 0;
		initJava3d( );
	}

	protected void addCanvas3D( Canvas3D c3d )
	{
		add( c3d );

		// IFF we have created the first view 
		// also add a button to allow more views 
		// to be created
		if( m_nNumViews == 1 )
		{		
			Button button = new Button( "Add View" );
			button.addActionListener( this );
			add( button );
		}

		doLayout( );
	}

	protected int getCanvas3dWidth( Canvas3D c3d )
	{
		return m_kCanvasSize;
	}

	protected int getCanvas3dHeight( Canvas3D c3d )
	{
		return m_kCanvasSize;
	}

	public BranchGroup createSceneBranchGroup( )
	{
		// Create the root of the branch graph
		BranchGroup objRoot = new BranchGroup( );

		//Make the scene graph
		try
		{
			TransformGroup objTrans = new TransformGroup( );
			objTrans.setCapability( TransformGroup.ALLOW_TRANSFORM_WRITE );
			objRoot.addChild( objTrans );

			// Create appearance object for textured cube
			Appearance app = new Appearance( );

			Texture tex = new TextureLoader( "Dog.jpg", this ).getTexture( );
			app.setTexture( tex );

			// Create a simple shape leaf node, add it to the scene graph.
			Box textureCube = new Box( 2, 3, 4, Box.GENERATE_TEXTURE_COORDS, app );

			objTrans.addChild( textureCube );

			// Create a new Behavior object that will perform the desired
			// operation on the specified transform object and add it into
			// the scene graph.
			Transform3D yAxis = new Transform3D( );
			Alpha rotationAlpha = new Alpha( -1, Alpha.INCREASING_ENABLE,
				0, 0,
				4000, 0, 0,
				0, 0, 0 );

			RotationInterpolator rotator = new RotationInterpolator( rotationAlpha, objTrans, yAxis, 0.0f, (float) Math.PI*2.0f );

			BoundingSphere bounds =	new BoundingSphere( new Point3d( 0.0,0.0,0.0 ), 100.0 );
			rotator.setSchedulingBounds( bounds );
			objTrans.addChild( rotator );
		} 
		catch( RuntimeException e )
		{
			System.out.println( "MultiView.createSceneBranchGroup:" + e.getMessage( ) );
			System.exit( -1 );
		}

		return objRoot;
	}

	public TransformGroup[] getViewTransformGroupArray( )
	{
		// increment the view count
		m_nNumViews++;

		TransformGroup[] tgArray = new TransformGroup[1];
		tgArray[0] = new TransformGroup( );

		Vector3d vTrans = new Vector3d( 0.0, 0.0, -20 );

		// move the camera BACK so we can view the scene
		// also set the scale so that the more views we have
		// the smaller the scene will be scaled
		Transform3D t3d = new Transform3D( );
		t3d.setTranslation( vTrans );
		t3d.setScale( 1.0 / m_nNumViews );
		t3d.invert( );
		tgArray[0].setTransform( t3d );

		return tgArray;
	}

	public void actionPerformed( ActionEvent event )
	{
		if( event.getActionCommand( ).equals( "Add View" ) != false )
		{	
			// create a new ViewPlatform
			ViewPlatform vp = createViewPlatform( );

			// create the BranchGroup for the ViewPlatform
			BranchGroup viewBranchGroup = createViewBranchGroup( getViewTransformGroupArray( ), vp );

			// add the ViewPlatform BranchGroup to the default Locale
			addViewBranchGroup( getFirstLocale( ), viewBranchGroup );

			// create the View (including Canvas3D) and attach to the ViewPlatform
			createView( vp );
		}
	}

	public static void main( String args[] )
	{
		MultiView multiView = new MultiView( );
		multiView.saveCommandLineArguments( args );

		new MainFrame( multiView, m_kWidth, m_kHeight );
	}
}
