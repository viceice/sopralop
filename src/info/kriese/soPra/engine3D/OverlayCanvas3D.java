/**
 * @version		$Id$
 * @copyright	(c)2007 Michael Kriese & Peer Sterner
 * 
 * This file is part of SoPraLOP Project.
 *
 *  SoPraLOP Project is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; version 2 of the License.
 *
 *  SoPraLOP Project is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with SoPraLOP Project; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 * ChangeLog:
 * 
 * 27.07.2007 - Version 0.1
 * - Datei hinzugefuegt
 */
package info.kriese.soPra.engine3D;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.image.BufferedImage;

import javax.media.j3d.Canvas3D;
import javax.media.j3d.J3DGraphics2D;

/**
 * Stellt die Zeichenfl&auml;che f&uuml;r die 3D-Objekte dar.
 * 
 * @author Michael Kriese 
 * @since 27.07.2007
 * @version 0.1
 */
public class OverlayCanvas3D extends Canvas3D {
	/** */
	private static final long serialVersionUID = 192221366470599287L;

	private BufferedImage drawIm;

	private Graphics2D drawg2d; // for drawing into drawIm

	private J3DGraphics2D render2D; // for 2D rendering into the 3D canvas

	private int panelWidth, panelHeight; // size of drawing area

	// for displaying messages
	private Font msgsFont;
	
	private static final Color BG = new Color(0.3f, 1.0f, 0.3f, 0.5f);

	private int moveNum = 0;

	public OverlayCanvas3D(GraphicsConfiguration gc) {
		super(gc);
		setSize(new Dimension(512, 512));
		panelWidth = getWidth();
		panelHeight = getHeight();

		drawIm = new BufferedImage(panelWidth, panelHeight,
				BufferedImage.TYPE_4BYTE_ABGR);
		drawg2d = drawIm.createGraphics();

		render2D = this.getGraphics2D();

		msgsFont = new Font("Arial", Font.PLAIN, 12);
		
	}

	private void clearSurface() {
		drawg2d.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR,
				0.0f));
		drawg2d.fillRect(0, 0, panelWidth, panelHeight);
		drawg2d.setComposite(AlphaComposite.getInstance(
				AlphaComposite.SRC_OVER, 1.0f));
		drawg2d.setColor(Color.BLACK);
	}

	public void postRender() {
		clearSurface();

		// draw the firing info.
		drawg2d.setColor(BG);
		drawg2d.fillRect(0, 0, 60, 35);
		drawg2d.setFont(msgsFont);
		drawg2d.setColor(Color.YELLOW);
		drawg2d.drawString("fps: " + moveNum, 10, 20);

		render2D.drawAndFlushImage(drawIm, 0, 0, this);
	}
	
	public void setFPS(int value){
		moveNum = value;
	}
}