import java.rmi.*;
import java.util.*;
 
public interface PishpirikChat extends Remote
{
    public void sendPublicMessage(String keyword, String username, String message) throws RemoteException;
    public ArrayList getClientList() throws RemoteException;
    public void connect(String username) throws RemoteException;
    public void disconnect(String username) throws RemoteException;
    public void regjistroLojtarin(String emri) throws RemoteException;
    public String ktheMesazhinLojes() throws RemoteException;
	public int nrlojtareve() throws RemoteException;
	 public ArrayList kthe_Lojtaret() throws RemoteException;
	 public String[] letrat_player1() throws RemoteException;
	 public String[] letrat_player2() throws RemoteException;
		public ArrayList kthe_kontainer() throws RemoteException;
		public void shto_kontainer(String a) throws RemoteException;
		 public void modifikoKartat1(int i) throws RemoteException;
		 public void modifikoKartat2(int i) throws RemoteException;
		 public void rritRadhen() throws RemoteException;
		 public int ktheradhen() throws RemoteException;
		 public void shperndajLetrat() throws RemoteException;
		 public void fshij_kontainer() throws RemoteException;
		 public void rritPiket(int lojtari,int piket) throws RemoteException;
		 public int[] kthePiket() throws RemoteException;
		 public int ktheNrKartave() throws RemoteException;
		 public void rifillo() throws RemoteException;
}
