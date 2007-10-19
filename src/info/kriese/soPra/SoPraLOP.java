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
 * 19.10.2007 - Version 0.3
 * - Funktionalität aus GUI hirhin ausgelagert
 * 14.09.2007 - Version 0.2.1
 * - Property für die RenderEngine hizugefügt (Unter Vista schlechte Performance mit OpenGL)
 * 11.09.2007- Version 0.2
 * - Default DateiCodierung auf UTF-8 geändert
 * 29.07.2007 - Version 0.1.1
 * - unnoetige Variablen geloescht
 * 12.05.2007 - Version 0.1
 * - Datei hinzugefuegt
 */
package info.kriese.soPra;

import java.awt.event.ActionEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import info.kriese.soPra.engine3D.Engine3D;
import info.kriese.soPra.gui.AboutDialog;
import info.kriese.soPra.gui.InputDialog;
import info.kriese.soPra.gui.MainFrame;
import info.kriese.soPra.gui.SplashDialog;
import info.kriese.soPra.gui.Visual3DFrame;
import info.kriese.soPra.gui.html.HTMLGenerator;
import info.kriese.soPra.gui.lang.Lang;
import info.kriese.soPra.io.IOUtils;
import info.kriese.soPra.math.LOPSolver;

/**
 * @author Michael Kriese
 * @version 0.3
 * @since 12.05.2007
 * 
 */
public final class SoPraLOP {

    private static AboutDialog ABOUT;

    private static JFileChooser FC;

    private static String FILE = null;

    private static InputDialog INPUT;

    private static MainFrame MAIN;
    private static LOPSolver SOLVER;

    private static Visual3DFrame VISUAL;

    public static void actionPerformed(ActionEvent e) {
	String cmd = e.getActionCommand();

	if (cmd.equals("Menu.File.Exit"))
	    System.exit(0);
	else if (cmd.equals("Menu.File.Open"))
	    fileOpenClass();
	else if (cmd.equals("Menu.File.Save"))
	    fileSaveClass(false);
	else if (cmd.equals("Menu.File.SaveAs"))
	    fileSaveClass(true);
	else if (cmd.equals("Menu.Edit.Data")) {
	    INPUT.setLocationRelativeTo(MAIN);
	    INPUT.setVisible(true);
	    SOLVER.solve();
	} else if (cmd.equals("Menu.Edit.Show")) {
	    VISUAL.setLocationRelativeTo(MAIN);
	    VISUAL.setVisible(true);
	} else if (cmd.equals("Menu.Edit.ShowSolution")) {
	    if (SOLVER.getProblem().isSolved())
		SOLVER.getProblem().showSolution();
	    else
		JOptionPane.showMessageDialog(MAIN, "Sie muessen erst"
			+ " ein Problem oeffnen oder eingeben");
	} else if (cmd.equals("Menu.Edit.ShowDualProblem")) {
	    if (SOLVER.getProblem().isSolved())
		SOLVER.getProblem().showDualProblem();
	    else
		JOptionPane.showMessageDialog(MAIN, "Sie muessen erst"
			+ " ein Problem oeffnen oder eingeben");
	} else if (cmd.equals("Menu.Edit.ShowPrimalProblem"))
	    SOLVER.getProblem().showPrimalProblem();
	else if (cmd.equals("Menu.Help.About")) {
	    ABOUT.setLocationRelativeTo(MAIN);
	    ABOUT.setVisible(true);
	} else if (cmd.startsWith("Menu.File.Samples")) {
	    FILE = null;
	    SOLVER.open(IOUtils.getURL("problems/"
		    + Lang.getString(cmd + ".File") + ".lop"));
	}
    }

    /**
     * @param args
     */
    public static void main(String[] args) {

	SplashDialog splash = SplashDialog.getInstance();

	splash.setVisible(true);

	System.setProperty("file.encoding", "UTF-8");

	String os = System.getProperty("os.name");
	System.out.println("OS: " + os);
	if (os != null && os.toLowerCase().contains("vista")) {
	    System.setProperty("j3d.rend", "d3d");
	    System.out.println("On Vista we have to use D3D Renderer! :-(");
	} else
	    System.setProperty("j3d.rend", "ogl");

	splash.setMessage("Creating solver ...");
	SOLVER = new LOPSolver();

	splash.setMessage("Creating main window ...");
	MAIN = new MainFrame(SOLVER.getProblem());

	splash.setMessage("Creating 3D window ...");
	VISUAL = new Visual3DFrame(MAIN);

	splash.setMessage("Creating about window ...");
	ABOUT = AboutDialog.getInstance(MAIN);
	splash.setMessage("Creating input window ...");
	INPUT = new InputDialog(MAIN, SOLVER.getProblem());

	// Referenzen auf diese Objekte werden nicht benötigt
	splash.setMessage("Creating HTML generator ...");
	new HTMLGenerator(SOLVER.getProblem(), MAIN.info);
	splash.setMessage("Creating 3D engine ...");
	new Engine3D(VISUAL, SOLVER.getProblem());

	splash.setMessage("Creating file chooser ...");
	FC = new JFileChooser();
	FC.addChoosableFileFilter(new FileFilter() {
	    @Override
	    public boolean accept(File f) {
		if (f.isDirectory())
		    return true;
		return f.getName().toLowerCase().endsWith(".lop");
	    }

	    @Override
	    public String getDescription() {
		return "Lineares Optimierungsproblem (*.lop)";
	    }
	});
	FC.setMultiSelectionEnabled(false);

	splash.setMessage("Solve LOP ...");
	SOLVER.solve();

	splash.setMessage("Showing main window ...");
	MAIN.setVisible(true);
	splash.setMessage("Closeing splash ...");
	splash.setVisible(false);
    }

    /**
     * Ladefunktion fuer Daten im xml-Format.
     * 
     */
    private static void fileOpenClass() {
	if (FC.showOpenDialog(MAIN) == JFileChooser.APPROVE_OPTION) {
	    FILE = FC.getSelectedFile().getAbsolutePath();
	    try {
		SOLVER.open(new URL(FILE));
	    } catch (MalformedURLException e) {
	    }
	}
    }

    /**
     * Speicherfunktion fuer Daten im xml-Format
     * 
     */
    private static void fileSaveClass(boolean saveAs) {
	if (saveAs || FILE == null)
	    if (FC.showSaveDialog(MAIN) == JFileChooser.APPROVE_OPTION) {
		FILE = FC.getSelectedFile().getAbsolutePath();
		if (!FILE.toLowerCase().endsWith(".lop"))
		    FILE += ".lop";
	    }
	if (FILE != null)
	    SOLVER.save(FILE);
    }
}
