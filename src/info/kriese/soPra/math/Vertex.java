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
 * 23.10.2007 - Version 0.1
 *  - Datei hinzugefuegt
 */
package info.kriese.soPra.math;

/**
 * Stellt ein Dreieck im Raum dar.
 * 
 * @author Michael Kriese
 * @version 0.1
 * @since 23.10.2007
 * 
 */
public class Vertex {

    public Vector3Frac p1, p2, p3;

    public Vertex() {
	this(Vector3Frac.ZERO.clone(), Vector3Frac.ZERO.clone(),
		Vector3Frac.ZERO.clone());
    }

    public Vertex(Vector3Frac p1, Vector3Frac p2, Vector3Frac p3) {
	this.p1 = p1;
	this.p2 = p2;
	this.p3 = p3;
    }

    @Override
    public boolean equals(Object obj) {
	if (obj instanceof Vertex)
	    return equals((Vertex) obj);
	return super.equals(obj);
    }

    public boolean equals(Vertex vtx) {
	return this.p1.equals(vtx.p1) && this.p2.equals(vtx.p2)
		&& this.p3.equals(vtx.p3);
    }

    public boolean isPointInVertex(Vector3Frac pnt) {
	return Math2.isPointInTriangle(this.p1, this.p2, this.p3, pnt);
    }

    public boolean isPointInVertex(Vector3Frac pnt, int scale) {
	return Math2.isPointInTriangle(this.p1.scale(scale), this.p2
		.scale(scale), this.p3.scale(scale), pnt);
    }

    @Override
    public String toString() {
	return "{ " + this.p1 + " | " + this.p2 + " | " + this.p3 + " }";
    }

}
