package compute;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface BingoServer extends Remote {
    void conecta(BingoClient bc) throws RemoteException;
}
