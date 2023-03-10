package jocTCP;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TCPServerJoc {
    SecretNum secretNum = new SecretNum();

    boolean farewellMessage;

    Scanner sc = new Scanner(System.in);

    static final int PORT=5555;
    private boolean end=false;

    public void listen(){
        farewellMessage=false;
        ServerSocket serverSocket=null;
        Socket clientSocket = null;
        secretNum.pensa(10);
        try {
            serverSocket = new ServerSocket(PORT);

            while(!end){
                clientSocket = serverSocket.accept();
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
            Logger.getLogger(apunts.TcpSocketServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    public void proccesClientRequest(Socket clientSocket){
        String clientMessage="";
        BufferedReader in=null;
        PrintStream out=null;
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out= new PrintStream(clientSocket.getOutputStream());
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
        }
    }
    public String processData(String client){
        if(client.equals("")){
            return  "Benvingut al joc";
        }
        System.out.println("$ "+client);
        String resposta = secretNum.comprova(client);
        System.out.println(resposta);
        if (resposta.equals("Correcte")){
            farewellMessage=true;
        }
        return resposta;
    }
    public boolean isFarewellMessage ( String clientmsg){
        if (clientmsg.equals("adeu")) return true;
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
            Logger.getLogger(apunts.TcpSocketServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void main(String[] args) {
        TCPServerJoc server = new TCPServerJoc();
        server.listen();

    }
}