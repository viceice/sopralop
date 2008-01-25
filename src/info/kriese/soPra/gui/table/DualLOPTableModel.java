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
 * 25.01.2008 - Version 0.2
 * - Vorbereitung zur Lösungsüberprüfung
 * 10.01.2008 - Version 0.1.1
 * - Tabellenkopfbeschriftung geändert (y1, y2, w)
 * 09.11.2007 - Version 0.1
 * - Kopie von LOPTableModel
 */
package info.kriese.soPra.gui.table;

import info.kriese.soPra.gui.MessageHandler;
import info.kriese.soPra.gui.lang.Lang;
import info.kriese.soPra.lop.LOP;
import info.kriese.soPra.lop.LOPAdapter;
import info.kriese.soPra.lop.LOPEditor;
import info.kriese.soPra.lop.LOPEditorAdapter;
import info.kriese.soPra.lop.LOPSolution;
import info.kriese.soPra.math.Gauss;
import info.kriese.soPra.math.Vector3Frac;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

/**
 * Wandelt das duale LOP in ein von JTable lesbares Format um.
 * 
 * @author Peer Sterner
 * @version 0.2
 * @since 09.11.2007
 * 
 */
public final class DualLOPTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 13L;

    private final String[] columnNames;
    private boolean max;

    private final Object sol[];

    private JComponent specialCases;

    private JTable table;

    private Vector3Frac target;

    private final List<Vector3Frac> vectors;

    public DualLOPTableModel() {
	this.vectors = new ArrayList<Vector3Frac>();
	this.target = Vector3Frac.ZERO;
	this.sol = new Object[3];

	this.columnNames = new String[5];
	this.columnNames[0] = " ";
	this.columnNames[1] = "<html><center><b>y<sub>1</sub></b></center></html>";
	this.columnNames[2] = "<html><center><b>y<sub>2</sub></b></center></html>";
	this.columnNames[3] = "<html><center><b>" + "&lt; / > / ="
		+ "</b></center></html>";
	this.columnNames[4] = "<html><center><b>w</b></center></html>";
    }

    @Override
    public Class<?> getColumnClass(int c) {
	return getValueAt(0, c).getClass();
    }

    public int getColumnCount() {
	return 5;
    }

    @Override
    public String getColumnName(int col) {
	return this.columnNames[col];
    }

    public int getRowCount() {
	return this.vectors.size() + 4;
    }

    public Object getValueAt(int row, int col) {
	if (row == 1 || row == getRowCount() - 2)
	    return "";

	switch (col) {
	    case 0:
		if (row == 0)
		    return "<html><b>"
			    + Lang.getString("Strings.TargetFunction.Short")
			    + ":</b></html>";
		else if (row == getRowCount() - 1)
		    return "<html><b>"
			    + Lang.getString("Strings.Solution.Short")
			    + ":</b></html>";
		else
		    return "<html><b>"
			    + Lang.getString("Strings.Constraint.Short") + " "
			    + (row - 1) + ":</b></html>";
	    case 1:
		if (row == 0)
		    return this.target.getCoordX();
		else if (row == getRowCount() - 1)
		    return this.sol[0];
		else
		    return this.vectors.get(row - 2).getCoordX().toString();
	    case 2:
		if (row == 0)
		    return this.target.getCoordY();
		else if (row == getRowCount() - 1)
		    return this.sol[1];
		else
		    return this.vectors.get(row - 2).getCoordY().toString();
	    case 3:
		if (row == 0 || row == getRowCount() - 1
			|| row == getRowCount() - 1)
		    return "=";
		else
		    return (this.max ? ">=" : "<=");
	    case 4:
		if (row == 0)
		    return (this.max ? "min" : "max");
		else if (row == getRowCount() - 1)
		    return this.sol[2];
		else
		    return this.vectors.get(row - 2).getCoordZ();

	    default:
		return "";
	}
    }

    /**
     * legt die Editierbarkeit einzelner Zellen fest
     * 
     * @param row
     * @param col
     */
    @Override
    public boolean isCellEditable(int row, int col) {
	if (getRowCount() - 1 == row && col != 0 && col != 3)
	    return true;
	else
	    return false;
    }

    public boolean isVisible() {
	return this.table != null && this.table.getModel() == this;
    }

    public void setEditor(LOPEditor editor) {
	editor.addListener(new LOPEditorAdapter() {
	    @Override
	    public void check(LOP lop) {
		DualLOPTableModel.this.check(lop);
	    }

	    @Override
	    public void update(LOP lop) {
		DualLOPTableModel.this.update(lop);
	    }
	});

	editor.getLOP().addProblemListener(new LOPAdapter() {
	    @Override
	    public void showDualProblem(LOP lop) {
		DualLOPTableModel.this.specialCases.removeAll();
		DualLOPTableModel.this.specialCases.setVisible(false);
	    }
	});
    }

    public void setSpecialCasesComponent(JComponent c) {
	this.specialCases = c;
    }

    public void setTable(JTable table) {
	this.table = table;
	table.setModel(this);
	fireTableStructureChanged();
    }

    public void update(LOP lop) {
	this.vectors.clear();
	for (Vector3Frac vec : lop.getVectors())
	    this.vectors.add(vec.clone());
	this.target = lop.getTarget().clone();
	this.max = lop.isMaximum();

	LOPSolution solution = lop.getSolution();
	if (solution.getSpecialCase() == LOPSolution.NO_SOLUTION) {
	    this.sol[0] = LOPInfinity.INFINITY;
	    this.sol[1] = LOPInfinity.INFINITY;
	    this.sol[2] = LOPInfinity.INFINITY;
	} else if (solution.getSpecialCase() == LOPSolution.UNLIMITED) {
	    this.sol[0] = LOPNotExsitent.NOT_EXISTENT;
	    this.sol[1] = LOPNotExsitent.NOT_EXISTENT;
	    this.sol[2] = LOPNotExsitent.NOT_EXISTENT;
	} else {
	    Vector3Frac vec1 = solution.getAreas().get(0).getL1();
	    Vector3Frac vec2 = solution.getAreas().get(0).getL2();

	    Vector3Frac Vector1 = Vector3Frac.ZERO.clone();
	    Vector3Frac Vector2 = Vector3Frac.ZERO.clone();
	    Vector3Frac Vector3 = Vector3Frac.ZERO.clone();

	    Vector1.setCoordX(vec1.getCoordX());
	    Vector1.setCoordY(vec2.getCoordX());
	    Vector1.setCoordZ(this.target.getCoordX());

	    Vector2.setCoordX(vec1.getCoordY());
	    Vector2.setCoordY(vec2.getCoordY());
	    Vector2.setCoordZ(this.target.getCoordY());

	    Vector3.setCoordX(vec1.getCoordZ());
	    Vector3.setCoordY(vec2.getCoordZ());
	    Vector3.setCoordZ(this.target.getCoordZ());

	    Vector3Frac tmp = Gauss
		    .gaussElimination2(Vector1, Vector2, Vector3);
	    this.sol[0] = tmp.getCoordX().toString();
	    this.sol[1] = tmp.getCoordY().toString();
	    this.sol[2] = tmp.getCoordZ().toString();
	}

	fireTableStructureChanged();
    }

    protected void check(LOP lop) {
	if (isVisible())
	    MessageHandler.showNotImplemented();
    }
}