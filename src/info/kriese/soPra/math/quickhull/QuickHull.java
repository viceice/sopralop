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
 * 23.10.2007 - Version 0.2
 * - Quickhull angepasst, Vektoren, die in einer Ebene liegen, werden nicht mehr herausgefiltert
 * 16.09.2007 - Version 0.1.1
 * - Runden im Konstruktor des Point3fWrapper hinzugefügt
 * 14.09.2007 - Version 0.1
 *  - Datei hinzugefuegt
 */
package info.kriese.soPra.math.quickhull;

import info.kriese.soPra.math.Vector3Frac;
import info.kriese.soPra.math.Vertex;
import info.kriese.soPra.math.impl.Vector3FracFactory;

import java.util.ArrayList;
import java.util.List;

import ca.ubc.cs.spider.lloyed.quickhull3d.Point3d;
import ca.ubc.cs.spider.lloyed.quickhull3d.QuickHull3D;

/**
 * WrapperKlasse für den QuickHull Algorithmus.
 * 
 * @author Michael Kriese
 * @version 0.2
 * @since 14.09.2007
 * 
 * @see http://www.cs.ubc.ca/spider/lloyd/index.html
 */
public final class QuickHull {

    private int[][] faces;

    private final QuickHull3D hull;
    private Point3d[] oldVertices;
    private final List<Vertex> vertices;

    public QuickHull() {
	this.hull = new QuickHull3D();

	this.vertices = new ArrayList<Vertex>();
    }

    public void build(List<Vector3Frac> points) {
	Point3d[] pts = new Point3d[points.size() + 1];
	for (int i = 0; i < points.size(); i++)
	    pts[i] = new Point3DWrapper(points.get(i));

	pts[pts.length - 1] = new Point3DWrapper();
	this.hull.build(pts);

	this.faces = this.hull.getFaces(QuickHull3D.INDEXED_FROM_ZERO);
	this.oldVertices = this.hull.getVertices();

	this.vertices.clear();

	for (int[] face : this.faces)
	    this.vertices.add(getVertex(this.oldVertices[face[0]],
		    this.oldVertices[face[1]], this.oldVertices[face[2]]));

	if (this.oldVertices.length == points.size() + 1)
	    return; // kein Vektor wurde gefiltert

	List<Vector3Frac> lost = new ArrayList<Vector3Frac>(points);
	List<Vertex> nvertices = new ArrayList<Vertex>(this.vertices), tvtx = new ArrayList<Vertex>();
	Vertex v1, v2;

	for (Point3d pnt : this.oldVertices)
	    lost.remove(Vector3FracFactory.getInstance(pnt.x, pnt.y, pnt.z));

	for (Vector3Frac vec : lost) {
	    for (Vertex vertex : nvertices)
		if (vertex.isPointInVertex(vec)
			&& vertex.p1.equals(Vector3Frac.ZERO)) {
		    // ok, spalte vertex auf

		    v1 = new Vertex();
		    v1.p2 = vertex.p2.clone();
		    v1.p3 = vec.clone();
		    tvtx.add(v1);

		    v2 = new Vertex();
		    v2.p2 = vec.clone();
		    v2.p3 = vertex.p3.clone();
		    tvtx.add(v2);

		} else
		    tvtx.add(vertex);

	    nvertices.clear();
	    nvertices.addAll(tvtx);
	}

	this.vertices.clear();
	this.vertices.addAll(nvertices);
    }

    public List<Vertex> getVerticesList() {
	return this.vertices;
    }

    public void print() {
	this.hull.print(System.out, QuickHull3D.INDEXED_FROM_ZERO);
    }

    public void triangulate() {
	this.hull.triangulate();
    }

    private Vertex getVertex(Point3d p1, Point3d p2, Point3d p3) {
	Vertex res = new Vertex();
	Vector3Frac v1, v2, v3;
	v1 = Vector3FracFactory.getInstance(p1.x, p1.y, p1.z);
	v2 = Vector3FracFactory.getInstance(p2.x, p2.y, p2.z);
	v3 = Vector3FracFactory.getInstance(p3.x, p3.y, p3.z);

	if (v1.equals(Vector3Frac.ZERO)) {
	    res.p1 = v1;
	    res.p2 = v2;
	    res.p3 = v3;
	} else if (v2.equals(Vector3Frac.ZERO)) {
	    res.p1 = v2;
	    res.p2 = v3;
	    res.p3 = v1;
	} else {
	    res.p1 = v3;
	    res.p2 = v1;
	    res.p3 = v2;
	}
	return res;
    }

}
