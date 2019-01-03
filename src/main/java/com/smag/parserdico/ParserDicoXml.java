package com.smag.parserdico;

import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ParserDicoXml {

    public static void main(String[] args) {
        long duration=0;
        Document document = XmlManager.getDocument("src/main/java/xmlFiles/dataset.xml");

        long nanoTime = System.nanoTime();
        System.out.println("Retrieve entryList started");
        NodeList nodes = XmlManager.retrieveEntryList(document, "entry");
        long elapsedTime = System.nanoTime() - nanoTime;
        double seconds = elapsedTime / 1_000_000_000.0; //convert nanoseconds to seconds
        System.out.println("EntryList size : " + nodes.getLength() + "retrieve at : " + seconds + " seconds");
        duration+=seconds;
        
        System.out.println("Retrieve retrieveInflectedList started");
        nanoTime = System.nanoTime();
        List<Element> wordList = XmlManager.retrieveInflectedList(nodes);
        elapsedTime = System.nanoTime() - nanoTime;
        seconds = elapsedTime / 1_000_000_000.0; //convert nanoseconds to seconds
        System.out.println("retrieveInflectedList size : " + wordList.size() + "retrieve at : " + seconds + " seconds");
        duration+=seconds;
        
        System.out.println("Retrieve retrieveInflectedList started");
        nanoTime = System.nanoTime();
        List<Element> retrieveInflectedInlineList = XmlManager.retrieveInflectedInlineList(wordList);
        elapsedTime = System.nanoTime() - nanoTime;
        seconds = elapsedTime / 1_000_000_000.0; //convert nanoseconds to seconds
        System.out.println("retrieveInflectedInlineList size : " + retrieveInflectedInlineList.size() + "retrieve at : " + seconds + " seconds");
        duration+=seconds;
        
        System.out.println("Start storage!");
        nanoTime = System.nanoTime();
        XmlManager.storeElementInXml(retrieveInflectedInlineList);
        elapsedTime = System.nanoTime() - nanoTime;
        seconds = elapsedTime / 1_000_000_000.0; //convert nanoseconds to seconds
        System.out.println("Finished storage at : " + seconds + " seconds");
        duration+=seconds;
        
         System.out.println("Parsing and Store duration : "+duration);
    }
}
