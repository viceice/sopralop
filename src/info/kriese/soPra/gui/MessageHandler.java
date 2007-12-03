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
 * 03.12.2007 - Version 0.1
 *  - Datei hinzugefuegt
 */
package info.kriese.soPra.gui;

import info.kriese.soPra.gui.lang.Lang;

import java.awt.Component;

import javax.swing.JOptionPane;

/**
 * Klasse zum Anzeigen von Standard-Dialogen.
 * 
 * @author Michael Kriese
 * @version 0.1
 * @since 03.12.2007
 * 
 */
public final class MessageHandler {

    private static Component PARENT = null;

    public static Component getParent() {
	return PARENT;
    }

    public static void setParent(Component c) {
	PARENT = c;
    }

    public static int showConfirmDialog(String title, String msg, int opt) {
	return JOptionPane.showConfirmDialog(PARENT, msg, title, opt);
    }

    public static void showError(String title, String msg) {
	JOptionPane.showMessageDialog(PARENT, msg, title,
		JOptionPane.ERROR_MESSAGE);
    }

    public static void showInfo(String title, String msg) {
	JOptionPane.showMessageDialog(PARENT, msg, title,
		JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showNotImplemented() {
	showInfo(Lang.getString("Errors.NotImplemented"), Lang
		.getString("Errors.NotImplemented.Title"));
    }

    private MessageHandler() {

    }
}
