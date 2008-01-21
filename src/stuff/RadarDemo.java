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
 * 19.01.2008 - Version 0.1
 *  - Datei hinzugefuegt
 */
package stuff;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.Random;

import javax.media.j3d.Alpha;
import javax.media.j3d.Behavior;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.GraphicsConfigTemplate3D;
import javax.media.j3d.GraphicsContext3D;
import javax.media.j3d.J3DGraphics2D;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.WakeupCondition;
import javax.media.j3d.WakeupOnElapsedFrames;
import javax.swing.JFrame;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.universe.MultiTransformGroup;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewInfo;

public class RadarDemo {

    public static class FPSRenderer {

	private static final int WRAP = 0x3ff;

	private long duration10;

	private long duration100;

	private long duration1000;

	private final DecimalFormat format;

	private double fps10;

	private double fps100;

	private double fps1000;

	private int index10 = (-10) & WRAP;

	private int index100 = (-100) & WRAP;

	private int index1000 = (-1000) & WRAP;

	private int indexCurrent = 0;

	private String s10;

	private String s100;

	private String s1000;

	private final DecimalFormatSymbols symbols;
	private final long[] times = new long[WRAP + 1];

	{
	    this.symbols = new DecimalFormatSymbols();
	    this.symbols.setNaN("???");
	    this.format = new DecimalFormat("#,#00.000", this.symbols);
	}

	public void postRender(GraphicsContext3D gc3d, J3DGraphics2D gc2d) {
	    calc(gc3d);
	    gc2d.setColor(Color.MAGENTA);
	    gc2d.setFont(new Font("Courier New", Font.PLAIN, 16));
	    gc2d.drawString("FPS: " + this.s10 + " / " + this.s100 + " / "
		    + this.s1000, 4, 12);
	    gc2d.flush(false);
	}

	private void calc(GraphicsContext3D gc3d) {
	    long now = gc3d.getCanvas3D().getView().getCurrentFrameStartTime();
	    this.times[this.indexCurrent] = now;
	    this.duration10 = now - this.times[this.index10];
	    this.duration100 = now - this.times[this.index100];
	    this.duration1000 = now - this.times[this.index1000];
	    this.fps10 = (this.duration10 == 0 ? Float.NaN
		    : 10000d / this.duration10);
	    this.fps100 = (this.duration100 == 0 ? Float.NaN
		    : 100000d / this.duration100);
	    this.fps1000 = (this.duration1000 == 0 ? Float.NaN
		    : 1000000d / this.duration1000);
	    this.s10 = this.format.format(this.fps10);
	    this.s100 = this.format.format(this.fps100);
	    this.s1000 = this.format.format(this.fps1000);

	    this.indexCurrent = (this.indexCurrent + 1) & WRAP;
	    this.index10 = (this.index10 + 1) & WRAP;
	    this.index100 = (this.index100 + 1) & WRAP;
	    this.index1000 = (this.index1000 + 1) & WRAP;
	}
    }

    static class BigBounds extends BoundingSphere {

	BigBounds() {
	    super(new Point3d(), Double.POSITIVE_INFINITY);
	}
    }

    @SuppressWarnings("serial")
    static class JFrame3D extends JFrame {

	protected static GraphicsConfiguration defaultGraphicsConfiguration() {
	    return GraphicsEnvironment.getLocalGraphicsEnvironment()
		    .getDefaultScreenDevice().getBestConfiguration(
			    new GraphicsConfigTemplate3D());
	}

	private final Canvas3D canvas;

	RadarRenderer radar;

	public JFrame3D() {
	    this(defaultGraphicsConfiguration());
	}

	public JFrame3D(GraphicsConfiguration gc) {
	    super(gc);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setLayout(new BorderLayout());
	    this.radar = new RadarRenderer();
	    this.canvas = new Canvas3D(gc) {

		@Override
		public void postRender() {
		    JFrame3D.this.radar.render(this);
		}
	    };
	    add(BorderLayout.CENTER, this.canvas);
	    setLocation(64, 64);
	    setSize(640, 480);
	    // setResizable(false);
	    setVisible(true);
	}

	public Canvas3D getCanvas3D() {
	    return this.canvas;
	}

	public RadarRenderer getRadarRenderer() {
	    return this.radar;
	}
    }

    static class RadarBehavior extends Behavior {

	private static final Point3d origin = new Point3d();

	private int blipTimeout;

	private final Color color;

	private final Transform3D mapNocToVwc = new Transform3D();

	private final WakeupCondition wakeup = new WakeupOnElapsedFrames(
		FRAMES_RADAR_UPDATE);

	RadarBehavior(Color color) {
	    this.color = color;
	    setSchedulingBounds(new RadarBounds());
	    setCapability(Behavior.ALLOW_LOCAL_TO_VWORLD_READ);
	}

	public Color getColor() {
	    return this.color;
	}

	public void getNocToVwcTransform(Transform3D mapNocToVwc) {
	    mapNocToVwc.set(this.mapNocToVwc);
	}

	public void getPosition(Point3d position) {
	    this.mapNocToVwc.transform(origin, position);
	}

	@Override
	public void initialize() {
	    wakeupOn(this.wakeup);
	}

	public boolean isRadarBlip(boolean clearBlip) {
	    if (clearBlip)
		this.blipTimeout -= 1;

	    return this.blipTimeout >= FRAMES_RADAR_UPDATE;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void processStimulus(Enumeration criteria) {
	    getLocalToVworld(this.mapNocToVwc);
	    this.blipTimeout = FRAMES_RADAR_UPDATE + 3;
	    wakeupOn(this.wakeup);
	}

    }

    static class RadarBounds extends BoundingSphere {

	RadarBounds() {
	    super(new Point3d(), VWRLD_RADAR_RANGE);
	}
    }

    static class RadarRenderer {

	static Shape3D blipSprite = new ColorCube(0.1);

	private final LinkedList<RadarBehavior> blips = new LinkedList<RadarBehavior>();

	private final RadarBehavior center = new RadarBehavior(Color.WHITE);

	private final FPSRenderer fpsRenderer = new FPSRenderer();

	private SimpleUniverse universe;
	private ViewInfo viewInfo;

	{
	    this.blips.add(this.center);
	}

	public RadarBehavior getBlip(Color color) {
	    RadarBehavior blip = new RadarBehavior(color);
	    this.blips.add(blip);
	    return blip;
	}

	public RadarBehavior getCenter() {
	    return this.center;
	}

	public SimpleUniverse getUniverse() {
	    return this.universe;
	}

	public void setUniverse(SimpleUniverse universe) {
	    this.universe = universe;
	    this.viewInfo = new ViewInfo(universe.getViewer().getView());
	}

	private void markRadarRange(J3DGraphics2D gc2d,
		Point3d rpcBlipPosition, Point3d Position, int centerX,
		int centerY, int blipSize, Transform3D t3dCombined, int scale) {
	    // mark radar range with yellow spots
	    gc2d.setColor(Color.YELLOW);
	    double d10 = VWRLD_RADAR_RANGE;
	    double d07 = d10 * Math.sqrt(0.5d);
	    Point3d[] points = { new Point3d(d10, 0, 0),
		    new Point3d(d07, 0, d07), new Point3d(0, 0, d10),
		    new Point3d(-d07, 0, d07), new Point3d(-d10, 0, 0),
		    new Point3d(-d07, 0, -d07), new Point3d(0, 0, -d10),
		    new Point3d(d07, 0, -d07), };
	    for (Point3d p : points) {
		t3dCombined.transform(p, rpcBlipPosition);
		rpcBlipPosition.sub(Position);
		gc2d.fillArc(
			(int) (rpcBlipPosition.x + centerX - blipSize - 1),
			(int) (rpcBlipPosition.y + centerY - blipSize - 1),
			blipSize * 2 + 1, blipSize * 2 + 1, 0, 360);
	    }
	    // mark origin with larger red spot
	    gc2d.setColor(Color.RED);
	    t3dCombined.transform(new Point3d(0, 0, 0), rpcBlipPosition);
	    rpcBlipPosition.sub(Position);
	    gc2d.fillArc((int) (rpcBlipPosition.x + centerX - blipSize - 3),
		    (int) (rpcBlipPosition.y + centerY - blipSize - 3),
		    blipSize * 2 + 5, blipSize * 2 + 5, 0, 360);
	    // draw axis
	    gc2d.setColor(Color.WHITE);
	    gc2d.drawLine((int) (centerX - scale * VWRLD_RADAR_RANGE), centerY,
		    (int) (centerX + scale * VWRLD_RADAR_RANGE), centerY);
	    gc2d.drawLine(centerX, (int) (centerY - scale * VWRLD_RADAR_RANGE),
		    centerX, (int) (centerY + scale * VWRLD_RADAR_RANGE));
	}

	private void renderBlips(GraphicsContext3D ctx, J3DGraphics2D gc2d) {
	    // noc = node object coordinates
	    // vpc = view platform coordinates
	    // vwc = virtual world coordinates
	    // epc = eye point coordinates (i.e. image plate @ z=-1)
	    // rpc = radar plane coords

	    Point3d vwcBlipPosition = new Point3d();
	    Point3d rpcBlipPosition = new Point3d();

	    Point3d vwcCenterPosition = new Point3d();
	    Point3d rpcCenterPosition = new Point3d();

	    Point3d Position = new Point3d();

	    this.center.getPosition(vwcCenterPosition);

	    Point3d eye = new Point3d(vwcCenterPosition);
	    Point3d target = new Point3d(0, 0, -1);
	    target.add(vwcCenterPosition);
	    Vector3d up = new Vector3d(0, 1, 0);
	    Transform3D t3dView = new Transform3D();
	    t3dView.lookAt(eye, target, up);

	    int centerX = ctx.getCanvas3D().getWidth() > 1 ? ctx.getCanvas3D()
		    .getWidth() : 1;
	    int centerY = ctx.getCanvas3D().getHeight() > 1 ? ctx.getCanvas3D()
		    .getHeight() : 1;
	    int blipSize = 4;
	    int scale = 4;
	    Transform3D t3dSwapZY = new Transform3D(new double[] { scale, 0, 0,
		    0, 0, 0, scale, 0, 0, 0, 0, -1, 0, 0, 0, 1 });

	    Transform3D t3dCombined = new Transform3D();
	    t3dCombined.mul(t3dSwapZY, t3dView);

	    t3dCombined.transform(vwcCenterPosition, rpcCenterPosition);
	    t3dCombined.transform(Position, Position);

	    Transform3D t3dEpcToVwc = new Transform3D();
	    this.viewInfo.getEyeToVworld(ctx.getCanvas3D(), t3dEpcToVwc, null);

	    markRadarRange(gc2d, rpcBlipPosition, Position, centerX, centerY,
		    blipSize, t3dCombined, scale);
	    for (RadarBehavior blip : this.blips) {
		if (!blip.isRadarBlip(true))
		    continue;
		blip.getPosition(vwcBlipPosition);
		t3dCombined.transform(vwcBlipPosition, rpcBlipPosition);
		// rpcBlipPosition.sub(Position);

		gc2d.setColor(blip.getColor());
		gc2d.fillArc(
			(int) (rpcBlipPosition.x + centerX - blipSize - 1),
			(int) (rpcBlipPosition.y + centerY - blipSize - 1),
			blipSize * 2 + 1, blipSize * 2 + 1, 0, 360);
	    }

	    // for (RadarBehavior blip : this.blips) {
	    // blip.getPosition(vwcBlipPosition);
	    // rpcBlipPosition.sub(Position);
	    // t3dCombined.transform(vwcBlipPosition, rpcBlipPosition);
	    //
	    // ctx.draw(blipSprite);
	    // }
	}

	private void renderRadarBorder(GraphicsContext3D ctx) {
	}

	private void renderRadarSweep(GraphicsContext3D ctx) {
	}

	void render(Canvas3D canvas) {
	    GraphicsContext3D ctx = canvas.getGraphicsContext3D();
	    J3DGraphics2D gc2d = ctx.getCanvas3D().getGraphics2D();

	    // ctx.setFrontBufferRendering(true);
	    renderRadarBorder(ctx);
	    renderRadarSweep(ctx);
	    renderBlips(ctx, gc2d);
	    this.fpsRenderer.postRender(ctx, gc2d);
	    gc2d.flush(true);
	    ctx.flush(true);
	}

    }

    static class TinyBounds extends BoundingSphere {

	TinyBounds() {
	    super(new Point3d(), 0.1d);
	}
    }

    static final Point3d CAMERA_PSITIN = new Point3d(0d, 1d, 4d);

    static int FRAMES_RADAR_UPDATE = 0;

    // a repeatable random generator
    static final Random random = new Random(0);

    static final double VWRLD_AVATAR_RTATIN_RADIUS = 6d;

    static final double VWRLD_AVATAR_SIZE = 0.25d;

    static final double VWRLD_AVATARFFSET = 6d;

    static final double VWRLD_RADAR_RANGE = 10d;

    public static void main(String[] args) {
	JFrame3D frame = new JFrame3D();
	Canvas3D canvas = frame.getCanvas3D();
	RadarRenderer radar = frame.getRadarRenderer();
	SimpleUniverse u = new SimpleUniverse(canvas);
	radar.setUniverse(u);
	initializeCamera(u, radar);
	initializeAvatars(u, radar);
	u.addBranchGraph(createScene());
    }

    private static BranchGroup createAvatar(RadarRenderer radar,
	    double offsetX, double offsetZ, double rotRadius, double size,
	    Color color) {
	Transform3D t3d;
	Shape3D avatarShape = new ColorCube(size);
	MultiTransformGroup mtg = new MultiTransformGroup(6);

	t3d = new Transform3D();
	t3d.set(new Vector3d(offsetX, 0, offsetZ));
	mtg.getTransformGroup(0).setTransform(t3d);

	RotationInterpolator rotatorCircle = new RotationInterpolator(
		new Alpha(-1, 4000 + random.nextInt(6000)), mtg
			.getTransformGroup(1));
	rotatorCircle.setSchedulingBounds(new BigBounds());

	t3d = new Transform3D();
	t3d.set(new Vector3d(rotRadius, 0, 0));
	mtg.getTransformGroup(2).setTransform(t3d);

	RotationInterpolator rotatorSpin1 = new RotationInterpolator(new Alpha(
		-1, 1000 + random.nextInt(400)), mtg.getTransformGroup(3));
	rotatorSpin1.setSchedulingBounds(new BigBounds());

	t3d = new Transform3D();
	t3d.rotZ(Math.toRadians(90));
	mtg.getTransformGroup(4).setTransform(t3d);

	RotationInterpolator rotatorSpin2 = new RotationInterpolator(new Alpha(
		-1, 1000 + random.nextInt(400)), mtg.getTransformGroup(5));
	rotatorSpin2.setSchedulingBounds(new BigBounds());

	mtg.getTransformGroup(5).addChild(avatarShape);
	mtg.getTransformGroup(5).addChild(rotatorCircle);
	mtg.getTransformGroup(5).addChild(rotatorSpin1);
	mtg.getTransformGroup(5).addChild(rotatorSpin2);
	mtg.getTransformGroup(5).addChild(radar.getBlip(color));

	BranchGroup bgAvatar = new BranchGroup();
	bgAvatar.addChild(mtg.getTransformGroup(0));
	bgAvatar.compile();
	return bgAvatar;
    }

    private static BranchGroup createScene() {

	BoundingSphere bounds = new BoundingSphere(new Point3d(), 100000d);
	TransformGroup rotatorTG = new TransformGroup();
	Alpha rotatorAlpha = new Alpha(-1, 5000);
	RotationInterpolator rotator = new RotationInterpolator(rotatorAlpha,
		rotatorTG);
	BranchGroup bg = new BranchGroup();

	rotator.setSchedulingBounds(bounds);
	rotatorTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	rotatorTG.addChild(new ColorCube(0.4));
	bg.addChild(rotator);
	bg.addChild(rotatorTG);
	bg.compile();
	return bg;
    }

    private static void initializeAvatars(SimpleUniverse u, RadarRenderer radar) {
	u.addBranchGraph(createAvatar(radar, VWRLD_AVATARFFSET,
		VWRLD_AVATARFFSET, VWRLD_AVATAR_RTATIN_RADIUS,
		VWRLD_AVATAR_SIZE, Color.MAGENTA));
	u.addBranchGraph(createAvatar(radar, -VWRLD_AVATARFFSET,
		VWRLD_AVATARFFSET, VWRLD_AVATAR_RTATIN_RADIUS,
		VWRLD_AVATAR_SIZE, Color.MAGENTA));
	u.addBranchGraph(createAvatar(radar, VWRLD_AVATARFFSET,
		-VWRLD_AVATARFFSET, VWRLD_AVATAR_RTATIN_RADIUS,
		VWRLD_AVATAR_SIZE, Color.MAGENTA));
	u.addBranchGraph(createAvatar(radar, -VWRLD_AVATARFFSET,
		-VWRLD_AVATARFFSET, VWRLD_AVATAR_RTATIN_RADIUS,
		VWRLD_AVATAR_SIZE, Color.MAGENTA));
    }

    private static void initializeCamera(SimpleUniverse u, RadarRenderer radar) {
	u.getViewingPlatform().getViewPlatform().setActivationRadius(0.1f);
	TransformGroup tgViewPlatform = u.getViewingPlatform()
		.getViewPlatformTransform();
	Transform3D t3dViewPlatform = new Transform3D();
	t3dViewPlatform.lookAt(CAMERA_PSITIN, new Point3d(), new Vector3d(0, 1,
		0));
	t3dViewPlatform.invert();
	tgViewPlatform.setTransform(t3dViewPlatform);

	BranchGroup bgRadarCenter = new BranchGroup();
	bgRadarCenter.addChild(radar.getCenter());
	tgViewPlatform.addChild(bgRadarCenter);
    }
}