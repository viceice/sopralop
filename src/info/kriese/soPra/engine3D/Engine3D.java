/**
 * @version		$Id$
 * @copyright	(c)2007-2008 Michael Kriese & Peer Sterner
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
 * 25.01.2008 - Version 0.5.5
 * - Variablennamen für Spezialfälle angepasst
 * 21.01.2008 - Version 0.5.4
 * - Vektor für duales Problem hinzugefügt
 * 15.01.2008 - Version 0.5.3
 * - Zielfunktion bekommt jetz Lösung übergeben (zur Skalierung nach unten oder oben)
 * 09.11.2007 - Version 0.5.2
 * - Lösung nicht mehr unsichtbar
 * - Änderung der Scene nur noch bei problemsolved, da nach problemChanged das
 *    Problem sofort gelöst wird (Performancegewinn)
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
 * TODO: Beleuchtung verbessern
 * 
 * @author Michael Kriese
 * @version 0.5.5
 * @since 26.04.2007
 */
public final class Engine3D {

    /**
     * Zeichenfläche, auf die gerendert wird.
     */
    private final Canvas3D canvas;

    /**
     * Wrapper-Objekt, welches den Kegel repräsentiert.
     */
    private final Cone3D cone;

    /**
     * Wrapper-Objekt, welches das Koordinatensystem repräsentiert.
     */
    private final CoordinatePlane3D coordsPlane;

    /**
     * Elemente zur Gruppierung von 3D-Objekten.
     */
    private final TransformGroup elemsGroup, hudGroup;

    /**
     * Quickhull-Algorithmus, zur Berechnung des konvexen Kegels.
     */
    private final QuickHull hull;

    /**
     * Wrapper-Objekt, welches den Schnittpunkt repräsentiert.
     */
    private final Point3D intersection;

    /**
     * Ermöglicht die Bewegung im Raum.
     */
    private OrbitBehavior orbit;

    /**
     * Skalierungsfaktor für alle 3D-Objekte.
     */
    private float size = 15.0f;

    /**
     * Verbindet die 3D-Objekte mir der Zeichenfläche.
     */
    private SimpleUniverse su;

    /**
     * Wrapper-Objekt, welches die Zielfunktion repräsentiert.
     */
    private final Target3D targetLine;

    /**
     * Konstruktor, er erstellt alle benötigten Objekte.
     */
    public Engine3D() {

	this.canvas = new Canvas3D(createConfig());
	this.canvas.addKeyListener(new KeyAdapter() {

	    @Override
	    public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_F2) {
		    resetScene();
		    e.consume();
		}
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

	BranchGroup bg = new BranchGroup();
	bg.addChild(this.elemsGroup);
	bg.compile();
	this.su.addBranchGraph(bg);

	bg = new BranchGroup();
	bg.addChild(this.hudGroup);
	bg.compile();
	this.su.getViewingPlatform().addChild(bg);
    }

    /**
     * Verknüpt die Zeichnungsfläche mit der Java-Oberfläche.
     * 
     * @param conn -
     *                Element, dem die Zeichnungfläche hinzufgefügt werden soll.
     */
    public void addConnection(Virtual3DFrame conn) {
	conn.addCanvas(this.canvas);
    }

    /**
     * Bewegt alle 3D-Objekte in ihre Ausgangsposition zurück.
     */
    public void resetScene() {
	TransformGroup targetTG = this.su.getViewingPlatform()
		.getViewPlatformTransform();
	Transform3D t3d = new Transform3D();
	t3d.setTranslation(new Vector3d(0, 1, this.size + 15.0));
	targetTG.setTransform(t3d);
    }

    /**
     * Verbindet das LOP mit der 3D-Scene.
     * 
     * @param lop
     */
    public void setLOP(LOP lop) {
	lop.addProblemListener(new LOPAdapter() {

	    @Override
	    public void problemSolved(LOP lop) {
		Engine3D.this.computeSolution(lop);
	    }

	    @Override
	    public void showDualProblem(LOP lop) {
		if (lop.getSolution().getSpecialCase() == (LOPSolution.OPTIMAL_SOLUTION_AREA_POINT
			| LOPSolution.SOLUTION_AREA_LIMITED | LOPSolution.TARGET_FUNCTION_LIMITED)) {
		    Engine3D.this.intersection.setDualLineVisible(true);
		    resetScene();
		}

	    }

	    @Override
	    public void showPrimalProblem(LOP lop) {
		Engine3D.this.intersection.setDualLineVisible(false);
	    }
	});

	this.cone.setLOP(lop);

    }

    /**
     * Berechnet den die Größen und Positionen aller 3D-Objekte.
     * 
     * Funktion wird aufgerufen, wenn das LOP gelöst wurde.
     * 
     * @param lop -
     *                LOP, mit welchen gerechnet werden soll.
     */
    private void computeSolution(LOP lop) {
	Vector3Frac vec = lop.getTarget();
	// TODO: Spezialfälle behandeln
	// int sCase = lop.getSolution().getSpecialCase();

	this.size = (vec.getCoordX().toFloat() > vec.getCoordY().toFloat() ? vec
		.getCoordX().toFloat()
		: vec.getCoordY().toFloat()) + 3.0f;

	// Gibt es eine oder mehrere Lösungen?
	// if ((sCase & LOPSolution.OPTIMAL_SOLUTION_AREA_POINT) != 0
	// || (sCase & LOPSolution.OPTIMAL_SOLUTION_AREA_MULTIPLE) != 0)
	// this.size = (this.size > lop.getSolution().getValue() + 3.0f ?
	// this.size
	// : (float) lop.getSolution().getValue()) + 3.0f;

	this.hull.build(lop.getVectors(), false);

	// fuege Koordinatensystem hinzu
	this.coordsPlane.compute(this.size);

	// fuege Kegel hinzu
	this.cone.compute(this.hull.getVerticesList(), this.size);

	// fuege ZielVektor hinzu
	this.targetLine.compute(lop.getSolution().getVector().toVector3f(),
		this.size);

	// Schnittpunkt / Vektor für Duales Problem
	// werden müssen (gibt ja jetzt noch mehr Permutationen...)
	this.intersection.setDualLineVisible(false);

	// Gibt es eine oder mehrere Lösungen?
	// if ((sCase & LOPSolution.OPTIMAL_SOLUTION_AREA_POINT) != 0
	// || (sCase & LOPSolution.OPTIMAL_SOLUTION_AREA_MULTIPLE) != 0) {
	// LOPSolutionArea area = lop.getSolution().getAreas().get(0);
	// this.intersection.compute(lop.getSolution().getVector()
	// .toVector3f(), area.getL1().toVector3f(), area.getL2()
	// .toVector3f());
	// } else
	this.intersection.compute(lop.getSolution().getVector().toVector3f(),
		null, null);

	resetScene();
    }

    /**
     * Erstellt die Grafik-Konfiguration.
     * 
     * Diese wird zum rendern der Scene benötigt.
     * 
     * @return Die Grafik-Konfiguration.
     */
    private GraphicsConfiguration createConfig() {
	GraphicsConfigTemplate3D template = new GraphicsConfigTemplate3D();
	// template.setSceneAntialiasing(GraphicsConfigTemplate3D.PREFERRED);
	GraphicsEnvironment env = GraphicsEnvironment
		.getLocalGraphicsEnvironment();
	GraphicsDevice device = env.getDefaultScreenDevice();
	GraphicsConfiguration config = device.getBestConfiguration(template);
	return config;
    }

    /**
     * Erstellt die Beleuchtung, ohne die man nichts sehen würde.
     */
    private void createLight() {
	this.elemsGroup.addChild(GeomObjects.getLight());

	DirectionalLight headlight = new DirectionalLight();
	headlight.setColor(Tools3D.WHITE);
	headlight.setInfluencingBounds(Tools3D.LIGHT_BOUNDS);
	headlight.setDirection(new Vector3f(0.0f, 0.0f, -1.0f));
	this.hudGroup.addChild(headlight);
    }

    /**
     * Erstellt den Viewer und die Bewegungssteuerung.
     */
    private void createUserControls() {

	this.su = new SimpleUniverse(this.canvas);

	// depth-sort transparent objects on a per-geometry basis
	View view = this.su.getViewer().getView();
	view.setTransparencySortingPolicy(View.TRANSPARENCY_SORT_GEOMETRY);
	view.setBackClipDistance(Tools3D.BACK_CLIP_DISTANCE);
	view.setFrontClipDistance(Tools3D.FRONT_CLIP_DISTANCE);
	// view.setSceneAntialiasingEnable(true);

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
