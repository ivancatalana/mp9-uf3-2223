package mp9.uf3.udp;

import java.io.IOException;
import java.net.*;

public class client {
        InetAddress serverIP;
        int serverPort;
        DatagramSocket socket;

        public void init(String host, int port) throws SocketException,
                UnknownHostException {
            serverIP = InetAddress.getByName(host);
            serverPort = port;
            socket = new DatagramSocket();
        }

        public void runClient() throws IOException {
            byte [] receivedData = new byte[1024];
            byte [] sendingData;

//a l'inici
            sendingData = getFirstRequest();
//el servidor atén el port indefinidament
            while(mustContinue(sendingData)) {
                DatagramPacket packet = new DatagramPacket(sendingData,
                        sendingData.length,
                        serverIP,
                        serverPort);
//enviament de la resposta
                    socket.send(packet);

//creació del paquet per rebre les dades
                packet = new DatagramPacket(receivedData, 1024);

//espera de les dades
                    socket.receive(packet);
//processament de les dades rebudes i obtenció de la resposta
                    sendingData = getDataToRequest(packet.getData(), packet.getLength());

            }
        }

        private byte[] getDataToRequest(byte[] data, int length) {
            // Aquí debemos procesar la respuesta del servidor y generar
            // el siguiente mensaje a enviar. Por ejemplo, si el servidor
            // nos devuelve un número, podríamos sumar 1 y enviar el resultado.
            // En este caso, como estamos haciendo una conversación sencilla, solo
            // devolvemos el mismo mensaje que recibimos.

            //return data;
            String rebut = new String(data,0, length);
            //Imprimeix el nom del client + el que es rep del server i demana més dades
            System.out.print("("+rebut+")"+"> ");
            String msg = "Conexio OK";
            return msg.getBytes();
        }

        private byte[] getFirstRequest() {
//procés diferent per cada aplicació
//bytes del missatge a enviar
            byte[] missatge = "Salutacions".getBytes();
           return missatge;
        }

        private boolean mustContinue(byte[] sendingData) {
            // Aquí debemos decidir si seguimos intercambiando mensajes con el servidor.
            // Por ejemplo, podríamos tener una condición que verifique que el mensaje enviado
            // no sea vacío o que el servidor no haya cerrado la conexión.
            // En este caso, vamos a seguir intercambiando mensajes mientras el mensaje enviado
            // no sea "Adiós".
            String message = new String(sendingData);
            return !message.equalsIgnoreCase("Adiós");
        }
   /*
    private void processData(byte[] data, int length) {
        // Aquí procesamos la respuesta del servidor. En este ejemplo,
        // simplemente imprimimos el mensaje recibido en la consola.
        String message = new String(data, 0, length);
        System.out.println("Servidor dice: " + message);
    }

    */
    public void close(){
        if(socket!=null && !socket.isClosed()){
            socket.close();
        }
    }

    public static void main(String[] args) {
        try {
            client c = new client();
            c.init("localhost", 5555);
            c.runClient();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
