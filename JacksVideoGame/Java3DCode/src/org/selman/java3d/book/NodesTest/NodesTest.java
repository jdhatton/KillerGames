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

package org.selman.java3d.book.nodestest;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;

import javax.media.j3d.*;
import javax.vecmath.*;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.geometry.*;

import org.selman.java3d.book.common.*;

/**
* This example creates a scenegraph that illustrates
* many of the Java 3D scenegraph Nodes.
* These are: <p>
* <br>
* Group Nodes:<br>
* 	BranchGroup, (implemented)<br>
* 	OrderedGroup, (implemented)<br>
* 	Primitive, (implemented)<br>
* 	SharedGroup, (implemented)<br>
* 	Switch, (implemented)<br>
* 	TransformGroup (implemented)<br>
* 	<br>
* Leaf Nodes:<br>
* 	Background, (implemented) <br>
* 	Behavior, (implemented)<br>
* 	BoundingLeaf, <br>
* 	Clip, <br>
* 	Fog, <br>
* 	Light, <br>
* 	Link, (implemented)<br>
* 	Morph, <br>
* 	Shape3D, (implemented)<br>
* 	Sound, <br>
* 	Soundscape, <br>
* 	ViewPlatform (implemented)<br>
*/
public class NodesTest extends Java3dApplet
{
	private static final int 			m_kWidth = 400;
	private static final int 			m_kHeight = 400;

	static int							m_nLabelNumber = 0;

	public NodesTest( )
	{
		initJava3d( );
	}

	protected BranchGroup createSceneBranchGroup( )
	{
		BranchGroup objRoot = super.createSceneBranchGroup( );

		double labelScale = 20;

		// create the top level Switch Node
		// we will use the Switch Node to switch the
		// other Nodes on and off.
		// 1: Switch
		Switch switchGroup = new Switch( );
		switchGroup.setCapability( Switch.ALLOW_SWITCH_WRITE );
		switchGroup.addChild( createLabel( "1. Switch Label", labelScale ) );

		// 2: BranchGroup
		BranchGroup branchGroup = new BranchGroup( );
		branchGroup.addChild( createLabel( "2. BranchGroup", labelScale ) );
		switchGroup.addChild( branchGroup );

		// 3: OrderedGroup,
		OrderedGroup orderedGroup = new OrderedGroup( );
		orderedGroup.addChild( createLabel( "3. OrderedGroup", labelScale ) );
		orderedGroup.addChild( createLabel( "Child 1", labelScale ) );
		orderedGroup.addChild( createLabel( "Child 2", labelScale ) );
		switchGroup.addChild( orderedGroup ); 

		// 4: SharedGroup, 
		SharedGroup sharedGroup1 = new SharedGroup( );
		sharedGroup1.addChild( createLabel( "4. Shared Group 1", labelScale ) );
		switchGroup.addChild( new Link( sharedGroup1 ) );

		// 5: Primitive, 
		BranchGroup primitiveGroup = new BranchGroup( );
		primitiveGroup.addChild( createLabel( "5. Primitive", labelScale ) );
		primitiveGroup.addChild( new Sphere( 2 ) );
		switchGroup.addChild( primitiveGroup );

		// 6: TransformGroup
		TransformGroup transformGroup = new TransformGroup( );
		transformGroup.setCapability( TransformGroup.ALLOW_TRANSFORM_WRITE );
		transformGroup.setCapability( TransformGroup.ALLOW_TRANSFORM_READ );

		Transform3D yAxis = new Transform3D( );
		Alpha rotationAlpha = new Alpha( -1, Alpha.INCREASING_ENABLE,
			0, 0,
			4000, 0, 0,
			0, 0, 0 );

		RotationInterpolator rotator = new RotationInterpolator( rotationAlpha, transformGroup, yAxis, 0.0f, (float) Math.PI*2.0f );
		rotator.setSchedulingBounds( createApplicationBounds( ) );
		transformGroup.addChild( rotator );

		transformGroup.addChild( new ColorCube( 2 ) );
		transformGroup.addChild( createLabel( "6. TransformGroup", labelScale ) );
		switchGroup.addChild( transformGroup );

		// 7: add another copy of the shared group
		switchGroup.addChild( new Link( sharedGroup1 ) );

		// create a SwitchValueInterpolator to
		// cycle through the child nodes in the Switch Node
		Alpha switchAlpha = new Alpha( -1, Alpha.INCREASING_ENABLE,
			0, 0,
			10000, 0, 0,
			0, 0, 0 );

		SwitchValueInterpolator switchInterpolator = new SwitchValueInterpolator( switchAlpha, switchGroup );
		switchInterpolator.setSchedulingBounds( createApplicationBounds( ) );
		switchInterpolator.setEnable( true );

		// WARNING: do not add the SwitchValueInterpolator to the Switch Node!
		objRoot.addChild( switchInterpolator );

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
			0.5 * (1 - m_nLabelNumber),
			0 ) );

		t3d.setScale( scale );


		tg.setTransform( t3d );
		tg.addChild( label3D );

		m_nLabelNumber++;

		return tg;
	}


	public static void main( String[] args )
	{
		NodesTest nodesTest = new NodesTest( );
		nodesTest.saveCommandLineArguments( args );

		new MainFrame( nodesTest, m_kWidth, m_kHeight );
	}
}
