/**
 * @author Peer Sterner
 * @version $Id$
 * @since 25.10.2007
 */
package info.kriese.soPra.test;

import info.kriese.soPra.io.IOUtils;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import org.xhtmlrenderer.simple.XHTMLPanel;

/**
 * @author pst
 *
 */
public class TestHelp {
	
	private static JScrollPane scrollPane;
	private static JFrame frame;
	private static XHTMLPanel myHtmlHelp;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		frame = new JFrame("Hilfe");
		frame.setSize(600, 500);
		myHtmlHelp = new XHTMLPanel();
		frame.add(myHtmlHelp);
		myHtmlHelp.setDocument(IOUtils.getURL("gui/html/help/edit_data_help.html").toString());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		scrollPane = new JScrollPane(myHtmlHelp);
		scrollPane.remove(scrollPane.getHorizontalScrollBar());
		frame.add(scrollPane);
		
		
		
		frame.setVisible(true);

	}

}
