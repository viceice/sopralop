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
 * 23.10.2007 - Version 0.1.2
 * - solve entfert,  wird jetzt implizit aufgerufen
 * 02.10.2007 - Version 0.1.1
 * - Einige Infos aus den Settings laden
 * 15.09.2007 - Version 0.1
 *  - Datei hinzugefuegt
 */
package info.kriese.soPra.test;

import info.kriese.soPra.io.IOUtils;
import info.kriese.soPra.io.impl.SettingsFactory;
import info.kriese.soPra.lop.LOP;
import info.kriese.soPra.lop.LOPEditor;
import info.kriese.soPra.lop.impl.LOPFactory;
import info.kriese.soPra.math.LOPSolver;

/**
 * 
 * @author Michael Kriese
 * @version 0.1.2
 * @since 15.09.2007
 * 
 */
public final class TestSolve {

    public static void main(String[] args) {
	LOP lop = LOPFactory.newLinearOptimizingProblem();
	LOPEditor editor = LOPFactory.newLOPEditor(lop);
	LOPSolver solver = new LOPSolver();
	solver.setEditor(editor);

	System.out.println("SoPraLOP Test - Version "
		+ SettingsFactory.getInstance().getVersion());
	System.out.println("\t(c) 2007 "
		+ SettingsFactory.getInstance().getAuthor());
	System.out.println();
	editor.open(IOUtils.getURL("problems/unlimited_high2.lop"));
	System.out.println();
	System.out.println("Problem: ");
	IOUtils.print(lop, System.out);
    }
}
