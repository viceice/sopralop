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
 * 29.01.2008 - Version 0.2
 * - Konstanten für min und max
 * 04.11.2007 - Version 0.1
 *  - Datei hinzugefuegt
 */
package info.kriese.soPra.gui.input;

/**
 * WrapperKlasse, dient dazu, dass die JTable den richtigen CellEditor benutzt.
 * 
 * @author Michael Kriese
 * @version 0.2
 * @since 04.11.2007
 * 
 */
public class LOPMinMax {

    public static final String MAX = "max!";
    public static final String MIN = "min!";

    public static LOPMinMax get(boolean value) {
	return new LOPMinMax(value ? MIN : MAX);
    }

    public static LOPMinMax get(String value) {
	return new LOPMinMax(value);
    }

    private final String value;

    private LOPMinMax(String value) {
	this.value = value;
    }

    public boolean isMax() {
	return MAX.equals(this.value);
    }

    @Override
    public String toString() {
	return this.value;
    }
}
