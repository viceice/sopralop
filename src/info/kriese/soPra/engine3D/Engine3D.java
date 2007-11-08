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
 * 08.11.2007 - Version 0.5.1
 * - LOP wird an Kegel weitergeben
 * 01.11.2007 - Version 0.5
 * - An neue Oberflächenstruktur angepasst
 * 30.10.2007 - Version 0.4.1
 * - FPS-Counter entfernt
 * 23.10.2007 - Version 0.4
 * - An neuen Quickhull-Algorithmus angepasst
 * 16.10.2007 - Version 0.3.6.2
 * - F6 zeigt Lösung an
 * 12.10.2007 - version 0.3.6.1
 * - BugFix: Fehlerhafte Skalierung führte zur Exception
 * 11.10.2007 - version 0.3.6
 * - Neues Skalierungsverfahren
 * 10.10.2007 - Version 0.3.5
 * - Startdrehung des Koordinatensystems verbessert
 * - Startposition verbessert
 * - Lösung wird auch bei Spezialfällen korrekt angezeigt
 * 03.10.2007 - Version 0.3.4
 * - LOPListener gegen LOPAdapter getauscht
 * 02.10.2007 - Version 0.3.3
 * - Skalierungsberechnung geändert
 * 17.09.2007 - Version 0.3.2.2
 * - Cliping Distanzen geändert
 * 16.09.2007 - Version 0.3.2.1
 * - An neues LOPSolution Interface angepasst
 * 14.09.2007 - Version 0.3.2
 * - Vektoren werden zum konvexem Kegel sortiert
 * - Änderung in Skalierung
 * 11.09.2007 - Version 0.3.1
 *  - LOPListener angepasst
 * 10.09.2007 - Version 0.3
 *  - Auf LOPListener umgestellt
 * 30.07.2007 - Version 0.2.1
 *  - 3D-Welt mit [F5] zuruecksetzbar
 *  - NullPointerException beseitigt
 *  - Kann jetzt dynamisch veraendert werden 
 * 27.07.2007 - Version 0.2
 *  - Strukturaenderungen
 *  - Rueckkehr zum SimpleUniverse
 *  - Navigation ueber Orbitbehavior
 *  - resetScene() neugeschrieben
 *  - Neuimplementierung des Canvas3D
 * 26.04.2007 - Version 0.1
 *  - Datei hinzugefuegt
 */

package info.kriese.soPra.engine3D;

import info.kriese.soPra.engine3D.objects.Cone3D;
import info.kriese.soPra.engine3D.objects.CoordinatePlane3D;
import info.kriese.soPra.engine3D.objects.GeomObjects;
import info.kriese.soPra.engine3D.objects.Point3D;
import info.kriese.soPra.engine3D.objects.Target3D;
import info.kriese.soPra.gui.Virtual3DFrame;
import info.kriese.soPra.lop.LOP;
import info.kriese.soPra.lop.LOPAdapter;
import info.kriese.soPra.lop.LOPSolution;
import info.kriese.soPra.math.Vector3Frac;
import info.kriese.soPra.math.quickhull.QuickHull;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.GraphicsConfigTemplate3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

/**
 * Stellt Methoden zur Berechnung der 3D-Szene bereit.
 * 
 * @author Michael Kriese
 * @version 0.5.1
 * @since 26.04.2007
 */
public final class Engine3D {

    /** */
    private static final long serialVersionUID = 1L;

    private final Canvas3D canvas;

    private final Cone3D cone;

    private final CoordinatePlane3D coordsPlane;

    private final TransformGroup elemsGroup, hudGroup;

    private final QuickHull hull;

    private final Point3D intersection;

    private OrbitBehavior orbit;

    private float size = 15.0f;

    private SimpleUniverse su;

    private final Target3D targetLine;

    public Engine3D() {

	this.canvas = new Canvas3D(createConfig());
	this.canvas.addKeyListener(new KeyAdapter() {

	    @Override
	    public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_F2)
		    resetScene();
	    }
	});

	this.hull = new QuickHull();

	this.hudGroup = new TransformGroup();
	this.elemsGroup = new TransformGroup();
	Transform3D elemsTransform = new Transform3D(), rot = new Transform3D();

	createUserControls();
	createLight();

	// Drehe zu std. Koordinatensystem (z nach oben, x nach vorn und y nach
	// rechts)
	rot.setEuler(new Vector3d(-Math.PI / 2.0, -Math.PI / 6 * 4, 0.0));
	// Kippe Koordinatensystem leicht nach vorn links
	elemsTransform.setEuler(new Vector3d(Math.PI / 8.0, 0.0, 0.0));
	elemsTransform.mul(rot);

	this.elemsGroup.setTransform(elemsTransform);
	this.elemsGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
	this.elemsGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	this.elemsGroup
		.setCapabilityIsFrequent(TransformGroup.ALLOW_TRANSFORM_READ);
	this.elemsGroup
		.setCapabilityIsFrequent(TransformGroup.ALLOW_TRANSFORM_WRITE);

	this.cone = new Cone3D();
	this.elemsGroup.addChild(this.cone);

	this.coordsPlane = new CoordinatePlane3D();
	this.elemsGroup.addChild(this.coordsPlane);

	this.targetLine = new Target3D();
	this.elemsGroup.addChild(this.targetLine);

	// fuege Schnittpunkt hinzu
	this.intersection = new Point3D();
	this.elemsGroup.addChild(this.intersection);
	// fuege Maximum hinzu

	BranchGroup bg = new BranchGroup();
	bg.addChild(this.elemsGroup);
	bg.compile();
	this.su.addBranchGraph(bg);

	bg = new BranchGroup();
	bg.addChild(this.hudGroup);
	bg.compile();
	this.su.getViewingPlatform().addChild(bg);
    }

    public void addConnection(Virtual3DFrame conn) {
	conn.addCanvas(this.canvas);
    }

    public void resetScene() {
	TransformGroup targetTG = this.su.getViewingPlatform()
		.getViewPlatformTransform();
	Transform3D t3d = new Transform3D();
	t3d.setTranslation(new Vector3d(0, 1, this.size + 15.0));
	targetTG.setTransform(t3d);
    }

    public void setLOP(LOP lop) {
	lop.addProblemListener(new LOPAdapter() {
	    @Override
	    public void problemChanged(LOP lop) {
		Engine3D.this.intersection.setVisible(false);
		Engine3D.this.computeProblem(lop);
	    }

	    @Override
	    public void problemSolved(LOP lop) {
		Engine3D.this.computeSolution(lop);
	    }

	    @Override
	    public void showSolution(LOP lop) {
		Engine3D.this.showSolution(lop);
	    }
	});

	this.cone.setLOP(lop);

    }

    public void showSolution(LOP lop) {
	if (lop.isSolved())
	    this.intersection.setVisible(true);
    }

    private void computeProblem(LOP lop) {

	Vector3Frac vec = lop.getTarget();

	this.size = (vec.getCoordX().toFloat() > vec.getCoordY().toFloat() ? vec
		.getCoordX().toFloat()
		: vec.getCoordY().toFloat()) + 3.0f;

	this.intersection.setVisible(false);

	this.hull.build(lop.getVectors());

	// fuege Koordinatensystem hinzu
	this.coordsPlane.compute(this.size);

	// fuege Kegel hinzu
	this.cone.compute(this.hull.getVerticesList(), this.size);

	// fuege ZielVektor hinzu
	this.targetLine.compute(lop.getTarget().toVector3f(), this.size);

	resetScene();
    }

    private void computeSolution(LOP lop) {
	int sCase = lop.getSolution().getSpecialCase();

	if (sCase == LOPSolution.SIMPLE
		|| sCase == LOPSolution.MORE_THAN_ONE_SOLUTION)
	    this.size = (this.size > lop.getSolution().getValue() + 3.0f ? this.size
		    : (float) lop.getSolution().getValue()) + 3.0f;

	this.intersection.setVisible(false);

	this.hull.build(lop.getVectors());

	// fuege Koordinatensystem hinzu
	this.coordsPlane.compute(this.size);

	// fuege Kegel hinzu
	this.cone.compute(this.hull.getVerticesList(), this.size);

	// fuege ZielVektor hinzu
	this.targetLine.compute(lop.getTarget().toVector3f(), this.size);

	this.intersection.compute(lop.getSolution().getVector().toVector3f());

	resetScene();
    }

    /**
     * @return
     */
    private GraphicsConfiguration createConfig() {
	GraphicsConfigTemplate3D template = new GraphicsConfigTemplate3D();
	GraphicsEnvironment env = GraphicsEnvironment
		.getLocalGraphicsEnvironment();
	GraphicsDevice device = env.getDefaultScreenDevice();
	GraphicsConfiguration config = device.getBestConfiguration(template);
	return config;
    }

    private void createLight() {
	this.elemsGroup.addChild(GeomObjects.getLight());

	DirectionalLight headlight = new DirectionalLight();
	headlight.setColor(Tools3D.WHITE);
	headlight.setInfluencingBounds(Tools3D.LIGHT_BOUNDS);
	headlight.setDirection(new Vector3f(0.0f, 0.0f, -1.0f));
	this.hudGroup.addChild(headlight);
    }

    private void createUserControls() {

	this.su = new SimpleUniverse(this.canvas);

	// depth-sort transparent objects on a per-geometry basis
	View view = this.su.getViewer().getView();
	view.setTransparencySortingPolicy(View.TRANSPARENCY_SORT_GEOMETRY);
	view.setBackClipDistance(Tools3D.BACK_CLIP_DISTANCE);
	view.setFrontClipDistance(Tools3D.FRONT_CLIP_DISTANCE);

	ViewingPlatform vp = this.su.getViewingPlatform();

	// position viewpoint
	resetScene();

	// set up keyboard controls to move the viewpoint
	this.orbit = new OrbitBehavior(this.canvas, OrbitBehavior.REVERSE_ALL
		| OrbitBehavior.PROPORTIONAL_ZOOM);
	this.orbit.setSchedulingBounds(Tools3D.LIGHT_BOUNDS);
	vp.setViewPlatformBehavior(this.orbit);
    }

}
