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
import info.kriese.soPra.io.impl.SettingsFactory;
import info.kriese.soPra.lop.LOP;
import info.kriese.soPra.lop.LOPAdapter;
import info.kriese.soPra.math.Vector3Frac;
import info.kriese.soPra.math.impl.FractionalFactory;
import info.kriese.soPra.math.impl.Vector3FracFactory;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
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
 * @version 0.4
 */
public final class InputDialog extends JDialog implements ActionListener {

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
	    setColumnCount(InputDialog.this.vectors.size());
	}

	/**
	 * erweitert die Tabelle um eine weitere Variable xi
	 * 
	 */
	public void addColumn() {
	    int num = InputDialog.this.vectors.size();

	    if (num == LOP.MAX_VECTORS)
		return;

	    InputDialog.this.vectors.add(Vector3FracFactory.getInstance());
	    num++;
	    this.columnNames.insertElementAt("<html><b>x<sub>" + num
		    + "</sub></b></html>", num);
	    fireTableStructureChanged();
	    pullDownColumn(InputDialog.this.table, InputDialog.this.table
		    .getColumnModel().getColumn(
			    InputDialog.this.table.getColumnCount() - 2));
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
	    int num = InputDialog.this.vectors.size();

	    if (col == 0)
		switch (row) {
		    case 0:
			return "<html><b>"
				+ Lang.getString("String.TargetFunction")
				+ ":</b></html>";

		    default:
			return "<html><b>NB " + row + ":</b></html>";
		}

	    if (col == num + 1) {
		if (row == 0)
		    return "=";
		return InputDialog.this.operators[row - 1];
	    }

	    if (col == num + 2)
		switch (row) {
		    case 0:
			return InputDialog.this.max ? "max" : "min";
		    case 1:
			return InputDialog.this.target.getCoordX()
				.getNumerator();
		    default:
			return InputDialog.this.target.getCoordY()
				.getNumerator();
		}

	    Vector3Frac vec = InputDialog.this.vectors.get(col - 1);

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
		    || ((row == 0) && (col == InputDialog.this.vectors.size() + 1)))
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
	    int num = InputDialog.this.vectors.size();

	    if (num > LOP.MIN_VECTORS) {
		InputDialog.this.vectors.remove(num - 1);
		this.columnNames.remove(num);
		num--;
		fireTableStructureChanged();
		pullDownColumn(InputDialog.this.table, InputDialog.this.table
			.getColumnModel().getColumn(
				InputDialog.this.table.getColumnCount() - 2));
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
	    int num = InputDialog.this.vectors.size();

	    if (col == num + 1) {
		if (row > 0)
		    InputDialog.this.operators[row - 1] = (String) value;
	    } else

	    if (col == num + 2)
		switch (row) {
		    case 0:
			String s = (String) value;
			if (s.contains("min"))
			    InputDialog.this.max = false;
			else
			    InputDialog.this.max = true;
			;
			break;
		    case 1:
			try {
			    InputDialog.this.target.getCoordX().setNumerator(
				    Integer.parseInt((String) value));
			} catch (NumberFormatException e) {
			}
			break;
		    default:
			try {
			    InputDialog.this.target.getCoordY().setNumerator(
				    Integer.parseInt((String) value));
			} catch (NumberFormatException e) {
			}
			break;
		}
	    else {

		Vector3Frac vec = InputDialog.this.vectors.get(col - 1);

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
	}

    }

    /** */
    private static final long serialVersionUID = 4944381133035213540L;

    private final JComboBox comboBox;

    private final LOP lop;

    private boolean max;

    private final InputTableModel model;

    private String[] operators;

    private final JTable table;

    private Vector3Frac target;

    private final List<Vector3Frac> vectors;

    /**
     * InputTable ueberladen: wenn Aufruf ohne Argumente, dann werden die
     * Variablen mit Standardwerten initialisiert ansonsten werden die
     * Vektordaten aus den uebergebenen Argumenten verwendet
     * 
     */
    public InputDialog(JFrame owner, LOP lop) {
	super(owner, Lang.getString("Input.Title") + " - Version "
		+ SettingsFactory.getInstance().getVersion(), true);
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

	setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }

    /**
     * ActionListener fuer die Toolbar...
     * 
     */
    public void actionPerformed(ActionEvent e) {
	String cmd = e.getActionCommand();
	if (cmd.equals("Input.Menu.AddVar"))
	    this.model.addColumn();
	else if (cmd.equals("Input.Menu.DelVar"))
	    this.model.removeColumn();
	else if (cmd.equals("Input.Menu.Save")) {
	    this.lop.setVectors(this.vectors);
	    this.lop.setTarget(this.target);
	    this.lop.setMaximum(this.max);
	    this.lop.setOperators(this.operators);
	    this.lop.problemChanged();
	} else if (cmd.equals("Input.Menu.Clear"))
	    clear();
	else if (cmd.equals("Input.Menu.Reset"))
	    update();
    }

    /**
     * Pulldown-Menue fuer die Spalte Nebenbedingungen
     * 
     * wird spaeter fuer die Generierung des dualen Problems benoetigt...
     */
    public void pullDownColumn(JTable table, TableColumn relation) {
	relation.setCellEditor(new DefaultCellEditor(this.comboBox));

	// Set up tool tips for the sport cells.
	DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
	renderer.setToolTipText(Lang.getString("Input.Relation"));
	relation.setCellRenderer(renderer);
    }

    @Override
    public void setVisible(boolean b) {
	super.setVisible(b);
	update();
    }

    private void clear() {

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
    }

    /**
     * Toolbar fuer das Eingabefenster, stellt div. Buttons bereit
     * 
     */
    private void generateEditToolbar() {
	JToolBar toolbar;
	toolbar = new JToolBar("Input.Menu");
	toolbar.setFloatable(false);

	toolbar.add(MenuMaker.getToolBarButton("Input.Menu.AddVar", this));
	toolbar.add(MenuMaker.getToolBarButton("Input.Menu.DelVar", this));
	toolbar.addSeparator();
	toolbar.add(MenuMaker.getToolBarButton("Input.Menu.Reset", this));
	toolbar.add(MenuMaker.getToolBarButton("Input.Menu.Clear", this));
	toolbar.addSeparator();
	toolbar.add(MenuMaker.getToolBarButton("Input.Menu.Save", this));

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

    private void update() {
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
    }
}
