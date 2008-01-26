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
 * 26.01.2008 - Version 0.3.3
 * - Einige überflüssige Methoden gelöscht.
 * 09.10.2007 - Version 0.3.2
 * - Konstante MAX_VECTORS hinzugefügt
 * 07.10.2007 - Version 0.3.1
 * - Konstanten um umbenannt für besseres Verständnis
 * 03.10.2007 - Version 0.3
 * - Konstanten "UNLIMITED_MIN", "UNLIMITED_MAX" und "MORE_THAN_ONE"
 *    hinzugefügt (für die Spezialfälle)
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
 * Diese Klasse kapselt ein lineares Optimierungsproblem der 2. geometrischen
 * Interpretation.
 * 
 * @author Michael Kriese
 * @version 0.4
 * @since 23.08.2007
 * 
 */
public interface LOP {

    /**
     * Die maximale Anzahl an Variablen, die das Problem aufnehmen kann.
     */
    static final int MAX_VECTORS = 10;

    /**
     * Die minimale Anzahl an Variablen, die das Problem aufnehmen kann.
     */
    static final int MIN_VECTORS = 3;

    /**
     * Hiermit kann man Listener hinzufügen, die informiert werden, sobald sich
     * an dem Problem etwas ändert.
     * 
     * @param listener -
     *                Listener, welcher infomiert werden soll.
     */
    void addProblemListener(LOPListener listener);

    /**
     * Gibt die Lösung zu diesem Problem zurück.
     * 
     * @return Lösung des Problems.
     */
    LOPSolution getSolution();

    /**
     * Gibt den Zielvektor zurück.
     * 
     * @return Zielvektor
     */
    Vector3Frac getTarget();

    /**
     * Gibt die Variablen als Vektor zurück.
     * 
     * @return Liste der Variablen.
     */
    List<Vector3Frac> getVectors();

    /**
     * Gibt zurück, ob dieses Problem ein Maximierungsproblem ist.
     * 
     * @return true "falls" es ein Maximierungsproblem ist, sonst "false"
     */
    boolean isMaximum();

    /**
     * Gibt zurück, ob dieses Problem ein Minimierungsproblem ist.
     * 
     * Dies ist ein Wrapper für !isMaximum().
     * 
     * @return true "falls" es ein Minimierungsproblem ist, sonst "false"
     */
    boolean isMinimum();

    /**
     * Muss aufgerufen werden, wenn das Problem gelöst wurde.
     * 
     * Diese Methode wird vom LOPEditor aufgerufen.
     */
    void problemSolved();

    /**
     * Hier kann man seinen Listener wieder entfernen.
     * 
     * @param listener -
     *                Listener, welcher entfernt werden soll
     */
    void removeProblemListener(LOPListener listener);

    /**
     * Hiermit setzt man, ob es ein Maximierungsproblem ist.
     * 
     * @param max -
     *                Boolean, der angibt, ob es ein Maximierungsproblem ist.
     */
    void setMaximum(boolean max);

    /**
     * Hiermit setzt man, ob es ein Minimierungsproblem ist.
     * 
     * Dies ist ein Wrapper für setMaximun(!min).
     * 
     * @param max -
     *                Boolean, der angibt, ob es ein Minimierungsproblem ist.
     */
    void setMinimum(boolean min);

    /**
     * Setzt den Zielvektor.
     * 
     * @param target -
     *                Zielvektor, welcher gesetzt werden soll.
     * @throws IllegalArgumentException -
     *                 Falls Zielvektor null.
     */
    void setTarget(Vector3Frac target);

    /**
     * Setzt die Liste der Variablen.
     * 
     * @param vectors -
     *                Liste von Vektoren, welche die Variablen repräsentieren.
     * @throws IllegalArgumentException -
     *                 Falls die Anzahl der Vektoren außerhalb des zulässigen
     *                 Bereichs.
     */
    void setVectors(List<Vector3Frac> vectors);

    /**
     * Fordert die Listener auf, das duale Problem anzuzeigen.
     */
    void showDualProblem();

    /**
     * Fordert die Listener auf, wieder das primale Problem anzuzeigen.
     */
    void showPrimalProblem();
}
