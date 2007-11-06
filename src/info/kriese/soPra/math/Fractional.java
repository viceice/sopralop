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
 * 09.10.2007 - Version 0.3
 * - Konstanten MIN_VALUE und MAX_VALUE hizugefügt
 * 15.09.2007 - Version 0.2.1
 * - Methode equals implemetiert
 * 14.07.2007 - Version 0.2
 * - Methode toDouble hinzugefügt
 * 11.04.2007 - Version 0.1
 * - Datei hinzugefuegt
 */
package info.kriese.soPra.math;

import info.kriese.soPra.math.impl.FractionalFactory;

/**
 * Interface, welches einen gemeinen Bruch darstellt.
 * 
 * @author Michael Kriese
 * @since 11.04.2007
 * @version 0.3
 */

public interface Fractional extends Cloneable, Comparable<Fractional> {

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
     * Addiert den übergebenen Bruch mit diesem und gibt das Ergebnis zurück.
     * 
     * @param frac
     * @return
     */
    Fractional add(Fractional frac);

    Fractional add(int x);

    /**
     * Erstellt eine Kopie des Bruchs
     * 
     * @return Kopie des Bruchs
     */
    Fractional clone();

    Fractional div(Fractional frac);

    Fractional div(int x);

    boolean equals(Fractional frac);
    
    boolean isZero(Fractional frac);

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
