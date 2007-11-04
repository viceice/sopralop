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
 * 04.11.2007 - Version 0.6.1
 * - CellEditoren funktionieren jetzt auch wieder
 * 03.11.2007 - Version 0.6
 * - Model-Klasse ausgelagert
 * - Funktionalität aus dem ActionHandler hier her verlagert
 * - Eigenen CellRenderer eingeführt
 * 23.10.2007 - Version 0.5
 * - In InputPanel umbenannt und in JPannel geändert ( zur Integration ins Hauptfenster)
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
import info.kriese.soPra.gui.table.FractionalTableCellEditor;
import info.kriese.soPra.gui.table.LOPMinMax;
import info.kriese.soPra.gui.table.LOPOperator;
import info.kriese.soPra.gui.table.LOPTableCellRenderer;
import info.kriese.soPra.gui.table.LOPTableModel;
import info.kriese.soPra.lop.LOPEditor;
import info.kriese.soPra.math.Fractional;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
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
 * @version 0.6
 */
public final class InputPanel extends JPanel {

    /** */
    private static final long serialVersionUID = 4944381133035213540L;

    private final LOPTableModel model;

    private final JComboBox opEditor, maxEditor;

    private final JTable table;

    private Component take = null;

    public InputPanel() {

	setLayout(new BorderLayout());

	this.opEditor = new JComboBox();
	this.opEditor.setFocusable(false);
	this.opEditor.setToolTipText(Lang.getString("Input.Relation"));
	this.opEditor.addItem("=");
	this.opEditor.addItem(">");
	this.opEditor.addItem("<");

	this.maxEditor = new JComboBox();
	this.maxEditor.setFocusable(false);
	this.maxEditor.setToolTipText(Lang.getString("Input.MinMax"));
	this.maxEditor.addItem("max");
	this.maxEditor.addItem("min");

	this.model = new LOPTableModel();
	this.model.addTableModelListener(new TableModelListener() {

	    public void tableChanged(TableModelEvent e) {
		setSaveBtn();
		initColumnSizes();
	    }

	});

	this.table = new JTable() {

	    private static final long serialVersionUID = 1L;

	    @Override
	    public TableCellEditor getCellEditor(int row, int column) {
		if (column > 0 && column < getColumnCount() - 2)
		    return getDefaultEditor(Fractional.class);

		if (column == getColumnCount() - 1 && row > 0)
		    return getDefaultEditor(Fractional.class);

		return super.getCellEditor(row, column);
	    }
	};
	this.table.setFillsViewportHeight(true);
	this.table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	this.table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	this.table.setRowSelectionAllowed(false);
	this.table.setRowHeight(this.table.getRowHeight() + 6);
	this.table.setShowGrid(false);
	this.table.setIntercellSpacing(new Dimension(2, 2));
	this.table.setDefaultRenderer(Object.class, new LOPTableCellRenderer());
	this.table.setDefaultEditor(LOPOperator.class, new DefaultCellEditor(
		this.opEditor));
	this.table.setDefaultEditor(LOPMinMax.class, new DefaultCellEditor(
		this.maxEditor));
	this.table.setDefaultEditor(Fractional.class,
		new FractionalTableCellEditor());

	this.table.getTableHeader().setReorderingAllowed(false);

	this.model.setTable(this.table);

	JScrollPane scrollPane = new JScrollPane(this.table);

	generateEditToolbar();

	add(scrollPane, BorderLayout.CENTER);
    }

    public void setEditor(LOPEditor editor) {
	this.model.setEditor(editor);
    }

    /**
     * Toolbar fuer das Eingabefenster, stellt div. Buttons bereit
     * 
     */
    private void generateEditToolbar() {
	JToolBar toolbar = new JToolBar("Input.Menu");
	add(toolbar, BorderLayout.PAGE_START);

	toolbar.setFloatable(false);

	toolbar.add(MenuMaker.getToolBarButton("Input.Menu.AddVar"));
	toolbar.add(MenuMaker.getToolBarButton("Input.Menu.DelVar"));
	toolbar.addSeparator();
	toolbar.add(MenuMaker.getToolBarButton("Input.Menu.Reset"));
	toolbar.add(MenuMaker.getToolBarButton("Input.Menu.Clear"));
	toolbar.addSeparator();
	this.take = toolbar.add(MenuMaker.getToolBarButton("Input.Menu.Save"));
    }

    /**
     * Formatierung der Zellenelemente
     * 
     */
    private void initColumnSizes() {
	TableColumn column = null;

	column = this.table.getColumnModel().getColumn(0);
	column.setPreferredWidth(40);

	for (int i = 1; i < this.table.getColumnCount() - 2; i++) {
	    column = this.table.getColumnModel().getColumn(i);
	    column.setPreferredWidth(40);
	}

	column = this.table.getColumnModel().getColumn(
		this.table.getColumnCount() - 2);
	column.setPreferredWidth(60);
	column = this.table.getColumnModel().getColumn(
		this.table.getColumnCount() - 1);
	column.setPreferredWidth(40);
    }

    private void setSaveBtn() {
	if (this.model.isEdited())
	    this.take.setEnabled(true);
	else
	    this.take.setEnabled(false);
    }
}
