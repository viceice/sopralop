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
 * 28.12.2007 - Version 0.1.2
 * - Standart-Text geändert
 * - ResourceLoading in IOUtils ausgelagert
 * 27.12.2007 - Version 0.1.1
 * - HyperlinkListener hinzugefügt
 * - Uneditierbar gesetzt
 * - Mehrsprachfähigkeit hizugefügt
 * 11.12.2007 - Version 0.1
 *  - Datei hinzugefuegt
 */
package info.kriese.soPra.gui;

import info.kriese.soPra.gui.lang.Lang;
import info.kriese.soPra.io.IOUtils;
import info.kriese.soPra.io.Settings;
import info.kriese.soPra.io.impl.SettingsFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.ToolTipManager;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

/**
 * 
 * @author Michael Kriese
 * @version 0.1.2
 * @since 11.12.2007
 * 
 */
public final class HelpDialog extends JDialog {

    private static final String DEFAULT = "<html><body style=\"background-color:"
	    + "green;color:white;text-align:center\"><h1>Error</h1></body></html>";

    private static HelpDialog INSTANCE = null;
    private static JFrame PARENT = null;

    private static final String PATH = "gui/lang/help";

    private static final Settings PROPS = SettingsFactory.getInstance();

    private static final long serialVersionUID = 1L;

    public static HelpDialog getInstance() {
	if (INSTANCE == null)
	    INSTANCE = new HelpDialog();

	INSTANCE.setLocationRelativeTo(PARENT);

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
	setSize(400, 400);

	this.content = new JEditorPane("text/html;charset=utf-8", DEFAULT);
	this.content.setBorder(BorderFactory.createEmptyBorder());
	this.content.setEditable(false);
	ToolTipManager.sharedInstance().registerComponent(this.content);
	this.content.setDoubleBuffered(true);
	this.content.addHyperlinkListener(new HyperlinkListener() {

	    public void hyperlinkUpdate(HyperlinkEvent e) {
		if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
		    try {
			String url = e.getURL().toString();
			setHelp(url.substring(url.lastIndexOf("/") + 1));
		    } catch (Exception t) {
			MessageHandler.exceptionThrown(t);
		    }
	    }
	});
	add(new JScrollPane(this.content));
    }

    public void setHelp(String file) {
	String query = "";

	if (file.contains("#")) {
	    query = file.substring(file.indexOf("#"));
	    file = file.substring(0, file.indexOf("#"));
	}

	URL url = IOUtils.getURL(PATH, file, Locale.getDefault());

	try {
	    url = new URL(url, query);
	} catch (MalformedURLException e) {
	    MessageHandler.exceptionThrown(e);
	}

	try {
	    this.content.setPage(url);
	} catch (IOException e) {
	    MessageHandler.exceptionThrown(e);
	}
    }
}
