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
 * 11.10.2007 - Version 0.3
 * - Gauß auf beliebige Ebenen im Raum erweitert
 * 08.10.2007 - Version 0.2
 * - unnötige Variablen und Methoden entfernt (optimalZ, optimalVectors ...)
 * 27.04.2007 - Version 0.1
 * - Datei hinzugefuegt
 */
package info.kriese.soPra.math;

import info.kriese.soPra.math.impl.FractionalFactory;
import info.kriese.soPra.math.impl.Vector3FracFactory;

/**
 * Klasse zur Berechnung des Loesungsraumes
 * 
 * @author Peer Sterner
 * @since 27.04.2007
 * @version 0.3
 */
public final class Gauss {

    /**
     * privater null-Bruch
     * 
     */
    private final Fractional zero = FractionalFactory.getInstance();

    /**
     * Methode zur Berechnung von x1, x2 und z fuer zwei gegebene Vektoren.
     * 
     * @param l1 -
     *                Vektor l1 zur Berechnung
     * @param l1 -
     *                Vektor l2 zur Berechnung
     * @param target -
     *                rechte Seite der Nebenbedingungen
     * 
     * @return - neuen Vektor z mit z.X = x1, z.Y = x2 und z.Z = z
     */
    public Vector3Frac gaussElimination(Vector3Frac l1, Vector3Frac l2, Vector3Frac target) {

	Vector3Frac a = l1.clone();
	Vector3Frac b = l2.clone();
	Vector3Frac z = target.clone();

	if (a.getCoordX().getNumerator() == 0
		&& b.getCoordY().getNumerator() == 0) {
	    z.setCoordX(z.getCoordX().div(b.getCoordX()));
	    z.setCoordY(z.getCoordY().div(a.getCoordY()));
	    Fractional temp = z.getCoordX();
	    z.setCoordX(z.getCoordY());
	    z.setCoordY(temp);
	} else if (a.getCoordY().getNumerator() == 0
		&& b.getCoordX().getNumerator() == 0) {
	    z.setCoordX(z.getCoordX().div(a.getCoordX()));
	    z.setCoordY(z.getCoordY().div(b.getCoordY()));
	} else if (a.getCoordX().getNumerator() == 0) {
	    z.setCoordX(z.getCoordX().div(b.getCoordX()));
	    z.setCoordY((z.getCoordY().sub(b.getCoordY().mul(z.getCoordX())))
		    .div(a.getCoordY()));
	    Fractional temp = z.getCoordX();
	    z.setCoordX(z.getCoordY());
	    z.setCoordY(temp);
	} else if (a.getCoordY().getNumerator() == 0) {
	    z.setCoordY(z.getCoordY().div(b.getCoordY()));
	    z.setCoordX((z.getCoordX().sub(b.getCoordX().mul(z.getCoordY())))
		    .div(a.getCoordX()));
	} else if (b.getCoordX().getNumerator() == 0) {
	    z.setCoordX(z.getCoordX().div(a.getCoordX()));
	    z.setCoordY((z.getCoordY().sub(a.getCoordY().mul(z.getCoordX())))
		    .div(b.getCoordY()));
	} else if (b.getCoordY().getNumerator() == 0) {
	    z.setCoordY(z.getCoordY().div(a.getCoordY()));
	    z.setCoordX((z.getCoordX().sub(a.getCoordX().mul(z.getCoordY())))
		    .div(b.getCoordX()));
	    Fractional temp = z.getCoordX();
	    z.setCoordX(z.getCoordY());
	    z.setCoordY(temp);
	} else {
	    Fractional factor = this.zero.sub(a.getCoordY().div(a.getCoordX()));
	    b.setCoordY(b.getCoordY().add(factor.mul(b.getCoordX())));
	    z.setCoordY((z.getCoordY().add(factor.mul(z.getCoordX()))).div(b
		    .getCoordY()));
	    z.setCoordX((z.getCoordX().sub(b.getCoordX().mul(z.getCoordY())))
		    .div(a.getCoordX()));
	}

	z.setCoordZ((a.getCoordZ().mul(z.getCoordX())).add((b.getCoordZ().mul(z
		.getCoordY()))));

	return z;
    }

    public Vector3Frac gaussElimination(Vector3Frac l1, Vector3Frac l2,  
	    Vector3Frac constant, Vector3Frac target) {
    	Vector3Frac a = renderVectors(l1, l2);
    	Vector3Frac b = renderVectors(l1, constant);
    	Vector3Frac c = constant.clone();
    	Vector3Frac z = target.clone();
    	z.setCoordX(z.getCoordX().sub(c.getCoordX()));
    	z.setCoordY(z.getCoordY().sub(c.getCoordY()));

    	if (a.getCoordX().getNumerator() == 0
    		&& b.getCoordY().getNumerator() == 0) {
    	    z.setCoordX(z.getCoordX().div(b.getCoordX()));
    	    z.setCoordY(z.getCoordY().div(a.getCoordY()));
    	    Fractional temp = z.getCoordX();
    	    z.setCoordX(z.getCoordY());
    	    z.setCoordY(temp);
    	} else if (a.getCoordY().getNumerator() == 0
    		&& b.getCoordX().getNumerator() == 0) {
    	    z.setCoordX(z.getCoordX().div(a.getCoordX()));
    	    z.setCoordY(z.getCoordY().div(b.getCoordY()));
    	} else if (a.getCoordX().getNumerator() == 0) {
    	    z.setCoordX(z.getCoordX().div(b.getCoordX()));
    	    z.setCoordY((z.getCoordY().sub(b.getCoordY().mul(z.getCoordX())))
    		    .div(a.getCoordY()));
    	    Fractional temp = z.getCoordX();
    	    z.setCoordX(z.getCoordY());
    	    z.setCoordY(temp);
    	} else if (a.getCoordY().getNumerator() == 0) {
    	    z.setCoordY(z.getCoordY().div(b.getCoordY()));
    	    z.setCoordX((z.getCoordX().sub(b.getCoordX().mul(z.getCoordY())))
    		    .div(a.getCoordX()));
    	} else if (b.getCoordX().getNumerator() == 0) {
    	    z.setCoordX(z.getCoordX().div(a.getCoordX()));
    	    z.setCoordY((z.getCoordY().sub(a.getCoordY().mul(z.getCoordX())))
    		    .div(b.getCoordY()));
    	} else if (b.getCoordY().getNumerator() == 0) {
    	    z.setCoordY(z.getCoordY().div(a.getCoordY()));
    	    z.setCoordX((z.getCoordX().sub(a.getCoordX().mul(z.getCoordY())))
    		    .div(b.getCoordX()));
    	    Fractional temp = z.getCoordX();
    	    z.setCoordX(z.getCoordY());
    	    z.setCoordY(temp);
    	} else {
    	    Fractional factor = this.zero.sub(a.getCoordY().div(a.getCoordX()));
    	    b.setCoordY(b.getCoordY().add(factor.mul(b.getCoordX())));
    	    z.setCoordY((z.getCoordY().add(factor.mul(z.getCoordX()))).div(b
    		    .getCoordY()));
    	    z.setCoordX((z.getCoordX().sub(b.getCoordX().mul(z.getCoordY())))
    		    .div(a.getCoordX()));
    	}

    	z.setCoordZ(((a.getCoordZ().mul(z.getCoordX())).add((b.getCoordZ().mul(z
    		.getCoordY())))).add(c.getCoordZ()));

    	return z;
    }
    
    private Vector3Frac renderVectors(Vector3Frac l1, Vector3Frac l2) {
    	Vector3Frac newVector = Vector3FracFactory.getInstance();
    	newVector.setCoordX(l2.getCoordX().sub(l1.getCoordX()));
    	newVector.setCoordY(l2.getCoordY().sub(l1.getCoordY()));
    	newVector.setCoordZ(l2.getCoordZ().sub(l1.getCoordZ()));
    	return newVector;
    }
}

