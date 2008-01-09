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
 * 08.11.2007 - Version 0.4.2
 * - Methode createAxisName nach Tools3D verschoben
 * 02.10.2007 - Version 0.4.1
 * - Methode createTransform in Tools3D verschoben
 * - Einheiten an Achsen hinzugefügt
 * 26.09.2007 - Version 0.4
 * - Linien gegen Kegel und Zylinder getauscht
 * 14.09.2007 - Version 0.3.2
 * - Instanz kann direkt zum SceneGraph hizugefügt werden
 * - Änderung bei Skalierung
 * 30.07.2007 - Version 0.3.1
 * -Einige CapabilityBits gesetzt
 * 27.07.2007 - Version 0.3
 * - Strukturaenderungen, koplett ueberarbeitet
 * - Antialising der Linien
 * 12.05.2007 - Version 0.2
 * - Umwandlung in eigenstaendiges Objekt
 * 11.04.2007 - Version 0.1
 * - Datei hinzugefuegt
 */

package info.kriese.soPra.engine3D.objects;

import info.kriese.soPra.engine3D.Tools3D;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Link;
import javax.media.j3d.Material;
import javax.media.j3d.Node;
import javax.media.j3d.SharedGroup;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Cone;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Sphere;

/**
 * Erstellt ein Koordinatensystem im Raum.
 * 
 * @author Michael Kriese
 * @since 11.04.2007
 * @version 0.4.2
 */
public final class CoordinatePlane3D extends TransformGroup {

    /**
     * Erstellt einen Pfeil für die Koordinatenachsen.
     * 
     * @param material -
     *                Material des Pfeil
     * @return 3D-Objekt
     */
    private static TransformGroup createCone(Material material) {
	Appearance apr = Tools3D.generateApperance(material);
	Cone cone = new Cone(0.05f, 0.5f, Cone.GENERATE_NORMALS, apr);

	TransformGroup tg = new TransformGroup();
	tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	tg.addChild(cone);
	return tg;
    }

    /**
     * Erstellt eine Koordinatenachse.
     * 
     * @param material -
     *                Material des Pfeil
     * @return 3D-Objekt
     */
    private static TransformGroup createLine(Material material) {
	Appearance apr = Tools3D.generateApperance(material);
	Cylinder line = new Cylinder(0.025f, 1.0f, Cylinder.GENERATE_NORMALS,
		apr);

	TransformGroup tg = new TransformGroup();
	tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	tg.addChild(line);
	return tg;
    }

    /**
     * Erstellt eine Markierung für die Koordinatenachseneinheiten.
     * 
     * @param material -
     *                Material des Pfeil
     * @return 3D-Objekt
     */
    private static Node createMark(Vector3d scale, Material material) {
	TransformGroup t = new TransformGroup();
	Sphere sp = new Sphere(0.05f, Sphere.GENERATE_NORMALS, Tools3D
		.generateApperance(material));

	Transform3D tr = new Transform3D();
	tr.setScale(scale);

	t.setTransform(tr);
	t.addChild(sp);
	return t;
    }

    /**
     * Gruppen für die Achsenmarkierungen.
     */
    private final SharedGroup markX, markY, markZ;

    /**
     * Gruppen für die Beschriftungen, Achsen, Pfeile und Markierungen.
     */
    private final TransformGroup xName, yName, zName, xLine, yLine, zLine,
	    xCone, yCone, zCone, units;

    /**
     * Konstruktor, welcher alle Objekte erstellt und initialisiert.
     */
    public CoordinatePlane3D() {

	// Begin X-Achse
	this.xLine = createLine(Tools3D.MATERIAL_RED);
	addChild(this.xLine);

	this.xCone = createCone(Tools3D.MATERIAL_RED);
	addChild(this.xCone);

	this.xName = Tools3D.createAxisName("x", Tools3D.MATERIAL_RED);
	addChild(this.xName);

	this.markX = new SharedGroup();
	this.markX.addChild(createMark(new Vector3d(0.1, 1.0, 1.0),
		Tools3D.MATERIAL_RED));
	this.markX.compile();

	// Begin Y-Achse
	this.yLine = createLine(Tools3D.MATERIAL_GREEN);
	addChild(this.yLine);

	this.yCone = createCone(Tools3D.MATERIAL_GREEN);
	addChild(this.yCone);

	this.yName = Tools3D.createAxisName("y", Tools3D.MATERIAL_GREEN);
	addChild(this.yName);

	this.markY = new SharedGroup();
	this.markY.addChild(createMark(new Vector3d(1.0, 0.1, 1.0),
		Tools3D.MATERIAL_GREEN));
	this.markY.compile();

	// Begin Z-Achse
	this.zLine = createLine(Tools3D.MATERIAL_BLUE);
	addChild(this.zLine);

	this.zCone = createCone(Tools3D.MATERIAL_BLUE);
	addChild(this.zCone);

	this.zName = Tools3D.createAxisName("z", Tools3D.MATERIAL_BLUE);
	addChild(this.zName);

	this.markZ = new SharedGroup();
	this.markZ.addChild(createMark(new Vector3d(1.0, 1.0, 0.1),
		Tools3D.MATERIAL_BLUE));
	this.markZ.compile();

	this.units = new TransformGroup();
	this.units.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
	this.units.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
	addChild(this.units);

    }

    /**
     * Berechnen der Größe des 3D-Koordinatensystems
     * 
     * @param size -
     *                Skalierungsfaktor
     */
    public void compute(float size) {

	this.units.removeAllChildren();
	BranchGroup bg = new BranchGroup();
	bg.setCapability(BranchGroup.ALLOW_DETACH);

	Transform3D trans;
	Vector3f pos;
	Vector3d scale, rot;

	// Begin X-Achse
	pos = new Vector3f(size / 2.0f, 0.0f, 0.0f);
	rot = new Vector3d(0.0, 0.0, Math.PI / 2.0);
	scale = new Vector3d(1.0, size, 1.0);

	this.xLine.setTransform(Tools3D.createTransform(pos, rot, scale));

	pos = new Vector3f(size, 0.0f, 0.0f);
	rot = new Vector3d(0.0, 0.0, -Math.PI / 2.0);
	scale = new Vector3d(1.0, 1.0, 1.0);

	this.xCone.setTransform(Tools3D.createTransform(pos, rot, scale));

	trans = new Transform3D();
	trans.setTranslation(new Vector3f(size + 0.5f, 0f, 0f));
	trans.setScale(0.5);
	this.xName.setTransform(trans);

	for (int x = 1; x < size; x++) {
	    TransformGroup tg = new TransformGroup();
	    trans = new Transform3D();
	    trans.setTranslation(new Vector3f(x, 0.0f, 0.0f));
	    tg.setTransform(trans);
	    tg.addChild(new Link(this.markX));
	    bg.addChild(tg);

	    tg = Tools3D.createAxisName("" + x, Tools3D.MATERIAL_RED);
	    trans = new Transform3D();
	    rot = new Vector3d();
	    scale = new Vector3d(0.2, 0.2, 0.2);
	    tg.setTransform(Tools3D.createTransform(new Vector3f(x, -0.25f,
		    -0.25f), rot, scale));
	    bg.addChild(tg);
	}
	// End X-Achse

	// Begin Y-Achse
	pos = new Vector3f(0.0f, size / 2.0f, 0.0f);
	rot = new Vector3d();
	scale = new Vector3d(1.0, size, 1.0);

	this.yLine.setTransform(Tools3D.createTransform(pos, rot, scale));

	pos = new Vector3f(0.0f, size, 0.0f);
	rot = new Vector3d();
	scale = new Vector3d(1.0, 1.0, 1.0);

	this.yCone.setTransform(Tools3D.createTransform(pos, rot, scale));

	trans = new Transform3D();
	trans.setTranslation(new Vector3f(0f, size + 0.5f, 0f));
	trans.setScale(0.5);
	this.yName.setTransform(trans);

	for (int x = 1; x < size; x++) {
	    TransformGroup tg = new TransformGroup();
	    trans = new Transform3D();
	    trans.setTranslation(new Vector3f(0.0f, x, 0.0f));
	    tg.setTransform(trans);
	    tg.addChild(new Link(this.markY));
	    bg.addChild(tg);

	    tg = Tools3D.createAxisName("" + x, Tools3D.MATERIAL_GREEN);
	    trans = new Transform3D();
	    rot = new Vector3d();
	    scale = new Vector3d(0.2, 0.2, 0.2);
	    tg.setTransform(Tools3D.createTransform(new Vector3f(-0.25f, x,
		    -0.25f), rot, scale));
	    bg.addChild(tg);
	}
	// End Y-Achse

	// Begin Z-Achse
	pos = new Vector3f(0.0f, 0.0f, size / 2.0f);
	rot = new Vector3d(Math.PI / 2.0, 0.0, 0.0);
	scale = new Vector3d(1.0, size, 1.0);

	this.zLine.setTransform(Tools3D.createTransform(pos, rot, scale));

	pos = new Vector3f(0.0f, 0.0f, size);
	rot = new Vector3d(Math.PI / 2.0, 0.0, 0.0);
	scale = new Vector3d(1.0, 1.0, 1.0);

	this.zCone.setTransform(Tools3D.createTransform(pos, rot, scale));

	trans = new Transform3D();
	trans.setTranslation(new Vector3f(0f, 0f, size + 0.5f));
	trans.setScale(0.5);
	this.zName.setTransform(trans);

	for (int x = 1; x < size; x++) {
	    TransformGroup tg = new TransformGroup();
	    trans = new Transform3D();
	    trans.setTranslation(new Vector3f(0.0f, 0.0f, x));
	    tg.setTransform(trans);
	    tg.addChild(new Link(this.markZ));
	    bg.addChild(tg);

	    tg = Tools3D.createAxisName("" + x, Tools3D.MATERIAL_BLUE);
	    trans = new Transform3D();
	    rot = new Vector3d();
	    scale = new Vector3d(0.2, 0.2, 0.2);
	    tg.setTransform(Tools3D.createTransform(new Vector3f(-0.25f,
		    -0.25f, x), rot, scale));
	    bg.addChild(tg);
	}
	// End Z-Achse

	bg.compile();
	this.units.addChild(bg);
    }
}