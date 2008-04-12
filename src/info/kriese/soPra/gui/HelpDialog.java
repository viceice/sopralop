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
 * 28.01.2008 - Version 0.1.4
 * - Fenstergröße des Hilfefensters etwas verändert
 * 29.12.2007 - Version 0.1.3
 * - BugFix: Wenn Programm als Jar-Datei gepackt war, fand es die Hilfe-Dateien nicht.
 * - BugFix: Fehler beim Laden der StyleSheets behoben.
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

import java.awt.Cursor;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

/**
 * Dialogfenster zum Anzeigen der Hilfe.
 * 
 * @author Michael Kriese
 * @version 0.1.3
 * @since 11.12.2007
 */
public final class HelpDialog extends JDialog {

    /**
     * Link auf das Stylesheet zum Formatieren der HTML-Hilfe.
     */
    private static URL CSS = IOUtils.getURL("gui/lang/style.css");

    /**
     * Standard-HTML-Code, der angezeigt wird, wenn noch keine Hilfe geladen
     * ist.
     */
    private static final String DEFAULT = "<html><body style=\"background-color:"
	    + "green;color:white;text-align:center\"><h1>Error</h1></body></html>";

    /**
     * Die einzige Instanz des Hilfefensters.
     */
    private static HelpDialog INSTANCE = null;

    /**
     * Übergeordnetes Fenster, vor dem die Hilfe dargestellt wird.
     */
    private static JFrame PARENT = null;

    /**
     * Pfad zu den Hilfe-Dateien.
     */
    private static final String PATH = "gui/lang/help";

    /**
     * Referenz auf die Einstellungen des Programms.
     */
    private static final Settings PROPS = SettingsFactory.getInstance();

    /**
     * Dient zur Serialisierung. (Hat bei uns keine Verwendung)
     */
    private static final long serialVersionUID = 1L;

    /**
     * Gibt eine Referenz auf das Hilfefenster zurück, welches beim ersten
     * Aufruf erstellt wird.
     * 
     * @return Referenz auf das Hilfefenster.
     */
    public static HelpDialog getInstance() {
	if (INSTANCE == null)
	    INSTANCE = new HelpDialog();

	INSTANCE.setLocationRelativeTo(PARENT);

	return INSTANCE;
    }

    /**
     * Setzt das Eltern-Fenster, vor dem das Hilfe-Fenster zentriert dargestellt
     * werden soll.
     * 
     * @param parent -
     *                Eltern-Fenster
     */
    public static void setParent(JFrame parent) {
	PARENT = parent;
    }

    /**
     * Liest die Stylesheet-Datei in das HTML-Dokument
     * 
     * @param doc -
     *                HTML-Dokument, in welches die Styles geladen werden sollen
     * @throws IOException -
     *                 Falls ein Lesefehler auftritt
     */
    private static void readCSS(HTMLDocument doc) throws IOException {
	Reader reader;
	StyleSheet ss = doc.getStyleSheet();

	reader = new StringReader(readDocument(CSS).toString());
	ss.loadRules(reader, CSS);
	reader.close();
    }

    /**
     * Versucht die angegebene URL zu laden
     * 
     * @param url -
     *                Die URL, welche zu laden ist
     * @return Gibt die angegebene URL als HTML-Text zurück. Im Fehlerfall gibt
     *         er eine Fehlermeldung zurück.
     */
    private static StringBuffer readDocument(final URL url) {
	StringBuffer sb = new StringBuffer();
	try {

	    BufferedReader br = new BufferedReader(new InputStreamReader(url
		    .openStream(), "UTF-8"));
	    String buff;

	    while ((buff = br.readLine()) != null)
		sb.append(buff + "\n");

	    br.close();
	} catch (IOException e) {
	    MessageHandler.exceptionThrown(e);
	    sb.append(DEFAULT);
	}
	return sb;
    }

    /**
     * Referenz auf die Anzeigekomponente für den HTML-Code.
     */
    private final JEditorPane content;

    /**
     * Referenz auf das EditorKit, welches den HTML-Code auf die
     * Anzeigekomponente rendert.
     */
    private final HTMLEditorKit editor;

    /**
     * Die aktuell geladene URL.
     */
    private URL page = null;

    /**
     * Konstruktor, welcher alle Variablen und Objekte initialisiert.
     */
    private HelpDialog() {
	super(PARENT, PROPS.getName() + " " + Lang.getString("Strings.Help")
		+ " - Version " + PROPS.getVersion(), true);

	setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
	setSize(500, 400);

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

	this.editor = (HTMLEditorKit) this.content.getEditorKit();
    }

    /**
     * Versucht die entsprechende Hilfe zu laden.
     * 
     * @param file -
     *                Hilfe-Datei, welche angezeigt werden soll.
     */
    public void setHelp(String file) {
	String query = null;

	if (file.contains("#")) {
	    query = file.substring(file.indexOf("#") + 1);
	    file = file.substring(0, file.indexOf("#"));
	}

	URL url = IOUtils.getURL(PATH, file, Locale.getDefault());

	try {
	    if (url == null)
		throw new FileNotFoundException(file);

	    if (this.page == null || !url.sameFile(this.page)) {
		Cursor old = getCursor();
		setCursor(new Cursor(Cursor.WAIT_CURSOR));

		this.page = url;
		Reader reader = new StringReader(readDocument(url).toString());

		HTMLDocument doc = (HTMLDocument) this.editor
			.createDefaultDocument();
		doc.putProperty("IgnoreCharsetDirective", true);
		doc.setBase(url);
		this.editor.read(reader, doc, 0);
		reader.close();

		readCSS(doc);

		this.content.setDocument(doc);
		setCursor(old);
	    }

	    scroll(query);
	} catch (IOException e) {
	    MessageHandler.exceptionThrown(e);
	} catch (BadLocationException e) {
	    MessageHandler.exceptionThrown(e);
	}
    }

    /**
     * Lässt die Hilfe zum entsprechenden HTML-Anker scrollen.
     * 
     * @param query -
     *                HTML-Anker, zu dem gescrollt werden soll.
     */
    private void scroll(final String query) {
	if (query == null)
	    return;

	SwingUtilities.invokeLater(new Runnable() {
	    public void run() {
		HelpDialog.this.content.scrollToReference(query);
	    }
	});
    }
}
