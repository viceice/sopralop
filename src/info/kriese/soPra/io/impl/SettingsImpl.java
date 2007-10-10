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
 * 09.10.2007 - Version 0.2
 * - Factory erstellt und unbenannte Klasse ausgelagert
 * 29.07.2007 - Version 0.1
 *  - Datei hinzugefuegt
 */
package info.kriese.soPra.io.impl;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import info.kriese.soPra.io.IOUtils;
import info.kriese.soPra.io.Settings;

/**
 * 
 * 
 * @author Michael Kriese
 * @version 0.2
 * @since 29.07.2007
 * 
 */
final class SettingsImpl implements Settings {
    private final Properties props = new Properties();

    public SettingsImpl(String file) {
	URL url = IOUtils.getURL(file);
	InputStream is = null;

	try {
	    if (url != null)
		is = url.openStream();
	    if (is == null)
		is = new FileInputStream(file);
	    this.props.load(is);
	} catch (Exception e) {
	    System.err.println(e);
	    e.printStackTrace();
	}
    }

    public String getAuthor() {
	return getProp("sopra.author");
    }

    public String getDescription() {
	return getProp("sopra.desc");
    }

    public String getGPG() {
	return getProp("sopra.gpg");
    }

    public String getMail() {
	return getProp("sopra.email");
    }

    public String getName() {
	return getProp("sopra.name");
    }

    public String getTitle() {
	return getProp("sopra.title");
    }

    public String getVersion() {
	return getProp("sopra.version");
    }

    public String getWeb() {
	return getProp("sopra.web");
    }

    private String getProp(String key) {
	String res = this.props.getProperty(key);
	if (res == null)
	    return "!" + key + "!";
	else
	    return res;
    }
}