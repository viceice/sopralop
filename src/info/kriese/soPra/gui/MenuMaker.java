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
 * 27.12.2007 - Version 0.3.2
 * - Konstante SEPARATOR hinzugefügt
 * 04.11.2007 - Version 0.3.1
 * - BugFix: NullPointer falls ActionHandler null behoben
 * 01.11.2007 - Version 0.3
 * - Es kann ein standard ActionHandler registriert werden, so muss er nicht 
 *   jedesmal übergeben werden. (weniger Referenzen)
 * 24.10.2007 - Version 0.2
 * - An neuen ActionHandler angepasst
 * 29.07.2007 - Version 0.1
 * - Datei hinzugefuegt
 */
package info.kriese.soPra.gui;

import info.kriese.soPra.gui.lang.Lang;

import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

/**
 * Mit dieser Klasse lassen sich leicht mehrsprachfähige Menüs und Tollbars
 * erstellen.
 * 
 * @author Michael Kriese
 * @version 0.3.2
 * @since 29.07.2007
 */
public final class MenuMaker {

    /**
     * Dient zur Erkennung von Separatoren in Menüs und Toolsbars.
     */
    public static final String SEPARATOR = "SEPARATOR";

    /**
     * AtionHandler, er wird durch Menüs und Toolbars aufgerufen.
     */
    private static ActionHandler AC = null;

    /**
     * Versucht ein Bild zu laden und gibt es gegebenenfalls zurück.
     * 
     * @param key -
     *                Schlüssel, über den der Pfad aus den Einstellungen geladen
     *                wird.
     * @return Das geladene Bild oder "NULL", falls das Bild nicht gefunden
     *         wurde
     */
    public static ImageIcon getImage(String key) {
	String imgLocation = Lang.getString(key + ".Image");
	URL imageURL = MenuMaker.class.getResource(imgLocation);
	if (imageURL != null)
	    return new ImageIcon(imageURL, getMenuTitle(key));
	else
	    return null;
    }

    /**
     * Erstellt ein Hauptmenüeintrag.
     * 
     * @param key -
     *                Schlüssel, über den die Einstellungen geladen werden.
     * @return Ein Hauptmenüeitrag.
     */
    public static JMenu getMenu(String key) {
	JMenu res = new JMenu(getMenuTitle(key));
	res.setMnemonic(getMenuMnemonic(key));
	res.setIcon(getImage(key));
	return res;
    }

    /**
     * Erstellt eine Tastenkombination, mit der ein Menüeintrag ausgewählt
     * werden kann.
     * 
     * @param key -
     *                Schlüssel, über den die Einstellung gefunden werden kann.
     * @return Eine Tastenkombination.
     */
    public static KeyStroke getMenuAccelerator(String key) {
	KeyStroke res = KeyStroke.getKeyStroke(Lang.getString(key
		+ ".Accelerator"));
	return res;
    }

    public static JMenuItem getMenuItem(String key) {
	return getMenuItem(key, AC);
    }

    public static JMenuItem getMenuItem(String key, ActionHandler ac) {
	JMenuItem res = new JMenuItem(getMenuTitle(key));
	res.setMnemonic(getMenuMnemonic(key));
	res.setAccelerator(getMenuAccelerator(key));
	res.setActionCommand(key);
	res.setIcon(getImage(key));
	ac.add(res);
	return res;
    }

    public static int getMenuMnemonic(String key) {
	KeyStroke res = KeyStroke.getKeyStroke(Lang
		.getString(key + ".Mnemonic"));
	if (res != null)
	    return res.getKeyCode();
	else
	    return -1;
    }

    public static String getMenuTitle(String key) {
	return Lang.getString(key + ".Title");
    }

    public static JButton getToolBarButton(String key) {
	return getToolBarButton(key, AC);
    }

    public static JButton getToolBarButton(String key, ActionHandler ac) {
	JButton button = new JButton();
	button.setFocusable(false);
	button.setActionCommand(key);
	button.setToolTipText(getMenuTitle(key));
	if (ac != null)
	    ac.add(button);
	ImageIcon icon = getImage(key);

	if (icon != null)
	    button.setIcon(icon);
	else
	    button.setText(getMenuTitle(key));

	return button;
    }

    public static void setDefaultActionHandler(ActionHandler ac) {
	AC = ac;
    }

    private MenuMaker() {
    }

}
