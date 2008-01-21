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
 * 17.01.2008 - Version 0.3.3
 * - Beschriftung der Geraden im negativen leicht verschoben
 * - Polygonzahl für Primitive erhöht
 * 15.01.2008 - Version 0.3.2
 * - Gerade hat Beschriftung bekommen
 * - Gerade skaliert nach unten, wenn der Schnittpunkt unten liegt
 * 10.01.2007 - Version 0.3.1
 * - Gerade nach unten verlängert
 * 17.09.2007 - Version 0.3
 * - Umwandlung Line zu Zylinder
 * 14.09.2007
 * - Instanz kann direkt zum SceneGraph hizugefügt werden
 * - Änderung bei Skalierung
 * 30.07.2007 - Version 0.2.1
 * - Einige CapabilityBits gesetzt
 * 27.07.2007 - Version 0.2
 * - Strukturaenderungen, koplett ueberarbeitet
 * - Antialising der Linien
 * 12.05.2007 - Version 0.1
 * - Datei hinzugefuegt
 */
package info.kriese.soPra.engine3D.objects;

import info.kriese.soPra.engine3D.Tools3D;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Cylinder;

/**
 * Erstellt aus dem Zielvektor eine Gerade im Raum.
 * 
 * @author Michael Kriese
 * @version 0.3.3
 * @since 12.05.2007
 * 
 */
public final class Target3D extends TransformGroup {

    private final TransformGroup desc, gline;

    /**
     * Zylinder, welcher die Zielfunktion darstellt.
     */
    private final Cylinder line;

    /**
     * Konstruktor, welcher alle Objekte erstellt und initialisiert.
     */
    public Target3D() {
	this.line = Tools3D.generateCylinder(0.05f, 1.0f, Tools3D
		.generateApperance(Tools3D.MATERIAL_GOLD));

	this.gline = new TransformGroup();
	this.gline.addChild(this.line);
	this.gline.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

	this.desc = Tools3D.createAxisName("g", Tools3D.MATERIAL_GOLD);
	this.desc.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

	addChild(this.gline);
	addChild(this.desc);
    }

    /**
     * Berechnet die Gerade der Zielfunktion.
     * 
     * @param pos -
     *                Position der Zielfunktion
     * @param size -
     *                Größe der Zielfunktion
     */
    public void compute(Vector3f pos, float size) {

	Transform3D rot = new Transform3D();
	Transform3D scale = new Transform3D();
	Transform3D trans = new Transform3D();
	Vector3f vec = new Vector3f(pos);

	float z = Math.abs(pos.z) > size ? size : pos.z;

	rot.rotX(Math.PI / 2.0);
	scale.setScale(new Vector3d(1.0, Math.abs(z) + 2.0, 1.0));

	vec.z = z / 1.75f;
	trans.setTranslation(vec);

	// erst Translation, dann Rotation und zuletzt die Skalierung
	rot.mul(scale);
	trans.mul(rot);

	this.gline.setTransform(trans);

	trans = new Transform3D();
	scale = new Transform3D();
	vec = new Vector3f(pos);

	if (z < 0)
	    vec.z = z - 1.6f;
	else
	    vec.z = z + 1.5f;
	trans.setTranslation(vec);
	scale.setScale(0.5);

	trans.mul(scale);

	this.desc.setTransform(trans);
    }
}
