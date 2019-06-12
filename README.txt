Author's : Caleb Dinehart and Austin Powers 
Date     : 12/10/18

A networked game of Battle Ship.

TO RUN:
    First compile all java files.
    Then start the Server with "java server.BattleShipDriver <Port Number> <Size>"
    Then create the clients, AKA players, with 
                "java client.BattleDriver <Hostname> <Port Number> <Username>
    Then when enough players are in the server, one of them needs to type "\play"
                to start the game.


Notes: The client port numbers must match the server port. 

Known Bugs: When all players quit, the server prints out that the socket has 
        closed. We are not sure why this prints out. 
