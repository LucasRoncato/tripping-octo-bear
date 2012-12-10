package server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import bingo.Sorteio;
import bingo.Ticket;
import bingo.Verifica;

import compute.BingoClient;
import compute.BingoServer;

public class BingoServerApp implements BingoServer, Runnable {
	Map<BingoClient, Ticket> clients = new HashMap<BingoClient, Ticket>();
	Sorteio sorteio = new Sorteio();
	final static String boasVindas = "Welcome to BingoLaw.\n "
			+ "The match will start as soon as the third player connects.\n "
			+ "Notice that the drawn numbers will be shown in the central position below N.";

	public BingoServerApp() {
		super();
	}

	@Override
	public ArrayList<Integer> conecta(BingoClient bc) throws RemoteException {
		Ticket ticket = new Ticket();
		clients.put(bc, ticket);
		System.out.println(bc);
		System.out.println(clients.size());
		bc.recebeMensagem(boasVindas);
		if (clients.size() == 3) {
			Thread t = new Thread(this);
			t.start();
		}
		return (ArrayList<Integer>) ticket.getList();
	}
	
	@Override
	public void verificaBingo(BingoClient bc) throws RemoteException {
		if(Verifica.verificaBingo(sorteio, clients.get(bc))){
			for (Map.Entry<BingoClient, Ticket> entry : clients.entrySet()) {
				try {
					if(entry.getKey() != bc){
						entry.getKey().recebeMensagem(bc + " won!");
					}else{
						entry.getKey().recebeMensagem("Congratulations, you won!");
					}
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			Thread.currentThread().interrupt();
			//TODO testar se isso ^ funciona.
		}
	}

	@Override
	public void verificaColuna(BingoClient bc, String coluna) throws RemoteException {
		//TODO Verificar se a coluna ja foi completada, talvez?
		if(Verifica.verificaColuna(coluna, sorteio, clients.get(bc))){
			for (Map.Entry<BingoClient, Ticket> entry : clients.entrySet()) {
				try {
					if(entry.getKey() != bc){
						entry.getKey().recebeMensagem(bc + " completed a column!");
					}else{
						entry.getKey().recebeMensagem("Congratulations, completed a column!");
					}
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	
	

	public void run() {

		while (!Thread.interrupted()) {
			int sorteado;
			if ((sorteado = sorteio.sorteia()) != -1) {
				// for (BingoClient c : clients) {
				// try {
				// c.recebeBola(sorteado);
				// } catch (RemoteException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				// }
				for (Map.Entry<BingoClient, Ticket> entry : clients.entrySet()) {
					try {
						entry.getKey().recebeBola(sorteado);
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
			} else {
				for (Map.Entry<BingoClient, Ticket> entry : clients.entrySet()) {
					try {
						entry.getKey().recebeMensagem("All numbers have been drawn.");
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				Thread.currentThread().interrupt();
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
