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
 * 29.01.2008 - Version 0.4.0.1
 * - BugFix: Nullpointer im Vergleich behoben
 * 07.11.2007 - Version 0.4
 * - Neue Interfacemethode implementiert
 * 17.09.2007 - Version 0.3.3
 * - getInstance Methoden in Factory ausgelagert
 * 16.09.2007 - Version 0.3.2
 * - Methode zur Erstellung aus einem Double hinzugefügt
 * 15.07.2007 - Version 0.3.1
 *  - Methode equals implemetiert
 *  - Falls Nenner oder Zähler gleich 0 gesetzt wird, setze Nenner = 1 & Zähler = 0
 *  - kürzen in eigene Methode ausgelagert
 * 14.07.2007 - Version 0.3
 * - Methode toDouble implementiert
 * 30.07.2007 - Version 0.2
 * - Konstruktor hinzugefuegt
 * 29.07.2007
 * - in neues Package verschoben und umbenannt
 * 11.04.2007 - Version 0.1
 * - Datei hinzugefuegt
 */
package info.kriese.soPra.math.impl;

import info.kriese.soPra.math.Fractional;
import info.kriese.soPra.math.Math2;

/**
 * Stellt die Implementierung des Bruch dar.
 * 
 * @author Michael Kriese
 * @since 11.04.2007
 * @version 0.4.0.1
 */
class FractionalImpl implements Fractional {

    /**
     * Nenner
     */
    private int denominator = 0;

    /**
     * Zähler
     */
    private int numerator = 0;

    protected FractionalImpl(int num, int denom) {
	this.denominator = denom;
	this.numerator = num;
	cancel();
    }

    public Fractional add(Fractional frac) {
	int num = (this.numerator * frac.getDenominator())
		+ (frac.getNumerator() * this.denominator);
	int denom = this.denominator * frac.getDenominator();
	return new FractionalImpl(num, denom);
    }

    public Fractional add(int x) {
	return add(FractionalFactory.getInstance(x));
    }

    @Override
    public Fractional clone() {

	Fractional copy;
	try {
	    copy = (Fractional) super.clone();
	    copy.setNumerator(this.numerator);
	    copy.setDenominator(this.denominator);
	    return copy;
	} catch (CloneNotSupportedException e) {
	    return FractionalFactory.getInstance(this.numerator,
		    this.denominator);
	}
    }

    public int compareTo(Fractional frac) {
	if ((this.numerator * frac.getDenominator()) > (frac.getNumerator() * this.denominator))
	    return 1;
	if ((this.numerator * frac.getDenominator()) < (frac.getNumerator() * this.denominator))
	    return -1;
	return 0;
    }

    public Fractional div(Fractional frac) {
	int num = this.numerator * frac.getDenominator();
	int denom = this.denominator * frac.getNumerator();
	return FractionalFactory.getInstance(num, denom);
    }

    public Fractional div(int x) {
	return div(FractionalFactory.getInstance(x));
    }

    public boolean equals(Fractional frac) {
	if (frac == null)
	    return false;
	return ((this.numerator * frac.getDenominator()) == frac.getNumerator()
		* this.denominator);
    }

    @Override
    public boolean equals(Object obj) {
	if (obj instanceof Fractional)
	    return equals((Fractional) obj);
	else
	    return super.equals(obj);
    }

    public int getDenominator() {
	return this.denominator;
    }

    public int getNumerator() {
	return this.numerator;
    }

    public boolean is(int what) {
	switch (what) {
	    case LEQUAL_ONE:
		return compareTo(ONE) <= 0;
	    case GEQUAL_ZERO:
		return compareTo(ZERO) >= 0;

	    default:
		throw new IllegalArgumentException("Test " + what
			+ " doesn't exists.");
	}
    }

    public boolean isZero() {
	return this.numerator == 0;
    }

    public Fractional max(Fractional frac) {
	if ((this.numerator * frac.getDenominator()) > (frac.getNumerator() * this.denominator))
	    return this;
	else
	    return frac;
    }

    public Fractional mul(Fractional frac) {
	int num = this.numerator * frac.getNumerator();
	int denom = this.denominator * frac.getDenominator();
	return FractionalFactory.getInstance(num, denom);
    }

    public Fractional mul(int x) {
	return mul(FractionalFactory.getInstance(x));
    }

    public void setDenominator(int denom) {
	if (denom == 0) {
	    this.denominator = 1;
	    this.numerator = 0;
	} else {
	    this.denominator = denom;
	    cancel();
	}
    }

    public void setNumerator(int num) {
	if (num == 0) {
	    this.denominator = 1;
	    this.numerator = 0;
	} else {
	    this.numerator = num;
	    cancel();
	}
    }

    public Fractional sub(Fractional frac) {
	int num = (this.numerator * frac.getDenominator())
		- (frac.getNumerator() * this.denominator);
	int denom = this.denominator * frac.getDenominator();
	return FractionalFactory.getInstance(num, denom);
    }

    public Fractional sub(int x) {
	return sub(FractionalFactory.getInstance(x));
    }

    public double toDouble() {
	return this.numerator / (double) this.denominator;
    }

    public float toFloat() {
	return this.numerator / (float) this.denominator;
    }

    @Override
    public String toString() {
	if (this.denominator == 1)
	    return Integer.toString(this.numerator);
	else
	    return this.numerator + "/" + this.denominator;
    }

    /**
     * Kürze den Bruch, falls ggt > 0
     */
    private void cancel() {
	int ggt = Math2.ggT(this.denominator, this.numerator);
	if (ggt > 0) {
	    this.denominator /= ggt;
	    this.numerator = this.numerator / ggt;
	}
    }
}