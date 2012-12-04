package client;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import compute.BingoClient;
import compute.BingoServer;

public class BingoClientApp implements BingoClient {
	
	public BingoClientApp() {
		super();
	}
	
	@Override
	public void recebeBola(int bola) throws RemoteException {
		System.out.println(bola);
		
	}
	

	public static void main(String[] args) {
		BingoClient stub = null;
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		try {
			String name = "BingoClient";
			BingoClient engine = new BingoClientApp();
			stub = (BingoClient) UnicastRemoteObject
					.exportObject(engine, 0);
			Registry registry = LocateRegistry.getRegistry();
			registry.rebind(name, stub);
			System.out.println("BingoClient bound");
		} catch (Exception e) {
			System.err.println("BingoClient exception:");
			e.printStackTrace();
		}
		
		try {
            String name = "BingoServer";
            Registry registry = LocateRegistry.getRegistry();
            BingoServer comp = (BingoServer) registry.lookup(name);
            comp.conecta(stub);
        } catch (Exception e) {
            System.err.println("ComputePi exception:");
            e.printStackTrace();
        }
	}

	

	

}
