/**
 * @version		$Id$
 * @copyright	(c)2007 Michael Kriese & Peer Sterner
 * 
 * This file is part of SoPraLOP Project.
 *
 *  SoPraLOP Project is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License.
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
 * 02.10.2007 - Version 0.4.1
 * - Neue Material-Konstante
 * - Methode createTransform hinzugefügt (aus CoordinatePlane3D)
 * 26.09.2007 - Version 0.4
 * - Neue Materialien hinzugefügt
 * - Einige ungenuzte Farben entfernt
 * 17.09.2007 - Version 0.3
 * - Material Gold hinzugefügt
 * - 3D-Objekte ohne Transparenz
 * 27.07.2007 - Version 0.2
 *  - neue Methode generateApperance()
 * 12.05.2007 - Version 0.1
 *  - Datei hinzugefuegt
 */
package info.kriese.soPra.engine3D;

import java.awt.Color;

import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.LineAttributes;
import javax.media.j3d.Material;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransparencyAttributes;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;
import com.sun.j3d.utils.geometry.Stripifier;

/**
 * Einige Hilfskonstanten und Funktionen für Berechnungen im Raum.
 * 
 * @author Michael Kriese
 * @version 0.4.1
 * @since 12.05.2007
 * 
 */
public final class Tools3D {

    public static final double BACK_CLIP_DISTANCE = 1000.0;

    public static final Color3f COLOR_BLACK = new Color3f(0.0f, 0.0f, 0.0f);

    public static final double FRONT_CLIP_DISTANCE = 0.01;

    public static final BoundingSphere LIGHT_BOUNDS = new BoundingSphere(
	    new Point3d(), BACK_CLIP_DISTANCE);

    public static final Material MATERIAL_BLUE = new Material(new Color3f(0.0f,
	    0.0f, 0.24f), COLOR_BLACK, new Color3f(0.0f, 0.0f, 0.75f),
	    new Color3f(0.62f, 0.55f, 0.36f), 51.2f);

    public static final Material MATERIAL_GOLD = new Material(new Color3f(
	    0.24f, 0.19f, 0.07f), COLOR_BLACK, new Color3f(0.75f, 0.6f, 0.22f),
	    new Color3f(0.62f, 0.55f, 0.36f), 51.2f);

    public static final Material MATERIAL_GREEN = new Material(new Color3f(
	    0.0f, 0.24f, 0.0f), COLOR_BLACK, new Color3f(0.0f, 0.75f, 0.0f),
	    new Color3f(0.62f, 0.55f, 0.36f), 51.2f);

    public static final Material MATERIAL_PINK = new Material(new Color3f(0f,
	    0f, 1f), new Color3f(0f, 0f, 0f), new Color3f(1f, 0f, 0f),
	    new Color3f(1f, 1f, 1f), 50f);

    public static final Material MATERIAL_RED = new Material(new Color3f(0.24f,
	    0.0f, 0.0f), COLOR_BLACK, new Color3f(0.75f, 0.0f, 0.0f),
	    new Color3f(0.62f, 0.55f, 0.36f), 51.2f);

    public static final Color3f WHITE = new Color3f(0.8f, 0.8f, 0.8f);

    public static final Color3f YELLOW = new Color3f(Color.YELLOW);

    private static NormalGenerator NORMAL = new NormalGenerator();

    private static Stripifier STRIP = new Stripifier();

    public static Transform3D createTransform(Vector3f pos, Vector3d rot,
	    Vector3d scale) {
	Transform3D ttrans, trot, tscale;

	ttrans = new Transform3D();
	trot = new Transform3D();
	tscale = new Transform3D();

	trot.setEuler(rot);
	tscale.setScale(scale);
	ttrans.setTranslation(pos);

	// erst Translation, dann Rotation und zuletzt die Skalierung
	trot.mul(tscale);
	ttrans.mul(trot);

	return ttrans;
    }

    public static Appearance generateApperance() {
	Appearance ap = new Appearance();

	ap.setLineAttributes(new LineAttributes(3.0f,
		LineAttributes.PATTERN_SOLID, true));
	ap.setTransparencyAttributes(new TransparencyAttributes(
		TransparencyAttributes.NONE, 0.0f));

	return ap;
    }

    public static Appearance generateApperance(Material material) {
	Appearance apr = generateApperance();
	apr.setMaterial(material);
	return apr;
    }

    public static void generateNormal(GeometryInfo gi) {
	NORMAL.generateNormals(gi);
    }

    public static void stripify(GeometryInfo gi) {
	STRIP.stripify(gi);
    }

    private Tools3D() {
    }
}
