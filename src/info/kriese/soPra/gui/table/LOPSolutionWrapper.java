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
 * 19.12.2007 - Version 0.1.1
 * - equals überladen.
 * 04.12.2007 - Version 0.1
 *  - Datei hinzugefuegt
 */
package info.kriese.soPra.gui.table;

import info.kriese.soPra.math.Fractional;
import info.kriese.soPra.math.impl.FractionalFactory;

/**
 * 
 * @author Michael Kriese
 * @version 0.1.1
 * @since 04.12.2007
 * 
 */
public final class LOPSolutionWrapper {

    public static String NotExistent = "ne";
    public static String Unlimited = "u";

    public static LOPSolutionWrapper getInstance() {
	return getInstance(null);
    }

    public static LOPSolutionWrapper getInstance(String text) {
	if (text == null)
	    return new LOPSolutionWrapper(FractionalFactory.getInstance());

	else if (text.equals(NotExistent))
	    return new LOPSolutionWrapper(LOPNotExsitent.NOT_EXISTENT);
	else if (text.equals(Unlimited))
	    return new LOPSolutionWrapper(LOPInfinity.INFINITY);
	else {
	    Fractional frac = FractionalFactory.getInstance(text);
	    if (frac != null)
		return new LOPSolutionWrapper(frac);
	}

	return null;
    }

    private final Object value;

    private LOPSolutionWrapper(Object value) {
	this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
	return this.value.equals(obj);
    }

    public Class<?> getType() {
	return this.value.getClass();
    }

    public Object getValue() {
	return this.value;
    }

    @Override
    public String toString() {
	return this.value.toString();
    }
}
