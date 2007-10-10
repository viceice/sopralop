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
 * 17.09.2007 - Version 0.1
 *  - Datei hinzugefuegt
 */
package info.kriese.soPra.math.impl;

import info.kriese.soPra.math.Fractional;
import info.kriese.soPra.math.Vector3Frac;

/**
 * 
 * @author Michael Kriese
 * @version 0.1
 * @since 17.09.2007
 * 
 */
public final class Vector3FracFactory {

    public static Vector3Frac[] getArray(int size) {
	return new Vector3FracImpl[size];
    }

    public static Vector3Frac getInstance() {
	return new Vector3FracImpl(FractionalFactory.getInstance(),
		FractionalFactory.getInstance(), FractionalFactory
			.getInstance());
    }

    public static Vector3Frac getInstance(double x, double y, double z) {
	return getInstance((int) x, (int) y, (int) z);
    }

    public static Vector3Frac getInstance(Fractional x, Fractional y,
	    Fractional z) {
	return new Vector3FracImpl(x, y, z);
    }

    public static Vector3Frac getInstance(int x, int y, int z) {
	return getInstance(FractionalFactory.getInstance(x), FractionalFactory
		.getInstance(y), FractionalFactory.getInstance(z));
    }
}
