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

import info.kriese.soPra.gui.lang.Lang;
import info.kriese.soPra.gui.table.DualLOPTableModel;
import info.kriese.soPra.gui.table.FractionalTableCellEditor;
import info.kriese.soPra.gui.table.LOPMinMax;
import info.kriese.soPra.gui.table.LOPSolutionWrapper;
import info.kriese.soPra.gui.table.LOPTableCellRenderer;
import info.kriese.soPra.gui.table.LOPTableModel;
import info.kriese.soPra.gui.table.SolutionTableCellEditor;
import info.kriese.soPra.lop.LOP;
import info.kriese.soPra.lop.LOPAdapter;
import info.kriese.soPra.lop.LOPEditor;
import info.kriese.soPra.math.Fractional;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

/**
 * grafische Klasse zur Eingabe der Zielfunktion
 * 
 * @author Peer Sterner
 * @since 13.05.2007
 * @version 0.7.1
 */
public final class InputPanel extends JPanel {

    /** */
    private static final long serialVersionUID = 4944381133035213540L;

    private JButton check = null;

    private final DualLOPTableModel dualModel;

    private final List<JMenuItem> functions;

    private final JComboBox maxEditor;

    private final LOPTableModel primalModel;

    private JButton reset = null;

    private final JTable table;

    private JButton take = null;
    private JMenuItem take2, check2, reset2;

    private final List<Component> toolbarBtns;

    public InputPanel() {

	setLayout(new BorderLayout());

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
	this.maxEditor.addItem("max");
	this.maxEditor.addItem("min");

	this.primalModel = new LOPTableModel();
	this.primalModel.addTableModelListener(new TableModelListener() {
	    public void tableChanged(TableModelEvent e) {
		setSaveBtn();
		initColumnSizes();
	    }
	});

	this.dualModel = new DualLOPTableModel();
	this.dualModel.addTableModelListener(new TableModelListener() {
	    public void tableChanged(TableModelEvent e) {
		initColumnSizes();
	    }
	});

	this.table = new JTable() {

	    private static final long serialVersionUID = 1L;

	    @Override
	    public TableCellEditor getCellEditor(int row, int column) {
		if (row == getRowCount() - 1)
		    return getDefaultEditor(LOPSolutionWrapper.class);

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
	this.table.setDefaultEditor(LOPSolutionWrapper.class,
		new SolutionTableCellEditor());

	this.table.getTableHeader().setReorderingAllowed(false);

	JScrollPane scrollPane = new JScrollPane(this.table);

	this.toolbarBtns = new ArrayList<Component>();
	this.functions = new ArrayList<JMenuItem>();
	generateEditToolbar();

	add(scrollPane, BorderLayout.CENTER);
    }

    public List<JMenuItem> getFunctions() {
	return this.functions;
    }

    public void setEditor(LOPEditor editor) {
	this.primalModel.setEditor(editor);
	this.dualModel.setEditor(editor);
	editor.getLOP().addProblemListener(new LOPAdapter() {

	    @Override
	    public void showDualProblem(LOP lop) {
		InputPanel.this.dualModel.setTable(InputPanel.this.table);
		setToolbarEnabled(false);
	    }

	    @Override
	    public void showPrimalProblem(LOP lop) {
		InputPanel.this.primalModel.setTable(InputPanel.this.table);
		setToolbarEnabled(true);
	    }
	});
    }

    /**
     * Toolbar fuer das Eingabefenster, stellt div. Buttons bereit
     * 
     */
    private void generateEditToolbar() {
	JToolBar toolbar = new JToolBar("Input.Menu");
	add(toolbar, BorderLayout.PAGE_START);

	toolbar.setFloatable(false);
	toolbar.setRollover(true);

	this.toolbarBtns.add(toolbar.add(MenuMaker
		.getToolBarButton("Input.Menu.AddVar")));
	this.functions.add(MenuMaker.getMenuItem("Input.Menu.AddVar"));

	this.toolbarBtns.add(toolbar.add(MenuMaker
		.getToolBarButton("Input.Menu.DelVar")));
	this.functions.add(MenuMaker.getMenuItem("Input.Menu.DelVar"));

	toolbar.addSeparator();
	this.functions.add(new JMenuItem(MenuMaker.SEPARATOR));

	this.reset = MenuMaker.getToolBarButton("Input.Menu.Reset");
	toolbar.add(this.reset);
	this.reset2 = MenuMaker.getMenuItem("Input.Menu.Reset");
	this.functions.add(this.reset2);

	this.toolbarBtns.add(toolbar.add(MenuMaker
		.getToolBarButton("Input.Menu.Clear")));
	this.functions.add(MenuMaker.getMenuItem("Input.Menu.Clear"));

	toolbar.addSeparator();
	this.functions.add(new JMenuItem(MenuMaker.SEPARATOR));

	this.take = MenuMaker.getToolBarButton("Input.Menu.Save");
	toolbar.add(this.take);
	this.take2 = MenuMaker.getMenuItem("Input.Menu.Save");
	this.functions.add(this.take2);

	this.check = MenuMaker.getToolBarButton("Input.Menu.Check");
	this.toolbarBtns.add(toolbar.add(this.check));
	this.check2 = MenuMaker.getMenuItem("Input.Menu.Check");
	this.functions.add(this.check2);
    }

    /**
     * Formatierung der Zellenelemente
     * 
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

    private void setToolbarEnabled(boolean value) {
	for (Component c : this.toolbarBtns)
	    c.setEnabled(value);

	for (Component c : this.functions)
	    c.setEnabled(value);

	if (value)
	    setSaveBtn();
    }
}
