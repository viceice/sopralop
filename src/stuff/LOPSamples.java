/**
 * @version		$Id$
 * @copyright	(c)2007 Michael Kriese & Peer Sterner
 * 
 * This file is part of SoPraLOP Project.
 *
 *  SoPraLOP Project is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License.
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
 * 11.05.2007 - Version 0.1
 * - Datei hinzugefuegt
 */
package stuff;

import info.kriese.sopra.math.Vector3Frac;
import info.kriese.sopra.math.impl.Vector3FracFactory;

import java.util.LinkedList;

/**
 * @author Michael Kriese
 * @version 0.1
 * @since 11.05.2007
 * 
 */
public final class LOPSamples {

    public static Vector3Frac MAXIMUM_2 = Vector3FracFactory.getInstance(3, 2,
	    2);

    public static Vector3Frac MINIMUM_2 = Vector3FracFactory.getInstance(3, 2,
	    1);
    public static Vector3Frac TARGET_1 = Vector3FracFactory
	    .getInstance(6, 2, 0);

    public static Vector3Frac TARGET_2 = Vector3FracFactory
	    .getInstance(3, 2, 0);

    public static LinkedList<Vector3Frac> VECTORS_1;
    public static LinkedList<Vector3Frac> VECTORS_2;

    static {
	VECTORS_1 = new LinkedList<Vector3Frac>();
	VECTORS_1.add(Vector3FracFactory.getInstance(-1, 2, 2));
	VECTORS_1.add(Vector3FracFactory.getInstance(2, 3, -1));
	VECTORS_1.add(Vector3FracFactory.getInstance(3, 1, -1));
	VECTORS_1.add(Vector3FracFactory.getInstance(2, -4, 2));
    }
    static {
	VECTORS_2 = new LinkedList<Vector3Frac>();
	// VECTORS_2.add(Vector3FracFactory.getInstance(2, 4, 2));
	VECTORS_2.add(Vector3FracFactory.getInstance(4, 2, 2));
	VECTORS_2.add(Vector3FracFactory.getInstance(1, 2, 2));
	VECTORS_2.add(Vector3FracFactory.getInstance(3, 1, 1));
	VECTORS_2.add(Vector3FracFactory.getInstance(3, 3, 1));
    }

    private LOPSamples() {
    }
}
