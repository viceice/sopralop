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
 * 19.12.2007 - Version 0.2
 * - isDebug hinzugefügt
 * 29.07.2007 - Version 0.1
 *  - Datei hinzugefuegt
 */
package info.kriese.soPra.io;

/**
 * 
 * @author Michael Kriese
 * @version 0.2
 * @since 29.07.2007
 * 
 */
public interface Settings {

    String getAuthor();

    String getDescription();

    String getGPG();

    String getMail();

    String getName();

    String getTitle();

    String getVersion();

    String getWeb();

    boolean isDebug();
}
