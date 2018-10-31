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
 * 01.11.2007 - Version 0.2
 * - An LOPEditor angepasst
 * 23.08.2007 - Version 0.1
 *  - Datei hinzugefuegt
 */
package info.kriese.sopra.lop.impl;

import info.kriese.sopra.lop.LOP;
import info.kriese.sopra.lop.LOPEditor;

/**
 * 
 * @author Michael Kriese
 * @version 0.2
 * @since 23.08.2007
 * 
 */
public final class LOPFactory {

    /**
     * Generiert ein neues lineares Optimierungsproblem
     * 
     * @return Lineares Optimierungsproblem
     */
    public static LOP newLinearOptimizingProblem() {
	return new LOPImpl();
    }

    public static LOPEditor newLOPEditor(LOP lop) {
	return new LOPEditorImpl(lop);
    }

    protected LOPFactory() {
    }
}
