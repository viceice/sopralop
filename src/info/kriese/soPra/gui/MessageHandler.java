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
 * 29.01.2008 - Version 0.2.1
 * - Informationsdialoge werden asynchron angezeigt.
 * 19.12.2007 - Version 0.2
 * - getString mit Ersetzungsparametern hinzugefügt
 * 17.12.2007 - Version 0.1.2
 * - BugFix: Bei NotImplemented Meldung waren Titel und Nachricht vertauscht.
 * 04.12.2007 - Version 0.1.1
 * - An neues Hilfesystem angepasst
 * 03.12.2007 - Version 0.1
 *  - Datei hinzugefuegt
 */
package info.kriese.soPra.gui;

import info.kriese.soPra.gui.lang.Lang;
import info.kriese.soPra.io.impl.SettingsFactory;

import java.awt.Component;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * Klasse zum Anzeigen von Standard-Dialogen.
 * 
 * @author Michael Kriese
 * @version 0.2.1
 * @since 03.12.2007
 * 
 */
public final class MessageHandler {

    private static HelpProvider HELP = null;
    private static Component PARENT = null;

    public static void exceptionThrown(Exception e) {
	if (SettingsFactory.getInstance().isDebug())
	    e.printStackTrace();
	else
	    MessageHandler.showError(Lang.getString("Strings.Error"), Lang
		    .getString("Errors.Exception", new Object[] {
			    e.getClass().getSimpleName(),
			    e.getLocalizedMessage() }));
    }

    public static Component getParent() {
	return PARENT;
    }

    public static void setHelp(HelpProvider help) {
	HELP = help;
    }

    public static void setParent(Component c) {
	PARENT = c;
    }

    public static int showConfirmDialog(String title, String msg, int opt) {
	return JOptionPane.showConfirmDialog(PARENT, msg, title, opt);
    }

    public static void showError(final String title, final String msg) {
	SwingUtilities.invokeLater(new Runnable() {
	    public void run() {
		JOptionPane.showMessageDialog(PARENT, msg, title,
			JOptionPane.ERROR_MESSAGE);
	    }
	});
    }

    public static void showHelp() {
	showHelp(null);
    }

    public static void showHelp(String msg) {
	if (HELP != null)
	    HELP.showHelp(msg);
    }

    public static void showInfo(final String title, final String msg) {
	SwingUtilities.invokeLater(new Runnable() {
	    public void run() {
		JOptionPane.showMessageDialog(PARENT, msg, title,
			JOptionPane.INFORMATION_MESSAGE);
	    }
	});
    }

    public static void showNotImplemented() {
	showInfo(Lang.getString("Errors.NotImplemented.Title"), Lang
		.getString("Errors.NotImplemented"));
    }

    private MessageHandler() {

    }
}
