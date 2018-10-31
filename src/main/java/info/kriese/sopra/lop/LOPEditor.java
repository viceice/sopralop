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
 * 20.05.2008 - Version 0.4
 * - neue Methode captureImage
 * 03.12.2007 - Verison 0.3
 * - Methoden check, isEdited & setEdited hinzugefügt
 * 06.11.2007 - Version 0.2
 * - getLOP hizugefügt
 * 01.11.2007 - Version 0.1
 *  - Datei hinzugefuegt
 */
package info.kriese.sopra.lop;

import java.io.File;
import java.net.URL;

/**
 * 
 * @author Michael Kriese
 * @version 0.4
 * @since 01.11.2007
 * 
 */
public interface LOPEditor {
    void addListener(LOPEditorListener l);

    void addVariable();

    void captureImage(File file);

    void check();

    void clear();

    LOP getLOP();

    boolean isEdited();

    void open(URL file);

    void removeListener(LOPEditorListener l);

    void removeVariable();

    void save(URL file);

    void setEdited(boolean value);

    void solve();

    void take();

    void update();
}
