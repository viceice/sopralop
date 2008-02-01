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
 * 01.02.2008 - Version 0.3.3
 * - Methode cross implementiert
 * 26.01.2008 - Version 0.3.2
 * - BugFix: Methode inv war fehlerhaft implementiert.
 * 07.11.2007 - Version 0.3.1
 * - NullPointer bei Vergleich behoben
 * 17.09.2007 - Version 0.3
 * - Neue Interface-Methoden implementiert
 * - getInstance Methoden in Factory ausgelagert
 * 15.09.2007 - Version 0.2
 * - Methode equals implemetiert
 * - Null-Vektor hizugefügt
 * 29.07.2007
 * - in neues Package verschoben und umbenannt
 * 11.04.2007 - Version 0.1
 * - Datein hinzugefuegt
 */
package info.kriese.soPra.math.impl;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

import info.kriese.soPra.math.Fractional;
import info.kriese.soPra.math.Math2;
import info.kriese.soPra.math.Vector3Frac;

/**
 * Stellt die Implementierung des Vectors dar.
 * 
 * @author Michael Kriese
 * @since 11.04.2007
 * @version 0.3.3
 */
final class Vector3FracImpl implements Vector3Frac {

    private Fractional x, y, z;

    protected Vector3FracImpl(Fractional x, Fractional y, Fractional z) {
	this.x = x;
	this.y = y;
	this.z = z;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#clone()
     */
    @Override
    public Vector3Frac clone() {
	try {
	    Vector3Frac copy = (Vector3Frac) super.clone();
	    copy.setCoordX(this.x.clone());
	    copy.setCoordY(this.y.clone());
	    copy.setCoordZ(this.z.clone());
	    return copy;
	} catch (CloneNotSupportedException e) {
	    return new Vector3FracImpl(this.x.clone(), this.y.clone(), this.z
		    .clone());
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see info.kriese.soPra.math.Vector3Frac#cross(info.kriese.soPra.math.Vector3Frac)
     */
    public Vector3Frac cross(Vector3Frac vec) {
	Vector3Frac res = clone();

	Fractional a1, a2, a3, b1, b2, b3;

	a1 = this.x;
	a2 = this.y;
	a3 = this.z;

	b1 = vec.getCoordX();
	b2 = vec.getCoordY();
	b3 = vec.getCoordZ();

	res.setCoordX(a2.mul(b3).sub(a3.mul(b2)));
	res.setCoordY(a3.mul(b1).sub(a1.mul(b3)));
	res.setCoordZ(a1.mul(b2).sub(a2.mul(b1)));

	return res;
    }

    /*
     * (non-Javadoc)
     * 
     * @see info.kriese.soPra.math.Vector3Frac#dot(info.kriese.soPra.math.Vector3Frac)
     */
    public float dot(Vector3Frac vec) {
	return this.x.mul(vec.getCoordX()).toFloat()
		+ this.y.mul(vec.getCoordY()).toFloat()
		+ this.z.mul(vec.getCoordZ()).toFloat();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
	if (obj instanceof Vector3Frac)
	    return equals((Vector3Frac) obj);
	else
	    return super.equals(obj);
    }

    /*
     * (non-Javadoc)
     * 
     * @see info.kriese.soPra.math.Vector3Frac#equals(info.kriese.soPra.math.Vector3Frac)
     */
    public boolean equals(Vector3Frac vec) {
	if (vec == null)
	    return false;
	return this.x.equals(vec.getCoordX()) && this.y.equals(vec.getCoordY())
		&& this.z.equals(vec.getCoordZ());
    }

    /*
     * (non-Javadoc)
     * 
     * @see info.kriese.soPra.math.Vector3Frac#getCoordX()
     */
    public Fractional getCoordX() {
	return this.x;
    }

    /*
     * (non-Javadoc)
     * 
     * @see info.kriese.soPra.math.Vector3Frac#getCoordY()
     */
    public Fractional getCoordY() {
	return this.y;
    }

    /*
     * (non-Javadoc)
     * 
     * @see info.kriese.soPra.math.Vector3Frac#getCoordZ()
     */
    public Fractional getCoordZ() {
	return this.z;
    }

    /*
     * (non-Javadoc)
     * 
     * @see info.kriese.soPra.math.Vector3Frac#getNorm()
     */
    public float getNorm() {
	return (float) Math.sqrt(Math2.square(this.x.toFloat())
		+ Math2.square(this.y.toFloat())
		+ Math2.square(this.z.toFloat()));
    }

    /*
     * (non-Javadoc)
     * 
     * @see info.kriese.soPra.math.Vector3Frac#inv()
     */
    public Vector3Frac inv() {
	Vector3Frac res = clone();

	res.setCoordX(this.x.mul(-1));
	res.setCoordY(this.y.mul(-1));
	res.setCoordZ(this.z.mul(-1));

	return res;
    }

    /*
     * (non-Javadoc)
     * 
     * @see info.kriese.soPra.math.Vector3Frac#scale(int)
     */
    public Vector3Frac scale(int i) {
	Vector3Frac res = clone();
	if (res.equals(ZERO))
	    return res;

	res.setCoordX(this.x.mul(i));
	res.setCoordY(this.y.mul(i));
	res.setCoordZ(this.z.mul(i));

	return res;
    }

    /*
     * (non-Javadoc)
     * 
     * @see info.kriese.soPra.math.Vector3Frac#setCoordX(info.kriese.soPra.math.Fractional)
     */
    public void setCoordX(Fractional x) {
	this.x = x;
    }

    /*
     * (non-Javadoc)
     * 
     * @see info.kriese.soPra.math.Vector3Frac#setCoordY(info.kriese.soPra.math.Fractional)
     */
    public void setCoordY(Fractional y) {
	this.y = y;
    }

    /*
     * (non-Javadoc)
     * 
     * @see info.kriese.soPra.math.Vector3Frac#setCoordZ(info.kriese.soPra.math.Fractional)
     */
    public void setCoordZ(Fractional z) {
	this.z = z;
    }

    /*
     * (non-Javadoc)
     * 
     * @see info.kriese.soPra.math.Vector3Frac#sub(info.kriese.soPra.math.Vector3Frac)
     */
    public Vector3Frac sub(Vector3Frac vec) {
	Vector3Frac res = this.clone();

	res.setCoordX(this.x.sub(vec.getCoordX()));
	res.setCoordY(this.y.sub(vec.getCoordY()));
	res.setCoordZ(this.z.sub(vec.getCoordZ()));

	return res;
    }

    /*
     * (non-Javadoc)
     * 
     * @see info.kriese.soPra.math.Vector3Frac#toPoint3f()
     */
    public Point3f toPoint3f() {
	return new Point3f(this.x.toFloat(), this.y.toFloat(), this.z.toFloat());
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "(" + this.x + ", " + this.y + ", " + this.z + ")";
    }

    /*
     * (non-Javadoc)
     * 
     * @see info.kriese.soPra.math.Vector3Frac#toVector3f()
     */
    public Vector3f toVector3f() {
	return new Vector3f(this.x.toFloat(), this.y.toFloat(), this.z
		.toFloat());
    }
}
