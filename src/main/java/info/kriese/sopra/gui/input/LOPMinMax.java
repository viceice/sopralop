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
 * 05.03.2008 - Version 0.3
 * - Komplett überarbeitet (es gibt nur noch 2 Instanzen)
 * 29.01.2008 - Version 0.2.0.1
 * - BugFix: Min & Max in get waren vertauscht
 * 29.01.2008 - Version 0.2
 * - Konstanten für min und max
 * 04.11.2007 - Version 0.1
 *  - Datei hinzugefuegt
 */
package info.kriese.sopra.gui.input;

import info.kriese.sopra.gui.lang.Lang;

/**
 * WrapperKlasse, dient dazu, dass die JTable den richtigen CellEditor benutzt.
 * 
 * @author Michael Kriese
 * @version 0.3
 * @since 04.11.2007
 * 
 */
public final class LOPMinMax {

    /**
     * Das LOP ist ein Maximierungsproblem.
     */
    public static final LOPMinMax MAX = new LOPMinMax(true);

    /**
     * Das LOP ist ein Minimierungsproblem.
     */
    public static final LOPMinMax MIN = new LOPMinMax(false);

    /**
     * Wandelt den Boolean in die Wrapperklasse um.
     * 
     * @param value -
     *                Minimum oder Maximum.
     * @return Die entsprechende Wrapperklasse.
     */
    public static LOPMinMax get(boolean value) {
	return value ? MAX : MIN;
    }

    /**
     * Privates Flag, das angibt, ob dieses Objekt ein Minimum oder Maximum
     * repräsentiert.
     */
    private final boolean value;

    /**
     * Privater Konstruktor, welcher das Objekt als Minimum oder Maximum
     * initialisiert.
     * 
     * @param value -
     *                Minimum ("FALSE") oder Maximum ("TRUE")
     */
    private LOPMinMax(boolean value) {
	this.value = value;
    }

    /**
     * Repräsentiert dieses Objekt ein Maximum?
     * 
     * @return "TRUE", wenn ja, sonst "FALSE".
     */
    public boolean isMax() {
	return this.value;
    }

    /**
     * Gibt eine Stringrepräsentation zur Anzeige in der Tabelle zurück.
     * 
     * @return Minimum oder Maximum als String
     */
    @Override
    public String toString() {
	return this.value ? Lang.getString("Strings.Max") : Lang
		.getString("Strings.Min");
    }
}
