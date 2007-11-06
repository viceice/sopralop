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
 * 06.11.2007 - Version 0.3
 * - Neue Interfacemethoden implementiert
 * 24.09.2007 - Version 0.2.1
 * - Interface Änderungen implementiert
 * 16.09.2007 - Version 0.2
 * - Interface Änderungen implementiert
 * 23.08.2007 - Version 0.1
 *  - Datei hinzugefuegt
 */
package info.kriese.soPra.lop.impl;

import java.util.ArrayList;
import java.util.List;

import info.kriese.soPra.exceptions.NotAllowedException;
import info.kriese.soPra.lop.LOP;
import info.kriese.soPra.lop.LOPSolution;
import info.kriese.soPra.lop.LOPSolutionArea;
import info.kriese.soPra.math.Fractional;
import info.kriese.soPra.math.Vector3Frac;
import info.kriese.soPra.math.impl.FractionalFactory;

/**
 * 
 * @author Michael Kriese
 * @version 0.3
 * @since 23.08.2007
 * 
 */
final class LOPSolutionImpl implements LOPSolution {

    private final List<LOPSolutionArea> areas;

    private final LOP problem;

    private int sCase;

    private Vector3Frac value;

    public LOPSolutionImpl(LOP problem) {
	this.value = null;
	this.sCase = SIMPLE;
	this.problem = problem;
	this.areas = new ArrayList<LOPSolutionArea>();
    }

    public void addArea(Vector3Frac l1, Vector3Frac l2, Fractional f1,
	    Fractional f2) {
	if (l1 == null || l2 == null || f1 == null || f2 == null)
	    throw new IllegalArgumentException();

	this.areas.add(new LOPSolutionAreaImpl(l1, l2, f1, f2));
    }

    public void clearAreas() {
	this.areas.clear();
    }

    public int countAreas() {
	return this.areas.size();
    }

    public List<LOPSolutionArea> getAreas() {
	return this.areas;
    }

    public LOP getProblem() {
	return this.problem;
    }

    public int getSpecialCase() {
	return this.sCase;
    }

    public double getValue() {
	return this.value.getCoordZ().toDouble();
    }

    public Vector3Frac getVector() {
	return this.value.clone();
    }

    public boolean isSpecialCase() {
	return this.sCase != SIMPLE;
    }

    public void setSpecialCase(int sCase) {
	if (sCase < 0)
	    throw new NotAllowedException(sCase);
	this.sCase = sCase;
    }

    public void setValue(double value) {
	setValue(FractionalFactory.getInstance(value));
    }

    public void setValue(Fractional value) {
	if (value == null)
	    throw new NotAllowedException();
	Vector3Frac vec = this.problem.getTarget().clone();
	vec.setCoordZ(value);
	setValue(vec);
    }

    public void setValue(Vector3Frac value) {
	if (value == null)
	    throw new NotAllowedException();
	this.value = value.clone();
    }
}