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
 * 29.01.2008 - Version 0.1.1.1
 * - Wenn Textfeld leer soll null zurückgegeben, damit keine Ausgabe in der
 *    Tabelle erscheint.
 * 03.12.2007 - Version 0.1.1
 * - Grünen Rahmen und gelben Hintergrund eingefügt
 * 04.11.2007 - Version 0.1
 *  - Datei hinzugefuegt
 */
package info.kriese.soPra.gui.input;

import info.kriese.soPra.math.impl.FractionalFactory;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;

/**
 * Ein CellEditor zum Bearbeiten von Fractionals in einer JTable.
 * 
 * @author Michael Kriese
 * @version 0.1.1.1
 * @since 04.11.2007
 * 
 */
public class FractionalTableCellEditor extends DefaultCellEditor {

    /**
     * Dient zur Serialisierung.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Farben für Schrift und Hintergrund des Textfeldes.
     */
    private final Color bg, fg;

    /**
     * TextFeld, in welches die Fractionals eingegeben werden.
     */
    private final JTextField tf;

    /**
     * Konstruktor, welcher alle Variablen und Objekte initialisiert.
     */
    public FractionalTableCellEditor() {
	super(new JTextField());
	this.tf = (JTextField) getComponent();
	this.tf.setHorizontalAlignment(JTextField.RIGHT);
	this.bg = Color.YELLOW;
	this.fg = this.tf.getForeground();

	Border inner, outer;
	inner = BorderFactory.createEmptyBorder(2, 2, 2, 2);
	outer = BorderFactory.createLineBorder(new Color(0, 128, 0), 2);
	this.tf.setBorder(BorderFactory.createCompoundBorder(outer, inner));
    }

    /**
     * Gibt den eingegebenen Wert als Fractional zurück.
     * 
     * @return Wert als Object
     */
    @Override
    public Object getCellEditorValue() {
	if (this.tf.getText() == null || this.tf.getText().length() == 0)
	    return null;
	return FractionalFactory.getInstance(this.tf.getText());
    }

    /**
     * Gibt die Editorkomponente zurück, in der der User Werte eingeben kann.
     * 
     * @param table -
     *                Tabelle, in der der Editor registriert ist.
     * @param value -
     *                Vordefinierter Wert (sollte vom Typ Fractional sein), den
     *                der Editor annehmen soll.
     * @param isSelected -
     *                Gibt an, ob der Editor durch den User selektiert wurde.
     * @param row -
     *                Zeile der Zelle, welche bearbeitet werden soll.
     * @param column -
     *                Spalte der Zelle, welche bearbeitet werden soll.
     * 
     * @return Die Bearbeitungskomponente.
     */
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
	    boolean isSelected, int row, int column) {

	JTextField comp = (JTextField) super.getTableCellEditorComponent(table,
		value, isSelected, row, column);

	comp.setText(value != null ? value.toString() : "0");
	comp.setBackground(this.bg);
	comp.setForeground(this.fg);
	comp.selectAll();

	return comp;
    }

    /**
     * Wird aufgerufen, wenn der User das Beabeiten der Zelle beenden will. Hier
     * wird der eingegebene Wert auf zulässigkeit überprüft (kann er in ein
     * Fractional konvertiert werden).
     * 
     * @return "True" wenn der eingegebene Wert gültig ist, ansonsten "FALSE".
     */
    @Override
    public boolean stopCellEditing() {
	if (FractionalFactory.getInstance(this.tf.getText()) != null)
	    return super.stopCellEditing();

	this.tf.setBackground(Color.RED);
	this.tf.setForeground(Color.WHITE);

	return false;
    }

}
