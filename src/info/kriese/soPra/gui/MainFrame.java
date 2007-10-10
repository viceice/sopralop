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
 * 03.10.2007 - Version 0.4.1
 * - Neue Menüpunkte hinzugefügt (DualProblem, Beispiele)
 * 11.09.2007 - Version 0.4
 * - Visualisierung hierher verlagert
 * - Neuer Menuepunkt: Lösung zeigen
 * - Größe angepasst, Autoresize bei zu kleinem Fenster
 * - XHTMLPanel in JScollPane eingebettet
 * - Weitere kleinere Änderungen
 * 23.08.2007 - Version 0.3
 * - LOP in neue Klasse mit Listener ausgelagert
 * - Weitere Umstrukturierungen
 * 30.07.2007 - Version 0.2.1
 * - Uebergabe der Eingabewerte komplettiert
 * - Beispiele koennen geladen werden
 * 29.07.2007 - Version 0.2
 * - komplett umgeschrieben
 *    + neues Menue
 *    + Speichern des Problems
 *    + Bearbeiten des Problems
 *    + Mehrspachigkeit vorbereitet
 * - Standardproblem wird bei Start geladen
 * 12.05.2007 - Version 0.1
 * - Datei hinzugefuegt
 */
package info.kriese.soPra.gui;

import info.kriese.soPra.engine3D.Engine3D;
import info.kriese.soPra.gui.html.HTMLGenerator;
import info.kriese.soPra.io.IOUtils;
import info.kriese.soPra.io.Settings;
import info.kriese.soPra.io.impl.SettingsFactory;
import info.kriese.soPra.math.LOPSolver;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.net.MalformedURLException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileFilter;

import org.xhtmlrenderer.simple.XHTMLPanel;

/**
 * @author Michael Kriese
 * @version 0.4.1
 * @since 12.05.2007
 * 
 */
public final class MainFrame extends JFrame {

    private static int HEIGHT = 600;

    /**	*/
    private static final long serialVersionUID = -2209082679810518777L;

    private static int WIDTH = 600;

    private final AboutDialog about;

    private final InputFrame data;

    private JMenu edit;

    private final JFileChooser fc;

    private String file = null;

    private final LOPSolver lop;

    private JMenuItem primale, duale;

    private final Settings PROPS = SettingsFactory.getInstance();

    private final Visual3DFrame visual;

    public MainFrame() {
	setTitle(this.PROPS.getName() + " - Version " + this.PROPS.getVersion());
	setSize(600, 500);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setLayout(new BorderLayout());

	addComponentListener(new ComponentAdapter() {

	    @Override
	    public void componentResized(ComponentEvent e) {
		MainFrame f = MainFrame.this;
		if (f.getWidth() < MainFrame.WIDTH)
		    f.setSize(MainFrame.WIDTH, f.getHeight());

		if (f.getHeight() < MainFrame.HEIGHT)
		    f.setSize(f.getWidth(), MainFrame.HEIGHT);
	    }

	});

	generateMainMenu();

	this.lop = new LOPSolver();

	this.data = new InputFrame(this, this.lop.getProblem());

	this.about = AboutDialog.getInstance(this);

	this.visual = new Visual3DFrame(this);

	XHTMLPanel info = new XHTMLPanel();
	// info.setDocument(lop.getDocument());

	JScrollPane scroll = new JScrollPane(info);
	add(scroll, BorderLayout.CENTER);

	// Referenzen auf diese Klassen werden nicht benoetigt
	new HTMLGenerator(this.lop.getProblem(), info);
	new Engine3D(this.visual, this.lop.getProblem());

	this.fc = new JFileChooser();
	this.fc.addChoosableFileFilter(new FileFilter() {
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
	this.fc.setMultiSelectionEnabled(false);
    }

    private void actionPerformed(ActionEvent e) {
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
	    this.data.setLocationRelativeTo(this);
	    this.data.setVisible(true);
	    this.lop.solve();
	} else if (cmd.equals("Menu.Edit.Show")) {
	    this.visual.setLocationRelativeTo(this);
	    this.visual.setVisible(true);
	} else if (cmd.equals("Menu.Edit.ShowSolution")) {
	    if (this.lop.getProblem().isSolved())
		this.lop.getProblem().showSolution();
	    else
		JOptionPane.showMessageDialog(this, "Sie muessen erst"
			+ " ein Problem oeffnen oder eingeben");
	} else if (cmd.equals("Menu.Edit.ShowDualProblem")) {
	    if (this.lop.getProblem().isSolved()) {
		this.lop.getProblem().showDualProblem();
		this.edit.remove(this.duale);
		this.edit.add(this.primale);
	    } else
		JOptionPane.showMessageDialog(this, "Sie muessen erst"
			+ " ein Problem oeffnen oder eingeben");
	} else if (cmd.equals("Menu.Edit.ShowPrimalProblem")) {
	    this.lop.getProblem().showPrimalProblem();
	    this.edit.remove(this.primale);
	    this.edit.add(this.duale);
	} else if (cmd.equals("Menu.Help.About")) {
	    this.about.setLocationRelativeTo(this);
	    this.about.setVisible(true);
	} else if (cmd.equals("Menu.File.Samples.S01")) {
	    this.file = null;
	    this.lop.open(IOUtils.getURL("problems/s1.lop"));
	} else if (cmd.equals("Menu.File.Samples.S02")) {
	    this.file = null;
	    this.lop.open(IOUtils.getURL("problems/s2.lop"));
	} else if (cmd.equals("Menu.File.Samples.S03")) {
	    this.file = null;
	    this.lop.open(IOUtils.getURL("problems/s3.lop"));
	} else if (cmd.equals("Menu.File.Samples.S04")) {
	    this.file = null;
	    this.lop.open(IOUtils.getURL("problems/s4.lop"));
	}
    }

    /**
     * Ladefunktion fuer Daten im xml-Format.
     * 
     */
    private void fileOpenClass() {
	int returnVal = this.fc.showOpenDialog(this);
	if (returnVal == JFileChooser.APPROVE_OPTION) {
	    File file = this.fc.getSelectedFile();
	    this.file = file.getAbsolutePath();
	    try {
		this.lop.open(file.toURI().toURL());
	    } catch (MalformedURLException e) {
	    }
	} else
	    System.out.println("Fehler beim Laden!");
    }

    /**
     * Speicherfunktion fuer Daten im xml-Format
     * 
     */
    private void fileSaveClass(boolean saveAs) {
	if (saveAs || this.file == null) {
	    int returnVal = this.fc.showSaveDialog(this);

	    if (returnVal == JFileChooser.APPROVE_OPTION) {
		File tmp = this.fc.getSelectedFile();
		this.file = tmp.getAbsolutePath();
		if (!this.file.toLowerCase().endsWith(".lop"))
		    this.file += ".lop";
	    }
	}
	if (this.file != null)
	    this.lop.save(this.file);
    }

    private void generateMainMenu() {
	ActionListener ac = new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		MainFrame.this.actionPerformed(e);
	    }
	};
	JMenuBar menubar;
	JMenu menu, submenu;

	menubar = new JMenuBar();
	setJMenuBar(menubar);

	menu = MenuMaker.getMenu("Menu.File");
	menubar.add(menu);
	menu.add(MenuMaker.getMenuItem("Menu.File.Open", ac));
	menu.add(MenuMaker.getMenuItem("Menu.File.Save", ac));
	menu.add(MenuMaker.getMenuItem("Menu.File.SaveAs", ac));
	menu.addSeparator();
	submenu = MenuMaker.getMenu("Menu.File.Samples");
	submenu.add(MenuMaker.getMenuItem("Menu.File.Samples.S01", ac));
	submenu.add(MenuMaker.getMenuItem("Menu.File.Samples.S02", ac));
	submenu.add(MenuMaker.getMenuItem("Menu.File.Samples.S03", ac));
	submenu.add(MenuMaker.getMenuItem("Menu.File.Samples.S04", ac));
	menu.add(submenu);
	menu.addSeparator();
	menu.add(MenuMaker.getMenuItem("Menu.File.Exit", ac));

	menu = MenuMaker.getMenu("Menu.Edit");
	menubar.add(menu);
	menu.add(MenuMaker.getMenuItem("Menu.Edit.Data", ac));
	menu.addSeparator();
	menu.add(MenuMaker.getMenuItem("Menu.Edit.Show", ac));
	menu.add(MenuMaker.getMenuItem("Menu.Edit.ShowSolution", ac));
	this.primale = MenuMaker.getMenuItem("Menu.Edit.ShowPrimalProblem", ac);
	this.duale = MenuMaker.getMenuItem("Menu.Edit.ShowDualProblem", ac);

	menu.add(this.duale);
	this.edit = menu;

	menu = MenuMaker.getMenu("Menu.Help");
	menu.add(MenuMaker.getMenuItem("Menu.Help.About", ac));
	menubar.add(menu);
    }

}
