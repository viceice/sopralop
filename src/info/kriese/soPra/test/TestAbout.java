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
 *  - Datei hinzugefuegt
 */
package info.kriese.soPra.test;

import info.kriese.soPra.gui.AboutDialog;
import info.kriese.soPra.io.impl.SettingsFactory;

import javax.swing.JDialog;

/**
 * 
 * @author Michael Kriese
 * @version 0.1.1
 * @since 17.12.2007
 * 
 */
public final class TestAbout {
    private static AboutDialog about;

    /**
     * @param args
     */
    public static void main(String[] args) {
	// Parse commandline arguments
	SettingsFactory.parseArgs(args);

	SettingsFactory.initJava();

	SettingsFactory.showTitle("AboutTest");

	about = AboutDialog.getInstance(null);
	about.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

	about.setVisible(true);
    }

}
