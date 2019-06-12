package client;

import java.io.IOException;
import java.util.Scanner;

/**
 * driver for client side of battleship game
 * @author Austin Powers
 * @version 12/9/2018
 */
public class BattleDriver {
    public static void main(String args[]){
        if(args.length != 3){
            System.out.println("Usage: Inetaddress port username");
            System.exit(1);
        }else {
            String inetaddress = args[0];
            int port = Integer.parseInt(args[1]);
            String username = args[2];
            try {
                BattleClient player = new BattleClient(inetaddress, port, username);
                player.send("/join " + username);
                Scanner in = new Scanner(System.in);
                boolean quit = false;
                String message = "";
                while(!quit){
                    message = in.nextLine();
                    String[] sMess = message.split("\\s+");
                    if(sMess[0].equals("/join") || sMess[0].equals("/attack") || sMess[0].equals("/show") ||
                            sMess[0].equals("/play") || sMess[0].equals("/quit")) {
                        player.send(message);
                        if(message.equals("/quit")){
                            quit = true;
                            player.sourceClosed(player.getConnectionAgent());
                            //System.exit(0);
                        }
                    }else{
                        System.out.println("Commands /attack /show /play /quit");
                    }
                }

            } catch (IOException e) {
                System.out.println(e.getMessage());
                System.exit(1);
            }
        }

    }
}
