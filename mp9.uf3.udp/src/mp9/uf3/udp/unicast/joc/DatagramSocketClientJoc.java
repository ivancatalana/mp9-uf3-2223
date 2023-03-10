package mp9.uf3.udp.unicast.joc;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Scanner;

/**
 * Created by jordi
 * Exemple Client UDP extret dels apunts del IOC i ampliat
 * El server és DatagramSocketServer
 *
 * Aquest client reb del server el mateix que se li envia
 * Si s'envia adeu s'acaba la connexió
 */

public class DatagramSocketClientJoc {
    InetAddress serverIP;
    int serverPort;
    DatagramSocket socket;
    Scanner sc;
    String nom;

    public DatagramSocketClientJoc() {
        sc = new Scanner(System.in);
    }

    public void init(String host, int port) throws SocketException, UnknownHostException {
        serverIP = InetAddress.getByName(host);
        serverPort = port;
        socket = new DatagramSocket();
    }

    public void runClient() throws IOException {
        byte [] receivedData = new byte[1024];
        byte [] sendingData;
        getFirstRequest();

        sendingData =   getFirstRequest();

        while (mustContinue(sendingData)) {
            DatagramPacket packet = new DatagramPacket(sendingData,sendingData.length,serverIP,serverPort);
            socket.send(packet);
            packet = new DatagramPacket(receivedData,1024);
            socket.receive(packet);
            sendingData = getDataToRequest(packet.getData());
        }

    }

    //Resta de conversa que se li envia al server
    private byte[] getDataToRequest(byte[] data) {
        int n = ByteBuffer.wrap(data).getInt();
        byte[] missatge = ByteBuffer.allocate(4).putInt(n).array();

        //Imprimeix el nom del client + el que es rep del server i demana més dades
        System.out.print(nom+"("+n+")"+"> ");
        int msg = sc.nextInt();
        return ByteBuffer.allocate(4).putInt(msg).array();
    }

    //primer missatge que se li envia al server
    private byte[] getFirstRequest() {
        System.out.println("Entra el teu nom: ");
        nom = sc.nextLine();
        return nom.getBytes();
    }

    //Si se li diu adeu al server, el client es desconnecta
    private boolean mustContinue(byte [] data) {
        String msg = new String(data).toLowerCase();
        return !msg.equals("adeu");
    }

    public static void main(String[] args) {
        DatagramSocketClientJoc client = new DatagramSocketClientJoc();
        try {
            client.init("localhost",5555);
            client.runClient();
        } catch (IOException e) {
            e.getStackTrace();
        }

    }

}
