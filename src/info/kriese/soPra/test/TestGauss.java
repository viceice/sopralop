/**
 * @version		$Id$
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
 * 08.11.2007 - Version 0.2.1
 * - Neue Testgleichung
 * - An Vereinfachte Vector3Frac Erstallung angepasst
 * 07.11.2007 - Version 0.2
 * - Tests erweitert, Lösungen werden mit handgerechneten Lösungen verglichen
 * 16.10.2007 - Version 0.1.1
 * - Aufruf der Gauss-Elimination angepasst
 * 02.05.2007 - Version 0.1
 * - Datei hinzugefuegt
 */
package info.kriese.soPra.test;

import info.kriese.soPra.io.impl.SettingsFactory;
import info.kriese.soPra.math.Gauss;
import info.kriese.soPra.math.Vector3Frac;
import info.kriese.soPra.math.Vertex;
import info.kriese.soPra.math.impl.Vector3FracFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Peer Sterner
 * @version 0.2.1
 * @since 02.05.2007
 */
public class TestGauss {

    private static List<Vector3Frac> sols = new ArrayList<Vector3Frac>(),
	    pnts = new ArrayList<Vector3Frac>();
    private static List<Vertex> vertices = new ArrayList<Vertex>();

    /**
     * @param args
     */
    public static void main(String[] args) {
	System.out.println("SoPraLOP GaussianTest - Version "
		+ SettingsFactory.getInstance().getVersion());
	System.out.println("\t(c) 2007  "
		+ SettingsFactory.getInstance().getAuthor());
	System.out.println();

	Vertex vert;
	Vector3Frac pnt, sol, tmp;

	vert = new Vertex();
	vert.p1 = Vector3FracFactory.getInstance(3, 4, 24);
	vert.p2 = Vector3FracFactory.getInstance(2, 1, 8);
	vert.p3 = Vector3FracFactory.getInstance(0, 1, 2);
	vertices.add(vert);
	pnts.add(Vector3FracFactory.getInstance(10, 10, 0));
	sols.add(Vector3FracFactory.getInstance("1/2", "-5/2", "71"));

	vert = new Vertex();
	vert.p1 = Vector3FracFactory.getInstance(0, 0, 0);
	vert.p2 = Vector3FracFactory.getInstance(2, 1, 8);
	vert.p3 = Vector3FracFactory.getInstance(0, 1, 2);
	vertices.add(vert);
	pnts.add(Vector3FracFactory.getInstance(10, 10, 0));
	sols.add(Vector3FracFactory.getInstance(5, 5, 50));

	vert = new Vertex();
	vert.p1 = Vector3FracFactory.getInstance(0, 0, 0);
	vert.p2 = Vector3FracFactory.getInstance(-2, 2, 2);
	vert.p3 = Vector3FracFactory.getInstance(2, 2, 2);
	vertices.add(vert);
	pnts.add(Vector3FracFactory.getInstance(-2, 0, 0));
	sols.add(Vector3FracFactory.getInstance("1/2", "-1/2", "0"));

	vert = new Vertex();
	vert.p1 = Vector3FracFactory.getInstance(0, 0, 0);
	vert.p2 = Vector3FracFactory.getInstance(1, 0, 0);
	vert.p3 = Vector3FracFactory.getInstance(0, 0, 1);
	vertices.add(vert);
	pnts.add(Vector3FracFactory.getInstance(1, 1, 0));
	sols.add(Vector3FracFactory.getInstance(-1, -1, 0));

	vert = new Vertex();
	vert.p1 = Vector3FracFactory.getInstance(0, 1, 2);
	vert.p2 = Vector3FracFactory.getInstance(2, 1, 8);
	vert.p3 = Vector3FracFactory.getInstance(3, 4, 24);
	vertices.add(vert);
	pnts.add(Vector3FracFactory.getInstance(10, 10, 0));
	sols.add(Vector3FracFactory.getInstance("1/2", "3", "71"));

	vert = new Vertex();
	vert.p1 = Vector3FracFactory.getInstance(2, 1, 8);
	vert.p2 = Vector3FracFactory.getInstance(0, 1, 2);
	vert.p3 = Vector3FracFactory.getInstance(3, 4, 24);
	vertices.add(vert);
	pnts.add(Vector3FracFactory.getInstance(10, 10, 0));
	sols.add(Vector3FracFactory.getInstance("-5/2", "3", "71"));

	vert = new Vertex();
	vert.p1 = Vector3FracFactory.getInstance(-2, 2, 2);
	vert.p2 = Vector3FracFactory.getInstance(2, -2, 2);
	vert.p3 = Vector3FracFactory.getInstance(2, 2, 2);
	vertices.add(vert);
	pnts.add(Vector3FracFactory.getInstance(1, 1, 0));
	sols.add(Vector3FracFactory.getInstance("1/4", "1/2", "2"));

	for (int i = 0; i < vertices.size(); i++) {
	    vert = vertices.get(i);
	    pnt = pnts.get(i);
	    sol = sols.get(i);
	    tmp = Gauss.eliminate(vert, pnt);
	    System.out.println(vert + " x  " + pnt + " = " + tmp
		    + (tmp.equals(sol) ? " -> richtig" : " -> falsch " + sol));
	}

    }
}
