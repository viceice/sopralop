/**
 * @author Peer Sterner
 * @version $Id$
 * @since 25.10.2007
 */
package info.kriese.soPra.test;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

/**
 * @author pst
 * 
 */
public class TestHelp {

    private static JFrame frame;
    private static JLabel myHtmlHelp;
    private static JScrollPane scrollPane;

    /**
     * @param args
     */
    public static void main(String[] args) {

	frame = new JFrame("SoPra LOP Hilfefenster");
	frame.setSize(600, 500);
	myHtmlHelp = new JLabel();
	frame.add(myHtmlHelp);
	// TODO: hilfe laden
	// myHtmlHelp.setDocument(IOUtils.getURL("gui/html/help/file_help.html").toString());
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	scrollPane = new JScrollPane(myHtmlHelp);
	scrollPane.remove(scrollPane.getHorizontalScrollBar());
	frame.add(scrollPane);

	frame.setVisible(true);

    }

}
