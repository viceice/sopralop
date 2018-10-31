/**
 * @version 	$Id$
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
 * 01.02.2008 - Version 0.3.1
 * Neue Methode cross
 * 17.09.2007 - Version 0.3
 * - Neue Methoden: dot, sub, inv, getArray, scale
 * 15.09.2007 - Version 0.2
 * - Methode equals hinzugefügt
 * 11.04.2007 - Version 0.1
 * - Datei hinzugefuegt
 */
package info.kriese.sopra.math;

import info.kriese.sopra.math.impl.Vector3FracFactory;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

/**
 * Interface, welches einen 3-dimensionalen Vector darstellt.
 * 
 * @author Michael Kriese
 * @since 11.04.2007
 * @version 0.3.1
 */
public interface Vector3Frac extends Cloneable {

    static Vector3Frac ZERO = Vector3FracFactory.getInstance();

    /**
     * Erstellt eine Kopie des Vectors
     * 
     * @return Kopie des Vectors
     */
    Vector3Frac clone();

    /**
     * Berechnet das Kreuzprodukt.
     * 
     * res = this x vec
     * 
     * @param vec -
     *                Vektor, welcher benutz werden soll.
     * @return Das Kreuzprodukt diese und des übergebenen Vektors.
     */
    Vector3Frac cross(Vector3Frac vec);

    /**
     * Berechnet das Skalarprodukt.
     * 
     * res = this . vec
     * 
     * @param vec -
     *                Vector
     * @return Skalarprodukt
     */
    float dot(Vector3Frac vec);

    /**
     * Vergleicht diesen Vektor mit einem anderen.
     * 
     * @param vec -
     *                Vektor, mit welchem verglichen werden soll.
     * @return "true" wenn alle Werte (x, y, z) gleich sind.
     */
    boolean equals(Vector3Frac vec);

    /**
     * Gibt die x-Koordinate zurueck.
     * 
     * @return x-Koordinate als Bruch
     */
    Fractional getCoordX();

    /**
     * Gibt die y-Koordinate zurueck.
     * 
     * @return y-Koordinate als Bruch
     */
    Fractional getCoordY();

    /**
     * Gibt die z-Koordinate zurueck.
     * 
     * @return z-Koordinate als Bruch
     */
    Fractional getCoordZ();

    /**
     * Gibt den Betrag des Vectors zurueck.
     * 
     * @return Betrag als float
     */
    float getNorm();

    /**
     * Gibt den inversen Vektor zurück.
     * 
     * @return
     */
    Vector3Frac inv();

    Vector3Frac scale(int x);

    /**
     * Setzt die x-Koordinate.
     * 
     * @param x
     *                x-Koordinate als Bruch
     */
    void setCoordX(Fractional x);

    /**
     * Setzt die y-Koordinate.
     * 
     * @param y
     *                y-Koordinate als Bruch
     */
    void setCoordY(Fractional y);

    /**
     * Setzt die z-Koordinate.
     * 
     * @param z
     *                z-Koordinate als Bruch
     */
    void setCoordZ(Fractional z);

    Vector3Frac sub(Vector3Frac vec);

    /**
     * Wandelt den Vector in einen Raumpunkt um.
     * 
     * @return Vector als Punkt im Raum.
     */
    Point3f toPoint3f();

    /**
     * Wandelt den Vector in einen Raumvector um.
     * 
     * @return Vector im Raum.
     */
    Vector3f toVector3f();
}
