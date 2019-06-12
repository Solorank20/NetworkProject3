package server;


import java.io.IOException;

/**
 * @author Austin Powers, Caleb Dineheart
 * driver for battleship server
 * @version 12/9/2018
 */
public class BattleShipDriver {
    public static void main(String[] args){

        if(args.length == 2){
            try {
                if(Integer.parseInt(args[0]) > 65535 || Integer.parseInt(args[0]) <1023){
                    System.out.println("Port number not valid: Valid ports are 1023-65535");
                }else {
                    if(Integer.parseInt(args[1])<5){
                        System.out.println("board size must be larger than five");
                    }else {
                            BattleServer server = new BattleServer(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
                        }
                    }
            }catch (IOException e){
                System.out.println("IO Exeception" + e.getMessage());
            }
        }else{
            System.out.println("Usage: Port Boardsize");
        }
    }
}
