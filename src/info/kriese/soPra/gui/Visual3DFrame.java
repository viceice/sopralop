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
 * 19.10.2007 - Version 0.2.4
 * - Icon in Fenster-Deko gesetzt
 * - Multisprachfähigkeit hinzugefügt
 * 17.09.2007 - Version 0.2.3
 * - Falls owner = null Programm beenden, sonst verstecken
 * 11.09.2007 - Version 0.2.2
 * - Konstruktor geändert, um Fenster relativ zum Hauptfenster zu setzen
 * 29.07.2007 - Version 0.2.1
 * - Programmversion wird aus Settings gelesen
 * 27.07.2007 - Version 0.2
 * - Neuimplementierung des Canvas3D
 * - Canvas in JPannel verschoben
 * - [ESC] schliesst Fenster  
 * 11.05.2007 - Version 0.1
 * - Datei hinzugefuegt
 */
package info.kriese.soPra.gui;

import info.kriese.soPra.gui.lang.Lang;
import info.kriese.soPra.io.impl.SettingsFactory;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.media.j3d.Canvas3D;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * @author Michael Kriese
 * @version 0.2.4
 * @since 11.05.2007
 * 
 */
public final class Visual3DFrame extends JFrame implements Virtual3DFrame {

    private static final int PHEIGHT = 512;

    private static final int PWIDTH = 512; // size of panel

    /** */
    private static final long serialVersionUID = 6930931764821628616L;

    private boolean close;

    private final JPanel pn;

    public Visual3DFrame(JFrame owner) {
	super(Lang.getString("Visual.Title") + " - Version "
		+ SettingsFactory.getInstance().getVersion());

	if (owner == null) {
	    this.close = true;
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	} else {
	    setLocationRelativeTo(owner);
	    setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	    this.close = false;
	}

	setLayout(new BorderLayout());
	setExtendedState(JFrame.MAXIMIZED_BOTH);
	setSize(new Dimension(600, 500));

	ImageIcon ico = MenuMaker.getImage("MainFrame");
	if (ico != null)
	    setIconImage(ico.getImage());

	this.pn = new JPanel();
	this.pn.setLayout(new BorderLayout());
	this.pn.setOpaque(false);
	this.pn.setPreferredSize(new Dimension(PWIDTH, PHEIGHT));

	add(this.pn, BorderLayout.CENTER);
    }

    public void addCanvas(Canvas3D canvas) {
	this.pn.add("Center", canvas);
	canvas.setFocusable(true); // give focus to the canvas
	canvas.requestFocus();

	canvas.addKeyListener(new KeyAdapter() {

	    @Override
	    public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
		    if (Visual3DFrame.this.close)
			System.exit(0);
		    else
			setVisible(false);
	    }
	});
    }

}
