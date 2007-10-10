package info.kriese.soPra.test;

import info.kriese.soPra.gui.InputFrame;
import info.kriese.soPra.math.Vector3Frac;
import info.kriese.soPra.math.impl.Vector3FracFactory;

import java.util.Vector;

/**
 * @author Peer Sterner
 * 
 */
public class TestDualProblem {

    private static int numVar;

    private static Vector3Frac target;

    // private static String[] ops;

    // private static boolean max;

    private static Vector<Vector3Frac> vectors;

    public static void main(String[] args) {

	generateTestData();

	// DualProblem d = new DualProblem();
	// d.generateDualProblem(vectors, target, ops, minMax);

	javax.swing.SwingUtilities.invokeLater(new Runnable() {
	    public void run() {
		createAndShowGUI();
	    }
	});

    }

    private static void createAndShowGUI() {
	// Create and set up the window.

	InputFrame frame = new InputFrame(null, null);
	// frame.setTarget(target);
	// frame.setVectors(vectors);
	// frame.setOps(ops);
	// frame.setMax(max);
	frame.setLocation(300, 200);
	frame.setVisible(true);
	System.exit(0);
    }

    private static void generateTestData() {

	vectors = new Vector<Vector3Frac>();
	target = Vector3FracFactory.getInstance();
	// ops = new String[] { "=", ">", "=" };
	// max = false;
	numVar = 3;
	for (int i = 0; i < numVar; i++)
	    vectors.add(Vector3FracFactory.getInstance());

	target.getCoordX().setNumerator(1);
	target.getCoordY().setNumerator(2);

	vectors.get(0).getCoordX().setNumerator(-4);
	vectors.get(0).getCoordY().setNumerator(5);
	vectors.get(0).getCoordZ().setNumerator(3);

	vectors.get(1).getCoordX().setNumerator(-7);
	vectors.get(1).getCoordY().setNumerator(8);
	vectors.get(1).getCoordZ().setNumerator(-6);

	vectors.get(2).getCoordX().setNumerator(10);
	vectors.get(2).getCoordY().setNumerator(-11);
	vectors.get(2).getCoordZ().setNumerator(9);

    }

}
