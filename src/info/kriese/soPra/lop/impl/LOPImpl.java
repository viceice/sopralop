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
 * 07.10.2007 - Version 0.2.2
 * - OptimalVectors hinzugefügt (Rückgabe der Vektoren, die das Optimum aufspannen)
 * 03.10.2007 - Version 0.2.1
 * - showDualProblem hinzugefügt
 * - showPrimalProblem hinzugefügt
 * 11.09.2007 - Version 0.2
 *  - showSolution hizugefügt
 * 23.08.2007 - Version 0.1
 *  - Datei hinzugefuegt
 */
package info.kriese.soPra.lop.impl;

import info.kriese.soPra.exceptions.NotAllowedException;
import info.kriese.soPra.lop.LOP;
import info.kriese.soPra.lop.LOPListener;
import info.kriese.soPra.lop.LOPSolution;
import info.kriese.soPra.math.Vector3Frac;
import info.kriese.soPra.math.impl.Vector3FracFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 
 * @author Michael Kriese
 * @version 0.2.1
 * @since 23.08.2007
 * 
 */
final class LOPImpl implements LOP {

    private final Collection<LOPListener> listeners;

    private boolean max;

    private String[] operators;

    private LOPSolution solution;

    private Vector3Frac target;

    private final List<Vector3Frac> vectors;

    public LOPImpl() {
	this.target = Vector3FracFactory.getInstance();
	this.vectors = new ArrayList<Vector3Frac>();
	this.vectors.add(Vector3FracFactory.getInstance(1, 0, 0));
	this.vectors.add(Vector3FracFactory.getInstance(0, 1, 0));
	this.vectors.add(Vector3FracFactory.getInstance(0, 0, 1));
	this.operators = new String[] { "=", "=" };
	this.max = true;
	this.solution = null;

	this.listeners = new ArrayList<LOPListener>();
    }

    public void addProblemListener(LOPListener listener) {
	this.listeners.add(listener);
    }

    public String[] getOperators() {
	return this.operators;
    }

    public LOPSolution getSolution() {
	if (this.solution == null)
	    this.solution = new LOPSolutionImpl(this);
	return this.solution;
    }

    public Vector3Frac getTarget() {
	return this.target;
    }

    public List<Vector3Frac> getVectors() {
	return this.vectors;
    }

    public boolean isMaximum() {
	return this.max;
    }

    public boolean isMinimum() {
	return !this.max;
    }

    public boolean isSolved() {
	return this.solution != null;
    }

    public void problemChanged() {
	this.solution = null;
	for (LOPListener listener : this.listeners)
	    listener.problemChanged(this);
    }

    public void problemSolved() {
	for (LOPListener listener : this.listeners)
	    listener.problemSolved(this);
    }

    public void removeProblemListener(LOPListener listener) {
	this.listeners.remove(listener);
    }

    public void setMaximum(boolean max) {
	this.max = max;
    }

    public void setMinimum(boolean min) {
	this.max = !min;
    }

    public void setOperators(String[] operators) {
	if (operators == null || operators.length != 2)
	    throw new NotAllowedException(operators);
	this.operators = operators;
    }

    public void setTarget(Vector3Frac target) {
	if (target == null)
	    throw new NotAllowedException(null);
	this.target = target;
    }

    public void setVectors(List<Vector3Frac> vectors) {
	if (vectors == null | vectors.size() < 3)
	    throw new NotAllowedException(vectors);
	this.vectors.clear();
	this.vectors.addAll(vectors);
    }

    public void showDualProblem() {
	for (LOPListener listener : this.listeners)
	    listener.showDualProblem(this);
    }

    public void showPrimalProblem() {
	for (LOPListener listener : this.listeners)
	    listener.showPrimalProblem(this);
    }

    public void showSolution() {
	for (LOPListener listener : this.listeners)
	    listener.showSolution(this);
    }

}
