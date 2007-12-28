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
 * 25.10.2007 - Version 0.1
 *  - Datei hinzugefügt
 */
package info.kriese.soPra.test;

import java.util.Locale;

import info.kriese.soPra.gui.HelpDialog;
import info.kriese.soPra.io.impl.SettingsFactory;

import javax.swing.JDialog;

/**
 * @author Peer Sterner
 * @version 0.1
 * @since 25.10.2007
 */
public class TestHelp {

    /**
     * @param args
     */
    public static void main(String[] args) {
	SettingsFactory.parseArgs(args);
	SettingsFactory.setDebug(true);

	System.out.println("SoPraLOP HelpTest - Version "
		+ SettingsFactory.getInstance().getVersion());
	System.out.println("\t(c) 2007  "
		+ SettingsFactory.getInstance().getAuthor());
	System.out.println();
	System.out.println("Locale: " + Locale.getDefault());

	HelpDialog help = HelpDialog.getInstance();

	help.setHelp("index.htm");

	help.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

	help.setVisible(true);

    }

}
