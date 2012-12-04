package compute;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface BingoClient extends Remote {
    void recebeBola(int bola) throws RemoteException;
}
