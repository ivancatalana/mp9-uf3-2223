package mp9.uf3.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Server {
        DatagramSocket socket;

        public void init(int port) throws SocketException {
            socket = new DatagramSocket(port);
        }

        public void runServer() throws IOException {
            byte [] receivingData = new byte[1024];
            byte [] sendingData;
            InetAddress clientIP;
            int clientPort;

//el servidor atén el port indefinidament
            while(true){
//creació del paquet per rebre les dades
                DatagramPacket packet = new DatagramPacket(receivingData, 1024);
//espera de les dades
                socket.receive(packet);
//processament de les dades rebudes i obtenció de la resposta
                sendingData = processData(packet.getData(), packet.getLength());
//obtenció de l'adreça del client
                clientIP = packet.getAddress();
//obtenció del port del client
                clientPort = packet.getPort();
//creació del paquet per enviar la resposta
                packet = new DatagramPacket(sendingData, sendingData.length,
                        clientIP, clientPort);
//enviament de la resposta
                socket.send(packet);
            }
        }

    private byte[] processData(byte[] data, int length) {
        //imprimir el mensaje recibido por consola
        String message = new String(data, 0, length);
        System.out.println("Mensaje recibido: " + message);

        //enviar un mensaje de confirmación al cliente
        String confirmation = "Mensaje recibido server";
        return confirmation.getBytes();
    }
    public void close(){
        if(socket!=null && !socket.isClosed()){
            socket.close();
        }
    }
    public static void main(String[] args) {
        try {
            Server server = new Server();
            server.init(5555); // Se puede especificar cualquier número de puerto deseado.
            server.runServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
