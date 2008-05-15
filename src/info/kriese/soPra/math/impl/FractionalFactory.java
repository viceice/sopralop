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
 * 15.05.2008 - Version 0.1.3
 * - Ein "+" am Anfang wird jetzt auch akzeptiert
 * 04.11.2007 - Version 0.1.2
 * - Ein Double String wird jetzt auch als Fractional geparsed
 * 03.10.2007 - Version 0.1.1
 * - BugFix: Fractional wurde nicht korrekt instanziert, falls im übergebenen
 * 	String kein Slash vorhanden war
 * 17.09.2007 - Version 0.1
 *  - Datei hinzugefuegt
 */
package info.kriese.soPra.math.impl;

import info.kriese.soPra.math.Fractional;
import info.kriese.soPra.math.Math2;

/**
 * 
 * @author Michael Kriese
 * @version 0.1.3
 * @since 17.09.2007
 * 
 */
public final class FractionalFactory {

    public static Fractional getInstance() {
	return getInstance(0);
    }

    public static Fractional getInstance(double ans) {

	return getInstance((int) Math.round(ans * Math2.EPSILON),
		Math2.EPSILON_INT);
    }

    public static Fractional getInstance(int num) {
	return getInstance(num, 1);
    }

    public static Fractional getInstance(int num, int denom) {
	if (num == 0 || denom == 0) {
	    num = 0;
	    denom = 1;
	} else if ((num < 0) && (denom < 0)) {
	    num = Math.abs(num);
	    denom = Math.abs(denom);
	} else if (denom < 0) {
	    num = 0 - num;
	    denom = Math.abs(denom);
	}
	return new FractionalImpl(num, denom);
    }

    public static Fractional getInstance(String frac) {
	int num = 0;
	int denom = 0;

	try {
	    if (frac == null || "".equals(frac))
		return getInstance();

	    if (frac.startsWith("+"))
		frac = frac.substring(1);

	    if (frac.contains("."))
		return getInstance(Double.parseDouble(frac));

	    String[] split = frac.split("/");

	    if (split.length != 2)
		denom = 1;
	    else
		denom = Integer.parseInt(split[1]);

	    num = Integer.parseInt(split[0]);

	    return getInstance(num, denom);
	} catch (RuntimeException e) {
	}
	return null;
    }

}
