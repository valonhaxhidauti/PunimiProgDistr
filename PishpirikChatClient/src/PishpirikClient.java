import java.rmi.*;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;

import java.awt.event.*;

import javax.swing.border.LineBorder;
import javax.swing.event.*;

public class PishpirikClient extends JFrame implements ActionListener, Runnable {
	private static PishpirikChat c;

	private static String ipAddress;
	private int port;
	private BufferedReader in = null;
	private Thread thread;

	private JButton jButtonConnect;
	private JButton jButtonSend;
	private JButton jButtonPlay;
	private static JButton[] imazhi = new JButton[4];

	private static String[] lojtari;
	private static ArrayList<String> lojtaret = new ArrayList<>();
	private static ArrayList<String> kontainer = new ArrayList<>();

	private static JLabel jLabelUserList;
	private static JLabel jLabelPritni;
	private static JLabel shfaqkontainer;
	private static JLabel radhaE;
	private static JLabel piket;
	private static JLabel[] letratKundershtarit;

	private JList jListUser;
	private JScrollPane jScrollPaneListUser;
	private JScrollPane jScrollPaneMessage;
	private static JTextArea jTextAreaMessage;
	private JTextField jTextSendMessage;
	private static JTextField jTextUserName;
	static boolean aperfundoi = false;
	private Socket socket = null;
	private DefaultListModel listClient;

	private String message;

	private final String SEPARATOR = "\\*\\*\\*";
	private final String PUBLICMESSAGE = "PUBLICMESSAGE";
	private final String ONLINE = "ONLINE";
	private final String OFFLINE = "OFFLINE";

	public PishpirikClient() {
		initComponents();
		thread = new Thread(this);
	}

	@SuppressWarnings("unchecked")
	private void initComponents() {
		listClient = new DefaultListModel();
		jScrollPaneMessage = new JScrollPane();
		jTextAreaMessage = new JTextArea();
		jTextUserName = new JTextField();
		jButtonConnect = new JButton();

		jScrollPaneListUser = new JScrollPane();
		jListUser = new JList(listClient);
		jTextSendMessage = new JTextField();
		jButtonSend = new JButton();
		jButtonPlay = new JButton();
		jLabelUserList = new JLabel();
		jLabelPritni = new JLabel();
		shfaqkontainer = new JLabel();
		letratKundershtarit = new JLabel[4];
		radhaE = new JLabel();
		piket = new JLabel();

		getContentPane().setBackground(new Color(0, 51, 51));
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("Chat Client and Pishpirik");
		setResizable(true);
		getContentPane().setLayout(null);

		jTextAreaMessage.setColumns(20);
		jTextAreaMessage.setEditable(false);
		jTextAreaMessage.setRows(5);
		jTextAreaMessage.setAutoscrolls(false);
		jScrollPaneMessage.setViewportView(jTextAreaMessage);

		getContentPane().add(jScrollPaneMessage);
		//vendi ku shfaqen mesazhet
		jScrollPaneMessage.setBounds(10, 300, 420, 240);

		jTextUserName.addActionListener(this);
		getContentPane().add(jTextUserName);
		//vendi per shkruarjen e emrit te perdoruesit
		jTextUserName.setBounds(10, 10, 180, 20);

		jButtonConnect.setFont(new java.awt.Font("Verdana", 0, 16));
		jButtonConnect.setText("Connect");

		getContentPane().add(jButtonConnect);
		jButtonConnect.setBounds(200, 10, 145, 21);

		jListUser.setToolTipText("List of User");
		jScrollPaneListUser.setViewportView(jListUser);

		getContentPane().add(jScrollPaneListUser);
		//lista
		jScrollPaneListUser.setBounds(10, 50, 330, 200);

		jTextSendMessage.addActionListener(this);

		getContentPane().add(jTextSendMessage);
		//text field per mesazhe
		jTextSendMessage.setBounds(10, 560, 330, 30);

		jButtonSend.setFont(new java.awt.Font("Verdana", 0, 16));
		jButtonSend.setText("Send");
		jButtonSend.addActionListener(this);

		jButtonPlay.setFont(new java.awt.Font("Verdana", 0, 36));
		jButtonPlay.setText("Luaj");
		jButtonPlay.addActionListener(this);

		ImageIcon imgg = new ImageIcon("res/letrapas.jpg");
		for (int i = 0; i < imazhi.length; i++) {
			imazhi[i] = new JButton();
			getContentPane().add(imazhi[i]);
			imazhi[i].setBounds(600 + i * 170, 560, 150, 218);
			imazhi[i].setVisible(false);
			imazhi[i].addActionListener(this);
			letratKundershtarit[i] = new JLabel();
			getContentPane().add(letratKundershtarit[i]);
			letratKundershtarit[i].setBounds(750 + i * 80, 10, 75, 115);
			letratKundershtarit[i].setIcon(imgg);
			letratKundershtarit[i].setVisible(false);

		}

		getContentPane().add(jButtonSend);
		jButtonSend.setBounds(350, 561, 90, 30);

		// play button
		getContentPane().add(jButtonPlay);
		jButtonPlay.setBounds(800, 300, 300, 150);

		jLabelUserList.setFont(new java.awt.Font("Arial Black", 0, 11));
		jLabelUserList.setForeground(Color.WHITE);
		jLabelUserList.setText("User List");
		getContentPane().add(jLabelUserList);
		jLabelUserList.setBounds(10, 30, 340, 17);

		getContentPane().add(shfaqkontainer);
		shfaqkontainer.setBounds(820, 200, 160, 227);
		shfaqkontainer.setBorder(BorderFactory.createMatteBorder(0, 0, 12, 12, Color.WHITE));

		shfaqkontainer.setVisible(false);
		Font font = new Font("Verdana", Font.BOLD, 12);
		getContentPane().add(radhaE);
		radhaE.setBounds(1200, 30, 250, 30);
		radhaE.setFont(font);
		radhaE.setForeground(Color.WHITE);
		radhaE.setText("");
		radhaE.setForeground(Color.WHITE);
		radhaE.setVisible(false);

		getContentPane().add(piket);
		piket.setBounds(1200, 60, 400, 70);
		piket.setFont(new java.awt.Font("Verdana", 0, 18));
		piket.setText("");
		piket.setForeground(Color.WHITE);
		piket.setVisible(false);

		// jLabelPritni
		jLabelPritni.setFont(font);
		jLabelPritni.setText("");
		jLabelPritni.setForeground(Color.WHITE);
		getContentPane().add(jLabelPritni);
		jLabelPritni.setBounds(750, 280, 550, 22);

		jLabelPritni.setVisible(false);

		java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		setBounds((screenSize.width - 570) / 2, (screenSize.height - 330) / 2, 1570, 830);

		jButtonConnect.addActionListener(this);
		jButtonSend.setEnabled(false);
		jButtonPlay.setEnabled(false);
	}

	public static void main(String args[]) {

		try {
			PishpirikClient rm = new PishpirikClient();
			 rm.setIPAddress(args[0]);
			 rm.setPort(Integer.parseInt(args[1]));
			 rm.setServer(ipAddress);
			rm.setVisible(true);
		} catch (ArrayIndexOutOfBoundsException ae) {
			JOptionPane.showMessageDialog(null, "Please insert the port", "ATTENTION", JOptionPane.INFORMATION_MESSAGE);
			System.exit(-1);
		}
		Runnable runnable = new Runnable() {

			public void run() {
				while (true && !aperfundoi) {
					try {

						if (letraPerseri()) {
							if (c.ktheNrKartave() == 0 && !imazhi[0].isVisible() && !imazhi[1].isVisible()
									&& !imazhi[2].isVisible() && !imazhi[3].isVisible()) {
								aperfundoi = true;
								shfaqkontainer.setIcon(null);
								if (c.kthePiket()[0] > c.kthePiket()[1]) {
									shfaqkontainer
											.setText("<html><h2 style='color:white;font-weight:bold;font-size:36px;'>"
													+ lojtaret.get(0).toUpperCase()
													+ " ka fituar lojen</h2><br><h2 style='color:red;font-weight:bold;'>"
													+ lojtaret.get(1).toUpperCase() + " ka humbur lojen</h2></html>");
								} else if (c.kthePiket()[0] < c.kthePiket()[1]) {
									shfaqkontainer
											.setText("<html><h2 style='color:white;font-weight:bold;font-size:36px'>"
													+ lojtaret.get(1).toUpperCase()
													+ " ka fituar lojen</h2><br><h2 style='color:red;font-weight:bold;'>"
													+ lojtaret.get(0).toUpperCase() + " ka humbur lojen</h2></html>");
								} else if (c.kthePiket()[0] == c.kthePiket()[1]) {
									shfaqkontainer.setText(
											"<html><h1 style='color:white;font-weight:bold;font-size:36px'>LOJTARET JANE BARAZ</h1></html>");

								}
								shfaqkontainer.setBounds(800, 300, 350, 218);
							} else {
								c.shperndajLetrat();
							}
						}
						lojtaret = c.kthe_Lojtaret();
						updateNrPlayers(c.ktheMesazhinLojes());

						if (lojtaret.size() >= 2) {
							piket.setVisible(true);
							piket.setText("<html>PIKET:<br>" + lojtaret.get(0).toUpperCase() + ": " + c.kthePiket()[0]
									+ "<br>" + lojtaret.get(1).toUpperCase() + ": " + c.kthePiket()[1] + "</html>");

							radhaE.setVisible(true);
							if (c.ktheradhen() == 1) {
								radhaE.setText(lojtaret.get(0).toUpperCase() + " e ka radhen");
							} else {
								radhaE.setText(lojtaret.get(1).toUpperCase() + " e ka radhen");
							}
							if (jTextUserName.getText().equals(lojtaret.get(0))) {
								lojtari = c.letrat_player1();
								for (int i = 0; i < c.letrat_player2().length; i++) {
									if (!c.letrat_player2()[i].equals("a"))
										letratKundershtarit[i].setVisible(true);
									else
										letratKundershtarit[i].setVisible(false);
								}

							} else {
								lojtari = c.letrat_player2();
								for (int i = 0; i < c.letrat_player1().length; i++) {
									if (!c.letrat_player1()[i].equals("a"))
										letratKundershtarit[i].setVisible(true);
									else
										letratKundershtarit[i].setVisible(false);
								}
							}

							kontainer = c.kthe_kontainer();
							if (!aperfundoi)
								if (kontainer.size() >= 1) {
									janeFituar();
									ImageIcon imgThisImg1 = new ImageIcon("res/" + kontainer.get(kontainer.size() - 1));
									shfaqkontainer.setIcon(imgThisImg1);
									jLabelPritni.setVisible(false);
									shfaqkontainer.setVisible(true);
								} else if (kontainer.size() == 0) {
									ImageIcon imgThisImg1 = new ImageIcon("res/1_1.jpg");
									shfaqkontainer.setIcon(imgThisImg1);
								}

							for (int i = 0; i < imazhi.length; i++) {
								if (!lojtari[i].equals("a")) {
									ImageIcon imgThisImg = new ImageIcon("res/" + lojtari[i]);
									imazhi[i].setIcon(imgThisImg);
									imazhi[i].setVisible(true);
								} else {

									imazhi[i].setVisible(false);
								}
							}

						}
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
			}
		};

		Thread thread1 = new Thread(runnable);
		thread1.start();

	}

	public void ndalo() {

		for (int i = 0; i < imazhi.length; i++) {
			imazhi[i].setVisible(false);
			letratKundershtarit[i].setVisible(false);
		}
		jButtonPlay.setVisible(true);
		jLabelPritni.setVisible(false);
		lojtaret.clear();
		kontainer.clear();
		shfaqkontainer.setVisible(false);
		radhaE.setVisible(false);
		piket.setVisible(false);
	}

	public static void janeFituar() throws RemoteException {
		String fundit = kontainer.get(kontainer.size() - 1).split("_")[0];
		if ((c.ktheradhen() == 0) && (jTextUserName.getText().equals(lojtaret.get(1)))) {
			if (kontainer.size() >= 2) {
				String parafundit = kontainer.get(kontainer.size() - 2).split("_")[0];
				if ((fundit.equals(parafundit) || fundit.equals("11")) && kontainer.size() > 2) {
					c.rritPiket(c.ktheradhen(), kontainer.size());
					c.fshij_kontainer();
				} else if ((fundit.equals(parafundit) && !fundit.equals("11")) && kontainer.size() == 2) {
					c.rritPiket(c.ktheradhen(), 10);

					c.fshij_kontainer();
				} else if (fundit.equals("11") && kontainer.size() == 2) {
					c.rritPiket(c.ktheradhen(), kontainer.size());
					c.fshij_kontainer();
				}
			}
		} else if ((c.ktheradhen() == 1) && (jTextUserName.getText().equals(lojtaret.get(0)))) {
			if (kontainer.size() >= 2) {
				String parafundit = kontainer.get(kontainer.size() - 2).split("_")[0];
				if ((fundit.equals(parafundit) || fundit.equals("11")) && kontainer.size() > 2) {
					c.rritPiket(c.ktheradhen(), kontainer.size());
					c.fshij_kontainer();
				} else if ((fundit.equals(parafundit) && !fundit.equals("11")) && kontainer.size() == 2) {
					c.rritPiket(c.ktheradhen(), 10);
					c.fshij_kontainer();
				} else if (fundit.equals("11") && kontainer.size() == 2) {
					c.rritPiket(c.ktheradhen(), kontainer.size());
					c.fshij_kontainer();
				}
			}
		}
	}

	public static boolean letraPerseri() throws RemoteException {
		int p = 0;
		String[] player1 = c.letrat_player1();
		String[] player2 = c.letrat_player2();
		for (int i = 0; i < player1.length; i++) {
			if (player1[i].equals("a")) {
				p++;
			}
			if (player2[i].equals("a")) {
				p++;
			}
		}
		if (p == 8)
			return true;

		return false;
	}

	private void setIPAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	private void setPort(int port) {
		this.port = port;
	}

	private String getIPAddress() {
		return ipAddress;
	}

	private int getPort() {
		return port;
	}

	private void setServer(String ipAddress) {
		try {
			c = (PishpirikChat) Naming.lookup("rmi://" + ipAddress + "/ChatService");
		}

		  catch (MalformedURLException murle) { System.out.println();
		  System.out.println("MalformedURLException");
		  System.out.println(murle); }

		catch (RemoteException re) {
			System.out.println();
			System.out.println("RemoteException");
			System.out.println(re);
		} catch (NotBoundException nbe) {
			System.out.println();
			System.out.println("NotBoundException");
			System.out.println(nbe);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void updateMessage(String username, String message) throws RemoteException {
		jTextAreaMessage.append(username + " :  " + message + "\n");
	}

	public static void updateNrPlayers(String the) throws RemoteException {
		jLabelPritni.setText(the);

	}

	public void run() {
		System.out.println(socket);

		try {
			updateNrPlayers(c.ktheMesazhinLojes()+" "+c.nrlojtareve());
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			while ((message = in.readLine()) != null) {
				System.out.println(message);
				String[] fromServer = message.split(SEPARATOR);

				if (fromServer[0].equals(ONLINE) || fromServer[0].equals(OFFLINE)) {
					updateClient(c.getClientList());
				} else if (fromServer[0].equals(PUBLICMESSAGE)) {
					String sender = fromServer[1];
					String content = fromServer[2];
					updateMessage(sender, content);
				}
			}

			in.close();
			socket.close();
		} catch (java.net.UnknownHostException e) {
		} catch (IOException e) {
		}
	}

	public void updateClient(ArrayList allClientList) throws RemoteException {
		listClient.clear();
		int i = 0;
		String username;

		for (i = 0; i < allClientList.size(); i++) {
			username = allClientList.get(i).toString();
			listClient.addElement(username);
		}
	}

	public void actionPerformed(ActionEvent ae) {
		if (ae.getActionCommand().equals("Connect") && !jTextUserName.getText().equals("")) {
			try {
				System.out.println("Player :" + jTextUserName.getText());
				c.connect("Player :" + jTextUserName.getText());
				socket = new Socket(ipAddress, port);
				jTextUserName.setEditable(false);
				jButtonConnect.setText("Disconnect");
				System.out.println("You are connect to server");
				thread.start();
				jButtonSend.setEnabled(true);
				jButtonPlay.setEnabled(true);

			} catch (RemoteException re) {
			} catch (java.net.UnknownHostException e) {
				JOptionPane.showMessageDialog(null, "Can not connect to server " + ipAddress, "WARNING",
						JOptionPane.WARNING_MESSAGE);
				System.exit(-1);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "The server " + ipAddress + " on port " + port + " is not found!",
						"ERROR", JOptionPane.ERROR_MESSAGE);
				System.exit(-1);
			}
		} else if (ae.getActionCommand().equals("Disconnect")) {
			try {
				c.disconnect(jTextUserName.getText());
				c.rifillo();
				ndalo();
				jTextUserName.setText("");
				jTextUserName.setEditable(true);
				listClient.clear();
				jButtonConnect.setText("Connect");
				thread.interrupt();

			} catch (RemoteException re) {
			}
		} else if (ae.getActionCommand().equals("Send") && !jTextUserName.getText().equals("")) {
			try {
				c.sendPublicMessage(PUBLICMESSAGE, jTextUserName.getText(), jTextSendMessage.getText());
				jTextSendMessage.setText("");
				//jTextAreaMessage.append(getName() + " :  " + message + "\n");
			} catch (RemoteException re) {
			}
		} else if (ae.getSource() == imazhi[0]) {

			try {
				if ((jTextUserName.getText().equals(lojtaret.get(0)) && c.ktheradhen() == 1)
						|| (jTextUserName.getText().equals(lojtaret.get(1)) && c.ktheradhen() == 0)) {
					c.rritRadhen();
					c.shto_kontainer(lojtari[0]);
					imazhi[0].setVisible(false);
					if (jTextUserName.getText().equals(lojtaret.get(0))) {
						c.modifikoKartat1(0);

					} else {
						c.modifikoKartat2(0);
					}
				}
			} catch (RemoteException re) {
			}
		} else if (ae.getSource() == imazhi[1]) {
			try {
				if ((jTextUserName.getText().equals(lojtaret.get(0)) && c.ktheradhen() == 1)
						|| (jTextUserName.getText().equals(lojtaret.get(1)) && c.ktheradhen() == 0)) {
					c.rritRadhen();
					c.shto_kontainer(lojtari[1]);
					imazhi[1].setVisible(false);
					if (jTextUserName.getText().equals(lojtaret.get(0))) {
						c.modifikoKartat1(1);

					} else {
						c.modifikoKartat2(1);
					}

				}
			} catch (RemoteException re) {
			}
		} else if (ae.getSource() == imazhi[2]) {
			try {
				if ((jTextUserName.getText().equals(lojtaret.get(0)) && c.ktheradhen() == 1)
						|| (jTextUserName.getText().equals(lojtaret.get(1)) && c.ktheradhen() == 0)) {
					c.rritRadhen();
					c.shto_kontainer(lojtari[2]);
					imazhi[2].setVisible(false);
					if (jTextUserName.getText().equals(lojtaret.get(0))) {
						c.modifikoKartat1(2);

					} else {
						c.modifikoKartat2(2);
					}
				}
			} catch (RemoteException re) {
			}
		} else if (ae.getSource() == imazhi[3]) {
			try {
				if ((jTextUserName.getText().equals(lojtaret.get(0)) && c.ktheradhen() == 1)
						|| (jTextUserName.getText().equals(lojtaret.get(1)) && c.ktheradhen() == 0)) {
					c.rritRadhen();
					c.shto_kontainer(lojtari[3]);
					imazhi[3].setVisible(false);
					if (jTextUserName.getText().equals(lojtaret.get(0))) {
						c.modifikoKartat1(3);

					} else {
						c.modifikoKartat2(3);
					}
				}
			} catch (RemoteException re) {
			}
		} else if (ae.getSource() == jButtonPlay) {
			try {
				c.sendPublicMessage(PUBLICMESSAGE, jTextUserName.getText(), " clicked play");
				jTextSendMessage.setText("");
				jButtonPlay.setVisible(false);
				c.regjistroLojtarin(jTextUserName.getText());
				jLabelPritni.setVisible(true);
			} catch (RemoteException re) {
			}
		}
	}

}
