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
 * 16.10.2007 - Version 0.3
 * - Methode getInt hinzugefügt
 * 09.10.2007 - Version 0.2.1
 * - Bundle Name geändert (durch verschieben in neues Package)
 * 11.09.2007 - Version 0.2
 * - Konvertierung von UTF-8 in Unicode eingefügt
 * 29.07.2007 - Version 0.1
 *  - Datei hinzugefuegt
 */
package info.kriese.soPra.gui.lang;

import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * 
 * @author Michael Kriese
 * @version 0.3
 * @since 29.07.2007
 * 
 */
public final class Lang {

    private static final String BUNDLE_NAME = "info.kriese.soPra.gui.lang.lang";

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
	    .getBundle(BUNDLE_NAME, Locale.getDefault());

    public static int getInt(String key) {
	try {
	    return Integer.parseInt(getString(key));
	} catch (NumberFormatException e) {
	    return 0;
	}
    }

    public static String getString(String key) {
	try {
	    String value = RESOURCE_BUNDLE.getString(key);
	    try {
		return new String(value.getBytes("ISO-8859-1"), "UTF-8");
	    } catch (UnsupportedEncodingException e) {
		e.printStackTrace();
		return '!' + key + '!';
	    }
	} catch (MissingResourceException e) {
	    // System.err.println(e);
	    return '!' + key + '!';
	}
    }
}
