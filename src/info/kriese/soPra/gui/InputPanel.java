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
import info.kriese.soPra.lop.LOP;
import info.kriese.soPra.lop.LOPAdapter;
import info.kriese.soPra.math.Vector3Frac;
import info.kriese.soPra.math.impl.FractionalFactory;
import info.kriese.soPra.math.impl.Vector3FracFactory;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 * grafische Klasse zur Eingabe der Zielfunktion
 * 
 * @author Peer Sterner
 * @since 13.05.2007
 * @version 0.5
 */
public final class InputPanel extends JPanel {

    /**
     * Modell für die Eingabeklasse. Beschreibt die zu erstellende Tabelle
     * 
     */
    private final class InputTableModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 13L;

	private final Vector<String> columnNames;

	public InputTableModel() {
	    this.columnNames = new Vector<String>();
	    setColumnCount(InputPanel.this.vectors.size());
	}

	/**
	 * erweitert die Tabelle um eine weitere Variable xi
	 * 
	 */
	public void addColumn() {
	    int num = InputPanel.this.vectors.size();

	    if (num == LOP.MAX_VECTORS)
		return;

	    InputPanel.this.vectors.add(Vector3FracFactory.getInstance());
	    num++;
	    this.columnNames.insertElementAt("<html><b>x<sub>" + num
		    + "</sub></b></html>", num);
	    fireTableStructureChanged();
	    pullDownColumn(InputPanel.this.table, InputPanel.this.table
		    .getColumnModel().getColumn(
			    InputPanel.this.table.getColumnCount() - 2));
	}

	@Override
	public Class<?> getColumnClass(int c) {
	    return getValueAt(0, c).getClass();
	}

	public int getColumnCount() {
	    return this.columnNames.size();
	}

	@Override
	public String getColumnName(int col) {
	    return this.columnNames.get(col);
	}

	public int getRowCount() {
	    return 3;
	}

	public Object getValueAt(int row, int col) {
	    int num = InputPanel.this.vectors.size();

	    if (col == 0)
		switch (row) {
		    case 0:
			return "<html><b>"
				+ Lang.getString("Strings.TargetFunction")
				+ ":</b></html>";

		    default:
			return "<html><b>NB " + row + ":</b></html>";
		}

	    if (col == num + 1) {
		if (row == 0)
		    return "=";
		return InputPanel.this.operators[row - 1];
	    }

	    if (col == num + 2)
		switch (row) {
		    case 0:
			return InputPanel.this.max ? "max" : "min";
		    case 1:
			return InputPanel.this.target.getCoordX()
				.getNumerator();
		    default:
			return InputPanel.this.target.getCoordY()
				.getNumerator();
		}

	    Vector3Frac vec = InputPanel.this.vectors.get(col - 1);

	    switch (row) {
		case 0:
		    return vec.getCoordZ().getNumerator();
		case 1:
		    return vec.getCoordX().getNumerator();
		default:
		    return vec.getCoordY().getNumerator();
	    }

	}

	/**
	 * legt die Editierbarkeit einzelner Zellen fest
	 * 
	 */
	@Override
	public boolean isCellEditable(int row, int col) {
	    if ((col < 1)
		    || ((row == 0) && (col == InputPanel.this.vectors.size() + 1)))
		return false;
	    else
		return true;
	}

	/**
	 * reduziert die Tabelle um eine Variable xi;
	 * 
	 * bei 2 Variablen wird keine weitere entfernt
	 */
	public void removeColumn() {
	    int num = InputPanel.this.vectors.size();

	    if (num > LOP.MIN_VECTORS) {
		InputPanel.this.vectors.remove(num - 1);
		this.columnNames.remove(num);
		num--;
		fireTableStructureChanged();
		pullDownColumn(InputPanel.this.table, InputPanel.this.table
			.getColumnModel().getColumn(
				InputPanel.this.table.getColumnCount() - 2));
	    }
	}

	public void setColumnCount(int size) {
	    this.columnNames.clear();
	    this.columnNames.add(" ");
	    for (int i = 1; i <= size; i++)
		this.columnNames.add("<html><b>x<sub>" + i
			+ "</sub></b></html>");
	    this.columnNames.add("<html><b>" + "&lt; / > / =" + "</b></html>");
	    this.columnNames.add("<html><b>z</b></html>");
	    fireTableStructureChanged();
	}

	@Override
	public void setValueAt(Object value, int row, int col) {
	    int num = InputPanel.this.vectors.size();

	    if (col == num + 1) {
		if (row > 0)
		    InputPanel.this.operators[row - 1] = (String) value;
	    } else

	    if (col == num + 2)
		switch (row) {
		    case 0:
			String s = (String) value;
			if (s.contains("min"))
			    InputPanel.this.max = false;
			else
			    InputPanel.this.max = true;
			;
			break;
		    case 1:
			try {
			    InputPanel.this.target.getCoordX().setNumerator(
				    Integer.parseInt((String) value));
			} catch (NumberFormatException e) {
			}
			break;
		    default:
			try {
			    InputPanel.this.target.getCoordY().setNumerator(
				    Integer.parseInt((String) value));
			} catch (NumberFormatException e) {
			}
			break;
		}
	    else {

		Vector3Frac vec = InputPanel.this.vectors.get(col - 1);

		int val = 0;
		try {
		    val = (Integer) value;
		} catch (NumberFormatException e) {
		}

		switch (row) {
		    case 0:
			vec.getCoordZ().setNumerator(val);
			break;
		    case 1:
			vec.getCoordX().setNumerator(val);
			break;
		    default:
			vec.getCoordY().setNumerator(val);
			break;
		}
	    }
	    fireTableCellUpdated(row, col);
	    setEdited(true);
	}

    }

    /** */
    private static final long serialVersionUID = 4944381133035213540L;

    private final JComboBox comboBox;

    private boolean edited = false;

    private final LOP lop;

    private boolean max;

    private final InputTableModel model;

    private String[] operators;

    private Component save;

    private final JTable table;

    private Vector3Frac target;

    private final List<Vector3Frac> vectors;

    /**
     * InputTable ueberladen: wenn Aufruf ohne Argumente, dann werden die
     * Variablen mit Standardwerten initialisiert ansonsten werden die
     * Vektordaten aus den uebergebenen Argumenten verwendet
     * 
     */
    public InputPanel(LOP lop) {
	this.lop = lop;

	this.vectors = new ArrayList<Vector3Frac>();

	lop.addProblemListener(new LOPAdapter() {

	    @Override
	    public void problemChanged(LOP lop) {
		update();
	    }
	});

	setLayout(new BorderLayout());
	setSize(600, 200);

	this.model = new InputTableModel();
	this.table = new JTable(this.model);

	JScrollPane scrollPane = new JScrollPane(this.table);
	this.table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

	initColumnSizes(this.table);

	this.comboBox = new JComboBox();
	this.comboBox.addItem("=");
	this.comboBox.addItem(">");
	this.comboBox.addItem("<");

	generateEditToolbar();

	add(scrollPane);

	update();
    }

    public void addColumn() {
	this.model.addColumn();
	setEdited(true);
    }

    public void clear() {

	while (this.vectors.size() > LOP.MIN_VECTORS)
	    this.model.removeColumn();

	for (int i = 0; i < LOP.MIN_VECTORS; i++) {
	    Vector3Frac vec = Vector3Frac.ZERO.clone();
	    switch (i % 3) {
		case 0:
		    vec.setCoordX(FractionalFactory.getInstance(1));
		    break;
		case 1:
		    vec.setCoordY(FractionalFactory.getInstance(1));
		    break;
		default:
		    vec.setCoordZ(FractionalFactory.getInstance(1));
		    break;
	    }
	    this.vectors.set(i, vec);
	}

	this.target.getCoordX().setNumerator(0);
	this.target.getCoordY().setNumerator(0);

	this.operators[0] = "=";
	this.operators[1] = "=";
	this.max = true;
	this.model.fireTableDataChanged();
	setEdited(true);
    }

    public String[] getOperators() {
	return this.operators;
    }

    public Vector3Frac getTarget() {
	return this.target;
    }

    public List<Vector3Frac> getVectors() {
	return this.vectors;
    }

    public boolean isEdited() {
	return this.edited;
    }

    public boolean isMax() {
	return this.max;
    }

    public void removeColumn() {
	this.model.removeColumn();
	setEdited(true);
    }

    public void setEdited(boolean value) {
	this.edited = value;
	this.save.setEnabled(value);
    }

    @Override
    public void setVisible(boolean b) {
	super.setVisible(b);
	update();
    }

    public void update() {
	this.vectors.clear();
	for (Vector3Frac vec : this.lop.getVectors())
	    this.vectors.add(vec.clone());
	this.target = this.lop.getTarget().clone();
	this.operators = new String[2];
	this.operators[0] = this.lop.getOperators()[0];
	this.operators[1] = this.lop.getOperators()[1];
	this.max = this.lop.isMaximum();

	this.model.setColumnCount(this.vectors.size());
	pullDownColumn(this.table, this.table.getColumnModel().getColumn(
		this.table.getColumnCount() - 2));
	setEdited(false);
    }

    /**
     * Toolbar fuer das Eingabefenster, stellt div. Buttons bereit
     * 
     */
    private void generateEditToolbar() {
	JToolBar toolbar;
	toolbar = new JToolBar("Input.Menu");
	toolbar.setFloatable(false);

	toolbar.add(MenuMaker.getToolBarButton("Input.Menu.AddVar",
		ActionHandler.INSTANCE));
	toolbar.add(MenuMaker.getToolBarButton("Input.Menu.DelVar",
		ActionHandler.INSTANCE));
	toolbar.addSeparator();
	toolbar.add(MenuMaker.getToolBarButton("Input.Menu.Reset",
		ActionHandler.INSTANCE));
	toolbar.add(MenuMaker.getToolBarButton("Input.Menu.Clear",
		ActionHandler.INSTANCE));
	toolbar.addSeparator();
	this.save = toolbar.add(MenuMaker.getToolBarButton("Input.Menu.Save",
		ActionHandler.INSTANCE));

	add(toolbar, BorderLayout.PAGE_START);
    }

    /**
     * Formatierung der Zellenelemente
     * 
     */
    private void initColumnSizes(JTable table) {
	InputTableModel model = (InputTableModel) table.getModel();
	TableColumn column = null;
	Component comp = null;
	int headerWidth = 0;
	int cellWidth = 0;
	TableCellRenderer headerRenderer = table.getTableHeader()
		.getDefaultRenderer();

	for (int i = 0; i < table.getColumnCount(); i++) {
	    column = table.getColumnModel().getColumn(i);

	    comp = headerRenderer.getTableCellRendererComponent(null, column
		    .getHeaderValue(), false, false, 0, 0);
	    headerWidth = comp.getPreferredSize().width;

	    comp = table.getDefaultRenderer(model.getColumnClass(i))
		    .getTableCellRendererComponent(table,
			    model.getColumnName(i), false, false, 0, i);
	    cellWidth = comp.getPreferredSize().width;
	    column.setPreferredWidth(Math.max(headerWidth, cellWidth));
	}
    }

    /**
     * Pulldown-Menue fuer die Spalte Nebenbedingungen
     * 
     * wird spaeter fuer die Generierung des dualen Problems benoetigt...
     */
    private void pullDownColumn(JTable table, TableColumn relation) {
	relation.setCellEditor(new DefaultCellEditor(this.comboBox));

	// Set up tool tips for the sport cells.
	DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
	renderer.setToolTipText(Lang.getString("Input.Relation"));
	relation.setCellRenderer(renderer);
    }
}
