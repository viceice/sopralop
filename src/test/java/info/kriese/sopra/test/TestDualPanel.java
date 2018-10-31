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
 * 10.11.2007 - Version 0.2
 * - Panel extrahiert
 * 07.11.2007 - Version 0.1
 *  - Datei erstellt
 */
package info.kriese.sopra.test;

import info.kriese.sopra.SoPraLOP;
import info.kriese.sopra.gui.ActionHandler;
import info.kriese.sopra.io.IOUtils;
import info.kriese.sopra.lop.LOP;
import info.kriese.sopra.lop.LOPEditor;
import info.kriese.sopra.lop.impl.LOPFactory;
import info.kriese.sopra.math.LOPSolver;

import javax.swing.JFrame;
import javax.swing.UIManager;

/**
 * 
 * 
 * @author Peer Sterner
 * @version 0.2
 * @since 07.11.2007
 * 
 */
public class TestDualPanel {

    public static void main(String[] args) {
	try { // use the local look and feel
	    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	} catch (Exception e) {
	}

	LOP lop = LOPFactory.newLinearOptimizingProblem();
	LOPEditor editor = LOPFactory.newLOPEditor(lop);
	LOPSolver solver = new LOPSolver();
	solver.setEditor(editor);
	ActionHandler.INSTANCE.setLOP(lop);
	SoPraLOP.EDITOR = editor;

	JFrame frame = new JFrame(
		"SoPra LOP - Visualisierung des Dualen Problems");
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	// schaue dort nach :-) , habe das panel extrahiert
	DualLOPPanel pn = new DualLOPPanel();

	pn.setLOP(lop);

	frame.add(pn);
	frame.pack();
	frame.setSize(600, 600);
	frame.setLocation(400, 200);

	editor.open(IOUtils.getURL("problems/testDual.lop"));

	frame.setVisible(true);

    }

}
