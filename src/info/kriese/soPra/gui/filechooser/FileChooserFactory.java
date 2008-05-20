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
package info.kriese.soPra.gui.filechooser;

import info.kriese.soPra.io.IOUtils;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 * Stellt Dialoge zum auswählen von Dateien bereit.
 * 
 * @author Michael Kriese
 * @version 0.1
 * @since 17.05.2008
 * 
 */
public final class FileChooserFactory {

    /**
     * Konstante für Bildtyp.
     */
    public static final int FILETYP_IMAGE = 0x1;

    /**
     * Konstante für LOP-typ.
     */
    public static final int FILETYP_LOP = 0x0;

    /*
     * FileChosserdialog, der benutzt wird
     */
    private static final JFileChooser FC = new JFileChooser();

    /**
     * Dateifilter für BMP's
     */
    private static final FileFilter FILTER_BMP = new BMPFilter();

    /**
     * Dateifilter für GIF's
     */
    private static final FileFilter FILTER_GIF = new GIFFilter();

    /**
     * Dateifilter für JPG's
     */
    private static final FileFilter FILTER_JPG = new JPGFilter();

    /**
     * Dateifilter für LOP's
     */
    private static final FileFilter FILTER_LOP = new LOPFilter();

    /**
     * Dateifilter für PNG's
     */
    private static final FileFilter FILTER_PNG = new PNGFilter();

    /**
     * Parent-Komponente, vor der der Dialog angezeigt werden soll.
     */
    private static Component PARENT = null;

    /**
     * Zeigt ein Dialog zum Datei öffnen an.
     * 
     * @param typ -
     *                Dialogtyp, der angezeigt werden soll
     * @return Die ausgewählte Datei falls vorhanden, sonst "NULL"
     */
    public static File open(int typ) {
	setFilter(typ);

	if (FC.showOpenDialog(PARENT) == JFileChooser.APPROVE_OPTION)
	    return FC.getSelectedFile();

	return null;
    }

    /**
     * Zeigt ein Dialog zum Datei speichern an.
     * 
     * @param typ -
     *                Dialogtyp, der angezeigt werden soll
     * @return Die ausgewählte Datei falls vorhanden, sonst "NULL"
     */
    public static File save(int typ) {
	setFilter(typ);

	if (FC.showSaveDialog(PARENT) == JFileChooser.APPROVE_OPTION)
	    return checkFile(FC.getSelectedFile(), FC.getFileFilter());

	return null;
    }

    /**
     * Setzt die Parent-Komponente für die Dialoge.
     * 
     * @param parent -
     *                Parent-Komponente, vor der die Dialoge angezeigt werden
     *                sollen
     */
    public static void setParent(Component parent) {
	PARENT = parent;
    }

    /**
     * Überprüft, ob die Datei dem Filter entspricht, falls nicht wird die
     * entsprechende Erweiterung angehangen.
     * 
     * @param file -
     *                Datei, die geprüft werden soll.
     * @param filter -
     *                Filter, mit dem getestet werden soll.
     * @return Die an den Filter angepasste Datei.
     */
    private static File checkFile(File file, FileFilter filter) {

	if (filter == FILTER_LOP && !IOUtils.checkExtension(file, "lop"))
	    return new File(file.getAbsoluteFile() + ".lop");

	else if (filter == FILTER_GIF && !IOUtils.checkExtension(file, "gif"))
	    return new File(file.getAbsoluteFile() + ".gif");

	else if (filter == FILTER_PNG && !IOUtils.checkExtension(file, "png"))
	    return new File(file.getAbsoluteFile() + ".png");

	else if (filter == FILTER_JPG && !IOUtils.checkExtension(file, "jpg")
		&& !IOUtils.checkExtension(file, "jpeg"))
	    return new File(file.getAbsoluteFile() + ".jpg");

	else if (filter == FILTER_BMP && !IOUtils.checkExtension(file, "bmp"))
	    return new File(file.getAbsoluteFile() + ".bmp");

	return file;
    }

    /**
     * Setzt die entprechenden Filter in den Dialog.
     * 
     * @param typ -
     *                Dialogtyp, dessen Filter gesetzt werden sollen
     */
    private static void setFilter(int typ) {
	FC.resetChoosableFileFilters();
	FC.setSelectedFile(null);

	switch (typ) {
	    case FILETYP_IMAGE:
		FC.addChoosableFileFilter(FILTER_PNG);
		FC.addChoosableFileFilter(FILTER_JPG);
		FC.addChoosableFileFilter(FILTER_GIF);
		FC.addChoosableFileFilter(FILTER_BMP);
		FC.setFileFilter(FILTER_PNG);
		break;

	    case FILETYP_LOP:
		FC.addChoosableFileFilter(FILTER_LOP);
		break;

	    default:
		break;
	}
	FC.setAcceptAllFileFilterUsed(false);

    }
}
