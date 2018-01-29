package Ason;

import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class linkInfo {
    public static void main(String[] args) throws Exception {
//        SAXReader reader=new SAXReader();
//        Document document=reader.read("test320.xml");//获取test320.xml的文件
//        Element root=document.getRootElement();//获取根节点
//        List<Element> elementsList=root.elements();//获取所有的子节点
//      // System.out.println(elementsList.size());
//        ArrayList<Element> linkInfoList=new ArrayList<>();
//        for (int i=0;i<elementsList.size();i++){
//            if(elementsList.get(i).attribute("class").getValue().equals("twaver.Link")){
//                linkInfoList.add(elementsList.get(i));
//            }
//        }
//        //System.out.println(linkInfoList.size());
//
//        Document getDocument = DocumentHelper.createDocument();
//        Element getRoot = getDocument.addElement("LinkInfomation");
//        for (int j=0;j<linkInfoList.size();j++){
//            Element element=getRoot.addElement("linkInfo").addAttribute("linkID",linkInfoList.get(j).elements().get(0).getTextTrim())
//                    .addAttribute("LinkStatus","true");
//        }
//        /*输出*/
//        OutputFormat outputFormat = OutputFormat.createPrettyPrint();
//        outputFormat.setEncoding("utf-8");//设置按格式输出
//        outputFormat.setNewlines(true);
//        XMLWriter xmlWriter = new XMLWriter(new FileWriter("linkInfomation.xml"), outputFormat);
//        xmlWriter.write(getDocument);//写入文件
//        xmlWriter.close();//关闭
//        System.out.println("close");

            linkInfo link=new linkInfo();
            link.initLinkInfoStatus();
            link.getAllLinkStatus();
            boolean a=link.isExistedInLinkXML("1_10");
            System.out.println(a);
    }

    public void getAllLinkStatus() throws DocumentException{
        //1.获取根节点
        SAXReader reader=new SAXReader();
        Document document=reader.read("linkInfomation.xml");
        Element root=document.getRootElement();
        HashMap<String,String> LinkStatusMap=new HashMap<String,String>();
        //2.HashMap里面存储所有的链路状态
        List<Element> list=root.elements();
        for(int i=0;i<list.size();i++){
            String linkID=list.get(i).attributeValue("linkID");
            String linkStatus=list.get(i).attributeValue("LinkStatus");
            LinkStatusMap.put(linkID,linkStatus);

        }
       Iterator it=LinkStatusMap.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry entry= (Map.Entry) it.next();
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
        }
        }

    public boolean isExistedInLinkXML(String LinkID) throws DocumentException {
        //1.获取根节点
        SAXReader reader = new SAXReader();
        Document document = reader.read("linkInfomation.xml");
        Element root = document.getRootElement();
        HashMap<String, String> LinkStatusMap = new HashMap<String, String>();
        //2.获取linkID对应的Element
        List<Element> list = root.elements();
        ArrayList<String> linkIDArrayList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            linkIDArrayList.add(list.get(i).attributeValue("linkID"));
        }
        return linkIDArrayList.contains(LinkID);
    }
    public void initLinkInfoStatus() throws DocumentException, IOException{
        //1.获取根节点
        SAXReader reader=new SAXReader();
        Document document=reader.read("linkInfomation.xml");
        Element root=document.getRootElement();
        //2.获取底下的所有子节点
        List<Element> linkList=root.elements();
        //3.逐个修改为false
        for (int i = 0; i <linkList.size() ; i++) {
            Attribute attribute=linkList.get(i).attribute("LinkStatus");
            attribute.setValue("false");
        }
        //4.写入文件
        OutputFormat outputFormat = OutputFormat.createPrettyPrint();
        outputFormat.setEncoding("utf-8");//设置按格式输出
        outputFormat.setNewlines(true);
        XMLWriter xmlWriter = new XMLWriter(new FileWriter("linkInfomation.xml"), outputFormat);
        xmlWriter.write(root);//写入文件
        xmlWriter.close();//关闭
    }
//    public void setLinkStatus(String linkID,String status) throws DocumentException, IOException {
//        //1.获取根节点
//        SAXReader reader=new SAXReader();
//        Document document=reader.read("linkInfomation.xml");
//        Element root=document.getRootElement();
//        //2.获取linkID对应的Element
//        List<Element> list=root.elements();
//        for (int i = 0; i <list.size() ; i++) {
//            if(list.get(i).attribute("linkID").getValue().equals(linkID)){
//                list.get(i).attribute("LinkStatus").setValue(status);
//            }
//        }
//        //4.写入文件
//        OutputFormat outputFormat = OutputFormat.createPrettyPrint();
//        outputFormat.setEncoding("utf-8");//设置按格式输出
//        outputFormat.setNewlines(true);
//        XMLWriter xmlWriter = new XMLWriter(new FileWriter("linkInfomation.xml"), outputFormat);
//        xmlWriter.write(root);//写入文件
//        xmlWriter.close();//关闭
//    }
//
//
//    public int[][] getLinkStatusMatrix() throws Exception{
//        //1.获取链路状态
//        int[][] linkStatusMatrix=new int[320][320];//320个节点 所以矩阵是320x320的矩阵
//        for(int i=0;i<320;i++){
//            for(int j=0;j<320;j++){
//                String link=i+"_"+j;//链路1
//                //判断一个链路是否存在linkInfomation.xml之中
//                System.out.println(link);
//                if(isExistedInLinkInfomationXml(link)){
//                    linkStatusMatrix[i][j]=1;//存在就是1
//                }else{
//                    linkStatusMatrix[j][i]=0;//不存在就是0
//                }
//            }
//        }
//
//        return null;
//
//    }
//    /**
//     * 判断linkInfomation.xml中是否包含相应的链路信息
//     * @param linkID  链路ID
//     * @return  包含就返回true  不包含就返回false
//     * @throws Exception  抛出异常
//     */
//    public boolean isExistedInLinkInfomationXml(String linkID) throws Exception{
//        //1.获取根节点
//        SAXReader reader=new SAXReader();
//        Document document=reader.read("linkInfomation.xml");
//        Element root=document.getRootElement();
//        //2.将所有的LinkID
//        ArrayList<String> linkInfoArrayList=new ArrayList<String>();//存储所有的linkID
//        List<Element> linkList=root.elements();
//        for(int i=0;i<linkList.size();i++){
//            String linkIDInfo=linkList.get(i).attribute("linkID").getValue();
//            linkInfoArrayList.add(linkIDInfo);
//        }
//        return linkInfoArrayList.contains(linkID);
//    }
    }



