package client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import compute.BingoClient;
import compute.BingoServer;

public class BingoClientApp extends JPanel implements BingoClient {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7146855525182131689L;
	static JLabel label = new JLabel("Sorteio");
	static JTextArea textArea = new JTextArea(6,100);
	static List<Integer> number;
	private ArrayList<JButton> botoes;
	private static BingoServer comp;
	private static BingoClient stub = null;

//	public BingoClientApp() {
//		super();
//	}
	
	@Override
	public void recebeBola(int bola) throws RemoteException {
		label.setText(String.valueOf(bola));
	}
	
	@Override
	public void recebeMensagem(String mensagem) throws RemoteException {
		textArea.append(mensagem);
	}
	
	public void bingoClientGui() {
		JFrame f = new JFrame("Bingo Law");
		f.getContentPane().add(this);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.pack();
		f.setVisible(true);
	    Dimension tela = Toolkit.getDefaultToolkit().getScreenSize();  
	    setSize(tela.width, tela.height);
		
		botoes = new ArrayList<JButton>(30);
		setLayout(new GridLayout(7, 5));
		String bingo = "BINGO";
		for (int i = 0; i < 5; i++) {
			final JButton buttoncol = new JButton(String.valueOf(bingo
					.charAt(i)));
			buttoncol.setBackground(Color.white);
			add(buttoncol);
			botoes.add(buttoncol);
			buttoncol.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					JButton b = (JButton) e.getSource();
					try {
						comp.verificaColuna(stub, b.getText());
					} catch (RemoteException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});
		}

		for (int i = 5; i < 29; i++) {

			if (i == 17) {
				 add(label);
			}

			JButton button = new JButton("" + number.get(i - 5));
			button.setBackground(Color.white);
			add(button);
			botoes.add(button);
			button.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					JButton b = (JButton) e.getSource();
					if (b.getBackground().equals(Color.green)) {
						b.setBackground(Color.white);
					} else {
						b.setBackground(Color.green);
					}
					System.out.println();
				}
			});
		}
		JButton buttonBingo = new JButton("BINGO!");
		add(buttonBingo);
		botoes.add(buttonBingo);
		buttonBingo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					comp.verificaBingo(stub);
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane( textArea );
		add(scrollPane);
	}

	public static void main(String[] args) {
		
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
            comp = (BingoServer) registry.lookup(name);
            number = comp.conecta(stub);
            BingoClientApp inicia = new BingoClientApp();
            inicia.bingoClientGui();
        } catch (Exception e) {
            System.err.println("ComputePi exception:");
            e.printStackTrace();
        }
	}

	


	

	

}
