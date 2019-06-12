package server;

import common.ConnectionAgent;
import common.MessageListener;
import common.MessageSource;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * server side logic contains and instance of the battleship game when it receives a message from a connectionAgent
 * it will parse it and return information reguarding to game state and other information depending on message
 * @author Austin Powers, Caleb Dineheart
 * @version 12/4/2018
 */
public class BattleServer extends MessageSource implements MessageListener {
    /* list of players connection agents*/
    protected ArrayList<ConnectionAgent> connectedPlayers;
    private ArrayList<String> pregamePlayers;
    /* port connected to*/
    private int port;
    /* server socket*/
    private ServerSocket serverSocket;
    /*current player*/
    private int current;
    /*Current game*/
    private Game game;
    /*size of board*/
    private int size;

    /**
     * default constructor
     * @param port port to connect to
     */
    public BattleServer(int port , int size) throws IOException {
        this.game = new Game(size);
        this.port = port;
        this.size = size;
        this.serverSocket = new ServerSocket(port);
        this.connectedPlayers = new ArrayList<>();
        this.pregamePlayers = new ArrayList<>();
        listen();
    }

    public void listen(){
        while(!serverSocket.isClosed()) {
            try {
                Socket client = serverSocket.accept();
                ConnectionAgent connection = new ConnectionAgent(client);
                connection.addMessageListener(BattleServer.this);
                connectedPlayers.add(connection);
            }catch(IOException e){
                System.out.println(e.getMessage());
            }
        }

    }

    /**
     * broadcast a message to all players
     * @param message message to broadcast
     */
    public void broadcast(String message) {
        for (ConnectionAgent c : connectedPlayers) {
            c.sendMessage(message);
        }
    }

    /**
     * signals when a message is received and parses it
     *
     * @param message message to parse it
     * @param source  source message come from
     */
    public void messageReceived(String message, MessageSource source) throws IndexOutOfBoundsException{
        ConnectionAgent playerSource = (ConnectionAgent)source;
        String[] command = message.split("\\s+"); //splits the string into an array

        switch (command[0]) {
            case ("/join"):
                boolean freeName = true;
                for (int i = 0; i < pregamePlayers.size(); i++){
                    if (command[1].equals(pregamePlayers.get(i))){
                        freeName =false;
                    }
                }
                if(freeName){
                    pregamePlayers.add(command[1]);
                    broadcast(command[1] + " Has joined!!");
                }else{
                    playerSource.sendMessage("Name taken /join to try again");
                }


                break;

            case ("/play"):
             play(playerSource);

                break;
            case ("/attack"):
               attack(playerSource, command);
                break;
            case ("/show"):
                show(playerSource,command);
                break;

            case ("/quit"):
                quit(playerSource);
                break;
        }
    }

    /**
     * used to attack a place on the board checks to ensure it is the correct player attempting to attack
     * @param playerSource player who wants to attack
     * @param command coordinates he wants to attack
     */
    public void attack(ConnectionAgent playerSource,String[] command){
        String playerShooting = "";
        String shotPlayer = command[1];
        int firstcoord = Integer.parseInt(command[2]);
        int secondcoord = Integer.parseInt(command[3]);
        if (game.getinProgress()) {
            for (int i = 0; i < game.playerNum(); i++) {
                if (playerSource.equals(connectedPlayers.get(i))) {
                    playerShooting = game.getPlayers().get(i); //gets who is attacking
                }
            }
            if (!playerShooting.equals(shotPlayer)) {
                if (game.isPlayersTurn(playerShooting, current)) {
                    if (firstcoord < size && secondcoord < size &&
                            firstcoord >= 0 && secondcoord >= 0) { //ensure its a valid shot
                        game.shootAt(shotPlayer, firstcoord, secondcoord);
                        broadcast("Shots Fired at " + shotPlayer + " by " + playerShooting);
                        if (!game.checkProgress()) { // checks to see if the game is over if not continues broadcasting for attacks
                            broadcast("GAME OVER: " + game.getPlayers().get(0) + " wins!");
                        } else {
                            if (current == game.playerNum() - 1) { //resets to first player
                                current = 0;
                            } else {
                                current++;
                            }
                            broadcast("it is " + pregamePlayers.get(current) + "'s turn");
                        }
                    } else {
                        playerSource.sendMessage("Not a valid position. Please shoot again.");
                    }
                } else {
                    playerSource.sendMessage("It is not your turn.");
                }
            }else {
                playerSource.sendMessage("Cannot attack yourself");
                }
        } else {
            playerSource.sendMessage("Game has not been started /play to start");
        }
    }

    /**
     * shows a grid to the player depending on if it is his or not
     * @param playerSource player who wants the grid
     * @param command grid the player wants
     */
    public void show(ConnectionAgent playerSource, String[] command){
        if(game.getinProgress()) {
                for (int i = 0; i < game.playerNum(); i++) {
                    if (playerSource.equals(connectedPlayers.get(i))) {
                        playerSource.sendMessage(game.showGrid(command[1], i));
                    }
                }
        }else{
            playerSource.sendMessage("Game not started");
        }
    }

    /**
     * starts the game
     * @param playerSource player who wants to start the game
     */
    public void play(ConnectionAgent playerSource){
        if (!game.getinProgress()) {
            if (connectedPlayers.size() < 2) {
                playerSource.sendMessage("Not enough players to play the game");
            } else {
                broadcast("The game begins");
                current = 0;
                for (String s : pregamePlayers) {
                    game.addPlayer(s);
                    game.start();
                }
                broadcast("it is " + pregamePlayers.get(0) + "'s turn");
            }
        } else {
            playerSource.sendMessage("Game already in progress");
        }
    }

    /**
     * logic for a player quitting
     * @param playerSource the players connection agent that is quitting
     */
    public void quit(ConnectionAgent playerSource){
        for (int i = 0; i < connectedPlayers.size(); i++) {
            if(i < game.playerNum()){
                if(playerSource.equals(connectedPlayers.get(i))) {
                    broadcast(game.getPlayers().get(i) + " has surrendered!");
                    game.removePlayer(pregamePlayers.get(i));
                }
                if(playerSource.equals(connectedPlayers.get(i))){
                    try {
                        connectedPlayers.get(i).close();
                    }catch(IOException e){
                        System.out.println(e.getMessage());
                    }
                    connectedPlayers.remove(i);

                    sourceClosed(playerSource);
                    pregamePlayers.remove(i);
                }
                if(connectedPlayers.isEmpty()) {
                    sourceClosed(this);
                    System.out.println("All players have quit.");
                    try {
                        serverSocket.close();
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                }
                else if(!game.checkProgress() && !connectedPlayers.isEmpty()){
                    broadcast("GAME OVER: " + game.getPlayers().get(0) + " wins!" );

                }
            }

        }
    }

    /**
     * removes this as a listener from the source
     * @param source The <code>MessageSource</code> that does not expect more messages.
     */
    public void sourceClosed(MessageSource source) {
        source.removeMessageListener(this);
    }
}




