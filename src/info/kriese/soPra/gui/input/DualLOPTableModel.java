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
 * 29.01.2008 - Version 0.3.1
 * - Teilweise implementierung der Lösungsüberprüfung
 * - BugFix: Nullpointer behoben
 * 28.01.2008 - Version 0.3
 * - Vorbereitung für Spezialfall überprüfung
 * 28.01.2008 - Version 0.2.1.2
 * - HTML-Zeichen für größer/gleich und kleiner/gleich eingefügt
 * 27.01.2008 - Version 0.2.1.1
 * - BugFix: Exception bei Spezialfääle der Lösung in Methode update()
 * 25.01.2008 - Version 0.2.1
 * - Variablennamen für Spezialfälle angepasst
 * - Fallprüfung für Spezialfälle erweitert
 * 25.01.2008 - Version 0.2
 * - Vorbereitung zur Lösungsüberprüfung
 * 10.01.2008 - Version 0.1.1
 * - Tabellenkopfbeschriftung geändert (y1, y2, w)
 * 09.11.2007 - Version 0.1
 * - Kopie von LOPTableModel
 */
package info.kriese.soPra.gui.input;

import info.kriese.soPra.gui.MessageHandler;
import info.kriese.soPra.gui.lang.Lang;
import info.kriese.soPra.io.impl.SettingsFactory;
import info.kriese.soPra.lop.LOP;
import info.kriese.soPra.lop.LOPEditor;
import info.kriese.soPra.lop.LOPEditorAdapter;
import info.kriese.soPra.lop.LOPSolution;
import info.kriese.soPra.lop.LOPSolutionArea;
import info.kriese.soPra.math.Gauss;
import info.kriese.soPra.math.Vector3Frac;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

/**
 * Wandelt das duale LOP in ein von JTable lesbares Format um.
 * 
 * @author Peer Sterner
 * @version 0.3
 * @since 09.11.2007
 * 
 */
public final class DualLOPTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 13L;

    private final String[] columnNames;

    private Vector3Frac lopSol;

    private boolean max;

    private int sCase = 0;

    private final LOPSolutionWrapper sol[];

    private SpecialCasesInput specialCases;

    private JTable table;

    private Vector3Frac target;

    private final List<Vector3Frac> vectors;

    public DualLOPTableModel() {
	this.vectors = new ArrayList<Vector3Frac>();
	this.target = Vector3Frac.ZERO;
	this.lopSol = Vector3Frac.ZERO;
	this.sol = new LOPSolutionWrapper[3];

	this.sol[0] = LOPSolutionWrapper.getInstance();
	this.sol[1] = LOPSolutionWrapper.getInstance();
	this.sol[2] = LOPSolutionWrapper.getInstance();

	this.columnNames = new String[5];
	this.columnNames[0] = " ";
	this.columnNames[1] = "<html><center><b>y<sub>1</sub></b></center></html>";
	this.columnNames[2] = "<html><center><b>y<sub>2</sub></b></center></html>";
	this.columnNames[3] = "<html><center><b>&ge; / &le; / = </b></center></html>";
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
		    return this.sol[0].getValue();
		else
		    return this.vectors.get(row - 2).getCoordX().toString();
	    case 2:
		if (row == 0)
		    return this.target.getCoordY();
		else if (row == getRowCount() - 1)
		    return this.sol[1].getValue();
		else
		    return this.vectors.get(row - 2).getCoordY().toString();
	    case 3:
		if (row == 0 || row == getRowCount() - 1
			|| row == getRowCount() - 1)
		    return "=";
		else
		    return (this.max ? "<html>&ge;</html>"
			    : "<html>&le;</html>");
	    case 4:
		if (row == 0)
		    return (this.max ? LOPMinMax.MIN : LOPMinMax.MAX);
		else if (row == getRowCount() - 1)
		    return this.sol[2].getValue();
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
    }

    public void setSpecialCasesComponent(SpecialCasesInput csci) {
	this.specialCases = csci;
    }

    public void setTable(JTable table) {
	this.table = table;
	table.setModel(this);
	this.sol[0] = LOPSolutionWrapper.getInstance();
	this.sol[1] = LOPSolutionWrapper.getInstance();
	this.sol[2] = LOPSolutionWrapper.getInstance();
	fireTableStructureChanged();
    }

    private void update(LOP lop) {
	this.vectors.clear();
	for (Vector3Frac vec : lop.getVectors())
	    this.vectors.add(vec.clone());
	this.target = lop.getTarget().clone();
	this.max = lop.isMaximum();

	this.sCase = 0;

	// TODO: Prüfen, welche Fälle auftreten können /behandelt werden müssen
	// (gibt ja jetzt noch mehr Permutationen...)

	// Gegeben sei ein Paar aus primalem und dualem LP. Dann trit immer
	// einer der folgenden Falle zu:
	// 1. Beide Probleme besitzen eine optimale Lösung.
	// 2. Das primale Problem ist unbeschränkt, das duale unlösbar.
	// 3. Das primale Problem ist unlösbar, das duale unbeschränkt.
	// 4. Beide Probleme sind unlosbar.

	LOPSolution solution = lop.getSolution();

	switch (solution.getSpecialCase() & LOPSolution.TARGET_FUNCTION) {
	    case LOPSolution.TARGET_FUNCTION_EMPTY:
		this.lopSol = Vector3Frac.ZERO;
		// TODO : Fall 2 oder 4?
		this.sCase = LOPSolution.TARGET_FUNCTION_UNLIMITED
			| LOPSolution.OPTIMAL_SOLUTION_AREA_EMPTY
			| LOPSolution.SOLUTION_AREA_UNLIMITED;
		break;
	    case LOPSolution.TARGET_FUNCTION_UNLIMITED:
		this.sCase = LOPSolution.TARGET_FUNCTION_EMPTY
			| LOPSolution.OPTIMAL_SOLUTION_AREA_EMPTY
			| LOPSolution.SOLUTION_AREA_EMPTY;
		break;
	    default:
		// TODO: Was sind korrekte Lösungen?
		for (LOPSolutionArea area : solution.getAreas()) {
		    Vector3Frac vec1 = area.getL1();
		    Vector3Frac vec2 = area.getL2();

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

		    this.lopSol = Gauss.gaussElimination2(Vector1, Vector2,
			    Vector3);
		    System.out.println("Possible solution: " + this.lopSol);
		}
		break;
	}

	fireTableStructureChanged();
    }

    protected void check(LOP lop) {
	if (!isVisible())
	    return;

	// Eingegebener Spezialfall muss stimmen
	if (this.sCase != this.specialCases.getSpecialCase()) {
	    if (SettingsFactory.getInstance().isDebug())
		System.out.println("Wrong user special case!");

	    MessageHandler.showError(Lang.getString("Strings.Solution"), Lang
		    .getString("Strings.IncorrectSolution"));
	    return;
	}

	switch (this.sCase & LOPSolution.TARGET_FUNCTION) {

	    case LOPSolution.TARGET_FUNCTION_EMPTY:
		// Es gibt keine Lösung

		if (this.sol[2].getValue() instanceof LOPEmpty)
		    MessageHandler.showInfo(Lang.getString("Strings.Solution"),
			    Lang.getString("Strings.CorrectSolution"));
		else {
		    if (SettingsFactory.getInstance().isDebug())
			System.out
				.println("Wrong user solution! Should be empty.");

		    MessageHandler.showError(
			    Lang.getString("Strings.Solution"), Lang
				    .getString("Strings.IncorrectSolution"));
		}
		return;

	    case LOPSolution.TARGET_FUNCTION_UNLIMITED:
		// Lösung ist unendlich, bzw -unendlich bei Minimum gesucht

		if (this.sol[2].getValue() instanceof LOPInfinity)
		    MessageHandler.showInfo(Lang.getString("Strings.Solution"),
			    Lang.getString("Strings.CorrectSolution"));
		else {
		    if (SettingsFactory.getInstance().isDebug())
			System.out
				.println("Wrong user solution! Should be infinity.");

		    MessageHandler.showError(
			    Lang.getString("Strings.Solution"), Lang
				    .getString("Strings.IncorrectSolution"));
		}
		return;
	    default:
		// TODO: implementieren
		// return;
	}

	MessageHandler.showNotImplemented();
    }
}