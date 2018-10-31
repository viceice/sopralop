/**
 * @version		$Id$
 * @copyright	(c)2007-2008 Michael Kriese & Peer Sterner
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
 * 26.01.2008 - Version 0.1.1
 * - An neue SettingsFactory angepasst.
 * 17.12.2007 - Version 0.1
 *  - Datei hinzugefügt
 */
package info.kriese.sopra.test;

import info.kriese.sopra.gui.AboutDialog;
import info.kriese.sopra.gui.MessageHandler;
import info.kriese.sopra.io.impl.SettingsFactory;

import java.awt.Container;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * 
 * @author Michael Kriese
 * @version 0.1.1
 * @since 17.12.2007
 * 
 */
public final class SplashGenerator {
    private static AboutDialog about;

    /**
     * Erstellt ein SplashImage aus dem AboutFenster.
     * 
     * @param args -
     *                Parameter, werden nicht benötigt
     */
    public static void main(String[] args) {

	// Parse commandline arguments
	SettingsFactory.parseArgs(args);

	SettingsFactory.initJava();

	SettingsFactory.showTitle("SplashGenerator");

	about = AboutDialog.getInstance(null);

	System.out.print("Create Splash Image ...");

	Container c = about.getContentPane();

	// Create a buffered image in which to draw
	BufferedImage bufferedImage = new BufferedImage(c.getWidth(), c
		.getHeight(), BufferedImage.TYPE_INT_RGB);

	// Draw graphics
	c.paint(bufferedImage.getGraphics());

	try {
	    File file = new File("splash.png");
	    ImageIO.write(bufferedImage, "png", file);
	} catch (IOException ex) {
	    MessageHandler.setParent(about);
	    MessageHandler.showError("Error: " + ex.getClass().getSimpleName(),
		    ex.getLocalizedMessage());
	    System.out.println(" Error!\nError: " + ex.getLocalizedMessage());
	    System.exit(1);
	}

	System.out.println(" Ready!");
	System.exit(0);
    }

}
