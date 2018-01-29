//package Ason;
//
//import twaver.*;// java.twaver.TData
//import twaver.base.A.E.l;
//import twaver.network.*;//java.twaver.network.TNetwork
//import twaver.network.background.*;
//import twaver.table.*;//java.twaver.table.TElementTable
//import twaver.tree.*; //java.twaver.tree.TTree
//
//import java.awt.BorderLayout;
//import java.awt.Color;
//import java.awt.Component;
//import java.awt.GridLayout;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.KeyEvent;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.net.InetSocketAddress;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.net.SocketAddress;
//import java.text.NumberFormat;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Date;
//
//import javax.swing.*;
//import javax.swing.filechooser.FileNameExtensionFilter;
//import javax.swing.table.TableCellRenderer;
//
//import org.apache.commons.net.io.SocketOutputStream;
//
//import com.shghuang.ason.AsonManager;
//
//@SuppressWarnings("serial")
//public class AsonManager extends JFrame {
//
//	JFrame jf = new JFrame();
//	static TDataBox box = new TDataBox("ELements(Nodes and Links)");// private
//	static TDataBox diplay_box = new TDataBox("Traffic Display");// add by YuYu
//
//	public AsonManager myself; // ͨ��myself���Ե���TrafficManager,�������õ�����Traffic
//	static TNetwork network;
//	private TTree tree;
//	private int rrmsgSize;
//
//	public TrafficManager trafficManager = new TrafficManager(this);
//
//	private static JTabbedPane treePane = new JTabbedPane();
//	private static JPanel containpane = new JPanel(new BorderLayout());
//	private static JPanel networkpane = new JPanel(new BorderLayout());
//	private JPanel nodetablepane = new JPanel(new BorderLayout());
//	private JPanel linktablepane = new JPanel(new BorderLayout());
//
//	private static JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treePane, containpane);
//
//	final static int NODENUM = 320;// 36
//	static String isDefLink[] = { "false" };
//	static boolean faultFlag = false; // �Ƿ����definedLink true : ����
//	static boolean faultFlagDel = false; // �Ƿ�ɾ��������definedLink true : ��ɾ��
//	static String isDelDefLink[] = { "false" };
//
//	private boolean[][] bilinks = new boolean[NODENUM + 2][NODENUM + 2];
//
//	NetworkNode[] networkNode = new NetworkNode[NODENUM + 2];
//	// private boolean[] EXITFLAG = new boolean[NODENUM+2];//fancy
//	// Ϊflaseʱ��Ӧ�ڵ��NodeListenRunnable�̹߳ر�
//	// private boolean[][] bilink = new boolean[NODENUM + 2][NODENUM +
//	// 2];//2012-2-28 wj
//
//	private TElementTable nodetable;
//
//	final TDataBox Box_tr = new TDataBox();
//	final TElementTable Table_tr = new TElementTable(Box_tr);
//
//	private Node nodetemp;
//
//	static int SetPathNum = 0;
//	static int SetPathSuccessNum = 0;
//	static int DelPathNum = 0;
//	static int DelPathSuccessNum = 0;
//	static int[] definedLink = { 1, 4 }; // ����-������ ʵ�ڵ���·
//	static String definedLinkstr1 = definedLink[0] + "_" + definedLink[1];
//	static String definedLinkstr2 = definedLink[1] + "_" + definedLink[0];
//
//	private boolean pathflag = false;
//	private Node nodestart;
//	private Node nodefinal;
//
//	boolean getpointstate = false;
//	boolean ready_add;
//	boolean ready_del;
//	int LinkState_am = 0;
//	// ????Ϊɶ��+2
//	private Socket clntSock = new Socket();
//	private Socket faultSimulationSock = new Socket(); // ���ڹ��ϻָ�
//	static Socket fiberFaultSock = new Socket(); // ���ڹ��˹���
//	static Socket[] sockarr = new Socket[NODENUM + 2];
//
//	static boolean FIBER_FAULT_DEL = false;
//	static boolean FIBER_FAULT_REROUTE_DEL = false;// ��·��
//	static boolean FIBER_FAULT_REROUTE = false;
//
//	static boolean checkfault = false;
//	static String normal_fault_flag[] = { "false" }; // false:normal delete
//														// true: fault delete
//
//	// int[] faultDel = {1000,1000,1000,1000,1000};
//	public ArrayList<Traffic> faultDel = new ArrayList<Traffic>();
//	private byte[] rerouteMessage = new byte[4];
//
//	private static int trafficID = 0;
//
//	static JTextArea textarea = new JTextArea(12, 20);
//	JTextArea ta = new JTextArea(12, 20);
//
//	private JScrollPane scroll = new JScrollPane(ta);
//	private JPanel textpane1 = new JPanel();
//	private JPanel textpane2 = new JPanel();
//	private JPanel textpane3 = new JPanel();// 2012-4-9zhouyu
//	private JPanel textpane4 = new JPanel();// 2012-4-9zhouyu
//	private JPanel textpaneNorth = new JPanel(new GridLayout(4, 1));// 2012-4-9zhouyu
//	private JPanel textpane = new JPanel(new BorderLayout());
//	private JFormattedTextField tfstart = new JFormattedTextField(NumberFormat.getIntegerInstance());
//	private JFormattedTextField tfkill = new JFormattedTextField(NumberFormat.getIntegerInstance());
//	// 2012-4-9zhouyu
//	private JFormattedTextField tfstartArea = new JFormattedTextField(NumberFormat.getIntegerInstance());
//	private JFormattedTextField tfkillArea = new JFormattedTextField(NumberFormat.getIntegerInstance());
//	// 2012-4-9zhouyu
//
//	private static JFileChooser chooser = new JFileChooser(".");
//	private static FileNameExtensionFilter filter = new FileNameExtensionFilter("xml", "xml");
//	public static boolean setpathTimeFlag;
//	public static boolean setpathTimeFlag_one_one;
//	public static long setpathEndTime_one_one;
//	public static long setpathStartTime;
//	public static long setpathEndTime;
//	public static boolean fiberFaultRerouteTimeFlag;
//	public static long fiberFaultRerouteEnd;
//	public static long fiberFaultRerouteStart;
//	public static boolean[] faultSimulationRerouteTimeFlag;
//	public static long[] faultSimulationRerouteEnd;
//	public static long[] faultSimulationRerouteStart;
//	public static String[][] faultSimulationReroutePath;
//	public static long[] faultSimulationProtectionEnd;
//	public static boolean[] faultSimulationProtectionTimeFlag;
//	public static long[] faultSimulationProtectionStart;
//	public static String[][] faultSimulationProtectionPath;
//	public static int rerouteFromNode;
//	public static int rerouteToNode;
//
//	private Runnable nodes_data_listener; // for the thread of server accept.
//	public AsonManager am;
//
//	public AsonManager() {
//
//	}
//
//	// a node may have different views when it in different situation.
//	public void NodeRunFeel(Node srf_node) throws Exception { // ���ý��治ͬ������ʾ�Ľڵ�ͼ��
//		int aid = GetCP_Int(srf_node, "areaid");
//		if (aid == 1) {
//			srf_node.setImage("/images/blee.PNG");
//		} else if (aid == 2) {
//			srf_node.setImage("/images/coffee.PNG");
//		} else if (aid == 3) {
//			srf_node.setImage("/images/green.PNG");
//		} else if (aid == 4) {
//			srf_node.setImage("/images/green2.PNG");
//		} else if (aid == 5) {
//			srf_node.setImage("/images/green3.PNG");
//		} else if (aid == 6) {
//			srf_node.setImage("/images/hong.PNG");
//		} else if (aid == 7) {
//			srf_node.setImage("/images/lan.PNG");
//		} else if (aid == 8) {
//			srf_node.setImage("/images/lnn.PNG");
//		} else if (aid == 9) {
//			srf_node.setImage("/images/blue.PNG");
//		} else if (aid == 10) {
//			srf_node.setImage("/images/black.PNG");
//		}
//
//	}
//
//	public void NodeRunFeel_ConfigDom(Node srf_node, int aidconfigDom) {
//		if (aidconfigDom == 1) {
//			srf_node.setImage("/images/blee.PNG");
//		} else if (aidconfigDom == 2) {
//			srf_node.setImage("/images/coffee.PNG");
//		} else if (aidconfigDom == 3) {
//			srf_node.setImage("/images/green.PNG");
//		} else if (aidconfigDom == 4) {
//			srf_node.setImage("/images/green2.PNG");
//		} else if (aidconfigDom == 5) {
//			srf_node.setImage("/images/green3.PNG");
//		} else if (aidconfigDom == 6) {
//			srf_node.setImage("/images/hong.PNG");
//		} else if (aidconfigDom == 7) {
//			srf_node.setImage("/images/lan.PNG");
//		} else if (aidconfigDom == 8) {
//			srf_node.setImage("/images/lnn.PNG");
//		} else if (aidconfigDom == 9) {
//			srf_node.setImage("/images/blue.PNG");
//		} else if (aidconfigDom == 10) {
//			srf_node.setImage("/images/black.PNG");
//		}
//	}
//
//	public void NodePathSetFeel1(Node spsf_node) {
//		spsf_node.setImage("/images/nodepic3.png");
//	}
//
//	public void NodePathSetFeel2(Node spsf_node) {
//		spsf_node.setImage("/images/nodepic4.png");
//	}
//
//	public void NodeInitFeel(Node spsf_node) {
//		spsf_node.setImage("/images/nodepic.png");
//	} // add by xiaoliang
//
//	private void initSocket() {
//		Thread tlisten = new Thread(new Runnable() {
//
//			public void run() {
//				try {
//
//					ServerSocket listensock = new ServerSocket(1235);// �����ͻ���1235�˿�
//
//					// 2012-2-21 wj
//					// the form of str_addr is "/192.168.x.x:xxxxx" !
//					// 01234567890
//					int stripbit3 = 9;// Str ip bit 3
//					int stripbit3len = 10;// Str ip bit 3 len
//					String stripbit3string = null;// Str ip bit 3 string =null
//					String addripbit3 = "1";// address ip bit 3
//					int stripbit4 = 11;// Str ip bit 4=11
//					int stripbit4len = 0;// str ip bit 4 len =0;
//					String stripbit4string = null;
//					int nodesum = 0;
//
//					while (true) {
//						ta.append("waiting...");
//						// System.out.println("initSocket start work");
//						Socket a = listensock.accept();// ���������ܵ����׽��ֵ�����
//						// ta.append("accepted...computing");
//						InetSocketAddress ipaddr = (InetSocketAddress) a.getRemoteSocketAddress();// ���ش��׽������ӵĶ˵�ĵ�ַ,SocketAddress��InetSocketAddress�ĸ���
//						String str_addr = ipaddr.toString();
//						// System.out.println("str_addr��ֵ"+str_addr);
//						// 2012-2-21 wj
//						stripbit3string = str_addr.substring(stripbit3, stripbit3len);// substring(9,10),"/192.168.X.x:xxxxx"ȡ����һ��X
//						stripbit4len = str_addr.indexOf(':');// ���أ�����λ������:stripbit4len=12
//						stripbit4string = str_addr.substring(stripbit4, stripbit4len);// substring(11,12),"/192.168.x.X:xxxxx"ȡ���ڶ���X
//						int indexofnode = Integer.parseInt(stripbit4string);// ���ڵ�����
//						if (stripbit3string.equalsIgnoreCase(addripbit3)) {
//							indexofnode += 150;
//						} else if (200 == indexofnode)// 2012-03-11 wj
//							indexofnode = 100;
//
//						String nodestring;
//						nodestring = "n" + indexofnode;
//
//						NetworkNode networkNode_tem = networkNode[indexofnode];
//
//						if (networkNode_tem == null) {
//							networkNode_tem = new NetworkNode((Node) (box.getElementByID(nodestring)), indexofnode);
//							networkNode[indexofnode] = networkNode_tem;
//							// System.out.println("We have got node
//							// "+indexofnode);
//						}
//
//						networkNode_tem.setControlSocket(a);
//
//						nodesum++;
//						ta.append(
//								str_addr.substring(1, stripbit4len) + " has connected!" + "nodesum:" + nodesum + "\n");
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//
//		tlisten.start();
//	}
//
//	private void initRerouteThread() {// ���ϴ���
//
//		Thread rerouteListen = new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				try {
//					ServerSocket servSock = new ServerSocket(7980);
//					clntSock = servSock.accept();
//					SocketAddress clientaAddress = clntSock.getRemoteSocketAddress();
//					System.out.println("the fiber node ip is: " + clientaAddress);
//					while (true) {
//
//						InputStream in = clntSock.getInputStream();
//						OutputStream out = clntSock.getOutputStream();
//
//						/**
//						 * if definedLink was included when set path. send a
//						 * message to fiber_client ����wait()/notify()���ƽ����̵߳�ִ�п���
//						 * NodeListenRunnable���ڴ���CC�ظ�����Ϣʱ,
//						 * �����⵽������·���������²�ʵ�ʹ������ڵ���·��ʱ��
//						 * ��isdefinedLink()��ͨ��notify()��������wait���̣߳�
//						 * ��·֪ͨ������isDefLink��־λ
//						 */
//
//						synchronized (isDefLink) {// ��ʼֵΪfalse
//							while (isDefLink[0] != "true") {
//								System.out.println("set path begin waiting...");
//								try {
//									isDefLink.wait();
//								} catch (Exception e) {
//									// TODO: handle exception
//								}
//								System.out.println("end of set path waiting");
//								String sendString = "B"; // 'B' means a link
//															// path was built
//								byte[] sendBytes = sendString.getBytes();
//								System.out.println("B send...");
//								out.write(sendBytes);
//								System.out.println("B send...finished");
//								out.flush();
//							}
//						}
//
//						Thread.sleep(2000);
//
//						// ******* deal with fault request ******
//
//						while ((rrmsgSize = in.read(rerouteMessage)) != -1) {
//							String recvString = new String(rerouteMessage);
//							System.out.println("***********: " + recvString + " :******");
//							String sendString = "f";
//							byte[] sendBytes = sendString.getBytes("UTF-8");
//							System.err.println("F SEND Mana-----");
//							out.write(sendBytes);
//							out.flush();
//
//							if (recvString.trim().equals("F") && (AsonManager.faultFlag)) {
//
//								// findFaultLink(NodeListenRunnable.SpathD);
//
//								/*
//								 * System.out.println("print all the link"); for
//								 * (int i = 0; i < NODENUM + 2; i++) { if (0 !=
//								 * NodeListenRunnable.SpathD[i][0]) {
//								 * System.out.println("row: " + i + " is " +
//								 * NodeListenRunnable.SpathD[i][0]+"->"+
//								 * NodeListenRunnable.SpathD[i][1]); } }
//								 *
//								 * for (int i = 0; i < faultDel.length; i++) {
//								 * System.out.println("faultDel[" + i + "]: " +
//								 * faultDel[i]); }
//								 */
//							}
//
//							if (checkfault) { // �й�����·
//
//								int fnrow;
//								/*
//								 * for (int i = 0; i < faultDel.size(); i++) {
//								 * if (faultDel[i] != 1000) { fnrow =
//								 * faultDel[i];
//								 *
//								 * //���ù�����·����ʽ
//								 * setFaultLinkStyle(NodeListenRunnable.SpathD,
//								 * NodeListenRunnable.setpathmatrx ,fnrow); } }
//								 */
//								// �������ù�����·��ʽʱ�ı��setpathmatrix[][]ֵ���лָ� /
//								// recover the value of setpathmatrix[][]
//								/*
//								 * for (int i = 0; i < faultDel.size(); i++) {
//								 *
//								 * if (faultDel[i] != 1000) { fnrow =
//								 * faultDel[i];
//								 * recoverSetpathmatrixValue(NodeListenRunnable.
//								 * SpathD,NodeListenRunnable.setpathmatrx
//								 * ,fnrow); } }
//								 *
//								 */
//								// sleep 5s (��ʾ�������) Ȼ���ٽ���ɾ·����·�Ĳ���
//								try {
//									Thread.sleep(5000);
//								} catch (Exception e) {
//
//								}
//
//								// delete the fault link
//								int index = 0; // for tempFaultLink
//								for (int i = 0; i < faultDel.size(); i++) {
//
//									fnrow = faultDel.get(i).src_id;
//									/*
//									 * int fn =
//									 * NodeListenRunnable.SpathD[fnrow][0];
//									 * //��·�׽ڵ�ID String fnstr = "n" +
//									 * String.valueOf(fn);
//									 *
//									 * int tn =
//									 * NodeListenRunnable.SpathD[fnrow][1];
//									 * //��·ĩ�ڵ�ID String tnstr = "n" +
//									 * String.valueOf(tn);
//									 *
//									 * tempFaultLink[index][0] = fn;
//									 * tempFaultLink[index][1] = tn; index++;
//									 *
//									 * //System.out.println("******************"
//									 * +fn+ "->"+tn); Node fnode =
//									 * (Node)(box.getElementByID(fnstr));
//									 * //��ȡ�׽ڵ� Node tnode =
//									 * (Node)(box.getElementByID(tnstr));
//									 *
//									 * System.out.println("fnode: "
//									 * +fnode.getName()+"->tnode: "
//									 * +tnode.getName()); int fnarea =
//									 * GetCP_Int(fnode, "areaid"); //�׽ڵ������� int
//									 * tnarea = GetCP_Int(tnode, "areaid");
//									 * //Ŀ�ĵ�������
//									 *
//									 * System.out.println("fn: " + fn +
//									 * "; fnarea: " + fnarea);
//									 * System.out.println("tn: " + tn +
//									 * "; tnarea: " + tnarea);
//									 * System.out.println("delpath run... ");
//									 *
//									 * rerouteFromNode = fn; rerouteToNode = tn;
//									 *
//									 * //��һɾ��������· delpath(fn, fnarea, tn,
//									 * tnarea);
//									 */
//								}
//								// faultFlag = false;
//								// rebuilt the new path
//
//								synchronized (isDelDefLink) {
//									while (isDelDefLink[0] != "true" && checkfault) {
//										isDelDefLink[0] = "false";
//										System.out.println("fault delete path begin waiting...");
//										try {
//											isDelDefLink.wait();
//										} catch (Exception e) {
//											System.out.println("fault delete waiting error.");
//										}
//										System.out.println("end of delete path waiting");
//										String sendDeleteString = "D"; // 'D'
//																		// means
//																		// a
//																		// link
//																		// path
//																		// was
//																		// built
//										System.out.println("D send...");
//										byte[] sendDeleteBytes = sendDeleteString.getBytes("UTF-8");
//										out.write(sendDeleteBytes);
//										System.out.println("D send...finished");
//										out.flush();
//									}
//								}
//
//								try {
//									Thread.sleep(5000); // ��5s���ٽ������½�·
//								} catch (Exception e) {
//
//								}
//								/*
//								 * if(checkfault) { //����й��ϣ�����лָ�
//								 *
//								 * for (int i = 0; i < tempFaultLink.length;
//								 * i++) { for (int j = 0; j <
//								 * tempFaultLink[i].length; j++) {
//								 * System.out.println("tempFaultLink["+i+"]["+j+
//								 * "]: "+tempFaultLink[i][j]); } }
//								 *
//								 * for (int i = 0; i < tempFaultLink.length;
//								 * i++) { if (1000 != tempFaultLink[i][0]) {
//								 *
//								 *
//								 * int fn = tempFaultLink[i][0]; //Դ�ڵ� String
//								 * fnstr = "n" + String.valueOf(fn);
//								 *
//								 * int tn = tempFaultLink[i][1]; //�޽ڵ� String
//								 * tnstr = "n" + String.valueOf(tn);
//								 *
//								 * Node fnode =
//								 * (Node)(box.getElementByID(fnstr)); Node tnode
//								 * = (Node)(box.getElementByID(tnstr));
//								 *
//								 * System.out.println("fnode: "+fnode.getName()+
//								 * "->tnode: "+tnode.getName()); int fnarea =
//								 * GetCP_Int(fnode, "areaid"); int tnarea =
//								 * GetCP_Int(tnode, "areaid");
//								 *
//								 * try { int j = findusablepath(SpathD);
//								 * //Ѱ�ҿɴ�Ŵ���·����һ�� SpathD[j][0] = fn;
//								 * SpathD[j][1] = tn; SpathD[j][2] = -1;
//								 * setpath_in(fn, tn, fnarea, tnarea); } catch
//								 * (Exception e) { // TODO: handle exception }
//								 *
//								 * } }
//								 */
//							}
//							/*
//							 * //��־λ�ָ� //tempFaultLink[i][j] ��־λ�ָ� for (int i =
//							 * 0; i < tempFaultLink.length; i++) { for (int j =
//							 * 0; j < tempFaultLink[i].length; j++) {
//							 * tempFaultLink[i][j] = 1000; } } checkfault =
//							 * false; for (int i = 0; i < faultDel.length; i++)
//							 * { faultDel[i] = 1000; }
//							 */
//							// }
//						} // end of fault dealing
//
//					}
//
//				} catch (Exception e) {
//					// TODO: handle exception
//				}
//
//			}
//		});
//		rerouteListen.start();
//
//	}
//
//	/*
//	 * protected void findFaultLink(ArrayList<Traffic> run) { int index = 0;
//	 * if(run.size()>0) { for(Traffic _runtraffic : run ) { for(int i = 0; i
//	 * <_runtraffic.hopNum;i++) { if(_runtraffic.hop_nodes.get(i)==
//	 * definedLink[0]&&_runtraffic.hop_nodes.get(i+1)== definedLink[1]) {
//	 * checkfault = true; faultDel.add(_runtraffic); index++; break; } else
//	 * if(_runtraffic.hop_nodes.get(i+1)==
//	 * definedLink[1]&&_runtraffic.hop_nodes.get(i)== definedLink[0]) {
//	 * checkfault = true; faultDel.add(_runtraffic); index++; break; } } } } }
//	 */
//	// protected void setFaultLinkStyle(int path[ ][ ],int pathmatrix[][], int
//	// fnrow){
//
//	// }
//
//	public void initDataSocketServerThread() {
//
//		nodes_data_listener = new Runnable() {
//			public void run() {
//				try {
//					ServerSocket s = new ServerSocket(1234, 400); // wj
//
//					// 2012-2-21 wj
//					// the form of str_addr is "/192.168.x.x:xxxxx" !
//					int stripbit3 = 9;
//					int stripbit3len = 10;
//					String stripbit3string = null;
//					String addripbit3 = "1";
//					int stripbit4 = 11;
//					int stripbit4len = 0;
//					String stripbit4string = null;
//					// int nodesum=0;
//					//
//
//					while (true) {
//						Socket a = s.accept();
//						InetSocketAddress ipaddr = (InetSocketAddress) a.getRemoteSocketAddress();
//						String str_addr = ipaddr.toString();
//						// System.out.println("initWaitThread start work");
//						// 2013-6-18 wj
//						// nodesum++;
//						// ta.append(str_addr + " ready :" + nodesum);
//						// 2012-2-21 wj
//						stripbit3string = str_addr.substring(stripbit3, stripbit3len);
//						stripbit4len = str_addr.indexOf(':');
//						stripbit4string = str_addr.substring(stripbit4, stripbit4len);
//						int indexofnode = Integer.parseInt(stripbit4string);
//						if (stripbit3string.equalsIgnoreCase(addripbit3)) {
//							indexofnode += 150;
//						} else if (200 == indexofnode)// 2012-03-11 wj
//							indexofnode = 100;
//
//						String nodestring;
//						nodestring = "n" + indexofnode;
//
//						NetworkNode networkNode_tem = networkNode[indexofnode];
//
//						if (networkNode_tem == null) {
//							networkNode_tem = new NetworkNode((Node) (box.getElementByID(nodestring)), indexofnode);
//							networkNode[indexofnode] = networkNode_tem;
//						}
//
//						networkNode_tem.setDataSocket(a);
//
//						NodeRunFeel(networkNode_tem.tv_node);
//
//						// change the box status of the node that's been
//						// discovered.
//						networkNode_tem.tv_node.putClientProperty("run", true);
//
//						// System.out.println("the num is:"+indexofnode+" has
//						// connet the data socket");
//
//						networkNode[indexofnode] = networkNode_tem;
//
//						networkNode_tem.setFatherAsonManager(myself);
//						Thread t = new Thread(networkNode_tem);
//						t.start();
//
//						/*
//						 * need to modify //���fiberFaultSock����fiber fault
//						 * simulation Thread ti = new Thread(new
//						 * NodeListenRunnable( indexofnode, textarea, box,
//						 * sockarr,bilink,SpathD,setpathmatrx,dataArray,EXITFLAG))
//						 * ;//2012-2-28 wj ti.start();
//						 */
//					}
//
//				} catch (IOException e) {
//					e.printStackTrace();
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		};
//	}
//
//	/**
//	 * TDataBox���ýڵ�λ�ú�������Ϣ
//	 */
//	private void setDataBox() {
//		try {
//			// box.parse("/topo-new.xml");
//			box.parse("/test320.xml");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * 2013-12-02,����˽�������
//	 */
//	public void initRerouteFiberFaultThread() {
//		Thread fiberFaultListen = new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				try {
//					ServerSocket serverSocket = new ServerSocket(8000);
//					Thread.sleep(1000);
//					while (true) {
//						fiberFaultSock = serverSocket.accept();
//
//						InetSocketAddress ipaddr = (InetSocketAddress) fiberFaultSock.getRemoteSocketAddress();
//						String str_addr = ipaddr.toString();
//						System.out.println("fiberFaultSock:" + str_addr);
//					}
//				} catch (Exception e) {
//					// TODO: handle exception
//				}
//
//			}
//		});
//		fiberFaultListen.start();
//	}
//
//	/**
//	 * 2013-12-02,�������Ͻ�����·ѡ����Ϊ���ϻָ�����·��Ȼ�����·���PCE�ͻ���
//	 */
//	public void initFaultRecoveryThread() {
//		Thread faultSimulationListen = new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				try {
//					ServerSocket serverSocket = new ServerSocket(7999);
//					Thread.sleep(1000);
//					while (true) {
//						faultSimulationSock = serverSocket.accept();
//
//						// InetSocketAddress ipaddr = (InetSocketAddress)
//						// faultSimulationSock.getRemoteSocketAddress();
//						// String str_addr = ipaddr.toString();
//						// System.out.println("faultSimulationSock:" +
//						// "YUYU---0413"+str_addr);
//					}
//				} catch (Exception e) {
//					// TODO: handle exception
//				}
//
//			}
//		});
//
//		faultSimulationListen.start();
//	}
//
//	// show the view that you want in the right window of the top frame.
//	private void shownodetable() {
//		containpane.removeAll();
//		containpane.add(nodetablepane, BorderLayout.CENTER);
//		containpane.updateUI();
//	}
//
//	private void showlinktable() {
//		containpane.removeAll();
//		containpane.add(linktablepane, BorderLayout.CENTER);
//		containpane.updateUI();
//	}
//
//	private void shownetwork() {
//		containpane.removeAll();
//		containpane.add(networkpane, BorderLayout.CENTER);
//		containpane.updateUI();
//	}
//
//	/*
//	 * private void sendMsg(Socket s, byte m) { try {
//	 *
//	 * byte[] b = new byte[20]; for (int i = 0; i < 20; i++) { b[i] = (byte) 0;
//	 * } b[0] = 1; b[1] = 0; b[2] = m; b[3] = 0; s.getOutputStream().write(b); }
//	 * catch (Exception e) { e.printStackTrace(); } }
//	 */
//
//	/**
//	 * Ason����control�˵���
//	 */
//	private void setMenuAndTool() {
//
//		// -------------------Menu Action �������ڲ������ʽ�����˵�������
//		/**
//		 * �����нڵ㰴ť
//		 */
//		Action start = new AbstractAction("Start All") {
//			public void actionPerformed(ActionEvent ae) {
//
//				for (int i = 1; i < (NODENUM + 1); i++) {
//					try {
//						Thread.sleep(100);// ���������100����100���룬Ҳ����0.1��
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//
//					if (networkNode[i] != null) {
//						// ta.append("send start msg to" + i + "\n");
//						networkNode[i].sendContrlMsg(NetworkNode.START_CMD);
//					}
//
//				}
//			}
//		};
//		/**
//		 * �ر����нڵ㰴ť
//		 */
//		Action kill = new AbstractAction("Kill All") {
//			public void actionPerformed(ActionEvent ae) {
//				for (int i = 1; i < (NODENUM + 1); i++) {
//
//					if (networkNode[i] != null) {
//						// ta.append("send kill msg to" + i + "\n");
//						networkNode[i].sendContrlMsg(NetworkNode.KILL_CMD);
//						String nodestring1 = "n" + i;
//
//						trafficManager.cleanAllTraffic();// @TODO
//															// �������ҵ����࣬��Ҫ������Ӻ���
//						// qqqq Arrays.fill(setpathmatrx[i],0);
//						Arrays.fill(bilinks[i], false);
//						// EXITFLAG[i] = false;//fancy
//						for (int j = 1; j < (NODENUM + 1); j++)// 2012-2-28 wj
//						{
//							String linkf = i + "_" + j;
//							String linkb = j + "_" + i;
//							if (box.getElementByID(linkf) != null) {
//								((Link) (box.getElementByID(linkf))).putLinkColor(Color.BLUE);
//							} else if (box.getElementByID(linkb) != null) {
//								((Link) (box.getElementByID(linkb))).putLinkColor(Color.BLUE);
//							}
//						}
//
//						NodeInitFeel((Node) (box.getElementByID(nodestring1)));
//					}
//				}
//			}
//		};
//
//		/**
//		 * Traffic Generate��ť��ӹ���@TODO ��Ҫ���ƣ�����Ϊ��
//		 */
//
//		Action traff_gen = new AbstractAction("Traffic Generate") {
//			public void actionPerformed(ActionEvent ae) {
//
//				/*
//				 * need to modify 20160707 TrafficGenetate tra = new
//				 * TrafficGenetate(); tra.actionPerformed(ae); textarea.append(
//				 * "Traffic Generate\n");
//				 */
//			}
//
//		};
//
//		/**
//		 * Fault Simulation���ܰ�ť
//		 */
//		Action fault_simulation = new AbstractAction("Fault Simulation", TWaverUtil.getImageIcon("/images/fault.png")) {
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				// need to modify 20160707
//				System.out.println("fault_simulation...��ť����");
//				// �����ȵȽ�����socket���Ӻ����ִ��FaultSimulation�Ĺ��췽��������faultSimulationSock��Ϊnull
//				this.setEnabled(false);
//
//				FaultGenerator faultSimulation = new FaultGenerator(faultSimulationSock, sockarr, am);// faultSimulationSock,
//																										// sockarr);
//				faultSimulation.actionPerformed(e);
//				textarea.append("Fault Simulation..." + "\n");
//				this.setEnabled(true);// */
//			}
//		};
//
//		// fiber fault simulation
//		Action fiber_fault = new AbstractAction("Fiber Fault Simulation",
//				TWaverUtil.getImageIcon("/images/recovery.png")) {
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//
//				System.out.println("fiber fault simulation...");
//				this.setEnabled(false);
//
//				// need to modify 20160707
//				// RerouteFiberFault rerouteFiberFault = new
//				// RerouteFiberFault(fiberFaultSock, sockarr);
//				// RerouteFiberFault rerouteFiberFault = new
//				// RerouteFiberFault();
//				// rerouteFiberFault.actionPerformed(e);
//				textarea.append("Fiber Fault Simulation..." + "\n");
//				this.setEnabled(true);
//			}
//		};
//
//		// 2014-12-11 by YuYu
//		// traffic display
//		Action traffic_display = new AbstractAction("Traffic Display", TWaverUtil.getImageIcon("/images/load.png")) {
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				System.out.println("Traffic Display...");
//				this.setEnabled(false);
//				// need to modify 20160707
//				// TrafficDisplay trafficdisplay = new
//				// TrafficDisplay(trafficdisplaySock, sockarr);
//				// trafficdisplay.actionPerformed(e);
//				textarea.append("Traffic Display...");
//				this.setEnabled(true);
//			}
//		};
//
//		Action exit = new AbstractAction("Exit", TWaverUtil.getImageIcon("/images/exit.png")) {
//			public void actionPerformed(ActionEvent e) {
//
//				// 2012-2-23 wj
//				int i = 0;
//				for (i = 1; i < NODENUM + 1; i++) {
//
//					try {
//						networkNode[i].closeAllSocket();
//						// 2013-11-27
//						// clntSock.close();
//						// fiberFaultSock.close();
//						// faultSimulationSock.close();
//					} catch (Exception e2) {
//						// TODO: handle exception
//					}
//				}
//				//
//
//				System.exit(0);
//			}
//		};
//
//		Action listenmn = new AbstractAction("Search NE", TWaverUtil.getImageIcon("/images/search.png")) {
//
//			public void actionPerformed(ActionEvent e) {
//
//				Thread t = new Thread(nodes_data_listener);
//				t.start();
//				System.out.println("Search NE start");
//				this.setEnabled(false);
//			}
//		};
//
//		Action addpath = new AbstractAction("Set Path", TWaverUtil // ��·��ť
//				.getImageIcon("/images/add.png")) {
//			public void actionPerformed(ActionEvent ae) {
//				LinkState_am = 1;
//				Traffic.LinkState = LinkState_am;
//			}
//		};
//
//		Action addpathParallel = new AbstractAction("Parallel Set Path", TWaverUtil.getImageIcon("/images/add.png")) {
//			public void actionPerformed(ActionEvent ae) {
//				LinkState_am = 3;
//				Traffic.LinkState = LinkState_am;
//			}
//		};
//
//		Action addpathSequence = new AbstractAction("Sequence Set Path", TWaverUtil.getImageIcon("/images/add.png")) {
//			public void actionPerformed(ActionEvent ae) {
//				LinkState_am = 4;
//				Traffic.LinkState = LinkState_am;
//			}
//		};
//
//		Action addpathTreetype = new AbstractAction("TreeType Set Path", TWaverUtil.getImageIcon("/images/add.png")) {
//			public void actionPerformed(ActionEvent ae) {
//				LinkState_am = 5;
//				Traffic.LinkState = LinkState_am;
//			}
//		};
//		Action delpath = new AbstractAction("Del Path", TWaverUtil.getImageIcon("/images/delete.png")) {
//			public void actionPerformed(ActionEvent ae) {
//				LinkState_am = 2;
//				Traffic.LinkState = LinkState_am;
//			}
//		};
//
//		Action stoppath = new AbstractAction("Stop Set/Del", TWaverUtil.getImageIcon("/images/stop.png")) {
//			public void actionPerformed(ActionEvent ae) {
//				LinkState_am = 0;
//				Traffic.LinkState = LinkState_am;
//			}
//		};
//
//		Action shownodetable = new AbstractAction("Node Table", TWaverUtil.getImageIcon("/images/nodetable.png")) {
//			public void actionPerformed(ActionEvent ae) {
//				shownodetable();
//			}
//		};
//
//		Action showlinktable = new AbstractAction("Link Table", TWaverUtil.getImageIcon("/images/linktable.png")) {
//			public void actionPerformed(ActionEvent ae) {
//				showlinktable();
//			}
//		};
//
//		Action shownetwork = new AbstractAction("Network", TWaverUtil.getImageIcon("/images/netview.png")) {
//			public void actionPerformed(ActionEvent ae) {
//				shownetwork();
//			}
//		};
//
//		Action setrate = new AbstractAction("SuccessRate", TWaverUtil.getImageIcon("/images/successrate.png")) {
//			public void actionPerformed(ActionEvent ae) {
//				double drate = 0;
//				if (SetPathNum != 0)
//					drate = SetPathSuccessNum * 1.0 / SetPathNum; // ������Ĭ����double�͵�
//				textarea.append("success rate is:" + drate + "\n");
//			}
//		};
//
//		Action getpoint = new AbstractAction("Getpoint", TWaverUtil.getImageIcon("/images/getpoint.png")) {
//			public void actionPerformed(ActionEvent ae) {
//				getpointstate = true;
//			}
//		};
//
//		Action cancelpoint = new AbstractAction("Cancelpoint", TWaverUtil.getImageIcon("/images/cancelpoint.png")) {
//			public void actionPerformed(ActionEvent ae) {
//				getpointstate = false;
//			}
//		};
//
//		Action about = new AbstractAction("About", TWaverUtil.getImageIcon("/images/profile.png")) {
//			public void actionPerformed(ActionEvent ae) {
//				String message = "													\n"
//						+ "*************************************************************\n" + "\n"
//						+ "Aim: Network Management of ASON\n" + "\n" + "Author: LI ShiPeng.\n" + "\n"
//						+ "CopyRight: AON(Advanced Optical Network) Lab at BUPT.";
//				JOptionPane.showMessageDialog(jf, message, "About This Program", JOptionPane.QUESTION_MESSAGE);
//			}
//
//		};
//
//		/* -------------------Menu Bar-------------------------------------- */
//		JMenuBar menubar = new JMenuBar();
//		setJMenuBar(menubar);
//
//		JMenu menuFile = new JMenu("File");
//		menubar.add(menuFile);
//		JMenu menuNet = new JMenu("Network");
//		menubar.add(menuNet);
//		// JMenu menuConfig = new JMenu("Config");
//		// menubar.add(menuConfig);
//		JMenu menuPath = new JMenu("Path");
//		menubar.add(menuPath);
//		JMenu menuView = new JMenu("View");
//		menubar.add(menuView);
//		JMenu menuMa = new JMenu("MAgent");
//		menubar.add(menuMa);
//		JMenu menuTest = new JMenu("Control");
//		menubar.add(menuTest);
//		JMenu menuHelp = new JMenu("Help");
//		menubar.add(menuHelp);
//
//		// Menu File
//		JMenuItem menuItemOpen = new JMenuItem("Open");
//		menuItemOpen.addActionListener(new FileOpenListener());
//		menuFile.add(menuItemOpen);
//		JMenuItem menuItemClose = new JMenuItem(exit);
//		menuFile.add(menuItemClose);
//
//		// Menu Network
//		JMenuItem menuItemGetInfo = new JMenuItem(listenmn);
//
//		menuNet.add(menuItemGetInfo);
//		JMenuItem menuItemSetRate = new JMenuItem(setrate);
//
//		menuNet.add(menuItemSetRate);
//
//		// Menu Path.
//		JMenuItem menuItemAddPath = new JMenuItem(addpath);
//
//		menuPath.add(menuItemAddPath);
//
//		JMenuItem menuItemAddPathParallel = new JMenuItem(addpathParallel);
//		menuPath.add(menuItemAddPathParallel);
//
//		JMenuItem menuItemAddPathSequence = new JMenuItem(addpathSequence);
//		menuPath.add(menuItemAddPathSequence);
//
//		JMenuItem menuItemAddPathTreetype = new JMenuItem(addpathTreetype);
//		menuPath.add(menuItemAddPathTreetype);
//
//		JMenuItem menuItemDelPath = new JMenuItem(delpath);
//		menuPath.add(menuItemDelPath);
//
//		JMenuItem menuItemStopPath = new JMenuItem(stoppath);
//		menuPath.add(menuItemStopPath);
//
//		// Menu View.
//		JMenuItem menuItemNodeTable = new JMenuItem(shownodetable);
//
//		menuView.add(menuItemNodeTable);
//		JMenuItem menuItemLinkTable = new JMenuItem(showlinktable);
//
//		menuView.add(menuItemLinkTable);
//		JMenuItem menuItemNetwork = new JMenuItem(shownetwork);
//
//		menuView.add(menuItemNetwork);
//
//		// Menu MA.
//		JMenuItem menuItemSendAgent = new JMenuItem("SendAgent");
//		menuItemSendAgent.addActionListener(new SendAgentListener());
//		menuMa.add(menuItemSendAgent);
//
//		// JMenuItem menuItemConfigDom = new JMenuItem("ConfigDomain");
//		// menuItemConfigDom.addActionListener(new ConfigDomListener());
//		// menuMa.add(menuItemConfigDom);
//
//		// Menu Control
//		JMenuItem menuItemGetPoint = new JMenuItem(start);
//		menuTest.add(menuItemGetPoint);
//
//		JMenuItem menuItemCancelPoint = new JMenuItem(kill);
//		menuTest.add(menuItemCancelPoint);
//
//		JMenuItem menuItemgen = new JMenuItem(traff_gen);
//		menuTest.add(menuItemgen);
//
//		JMenuItem menuItemTrafficDis = new JMenuItem("Traffic Display");
//		menuItemTrafficDis.addActionListener(new TrafficDisListener());
//		menuTest.add(menuItemTrafficDis);
//		// ���LayerManager��ť
//		JMenuItem menuItemLayerCol = new JMenuItem("LayerManager");
//		menuItemLayerCol.addActionListener(new LayerColListener());
//		menuTest.add(menuItemLayerCol);
//
//		// Menu Help.
//		JMenuItem menuItemAbout = new JMenuItem(about);
//
//		menuHelp.add(menuItemAbout);
//
//		// ---------------------Tool Bar-----------------------------------
//		JToolBar toolbar = new JToolBar("");
//		add(toolbar, BorderLayout.NORTH);
//		toolbar.setFloatable(true);
//		toolbar.addSeparator();
//		toolbar.add(listenmn);
//		listenmn.putValue(Action.SHORT_DESCRIPTION, "Search NE");
//		toolbar.add(setrate);
//		setrate.putValue(Action.SHORT_DESCRIPTION, "SuccessRate");
//		toolbar.addSeparator();
//		toolbar.add(addpath);
//		addpath.putValue(Action.SHORT_DESCRIPTION, "Set Path");
//		toolbar.add(delpath);
//		delpath.putValue(Action.SHORT_DESCRIPTION, "Del Path");
//
//		// 2013-11-27
//		toolbar.add(fault_simulation);
//		fault_simulation.putValue(Action.SHORT_DESCRIPTION, "Fault Simulation");
//		toolbar.addSeparator();
//
//		toolbar.add(fiber_fault);
//		fiber_fault.putValue(Action.SHORT_DESCRIPTION, "Fiber Fault Simulation");
//		toolbar.addSeparator();
//
//		toolbar.add(traffic_display);
//		traffic_display.putValue(Action.SHORT_DESCRIPTION, "Traffic Display");
//		toolbar.addSeparator();
//
//		toolbar.add(stoppath);
//		stoppath.putValue(Action.SHORT_DESCRIPTION, "Stop set/del");
//		toolbar.addSeparator();
//		toolbar.add(shownodetable);
//		shownodetable.putValue(Action.SHORT_DESCRIPTION, "Node Table");
//		toolbar.add(showlinktable);
//		showlinktable.putValue(Action.SHORT_DESCRIPTION, "Link Table");
//		toolbar.add(shownetwork);
//		shownetwork.putValue(Action.SHORT_DESCRIPTION, "Network");
//		toolbar.addSeparator();
//		toolbar.add(getpoint);
//		getpoint.putValue(Action.SHORT_DESCRIPTION, "GetPoint");
//		toolbar.add(cancelpoint);
//		cancelpoint.putValue(Action.SHORT_DESCRIPTION, "CancelPoint");
//		toolbar.addSeparator();
//		toolbar.add(about);
//		about.putValue(Action.SHORT_DESCRIPTION, "About");
//		toolbar.add(exit);
//		exit.putValue(Action.SHORT_DESCRIPTION, "Exit");
//	}
//
//	public void startIconThread() {
//
//		Thread tnode = new Thread(new Runnable() {
//			public void run() {
//				// System.out.println("startIconThread start work");
//				while (true) {
//					try {
//						Thread.sleep(3000);
//						if (pathflag) {
//							NodeRunFeel(nodestart);
//							NodeRunFeel(nodefinal);
//							pathflag = false;
//						}
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		});
//		tnode.start();
//
//	}
//
//	private void makeNodeTable() {
//		nodetable = new TElementTable(box) {
//			public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
//				Component component = super.prepareRenderer(renderer, row, col);
//				// ͨ����ѯ row��column ����Ԫ��ֵ������ģ�ͺ͵�Ԫ��ѡ��״̬��׼����Ⱦ��
//				setGradientRowColor(component, row, col);
//
//				return component;
//			}
//
//			public void setGradientRowColor(Component component, int row, int col) {
//				if (component instanceof JComponent) {
//					JComponent comp = (JComponent) component;
//					comp.setOpaque(true);
//
//					Node rownode = (Node) nodetable.getElementByRowIndex(row);
//
//					if (rownode == null) {
//						return;
//					}
//
//					Boolean runStatus = (Boolean) rownode.getClientProperty("run");
//					if (runStatus != null && runStatus) {
//						comp.setBackground(Color.green);
//					} else {
//						comp.setBackground(nodetable.getBackground());
//					}
//				}
//			}
//		};
//
//		nodetable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
//		nodetable.setElementClass(Node.class);
//
//		java.util.List attributes = new ArrayList();// there are two kinds of
//		// List.
//
//		ElementAttribute ntname = new ElementAttribute();
//		ntname.setName("name");
//		ntname.setDisplayName("Station Name");
//		ntname.setSortable(false);
//		ntname.setEditable(true);
//		attributes.add(ntname);
//
//		ElementAttribute ntnodeid = new ElementAttribute();
//		ntnodeid.setEditable(false);
//		ntnodeid.setJavaClass(Integer.class);
//		ntnodeid.setClientPropertyKey("nodeid");
//		ntnodeid.setDisplayName("Station ID");
//		ntnodeid.setSortable(true);// �����Ƿ������
//		ntnodeid.setDescription("identification of node");
//		attributes.add(ntnodeid);
//
//		ElementAttribute ntareaID = new ElementAttribute();
//		ntareaID.setEditable(false);
//		ntareaID.setJavaClass(Integer.class);
//		ntareaID.setClientPropertyKey("areaid");
//		ntareaID.setDisplayName("Area ID");
//		ntareaID.setSortable(false);
//		ntareaID.setDescription("identification of area");
//		attributes.add(ntareaID);
//
//		ElementAttribute ntgateway = new ElementAttribute();
//		ntgateway.setEditable(false);
//		ntgateway.setJavaClass(Boolean.class);
//		ntgateway.setClientPropertyKey("gateway");
//		ntgateway.setDisplayName("Gateway");
//		ntgateway.setSortable(false);
//		ntgateway.setDescription("If the node is a gateway");
//		attributes.add(ntgateway);
//
//		ElementAttribute ntrun = new ElementAttribute();
//		ntrun.setEditable(false);
//		ntrun.setJavaClass(Boolean.class);
//		ntrun.setClientPropertyKey("run");
//		ntrun.setDisplayName("Run State");
//		ntrun.setSortable(true);
//		ntrun.setDescription("If the node is run");
//		attributes.add(ntrun);
//
//		ElementAttribute ntfault = new ElementAttribute();
//		ntfault.setEditable(false);
//		ntfault.setJavaClass(Boolean.class);
//		ntfault.setClientPropertyKey("fault");
//		ntfault.setDisplayName("Fault State");
//		ntfault.setSortable(false);
//		ntfault.setDescription("If the node is fault");
//		attributes.add(ntfault);
//
//		ElementAttribute ntalarmlevel = new ElementAttribute();
//		ntalarmlevel.setEditable(false);
//		ntalarmlevel.setJavaClass(Integer.class);
//		ntalarmlevel.setClientPropertyKey("alarmlevel");
//		ntalarmlevel.setDisplayName("Alarm Level");
//		ntalarmlevel.setSortable(false);
//		ntalarmlevel.setDescription("the level of its alarm state");
//		attributes.add(ntalarmlevel);
//
//		ElementAttribute ntneighbornum = new ElementAttribute();
//		ntneighbornum.setEditable(false);
//		ntneighbornum.setJavaClass(Integer.class);
//		ntneighbornum.setClientPropertyKey("neighbornum");
//		ntneighbornum.setDisplayName("Neighbor Number");
//		ntneighbornum.setSortable(false);
//		ntneighbornum.setDescription("the Number of its Neighbor Node");
//		attributes.add(ntneighbornum);
//
//		ElementAttribute ntipaddr = new ElementAttribute();
//		ntipaddr.setEditable(false);
//		ntipaddr.setJavaClass(String.class);
//		ntipaddr.setClientPropertyKey("ipaddr");
//		ntipaddr.setDisplayName("IP Address");
//		ntipaddr.setSortable(false);
//		ntipaddr.setDescription("ip address");
//		attributes.add(ntipaddr);
//
//		ElementAttribute ntlocation = new ElementAttribute();
//		ntlocation.setName("location");
//		ntlocation.setDisplayName("Location");
//		ntlocation.setSortable(false);
//		ntlocation.setEditable(false);
//		attributes.add(ntlocation);
//
//		nodetable.registerElementClassAttributes(Node.class, attributes);
//		// Registers the specified attributes for the specified element class.
//		nodetablepane.add(new JScrollPane(nodetable));
//
//	}
//
//	private void makeLinkTable() {
//		final TElementTable linktable = new TElementTable(box);
//		linktable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
//		linktable.setElementClass(Link.class);
//
//		java.util.List attributes = new ArrayList();// there are two kinds of
//		// List.
//
//		ElementAttribute nameAttribute = new ElementAttribute();
//		nameAttribute.setName("name");
//		nameAttribute.setDisplayName("Name");
//		nameAttribute.setSortable(false);
//		nameAttribute.setEditable(false);
//		attributes.add(nameAttribute);
//
//		ElementAttribute ltfrom = new ElementAttribute();
//		ltfrom.setEditable(false);
//		ltfrom.setJavaClass(Integer.class);
//		ltfrom.setClientPropertyKey("nodefrom");
//		ltfrom.setDisplayName("From Node");
//		ltfrom.setSortable(false);
//		ltfrom.setDescription("from which node");
//		attributes.add(ltfrom);
//
//		ElementAttribute ltto = new ElementAttribute();
//		ltto.setEditable(false);
//		ltto.setJavaClass(Integer.class);
//		ltto.setClientPropertyKey("nodeto");
//		ltto.setDisplayName("To Node");
//		ltto.setSortable(false);
//		ltto.setDescription("from which node");
//		attributes.add(ltto);
//
//		ElementAttribute des = new ElementAttribute();
//		des.setEditable(false);
//		des.setJavaClass(Color.class);
//		des.setClientPropertyKey("link.color");
//		des.setDisplayName("State");
//		des.setSortable(false);
//		des.setDescription("if the link has been found");
//		attributes.add(des);
//
//		ElementAttribute ltwidth = new ElementAttribute();
//		ltwidth.setEditable(false);
//		ltwidth.setJavaClass(Integer.class);
//		ltwidth.setClientPropertyKey("link.width");
//		ltwidth.setDisplayName("Width");
//		ltwidth.setSortable(false);
//		ltwidth.setDescription("width of link");
//		attributes.add(ltwidth);
//
//		linktable.registerElementClassAttributes(Link.class, attributes);
//		linktablepane.add(new JScrollPane(linktable));
//	}
//
//	private void setMyTreePane() {
//
//		ta.setEditable(false);
//		JLabel labelstart = new JLabel("Start node (id) :       ");
//		JLabel labelkill = new JLabel("Kill     node (id) :       ");
//
//		tfstart.setColumns(3);
//		JButton textbuttonstart = new JButton("send");
//
//		tfkill.setColumns(3);
//		JButton textbuttonkill = new JButton("send");
//
//		// begin 2012-4-9 zhouyu Add a function :start/kill all nodes of one
//		// area
//		JLabel labelStartArea = new JLabel("Start nodes(Area):");
//		JLabel labelkillArea = new JLabel("Kill  nodes(Area):  ");
//
//		tfstartArea.setColumns(3);
//		JButton textbuttonstartArea = new JButton("send");
//
//		tfkillArea.setColumns(3);
//		JButton textbuttonkillArea = new JButton("send");
//
//		textbuttonstartArea.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent ae) {
//				Number value = (Number) (tfstartArea.getValue());
//				int startAreaNum = value.intValue();// ��ȡ��ʼ��
//				// System.out.println(startAreaNum);
//				String nodestring = null;
//				int aid;
//				Node nodeTem = null;
//				try {
//					for (int i = 0; i < NODENUM + 1; i++) {
//						nodestring = "n" + i;
//						if ((nodeTem = (Node) (box.getElementByID(nodestring))) != null) {
//							aid = GetCP_Int(nodeTem, "areaid");
//							// System.out.println("The area is"+aid+"id is:"+i);
//							if (aid == startAreaNum && networkNode[i] != null) {
//								// System.out.println("why");
//								networkNode[i].sendContrlMsg(NetworkNode.START_CMD);
//								// NodeRunFeel(networkNode[i].tv_node);//��ͬ��ڵ���ʾ��ͬ����ɫ
//								// System.out.println("node: "+i+" has start");
//							}
//						}
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//
//		textbuttonkillArea.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent ae) {
//				Number value = (Number) (tfkillArea.getValue());
//				int killnumArea = value.intValue();
//				String nodestring = null;
//				int aid;
//				int killnum;
//
//				System.out.println("We are in kill are action!");
//				Node nodeTem = null;
//				try {
//					for (killnum = 0; killnum < NODENUM + 1; killnum++) {
//						nodestring = "n" + killnum;
//						if ((nodeTem = (Node) (box.getElementByID(nodestring))) != null) {
//							aid = GetCP_Int(nodeTem, "areaid");
//							// System.out.println("Area is "+aid);
//							// if( networkNode[killnum] ==null)
//							// System.out.println("No node ");
//
//							if (aid == killnumArea && networkNode[killnum] != null) {
//								networkNode[killnum].sendContrlMsg(NetworkNode.KILL_CMD);
//
//								Arrays.fill(bilinks[killnum], false);
//								// Arrays.fill(setpathmatrx[killnum],0);
//								// EXITFLAG[killnum] = false;//fancy
//								for (int i = 1; i < (NODENUM + 1); i++) {
//									// setpathmatrx[i][killnum]=0;
//									String linkf = killnum + "_" + i;
//									String linkb = i + "_" + killnum;
//									if (box.getElementByID(linkf) != null) {
//										((Link) (box.getElementByID(linkf))).putLinkColor(Color.BLUE);
//									} else if (box.getElementByID(linkb) != null) {
//										((Link) (box.getElementByID(linkb))).putLinkColor(Color.BLUE);
//									}
//								}
//								NodeInitFeel(nodeTem);
//								Thread.sleep(10);
//							}
//
//						}
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//
//		// end 2012-4-9 zhouyu
//
//		textbuttonstart.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent ae) {
//				Number value = (Number) (tfstart.getValue());
//				int startnum = value.intValue();
//				networkNode[startnum].sendContrlMsg(NetworkNode.START_CMD);
//				// ta.append("send start msg to" + startnum + "\n");
//				try {
//					NodeRunFeel(networkNode[startnum].tv_node);// ���ݽڵ��������򣬱���ɫ
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		});
//		textbuttonkill.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent ae) {
//				Number value = (Number) (tfkill.getValue());
//				int killnum = value.intValue();
//				networkNode[killnum].sendContrlMsg(NetworkNode.KILL_CMD);
//				// ta.append("send kill msg to" + killnum + "\n");
//
//				// 2012-2-28 wj
//				Arrays.fill(bilinks[killnum], false);
//				// Arrays.fill(setpathmatrx[killnum],0);
//				// Arrays.fill(EXITFLAG, false);//fancy
//				for (int i = 1; i < (NODENUM + 1); i++) {
//					// setpathmatrx[i][killnum]=0;
//					String linkf = killnum + "_" + i;
//					String linkb = i + "_" + killnum;
//					if (box.getElementByID(linkf) != null) {
//						((Link) (box.getElementByID(linkf))).putLinkColor(Color.BLUE);
//					} else if (box.getElementByID(linkb) != null) {
//						((Link) (box.getElementByID(linkb))).putLinkColor(Color.BLUE);
//					}
//				}
//				// wj
//
//				String nodestring2 = "n" + killnum;
//				NodeInitFeel((Node) (box.getElementByID(nodestring2)));
//			}
//		});
//
//		textpane1.add(labelstart);
//		textpane1.add(tfstart);
//		textpane1.add(textbuttonstart);
//
//		textpane2.add(labelkill);
//		textpane2.add(tfkill);
//		textpane2.add(textbuttonkill);
//
//		// 2012-4-9 zhouyu
//		textpane3.add(labelStartArea);
//		textpane3.add(tfstartArea);
//		textpane3.add(textbuttonstartArea);
//
//		textpane4.add(labelkillArea);
//		textpane4.add(tfkillArea);
//		textpane4.add(textbuttonkillArea);
//
//		// textpaneNorth.add(textpane1, BorderLayout.NORTH);
//		// textpaneNorth.add(textpane2, BorderLayout.SOUTH);
//		textpaneNorth.add(textpane1);
//		textpaneNorth.add(textpane2);
//		textpaneNorth.add(textpane3);
//		textpaneNorth.add(textpane4);
//		// 2012-4-9 zhouyu
//
//		textpane.add(textpaneNorth, BorderLayout.NORTH);
//
//		textpane.add(ta, BorderLayout.CENTER);
//		textpane.add(new JScrollPane(ta));
//		treePane.add("Set CP", textpane);
//		// �ڵ����·��ʾ��ʵ��
//		// �ڵ�
//		Dummy nodeDummy1 = new Dummy("area1 dummy");
//		nodeDummy1.setName("Area 1");
//		Dummy nodeDummy2 = new Dummy("area2 dummy");
//		nodeDummy2.setName("Area 2");
//		Dummy nodeDummy3 = new Dummy("area3 dummy");
//		nodeDummy3.setName("Area 3");
//		Dummy nodeDummy4 = new Dummy("area4 dummy");
//		nodeDummy4.setName("Area 4");
//		Dummy nodeDummy5 = new Dummy("area5 dummy");
//		nodeDummy5.setName("Area 5");
//		Dummy nodeDummy6 = new Dummy("area6 dummy");
//		nodeDummy6.setName("Area 6");
//		Dummy nodeDummy7 = new Dummy("area7 dummy");
//		nodeDummy7.setName("Area 7");
//		Dummy nodeDummy8 = new Dummy("area8 dummy");
//		nodeDummy8.setName("Area 8");
//		Dummy nodeDummy9 = new Dummy("area9 dummy");
//		nodeDummy9.setName("Area 9");
//		Dummy nodeDummy10 = new Dummy("area10 dummy");
//		nodeDummy10.setName("Area 10");
//		// Dummy nodeDummy11 = new Dummy("area11 dummy");
//		// nodeDummy11.setName("Area 11");
//
//		String nodestr;
//
//		for (int i = 1; i <= NODENUM; i++) {
//			nodestr = "n" + i;
//			try {
//				if (GetCP_Int(nodestr, "areaid") == 1)
//					nodeDummy1.addChild(box.getElementByID(nodestr));
//				else if (GetCP_Int(nodestr, "areaid") == 2)
//					nodeDummy2.addChild(box.getElementByID(nodestr));
//				else if (GetCP_Int(nodestr, "areaid") == 3)
//					nodeDummy3.addChild(box.getElementByID(nodestr));
//				else if (GetCP_Int(nodestr, "areaid") == 4)
//					nodeDummy4.addChild(box.getElementByID(nodestr));
//				else if (GetCP_Int(nodestr, "areaid") == 5)
//					nodeDummy5.addChild(box.getElementByID(nodestr));
//				else if (GetCP_Int(nodestr, "areaid") == 6)
//					nodeDummy6.addChild(box.getElementByID(nodestr));
//				else if (GetCP_Int(nodestr, "areaid") == 7)
//					nodeDummy7.addChild(box.getElementByID(nodestr));
//				else if (GetCP_Int(nodestr, "areaid") == 8)
//					nodeDummy8.addChild(box.getElementByID(nodestr));
//				else if (GetCP_Int(nodestr, "areaid") == 9)
//					nodeDummy9.addChild(box.getElementByID(nodestr));
//				else if (GetCP_Int(nodestr, "areaid") == 10)
//					nodeDummy10.addChild(box.getElementByID(nodestr));
//				// else if (GetCP_Int(nodestr, "areaid") == 11)
//				// nodeDummy11.addChild(box.getElementByID(nodestr));
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//
//		box.addElement(nodeDummy1);
//		box.addElement(nodeDummy2);
//		box.addElement(nodeDummy3);
//		box.addElement(nodeDummy4);
//		box.addElement(nodeDummy5);
//		box.addElement(nodeDummy6);
//		box.addElement(nodeDummy7);
//		box.addElement(nodeDummy8);
//		box.addElement(nodeDummy9);
//		box.addElement(nodeDummy10);
//		// box.addElement(nodeDummy11);
//
//		// ��·
//		Dummy linkDummy = new Dummy("link dummy");
//		linkDummy.setName("Links");
//		for (int m = 1; m <= NODENUM; m++)
//			for (int n = 1; n <= NODENUM; n++) {
//				String str = (m + "_" + n);
//				linkDummy.addChild(box.getElementByID(str));
//			}
//		box.addElement(linkDummy);
//		// tree
//		tree = new TTree(box);
//		tree.setTTreeSelectionMode(TTree.CHECK_DESCENDANT_SELECTION);
//		JScrollPane scroll = new JScrollPane(tree);
//		treePane.add("Node Element", scroll);// *******************************
//
//		// start by YuYu
//		// Dummy trafficDummy1 = new Dummy("set path");
//		// trafficDummy1.setName("set path");
//		// //display_box.addElement(trafficDummy1);
//		// Dummy trafficDummy2 = new Dummy("delete path");
//		// trafficDummy2.setName("delete path");
//		// //display_box.addElement(trafficDummy2);
//		//
//
//		// tree
//		// dis_tree = new TTree(display_box);
//		// dis_tree.setTTreeSelectionMode(TTree.CHECK_DESCENDANT_SELECTION);
//		// JScrollPane dis_scroll = new JScrollPane(dis_tree);
//		// treePane.add("traffic display", dis_scroll);//
//		// ******************************
//
//		// end YuYu 14-12-08
//
//		JScrollPane scrollta = new JScrollPane(textarea);
//
//		scrollta.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//		scrollta.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
//
//		treePane.add("Message", scrollta);// *************************************
//
//	}
//
//	/**
//	 * Ϊxml�ļ���SearchNE��׼�� add by zhujian 2017/11/26
//	 *
//	 * @throws InterruptedException
//	 */
//	public void SearchNE() throws InterruptedException {
//		Thread t = new Thread(nodes_data_listener);
//		t.start();
//		System.out.println("Search NE start");
//		Thread.sleep(15000);
//	}
//
//	// add by ty 2017-11-24
//	private void startArea(ArrayList<Integer> startarea) throws InterruptedException {
//		// SearchNE();// SearchNE
//		for (Integer areaNum : startarea) {
//			System.out.println("The area is " + areaNum);
//			String nodestring = null;
//			int aid;
//			Node nodeTem = null;
//			try {
//				for (int i = 0; i < NODENUM + 1; i++) {
//					nodestring = "n" + i;
//					if ((nodeTem = (Node) (box.getElementByID(nodestring))) != null) {
//						aid = GetCP_Int(nodeTem, "areaid");
//						if (aid == areaNum && networkNode[i] != null) {
//							System.out.println("The area is" + aid + " id is:" + i);
//							// System.out.println("why");
//							networkNode[i].sendContrlMsg(NetworkNode.START_CMD);
//							// System.out.println("����ɫ");
//							NodeRunFeel(networkNode[i].tv_node);
//							Thread.sleep(300);
//
//						}
//					}
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//
//		}
//	}
//
//	/**
//	 * add by zhujian 2017/11/27
//	 *
//	 * @param killArea
//	 * @throws InterruptedException
//	 */
//
//	private void killArea(ArrayList<Integer> killArea) throws InterruptedException {
//		for (Integer killAreaNum : killArea) {
//			String nodestring = null;
//			int aid;
//			int killnum;
//			System.out.println("We are in kill are action!");
//			Node nodeTem = null;
//			try {
//				for (killnum = 0; killnum < NODENUM + 1; killnum++) {
//					nodestring = "n" + killnum;
//					if ((nodeTem = (Node) (box.getElementByID(nodestring))) != null) {
//						aid = GetCP_Int(nodeTem, "areaid");
//						// System.out.println("Area is "+aid);
//						// if( networkNode[killnum] ==null)
//						// System.out.println("No node ");
//
//						if (aid == killAreaNum && networkNode[killnum] != null) {
//							networkNode[killnum].sendContrlMsg(NetworkNode.KILL_CMD);
//
//							Arrays.fill(bilinks[killnum], false);
//							// Arrays.fill(setpathmatrx[killnum],0);
//							// EXITFLAG[killnum] = false;//fancy
//							for (int i = 1; i < (NODENUM + 1); i++) {
//								// setpathmatrx[i][killnum]=0;
//								String linkf = killnum + "_" + i;
//								String linkb = i + "_" + killnum;
//								if (box.getElementByID(linkf) != null) {
//									((Link) (box.getElementByID(linkf))).putLinkColor(Color.BLUE);
//								} else if (box.getElementByID(linkb) != null) {
//									((Link) (box.getElementByID(linkb))).putLinkColor(Color.BLUE);
//								}
//							}
//							NodeInitFeel(nodeTem);
//							Thread.sleep(10);
//						}
//
//					}
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	// add by ty 2017-11-25
//	private ArrayList<Traffic> completetraffic(ArrayList<Traffic> setpathtraffic) throws InterruptedException, IOException {
//		SendXML sendxml=new SendXML();
//		sendxml.initIntemediateSendXml();
//		for (Traffic tf : setpathtraffic) {
//			String fromnode = "n" + tf.src_id;
//			String tonode = "n" + tf.dst_id;
//			try {
//				tf.src_area_id = GetCP_Int(fromnode, "areaid");
//				tf.dst_area_aid = GetCP_Int(tonode, "areaid");
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			tf.LinkState=1;//linkstate���� add by zhujian 2017/12/18
//			System.out.println("ҵ��ID:" + (trafficID + 1) + ",Դ�ڵ㣺" + tf.src_id + ",Դ�ڵ����ڵ���" + tf.src_area_id + ",Ŀ�Ľڵ㣺"
//					+ tf.dst_id + ",Ŀ�Ľڵ����ڵ���" + tf.dst_area_aid + ",ҵ�����ԣ�" + tf.granularityValue + ",·�����ԣ�" + tf.resv
//					+ ",�������ԣ�" + tf.pro_n);
//			/*��·��ʼʱ��  add by zhujian 2017/12/7*/
//
//			long systemMillsBegin=System.currentTimeMillis();
//			tf.trafficBeginTime=new Date(systemMillsBegin);
//			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
//
//			try{
//			this.trafficManager.buildPath(tf);// ����ҵ��
//			trafficID++;
//			} catch (Exception e){
//			System.out.println("���ͽ�·��ʧ�ܣ�");
//			}
//
//			Thread.sleep(10000);
//		}
//		return setpathtraffic;
//	}
//
//	public void setPathParameter(final int src_id, final int des_id, final int srcaid, final int desaid) { // ���ý�·ʱ������
//
//		PathParameterFrame ppf = new PathParameterFrame(src_id, des_id, srcaid, desaid, this);
//		return;
//	}
//
//	public void setDelPathParameter(final int src_id, final int des_id, final int srcaid, final int desaid) { // ����ɾ·ʱ������
//		deletePathParameterFrame ppf = new deletePathParameterFrame(src_id, des_id, srcaid, desaid, this);
//		return;
//	}
//
//	// use the ActionListener to set up path.
//	private void setMyNetworkPane() {
//		network = new TNetwork(box);// associate with DataBox.
//		networkpane.add(network, BorderLayout.CENTER);
//		// set the background of network background.
//		ImageBackground ibg = new ImageBackground("/images/pic2000.png");// ���õ�ͼ
//		// ibg.setSpecificSize(new Dimension(1005,839));
//		network.setBackground(ibg);
//
//		// network.setToolbar(null);// no tool bar.
//		// network.setToolbarByName("default");
//
//		network.getCanvas().addMouseListener(new MouseAdapter() {
//			public void mouseClicked(MouseEvent e) {
//				/*
//				 * click the mouse once.
//				 */
//				if (e.getClickCount() == 1) {
//					if ((LinkState_am == 1) || (LinkState_am == 3) || (LinkState_am == 4) || (LinkState_am == 5)) {// set
//																													// path�����ֽ�·��Ӧ��״̬
//
//						Node node = null;// ��������
//						try {
//							node = (Node) (network.getElementPhysicalAt(e.getPoint()));// ���ڵ�������Ǹ��ڵ�
//
//						} catch (ClassCastException cce) {// do not throw
//							// ClassCastException.
//						}
//						if (node == null) {// ����ǿսڵ� read_add=false
//
//							if (ready_add) {// read_add=true
//								if (GetCP_Boolean(node, "run"))
//									try {
//										NodeRunFeel(node);// �ı����
//									} catch (Exception e1) {
//										// TODO Auto-generated catch block
//										e1.printStackTrace();
//									}
//								else
//									NodeInitFeel(node);
//							}
//							ready_add = false;
//						} else if (ready_add == false) {// û�б�ѡ�е�ʱ��
//							if (GetCP_Boolean(node, "run")) {
//								nodetemp = node;
//								NodePathSetFeel1(nodetemp);// ת��Ϊ��ɫ���ϼ�ͷ
//								ready_add = true;// ѡ��״̬���Ϊtrue
//							} else
//								ready_add = false;// ������Ϊfalse
//						} else {
//							if (nodetemp.getName() == node.getName() || !GetCP_Boolean(node, "run")) {
//								if (GetCP_Boolean(nodetemp, "run"))
//									try {
//										NodeRunFeel(nodetemp);
//									} catch (Exception e1) {
//										// TODO Auto-generated catch block
//										e1.printStackTrace();
//									}
//								else
//									NodeInitFeel(nodetemp);
//								ready_add = false;
//							} else {
//								NodePathSetFeel2(node);
//								textarea.append(
//										"Setup Path: Node" + nodetemp.getName() + "-> Node" + node.getName() + "\n");
//								try {
//
//									// 2012-03-11 wj rember to clear the
//									// unuseable path like src dst -1 0!!!!
//									// int j=findusablepath(SpathD);
//									// SpathD[j][0]=GetCP_Int(nodetemp,
//									// "nodeid");
//									// SpathD[j][1]=GetCP_Int(node, "nodeid");
//									// SpathD[j][2]=-1;
//									// wj
//
//									// if (GetCP_Int(nodetemp, "areaid") ==
//									// GetCP_Int(node, "areaid"))//OTZ
//									// setpath(GetCP_Int(nodetemp, "nodeid"),
//									// GetCP_Int(node, "nodeid"));//����������
//									setPathParameter(GetCP_Int(nodetemp, "nodeid"), GetCP_Int(node, "nodeid"),
//											GetCP_Int(nodetemp, "areaid"), GetCP_Int(node, "areaid"));
//
//									// setpath_in(GetCP_Int(nodetemp, "nodeid"),
//									// GetCP_Int(node, "nodeid"),
//									// GetCP_Int(nodetemp, "areaid"),
//									// GetCP_Int(node, "areaid"));// ����
//									// ��������
//
//									/*
//									 * else if (((Boolean) (nodetemp
//									 * .getClientProperty("gateway"))) == true)
//									 * setpath_out(GetCP_Int(nodetemp,
//									 * "nodeid"), GetCP_Int(node, "nodeid"),
//									 * GetCP_Int(nodetemp, "nodeid"),
//									 * GetCP_Int(nodetemp, "areaid"),
//									 * GetCP_Int(node, "areaid"));
//									 *
//									 * else { int gateid = 0; String nodestr;
//									 * for (int i = 1; i < NODENUM; i++) {
//									 * nodestr = "n" + i; if
//									 * ((GetCP_Int(nodestr, "areaid") ==
//									 * GetCP_Int( nodetemp, "areaid")) &&
//									 * ((Boolean) ( .getElementByID(nodestr)
//									 * .getClientProperty("gateway"))) == true
//									 * &&(GetCP_Boolean(nodestr, "run"))) {
//									 * gateid = i; break; } } textarea.append(
//									 * "the gateid is:" + gateid + "\n");
//									 * setpath_out(GetCP_Int(nodetemp,
//									 * "nodeid"), GetCP_Int(node, "nodeid"),
//									 * gateid, GetCP_Int(nodetemp, "areaid"),
//									 * GetCP_Int(node, "areaid"));
//									 *
//									 * }
//									 */
//									// OTZ
//								} catch (Exception e1) {
//									// TODO Auto-generated catch block
//									e1.printStackTrace();
//
//								}
//								nodestart = nodetemp;
//								nodefinal = node;
//								pathflag = true;
//								ready_add = false;
//							}
//						}
//					}
//					// --------------------DELETE
//					// PATH---------------------------------
//					if (LinkState_am == 2) {
//						Node node = (Node) (network.getElementPhysicalAt(e.getPoint()));
//						if (node == null) {
//							if (ready_del) {
//								if (GetCP_Boolean(nodetemp, "run"))
//									try {
//										NodeRunFeel(nodetemp);
//									} catch (Exception e1) {
//										// TODO Auto-generated catch block
//										e1.printStackTrace();
//									}
//								else
//									NodeInitFeel(nodetemp);
//							}
//							ready_del = false;
//						} else if (ready_del == false) {
//							if (GetCP_Boolean(node, "run")) {
//								nodetemp = node;
//								NodePathSetFeel1(nodetemp);// @TODO
//								ready_del = true;
//							} else
//								ready_del = false;
//						} else {
//							if (nodetemp.getName() == node.getName() || !GetCP_Boolean(node, "run")) {
//								if (GetCP_Boolean(nodetemp, "run"))
//									try {
//										NodeRunFeel(nodetemp);
//									} catch (Exception e1) {
//										// TODO Auto-generated catch block
//										e1.printStackTrace();
//									}
//								else
//									NodeInitFeel(nodetemp);
//								ready_del = false;
//							} else {
//								NodePathSetFeel2(node);
//								textarea.append(
//										"Delete Path: Node" + nodetemp.getName() + "-> Node" + node.getName() + "\n");
//								try {
//									setDelPathParameter(GetCP_Int(nodetemp, "nodeid"), GetCP_Int(node, "nodeid"),
//											GetCP_Int(nodetemp, "areaid"), GetCP_Int(node, "areaid"));
//
//								} catch (Exception e1) {
//									// TODO Auto-generated catch block
//									e1.printStackTrace();
//								}
//
//								NodeInitFeel(nodetemp); // add by Xiaoliang
//
//								nodestart = nodetemp;
//								nodefinal = node;
//								pathflag = true;
//
//								ready_del = false;
//							}
//						}
//					}
//				}
//
//				if (e.getButton() == e.BUTTON3) {
//					if (getpointstate == true)
//						textarea.append("location" + e.getPoint().x + ":" + e.getPoint().y + "\n");
//				}
//			}
//
//		});
//	}
//
//	/**
//	 * ��ʾ��·�ķ���
//	 *
//	 * @param _source_node_id
//	 * @param _dst_node_ids
//	 */
//	public synchronized void showLinks(int _source_node_id, ArrayList<Integer> _dst_node_ids) // ��ڵ�Ľ�����ʾ
//	{
//		if (_dst_node_ids != null && _dst_node_ids.size() > 0)
//			;
//		else
//			return;
//
//		for (int dst_node_id : _dst_node_ids) {
//			String linkf = _source_node_id + "_" + dst_node_id;
//			String linkb = dst_node_id + "_" + _source_node_id;
//			String linkp = _source_node_id + "->" + dst_node_id;
//
//			bilinks[_source_node_id][dst_node_id] = true;
//
//			if (true == bilinks[dst_node_id][_source_node_id])// 2012-2-28 wj
//			{
//				if (box.getElementByID(linkf) != null) {
//					((Link) (box.getElementByID(linkf))).putLinkColor(Color.RED);
//				} else {
//					((Link) (box.getElementByID(linkb))).putLinkColor(Color.RED);
//				}
//			}
//			textarea.append("link:" + linkp + "found\n");
//		}
//	}
//
//	/*
//	 * public synchronized void dealCCMsg_delate( int id) {
//	 * System.out.println("****testc"+id); }
//	 */
//	/**
//	 * �����շ����ķ���
//	 *
//	 * @param hop_node_ids
//	 * @param pro_no
//	 */
//	public synchronized void showTrafficPath(ArrayList hop_node_ids, int pro_no) // ��·���ذ������󣬸÷�����������·����ʾ
//	{
//		if (hop_node_ids != null && hop_node_ids.size() > 0)
//			; // ����hop_node_idsΪ·����Ϣ��pro_no���ֹ���·������·��
//		else
//			return;
//		for (int i = 0; i < hop_node_ids.size() - 1; i++) {
//			String linkf = hop_node_ids.get(i) + "_" + hop_node_ids.get(i + 1);
//			String linkb = hop_node_ids.get(i + 1) + "_" + hop_node_ids.get(i);
//			if (pro_no == 0) // ����·����ʾ
//			{
//				if (box.getElementByID(linkf) != null) {
//					((Link) (box.getElementByID(linkf))).putLinkColor(Color.YELLOW);
//					System.out.println("����·��" + linkf);
//				} else {
//					((Link) (box.getElementByID(linkb))).putLinkColor(Color.YELLOW);
//					System.out.println("����·��" + linkb);
//				}
//			} else if (pro_no == 1) // ����·����ʾ
//			{
//				if (box.getElementByID(linkf) != null) {
//					((Link) (box.getElementByID(linkf))).putLinkColor(Color.CYAN);
//					System.out.println("����·��" + linkf);
//				} else {
//					((Link) (box.getElementByID(linkb))).putLinkColor(Color.CYAN);
//					System.out.println("����·��" + linkb);
//				}
//
//			}
//		}
//	}
//
//	// ɾ·���ذ������󣬸÷�����������·����ʾ
//	public synchronized void unShowTrafficPath(ArrayList hop_node_ids) {// wait
//																		// for
//																		// modify
//																		// ÿ����·�ָ�ԭɫ
//		if (hop_node_ids != null && hop_node_ids.size() > 0)
//			;
//		else
//			return;
//		for (int i = 0; i < hop_node_ids.size() - 1; i++) {
//			String linkf = hop_node_ids.get(i) + "_" + hop_node_ids.get(i + 1);
//			String linkb = hop_node_ids.get(i + 1) + "_" + hop_node_ids.get(i);
//			if (box.getElementByID(linkf) != null) {
//				((Link) (box.getElementByID(linkf))).putLinkColor(Color.RED);
//			} else {
//				((Link) (box.getElementByID(linkb))).putLinkColor(Color.RED);
//			}
//
//		}
//	}
//
//	private class FileOpenListener implements ActionListener {
//		public void actionPerformed(ActionEvent e) {
//
//			int result = chooser.showOpenDialog(jf);
//
//			if (result == JFileChooser.APPROVE_OPTION) {
//				File file = chooser.getSelectedFile();
//				// System.out.println(file);
//				// System.out.println(file.toURI());
//
//				try {
//					box.clear();
//					box.parse((file.toURI()).toString());
//					// box.output("topo123", true);
//					box.toXML();
//					// System.out.println(box.toXML());// �ڿ���̨��ӡ��xml�ļ�
//				} catch (Exception ee) {
//					ee.printStackTrace();
//				}
//
//			} else if (result == JFileChooser.CANCEL_OPTION) {
//				return;
//			}
//
//		}
//	}
//
//	private class SendAgentListener implements ActionListener {
//		public void actionPerformed(ActionEvent e) {
//			final JFrame jf = new JFrame("MobileAgent");
//			jf.setLayout(null);
//
//			JButton sourceNodeId = new JButton("Դ�ڵ�ID");
//			sourceNodeId.setBounds(58, 48, 120, 28);
//			jf.add(sourceNodeId);
//
//			JButton destinationNodeId = new JButton("Ŀ�Ľڵ�ID");
//			destinationNodeId.setBounds(58, 98, 120, 28);
//			jf.add(destinationNodeId);
//
//			JButton sourceAreaId = new JButton("Դ�ڵ���ID");
//			sourceAreaId.setBounds(58, 148, 120, 28);
//			jf.add(sourceAreaId);
//
//			JButton destinationAreaId = new JButton("Ŀ�Ľڵ���ID");
//			destinationAreaId.setBounds(58, 198, 120, 28);
//			jf.add(destinationAreaId);
//
//			final JTextField tf1 = new JTextField();
//			tf1.setBounds(230, 48, 120, 28);
//			jf.add(tf1);
//
//			final JTextField tf2 = new JTextField();
//			tf2.setBounds(230, 98, 120, 28);
//			jf.add(tf2);
//
//			final JTextField tf3 = new JTextField();
//			tf3.setBounds(230, 148, 120, 28);
//			jf.add(tf3);
//
//			final JTextField tf4 = new JTextField();
//			tf4.setBounds(230, 198, 120, 28);
//			jf.add(tf4);
//
//			JButton ok = new JButton("ȷ��");
//			ok.setBounds(100, 248, 90, 28);
//			jf.add(ok);
//			ok.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent event) {
//					String s_Nodeid = tf1.getText();
//					Byte s_NodeID = Byte.parseByte(s_Nodeid);
//
//					String d_NodeId = tf2.getText();
//					Byte d_NodeID = Byte.parseByte(d_NodeId);
//
//					String s_AreaId = tf3.getText();
//					Byte s_AreaID = Byte.parseByte(s_AreaId);
//
//					String d_AreaId = tf4.getText();
//					Byte d_AreaID = Byte.parseByte(d_AreaId);
//
//					byte[] NetManager_MA;
//					NetManager_MA = new byte[] { s_NodeID, d_NodeID, s_AreaID, d_AreaID };
//
//					// Java Client
//
//					try {
//						String str = "192.168.0." + s_NodeID;
//						Socket socket = new Socket("192.168.0.254", 8888);// ��������ת������������������
//						// Socket socket = new Socket(str, 8888);
//						// MA�ڱ����� Ӳ���ڵ�(AreaID: 2)������12 ���13 ����26 �ൺ27
//
//						BufferedReader in_data = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//						// ��ȡ��������������
//						// String stri = in_data.readLine();
//						// System.out.println("MA_Server said:" + stri);//
//						// Hello,
//						// // you are connected!
//
//						for (int i = 0; i < NetManager_MA.length; i++) {
//							System.out.println(NetManager_MA[i]);
//						}
//						socket.getOutputStream().write(NetManager_MA);
//						socket.getOutputStream().flush(); // ������д�뵽socket������У�MA������socket�������ж�ȡ����
//
//						// System.out.println("From MA:" +
//						// in_data.readLine() +"OK");
//
//						socket.getOutputStream().close();
//						in_data.close();
//						socket.close();
//
//					} catch (Exception e) {
//						e.printStackTrace();
//						// System.out.println("Couldn't listen on 8888!");
//						System.exit(-1);
//					}
//
//					jf.setVisible(false);
//				}
//			});
//
//			JButton cancel = new JButton("ȡ��");
//			cancel.setBounds(213, 248, 90, 28);
//			jf.add(cancel);
//			cancel.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent event) {
//					jf.setVisible(false);
//				}
//			});
//
//			jf.setBounds(350, 211, 410, 330);
//			jf.setResizable(false);
//			jf.setVisible(true);
//
//		}
//	}
//
//	private boolean GetCP_Boolean(Node gcpb_node, String gcpb_str) {
//		boolean gcpb_bool = (Boolean) (gcpb_node.getClientProperty(gcpb_str));
//		return gcpb_bool;
//	}
//
//	// Get a element's ClientProperty which is a INT type. * Provided
//	// "box","Node type"
//	private int GetCP_Int(String ElementNodeID, String clientproperty) throws Exception {
//		String str_clientproperty = (box.getElementByID(ElementNodeID)).getClientProperty(clientproperty).toString();
//
//		// System.out.println(str_clientproperty);
//		int int_clientproperty = Integer.parseInt((str_clientproperty));
//		// int int_clientproperty =
//		// (Integer.valueOf(str_clientproperty)).intValue();
//		return int_clientproperty;
//	}
//
//	protected int GetCP_Int(Node noderef, String clientproperty) throws Exception {
//		String str_clientproperty = noderef.getClientProperty(clientproperty).toString();
//		int int_clientproperty = Integer.valueOf(str_clientproperty).intValue();
//		return int_clientproperty;
//
//	}
//
//	/**
//	 * ���Layer Manager����
//	 *
//	 * @author Jobs
//	 *
//	 */
//	private class LayerColListener implements ActionListener {
//		public void actionPerformed(ActionEvent e) {
//			final JFrame jf_col = new JFrame("Layer Manager");
//			jf_col.setLayout(null);
//			jf_col.setBounds(50, 111, 630, 350);
//			jf_col.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//			jf_col.getContentPane().setLayout(new BorderLayout());
//
//			JPanel contentPanel_col = new JPanel();
//			contentPanel_col.setLayout(null);
//			jf_col.getContentPane().add(contentPanel_col);
//
//			final JLabel promptLabel = new JLabel("Layer Manager Table");
//			promptLabel.setFont(new java.awt.Font("Dialog", 1, 15)); // "dialog"
//																		// �������壬1������ʽ(1�Ǵ��壬0��ƽ���ģ�15���ֺ�
//			promptLabel.setForeground(Color.blue);
//			promptLabel.setBounds(40, 10, 400, 30);
//			contentPanel_col.add(promptLabel);
//
//			final LayerManagerPane pane_col = new LayerManagerPane(network);
//			pane_col.setBounds(10, 40, 603, 250);
//			contentPanel_col.add(pane_col);
//
//			jf_col.setResizable(false);
//			jf_col.setVisible(true);
//		}
//	}
//
//	// @TODO ����ܴ���Ҫ�ص��޸�
//	private class TrafficDisListener implements ActionListener {
//		public void actionPerformed(ActionEvent e) {
//			final JFrame jf_tr = new JFrame("Traffic Display");
//
//			jf_tr.setLayout(null);
//			jf_tr.setBounds(50, 111, 700, 430);
//			jf_tr.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//			jf_tr.getContentPane().setLayout(new BorderLayout());
//
//			JPanel contentPanel_tr = new JPanel();
//			contentPanel_tr.setLayout(null);
//			jf_tr.getContentPane().add(contentPanel_tr);
//
//			final JLabel promptLabel = new JLabel("Traffic display table");
//			promptLabel.setFont(new java.awt.Font("Dialog", 1, 15)); // "dialog"
//																		// �������壬1������ʽ(1�Ǵ��壬0��ƽ���ģ�15���ֺ�
//			promptLabel.setForeground(Color.blue);
//			promptLabel.setBounds(60, 10, 660, 30);
//			contentPanel_tr.add(promptLabel);
//
//			final JPanel tablePanel_tr = new JPanel(new BorderLayout());
//			tablePanel_tr.setBounds(10, 40, 680, 450);
//			contentPanel_tr.add(tablePanel_tr);
//
//			class TrafficData extends Node {
//
//				public TrafficData(int number, String link_com, String name, int srcid, int dstid, String pro_nam,
//						int tunnelId, int hopnum, String linkPath) {
//					this.putClientProperty("*", number);
//					this.putClientProperty("link_com", link_com);
//					this.putClientProperty("linkName", name);
//
//					this.putClientProperty("srcid", srcid);
//					this.putClientProperty("dstid", dstid);
//					this.putClientProperty("pro_nam", pro_nam);
//					this.putClientProperty("tunnelId", tunnelId);
//					this.putClientProperty("hopnum", hopnum);
//					this.putClientProperty("linkPath", linkPath);
//					// ����Ҫ�����·���������
//
//				}
//
//				public TrafficData(Traffic _tf, int _number) {
//					this.putClientProperty("*", _number);
//					if (_tf.pro_n == 0) {
//						this.putClientProperty("link_com", "�ޱ���·��");// wait to
//																	// modify
//					} else if (_tf.pro_n == 2) {
//						this.putClientProperty("link_com", "1+1��·");
//					}
//
//					System.out.println("TrafficDisplay: src_id " + _tf.src_id + " dst_id " + _tf.dst_id);
//					Node fnode_tep = (Node) (box.getElementByID("n" + _tf.src_id)); // ��ȡ�׽ڵ�
//					Node tnode_tep = (Node) (box.getElementByID("n" + _tf.dst_id));
//
//					// System.out.println("fnode_tep: "+fnode_tep+"->tnode_tep:
//					// "+tnode_tep);
//					// System.out.println("fnode_tep:
//					// "+fnode_tep.getName()+"->tnode_tep:
//					// "+tnode_tep.getName());
//					String _linkName = fnode_tep.getName() + "_" + tnode_tep.getName();
//					this.putClientProperty("linkName", _linkName);
//
//					this.putClientProperty("srcid", _tf.src_id);
//					this.putClientProperty("dstid", _tf.dst_id);
//					if (_tf.IsRunning == true) {
//						if (_tf.pro_no == 0) {
//							this.putClientProperty("pro_nam", "����·��");// wait to
//																		// modify
//						} else {
//							this.putClientProperty("pro_nam", "����·��");// wait to
//																		// modify
//						}
//					} else {
//						this.putClientProperty("pro_nam", "��ֹͣ");
//					}
//
//					this.putClientProperty("tunnelId", _tf.tunnelID);
//					this.putClientProperty("hopnum", _tf.hopNum);
//
//					String _linkPath = "";
//					if (_tf.hopNum == 0)
//						this.putClientProperty("linkPath", "��");
//					else {
//						for (int _hop : _tf.hop_nodes) {// wait for modify
//							int z = _tf.hop_nodes.size();
//							if (_hop != _tf.hop_nodes.get(z - 1)) {
//								Node linkpath_node_tep = (Node) (box.getElementByID("n" + _hop));
//								_linkPath += linkpath_node_tep.getName() + "+";
//							} else {
//								Node linkpath_node_tep = (Node) (box.getElementByID("n" + _hop));
//								_linkPath += linkpath_node_tep.getName();
//							}
//
//						}
//					}
//
//					this.putClientProperty("linkPath", _linkPath);
//
//				}
//
//				public String getLink_com() {
//					return (this.getClientProperty("link_com")).toString();
//				}
//
//				public int getSrcID() {
//					return ((Integer) this.getClientProperty("srcID")).intValue();
//				}
//
//				public int getDstID() {
//					return ((Integer) this.getClientProperty("dstID")).intValue();
//				}
//
//				public String getPro_nam() {
//					return (this.getClientProperty("pro_nam")).toString();
//				}
//				// public int getSrcAreaID() {
//				// return
//				// ((Integer)this.getClientProperty("srcAreaID")).intValue();
//				// }
//
//				public int getTunnelId() {
//					return ((Integer) this.getClientProperty("tunnelId")).intValue();
//				}
//
//				public int getHopNum() {
//					return ((Integer) this.getClientProperty("hopnum")).intValue();
//				}
//
//				public String getLinkName() {
//					return this.getClientProperty("linkName").toString();
//
//				}
//
//				public String getLinkPath() {
//					return this.getClientProperty("linkPath").toString();
//
//				}
//
//			} // end of class TrafficData
//
//			Table_tr.registerElementClassXML(TrafficData.class, "/trafficDisplay.xml");
//			Table_tr.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//			Table_tr.setMultiColumnSortable(true);
//			Table_tr.packAllColumns(true);
//
//			Table_tr.setIteratorByHiberarchy(true);
//			Table_tr.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
//			Table_tr.setElementClass(TrafficData.class);
//			tablePanel_tr.add(new JScrollPane(Table_tr));
//
//			Box_tr.clear();
//			ArrayList<Traffic> _allTraffics = trafficManager.allTraffics();
//
//			int numberCount = 1;
//			if (_allTraffics != null) {
//				for (Traffic _tf : _allTraffics)
//					Box_tr.addElement(new TrafficData(_tf, numberCount++));
//			}
//
//			// Box_tr.addElement(new
//			// TrafficData(NodeListenRunnable.dataArray[i][0],cmd_nam,linkName,NodeListenRunnable.dataArray[i][2],NodeListenRunnable.dataArray[i][3],Pro_nam,NodeListenRunnable.dataArray[i][5],NodeListenRunnable.dataArray[i][6],linkPath));
//
//			/*
//			 * int tep=findusablepath_dis(dataArray); System.err.print(tep); int
//			 * col_tep=tep-1; if(NodeListenRunnable.dataArray[col_tep][2]!=0){
//			 * //��Ҫȥ���Լ� Box_tr.clear(); for(int i=0;i<=col_tep &
//			 * (NodeListenRunnable.dataArray[i][2]>0);i++){ if (
//			 * NodeListenRunnable.dataArray[i][1]== 1) { cmd_nam="����·��"; } else if
//			 * ( NodeListenRunnable.dataArray[i][1]== 2) { cmd_nam="ɾ��·��"; } if (
//			 * NodeListenRunnable.dataArray[i][4]== 0) { Pro_nam="����·��"; } else if
//			 * ( NodeListenRunnable.dataArray[i][4]== 1) { Pro_nam="����·��"; } int
//			 * fn_tep =NodeListenRunnable.dataArray[i][2]; //��·�׽ڵ�ID String
//			 * fnstr_tep = "n" + String.valueOf(fn_tep);
//			 *
//			 * int tn_tep =NodeListenRunnable.dataArray[i][3]; //��·ĩ�ڵ�ID String
//			 * tnstr_tep = "n" + String.valueOf(tn_tep);
//			 * System.out.println("******************"+fn_tep+ "->"+tn_tep);
//			 * Node fnode_tep = (Node)(box.getElementByID(fnstr_tep)); //��ȡ�׽ڵ�
//			 * Node tnode_tep = (Node)(box.getElementByID(tnstr_tep));
//			 * System.out.println("fnode_tep: "+fnode_tep+"->tnode_tep: "
//			 * +tnode_tep); System.out.println("fnode_tep: "
//			 * +fnode_tep.getName()+"->tnode_tep: "+tnode_tep.getName());
//			 * linkName=fnode_tep.getName()+"_"+tnode_tep.getName(); for(int
//			 * t=0;t<30;t++)//���ɾ��·����Ϣ����ʾ���Ĵ��� { for(int h=0;h<30;h++){
//			 * if((NodeListenRunnable.dataArray[t][1]==2)&
//			 * (NodeListenRunnable.dataArray[h][5]==NodeListenRunnable.dataArray[t][
//			 * 5])) {
//			 * NodeListenRunnable.dataArray[t][6]=NodeListenRunnable.dataArray[h][6]
//			 * ; for(int p=6;p<NodeListenRunnable.dataArray[t][6]+7;p++)//·����Ϣ����
//			 * NodeListenRunnable.dataArray[t][p]=NodeListenRunnable.dataArray[h][p]
//			 * ;
//			 * //NodeListenRunnable.dataArray[t][6]=NodeListenRunnable.dataArray[h][
//			 * 6]; } } } int hopnum=NodeListenRunnable.dataArray[i][6];
//			 * if(hopnum!=0){ for(int k=7;k<7+hopnum;k++) {
//			 *
//			 * int linkpath_tep =NodeListenRunnable.dataArray[i][k];
//			 * if(linkpath_tep!=0) { String linkpathstr_tep = "n" +
//			 * String.valueOf(linkpath_tep); Node linkpath_node_tep =
//			 * (Node)(box.getElementByID(linkpathstr_tep)); if(k==7) {
//			 * linkPath=linkpath_node_tep.getName()+"-"; } else if((k!=6+hopnum)
//			 * & (k!=7)) { linkPath=linkPath+linkpath_node_tep.getName()+"-"; }
//			 * else { linkPath=linkPath+linkpath_node_tep.getName(); } } else
//			 * System.out.println("·����ϢΪ�ա�����"); } } else { linkPath="��"; }
//			 *
//			 *
//			 * Box_tr.addElement(new
//			 * TrafficData(NodeListenRunnable.dataArray[i][0],cmd_nam,linkName,
//			 * NodeListenRunnable.dataArray[i][2],NodeListenRunnable.dataArray[i][3]
//			 * ,Pro_nam,NodeListenRunnable.dataArray[i][5],NodeListenRunnable.
//			 * dataArray[i][6],linkPath)); } } //�������֪����ȡ����Ҫ������--add by YUYU
//			 * 20150721 Table_tr.addMouseListener(new MouseAdapter(){
//			 *
//			 * @Override public void mouseClicked(MouseEvent e) {
//			 * if(e.getClickCount()==2){ int
//			 * rowIndex=Table_tr.rowAtPoint(e.getPoint());//��ȡ������
//			 * System.out.println("��ȡ����"+rowIndex); for(int k=0;k<30;k++){
//			 * for(int tep=7; tep<30 &
//			 * (NodeListenRunnable.dataArray[k][tep+1]>0);){ int
//			 * linkcom=NodeListenRunnable.dataArray[k][1]; int
//			 * pro_no=NodeListenRunnable.dataArray[k][1]; String
//			 * linkf=NodeListenRunnable.dataArray[k][tep]+ "_" +
//			 * NodeListenRunnable.dataArray[k][tep+1]; String
//			 * linkb=NodeListenRunnable.dataArray[k][tep+1]+ "_" +
//			 * NodeListenRunnable.dataArray[k][tep];
//			 * recLinkcol(linkcom,linkf,linkb,pro_no); tep++;
//			 * System.out.println("ҵ��ָ�ʵ�� " + linkf +"tep"+ tep); } }
//			 * if(rowIndex>=0){//display ok //first step:recovery link style
//			 *
//			 * for(int tep=7; tep<30 &
//			 * (NodeListenRunnable.dataArray[rowIndex][tep]>0);tep++){
//			 * System.out.println("��·������ʾ"+NodeListenRunnable.dataArray[rowIndex][
//			 * tep]); }
//			 *
//			 * for(int tep=7; tep<30 &
//			 * (NodeListenRunnable.dataArray[rowIndex][tep+1]>0);){
//			 * //System.out.println("��·������ʾ"+NodeListenRunnable.dataArray[rowIndex
//			 * ][tep]); int pro_no=NodeListenRunnable.dataArray[rowIndex][4];
//			 * String linktep=NodeListenRunnable.dataArray[rowIndex][tep]+ "_" +
//			 * NodeListenRunnable.dataArray[rowIndex][tep+1]; String
//			 * linkbtep=NodeListenRunnable.dataArray[rowIndex][tep+1]+ "_" +
//			 * NodeListenRunnable.dataArray[rowIndex][tep];
//			 * setTraDisLinkcol(linktep,linkbtep,pro_no); tep++;
//			 * System.out.println("ҵ��������ʾ " + linktep +"tep"+ tep); } } else{
//			 * System.out.println("�������"); } } else{//û�е������ʱҲ��Ҫ�ָ� for(int
//			 * k=0;k<30;k++){ for(int tep=7; tep<30 &
//			 * (NodeListenRunnable.dataArray[k][tep+1]>0);){ int
//			 * linkcom=NodeListenRunnable.dataArray[k][1]; int
//			 * pro_no=NodeListenRunnable.dataArray[k][4]; String
//			 * linkf=NodeListenRunnable.dataArray[k][tep]+ "_" +
//			 * NodeListenRunnable.dataArray[k][tep+1]; String
//			 * linkb=NodeListenRunnable.dataArray[k][tep+1]+ "_" +
//			 * NodeListenRunnable.dataArray[k][tep];
//			 * recLinkcol(linkcom,linkf,linkb,pro_no); tep++;
//			 * System.out.println("ҵ��ָ�ʵ�� " + linkf +"tep"+ tep); } } }
//			 *
//			 * } });
//			 */
//			jf_tr.setResizable(false);
//			jf_tr.setVisible(true);
//		}
//
//	}
//
//	/**
//	 * Traffic��ʾ������
//	 */
//	private void DataToXML() {
//
//		ArrayList<Traffic> _allTraffics = trafficManager.allTraffics();
//		System.out.println(_allTraffics.size()+"�Ѿ���·�������");
//		for (Traffic tf : _allTraffics) {
//
//			String linkPath = "";
//			if (tf.hopNum == 0)
//				System.out.println("there is something wrong!");
//			else {
//				for (int _hop : tf.hop_nodes) {// wait for modify
//					int z = tf.hop_nodes.size();
//					if (_hop != tf.hop_nodes.get(z - 1)) {
//						Node linkpath_node_tep = (Node) (box.getElementByID("n" + _hop));
//						linkPath += linkpath_node_tep.getName() + "|";
//					} else {
//						Node linkpath_node_tep = (Node) (box.getElementByID("n" + _hop));
//						linkPath += linkpath_node_tep.getName();
//					}
//
//				}
//			}
//
//			System.out.println("ҵ���ʶ��" + tf.tunnelID + ";Դ�ڵ��ʶ" + tf.src_id + ";Ŀ�Ľڵ��ʶ" + tf.dst_id + ";·�������"
//					+ linkPath + ";ҵ�����ͣ�" + tf.granularityValue + ";ҵ�����ȣ�(ĿǰΪ��)" + "ͨ·����ʱ��" + "ͨ·��������ʱ��" + "ͨ·�ָ�����ʱ��"
//					+ "��ʼʱ��" + "����ʱ��");
//		}
//	}
//
//	/**
//	 * ��ʾAson��Ŀ�����巽��
//	 *
//	 * @author zhujian
//	 * @throws Exception
//	 */
//	public void showFrame() throws Exception {
//		// AsonManager frame = new AsonManager();//�������������
//		this.myself = this;
//		AnalysisXML analysis = new AnalysisXML();
//		this.setExtendedState(JFrame.MAXIMIZED_BOTH);// ���
//		this.setSize(1024, 768);
//		this.setTitle("ASON Network Manager 0.11, Beta Edition");// �������
//		this.setResizable(true);// �Ƿ�ɼ�
//
//		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// �رշ���
//		this.getContentPane().add(split, BorderLayout.CENTER);// add a panel to
//																// the content
//																// pane of the
//																// frame
//
//		split.setContinuousLayout(true); // �����϶��ָ����������������Ĵ�С
//		split.setDividerLocation(323);
//		split.setEnabled(true); // �����Ƿ����ô����
//		containpane.add(networkpane, BorderLayout.CENTER);
//
//		chooser.addChoosableFileFilter(filter); // Ϊ�ļ��Ի�������ļ�������
//		System.out.println("system begin");
//
//		try {
//			this.setDataBox();// ���ýڵ��ʼ����Ϣ
//
//			this.setMenuAndTool();
//			this.setMyNetworkPane();
//			this.setMyTreePane();
//
//			// this.startArea(areaNum);
//
//			this.makeNodeTable();
//			this.makeLinkTable();
//
//			// initWaitThread();
//			this.initDataSocketServerThread();
//			this.startIconThread();
//			this.initSocket();
//
//			this.initRerouteThread(); // ???????
//
//			this.initRerouteFiberFaultThread(); // fiber fault simulation
//
//			this.initFaultRecoveryThread(); // fault simulation
//
////			SearchNE();
////			this.killArea(analysis.startArea);
////			this.startArea(analysis.startArea);// ����
////			Thread.sleep(50000);
////			this.completetraffic(analysis.setpathtraffic);
////			this.DataToXML();
//			// box.clear();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		// frame.setAlwaysOnTop(true);
//		// TWaverUtil.centerWindow(frame);
//		this.setVisible(true);
//
//		/*
//		 * try { this.startArea(AnalysisXML.test()); } catch (Exception e) { //
//		 * TODO Auto-generated catch block e.printStackTrace(); }
//		 */
//	}
//}
