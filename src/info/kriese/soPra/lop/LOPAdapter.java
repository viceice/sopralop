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
 * 03.10.2007 - Version 0.1
 *  - Datei hinzugefuegt
 */
package info.kriese.soPra.lop;

/**
 * 
 * @author Michael Kriese
 * @version 0.1
 * @since 03.10.2007
 * 
 */
public abstract class LOPAdapter implements
	LOPListener {

    public void problemChanged(LOP lop) {
    }

    public void problemSolved(LOP lop) {
    }

    public void showDualProblem(LOP lop) {
    }

    public void showPrimalProblem(LOP lop) {
    }

    public void showSolution(LOP lop) {
    }

}
