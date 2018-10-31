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
 * 17.05.2008 - Version 0.1
 *  - Datei hinzugefuegt
 */
package info.kriese.sopra.gui.filechooser;

import info.kriese.sopra.gui.lang.Lang;
import info.kriese.sopra.io.IOUtils;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * 
 * @author Michael Kriese
 * @version 0.1
 * @since 17.05.2008
 * 
 */
public final class BMPFilter extends FileFilter {

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
     */
    @Override
    public boolean accept(File f) {
	if (f == null)
	    return false;

	if (f.isDirectory())
	    return true;

	return IOUtils.checkExtension(f, "bmp");
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.filechooser.FileFilter#getDescription()
     */
    @Override
    public String getDescription() {
	return Lang.getString("Files.BMP") + " (*.bmp)";
    }

}
