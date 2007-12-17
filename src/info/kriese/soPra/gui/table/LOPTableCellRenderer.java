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
 * 17.12.2007 - Version 0.1.2
 * - SelectionBorder auch für nicht editierbare Felder
 * 09.11.2007 - Version 0.1.1
 * - Neue Darstellung für Unendlich und nicht existent
 * 03.11.2007 - Version 0.1
 *  - Datei hinzugefuegt
 */
package info.kriese.soPra.gui.table;

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
 * Dient zur Visualisierung von Werte in einer Tabelle.
 * 
 * @author Michael Kriese
 * @version 0.1.2
 * @since 03.11.2007
 */
public class LOPTableCellRenderer implements TableCellRenderer {

    private static final long serialVersionUID = -253463264318895844L;

    private final JLabel content;
    private final Border noBorder = BorderFactory.createEmptyBorder(1, 1, 1, 1);
    private final Border normal;

    private final Border selected, selected2;

    public LOPTableCellRenderer() {
	this.content = new JLabel();
	this.content.setOpaque(true);
	this.content.setBorder(this.noBorder);
	this.content.setBackground(Color.WHITE);
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

	// ToolTips
	if (column == cols - 2 && row > 1 && row < rows - 2)
	    this.content.setToolTipText(Lang.getString("Input.Relation"));
	else if (column == 0) {
	    if (row == 0)
		this.content.setToolTipText(Lang
			.getString("Strings.TargetFunction"));
	    else if (row > 1 && row < rows - 2)
		this.content.setToolTipText(Lang
			.getString("Strings.Constraint"));
	    else if (row == rows - 1)
		this.content.setToolTipText(Lang.getString("Strings.Solution"));
	} else if (value instanceof LOPInfinity)
	    this.content.setToolTipText(Lang.getString("Strings.Infinity"));
	else if (value instanceof LOPNotExsitent)
	    this.content.setToolTipText(Lang.getString("Strings.NotExistent"));
	else
	    this.content.setToolTipText(null);

	// Wert
	this.content.setText(value == null ? "" : value.toString());

	return this.content;
    }
}
