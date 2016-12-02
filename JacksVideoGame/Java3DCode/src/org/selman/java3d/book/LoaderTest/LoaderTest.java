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

package org.selman.java3d.book.loadertest;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;

import javax.media.j3d.*;
import javax.vecmath.*;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.geometry.*;

import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.loaders.Scene;

import org.selman.java3d.book.common.*;

/**
* Simple example that illustrates using Java 3D's
* built in object loader to load a Lightwave file.
*/
public class LoaderTest extends Java3dApplet implements ActionListener
{
	private static int 			m_kWidth = 400;
	private static int 			m_kHeight = 400;

	public LoaderTest( )
	{
		initJava3d( );
	}

	public void actionPerformed( ActionEvent event )
	{
	}

	protected BranchGroup createSceneBranchGroup( )
	{
		BranchGroup objRoot = super.createSceneBranchGroup( );

		// create a TransformGroup to flip the hand onto its end and enlarge it.
		TransformGroup objTrans1 = new TransformGroup( );
		Transform3D tr = new Transform3D( );
		objTrans1.getTransform( tr );
		tr.rotX( 90.0 * Math.PI / 180.0 );
		tr.setScale( 10.0 );
		objTrans1.setTransform( tr );

		// create a TransformGroup to rotate the hand
		TransformGroup objTrans2 = new TransformGroup( );
		objTrans2.setCapability( TransformGroup.ALLOW_TRANSFORM_WRITE );
		objTrans2.setCapability( TransformGroup.ALLOW_TRANSFORM_READ );

		BoundingSphere bounds =	new BoundingSphere( new Point3d( 0.0,0.0,0.0 ), 100.0 );

		// create a RotationInterpolator behavior to rotate the hand
		Transform3D yAxis = new Transform3D( );
		Alpha rotationAlpha = new Alpha( -1, Alpha.INCREASING_ENABLE,
			0, 0,
			4000, 0, 0,
			0, 0, 0 );

		RotationInterpolator rotator = new RotationInterpolator( rotationAlpha, objTrans2, yAxis, 0.0f, (float) Math.PI*2.0f );
		rotator.setSchedulingBounds( bounds );
		objTrans2.addChild( rotator );

		// Set up the global lights
		Color3f lColor1 = new Color3f( 0.7f, 0.7f, 0.7f );
		Vector3f lDir1  = new Vector3f( -1.0f, -1.0f, -1.0f );
		Color3f alColor = new Color3f( 0.2f, 0.2f, 0.2f );

		AmbientLight aLgt = new AmbientLight( alColor );
		aLgt.setInfluencingBounds( bounds );
		DirectionalLight lgt1 = new DirectionalLight( lColor1, lDir1 );
		lgt1.setInfluencingBounds( bounds );

		objRoot.addChild( aLgt );
		objRoot.addChild( lgt1 );

		// load the object file
		Scene scene = null;
		Shape3D shape = null;

		// read in the geometry information from the data file
		ObjectFile objFileloader = new ObjectFile( ObjectFile.RESIZE );

		try
		{
			scene = objFileloader.load( "hand1.obj" );
		}
		catch ( Exception e )
		{
			scene = null;
			System.err.println( e );
		}

		if( scene == null )
			System.exit( 1 );

		// retrieve the Shape3D object from the scene
		BranchGroup branchGroup = scene.getSceneGroup( );
		shape = (Shape3D) branchGroup.getChild( 0 );

		// create an Appearance and Material
		Appearance app = new Appearance( );
		Color3f objColor = new Color3f( 1.0f, 0.7f, 0.8f );
		Color3f black = new Color3f( 0.0f, 0.0f, 0.0f );
		app.setMaterial( new Material( objColor, black, objColor, black, 80.0f ) );

		// assign the appearance to the Shape
		shape.setAppearance( app );

		// connect the scenegraph
		objTrans2.addChild( scene.getSceneGroup( ) );
		objTrans1.addChild( objTrans2 );
		objRoot.addChild( objTrans1 );

		return objRoot;
	}


	public static void main( String[] args )
	{
		LoaderTest loaderTest = new LoaderTest( );
		loaderTest.saveCommandLineArguments( args );

		new MainFrame( loaderTest, m_kWidth, m_kHeight );
	}
}
