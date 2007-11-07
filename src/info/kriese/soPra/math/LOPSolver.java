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
 * 01.11.2007 - Version 0.5
 * - An LOPEditor angepasst
 * 24.10.2007 - Version 0.4.0.1
 * - BugFix: Mehrere Lösungen wurden als eine erkannt
 * 23.10.2007 - Version 0.4
 * - An neuen Quickhull-Algorithmus angepasst
 * 16.10.2007 - Version 0.3.7.2
 * - NullPointer behoben, falls zu öffnendes LOP nicht existiert
 * - Methode solve() überarbeitet, Gausselimination vereinheitlicht
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

import info.kriese.soPra.io.IOUtils;
import info.kriese.soPra.lop.LOP;
import info.kriese.soPra.lop.LOPEditor;
import info.kriese.soPra.lop.LOPEditorAdapter;
import info.kriese.soPra.lop.LOPSolution;
import info.kriese.soPra.math.impl.FractionalFactory;
import info.kriese.soPra.math.quickhull.QuickHull;

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
 * @version 0.5
 * @since 10.05.2007
 * 
 */
public final class LOPSolver {

    private final Gauss gauss;

    private final QuickHull hull;

    public LOPSolver() {
	this.hull = new QuickHull();
	this.gauss = new Gauss();
    }

    public void setEditor(LOPEditor editor) {
	editor.addListener(new LOPEditorAdapter() {
	    @Override
	    public boolean open(LOP lop, URL file) {
		return LOPSolver.this.open(lop, file);
	    }

	    @Override
	    public void save(LOP lop, URL file) {
		LOPSolver.this.save(lop, file);
	    }

	    @Override
	    public void solve(LOP lop) {
		LOPSolver.this.solve(lop);
	    }
	});
    }

    private boolean open(LOP lop, URL file) {
	if (file == null)
	    return false;
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
		lop.setMaximum(bMax);
		lop.setTarget(vTarget);
		lop.setOperators(vOps);
		lop.setVectors(vecs);
		lop.problemChanged();
	    }
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

    private boolean save(LOP lop, URL file) {
	try {
	    FileOutputStream fout = new FileOutputStream(file.getFile(), false);
	    PrintStream myOutput = new PrintStream(fout, false, "UTF-8");
	    String out = IOUtils.generateXMLContent(lop);
	    myOutput.println(out);
	    myOutput.flush();
	    myOutput.close();
	    fout.close();
	    return true;
	} catch (IOException ex) {
	    System.out.println("Error writing to file: " + file);
	    System.err.println(ex);
	    return false;
	}
    }

    private void solve(LOP lop) {

	LOPSolution sol = lop.getSolution();

	this.hull.build(lop.getVectors());

	Vector3Frac sln, opt_vector = lop.getTarget().clone();

	boolean max = lop.isMaximum(), unlimited = false;
	Fractional value_high, value_low, opt = null, value_unlimit = null;

	value_high = Fractional.MAX_VALUE;
	value_low = Fractional.MIN_VALUE;

	Fractional ZERO = FractionalFactory.getInstance();

	for (Vertex vertex : this.hull.getVerticesList())
	    if (vertex.p1.equals(Vector3Frac.ZERO)) {

		sln = this.gauss.gaussElimination2(vertex.p1, vertex.p2,
			vertex.p3, lop.getTarget());

		opt_vector.setCoordZ(sln.getCoordZ());

		if (sln.getCoordX().compareTo(ZERO) >= 0
			&& sln.getCoordY().compareTo(ZERO) >= 0) {
		    // TODO: remove that
		    System.err.println(vertex + " = " + sln);
		    if (opt == null) {
			opt = sln.getCoordZ();
			sol.addArea(vertex.p2, vertex.p3, sln.getCoordX(), sln
				.getCoordY());
		    }

		    if (max && sln.getCoordZ().equals(opt)
			    && !value_high.equals(Fractional.MAX_VALUE)) {
			sol.addArea(vertex.p2, vertex.p3, sln.getCoordX(), sln
				.getCoordY());
			break;
		    }
		    if (!max && sln.getCoordZ().equals(opt)
			    && !value_low.equals(Fractional.MIN_VALUE)) {
			sol.addArea(vertex.p2, vertex.p3, sln.getCoordX(), sln
				.getCoordY());
			break;
		    }
		    if (sln.getCoordZ().compareTo(value_high) < 0) {
			if (!max) {
			    sol.clearAreas();
			    sol.addArea(vertex.p2, vertex.p3, sln.getCoordX(),
				    sln.getCoordY());
			}
			value_low = sln.getCoordZ();
		    }
		    if (sln.getCoordZ().compareTo(value_low) > 0) {
			if (max) {
			    sol.clearAreas();
			    sol.addArea(vertex.p2, vertex.p3, sln.getCoordX(),
				    sln.getCoordY());
			}
			value_high = sln.getCoordZ();
		    }
		    if (sln.getCoordZ().compareTo(opt) > 0) {
			if (max) {
			    opt = sln.getCoordZ();
			    sol.clearAreas();
			    sol.addArea(vertex.p2, vertex.p3, sln.getCoordX(),
				    sln.getCoordY());
			}
			value_high = sln.getCoordZ();
		    }
		    if (sln.getCoordZ().compareTo(opt) < 0) {
			if (!max) {
			    opt = sln.getCoordZ();
			    sol.clearAreas();
			    sol.addArea(vertex.p2, vertex.p3, sln.getCoordX(),
				    sln.getCoordY());
			}
			value_low = sln.getCoordZ();
		    }
		}
	    } else {
		// Überprüfe ob Zielvektor den Kegelboden durchstößt
		// Falls ja ist MAX oder MIN unendlich

		sln = this.gauss.gaussElimination2(vertex.p1, vertex.p2,
			lop.getTarget());
		opt_vector.setCoordZ(sln.getCoordZ());

		if (vertex.isPointInVertex(opt_vector)) {
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

	if (sol.countAreas() >= 2)
	    sol.setSpecialCase(LOPSolution.MORE_THAN_ONE_SOLUTION);

	sol.setValue(opt);

	lop.problemSolved();
	lop.showSolution();

	IOUtils.print(lop, System.err);
    }
}
