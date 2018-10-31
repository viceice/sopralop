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
package info.kriese.sopra.gui;

import info.kriese.sopra.gui.lang.Lang;
import info.kriese.sopra.io.impl.SettingsFactory;

import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.media.j3d.Canvas3D;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 * @author Michael Kriese
 * @version 0.2.4
 * @since 11.05.2007
 * 
 */
public final class Visual3DFrame extends JFrame implements Virtual3DFrame {

    /** */
    private static final long serialVersionUID = 6930931764821628616L;

    private boolean close;

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

	setSize(new Dimension(400, 400));
	setExtendedState(JFrame.MAXIMIZED_BOTH);

	ImageIcon ico = MenuMaker.getImage("MainFrame");
	if (ico != null)
	    setIconImage(ico.getImage());
    }

    public void addCanvas(Canvas3D canvas) {
	add(canvas);
	validate();
	repaint();
	canvas.setFocusable(true); // give focus to the canvas
	canvas.requestFocus();

	KeyListener l = new KeyAdapter() {

	    @Override
	    public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
		    if (Visual3DFrame.this.close)
			System.exit(0);
		    else
			setVisible(false);
	    }
	};

	canvas.addKeyListener(l);
	addKeyListener(l);
    }
}
