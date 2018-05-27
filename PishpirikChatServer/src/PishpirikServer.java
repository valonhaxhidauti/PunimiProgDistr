
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;
import java.net.*;
import java.io.*;
import java.util.*;
import javax.swing.JOptionPane;

public class PishpirikServer {
	private static HashMap<String, Socket> connectedUser = new HashMap<String, Socket>();
	private static Socket ClientSocket = null;
	private static ServerSocket serverSocket;
	private static String username = null;
	private static PrintWriter output;
	private int numrilojtareve = 0;
	private static int radha = 1;
	private static String emri = null;
	private ArrayList<String> lojtari = new ArrayList<>();
	private static ArrayList<String> Kartat = new ArrayList<>();
	private static ArrayList<String> container = new ArrayList<>();
	private static int[] piketLojtareve = { 0, 0 };

	private static ArrayList<String> KartatNeTavoline = new ArrayList<>();
	public static String[] k_lojtari1 = new String[4];
	public static String[] k_lojtari2 = new String[4];

	private static final String PUBLICMESSAGE = "PUBLICMESSAGE";
	private static final String ONLINE = "ONLINE";
	private static final String OFFLINE = "OFFLINE";
	

	public PishpirikServer() {
		
		try {
			// lidhja me server
			PishpirikChatImplementation csi = new PishpirikChatImplementation(this);
			
            Naming.rebind("rmi://192.168.0.103/ChatService", csi);

		} catch (java.rmi.ConnectException ce) {
			JOptionPane.showMessageDialog(null, "Trouble : Please run rmiregistry.", "Connect Exception",
					JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(null, "Trouble : Please run rmiregistry.", "Exception",
					JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Trouble : Please run rmiregistry.", "Exception",
					JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		}
	}

	public static void main(String[] args) {
		
		for (int i = 1; i <= 13; i++)
			for (int j = 1; j <= 4; j++)
				Kartat.add(i + "_" + j + ".jpg");

		Collections.shuffle(Kartat);
		shperndajLetrat();
		
		try
        {
            int port = Integer.parseInt(args[0]);
            PishpirikServer cs = new PishpirikServer();
            cs.processConnection(port);
        }
        catch (ArrayIndexOutOfBoundsException ae)
        {
            JOptionPane.showMessageDialog(null, "Please insert the port", "ATTENTION", JOptionPane.INFORMATION_MESSAGE);
            System.exit(-1);
        }
	}

	public void rifillo() {
		for (int i = 1; i <= 13; i++) {
			for (int j = 1; j <= 4; j++) {
				Kartat.add(i + "_" + j + ".jpg");
				k_lojtari1[j - 1] = "";
				k_lojtari2[j - 1] = "";
			}
		}

		Collections.shuffle(Kartat);
		shperndajLetrat();
		numrilojtareve = 0;
		radha = 1;
		emri = null;
		lojtari.clear();
		piketLojtareve[0] = 0;
		piketLojtareve[1] = 0;
		KartatNeTavoline.clear();
		container.clear();

	}

	private void processConnection(int port) {
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("Server is running on port " + port + " .... ");
		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(null, "Nuk mund te lexohet porti " + port, "GABIM",
					JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		}

		try {
			while (true) {
				addClient(serverSocket.accept());
				String username = getUsername();
				sendPublicMessage(PUBLICMESSAGE, "SERVERI", "[" + username + "] eshte tani ne linje");
			}
		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(null, "Nuk mund te lidhet", "GABIM", JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		}
	}

	public static void shperndajLetrat() {
		if (Kartat.size() >= 8)
			for (int i = 0; i < 8; i++) {
				if (i < 4) {
					k_lojtari1[i] = Kartat.get(0);
					Kartat.remove(0);
				} else {
					k_lojtari2[i % 4] = Kartat.get(0);
					Kartat.remove(0);
				}
			}
		else if (Kartat.size() == 4) {
			for (int i = 0; i < 4; i++) {
				if (i < 2) {
					k_lojtari1[i] = Kartat.get(0);
					Kartat.remove(0);
				} else {
					k_lojtari2[i] = Kartat.get(0);
					Kartat.remove(0);
				}
			}

		}

	}

	public static int ktheNrKartave() {
		return Kartat.size();
	}

	public String[] letrat_player1() {
		return k_lojtari1;

	}

	public void rritPiket(int lojtari, int piket) {
		piketLojtareve[lojtari] += piket;
	}

	public int[] kthePiket() {
		return piketLojtareve;
	}

	public String[] letrat_player2() {
		return k_lojtari2;

	}

	public void modifikoKartat1(int i) {
		k_lojtari1[i] = "a";

	}

	public void modifikoKartat2(int i) {
		k_lojtari2[i] = "a";

	}

	public void shto_kontainer(String a) {
		container.add(a);
	}

	public void fshij_kontainer() {
		container.clear();

	}

	public ArrayList kthe_kontainer() {
		return container;
	}

	public int ktheradhen() {
		return radha;
	}

	public void rritRadhen() {
		radha++;
		radha = radha % 2;
	}

	public void connect(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void regjistroLojtarin(String emri) {
		this.emri = emri;
		regjistroLojtarinn();

	}

	public void regjistroLojtarinn() {
		lojtari.add(emri);
		numrilojtareve++;

	}

	public ArrayList kthe_Lojtaret() {
		return lojtari;
	}

	public String ktheMesazhinLojes() {
		if (lojtari.size() % 2 == 1) {
			return "Ne pritje te kundershtarit...";
		} else {
			return "Tani loja fillon !";
		}
	}

	public int nrlojtareve() {
		return lojtari.size();

	}

	public ArrayList getClientList() {
		ArrayList myUser = new ArrayList();

		Iterator i = connectedUser.keySet().iterator();
		String user = null;

		while (i.hasNext()) {
			user = i.next().toString();
			myUser.add(user);
		}

		return myUser;
	}

	public void addClient(Socket clientSocket) throws RemoteException {
		connectedUser.put(getUsername(), clientSocket);
		sendPublicMessage(ONLINE, getUsername(), "CLIENT");
	}

	public void sendPublicMessage(String keyword, String username, String message) throws RemoteException {
		Iterator i = connectedUser.keySet().iterator();
		String user = null;
		while (i.hasNext()) {
			try {
				user = i.next().toString();
				ClientSocket = connectedUser.get(user);
				output = new PrintWriter(ClientSocket.getOutputStream(), true);
				output.println(keyword + "***" + username + "***" + message);
				output.flush();
			} catch (IOException ioe) {
				connectedUser.remove(user);
				sendPublicMessage(OFFLINE, user, user + " has been left the conversation");
			}
		}
	}

	public void disconnect(String username) throws RemoteException {
		connectedUser.remove(username);
		sendPublicMessage(OFFLINE, username, username + " has been left the conversation");
		sendPublicMessage(PUBLICMESSAGE, "SERVER", username + " has been left the conversation");
	}
}