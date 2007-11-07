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
 * - Gauss statisch gemacht, Instanz ist unnötig
 * 16.10.2007 - Version 0.3.1
 * - Gauss überarbeitet, keine Überladung mehr
 * 11.10.2007 - Version 0.3
 * - Gauss auf beliebige Ebenen im Raum erweitert
 * 08.10.2007 - Version 0.2
 * - unnötige Variablen und Methoden entfernt (optimalZ, optimalVectors ...)
 * 27.04.2007 - Version 0.1
 * - Datei hinzugefuegt
 */
package info.kriese.soPra.math;

import info.kriese.soPra.math.impl.Vector3FracFactory;

/**
 * Klasse zur Berechnung des Loesungsraumes
 * 
 * @author Peer Sterner
 * @since 27.04.2007
 * @version 0.4
 */
public final class Gauss {

    /**
     * Methode zur Berechnung von x1, x2 und z fuer zwei gegebene Vektoren.
     * 
     * @param l1 -
     *                Vektor l1 zur Berechnung
     * @param l2 -
     *                Vektor l2 zur Berechnung
     * @param constant -
     *                Vektor zur Hebung bei Verschiebung aus dem Ursprung
     * @param target -
     *                rechte Seite der Nebenbedingungen
     * 
     * @return - neuen Vektor z mit z.X = x1, z.Y = x2 und z.Z = z
     */
    public static Vector3Frac gaussElimination2(Vector3Frac l1, Vector3Frac l2,
	    Vector3Frac target) {
	return gaussElimination2(Vector3Frac.ZERO, l1, l2, target);
    }

    public static Vector3Frac gaussElimination2(Vector3Frac constant,
	    Vector3Frac l1, Vector3Frac l2, Vector3Frac target) {

	Vector3Frac a, b, c, z;
	Fractional temp, factor;

	if (!constant.equals(Vector3Frac.ZERO)) {
	    a = constant.clone();
	    b = renderVectors(l1, constant);
	    c = renderVectors(l1, l2);
	    z = target.clone();
	    z.setCoordX(z.getCoordX().sub(a.getCoordX()));
	    z.setCoordY(z.getCoordY().sub(a.getCoordY()));
	} else {
	    a = constant.clone();
	    b = l1.clone();
	    c = l2.clone();
	    z = target.clone();
	}

	if (c.getCoordX().isZero() && b.getCoordY().isZero()) {
	    z.setCoordX(z.getCoordX().div(b.getCoordX()));
	    z.setCoordY(z.getCoordY().div(c.getCoordY()));
	    temp = z.getCoordX();
	    z.setCoordX(z.getCoordY());
	    z.setCoordY(temp);
	} else if (c.getCoordY().isZero() && b.getCoordX().isZero()) {
	    z.setCoordX(z.getCoordX().div(c.getCoordX()));
	    z.setCoordY(z.getCoordY().div(b.getCoordY()));
	} else if (c.getCoordX().isZero()) {
	    z.setCoordX(z.getCoordX().div(b.getCoordX()));
	    z.setCoordY((z.getCoordY().sub(b.getCoordY().mul(z.getCoordX())))
		    .div(c.getCoordY()));
	    temp = z.getCoordX();
	    z.setCoordX(z.getCoordY());
	    z.setCoordY(temp);
	} else if (c.getCoordY().isZero()) {
	    z.setCoordY(z.getCoordY().div(b.getCoordY()));
	    z.setCoordX((z.getCoordX().sub(b.getCoordX().mul(z.getCoordY())))
		    .div(c.getCoordX()));
	} else if (b.getCoordX().isZero()) {
	    z.setCoordX(z.getCoordX().div(c.getCoordX()));
	    z.setCoordY((z.getCoordY().sub(c.getCoordY().mul(z.getCoordX())))
		    .div(b.getCoordY()));
	} else if (b.getCoordY().isZero()) {
	    z.setCoordY(z.getCoordY().div(c.getCoordY()));
	    z.setCoordX((z.getCoordX().sub(c.getCoordX().mul(z.getCoordY())))
		    .div(b.getCoordX()));
	    temp = z.getCoordX();
	    z.setCoordX(z.getCoordY());
	    z.setCoordY(temp);
	} else {
	    factor = Fractional.ZERO.sub(b.getCoordY().div(c.getCoordY()));
	    temp = b.getCoordX().add(factor.mul(c.getCoordX()));
	    b.setCoordZ(b.getCoordZ().add(factor.mul(c.getCoordZ())).div(temp));
	    c.setCoordZ((c.getCoordZ().sub(b.getCoordZ().mul(c.getCoordX())))
		    .div(c.getCoordY()));
	}

	z.setCoordZ(((b.getCoordZ().mul(z.getCoordX())).add((c.getCoordZ()
		.mul(z.getCoordY())))).add(a.getCoordZ()));
	z.setCoordX(b.getCoordZ());
	z.setCoordY(c.getCoordZ());

	return z;
    }

    private static Vector3Frac renderVectors(Vector3Frac l1, Vector3Frac l2) {
	Vector3Frac newVector = Vector3FracFactory.getInstance();
	newVector.setCoordX(l2.getCoordX().sub(l1.getCoordX()));
	newVector.setCoordY(l2.getCoordY().sub(l1.getCoordY()));
	newVector.setCoordZ(l2.getCoordZ().sub(l1.getCoordZ()));
	return newVector;
    }

    /**
     * Methode zur Berechnung von x1, x2 und z fuer zwei gegebene Vektoren.
     * 
     * @param l1 -
     *                Vektor l1 zur Berechnung
     * @param l2 -
     *                Vektor l2 zur Berechnung
     * @param constant -
     *                Vektor zur Hebung bei Verschiebung aus dem Ursprung
     * @param target -
     *                rechte Seite der Nebenbedingungen
     * 
     * @return - neuen Vektor z mit z.X = x1, z.Y = x2 und z.Z = z
     * 
     * @deprecated use static method
     */
    @Deprecated
    public Vector3Frac gaussElimination(Vector3Frac l1, Vector3Frac l2,
	    Vector3Frac target) {
	return gaussElimination2(Vector3Frac.ZERO, l1, l2, target);
    }

    /**
     * Use static method.
     * 
     * @param constant
     * @param l1
     * @param l2
     * @param target
     * @return
     * 
     * @deprecated use static method
     */
    @Deprecated
    public Vector3Frac gaussElimination(Vector3Frac constant, Vector3Frac l1,
	    Vector3Frac l2, Vector3Frac target) {

	return gaussElimination2(constant, l1, l2, target);
    }
}
