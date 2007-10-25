/**
 * @author Peer Sterner
 * @version $Id$
 * @since 25.10.2007
 */
package info.kriese.soPra.test;

import info.kriese.soPra.io.IOUtils;

import javax.swing.JFrame;

import org.xhtmlrenderer.simple.XHTMLPanel;

/**
 * @author pst
 *
 */
public class TestHelp {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setSize(800, 600);
		XHTMLPanel myHtmlHelp = new XHTMLPanel();
		frame.add(myHtmlHelp);
		myHtmlHelp.setDocument(IOUtils.getURL("gui/html/help/file_help.html").toString());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		
		frame.setVisible(true);

	}

}
