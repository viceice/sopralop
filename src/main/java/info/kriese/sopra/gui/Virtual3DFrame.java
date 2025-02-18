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
 * 29.07.2007
 * - in gui Package verschoben
 * 11.05.2007 - Version 0.1
 * - Datei hinzugefuegt
 */
package info.kriese.sopra.gui;

import org.jogamp.java3d.Canvas3D;

/**
 * Interface zum verbinden von 3D-Engine und Oberfläche.
 * 
 * @author Michael Kriese
 * @version 0.1
 * @since 11.05.2007
 */
public interface Virtual3DFrame {

    /**
     * Fügt der Oberfläche eine 3D-Zeichenfläche hinzu.
     * 
     * @param canvas -
     *                3D-Zeichenfläche.
     */
    void addCanvas(Canvas3D canvas);
}
