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

import javax.media.j3d.Appearance;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Cylinder;

/**
 * Erstellt aus dem Zielvektor eine Gerade im Raum.
 * 
 * @author Michael Kriese
 * @version 0.3.1
 * @since 12.05.2007
 * 
 */
public final class Target3D extends TransformGroup {

    /**
     * Zylinder, welcher die Zielfunktion darstellt.
     */
    private final Cylinder line;

    /**
     * Konstruktor, welcher alle Objekte erstellt und initialisiert.
     */
    public Target3D() {

	setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

	Appearance apr = Tools3D.generateApperance();
	apr.setMaterial(Tools3D.MATERIAL_GOLD);
	this.line = new Cylinder(0.05f, 1.0f, Cylinder.GENERATE_NORMALS, apr);

	addChild(this.line);
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

	rot.rotX(Math.PI / 2.0);
	scale.setScale(new Vector3d(1.0, size + 1.0, 1.0));
	pos.z += size / 2.0f;
	trans.setTranslation(pos);

	// erst Translation, dann Rotation und zuletzt die Skalierung
	rot.mul(scale);
	trans.mul(rot);

	setTransform(trans);
    }
}
