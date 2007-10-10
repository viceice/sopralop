/**
 * @version		$Id$
 * @copyright	(c)2007 Michael Kriese & Peer Sterner
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
 * 16.09.2007
 * - Runden im Konstruktor des Point3fWrapper hinzugefügt
 * 14.09.2007 - Version 0.1
 *  - Datei hinzugefuegt
 */
package info.kriese.soPra.math;

import info.kriese.soPra.math.impl.Vector3FracFactory;

import java.util.List;

import javax.vecmath.Point3f;

import ca.ubc.cs.spider.lloyed.quickhull3d.Point3d;
import ca.ubc.cs.spider.lloyed.quickhull3d.QuickHull3D;

/**
 * WrapperKlasse für den QuickHull Algorithmus.
 * 
 * @author Michael Kriese
 * @version 0.1
 * @since 14.09.2007
 * 
 * @see http://www.cs.ubc.ca/spider/lloyd/index.html
 */
public final class QuickHull {

    private class Point3DWrapper extends Point3d {

	public Point3DWrapper() {
	    set(0.0, 0.0, 0.0);
	}

	public Point3DWrapper(Vector3Frac frac) {
	    set(frac.getCoordX().toDouble(), frac.getCoordY().toDouble(), frac
		    .getCoordZ().toDouble());
	}
    }

    private class Point3fWrapper extends Point3f {

	/**  */
	private static final long serialVersionUID = -6351806429768904428L;

	public Point3fWrapper(Point3d p) {
	    this.x = Math2.round(p.x);
	    this.y = Math2.round(p.y);
	    this.z = Math2.round(p.z);
	}

    }

    private static class QuickHullWrapper extends QuickHull3D {

	public double size() {
	    return this.charLength;
	}
    }

    private final QuickHullWrapper hull;

    public QuickHull() {
	this.hull = new QuickHullWrapper();
    }

    public void build(List<Vector3Frac> points) {
	Point3d[] pts = new Point3d[points.size() + 1];
	for (int i = 0; i < points.size(); i++)
	    pts[i] = new Point3DWrapper(points.get(i));

	pts[pts.length - 1] = new Point3DWrapper();
	this.hull.build(pts);
    }

    public int[][] getFaces() {
	return this.hull.getFaces(QuickHull3D.INDEXED_FROM_ZERO);
    }

    public Point3f[] getVertices() {
	Point3d[] ver = this.hull.getVertices();
	Point3f[] res = new Point3f[ver.length];

	for (int i = 0; i < res.length; i++) {
	    ver[i].normalize();
	    res[i] = new Point3fWrapper(ver[i]);
	}

	return res;
    }

    public Vector3Frac[] getVerticesAsVector3Frac() {
	Point3d[] vert = this.hull.getVertices();

	Vector3Frac[] res = Vector3FracFactory.getArray(vert.length);

	for (int i = 0; i < res.length; i++)
	    res[i] = Vector3FracFactory.getInstance(vert[i].x, vert[i].y,
		    vert[i].z);

	return res;
    }

    public void print() {
	this.hull.print(System.out, QuickHull3D.INDEXED_FROM_ZERO);
    }

    public float size() {
	// TODO: korrekte Größe ermitteln
	return 15.0f; // (float) hull.size();
    }

    public void triangulate() {
	this.hull.triangulate();
    }

}
