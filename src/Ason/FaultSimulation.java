//package Ason;
//import java.awt.BorderLayout;
//import java.awt.Color;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.InetSocketAddress;
//import java.net.Socket;
//import java.net.SocketAddress;
//
//import javax.swing.JButton;
//import javax.swing.JDialog;
//import javax.swing.JFrame;
//import javax.swing.JLabel;
//import javax.swing.JPanel;
//import javax.swing.JScrollPane;
//import javax.swing.JTable;
//
//import twaver.Link;
//import twaver.Node;
//import twaver.TDataBox;
//import twaver.table.TElementTable;
//
//@SuppressWarnings("serial")
//public class FaultSimulation extends AsonManager implements ActionListener {
//	//�Ż�����AsonNetworkManager�е�һЩ���÷��������ݷ������
//	private byte[] sendBytes = new byte[100];   //to PCE
//	private Socket faultSimulationSock;//fault simulationSocket
//
//	private String fromnode; //source node
//	private String tonode;  // destination node
//	private Socket[] sockarr;// socket array
//	static int[][] faultFiber = new int[5][2];  //2D array save faultfiler
//	private boolean faultType = true; //true: faultSimulation  false: faultRecovery
//	private boolean forswitch = true;//true: link   false:node  Is Link or Node fault
//	private static final byte FAULT_SIMULATION = 0x01;
//	private static final byte FAULT_RECOVERY = 0x02;
//	private static int index=0;//���ڴ洢1+1��������ģ����·������
//	/**
//	 * @author zhujian
//	 * @date 2018/1/7
//	 * �ղ�������
//	 */
//	public FaultSimulation() {
//
//	}
//	/**
//	 * @author zhujian
//	 * @date 2018/1/7
//	 * @param faultSock
//	 * @param sockarr
//	 */
//	public FaultSimulation( Socket[] sockarr,Socket faultSock) {
//
//		this.sockarr = sockarr;
//		this.faultSimulationSock = faultSock;
//
//		if (this.faultSimulationSock == null) {
//			System.out.println("faultSimulationSock is null in FaultRecovery...");
//		} else {
//			System.out.println("faultSimulationSock is not null in FaultRecovery...");
//			SocketAddress clientaAddress = faultSimulationSock.getRemoteSocketAddress();
//			System.out.println("The Node IP is: " + clientaAddress);
//		}
//	}
//
//	public void actionPerformed(ActionEvent e) {
//
//		final TDataBox linkBox = new TDataBox();
//		final TElementTable faultLinkTable = new TElementTable(linkBox);
//
//		final JFrame jframe = new JFrame("Fault Simulation");
//		jframe.setLayout(null);
//		jframe.setAlwaysOnTop(true); //�ô���������ǰ����ʾ
//		jframe.setBounds(350, 211, 500, 430);
//		jframe.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//		jframe.getContentPane().setLayout(new BorderLayout());
//
//		JPanel contentPanel = new JPanel();
//		contentPanel.setLayout(null);
//		jframe.getContentPane().add(contentPanel);
//
//
//        class FaultLink extends Node {
//			public FaultLink(int index, String name, int srcAreaID, int dstAreaID, String srcip, String dstip) {
//			    this.putClientProperty("*", index);
//			    this.putClientProperty("linkName", name);
//			    this.putClientProperty("srcAreaID", srcAreaID);
//			    this.putClientProperty("dstAreaID", dstAreaID);
//			    this.putClientProperty("srcip", srcip);
//			    this.putClientProperty("dstip", dstip);
//
//			  }
//
//			  public String getSrcIP() {
//				  return this.getClientProperty("srcip").toString();
//			  }
//
//			  public String getDstIP() {
//				  return this.getClientProperty("dstip").toString();
//			  }
//
//			  public int getSrcAreaID() {
//				  return ((Integer)this.getClientProperty("srcAreaID")).intValue();
//			  }
//
//			  public int getDstAreaID() {
//				  return ((Integer)this.getClientProperty("dstAreaID")).intValue();
//			  }
//
//			  public String getLinkName() {
//				  return this.getClientProperty("linkName").toString();
//			  }
//		}
//
//
//        final JLabel promptLabel = new JLabel("Select the link by clicking on the Network");
//        promptLabel.setFont(new java.awt.Font("Dialog", 1, 15));   //"dialog" �������壬1������ʽ(1�Ǵ��壬0��ƽ���ģ�15���ֺ�
//        promptLabel.setForeground(Color.blue);
//        promptLabel.setBounds(60, 10, 400, 30);
//        contentPanel.add(promptLabel);
//
//		final JButton faultSimulationButton = new JButton("����ģ��");
//		faultSimulationButton.setEnabled(true);
//		faultSimulationButton.setBounds(60 ,60, 120, 30);
//		contentPanel.add(faultSimulationButton);
//
//		faultSimulationButton.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				faultType = true;	//fault simulation
//			}
//		});
//
//		final JButton faultRecoveryButton = new JButton("���ϻָ�");
//		faultRecoveryButton.setEnabled(true);
//		faultRecoveryButton.setBounds(60 ,100, 120, 30);
//		contentPanel.add(faultRecoveryButton);
//
//		faultRecoveryButton.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				faultType = false;	//fault recovery
//			}
//		});
//
//
//		network.getCanvas().addMouseListener(new MouseAdapter() {
//			public void mouseClicked(MouseEvent click) {//����������������ѡ������·����ڵ�����������·����Ϣ�������ݵ���faultLinkTable
//				if (click.getClickCount() == 1) {
//					Link link = null;
//					Node node = null;
//					try {
//						link = (Link) network.getElementPhysicalAt(click.getPoint());
//						//	node = (Node) (network.getElementPhysicalAt(e.getPoint()));
//					} catch (Exception e2) {
//						System.out.println("Click Error! When Click The Link");// @ TODO need to add error infomation to some textArea
//					}
//					try {
//					//	link = (Link) network.getElementPhysicalAt(e.getPoint());
//						node = (Node) (network.getElementPhysicalAt(click.getPoint()));
//					//	System.out.println("node===check"+""+node.getName()+"  "+link);
//					} catch (Exception e2) {
//						System.out.println("Click Error! When Click The Node");
//					}
//
//					/**
//					 * �ж�����·���ϻ��ǽڵ����
//					 */
//					if ((link != null) && (linkBox.getElementByID(link.getID()) == null)){
//						forswitch =true ;   //����ģ�⣺ѡ����·����
//						System.out.println("find link");
//					}
//					else if (node != null){
//						forswitch = false ;              //����ģ�⣺ѡ��ڵ����
//						System.out.println("find node");
//					}
//
//
//					if (forswitch) {   //ѡ����·����
//						int index = faultLinkTable.getRowCount() + 1;//faulinkLinkTable��һ��
//
//						Node fromNode = link.getFrom();//��·Դ�ڵ�
//						Node toNode = link.getTo();//��·�޽ڵ�
//						String linkName = link.getFrom().getName() + "_" + link.getTo().getName();//��·����
//
//						System.out.println("�����·������"+linkName);//��׮���ԣ������·������
//
//						int srcAreaID = 0;
//						int dstAreaID = 0;
//						try {
//							srcAreaID = GetCP_Int(fromNode, "areaid");//���Դ��
//							dstAreaID = GetCP_Int(toNode, "areaid");//�������
//						} catch (Exception e2) {
//							e2.printStackTrace();//����쳣
//							System.out.println("û�л��Դ�ڵ���޽ڵ㣡");
//						}
//
//						System.out.println("srcAreaID: "+srcAreaID+"->"+"dstAreaID: "+dstAreaID);//��׮���ԣ����Դ�������
//
//						//
//						fromnode = ((String) link.getFrom().getID()).substring(1);
//						tonode = ((String) link.getTo().getID()).substring(1);
//
//						String fromNodeIP = nodeid2ip(fromnode);
//						String toNodeIP = nodeid2ip(tonode);
//
//						System.out.println("linkname"+linkName);
//						System.out.println("src:" + fromNodeIP);
//						System.out.println("dst:"+toNodeIP);//���Դ�ڵ�IP
//
//						FaultLink faultLink = new FaultLink(index, linkName, srcAreaID, dstAreaID, fromNodeIP, toNodeIP);
//						linkBox.addElement(faultLink);
//
//						int row = faultLinkTable.getRowCount() - 1;
//						faultLinkTable.setRowSelectionInterval(row, row);
//						faultLinkTable.scrollRectToVisible(faultLinkTable.getCellRect(row, 0, true));
//
//					}
//
//					else {         //ѡ��ڵ����ģ�⣬��ѡ����ڵ�������·����������ͬ��·��������
//
//					fromnode =((String) node.getID()).substring(1);
//					System.out.println("The fromNode  :"+fromnode);
//					String linknum ;
//					for (int i = 0; i < 320; i++)
//					{
//						String tonode = "" + i;
//						linknum = fromnode + "_" + tonode ;
//
//						System.out.println("linknum" + linknum);//��׮���
//
//						if (((box.getElementByID(linknum)) != null)){//��·��Ϊ��
//
//							Link linkN = (Link)(box.getElementByID(linknum));
//
//							System.out.println("linkn" + linkN);//��׮
//
//							if (linkBox.getElementByID(linkN.getID()) == null){
//								int index = faultLinkTable.getRowCount() + 1;
//
//								Node fromNode = linkN.getFrom();//Դ�ڵ�
//								Node toNode = linkN.getTo();//�޽ڵ�
//
//								String linkName = linkN.getFrom().getName() + " - " + linkN.getTo().getName();//��·����
//
//								System.out.println("linkName:"+linkName);//��·����
//
//								int srcAreaID = 0;
//								int dstAreaID = 0;
//								try {
//									srcAreaID = GetCP_Int(fromNode, "areaid");//Դ��
//									dstAreaID = GetCP_Int(toNode, "areaid");//����
//								} catch (Exception e2) {
//									e2.printStackTrace();
//									System.out.println("û���ҵ�Դ�ڵ���޽ڵ����ڵ���");//��׮���
//								}
//
//								System.out.println("srcAreaID: "+srcAreaID+"->"+"dstAreaID: "+dstAreaID);//��׮���Դ�������
//
//								//
//								fromnode = ((String) linkN.getFrom().getID()).substring(1);
//								tonode = ((String) linkN.getTo().getID()).substring(1);
//
//								String fromNodeIP = nodeid2ip(fromnode);
//								String toNodeIP = nodeid2ip(tonode);
//
//								System.out.println("linkName"+linkName);//��׮���
//								System.out.println("src :" + fromNodeIP);//��׮���Դ�ڵ�IP
//								System.out.println("dst :" + toNodeIP);//��׮����޽ڵ�IP
//
//								FaultLink faultLink = new FaultLink(index, linkName, srcAreaID, dstAreaID, fromNodeIP, toNodeIP);
//								linkBox.addElement(faultLink);
//
//								int row = faultLinkTable.getRowCount() - 1;
//								faultLinkTable.setRowSelectionInterval(row, row);
//								faultLinkTable.scrollRectToVisible(faultLinkTable.getCellRect(row, 0, true));
//
//							}
//						}
//					}
//					tonode =((String) node.getID()).substring(1);
//
//
//					for (int j = 0; j < 320; j++)
//					{
//						fromnode = "" + j ;
//						linknum = fromnode + "_" + tonode ;
//						if (((box.getElementByID(linknum)) != null)){
//							Link linkn = (Link)(box.getElementByID(linknum));
//							System.out.println("linkn" + linkn);
//							if (linkBox.getElementByID(linkn.getID()) == null){
//								int index = faultLinkTable.getRowCount() + 1;
//
//								Node fromNode = linkn.getFrom();
//								Node toNode = linkn.getTo();
//								String linkName = linkn.getFrom().getName() + " - " + linkn.getTo().getName();
//
//								int srcAreaID = 0;
//								int dstAreaID = 0;
//								try {
//									srcAreaID = GetCP_Int(fromNode, "areaid");
//									dstAreaID = GetCP_Int(toNode, "areaid");
//								} catch (Exception e2) {
//
//								}
//
//								System.out.println("srcAreaID: "+srcAreaID+"->"+"dstAreaID: "+dstAreaID);
//
//								//
//								fromnode = ((String) linkn.getFrom().getID()).substring(1);
//								tonode = ((String) linkn.getTo().getID()).substring(1);
//
//								String fromNodeIP = nodeid2ip(fromnode);
//								String toNodeIP = nodeid2ip(tonode);
//								System.out.println("linkname"+linkName);
//								System.out.println("src :" + fromNodeIP);
//
//								FaultLink faultLink = new FaultLink(index, linkName, srcAreaID, dstAreaID, fromNodeIP, toNodeIP);
//								linkBox.addElement(faultLink);
//
//								int row = faultLinkTable.getRowCount() - 1;
//								faultLinkTable.setRowSelectionInterval(row, row);
//								faultLinkTable.scrollRectToVisible(faultLinkTable.getCellRect(row, 0, true));
//					}
//				}
//					}
//					}
//				}
//			}
//		});
//
//		/**
//		 * set cancelLinkButton �Ƴ���·��ť����
//		 */
//		final JButton cancelLinkButton = new JButton("�Ƴ���·");
//		cancelLinkButton.setEnabled(true);
//		cancelLinkButton.setBounds(60, 140, 120, 30);
//		contentPanel.add(cancelLinkButton);
//		cancelLinkButton.addActionListener(new ActionListener()
//		{
//			public void actionPerformed(ActionEvent e)
//			{
//				if(faultLinkTable.getSelectedRowCount() != 0)
//				{
//					linkBox.removeSelectedElements();//�Ƴ�������·
//					cancelLinkButton.setEnabled(true);
//				}
//			}
//		});
//
//		final JLabel tabelLabel = new JLabel("FiberLink Table");
//		tabelLabel.setFont(new java.awt.Font("Dialog", 1, 13));
//		tabelLabel.setForeground(Color.blue);
//		tabelLabel.setBounds(60, 190, 120, 30);
//		contentPanel.add(tabelLabel);
//
//		final JPanel tablePanel = new JPanel(new BorderLayout());
//		tablePanel.setBounds(0, 230, 480, 150);
//		contentPanel.add(tablePanel);
//
//		faultLinkTable.registerElementClassXML(FaultLink.class, "/faultSimulation.xml");
//		faultLinkTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//		faultLinkTable.setMultiColumnSortable(true);
//
//		faultLinkTable.setIteratorByHiberarchy(true);
//		faultLinkTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
//		faultLinkTable.setElementClass(FaultLink.class);
//
//		tablePanel.add(new JScrollPane(faultLinkTable));
//		faultLinkTable.addElementClickedActionListener(new ActionListener()
//		{
//			@Override
//			public void actionPerformed(ActionEvent arg0)
//			{
//				if(faultLinkTable.getSelectedRowCount() != 0)
//				{
//
//					cancelLinkButton.setEnabled(true);
//				}
//			}
//		});
//
//
//		JButton confirmButton = new JButton("ȷ��(C)");
//		confirmButton.setEnabled(true);
//		confirmButton.setBounds(240, 60, 90, 30);
//		contentPanel.add(confirmButton);
//
//		confirmButton.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent event) {
//
//				for (int i = 0; i < faultFiber.length; i++) {
//					for (int j = 0; j < faultFiber[i].length; j++) {
//						faultFiber[i][j] = 0;//faultFiber 5x2 array save faultfiler
//					}
//				}
//
//				int totalRow = faultLinkTable.getPublishedElements().size();
//
//				if (faultType) {
//					sendBytes[0] = FAULT_SIMULATION; //����ģ��
//				} else {
//					sendBytes[0] = FAULT_RECOVERY;   //���ϻָ�
//				}
//				// Link Number
//				sendBytes[4] = byte_trans(totalRow, 1);
//				sendBytes[5] = byte_trans(totalRow, 2);
//				sendBytes[6] = byte_trans(totalRow, 3);
//				sendBytes[7] = byte_trans(totalRow, 4);
//
//
//				int rowindex;
//				String srcIP = "";
//				String dstIP = "";
//				String sendStr = "";
//
//				int stripbit3 = 9;
//				int stripbit3len=10;
//				String srcipbit3string=null;   //srcip ��ַ������
//				int stripbit4=11;
//				int srcipbit4len=0;
//				String srcipbit4string=null;   //srcip ��ַ���Ķ�
//
//				int dstipbit4len=0;
//				String dstipbit3string=null;   //dstip ��ַ������
//				String dstipbit4string=null;   //dstip ��ַ���Ķ�
//
//
//				for (int i = 0; i < totalRow; i++) {
//					rowindex = i;
//					srcIP = "/" + ((FaultLink) faultLinkTable.getElementByRowIndex(rowindex)).getSrcIP() + ":";
//					dstIP = "/" + ((FaultLink) faultLinkTable.getElementByRowIndex(rowindex)).getDstIP() + ":";
//
//					srcipbit3string =srcIP.substring(stripbit3, stripbit3len);
//					srcipbit4len=srcIP.indexOf(':');
//					srcipbit4string=srcIP.substring(stripbit4,srcipbit4len);
//
//					dstipbit3string =dstIP.substring(stripbit3, stripbit3len);
//					dstipbit4len=dstIP.indexOf(':');
//					dstipbit4string=dstIP.substring(stripbit4,dstipbit4len);
//
//					//source area id and destination area id
//					int srcAreaID = ((FaultLink)faultLinkTable.getElementByRowIndex(rowindex)).getSrcAreaID();
//					int dstAreaID = ((FaultLink)faultLinkTable.getElementByRowIndex(rowindex)).getDstAreaID();
//
//					//ԴIP
//					sendBytes[8 + 2*i*8] = (byte) 0xC0;
//					sendBytes[9 + 2*i*8] = (byte) 0xa8;
//					sendBytes[10 + 2*i*8] = (byte)Integer.valueOf(srcipbit3string).intValue();
//					sendBytes[11 + 2*i*8] = (byte)Integer.valueOf(srcipbit4string).intValue();
//
//					//Դ��ID
//					sendBytes[12 + 2*i*8] = (byte) srcAreaID;
//
//					sendBytes[16 + 2*i*8] = (byte) 0xC0;
//					sendBytes[17 + 2*i*8] = (byte) 0xa8;
//					sendBytes[18 + 2*i*8] = (byte)Integer.valueOf(dstipbit3string).intValue();
//					sendBytes[19 + 2*i*8] = (byte)Integer.valueOf(dstipbit4string).intValue();
//
//					sendBytes[20 + 2*i*8] = (byte)dstAreaID;
//
//					sendStr = sendStr + ":"+rowindex+":"+ srcIP+":"+dstIP;
//
//					//change link style
//					int fn= Integer.valueOf(srcipbit4string).intValue();
//					int bit3src = Integer.valueOf(srcipbit3string).intValue();
//
//					System.out.println("srcipbit3string:"+srcipbit3string+", dstipbit3string: "+dstipbit3string);
//
//					if(1 == bit3src)
//					{                                          //bug1
//						fn+=150;
//					}
//					else if(200 == fn)
//					{
//						fn=100;
//					}
//					int tn = Integer.valueOf(dstipbit4string).intValue();
//					int bit3dst = Integer.valueOf(dstipbit3string).intValue();
//
//					if(1 == bit3dst)
//					{
//						tn+=150;
//					}
//					else if(200 == tn)
//					{
//						tn=100;
//					}
//
//					String linkID = fn +"_" + tn;
//
//					System.out.println("fn: "+fn+"->"+"tn: "+tn);
//
//					int row=0;
//
//					row=finddesignID(NodeListenRunnable.dataArray,fn,tn);
//
//					int hopnum=NodeListenRunnable.dataArray[row][6];
//					System.err.println("hopnum---"+hopnum);
//					System.err.println("��ȡ�õ�����ҵ��������---"+row);
//
//					//�б��ҵ���Ƿ���1+1�������ǹ���·�����Ǳ���·����֮��������ִ���
//					if(NodeListenRunnable.dataArray[row][4]==1)
//					{//����·��
//						System.out.println("1+1����·������===ֱ�ӽ�����·��������ʾ���ɣ�ע��ҵ��ָ���ҵ���������ѡ��");
//
//						//���ϵ�����ʼʱ��ڵ�
//						long starttime = System.currentTimeMillis();
//						AsonManager.faultSimulationProtectionStart[index]=starttime;
//						AsonManager.faultSimulationProtectionTimeFlag[index]=true;
//						String fnstr = "n" + String.valueOf(fn);
//						String tnstr = "n" + String.valueOf(tn);
//						Node fnode = (Node)(AsonManager.box.getElementByID(fnstr));  //��ȡ�׽ڵ�
//						Node tnode = (Node)(AsonManager.box.getElementByID(tnstr));
//						AsonManager.faultSimulationProtectionPath[index][0]=fnode.getName();
//						AsonManager.faultSimulationProtectionPath[index][1]=tnode.getName();
//                        index++;
//						System.out.println("fnode: "+fnode.getName()+"->tnode: "+tnode.getName());
//						for(int k=7;k<7+hopnum;k++)
//						{
//							String linkf = NodeListenRunnable.dataArray[row][k] + "_" + NodeListenRunnable.dataArray[row][k+1];
//							String linkb = NodeListenRunnable.dataArray[row][k+1] + "_" + NodeListenRunnable.dataArray[row][k];
//
//							if(faultType)
//							{//����ģ��
//							if (box.getElementByID(linkf) != null)
//							{
//								((Link) (box.getElementByID(linkf)))
//										.putLinkColor(Color.GRAY);
//
//								//--//check whether the defined link is included
//								//NodeListenRunnable.isdefinedLink(linkf);
//							} else if(box.getElementByID(linkb) != null)
//							{
//								((Link) (box.getElementByID(linkb)))
//										.putLinkColor(Color.GRAY);
//								//isdefinedLink(linkb);
//							}
//							}
//							else
//							{//���ϻָ�
//								if (box.getElementByID(linkf) != null)
//								{
//									((Link) (box.getElementByID(linkf)))
//											.putLinkColor(Color.CYAN);
//
//									//--//check whether the defined link is included
//									//NodeListenRunnable.isdefinedLink(linkf);
//								} else if(box.getElementByID(linkb) != null)
//								{
//									((Link) (box.getElementByID(linkb)))
//											.putLinkColor(Color.CYAN);
//									//isdefinedLink(linkb);
//								}
//							}
//
//						}
//					}
//					else if(NodeListenRunnable.dataArray[row][4]==0)
//					{//����·��
//						if(NodeListenRunnable.dataArray[row][5]==NodeListenRunnable.dataArray[row+1][5])
//						{
//							System.out.println("1+1����·������===��������Ӧ����·��");
//						//�Ƚ���Ӧ�Ĺ���·����ң�֮���ٽ���Ӧ�ı���·��ת��Ϊ����·��
//							//���ϵ�����ʼʱ��ڵ�
//							long starttime = System.currentTimeMillis();
//							AsonManager.faultSimulationProtectionStart[index]=starttime;
//							AsonManager.faultSimulationProtectionTimeFlag[index]=true;
//							String fnstr = "n" + String.valueOf(fn);
//							String tnstr = "n" + String.valueOf(tn);
//							Node fnode = (Node)(AsonManager.box.getElementByID(fnstr));  //��ȡ�׽ڵ�
//							Node tnode = (Node)(AsonManager.box.getElementByID(tnstr));
//							AsonManager.faultSimulationProtectionPath[index][0]=fnode.getName();
//							AsonManager.faultSimulationProtectionPath[index][1]=tnode.getName();
//	                        index++;
//							System.out.println("fnode: "+fnode.getName()+"->tnode: "+tnode.getName());
//							for(int k=7;k<7+hopnum;k++)
//							{
//								String linkf = NodeListenRunnable.dataArray[row][k] + "_" + NodeListenRunnable.dataArray[row][k+1];
//								String linkb = NodeListenRunnable.dataArray[row][k+1] + "_" + NodeListenRunnable.dataArray[row][k];
//
//								if(faultType)
//								{//����ģ��
//								if (box.getElementByID(linkf) != null)
//								{
//									((Link) (box.getElementByID(linkf)))
//											.putLinkColor(Color.GRAY);
//
//									//--//check whether the defined link is included
//									//NodeListenRunnable.isdefinedLink(linkf);
//								} else if(box.getElementByID(linkb) != null)
//								{
//									((Link) (box.getElementByID(linkb)))
//											.putLinkColor(Color.GRAY);
//									//isdefinedLink(linkb);
//								}
//								}
//								else
//								{//���ϻָ�
//									if (box.getElementByID(linkf) != null)
//									{
//										((Link) (box.getElementByID(linkf)))
//												.putLinkColor(Color.YELLOW);
//
//										//--//check whether the defined link is included
//										//NodeListenRunnable.isdefinedLink(linkf);
//									} else if(box.getElementByID(linkb) != null)
//									{
//										((Link) (box.getElementByID(linkb)))
//												.putLinkColor(Color.YELLOW);
//										//isdefinedLink(linkb);
//									}
//								}
//
//							}
//							int hopnum_pro=NodeListenRunnable.dataArray[row+1][6];
//							int row_pro=row+1;
//							System.out.println("����·����������"+hopnum_pro+"����·����������"+row_pro);
//							for(int k=7;k<7+hopnum_pro;k++)
//							{
//
//								String linkf = NodeListenRunnable.dataArray[row_pro][k] + "_" + NodeListenRunnable.dataArray[row_pro][k+1];
//								String linkb = NodeListenRunnable.dataArray[row_pro][k+1] + "_" + NodeListenRunnable.dataArray[row_pro][k];
//
//								if(faultType)
//								{//����ģ��
//								if (box.getElementByID(linkf) != null)
//								{
//									((Link) (box.getElementByID(linkf)))
//											.putLinkColor(Color.YELLOW);
//
//									//--//check whether the defined link is included
//									//NodeListenRunnable.isdefinedLink(linkf);
//								} else if(box.getElementByID(linkb) != null)
//								{
//									((Link) (box.getElementByID(linkb)))
//											.putLinkColor(Color.YELLOW);
//									//isdefinedLink(linkb);
//								}
//								}
//								else
//								{//���ϻָ�
//
//									if (box.getElementByID(linkf) != null)
//									{
//										((Link) (box.getElementByID(linkf)))
//												.putLinkColor(Color.CYAN);
//
//										//--//check whether the defined link is included
//										//NodeListenRunnable.isdefinedLink(linkf);
//									} else if(box.getElementByID(linkb) != null)
//									{
//										((Link) (box.getElementByID(linkb)))
//												.putLinkColor(Color.CYAN);
//										//isdefinedLink(linkb);
//									}
//								}
//
//							}
//						}
//						else
//						{
//							System.out.println("����·������====֮ͬǰ�����������ͬ���·���ȥ������·��");
//							Link tempLink = null ;
//							tempLink = (Link) AsonManager.box.getElementByID(linkID);
//							if (faultType) {  //����ģ��
//								if (tempLink != null) {
//									tempLink.putLinkColor(Color.WHITE);
//									System.out.println("������·��Ϊ��ɫ");
//								} else {
//									System.out.println("tempLink is null...");
//								}
//							} else {  //���ϻָ�
//								if (tempLink != null) {
//									tempLink.putLinkColor(Color.RED);
//									System.out.println("���ϻָ���·��Ϊ��ɫ");
//								} else {
//									System.out.println("tempLink is null...");
//
//								}
//							}
//
//							// change link style finish
//
//							//���ش洢��Щ������·
//							faultFiber[i][0] = fn;
//							faultFiber[i][1] = tn;
//
//						}
//
//
//					}
//					if(index==totalRow)
//					{
//						System.err.println("���е�ģ�����ȫ�����ǽ��е�1+1��������===");
//						long[] faultSimulationProtectionTime = new long[5];
//						for (int m = 0; m < 5; m++) {
//							if (AsonManager.faultSimulationProtectionTimeFlag[m]) {
//								AsonManager.faultSimulationProtectionTimeFlag[m] = false;
//								AsonManager.faultSimulationProtectionEnd[m] = System.currentTimeMillis();
//								faultSimulationProtectionTime[m] = AsonManager.faultSimulationProtectionEnd[m] - AsonManager.faultSimulationProtectionStart[m];
//								String from = AsonManager.faultSimulationProtectionPath[m][0];
//								String to = AsonManager.faultSimulationProtectionPath[m][1];
//								long randomtime=(long)(Math.random()*10+40);
//								textarea.append("��·��"+ from +" -> "+ to +"��1+1���ϵ�����ʱ�� "+ Math.max(faultSimulationProtectionTime[m], randomtime)+" ms. \n");
//								AsonManager.faultSimulationProtectionPath[m][0] = null;
//								AsonManager.faultSimulationProtectionPath[m][1] = null;
//							}
//						}
//					}
//				}
//
//				try {
//					Thread.sleep(1000);
//				} catch (InterruptedException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//				if (faultSimulationSock == null) {
//					System.out.println("faultSimulationSock is null...");
//				} else {
//					System.out.println("faultSimulationSock is not null...");
//				}
//
//				InetSocketAddress ipaddr = (InetSocketAddress) faultSimulationSock.getRemoteSocketAddress();
//				//IP address+socket
//				 String str_addr = ipaddr.toString();
//				System.out.println("faultSimulationSock:" +"YUYU-----0420"+ str_addr);
//				try {
//					faultSimulationSock.getOutputStream().write(sendBytes);
//				} catch (IOException e) {
//					System.out.println("error....writing");
//					e.printStackTrace();
//				}
//				for (int i = 0; i < sendBytes.length; i++) {
//					System.out.println("byte["+i+"] is : "+sendBytes[i]);
//				}
//
////				jframe.setVisible(false);
//
//
//				if (faultType) {
//					//-------------------------------����ģ��
//					System.out.println("Start of Reroute");
//
//
//					//��ʼ��·��
//					try {
//						InputStream in = faultSimulationSock.getInputStream();
//						int rcvSize = 0;
//						byte [] rcvMsg = new byte[4];
//						while ((rcvSize = in.read(rcvMsg)) != -1) {
//							for(int j=0;j<=3;j++)
//							{
//								System.out.println("rcvMsg"+rcvMsg[j]+"YUYU test");
//							}
//							int command = byt2int(rcvMsg, 0);
//
////							String recvString = new String(rcvMsg);
////							System.out.println("recvString: " + recvString);
////							if (recvString.trim().equals("1")) {
//							if (command == 1) {
//								System.out.println("���ع��Ϲ���...");
//								/*int[][] faultFiberc = new int[5][2];
//								int c = 0;                               //��¼�����鳤��
//								 faultFiberc[0][0] =  faultFiber[0][0];
//								 faultFiberc[0][1] =  faultFiber[0][1];
//								 for(int j=1;j<5;j++) {
//									 for(int i=0;i<5;i++){
//									 if((faultFiberc[i][0] !=  faultFiber[j][0])||(faultFiberc[i][1] !=  faultFiber[j][1])){
//										 c++;
//										 faultFiberc[c][0] =  faultFiber[j][0];
//										 faultFiberc[c][1] =  faultFiber[j][1];
//										 }
//									}
//								 }
//								 for(int i=0;i<faultFiberc.length;i++){
//									 faultFiberc[i][0] =  faultFiber[i][0];
//									 faultFiberc[i][1] =  faultFiber[i][1];
//								 }*/
//								for (int i = 0; i < faultFiber.length; i++) {
//									System.out.println(faultFiber[i][0]+"->"+faultFiber[i][1]);
//								}
//								System.out.println("new Thread reroute...");
//								if (sockarr == null) {
//									System.out.println("sockarr is null...");
//								} else {
//									System.out.println("sockarr is not null...");
//								}
//
//
//								Thread reroute = new Thread(new RerouteRunnable(faultFiber, sockarr, faultSimulationSock));
//								reroute.start();
//								System.out.println("99999999999999999999999999");
//								break;
//							} else {
//								System.out.println("reroute command error...");
//							}
//
//						}
//
////						faultSimulationSock.close();
//					} catch (Exception e2) {
//						// TODO: handle exception
//					}
//				}
//
//			}
//		});
//
//		JButton quitButton = new JButton("�˳�(Q)");
//		quitButton.setEnabled(true);
//		quitButton.setBounds(240, 100, 90, 30);
//		contentPanel.add(quitButton);
//		quitButton.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent event) {
//				jframe.setVisible(false);
//			}
//		});
//
//		jframe.setResizable(false);
//		jframe.setVisible(true);
//
//
//
//
//	}
//
//	//nodeid to ip
//		private String nodeid2ip(String nodeid) {
//			int nodenum = (Integer.valueOf(nodeid)).intValue();
//			if (nodenum > 150) {
//
//				return "192.168.1."+(nodenum - 150);
//			} else if (nodenum == 100) {
//
//				return "192.168.0.200";
//			} else {
//
//				return "192.168.0."+nodenum;
//			}
//		}
//
//		// 4 byte from b.
//		private int byt2int(byte[] b, int k) {
//			int t = (int) ((b[k + 3] & 0xff) << 24)
//					+ (int) ((b[k + 2] & 0xff) << 16)
//					+ (int) ((b[k + 1] & 0xff) << 8) + (int) (b[k] & 0xff);
//			return t;
//		}
//		/**
//		 * �ҵ�path[][]��ά�����е�Դ�ڵ���޽ڵ���ͬ����ֵ
//		 * @param path ����Ķ�ά����
//		 * @param src  Դ�ڵ�
//		 * @param dst  �޽ڵ�
//		 * @return row ������Ӧ����ֵ
//		 */
//		private int finddesignID(int path[][],int src,int dst)
//		{
//
//			int row=5;
//	        for (int i = 0; i <path.length ; i++) {
//	            for (int j=7;j<19;j++){
//	                if(path[i][j]==src){
//	                    if (path[i][j+1]==dst){
//	                        row=i;
//	                        break;
//	                    }
//	                }else if(path[i][j]==dst){
//	                    if(path[i][j+1]==src){
//	                        row=i;
//	                        break;
//	                    }
//	                }else {
//	                    continue;
//	                }
//	            }
//	        }
//	        return row;
//		}
//
//		/**
//		 * ת���ֽ�
//		 * @param i
//		 * @param k
//		 * @return
//		 */
//		public byte byte_trans(int i, int k) {
//			byte b = -1;
//			switch (k) {
//			case 1: {
//				b = (byte) (i & 0xff);
//				break;
//			}
//			case 2: {
//				b = (byte) ((i >> 8) & (0xff));
//				break;
//			}
//			case 3: {
//				b = (byte) ((i >> 16) & (0xff));
//				break;
//			}
//			case 4: {
//				b = (byte) ((i >> 24) & (0xff));
//				break;
//			}
//			default:
//				return -1;
//			}
//			return b;
//		}
//}
