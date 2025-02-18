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
 * 30.05.2008 - Version 0.5.2
 * - BugFix: Falls Punkt unterhalb von x-y-Ebene, muss die Rotation um
 *   z-Achse invertiert werden
 * 04.03.2008 - Version 0.5.1
 * - Falls Normalenvektor der Ebene nach unten zeigt muss die Rotation
 *    um die x-Achse negiert werden.
 * 17.01.2008 - Version 0.5
 * - Polygonzahl für Primitive erhöht
 * - Vektor für duales Problem hinzugefügt
 * 10.01.2008 - Version 0.4.1
 * - Hilfslinie für U3-Achse entfernt
 * 09.11.2007 - Version 0.4
 * - Lösung ist jetzt immer sichtbar
 * 12.10.2007 - Version 0.3.1
 * - BugFix: Fehlerhafte Rotation führte zur Exception, wenn Zielfunktion auf der Z-Achse liegt
 * 02.10.2007 - Version 0.3
 * - Hilfslinien zum Ablesen der Koordinaten hizugefügt
 * 14.09.2007 - Version 0.2
 * - Instanz kann direkt zum SceneGraph hizugefügt werden
 * - Implementierung fortgeführt
 * 10.09.2007 - Version 0.1
 *  - Datei hinzugefuegt
 */
package info.kriese.sopra.engine3D.objects;

import info.kriese.sopra.engine3D.Tools3D;
import info.kriese.sopra.math.Math2;

import org.jogamp.java3d.Appearance;
import org.jogamp.java3d.RenderingAttributes;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.vecmath.Vector3d;
import org.jogamp.vecmath.Vector3f;

import org.jogamp.java3d.utils.geometry.Cylinder;
import org.jogamp.java3d.utils.geometry.Primitive;
import org.jogamp.java3d.utils.geometry.Sphere;

/**
 * Klasse, welche den Schnittpunkt und die Hilfslinien repräsentiert.
 * 
 * @author Michael Kriese
 * @version 0.5.2
 * @since 10.09.2007
 * 
 */
public class Point3D extends TransformGroup {

    /**
     * Gruppen für Hilfslinien und Kugel.
     */
    private final TransformGroup grpX, grpY, grpPoint, grpDualLine;

    /**
     * Zylinder, welche die Hilfslinien darstellen.
     */
    private final Cylinder lineX, lineY, dualline;

    /**
     * Kugel, welche den Schnittpunkt darstellt.
     */
    private final Sphere point;

    /**
     * Konstruktor, welcher alle Objekte erstellt und initialisiert.
     */
    public Point3D() {

	this.grpPoint = new TransformGroup();
	this.grpX = new TransformGroup();
	this.grpY = new TransformGroup();

	Appearance ap = Tools3D.generateApperance(Tools3D.MATERIAL_PINK);

	this.point = new Sphere(0.1f, Primitive.GENERATE_NORMALS, 100, ap);

	this.lineX = Tools3D.generateCylinder(0.005f, 1.0f, ap);
	this.lineY = Tools3D.generateCylinder(0.005f, 1.0f, ap);

	ap = Tools3D.generateApperance(Tools3D.MATERIAL_PINK);
	ap.setCapability(Appearance.ALLOW_RENDERING_ATTRIBUTES_WRITE);
	this.dualline = Tools3D.generateCylinder(0.05f, 1.0f, ap);

	this.grpPoint.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	this.grpPoint.addChild(this.point);

	this.grpX.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	this.grpX.addChild(this.lineX);

	this.grpY.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	this.grpY.addChild(this.lineY);

	this.grpDualLine = new TransformGroup();
	this.grpDualLine.addChild(this.dualline);
	this.grpDualLine.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	this.grpPoint.addChild(this.grpDualLine);

	addChild(this.grpPoint);
	addChild(this.grpX);
	addChild(this.grpY);
    }

    /**
     * Berechnet die Position des Schnittpunkts und der Hilfslinien.
     * 
     * @param pos -
     *                Position, an der der Schnittpunkt liegt.
     * @param l1 -
     *                Erster Spannvektor der Lösungebene
     * @param l2 -
     *                Zweiter Spannvektor der Lösungebene
     */
    public void compute(Vector3f pos, Vector3f l1, Vector3f l2) {

	// Settings sets = SettingsFactory.getInstance();

	Vector3d rot = new Vector3d(), scale = new Vector3d(1.0, 1.0, 1.0);
	Vector3f trans;

	this.grpPoint.setTransform(Tools3D.createTransform(pos, rot, scale));

	scale = new Vector3d(1.0, pos.y, 1.0);
	trans = new Vector3f();
	trans.x = pos.x;
	trans.y = pos.y / 2.0f;
	this.grpX.setTransform(Tools3D.createTransform(trans, rot, scale));

	rot.z = Math.PI / 2.0;
	scale = new Vector3d(1.0, pos.x, 1.0);
	trans = new Vector3f();
	trans.x = pos.x / 2.0f;
	trans.y = pos.y;
	this.grpY.setTransform(Tools3D.createTransform(trans, rot, scale));

	scale = new Vector3d(1.0, 3.0, 1.0);
	trans = new Vector3f();

	if (l1 != null && l2 != null) {
	    Vector3d v1, v2;
	    Vector3f targ = new Vector3f();
	    targ.cross(l1, l2);
	    v1 = new Vector3d(targ.x, targ.y, 0.0);
	    v2 = new Vector3d(targ);
	    rot.x = Math2.angle(v1, v2);

	    v1 = new Vector3d(0.0, 1.0, 0.0);
	    v2 = new Vector3d(targ.x, targ.y, 0.0);
	    rot.z = -Math2.angle(v1, v2);

	    // Falls Normalenvektor der Ebene nach unten zeigt muss die Rotation
	    // um die x-Achse negiert werden.
	    if (targ.z < 0)
		rot.x *= -1.0;

	    // falls Punkt unterhalb von x-y-Ebene, muss die Roptation um
	    // z-Achse invertiert werden
	    if (pos.z < 0.0)
		rot.z *= -1.0;
	}

	this.grpDualLine.setTransform(Tools3D
		.createTransform(trans, rot, scale));
    }

    /**
     * Macht den Normalenvektor der Lösungfläche sichbar oder unsichtbar.
     * 
     * @param visible -
     *                "True" für sichtbar und "False" für unsichbar
     */
    public void setDualLineVisible(boolean visible) {
	Appearance apr = this.dualline.getAppearance();
	RenderingAttributes ra = new RenderingAttributes();

	if (visible)
	    ra.setVisible(true);
	else
	    ra.setVisible(false);

	apr.setRenderingAttributes(ra);
    }
}
