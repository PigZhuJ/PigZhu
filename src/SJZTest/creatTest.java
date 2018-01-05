package SJZTest;

import org.dom4j.DocumentException;
import org.dom4j.Element;

import java.io.IOException;
import java.util.ArrayList;

public class creatTest {
    public static void main(String[] args) throws IOException, DocumentException {
        creatxml c = new creatxml();
        ParseXML p = new ParseXML();
        ArrayList<Element> nodeElementList = p.getNodeElement(p.getElementList());
        ArrayList<Element> linkElementList = p.getLinkElement(p.getElementList());
        c.creatpzxyxml(nodeElementList, linkElementList);
        System.out.println("completed!");
        c.creatgzxyxml(linkElementList);
        System.out.println("finished!");
        c.creatxnsbxml();
        System.out.println("over!");
    }
}
