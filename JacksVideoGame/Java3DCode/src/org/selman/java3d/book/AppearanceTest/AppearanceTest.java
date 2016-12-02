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

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.*;

import javax.media.j3d.*;
import javax.vecmath.*;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.geometry.*;

import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.loaders.Scene;

import com.sun.j3d.utils.behaviors.keyboard.KeyNavigatorBehavior;

import org.selman.java3d.book.common.*;

/**
* Allows the various Java 3D Appearance components to 
* be specified interactively and applies the Appearance to
* an object in a scene.
* <p>
* This example can only be run as an application
* as it uses a MenuBar which can only be
* associated with a Frame (in AWT)
*/
public class AppearanceTest extends Java3dApplet
{
	private static int 				m_kWidth = 400;
	private static int 				m_kHeight = 400;

	private Frame						m_Frame = null;

	private Appearance				m_Appearance = null;

	private AppearanceComponent[]	m_ComponentArray = null;

	public AppearanceTest( )
	{
		m_Appearance = new Appearance( );

		TextureComponent.setComponent( this );

		m_ComponentArray = new AppearanceComponent[]
									{
										new PolygonComponent( m_Appearance ),
										new ColoringComponent( m_Appearance ),
										new LineComponent( m_Appearance ),
										new MaterialComponent( m_Appearance ),
										new PointComponent( m_Appearance ),
										new RenderingComponent( m_Appearance ),
										new TransparencyComponent( m_Appearance ),
										new TextureComponent( m_Appearance ),
										new TextureAttributesComponent( m_Appearance ),
										new TexGenComponent( m_Appearance )
									};
	}

	protected void addCanvas3D( Canvas3D c3d )
	{
		if( m_Frame != null )
		{	
			MenuBar menuBar = new MenuBar( );

			for( int n = 0; n < m_ComponentArray.length; n++ )
				menuBar.add( m_ComponentArray[n].createMenu( ) );

			m_Frame.setMenuBar( menuBar );
		}

		setLayout( new BorderLayout( ) );
		add( c3d, BorderLayout.CENTER );
		doLayout( );
	}

	protected double getScale( )
	{
		return 0.1;
	}

	protected BranchGroup createSceneBranchGroup( )
	{
		BranchGroup objRoot = super.createSceneBranchGroup( );

		TransformGroup zoomTg = new TransformGroup( );
		zoomTg.setCapability( TransformGroup.ALLOW_TRANSFORM_WRITE );
		zoomTg.setCapability( TransformGroup.ALLOW_TRANSFORM_READ );

		// attach a navigation behavior to the position of the viewer
		KeyNavigatorBehavior key = new KeyNavigatorBehavior( zoomTg );
		key.setSchedulingBounds( createApplicationBounds( ) );
		key.setEnable( true );
		objRoot.addChild( key );

		// create a TransformGroup to flip the hand onto its end and enlarge it.
		TransformGroup objTrans1 = new TransformGroup( );
		Transform3D tr = new Transform3D( );
		objTrans1.getTransform( tr );
		tr.setEuler( new Vector3d( 0.5 * Math.PI, 0.6, 0 ) );
		objTrans1.setTransform( tr );

		// Set up the global lights
		Color3f lColor1 = new Color3f( 0.7f, 0.7f, 0.7f );
		Vector3f lDir1  = new Vector3f( -1.0f, -1.0f, -1.0f );
		Color3f alColor = new Color3f( 0.2f, 0.2f, 0.2f );

		AmbientLight aLgt = new AmbientLight( alColor );
		aLgt.setInfluencingBounds( getApplicationBounds( ) );
		DirectionalLight lgt1 = new DirectionalLight( lColor1, lDir1 );
		lgt1.setInfluencingBounds( getApplicationBounds( ) );

		objRoot.addChild( aLgt );
		objRoot.addChild( lgt1 );

		int nScale = 50;

		Box box = new Box( nScale,nScale,nScale, Primitive.GENERATE_NORMALS | Primitive.GENERATE_TEXTURE_COORDS, m_Appearance );

		Shape3D frontFace = box.getShape( Box.LEFT );

		// create a new left face so we can 
		// assign per-vertex colors

		GeometryArray geometry = new QuadArray( 4, 	GeometryArray.COORDINATES | 
			GeometryArray.NORMALS | 
			GeometryArray.COLOR_4 | 
			GeometryArray.TEXTURE_COORDINATE_2 );

		nScale = 40;

		final float[] verts = 
		{
			// left face
			-1.0f * nScale, -1.0f * nScale,  1.0f * nScale,
			-1.0f * nScale,  1.0f * nScale,  1.0f * nScale,
			-1.0f * nScale,  1.0f * nScale, -1.0f * nScale,
			-1.0f * nScale, -1.0f * nScale, -1.0f * nScale
		};

		final float[] colors = 
		{
			// left face
			1,0,0,0,
			0,1,0,0.2f,
			0,0,1,0.8f,
			0,0,0,1,
		};

		float[] tcoords = 
		{
			// left
			1, 0,
			1, 1,
			0, 1,
			0, 0
		};

		Vector3f normalVector = new Vector3f( -1.0f,  0.0f,  0.0f );

		geometry.setColors( 0, colors, 0, 4 );

		for( int n = 0; n < 4; n++ )
			geometry.setNormal( n, normalVector );

		geometry.setTextureCoordinates( 0, tcoords, 0, 4 );

		geometry.setCoordinates( 0, verts );

		frontFace.setGeometry( geometry );

		// connect the scenegraph
		objTrans1.addChild( box );
		zoomTg.addChild( objTrans1 );
		objRoot.addChild( zoomTg );

		return objRoot;
	}	

	public static void main( String[] args )
	{
		AppearanceTest appearanceTest = new AppearanceTest( );
		appearanceTest.saveCommandLineArguments( args );

		Frame frame = (Frame) new MainFrame( appearanceTest, m_kWidth, m_kHeight );

		appearanceTest.m_Frame = frame;
		appearanceTest.initJava3d( );
	}
}
