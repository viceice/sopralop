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
 * 12.05.2007 - Version 0.2
 * - kleinere Aenderungen
 * - Fehler behoben, der bei Applets zur Exception fuehren konnte
 * 26.04.2007 - Version 0.1
 * - Datei hinzugefuegt
 */
package info.kriese.soPra.engine3D.manipulate;

import info.kriese.soPra.engine3D.OverlayCanvas3D;
import info.kriese.soPra.engine3D.Tools3D;

import java.util.Enumeration;

import javax.media.j3d.Behavior;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnElapsedFrames;

/**
 * Erstellt einen Frameratecounter.
 * 
 * @author Michael Kriese
 * @since 26.04.2007
 * @version 0.2
 */
public class FrameRateBehavior extends Behavior {

    private final OverlayCanvas3D canvas;

    private WakeupCriterion criterion;

    private int frames = 0;

    private long time = System.currentTimeMillis();

    public FrameRateBehavior(OverlayCanvas3D canvas) {
	this.canvas = canvas;
	setSchedulingBounds(Tools3D.LIGHT_BOUNDS);
    }

    @Override
    public void initialize() {
	this.criterion = new WakeupOnElapsedFrames(0);
	wakeupOn(this.criterion);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void processStimulus(Enumeration criteria) {

	while (criteria.hasMoreElements()) {
	    Object obj = criteria.nextElement();
	    if (obj instanceof WakeupOnElapsedFrames) {
		this.frames++;
		if (System.currentTimeMillis() - this.time > 1000) {
		    this.canvas.setFPS(this.frames);
		    this.frames = 0;
		    this.time = System.currentTimeMillis();
		}
	    }
	}
	wakeupOn(this.criterion);
    }

}
