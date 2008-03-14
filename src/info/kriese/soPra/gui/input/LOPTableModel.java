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
 * 05.03.2008 - Version 0.4.8
 * - An Änderungen in LOPMinMax angepasst.
 * 01.02.2008 - Version 0.4.7
 * - LOPSolutionWrapper in Fractionals geändert
 * - Lösungüberprüfung überarbeitet
 * 29.01.2008 - Version 0.4.6
 * - Eingabe der Lösungen toleranter gemacht
 * 29.01.2008 - Version 0.4.5
 * - Lösungsüberprüfung überarbeitet / vereinfacht
 * - An LOPMinMax angepasst
 * 28.01.2008 - Version 0.4.4
 * - Lösungsüberprüfung komplett.
 * - Xi-Lösungseingabefelder nur noch für Fractional
 * - NotExistent in Empty umbenannt
 * 27.01.2008 - Version 0.4.3
 * - Beschriftung der Input-Panels jetzt auch dynamisch über die Sprachdateien
 * 26.01.2008 - Version 0.4.2
 * - Operatoren-Handling entfernt
 * 25.01.2008 - Version 0.4.1
 * - Variablennamen für Spezialfälle angepasst
 * 25.01.2007 - Version 0.4
 * - Vorbereitung auf erweiterte Lösungsüberprüfung
 * 10.01.2008 - Version 0.3.3
 * - Lösungscheck gibt jetzt eine Fehlermeldung, wenn das Problem nicht 
 *    übernommen wurde.
 * - Relationszeichen nicht mehr editierbar
 * 19.12.2007 - Version 0.3.2
 * - BugFix: Lösungcheck gab immer falsch zurück, da der Typ-Vergleich
 *    fehlerhaft implementiert war 
 * 04.12.2007 - Version 0.3.1
 * - An Lösungseditor angepasst, um unendlich und nicht exitent eingeben zu
 *   können.
 * - Lösungcheck angepasst
 * 03.12.2007 - Version 0.3
 * - Lösungcheck implementiert
 * - isEdited & setEdited an LOPEditor weitergeleitet
 * - An ErrorHandler angepasst
 * 09.11.2007 - Version 0.2.3
 * - Nachdem das Model aktiviert wurde wird structureChanged aufgerufen,
 *    damit sich die Tabelle neuzeichnet
 * 04.11.2007 - Version 0.2.2
 * - An neue CellEditoren angepasst
 * 03.10.2007 - Version 0.2.1
 * - An neuen CellRenderer angepasst
 * 01.11.2007 - Version 0.2
 * - An neuen ActionHandler angepasst
 * 30.10.2007 - Version 0.1
 * - Aus InputPanel ausgelagert
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
import info.kriese.soPra.math.Fractional;
import info.kriese.soPra.math.Vector3Frac;
import info.kriese.soPra.math.impl.FractionalFactory;
import info.kriese.soPra.math.impl.Vector3FracFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

/**
 * Wandelt das LOP in ein von JTable lesbares Format um.
 * 
 * @author Peer Sterner
 * @version 0.4.8
 * @since 01.11.2007
 * 
 */
public final class LOPTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 13L;

    private final Vector<String> columnNames;

    private LOPEditor editor;

    private boolean max;

    private Fractional sol;

    private SpecialCasesInput specialCases;

    private JTable table;
    private Vector3Frac target;

    private final List<Fractional> values;

    private final List<Vector3Frac> vectors;

    public LOPTableModel() {
	this.columnNames = new Vector<String>();
	this.vectors = new ArrayList<Vector3Frac>();
	this.values = new ArrayList<Fractional>();
	this.sol = null;
	this.editor = null;

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
	    if (row != 1 || row != 4)
		return "=";
	    return "";
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
		    return this.sol;
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
		return this.values.get(col - 1);
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
	if ((col < 1) || (col == vecs + 1) || row == 1 || row == 4)
	    return false;
	else
	    return true;
    }

    public boolean isEdited() {
	if (this.editor != null)
	    return this.editor.isEdited();
	else
	    return false;
    }

    public boolean isVisible() {
	return this.table != null && this.table.getModel() == this;
    }

    public void setEdited(boolean value) {
	if (this.editor != null)
	    this.editor.setEdited(value);
    }

    public void setEditor(LOPEditor editor) {
	this.editor = editor;
	editor.addListener(new LOPEditorAdapter() {
	    @Override
	    public void addVariable(LOP lop) {
		LOPTableModel.this.addColumn();
	    }

	    @Override
	    public void check(LOP lop) {
		LOPTableModel.this.check(lop);
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

    public void setSpecialCasesComponent(SpecialCasesInput sci) {
	this.specialCases = sci;
    }

    /**
     * Aktiviert dieses Modell in der Tabelle.
     * 
     * @param table -
     *                Tablle, in der dieses Modell aktiviert werden soll.
     */
    public void setTable(JTable table) {
	this.table = table;
	table.setModel(this);

	for (int i = 0; i < this.values.size(); i++)
	    this.values.set(i, null);

	this.sol = null;
	fireTableRowsUpdated(4, 4);
    }

    /**
     * Ersetzt den internen Wert durch den vom Nutzer eingegeben.
     * 
     * @param value -
     *                Eingegebener Wert
     * @param row -
     *                Zeile, in die Eingetragen werden soll.
     * @param col -
     *                Spalte, in die Eingetragen werden soll.
     */
    @Override
    public void setValueAt(Object value, int row, int col) {
	int num = this.vectors.size();

	if (col == num + 2)
	    switch (row) {
		case 0:
		    boolean bool = ((LOPMinMax) value).isMax();
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

	    if (row == 5)
		this.values.set(col - 1, (Fractional) value);
	    else {

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
		}
	    }
	}

	if (row != 5)
	    setEdited(true);

	fireTableCellUpdated(row, col);
    }

    /**
     * Aktualisiert die Tabelle mit dem angegeben Problem.
     * 
     * @param lop -
     *                Problem, welches zum Aktualisieren benutzt werden soll.
     */
    public void update(LOP lop) {
	this.vectors.clear();
	this.values.clear();
	for (Vector3Frac vec : lop.getVectors()) {
	    this.vectors.add(vec.clone());
	    this.values.add(null);
	}
	this.target = lop.getTarget().clone();
	this.max = lop.isMaximum();

	this.sol = null;

	setEdited(false);
	setColumnCount();
    }

    /**
     * Erweitert die Tabelle um eine weitere Variable Xi.
     */
    private void addColumn() {
	int num = this.vectors.size();

	if (num == LOP.MAX_VECTORS)
	    return;

	this.vectors.add(Vector3FracFactory.getInstance());
	this.values.add(null);
	num++;
	this.columnNames.insertElementAt("<html><center><b>x<sub>" + num
		+ "</sub></b></center></html>", num);
	setEdited(true);
	fireTableStructureChanged();
    }

    /**
     * Überprüft die eingegebene Lösung.
     * 
     * @param lop -
     *                LOP, mit dem die Lösung überprüft werden soll.
     */
    private void check(LOP lop) {

	if (!isVisible())
	    return;

	if (isEdited()) {
	    MessageHandler.showError(Lang.getString("Strings.Solution"), Lang
		    .getString("Errors.TakeNewValues"));
	    return;
	}

	int idx1, idx2, vals = 0, sCase = lop.getSolution().getSpecialCase();

	// Eingegebener Spezialfall muss stimmen
	if (sCase != this.specialCases.getSpecialCase()) {
	    if (SettingsFactory.getInstance().isDebug())
		System.out.println("Wrong user special case!");

	    MessageHandler.showError(Lang.getString("Strings.Solution"), Lang
		    .getString("Strings.IncorrectSolution"));
	    return;
	}

	switch (sCase & LOPSolution.TARGET_FUNCTION) {

	    case LOPSolution.TARGET_FUNCTION_EMPTY:
		// Es gibt keine Lösung

		for (Fractional sol : this.values)
		    if (sol != null) {
			if (SettingsFactory.getInstance().isDebug())
			    System.out
				    .println("Wrong user solution! Should be empty.");

			MessageHandler.showError(Lang
				.getString("Strings.Solution"), Lang
				.getString("Strings.IncorrectSolution"));
			return;
		    }

		if (this.sol == null)
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

		for (Fractional sol : this.values)
		    if (sol != null) {
			if (SettingsFactory.getInstance().isDebug())
			    System.out
				    .println("Wrong user solution! Should be empty.");

			MessageHandler.showError(Lang
				.getString("Strings.Solution"), Lang
				.getString("Strings.IncorrectSolution"));
			return;
		    }

		if (this.sol == null)
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
		// Überprüfe normale Lösung

		for (Fractional sol : this.values)
		    if (sol == null) {
			if (SettingsFactory.getInstance().isDebug())
			    System.out
				    .println("Wrong user solution! Shouldn't be empty.");

			MessageHandler.showError(Lang
				.getString("Strings.Solution"), Lang
				.getString("Strings.IncorrectSolution"));
			return;
		    } else if (!sol.isZero())
			vals++;

		if (vals > 2) {
		    MessageHandler.showError(
			    Lang.getString("Strings.Solution"), Lang
				    .getString("Strings.MoreThan2SolValues"));
		    return;
		}

		if (!this.sol.equals(lop.getSolution().getVector().getCoordZ())) {
		    if (SettingsFactory.getInstance().isDebug())
			System.out.println("Wrong user solution! Z should be "
				+ lop.getSolution().getValue());

		    MessageHandler.showError(
			    Lang.getString("Strings.Solution"), Lang
				    .getString("Strings.IncorrectSolution"));
		    return;
		}

		for (LOPSolutionArea area : lop.getSolution().getAreas()) {
		    idx1 = lop.getVectors().indexOf(area.getL1());
		    idx2 = lop.getVectors().indexOf(area.getL2());

		    if (area.getL1Amount().equals(this.values.get(idx1))
			    && area.getL2Amount().equals(this.values.get(idx2))) {
			MessageHandler.showInfo(Lang
				.getString("Strings.Solution"), Lang
				.getString("Strings.CorrectSolution"));
			return;
		    }
		}

		if (SettingsFactory.getInstance().isDebug())
		    System.out
			    .println("Wrong user solution! One or more Xi are incorrect.");

		MessageHandler.showError(Lang.getString("Strings.Solution"),
			Lang.getString("Strings.IncorrectSolution"));
	}
    }

    /**
     * Löscht die Tabelle und stellt das Standard-Problem wieder her.
     */
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
	    this.values.set(i, null);
	}

	this.target.getCoordX().setNumerator(0);
	this.target.getCoordY().setNumerator(0);

	this.max = true;

	this.sol = null;

	setEdited(true);
	fireTableDataChanged();
    }

    /**
     * Reduziert die Tabelle um eine Variable xi;
     */
    private void removeColumn() {
	int num = this.vectors.size();

	if (num == LOP.MIN_VECTORS)
	    return;

	this.vectors.remove(num - 1);
	this.columnNames.remove(num);
	this.values.remove(num - 1);
	num--;
	setEdited(true);
	fireTableStructureChanged();
    }

    /**
     * Übernimmt die Änderungen des Nutzers in das Problem.
     * 
     * Danach wird das Problem automatisch gelöst.
     * 
     * @param lop -
     *                Das LOP, in das die Änderungen übernommen werden sollen.
     * @return Gibt "true" zurück, wenn die Übernahme erfolgreich war, sonst
     *         "false"
     */
    private boolean save(LOP lop) {
	int cnt = 0;
	for (Vector3Frac vec : this.vectors)
	    if (!vec.equals(Vector3Frac.ZERO))
		cnt++;
	if (cnt < this.vectors.size()) {
	    MessageHandler.showError(Lang.getString("Strings.Error"), Lang
		    .getString("Errors.NoZeroVectors"));
	    return false;
	}

	lop.setVectors(this.vectors);
	lop.setTarget(this.target);
	lop.setMaximum(this.max);
	setEdited(false);
	fireTableCellUpdated(0, 0);
	return true;
    }

    /**
     * Erstellt den Tabellenkopf.
     */
    private void setColumnCount() {
	this.columnNames.clear();
	this.columnNames.add(" ");
	for (int i = 1; i <= this.vectors.size(); i++)
	    this.columnNames.add("<html><center><b>x<sub>" + i
		    + "</sub></b></center></html>");
	this.columnNames.add("<html><center><b>=</b></center></html>");
	this.columnNames.add("<html><center><b>z</b></center></html>");
	fireTableStructureChanged();
    }
}