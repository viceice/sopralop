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
 * 26.01.2008 - Version 0.2.1
 * - An neue SettingsFactory angepasst.
 * 25.01.2008 - Version 0.2
 * - TestCase komplett überarbeitet
 * 04.11.2007 - Version 0.1
 *  - Datei hinzugefuegt
 */
package info.kriese.soPra.test;

import info.kriese.soPra.SoPraLOP;
import info.kriese.soPra.gui.ActionHandler;
import info.kriese.soPra.gui.InputPanel;
import info.kriese.soPra.gui.lang.Lang;
import info.kriese.soPra.io.IOUtils;
import info.kriese.soPra.io.impl.SettingsFactory;
import info.kriese.soPra.lop.LOP;
import info.kriese.soPra.lop.LOPEditor;
import info.kriese.soPra.lop.impl.LOPFactory;
import info.kriese.soPra.math.LOPSolver;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * 
 * @author Michael Kriese
 * @version 0.2.1
 * @since 04.11.2007
 * 
 */
public class TestInputPanel {

    private static boolean DUAL = true;
    private static LOP lop;
    private static final String SAMPLE = "S01";

    public static void main(String[] args) {
	// Parse commandline arguments
	SettingsFactory.parseArgs(args);
	SettingsFactory.setDebug(true);

	SettingsFactory.initJava();

	SettingsFactory.showTitle("InputTest");

	lop = LOPFactory.newLinearOptimizingProblem();
	LOPEditor editor = LOPFactory.newLOPEditor(lop);

	LOPSolver solver = new LOPSolver();
	solver.setEditor(editor);

	ActionHandler.INSTANCE.setLOP(lop);

	SoPraLOP.EDITOR = editor;

	InputPanel ip = new InputPanel();
	ip.setEditor(editor);

	JButton pdbtn = new JButton("zeige Primales / Duales Problem");
	pdbtn.setFocusPainted(false);
	pdbtn.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		if (DUAL) {
		    lop.showDualProblem();
		    DUAL = false;
		} else {
		    lop.showPrimalProblem();
		    DUAL = true;
		}
	    }
	});

	editor.open(IOUtils.getURL("problems/"
		+ Lang.getString("Menu.File.Samples." + SAMPLE + ".File")
		+ ".lop"));

	JFrame frame = new JFrame("SoPraLOP InputTest - Version "
		+ SettingsFactory.getInstance().getVersion());
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.add(ip, BorderLayout.CENTER);
	frame.add(pdbtn, BorderLayout.PAGE_END);
	frame.setSize(400, 400);
	frame.setVisible(true);
    }
}
