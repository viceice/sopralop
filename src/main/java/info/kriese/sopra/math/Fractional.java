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
 * 07.11.2007 - Version 0.4
 * - Neue Konstanten ONE, ZERO, LEQUAL_ONE, GEQUAL_ZERO
 * - Neue Methoden isZero(), is(int what)
 * 09.10.2007 - Version 0.3
 * - Konstanten MIN_VALUE und MAX_VALUE hizugefügt
 * 15.09.2007 - Version 0.2.1
 * - Methode equals implemetiert
 * 14.07.2007 - Version 0.2
 * - Methode toDouble hinzugefügt
 * 11.04.2007 - Version 0.1
 * - Datei hinzugefuegt
 */
package info.kriese.sopra.math;

import info.kriese.sopra.math.impl.FractionalFactory;

/**
 * Interface, welches einen gemeinen Bruch darstellt.
 * 
 * @author Michael Kriese
 * @since 11.04.2007
 * @version 0.4
 */

public interface Fractional extends Cloneable, Comparable<Fractional> {

    /**
     * Is Fractional größer oder gleich 0
     */
    static final int GEQUAL_ZERO = 0;

    /**
     * Ist Fractional kleiner oder gleich 1
     */
    static final int LEQUAL_ONE = 1;

    /**
     * Größter Bruch
     */
    static final Fractional MAX_VALUE = FractionalFactory
	    .getInstance(Integer.MAX_VALUE);

    /**
     * Kleinster Bruch.
     */
    static final Fractional MIN_VALUE = FractionalFactory
	    .getInstance(Integer.MIN_VALUE);

    /**
     * Stellt den Bruch 1/1 dar.
     */
    static final Fractional ONE = FractionalFactory.getInstance(1);

    /**
     * Stellt den Bruch 0/1 dar.
     */
    static final Fractional ZERO = FractionalFactory.getInstance();

    /**
     * Addiert den übergebenen Bruch mit diesem und gibt das Ergebnis zurück.
     * 
     * @param frac -
     *                Der auf diesen Bruch zu addiernde Bruch.
     * @return Das Ergebnis der Addition.
     */
    Fractional add(Fractional frac);

    /**
     * Addiert eine ganze Zahl auf einen Bruch
     * 
     * @param x -
     *                Ganze Zahlt, die auf diesen Bruch zu addieren ist.
     * @return Das Ergebnis der Addition.
     */
    Fractional add(int x);

    /**
     * Erstellt eine Kopie des Bruchs
     * 
     * @return Kopie des Bruchs
     */
    Fractional clone();

    /**
     * Dividiert zwei Brüche.
     * 
     * result = this / frac
     * 
     * @param frac -
     *                Der Bruch, durch welchen dieser Bruch dividiert wird.
     * @return Das Ergebnis der Division.
     */
    Fractional div(Fractional frac);

    Fractional div(int x);

    boolean equals(Fractional frac);

    /**
     * Gibt den Nenner zurück.
     * 
     * @return Nenner
     */
    int getDenominator();

    /**
     * Gibt den Zähler zurück.
     * 
     * @return Zähler
     */
    int getNumerator();

    /**
     * Testet den Fractional auf den übergebenen Fall.
     * 
     * @param what -
     *                Konstante, welche den Fall angibt, auf den getestet werden
     *                soll.
     * @return true, falls der Test erfolgreich ist, sonst false
     * @throws IllegalArgumentException,
     *                 falls Test nicht existiert.
     */
    boolean is(int what);

    boolean isZero();

    Fractional max(Fractional frac);

    Fractional mul(Fractional frac);

    Fractional mul(int x);

    /**
     * Setzt den Nenner.
     * 
     * @param denom
     *                Der zu setzende Nenner.
     */
    void setDenominator(int denom);

    /**
     * Setzt den Zähler.
     * 
     * @param nom
     *                Der zu setzende Zähler
     */
    void setNumerator(int num);

    Fractional sub(Fractional frac);

    Fractional sub(int x);

    double toDouble();

    /**
     * Wandelt den Bruch in eine Flie�kommazahl um.
     * 
     * @return Fie�kommazahl, welche den gerundeten Bruch darstellt.
     */
    float toFloat();
}
