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
 * 01.11.2007 - Version 0.2
 * - An neuen ActionHandler angepasst
 * 30.10.2007 - Version 0.1
 * - Aus InputPanel ausgelagert
 */
package info.kriese.soPra.gui.table;

import info.kriese.soPra.gui.lang.Lang;
import info.kriese.soPra.lop.LOP;
import info.kriese.soPra.lop.LOPEditor;
import info.kriese.soPra.lop.LOPEditorAdapter;
import info.kriese.soPra.math.Vector3Frac;
import info.kriese.soPra.math.impl.FractionalFactory;
import info.kriese.soPra.math.impl.Vector3FracFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

/**
 * Wandelt das LOP in ein von JTable lesbares Format um.
 * 
 * @author Peer Sterner
 * @version 0.2
 * @since 01.11.2007
 * 
 */
public final class LOPTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 13L;

    private final Vector<String> columnNames;

    private final JComboBox comboBox;

    private boolean edited = false;

    private boolean max;

    private final String[] operators;

    private JTable table;

    private Vector3Frac target;

    private final List<Vector3Frac> vectors;

    public LOPTableModel() {
	this.columnNames = new Vector<String>();
	this.vectors = new ArrayList<Vector3Frac>();
	this.operators = new String[2];
	setColumnCount();

	this.comboBox = new JComboBox();
	this.comboBox.addItem("=");
	this.comboBox.addItem(">");
	this.comboBox.addItem("<");
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
	int num = this.vectors.size();

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
	    return this.operators[row - 1];
	}

	if (col == num + 2)
	    switch (row) {
		case 0:
		    return this.max ? "max" : "min";
		case 1:
		    return this.target.getCoordX().getNumerator();
		default:
		    return this.target.getCoordY().getNumerator();
	    }

	Vector3Frac vec = this.vectors.get(col - 1);

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
	if ((col < 1) || ((row == 0) && (col == this.vectors.size() + 1)))
	    return false;
	else
	    return true;
    }

    public boolean isEdited() {
	return this.edited;
    }

    public void setEdited(boolean value) {
	this.edited = value;
    }

    public void setEditor(LOPEditor editor) {
	editor.addListener(new LOPEditorAdapter() {
	    @Override
	    public void addVariable(LOP lop) {
		LOPTableModel.this.addColumn();
	    }

	    @Override
	    public void clear(LOP lop) {
		LOPTableModel.this.clear();
	    }

	    @Override
	    public void removeVariable(LOP lop) {
		LOPTableModel.this.removeColumn();
	    }

	    @Override
	    public boolean take(LOP lop) {
		return LOPTableModel.this.save(lop);
	    }

	    @Override
	    public void update(LOP lop) {
		LOPTableModel.this.update(lop);
	    }
	});
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
	int num = this.vectors.size();

	if (col == num + 1) {
	    if (row > 0)
		this.operators[row - 1] = (String) value;
	} else

	if (col == num + 2)
	    switch (row) {
		case 0:
		    String s = (String) value;
		    if (s.contains("min"))
			this.max = false;
		    else
			this.max = true;
		    ;
		    break;
		case 1:
		    try {
			this.target.getCoordX().setNumerator(
				Integer.parseInt((String) value));
		    } catch (NumberFormatException e) {
		    }
		    break;
		default:
		    try {
			this.target.getCoordY().setNumerator(
				Integer.parseInt((String) value));
		    } catch (NumberFormatException e) {
		    }
		    break;
	    }
	else {

	    Vector3Frac vec = this.vectors.get(col - 1);

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

    public void update(LOP lop) {
	this.vectors.clear();
	for (Vector3Frac vec : lop.getVectors())
	    this.vectors.add(vec.clone());
	this.target = lop.getTarget().clone();
	this.operators[0] = lop.getOperators()[0];
	this.operators[1] = lop.getOperators()[1];
	this.max = lop.isMaximum();

	setColumnCount();

	if (this.table != null)
	    pullDownColumn(this.table.getColumnModel().getColumn(
		    this.table.getColumnCount() - 2));

	setEdited(false);
    }

    /**
     * erweitert die Tabelle um eine weitere Variable xi
     * 
     */
    private void addColumn() {
	int num = this.vectors.size();

	if (num == LOP.MAX_VECTORS)
	    return;

	this.vectors.add(Vector3FracFactory.getInstance());
	num++;
	this.columnNames.insertElementAt("<html><center><b>x<sub>" + num
		+ "</sub></b></center></html>", num);
	fireTableStructureChanged();
	setEdited(true);
    }

    private void clear() {
	while (this.vectors.size() > LOP.MIN_VECTORS)
	    removeColumn();

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
	fireTableDataChanged();
	setEdited(true);
    }

    /**
     * Pulldown-Menue fuer die Spalte Nebenbedingungen
     * 
     * wird spaeter fuer die Generierung des dualen Problems benoetigt...
     */
    private void pullDownColumn(TableColumn relation) {
	relation.setCellEditor(new DefaultCellEditor(this.comboBox));

	// Set up tool tips for the cells.
	DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
	renderer.setToolTipText(Lang.getString("Input.Relation"));
	relation.setCellRenderer(renderer);
    }

    /**
     * reduziert die Tabelle um eine Variable xi;
     * 
     * bei 2 Variablen wird keine weitere entfernt
     */
    private void removeColumn() {
	int num = this.vectors.size();

	if (num > LOP.MIN_VECTORS) {
	    setEdited(true);
	    this.vectors.remove(num - 1);
	    this.columnNames.remove(num);
	    num--;
	    fireTableStructureChanged();
	}
    }

    private boolean save(LOP lop) {
	int cnt = 0;
	for (Vector3Frac vec : this.vectors)
	    if (!vec.equals(Vector3Frac.ZERO))
		cnt++;
	if (cnt < this.vectors.size()) {
	    JOptionPane.showMessageDialog(this.table, Lang
		    .getString("Errors.NoZeroVectors"), Lang
		    .getString("Strings.Error"), JOptionPane.ERROR_MESSAGE);
	    return false;
	}

	lop.setVectors(this.vectors);
	lop.setTarget(this.target);
	lop.setMaximum(this.max);
	lop.setOperators(this.operators);
	lop.problemChanged();
	setEdited(false);
	return true;
    }

    private void setColumnCount() {
	this.columnNames.clear();
	this.columnNames.add(" ");
	for (int i = 1; i <= this.vectors.size(); i++)
	    this.columnNames.add("<html><center><b>x<sub>" + i
		    + "</sub></b></center></html>");
	this.columnNames.add("<html><center><b>" + "&lt; / > / ="
		+ "</b></center></html>");
	this.columnNames.add("<html><center><b>z</b></center></html>");
	fireTableStructureChanged();
    }

}