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
package info.kriese.soPra.engine3D.objects;

import info.kriese.soPra.engine3D.Tools3D;
import info.kriese.soPra.math.Math2;

import javax.media.j3d.Appearance;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.geometry.Sphere;

/**
 * Klasse, welche den Schnittpunkt und die Hilfslinien repräsentiert.
 * 
 * @author Michael Kriese
 * @version 0.4
 * @since 10.09.2007
 * 
 */
public class Point3D extends TransformGroup {

    /**
     * Apperance-Objekt, welches die visuellen Eigenschaften kapselt.
     */
    private final Appearance ap;

    /**
     * Gruppen für Hilfslinien und Kugel.
     */
    private final TransformGroup grpX, grpY, grpZ, grpPoint;

    /**
     * Zylinder, welche die Hilfslinien darstellen.
     */
    private final Cylinder lineX, lineY, lineZ;

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
	this.grpZ = new TransformGroup();

	this.ap = Tools3D.generateApperance(Tools3D.MATERIAL_PINK);
	this.ap.setTransparencyAttributes(new TransparencyAttributes(
		TransparencyAttributes.NONE, 0.0f));

	int flags = Primitive.GENERATE_NORMALS;

	this.point = new Sphere(0.1f, flags, 40, this.ap);

	this.lineX = new Cylinder(0.005f, 1.0f, flags, this.ap);
	this.lineY = new Cylinder(0.005f, 1.0f, flags, this.ap);
	this.lineZ = new Cylinder(0.005f, 1.0f, flags, this.ap);

	this.grpPoint.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	this.grpPoint.addChild(this.point);

	this.grpX.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	this.grpX.addChild(this.lineX);

	this.grpY.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	this.grpY.addChild(this.lineY);

	this.grpZ.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	this.grpZ.addChild(this.lineZ);

	addChild(this.grpPoint);
	addChild(this.grpX);
	addChild(this.grpY);
	addChild(this.grpZ);
    }

    /**
     * Berechnet die Position des Schnittpunkts und der Hilfslinien.
     * 
     * @param pos -
     *                Position, an der der Schnittpunkt liegt.
     */
    public void compute(Vector3f pos) {

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

	float len = new Vector3f(pos.x, pos.y, 0.0f).length();
	if (pos.x != 0 && pos.y != 0)
	    rot.z = -Math2.angle(new Vector3d(0.0, 1.0, 0.0), new Vector3d(
		    pos.x, pos.y, 0.0));
	scale = new Vector3d(1.0, len, 1.0);
	trans = new Vector3f(pos.x / 2.0f, pos.y / 2.0f, pos.z);
	this.grpZ.setTransform(Tools3D.createTransform(trans, rot, scale));
    }
}
