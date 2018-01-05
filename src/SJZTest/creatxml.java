package SJZTest;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

/**
 * 配置响应
 */
public class creatxml {

    ParseXML p = new ParseXML();

    /**
     * creat peizhi xiangying xml file
     *
     * @param nodeElementsList get nodeElement list
     * @param linkElementList  get linkElement list
     * @throws IOException
     */
    public void creatpzxyxml(ArrayList<Element> nodeElementsList, ArrayList<Element> linkElementList) throws IOException {

        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("Scenario");//add root Element
        root.addAttribute("ID", "GUID");
        /*****1.add child element*****/
        Element element0 = root.addElement("Generic");//add root child0 Element
        element0.addAttribute("QueryType", "3").addAttribute("NetID", "2");//add Generic attribute
        Element element1 = root.addElement("NetDevices");//add root child1 Element
        element1.addAttribute("BeginIndex", "0").addAttribute("EndIndex", "100").addAttribute("Total", "101");
        Element element2 = root.addElement("Links");//add root child2 Element
        element2.addAttribute("BeginIndex", "0").addAttribute("EndIndex", "100").addAttribute("Total", "101");

            /*2.add NetDevices child Element*/
//        Element element11 = element1.addElement("NetDevice");
//        //add element11 child element
//        Element element111 = element11.addElement("info");
//        element111.addAttribute("Name", "NodeID").addAttribute("Value", "节点ID");//@TODO need to change value
//        Element element112 = element11.addElement("info");
//        element112.addAttribute("Name", "IpAddress").addAttribute("Value", "设备网管ip地址");//@TODO need to change value
//        Element element113 = element11.addElement("info");
//        element113.addAttribute("Name", "MaskLength").addAttribute("Value", "设备网管掩码地址");//@TODO need to change value
//
//        Element element12 = element1.addElement("NetDevice");
//        //add element12 child element
//        Element element121 = element12.addElement("info");
//        element121.addAttribute("Name", "NodeID").addAttribute("Value", "节点ID");//@TODO need to change value
//        Element element122 = element12.addElement("info");
//        element122.addAttribute("Name", "IpAddress").addAttribute("Value", "设备网管ip地址");//@TODO need to change value
//        Element element123 = element12.addElement("info");
//        element123.addAttribute("Name", "MaskLength").addAttribute("Value", "设备网管掩码地址");//@TODO need to change value

        /*****2.add NetDevices child Element*****/
        for (int i = 0; i < nodeElementsList.size(); i++) {
            Element element12 = element1.addElement("NetDevice");
            //add element12 child element
            Element element121 = element12.addElement("info");
            element121.addAttribute("Name", "NodeID").addAttribute("Value", "Node" + (i + 1));
            Element element122 = element12.addElement("info");
            element122.addAttribute("Name", "IpAddress").addAttribute("Value", p.getIP(nodeElementsList.get(i)));
            Element element123 = element12.addElement("info");
            element123.addAttribute("Name", "MaskLength").addAttribute("Value", "255.255.255.0");
        }

//            /*add Link child Element*/
//        Element element21 = element2.addElement("Link");
//        //add element21 child element
//        Element element211 = element21.addElement("info");
//        element211.addAttribute("Name", "ID").addAttribute("Value", "唯一标识");//@TODO need to change value
//        Element element212 = element21.addElement("info");
//        element212.addAttribute("Name", "Bandwidth").addAttribute("Value", "带宽");//@TODO need to change value
//        Element element213 = element21.addElement("info");
//        element213.addAttribute("Name", "BusinessType").addAttribute("Vlaue", "业务类型");//@TODO need to change value
//        Element element214 = element21.addElement("info");
//        element214.addAttribute("Name", "LinkStatus").addAttribute("Value", "状态");//@TODO need to change value
//        Element element215 = element21.addElement("info");
//        element215.addAttribute("Name", "StartNode").addAttribute("Value", "起始节点ID");//@TODO need to change value
//        Element element216 = element21.addElement("info");
//        element216.addAttribute("Name", "DestNode").addAttribute("Value", "目的节点ID");//@TODO need to change value
//
//
//        Element element22 = element2.addElement("Link");
//        //add element22 child element
//        Element element221 = element22.addElement("info");
//        element221.addAttribute("Name", "ID").addAttribute("Value", "唯一标识");//@TODO need to change value
//        Element element222 = element22.addElement("info");
//        element222.addAttribute("Name", "Bandwidth").addAttribute("Value", "带宽");//@TODO need to change value
//        Element element223 = element22.addElement("info");
//        element223.addAttribute("Name", "BusinessType").addAttribute("Vlaue", "业务类型");//@TODO need to change value
//        Element element224 = element22.addElement("info");
//        element224.addAttribute("Name", "LinkStatus").addAttribute("Value", "状态");//@TODO need to change value
//        Element element225 = element22.addElement("info");
//        element225.addAttribute("Name", "StartNode").addAttribute("Value", "起始节点ID");//@TODO need to change value
//        Element element226 = element22.addElement("info");
//        element226.addAttribute("Name", "DestNode").addAttribute("Value", "目的节点ID");//@TODO need to change value

        /******add Link child Element******/
        for (int j = 0; j < linkElementList.size(); j++) {
            Element element21 = element2.addElement("Link");
            //add element21 child element
            Element element211 = element21.addElement("info");
            element211.addAttribute("Name", "ID").addAttribute("Value", p.getLinkID(linkElementList.get(j)));
            Element element212 = element21.addElement("info");
            element212.addAttribute("Name", "Bandwidth").addAttribute("Value", p.getLinkWidth(linkElementList.get(j)));
            Element element213 = element21.addElement("info");
            element213.addAttribute("Name", "BusinessType").addAttribute("Vlaue", "01");
            Element element214 = element21.addElement("info");
            element214.addAttribute("Name", "LinkStatus").addAttribute("Value", "01");
            Element element215 = element21.addElement("info");
            element215.addAttribute("Name", "StartNode").addAttribute("Value", p.getStartNode(linkElementList.get(j)));//@TODO need to change value
            Element element216 = element21.addElement("info");
            element216.addAttribute("Name", "DestNode").addAttribute("Value", p.getEndNode(linkElementList.get(j)));//@TODO need to change value
        }

        /**
         * 3.输出
         */
        OutputFormat outputFormat = OutputFormat.createPrettyPrint();
        outputFormat.setEncoding("utf-8");//设置按格式输出
        outputFormat.setNewlines(true);
        XMLWriter xmlWriter = new XMLWriter(new FileWriter("peizhixiangying.xml"), outputFormat);
        xmlWriter.write(document);//写入文件
        xmlWriter.close();//关闭
    }

    /**
     * 2.故障响应xml文件的创建方法
     */
    public void creatgzxyxml(ArrayList<Element> linkElementlist) throws IOException {
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("Scenario");//add root Element
        root.addAttribute("ID", "GUID");
            /*1.add child element*/
        Element element0 = root.addElement("Generic");//add root child0 Element
        element0.addAttribute("QueryType", "4").addAttribute("NetID", "2");//add Generic attribute
        Element element1 = root.addElement("NodeFaults");//add root child1 Element
        element1.addAttribute("BeginIndex", "0").addAttribute("EndIndex", "100").addAttribute("Total", "101");
        Element element2 = root.addElement("LinkFaults");//add root child2 Element
        element2.addAttribute("BeginIndex", "0").addAttribute("EndIndex", "100").addAttribute("Total", "101");
        Element element3 = root.addElement("BusinessFaults");//add root child2 Element
        element3.addAttribute("BeginIndex", "0").addAttribute("EndIndex", "100").addAttribute("Total", "101");

        /*add element1 child elements*/
        for (int i = 1; i < 11; i++) {

            Element element11 = element1.addElement("NodeFault");
            long beginTime = System.currentTimeMillis() + i * 50000;
            Element element111 = element11.addElement("info");
            element111.addAttribute("Name", "FaultID").addAttribute("Value", "01");//
            Element element112 = element11.addElement("info");
            element112.addAttribute("Name", "NodeID").addAttribute("Value", "Node" + (i * 5));//@TODO need to change value
            Element element113 = element11.addElement("info");
            element113.addAttribute("Name", "FaultType").addAttribute("Value", "01");
            Element element114 = element11.addElement("info");
            element114.addAttribute("Name", "FaultStatus").addAttribute("Value", "01");
            Element element115 = element11.addElement("info");
            element115.addAttribute("Name", "StartTime").addAttribute("Value", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(beginTime)));
            Element element116 = element11.addElement("info");
            element116.addAttribute("Name", "EndTime").addAttribute("Value", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(beginTime + 5000)));//@TODO need to change value
            Element element117 = element11.addElement("info");
            element117.addAttribute("Name", "FaultLink").addAttribute("Value", "id1,id2");//@TODO need to gai
            element117.addComment("故障影响的链路");
            Element element118 = element11.addElement("info");
            element118.addAttribute("Name", "Description").addAttribute("Value", "故障描述");//
            Element element119 = element11.addElement("info");
            element119.addAttribute("Name", "HandleResult").addAttribute("Value", "正在结局");//@TODO need to change value
        }
//
//        Element element12 = element1.addElement("NodeFault");
//
//        Element element121 = element12.addElement("info");
//        element121.addAttribute("Name", "FaultID").addAttribute("Value", "故障ID");//@TODO need to change value
//        Element element122 = element12.addElement("info");
//        element122.addAttribute("Name", "NodeID").addAttribute("Value", "故障节点ID");//@TODO need to change value
//        Element element123 = element12.addElement("info");
//        element123.addAttribute("Name", "FaultType").addAttribute("Value", "故障类型");//@TODO need to change value
//        Element element124 = element12.addElement("info");
//        element124.addAttribute("Name", "FaultStatus").addAttribute("Value", "故障状态");//@TODO need to change value
//        Element element125 = element12.addElement("info");
//        element125.addAttribute("Name", "StartTime").addAttribute("Value", "开始时间");//@TODO need to change value
//        Element element126 = element12.addElement("info");
//        element126.addAttribute("Name", "EndTime").addAttribute("Value", "结束时间");//@TODO need to change value
//        Element element127 = element12.addElement("info");
//        element127.addAttribute("Name", "FaultLink").addAttribute("Value", "id1,id2");//@TODO need to change value
//        element127.addComment("故障影响的链路");
//        Element element128 = element12.addElement("info");
//        element128.addAttribute("Name", "Description").addAttribute("Value", "故障描述");//@TODO need to change value
//        Element element129 = element12.addElement("info");
//        element129.addAttribute("Name", "HandleResult").addAttribute("Value", "故障处理结果");//@TODO need to change value

        /*add element2 child elements*/
        for (int i = 1; i < 11; i++) {
            long beginTime = System.currentTimeMillis() + i * 50000;
            Element element21 = element2.addElement("LinkFault");

            Element element211 = element21.addElement("info");
            element211.addAttribute("Name", "FaultID").addAttribute("Value", "01");
            Element element212 = element21.addElement("info");
            element212.addAttribute("Name", "NodeID").addAttribute("Value", p.getLinkID(linkElementlist.get(5)));
            Element element213 = element21.addElement("info");
            element213.addAttribute("Name", "FaultType").addAttribute("Value", "02");
            Element element214 = element21.addElement("info");
            element214.addAttribute("Name", "FaultStatus").addAttribute("Value", "01");
            Element element215 = element21.addElement("info");
            element215.addAttribute("Name", "StartTime").addAttribute("Value", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(beginTime)));//@TODO need to change value
            Element element216 = element21.addElement("info");
            element216.addAttribute("Name", "EndTime").addAttribute("Value", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(beginTime + 5000)));//@TODO need to change value
            Element element217 = element21.addElement("info");
            element217.addAttribute("Name", "FaultLink").addAttribute("Value", "故障关联性业务");//@TODO need to change
            Element element218 = element21.addElement("info");
            element218.addAttribute("Name", "Description").addAttribute("Value", "故障描述");
            Element element219 = element21.addElement("info");
            element219.addAttribute("Name", "HandleResult").addAttribute("Value", "正在处理");
        }
//        Element element22 = element2.addElement("LinkFault");
//
//        Element element221 = element22.addElement("info");
//        element221.addAttribute("Name", "FaultID").addAttribute("Value", "故障ID");//@TODO need to change value
//        Element element222 = element22.addElement("info");
//        element222.addAttribute("Name", "NodeID").addAttribute("Value", "故障链路ID");//@TODO need to change value
//        Element element223 = element22.addElement("info");
//        element223.addAttribute("Name", "FaultType").addAttribute("Value", "故障类型");//@TODO need to change value
//        Element element224 = element22.addElement("info");
//        element224.addAttribute("Name", "FaultStatus").addAttribute("Value", "故障状态");//@TODO need to change value
//        Element element225 = element22.addElement("info");
//        element225.addAttribute("Name", "StartTime").addAttribute("Value", "开始时间");//@TODO need to change value
//        Element element226 = element22.addElement("info");
//        element226.addAttribute("Name", "EndTime").addAttribute("Value", "结束时间");//@TODO need to change value
//        Element element227 = element22.addElement("info");
//        element227.addAttribute("Name", "FaultLink").addAttribute("Value", "故障关联性业务");//@TODO need to change value
//        Element element228 = element22.addElement("info");
//        element228.addAttribute("Name", "Description").addAttribute("Value", "故障描述");//@TODO need to change value
//        Element element229 = element22.addElement("info");
//        element229.addAttribute("Name", "HandleResult").addAttribute("Value", "故障处理结果");//@TODO need to change value


        /*add  element3 child Element*/
        for (int i = 1; i < 11; i++) {
            long beginTime = System.currentTimeMillis() + i * 50000;
            Element element31 = element3.addElement("BusinessFault");

            Element element311 = element31.addElement("info");
            element311.addAttribute("Name", "FaultID").addAttribute("Value", "01");
            Element element312 = element31.addElement("info");
            element312.addAttribute("Name", "BusinessType").addAttribute("Value", "ASON");
            Element element313 = element31.addElement("info");
            element313.addAttribute("Name", "FaultStatus").addAttribute("Value", "01");
            Element element314 = element31.addElement("info");
            element314.addAttribute("Name", "StartTime").addAttribute("Value", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(beginTime)));
            Element element315 = element31.addElement("info");
            element315.addAttribute("Name", "EndTime").addAttribute("Value", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(beginTime + 5000)));
            Element element316 = element31.addElement("info");
            element316.addAttribute("Name", "FaultLink").addAttribute("Value", "故障关联性业务");
            Element element317 = element31.element("info");
            element317.addAttribute("Name", "Description").addAttribute("Value", "故障描述");
            Element element318 = element31.addElement("info");
            element318.addAttribute("Name", "HandleResult").addAttribute("Value", "正在处理");
        }

//            Element element32 = element3.addElement("BusinessFault");
//
//            Element element321 = element32.addElement("info");
//            element321.addAttribute("Name", "FaultID").addAttribute("Value", "故障ID");
//            Element element322 = element32.addElement("info");
//            element322.addAttribute("Name", "BusinessType").addAttribute("Value", "业务类型");
//            Element element323 = element32.addElement("info");
//            element323.addAttribute("Name", "FaultStatus").addAttribute("Value", "故障状态");
//            Element element324 = element32.addElement("info");
//            element324.addAttribute("Name", "StartTime").addAttribute("Value", "开始时间");
//            Element element325 = element32.addElement("info");
//            element325.addAttribute("Name", "EndTime").addAttribute("Value", "结束时间");
//            Element element326 = element32.addElement("info");
//            element326.addAttribute("Name", "FaultLink").addAttribute("Value", "故障关联性业务");
//            Element element327 = element32.element("info");
//            element327.addAttribute("Name", "Description").addAttribute("Value", "故障描述");
//            Element element328 = element32.addElement("info");
//            element328.addAttribute("Name", "HandleResult").addAttribute("Value", "故障处理结果");

        /*输出*/
            OutputFormat outputFormat = OutputFormat.createPrettyPrint();
            outputFormat.setEncoding("utf-8");//设置按格式输出
            outputFormat.setNewlines(true);
            XMLWriter xmlWriter = new XMLWriter(new FileWriter("guzhangxiangying.xml"), outputFormat);
            xmlWriter.write(document);//写入文件
            xmlWriter.close();//关闭
        }

        /**
         * 性能上报 xml 表格创建
         */

    public void creatxnsbxml() throws IOException {
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("Scenario");//add root Element
        root.addAttribute("ID", "GUID");
            /*1.add child element*/
        Element element0 = root.addElement("Generic");//add root child0 Element
        element0.addAttribute("QueryType", "5").addAttribute("NetID", "2");//add Generic attribute
        Element element1 = root.addElement("Performances");//add root child1 Element
        element1.addAttribute("BeginIndex", "0").addAttribute("EndIndex", "100").addAttribute("Timestamp", "time");//@TODO need to add time
        for (int i = 1; i < 11; i++) {

            long beginTime = System.currentTimeMillis() + i * 50000;
            Element element11 = element1.addElement("Performance");

            Element element111 = element11.addElement("info");
            element111.addAttribute("Name", "BusinessID").addAttribute("Value", "0"+i);
            element11.addComment("通路建立时间 单位为毫秒");
            Element element112 = element11.addElement("info");
            element112.addAttribute("Name", "PathBuildTime").addAttribute("Value", String.valueOf(creatxml.getRandomNum1()));
            element11.addComment("通路保护到换时间 单位为毫秒");
            Element element113 = element11.addElement("info");
            element113.addAttribute("Name", "RecoveryTime").addAttribute("Value", String.valueOf(creatxml.getRandomNum2()));
            element11.addComment("通路保护到换时间 单位为毫秒");
            Element element114 = element11.addElement("info");
            element114.addAttribute("Name", "PathRecoverTransformTime").addAttribute("Value", String.valueOf(creatxml.getRandomNum3()));
        }
        /*输出*/
        OutputFormat outputFormat = OutputFormat.createPrettyPrint();
        outputFormat.setEncoding("utf-8");//设置按格式输出
        outputFormat.setNewlines(true);
        XMLWriter xmlWriter = new XMLWriter(new FileWriter("xingnengshangbao.xml"), outputFormat);
        xmlWriter.write(document);//写入文件
        xmlWriter.close();//关闭

    }

    public static double getRandomNum1(){
        Random random=new Random();
        int rand=random.nextInt(100);
        return rand;
    }
    public static double getRandomNum2(){
        Random random=new Random();
        int rand=random.nextInt(10);
        return rand;
    } public static double getRandomNum3(){
        Random random=new Random();
        int rand=random.nextInt(2);
        return rand;
    }

}
