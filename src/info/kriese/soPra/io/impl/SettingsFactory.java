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
 * 28.12.2007 - Version 0.2.1
 * - Funktion zum parsen der Komandozeilenparameter hinzugefügt
 * 19.12.2007 - Version 0.2
 * - Debug Handling hinzugefügt
 * 09.10.2007 - Version 0.1
 *  - Datei hinzugefuegt
 */
package info.kriese.soPra.io.impl;

import java.util.Locale;

import info.kriese.soPra.io.Settings;

/**
 * 
 * @author Michael Kriese
 * @version 0.2.1
 * @since 09.10.2007
 * 
 */
public final class SettingsFactory {

    private static boolean DEBUG = false;
    private static final String FILE = "sopra.properties";

    private static Settings props = null;

    /**
     * Erstellt bei Bedarf eine Instanz des Settings-Interface und gibt die
     * Referenz darauf zurück.
     * 
     * @return Referenz auf ein Settings-Objekt.
     */
    public static Settings getInstance() {
	if (props == null)
	    props = new SettingsImpl(FILE, DEBUG);
	return props;
    }

    /**
     * Durchsucht die Liste auf anwendbare Parameter.
     * 
     * Aktuelle Parameter:
     * <ul>
     * <li>debug - Aktiviert den Debug-Modus</li>
     * <li>lang=XX - Ändert die lokale Sprache, wobei XX die gewünschte Sprache
     * ist.</li>
     * </ul>
     * 
     * @param args -
     *                Liste, welche durchsucht werden soll.
     */
    public static void parseArgs(String[] args) {
	// Parse Parameters
	for (String arg : args) {
	    if (arg.toLowerCase().contains("debug"))
		DEBUG = true;
	    if (arg.toLowerCase().contains("lang")) {
		String[] lang = arg.substring(5).split("_");
		switch (lang.length) {
		    case 3:
			Locale
				.setDefault(new Locale(lang[0], lang[1],
					lang[2]));
			break;
		    case 2:
			Locale.setDefault(new Locale(lang[0], lang[1]));
			break;
		    case 1:
			Locale.setDefault(new Locale(lang[0]));
			break;

		    default:
			break;
		}
	    }
	}
    }

    /**
     * Aktiviert oder deaktiviert den Debug-Modus.
     * 
     * @param value -
     *                true zum aktivieren, andernfalls false.
     */
    public static void setDebug(boolean value) {
	DEBUG = value;
    }

    private SettingsFactory() {
    }
}
