/**
 * @author Peer Sterner
 * @version $Id$
 * @since 25.10.2007
 */
package info.kriese.soPra.test;

import info.kriese.soPra.math.Vector3Frac;
import info.kriese.soPra.math.impl.Vector3FracFactory;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.*;
import java.util.Vector;

import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;

/**
 * @author pst
 *
 */
public class TestDualPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1769367299092520935L;
	private static JFrame frame;
	private static JPanel panel;
	private static JButton showResult;
	private static Color fg = Color.BLACK, bg = Color.WHITE, element = Color.RED, grey = Color.GRAY;
	private static int offsetX = 30, offsetY = 30, stepWidth;
	private static float scale;
	private static float[] mouseCoords = new float[2];
	private static int numVar;
    private static Vector3Frac target;
    private static Vector<Vector3Frac> vectors;
    
    private static MouseListener e;
    
    private static float coordX, coordY;
    private static Dimension d;
	
	final static BasicStroke normal = new BasicStroke(1.0f);
	final static BasicStroke stroke = new BasicStroke(2.0f);
	final static float dash1[] = {1.5f};
	final static float dash2[] = {10.0f};
	final static BasicStroke dashed = new BasicStroke(1.0f, 
            BasicStroke.CAP_BUTT, 
            BasicStroke.JOIN_MITER, 
            10.0f, dash1, 0.0f);
	final static BasicStroke dashed_bold = new BasicStroke(2.0f, 
			BasicStroke.CAP_BUTT, 
            BasicStroke.JOIN_MITER, 
            10.0f, dash2, 0.0f);
	
	Dimension totalSize, base;

	/**
	 * @param args
	 */
	
	
		public void paint(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			d = getSize();
			stepWidth = Math.round(d.width / scale);

			// Linien und Beschriftung des Koordinatensystems
			g2.setPaint(fg);
			g2.setStroke(normal);
			g2.drawLine(offsetX, 20, offsetX, d.height - offsetY);
			g2.drawString("^", 28, 28);
			g2.drawString("X", 12, 25);
			g2.drawString("2", 19, 28);
			g2.drawLine(offsetX, d.height - 30, d.width - offsetX, d.height
					- offsetY);
			g2.drawString("X", d.width - 34, d.height - 13);
			g2.drawString("1", d.width - 27, d.height - 10);
			g2.drawString(">", d.width - 34, d.height - 25);

			// Hilfslinien und Koordinaten der X-Achse
			for (int step = offsetX; step <= d.width - 50; step += stepWidth) {
				g2.setStroke(dashed);
				g2.setPaint(grey);
				g2.drawLine(step, d.height - offsetY, step, 30);
				g2.setPaint(fg);
				g2.drawString((step - offsetX) / stepWidth + "", step - 3,
						d.height - 15);
			}

			// Hilfslinien und Koordinaten der Y-Achse
			for (int step = d.height - offsetY; step >= 40; step -= stepWidth) {
				g2.setStroke(dashed);
				g2.setPaint(grey);
				g2.drawLine(offsetX, step, d.width - 30, step);
				g2.setPaint(fg);
				g2.drawString((d.height - offsetY - step) / stepWidth + "", 13,
						step + 5);
			}

			g2.setPaint(element);
			g2.setStroke(stroke);
			for (int i = 0; i < numVar; i++) {
				if (vectors.get(i).getCoordX().getNumerator() == 0) {
					g2.drawLine(offsetX, Math.round(Math.round(d.getHeight()
							- offsetY)
							- (Math.round(vectors.get(i).getCoordZ().mul(
									stepWidth).div(vectors.get(i).getCoordY())
									.toFloat()))), offsetX
							+ Math.round(Math.round(d.getWidth() - 60)), Math
							.round(Math.round(d.getHeight() - offsetY)
									- (Math.round(vectors.get(i).getCoordZ()
											.mul(stepWidth).div(
													vectors.get(i).getCoordY())
											.toFloat()))));
					g2.drawString("NB" + (i + 1), offsetX
							+ (Math.round(vectors.get(i).getCoordZ().mul(
									stepWidth).div(vectors.get(i).getCoordX())
									.toFloat())), Math.round(Math.round(d
							.getHeight()))
							- (Math.round(vectors.get(i).getCoordZ().mul(
									stepWidth).div(vectors.get(i).getCoordY())
									.toFloat())) - offsetY - 5);
				} else if (vectors.get(i).getCoordY().getNumerator() == 0) {
					g2.drawLine(offsetX
							+ (Math.round(vectors.get(i).getCoordZ().mul(
									stepWidth).div(vectors.get(i).getCoordX())
									.toFloat())), Math.round(Math.round(d
							.getHeight()
							- offsetY)), offsetX
							+ (Math.round(vectors.get(i).getCoordZ().mul(
									stepWidth).div(vectors.get(i).getCoordX())
									.toFloat())), 30);
					g2.drawString("NB" + (i + 1), offsetX
							+ (Math.round(vectors.get(i).getCoordZ().mul(
									stepWidth).div(vectors.get(i).getCoordX())
									.toFloat())), Math.round(Math.round(d
							.getHeight()
							- offsetY) - 5));
				} else {
					g2.drawLine(offsetX
							+ (Math.round(vectors.get(i).getCoordZ().mul(
									stepWidth).div(vectors.get(i).getCoordX())
									.toFloat())), Math.round(Math.round(d
							.getHeight()
							- offsetY)), offsetX, Math.round(Math.round(d
							.getHeight()
							- offsetY)
							- (Math.round(vectors.get(i).getCoordZ().mul(
									stepWidth).div(vectors.get(i).getCoordY())
									.toFloat()))));
					g2.drawString("NB" + (i + 1), offsetX
							+ (Math.round(vectors.get(i).getCoordZ().mul(
									stepWidth).div(vectors.get(i).getCoordX())
									.toFloat())), Math.round(Math.round(d
							.getHeight()
							- offsetY) - 5));
				}
			}
			
			if (mouseCoords[0] != 0) {
			g2.drawString("(" + coordX + ";" + coordY + ")",
					mouseCoords[0] + 7, mouseCoords[1] + 7);
			mouseCoords[0] = offsetX + (stepWidth * coordX);
			mouseCoords[1] = d.height - offsetY - (stepWidth * coordY);
			panel.removeMouseListener(e);
		}
			
		}

		
	public static void saveMouseCoordinates(int x, int y) {
		mouseCoords[0] = x;
		mouseCoords[1] = y;
	    coordX = (mouseCoords[0] - offsetX) / stepWidth;
		coordY = (d.height - offsetY - mouseCoords[1]) / stepWidth;
	}

	
	private static void generateTestData() {

		vectors = new Vector<Vector3Frac>();
		target = Vector3FracFactory.getInstance();
		// ops = new String[] { "=", ">", "=" };
		// max = false;
		numVar = 3;
		for (int i = 0; i < numVar; i++)
		    vectors.add(Vector3FracFactory.getInstance());

		target.getCoordX().setNumerator(10);
		target.getCoordY().setNumerator(10);

		vectors.get(0).getCoordX().setNumerator(2);
		vectors.get(0).getCoordY().setNumerator(1);
		vectors.get(0).getCoordZ().setNumerator(8);

		vectors.get(1).getCoordX().setNumerator(3);
		vectors.get(1).getCoordY().setNumerator(4);
		vectors.get(1).getCoordZ().setNumerator(24);

		vectors.get(2).getCoordX().setNumerator(0);
		vectors.get(2).getCoordY().setNumerator(1);
		vectors.get(2).getCoordZ().setNumerator(1);

	    }
	
	private static float getScale() {
		float temp = 0;
		
		for (int i = 0; i < numVar; i ++) {
			if (vectors.get(i).getCoordZ().div(vectors.get(i).getCoordX()).toFloat() > temp) {
				temp = vectors.get(i).getCoordZ().div(vectors.get(i).getCoordX()).toFloat();
			}
			if (vectors.get(i).getCoordZ().div(vectors.get(i).getCoordY()).toFloat() > temp) {
				temp = vectors.get(i).getCoordZ().div(vectors.get(i).getCoordX()).toFloat();
			}
		}
		scale = temp + 3;
		return scale;
	}


	
	public static void main(String[] args) {
		
		generateTestData();
		
		getScale();
		
		System.out.println("Skalierung: " + scale);
		
		System.out.println("Zielfunktion: " + target.toString());
		for (int i = 0; i < numVar; i++) {
			System.out.println("Vektoren " + vectors.get(i).toString());
		}
		
		frame = new JFrame("SoPra LOP - Visualisierung des dualen Problems");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		panel = new TestDualPanel();
		
		e = new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
                saveMouseCoordinates(e.getX(),e.getY());
                panel.repaint();
            }
		};
		panel.addMouseListener(e);

		frame.setBackground(bg);
		
		frame.add(panel);
		frame.pack();
		frame.setSize(600, 600);
		frame.setLocation(400, 200);
		frame.setVisible(true);

	}

}