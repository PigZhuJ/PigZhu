//package Test;
//
//import org.dom4j.Document;
//import org.dom4j.DocumentException;
//import org.dom4j.DocumentHelper;
//import org.dom4j.Element;
//import org.dom4j.io.OutputFormat;
//import org.dom4j.io.SAXReader;
//import org.dom4j.io.XMLWriter;
//
//import java.io.FileWriter;
//import java.io.IOException;
//
//public class data {
//
//    public void creatTrafficInfo() throws IOException {
//        Document document = DocumentHelper.createDocument();
//        Element root = document.addElement("trafficInfomation");
//        root.addAttribute("Name", "traffic");
//
//    /*输出*/
//        OutputFormat outputFormat = OutputFormat.createPrettyPrint();
//        outputFormat.setEncoding("utf-8");//设置按格式输出
//        outputFormat.setNewlines(true);
//        XMLWriter xmlWriter = new XMLWriter(new FileWriter("trafficInfo.xml"), outputFormat);
//        xmlWriter.write(document);//写入文件
//        xmlWriter.close();//关闭
//    }
//
//    /**
//     * 增加业务子节点
//     *
//     * @throws DocumentException
//     */
//    public void addTrafficInfo() throws DocumentException {
//        //读xml表格增加一个子节点
//        SAXReader reader = new SAXReader();
//        Document document = reader.read("trafficInfo.xml");
//
//        Element root = document.getRootElement();
//
//        Element element = root.addElement("traffic");
//        Element element1 = element.addElement("tunnelID");
//        element1.setText("1");
//
//
//    }
//
//    /**
//     * 增加业务子节点
//     *
//     * @throws DocumentException
//     */
//    public void addTrafficInfo(Traffic traffic) throws DocumentException {
//        //读xml表格增加一个子节点
//        SAXReader reader = new SAXReader();
//        Document document = reader.read("trafficInfo.xml");
//
//        Element root = document.getRootElement();
//
//        Element element = root.addElement("traffic");
//
//        Element element1 = element.addElement("tunnelID");
//        element1.setText(String.valueOf(traffic.tunnelID));//save tunnelID
//
//        Element element2 = element.addElement("srcNode");
//        element2.setText(String.valueOf(traffic.src_id));//源节点
//
//        Element element3 = element.addElement("srcArea");
//        element3.setText(String.valueOf(traffic.src_area_id));//源域
//
//        Element element4 = element.addElement("dstNode");
//        element4.setText(String.valueOf(traffic.dst_id));//宿节点
//
//        Element element5 = element.addElement("dstArea");
//        element5.setText(String.valueOf(traffic.dst_area_aid));//宿域
//
//        Element element6 = element.addElement("");
//        element6.setText("");
//
//        Element element7 = element.addElement("");
//        element7.setText("");
//
//        Element element8 = element.addElement("");
//        element8.setText("");
//
//        Element element9 = element.addElement("");
//        element9.setText("");
//
//        Element element10 = element.addElement("");
//        element10.setText("");
//
//        Element element11 = element.addElement("");
//        element11.setText("");
//
//
//    }
