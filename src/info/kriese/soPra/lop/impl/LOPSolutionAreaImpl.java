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
 * 27.01.2007 - Version 0.4
 * - Objekte vergleichbar gemacht
 * 06.11.2007 - Version 0.3
 * - Neue Interfacemethoden implementiert
 * 11.10.2007 - Version 0.2
 * - toString Methode überladen
 * 09.10.2007 - Version 0.1
 *  - Datei hinzugefuegt
 */
package info.kriese.soPra.lop.impl;

import info.kriese.soPra.lop.LOPSolutionArea;
import info.kriese.soPra.math.Fractional;
import info.kriese.soPra.math.Vector3Frac;

/**
 * 
 * @author Michael Kriese
 * @version 0.4
 * @since 09.10.2007
 * 
 */
public final class LOPSolutionAreaImpl implements LOPSolutionArea {

    private final Vector3Frac l1, l2;

    private final Fractional l1a, l2a;

    protected LOPSolutionAreaImpl(Vector3Frac l1, Vector3Frac l2,
	    Fractional f1, Fractional f2) {
	this.l1 = l1;
	this.l2 = l2;
	this.l1a = f1;
	this.l2a = f2;
    }

    public boolean equals(LOPSolutionArea obj) {
	return this.l1.equals(obj.getL1()) && this.l2.equals(obj.getL2())
		&& this.l1a.equals(obj.getL1Amount())
		&& this.l2a.equals(obj.getL2Amount());
    }

    @Override
    public boolean equals(Object obj) {
	if (obj instanceof LOPSolutionArea)
	    return equals((LOPSolutionArea) obj);
	return super.equals(obj);
    }

    public Vector3Frac getL1() {
	return this.l1;
    }

    public Fractional getL1Amount() {
	return this.l1a;
    }

    public Vector3Frac getL2() {
	return this.l2;
    }

    public Fractional getL2Amount() {
	return this.l2a;
    }

    @Override
    public String toString() {
	return "[ " + this.l1 + " | " + this.l2 + " ] (" + this.l1a + " | "
		+ this.l2a + ")";
    }

}
