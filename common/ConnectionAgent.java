package common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * This is the class responsible as a median for sending and receiving messages
 * @author Austin Powers, Caleb Dineheart
 * @version 12-4-2018
 */
public class ConnectionAgent extends MessageSource implements Runnable {
    private Socket socket;
    private Scanner in;
    private PrintStream out;
    private Thread thread;
    private MessageSource source;

    /**
     * default constructor
     * @param socket socket that we are connecting to
     */
    public ConnectionAgent(Socket socket){

        this.socket = socket;
        try {
            in = new Scanner(socket.getInputStream());
            //System.out.println("I got here");
            out = new PrintStream(socket.getOutputStream());
            //System.out.println("I got here too");
        }catch(IOException e){

        }
        this.thread = new Thread(this);
        this.thread.start();
    }

    /**
     * sending message to receiver
     * @param message message to send
     */
    public void sendMessage(String message){
        out.println(message);
    }

    /**
     * to run the connection agent logic
     */
    public void run(){
        while(socket.isConnected() && !thread.isInterrupted()) {
            if (in.hasNextLine()) {
                String temp = in.nextLine();
                notifyReceipt(temp);
            }
        }
    }

    /**
     * returns if the socket is connected
     * @return if the socket is connected
     */
    public boolean isConnected(){
        return socket.isConnected();
    }

    /**
     * closes the socket and the input and output for the socket as well as the connection agent
     * @throws IOException socket throws it
     */
    public void close() throws IOException{
        thread.interrupt();
        socket.close();
        out.close();
        in.close();

        //closeMessageSource();
    }

}
