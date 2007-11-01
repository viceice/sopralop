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
 * 01.11.2007 - Version 0.7
 * - 3D-Visualisierung integriert
 * - An LOPEditor angepasst
 * - Im Titel wird der Name der geöffneten Datei angezeigt
 * 25.10.2007 - Version 0.6.1
 * - StatusBar hinzugefügt
 * 24.10.2007 - Version 0.6
 * - Menü "Bearbeiten" in "Ansicht" geändert
 * - Das zentrale Panel ist jetzt austauschbar
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

import info.kriese.soPra.gui.lang.Lang;
import info.kriese.soPra.io.Settings;
import info.kriese.soPra.io.impl.SettingsFactory;
import info.kriese.soPra.lop.LOP;
import info.kriese.soPra.lop.LOPAdapter;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.media.j3d.Canvas3D;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

/**
 * @author Michael Kriese
 * @version 0.7
 * @since 12.05.2007
 * 
 */
public final class MainFrame extends JFrame implements Virtual3DFrame {

    private static final int HEIGHT = 400;

    /**	*/
    private static final long serialVersionUID = -2209082679810518777L;

    private static final int WIDTH = 800;

    private static Border createStatusBarBorder() {
	Border inner, outer;
	outer = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
	inner = BorderFactory.createEmptyBorder(2, 5, 2, 5);
	return BorderFactory.createCompoundBorder(outer, inner);
    }

    private final JSplitPane body;

    private JMenu edit;

    private JMenuItem primale, duale;

    private final Settings PROPS = SettingsFactory.getInstance();

    private final JLabel status;

    private String title = "";

    public MainFrame() {
	setTitle(this.PROPS.getName() + " - Version " + this.PROPS.getVersion());
	setSize(WIDTH, HEIGHT);
	setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	setLayout(new BorderLayout());
	setLocationRelativeTo(null);
	ImageIcon ico = MenuMaker.getImage("MainFrame");
	if (ico != null)
	    setIconImage(ico.getImage());

	addComponentListener(new ComponentAdapter() {

	    @Override
	    public void componentResized(ComponentEvent e) {
		validate();
		repaint();
	    }
	});

	this.body = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
	this.body.setOneTouchExpandable(false);
	this.body.setDividerSize(10);
	this.body.setDividerLocation(WIDTH / 2);
	add(this.body, BorderLayout.CENTER);

	this.status = new JLabel();
	this.status.setPreferredSize(new Dimension(200, 20));
	this.status.setBorder(createStatusBarBorder());
	add(this.status, BorderLayout.PAGE_END);

	generateMainMenu();
	generateMainToolbar();
    }

    public void addCanvas(Canvas3D canvas) {
	canvas.setMinimumSize(new Dimension(0, 0));
	canvas.setPreferredSize(new Dimension(WIDTH / 2, 300));
	this.body.setRightComponent(canvas);
	this.body.setDividerLocation(WIDTH / 2);
	validate();
	repaint();
    }

    @Override
    public String getTitle() {
	return this.title;
    }

    public void setContent(JComponent content) {
	content.setMinimumSize(new Dimension(0, 0));
	content.setPreferredSize(new Dimension(WIDTH / 2, 300));
	this.body.setLeftComponent(content);
	this.body.setDividerLocation(WIDTH / 2);
	validate();
	repaint();
    }

    public void setLOP(LOP lop) {
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
    }

    public void setStatus(String msg) {
	this.status.setText(msg);
    }

    @Override
    public void setTitle(String title) {
	this.title = title;
	super.setTitle(title + " - " + this.PROPS.getName() + " - Version "
		+ this.PROPS.getVersion());
    }

    private void generateMainMenu() {
	JMenuBar menubar;
	JMenu menu, submenu;

	menubar = new JMenuBar();
	setJMenuBar(menubar);

	menu = MenuMaker.getMenu("Menu.File");
	menubar.add(menu);
	menu.add(MenuMaker.getMenuItem("Menu.File.Open"));
	menu.add(MenuMaker.getMenuItem("Menu.File.Save"));
	menu.add(MenuMaker.getMenuItem("Menu.File.SaveAs"));
	menu.addSeparator();
	submenu = MenuMaker.getMenu("Menu.File.Samples");

	for (int i = 1; i <= Lang.getInt("Menu.File.Samples.Count"); i++)
	    submenu.add(MenuMaker.getMenuItem("Menu.File.Samples.S"
		    + (i < 10 ? "0" + i : i)));

	menu.add(submenu);
	menu.addSeparator();
	menu.add(MenuMaker.getMenuItem("Menu.File.Exit"));

	menu = MenuMaker.getMenu("Menu.View");
	menubar.add(menu);
	menu.add(MenuMaker.getMenuItem("Menu.View.Reset"));
	menu.addSeparator();
	menu.add(MenuMaker.getMenuItem("Menu.View.Show"));
	menu.add(MenuMaker.getMenuItem("Menu.View.ShowSolution"));
	this.primale = MenuMaker.getMenuItem("Menu.View.ShowPrimalProblem");
	this.duale = MenuMaker.getMenuItem("Menu.View.ShowDualProblem");

	menu.add(this.duale);
	this.edit = menu;

	menu = MenuMaker.getMenu("Menu.Help");
	menu.add(MenuMaker.getMenuItem("Menu.Help.Help"));
	menu.addSeparator();
	menu.add(MenuMaker.getMenuItem("Menu.Help.About"));
	menubar.add(menu);
    }

    private void generateMainToolbar() {
	JToolBar tb = new JToolBar();
	tb.setFloatable(false);
	add(tb, BorderLayout.NORTH);

	tb.add(MenuMaker.getToolBarButton("Menu.File.Open"));
	tb.add(MenuMaker.getToolBarButton("Menu.File.Save"));
	tb.addSeparator();
	tb.add(MenuMaker.getToolBarButton("Menu.View.Show"));
	tb.add(MenuMaker.getToolBarButton("Menu.View.Reset"));
    }
}
