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
 * 19.10.2007 - Version 0.5
 * - Funktionalität aus GUI ausgelagert
 * - Menu Icons hinzugefügt
 * 16.10.2007 - Version 0.4.3
 * - Für neue Beispiel LOP's erweitert, werden komplett aus Sprach-Datei geladen
 * 12.10.2007 - Version 0.4.2
 * - LOP wird schon beim Start gelöst
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

import info.kriese.soPra.SoPraLOP;
import info.kriese.soPra.gui.lang.Lang;
import info.kriese.soPra.io.Settings;
import info.kriese.soPra.io.impl.SettingsFactory;
import info.kriese.soPra.lop.LOP;
import info.kriese.soPra.lop.LOPAdapter;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;

import org.xhtmlrenderer.simple.XHTMLPanel;

/**
 * @author Michael Kriese
 * @version 0.5
 * @since 12.05.2007
 * 
 */
public final class MainFrame extends JFrame {

    private static int HEIGHT = 600;

    /**	*/
    private static final long serialVersionUID = -2209082679810518777L;

    private static int WIDTH = 600;

    public XHTMLPanel info;

    private JMenu edit;

    private JMenuItem primale, duale;

    private final Settings PROPS = SettingsFactory.getInstance();

    public MainFrame(LOP lop) {
	setTitle(this.PROPS.getName() + " - Version " + this.PROPS.getVersion());
	setSize(600, 500);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setLayout(new BorderLayout());
	setLocationRelativeTo(null);
	ImageIcon ico = MenuMaker.getImage("MainFrame");
	if (ico != null)
	    setIconImage(ico.getImage());

	lop.addProblemListener(new LOPAdapter() {
	    @Override
	    public void showDualProblem(LOP lop) {
		MainFrame.this.edit.remove(MainFrame.this.duale);
		MainFrame.this.edit.add(MainFrame.this.primale);
	    }

	    @Override
	    public void showPrimalProblem(LOP lop) {
		MainFrame.this.edit.remove(MainFrame.this.primale);
		MainFrame.this.edit.add(MainFrame.this.duale);
	    }
	});

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

	this.info = new XHTMLPanel();

	JScrollPane scroll = new JScrollPane(this.info);
	add(scroll, BorderLayout.CENTER);
    }

    private void generateMainMenu() {
	ActionListener ac = new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		SoPraLOP.actionPerformed(e);
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

	for (int i = 1; i <= Lang.getInt("Menu.File.Samples.Count"); i++)
	    submenu.add(MenuMaker.getMenuItem("Menu.File.Samples.S"
		    + (i < 10 ? "0" + i : i), ac));

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
