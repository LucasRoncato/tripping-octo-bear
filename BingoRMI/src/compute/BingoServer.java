package compute;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;


public interface BingoServer extends Remote {
    ArrayList<Integer> conecta(BingoClient bc) throws RemoteException;
    void verificaBingo(BingoClient bc) throws RemoteException;
    void verificaColuna(BingoClient bc, String coluna) throws RemoteException;
}
