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
 * 27.07.2007 - Version 0.1.1
 *  - Nervige Wahrnungen entfernt (Getter-Methoden hinzugefuegt)
 * 04.06.2007 - Version 0.1
 * - Datei hinzugefuegt
 */
package info.kriese.soPra.math;

import java.util.Vector;


/**
 * Erzeugung des dualen Problems
 * 
 * @author Peer Sterner
 * 
 * @since 04.06.2007
 * @version 0.1.1
 */
public final class DualProblem {

	private Vector<Vector3Frac> vectors;

	private Vector3Frac target;

	private char[] ops;

	private String[] newOps = new String[3];

	private String minMax;

	private int numVar;

	private int minMaxToInteger;

	public void generateDualProblem(Vector<Vector3Frac> vectors,
			Vector3Frac target, char[] ops, String minMax) {

		numVar = vectors.size();
		minMaxToInteger = maxTemp(minMax);
		newOps = generateNewOps(ops);

		switch (minMaxToInteger) {
		case 0: // Primales Problem ist Minimierung
			System.out.println("(" + target.getCoordX() + ")y1 + ("
					+ target.getCoordY() + ")y2 = Max!");
			for (int j = 0; j < numVar; j++) {
				System.out.println("(" + vectors.elementAt(j).getCoordX()
						+ ")y1 + (" + vectors.elementAt(j).getCoordY()
						+ ")y2 =< " + vectors.elementAt(j).getCoordZ());
			}
			if (ops[1] == '=')
				System.out.println("y1 beliebig");
			else System.out.println("y1 " + ops[1] + "= 0");
			if (ops[2] == '=')
				System.out.println("y2 beliebig");
			else System.out.println("y1 " + ops[1] + "= 0");
			break;

		case 1: // Primales Problem ist Maximierung
			System.out.println("(" + target.getCoordX() + ")y1 + ("
					+ target.getCoordY() + ")y2 = Min!");

			for (int i = 0; i < numVar; i++) {
				System.out.println("(" + vectors.elementAt(i).getCoordX()
						+ ")y1 + (" + vectors.elementAt(i).getCoordY()
						+ ")y2 >= " + vectors.elementAt(i).getCoordZ());
			}

			System.out.println("y1" + newOps[1]);
			System.out.println("y2" + newOps[2]);
			break;

		default:
			System.out.println("(" + target.getCoordX() + ")y1 + ("
					+ target.getCoordY() + ")y2 = Min!");

			for (int i = 0; i < numVar; i++) {
				System.out.println("(" + vectors.elementAt(i).getCoordX()
						+ ")y1 + (" + vectors.elementAt(i).getCoordY()
						+ ")y2 >= " + vectors.elementAt(i).getCoordZ());
			}

			System.out.println("y1" + newOps[1]);
			System.out.println("y2" + newOps[2]);
			break;
		}

	}

	private int maxTemp(String minMax) {
		if (minMax == "max")
			return 1;
		else
			return 0;
	}

	private String[] generateNewOps(char[] ops) {
		newOps[0] = "=";
		for (int i = 1; i < 3; i++) {
			if (ops[i] == '>')
				newOps[i] = " =< 0";
			else if (ops[i] == '<')
				newOps[i] = " >= 0";
			else if (ops[i] == '=')
				newOps[i] = " beliebig ";
		}
		return newOps;
	}

	/**
	 * @return the minMax
	 */
	public String getMinMax() {
		return minMax;
	}

	/**
	 * @return the ops
	 */
	public char[] getOps() {
		return ops;
	}

	/**
	 * @return the target
	 */
	public Vector3Frac getTarget() {
		return target;
	}

	/**
	 * @return the vectors
	 */
	public Vector<Vector3Frac> getVectors() {
		return vectors;
	}

}
