/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smag.parserdico;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * oumygueye939
 *
 * @author smag
 */
public class XmlManager {

    public static Document getDocument(String location) {

        Document doc = null;
        try {
            File inputFile = new File(location);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return doc;
    }

    public static NodeList retrieveEntryList(Document document, String tag) {
        Set<String> wordSet = new HashSet();
        NodeList elementsByTagName = document.getElementsByTagName(tag);
        return elementsByTagName;
    }

    public static List<Element> retrieveInflectedList(NodeList nodes) {
        List<Element> retrieveInflected = new ArrayList<>();
        for (int i = 0; i < 5000; i++) {
            Element item = (Element) nodes.item(i);
            retrieveInflected.addAll(retrieveInflected(item));
            System.out.println("RetrieveInflectedList CW SIZE: " + i);
        }
        return retrieveInflected;
    }

    public static List<Element> retrieveInflectedInlineList(List<Element> retrieveInflectedList) {
        List<Element> retrieveInflectedInlineList = new ArrayList<>();
        for (Element inflected : retrieveInflectedList) {
            retrieveInflectedInlineList.add(inlineInflected(inflected));
            System.out.println("Word inflectedInline : " + inflected.getAttribute("value"));
        }
        return retrieveInflectedInlineList;
    }

    public static Element inlineInflected(Element e) {
        e.setAttribute("value", e.getElementsByTagName("form").item(0).getTextContent());
        e.removeChild(e.getElementsByTagName("form").item(0));

        NodeList feats = e.getElementsByTagName("feat");
        for (int i = 0; i < feats.getLength(); i++) {
            Element feat = (Element) feats.item(i);
            e.setAttribute(feat.getAttribute("name"), feat.getAttribute("value"));
            e.removeChild(feats.item(i));
        }
        return e;
    }

    public static boolean storeElementInXml(List<Element> retrieveInflectedInline) {
        Document doc_dictionary = null;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db;
            db = dbf.newDocumentBuilder();
            doc_dictionary = db.parse("src/main/java/xmlFiles/dictionary.xml");

            for (Element n : retrieveInflectedInline) {
                // Create a duplicate node
                n.normalize();
                Node newNode = n.cloneNode(true);
                // Transfer ownership of the new node into the destination document
                doc_dictionary.adoptNode(newNode);
                // Make the new node an actual item in the target document
                doc_dictionary.getDocumentElement().appendChild(newNode);
                //root.appendChild(newNode);
                doc_dictionary.normalize();

            }

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            Result output = new StreamResult(new File("src/main/java/xmlFiles/ouput.xml"));
            Source input = new DOMSource(doc_dictionary);

            transformer.transform(input, output);

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(doc_dictionary.getElementsByTagName("inflected").getLength());
        return true;
    }

    private static List<Element> retrieveInflected(Element entry) {
        List<Element> retrieveInflected = new ArrayList<>();
        String pos = ((Element) entry.getElementsByTagName("pos").item(0)).getAttribute("name");
        String lemma = entry.getElementsByTagName("lemma").item(0).getTextContent();
        NodeList inflectedList = entry.getElementsByTagName("inflected");

        for (int i = 0; i < inflectedList.getLength(); i++) {
            Element inflected = (Element) inflectedList.item(i);
            if(pos.equals("verb")){
                inflected.setAttribute("inf", lemma);
            }
            retrieveInflected.add(inflected);
        }
        return retrieveInflected;
    }

}
