/**
 * @author Peer Sterner
 * @version $Id$
 * @since 25.10.2007
 */
package info.kriese.soPra.test;

import info.kriese.soPra.SoPraLOP;
import info.kriese.soPra.gui.ActionHandler;
import info.kriese.soPra.io.IOUtils;
import info.kriese.soPra.lop.LOP;
import info.kriese.soPra.lop.LOPEditor;
import info.kriese.soPra.lop.impl.LOPFactory;
import info.kriese.soPra.lop.LOPSolution;
import info.kriese.soPra.math.Gauss;
import info.kriese.soPra.math.LOPSolver;
import info.kriese.soPra.math.Vector3Frac;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.List;

/**
 * 
 * 
 * @author Peer Sterner
 * @version 0.1
 * @since 07.11.2007
 * 
 */
public class TestDualPanel extends JPanel {

	private static Dimension d;

	private static Color fg = Color.BLACK, bg = Color.WHITE,
			element = Color.RED, optimum = Color.BLUE, grey = Color.GRAY;

	private static JFrame frame;

	private static int offsetX = 30, offsetY = 30, stepWidth, numVar;

	private static JPanel panel;

	private static float scale, scaleFactor, dash1[] = { 1.5f },
			dash2[] = { 10.0f };

	private static final long serialVersionUID = 1769367299092520935L;

	private static LOPSolution solution;

	private static Vector3Frac target, vec1, vec2, tmp;

	private static List<Vector3Frac> vectors;

	// private static String[] operators;

	final static BasicStroke dashed = new BasicStroke(1.0f,
			BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);

	final static BasicStroke dashed_bold = new BasicStroke(2.0f,
			BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash2, 0.0f);

	final static BasicStroke normal = new BasicStroke(1.0f);

	final static BasicStroke stroke = new BasicStroke(2.0f);

	public static void main(String[] args) {
		try { // use the local look and feel
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}

		generateData();

		frame = new JFrame("SoPra LOP - Visualisierung des Dualen Problems");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		panel = new TestDualPanel();

		frame.setBackground(bg);
		frame.add(panel);
		frame.pack();
		frame.setSize(600, 600);
		frame.setLocation(400, 200);
		frame.setVisible(true);

	}

	private static void generateData() {

		LOP lop = LOPFactory.newLinearOptimizingProblem();
		LOPEditor editor = LOPFactory.newLOPEditor(lop);
		LOPSolver solver = new LOPSolver();
		solver.setEditor(editor);
		editor.open(IOUtils.getURL("problems/minEqualsMax.lop"));
		ActionHandler.INSTANCE.setLOP(lop);
		SoPraLOP.EDITOR = editor;
		editor.update();

		vectors = editor.getLOP().getVectors();
		target = editor.getLOP().getTarget();
		// operators = editor.getLOP().getOperators();
		numVar = editor.getLOP().getVectors().size();
		solution = editor.getLOP().getSolution();
		if (solution.getSpecialCase() != LOPSolution.NO_SOLUTION) {
			vec1 = solution.getAreas().get(0).getL1();
			vec2 = solution.getAreas().get(0).getL2();
		}

		Vector3Frac Vector1 = Vector3Frac.ZERO.clone();
		Vector3Frac Vector2 = Vector3Frac.ZERO.clone();
		Vector3Frac Vector3 = Vector3Frac.ZERO.clone();

		Vector1.setCoordX(vec1.getCoordX());
		Vector1.setCoordY(vec2.getCoordX());
		Vector1.setCoordZ(target.getCoordX());

		Vector2.setCoordX(vec1.getCoordY());
		Vector2.setCoordY(vec2.getCoordY());
		Vector2.setCoordZ(target.getCoordY());

		Vector3.setCoordX(vec1.getCoordZ());
		Vector3.setCoordY(vec2.getCoordZ());
		Vector3.setCoordZ(target.getCoordZ());

		tmp = Gauss.gaussElimination2(Vector1, Vector2, Vector3);

		setScale();
	}

	private static void setScale() {
		float temp = 0;
		float scaleTemp1;
		float scaleTemp2;

		for (int i = 0; i < vectors.size(); i++) {
			scaleTemp1 = vectors.get(i).getCoordZ().div(
					vectors.get(i).getCoordX()).toFloat();
			scaleTemp2 = vectors.get(i).getCoordZ().div(
					vectors.get(i).getCoordY()).toFloat();
			if (scaleTemp1 > temp)
				temp = scaleTemp1;
			if (scaleTemp2 > temp)
				temp = scaleTemp2;
		}
		if (temp >= 1) {
			scale = temp + 2;
			scaleFactor = 1;
		} else {
			scale = (float) 1.2;
			scaleFactor = (float) 0.1;
		}
	}

	/**
	 * @param args
	 */

	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		d = getSize();

		stepWidth = Math.round((d.width / scale));
		if (((d.height - 2 * offsetY) / stepWidth) < scale)
			stepWidth = Math.round(d.height / scale);

		// Linien und Beschriftung des Koordinatensystems
		g2.setPaint(fg);
		g2.setStroke(normal);
		g2.drawLine(offsetX, offsetY, offsetX, d.height - offsetY);
		g2.drawString("^", 27, 39);
		g2.drawString("X", 12, 30);
		g2.drawString("2", 19, 33);
		g2.drawLine(offsetX, d.height - offsetY, d.width - offsetX, d.height
				- offsetY);
		g2.drawString("X", d.width - 34, d.height - 13);
		g2.drawString("1", d.width - 27, d.height - 10);
		g2.drawString(">", d.width - 35, d.height - 26);

		// Hilfslinien und Koordinaten der X-Achse
		g2.setStroke(dashed);
		g2.setPaint(grey);
		for (int step = offsetX; step <= d.width - 50; step += stepWidth
				* scaleFactor) {
			g2.drawLine(step, d.height - offsetY, step, offsetY);
			g2.setPaint(fg);
			g2.drawString(Math.round(((step - offsetX) * 10) / stepWidth)
					/ (10.0 / scaleFactor) + "", step - 3, d.height - 15);
		}

		// Hilfslinien und Koordinaten der Y-Achse
		for (int step = d.height - offsetY; step >= 40; step -= stepWidth
				* scaleFactor) {
			g2.drawLine(offsetX, step, d.width - 30, step);
			g2.setPaint(fg);
			g2.drawString(Math.round(((d.height - offsetY - step) * 10)
					/ stepWidth)
					/ (10.0 / scaleFactor) + "", 13, step + 5);
		}

		// Zeichnen der der aus den Vektoren abgeleiteten Geraden
		g2.setPaint(element);
		g2.setStroke(stroke);
		for (int i = 0; i < numVar; i++) {
			int localCoordX1 = offsetX
					+ (Math.round((vectors.get(i).getCoordZ().mul(stepWidth)
							.div(vectors.get(i).getCoordX()).toFloat())
							/ scaleFactor));
			int localCoordY1 = Math
					.round(Math.round((d.getHeight()) - offsetY));
			int localCoordX2 = offsetX
					+ Math.round(Math.round((d.getWidth()) - 60));
			int localCoordY2 = Math
					.round(Math.round((d.getHeight()) - offsetY)
							- (Math.round(vectors.get(i).getCoordZ().mul(
									stepWidth).div(vectors.get(i).getCoordY())
									.toFloat()) / scaleFactor));

			if (localCoordX1 >= offsetX
					&& localCoordY2 <= d.getHeight() - offsetY)
				if (vectors.get(i).getCoordX().isZero()) {
					g2.drawLine(offsetX, localCoordY2, localCoordX2,
							localCoordY2);
					g2.drawString("NB" + (i + 1), localCoordX1,
							localCoordY2 - 5);
				} else if (vectors.get(i).getCoordY().isZero()) {
					g2.drawLine(localCoordX1, localCoordY1, localCoordX1,
							offsetY);
					g2.drawString("NB" + (i + 1), localCoordX1,
							localCoordY1 - 5);
				} else {
					g2.drawLine(localCoordX1, localCoordY1, offsetX,
							localCoordY2);
					g2.drawString("NB" + (i + 1), localCoordX1,
							localCoordY1 - 5);
				}
			
			// TODO: Richtungvektoren der Lösungsgeraden (abhängig vom
			// Relationszeichen)
			
		}

		// Zeichnen des Strahls, der durch das Optimum geht (nur, wenn es mindestens eine Lösung gibt)
		if (solution.getSpecialCase() == LOPSolution.SIMPLE) {
			if (tmp.getCoordX().toFloat() >= 0
					&& tmp.getCoordY().toFloat() >= 0) {

				g2.setPaint(optimum);
				int localOptimumX1 = offsetX
						+ (Math.round((solution.getVector().getCoordZ().mul(
								stepWidth).div(target.getCoordX()).toFloat())
								/ scaleFactor));
				int localOptimumY1 = Math.round(Math.round(d.getHeight()
						- offsetY));
				int localOptimumX2 = offsetX
						+ Math.round(Math.round(d.getWidth() - 60));
				int localOptimumY2 = Math
						.round(Math.round(d.getHeight() - offsetY)
								- (Math.round(solution.getVector().getCoordZ()
										.mul(stepWidth).div(target.getCoordY())
										.toFloat()) / scaleFactor));

				if (target.getCoordX().getNumerator() == 0) {
					g2.drawLine(offsetX, localOptimumY2, localOptimumX2,
							localOptimumY2);
					g2
							.drawString("Optimum", localOptimumX1,
									localOptimumY2 - 5);
				} else if (target.getCoordY().getNumerator() == 0) {
					g2.drawLine(localOptimumX1, localOptimumY1, localOptimumX1,
							offsetY);
					g2
							.drawString("Optimum", localOptimumX1,
									localOptimumY2 - 5);
				} else {
					g2.drawLine(localOptimumX1, localOptimumY1, offsetX,
							localOptimumY2);
					g2.drawString("Gerade der Zielfunktion", offsetX + 5,
							localOptimumY2 - 5);
				}
				
				// TODO: Richtungvektoren der Lösungsgeraden (abhängig vom
				// Relationszeichen)
				
				g2.fillOval(Math.round((offsetX + tmp.getCoordX()
						.mul(stepWidth).toFloat())
						/ scaleFactor) - 3, localOptimumY1
						- Math.round(tmp.getCoordY().mul(stepWidth).toFloat())
						- 3, 9, 9);
				g2.drawString("Optimum (" + tmp.getCoordX() + ", "
						+ tmp.getCoordY() + ")", offsetX
						+ tmp.getCoordX().mul(stepWidth).toFloat() + 5,
						localOptimumY1
								- Math.round(tmp.getCoordY().mul(stepWidth)
										.toFloat()) - 3);
			}

		}
	}

}
