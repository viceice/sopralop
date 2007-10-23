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
package info.kriese.soPra.test;

import info.kriese.soPra.io.impl.SettingsFactory;
import info.kriese.soPra.math.Vector3Frac;
import info.kriese.soPra.math.Vertex;
import info.kriese.soPra.math.impl.Vector3FracFactory;

/**
 * 
 * @author Michael Kriese
 * @version 0.1
 * @since 23.10.2007
 * 
 */
public final class TestTriangle {

    public static void main(String[] args) {

	System.out.println("SoPraLOP TriangleTest - Version "
		+ SettingsFactory.getInstance().getVersion());
	System.out.println("\t(c) 2007 "
		+ SettingsFactory.getInstance().getAuthor());
	System.out.println();
	Vertex vtx = new Vertex();
	vtx.p2 = Vector3FracFactory.getInstance(-2, -2, 2);
	vtx.p3 = Vector3FracFactory.getInstance(-2, 2, 2);
	Vector3Frac pnt = Vector3FracFactory.getInstance(2, 0, 2);

	System.out.println(vtx + "\t" + pnt);
	System.out.println(vtx.isPointInVertex(pnt) ? "True" : "False");

	vtx = new Vertex();
	vtx.p2 = Vector3FracFactory.getInstance(2, 2, 2);
	vtx.p3 = Vector3FracFactory.getInstance(2, -2, 2);
	pnt = Vector3FracFactory.getInstance(2, 0, 2);

	System.out.println(vtx + "\t" + pnt);
	System.out.println(vtx.isPointInVertex(pnt) ? "True" : "False");
    }
}
