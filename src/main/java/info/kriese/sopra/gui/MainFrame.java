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
 * 21.05.2008 - Version 0.10.1
 * - Neuer Menüpunkt "change background color"
 * 20.05.2008 - Version 0.10
 * - neuer Menüpunkt "save as image" und 2 neue ToolbarButtons "save as image"
 *   & "schow pp / dp"
 * 15.05.2008 - Version 0.9.1
 * - BugFix: Bei Programmstart war der Titel doppelt
 * 12.02.2008 - Version 0.9
 * - setStatus und getTitle entfernt
 * - edit in view umbenant
 * 01.02.2008 - Version 0.8.4
 * - Schriftformatierung vom Info-Label in HTML-Code ausgelagert
 * 26.01.2008 - Version 0.8.3.1
 * - Schrift auf Info-Label jetzt fett gedruckt
 * 25.01.2008 - Version 0.8.3
 * - Fenstergröße geändert
 * - setDualPanel entfernt, da nicht mehr benötigt
 * 17.01.2008 - Version 0.8.2
 * - Panel für Duales Problem entfernt
 * - InfoLabel hinzugefügt, es zeigt an ob das primale oder duale Problem
 *    angezeigt wird
 * 10.01.2008 - Version 0.8.1
 * - Rollover-Effekt für die Toolbar
 * 27.12.2007 - Version 0.8
 * - setFunctions hizugefügt
 * 04.12.2007 - Version 0.7.3
 * - An neues Hilfesystem angepasst
 * 26.11.2007 - Version 0.7.2
 * - Panel zur Visualisierung des Dualen Problems hinzugefügt
 * 09.11.2007 - Version 0.7.1
 * - ShowSolution-Button entfernt, da überflüssig
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
package info.kriese.sopra.gui;

import info.kriese.sopra.gui.lang.Lang;
import info.kriese.sopra.io.Settings;
import info.kriese.sopra.io.impl.SettingsFactory;
import info.kriese.sopra.lop.LOP;
import info.kriese.sopra.lop.LOPAdapter;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;

import org.jogamp.java3d.Canvas3D;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
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
 * Erstellt das Hauptfenster des Programms
 * 
 * @author Michael Kriese
 * @version 0.10.1
 * @since 12.05.2007
 */
public final class MainFrame extends JFrame implements Virtual3DFrame,
	HelpProvider {

    /**
     * Höhe des Hauptfensters.
     */
    private static final int HEIGHT = 500;

    /**
     * Dient zur Serialisierung. ( Hat bei uns keine Verwendung)
     */
    private static final long serialVersionUID = -2209082679810518777L;

    /**
     * Höhe des Hauptfensters.
     */
    private static final int WIDTH = 800;

    /**
     * Erstellt den Rahmen für die Statusleiste.
     * 
     * @return ein Rahmen.
     */
    private static Border createStatusBarBorder() {
	Border inner, outer;
	outer = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
	inner = BorderFactory.createEmptyBorder(2, 5, 2, 5);
	return BorderFactory.createCompoundBorder(outer, inner);
    }

    /**
     * Das SplitPane ermöglicht es den Platz der Eingabe und des 3D-Panels frei
     * zu definieren.
     */
    private final JSplitPane body;

    private JButton btn_primal_duale;

    /**
     * Menüpunkte zum anzeigen des primalen bzw. dualen Problems.
     */
    private JMenuItem primale, duale;

    /**
     * Label, auf dem angezeigt wird, ob das primale oder duale Problem aktiv
     * ist.
     */
    private final JLabel problem;

    /**
     * Referenz auf die Programmeinstellungen.
     */
    private final Settings PROPS = SettingsFactory.getInstance();

    /**
     * Referenz auf die Statusbar
     */
    private final JLabel status;

    /**
     * Referenzen auf Menüpunkte des Hauptmenüs ( Ansicht & Bearbeiten )
     */
    private JMenu view, functions;

    /**
     * Konstuktor, welcher alle Variablen und Objekte initialisiert.
     */
    public MainFrame() {
	setTitle(null);
	setSize(WIDTH, HEIGHT);
	setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	setLayout(new BorderLayout());
	setLocationRelativeTo(null);
	ImageIcon ico = MenuMaker.getImage("MainFrame");
	if (ico != null)
	    setIconImage(ico.getImage());

	// reagiere auf Größenveränderungen
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

	this.problem = new JLabel(Lang.getString("Strings.PrimalProblem"));
	this.problem.setBorder(BorderFactory.createEmptyBorder(5, 50, 5, 50));

	generateMainMenu();
	generateMainToolbar();
    }

    /**
     * Hiermit wird die 3D-Zeichenfläche dem Hauptfenster hinzugefügt.
     * 
     * @param canvas -
     *                3D-Zeichenfläche
     */
    public void addCanvas(Canvas3D canvas) {
	canvas.setMinimumSize(new Dimension(0, 0));
	canvas.setPreferredSize(new Dimension(WIDTH / 2, 300));
	this.body.setRightComponent(canvas);
	this.body.setDividerLocation(WIDTH / 2);
	validate();
	repaint();
    }

    /**
     * Hiermit wird das EingabePanel dem Hauptfenster hinzugefügt.
     * 
     * @param content -
     *                Komponente zur Eingabe / Änderung des LOP's
     */
    public void setContent(JComponent content) {
	content.setMinimumSize(new Dimension(0, 0));
	content.setPreferredSize(new Dimension(WIDTH / 2, 300));
	this.body.setLeftComponent(content);
	this.body.setDividerLocation(WIDTH / 2);
    }

    /**
     * Hiermit kann man dem Menüpunkt "Bearbeiten" Menupunkte hinzufügen.
     * 
     * @param items -
     *                Liste von Menüpunkten, welche hinzugefügt werden sollen.
     */
    public void setFunctions(List<JMenuItem> items) {
	this.functions.removeAll();

	for (JMenuItem item : items)
	    if (item.getText().equals(MenuMaker.SEPARATOR))
		this.functions.addSeparator();
	    else
		this.functions.add(item);
    }

    /**
     * Gibt dem Hauptfenster eine Referenz auf das LOP, damit es auf Aktionen
     * reagieren kann.
     * 
     * @param lop -
     *                LOP, bei welchem sich das Hauptfenster registrieren soll.
     */
    public void setLOP(LOP lop) {
	lop.addProblemListener(new LOPAdapter() {
	    @Override
	    public void showDualProblem(LOP lop) {
		MainFrame.this.view.remove(MainFrame.this.duale);
		MainFrame.this.view.add(MainFrame.this.primale);
		MainFrame.this.problem.setText(Lang
			.getString("Strings.DualProblem"));
		MainFrame.this.btn_primal_duale
			.setActionCommand("Menu.View.ShowPrimalProblem");
		MainFrame.this.btn_primal_duale.setToolTipText(MenuMaker
			.getMenuTitle("Menu.View.ShowPrimalProblem"));
		MainFrame.this.btn_primal_duale.setIcon(MenuMaker
			.getImage("Menu.View.ShowPrimalProblem"));
		validate();
		repaint();
	    }

	    @Override
	    public void showPrimalProblem(LOP lop) {
		MainFrame.this.view.remove(MainFrame.this.primale);
		MainFrame.this.view.add(MainFrame.this.duale);
		MainFrame.this.problem.setText(Lang
			.getString("Strings.PrimalProblem"));
		MainFrame.this.btn_primal_duale
			.setActionCommand("Menu.View.ShowDualProblem");
		MainFrame.this.btn_primal_duale.setToolTipText(MenuMaker
			.getMenuTitle("Menu.View.ShowDualProblem"));
		MainFrame.this.btn_primal_duale.setIcon(MenuMaker
			.getImage("Menu.View.ShowDualProblem"));
		validate();
		repaint();
	    }
	});
    }

    /**
     * Hiermit wird der Titel des Hauptfensters geändert.
     */
    @Override
    public void setTitle(String title) {
	if (title == null || title.length() == 0)
	    super.setTitle(this.PROPS.getName() + " - Version "
		    + this.PROPS.getVersion());
	else
	    super.setTitle(title + " - " + this.PROPS.getName() + " - Version "
		    + this.PROPS.getVersion());
    }

    /**
     * Zeigt die Schnellhilfe in der Statuszeile an.
     */
    public void showHelp(String msg) {
	this.status.setText(msg != null ? msg : "");
    }

    /**
     * Hiermit wird das Hauptmenü generiert.
     */
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
	// TODO: Übersetzung
	menu.add(MenuMaker.getMenuItem("Menu.File.SaveImage"));
	menu.addSeparator();
	submenu = MenuMaker.getMenu("Menu.File.Samples");

	for (int i = 1; i <= Lang.getInt("Menu.File.Samples.Count"); i++)
	    submenu.add(MenuMaker.getMenuItem("Menu.File.Samples.S"
		    + (i < 10 ? "0" + i : i)));

	menu.add(submenu);
	menu.addSeparator();
	menu.add(MenuMaker.getMenuItem("Menu.File.Exit"));

	this.functions = MenuMaker.getMenu("Menu.Functions");
	menubar.add(this.functions);

	menu = MenuMaker.getMenu("Menu.View");
	menubar.add(menu);
	menu.add(MenuMaker.getMenuItem("Menu.View.Reset"));
	// TODO: Übersetzung
	menu.add(MenuMaker.getMenuItem("Menu.View.Color"));
	menu.addSeparator();
	menu.add(MenuMaker.getMenuItem("Menu.View.Show"));
	this.primale = MenuMaker.getMenuItem("Menu.View.ShowPrimalProblem");
	this.duale = MenuMaker.getMenuItem("Menu.View.ShowDualProblem");

	menu.add(this.duale);
	this.view = menu;

	menu = MenuMaker.getMenu("Menu.Help");
	menu.add(MenuMaker.getMenuItem("Menu.Help.Help"));
	menu.addSeparator();
	menu.add(MenuMaker.getMenuItem("Menu.Help.About"));
	menubar.add(menu);
    }

    /**
     * Hiermit wird die HauptToolbar generiert.
     */
    private void generateMainToolbar() {
	JToolBar tb = new JToolBar();
	tb.setFloatable(false);
	tb.setRollover(true);
	add(tb, BorderLayout.NORTH);

	tb.add(MenuMaker.getToolBarButton("Menu.File.Open"));
	tb.add(MenuMaker.getToolBarButton("Menu.File.Save"));
	tb.add(MenuMaker.getToolBarButton("Menu.File.SaveImage"));
	tb.addSeparator();
	tb.add(MenuMaker.getToolBarButton("Menu.View.Color"));
	tb.add(MenuMaker.getToolBarButton("Menu.View.Show"));
	tb.add(MenuMaker.getToolBarButton("Menu.View.Reset"));
	tb.addSeparator();
	this.btn_primal_duale = MenuMaker
		.getToolBarButton("Menu.View.ShowDualProblem");
	tb.add(this.btn_primal_duale);
	tb.addSeparator();
	tb.add(this.problem);
    }
}
