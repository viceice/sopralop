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
 *  05.03.2008 - Version 0.3.3
 * - An Änderungen in LOPMinMax angepasst.
 * 01.02.2008 - Version 0.3.2.1
 * - BugFix: Fehler in der Lösungsberechnung behoben
 * 01.02.2008 - Version 0.3.2
 * - LOPSolutionWrapper in Fractionals geändert
 * - Lösungsüberprüfung überarbeitet
 * 29.01.2008 - Version 0.3.1
 * - Teilweise implementierung der Lösungsüberprüfung
 * - Eingabe der Lösung jetzt möglich
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
import info.kriese.soPra.math.Fractional;
import info.kriese.soPra.math.Vector3Frac;
import info.kriese.soPra.math.Vertex;
import info.kriese.soPra.math.impl.Vector3FracFactory;
import info.kriese.soPra.math.quickhull.QuickHull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

/**
 * Wandelt das duale LOP in ein von JTable lesbares Format um.
 * 
 * @author Peer Sterner
 * @version 0.3.3
 * @since 09.11.2007
 * 
 */
public final class DualLOPTableModel extends AbstractTableModel {

    /**
     * Dient zur Serialisierung.
     */
    private static final long serialVersionUID = 13L;

    /**
     * Titel der Tabellenköpfe.
     */
    private final String[] columnNames;

    /**
     * Lösungen des dualen Problems.
     */
    private final List<Vector3Frac> dualLOPSols;

    /**
     * Primales Problem ist Maximum oder Minimum.
     */
    private boolean max;

    /**
     * Spezialfall des dualen Problems.
     */
    private int sCase = 0;

    /**
     * Eigabefelder für Spezialfälle.
     */
    private SpecialCasesInput specialCases;

    /**
     * Tabelle der dieses Tabellenmodell hinzugefügt wird.
     */
    private JTable table;

    /**
     * Zielvektor des primalen Problems.
     */
    private Vector3Frac target;

    /**
     * Die durch den Nutzer eingegebenen Lösungswerte.
     */
    private final Fractional[] userValues;

    /**
     * Die Vektoren des primalen Problems.
     */
    private final List<Vector3Frac> vectors;

    /**
     * Konstruktor, welcher alle nötigen Variablen und Objekte initialisiert.
     */
    public DualLOPTableModel() {
	this.vectors = new ArrayList<Vector3Frac>();
	this.target = Vector3Frac.ZERO;

	this.userValues = new Fractional[3];

	this.dualLOPSols = new LinkedList<Vector3Frac>();

	this.columnNames = new String[5];
	this.columnNames[0] = " ";
	this.columnNames[1] = "<html><center><b>y<sub>1</sub></b></center></html>";
	this.columnNames[2] = "<html><center><b>y<sub>2</sub></b></center></html>";
	this.columnNames[3] = "<html><center><b>&ge; / &le; / = </b></center></html>";
	this.columnNames[4] = "<html><center><b>w</b></center></html>";
    }

    /**
     * Gibt den Klassentyp der jeweiligen Spalte zurück.
     * 
     * @return Klassentyp der Spalte.
     */
    @Override
    public Class<?> getColumnClass(int c) {
	return getValueAt(0, c).getClass();
    }

    /**
     * Gibt die Anzahl der Spalten zurück.
     * 
     * @return Anzahl der Spalten.
     */
    public int getColumnCount() {
	return 5;
    }

    /**
     * Gibt den Titel der Spalte zurück.
     * 
     * @param col -
     *                Spalte, für die der Titel zurückgegeben werden soll.
     * 
     * @return Titel der Spalte.
     */
    @Override
    public String getColumnName(int col) {
	return this.columnNames[col];
    }

    /**
     * Gibt die Anzahl der Zeilen zurück.
     * 
     * @return Anzahl der Zeilen.
     */
    public int getRowCount() {
	return this.vectors.size() + 4;
    }

    /**
     * Gibt den Wert der Tabellenzelle zurück.
     * 
     * @param row -
     *                Zeile der Zelle.
     * @param col -
     *                Spalte der Zelle.
     * @return Wert der Zelle.
     */
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
		    return this.userValues[0];
		else
		    return this.vectors.get(row - 2).getCoordX().toString();
	    case 2:
		if (row == 0)
		    return this.target.getCoordY();
		else if (row == getRowCount() - 1)
		    return this.userValues[1];
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
		    return LOPMinMax.get(!this.max);
		else if (row == getRowCount() - 1)
		    return this.userValues[2];
		else
		    return this.vectors.get(row - 2).getCoordZ();

	    default:
		return "";
	}
    }

    /**
     * legt die Editierbarkeit einzelner Zellen fest
     * 
     * @param row -
     *                Zeile der Zelle.
     * @param col -
     *                Spalte der Zelle.
     * @return "True", falls Zelle editierbar ist, sonst "False".
     */
    @Override
    public boolean isCellEditable(int row, int col) {
	if (getRowCount() - 1 == row && col != 0 && col != 3)
	    return true;
	else
	    return false;
    }

    /**
     * Sind die Werte dieses Modells sichbar?
     * 
     * @return "True", wenn ja, sonst "False".
     */
    public boolean isVisible() {
	return this.table != null && this.table.getModel() == this;
    }

    /**
     * Setzt den LOPEditor, um auf Interaktion des Users reagieren zu können.
     * 
     * @param editor -
     *                Editor, bei dem die Callbacks registriert werden.
     */
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

    /**
     * Registriert das Eingabeelement für Spezialfälle.
     * 
     * @param sci -
     *                Eingabeelement für Spezialfälle.
     */
    public void setSpecialCasesComponent(SpecialCasesInput sci) {
	this.specialCases = sci;
    }

    /**
     * Aktiviert dieses Modell.
     * 
     * @param table -
     *                Tabelle bei der dieses Modell aktiviert werden soll.
     */
    public void setTable(JTable table) {
	this.table = table;
	table.setModel(this);
	this.userValues[0] = null;
	this.userValues[1] = null;
	this.userValues[2] = null;
	fireTableStructureChanged();
    }

    /**
     * Setzt den Wert einer Zelle.
     * 
     * @param value -
     *                Wert, welcher gesetzt werden soll.
     * @param row -
     *                Zeile der Zelle.
     * @param col -
     *                Spalte der Zelle.
     */
    @Override
    public void setValueAt(Object value, int row, int col) {

	switch (col) {
	    case 1:
		this.userValues[0] = (Fractional) value;
		break;

	    case 2:
		this.userValues[1] = (Fractional) value;
		break;

	    case 4:
		this.userValues[2] = (Fractional) value;
		break;
	}
	fireTableCellUpdated(row, col);
    }

    /**
     * Aktualisiert das Tabellenmodell mit dem LOP.
     * 
     * @param lop -
     *                LOP, welches zum aktualisieren verwendet werden soll.
     */
    private void update(LOP lop) {
	this.vectors.clear();
	for (Vector3Frac vec : lop.getVectors())
	    this.vectors.add(vec.clone());
	this.target = lop.getTarget().clone();
	this.max = lop.isMaximum();

	this.dualLOPSols.clear();
	this.sCase = 0;

	LOPSolution solution = lop.getSolution();

	switch (solution.getSpecialCase() & LOPSolution.TARGET_FUNCTION) {
	    case LOPSolution.TARGET_FUNCTION_EMPTY:

		List<Vector3Frac> list = new LinkedList<Vector3Frac>(
			this.vectors);
		list.add(Vector3FracFactory.getInstance(0, 0, 1));

		QuickHull hull = new QuickHull();
		hull.build(list);
		list.clear();

		for (Vertex vtx : hull.getVerticesList()) {
		    if (!list.contains(vtx.p2))
			list.add(vtx.p2);
		    if (!list.contains(vtx.p3))
			list.add(vtx.p3);
		}

		if (list.contains(Vector3FracFactory.getInstance(0, 0, 1)))
		    this.sCase = LOPSolution.TARGET_FUNCTION_EMPTY
			    | LOPSolution.OPTIMAL_SOLUTION_AREA_EMPTY;
		else
		    this.sCase = LOPSolution.TARGET_FUNCTION_UNLIMITED
			    | LOPSolution.OPTIMAL_SOLUTION_AREA_EMPTY;
		break;

	    case LOPSolution.TARGET_FUNCTION_UNLIMITED:
		this.sCase = LOPSolution.TARGET_FUNCTION_EMPTY
			| LOPSolution.OPTIMAL_SOLUTION_AREA_EMPTY;
		break;

	    default:

		if (SettingsFactory.getInstance().isDebug()) {
		    System.out
			    .println("--------------------------------------------------------");
		    System.out
			    .println("Searching for solutions of the dual problem!");
		}
		for (LOPSolutionArea area : solution.getAreas()) {

		    Vector3Frac vec = area.getL1().cross(area.getL2());

		    if (SettingsFactory.getInstance().isDebug())
			System.out.print(area + "\t_|_\t");

		    Fractional z = vec.getCoordZ();

		    if (z.is(Fractional.GEQUAL_ZERO))
			z = z.mul(-1);

		    vec.setCoordX(vec.getCoordX().div(z));
		    vec.setCoordY(vec.getCoordY().div(z));
		    vec.setCoordZ(vec.getCoordZ().div(z));

		    z = vec.getCoordX().mul(this.target.getCoordX()).add(
			    vec.getCoordY().mul(this.target.getCoordY()));

		    if (SettingsFactory.getInstance().isDebug())
			System.out.println(vec + "\t|\t" + z);

		    if (z.equals(solution.getVector().getCoordZ()))
			if (!this.dualLOPSols.contains(vec))
			    this.dualLOPSols.add(vec);

		}

		if (this.dualLOPSols.size() > 1)
		    this.sCase = LOPSolution.TARGET_FUNCTION_LIMITED
			    | LOPSolution.OPTIMAL_SOLUTION_AREA_MULTIPLE;
		else
		    this.sCase = LOPSolution.OPTIMAL_SOLUTION_AREA_POINT
			    | LOPSolution.TARGET_FUNCTION_LIMITED;

		if (SettingsFactory.getInstance().isDebug())
		    System.out
			    .println("--------------------------------------------------------");
		break;
	}

	fireTableStructureChanged();
    }

    /**
     * Überprüft die vom Nutzer eingegebenen Werte.
     * 
     * @param lop -
     *                LOP, mit dem die Werte überprüft werden soll.
     */
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

		// Benutzer darf nichts eingegeben haben, da Lösung nicht
		// existiert oder unbeschränkt ist
		if (this.userValues[0] == null && this.userValues[1] == null
			&& this.userValues[2] == null)
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

		// Benutzer darf nichts eingegeben haben
		if (this.userValues[0] == null && this.userValues[1] == null
			&& this.userValues[2] == null)
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

	    default:
		// Überprüfe ob Benutzer Werte eingegeben hat
		if (this.userValues[0] == null || this.userValues[1] == null
			|| this.userValues[2] == null) {
		    if (SettingsFactory.getInstance().isDebug())
			System.out
				.println("Wrong user solution! Shouldn't be empty.");

		    MessageHandler.showError(
			    Lang.getString("Strings.Solution"), Lang
				    .getString("Strings.IncorrectSolution"));
		    return;
		}

		// Überprüfe ob Benutzer-Zielwert stimmt
		if (this.userValues[2].equals(lop.getSolution().getValue())) {
		    if (SettingsFactory.getInstance().isDebug())
			System.out
				.println("Wrong user solution! Target should be: "
					+ lop.getSolution().getValue());

		    MessageHandler.showError(
			    Lang.getString("Strings.Solution"), Lang
				    .getString("Strings.IncorrectSolution"));
		    return;
		}

		// Überprüfe ob Benutzer eine richtige BasisLösung eingegeben
		// hat
		for (Vector3Frac vec : this.dualLOPSols) {
		    System.out.println(vec + "\t" + this.userValues[0] + " "
			    + this.userValues[1]);
		    if (vec.getCoordX().equals(this.userValues[0])
			    && vec.getCoordY().equals(this.userValues[1])) {
			MessageHandler.showInfo(Lang
				.getString("Strings.Solution"), Lang
				.getString("Strings.CorrectSolution"));
			return;
		    }
		}

		if (SettingsFactory.getInstance().isDebug())
		    System.out.println("Wrong user solution! Wrong values.");

		MessageHandler.showError(Lang.getString("Strings.Solution"),
			Lang.getString("Strings.IncorrectSolution"));
		return;
	}
    }
}