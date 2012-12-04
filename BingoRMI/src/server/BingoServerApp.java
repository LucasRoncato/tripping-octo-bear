package server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import compute.BingoClient;
import compute.BingoServer;

public class BingoServerApp implements BingoServer, Runnable {
	List<BingoClient> clients = new ArrayList<BingoClient>();

	public BingoServerApp() {
		super();
	}

	@Override
	public void conecta(BingoClient bc) throws RemoteException {
		clients.add(bc);
		System.out.println(bc);

		if (clients.size() == 3) {
			Thread t = new Thread(this);
			t.start();
		}

	}

	public void run() {
		
		while (!Thread.interrupted()) {
			for (BingoClient c : clients) {
				try {
					c.recebeBola(28);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		try {
			java.rmi.registry.LocateRegistry.createRegistry(1099);
			System.out.println("RMI registry ready.");
		} catch (Exception e) {
			System.out.println("Exception starting RMI registry:");
			e.printStackTrace();
		}
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		try {
			String name = "BingoServer";
			BingoServer engine = new BingoServerApp();
			BingoServer stub = (BingoServer) UnicastRemoteObject.exportObject(
					engine, 0);
			Registry registry = LocateRegistry.getRegistry();
			registry.rebind(name, stub);
			System.out.println("BingoServer bound");
		} catch (Exception e) {
			System.err.println("BingoServer exception:");
			e.printStackTrace();
		}
	}

}
