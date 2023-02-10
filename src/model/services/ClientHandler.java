package model.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler extends Thread{

    private ArrayList<ClientHandler> clients;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    /*
    This code defines a ClientHandler class which extends the Thread class.
    The ClientHandler class has a constructor that takes two arguments, a Socket
    object and an ArrayList of ClientHandler objects.
    In the constructor, the input and output streams of the socket are obtained and
    stored as a BufferedReader and a PrintWriter object respectively.
     */


    public ClientHandler(Socket socket, ArrayList<ClientHandler> clients) {
        try {
            this.socket= socket;
            this.clients = clients;
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new PrintWriter(socket.getOutputStream(),true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    The run method of the ClientHandler class is where the communication between the client
    and the server takes place. The run method continuously reads incoming messages from the
    client using the readLine method of the BufferedReader object until it receives the message
    "exit". For each incoming message, the message is broadcasted to all clients by writing the
    message to the output stream of each ClientHandler object in the clients list.
    If an IOException occurs during the communication process, it is caught and logged in the
    catch block. In the finally block, the BufferedReader, PrintWriter and Socket objects are
    closed to free up resources.
     */

    @Override
    public void run() {
        try {
            String msg;
            while ((msg = reader.readLine()) != null) {
                if (msg.equalsIgnoreCase("exit")) {
                    break;
                }
                for (ClientHandler cl : clients){
                    cl.writer.println(msg);
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try{
                reader.close();
                writer.close();
                socket.close();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
