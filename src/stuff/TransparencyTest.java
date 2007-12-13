package stuff;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class TransparencyTest extends JFrame implements ChangeListener {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
	new TransparencyTest().setVisible(true);
    }

    private final JSlider slider;

    public TransparencyTest() {
	super("Transparency Test");
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	this.slider = new JSlider(0, 255);
	this.slider.setValue(255);
	this.slider.addChangeListener(this);

	JPanel panel = new JPanel(new BorderLayout());
	panel.add(this.slider, BorderLayout.NORTH);

	JLabel l = new JLabel("Test");
	l.setOpaque(true);
	panel.add(l);
	setContentPane(panel);
	setSize(new Dimension(640, 480));
	setLocationRelativeTo(null);
    }

    public void setWindowOpacity(Frame frame, double opacity) {
	long value = (int) (0xff * opacity) << 24;
	try {
	    // long windowId = peer.getWindow();
	    Field peerField = Component.class.getDeclaredField("peer");
	    peerField.setAccessible(true);
	    Class<?> xWindowPeerClass = Class
		    .forName("sun.awt.X11.XWindowPeer");
	    Method getWindowMethod = xWindowPeerClass.getMethod("getWindow",
		    new Class[0]);
	    long windowId = ((Long) getWindowMethod.invoke(
		    peerField.get(frame), new Object[0])).longValue();

	    // sun.awt.X11.XAtom.get("_NET_WM_WINDOW_OPACITY").setCard32Property(windowId,
	    // value);
	    Class<?> xAtomClass = Class.forName("sun.awt.X11.XAtom");
	    Method getMethod = xAtomClass.getMethod("get", String.class);
	    Method setCard32PropertyMethod = xAtomClass.getMethod(
		    "setCard32Property", long.class, long.class);
	    setCard32PropertyMethod.invoke(getMethod.invoke(null,
		    "_NET_WM_WINDOW_OPACITY"), windowId, value);
	} catch (Exception ex) {
	    // Boo hoo! No transparency for you!
	    ex.printStackTrace();
	    return;
	}
    }

    public void stateChanged(ChangeEvent e) {
	double value = ((double) this.slider.getValue())
		/ ((double) this.slider.getMaximum());
	setWindowOpacity(this, value);
    }
}