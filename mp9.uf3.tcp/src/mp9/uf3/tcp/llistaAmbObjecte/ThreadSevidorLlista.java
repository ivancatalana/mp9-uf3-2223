package mp9.uf3.tcp.llistaAmbObjecte;

import jocTCP.SecretNum;
import mp9.uf3.tcp.llistaAmbObjecte.Llista;

import java.io.*;
import java.net.Socket;

public class ThreadSevidorLlista implements Runnable {
	/* Thread que gestiona la comunicació de SrvTcPAdivina_Obj.java i un cllient ClientTcpAdivina_Obj.java */

	private Socket clientSocket = null;
	private InputStream in = null;
	private OutputStream out = null;
	Llista llista=null;
	private boolean acabat;

	public ThreadSevidorLlista(Socket clientSocket) throws IOException {
		this.clientSocket = clientSocket;
		//Al inici de la comunicació el resultat ha de ser diferent de 0(encertat)
		acabat = false;
		//Enllacem els canals de comunicació
		in = clientSocket.getInputStream();
		out = clientSocket.getOutputStream();
		System.out.println("canals i/o creats amb un nou jugador");
	}

	@Override
	public void run() {
		try {
			while(!acabat) {


				//Enviem tauler al jugador
				ObjectOutputStream oos = new ObjectOutputStream(out);

				oos.writeObject(llista);
				oos.flush();

				//Llegim la llista
				ObjectInputStream ois = new ObjectInputStream(in);

				llista = (Llista) ois.readObject();
				System.out.println("Llista rebuda: " + llista.getNom()+ " Numeros:  "+ llista.getNumberList());

			}} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

}