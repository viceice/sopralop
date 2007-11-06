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
 * @version 0.3
 */
final class Vector3FracImpl implements Vector3Frac {

    private Fractional x, y, z;

    protected Vector3FracImpl(Fractional x, Fractional y, Fractional z) {
	this.x = x;
	this.y = y;
	this.z = z;
    }

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

    public float dot(Vector3Frac vec) {
	return this.x.mul(vec.getCoordX()).toFloat()
		+ this.y.mul(vec.getCoordY()).toFloat()
		+ this.z.mul(vec.getCoordZ()).toFloat();
    }

    @Override
    public boolean equals(Object obj) {
	if (obj instanceof Vector3Frac)
	    return equals((Vector3Frac) obj);
	else
	    return super.equals(obj);
    }

    public boolean equals(Vector3Frac vec) {
	return this.x.equals(vec.getCoordX()) && this.y.equals(vec.getCoordY())
		&& this.z.equals(vec.getCoordZ());
    }

    public Fractional getCoordX() {
	return this.x;
    }

    public Fractional getCoordY() {
	return this.y;
    }

    public Fractional getCoordZ() {
	return this.z;
    }

    public float getNorm() {
	return (float) Math.sqrt(Math2.square(this.x.toFloat())
		+ Math2.square(this.y.toFloat())
		+ Math2.square(this.z.toFloat()));
    }

    public Vector3Frac inv() {
	Vector3Frac res = this.clone();

	res.getCoordX().mul(-1);
	res.getCoordY().mul(-1);
	res.getCoordZ().mul(-1);

	return res;
    }

    public Vector3Frac scale(int i) {
	Vector3Frac res = this.clone();
	if (res.equals(ZERO))
	    return res;

	res.setCoordX(this.x.mul(i));
	res.setCoordY(this.y.mul(i));
	res.setCoordZ(this.z.mul(i));

	return res;
    }

    public void setCoordX(Fractional x) {
	this.x = x;
    }

    public void setCoordY(Fractional y) {
	this.y = y;
    }

    public void setCoordZ(Fractional z) {
	this.z = z;
    }

    public Vector3Frac sub(Vector3Frac vec) {
	Vector3Frac res = this.clone();

	res.setCoordX(this.x.sub(vec.getCoordX()));
	res.setCoordY(this.y.sub(vec.getCoordY()));
	res.setCoordZ(this.z.sub(vec.getCoordZ()));

	return res;
    }

    public Point3f toPoint3f() {
	return new Point3f(this.x.toFloat(), this.y.toFloat(), this.z.toFloat());
    }

    @Override
    public String toString() {
	return "(" + this.x + ", " + this.y + ", " + this.z + ")";
    }

    public Vector3f toVector3f() {
	return new Vector3f(this.x.toFloat(), this.y.toFloat(), this.z
		.toFloat());
    }
}
