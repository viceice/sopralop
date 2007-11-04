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
package info.kriese.soPra.test;

import info.kriese.soPra.SoPraLOP;
import info.kriese.soPra.gui.ActionHandler;
import info.kriese.soPra.gui.InputPanel;
import info.kriese.soPra.io.impl.SettingsFactory;
import info.kriese.soPra.lop.LOP;
import info.kriese.soPra.lop.LOPEditor;
import info.kriese.soPra.lop.impl.LOPFactory;

import javax.swing.JFrame;
import javax.swing.UIManager;

/**
 * 
 * @author Michael Kriese
 * @version 0.1
 * @since 04.11.2007
 * 
 */
public class TestInputPanel {

    public static void main(String[] args) {
	try { // use the local look and feel
	    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	} catch (Exception e) {
	}

	System.out.println("SoPraLOP InputTest - Version "
		+ SettingsFactory.getInstance().getVersion());
	System.out.println("\t(c) 2007  "
		+ SettingsFactory.getInstance().getAuthor());
	System.out.println();

	LOP lop = LOPFactory.newLinearOptimizingProblem();
	LOPEditor editor = LOPFactory.newLOPEditor(lop);

	ActionHandler.INSTANCE.setLOP(lop);

	SoPraLOP.EDITOR = editor;

	InputPanel ip = new InputPanel();
	ip.setEditor(editor);

	editor.update();

	JFrame frame = new JFrame("SoPraLOP InputTest - Version "
		+ SettingsFactory.getInstance().getVersion());
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.add(ip);
	frame.setSize(400, 300);
	frame.setVisible(true);
    }
}
