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
 * 29.01.2008 - Version 0.5.1
 * - Konstanten zur Extraktion der Spezialfälle hinzugefügt.
 * 27.01.2008 - Version 0.5
 * - isSpecialCase gelöscht, da Problem immer ein Spezialfall.
 * 25.01.2008 - Version 0.4.1
 * - Variablen für Spezialfälle geändert (jetzt Bitfeld)
 * 06.11.2007 - Version 0.4
 * - Die Lösungsflächen Funktionen erweitert
 * 10.10.2007 - Version 0.3.1
 * - Konstantent neu numeriert
 * 09.10.2007 - Version 0.3
 * - Konstanten von LOP nach LOPSolution verschoben
 * - Neue Methoden getAreas, addArea, clearAreas
 * 24.09.2007 - Version 0.2.1
 * - Weitere Überladung von Methode setValue hinzugefügt
 * 16.09.2007 - Version 0.2
 * - Interface neugestaltet
 * 23.08.2007 - Version 0.1
 *  - Datei hinzugefuegt
 */
package info.kriese.soPra.lop;

import java.util.List;

import info.kriese.soPra.math.Fractional;
import info.kriese.soPra.math.Vector3Frac;

/**
 * Interface, welches die Lösung eines LOP's repräsentiert.
 * 
 * @author Michael Kriese
 * @version 0.5.1
 * @since 23.08.2007
 * 
 */
public interface LOPSolution {

    /**
     * Konstante zum extrahieren des Spezialfalls "Bereich der optimalen Lösung
     * ist:".
     */
    static final int OPTIMAL_SOLUTION_AREA = 0xC;

    /**
     * Konstante für Spezialfall "Bereich der optimalen Lösung ist leer".
     */
    static final int OPTIMAL_SOLUTION_AREA_EMPTY = 0xC;

    /**
     * Konstante für Spezialfall "Bereich der optimalen Lösung ist mehr als ein
     * Punkt".
     */
    static final int OPTIMAL_SOLUTION_AREA_MULTIPLE = 0x8;

    /**
     * Konstante für Spezialfall "Bereich der optimalen Lösung ist ein Punkt".
     */
    static final int OPTIMAL_SOLUTION_AREA_POINT = 0x4;

    /**
     * Konstante zum extrahieren des Spezialfalls "Lösungsbereich des Problems
     * ist:".
     */
    static final int SOLUTION_AREA = 0x3;

    /**
     * Konstante für Spezialfall "Lösungsbereich des Problems ist leer".
     */
    static final int SOLUTION_AREA_EMPTY = 0x3;

    /**
     * Konstante für Spezialfall "Lösungsbereich des Problems ist beschränkt".
     */
    static final int SOLUTION_AREA_LIMITED = 0x1;

    /**
     * Konstante für Spezialfall "Lösungsbereich des Problems ist unbeschränkt".
     */
    static final int SOLUTION_AREA_UNLIMITED = 0x2;

    /**
     * Konstante zum extrahieren des Spezialfalls "Die Zielfunktion des Problems
     * ist:".
     */
    static final int TARGET_FUNCTION = 0x30;

    /**
     * Konstante für Spezialfall "Die Zielfunktion des Problems ist weder /
     * noch".
     */
    static final int TARGET_FUNCTION_EMPTY = 0x30;

    /**
     * Konstante für Spezialfall "Die Zielfunktion des Problems ist beschränkt".
     */
    static final int TARGET_FUNCTION_LIMITED = 0x10;

    /**
     * Konstante für Spezialfall "Die Zielfunktion des Problems ist
     * unbeschränkt".
     */
    static final int TARGET_FUNCTION_UNLIMITED = 0x20;

    /**
     * Fügt eine Fläche hinzu.
     * 
     * @param l1 -
     *                Erster Vektor, welcher die Fläche aufspannt
     * @param l2 -
     *                Zweiter Vektor, welcher die Fläche aufspannt
     * @param f1 -
     *                Anteil des ersten Vektors (Linearkombination)
     * @param f2 -
     *                Anteil des zweiten Vektors (Linearkombination)
     */
    void addArea(Vector3Frac l1, Vector3Frac l2, Fractional f1, Fractional f2);

    /**
     * Löscht alle Flächen.
     */
    void clearAreas();

    /**
     * Gibt die Anzahl der Flächen zurück.
     * 
     * Dies ist gleichzeitig die Anzahl der Lösungen.
     * 
     * @return Anzahl der Lösungsflächen
     */
    int countAreas();

    /**
     * Gibt eine Liste der Flächen zurück, die den Lösungsraum aufspannen.
     * 
     * @return Liste der Lösungsflächen.
     */
    List<LOPSolutionArea> getAreas();

    /**
     * Gibt das Problem zu dieser Lösung zurück.
     * 
     * @return Lineares Optimierungsproblem.
     */
    LOP getProblem();

    /**
     * Gibt die Spezialfälle des LOP zurück.
     * 
     * @return Spezialfälle als Bitfeld
     */
    int getSpecialCase();

    /**
     * Gibt den Zielwert des LOP zurück.
     * 
     * @return Zielwert
     */
    double getValue();

    /**
     * Gibt die Koordinaten des Schnittpunktes zurück.
     * 
     * @return Vektor, des Schnittpunktes.
     */
    Vector3Frac getVector();

    /**
     * Setzt die Spezialfälle.
     * 
     * @param sCase -
     *                Verknüpfte Spezialfälle.
     */
    void setSpecialCase(int sCase);

    /**
     * Setzt den Zielwert.
     * 
     * @param value -
     *                Zielwert als double
     */
    void setValue(double value);

    /**
     * Setzt den Zielwert.
     * 
     * @param value -
     *                Zielwert als Fractional
     */
    void setValue(Fractional value);

    /**
     * Setzt den Zielwert.
     * 
     * @param value -
     *                Zielwert als Vektor (Ganzer Punkt)
     */
    void setValue(Vector3Frac value);
}
