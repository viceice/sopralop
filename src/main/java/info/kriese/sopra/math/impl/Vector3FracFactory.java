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
 * 08.11.2007- Version 0.2
 * - Vektor kann aus Strings erstellt werden
 * - Doubles werden korrekt behandelt, nicht mehr in Integer umgewandelt
 * 17.09.2007 - Version 0.1
 *  - Datei hinzugefuegt
 */
package info.kriese.sopra.math.impl;

import info.kriese.sopra.math.Fractional;
import info.kriese.sopra.math.Vector3Frac;

/**
 * 
 * @author Michael Kriese
 * @version 0.2
 * @since 17.09.2007
 * 
 */
public final class Vector3FracFactory {

    /**
     * Erstellt ein Vector3Frac-Array der definierten Länge.
     * 
     * @param size -
     *                Größe des Arrays
     * @return Ein Vector3Frac Array
     */
    public static Vector3Frac[] getArray(int size) {
	return new Vector3FracImpl[size];
    }

    /**
     * Erstellt eine Instanz des (0, 0, 0) Vektors.
     * 
     * @return (0, 0, 0) Vektor
     */
    public static Vector3Frac getInstance() {
	return getInstance(0, 0, 0);
    }

    /**
     * Erstellt ein ein Vektor aus den gegebenen Fließkommazahlen.
     * 
     * @param x -
     *                x-Koordinate
     * @param y -
     *                y-Koordinate
     * @param z -
     *                z-Koordinate
     * @return Ein Vektor mit den gegeben Koordinaten
     */
    public static Vector3Frac getInstance(double x, double y, double z) {
	Fractional rx, ry, rz;

	rx = FractionalFactory.getInstance(x);

	ry = FractionalFactory.getInstance(y);

	rz = FractionalFactory.getInstance(z);

	return getInstance(rx, ry, rz);
    }

    /**
     * Erstellt einen Vektor aus den gegebenen Brüchen.
     * 
     * @param x -
     *                x-Koordinate
     * @param y -
     *                y-Koordinate
     * @param z -
     *                z-Koordinate
     * @return Ein Vektor mit den gegeben Koordinaten
     */
    public static Vector3Frac getInstance(Fractional x, Fractional y,
	    Fractional z) {
	return new Vector3FracImpl(x, y, z);
    }

    /**
     * Erstellt einen Vektor aus den gegebenen Ganzzahlen.
     * 
     * @param x -
     *                x-Koordinate
     * @param y -
     *                y-Koordinate
     * @param z -
     *                z-Koordinate
     * @return Ein Vektor mit den gegeben Koordinaten
     */
    public static Vector3Frac getInstance(int x, int y, int z) {
	Fractional rx, ry, rz;

	rx = FractionalFactory.getInstance(x);

	ry = FractionalFactory.getInstance(y);

	rz = FractionalFactory.getInstance(z);

	return getInstance(rx, ry, rz);
    }

    /**
     * Erstellt einen Vektor aus den gegebenen Zeichenketten.
     * 
     * @param x -
     *                x-Koordinate
     * @param y -
     *                y-Koordinate
     * @param z -
     *                z-Koordinate
     * @return Ein Vektor mit den gegeben Koordinaten
     */
    public static Vector3Frac getInstance(String x, String y, String z) {
	Fractional rx, ry, rz;

	rx = FractionalFactory.getInstance(x);

	ry = FractionalFactory.getInstance(y);

	rz = FractionalFactory.getInstance(z);

	return getInstance(rx, ry, rz);

    }
}
