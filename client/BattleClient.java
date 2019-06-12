package client;

import common.ConnectionAgent;
import common.MessageListener;
import common.MessageSource;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class BattleClient extends MessageSource implements MessageListener{
    /*connection agent*/
    ConnectionAgent agent;
    /* InetAddress*/
    InetAddress host;
    /* port*/
    int port;
    /* username*/
    String username;
    /*print stream*/
    PrintStreamMessageListener out;

    /**
     * default constructor
     * @param hostname name of the host
     * @param port port
     * @param username player name
     * @throws IOException sockets throw it
     */
    public BattleClient(String hostname, int port, String username) throws IOException{
        this.host = InetAddress.getByName(hostname);
        this.port = port;
        this.username = username;
        this.connect();
        this.out = new PrintStreamMessageListener(System.out);
        addMessageListener(out);


    }

    /**
     * connects the socket and the agent and sets the client as a listener to the agent
     * @throws IOException in case of socket
     */
    public void connect() throws IOException {
        Socket socket = new Socket(host, port);
        this.agent = new ConnectionAgent(socket);
        agent.addMessageListener(this);
        agent.sendMessage("");
    }

    /**
     * send the message to the connection agent
     * @param message message to be sent
     */
    public void send(String message){
        agent.sendMessage(message);
    }


    @Override
    /**
     * closes the source
     */
    public void sourceClosed(MessageSource source) {
        try {
            agent.close();
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        source.removeMessageListener(this);

    }

    @Override
    /**
     * sends a message to the listener to print
     */
    public void messageReceived(String message, MessageSource source) {
        notifyReceipt(message);

    }

    /**
     * getter to the connection agent
     * @return the connection agent
     */
    public ConnectionAgent getConnectionAgent() {
        return agent;
    }
}
