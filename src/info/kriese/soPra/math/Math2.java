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
 * 23.10.2007 - Versaion 0.3
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

import info.kriese.soPra.math.impl.Vector3FracFactory;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

/**
 * 
 * @author Michael Kriese
 * @since 13.04.2007
 * @version 0.3
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
	    Vector3Frac p) {
	return isPointInTriangle(Vector3Frac.ZERO, a, b, p);
    }

    public static boolean isPointInTriangle(Vector3Frac a, Vector3Frac b,
	    Vector3Frac c, Vector3Frac p) {

	Vector3Frac v0 = c.sub(a);
	Vector3Frac v1 = b.sub(a);
	Vector3Frac v2 = p.sub(a);

	// Compute dot products
	float dot00 = v0.dot(v0);
	float dot01 = v0.dot(v1);
	float dot02 = v0.dot(v2);
	float dot11 = v1.dot(v1);
	float dot12 = v1.dot(v2);

	// Compute barycentric coordinates
	float invDenom = 1 / (dot00 * dot11 - dot01 * dot01);
	float u = (dot11 * dot02 - dot01 * dot12) * invDenom;
	float v = (dot00 * dot12 - dot01 * dot02) * invDenom;

	u = round(u);
	v = round(v);

	// Check if point is in triangle
	boolean res = (u >= 0) && (v >= 0) && (u + v <= 1)
		&& isPointInTriangle2(a, b, c, p);

	// System.err.println("[ " + a + ", " + b + ", " + c + " ] = " + p
	// + "\t\t\t[ u=" + u + ", v=" + v + " | "
	// + (res ? "true" : "false") + "]");
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

    private static Vector3Frac cross(Vector3Frac v1, Vector3Frac v2) {
	Vector3Frac res = Vector3FracFactory.getInstance();
	Fractional a, b;

	a = v1.getCoordY().mul(v2.getCoordZ());
	b = v1.getCoordZ().mul(v2.getCoordY());
	res.setCoordX(a.sub(b));

	a = v1.getCoordZ().mul(v2.getCoordX());
	b = v1.getCoordX().mul(v2.getCoordZ());
	res.setCoordY(a.sub(b));

	a = v1.getCoordX().mul(v2.getCoordY());
	b = v1.getCoordY().mul(v2.getCoordX());
	res.setCoordZ(a.sub(b));

	return res;
    }

    private static boolean isPointInTriangle2(Vector3Frac a, Vector3Frac b,
	    Vector3Frac c, Vector3Frac p) {

	Vector3Frac n = cross(b.sub(a), c.sub(a));

	if (sameSide(p, a, b, c) && sameSide(p, b, a, c)
		&& sameSide(p, c, a, b) && p.dot(n) == 0)
	    return true;
	else
	    return false;
    }

    private static boolean sameSide(Vector3Frac p1, Vector3Frac p2,
	    Vector3Frac a, Vector3Frac b) {
	Vector3Frac cp1 = cross(b.sub(a), p1.sub(a));
	Vector3Frac cp2 = cross(b.sub(a), p2.sub(a));
	if (cp1.dot(cp2) >= 0)
	    return true;
	else
	    return false;
    }

    private Math2() {
    }

}
