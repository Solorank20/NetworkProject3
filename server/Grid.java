package server;

import java.util.HashMap;
import java.util.Random;

/**
 * @author Austin Powers and Caleb Dinehart
 * @version 11/7/2018
 * Grid for Battleship game. The grid is a 2d array of strings.
 */
public class Grid {
    /*2d array of grid*/
    private String[][] grid;
    /* used to get direction and coordinate*/
    private Random dir;
    /* hashmaps for the ships*/
    private HashMap<String, Integer> ships;
    /* size of the coordinate*/
    private int size;
    /* North Direction */
    private final int NORTH = 0;
    /* South Direction */
    private final int SOUTH = 1;
    /* East Direction */
    private final int EAST = 2;
    /* West Direction */
    private final int WEST = 3;


    /**
     * default constructor
     * */
    public Grid(int size){
        this.grid = new String[size][size];
        for(int i = 0; i < size; i++){
            for(int k = 0; k < size; k++ ){
                grid[i][k] = "";
            }
        }
        this.ships = new HashMap<>();
        this.ships.put("C", 5);
        this.ships.put("B", 4);
        this.ships.put("R", 3);
        this.ships.put("S", 3);
        this.ships.put("D", 2);
        this.size = size;
        this.dir = new Random();
    }

    /**
     * checks to see if a spot is filled
     * @param spot1 spot one to check
     * @param spot2 spot two to check
     * @return if the spot is filled or not
     */
    public boolean isFilledSpot(int spot1, int spot2, int dir, String name ){
        boolean filled = false;
        for(int i = 0; i < ships.get(name); i++){                           
              if(dir == NORTH){ //compares passed direction                   
                  if(!grid[spot1 - i][spot2].equals("")){ //checks to see if the spot is 
                      filled = true;                                              
                      }                                                           
              }else if(dir == SOUTH){//compares passed direction              
                  if(!grid[spot1 + i][spot2].equals("")){//checks to see if the spot is e
                     filled = true;                                              
                  }                                                           
              }else if(dir == EAST){//compares passed direction               
                  if(!grid[spot1][spot2 + i].equals("")) {//checks to see if the spot is 
                     filled = true;                                             
                  }                                                           
              }else{                                                          
                  if(!grid[spot1][spot2 - i].equals("")){//checks to see if the spot is e
                     filled = true;                                          
                  }                                                           
                                                                             
              }                    
        }
        return filled;
    }

    /**
     * gets the grid
     * @return the grid
     */
    public String[][] getGrid() {
        return grid;
    }

    /**
     * calls Place ship for all ships.
     */
    public void placeShips(){
        placeShip("C");
        placeShip("B");
        placeShip("R");
        placeShip("S");
        placeShip("D");
    }

    /**
     * Places a ship in the grid checks to ensure that it can fit and does not collide with ships
     * @param name name of the ship type
     */
    public void placeShip(String name){
        int direction = dir.nextInt(3);
        boolean fit = true;
        int xlocation = 0;
        int ylocation = 0;
        switch(direction) {
            case NORTH:
                while(fit) {
                    xlocation = dir.nextInt(size - SOUTH);
                    ylocation = dir.nextInt(size - SOUTH);
                    if (size - ylocation <= ships.get(name)) { //ensures that it stays inside the board
                        fit = isFilledSpot(ylocation, xlocation, NORTH, name);
                    }
                }
                for(int i = 0; i < ships.get(name); i++){ //adds ship
                    grid[ylocation - i][xlocation] = name;
                }
                break;
            case SOUTH:
                while(fit) {
                    xlocation = dir.nextInt(size - 1);
                    ylocation = dir.nextInt(size - 1);
                    if (size - ylocation >= ships.get(name)) { //ensures that it stays inside the board
                        fit = isFilledSpot(ylocation,xlocation,SOUTH,name);
                    }
                }
                for(int i = 0; i < ships.get(name); i++) //adds ship
                    grid[ylocation + i][xlocation] = name;
                break;
            case EAST:
                    while(fit) {
                        xlocation = dir.nextInt(size - 1);
                        ylocation = dir.nextInt(size - 1);
                        if (size - xlocation >= ships.get(name)) { //ensures that it stays inside the board
                            fit = isFilledSpot(ylocation,xlocation,EAST,name);
                        }
                    }
                    for(int i = 0; i < ships.get(name); i++) //adds ship
                        grid[ylocation][xlocation + i] = name;
                break;
            case WEST:
                    while(fit) {
                        xlocation = dir.nextInt(size - 1);
                        ylocation = dir.nextInt(size - 1);
                        if (size - xlocation <= ships.get(name)) { //ensures that it stays in the board
                            fit = isFilledSpot(ylocation,xlocation,WEST,name);
                        }
                    }
                        for(int i = 0; i < ships.get(name); i++) //adds the ship
                            grid[ylocation][xlocation - i] = name;
                break;
        }
    }

    /**
     * shoots the space if it contains a ship place and @ else place and X
     * @param xSpot x coordinate
     * @param ySpot y coordinate
     */
    public String shoot(int ySpot, int xSpot){
        String sym = "";
        if(!grid[ySpot][xSpot].equals("")) {
            int damaged = ships.get(grid[ySpot][xSpot]);
            damaged--;
            ships.replace(grid[ySpot][xSpot],damaged);
            grid[ySpot][xSpot] = "@";
            sym = "@";
        }
        else{
            grid[ySpot][xSpot] = "X";
            sym = "X";
        }
        return sym;
    }

    public void setSpot(int ySpot, int xSpot, String sym){
        this.grid[ySpot][xSpot] = sym;
    }

    /**
     * Prints the current state of the grid
     */
    public String show(){
        String gridStr = "";
        String divide = "  ";
        gridStr = gridStr + "    ";

        for(int i = 0; i < size; i++){
            gridStr = gridStr + i + "   ";
        }
        gridStr+="\n";
        for(int i = 0; i < size; i++){
            divide = divide + "+---";
        }
        divide = divide + "+";
        gridStr = gridStr + divide + "\n";

        for(int i = 0; i < size; i++){
            gridStr = gridStr + i;
            for(int k = 0; k < size; k++){
                if(grid[i][k].equals("")){
                    gridStr = gridStr + " |  ";
                }else {
                    gridStr = gridStr + " | " + grid[i][k];
                }
            }
            gridStr = gridStr + " |\n" + divide + "\n";

        }
        return gridStr;
    }
    /**
     * getter for hashmaps
     */
    public HashMap<String, Integer> getShips() {
        return ships;
    }
}
