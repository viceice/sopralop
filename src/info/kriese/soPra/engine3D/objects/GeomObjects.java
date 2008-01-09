/**
 * @version 	$Id$
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
 * 11.09.2007 - Version 0.3
 * - getPoint gelöscht, in neue Klasse konvertiert
 * 30.07.2007 - Version 0.2.1
 * - Einige CapabilityBits gesetzt
 * 27.07.2007 - Version 0.2
 * - Antialising der Linien
 * 12.05.2007
 * - einige Objekte in eigene Klassen ausgelagert
 * 25.04.2007 - Version 0.1
 * - Datei hinzugefuegt
 */
package info.kriese.soPra.engine3D.objects;

import info.kriese.soPra.engine3D.Tools3D;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Node;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;

/**
 * Dient zur Erstellung der Beleuchtung.
 * 
 * @author Michael Kriese
 * @since 25.04.2007
 * @version 0.3
 */
public final class GeomObjects {

    /**
     * Erstellt die Beleuchtung.
     * 
     * @return Gruppe der Lichter.
     */
    public static Node getLight() {
	TransformGroup node = new TransformGroup();

	AmbientLight global = new AmbientLight(new Color3f(0.4f, 0.4f, 0.4f));
	global.setInfluencingBounds(Tools3D.LIGHT_BOUNDS);
	node.addChild(global);

	DirectionalLight direct = new DirectionalLight(Tools3D.WHITE,
		new Vector3f(-0.5f, -1.0f, -1.0f));
	direct.setInfluencingBounds(Tools3D.LIGHT_BOUNDS);
	node.addChild(direct);

	direct = new DirectionalLight(Tools3D.WHITE, new Vector3f(0.0f, 0.0f,
		1.0f));
	direct.setInfluencingBounds(Tools3D.LIGHT_BOUNDS);
	node.addChild(direct);

	return node;
    }

    /**
     * Privater Konstruktor, da keine Instanz der Klasse erwünscht.
     */
    private GeomObjects() {
    }
}
