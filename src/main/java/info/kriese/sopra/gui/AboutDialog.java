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
 * 13.04.2008 - Version 0.2.3
 * - Copyright geändert
 * 23.10.2007 - Version 0.2.1
 * - An neues Logo angepasst
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
package info.kriese.sopra.gui;

import info.kriese.sopra.gui.lang.Lang;
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

/**
 * Diese Klasse erstellt ein kleines Aboutfenster.
 * 
 * @author Michael Kriese
 * @version 0.2.2
 * @since 29.07.2007
 * 
 */
public class AboutDialog extends JDialog {

    /**
     * Private Instanz des Aboutfensters, da es diese nur einmal geben darf.
     */
    private static AboutDialog instance;

    /** Dient zur Serialisierung. (Hat bei uns keine Verwendung) */
    private static final long serialVersionUID = -254806736634900337L;

    /**
     * Erstellt Das Aboutfenster und gibt eine Referenz darauf zurück.
     * 
     * @param owner -
     *                Übergeordnetes Fenster, über dem das Aboutfenster
     *                zentriert werden soll.
     * @return Referenz auf das Aboutfenster.
     */
    public static AboutDialog getInstance(MainFrame owner) {
	if (instance == null)
	    instance = new AboutDialog(owner);
	return instance;
    }

    /**
     * Versucht ein Bild relativ zum Speicherort dieser Klasse zu laden und gibt
     * es dann zurück.
     * 
     * @param path -
     *                Pfad inklusive Dateiname des Bildes
     * @return Gibt das gefundene Bild zurück, oder "null", falls das Bild nicht
     *         gefunden wurde.
     */
    protected static ImageIcon createImageIcon(String path) {
	java.net.URL imgURL = AboutDialog.class.getResource(path);
	if (imgURL != null)
	    return new ImageIcon(imgURL);
	else {
	    System.err.println("Couldn't find file: " + path);
	    return null;
	}
    }

    /**
     * Label, auf dem das Programmlogo angezeigt wird.
     */
    private final JLabel pictureLabel;

    /**
     * Eine Referenz auf die Einstellungen des Programms.
     */
    private final Settings props = SettingsFactory.getInstance();

    /**
     * Konstruktor, welcher die Einstellungen lädt und alle Variablen
     * initialisiert.
     * 
     * @param owner
     *                Hauptfenster, zu dem das Aboutfenster gehört.
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

	JLabel info = new JLabel("<html><body style=\"width:180px\">"
		+ "<font color=\"#FFFFFF\"><center><font size=\"5\">"
		+ this.props.getTitle() + "</font><br><br> Version: "
		+ this.props.getVersion() + "<br><br>Copyright: &copy; 2007 - "
		+ GregorianCalendar.getInstance().get(GregorianCalendar.YEAR)
		+ "<br> " + this.props.getAuthor() + " <br><br>" + "eMail: "
		+ this.props.getMail() + "<br>Web: " + this.props.getWeb()
		+ "</center></font></body></html>");
	pn.add(info, BorderLayout.EAST);

	pack();
    }

    /**
     * Diese Funktion versucht das Logo auf das Panel zu bringen.
     */
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
