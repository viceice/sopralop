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
 * 09.10.2007 - Version 0.3.2
 * - Konstante MAX_VECTORS hinzugefügt
 * 07.10.2007 - Version 0.3.1
 * - Konstanten um umbenannt für besseres Verständnis
 * 03.10.2007 - Version 0.3
 * - Konstanten "UNLIMITED_MIN", "UNLIMITED_MAX" und "MORE_THAN_ONE" hinzugefügt (für die Spezialfälle)
 * - showDualProblem hinzugefügt
 * - showPrimalProblem hinzugefügt
 * 24.09.2007 - Versionm 0.2.2
 * - Konstante "SIMPLE" hizugefügt
 * - Mindestens 3 Vektoren müssen sein
 * 17.09.2007 - Version 0.2.1
 * - Konstante "NO_SOLUTION" hinzugefügt
 * 11.09.2007 - Version 0.2
 *  - createProblem geloescht, da nicht noetig
 *  - showSolution hinzugefuegt
 *  - add und remove Listener Methoden umbenannt
 * 23.08.2007 - Version 0.1
 *  - Datei hinzugefuegt
 */
package info.kriese.soPra.lop;

import info.kriese.soPra.math.Vector3Frac;

import java.util.List;

/**
 * 
 * @author Michael Kriese
 * @version 0.3.2
 * @since 23.08.2007
 * 
 */
public interface LOP {

    static final int MAX_VECTORS = 10;
    static final int MIN_VECTORS = 3;

    void addProblemListener(LOPListener listener);

    String[] getOperators();

    LOPSolution getSolution();

    Vector3Frac getTarget();

    List<Vector3Frac> getVectors();

    boolean isMaximum();

    boolean isMinimum();

    boolean isSolved();

    void problemChanged();

    void problemSolved();

    void removeProblemListener(LOPListener listener);

    void setMaximum(boolean max);

    void setMinimum(boolean min);

    void setOperators(String[] operators);

    void setTarget(Vector3Frac target);

    void setVectors(List<Vector3Frac> vectors);

    void showDualProblem();

    void showPrimalProblem();

    void showSolution();
}
