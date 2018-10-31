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
 * 13.04.2008 - Version 0.2.1
 * - Copyright geändert
 * 19.10.2007 - Version 0.2
 * - Kopie von AboutDialog
 * - In Splash-Fenster umdesigned
 * 11.09.2007 - Version 0.1.2
 *  - Schriftgröße geändert
 * 30.07.2007 - Version 0.1.1
 * - Groeße angepasst
 * 29.07.2007 - Version 0.1
 *  - Datei hinzugefuegt
 */
package info.kriese.sopra.gui;

import info.kriese.sopra.io.Settings;
import info.kriese.sopra.io.impl.SettingsFactory;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.util.GregorianCalendar;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

/**
 * Stellt den SplashScreen beim Programmstart dar.
 * 
 * @author Michael Kriese
 * @version 0.2.1
 * @since 19.10.2007
 */
public class SplashDialog extends JDialog {

    /**
     * Einzige Instanz dieser KLasse.
     */
    private static SplashDialog instance;

    /**
     * Dient zur Serialisierung (Hat bei uns keine Verwendung).
     */
    private static final long serialVersionUID = -254806736634900337L;

    /**
     * Gibt eine Referenz auf den SplashScreen zurück.
     * 
     * @return Referenz auf SplashScreen.
     */
    public static SplashDialog getInstance() {
	if (instance == null)
	    instance = new SplashDialog();
	return instance;
    }

    /**
     * Erstellt dern Hauptrahmen des Fensters.
     * 
     * @return Ein Rahmen.
     */
    private static Border createMainBorder() {
	Border inner, outer;
	outer = BorderFactory.createBevelBorder(BevelBorder.RAISED,
		Color.LIGHT_GRAY, Color.GRAY, Color.LIGHT_GRAY, Color.GRAY);
	inner = BorderFactory.createEmptyBorder(20, 20, 10, 20);
	return BorderFactory.createCompoundBorder(outer, inner);
    }

    /**
     * Versucht ein Bild zu laden und gibt es gegebenenfalls zurück.
     * 
     * @param path -
     *                Relativer Pfad zum Bild
     * @return Das gefundene Bild, andernfalls gibt es "NULL" zurück.
     */
    protected static ImageIcon createImageIcon(String path) {
	java.net.URL imgURL = SplashDialog.class.getResource(path);
	if (imgURL != null)
	    return new ImageIcon(imgURL);
	else {
	    System.err.println("Couldn't find file: " + path);
	    return null;
	}
    }

    /**
     * Referenzen auf die LogoZeichnugsfläche und die Nachrichtenfläche.
     */
    private final JLabel pictureLabel, msg;

    /**
     * Referenz auf die Programmeinstellungen.
     */
    private final Settings props = SettingsFactory.getInstance();

    /**
     * Konstruktor, welcher alle Variablen und Objekte initialisiert.
     */
    private SplashDialog() {
	setTitle("Splash " + this.props.getName());
	setUndecorated(true);
	setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

	JPanel pn = new JPanel();
	pn.setBorder(createMainBorder());
	pn.setBackground(new Color(0, 128, 0));
	pn.setLayout(new BorderLayout());

	this.pictureLabel = new JLabel();
	this.pictureLabel.setPreferredSize(new Dimension(200, 200));
	updatePicture();

	pn.add(this.pictureLabel, BorderLayout.CENTER);

	JLabel info = new JLabel(
		"<html><font color=\"#FFFFFF\"><center><font size=\"5\">"
			+ this.props.getTitle()
			+ "</font><br><br> Version: "
			+ this.props.getVersion()
			+ "<br><br>Copyright: &copy; 2007 - "
			+ GregorianCalendar.getInstance().get(
				GregorianCalendar.YEAR) + "<br> "
			+ this.props.getAuthor() + " <br><br>" + "eMail: "
			+ this.props.getMail() + "<br>Web: "
			+ this.props.getWeb() + "</center></font></html>");

	info.setPreferredSize(new Dimension(250, 200));
	info.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
	pn.add(info, BorderLayout.EAST);

	this.msg = new JLabel();
	this.msg.setForeground(Color.WHITE);
	this.msg.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));
	this.msg.setPreferredSize(new Dimension(450, 50));
	pn.add(this.msg, BorderLayout.SOUTH);
	add(pn);
	pack();
	setLocationRelativeTo(null);
    }

    /**
     * Setzt die Nachricht, die dem Benutzer auf dem SplashScreen angezeigt
     * wird.
     * 
     * @param msg -
     *                Nachricht, die angezeigt werden soll.
     */
    public void setMessage(String msg) {
	this.msg.setText(msg);
    }

    /**
     * Versucht das Programmlogo auf die BildZeichenfläche zu rendern.
     */
    protected void updatePicture() {
	// Get the icon corresponding to the image.
    ImageIcon icon = createImageIcon("images/logo.png");
    if (icon == null) {
        this.pictureLabel.setText("Missing Image");
        return;
    }
	Image im = icon.getImage();
	im = im.getScaledInstance(200, 200, Image.SCALE_SMOOTH);

	this.pictureLabel.setIcon(new ImageIcon(im));
    //this.pictureLabel.setText(null);
    }
}
