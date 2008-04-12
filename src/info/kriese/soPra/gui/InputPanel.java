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
 * 01.02.2008 - Version 0.7.3
 * - Fälle für Lösungsbereich werden im dualen ausgeblendet
 * - Unendlich und Leer kann nicht mehr eingegeben werden
 * 29.01.2008 - Version 0.7.2.1
 * - An LOPMinMax Konstanten angepasst.
 * 25.01.2008 - Version 0.7.2
 * - Vorbereitung zur erweiterten Lösungsüberprüfung
 * 10.01.2008 - Version 0.7.1
 * - BugFix: Lösung-Überprüfen-Button wurde bei Dual-LOP-Ansicht nicht
 *    deaktiviert
 * - Lösung-Überprüfen-Button bleibt auch bei editiertem LOP aktiv, es gibt
 *    jetzt eine Fehlermeldung
 * - Rollover-Effekt für die Toolbar
 * - Erste Spalte etwas verbreitert
 * - Editor für Relationszeichen entfernt
 * 27.12.2007 - Version 0.7
 * - Funktionsmenü hinzugefügt
 * 04.12.2007 - Version 0.6.4
 * - An Lösungseditor angepasst, um unendlich und nicht exitent  als Lösung
 *    eingeben zu können.
 * 03.12.2007 - Version 0.6.3
 * - Toolbar wird bei Anzeige des Dualen Problems deaktiviert
 * - Reset-Button wird jetzt deaktiviert, wenn nichts zu resetten ist
 * 09.11.2007 - Version 0.6.2
 * - z-Spalte etwas breiter gemacht, dadurch sind keine Punkte mehr im
 *    MinMaxDropDownFeld
 * - Das duale Problem kann jetzt wieder angezeigt werden
 * 04.11.2007 - Version 0.6.1
 * - CellEditoren funktionieren jetzt auch wieder
 * 03.11.2007 - Version 0.6
 * - Model-Klasse ausgelagert
 * - Funktionalität aus dem ActionHandler hier her verlagert
 * - Eigenen CellRenderer eingeführt
 * 23.10.2007 - Version 0.5
 * - In InputPanel umbenannt und in JPannel geändert ( zur Integration ins
 *    Hauptfenster)
 * - An neuen ActionHandler angepasst
 * - BugFix: String wurde nicht korrekt aus Sprach-Datei geladen
 * 19.10.2007 - Version 0.4
 * - In InputDialog umbenannt
 * - Menü neu designed (Icons hinzugefügt, Multisprachfähigkeit)
 * - Neuer Menüpunkt "Werte zurücksetzen"
 * - Menüpunkt "Quit" in speichern geändert
 * 12.10.2007 - Version 0.3.3
 * - Startwerte geändert
 * 03.10.2007 - Version 0.3.2
 * - LOPListener gegen LOPAdapter getauscht
 * 11.09.2007 - Version 0.3.1
 *  - LOPListener erweitert
 * 28.08.2007 - Version 0.3
 *  - Umstrukturierung, Aenderungen werden jetzt ueber Listener weitergegeben
 *  - Diverse Methoden geloescht
 * 30.07.2007 - Version 0.2.1
 * - Combobox global abgelegt
 * 29.07.2007 - Version 0.2
 * - in gui Package verschoben
 * - Neue Methoden eingefuegt
 * - Weitere Methoden geaendert
 * - Laden & Speichern ausgelagert
 * 13.05.2007 - Version 0.1
 * - Datei hinzugefuegt
 */

package info.kriese.soPra.gui;

import info.kriese.soPra.gui.input.DualLOPTableModel;
import info.kriese.soPra.gui.input.FractionalTableCellEditor;
import info.kriese.soPra.gui.input.LOPMinMax;
import info.kriese.soPra.gui.input.LOPTableCellRenderer;
import info.kriese.soPra.gui.input.LOPTableModel;
import info.kriese.soPra.gui.input.SpecialCaseInputPanel;
import info.kriese.soPra.gui.lang.Lang;
import info.kriese.soPra.lop.LOP;
import info.kriese.soPra.lop.LOPAdapter;
import info.kriese.soPra.lop.LOPEditor;
import info.kriese.soPra.lop.LOPSolution;
import info.kriese.soPra.math.Fractional;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

/**
 * Grafische Klasse zur Eingabe der Zielfunktion
 * 
 * @author Peer Sterner
 * @since 13.05.2007
 * @version 0.7.3
 */
public final class InputPanel extends JPanel {

    /**
     * Dient zu Serialisierung. (Hat bei uns keine Verwendung)
     */
    private static final long serialVersionUID = 4944381133035213540L;

    /**
     * Erstellt einen einfachen Rahmen mit Titel.
     * 
     * @param title -
     *                Titel des Rahmens
     * @return Ein Rahmen mit Titel.
     */
    public static Border createBorder(String title) {
	return createBorder(title, false);
    }

    /**
     * Erstellt einen einfachen Rahmen mit Titel.
     * 
     * @param title -
     *                Titel des Rahmens
     * @param last -
     *                Falls "TRUE", wird unter dem Rahmen noch ein kleiner
     *                Abstand hinzugefügt.
     * @return Ein Rahmen mit Titel.
     */
    public static Border createBorder(String title, boolean last) {
	Border outer, inner, titled;

	inner = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
	outer = BorderFactory.createEmptyBorder(10, 10, last ? 10 : 0, 10);

	titled = BorderFactory.createTitledBorder(inner, title,
		TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION);

	return BorderFactory.createCompoundBorder(outer, titled);
    }

    /**
     * Button, der die Überprüfung der durch den Benutzer eigegebenen Lösungen
     * anstößt.
     */
    private JButton check = null;

    /**
     * Modell, welches das duale LOP in die Tabelle rendert.
     */
    private final DualLOPTableModel dualModel;

    /**
     * Liste von Menüeinträgen, welche in das Hauptmenü eingebunden werden
     * sollen.
     */
    private final List<JMenuItem> functions;

    /**
     * Aufwahlbox, damit kann der Benutzer das primale Problem als Minimum oder
     * Maximum markieren.
     */
    private final JComboBox maxEditor;

    /**
     * Modell, welches das primale LOP in die Tabelle rendert.
     */
    private final LOPTableModel primalModel;

    /**
     * Button, welcher das LOP auf den zuletzt übernommenen Zustand setzten
     * lässt.
     */
    private JButton reset = null;

    /**
     * EingabePanel, auf dem der Benutzer die Spezialfälle der LOP eingeben
     * kann.
     */
    private final SpecialCaseInputPanel sci;

    /**
     * Tabelle, in der die LOP's gerendert werden.
     */
    private final JTable table;

    /**
     * Button, der Änderungen in der Tabelle in das LOP übernehmen lässt.
     */
    private JButton take = null;

    /**
     * Menüpinkte, welche den Funktionen der entsprechenden Buttons entsprechen.
     */
    private JMenuItem take2, check2, reset2;

    /**
     * Toolbar, in der die Buttons angezeigt werden.
     */
    private final JToolBar toolbar;

    /**
     * Liste der Buttons, diese dient zum de- / aktivieren der Buttons.
     */
    private final List<Component> toolbarBtns;

    /**
     * Konstruktor, der alle benötigten Variablen und Objekte initialisiert.
     */
    public InputPanel() {
	JScrollPane scrollPane;
	JPanel body;

	this.sci = new SpecialCaseInputPanel();

	this.maxEditor = new JComboBox();
	this.maxEditor.setFocusable(false);
	this.maxEditor.setToolTipText(Lang.getString("Input.MinMax"));
	this.maxEditor.addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseEntered(MouseEvent e) {
		MessageHandler.showHelp(InputPanel.this.maxEditor
			.getToolTipText());
	    }

	    @Override
	    public void mouseExited(MouseEvent e) {
		MessageHandler.showHelp();
	    }
	});
	this.maxEditor.addItem(LOPMinMax.MAX);
	this.maxEditor.addItem(LOPMinMax.MIN);
	this.maxEditor.setBorder(BorderFactory.createEmptyBorder());

	this.primalModel = new LOPTableModel();
	this.primalModel.setSpecialCasesComponent(this.sci);
	this.primalModel.addTableModelListener(new TableModelListener() {
	    public void tableChanged(TableModelEvent e) {
		setSaveBtn();
		initColumnSizes();
	    }
	});

	this.dualModel = new DualLOPTableModel();
	this.dualModel.setSpecialCasesComponent(this.sci);
	this.dualModel.addTableModelListener(new TableModelListener() {
	    public void tableChanged(TableModelEvent e) {
		initColumnSizes();
	    }
	});

	// Überladen einiger Funktionen der JTable
	this.table = new JTable() {

	    private static final long serialVersionUID = 1L;

	    /**
	     * Überschriebene Funktion zur Selektierung von Zellen.
	     * 
	     * Nicht editierbare Zellen können nicht markiert werden.
	     * 
	     * @param row -
	     *                Zu selektierende Zeile.
	     * @param col -
	     *                Zu selektierende Spalte.
	     * @param toggle -
	     *                Hat bei uns keine Bedeutung.
	     * @param extend -
	     *                Hat bei uns keine Bedeutung.
	     * @see JTable.changeSelection(int, int,boolean,boolean)
	     */
	    @Override
	    public void changeSelection(int row, int col, boolean toggle,
		    boolean extend) {

		int rows = getRowCount(), cols = getColumnCount();

		if (getModel() == InputPanel.this.primalModel) {

		    if (row == 1 || row == rows - 2)
			row++;
		} else
		    row = rows - 1;

		if (col == 0 || col == cols - 2)
		    col++;

		super.changeSelection(row, col, toggle, extend);
	    }

	    /**
	     * Überschriebene Funktion, welche den Zellspeziefischen Editor
	     * zurückgibt.
	     * 
	     * @param row -
	     *                Zeile der Zelle.
	     * @param column -
	     *                Spalte der Zelle.
	     * @return Ein TableCellEditor für die gewählte Zelle
	     */
	    @Override
	    public TableCellEditor getCellEditor(int row, int column) {

		if (column > 0 && column < getColumnCount() - 2)
		    return getDefaultEditor(Fractional.class);

		if (column == getColumnCount() - 1 && row > 0)
		    return getDefaultEditor(Fractional.class);

		return super.getCellEditor(row, column);
	    }
	};
	// this.table.setFillsViewportHeight(true);
	this.table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	this.table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	this.table.setRowSelectionAllowed(false);
	this.table.setRowHeight(this.table.getRowHeight() + 6);
	this.table.setShowGrid(false);
	this.table.setIntercellSpacing(new Dimension(2, 2));
	this.table.setDefaultRenderer(Object.class, new LOPTableCellRenderer());
	this.table.setDefaultEditor(LOPMinMax.class, new DefaultCellEditor(
		this.maxEditor));
	this.table.setDefaultEditor(Fractional.class,
		new FractionalTableCellEditor());
	this.table.setBackground(null);
	this.table.setOpaque(false);
	this.table.setBorder(BorderFactory.createEmptyBorder());

	this.table.getTableHeader().setReorderingAllowed(false);
	this.table.getTableHeader().setResizingAllowed(true);

	this.toolbar = new JToolBar("Input.Menu");
	this.toolbar.setFloatable(false);
	this.toolbar.setRollover(true);
	this.toolbar.setBorder(createBorder(Lang.getString("Input.Menu")));
	this.toolbarBtns = new ArrayList<Component>();
	this.functions = new ArrayList<JMenuItem>();
	generateEditToolbar();

	body = new JPanel(new BorderLayout());
	body.setBorder(BorderFactory.createEmptyBorder());

	scrollPane = new JScrollPane(this.table);
	scrollPane.setBorder(createBorder(Lang.getString("Input.Title")));
	scrollPane.setPreferredSize(new Dimension(300, 200));
	body.add(scrollPane, BorderLayout.CENTER);

	body.add(this.sci, BorderLayout.SOUTH);

	scrollPane = new JScrollPane(body);
	scrollPane.setBorder(BorderFactory.createEmptyBorder());

	setLayout(new BorderLayout());
	add(this.toolbar, BorderLayout.NORTH);
	add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Gibt eine Referenz auf die Menüpunkte zurück, über diese man sie in das
     * Hauptmenü integrieren kann.
     * 
     * @return Liste von Menüpunkten.
     */
    public List<JMenuItem> getFunctions() {
	return this.functions;
    }

    /**
     * Setzt den LOPEditor, damit sich das EingabePanel und die TabellenModelle
     * für Aktionen registrieren können.
     * 
     * @param editor -
     *                LOPEditor, bei dem sich registriert werden soll.
     */
    public void setEditor(LOPEditor editor) {
	this.primalModel.setEditor(editor);
	this.dualModel.setEditor(editor);
	editor.getLOP().addProblemListener(new LOPAdapter() {

	    @Override
	    public void showDualProblem(LOP lop) {
		InputPanel.this.dualModel.setTable(InputPanel.this.table);
		InputPanel.this.sci
			.setVisible(LOPSolution.SOLUTION_AREA, false);
		InputPanel.this.sci.reset();
		setToolbarEnabled(false);
	    }

	    @Override
	    public void showPrimalProblem(LOP lop) {
		InputPanel.this.primalModel.setTable(InputPanel.this.table);
		InputPanel.this.sci.setVisible(LOPSolution.SOLUTION_AREA, true);
		InputPanel.this.sci.reset();
		setToolbarEnabled(true);
	    }
	});
    }

    /**
     * Generiert die Toolbar und die Menüpunkte für das Eingabefenster.
     */
    private void generateEditToolbar() {

	this.toolbarBtns.add(this.toolbar.add(MenuMaker
		.getToolBarButton("Input.Menu.AddVar")));
	this.functions.add(MenuMaker.getMenuItem("Input.Menu.AddVar"));

	this.toolbarBtns.add(this.toolbar.add(MenuMaker
		.getToolBarButton("Input.Menu.DelVar")));
	this.functions.add(MenuMaker.getMenuItem("Input.Menu.DelVar"));

	this.toolbar.addSeparator();
	this.functions.add(new JMenuItem(MenuMaker.SEPARATOR));

	this.reset = MenuMaker.getToolBarButton("Input.Menu.Reset");
	this.toolbar.add(this.reset);
	this.reset2 = MenuMaker.getMenuItem("Input.Menu.Reset");
	this.functions.add(this.reset2);

	this.toolbarBtns.add(this.toolbar.add(MenuMaker
		.getToolBarButton("Input.Menu.Clear")));
	this.functions.add(MenuMaker.getMenuItem("Input.Menu.Clear"));

	this.toolbar.addSeparator();
	this.functions.add(new JMenuItem(MenuMaker.SEPARATOR));

	this.take = MenuMaker.getToolBarButton("Input.Menu.Save");
	this.toolbar.add(this.take);
	this.take2 = MenuMaker.getMenuItem("Input.Menu.Save");
	this.functions.add(this.take2);

	this.check = MenuMaker.getToolBarButton("Input.Menu.Check");
	this.toolbar.add(this.check);
	this.check2 = MenuMaker.getMenuItem("Input.Menu.Check");
	this.functions.add(this.check2);
    }

    /**
     * Formatiert die TabellenSpaleten auf bestimmte Breiten.
     */
    private void initColumnSizes() {
	TableColumn column = null;

	if (this.table.getColumnCount() == 0)
	    return;

	column = this.table.getColumnModel().getColumn(0);
	column.setPreferredWidth(50);

	for (int i = 1; i < this.table.getColumnCount() - 2; i++) {
	    column = this.table.getColumnModel().getColumn(i);
	    column.setPreferredWidth(40);
	}

	column = this.table.getColumnModel().getColumn(
		this.table.getColumnCount() - 2);
	column.setPreferredWidth(60);
	column = this.table.getColumnModel().getColumn(
		this.table.getColumnCount() - 1);
	column.setPreferredWidth(50);
    }

    /**
     * De- / Aktiviert Buttons und Menüpunkte entsprechend dem Zustand des
     * LOP's.
     */
    private void setSaveBtn() {
	if (this.primalModel.isEdited()) {
	    this.take.setEnabled(true);
	    this.take2.setEnabled(true);
	    this.reset.setEnabled(true);
	    this.reset2.setEnabled(true);
	} else {
	    this.take.setEnabled(false);
	    this.take2.setEnabled(false);
	    this.reset.setEnabled(false);
	    this.reset2.setEnabled(false);
	}
    }

    /**
     * De- / Aktiviert Buttons und Menüpunkte entsprechend dem Zustand des
     * LOP's.
     * 
     * @param value -
     *                "TRUE", falls alle Buttons und Menüpunkte aktiv sein
     *                sollen.
     */
    private void setToolbarEnabled(boolean value) {
	for (Component c : this.toolbarBtns)
	    c.setEnabled(value);

	for (Component c : this.functions)
	    c.setEnabled(value);

	this.check2.setEnabled(true);

	if (value)
	    setSaveBtn();
    }
}
