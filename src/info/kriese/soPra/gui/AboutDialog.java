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
 * 19.10.2007 - Version 0.2
 * - Fenster neu designed
 * - Multisprachfähigkeit hinzugefügt
 * 11.09.2007 - Version 0.1.2
 *  - Schriftgröße geändert
 * 30.07.2007 - Version 0.1.1
 * - Groeße angepasst
 * 29.07.2007 - Version 0.1
 *  - Datei hinzugefuegt
 */
package info.kriese.soPra.gui;

import info.kriese.soPra.gui.lang.Lang;
import info.kriese.soPra.io.Settings;
import info.kriese.soPra.io.impl.SettingsFactory;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AboutDialog extends JDialog {

    private static AboutDialog instance;

    /** */
    private static final long serialVersionUID = -254806736634900337L;

    public static AboutDialog getInstance(MainFrame owner) {
	if (instance == null)
	    instance = new AboutDialog(owner);
	return instance;
    }

    protected static ImageIcon createImageIcon(String path) {
	java.net.URL imgURL = AboutDialog.class.getResource(path);
	if (imgURL != null)
	    return new ImageIcon(imgURL);
	else {
	    System.err.println("Couldn't find file: " + path);
	    return null;
	}
    }

    private final JLabel pictureLabel;

    private final Settings props = SettingsFactory.getInstance();

    /**
     * 
     * @author Michael Kriese
     * @since 29.07.2007
     * @version 0.2
     * @param owner
     *                Hauptfenster
     */
    private AboutDialog(MainFrame owner) {
	super(owner, true);
	setTitle(Lang.getString("About.Title") + " " + this.props.getName());
	setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
	setLocationRelativeTo(owner);

	JPanel pn = new JPanel();
	pn.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
	pn.setBackground(new Color(0, 128, 0));
	pn.setLayout(new BorderLayout());
	add(pn);

	this.pictureLabel = new JLabel();
	this.pictureLabel.setPreferredSize(new Dimension(200, 200));
	updatePicture();
	pn.add(this.pictureLabel, BorderLayout.CENTER);

	JLabel info = new JLabel(
		"<html><font color=\"#FFFFFF\"><center><font size=\"5\">"
			+ this.props.getTitle() + "</font><br><br> Version: "
			+ this.props.getVersion()
			+ "<br><br>Copyright: &copy; 2007 <br> "
			+ this.props.getAuthor() + " <br><br>" + "eMail: "
			+ this.props.getMail() + "<br>Web: "
			+ this.props.getWeb() + "</center></font></html>");

	info.setPreferredSize(new Dimension(250, 200));
	info.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
	pn.add(info, BorderLayout.EAST);

	pack();
    }

    protected void updatePicture() {
	// Get the icon corresponding to the image.
	ImageIcon icon = createImageIcon("images/logo.png");
	Image im = icon.getImage();
	im = im.getScaledInstance(200, 200, Image.SCALE_SMOOTH);

	this.pictureLabel.setIcon(new ImageIcon(im));

	if (icon == null)
	    this.pictureLabel.setText("Missing Image");
	else
	    this.pictureLabel.setText(null);
    }
}
