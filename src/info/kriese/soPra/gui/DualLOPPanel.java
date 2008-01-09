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
 * 04.12.2007 - Version 0.2.2
 * - Fehlerbereinigung bei den Richtungsvektoren
 * - Änderung der Beschriftung an den Vektoren
 * 28.11.2007 - Version 0.2.1
 * - Skalierung nochmals angepasst
 * - Vektoren, die in x1 oder x2 gleich null sind, werden angezeigt
 * 27.11.2007 - Version 0.2
 * - Skalierungsfehler behoben
 * 10.11.2007 - Version 0.1
 *  - Aus TestDualPanel extrahiert
 */
package info.kriese.soPra.gui;

import info.kriese.soPra.lop.LOP;
import info.kriese.soPra.lop.LOPAdapter;
import info.kriese.soPra.lop.LOPSolution;
import info.kriese.soPra.math.Gauss;
import info.kriese.soPra.math.Vector3Frac;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

/**
 * 
 * @author Peer Sterner
 * @version 0.1
 * @since 10.11.2007
 * 
 */
public final class DualLOPPanel extends JPanel {

    private static final float dash1[] = { 1.5f };

    private static final BasicStroke dashed = new BasicStroke(1.0f,
	    BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);

    private static final Color fg = Color.BLACK, bg = Color.WHITE,
	    element = Color.RED, optimum = Color.BLUE, grey = Color.GRAY;

    private static final long serialVersionUID = 1L;

    private static final BasicStroke stroke = new BasicStroke(2.0f);

    private Dimension d;
    private boolean minMax;

    private final BasicStroke normal = new BasicStroke(1.0f);

    private final int offsetX = 30, offsetY = 30;

    private float scale, scaleFactor;

    private LOPSolution solution;

    private int stepWidth, numVar;

    private Vector3Frac target;

    private Vector3Frac tmp;

    private List<Vector3Frac> vectors;

    public DualLOPPanel() {
	this.target = Vector3Frac.ZERO;
	this.tmp = Vector3Frac.ZERO;
	this.vectors = new ArrayList<Vector3Frac>();
	this.solution = null;
	setBackground(bg);
    }

    /**
     * Methode zum Zeichnen des Diagrammes zur Visualisierung des dualen Problems.
     * 
     */
    @Override
    public void paint(Graphics g) {
	// Zeichne Hintergrund
	super.paint(g);

	// Falls Handler noch nicht zugewiesen, abbrechen
	if (this.solution == null)
	    return;

	Graphics2D g2 = (Graphics2D) g;
	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		RenderingHints.VALUE_ANTIALIAS_ON);
	this.d = getSize();

	this.stepWidth = Math.round((this.d.width / this.scale));
	if (((this.d.height - 2 * this.offsetY) / this.stepWidth) < this.scale)
	    this.stepWidth = Math.round(this.d.height / this.scale);

	// Linien und Beschriftung des Koordinatensystems
	g2.setPaint(fg);
	g2.setStroke(this.normal);
	g2.drawLine(this.offsetX, this.offsetY, this.offsetX, this.d.height
		- this.offsetY);
	g2.drawString("^", 27, 39);
	g2.drawString("X", 12, 30);
	g2.drawString("2", 19, 33);
	g2.drawLine(this.offsetX, this.d.height - this.offsetY, this.d.width
		- this.offsetX, this.d.height - this.offsetY);
	g2.drawString("X", this.d.width - 34, this.d.height - 13);
	g2.drawString("1", this.d.width - 27, this.d.height - 10);
	g2.drawString(">", this.d.width - 35, this.d.height - 26);
	g2.setPaint(element);
	g2.drawLine(this.offsetX, 55, this.offsetX + 15, 55);
	g2.drawString(">", this.offsetX + 10, 59);
	g2.drawLine(this.d.width - 65, this.d.height - this.offsetY,
		this.d.width - 65, this.d.height - this.offsetY - 15);
	g2.drawString("^", this.d.width - 68, this.d.height - this.offsetY - 8);
	g2.setPaint(fg);

	// Hilfslinien und Koordinaten der X-Achse
	g2.setStroke(dashed);
	g2.setPaint(grey);
	for (int step = this.offsetX; step <= this.d.width - 50; step += this.stepWidth
		* this.scaleFactor) {
	    g2.drawLine(step, this.d.height - this.offsetY, step, this.offsetY);
	    g2.setPaint(fg);
	    g2.drawString(Math
		    .round((((step - this.offsetX) / this.scaleFactor * 10) // *
												// 10)
			    / this.stepWidth)
			    * this.scaleFactor)
		    / 10.0 + "", step - 3, this.d.height - 15);
	}

	// Hilfslinien und Koordinaten der Y-Achse
	for (int step = this.d.height - this.offsetY; step >= 40; step -= this.stepWidth
		* this.scaleFactor) {
	    g2.drawLine(this.offsetX, step, this.d.width - 30, step);
	    g2.setPaint(fg);
	    g2.drawString(Math.round((((this.d.height - this.offsetY - step)
		    / this.scaleFactor * 10) // *10
	    / this.stepWidth)
		    * this.scaleFactor)
		    / 10.0 + "", 13, step + 5);
	}

	// Zeichnen der der aus den Vektoren abgeleiteten Geraden
	g2.setPaint(element);
	g2.setStroke(stroke);
	for (int i = 0; i < this.numVar; i++) {
	    int localCoordX1 = this.offsetX
		    + (Math.round((this.vectors.get(i).getCoordZ().mul(
			    this.stepWidth)
			    .div(this.vectors.get(i).getCoordX()).toFloat())
			    / this.scaleFactor));
	    int localCoordY1 = Math.round(Math.round((this.d.getHeight())
		    - this.offsetY));
	    int localCoordX2 = this.offsetX
		    + Math.round(Math.round((this.d.getWidth()) - 60));
	    int localCoordY2 = Math
		    .round(Math.round((this.d.getHeight()) - this.offsetY)
			    - (Math.round(this.vectors.get(i).getCoordZ().mul(
				    this.stepWidth).div(
				    this.vectors.get(i).getCoordY()).toFloat()) / this.scaleFactor));

	    if (localCoordX1 >= this.offsetX
		    && localCoordY2 <= this.d.getHeight() - this.offsetY) {
		if (this.vectors.get(i).getCoordX().isZero()) {
		    g2.drawLine(this.offsetX, localCoordY2, localCoordX2,
			    localCoordY2);
		    g2.drawString("NB" + (i + 1), localCoordX1,
			    localCoordY2 - 5);
		} else if (this.vectors.get(i).getCoordY().isZero()) {
		    g2.drawLine(localCoordX1, localCoordY1, localCoordX1,
			    this.offsetY);
		    g2.drawString("NB" + (i + 1), localCoordX1,
			    localCoordY1 - 5);
		} else {
		    g2.drawLine(localCoordX1, localCoordY1, this.offsetX,
			    localCoordY2);
		    g2.drawString("NB" + (i + 1), localCoordX1,
			    localCoordY1 - 5);
		}

		// Zeichnen der Senkrechten auf den Geraden (Visualisierung der
		// Verschieberichtung)
		double horizontal, vertical;
		int dirVecX, dirVecY, dirLength = 15;
		
		if (!this.minMax) {
			dirLength = -15;
		}
		
		g2.setStroke(this.normal);
		if (this.vectors.get(i).getCoordX().isZero())
		    g2.drawLine((this.d.width - this.offsetX) / 2,
			    localCoordY2, (this.d.width - this.offsetX) / 2,
			    localCoordY2 - dirLength);
		else if (this.vectors.get(i).getCoordY().isZero())
		    g2.drawLine(localCoordX1,
			    (this.d.height - this.offsetX) / 2,
			    localCoordX1 + dirLength,
			    (this.d.height - this.offsetX) / 2);
		else {
		    horizontal = localCoordX1 - this.offsetX;
		    vertical = this.d.height - localCoordY2 - this.offsetY;
		    double lineLength = Math
			    .sqrt((Math.pow(horizontal, 2) + Math.pow(vertical,
				    2)));
		    double angleA = (Math.PI / 2)
			    - Math.cos((horizontal / lineLength));
		    double c = (15 / Math.sin(angleA));
		    double p = (225 / c);
		    double q = (c - p);
		    double h = Math.sqrt(p * q);
		    dirVecX = (int) (horizontal / 2);
		    dirVecY = (int) ((vertical * dirVecX) / horizontal);

		    if (this.minMax)
			g2.drawLine(this.offsetX + dirVecX, this.d.height
				- this.offsetY - dirVecY, this.offsetX
				+ dirVecX + (int) h, this.d.height
				- this.offsetY - dirVecY - (int) p);
		    else
			g2.drawLine(this.offsetX + dirVecX, this.d.height
				- this.offsetY - dirVecY, this.offsetX
				+ dirVecX - (int) h, this.d.height
				- this.offsetY - dirVecY + (int) p);
		}
		g2.setStroke(stroke);
	    }
	}

	// Zeichnen des Strahls, der durch das Optimum geht (nur, wenn es
	// mindestens eine Lösung gibt)
	if (this.solution.getSpecialCase() == LOPSolution.SIMPLE)
	    if (this.tmp.getCoordX().toFloat() >= 0
		    && this.tmp.getCoordY().toFloat() >= 0) {

		g2.setPaint(optimum);
		int localOptimumX1 = this.offsetX
			+ (Math.round((this.solution.getVector().getCoordZ()
				.mul(this.stepWidth).div(
					this.target.getCoordX()).toFloat())
				/ this.scaleFactor));
		int localOptimumY1 = Math.round(Math.round(this.d.getHeight()
			- this.offsetY));
		int localOptimumX2 = this.offsetX
			+ Math.round(Math.round(this.d.getWidth() - 60));
		int localOptimumY2 = Math
			.round(Math.round(this.d.getHeight() - this.offsetY)
				- (Math.round(this.solution.getVector()
					.getCoordZ().mul(this.stepWidth).div(
						this.target.getCoordY())
					.toFloat()) / this.scaleFactor));

		if (this.target.getCoordX().getNumerator() == 0) {
		    g2.drawLine(this.offsetX, localOptimumY2, localOptimumX2,
			    localOptimumY2);
		    g2
			    .drawString("Optimum", localOptimumX1,
				    localOptimumY2 - 5);
		} else if (this.target.getCoordY().getNumerator() == 0) {
		    g2.drawLine(localOptimumX1, localOptimumY1, localOptimumX1,
			    this.offsetY);
		    g2
			    .drawString("Optimum", localOptimumX1,
				    localOptimumY2 - 5);
		} else {
		    g2.drawLine(localOptimumX1, localOptimumY1, this.offsetX,
			    localOptimumY2);
		    g2.drawString("Gerade der Zielfunktion", ((localOptimumX1 - this.offsetX) / 3) + this.offsetX + 5,
			    localOptimumY2 + (this.d.height - this.offsetY - localOptimumY2) / 3);
		}

		// Zeichnen der Senkrechten auf der optimalen Geraden
		// (Visualisierung der Verschieberichtung)
		double horizontal = localOptimumX1 - this.offsetX;
		double vertical = this.d.height - localOptimumY2 - this.offsetY;
		double lineLength = Math.sqrt((Math.pow(horizontal, 2) + Math
			.pow(vertical, 2)));
		double angleA = (Math.PI / 2)
			- Math.cos((horizontal / lineLength));
		double c = (15 / Math.sin(angleA));
		double p = (225 / c);
		double q = (c - p);
		double h = Math.sqrt(p * q);
		int dirVecX = (int) (horizontal / 2);
		int dirVecY = (int) ((vertical * dirVecX) / horizontal);

		g2.setStroke(this.normal);
		if (this.minMax)
		    g2.drawLine(this.offsetX + dirVecX, this.d.height
			    - this.offsetY - dirVecY, this.offsetX + dirVecX
			    - (int) h, this.d.height - this.offsetY - dirVecY
			    + (int) p);
		else
		    g2.drawLine(this.offsetX + dirVecX, this.d.height
			    - this.offsetY - dirVecY, this.offsetX + dirVecX
			    + (int) h, this.d.height - this.offsetY - dirVecY
			    - (int) p);
		g2.setStroke(stroke);

		g2.fillOval(Math.round((this.offsetX + this.tmp.getCoordX()
			.mul(this.stepWidth).toFloat())
			/ this.scaleFactor) - 3, localOptimumY1
			- Math.round(this.tmp.getCoordY().mul(this.stepWidth)
				.toFloat()) - 3, 9, 9);
		g2.drawString("Optimum (" + this.tmp.getCoordX() + ", "
			+ this.tmp.getCoordY() + ")", this.offsetX
			+ this.tmp.getCoordX().mul(this.stepWidth).toFloat()
			+ 5, localOptimumY1
			- Math.round(this.tmp.getCoordY().mul(this.stepWidth)
				.toFloat()) - 3);
	    }
    }

    /**
     * Methode zum Transponieren der Vektormatrix (notwendig zur Darstellung des dualen LOP).
     * 
     * @param lop - das aktuell bearbeitete LOP
	 *
     */
    public void setLOP(LOP lop) {
	lop.addProblemListener(new LOPAdapter() {
	    @Override
	    public void problemSolved(LOP lop) {
		DualLOPPanel.this.solution = lop.getSolution();

		DualLOPPanel.this.minMax = lop.isMaximum();

		DualLOPPanel.this.numVar = lop.getVectors().size();

		DualLOPPanel.this.target = lop.getTarget();

		DualLOPPanel.this.vectors = lop.getVectors();

		if (DualLOPPanel.this.solution.getSpecialCase() != LOPSolution.NO_SOLUTION) {
		    Vector3Frac vec1 = DualLOPPanel.this.solution.getAreas()
			    .get(0).getL1();
		    Vector3Frac vec2 = DualLOPPanel.this.solution.getAreas()
			    .get(0).getL2();

		    Vector3Frac Vector1 = Vector3Frac.ZERO.clone();
		    Vector3Frac Vector2 = Vector3Frac.ZERO.clone();
		    Vector3Frac Vector3 = Vector3Frac.ZERO.clone();

		    Vector1.setCoordX(vec1.getCoordX());
		    Vector1.setCoordY(vec2.getCoordX());
		    Vector1.setCoordZ(DualLOPPanel.this.target.getCoordX());

		    Vector2.setCoordX(vec1.getCoordY());
		    Vector2.setCoordY(vec2.getCoordY());
		    Vector2.setCoordZ(DualLOPPanel.this.target.getCoordY());

		    Vector3.setCoordX(vec1.getCoordZ());
		    Vector3.setCoordY(vec2.getCoordZ());
		    Vector3.setCoordZ(DualLOPPanel.this.target.getCoordZ());

		    DualLOPPanel.this.tmp = Gauss.gaussElimination2(Vector1,
			    Vector2, Vector3);
		}

		setScale();
	    }
	});
    }

    /**
     * Methode zum Berechnen des Skalierungsfaktors des Koordinatensystems
     * zur feineren Darstellung der Vektorengeraden
	 *
     */
    private void setScale() {
	float temp = 0;
	float scaleTemp1;
	float scaleTemp2;

	for (int i = 0; i < this.vectors.size(); i++) {
	    scaleTemp1 = this.vectors.get(i).getCoordZ().div(
		    this.vectors.get(i).getCoordX()).toFloat();
	    scaleTemp2 = this.vectors.get(i).getCoordZ().div(
		    this.vectors.get(i).getCoordY()).toFloat();
	    if (scaleTemp1 > temp)
		temp = scaleTemp1;
	    if (scaleTemp2 > temp)
		temp = scaleTemp2;
	}
	if (temp >= 1) {
	    this.scale = temp + 2;
	    this.scaleFactor = 1;
	} else {
	    this.scale = (float) 1.2;
	    this.scaleFactor = (float) 0.1;
	}
    }

}
