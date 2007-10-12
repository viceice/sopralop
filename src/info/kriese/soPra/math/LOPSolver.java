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
 * 12.10.2007 - Version 0.3.7.1
 * - Optimum wird auch bei Unlimited & NoSolution gesetzt
 * 11.10.2007 - Version 0.3.7
 * - Spezialfallbehandlung überarbeitet, sie war fehlerhaft
 * 09.10.2007 - Version 0.3.6
 * - Speichern der optimalen Vektoren überarbeitet
 * - Lösung wird nicht mehr aus Datei geladen (Live Berechnung)
 * 07.10.2007 - Version 0.3.5
 * - spezielle Lösungen eines LOP werden ermittelt
 * - Optimum und optimale Vektoren werden gespeichert
 * 03.10.2007 - Version 0.3.4
 * - Behandlung von Speziellen LOP's
 * 24.09.2007 - Version 0.3.3
 * - An verändertes Speicherformat angepasst
 * 17.09.2007 - Version 0.3.2
 * - Kann einfache Probleme lösen
 * 11.09.2007 - Version 0.3.1
 *  - problem.createSolution -> problem.getSolution()
 *  - HTML-Kram ausgelagert -> HTMLLinearOptimizingProblem
 *  - Visualisierung in MainFrame ausgelagert
 * 23.08.2007 - Version 0.3
 *  - Das Grundproblem in eigene Klasse extrahiert
 *  - Diverse Methoden geloescht
 *  - Umbenannt
 *  - Weitere Aenderungen folgen
 * 30.07.2007 - Version 0.2
 * - Problem laden implementiert
 * - Weitere Methoden hizugefuegt
 * 29.07.2007
 * - Problem kann teilweise gespeichert werden
 * - Getter & Setter hinzugefuegt
 * 10.05.2007 - Version 0.1
 * - Datei hinzugefuegt
 */
package info.kriese.soPra.math;

import info.kriese.soPra.gui.lang.Lang;
import info.kriese.soPra.io.IOUtils;
import info.kriese.soPra.lop.LOP;
import info.kriese.soPra.lop.LOPSolution;
import info.kriese.soPra.lop.impl.LOPFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * @author Michael Kriese
 * @version 0.3.7.1
 * @since 10.05.2007
 * 
 */
public final class LOPSolver {

    private final Gauss gauss;

    private final QuickHull hull;

    private final LOP lop;

    public LOPSolver() {
	this.lop = LOPFactory.newLinearOptimizingProblem();

	this.hull = new QuickHull();
	this.gauss = new Gauss();
    }

    public LOP getProblem() {
	return this.lop;
    }

    public boolean open(URL file) {
	try {
	    Node n = null;
	    NamedNodeMap att = null;
	    NodeList ndList = null;

	    boolean bMax = true;
	    Vector3Frac vTarget = null;
	    Vector<Vector3Frac> vecs = new Vector<Vector3Frac>();
	    String[] vOps = null;
	    // ---- Parse XML file ----
	    DocumentBuilderFactory factory = DocumentBuilderFactory
		    .newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    Document document = builder.parse(file.openStream());

	    // ---- Get list of nodes to given element tag name ----
	    ndList = document.getElementsByTagName("lop");
	    n = ndList.item(0);

	    att = n.getAttributes();
	    if (att != null) {
		n = att.getNamedItem("type");
		if (n != null && n.getNodeValue().equals("min"))
		    bMax = false;
	    }

	    ndList = document.getElementsByTagName("target");
	    if (ndList.getLength() == 1) {
		n = ndList.item(0);
		att = n.getAttributes();
		if (att != null)
		    vTarget = IOUtils.getVector(att);
	    }

	    ndList = document.getElementsByTagName("ops");
	    if (ndList.getLength() == 1) {
		n = ndList.item(0);
		att = n.getAttributes();
		if (att != null)
		    vOps = IOUtils.getOperators(att);
	    }

	    ndList = document.getElementsByTagName("vectors");
	    if (ndList.getLength() == 1) {
		ndList = ndList.item(0).getChildNodes();

		if (ndList.getLength() > 2)
		    for (int i = 0; i < ndList.getLength(); i++) {
			if (!ndList.item(i).getNodeName().equals("vector"))
			    continue;
			Vector3Frac tmp = IOUtils.getVector(ndList.item(i)
				.getAttributes());
			if (tmp != null)
			    vecs.add(tmp);
			else {
			    vecs.clear();
			    break;
			}
		    }
	    }

	    if (vTarget != null && vecs.size() >= LOP.MIN_VECTORS
		    && vecs.size() <= LOP.MAX_VECTORS && vOps != null) {
		this.lop.setMaximum(bMax);
		this.lop.setTarget(vTarget);
		this.lop.setOperators(vOps);
		this.lop.setVectors(vecs);
		this.lop.problemChanged();
	    }

	    solve();
	    return true;

	    // ---- Error handling ----
	} catch (SAXParseException spe) {
	    System.out.println("\n** Parsing error, line "
		    + spe.getLineNumber() + ", uri " + spe.getSystemId());
	    System.out.println("   " + spe.getMessage());
	    Exception e = (spe.getException() != null) ? spe.getException()
		    : spe;
	    e.printStackTrace();
	} catch (SAXException sxe) {
	    Exception e = (sxe.getException() != null) ? sxe.getException()
		    : sxe;
	    e.printStackTrace();
	} catch (ParserConfigurationException pce) {
	    pce.printStackTrace();
	} catch (IOException ioe) {
	    ioe.printStackTrace();
	}
	return false;
    }

    public void print(PrintStream out) {
	StringBuffer x = new StringBuffer(), y = new StringBuffer(), z = new StringBuffer();

	Vector3Frac vec = this.lop.getVectors().get(0);

	x.append(vec.getCoordX() + " ");
	y.append(vec.getCoordY() + " ");
	z.append(vec.getCoordZ() + " ");

	for (int i = 1; i < this.lop.getVectors().size(); i++) {
	    vec = this.lop.getVectors().get(i);
	    if (vec.getCoordX().toDouble() >= 0)
		x.append("+");
	    if (vec.getCoordY().toDouble() >= 0)
		y.append("+");
	    if (vec.getCoordZ().toDouble() >= 0)
		z.append("+");

	    x.append(vec.getCoordX() + " ");
	    y.append(vec.getCoordY() + " ");
	    z.append(vec.getCoordZ() + " ");
	}

	vec = this.lop.getTarget();

	x.append(" " + this.lop.getOperators()[0] + " " + vec.getCoordX());
	y.append(" " + this.lop.getOperators()[1] + " " + vec.getCoordY());
	z.append(" = " + (this.lop.isMaximum() ? "max" : "min"));

	out.println(x.toString());
	out.println(y.toString());
	out.println(z.toString());

	LOPSolution sol = this.lop.getSolution();

	out.println();
	out.println("Lösung: " + sol.getValue());

	switch (sol.getSpecialCase()) {
	    case LOPSolution.NO_SOLUTION:
		out.println(Lang.getString("Strings.NoSolution"));
		break;
	    case LOPSolution.MORE_THAN_ONE_SOLUTION:
		out.println(Lang.getString("Strings.MoreSolutions").replace(
			"{0}", sol.countAreas() + ""));
		break;
	    case LOPSolution.UNLIMITED:
		out.println(Lang.getString("Strings.UnlimitedSol"));
		break;
	    case LOPSolution.SIMPLE:
		out.println(Lang.getString("Strings.Simple"));
		break;
	    default:
		out.println("Error! Wrong special case. ("
			+ sol.getSpecialCase() + ")");
		break;
	}
    }

    public boolean save(String file) {
	try {
	    FileOutputStream fout = new FileOutputStream(file, false);
	    PrintStream myOutput = new PrintStream(fout, false, "UTF-8");
	    String out = IOUtils.generateXMLContent(this.lop);
	    myOutput.println(out);
	    myOutput.flush();
	    myOutput.close();
	    fout.close();
	    return true;
	} catch (IOException ex) {
	    System.out.println("Error writing to file: " + file);
	    return false;
	}
    }

    public void solve() {

	LOPSolution sol = this.lop.getSolution();

	this.hull.build(this.lop.getVectors());

	Vector3Frac[] vertices = this.hull.getVerticesAsVector3Frac();
	int[][] faces = this.hull.getFaces();

	Vector3Frac sln, l1, l2, l3, opt_vector = this.lop.getTarget().clone();

	boolean max = this.lop.isMaximum(), unlimited = false;
	Fractional value_high, value_low, opt = null, value_unlimit = null;

	value_high = Fractional.MAX_VALUE;
	value_low = Fractional.MIN_VALUE;

	for (int[] face : faces)
	    if (vertices[face[0]].equals(Vector3Frac.ZERO)
		    || vertices[face[1]].equals(Vector3Frac.ZERO)
		    || vertices[face[2]].equals(Vector3Frac.ZERO)) {

		l1 = vertices[face[0]].equals(Vector3Frac.ZERO) ? vertices[face[1]]
			: vertices[face[0]];
		l2 = vertices[face[2]].equals(Vector3Frac.ZERO) ? vertices[face[1]]
			: vertices[face[2]];

		sln = this.gauss.gaussElimination(l1, l2, this.lop.getTarget());

		opt_vector.setCoordZ(sln.getCoordZ());

		if (isPointInTriangle(l1.scale(100), l2.scale(100), opt_vector)) {
		    if (opt == null) {
			opt = sln.getCoordZ();
			sol.addArea(l1, l2);
		    }

		    if (max && sln.getCoordZ().equals(opt)
			    && !value_high.equals(Fractional.MAX_VALUE)) {
			sol.addArea(l1, l2);
			break;
		    }
		    if (!max && sln.getCoordZ().equals(opt)
			    && !value_low.equals(Fractional.MIN_VALUE)) {
			sol.addArea(l1, l2);
			break;
		    }
		    if (sln.getCoordZ().compareTo(value_high) < 0) {
			if (!max) {
			    sol.clearAreas();
			    sol.addArea(l1, l2);
			}
			value_low = sln.getCoordZ();
		    }
		    if (sln.getCoordZ().compareTo(value_low) > 0) {
			if (max) {
			    sol.clearAreas();
			    sol.addArea(l1, l2);
			}
			value_high = sln.getCoordZ();
		    }
		    if (sln.getCoordZ().compareTo(opt) > 0) {
			if (max) {
			    opt = sln.getCoordZ();
			    sol.clearAreas();
			    sol.addArea(l1, l2);
			}
			value_high = sln.getCoordZ();
		    }
		    if (sln.getCoordZ().compareTo(opt) < 0) {
			if (!max) {
			    opt = sln.getCoordZ();
			    sol.clearAreas();
			    sol.addArea(l1, l2);
			}
			value_low = sln.getCoordZ();
		    }
		}
	    } else {
		// Überprüfe ob Zielvektor den Kegelboden durchstößt
		// Falls ja ist MAX oder MIN unendlich
		l1 = vertices[face[0]].scale(100);
		l2 = vertices[face[1]].scale(100);
		l3 = vertices[face[2]].scale(100);

		sln = this.gauss.gaussElimination(l1, l2, l3, this.lop
			.getTarget());
		opt_vector.setCoordZ(sln.getCoordZ());

		if (isPointInTriangle(l1, l2, l3, opt_vector)) {
		    unlimited = true;
		    value_unlimit = sln.getCoordZ();
		}
	    }

	if (unlimited && opt != null) {
	    if (max && opt.compareTo(value_unlimit) < 0) {
		sol.setSpecialCase(LOPSolution.UNLIMITED);
		opt = Fractional.MAX_VALUE;
	    }

	    if (!max && opt.compareTo(value_unlimit) > 0) {
		sol.setSpecialCase(LOPSolution.UNLIMITED);
		opt = Fractional.MIN_VALUE;
	    }
	}

	if (opt == null) {
	    sol.setSpecialCase(LOPSolution.NO_SOLUTION);
	    opt = Fractional.MAX_VALUE;
	}

	if (sol.countAreas() > 2)
	    sol.setSpecialCase(LOPSolution.MORE_THAN_ONE_SOLUTION);

	sol.setValue(opt);

	this.lop.problemSolved();
    }

    private boolean isPointInTriangle(Vector3Frac a, Vector3Frac b,
	    Vector3Frac p) {
	return isPointInTriangle(Vector3Frac.ZERO, a, b, p);
    }

    private boolean isPointInTriangle(Vector3Frac a, Vector3Frac b,
	    Vector3Frac c, Vector3Frac p) {

	Vector3Frac v0 = c.sub(a);
	Vector3Frac v1 = b.sub(a);
	Vector3Frac v2 = p.sub(a);

	// Compute dot products
	float dot00 = v0.dot(v0);
	float dot01 = v0.dot(v1);
	float dot02 = v0.dot(v2);
	float dot11 = v1.dot(v1);
	float dot12 = v1.dot(v2);

	// Compute barycentric coordinates
	float invDenom = 1 / (dot00 * dot11 - dot01 * dot01);
	float u = (dot11 * dot02 - dot01 * dot12) * invDenom;
	float v = (dot00 * dot12 - dot01 * dot02) * invDenom;

	u = Math2.round(u);
	v = Math2.round(v);

	// Check if point is in triangle
	boolean res = (u >= 0) && (v >= 0) && (u + v <= 1);

	// System.err.println("[ " + a + ", " + b + ", " + c + " ] = " + p
	// + "\t\t\t[ u=" + u + ", v=" + v + " | "
	// + (res ? "true" : "false") + "]");
	return res;
    }
}
