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
 * 24.10.2007 - Version 0.1
 *  - Datei hinzugefuegt
 */
package info.kriese.soPra.gui;

import info.kriese.soPra.SoPraLOP;
import info.kriese.soPra.gui.lang.Lang;
import info.kriese.soPra.io.IOUtils;
import info.kriese.soPra.lop.LOP;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.AbstractButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * Klasse zum handeln aller Actions in SoPraLOP
 * 
 * @author Michael Kriese
 * @version 0.1
 * @since 24.10.2007
 * 
 */
public final class ActionHandler {

    public static final ActionHandler INSTANCE = new ActionHandler();

    public String FILE = null;

    private final ActionListener ac;

    private LOP lop = null;

    private ActionHandler() {
	this.ac = new ActionListener() {

	    public void actionPerformed(ActionEvent e) {
		ActionHandler.this.actionPerformed(e);
	    }
	};
    }

    public void add(AbstractButton btn) {
	btn.addActionListener(this.ac);
    }

    public void setLOP(LOP lop) {
	this.lop = lop;
    }

    private void actionPerformed(ActionEvent e) {
	String cmd = e.getActionCommand();

	if (this.lop == null) {
	    System.err.println("ActionHandler: lop shoudn\'t be null!");
	    System.exit(-1);
	}

	if (cmd.equals("Menu.File.Exit"))
	    System.exit(0);
	else if (cmd.equals("Menu.File.Open"))
	    fileOpenClass();
	else if (cmd.equals("Menu.File.Save"))
	    fileSaveClass(false);
	else if (cmd.equals("Menu.File.SaveAs"))
	    fileSaveClass(true);
	else if (cmd.equals("Menu.View.Data")) {
	    SoPraLOP.MAIN.setContent(SoPraLOP.INPUT);
	    SoPraLOP.INPUT.update();
	} else if (cmd.equals("Menu.View.Result")) {
	    if (SoPraLOP.INPUT.isEdited()) {
		int res = JOptionPane.showConfirmDialog(SoPraLOP.MAIN, Lang
			.getString("Errors.IsEdited"));
		if (res == JOptionPane.YES_OPTION) {
		    this.lop.setVectors(SoPraLOP.INPUT.getVectors());
		    this.lop.setTarget(SoPraLOP.INPUT.getTarget());
		    this.lop.setMaximum(SoPraLOP.INPUT.isMax());
		    this.lop.setOperators(SoPraLOP.INPUT.getOperators());
		    this.lop.problemChanged();
		    SoPraLOP.SOLVER.solve();
		} else if (res == JOptionPane.CANCEL_OPTION)
		    return;
		SoPraLOP.INPUT.setEdited(false);
	    }
	    SoPraLOP.MAIN.setContent(SoPraLOP.HTML.getPanel());
	} else if (cmd.equals("Menu.View.Show")) {
	    SoPraLOP.VISUAL.setLocationRelativeTo(SoPraLOP.MAIN);
	    SoPraLOP.VISUAL.setVisible(true);
	} else if (cmd.equals("Menu.View.ShowSolution")) {
	    if (this.lop.isSolved())
		this.lop.showSolution();
	    else
		JOptionPane.showMessageDialog(SoPraLOP.MAIN, Lang
			.getString("Errors.OpenOrEdit"));
	} else if (cmd.equals("Menu.View.ShowDualProblem")) {
	    if (this.lop.isSolved())
		this.lop.showDualProblem();
	    else
		JOptionPane.showMessageDialog(SoPraLOP.MAIN, Lang
			.getString("Errors.OpenOrEdit"));
	} else if (cmd.equals("Menu.View.ShowPrimalProblem"))
	    this.lop.showPrimalProblem();
	else if (cmd.equals("Menu.Help.About")) {
	    SoPraLOP.ABOUT.setLocationRelativeTo(SoPraLOP.MAIN);
	    SoPraLOP.ABOUT.setVisible(true);
	} else if (cmd.startsWith("Menu.File.Samples")) {
	    this.FILE = null;
	    SoPraLOP.SOLVER.open(IOUtils.getURL("problems/"
		    + Lang.getString(cmd + ".File") + ".lop"));
	} else if (cmd.equals("Input.Menu.AddVar"))
	    SoPraLOP.INPUT.addColumn();
	else if (cmd.equals("Input.Menu.DelVar"))
	    SoPraLOP.INPUT.removeColumn();
	else if (cmd.equals("Input.Menu.Save")) {
	    this.lop.setVectors(SoPraLOP.INPUT.getVectors());
	    this.lop.setTarget(SoPraLOP.INPUT.getTarget());
	    this.lop.setMaximum(SoPraLOP.INPUT.isMax());
	    this.lop.setOperators(SoPraLOP.INPUT.getOperators());
	    this.lop.problemChanged();
	    SoPraLOP.SOLVER.solve();
	} else if (cmd.equals("Input.Menu.Clear"))
	    SoPraLOP.INPUT.clear();
	else if (cmd.equals("Input.Menu.Reset"))
	    SoPraLOP.INPUT.update();
    }

    /**
     * Ladefunktion fuer Daten im xml-Format.
     * 
     */
    private void fileOpenClass() {
	if (SoPraLOP.FC.showOpenDialog(SoPraLOP.MAIN) == JFileChooser.APPROVE_OPTION) {
	    this.FILE = SoPraLOP.FC.getSelectedFile().getAbsolutePath();
	    try {
		SoPraLOP.SOLVER.open(new URL(this.FILE));
	    } catch (MalformedURLException e) {
	    }
	}
    }

    /**
     * Speicherfunktion fuer Daten im xml-Format
     * 
     */
    private void fileSaveClass(boolean saveAs) {
	if (saveAs || this.FILE == null)
	    if (SoPraLOP.FC.showSaveDialog(SoPraLOP.MAIN) == JFileChooser.APPROVE_OPTION) {
		this.FILE = SoPraLOP.FC.getSelectedFile().getAbsolutePath();
		if (!this.FILE.toLowerCase().endsWith(".lop"))
		    this.FILE += ".lop";
	    }
	if (this.FILE != null)
	    SoPraLOP.SOLVER.save(this.FILE);
    }

}
