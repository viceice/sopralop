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
import info.kriese.soPra.gui.Visual3DFrame;
import info.kriese.soPra.gui.html.HTMLGenerator;
import info.kriese.soPra.gui.lang.Lang;
import info.kriese.soPra.math.LOPSolver;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 * @author Michael Kriese
 * @version 0.4
 * @since 12.05.2007
 * 
 */
public final class SoPraLOP {

    public static AboutDialog ABOUT;

    public static JFileChooser FC;

    public static HTMLGenerator HTML;

    public static InputPanel INPUT;
    public static MainFrame MAIN;
    public static LOPSolver SOLVER;

    public static Visual3DFrame VISUAL;

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

	splash.setMessage(Lang.getString("Boot.Solver"));
	SOLVER = new LOPSolver();

	ActionHandler.INSTANCE.setLOP(SOLVER.getProblem());

	splash.setMessage(Lang.getString("Boot.MainFrame"));
	MAIN = new MainFrame(SOLVER.getProblem());

	splash.setMessage(Lang.getString("Boot.VisualPanel"));
	VISUAL = new Visual3DFrame(MAIN);
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
	new Engine3D(VISUAL, SOLVER.getProblem());

	splash.setMessage(Lang.getString("Boot.SolveLOP"));
	SOLVER.solve();

	splash.setMessage(Lang.getString("Boot.ShowMain"));
	MAIN.setVisible(true);
	splash.setMessage("");
	splash.setVisible(false);
    }

}
