//package Ason;
//
//import java.awt.Color;
//import java.io.DataInputStream;
//import java.io.IOException;
//import java.net.Socket;
//
//import javax.swing.JOptionPane;
//import javax.swing.JTextArea;
//
//import org.junit.Test;
//import twaver.Node;
//import twaver.TDataBox;
//import twaver.Link;
//import twaver.TWaverConst;
//
//import java.util.Date;
//import java.util.concurrent.ConcurrentHashMap;
//
//public class NodeListenRunnable extends AsonManager implements Runnable {
//
//    //	private DataInputStream in_stream;//输入流
//    private DataInputStream dataInputstream;//输入流
//    //	private JTextArea textarea;//JTextArea
////	private TDataBox box;//TDataBox
//    private JTextArea jTextArea;//JTextArea
//    private TDataBox tDataBox;//TDataBox
//
//
//    //	private boolean FIND_REROUTE_PATH = false;//重路由路径
////	private boolean FAULT_LINK_FLAT = false;//错误链路标志
//    private boolean reRouteFlag = false;//重路由路径
//    private boolean faultLinkFlag = false;//错误链路标志
//
//
//    private byte[] CommonHead = new byte[20];//20个字节表示包头
//    //	private int node_id;//节点ID
//    private int Node_ID;
//    private int module;
//    private int cmd;                 //类型 建路 删路 等
//    private int op;
//
//    private static int number_tr = 0;
//
//    private String cmd_nam = null;
//
//    private int reportinfo;
//    private int pkt_len;
//
//    private boolean[] exitflag = null;// 2012-2-24 wj
//    boolean[][] bilink = null;// 2012-2-28 wj
//    static int[][] SpathD = null;// 2012-03-10 wj
//    static int[][] setpathmatrx = null;// 2012-03-11 wj
//
//    static int node_tr = 0; // yuyu
//
//    final static int NODENUM = 320;// 2012-03-10 wj 320 36
//    static int nlinks = 0; // 2013-06-15 wj
//
//    Socket[] sockarr = null;
//    public static ConcurrentHashMap Dmap = new ConcurrentHashMap();     //用于链路资源查询 key为链路  IDvalue为已用时隙个数
//    public static int[][] dataArray = new int[30][30];// 暂时用来存储数据
//
//    // just to transform the socket to a in stream.
//    public NodeListenRunnable(int ID, JTextArea jTextArea, TDataBox jDataBox, Socket[] sockArr, boolean[][] link, int[][] path,
//                              int[][] pathmatrix, int[][] datadis, boolean[] exitFlagArr) {
//        try {
//            this.Node_ID = ID;
//            this.textarea = ta;
//            this.tDataBox = jDataBox;
//            this.sockarr = sockArr;
//            this.node_tr = Node_ID;
//            dataInputstream = new DataInputStream(sockarr[ID].getInputStream());
//
//            this.exitflag = exitFlagArr;
//            this.bilink = link;
//            this.SpathD = path;
//            this.dataArray = datadis;
//            this.setpathmatrx = pathmatrix;
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /************************************add by zhujian 2017/01/09*************************************/
//
//
//    @Override
//    public void run() {
//        // 初始化使用的数组
//        initialArray(dataArray);
//        // exitflag[node_id]在AsonNetworkMangager中有关kill节点的函数中设置成false之后该线程终止，否则一直读lmp包
//        while (exitflag[Node_ID]) {
//            // System.out.println("NodeListenRunnable is running...");
//            deal_pkt(dataInputstream, Node_ID);
//        }
//    }
//
//    /**
//     * deal_pkt
//     *
//     * @param dis    输入流
//     * @param number
//     */
//    private void deal_pkt(DataInputStream dis, int number) {
//        try {
//            textarea.append("Received a pacakge and begin to deal with...\n");
//
//            dis.read(CommonHead);
//            //读module
//            module = byt2int(CommonHead, 0);// CommonHead中读取四个byte
//            textarea.append("module number" + module);
//            //读cmd
//            cmd = short_trans(CommonHead, 4);//  cmd=1:建路  2：删路
//            textarea.append("cmd:" + cmd);
//            //读op
//            op = short_trans(CommonHead, 6);
//            //读reportinfo
//            reportinfo = byt2int(CommonHead, 12);
//            //读pkt_len
//            pkt_len = byt2int(CommonHead, 16);
//
//            if (check_header() == true) {
//
//                textarea.append("\nPacket from " + Node_ID + ",and Length is:" + pkt_len + "\n");
//                byte[] msgBody = new byte[pkt_len];
//                dis.read(msgBody);
//
//                switch (module) { //
//                    case 0x01:
//                        deal_rc();//处理rc的消息
//                        break;
//                    case 0x02:
//                        deal_cc(msgBody);
//                        textarea.append("receive a cc Message packet\n");
//                        if (cmd == 1) {
//                            number_tr++;
//                        } else if (cmd == 2) {
//                        }
//                        break;
//                    case 0x03:
//                        deal_lmp(msgBody);//处理lmp的消息
//                        break;
//                    case 0x05:
//                        textarea.append("module==0x05");
//                        break;
//                    default:
//                }
//            } else {
//                System.out.println("Missing a package:Node " + Node_ID);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 处理ccMsg包的信息
//     *
//     * @param packageBody 传入的byte数组
//     */
//    public void deal_cc(byte[] packageBody) {
//
//
//        System.out.println("Begin to Deal With CC Msg");
//
//        int srcipbit4 = 0;
//        int fromNode = packageBody[3];// byt2int(pkt_body, 0);
//        int srcipbit3 = packageBody[2];
//        fromNode = (fromNode & 0x000000ff);
//        if (1 == srcipbit3)// 2012-2-20 wj
//        {
//            fromNode += 150;
//        } else if (200 == fromNode) {
//            fromNode = 100;
//        }
//
//        int toNode = packageBody[11];// byt2int(pkt_body, 8);2012-2-20 wj
//        int desipbit3 = packageBody[10];// 2012-2-20 wj
//        toNode = (toNode & 0x000000ff);
//        if (1 == desipbit3)// 2012-2-20 wj
//        {
//            toNode += 150;
//        } else if (200 == toNode) {
//            toNode = 100;
//        }
//
//        // 通道ID
//        int tunnelidhigh = packageBody[16];
//        int tunnelidlow = packageBody[17];
//
//
//        switch (op)// op作为主命令标识，分别是GET,SET,REPROT,LINK
//        {
//            case 0x01:
//                // textarea.append("cc msg,type:GET， from"+node_id+"\n"); break;
//            case 0x02:
//                // textarea.append("cc msg,type:SET， from"+node_id+"\n");break;
//            case 0x03:
//                // textarea.append("cc msg,type:REPORT， from"+node_id+"\n");break;
//            case 0x04: {
//                // textarea.append("cc msg,type:LINK， from"+node_id+"\n");
//                if (cmd == 0x01) {      //建路
//                    dealSetPath(packageBody, fromNode, toNode, tunnelidlow, tunnelidhigh, srcipbit3, desipbit3, srcipbit4);//处理建路相关操作
//                } else if (cmd == 0x02) {
//                    dealDeletePath(packageBody, fromNode, toNode, tunnelidlow, tunnelidhigh, srcipbit3, desipbit3, srcipbit4);//处理删路相关操作
//                }
//            }
//            case 0x05: {
//
//                if (cmd == 0x03) {
//                    dealwith1(packageBody, fromNode, toNode);
//                } else if (cmd == 0x04) {
//                    dealwith2(packageBody, fromNode, toNode);
//                }
//            }
//        }
//
//    }
//    private void deal_rc() {
//
//    }
//
//    private void deal_lmp(byte[] pkt_body) {
//        // textarea.append("lmp\n");
//        try {
//            if (cmd == 3) {
//                switch (op) {
//                    case 0x01:// DataLink info.For link discover.
//                    {
//                        textarea.append(Node_ID + ":datalink pkt\n");
//                        textarea.append("receive a lmp packet\n");// fancy
//                        int actual_num = byt2int(pkt_body, pkt_len - 4);
//                        for (int i = 0; i < actual_num; i++) {
//                            int srcipbit3 = pkt_body[82 + i * 100];// 2012-2-20 wj
//                            int src_id = (pkt_body[83 + i * 100] & 0x000000ff);
//                            if (1 == srcipbit3)// 2012-2-20 wj
//                            {
//                                src_id += 150;
//                            } else if (200 == src_id) {
//                                src_id = 100;
//                            }
//                            int desipbit3 = pkt_body[90 + i * 100];// 2012-2-20 wj
//                            int des_id = (pkt_body[91 + i * 100] & 0x000000ff);
//                            if (1 == desipbit3)// 2012-2-20 wj
//                            {
//
//                                des_id += 150;
//                            } else if (200 == des_id) {
//                                des_id = 100;
//                            }
//
//                            if (src_id != Node_ID)
//                                textarea
//                                        .append("lmp pkt src id != node thread id\n");
//                            String linkf = src_id + "_" + des_id;
//                            String linkb = des_id + "_" + src_id;
//                            String linkp = src_id + "->" + des_id;// only for GUI
//                            // show.
//
//                            bilink[src_id][des_id] = true;// 2012-2-28 wj
//                            if (true == bilink[des_id][src_id])// 2012-2-28 wj
//                            {
//                                if (box.getElementByID(linkf) != null) {
//                                    ((Link) (box.getElementByID(linkf)))
//                                            .putLinkColor(Color.RED);
//                                } else {
//                                    ((Link) (box.getElementByID(linkb)))
//                                            .putLinkColor(Color.RED);
//                                }
//                            }
//                            textarea.append("link:" + linkp + "found\n");
//                        }
//                        break;
//                    }
//                    case 0x02:// TeLink info
//                    {
//                        // textarea.append(node_id+":Telink pkt\n");
//                        break;
//                    }
//                    case 0x04:// discovery info
//                    {
//                        // textarea.append(node_id+":Discovery pkt\n");
//                        break;
//                    }
//                    default:
//                        textarea.append(Node_ID + ":unknown lmp report pkt\n");
//                }
//            } else
//                textarea.append(Node_ID + ":lmp nonreport pkt \n");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    /**
//     * design Path
//     *
//     * @param path 二维数组路径
//     * @param src  源节点
//     * @param dst  宿节点
//     * @return
//     */
//    private int finddesignpath(int path[][], int src, int dst) {
//
//        System.out.println("源节点--->宿节点 ：" + src + "->" + dst);//源节点---->宿节点
//
//        int i = 0;
//        for (i = 1; i < NODENUM + 2; i++) {
//            if (0 != path[i][0]) {
//                if ((path[i][0] == src) && (path[i][1] == dst)) {
//                    if (path[i][2] == -1 && path[i][3] == 0) { // no path stores
//                        // in this row
//                        break;
//                    } else // a path have stored in this row
//                    {
//                        continue; // jump this row
//                    }
//                }
//            }
//        }
//        System.out.println("2014-03-20 12222222");//打桩输出
//        return i;
//    }
//
//    /**
//     * delete path
//     *
//     * @param path
//     * @param src
//     * @param dst
//     * @return
//     */
//    private int finddelpath(int path[][], int src, int dst) {
//        int i = 0;
//        for (i = 1; i < NODENUM + 2; i++) {
//            if (0 != path[i][0]) {
//                if ((path[i][0] == src) && (path[i][1] == dst)) {
//                    if ((-1 == path[i][2]) && (0 != path[i][3]))
//                        break;
//                }
//            }
//        }
//        return i;
//    }
//
//    // 2013-12-02 temp method
//
//    /**
//     * find Reroute Path
//     *
//     * @param path
//     * @param src
//     * @param dst
//     */
//    private void findDelReroutePath(int path[][], int src, int dst) {
//        int i = 0;
//        for (i = 1; i < NODENUM + 2; i++) {
//            if (0 != path[i][0]) {
//                if ((path[i][0] == src) && (path[i][1] == dst)) {
//                    if ((-1 == path[i][2]) && (0 != path[i][3])) {
//                        reRouteFlag = true;
//                        break;
//                    }
//                }
//            }
//        }
//    }
//
//    /**
//     * find useable postion
//     *
//     * @param path
//     * @param src
//     * @param dst
//     * @return
//     */
//    private int finduseablepostion(int path[][], int src, int dst) {
//        int i = 0, j = 0;
//        for (i = 1; i < NODENUM + 2; i++) {
//            if (0 != path[i][0]) {
//                if ((path[i][0] == src) && (path[i][1] == dst)) {
//                    for (j = 1; j < NODENUM + 2; j++)// skip the 1st -1
//                    {
//                        if ((-1 == path[i][j]) && (0 == path[i][j + 1]))
//                            break;
//                    }
//                }
//            }
//        }
//        return j + 1;
//    }
//
//    /**
//     * delete path
//     *
//     * @param path
//     * @param row
//     * @return
//     */
//    private int delpath(int path[][], int row) {
//        int index = 0;
//        for (index = 0; index < NODENUM + 2; index++) {
//            if (-1 == path[row][index]) {
//                if (0 != path[row][index + 1]) {
//                    path[row][index] = 0;
//                    break;
//                } else {
//                    index = -2;
//                    break;
//                }
//            }
//        }
//
//        return index + 1;
//    }
//
//    /**
//     * clear self path
//     *
//     * @param path
//     * @param src
//     * @param dst
//     * @param delrow
//     */
//    private void clearselfpath(int path[][], int src, int dst, int delrow) {
//        int row = delrow;
//        if ((src == path[row][0]) && (dst == path[row][1])) {
//            if (0 == path[row][2]) // 在删路时已经将index=2处的值-1改为了0
//            {
//                for (int j = 0; j < NODENUM + 2; j++) {
//                    if ((0 == path[row][j]) && (0 == path[row][j + 1]))
//                        break;
//                    path[row][j] = 0;
//                }
//            }
//        }
//    }
//
//    /**
//     * clear unusable path
//     *
//     * @param path
//     * @param src
//     * @param dst
//     */
//    private void clearunusablepath(int path[][], int src, int dst) {
//        int row = 0;
//        for (row = 1; row < NODENUM + 1; row++) {
//            if ((src == path[row][0]) && (dst == path[row][1])) {
//                // if((-1==path[row][2]) || (0==path[row][3]))
//                if ((-1 == path[row][2]) && (0 == path[row][3])) {
//                    for (int j = 0; j < NODENUM + 2; j++) {
//                        if ((0 == path[row][j]) && (0 == path[row][j + 1]))
//                            break;
//                        path[row][j] = 0;
//                    }
//                }
//            }
//        }
//    }
//
//    /**
//     * get 4 byte from b
//     *
//     * @param b 字节数组
//     * @param k 整数
//     * @return t 整数
//     */
//    private int byt2int(byte[] b, int k) {
//        int t = (int) ((b[k + 3] & 0xff) << 24)
//                + (int) ((b[k + 2] & 0xff) << 16)
//                + (int) ((b[k + 1] & 0xff) << 8) + (int) (b[k] & 0xff);
//        return t;
//    }
//
//    /**
//     * get 2 byte from b
//     *
//     * @param b 字节数组
//     * @param k 数字
//     * @return t 整数
//     */
//    private int short_trans(byte[] b, int k) {
//        int t = (int) ((b[k + 1] & 0xff) << 8) + (int) (b[k] & 0xff);
//        return t;
//    }
//
//    /**
//     * initial Array
//     *
//     * @param Array 初始化数组
//     * @return 返回数组
//     */
//    public int[][] initialArray(int[][] Array) {
//
//        for (int i = 0; i < Array.length; i++) {
//            for (int j = 0; j < Array[i].length; j++) {
//                Array[i][j] = 0;
//            }
//
//        }
//        return Array;
//    }
//
//    /**
//     * display2D array
//     *
//     * @param dataArray
//     */
//    public void display2DArray(int[][] dataArray) {
//        for (int i = 0; i < dataArray.length; i++) {
//            for (int j = 0; j < dataArray[i].length; j++) {
//                System.out.print(dataArray[i][j] + " ");
//            }
//            System.out.println();
//        }
//    }
//
//
//    /**
//     * 检查包头
//     *
//     * @return
//     */
//    private boolean check_header() {
//        if (module < 0 || module > 4) {
//            textarea.append("pkt header-module error\n");
//            return false;
//        }
//        if (cmd < 0 || cmd > 4) {
//            textarea.append("pkt header-cmd error\n");
//            return false;
//        }
//        if (op < 0) {
//            textarea.append("pkt header-op error\n");
//            return false;
//        }
//        if (pkt_len < 0) {
//            textarea.append("pkt header-len error\n");
//            return false;
//        }
//        return true;
//    }
//
//
//
//
//    private void dealwith1(byte[] packageBody, int fromNode, int toNode) {
//        int srcipbit3;
//        int srcipbit4;
//        int len = pkt_len + 20;
//        byte[] pkt = new byte[len];
//        for (int i = 0; i < 20; i++) {
//            pkt[i] = CommonHead[i];
//        }
//        for (int j = 20; j < len; j++) {
//            pkt[j] = packageBody[j - 20];
//        }
//        srcipbit3 = pkt[22];// the 3nd byte of ip address
//        srcipbit4 = pkt[23];// the 4th byte of ip address
//        srcipbit4 = (srcipbit4 & 0x000000ff);// make sure the value is positive
//        if (1 == srcipbit3)// case 192.168.1.XXX,add 150
//        {
//            srcipbit4 += 150;
//        }
//        try {
//
//            sockarr[srcipbit4].getOutputStream().write(pkt); // wj 需转换
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        int asnum = (int) (packageBody[824] & 0xff);
//        if (asnum > 0) {
//            // inter
//            textarea.append("Inter2 Addr Path:");
//
//            int row = 0, colum = 0;
//            row = finddesignpath(SpathD, fromNode, toNode);
//            colum = finduseablepostion(SpathD, fromNode, toNode);
//
//            int[] linkSD = new int[asnum];
//            for (int i = 0; i < asnum; i++) {
//                srcipbit3 = packageBody[286 + i * 4];// the 3nd byte of ip
//                // address
//                srcipbit4 = packageBody[287 + i * 4];// the 4th byte of ip
//                // address
//                srcipbit4 = (srcipbit4 & 0x000000ff);// makesure the
//                // value is
//                // positive
//                if (1 == srcipbit3)// case 192.168.1.XXX,add 150
//                {
//                    srcipbit4 += 150;
//                } else if (200 == srcipbit4) {
//                    srcipbit4 = 100;
//                }
//                linkSD[i] = srcipbit4;
//                SpathD[row][colum] = linkSD[i];
//                colum++;
//                textarea.append("" + srcipbit4);
//                if (i != (asnum - 1)) {
//                    textarea.append("->");
//                }
//            }
//            SpathD[row][colum] = -1;
//            textarea.append("\n");
//
//            for (int i = 0; i < asnum - 1; i++) {
//                String linkf = linkSD[i] + "_" + linkSD[i + 1];
//                String linkb = linkSD[i + 1] + "_" + linkSD[i];
//                if (box.getElementByID(linkf) != null) {
//                    ((Link) (box.getElementByID(linkf)))
//                            .putLinkColor(Color.YELLOW);
//                    isdefinedLink(linkf);
//
//                } else if (box.getElementByID(linkb) != null) {
//                    ((Link) (box.getElementByID(linkb)))
//                            .putLinkColor(Color.YELLOW);
//                    isdefinedLink(linkb);
//                }
//                setpathmatrx[linkSD[i]][linkSD[i + 1]]++;
//            }
//        }
//    }
//    private void dealwith2(byte[] packageBody, int fromNode, int toNode) {
//        int srcipbit3;
//        int srcipbit4;// 2012-2-24 wj
//        // display the path
//        textarea.append("Intra2 Addr Path:");
//        int hopNum = (int) (packageBody[64] & 0xff);
//        if (hopNum > 0) {
//            int row = 0, colum = 0;
//            row = finddesignpath(SpathD, fromNode, toNode);
//            colum = finduseablepostion(SpathD, fromNode, toNode);
//
//            int[] linkSD = new int[hopNum];
//            for (int i = 0; i < hopNum; i++) {
//                srcipbit3 = packageBody[86 + i * 4];// the 3nd byte of ip
//                // address
//                srcipbit4 = packageBody[87 + i * 4];// the 4th byte of ip
//                // address
//                srcipbit4 = (srcipbit4 & 0x000000ff);// makesure the
//                // value is
//                // positive
//                if (1 == srcipbit3)// case 192.168.1.XXX,add 150
//                {
//                    srcipbit4 += 150;
//                } else if (200 == srcipbit4) {
//                    srcipbit4 = 100;
//                }
//                linkSD[i] = srcipbit4;
//                SpathD[row][colum] = linkSD[i];
//                colum++;
//                textarea.append("" + srcipbit4);
//                if (i != (hopNum - 1)) {
//                    textarea.append("->");
//                }
//            }
//            SpathD[row][colum] = -1;
//            textarea.append("\n");
//
//            for (int i = 0; i < hopNum - 1; i++) {
//                String linkf = linkSD[i] + "_" + linkSD[i + 1];
//                String linkb = linkSD[i + 1] + "_" + linkSD[i];
//                if (box.getElementByID(linkf) != null) {
//                    ((Link) (box.getElementByID(linkf)))
//                            .putLinkColor(Color.YELLOW);
//                    isdefinedLink(linkf);
//
//                } else if (box.getElementByID(linkb) != null) {
//                    ((Link) (box.getElementByID(linkb)))
//                            .putLinkColor(Color.YELLOW);
//                    isdefinedLink(linkb);
//                }
//                setpathmatrx[linkSD[i]][linkSD[i + 1]]++;
//            }
//        }
//    }
//
//
//    private void dealSetPath(byte[] packageBody, int fromNode, int toNode, int tunnelidlow, int tunnelidhigh, int srcipbit3, int desipbit3, int srcipbit4) {
//
//        int hopNum = 0;
//        int asnum = 0;//域内爱是域间
//        int proType = 0;//pkt_body[57];保护类型
//        int workOrProLink = 0;//pkt_body[59];// 用于标识工作路径（0）or保护路径（1）[79]byte对应数值
//
//        System.out.println("保护类型" + proType + "保护or工作" + workOrProLink);
//
//        textarea.append("Protection or work number:" + workOrProLink + "\n");
//        int hopNumer = (int) (packageBody[64] & 0xff);   //路径 跳数
//
//        int resourceNum = byt2int(packageBody, 32);      //占用时隙个数
//
//        int resource = byt2int(packageBody, 828);//占用资源
//
//        if (reportinfo == 0) {        //reportinfo==0
//            cmd_nam = "Set Path";
//            proType = packageBody[57];                            //路径类型
//            workOrProLink = packageBody[59];                     //工作路径还是保护路径
//
//            AsonManager.SetPathSuccessNum++;
//
//            Date End_date = new Date();
//            long PathTime = End_date.getTime();
//            // PathTime=
//
//            String fromnode = "Node" + fromNode;
//            String tonode = "Node" + toNode;
//
//            dataArray[number_tr][0] = number_tr + 1;//记录业务数字
//            dataArray[number_tr][1] = cmd;
//            dataArray[number_tr][2] = fromNode;
//            dataArray[number_tr][3] = toNode;
//            dataArray[number_tr][4] = workOrProLink;
//            dataArray[number_tr][5] = tunnelidhigh;
//            dataArray[number_tr][26] = resource;
//            dataArray[number_tr][28] = resourceNum;
//
//            display2DArray(dataArray);//输出数据
//
//            textarea.append("Set Path begin,from " + fromnode + " to " + tonode + "\n");
//
//            // display the path
//            asnum = (int) (packageBody[824] & 0xff);// 判别域内还是域间
//            if (asnum > 0) {
//                // inter
//                textarea.append("Inter1 Addr Path:" + fromNode + "->");
//
//                int row = 0, colum = 0;
//                row = finddesignpath(SpathD, fromNode, toNode); // 同源同宿时会有问题
//                colum = finduseablepostion(SpathD, fromNode, toNode);
//                SpathD[row][colum] = fromNode;
//                colum++;
//
//                int[] linkSD = new int[asnum];
//                for (int i = 0; i < asnum - 1; i++) {
//                    srcipbit3 = packageBody[286 + i * 4];// the 3nd byte of
//                    // ip address
//                    srcipbit4 = packageBody[287 + i * 4];// the 4th byte of
//                    // ip address
//                    srcipbit4 = (srcipbit4 & 0x000000ff);// makesure the value is postive
//
//                    if (1 == srcipbit3)// case 192.168.1.XXX,add 150
//                    {
//                        srcipbit4 += 150;
//                    } else if (200 == srcipbit4) {
//                        srcipbit4 = 100;
//                    }
//
//                    linkSD[i] = srcipbit4;
//                    SpathD[row][colum] = linkSD[i];
//                    colum++;
//                    textarea.append("" + srcipbit4);
//                    if (i != (asnum - 2)) {
//                        textarea.append("->");
//                    }
//
//                }
//
//                SpathD[row][colum] = -1;
//
//                // Debug 2014-1-6 a temp tunnel id add after '-1'
//                SpathD[row][colum + 1] = 0;
//                SpathD[row][colum + 2] = tunnelidhigh;
//                SpathD[row][colum + 3] = tunnelidlow;
//                System.out.println("1 row and column is: " + row
//                        + " - " + colum);
//                System.out.println("2 tunnel id is: " + tunnelidhigh
//                        + " -- " + tunnelidlow);
//                textarea.append("\n");
//
//                for (int i = 0; i < asnum - 1; i++) {
//                    String linkf = linkSD[i] + "_" + linkSD[i + 1];
//                    String linkb = linkSD[i + 1] + "_" + linkSD[i];
//
//
//                    System.out.println("only text: " + linkf
//                            + " -- " + linkb);
//                    textarea.append("工作路径显示11" + workOrProLink + "\n");
//                    if (workOrProLink == 0) {
//                        if (box.getElementByID(linkf) != null) {
//                            ((Link) (box.getElementByID(linkf)))
//                                    .putLinkColor(Color.YELLOW);
//
//                            //check whether the defined link isincluded
//                            isdefinedLink(linkf);
//                        } else if (box.getElementByID(linkb) != null) {
//                            ((Link) (box.getElementByID(linkb)))
//                                    .putLinkColor(Color.YELLOW);
//                            isdefinedLink(linkb);
//                        }
//                        setpathmatrx[linkSD[i]][linkSD[i + 1]]++;
//                    } else {
//                        textarea.append("保护路径显示11" + workOrProLink + "\n");
//                        if (box.getElementByID(linkf) != null) {
//                            ((Link) (box.getElementByID(linkf)))
//                                    .putLinkColor(Color.CYAN);
//                            // --//check whether the defined link is
//                            // included
//                            isdefinedLink(linkf);
//                        } else if (box.getElementByID(linkb) != null) {
//                            ((Link) (box.getElementByID(linkb)))
//                                    .putLinkColor(Color.CYAN);
//                            isdefinedLink(linkb);
//                        }
//                        setpathmatrx[linkSD[i]][linkSD[i + 1]]++;
//                    }
//
//                }
//
//                String linkf = fromNode + "_" + linkSD[0];
//                String linkb = linkSD[0] + "_" + fromNode;
//
//                textarea.append("工作路径显示22" + workOrProLink + "\n");
//                if (workOrProLink == 0) {
//                    if (box.getElementByID(linkf) != null) {
//                        ((Link) (box.getElementByID(linkf)))
//                                .putLinkColor(Color.YELLOW);
//                        isdefinedLink(linkf);
//                    } else if (box.getElementByID(linkb) != null) {
//                        ((Link) (box.getElementByID(linkb)))
//                                .putLinkColor(Color.YELLOW);
//                        isdefinedLink(linkb);
//                    }
//                    setpathmatrx[fromNode][linkSD[0]]++;
//                } else {
//                    textarea.append("保护路径显示22" + workOrProLink + "\n");
//                    if (box.getElementByID(linkf) != null) {
//                        ((Link) (box.getElementByID(linkf)))
//                                .putLinkColor(Color.CYAN);
//                        isdefinedLink(linkf);
//                    } else if (box.getElementByID(linkb) != null) {
//                        ((Link) (box.getElementByID(linkb)))
//                                .putLinkColor(Color.CYAN);
//                        isdefinedLink(linkb);
//                    }
//                    setpathmatrx[fromNode][linkSD[0]]++;
//                }
//
//            }
////asnum>o over
//            // intra
//            System.out.println("2014-03-20 1111111");
//            hopNum = (int) (packageBody[64] & 0xff);
//
//            textarea.append("intra set path begin" + "\n");
//
//            if (hopNum > 0) {
//                textarea.append("Intra1 Addr Path:00000000000000000");
//                textarea.append("保护路径的hopnum值" + hopNum + "\n");
//                int row = 0, colum = 0;
//                row = finddesignpath(SpathD, fromNode, toNode);
//
//                System.out.println("row的值" + row);
//                colum = finduseablepostion(SpathD, fromNode, toNode);
//                System.out.println("colum的值" + colum);
//                int[] linkSD = new int[hopNum];
//                System.out.println("hopnum: " + hopNum);
//                dataArray[number_tr][6] = hopNum;
//                // -------------------YUYU--0708
//                for (int i = 0; i < hopNum; i++) {
//                    System.out.print("a" + " row is " + row
//                            + "; column is " + colum + "; ");
//
//
//                    srcipbit3 = packageBody[86 + i * 4];// the 3nd byte of
//                    // ip address
//                    srcipbit4 = packageBody[87 + i * 4];// the 4th byte of
//                    // ip address
//                    srcipbit4 = (srcipbit4 & 0x000000ff);// makesure the
//                    // value is
//                    // positive
//                    if (1 == srcipbit3)// case 192.168.1.XXX,add 150
//                    {
//                        srcipbit4 += 150;
//                    } else if (200 == srcipbit4) {
//                        srcipbit4 = 100;
//                    }
//                    System.err.println("srcipbit4" + srcipbit4);
//                    linkSD[i] = srcipbit4;
//                    /*		for (int m = 0; m < SpathD.length; m++) {
//                                for (int t = 0; t < SpathD[m].length; t++) {
//									// System.err.println("SpathD的值"+SpathD[m][t]);
//								}
//
//							}
//*/
//                    SpathD[row][colum] = linkSD[i];
//
//
//                    if (workOrProLink == 0) {
//                        dataArray[number_tr][colum + 4] = SpathD[row][colum];
//                    } else if (workOrProLink == 1) {
//                        dataArray[number_tr][colum] = SpathD[row][colum];
//                    }
//
//                    colum++;
//                    textarea.append("" + srcipbit4);
//                    if (i != (hopNum - 1)) {
//                        textarea.append("->");
//                    }
//                }
//
//                SpathD[row][colum] = -1; // 在hop后加一个 -1
//                if (workOrProLink == 0) {
//                    dataArray[number_tr][colum + 4] = -1;
//                } else if (workOrProLink == 1) {
//                    dataArray[number_tr][colum] = -1;
//                }
//
//                // Debug 2014-1-6 a temp tunnel id add after '-1'
//                SpathD[row][colum + 1] = 0;
//                System.out.println("2014-03-20 6666666");
//                SpathD[row][colum + 2] = tunnelidhigh;
//                SpathD[row][colum + 3] = tunnelidlow;
//                System.out.println("2014-03-20 77777");
//
//                // debug
//                //		System.out.println("----------ddddddddddddddddddddddd");
//                for (int i = 0; i < 100; i++) {
//                    System.out.print("SpathD is: ");
//                    for (int j = 0; j < 15; j++) {
//                        System.out.print(SpathD[i][j]);
//                    }
//                    System.out.println();
//                }
//
//                for (int i = 0; i < dataArray.length; i++) {
//                    for (int j = 0; j < dataArray[i].length; j++) {
//                        System.out.print(dataArray[i][j] + " ");
//                    }
//                    System.out.println();
//                }
//                System.out
//                        .println("---------ggggggggggggggggggggggggg");
//
//                System.out.println("Node class :row and column is: "
//                        + row + " - " + colum);
//                System.out.println("3 tunnel id is: " + tunnelidhigh
//                        + " -- " + tunnelidlow);
//                textarea.append("\n");
//
//                for (int i = 0; i < hopNum - 1; i++) {
//                    String linkf = linkSD[i] + "_" + linkSD[i + 1];
//                    String linkb = linkSD[i + 1] + "_" + linkSD[i];
//                    textarea.append("工作 or 保护标识33:" + workOrProLink + "\n");
//                    textarea.append("linkf" + linkf);
//                    if (box.getElementByID(linkb) != null) {
//                        Link linkfcc = ((Link) (box.getElementByID(linkb)));
//                        String linkfccName = linkfcc.getID().toString();
//                        int ResourceUsedOld = (Integer) Dmap.get(linkfccName);
//
//                        int ResourceUsedNum = resourceNum + ResourceUsedOld;
//                        Dmap.put(linkfccName, ResourceUsedNum);
//
//                    }
//                    if (box.getElementByID(linkf) != null) {
//                        Link link_f = ((Link) (box.getElementByID(linkf)));
//                        String linkfName = link_f.getID().toString();
//                        int ResourceUsedOld = (Integer) Dmap.get(linkfName);
//                        int ResourceUsedNum = resourceNum + ResourceUsedOld;
//                        Dmap.put(linkfName, ResourceUsedNum);
//
//                    }
//							/*
//							System.out.println("map.size++."+Dmap.size());
//							Set<String> ltemp =new HashSet<String>();
//							ltemp = Dmap.keySet();
//							System.out.println("keySet Size " + ltemp.size());
//							System.out.println("Data begins");
//							for(String  ii: ltemp){
//								System.out.print(Dmap.get(ii)+" ");
//							}
//							System.out.println("end");
//
//							*/
//                    String linkfc = linkSD[i] + "_" + linkSD[i + 1];
//                    String linkbc = linkSD[i + 1] + "_" + linkSD[i];
//
//
//                    if (Dmap.get(linkbc) != null) {
//                        Dmap.remove(linkbc);
//                        Dmap.put(linkbc, resourceNum);
//                        System.out.println("text cc ..linkb." + linkbc + "resourcenum" + resourceNum);
//                    }
//
//
//                    if (workOrProLink == 0) {
//                        if (box.getElementByID(linkf) != null) {
//                            // ((Link)
//                            // (box.getElementByID(linkf))).setLayerID("layer1");
//                            ((Link) (box.getElementByID(linkf)))
//                                    .putLinkColor(Color.YELLOW);
//                            ((Link) (box.getElementByID(linkf)))
//                                    .putLinkBlinking(false);
//                            ((Link) (box.getElementByID(linkf)))
//                                    .putLinkStyle(TWaverConst.LINK_STYLE_SOLID);
//                            ((Link) (box.getElementByID(linkf)))
//                                    .putLink3D(false);
//                            // ((Link) (box.getElementByID(linkf)))
//                            // .putLink3D(true);
//                            isdefinedLink(linkf);
//                        } else if (box.getElementByID(linkb) != null) {
//                            // ((Link)
//                            // (box.getElementByID(linkb))).setLayerID("layer1");
//                            ((Link) (box.getElementByID(linkb)))
//                                    .putLinkColor(Color.YELLOW);
//                            ((Link) (box.getElementByID(linkb)))
//                                    .putLinkBlinking(false);
//                            ((Link) (box.getElementByID(linkb)))
//                                    .putLinkStyle(TWaverConst.LINK_STYLE_SOLID);
//                            ((Link) (box.getElementByID(linkb)))
//                                    .putLink3D(false);
//                            // ((Link) (box.getElementByID(linkb)))
//                            // .putLink3D(true);
//                            isdefinedLink(linkb);
//                        }
//                        setpathmatrx[linkSD[i]][linkSD[i + 1]]++;
//                    } else if (workOrProLink == 1) {
//                        textarea.append("保护路径显示33:" + workOrProLink + "\n");
//                        if (box.getElementByID(linkf) != null) {
//                            ((Link) (box.getElementByID(linkf)))
//                                    .setLayerID("layer1");
//                            ((Link) (box.getElementByID(linkf)))
//                                    .putLinkColor(Color.CYAN);
//                            isdefinedLink(linkf);
//                        } else if (box.getElementByID(linkb) != null) {
//                            ((Link) (box.getElementByID(linkb)))
//                                    .setLayerID("layer1");
//                            ((Link) (box.getElementByID(linkb)))
//                                    .putLinkColor(Color.CYAN);
//                            isdefinedLink(linkb);
//                        }
//                        setpathmatrx[linkSD[i]][linkSD[i + 1]]++;
//                    }
//
//                    textarea.append("set intra path success..." + "\n");
//                }
//
//            }
//
//            //
//            /**
//             * 建路，故障重路由计时结束
//             */
//            if (workOrProLink == 1)// 有保护类型时进入此条件
//            {
//
//                if (proType == 2) {
//                    if (AsonManager.setpathTimeFlag
//                            & AsonManager.setpathTimeFlag_one_one) {
//                        // 1+1保护的时候时间统计
//                        AsonManager.setpathTimeFlag = false;
//                        AsonManager.setpathTimeFlag_one_one = false;
//                        AsonManager.setpathEndTime_one_one = System
//                                .currentTimeMillis();
//                        long setpathTime_one_one = AsonManager.setpathEndTime_one_one
//                                - AsonManager.setpathStartTime;
//                        String message = "1+1保护建路耗时 "
//                                + setpathTime_one_one + " ms";
//
//                        JOptionPane.showMessageDialog(null, message,
//                                "1+1 Setpath Time",
//                                JOptionPane.INFORMATION_MESSAGE);
//
//                    }
//                } else {
//                    System.out.println("其他类型保护方式对应的保护路径");
//                }
//            } else if (workOrProLink == 0) {
//                if (proType == 0) {
//                    System.out.println("无保护--工作路径完成时间显示");
//                    if (AsonManager.setpathTimeFlag) {
//                        // 普通建路或者重路由的时间统计
//                        AsonManager.setpathTimeFlag = false;
//                        AsonManager.setpathEndTime = System
//                                .currentTimeMillis();
//                        long setpathTime = AsonManager.setpathEndTime
//                                - AsonManager.setpathStartTime;
//                        String message = "本次建路耗时 " + setpathTime
//                                + " ms";
//
//                        JOptionPane.showMessageDialog(null, message,
//                                "Setpath Time",
//                                JOptionPane.INFORMATION_MESSAGE);
//
//                    }
//                } else if (proType == 1) {
//                    System.out.println("reroute-工作路径完成时间显示");
//                    if (AsonManager.setpathTimeFlag) {
//                        // 普通建路或者重路由的时间统计
//                        AsonManager.setpathTimeFlag = false;
//                        AsonManager.setpathEndTime = System
//                                .currentTimeMillis();
//                        long setpathTime = AsonManager.setpathEndTime
//                                - AsonManager.setpathStartTime;
//                        String message = "本次建路耗时 " + setpathTime
//                                + " ms";
//
//                        JOptionPane.showMessageDialog(null, message,
//                                "Reroute Setpath Time",
//                                JOptionPane.INFORMATION_MESSAGE);
//
//                    }
//                } else if (proType == 2) {
//                    System.out.println("1+1保护，等待接收到保护路径之后显示");//  @TODO add by zhujian
//                } else if (proType == 3) {
//                    System.out.println("1:n保护");
//                    if (AsonManager.setpathTimeFlag) {
//                        // 普通建路或者重路由的时间统计
//                        AsonManager.setpathTimeFlag = false;
//                        AsonManager.setpathEndTime = System
//                                .currentTimeMillis();
//                        long setpathTime = AsonManager.setpathEndTime
//                                - AsonManager.setpathStartTime;
//                        String message = "本次建路耗时 " + setpathTime
//                                + " ms";
//
//                        JOptionPane.showMessageDialog(null, message,
//                                "Setpath Time",
//                                JOptionPane.INFORMATION_MESSAGE);
//
//                    }
//                }
//
//            }
//
//            if (AsonManager.fiberFaultRerouteTimeFlag) {
//
//                AsonManager.fiberFaultRerouteTimeFlag = false;
//                AsonManager.fiberFaultRerouteEnd = System
//                        .currentTimeMillis();
//                long fiberFaultRerouteTime = AsonManager.fiberFaultRerouteEnd
//                        - AsonManager.fiberFaultRerouteStart;
//                int time = (int) fiberFaultRerouteTime;
//                String message = "光纤故障重路由耗时 " + fiberFaultRerouteTime
//                        + " ms";
//
//                JOptionPane
//                        .showMessageDialog(null, message,
//                                "Reroute Time",
//                                JOptionPane.INFORMATION_MESSAGE);
//
//            }
//
//            //
//            long[] faultSimulationRerouteTime = new long[5];
//            for (int i = 0; i < 5; i++) {
//                if (AsonManager.faultSimulationRerouteTimeFlag[i]) {
//                    AsonManager.faultSimulationRerouteTimeFlag[i] = false;
//                    AsonManager.faultSimulationRerouteEnd[i] = System
//                            .currentTimeMillis();
//                    faultSimulationRerouteTime[i] = AsonManager.faultSimulationRerouteEnd[i]
//                            - AsonManager.faultSimulationRerouteStart[i];
//                    String from = AsonManager.faultSimulationReroutePath[i][0];
//                    String to = AsonManager.faultSimulationReroutePath[i][1];
//                    textarea
//                            .append("链路（" + from + " -> " + to
//                                    + "）故障重路由耗时： "
//                                    + faultSimulationRerouteTime[i]
//                                    + " ms. \n");
//                    AsonManager.faultSimulationReroutePath[i][0] = null;
//                    AsonManager.faultSimulationReroutePath[i][1] = null;
//                }
//            }
//
//            long[] faultSimulationProtectionTime = new long[5];
//            for (int i = 0; i < 5; i++) {
//                if (AsonManager.faultSimulationProtectionTimeFlag[i]) {
//                    AsonManager.faultSimulationProtectionTimeFlag[i] = false;
//                    AsonManager.faultSimulationProtectionEnd[i] = System
//                            .currentTimeMillis();
//                    faultSimulationProtectionTime[i] = AsonManager.faultSimulationProtectionEnd[i]
//                            - AsonManager.faultSimulationProtectionStart[i];
//                    String from = AsonManager.faultSimulationProtectionPath[i][0];
//                    String to = AsonManager.faultSimulationProtectionPath[i][1];
//                    textarea.append("链路（" + from + " -> " + to
//                            + "）1+1故障倒换耗时： "
//                            + faultSimulationProtectionTime[i]
//                            + " ms. \n");
//                    AsonManager.faultSimulationProtectionPath[i][0] = null;
//                    AsonManager.faultSimulationProtectionPath[i][1] = null;
//                }
//            }
//
//
//        } else {
//            textarea.append("Set Path error, from" + fromNode + "to" + toNode
//                    + "\n");
//        }
//    }
//    private void dealDeletePath(byte[] packageBody, int fromNode, int toNode, int tunnelidlow, int tunnelidhigh, int srcipbit3, int desipbit3, int srcipbit4) {
//        { //删路
//            int hopNum = 0;
//            int asnum = 0;//域内爱是域间
//            int proType = 0;//pkt_body[57];保护类型
//            int workOrProLink = 0;//pkt_body[59];// 用于标识工作路径（0）or保护路径（1）[79]byte对应数值
//
//            System.out.println("保护类型" + proType + "保护or工作" + workOrProLink);
//
//            textarea.append("Protection or work number:" + workOrProLink + "\n");
//            System.out.println(" cmd== 0x02");
//
//            cmd_nam = "Del Path";
//            System.out.println(" cmd_nam2" + cmd_nam);
//
//            if (reportinfo == 0) {
//                number_tr++;
//                System.out.println("业务流显示序号YUYUYUYUYUYU0125：" + number_tr);
//                textarea.append("\n----------delpath---------\n");
//                AsonManager.DelPathSuccessNum++;
//
//                System.out.println("delete path success...");
//                // 2012-3-10 wj
//                srcipbit3 = packageBody[2];// the 3nd byte of ip address
//                fromNode = packageBody[3];// the 4th byte of ip address
//                fromNode = (fromNode & 0x000000ff);// makesure the value is positive
//                if (1 == srcipbit3)// case 192.168.1.XXX,add 150
//                {
//                    fromNode += 150;
//                } else if (200 == fromNode) {
//                    fromNode = 100;
//                }
//                srcipbit3 = packageBody[10];
//                toNode = packageBody[11];// the 4th byte of ip address
//                toNode = (toNode & 0x000000ff);// makesure the value is positive
//                if (1 == srcipbit3)// case 192.168.1.XXX,add 150
//                {
//                    toNode += 150;
//                } else if (200 == toNode) {
//                    toNode = 100;
//                }
//                textarea.append("\n----------delpath2---------\n");
//                textarea.append("\ndel:" + fromNode + "->" + toNode + "\n");
//                // 通道ID
//                int tunnelidhigh_del = packageBody[16];
//                int tunnelidlow_del = packageBody[17];
//
//                int number_tep = number_tr - 1;
//                dataArray[number_tep][0] = number_tr;
//                dataArray[number_tep][1] = cmd;
//                dataArray[number_tep][2] = fromNode;
//                dataArray[number_tep][3] = toNode;
//                dataArray[number_tep][4] = workOrProLink;
//                dataArray[number_tep][5] = tunnelidhigh_del;
//                int[] linkSD = new int[hopNum];
//
//                dataArray[number_tr][6] = hopNum;
//
//                display2DArray(dataArray);
//
//                // 2013-12-02 temp 删除故障重路由的链路时通知fiber
//                findDelReroutePath(SpathD,
//                        AsonManager.rerouteFromNode,
//                        AsonManager.rerouteToNode);
//                if (reRouteFlag
//                        && AsonManager.FIBER_FAULT_REROUTE_DEL) {
//
//                    reRouteFlag = false;
//                    AsonManager.FIBER_FAULT_REROUTE_DEL = false;
//                    System.out.println("delete reroute link... send Delete");
//                    try {
//                        String sendString = "D";
//                        byte[] sendBytes = sendString.getBytes("UTF-8");
//                        AsonManager.fiberFaultSock.getOutputStream()
//                                .write(sendBytes);
//                    } catch (Exception e) {
//                        // TODO: handle exception
//                    }
//                }
//
//                display2DArray(SpathD);
//
//                int row = 0, colum = 0;
//                row = finddelpath(SpathD, fromNode, toNode);
//
//
//                // while(-1!=(colum=delpath(SpathD,row))) //将index=2处的-1置为0,
//                // 返回从index=3开始的列
//                while (-1 != (colum = delpath(SpathD, row))) // 因为在后面加了tunnelID，所以改变判断条件
//                {
//                    System.out.println("3333333333");
//                    for (int index = colum; -1 != SpathD[row][index + 1]; index++) {
//                        System.out.println("aa aa");
//                        int srcnode = SpathD[row][index];
//                        int dstnode = SpathD[row][index + 1];
//                        setpathmatrx[srcnode][dstnode]--;
//                        if ((0 == setpathmatrx[srcnode][dstnode])
//                                && (0 == setpathmatrx[dstnode][srcnode])) {
//                            String linkf = srcnode + "_" + dstnode;
//                            String linkb = dstnode + "_" + srcnode;
//
//                            if (box.getElementByID(linkb) != null) {
//                                Link linkfcc = ((Link) (box.getElementByID(linkb)));
//                                String linkfccName = linkfcc.getID().toString();
//                                int ResourceUsedOld = (Integer) Dmap.get(linkfccName);
//
//                                int ResourceUsedNum = 0;
//                                Dmap.put(linkfccName, ResourceUsedNum);
//                            } else if (box.getElementByID(linkf) != null) {
//                                Link linkfcc = ((Link) (box.getElementByID(linkf)));
//                                String linkfccName = linkfcc.getID().toString();
//                                int ResourceUsedOld = (Integer) Dmap.get(linkfccName);
//
//                                int ResourceUsedNum = 0;
//                                Dmap.put(linkfccName, ResourceUsedNum);
//                            }
//                            boolean LinkType = false;
//
//                            // 对于每一段光纤X，在故障光纤中进行遍历，看是否包含这段光纤X，若包含，则将该光纤X置为白色
//                            for (int i = 0; i < FaultSimulation.faultFiber.length; i++) {
//                                if (linkf
//                                        .equals(FaultSimulation.faultFiber[i][0]
//                                                + "_"
//                                                + FaultSimulation.faultFiber[i][1])
//                                        || linkf
//                                        .equals(FaultSimulation.faultFiber[i][1]
//                                                + "_"
//                                                + FaultSimulation.faultFiber[i][0])) {
//                                    LinkType = true;
//                                } else if (linkb
//                                        .equals(FaultSimulation.faultFiber[i][0]
//                                                + "_"
//                                                + FaultSimulation.faultFiber[i][1])
//                                        || linkb
//                                        .equals(FaultSimulation.faultFiber[i][1]
//                                                + "_"
//                                                + FaultSimulation.faultFiber[i][0])) {
//                                    LinkType = true;
//                                }
//                            }
//
//                            if (LinkType) { // 设置为故障的链路在故障删路后设置为白色
//                                LinkType = false;
//                                if (box.getElementByID(linkf) != null) {
//                                    ((Link) (box.getElementByID(linkf)))
//                                            .putLinkColor(Color.WHITE);
//                                    isdelDefinedLink(linkf);
//                                } else if (box.getElementByID(linkb) != null) {
//                                    ((Link) (box.getElementByID(linkb)))
//                                            .putLinkColor(Color.WHITE);
//                                    isdelDefinedLink(linkb);
//                                }
//                            } else {
//                                if (box.getElementByID(linkf) != null) {
//                                    ((Link) (box.getElementByID(linkf)))
//                                            .putLinkColor(Color.RED);
//                                    isdelDefinedLink(linkf);
//                                } else if (box.getElementByID(linkb) != null) {
//                                    ((Link) (box.getElementByID(linkb)))
//                                            .putLinkColor(Color.RED);
//                                    isdelDefinedLink(linkb);
//                                }
//                                //删路提示框
//                                JOptionPane.showMessageDialog(null, "删路成功", "删路提示", JOptionPane.ERROR_MESSAGE);
//                            }
//
//                            //
//                            if (RerouteFiberFault.FAULT_FIBER_LINK_DELETE
//                                    && ((linkf
//                                    .equals(AsonManager.definedLinkstr1))
//                                    || linkf
//                                    .equals(AsonManager.definedLinkstr2)
//                                    || linkb
//                                    .equals(AsonManager.definedLinkstr1) || linkb
//                                    .equals(AsonManager.definedLinkstr2))) {
//                                RerouteFiberFault.FAULT_FIBER_LINK_DELETE = false;
//                                if (box.getElementByID(linkf) != null) {
//                                    ((Link) (box.getElementByID(linkf)))
//                                            .putLinkColor(Color.WHITE);
//                                } else if (box.getElementByID(linkb) != null) {
//                                    ((Link) (box.getElementByID(linkb)))
//                                            .putLinkColor(Color.WHITE);
//
//                                }
//                            }
//
//                        }
//                    }
//                }
//                textarea.append("\n----------delpath4---------\n");
//                System.out.println("6666666666");
//                /*
//                    for (int i = 0; i < 30; i++) {
//
//						for (int k = 0; k < 30; k++) {
//							System.out.println("SpathD[" + i + "][" + k + "]"
//									+ NodeListenRunnable.SpathD[i][k]);
//						}
//						// System.out.println();
//
//
//					}
//					*/
//                clearselfpath(SpathD, fromNode, toNode, row);
//                textarea.append("\n----------delpath5---------\n");
//                System.out.println("middle.......");
//                for (int i = 0; i < 10; i++) {
//
//                    for (int k = 0; k < 15; k++) {
//                        System.out.println("SpathD[" + i + "][" + k + "]"
//                                + NodeListenRunnable.SpathD[i][k]);
//                    }
//                    // System.out.println();
//                }
//
//                clearunusablepath(SpathD, fromNode, toNode);
//
//                //针对1+1删路，处理保护路径   xc
//                for (int j = 0; j < 30; j++) {
//                    if (1 != dataArray[j][0] && fromNode == dataArray[j][2] && toNode == dataArray[j][3] && 0 != dataArray[j][6]) {
//                        int _hopnum = dataArray[j][6];//保护路径跳数
//                        for (int n = 1; n < _hopnum; n++) {
//                            int _srcnode = dataArray[j][6 + n];
//                            int _dstnode = dataArray[j][7 + n];
//                            String linkf = _srcnode + "_" + _dstnode;
//                            String linkb = _dstnode + "_" + _srcnode;
//                            boolean LinkType = false;
//                            for (int i = 0; i < FaultSimulation.faultFiber.length; i++) {
//                                if (linkf
//                                        .equals(FaultSimulation.faultFiber[i][0]
//                                                + "_"
//                                                + FaultSimulation.faultFiber[i][1])
//                                        || linkf
//                                        .equals(FaultSimulation.faultFiber[i][1]
//                                                + "_"
//                                                + FaultSimulation.faultFiber[i][0])) {
//                                    LinkType = true;
//                                } else if (linkb
//                                        .equals(FaultSimulation.faultFiber[i][0]
//                                                + "_"
//                                                + FaultSimulation.faultFiber[i][1])
//                                        || linkb
//                                        .equals(FaultSimulation.faultFiber[i][1]
//                                                + "_"
//                                                + FaultSimulation.faultFiber[i][0])) {
//                                    LinkType = true;
//                                }
//                            }
//
//                            if (LinkType) { // 设置为故障的链路在故障删路后设置为白色
//                                LinkType = false;
//                                if (box.getElementByID(linkf) != null) {
//                                    ((Link) (box.getElementByID(linkf)))
//                                            .putLinkColor(Color.WHITE);
//                                    isdelDefinedLink(linkf);
//                                } else if (box.getElementByID(linkb) != null) {
//                                    ((Link) (box.getElementByID(linkb)))
//                                            .putLinkColor(Color.WHITE);
//                                    isdelDefinedLink(linkb);
//                                }
//                            } else {
//                                if (box.getElementByID(linkf) != null) {
//                                    ((Link) (box.getElementByID(linkf)))
//                                            .putLinkColor(Color.RED);
//                                    isdelDefinedLink(linkf);
//                                } else if (box.getElementByID(linkb) != null) {
//                                    ((Link) (box.getElementByID(linkb)))
//                                            .putLinkColor(Color.RED);
//                                    isdelDefinedLink(linkb);
//                                }
//
//                            }
//
//                        }
//                        for (int r = 0; r < 30; r++) {
//                            dataArray[j][r] = 0;
//                        }
//                    }
//                    for (int i = 0; i < dataArray.length; i++) {
//                        for (int m = 0; m < dataArray[i].length; m++) {
//                            System.out.print(dataArray[i][m] + " ");
//                        }
//                        System.out.println();
//                    }
//                    System.out
//                            .println("---------ggggggggggggggggggggggggg");
//
//                }
//
//                // 给GMPLS一个资源预留缓冲时间
//                // try {
//                // Thread.sleep(100); // 1000ms
//                // } catch (Exception e) {
//                // e.printStackTrace();
//                //
//                // }
///*
//					textarea.append("\n----------delpath6---------\n");
//					System.out.println("Again");
//
//					for (int i = 0; i < 10; i++) {
//
//						for (int k = 0; k < 15; k++) {
//							System.out.println("SpathD[" + i + "][" + k + "]"
//									+ NodeListenRunnable.SpathD[i][k]);
//						}
//						// System.out.println();
//					}
//					*/
//                if (AsonManager.FIBER_FAULT_DEL) {
//                    AsonManager.FIBER_FAULT_DEL = false;
//                    AsonManager.FIBER_FAULT_REROUTE_DEL = true;
//                }
//                textarea.append("Del Path success,from" + fromNode + "to" + toNode
//                        + "\n");
//
//                for (int a = 0; a < packageBody.length; a++) {
//                    System.out.println("byte" + a + ":" + packageBody[a]);
//                }
//            } else {
//                textarea.append("Del Path error,from" + fromNode + "to" + toNode
//                        + "\n");
//            }
//        }
//    }
//
//    // 2013-11-22
//    // check whether the difinedLink is included and set the flag.
//    // if the faultFlag was set, then send a build message to fiber_client
//    // through socket
//    //
//    private void isdefinedLink(String slink) {
//        if (AsonManager.definedLinkstr1.equals(slink)
//                || AsonManager.definedLinkstr2.equals(slink)) {
//            AsonManager.faultFlag = true;
//
//            System.out.println("isdefined link... send B");
//            try {
//                String sendString = "B";
//                byte[] sendBytes = sendString.getBytes("UTF-8");
//
//                AsonManager.fiberFaultSock.getOutputStream().write(
//                        sendBytes);
//                System.out.println("send B finished..");
//            } catch (Exception e) {
//                // TODO: handle exception
//            }
//
//            // synchronized(AsonNetworkManager.isDefLink) {
//            // System.out.println("notify set path wait end...");
//            // AsonNetworkManager.isDefLink[0] = "true";
//            // AsonNetworkManager.isDefLink.notify();
//            // }
//        }
//    }
//    public void finishThread() {
//        exitflag[Node_ID] = false;
//    }
//
//    private void isdelDefinedLink(String slink) {
//        if (AsonManager.definedLinkstr1.equals(slink)
//                || AsonManager.definedLinkstr2.equals(slink)) {
//            AsonManager.faultFlag = false;
//            AsonManager.faultFlagDel = true;
//
//            if (!AsonManager.FIBER_FAULT_DEL) {
//                System.out.println("isdeldefined link... send D");
//                try {
//                    String sendString = "D";
//                    byte[] sendBytes = sendString.getBytes("UTF-8");
//                    AsonManager.fiberFaultSock.getOutputStream().write(
//                            sendBytes);
//                } catch (Exception e) {
//                    // TODO: handle exception
//                }
//            }
//
//            // synchronized(AsonNetworkManager.isDelDefLink) {
//            // System.out.println("notify delete path wait end...");
//            // AsonNetworkManager.isDefLink[0] = "false";
//            // AsonNetworkManager.isDelDefLink[0] = "true";
//            // AsonNetworkManager.isDelDefLink.notify();
//            // }
//            // FAULT_LINK_FLAT = true;
//        }
//    }
//
//}