package mp9.uf3.tcp.apunts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TcpSocketServer {
    private Scanner sc = new Scanner(System.in);
    static final int PORT = 9090;
    private boolean end = false;

    public void listen(){
        ServerSocket serverSocket=null;
        Socket clientSocket = null;
        try {
            serverSocket = new ServerSocket(PORT);
            while(!end){
                clientSocket = serverSocket.accept();
                System.out.println("Connexió amb: " + clientSocket.getInetAddress());
                //processem la petició del client
                proccesClientRequest(clientSocket);
                //tanquem el sòcol temporal per atendre el client
                closeClient(clientSocket);
            }
            //tanquem el sòcol principal
            if(serverSocket!=null && !serverSocket.isClosed()){
                serverSocket.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(TcpSocketServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void proccesClientRequest(Socket clientSocket){
        boolean farewellMessage=false;
        String clientMessage="";
        BufferedReader in=null;
        PrintStream out=null;
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out= new PrintStream(clientSocket.getOutputStream());
            System.out.println("Canals d'e/s establerts...");
            do{
                //processem el missatge del client i generem la resposta. Si
                //clientMessage és buida generarem el missatge de benvinguda
                String dataToSend = processData(clientMessage);
                out.println(dataToSend);
                out.flush();
                clientMessage=in.readLine();
                farewellMessage = isFarewellMessage(clientMessage);
            }while((clientMessage)!=null && !farewellMessage);
        } catch (IOException ex) {
            Logger.getLogger(TcpSocketServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String processData(String data) {
        System.out.println("Clientt$ " + data);
        System.out.print("$ ");
        return sc.nextLine();
    }

    private boolean isFarewellMessage(String msg) {
        if(msg.equals("bye")) return true;
        else return false;
    }

    private void closeClient(Socket clientSocket){
    //si falla el tancament no podem fer gaire cosa, només enregistrar
    //el problema
        try {
            //tancament de tots els recursos
            if(clientSocket!=null && !clientSocket.isClosed()){
                if(!clientSocket.isInputShutdown()){
                    clientSocket.shutdownInput();
                }
                if(!clientSocket.isOutputShutdown()){
                    clientSocket.shutdownOutput();
                }
                clientSocket.close();
            }
        } catch (IOException ex) {
            //enregistrem l'error amb un objecte Logger
            Logger.getLogger(TcpSocketServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        TcpSocketServer tcpSocketServer = new TcpSocketServer();
        tcpSocketServer.listen();
    }
}
