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
 * 26.01.2008 - Version 0.1.3
 * - An neue SettingsFactory angepasst.
 * 23.10.2007 - Version 0.1.2
 * - solve entfert,  wird jetzt implizit aufgerufen
 * 02.10.2007 - Version 0.1.1
 * - Einige Infos aus den Settings laden
 * 15.09.2007 - Version 0.1
 *  - Datei hinzugefuegt
 */
package info.kriese.sopra.test;

import info.kriese.sopra.io.IOUtils;
import info.kriese.sopra.io.impl.SettingsFactory;
import info.kriese.sopra.lop.LOP;
import info.kriese.sopra.lop.LOPEditor;
import info.kriese.sopra.lop.impl.LOPFactory;
import info.kriese.sopra.math.LOPSolver;

/**
 * 
 * @author Michael Kriese
 * @version 0.1.3
 * @since 15.09.2007
 * 
 */
public final class TestSolve {

    public static void main(String[] args) {
	// Parse commandline arguments
	SettingsFactory.parseArgs(args);
	SettingsFactory.setDebug(true);

	SettingsFactory.initJava();

	SettingsFactory.showTitle("SolveTest");

	LOP lop = LOPFactory.newLinearOptimizingProblem();
	LOPEditor editor = LOPFactory.newLOPEditor(lop);
	LOPSolver solver = new LOPSolver();
	solver.setEditor(editor);

	editor.open(IOUtils.getURL("problems/ray_solution2.lop"));
    }
}
