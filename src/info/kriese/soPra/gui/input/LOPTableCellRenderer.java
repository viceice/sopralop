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
 * 05.03.2008 - Version 0.1.4
 * - LOPEmpty & LOPInfinity entfernt, da nicht mehr verwendet
 * 25.01.2008 - Version 0.1.3
 * - Hintergrund geändert
 * - Tooltips für Relationszeichen gelöscht, da nicht mehr benötigt
 * - NotExistent in Empty umbenannt
 * 17.12.2007 - Version 0.1.2
 * - SelectionBorder auch für nicht editierbare Felder
 * 09.11.2007 - Version 0.1.1
 * - Neue Darstellung für Unendlich und nicht existent
 * 03.11.2007 - Version 0.1
 *  - Datei hinzugefuegt
 */
package info.kriese.soPra.gui.input;

import info.kriese.soPra.gui.MessageHandler;
import info.kriese.soPra.gui.lang.Lang;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;

/**
 * Dient zur Visualisierung von Werten in einer Tabelle.
 * 
 * @author Michael Kriese
 * @version 0.1.4
 * @since 03.11.2007
 */
public class LOPTableCellRenderer implements TableCellRenderer {

    /**
     * Dient zur Serialisierung.
     */
    private static final long serialVersionUID = -253463264318895844L;

    /**
     * Komponente, welche zum Rendern der Fractionals benutzt wird.
     */
    private final JLabel content;

    /**
     * Nicht sichbarer Rahmen.
     */
    private final Border noBorder = BorderFactory.createEmptyBorder(1, 1, 1, 1);

    /**
     * Standardrahmen für die Komponente. (ein grauer EtchedBorder außen & ein
     * unsichbarer Rahmen innen)
     */
    private final Border normal;

    /**
     * Rahmen, falls die Komponente durch den User selektiert ist. (selected für
     * editierbare Felder, er ist grün, und selected2 für uneditierbare Felder,
     * er ist schwarz)
     */
    private final Border selected, selected2;

    /**
     * Konstruktor, der alle Variablen und Objekte initialisiert.
     */
    public LOPTableCellRenderer() {
	this.content = new JLabel();
	this.content.setBorder(this.noBorder);
	this.content.setOpaque(true);
	this.content.addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseEntered(MouseEvent e) {
		MessageHandler.showHelp(LOPTableCellRenderer.this.content
			.getToolTipText());
	    }

	    @Override
	    public void mouseExited(MouseEvent e) {
		MessageHandler.showHelp();
	    }
	});

	Border inner, outer;

	outer = BorderFactory.createEtchedBorder();
	inner = BorderFactory.createEmptyBorder(2, 2, 2, 2);
	this.normal = BorderFactory.createCompoundBorder(outer, inner);

	outer = BorderFactory.createLineBorder(new Color(0, 128, 0), 2);
	this.selected = BorderFactory.createCompoundBorder(outer, inner);

	outer = BorderFactory.createLineBorder(Color.BLACK, 1);
	this.selected2 = BorderFactory.createCompoundBorder(outer, inner);

    }

    /**
     * Gibt die Komponente mit dem entsprechenden Wert aus der Tabelle zurück.
     * 
     * @param table -
     *                Tabelle, in der diser Renderer registriert ist.
     * @param value -
     *                Wert, welcher angezeigt werden soll.
     * @param isSelected -
     *                Ist die Komponente durch den User selektiert.
     * @param hasFocus -
     *                Hat die Komponente den Fokus.
     * @param row -
     *                Zeile der Zelle, die gerendert werden soll.
     * @param column -
     *                Spalte der Zelle, die gerendert werden soll.
     * 
     * @return Die Komponente zum rendern.
     */
    public Component getTableCellRendererComponent(JTable table, Object value,
	    boolean isSelected, boolean hasFocus, int row, int column) {
	int rows = table.getRowCount();
	int cols = table.getColumnCount();

	// Zellausrichtung
	if (column == 0)
	    this.content.setHorizontalAlignment(JLabel.LEFT);
	else if (column == cols - 2)
	    this.content.setHorizontalAlignment(JLabel.CENTER);
	else
	    this.content.setHorizontalAlignment(JLabel.RIGHT);

	// Rahmen
	if (row != 1 && row != rows - 2) {
	    if (hasFocus && table.isCellEditable(row, column))
		this.content.setBorder(this.selected);
	    else if (hasFocus)
		this.content.setBorder(this.selected2);
	    else
		this.content.setBorder(this.normal);
	} else if (hasFocus)
	    this.content.setBorder(this.selected2);
	else
	    this.content.setBorder(this.noBorder);

	// Hintergrund
	if (table.isCellEditable(row, column))
	    this.content.setBackground(Color.WHITE);
	else
	    this.content.setBackground(null);

	// ToolTips
	if (column == 0) {
	    if (row == 0)
		this.content.setToolTipText(Lang
			.getString("Strings.TargetFunction"));
	    else if (row > 1 && row < rows - 2)
		this.content.setToolTipText(Lang
			.getString("Strings.Constraint"));
	    else if (row == rows - 1)
		this.content.setToolTipText(Lang.getString("Strings.Solution"));
	} else
	    this.content.setToolTipText(null);

	// Wert
	this.content.setText(value == null ? "" : value.toString());

	return this.content;

    }
}
