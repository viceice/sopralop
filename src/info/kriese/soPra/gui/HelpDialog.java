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
 * 11.12.2007 - Version 0.1
 *  - Datei hinzugefuegt
 */
package info.kriese.soPra.gui;

import info.kriese.soPra.gui.lang.Lang;
import info.kriese.soPra.io.IOUtils;
import info.kriese.soPra.io.Settings;
import info.kriese.soPra.io.impl.SettingsFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;

import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

/**
 * 
 * @author Michael Kriese
 * @version 0.1
 * @since 11.12.2007
 * 
 */
public final class HelpDialog extends JDialog {

    private static final String DEFAULT = "<html><body bgcolor=\"#008000\">Test</body></html>";
    private static HelpDialog INSTANCE = null;

    private static JFrame PARENT = null;

    private static final Settings PROPS = SettingsFactory.getInstance();

    private static final long serialVersionUID = 1L;

    public static HelpDialog getInstance() {
	if (INSTANCE == null)
	    INSTANCE = new HelpDialog();

	return INSTANCE;
    }

    public static void setParent(JFrame parent) {
	PARENT = parent;
    }

    private final JEditorPane content;

    private HelpDialog() {
	super(PARENT, PROPS.getName() + " " + Lang.getString("Strings.Help")
		+ " - Version " + PROPS.getVersion(), true);

	setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
	setSize(400, 600);

	this.content = new JEditorPane("text/html", DEFAULT);
	// this.content.setOpaque(true);
	// this.content.setBackground(new Color(0, 128, 0));
	// this.content.setForeground(Color.WHITE);
	add(new JScrollPane(this.content));
	// add(this.content);
    }

    public void setHelp(String file) {

	URL url = IOUtils.getURL(file);
	StringBuffer sb = new StringBuffer();

	try {
	    InputStreamReader isr = new InputStreamReader(url.openStream(),
		    Charset.forName("utf-8"));
	    BufferedReader r = new BufferedReader(isr);
	    String line;

	    while ((line = r.readLine()) != null)
		sb.append(line);

	    r.close();
	    isr.close();

	} catch (IOException e) {
	    MessageHandler.showError(Lang.getString("Strings.Error"), e
		    .getLocalizedMessage());
	} catch (Exception e) {
	    MessageHandler.showError(Lang.getString("Strings.Error"), e
		    .getClass().getCanonicalName()
		    + ": " + e.getLocalizedMessage());
	}

	try {
	    this.content.setPage(url);
	} catch (IOException e) {
	    MessageHandler.showError(Lang.getString("Strings.Error"), e
		    .getLocalizedMessage());
	}
    }
}
