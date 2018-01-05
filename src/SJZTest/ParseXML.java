package SJZTest;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.ArrayList;
import java.util.List;

/**
 * Parse 320 xml文件
 */
public class ParseXML {
    /**
     * get elementList
     * @return
     * @throws DocumentException
     */
    public List<Element> getElementList() throws DocumentException {
        SAXReader reader=new SAXReader();
        Document document=reader.read("test320.xml");//获取test320.xml的文件
        Element root=document.getRootElement();//获取根节点
        List<Element> elementsList=root.elements();//获取所有的子节点
        return  elementsList;
    }
    /**
     * getNodeElement ArrayList
     */
    public ArrayList<Element> getNodeElement(List<Element> elementsList) throws DocumentException {
        ArrayList<Element> arrayNodeList=new ArrayList<>();//save NodeElement
//        System.out.println(elementsList.size());
//        get nodeelement
        for (int i=0;i<elementsList.size();i++){

            if((elementsList.get(i).attribute("class").getValue()).equals("twaver.Node")){
                arrayNodeList.add(elementsList.get(i));
            }
        }
//        System.out.println(arrayList.size()); finshed get Node
        return arrayNodeList;
    }
    /**
     * getLinkElement ArrayList
     */
    public ArrayList<Element> getLinkElement(List<Element> elementsList) throws DocumentException {

        ArrayList<Element> arrayLinkList=new ArrayList<>();//save NodeElement
//        System.out.println(elementsList.size());
//        get nodeelement
        for (int i=0;i<elementsList.size();i++){

            if((elementsList.get(i).attribute("class").getValue()).equals("twaver.Link")){
                arrayLinkList.add(elementsList.get(i));
            }
        }
        System.out.println(arrayLinkList.size()); //finshed get Node
        return arrayLinkList;
    }

    /**
     * get Nodename
     * @param Node
     * @return
     */
    public String getNodeName(Element Node){
        List<Element> childElement=Node.elements();
        String name=childElement.get(11).element("string").getTextTrim();
        return name;
    }

    /**
     * get Node IP
     * @param Node
     * @return
     */
    public String getIP(Element Node){
        List<Element> childElement=Node.elements();
        List<Element> list=childElement.get(3).elements();
        String IP=list.get(1).getTextTrim();
        return IP;
    }

    /**
     * get some  child link's children node
     * @param Link
     * @return
     */
    public List<Element> getLinkChild(Element Link){
        List<Element> list=Link.elements();
        return list;
    }

    public String getLinkID(Element link){
        List<Element> childElement=link.elements();
        String linkID=childElement.get(0).getTextTrim();
        return  linkID;
    }

    /**
     * get link name
     * @param list
     * @return
     */
    public String getLinkName(List<Element> list){
        String linkname=list.get(3).element("string").getTextTrim();
        return linkname;
    }

    /**
     * get Width
     * @param link
     * @return
     */
    public String getLinkWidth(Element link){
        List<Element> childElement=link.elements();
        String Width=childElement.get(8).element("int").getTextTrim();
        return Width;
    }

    /**
     * get startNode
     * @param link
     * @return
     */
    public String getStartNode(Element link){
        String startNode=link.elements().get(1).attribute("idref").getValue();
        return startNode;
    }
    /**
     * get endNode
     * @param link
     * @return
     */
    public String getEndNode(Element link){
        String endNode=link.elements().get(2).attribute("idref").getValue();
        return endNode;
    }

}
