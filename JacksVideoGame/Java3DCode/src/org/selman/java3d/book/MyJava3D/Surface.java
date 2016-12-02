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
import java.awt.image.*;
import java.awt.print.*;
import java.awt.event.*;
import javax.swing.JPanel;
import javax.swing.RepaintManager;


/**
 * The Surface class implements a 2D rendering surface
 * using a Swing JPanel. The Surface can contain an AlphaComposite
 * and a background Texture as well as foreground rendered output.
 * <p>
 * The Surface can have anti-aliasing enabled and be optimized
 * for speed or quality.
 * <p>
 * This class is based on that found in the Java 2D examples.
 */
public abstract class Surface extends JPanel implements Printable 
{
	public Object AntiAlias = RenderingHints.VALUE_ANTIALIAS_OFF;
	public Object Rendering = RenderingHints.VALUE_RENDER_SPEED;
	public AlphaComposite composite;
	public Paint texture;
	public String perfStr;            // PerformanceMonitor
	public BufferedImage bimg;
	public int imageType;
	public String name;         
	public boolean clearSurface = true;
	public AnimatingSurface animating;

	protected long sleepAmount = 0;

	private long orig, start, frame;
	private Toolkit toolkit;
	private int biw, bih;
	private boolean clearOnce;

	public Surface( )
	{
		toolkit = getToolkit( );
		setImageType( 0 );

		if (this instanceof AnimatingSurface)
		{
			animating = (AnimatingSurface) this;
		}
	}


	public int getImageType( )
	{
		return imageType;
	}

	public void setImageType( int imgType )
	{
		if (imgType == 0)
		{
			if (this instanceof AnimatingSurface)
			{
				imageType = 2;
			}
			else
			{
				imageType = 1;
			}
		}
		else
		{
			imageType = imgType;
		}
		bimg = null;
	}


	public void setAntiAlias( boolean aa )
	{
		AntiAlias = aa 
			? RenderingHints.VALUE_ANTIALIAS_ON
			: RenderingHints.VALUE_ANTIALIAS_OFF;
	}


	public void setRendering( boolean rd )
	{
		Rendering = rd
			? RenderingHints.VALUE_RENDER_QUALITY
			: RenderingHints.VALUE_RENDER_SPEED;
	}


	public void setTexture( Object obj )
	{
		if (obj instanceof GradientPaint)
		{
			texture = new GradientPaint( 0, 0, Color.white,
				getSize( ).width*2, 0, Color.green );
		}
		else
		{
			texture = (Paint) obj;
		}
	}


	public void setComposite( boolean cp )
	{
		composite = cp 
			? AlphaComposite.getInstance( AlphaComposite.SRC_OVER, 0.5f ) 
		: null;
	}


	public void setSleepAmount( long amount )
	{
		sleepAmount = amount;
	}


	public long getSleepAmount( )
	{
		return sleepAmount;
	}


	public BufferedImage createBufferedImage( int w, int h, int imgType )
	{
		BufferedImage bi = null;
		if (imgType == 0)
		{
			bi = (BufferedImage) createImage( w, h );  
		}
		else if (imgType > 0 && imgType < 14)
		{
			bi = new BufferedImage( w, h, imgType );
		}
		else if (imgType == 14)
		{
			bi = createBinaryImage( w, h, 2 );
		}
		else if (imgType == 15)
		{
			bi = createBinaryImage( w, h, 4 );
		}
		biw = w;
		bih = h;
		return bi;
	}


	// Lookup tables for BYTE_BINARY 1, 2 and 4 bits.
	static byte[] lut1Arr = new byte[] {0, (byte)255 };
	static byte[] lut2Arr = new byte[] {0, (byte)85, (byte)170, (byte)255};
	static byte[] lut4Arr = new byte[] {0, (byte)17, (byte)34, (byte)51,
                                  (byte)68, (byte)85,(byte) 102, (byte)119,
                                  (byte)136, (byte)153, (byte)170, (byte)187,
                                  (byte)204, (byte)221, (byte)238, (byte)255};


	private BufferedImage createBinaryImage( int w, int h, int pixelBits )
	{

		int[] pixels = new int[w*h];
		int bytesPerRow = w * pixelBits / 8;
		if (w * pixelBits % 8 != 0)
		{
			bytesPerRow++;
		}
		byte[] imageData = new byte[h * bytesPerRow];
		IndexColorModel cm = null;
		switch (pixelBits)
		{
		case 1:
			cm = new IndexColorModel( pixelBits, lut1Arr.length,
				lut1Arr, lut1Arr, lut1Arr );
			break;
		case 2:
			cm = new IndexColorModel( pixelBits, lut2Arr.length,
				lut2Arr, lut2Arr, lut2Arr );
			break;
		case 4:
			cm = new IndexColorModel( pixelBits, lut4Arr.length,
				lut4Arr, lut4Arr, lut4Arr );
			break;
		default:
			{
           	new Exception( "Invalid # of bit per pixel" ).printStackTrace( );
			}
		}

		DataBuffer db = new DataBufferByte( imageData, imageData.length );
		WritableRaster r = Raster.createPackedRaster( db, w, h, pixelBits, null );
		return new BufferedImage( cm, r, false, null );
	}


	public Graphics2D createGraphics2D( int width, 
		int height, 
		BufferedImage bi, 
		Graphics g )
	{

		Graphics2D g2 = null;

		if (bi != null)
		{
			g2 = bi.createGraphics( );
		}
		else
		{
			g2 = (Graphics2D) g;
		}

		g2.setBackground( getBackground( ) );
		g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING, AntiAlias );
		g2.setRenderingHint( RenderingHints.KEY_RENDERING, Rendering );

		if (clearSurface || clearOnce)
		{
			g2.clearRect( 0, 0, width, height );
			clearOnce = false;
		}

		if (texture != null)
		{
			// set composite to opaque for texture fills
			g2.setComposite( AlphaComposite.SrcOver );
			g2.setPaint( texture );
			g2.fillRect( 0, 0, width, height );
		}

		if (composite != null)
		{
			g2.setComposite( composite );
		}

		return g2;
	}

	// ...demos that extend Surface must implement this routine...
	public abstract void render( int w, int h, Graphics2D g2 );


	/**
	* It's possible to turn off double-buffering for just the repaint 
	* calls invoked directly on the non double buffered component.  
	* This can be done by overriding paintImmediately() (which is called 
	* as a result of repaint) and getting the current RepaintManager and 
	* turning off double buffering in the RepaintManager before calling 
	* super.paintImmediately(g).
	*/
	public void paintImmediately( int x,int y,int w, int h )
	{
		RepaintManager repaintManager = null;
		boolean save = true;
		if (!isDoubleBuffered( ))
		{
			repaintManager = RepaintManager.currentManager( this );
			save = repaintManager.isDoubleBufferingEnabled( );
			repaintManager.setDoubleBufferingEnabled( false );
		}
		super.paintImmediately( x, y, w, h );

		if (repaintManager != null)
		{
			repaintManager.setDoubleBufferingEnabled( save );
		}
	}


	public void paint( Graphics g )
	{
		Dimension d = getSize( );

		if (imageType == 1)
		{
			bimg = null;
			startClock( );
		}
		else if (bimg == null || biw != d.width || bih != d.height)
		{
			if (animating != null && (biw != d.width || bih != d.height))
			{
				animating.reset( d.width, d.height );
			}
			bimg = createBufferedImage( d.width, d.height, imageType-2 );
			clearOnce = true;
			startClock( );
		}

		if (animating != null && animating.thread != null)
		{
			animating.step( d.width, d.height );
		}

		Graphics2D g2 = createGraphics2D( d.width, d.height, bimg, g );
		render( d.width, d.height, g2 );
		g2.dispose( );

		if (bimg != null)
		{
			g.drawImage( bimg, 0, 0, null );
			toolkit.sync( );
		}
	}


	public int print( Graphics g, PageFormat pf, int pi ) throws PrinterException
	{
		if (pi >= 1)
		{
			return Printable.NO_SUCH_PAGE;
		}

		Graphics2D g2d = (Graphics2D) g;
		g2d.translate( pf.getImageableX( ), pf.getImageableY( ) );
		g2d.translate( pf.getImageableWidth( ) / 2,
			pf.getImageableHeight( ) / 2 );

		Dimension d = getSize( );

		double scale = Math.min( pf.getImageableWidth( ) / d.width,
			pf.getImageableHeight( ) / d.height );
		if (scale < 1.0)
		{
			g2d.scale( scale, scale );
		}

		g2d.translate( -d.width / 2.0, -d.height / 2.0 );

		if (bimg == null)
		{
			Graphics2D g2 = createGraphics2D( d.width, d.height, null, g2d );
			render( d.width, d.height, g2 );
			g2.dispose( );
		}
		else
		{
			g2d.drawImage( bimg, 0, 0, this );
		}

		return Printable.PAGE_EXISTS;
	}


	private void startClock( )
	{
		orig = System.currentTimeMillis( );
		start = orig;
	}
}
