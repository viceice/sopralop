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
 * 31.10.2007 - Version 0.4.1
 * - 3D-Fenster entfernt, wird nicht mehr benötigt
 * 24.10.2007 - Version 0.4
 *  - ActionListener Verhalten geändert
 *  - Startnachrichten multisprachfähig gemacht
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

import info.kriese.soPra.engine3D.Engine3D;
import info.kriese.soPra.gui.AboutDialog;
import info.kriese.soPra.gui.ActionHandler;
import info.kriese.soPra.gui.InputPanel;
import info.kriese.soPra.gui.MainFrame;
import info.kriese.soPra.gui.SplashDialog;
import info.kriese.soPra.gui.html.HTMLGenerator;
import info.kriese.soPra.gui.lang.Lang;
import info.kriese.soPra.math.LOPSolver;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JPopupMenu;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;

/**
 * @author Michael Kriese
 * @version 0.4.1
 * @since 12.05.2007
 * 
 */
public final class SoPraLOP {

    public static AboutDialog ABOUT;

    public static Engine3D ENGINE;

    public static JFileChooser FC;

    public static HTMLGenerator HTML;
    public static InputPanel INPUT;
    public static MainFrame MAIN;

    public static LOPSolver SOLVER;

    /**
     * @param args
     */
    public static void main(String[] args) {

	try { // use the local look and feel
	    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	} catch (Exception e) {
	    e.printStackTrace(System.err);
	}

	// We need heavyweight elements, so we can see them infront of our
	// canvas3d
	JPopupMenu.setDefaultLightWeightPopupEnabled(false);
	ToolTipManager.sharedInstance().setLightWeightPopupEnabled(false);

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

	splash.setMessage(Lang.getString("Boot.Solver"));
	SOLVER = new LOPSolver();

	ActionHandler.INSTANCE.setLOP(SOLVER.getProblem());

	splash.setMessage(Lang.getString("Boot.MainFrame"));
	MAIN = new MainFrame(SOLVER.getProblem());

	splash.setMessage(Lang.getString("Boot.InputPanel"));
	INPUT = new InputPanel(SOLVER.getProblem());

	splash.setMessage(Lang.getString("Boot.About"));
	ABOUT = AboutDialog.getInstance(MAIN);

	splash.setMessage(Lang.getString("Boot.FileChooser"));
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
		return Lang.getString("Strings.LOP") + " (*.lop)";
	    }
	});
	FC.setMultiSelectionEnabled(false);

	// Referenzen auf diese Objekte werden nicht benötigt
	splash.setMessage(Lang.getString("Boot.HTML"));
	HTML = new HTMLGenerator(SOLVER.getProblem());
	MAIN.setContent(HTML.getPanel());
	splash.setMessage(Lang.getString("Boot.3DEngine"));
	ENGINE = new Engine3D(MAIN, SOLVER.getProblem());

	splash.setMessage(Lang.getString("Boot.SolveLOP"));
	SOLVER.getProblem().problemChanged();
	SOLVER.solve();

	splash.setMessage(Lang.getString("Boot.ShowMain"));
	MAIN.setVisible(true);
	splash.setMessage("");
	splash.setVisible(false);
    }

}
