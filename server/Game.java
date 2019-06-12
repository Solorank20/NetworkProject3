package server;
import java.util.*;
/**
 * @author Austin powers and Caleb Dinehart
 * @version 11/7/2018
 * The game of battleship logic
 */
public class Game {

    /*All of the Hostile Grids*/
    private int size;
    private ArrayList<Grid> HostileGrids;
    /* all of the Player Grids*/
    private ArrayList<Grid> PlayerGrids;
    /* player list*/
    private ArrayList<String> players;
    /*player health*/
    private ArrayList<Integer> playerhealth;
    /*start of total player health*/
    private int TOTALHEALTH = 17;
    /* players at 0 health*/
    int deadPlayers = 0;
    private boolean inProgress;
    int gameCompleted = 0;

    /**
     * Default constructor
     * @param size the size of the grid
     */
    public Game(int size ){
        this.size = size ;
        this.HostileGrids = new ArrayList<>();
        this.PlayerGrids = new ArrayList<>();
        this.players = new ArrayList<>();
        this.playerhealth = new ArrayList<>();
        this.inProgress = false;
    }

    /**
     * add a player to the player list
     * @param name name of player
     */
    public void addPlayer(String name){
        Grid selfGrid = new Grid(size);
        selfGrid.placeShips();
        Grid hostileGrid = new Grid(size);
        HostileGrids.add(hostileGrid);
        PlayerGrids.add(selfGrid);
        players.add(name);
        playerhealth.add(TOTALHEALTH);

    }

    /**
     * remove player from list
     * @param name name of player to remove
     */
    public void removePlayer(String name){
        for(int i = 0; i < players.size(); i++){
            if(players.get(i).equals(name)){
                players.remove(i);
                HostileGrids.remove(i);
                PlayerGrids.remove(i);
            }
        }
    }

    /**
     * shooting at player fixes both hostile and player grids
     * @param name player to shoot at
     * @param ySpot y spot of grid
     * @param xSpot x spot of grid
     */
    public void shootAt(String name, int ySpot, int xSpot){
        String horm; // will hold the symbol for if it is a hit or miss
        for(int i = 0; i < players.size(); i++){
         if(players.get(i).equals(name)){
             horm = PlayerGrids.get(i).shoot(ySpot,xSpot);
             HostileGrids.get(i).setSpot(ySpot,xSpot,horm);
             if(horm == "@"){
                 playerhealth.set(i,playerhealth.get(i)- 1);
                     if(playerhealth.get(i) == 0){
                         deadPlayers++;
                         removePlayer(players.get(i));
                         playerhealth.remove(i);
                     }

                 }
             }
         }
        }
    /**
     * shows grid of players
     * @param playerName player name to retrieve grid
     * @param playerAsking player asking for grid
     */
    public String showGrid(String playerName, int playerAsking){
        String result = "";
        if(players.get(playerAsking).equals(playerName)){
            result = PlayerGrids.get(players.indexOf(playerName)).show();
        }else {
            result = HostileGrids.get(players.indexOf(playerName)).show();
        }
        return result;
    }

    /**
     * checks to see if its the players turn
     * @param playerName player to check
     * @param currentPlayer current players turn
     * @return if it is their turn
     */
    public boolean isPlayersTurn(String playerName, int currentPlayer){
        boolean result = false;
        if(playerName.equals(players.get(currentPlayer))){
            result = true;
        }
        return result;
    }

    /**
     * gets the progress of the game
     * @return if the game is in progress or not
     */
    public boolean getinProgress(){
        return inProgress;
    }

    /**
     * checks to ensure there are enough people to play the game
     * @return if the game is over or not
     */
    public boolean checkProgress(){
        if(players.size() == 1) {
            inProgress = false;
            gameCompleted++;
        }
        return inProgress;
    }

    /**
     * starts the game by setting the boolean to true
     */
    public void start(){
        inProgress = true;
    }

    /**
     * getter for number of players
     * @return number of players
     */
    public int playerNum(){
        return players.size();
    }

    /**
     * getter for players
     * @return the players
     */
    public ArrayList<String> getPlayers(){
        return players;
    }


}
