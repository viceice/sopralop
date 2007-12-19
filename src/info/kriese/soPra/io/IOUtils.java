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
 * 19.12.2007 - Version 0.3.5
 * - Auf neues ExceptionHandling umgestellt
 * - DebugAusgabe optimiert
 * 17.12.2007 - version 0.3.4
 * - Unnötige Ausgaben entfernt
 * - Ausgabe der Lösungsflächen hinzugefügt
 * 08.11.2007 - Version 0.3.3.2
 * - BugFix: NullPointer in print behoben (falls lop nicht gelöst)
 * 04.11.2007 - Version 0.3.3.1
 * - BugFix: Doppelte Anführungszeichen bei den Operatoren entfernt.
 * 09.10.2007 - Version 0.3.3
 * - Fehler in getURL behoben
 * - Lösung des LOP nicht mehr speichern
 * 03.10.2007 - Version 0.3.2
 * - Fehler beim Speichern des Problems behoben (ich habe die Quotes vergessen,
 * 	dadurch kam es zu ungültigem XML-Code)
 * - XML-Encoding-Tag gesetzt
 * 24.09.2007 - Version 0.3.1
 * - An verändertes Speicherformat angepasst
 * 23.08.2007 - Version 0.3
 * - Weitere Methoden hinzugefuegt
 * 30.07.2007 - Version 0.2
 * - Weitere Methoden hinzugefuegt
 * 29.07.2007 - Version 0.1
 *  - Datei hinzugefuegt
 */
package info.kriese.soPra.io;

import info.kriese.soPra.SoPraLOP;
import info.kriese.soPra.gui.MessageHandler;
import info.kriese.soPra.gui.lang.Lang;
import info.kriese.soPra.lop.LOP;
import info.kriese.soPra.lop.LOPSolution;
import info.kriese.soPra.lop.LOPSolutionArea;
import info.kriese.soPra.math.Fractional;
import info.kriese.soPra.math.Vector3Frac;
import info.kriese.soPra.math.impl.FractionalFactory;
import info.kriese.soPra.math.impl.Vector3FracFactory;

import java.io.PrintStream;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;

/**
 * 
 * @author Michael Kriese
 * @version 0.3.5
 * @since 29.07.2007
 * 
 */
public final class IOUtils {

    public static URL BASE_URL = getURL("gui/html/tmpl.html");

    /**
     * generieren des xml-Content fuer das Speichern der Daten
     * 
     */
    public static String generateXMLContent(LOP lop) {

	StringBuilder out = new StringBuilder();

	// xml-Header
	out.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n<lop type=\""
		+ (lop.isMaximum() ? "max" : "min") + "\">\n");

	String[] newOps = new String[2], ops = lop.getOperators();
	for (int i = 0; i < 2; i++)
	    if (ops[i].equals("="))
		newOps[i] = "equal";
	    else if (ops[i].equals("<"))
		newOps[i] = "less";
	    else if (ops[i].equals(">"))
		newOps[i] = "greater";

	// die Vektordaten der Variablen xi
	out.append("<vectors>\n");
	for (Vector3Frac vec : lop.getVectors())
	    out.append("  <vector x=\"" + vec.getCoordX() + "\" y=\""
		    + vec.getCoordY() + "\" z=\"" + vec.getCoordZ()
		    + "\"  />\n");
	out.append("</vectors>\n");

	// die Vektordaten der Zielfunktion
	Vector3Frac target = lop.getTarget();
	out.append("<target  x=\"" + target.getCoordX() + "\" y=\""
		+ target.getCoordY() + "\" z=\"" + target.getCoordZ()
		+ "\" />\n");

	// die Operatoren
	out.append("<ops x=\"" + newOps[0] + "\" y=\"" + newOps[1] + "\" />\n");

	// schliessendes xml-Tag
	out.append("</lop>\n");

	return out.toString();
    }

    public static int getCase(NamedNodeMap map) {
	if (map == null)
	    return -1;

	try {
	    return Integer.parseInt(map.getNamedItem("case").getNodeValue());
	} catch (Exception e) {
	    MessageHandler.exceptionThrown(e);
	}
	return -1;
    }

    public static Document getDocumentTemplate() {
	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	DocumentBuilder builder;

	Document doc = null;
	try {
	    builder = factory.newDocumentBuilder();
	    doc = builder.parse(BASE_URL.openStream());
	    doc.setDocumentURI(BASE_URL.toString());
	} catch (Exception e) {
	    MessageHandler.exceptionThrown(e);
	}

	return doc;
    }

    /**
     * Extrahiert die Operatoren aus den xml-Daten.
     * 
     * @param map
     * @return
     */
    public static String[] getOperators(NamedNodeMap map) {
	if (map == null)
	    return null;

	try {
	    String[] res = new String[2];
	    res[0] = getOperator(map.getNamedItem("x").getNodeValue());
	    res[1] = getOperator(map.getNamedItem("y").getNodeValue());

	    if (res[0] != null && res[1] != null)
		return res;
	} catch (Exception e) {
	    MessageHandler.exceptionThrown(e);
	}
	return null;
    }

    public static Fractional getSol(NamedNodeMap map) {
	if (map == null)
	    return null;

	try {
	    return FractionalFactory.getInstance(map.getNamedItem("value")
		    .getNodeValue());
	} catch (Exception e) {
	    MessageHandler.exceptionThrown(e);
	}
	return null;
    }

    /**
     * Findet eine Datei und gibt die URL zurück.
     * 
     * @param file
     * @return
     */
    public static URL getURL(String file) {
	URL res = SoPraLOP.class.getResource(file);
	return res;
    }

    /**
     * Extrahiere einen Vector aus den xml-Daten.
     * 
     * @param map -
     *                NamedNodeMap aus xml-Daten
     * @return einen Vector3Frac
     */
    public static Vector3Frac getVector(NamedNodeMap map) {
	if (map == null)
	    return null;

	try {
	    Fractional x, y, z;
	    x = FractionalFactory.getInstance(map.getNamedItem("x")
		    .getNodeValue());
	    y = FractionalFactory.getInstance(map.getNamedItem("y")
		    .getNodeValue());
	    z = FractionalFactory.getInstance(map.getNamedItem("z")
		    .getNodeValue());

	    if (x != null && y != null && z != null)
		return Vector3FracFactory.getInstance(x, y, z);
	} catch (Exception e) {
	    MessageHandler.exceptionThrown(e);
	}
	return null;
    }

    public static void print(Document doc) {
	System.out
		.println("---------------------------------------------------------------------------");
	Transformer transformer = null;
	TransformerFactory transformerFactory = TransformerFactory
		.newInstance();
	try {
	    transformer = transformerFactory.newTransformer();
	} catch (TransformerConfigurationException e) {
	    MessageHandler.exceptionThrown(e);
	}
	DOMSource source = new DOMSource(doc);
	StreamResult result = new StreamResult(System.out);
	try {
	    transformer.transform(source, result);
	} catch (TransformerException e) {
	    MessageHandler.exceptionThrown(e);
	}
	System.out.println();
	System.out
		.println("---------------------------------------------------------------------------");
    }

    /**
     * Dient zur Ausgabe eines LOP.
     * 
     * @param lop -
     *                LOP, welches ausgegeben werden soll
     * @param out -
     *                PrintStream, auf dem das LOP ausgegeben werden soll.
     */
    public static void print(LOP lop, PrintStream out) {
	StringBuffer x = new StringBuffer(), y = new StringBuffer(), z = new StringBuffer();

	Vector3Frac vec = lop.getVectors().get(0);

	x.append(vec.getCoordX() + " ");
	y.append(vec.getCoordY() + " ");
	z.append(vec.getCoordZ() + " ");

	for (int i = 1; i < lop.getVectors().size(); i++) {
	    vec = lop.getVectors().get(i);
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

	vec = lop.getTarget();

	x.append(" " + lop.getOperators()[0] + " " + vec.getCoordX());
	y.append(" " + lop.getOperators()[1] + " " + vec.getCoordY());
	z.append(" = " + (lop.isMaximum() ? "max" : "min"));

	out.println(x.toString());
	out.println(y.toString());
	out.println(z.toString());

	if (!lop.isSolved()) {
	    System.out.println("LOP is not solved.");
	    return;
	}

	LOPSolution sol = lop.getSolution();

	out.println();
	out.println("Lösung: " + sol.getValue());

	switch (sol.getSpecialCase()) {
	    case LOPSolution.NO_SOLUTION:
		out.println(Lang.getString("Strings.NoSolution"));
		break;
	    case LOPSolution.MORE_THAN_ONE_SOLUTION:
		out.println(Lang.getString("Strings.MoreSolutions",
			new Object[] { sol.countAreas() }));
		out.println(sol.getAreas());
		break;
	    case LOPSolution.UNLIMITED:
		out.println(Lang.getString("Strings.UnlimitedSol"));
		break;
	    case LOPSolution.SIMPLE:
		out.println(Lang.getString("Strings.SimpleSolution"));
		break;
	    default:
		out.println("Error! Wrong special case. ("
			+ sol.getSpecialCase() + ")");
		break;
	}

	for (LOPSolutionArea area : lop.getSolution().getAreas())
	    out.println("[ X" + (lop.getVectors().indexOf(area.getL1()) + 1)
		    + ", X" + (lop.getVectors().indexOf(area.getL2()) + 1)
		    + " ] = [ " + area.getL1Amount() + ", "
		    + area.getL2Amount() + " ]");

	out.println();
    }

    /**
     * Extrahiert einen Operator aus dem String
     * 
     * @param op
     * @return
     */
    private static String getOperator(String op) {

	if (op.equals("equal"))
	    return "=";
	else if (op.equals("less"))
	    return "<";
	else if (op.equals("greater"))
	    return ">";

	return null;
    }
}
