package mp9.uf3.udp.unicast.joc;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;

/**
 * Created by jordi.
 * Exemple Servidor UDP extret dels apunts IOC i ampliat
 * El seu CLient és DatagramSocketClient
 */
public class DatagramSocketServerJoc {
    DatagramSocket socket;
    InetAddress clientIP;

    SecretNum secretNum;

    //Instàciar el socket
    public void init(int port) throws SocketException {
        socket = new DatagramSocket(port);
        secretNum = new SecretNum();
    }

    public void runServer() throws IOException {
        byte [] receivingData = new byte[1024];
        byte [] sendingData;

        int clientPort;

        secretNum.pensa(10);

        while(true) {
            DatagramPacket packet = new DatagramPacket(receivingData,1024);
            socket.receive(packet);
            clientIP = packet.getAddress();
            sendingData = processData(packet.getData());
            //Llegim el port i l'adreça del client per on se li ha d'enviar la resposta
            clientPort = packet.getPort();
            packet = new DatagramPacket(sendingData,sendingData.length,clientIP,clientPort);
            socket.send(packet);
        }
    }

    //El server retorna al client el mateix missatge que li arriba però en majúscules
    private byte[] processData(byte[] data) {
        // int -> byte[]    n -> missatge
        int n = ByteBuffer.wrap(data).getInt();

        byte[] missatge = ByteBuffer.allocate(4).putInt(n).array();
        // byte[] -> int     data -> n
        int msg = secretNum.comprova(n);

        //Imprimir el missatge rebut i retornar-lo
        System.out.println("El Client amb la IP num "+ clientIP+ " ha enviat aquest numero "+ n );
        return ByteBuffer.allocate(4).putInt(msg).array();
    }

    public static void main(String[] args) {
        DatagramSocketServerJoc server = new DatagramSocketServerJoc();
        try {
            server.init(5555);
            server.runServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
