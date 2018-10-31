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
 * 28.12.2007 - Version 0.4.2
 * - Verzögertes ResourceLoading, um auf Komandozeilenparameter zu reagieren
 * 27.12.2007 - Version 0.4.1
 * - Fehlerbehandlung aufgeräumt
 * 25.10.2007 - Version 0.4
 * - Methode getString kann jetzt ein Default-rückgabewert übergeben werden
 * 16.10.2007 - Version 0.3
 * - Methode getInt hinzugefügt
 * 09.10.2007 - Version 0.2.1
 * - Bundle Name geändert (durch verschieben in neues Package)
 * 11.09.2007 - Version 0.2
 * - Konvertierung von UTF-8 in Unicode eingefügt
 * 29.07.2007 - Version 0.1
 *  - Datei hinzugefuegt
 */
package info.kriese.sopra.gui.lang;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Diese Klasse dient zur Realisierung der Mehrsprachigkeit.
 * 
 * @author Michael Kriese
 * @version 0.4.2
 * @since 29.07.2007
 * 
 */
public final class Lang {

    /**
     * Name der Resource, in der nach den Sprachdaten gesucht werden soll.
     */
    private static final String BUNDLE_NAME = "info.kriese.sopra.gui.lang.lang";

    /**
     * Referenz auch die geladene SprachResource.
     */
    private static ResourceBundle RESOURCE_BUNDLE = null;

    /**
     * Sucht in der Sprachresource nach dem "KEY" und gibt den Wert als Ganzzahl
     * zurück.
     * 
     * @param key -
     *                "KEY", nach dem gesucht werden soll.
     * @return Wert als Ganzzahl
     */
    public static int getInt(String key) {
	try {
	    return Integer.parseInt(getString(key));
	} catch (NumberFormatException e) {
	    return 0;
	}
    }

    /**
     * Sucht in der Sprachresource nach dem "KEY" und gibt den Wert als
     * Zeichenkette zurück.
     * 
     * @param key -
     *                "KEY", nach dem gesucht werden soll.
     * @return Wert als Zeichenkette
     */
    public static String getString(String key) {
	return getString(key, "!" + key + "!");
    }

    /**
     * Sucht in der Sprachresource nach dem "KEY" und gibt den Wert als
     * Zeichenkette zurück. Zusätzlich werden in der gefundenen Zeichenkette die
     * Platzhalter durch die übergebenen Objekte ersetzt.
     * 
     * @param key -
     *                "KEY", nach dem gesucht werden soll.
     * @param args -
     *                Liste von Objekten, die für die Platzhalter eingesetzt
     *                werden sollen.
     * @return Wert als Zeichenkette
     */
    public static String getString(String key, Object[] args) {

	try {
	    return MessageFormat.format(getString(key), args);
	} catch (Exception e) {
	    // Sollte niemals auftreten!!!
	    e.printStackTrace();
	    return "!" + key + "!";
	}
    }

    /**
     * Sucht in der Sprachresource nach dem "KEY" und gibt den Wert als
     * Zeichenkette zurück. Hier kann ein Standardwert übergeben werden, der
     * zurückgegeben wird, wenn der "KEY" nicht gefunden wird.
     * 
     * @param key -
     *                "KEY", nach dem gesucht werden soll.
     * @param def -
     *                Standardwert, der genutzt werden soll
     * @return Wert als Zeichenkette
     */
    public static String getString(String key, String def) {
	if (RESOURCE_BUNDLE == null)
	    RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME, Locale
		    .getDefault());
	try {
	    String value = RESOURCE_BUNDLE.getString(key);
	    return new String(value.getBytes("ISO-8859-1"), "UTF-8");
	} catch (MissingResourceException e) {
	} catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	}
	return def;
    }
}
