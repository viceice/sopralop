/**
 * @author Peer Sterner
 * @version $Id$
 * @since 25.10.2007
 */
package info.kriese.soPra.test;

import javax.swing.JDialog;

import info.kriese.soPra.gui.HelpDialog;
import info.kriese.soPra.io.impl.SettingsFactory;

/**
 * @author pst
 * 
 */
public class TestHelp {

    /**
     * @param args
     */
    public static void main(String[] args) {

	System.out.println("SoPraLOP HelpTest - Version "
		+ SettingsFactory.getInstance().getVersion());
	System.out.println("\t(c) 2007  "
		+ SettingsFactory.getInstance().getAuthor());
	System.out.println();

	HelpDialog help = HelpDialog.getInstance();

	help.setHelp("gui/html/help/edit_help.html");

	help.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

	help.setVisible(true);

    }

}
