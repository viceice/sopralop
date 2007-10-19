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
 * 29.07.2007 - Version 0.1
 * - Datei hinzugefuegt
 */
package info.kriese.soPra.gui;

import info.kriese.soPra.gui.lang.Lang;

import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

/**
 * 
 * @author Michael Kriese
 * @version 0.1
 * @since 29.07.2007
 * 
 */
public final class MenuMaker {

    public static ImageIcon getImage(String key) {
	String imgLocation = Lang.getString(key + ".Image");
	URL imageURL = MenuMaker.class.getResource(imgLocation);
	if (imageURL != null)
	    return new ImageIcon(imageURL, getMenuTitle(key));
	else
	    return null;
    }

    public static JMenu getMenu(String key) {
	JMenu res = new JMenu(getMenuTitle(key));
	res.setMnemonic(getMenuMnemonic(key));
	res.setIcon(getImage(key));
	return res;
    }

    public static KeyStroke getMenuAccelerator(String key) {
	KeyStroke res = KeyStroke.getKeyStroke(Lang.getString(key
		+ ".Accelerator"));
	return res;
    }

    public static JMenuItem getMenuItem(String key, ActionListener ac) {
	JMenuItem res = new JMenuItem(getMenuTitle(key));
	res.setMnemonic(getMenuMnemonic(key));
	res.setAccelerator(getMenuAccelerator(key));
	res.setActionCommand(key);
	res.setIcon(getImage(key));
	res.addActionListener(ac);
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

    public static JButton getToolBarButton(String key, ActionListener ac) {
	JButton button = new JButton();
	button.setFocusable(false);
	button.setActionCommand(key);
	button.setToolTipText(getMenuTitle(key));
	button.addActionListener(ac);
	ImageIcon icon = getImage(key);

	if (icon != null)
	    button.setIcon(icon);
	else
	    button.setText(getMenuTitle(key));

	return button;
    }

    private MenuMaker() {
    }

}
