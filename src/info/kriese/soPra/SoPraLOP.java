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
 * 26.01.2008 - Version 0.5.8.1
 * - Aufruf von LOP.problemChanged entfernt, da überflüssig.
 * 17.01.2008 - Version 0.5.8
 * - Panel für Duales Problem entfernt
 * 28.12.2007 - Version 0.5.7
 * - Komandozeilenparameterhandling in SettingsFactory ausgelagert
 * 27.12.2007 - Version 0.5.6
 * - Hilfe-Fenster hinzugefügt
 * - Übergabe für Funktionsmenü hinzugefügt
 * 19.12.2007 - Version 0.5.5
 * - StartParameter werden verarbeitet
 * 04.12.2007 - Version 0.5.4
 * - An neuen HelpProvider angepasst
 * 03.12.2007 - Version 0.5.3
 * - An ErrorHandler angepasst
 * 26.11.2007 - Version 0.5.2
 * - Panel zur Visualisierung des Dualen Problems hinzugefügt
 * 04.11.2007 - Version 0.5.1
 * - Unnötige Ausgabe entfernt
 * 31.10.2007 - Version 0.5
 * - An LOPEditor angepasst
 * - Boot Meldungen angepasst (Reihenfolge)
 * 24.10.2007 - Version 0.4
 *  - ActionListener Verhalten geändert
 *  - Startnachrichten multisprachfähig gemacht
 * 19.10.2007 - Version 0.3
 * - Funktionalität aus GUI hirhin ausgelagert
 * 14.09.2007 - Version 0.2.1
 * - Property für die RenderEngine hizugefügt (Unter Vista schlechte
 *    Performance mit OpenGL)
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
import info.kriese.soPra.gui.HelpDialog;
import info.kriese.soPra.gui.InputPanel;
import info.kriese.soPra.gui.MainFrame;
import info.kriese.soPra.gui.MessageHandler;
import info.kriese.soPra.gui.SplashDialog;
import info.kriese.soPra.gui.Visual3DFrame;
import info.kriese.soPra.gui.lang.Lang;
import info.kriese.soPra.io.impl.SettingsFactory;
import info.kriese.soPra.lop.LOP;
import info.kriese.soPra.lop.LOPEditor;
import info.kriese.soPra.lop.impl.LOPFactory;
import info.kriese.soPra.math.LOPSolver;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JPopupMenu;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;

/**
 * @author Michael Kriese
 * @version 0.5.8.1
 * @since 12.05.2007
 * 
 */
public final class SoPraLOP {

    public static AboutDialog ABOUT;

    public static LOPEditor EDITOR;

    public static Engine3D ENGINE;

    public static JFileChooser FC;

    public static InputPanel INPUT;

    public static MainFrame MAIN;

    public static LOPSolver SOLVER;

    public static Visual3DFrame VISUAL;

    /**
     * @param args
     */
    public static void main(String[] args) {

	// Parse commandline arguments
	SettingsFactory.parseArgs(args);

	try { // use the local look and feel
	    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	} catch (Exception e) {
	}

	// We need heavyweight elements, so we can see them infront of our
	// canvas3d
	JPopupMenu.setDefaultLightWeightPopupEnabled(false);
	ToolTipManager.sharedInstance().setLightWeightPopupEnabled(false);

	SplashDialog splash = SplashDialog.getInstance();

	splash.setVisible(true);

	System.setProperty("file.encoding", "UTF-8");

	System.out.println("SoPraLOP - Version "
		+ SettingsFactory.getInstance().getVersion());
	System.out.println("\t(c) 2007-2008  "
		+ SettingsFactory.getInstance().getAuthor());
	System.out.println();

	String os = System.getProperty("os.name");
	if (SettingsFactory.getInstance().isDebug())
	    System.out.println("OS: " + os);
	if (os != null && os.toLowerCase().contains("vista")) {
	    System.setProperty("j3d.rend", "d3d");
	    if (SettingsFactory.getInstance().isDebug())
		System.out.println("On Vista we have to use D3D Renderer! :-(");
	} else
	    System.setProperty("j3d.rend", "ogl");

	splash.setMessage(Lang.getString("Boot.LOP"));
	LOP lop = LOPFactory.newLinearOptimizingProblem();
	EDITOR = LOPFactory.newLOPEditor(lop);
	ActionHandler.INSTANCE.setLOP(lop);

	splash.setMessage(Lang.getString("Boot.MainFrame"));
	MAIN = new MainFrame();
	MAIN.setLOP(lop);
	MAIN.addComponentListener(new ComponentAdapter() {
	    @Override
	    public void componentHidden(ComponentEvent e) {
		ActionHandler.exit();
	    }
	});
	MessageHandler.setParent(MAIN);
	MessageHandler.setHelp(MAIN);

	splash.setMessage(Lang.getString("Boot.VisualFrame"));
	VISUAL = new Visual3DFrame(MAIN);
	VISUAL.addComponentListener(new ComponentAdapter() {
	    @Override
	    public void componentHidden(ComponentEvent e) {
		ENGINE.addConnection(MAIN);
	    }

	    @Override
	    public void componentShown(ComponentEvent e) {
		ENGINE.addConnection(VISUAL);
	    }
	});

	splash.setMessage(Lang.getString("Boot.About"));
	ABOUT = AboutDialog.getInstance(MAIN);

	splash.setMessage(Lang.getString("Boot.Help"));
	HelpDialog.setParent(MAIN);
	HelpDialog.getInstance().setHelp("index.htm");

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

	splash.setMessage(Lang.getString("Boot.InputPanel"));
	INPUT = new InputPanel();
	INPUT.setEditor(EDITOR);
	MAIN.setContent(SoPraLOP.INPUT);
	MAIN.setFunctions(INPUT.getFunctions());

	splash.setMessage(Lang.getString("Boot.3DEngine"));
	ENGINE = new Engine3D();
	ENGINE.addConnection(MAIN);
	ENGINE.setLOP(lop);

	splash.setMessage(Lang.getString("Boot.Solver"));
	SOLVER = new LOPSolver();
	SOLVER.setEditor(EDITOR);
	EDITOR.solve();

	splash.setMessage(Lang.getString("Boot.ShowMain"));
	MAIN.setVisible(true);
	splash.setMessage("");
	splash.setVisible(false);
    }

}
