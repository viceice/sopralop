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
 * 04.11.2007 - Version 0.1
 *  - Datei hinzugefuegt
 */
package info.kriese.soPra.gui.table;

import info.kriese.soPra.math.impl.FractionalFactory;

import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;

/**
 * Ein CellEditor zum bearbeiten von Fractionals in einer JTable.
 * 
 * @author Michael Kriese
 * @version 0.1
 * @since 04.11.2007
 * 
 */
public class FractionalTableCellEditor extends DefaultCellEditor {

    private static final long serialVersionUID = 1L;

    private final Color bg, fg;
    private final JTextField tf;

    public FractionalTableCellEditor() {
	super(new JTextField());
	this.tf = (JTextField) getComponent();
	this.tf.setHorizontalAlignment(JTextField.RIGHT);
	this.bg = this.tf.getBackground();
	this.fg = this.tf.getForeground();
    }

    @Override
    public Object getCellEditorValue() {
	return FractionalFactory.getInstance(this.tf.getText());
    }

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

    @Override
    public boolean stopCellEditing() {
	if (FractionalFactory.getInstance(this.tf.getText()) != null)
	    return super.stopCellEditing();

	this.tf.setBackground(Color.RED);
	this.tf.setForeground(Color.WHITE);

	return false;
    }

}
