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
 * 20.05.2008 - Version 0.3
 * - neue Methode captureImage
 * 03.12.2007 - Verison 0.2
 * - Methode check hinzugefügt
 * 01.11.2007 - Version 0.1
 * - Datei hinzugefuegt
 */
package info.kriese.sopra.lop;

import java.io.File;
import java.net.URL;

/**
 * 
 * @author Michael Kriese
 * @version 0.3
 * @since 01.11.2007
 * 
 */
public interface LOPEditorListener {

    void addVariable(LOP lop);

    void captureImage(LOP lop, File file);

    void check(LOP lop);

    void clear(LOP lop);

    boolean open(LOP lop, URL file);

    void removeVariable(LOP lop);

    void save(LOP lop, URL file);

    void solve(LOP lop);

    boolean take(LOP lop);

    void update(LOP lop);
}
