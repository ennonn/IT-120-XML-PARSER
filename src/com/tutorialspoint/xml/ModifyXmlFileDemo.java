package com.tutorialspoint.xml;

import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class ModifyXmlFileDemo {

    public static void main(String argv[]) {

        try {
            File inputFile = new File("src/com/tutorialspoint/xml/cars.xml");
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            docFactory.setIgnoringElementContentWhitespace(true);
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(inputFile);

            // Manually remove empty text nodes
            removeEmptyTextNodes(doc);

            Node cars = doc.getFirstChild();
            Node supercar = doc.getElementsByTagName("supercars").item(0);

            // update supercar attribute
            NamedNodeMap attr = supercar.getAttributes();
            Node nodeAttr = attr.getNamedItem("company");
            nodeAttr.setTextContent("Lamborigini");

            // loop the supercar child node
            NodeList list = supercar.getChildNodes();

            for (int temp = 0; temp < list.getLength(); temp++) {
                Node node = list.item(temp);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;
                    if ("carname".equals(eElement.getNodeName())) {
                        if ("Ferrari 101".equals(eElement.getTextContent())) {
                            eElement.setTextContent("Lamborigini 001");
                        }
                        if ("Ferrari 202".equals(eElement.getTextContent()))
                            eElement.setTextContent("Lamborigini 002");
                    }
                }
            }

            // Add at least 5 new data nodes
            for (int i = 1; i <= 5; i++) {
                Element newCarName = doc.createElement("carname");
                Attr attrType = doc.createAttribute("type");
                attrType.setValue("type" + i);
                newCarName.setAttributeNode(attrType);
                newCarName.appendChild(doc.createTextNode("Lamborigini " + (i + 2)));
                supercar.appendChild(newCarName);
            }

            // Format the XML data with indentation
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            DOMSource source = new DOMSource(doc);
            System.out.println("-----------Modified File-----------");
            StreamResult consoleResult = new StreamResult(System.out);
            transformer.transform(source, consoleResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Helper method to remove empty text nodes
    private static void removeEmptyTextNodes(Node node) {
        NodeList childNodes = node.getChildNodes();
        for (int i = childNodes.getLength() - 1; i >= 0; i--) {
            Node child = childNodes.item(i);
            if (child.getNodeType() == Node.TEXT_NODE && child.getNodeValue().trim().isEmpty()) {
                node.removeChild(child);
            } else if (child.getNodeType() == Node.ELEMENT_NODE) {
                removeEmptyTextNodes(child);
            }
        }
    }
}
