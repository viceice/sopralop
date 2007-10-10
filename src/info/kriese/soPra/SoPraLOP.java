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
 * 14.09.2007 - Version 0.2.1
 * - Property für die RenderEngine hizugefügt (Unter Vista schlechte Performance mit OpenGL)
 * 11.09.2007- Version 0.2
 * - Default DateiCodierung auf UTF-8 geändert
 * 29.07.2007 - Version 0.1.1
 * - unnoetige Variablen geloescht
 * 12.05.2007 - Version 0.1
 * - Datei hinzugefuegt
 */
package info.kriese.soPra;

import info.kriese.soPra.gui.MainFrame;

/**
 * @author Michael Kriese
 * @version 0.2.1
 * @since 12.05.2007
 * 
 */
public final class SoPraLOP {

    /**
     * @param args
     */
    public static void main(String[] args) {

	System.setProperty("file.encoding", "UTF-8");

	String os = System.getProperty("os.name");
	System.out.println("OS: " + os);
	if (os != null && os.toLowerCase().contains("vista")) {
	    System.setProperty("j3d.rend", "d3d");
	    System.out.println("On Vista we have to use D3D Renderer! :-(");
	} else
	    System.setProperty("j3d.rend", "ogl");

	MainFrame frame = new MainFrame();
	frame.setLocationRelativeTo(null);
	frame.setVisible(true);
    }

    MainFrame frame;
}
