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
 * 04.11.2007 - Version 0.2.2
 * - An neue CellEditoren angepasst
 * 03.10.2007 - Version 0.2.1
 * - An neuen CellRenderer angepasst
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
import info.kriese.soPra.math.Fractional;
import info.kriese.soPra.math.Vector3Frac;
import info.kriese.soPra.math.impl.FractionalFactory;
import info.kriese.soPra.math.impl.Vector3FracFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

/**
 * Wandelt das LOP in ein von JTable lesbares Format um.
 * 
 * @author Peer Sterner
 * @version 0.2.2
 * @since 01.11.2007
 * 
 */
public final class LOPTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 13L;

    private final Vector<String> columnNames;

    private boolean edited = false;

    private boolean max;

    private final String[] operators;

    private Fractional sol;

    private JTable table;

    private Vector3Frac target;

    private final List<Fractional> values;
    private final List<Vector3Frac> vectors;

    public LOPTableModel() {
	this.columnNames = new Vector<String>();
	this.vectors = new ArrayList<Vector3Frac>();
	this.operators = new String[2];
	this.values = new ArrayList<Fractional>();
	this.sol = FractionalFactory.getInstance();

	setColumnCount();
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
	return 6;
    }

    public Object getValueAt(int row, int col) {
	int num = this.vectors.size();

	if (row == 1 || row == 4)
	    return "";

	if (col == 0)
	    switch (row) {
		case 0:
		    return "<html><b>"
			    + Lang.getString("Strings.TargetFunction.Short")
			    + ":</b></html>";
		case 5:
		    return "<html><b>"
			    + Lang.getString("Strings.Solution.Short")
			    + ":</b></html>";
		default:
		    return "<html><b>"
			    + Lang.getString("Strings.Constraint.Short") + " "
			    + (row - 1) + ":</b></html>";
	    }

	if (col == num + 1) {
	    if (row == 0 || row == 5)
		return LOPOperator.getOp("=");
	    return LOPOperator.getOp(this.operators[row - 2]);
	}

	if (col == num + 2)
	    switch (row) {
		case 0:
		    return LOPMinMax.get(this.max);
		case 2:
		    return this.target.getCoordX();
		case 3:
		    return this.target.getCoordY();
		default:
		    return this.sol; // TODO:
		    // Lösung
		    // ein-
		    // /
		    // ausgeben
	    }

	Vector3Frac vec = this.vectors.get(col - 1);

	switch (row) {
	    case 0:
		return vec.getCoordZ();
	    case 2:
		return vec.getCoordX();
	    case 3:
		return vec.getCoordY();
	    case 5:
		return this.values.get(col - 1); // TODO: Lösungen für X1 ...
		// Xn ein- / asugeben
	}

	return "";
    }

    /**
     * legt die Editierbarkeit einzelner Zellen fest
     * 
     */
    @Override
    public boolean isCellEditable(int row, int col) {
	int vecs = this.vectors.size();
	if ((col < 1) || ((row == 0) && (col == vecs + 1)) || row == 1
		|| row == 4 || (row == 5 && col == vecs + 1))
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

    public void setTable(JTable table) {
	this.table = table;
	table.setModel(this);
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
	int num = this.vectors.size();

	if (col == num + 1) {
	    if (row == 2 || row == 3) {
		String s = (String) value;
		if (!this.operators[row - 2].equals(s))
		    this.operators[row - 2] = s;
		else
		    return;
	    }
	} else if (col == num + 2)
	    switch (row) {
		case 0:
		    boolean bool = LOPMinMax.get((String) value).isMax();
		    if (this.max && bool)
			return;
		    this.max = bool;
		    break;
		case 2:
		    if (this.target.getCoordX().equals(value))
			return;
		    this.target.setCoordX((Fractional) value);
		    break;
		case 3:
		    if (this.target.getCoordY().equals(value))
			return;
		    this.target.setCoordY((Fractional) value);
		    break;
		case 5:
		    this.sol = (Fractional) value;
		    break;
	    }
	else {
	    Vector3Frac vec = this.vectors.get(col - 1);

	    Fractional frac = (value != null ? (Fractional) value
		    : FractionalFactory.getInstance());

	    switch (row) {
		case 0:
		    if (vec.getCoordZ().equals(frac))
			return;
		    vec.setCoordZ(frac);
		    break;
		case 2:
		    if (vec.getCoordX().equals(frac))
			return;
		    vec.setCoordX(frac);
		    break;
		case 3:
		    if (vec.getCoordY().equals(frac))
			return;
		    vec.setCoordY(frac);
		    break;
		case 5:
		    this.values.set(col - 1, (Fractional) value);
		    break;
	    }
	}
	setEdited(true);
	fireTableCellUpdated(row, col);
    }

    public void update(LOP lop) {
	this.vectors.clear();
	this.values.clear();
	for (Vector3Frac vec : lop.getVectors()) {
	    this.vectors.add(vec.clone());
	    this.values.add(FractionalFactory.getInstance());
	}
	this.target = lop.getTarget().clone();
	this.operators[0] = lop.getOperators()[0];
	this.operators[1] = lop.getOperators()[1];
	this.max = lop.isMaximum();

	this.sol = FractionalFactory.getInstance();

	setEdited(false);
	setColumnCount();
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
	this.values.add(FractionalFactory.getInstance());
	num++;
	this.columnNames.insertElementAt("<html><center><b>x<sub>" + num
		+ "</sub></b></center></html>", num);
	setEdited(true);
	fireTableStructureChanged();
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
	    this.values.set(i, FractionalFactory.getInstance());
	}

	this.target.getCoordX().setNumerator(0);
	this.target.getCoordY().setNumerator(0);

	this.operators[0] = "=";
	this.operators[1] = "=";
	this.max = true;

	this.sol = FractionalFactory.getInstance();

	setEdited(true);
	fireTableDataChanged();
    }

    /**
     * reduziert die Tabelle um eine Variable xi;
     * 
     * bei 2 Variablen wird keine weitere entfernt
     */
    private void removeColumn() {
	int num = this.vectors.size();

	if (num <= LOP.MIN_VECTORS)
	    return;

	this.vectors.remove(num - 1);
	this.columnNames.remove(num);
	this.values.remove(num - 1);
	num--;
	setEdited(true);
	fireTableStructureChanged();
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
	fireTableCellUpdated(0, 0);
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