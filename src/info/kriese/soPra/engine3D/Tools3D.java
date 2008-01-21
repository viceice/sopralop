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
 * 08.11.2007 - Verison 0.4.2
 * - Methode createAxisName hinzugefügt
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
import java.awt.Font;

import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Font3D;
import javax.media.j3d.FontExtrusion;
import javax.media.j3d.LineAttributes;
import javax.media.j3d.Material;
import javax.media.j3d.OrientedShape3D;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.Text3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;
import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.geometry.Stripifier;

/**
 * Einige Hilfskonstanten und Funktionen für Berechnungen im Raum.
 * 
 * @author Michael Kriese
 * @version 0.4.2
 * @since 12.05.2007
 * 
 */
public final class Tools3D {

    /**
     * Back-Clipping-Plane, ab der Entfernung wird nicht mehr gezeichnet.
     */
    public static final double BACK_CLIP_DISTANCE = 1000.0;

    /**
     * Farbe Schwarz.
     */
    public static final Color3f COLOR_BLACK = new Color3f(0.0f, 0.0f, 0.0f);

    /**
     * Front-Clipping-Plane, ab der Entfernung wird nicht mehr gezeichnet.
     */
    public static final double FRONT_CLIP_DISTANCE = 0.01;

    /**
     * Kugel, in der Lichtreflexionen berechnet werden.
     */
    public static final BoundingSphere LIGHT_BOUNDS = new BoundingSphere(
	    new Point3d(), BACK_CLIP_DISTANCE);

    /**
     * Blaues Material für das Koordinatensystem.
     */
    public static final Material MATERIAL_BLUE = new Material(new Color3f(0.0f,
	    0.0f, 0.24f), COLOR_BLACK, new Color3f(0.0f, 0.0f, 0.75f),
	    new Color3f(0.62f, 0.55f, 0.36f), 51.2f);

    /**
     * Goldenes Material für den Kegel.
     */
    public static final Material MATERIAL_GOLD = new Material(new Color3f(
	    0.24f, 0.19f, 0.07f), COLOR_BLACK, new Color3f(0.75f, 0.6f, 0.22f),
	    new Color3f(0.62f, 0.55f, 0.36f), 51.2f);

    /**
     * Grünes Material für das Koordinatensystem.
     */
    public static final Material MATERIAL_GREEN = new Material(new Color3f(
	    0.0f, 0.24f, 0.0f), COLOR_BLACK, new Color3f(0.0f, 0.75f, 0.0f),
	    new Color3f(0.62f, 0.55f, 0.36f), 51.2f);

    /**
     * Pinkes Material für die Schnittpunkte.
     */
    public static final Material MATERIAL_PINK = new Material(new Color3f(0f,
	    0f, 1f), new Color3f(0f, 0f, 0f), new Color3f(1f, 0f, 0f),
	    new Color3f(1f, 1f, 1f), 50f);

    /**
     * Rotes Material für das Koordinatensystem.
     */
    public static final Material MATERIAL_RED = new Material(new Color3f(0.24f,
	    0.0f, 0.0f), COLOR_BLACK, new Color3f(0.75f, 0.0f, 0.0f),
	    new Color3f(0.62f, 0.55f, 0.36f), 51.2f);

    /**
     * Weiße Farbe.
     */
    public static final Color3f WHITE = new Color3f(0.8f, 0.8f, 0.8f);

    /**
     * Gelbe Farbe.
     */
    public static final Color3f YELLOW = new Color3f(Color.YELLOW);

    /**
     * Normalen-Generator, dient zur Berechnung der Lichtreflexionen.
     */
    private static NormalGenerator NORMAL = new NormalGenerator();

    /**
     * Dient zur Optimierung von Dreiecksflächen.
     */
    private static Stripifier STRIP = new Stripifier();

    /**
     * Erstellt ein Beschriftungsobjekt für den Java3D-Scene-Graph.
     * 
     * @param textString -
     *                Beschriftung, die angezeigt werden soll.
     * @param material -
     *                Material, mit welchem die Beschriftung gerendert werden
     *                soll.
     * @return Ein TransformGroup Objekt mit den gewünschten Eigenschaften,
     *         welches transformierbar ist.
     */
    public static TransformGroup createAxisName(String textString,
	    Material material) {
	Font3D f3d;
	Text3D txt;
	OrientedShape3D sh = new OrientedShape3D();
	Appearance appearance = generateApperance();
	appearance.setMaterial(material);

	f3d = new Font3D(new Font("Arial", Font.PLAIN, 1), 0.1,
		new FontExtrusion());

	txt = new Text3D(f3d, textString);

	sh.addGeometry(txt);
	sh.setAppearance(appearance);
	sh.setRotationPoint(new Point3f());
	sh.setAlignmentMode(OrientedShape3D.ROTATE_ABOUT_POINT);

	TransformGroup tg = new TransformGroup();
	tg.addChild(sh);
	tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	return tg;
    }

    /**
     * Erstellt ein Transform3D-Objekt, mit dem man 3D-Objekte bewegen kann.
     * 
     * Es wird eine kombinierte Transformationsmatrix erstellt, die von dem
     * Transform3D-Objekt gekapselt wird.
     * 
     * @param pos -
     *                Die Position, an die das Objekt verschoben werden soll.
     * @param rot -
     *                Die Rotationswinkel um die das Objekt gedreht werden soll.
     * @param scale -
     *                Die Skalierungen, um die das Objekt vergrößert /
     *                verkleinert werden soll.
     * @return Das fertige Transform3D-Objekt.
     */
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

    /**
     * Erstellt ein einfaches Apperance-Objekt.
     * 
     * Dieses wird benötigt, um 3D-Objekten Materialien zuweisen zu können.
     * Desweiteren kan man hiermit weitere visuelle Einstellungen tätigen.
     * 
     * @return Einfaches Apperance-Objekt.
     */
    public static Appearance generateApperance() {
	Appearance ap = new Appearance();

	ap.setLineAttributes(new LineAttributes(3.0f,
		LineAttributes.PATTERN_SOLID, true));
	ap.setTransparencyAttributes(new TransparencyAttributes(
		TransparencyAttributes.NONE, 0.0f));
	PolygonAttributes pa = new PolygonAttributes();
	pa.setCullFace(PolygonAttributes.CULL_BACK);
	pa.setPolygonMode(PolygonAttributes.POLYGON_FILL);
	ap.setPolygonAttributes(pa);

	return ap;
    }

    /**
     * Erstellt ein Apperance-Objekt.
     * 
     * Dieses wird benötigt, um 3D-Objekten Materialien zuweisen zu können.
     * Desweiteren kan man hiermit weitere visuelle Einstellungen tätigen.
     * 
     * @param material -
     *                Material, welches dem Objekt zugewiesen werden soll.
     * @return Apperance-Objekt.
     */
    public static Appearance generateApperance(Material material) {
	Appearance apr = generateApperance();
	apr.setMaterial(material);
	return apr;
    }

    public static Cylinder generateCylinder(float radius, float heigth,
	    Appearance apr) {
	return new Cylinder(radius, heigth, Primitive.GENERATE_NORMALS, 100,
		100, apr);
    }

    /**
     * Erstellt die Normalen für 3D-Objekte.
     * 
     * Dies ist nötig, um Lichtreflexionen korrekt zu berechnen.
     * 
     * @param gi -
     *                Geometrie-Objekt, welches die Daten enthält.
     */
    public static void generateNormal(GeometryInfo gi) {
	NORMAL.generateNormals(gi);
    }

    /**
     * Erstellt Triangle-Strips aus den Geometrie-Daten, dadurch können
     * 3D-Objekte schneller gerendert werden.
     * 
     * @param gi -
     *                Geometrie-Objekt, welches die Daten enthält.
     */
    public static void stripify(GeometryInfo gi) {
	STRIP.stripify(gi);
    }

    /**
     * Privater Konstruktor, da wir keine Instanz dieser Klasse wollen.
     */
    private Tools3D() {
    }
}
