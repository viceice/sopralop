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
 * 17.01.2008 - Verison 0.1.2
 * - [F7] wechselt in die Duales-Problem-Ansicht
 * 02.10.2007 - Version 0.1.1
 * - Einige Infos aus den Settings laden
 * 17.09.2007 - Version 0.1
 *  - Datei hinzugefuegt
 */
package info.kriese.soPra.test;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.media.j3d.Canvas3D;

import info.kriese.soPra.engine3D.Engine3D;
import info.kriese.soPra.gui.Virtual3DFrame;
import info.kriese.soPra.gui.Visual3DFrame;
import info.kriese.soPra.gui.lang.Lang;
import info.kriese.soPra.io.IOUtils;
import info.kriese.soPra.io.impl.SettingsFactory;
import info.kriese.soPra.lop.LOP;
import info.kriese.soPra.lop.LOPEditor;
import info.kriese.soPra.lop.impl.LOPFactory;
import info.kriese.soPra.math.LOPSolver;

/**
 * 
 * @author Michael Kriese
 * @version 0.1.2
 * @since 17.09.2007
 * 
 */
public final class TestView {

    private static boolean DUAL = true;
    private static LOP lop;
    private static final String SAMPLE = "S02";

    /**
     * @param args
     */
    public static void main(String[] args) {
	lop = LOPFactory.newLinearOptimizingProblem();
	LOPEditor editor = LOPFactory.newLOPEditor(lop);
	LOPSolver solver = new LOPSolver();
	solver.setEditor(editor);

	System.out.println("SoPraLOP VisualTest - Version "
		+ SettingsFactory.getInstance().getVersion());
	System.out.println("\t(c) 2007-2008  "
		+ SettingsFactory.getInstance().getAuthor());
	System.out.println();

	Visual3DFrame view = new Visual3DFrame(null);

	Engine3D engine = new Engine3D();
	engine.addConnection(view);
	engine.addConnection(new Virtual3DFrame() {

	    public void addCanvas(Canvas3D canvas) {
		canvas.addKeyListener(new KeyAdapter() {
		    @Override
		    public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_F7) {
			    if (DUAL) {
				lop.showDualProblem();
				DUAL = false;
			    } else {
				lop.showPrimalProblem();
				DUAL = true;
			    }
			    e.consume();
			}
		    }
		});
	    }
	});
	engine.setLOP(lop);

	editor.open(IOUtils.getURL("problems/"
		+ Lang.getString("Menu.File.Samples." + SAMPLE + ".File")
		+ ".lop"));

	System.err.flush();
	System.out.println("Problem: ");
	IOUtils.print(lop, System.out);

	view.setVisible(true);
    }

}
