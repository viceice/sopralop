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
 * 31.10.2007- Version 0.4.1
 * - BugFix: Anzeige des Dualen Problems korrigiert (es fehlte ein Leerzeichen)
 * - Verhalten bei Größenänderung verändert
 * 24.10.2007 - Version 0.4
 * - Struktur für neuen ActionHandler geändert
 * 10.10.2007 - Version 0.3.1
 * - Spezialfälle werden beachtet
 * 03.10.2007 - Version 0.3
 * - LOPListener gegen LOPAdapter ausgetauscht
 * - Duales Problem anzeigen implementiert
 * 24.09.2007 - Version 0.2.1
 * - BugFix: Bei Lösung wurde ein "x" zuviel angezeigt
 * 16.09.2007 - Version 0.2
 * - An neues LOPSolution Interface angepasst
 * 11.09.2007
 *  - LOP Anzeige in MainFrame implementiert
 * 23.08.2007 - Version 0.1
 *  - Datei hinzugefuegt
 */
package info.kriese.soPra.gui.html;

import info.kriese.soPra.gui.lang.Lang;
import info.kriese.soPra.io.IOUtils;
import info.kriese.soPra.lop.LOP;
import info.kriese.soPra.lop.LOPAdapter;
import info.kriese.soPra.lop.LOPSolution;
import info.kriese.soPra.math.Fractional;
import info.kriese.soPra.math.Vector3Frac;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xhtmlrenderer.simple.XHTMLPanel;

/**
 * 
 * @author Michael Kriese
 * @version 0.4.1
 * @since 23.08.2007
 * 
 */
public final class HTMLGenerator {

    private static String getOperator(boolean max) {
	if (max)
	    return ">=";
	else
	    return "<=";
    }

    private final JScrollPane content;

    private final Document doc;

    private final XHTMLPanel info;

    private final LOP lop;

    private Element problem, solution, solution_box;

    public HTMLGenerator(LOP lop) {
	this.lop = lop;

	this.info = new XHTMLPanel();
	this.info.setDocument(IOUtils.BASE_URL.toString());

	this.content = new JScrollPane(this.info);

	this.doc = this.info.getDocument();

	this.content.addComponentListener(new ComponentAdapter() {

	    @Override
	    public void componentResized(ComponentEvent e) {
		HTMLGenerator.this.info.reloadDocument(HTMLGenerator.this.doc);
	    }
	});

	NodeList list = this.doc.getElementsByTagName("div");

	for (int i = 0; i < list.getLength(); i++) {
	    NamedNodeMap map = list.item(i).getAttributes();
	    Node n = map.getNamedItem("id");
	    if (n != null) {
		String value = n.getNodeValue();
		if (value.equals("problem"))
		    this.problem = (Element) list.item(i);
		else if (value.equals("solution"))
		    this.solution = (Element) list.item(i);
		else if (value.equals("solution_box"))
		    this.solution_box = (Element) list.item(i);
	    }
	}

	lop.addProblemListener(new LOPAdapter() {

	    @Override
	    public void problemChanged(LOP lop) {
		HTMLGenerator.this.updateProblem();
	    }

	    @Override
	    public void problemSolved(LOP lop) {
		HTMLGenerator.this.updateSolution();
	    }

	    @Override
	    public void showDualProblem(LOP lop) {
		HTMLGenerator.this.showDualProblem();
	    }

	    @Override
	    public void showPrimalProblem(LOP lop) {
		HTMLGenerator.this.updateProblem();
		HTMLGenerator.this.updateSolution();
	    }

	    @Override
	    public void showSolution(LOP lop) {
		HTMLGenerator.this.showSolution(true);
	    }
	});
    }

    public JComponent getPanel() {
	return this.content;
    }

    private Element createNode(String name) {
	Element elem = this.doc.createElement(name);
	return elem;
    }

    private Element createNode(String name, Fractional frac) {
	return createNode(name, frac.toString());
    }

    private Element createNode(String name, int i) {
	return createNode(name, Integer.toString(i));
    }

    private Element createNode(String name, Node child1, Node child2) {
	Element res = createNode(name);
	res.appendChild(child1);
	res.appendChild(child2);
	return res;
    }

    private Element createNode(String name, String content) {
	Element elem = createNode(name);
	if (content != null) {
	    elem.appendChild(createTextNode(content));
	    if (content.equals("+") || content.equals("-")
		    || content.equals("="))
		elem.setAttribute("class", "operator");
	}

	return elem;
    }

    private Element createNode(String name, String child1, Node child2) {
	return createNode(name, createTextNode(child1), child2);
    }

    private Text createTextNode(Fractional frac) {
	return createTextNode(frac.toString());
    }

    private Text createTextNode(String text) {
	return this.doc.createTextNode(text);
    }

    private void showDualProblem() {
	if (this.problem == null) {
	    System.out.println("NullPointer: problem ist null!");
	    return;
	}

	// remove all childnodes
	while (this.problem.hasChildNodes())
	    this.problem.removeChild(this.problem.getLastChild());

	this.problem.appendChild(createNode("b", Lang
		.getString("Strings.DualProblem")
		+ ":"));
	this.problem.appendChild(createNode("br"));
	this.problem.appendChild(createNode("br"));

	Element table = createNode("table"), tr;

	this.problem.appendChild(table);

	Vector3Frac target = this.lop.getTarget();

	tr = createNode("tr");
	table.appendChild(tr);

	tr.appendChild(createNode("td", target.getCoordX() + " x", createNode(
		"sub", 1)));
	tr.appendChild(createNode("td", "+"));
	tr.appendChild(createNode("td", target.getCoordY() + "x", createNode(
		"sub", 1)));
	tr.appendChild(createNode("td", "="));
	tr.appendChild(createNode("td", this.lop.isMinimum() ? "max" : "min"));

	for (Vector3Frac vec : this.lop.getVectors()) {
	    String x, y, z;
	    tr = createNode("tr");
	    table.appendChild(tr);

	    x = vec.getCoordX().toString();
	    y = vec.getCoordY().toString();
	    z = vec.getCoordZ().toString();

	    tr.appendChild(createNode("td", x + " x", createNode("sub", 1)));

	    if (y.startsWith("-")) {
		tr.appendChild(createNode("td", "-"));
		y = y.substring(1);
	    } else
		tr.appendChild(createNode("td", "+"));

	    tr.appendChild(createNode("td", y + " x", createNode("sub", 2)));

	    tr.appendChild(createNode("td", getOperator(this.lop.isMaximum())));
	    tr.appendChild(createNode("td", z));
	}

	showSolution(false);

	this.info.reloadDocument(this.doc);
    }

    private void showSolution(boolean show) {
	if (this.solution_box == null) {
	    System.out.println("NullPointer: solution_box ist null!");
	    return;
	}

	if (show)
	    this.solution_box.setAttribute("style", "display:block;");
	else
	    this.solution_box.setAttribute("style", "display:none;");

	this.info.reloadDocument(this.doc);
    }

    private void updateProblem() {

	if (this.problem == null) {
	    System.out.println("NullPointer: problem ist null!");
	    return;
	}

	// remove all childnodes
	while (this.problem.hasChildNodes())
	    this.problem.removeChild(this.problem.getLastChild());

	this.problem.appendChild(createNode("b", Lang
		.getString("Strings.Problem")
		+ ":"));
	this.problem.appendChild(createNode("br"));
	this.problem.appendChild(createNode("br"));
	Element node = createNode("table"), trX = createNode("tr"), trY = createNode("tr"), trZ = createNode("tr");

	node.appendChild(trZ);
	node.appendChild(trX);
	node.appendChild(trY);
	this.problem.appendChild(node);

	Vector3Frac vec, target = this.lop.getTarget();
	List<Vector3Frac> vectors = new ArrayList<Vector3Frac>(this.lop
		.getVectors());
	int i = 0;

	vec = vectors.get(i);

	trX.appendChild(createNode("td", vec.getCoordX() + " x", createNode(
		"sub", i + 1)));
	trY.appendChild(createNode("td", vec.getCoordY() + " x", createNode(
		"sub", i + 1)));
	trZ.appendChild(createNode("td", vec.getCoordZ() + " x", createNode(
		"sub", i + 1)));
	i++;

	for (; i < vectors.size(); i++) {
	    vec = vectors.get(i).clone();
	    String x, y, z;

	    x = vec.getCoordX().toString();
	    y = vec.getCoordY().toString();
	    z = vec.getCoordZ().toString();

	    if (x.startsWith("-")) {
		trX.appendChild(createNode("td", "-"));
		x = x.substring(1);
	    } else
		trX.appendChild(createNode("td", "+"));
	    if (y.startsWith("-")) {
		trY.appendChild(createNode("td", "-"));
		y = y.substring(1);
	    } else
		trY.appendChild(createNode("td", "+"));
	    if (z.startsWith("-")) {
		trZ.appendChild(createNode("td", "-"));
		z = z.substring(1);
	    } else
		trZ.appendChild(createNode("td", "+"));

	    trX.appendChild(createNode("td", x + "  x",
		    createNode("sub", i + 1)));
	    trY
		    .appendChild(createNode("td", y + " x", createNode("sub",
			    i + 1)));
	    trZ
		    .appendChild(createNode("td", z + " x", createNode("sub",
			    i + 1)));
	}

	trX.appendChild(createNode("td", this.lop.getOperators()[0]));
	trY.appendChild(createNode("td", this.lop.getOperators()[1]));
	trZ.appendChild(createNode("td", "="));

	trX.appendChild(createNode("td", target.getCoordX()));
	trY.appendChild(createNode("td", target.getCoordY()));
	trZ
		.appendChild(createNode("td", (this.lop.isMaximum() ? "max"
			: "min")));

	showSolution(false);

	// IOUtils.print(doc);

	this.info.reloadDocument(this.doc);
    }

    private void updateSolution() {
	if (this.solution == null) {
	    System.out.println("NullPointer: solution ist null!");
	    return;
	}

	// remove all childnodes
	while (this.solution.hasChildNodes())
	    this.solution.removeChild(this.solution.getLastChild());

	LOPSolution sol = this.lop.getSolution();

	Vector3Frac solVec = this.lop.getSolution().getVector();

	this.solution.appendChild(createNode("b", Lang
		.getString("Strings.Solution")
		+ ":"));
	this.solution.appendChild(createNode("br"));
	this.solution.appendChild(createNode("br"));

	if (sol.getSpecialCase() != LOPSolution.NO_SOLUTION) {
	    this.solution.appendChild(createTextNode("z = "));

	    if (sol.getSpecialCase() == LOPSolution.UNLIMITED)
		this.solution.appendChild(createTextNode("unendlich"));
	    else
		this.solution.appendChild(createTextNode(solVec.getCoordZ()));

	    this.solution.appendChild(createTextNode(" x"));
	    this.solution.appendChild(createNode("sub", "1"));
	    this.solution.appendChild(createTextNode(" = " + solVec.getCoordX()
		    + " x"));
	    this.solution.appendChild(createNode("sub", "2"));
	    this.solution
		    .appendChild(createTextNode(" = " + solVec.getCoordY()));
	    this.solution.appendChild(createNode("br"));
	    this.solution.appendChild(createNode("br"));
	}

	switch (this.lop.getSolution().getSpecialCase()) {
	    case LOPSolution.NO_SOLUTION:
		this.solution.appendChild(createTextNode(Lang
			.getString("Strings.NoSolution")));
		break;
	    case LOPSolution.MORE_THAN_ONE_SOLUTION:
		this.solution.appendChild(createTextNode(Lang.getString(
			"Strings.MoreSolutions").replace("{0}",
			this.lop.getSolution().countAreas() + "")));
		break;
	    case LOPSolution.UNLIMITED:
		this.solution.appendChild(createTextNode(Lang
			.getString("Strings.UnlimitedSol")));
		break;

	    default:
		break;
	}

	this.info.reloadDocument(this.doc);
	// IOUtils.print(doc);
    }

}
