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
 * 21.05.2008 - Version 0.1.6
 * - Hintergrung kann zwischen weiß & schwarz wechseln
 * 20.05.2008 - Version 0.1.5
 * - ScreenShot der Szene wird erstellt
 * 04.03.2008 - Version 0.1.4
 * - Fensterposition angepasst
 * - Mit [F5] kann die Szene zurückgesetzt werden
 * - Doppelte Ausgabe entfernt
 * 26.01.2008 - Version 0.1.3
 * - An neue SettingsFactory angepasst.
 * 17.01.2008 - Verison 0.1.2
 * - [F7] wechselt in die Duales-Problem-Ansicht
 * 02.10.2007 - Version 0.1.1
 * - Einige Infos aus den Settings laden
 * 17.09.2007 - Version 0.1
 *  - Datei hinzugefuegt
 */
package info.kriese.sopra.test;

import info.kriese.sopra.engine3D.Engine3D;
import info.kriese.sopra.gui.Virtual3DFrame;
import info.kriese.sopra.gui.Visual3DFrame;
import info.kriese.sopra.gui.lang.Lang;
import info.kriese.sopra.io.IOUtils;
import info.kriese.sopra.io.impl.SettingsFactory;
import info.kriese.sopra.lop.LOP;
import info.kriese.sopra.lop.LOPEditor;
import info.kriese.sopra.lop.impl.LOPFactory;
import info.kriese.sopra.math.LOPSolver;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.media.j3d.Canvas3D;
import javax.swing.JFrame;

/**
 * Programm zum testen der 3D-Szene.
 * 
 * @author Michael Kriese
 * @version 0.1.6
 * @since 17.09.2007
 * 
 */
public final class TestView {

    private static Engine3D engine;
    private static LOP lop;
    private static final String SAMPLE = "S03";

    /**
     * @param args
     */
    public static void main(String[] args) {

	// Parse commandline arguments
	SettingsFactory.parseArgs(args);

	SettingsFactory.initJava();

	SettingsFactory.setDebug(true);

	SettingsFactory.showTitle("VisualTest");

	lop = LOPFactory.newLinearOptimizingProblem();
	LOPEditor editor = LOPFactory.newLOPEditor(lop);
	LOPSolver solver = new LOPSolver();
	solver.setEditor(editor);

	Visual3DFrame view = new Visual3DFrame(null);
	view.setExtendedState(JFrame.NORMAL);
	view.setLocationRelativeTo(null);

	engine = new Engine3D();
	engine.addConnection(view);
	engine.addConnection(new Virtual3DFrame() {

	    public void addCanvas(Canvas3D canvas) {
		canvas.addKeyListener(new KeyAdapter() {
		    private boolean dual = true;

		    @Override
		    public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_F7) {
			    if (this.dual) {
				lop.showDualProblem();
				this.dual = false;
			    } else {
				lop.showPrimalProblem();
				this.dual = true;
			    }
			    e.consume();
			} else if (e.getKeyCode() == KeyEvent.VK_F5) {
			    engine.resetScene();
			    e.consume();
			} else if (e.getKeyCode() == KeyEvent.VK_F2)
			    engine.captureImage(new File("test.png"));
			else if (e.getKeyCode() == KeyEvent.VK_F3)
			    engine.switchBackgroundColor();
			e.consume();
		    }
		});
	    }
	});
	engine.setLOPEditor(editor);

	editor.open(IOUtils.getURL("problems/"
		+ Lang.getString("Menu.File.Samples." + SAMPLE + ".File")
		+ ".lop"));

	view.setVisible(true);
    }
}
