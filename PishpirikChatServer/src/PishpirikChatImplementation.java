
import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
import java.net.*;
 
public class PishpirikChatImplementation extends UnicastRemoteObject implements PishpirikChat
{
    private PishpirikServer cs;
 
    public PishpirikChatImplementation(PishpirikServer cs) throws RemoteException
    {
        super();
        this.cs = cs;
    }
 
    public void sendPublicMessage(String keyword, String username, String message) throws RemoteException
    {
        cs.sendPublicMessage(keyword, username, message);
    }
 
    public ArrayList getClientList() throws RemoteException
    {
        return cs.getClientList();
    }
 
    public void connect(String username) throws RemoteException
    {
        cs.connect(username);
    }
 
    public void disconnect(String username) throws RemoteException
    {
        cs.disconnect(username);
    }

	public void regjistroLojtarin(String emri) throws RemoteException {
		cs.regjistroLojtarin(emri);
	}

	public String ktheMesazhinLojes() throws RemoteException {
	
		return cs.ktheMesazhinLojes();
	}

	
	public int nrlojtareve() throws RemoteException {
		
		return cs.nrlojtareve();
	}

	public ArrayList kthe_Lojtaret() throws RemoteException {
		return cs.kthe_Lojtaret();
	}

	
	public String[] letrat_player1() throws RemoteException {
		
		return cs.letrat_player1();
	}

	
	public String[] letrat_player2() throws RemoteException {
		
		return cs.letrat_player2();
	}
	public void shto_kontainer(String a)
	 {
		cs.shto_kontainer(a) ;
		 
		 
	 }
	
	public ArrayList kthe_kontainer()
	 {
		return cs.kthe_kontainer();
		 
	 }
	 public void modifikoKartat1(int i)
	 {
		 cs.modifikoKartat1(i);
	 }
	 public void modifikoKartat2(int i)
	 {
		 cs.modifikoKartat2(i);
	 }


	public void rritRadhen() throws RemoteException {
		cs.rritRadhen();
		
	}

	public int ktheradhen() throws RemoteException {
		return cs.ktheradhen();
		
	}

	public void shperndajLetrat() throws RemoteException {
		cs.shperndajLetrat();
		
	}


	public void fshij_kontainer() throws RemoteException {
		cs.fshij_kontainer();
		
	}

	public void rritPiket(int lojtari, int piket) throws RemoteException {
		cs.rritPiket(lojtari,piket);
	}


	public int[] kthePiket() throws RemoteException {
		return cs.kthePiket();
	}

	public int ktheNrKartave() throws RemoteException {
		
		return cs.ktheNrKartave();
	}
        public void rifillo(){
        cs.rifillo();
        }
}