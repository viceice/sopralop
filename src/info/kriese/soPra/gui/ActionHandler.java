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
 * 20.05.2008 - Version 0.5
 * - an neuen FileChooser angepasst
 * - Handler für captureImage implementiert
 * 27.12.2007 - Version 0.4.3
 * - Hilfe-Fenster hinzugefügt
 * - BugFix: Bei einer Fehlermeldung waren Titel und Nachricht vertauscht
 * 17.12.2007 - Version 0.4.2
 * - Fehlerausgaben verändert
 * 04.12.2007 - Version 0.4.1
 * - An neues Hilfesystem angepasst
 * 03.12.2007 - Version 0.4
 * - An ErrorHandler angepasst
 * - Abfrage für versehentliches verwerfen des LOP eingefügt
 * 26.11.2007 - Version 0.3.3
 * - VisualFrame ausblenden beim Wechseln von Duale in Primale Ansicht
 * 08.11.2007 - Version 0.3.2
 * - Wenn ein Beispiel geladen wird, wird die Nummer in der Titelzeile angezeigt
 * 04.11.2007 - Version 0.3.1
 * - BugFix: NullPointer abgefangen, wenn MainFrame nicht existiert
 * - BugFix: Fehler bei der Übergabe der zu speichernden Datei behoben. Das
 *   Problem kann jetzt wieder gespeichert werden.
 * 01.11.2007 - Version 0.3
 * - LOPEditor hizugefügt
 * - Verlagerung von Fuktionalität
 * 31.10.2007 - Version 0.2.1
 * - Funktionalität in InputPanel verschoben
 * 25.10.2007 - Version 0.2
 * - MouseListener hinzugefügt, welcher Hilfetexte auf der StatusBar anzeigt
 * 24.10.2007 - Version 0.1
 *  - Datei hinzugefuegt
 */
package info.kriese.soPra.gui;

import info.kriese.soPra.SoPraLOP;
import info.kriese.soPra.gui.filechooser.FileChooserFactory;
import info.kriese.soPra.gui.lang.Lang;
import info.kriese.soPra.io.IOUtils;
import info.kriese.soPra.lop.LOP;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.net.MalformedURLException;

import javax.swing.AbstractButton;
import javax.swing.JOptionPane;

/**
 * Klasse zum Handeln und Delegieren aller Aktionen in SoPraLOP
 * 
 * @author Michael Kriese
 * @version 0.5
 * @since 24.10.2007
 * 
 * TODO: mögliche NullPointer abfangen
 */
public final class ActionHandler {

    /**
     * Die einziege Instanz dieser Klasse.
     */
    public static final ActionHandler INSTANCE = new ActionHandler();

    /**
     * Beendet das Programm.
     */
    public static void exit() {
	exit(0);
    }

    /**
     * Beendet das Programm mit einem definierten Exit-Code.
     * 
     * @param code -
     *                Exit-Code des Programms.
     */
    public static void exit(int code) {
	SoPraLOP.ABOUT.setVisible(false);
	SoPraLOP.VISUAL.setVisible(false);
	SoPraLOP.MAIN.setVisible(false);
	System.exit(code);
    }

    /**
     * LOP-Dateiname, welche im Programm gerade geöffnet ist.
     */
    public File file = null;

    /**
     * ActionListener, welcher in diversen Objekten registriert wird. Über
     * diesen werden Aktionen an andere Objekte delegiert.
     */
    private final ActionListener ac;

    /**
     * Das LOP, welches im Programm geöffnet ist.
     */
    private LOP lop = null;

    /**
     * MouseListener, welcher in diversen Objekten registriert wird. Über diesen
     * werden Hilfstexte in der Statusleiste angezeigt..
     */
    private final MouseListener ml;

    /**
     * Konstruktor, welcher die Variablen und Objekte dieser Klasse
     * initialisiert.
     */
    private ActionHandler() {
	this.ac = new ActionListener() {

	    /**
	     * Delegation der Aktion an den ActionHandler.
	     */
	    public void actionPerformed(ActionEvent e) {
		ActionHandler.this.actionPerformed(e);
	    }
	};

	this.ml = new MouseAdapter() {

	    /**
	     * Zeigt einen Hilfstext der Komponete, über der die Maus sich
	     * befindet, in der Statusleiste an, sofern vorhanden.
	     */
	    @Override
	    public void mouseEntered(MouseEvent e) {
		Component c = e.getComponent();

		if (c instanceof AbstractButton) {
		    AbstractButton btn = (AbstractButton) c;
		    String s = Lang.getString(btn.getActionCommand() + ".Help",
			    (String) null);
		    MessageHandler.showHelp(s);
		}
	    }

	    /**
	     * Blendet den Hilfstext wieder aus, wenn die Maus die Komponente
	     * verlässt.
	     */
	    @Override
	    public void mouseExited(MouseEvent e) {
		MessageHandler.showHelp();
	    }
	};

	/**
	 * Registriert den ActionHandler im Menü.
	 */
	MenuMaker.setDefaultActionHandler(this);
    }

    /**
     * Mit dieser Funtion kann sich ein Button beim ActionHandler registrieren.
     * 
     * @param btn -
     *                Ein Button, der registriert werden soll.
     */
    public void add(AbstractButton btn) {
	btn.addActionListener(this.ac);
	btn.addMouseListener(this.ml);
    }

    /**
     * Setzt die Referenz des LOP's.
     * 
     * Diese wird benötigt, um Aktionen auf dem LOP aufrufen zu können.
     * 
     * @param lop -
     *                Referenz auf ein LOP
     */
    public void setLOP(LOP lop) {
	this.lop = lop;
    }

    /**
     * Delegiert die Aktionen an die entsprechenden Komponenten weiter.
     * 
     * @param e -
     *                Eine Referenz auf den Auslöser der Aktion.
     */
    private void actionPerformed(ActionEvent e) {
	String cmd = e.getActionCommand();

	if (this.lop == null) {
	    MessageHandler.showError(Lang.getString("Strings.Error"),
		    "ActionHandler: lop shoudn\'t be null!");
	    exit(-1);
	}

	if (cmd.equals("Menu.File.Exit"))
	    exit();
	else if (cmd.equals("Menu.File.Open")) {
	    if (askEdit())
		fileOpenClass();
	} else if (cmd.equals("Menu.File.Save")) {
	    if (askEdit())
		fileSaveClass(false);
	} else if (cmd.equals("Menu.File.SaveAs")) {
	    if (askEdit())
		fileSaveClass(true);
	} else if (cmd.equals("Menu.File.SaveImage"))
	    fileSaveClassImage();
	else if (cmd.equals("Menu.View.Reset"))
	    SoPraLOP.ENGINE.resetScene();
	else if (cmd.equals("Menu.View.Show"))
	    SoPraLOP.VISUAL.setVisible(true);
	else if (cmd.equals("Menu.View.ShowDualProblem")) {
	    if (askEdit())
		this.lop.showDualProblem();
	} else if (cmd.equals("Menu.View.ShowPrimalProblem")) {
	    SoPraLOP.VISUAL.setVisible(false);
	    this.lop.showPrimalProblem();
	} else if (cmd.equals("Menu.Help.Help")) {
	    HelpDialog.getInstance().setHelp("index.htm");
	    HelpDialog.getInstance().setVisible(true);
	} else if (cmd.equals("Menu.Help.About")) {
	    SoPraLOP.ABOUT.setLocationRelativeTo(SoPraLOP.MAIN);
	    SoPraLOP.ABOUT.setVisible(true);
	} else if (cmd.startsWith("Menu.File.Samples")) {
	    this.file = null;
	    SoPraLOP.EDITOR.open(IOUtils.getURL("problems/"
		    + Lang.getString(cmd + ".File") + ".lop"));
	    SoPraLOP.MAIN.setTitle(Lang.getString("Strings.Sample") + " "
		    + cmd.substring(cmd.lastIndexOf('.') + 1));
	} else if (cmd.equals("Input.Menu.AddVar"))
	    SoPraLOP.EDITOR.addVariable();
	else if (cmd.equals("Input.Menu.DelVar"))
	    SoPraLOP.EDITOR.removeVariable();
	else if (cmd.equals("Input.Menu.Save"))
	    SoPraLOP.EDITOR.take();
	else if (cmd.equals("Input.Menu.Check"))
	    SoPraLOP.EDITOR.check();
	else if (cmd.equals("Input.Menu.Clear"))
	    SoPraLOP.EDITOR.clear();
	else if (cmd.equals("Input.Menu.Reset"))
	    SoPraLOP.EDITOR.update();
    }

    /**
     * Sicherheitsabfrage, falls das Problem bearbeitet wurde, aber nicht
     * gespeichert wurde.
     * 
     * @return "TRUE", falls der Benutzer zugestimmt hat, sonst "FALSE".
     */
    private boolean askEdit() {
	if (!SoPraLOP.EDITOR.isEdited())
	    return true;

	int res = MessageHandler
		.showConfirmDialog(Lang.getString("Errors.IsEdited.Title"),
			Lang.getString("Errors.IsEdited"),
			JOptionPane.YES_NO_CANCEL_OPTION);

	if (res == JOptionPane.NO_OPTION)
	    SoPraLOP.EDITOR.take();
	else if (res == JOptionPane.YES_OPTION)
	    SoPraLOP.EDITOR.update();

	return !SoPraLOP.EDITOR.isEdited();
    }

    /**
     * Ladefunktion für LOP-Dateien.
     * 
     * Zeigt dem Benutzer einen Datei-Öffnen-Dialog und öffnet gegebenenfalls
     * das ausgewählte LOP.
     */
    private void fileOpenClass() {
	this.file = FileChooserFactory.open(FileChooserFactory.FILETYP_LOP);
	if (this.file != null && this.file.canRead())
	    try {
		SoPraLOP.EDITOR.open(this.file.toURI().toURL());
		SoPraLOP.MAIN.setTitle(this.file.getName());
	    } catch (MalformedURLException e) {
		e.printStackTrace();
	    }
    }

    /**
     * Speicherfunktion für LOP-Dateien.
     * 
     * Zeigt dem Benutzer einen Datei-Speichern-Dialog und speichert
     * gegebenenfalls das LOP.
     */
    private void fileSaveClass(boolean saveAs) {
	if (saveAs || this.file == null)
	    this.file = FileChooserFactory.save(FileChooserFactory.FILETYP_LOP);

	if (this.file != null)
	    try {
		SoPraLOP.EDITOR.save(this.file.toURI().toURL());
		SoPraLOP.MAIN.setTitle(this.file.getName());
	    } catch (MalformedURLException e) {
		e.printStackTrace();
	    }
    }

    /**
     * Speicherfunktion für LOP's als Bild.
     * 
     * Zeigt dem Benutzer einen Datei-Speichern-Dialog und speichert
     * gegebenenfalls das LOP als Bild.
     */
    private void fileSaveClassImage() {

	File file = FileChooserFactory.save(FileChooserFactory.FILETYP_IMAGE);

	if (file != null)
	    SoPraLOP.EDITOR.captureImage(file);
    }

}
