package info.kriese.soPra.test;

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
 * 16.10.2007 - Version 0.1.1
 * - Aufruf der Gauss-Elimination angepasst
 * 02.05.2007 - Version 0.1
 * - Datei hinzugefuegt
 */
import info.kriese.soPra.math.Gauss;
import info.kriese.soPra.math.Vector3Frac;
import info.kriese.soPra.math.impl.Vector3FracFactory;

/**
 * @author Peer Sterner
 * @version 0.1
 * @since 02.05.2007
 */
public class TestGauss {

    /**
     * @param args
     */
    public static void main(String[] args) {
	Vector3Frac l1 = Vector3FracFactory.getInstance(3, 4, 24);
	Vector3Frac l2 = Vector3FracFactory.getInstance(2, 1, 8);
	Vector3Frac l3 = Vector3FracFactory.getInstance(0, 1, 2);
	Vector3Frac target = Vector3FracFactory.getInstance(10, 10, 0);

	Gauss g = new Gauss();

	Vector3Frac s = g.gaussElimination(l1, l2, target);

	System.out.println("x1= " + s.getCoordX() + ", x2= " + s.getCoordY()
		+ ", z= " + s.getCoordZ());

    }

}
