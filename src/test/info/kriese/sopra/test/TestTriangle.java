/**
 * @version		$Id$
 * @copyright	(c)2007-2008 Michael Kriese & Peer Sterner
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
 * 26.01.2008 - Version 0.1.1
 * - TestCase erweitert
 * 23.10.2007 - Version 0.1
 *  - Datei hinzugefuegt
 */
package info.kriese.sopra.test;

import info.kriese.sopra.io.impl.SettingsFactory;
import info.kriese.sopra.math.Vector3Frac;
import info.kriese.sopra.math.Vertex;
import info.kriese.sopra.math.impl.Vector3FracFactory;

/**
 * 
 * @author Michael Kriese
 * @version 0.1.1
 * @since 23.10.2007
 * 
 */
public final class TestTriangle {

    public static void main(String[] args) {

	// Parse commandline arguments
	SettingsFactory.parseArgs(args);

	SettingsFactory.initJava();

	SettingsFactory.showTitle("TriangleTest");

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

	vtx = new Vertex();
	vtx.p2 = Vector3FracFactory.getInstance(2, 2, 2);
	vtx.p3 = Vector3FracFactory.getInstance(2, -2, 2);
	pnt = Vector3FracFactory.getInstance(4, 0.5, 4);

	System.out.println(vtx + "\t" + pnt);
	System.out.println(vtx.isPointInVertex(pnt) ? "True" : "False");
    }
}
