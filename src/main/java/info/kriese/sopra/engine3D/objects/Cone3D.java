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
 * 21.05.2008 - Version 0.6
 * - Neue Methode switchBackgroundColor, diese verändert die Seiten des Kegels
 *   um den Kontrast bei weißem Hintergrund zu erhöhen
 * 08.11.2007 - Version 0.5
 * - Kegelkanten haben jetzt ihre Beschriftung bekommen
 * 23.10.2007 - Version 0.4
 * - An neuen Quickhull-Algorithmus angepasst
 * 14.09.2007 - Version 0.3
 * - Instanz kann direkt zum SceneGraph hizugefügt werden
 * - Änderung bei Skalierung
 * 30.07.2007 - Version 0.2.1
 * -Einige CapabilityBits gesetzt
 * 27.07.2007 - Version 0.2
 *  - Strukturaenderungen, koplett ueberarbeitet
 *  - Antialising der Linien
 * 12.05.2007 - Version 0.1
 * - Datei hinzugefuegt
 */
package info.kriese.sopra.engine3D.objects;

import info.kriese.sopra.engine3D.Tools3D;
import info.kriese.sopra.lop.LOP;
import info.kriese.sopra.math.Math2;
import info.kriese.sopra.math.Vector3Frac;
import info.kriese.sopra.math.Vertex;

import java.util.ArrayList;
import java.util.List;

import org.jogamp.java3d.Appearance;
import org.jogamp.java3d.BranchGroup;
import org.jogamp.java3d.LineArray;
import org.jogamp.java3d.Material;
import org.jogamp.java3d.Shape3D;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.java3d.TransparencyAttributes;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Point3f;
import org.jogamp.vecmath.Vector3f;

import org.jogamp.java3d.utils.geometry.GeometryInfo;

/**
 * Erstellt aus den Vektoren des LOP's einen konvexen Kegel im Raum.
 * 
 * @author Michael Kriese
 * @version 0.6
 * @since 12.05.2007
 * 
 */
public class Cone3D extends TransformGroup {

    private final Appearance aprBack = Tools3D.generateApperance();
    private final Appearance aprFront = Tools3D.generateApperance();

    /**
     * Außenseite, Innenseite und Kanten des Kegels.
     */
    private Shape3D front, back, lines;

    /**
     * Geordnete Indizes der Seitenflächen des Kegels.
     */
    private int[] indices;

    /**
     * LOP, welches der Kegel darstellt.
     */
    private LOP lop;

    private final Material matFront1, matFront2, matBack1, matBack2;

    /**
     * Gruppe der Kantenbeschriftung.
     */
    private final TransformGroup names;

    /**
     * Kegelflächen
     */
    private Point3f[] vertices;

    /**
     * Kegelflächen.
     */
    private final List<Vector3Frac> verticesOrig;

    /**
     * Konstruktor, welcher alle Objekte erstellt und initialisiert.
     */
    public Cone3D() {

	this.names = new TransformGroup();
	this.names.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
	this.names.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);

	this.verticesOrig = new ArrayList<Vector3Frac>();

	addChild(this.names);

	this.matFront1 = new Material(new Color3f(0.8f, 0.8f, 0.8f),
		new Color3f(0.0f, 0.0f, 0.0f), new Color3f(1f, 1f, 1f),
		new Color3f(1f, 1f, 0f), 10f);
	this.matFront2 = new Material(new Color3f(0.5f, 0.5f, 0.5f),
		new Color3f(03f, 0.3f, 0.3f), new Color3f(0.0f, 0.0f, 0.0f),
		new Color3f(0.7f, 0.7f, 0f), 10f);

	this.matBack1 = new Material(new Color3f(0.4f, 0.7f, 0.0f),
		new Color3f(0.0f, 0.0f, 0.0f), new Color3f(1f, 1f, 1f),
		new Color3f(1f, 1f, 0f), 10f);

	this.matBack2 = new Material(new Color3f(0.4f, 0.7f, 0.0f),
		new Color3f(0.7f, 0.7f, 0.0f), new Color3f(0.3f, 0.3f, 0.3f),
		new Color3f(0.7f, 0.7f, 0f), 10f);

	initFront();
	initBack();
	initLines();
    }

    /**
     * Berechnet das 3D-Koordinatensystem.
     * 
     * @param vertices -
     *                Seitenflächen
     * @param size -
     *                Skalierungsfaktor
     */
    public void compute(List<Vertex> vertices, float size) {
	this.verticesOrig.clear();

	List<Integer> tidx = new ArrayList<Integer>();
	List<Point3f> tvtx = new ArrayList<Point3f>();
	Point3f p2, p3;
	int idx;

	tvtx.add(new Point3f());
	this.verticesOrig.add(Vector3Frac.ZERO);

	for (Vertex v : vertices) {
	    if (!v.p1.equals(Vector3Frac.ZERO))
		continue;

	    p2 = Math2.normalizeScale(v.p2.toPoint3f(), size);
	    p3 = Math2.normalizeScale(v.p3.toPoint3f(), size);
	    tidx.add(0);

	    idx = tvtx.indexOf(p2);
	    if (idx == -1) {
		tvtx.add(p2);
		this.verticesOrig.add(v.p2);
		idx = tvtx.indexOf(p2);
	    }
	    tidx.add(idx);

	    idx = tvtx.indexOf(p3);
	    if (idx == -1) {
		tvtx.add(p3);
		this.verticesOrig.add(v.p3);
		idx = tvtx.indexOf(p3);
	    }
	    tidx.add(idx);
	}

	this.indices = new int[tidx.size()];
	for (int i = 0; i < this.indices.length; i++)
	    this.indices[i] = tidx.get(i);

	this.vertices = new Point3f[tvtx.size()];
	for (int i = 0; i < this.vertices.length; i++)
	    this.vertices[i] = tvtx.get(i);

	computeFront();
	computeBack();
	computeLines();
	computeNames();
    }

    /**
     * Verknüpfung zum LOP.
     * 
     * Wird zur Darstellung der Kantennamen gebraucht.
     * 
     * @param lop -
     *                LOP, welches benutzt werden soll
     */
    public void setLOP(LOP lop) {
	this.lop = lop;
    }

    public void switchBackgroundColor(boolean bgBlack) {
	if (bgBlack) {
	    this.aprFront.setMaterial(this.matFront1);
	    this.aprBack.setMaterial(this.matBack1);
	} else {
	    this.aprFront.setMaterial(this.matFront2);
	    this.aprBack.setMaterial(this.matBack2);
	}

	this.back.setAppearance(this.aprBack);
	this.front.setAppearance(this.aprFront);
    }

    /**
     * Berechnet die Innenseiten des Kegels.
     */
    private void computeBack() {
	GeometryInfo gInfo = new GeometryInfo(GeometryInfo.TRIANGLE_ARRAY);

	gInfo.setCoordinates(this.vertices);
	gInfo.setCoordinateIndices(this.indices);
	gInfo.compact();

	Tools3D.generateNormal(gInfo);
	Tools3D.stripify(gInfo);

	this.back.setGeometry(gInfo.getGeometryArray());

    }

    /**
     * Berechnet die Vorderseiten des Kegels.
     */
    private void computeFront() {
	GeometryInfo gInfo = new GeometryInfo(GeometryInfo.TRIANGLE_ARRAY);

	int[] nidx = new int[this.indices.length];

	for (int i = 0; i < nidx.length; i += 3) {
	    nidx[i] = this.indices[i + 2];
	    nidx[i + 1] = this.indices[i + 1];
	    nidx[i + 2] = this.indices[i];
	}

	gInfo.setCoordinates(this.vertices);
	gInfo.setCoordinateIndices(nidx);
	gInfo.compact();

	Tools3D.generateNormal(gInfo);
	Tools3D.stripify(gInfo);

	this.front.setGeometry(gInfo.getGeometryArray());
    }

    /**
     * Berechnet die Kanten des Kegels.
     */
    private void computeLines() {
	int lines = this.vertices.length - 1;
	LineArray lineArray = new LineArray(lines * 2, LineArray.COORDINATES
		| LineArray.COLOR_3);

	Point3f[] coords = new Point3f[lines * 2];
	int pos = 0;

	for (Point3f pt : this.vertices) {
	    if (pt.x == 0 && pt.y == 0 && pt.z == 0)
		continue;
	    coords[pos * 2] = new Point3f();
	    coords[pos * 2 + 1] = pt;
	    lineArray.setColor(pos * 2, Tools3D.YELLOW);
	    lineArray.setColor(pos * 2 + 1, Tools3D.YELLOW);
	    pos++;
	}

	lineArray.setCoordinates(0, coords);
	this.lines.setGeometry(lineArray);
    }

    /**
     * Berechnet die Kantenbeschriftungen.
     */
    private void computeNames() {
	this.names.removeAllChildren();
	BranchGroup bg = new BranchGroup();
	bg.setCapability(BranchGroup.ALLOW_DETACH);

	for (int i = 0; i < this.vertices.length; i++) {
	    Vector3Frac vec = this.verticesOrig.get(i);
	    if (!vec.equals(Vector3Frac.ZERO)) {
		TransformGroup name = Tools3D.createAxisName("L"
			+ (this.lop.getVectors().indexOf(vec) + 1),
			Tools3D.MATERIAL_GOLD);
		Transform3D tr = new Transform3D();
		Vector3f v = new Vector3f(this.vertices[i]);
		v.scale(1.05f);
		tr.setTranslation(v);
		tr.setScale(0.3);
		name.setTransform(tr);
		bg.addChild(name);
	    }
	}

	bg.compile();
	this.names.addChild(bg);
    }

    /**
     * Initialisiert die Innenseiten des Kegels.
     */
    private void initBack() {

	this.aprBack.setMaterial(this.matBack1);
	this.aprBack.getMaterial()
		.setCapability(Material.ALLOW_COMPONENT_WRITE);
	this.aprBack.getTransparencyAttributes().setTransparencyMode(
		TransparencyAttributes.NICEST);
	this.aprBack.getTransparencyAttributes().setTransparency(0.2f);
	this.aprBack.getTransparencyAttributes().setCapability(
		TransparencyAttributes.ALLOW_VALUE_WRITE);

	this.aprBack
		.setCapability(Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_WRITE);
	this.aprBack.setCapability(Appearance.ALLOW_MATERIAL_WRITE);
	this.aprBack.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);

	this.back = new Shape3D();
	this.back.setAppearance(this.aprBack);
	this.back.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
	this.back.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
	addChild(this.back);
    }

    /**
     * Initialisiert die Außenseiten des Kegels.
     */
    private void initFront() {
	this.aprFront.setMaterial(this.matFront1);

	this.aprFront.getTransparencyAttributes().setTransparencyMode(
		TransparencyAttributes.NICEST);
	this.aprFront.getTransparencyAttributes().setTransparency(0.4f);
	this.aprFront.getTransparencyAttributes().setCapability(
		TransparencyAttributes.ALLOW_VALUE_WRITE);
	this.aprFront.getTransparencyAttributes().setCapability(
		TransparencyAttributes.ALLOW_MODE_WRITE);

	this.aprFront
		.setCapability(Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_WRITE);
	this.aprFront.setCapability(Appearance.ALLOW_MATERIAL_WRITE);
	this.aprFront.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);

	this.front = new Shape3D();
	this.front.setAppearance(this.aprFront);
	this.front.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
	this.front.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
	addChild(this.front);
    }

    /**
     * Initialisiert die Kanten des Kegels.
     */
    private void initLines() {
	Appearance apr = Tools3D.generateApperance();

	this.lines = new Shape3D();
	this.lines.setAppearance(apr);
	this.lines.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
	addChild(this.lines);
    }
}
