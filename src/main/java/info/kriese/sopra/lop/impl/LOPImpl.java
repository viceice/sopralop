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
 * 26.01.2008 - Version 0.3
 * - An neues Interface angepasst (einige Methoden entfernt)
 * - Zugriff auf Listener erfolgt jetzt asynchron
 * - Exceptions in IllegalArgumentException geändert
 * 09.11.2007 - Version 0.2.3
 * - Nachdem ein Problem gelöst wird, wird automatisch wieder das primale
 *    Problem angezeigt
 * 07.10.2007 - Version 0.2.2
 * - OptimalVectors hinzugefügt (Rückgabe der Vektoren, die das Optimum
 *    aufspannen)
 * 03.10.2007 - Version 0.2.1
 * - showDualProblem hinzugefügt
 * - showPrimalProblem hinzugefügt
 * 11.09.2007 - Version 0.2
 *  - showSolution hizugefügt
 * 23.08.2007 - Version 0.1
 *  - Datei hinzugefuegt
 */
package info.kriese.sopra.lop.impl;

import info.kriese.sopra.gui.lang.Lang;
import info.kriese.sopra.lop.LOP;
import info.kriese.sopra.lop.LOPListener;
import info.kriese.sopra.lop.LOPSolution;
import info.kriese.sopra.math.Vector3Frac;
import info.kriese.sopra.math.impl.Vector3FracFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.SwingUtilities;

/**
 * 
 * @author Michael Kriese
 * @version 0.3
 * @since 23.08.2007
 * 
 */
final class LOPImpl implements LOP {

    private final Collection<LOPListener> listeners;

    private boolean max;

    private final LOPSolution solution;

    private Vector3Frac target;

    private final List<Vector3Frac> vectors;

    public LOPImpl() {
	this.target = Vector3FracFactory.getInstance();
	this.vectors = new ArrayList<Vector3Frac>();
	this.vectors.add(Vector3FracFactory.getInstance(1, 0, 0));
	this.vectors.add(Vector3FracFactory.getInstance(0, 1, 0));
	this.vectors.add(Vector3FracFactory.getInstance(0, 0, 1));
	this.max = true;
	this.solution = new LOPSolutionImpl(this);

	this.listeners = Collections
		.synchronizedCollection(new LinkedList<LOPListener>());
    }

    public void addProblemListener(final LOPListener listener) {
	SwingUtilities.invokeLater(new Runnable() {
	    public void run() {
		synchronized (LOPImpl.this.listeners) {
		    LOPImpl.this.listeners.add(listener);
		}
	    }
	});
    }

    public LOPSolution getSolution() {
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

    public void problemSolved() {
	SwingUtilities.invokeLater(new Runnable() {
	    public void run() {
		synchronized (LOPImpl.this.listeners) {
		    for (LOPListener listener : LOPImpl.this.listeners)
			listener.problemSolved(LOPImpl.this);
		}
		showPrimalProblem();
	    }
	});
    }

    public void removeProblemListener(final LOPListener listener) {
	SwingUtilities.invokeLater(new Runnable() {
	    public void run() {
		synchronized (LOPImpl.this.listeners) {
		    LOPImpl.this.listeners.remove(listener);
		}
	    }
	});
    }

    public void setMaximum(boolean max) {
	this.max = max;
    }

    public void setMinimum(boolean min) {
	this.max = !min;
    }

    public void setTarget(Vector3Frac target) {
	if (target == null)
	    throw new IllegalArgumentException(Lang
		    .getString("Errors.TargetNotNull"));
	this.target = target;
    }

    public void setVectors(List<Vector3Frac> vectors) {
	if (vectors == null | vectors.size() < MIN_VECTORS
		|| vectors.size() > MAX_VECTORS)
	    throw new IllegalArgumentException(Lang.getString(
		    "Errors.MinMaxVectors", new Object[] { MIN_VECTORS,
			    MAX_VECTORS }));
	this.vectors.clear();
	this.vectors.addAll(vectors);
    }

    public void showDualProblem() {
	SwingUtilities.invokeLater(new Runnable() {
	    public void run() {
		synchronized (LOPImpl.this.listeners) {
		    for (LOPListener listener : LOPImpl.this.listeners)
			listener.showDualProblem(LOPImpl.this);
		}
	    }
	});
    }

    public void showPrimalProblem() {
	SwingUtilities.invokeLater(new Runnable() {
	    public void run() {
		synchronized (LOPImpl.this.listeners) {
		    for (LOPListener listener : LOPImpl.this.listeners)
			listener.showPrimalProblem(LOPImpl.this);
		}
	    }
	});
    }
}
