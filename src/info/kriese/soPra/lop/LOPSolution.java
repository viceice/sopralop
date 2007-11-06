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
 * 
 * @author Michael Kriese
 * @version 0.4
 * @since 23.08.2007
 * 
 */
public interface LOPSolution {

    static final int MORE_THAN_ONE_SOLUTION = 2;
    static final int NO_SOLUTION = 1;
    static final int SIMPLE = 0;
    static final int UNLIMITED = 3;

    /**
     * Fügt eine Fläche hinzu.
     * 
     * @param l1
     * @param l2
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
     * @return
     */
    int countAreas();

    /**
     * Gibt eine Liste der Flächen zurück, die den Lösungsraum aufspannen.
     * 
     * @return
     */
    List<LOPSolutionArea> getAreas();

    LOP getProblem();

    int getSpecialCase();

    double getValue();

    Vector3Frac getVector();

    boolean isSpecialCase();

    void setSpecialCase(int sCase);

    void setValue(double value);

    void setValue(Fractional value);

    void setValue(Vector3Frac value);
}
