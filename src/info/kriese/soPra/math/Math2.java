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
 * 08.11.2007 - Version 0.3.2
 * - isPointInTriangle überarbeitet, sollte jetzt korrekt funktionieren
 * 07.11.2007 - Version 0.3.1
 * - isPointinTriangle benutzt jetzt Gauß zur Bestimmung
 * 30.10.2007 - Version 0.3
 * - Spezialversion von sqrt hinzugefügt
 * 23.10.2007 - Version 0.2.3
 * - BugFix: isPointInTriangle hat bei bestimmten konstellationen falsch gerechnet
 * 02.10.2007 - Version 0.2.2
 * - Methode angle hizugefügt
 * 16.09.2007 - Version 0.2.1
 *  - Methode round hinzugefügt
 * 12.05.2007 - Version 0.2
 * - normalizeScale Methode hinzugefuegt
 * 13.04.2007 - Version 0.1
 * - Datei hinzugefuegt
 */
package info.kriese.soPra.math;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

/**
 * 
 * @author Michael Kriese
 * @since 13.04.2007
 * @version 0.3.2
 */
public final class Math2 {

    public static final float EPSILON = 1000.0f;
    public static final int EPSILON_INT = 1000;

    /**
     * Berechnet den Winkel zwischen 2 Vektoren mittels Skalarprodukt.
     * 
     * @param v1 -
     *                Der erste Vektor
     * @param v2 -
     *                Der zweite Vektor
     * @return Der Winkel zwischen den Vektoren.
     */
    public static double angle(Vector3d v1, Vector3d v2) {

	Vector3d t1 = new Vector3d(v1), t2 = new Vector3d(v2);

	t1.normalize();
	t2.normalize();

	return Math.acos(t1.dot(t2));
    }

    /**
     * Berechnet den kleinsten gemeinsamen Teiler zweier Zahlen.
     * 
     * @param m -
     *                1. Zahl
     * @param n -
     *                2. Zahl
     * @return Kleinster gemeinsamer Teiler
     * 
     * @author Michael Kriese
     */
    public static int ggT(int m, int n) {
	m = Math.abs(m);
	n = Math.abs(n);
	int z;
	while (n > 0) {
	    z = n;
	    n = m % n;
	    m = z;
	}
	return m;
    }

    public static boolean isPointInTriangle(Vector3Frac a, Vector3Frac b,
	    Vector3Frac c, Vector3Frac p) {

	Vector3Frac sol = Gauss.gaussElimination2(a, b, c, p);

	Fractional u = sol.getCoordX().mul(b.getCoordZ().sub(a.getCoordZ()));
	Fractional v = sol.getCoordY().mul(c.getCoordZ().sub(a.getCoordZ()));

	boolean res = false;

	if (u.add(v).add(a.getCoordZ()).equals(p.getCoordZ())) {
	    u = sol.getCoordX();
	    v = sol.getCoordY();
	    if (a.equals(Vector3Frac.ZERO))
		res = ((u.compareTo(Fractional.ZERO) > 0) && (v
			.compareTo(Fractional.ZERO) > 0));
	    else
		res = ((u.compareTo(Fractional.ZERO) > 0)
			&& (v.compareTo(Fractional.ZERO) > 0) && (u.add(v)
			.compareTo(Fractional.ONE) <= 0));
	} else
	    System.err.print("? ");

	System.err.println("[ " + a + ", " + b + ", " + c + " ] = " + p
		+ "\t\t\t[ u=" + u + ", v=" + v + " | "
		+ (res ? "true" : "false") + " | " + sol + " ]");
	return res;
    }

    /**
     * Normiert und skaliert einen Vektor im Point3f Format.
     * 
     * @param orig -
     *                Originalvektor
     * @param scale -
     *                Skalierfaktor
     * @return der normierte und dann skalierte Vektor
     */
    public static Point3f normalizeScale(Point3f orig, float scale) {
	if (orig.x == 0.0f && orig.y == 0.0f && orig.z == 0.0f)
	    return orig;
	Vector3f tmp = new Vector3f(orig.x, orig.y, orig.z);
	tmp.normalize();
	tmp.scale(scale);
	return new Point3f(tmp.x, tmp.y, tmp.z);
    }

    /**
     * Rundet ein Double mit EPSILON Genauigkeit.
     * 
     * @param val -
     *                Double Wert, welcher gerundet werden soll
     * @return Den gerundeten Wert als float
     */
    public static float round(double val) {
	return Math.round(val * EPSILON) / EPSILON;
    }

    /**
     * Berechnet die quadratische Wurzel.
     * 
     * @param a
     * @return
     */
    public static double sqrt(double a) {
	if (Math.abs(a) <= 2.2204460492503131e-16)
	    return a;
	return Math.sqrt(a);
    }

    /**
     * Berechnet das Quadrat der Zahl.
     * 
     * @param x -
     *                Zahl, welche quadriet werden soll
     * @return Die quadrierte Zahl
     * 
     * @author Michael Kriese
     */
    public static float square(float x) {
	return x * x;
    }

    private Math2() {
    }

}
