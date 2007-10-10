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
import info.kriese.soPra.lop.LOP;
import info.kriese.soPra.math.Fractional;
import info.kriese.soPra.math.Vector3Frac;
import info.kriese.soPra.math.impl.FractionalFactory;
import info.kriese.soPra.math.impl.Vector3FracFactory;

import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.SAXException;

/**
 * 
 * @author Michael Kriese
 * @version 0.3.2.1
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
		newOps[i] = "\"equal\"";
	    else if (ops[i].equals("<"))
		newOps[i] = "\"less\"";
	    else if (ops[i].equals(">"))
		newOps[i] = "\"greater\"";

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
	    System.err.println(e);
	    e.printStackTrace();
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
	} catch (SAXException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	} catch (ParserConfigurationException e) {
	    e.printStackTrace();
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
	    System.err.println(e);
	    e.printStackTrace();
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
	    System.err.println(e);
	    e.printStackTrace();
	}
	return null;
    }

    /**
     * Findet eine Datei und gibt die URL zurueck.
     * 
     * @param file
     * @return
     */
    public static URL getURL(String file) {
	URL res = SoPraLOP.class.getResource(file);
	// System.out.println("Url: " + res);
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
	    System.err.println(e);
	    e.printStackTrace();
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
	    System.out.println("Transformer configuration error: "
		    + e.getMessage());
	}
	DOMSource source = new DOMSource(doc);
	StreamResult result = new StreamResult(System.out);
	try {
	    transformer.transform(source, result);
	} catch (TransformerException e) {
	    System.out.println("Error transform: " + e.getMessage());
	}
	System.out.println();
	System.out
		.println("---------------------------------------------------------------------------");
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
