package Network;

import Controller.GameController;
import Listeners.Message;
import Model.Cards.Card;
import Model.Cards.Colors;
import Model.Table;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * An interface implemented by server classes, containing many useful methods that the client can call in order
 * to communicate with the server.
 */
public interface VirtualServer extends Remote {


    /**
     * We register a client to a list of clients (VirtualView) the server keeps.
     *
     * @param client is the client that needs to be added
     * @throws RemoteException if a network related error occurs
     */
    void connect(ClientData client) throws RemoteException;


    /**
     * Checks if the nickname chosen by the client is already in use (check is case-sensitive).
     *
     * @param nameToCheck is the name the client would like to use
     * @return true if the name is already in use, false otherwise
     * @throws RemoteException if a network related error occurs
     */
    boolean checkName(String nameToCheck) throws RemoteException;


    /**
     * Checks if a command selected by the client is legal during this phase of their turn.
     *
     * @param idMatch is the id which identifies the match the clint is playing
     * @param nickname is the name of the player who launched the command
     * @param command is the command indicating the action the client is trying to perform
     * @return true if the command is legal, false otherwise
     * @throws RemoteException if a network related error occurs
     */
    boolean checkCommand(int idMatch, String nickname, String command) throws RemoteException;


    /**
     * Checks if the player with the specified nickname is present in the specified match.
     *
     * @param idMatch indicates the match
     * @param nickname indicates the player to search
     * @return true if the player is present in the match
     * @throws RemoteException if a network related error occurs
     */
    boolean checkPlayer(int idMatch, String nickname) throws RemoteException;


    /**
     * Checks whether the client  inserted a valid recipient name for chatting.
     *
     * @param idMatch is the id which identifies the match the clint is playing
     * @param sender the nickname of the sender
     * @param recipient is the name the client typed
     * @return true if recipient is a valid name, false otherwise
     * @throws RemoteException in case of network errors
     */
    boolean checkRecipient(int idMatch, String sender, String recipient) throws RemoteException;


    /**
     * Getter method for the server's controller attribute.
     *
     * @return the instance of the server's controller
     * @throws RemoteException in case of network errors
     */
    GameController getController() throws RemoteException;


    /**
     * Certifies if there is an open match.
     *
     * @return true if there isn't an open match
     * @throws RemoteException in case of network errors
     */
    boolean firstPlayer() throws RemoteException;


    /**
     * Initializes the number of players in the game.
     *
     * @param n is the number of players declared by the client
     * @throws RemoteException in case of network errors
     */
    void setNumbOfPlayers(int n) throws RemoteException;


    /**
     * Calls the respective method present in GameController.
     * Adds a new match to the game.
     *
     * @param name is the name of the player who created the new match
     * @return the ID of the newly created match
     * @throws RemoteException in case of network errors
     */
    int addMatch(String name) throws RemoteException;


    /**
     * Calls the respective method present in GameController.
     * The method adds a Player in a match which has not yet started.
     *
     * @param name of the player
     * @return the ID related to the match
     * @throws RemoteException in case of network errors
     */
    int addPlayer(String name) throws RemoteException;


    /**
     * Calls the respective method present in Table.
     * This method search for the nickname of every player in the current game.
     *
     * @param idMatch is the ID related to the match
     * @return a list containing all players' nicknames
     * @throws RemoteException in case of network errors
     */
    List<String> getPlayersNames(int idMatch)throws RemoteException;


    /**
     * Sends the update contained in a message to the respective client, by calling their method showUpdate.
     *
     * @param message is the message containing the necessary information to display
     * @throws RemoteException in case of network errors
     */
    void sendUpdate(Message message) throws RemoteException;


    /**
     * Gets the table of a specified match.
     *
     * @param idMatch is the ID related to the match
     * @return the table related to the specified match
     * @throws RemoteException in case of network errors     *
     */
    Table getTable(int idMatch) throws RemoteException;


    /**
     * Calls the respective place method present on the controller.
     * Lets the player place a card on their play area.
     *
     * @param idMatch is the ID related to the match the player is currently playing
     * @param nickname is the name of the player who launched the command
     * @param cardSelected references to the index of the card which has been selected
     * @param faceSelected indicates of the card needs to be placed by the front or by the back
     * @param x is the first coordinate of the position where the player wants to place the card
     * @param y is the second coordinate of the position where the player wants to place the card
     * @throws RemoteException in case of network errors
     */
    void place(int idMatch, String nickname, int cardSelected, String faceSelected, int x, int y) throws RemoteException;


    /**
     * Calls the respective method present on MatchController.
     * Gets the turn phase to a player playing via GUI.
     *
     * @param idMatch is the ID related to the match the player is currently playing
     * @return the phase to the GUI
     * @throws RemoteException in case of network errors
     */
    String getPhaseGui(int idMatch) throws RemoteException;


    /**
     * Calls the respective drawResource method present on the controller.
     * Lets the player draw a card from the resource deck.
     *
     * @param idMatch is the ID of the match the player is currently playing
     * @param nickname is the name of the player who wants to play the action
     * @throws RemoteException in case of network errors
     */
    void drawResource(int idMatch, String nickname) throws RemoteException;


    /**
     * Calls the respective drawGold method present on the controller.
     * Lets the player draw a card from the gold deck.
     *
     * @param idMatch is the ID of the match the player is currently playing
     * @param nickname is the name of the player who wants to play the action
     * @throws RemoteException in case of network errors
     */
    void drawGold(int idMatch, String nickname) throws RemoteException;


    /**
     * Calls the respective drawTable method present on the controller.
     * Lets the player draw a card from the table.
     *
     * @param idMatch is the ID of the match the player is currently playing
     * @param nickname is the name of the player who wants to play the action
     * @param cardSelected references to the index of the card which has been selected from the table
     * @throws RemoteException in case of network errors
     */
    void drawTable(int idMatch, String nickname, int cardSelected) throws RemoteException;


    /**
     * Calls the respective method in VirtualServer.
     * Method used by a user playing via Gui to receive placed cards and their coordinates,
     * the cards in the array are sorted by placement order.
     *
     * @param idMatch is the ID of the match the player is currently playing
     * @param nickname is the name of the player who placed the cards contained in the list
     * @return an array containing the cards placed and their coordinates in a system where
     *         the starter card is in position (0, 0)
     * @throws RemoteException in case of network errors
     */
    Card[] getOrderedCardsList(int idMatch, String nickname) throws RemoteException;


    /**
     * This method is called to get the points of a player by his nickname.
     *
     * @param idMatch is the ID of the match the player is currently playing
     * @param nickname the nickname of the player
     * @return the points of the player
     * @throws RemoteException in case of network errors
     */
    int getPlayerPoints( int idMatch, String nickname) throws RemoteException;


    /**
     * Calls the respective method on MatchController.
     * Notifies if some of the decks are empty.
     *
     * @param idMatch is the ID of the match the player is currently playing
     * @return 0 if everything is fine
     *         1 if the Resource deck is empty
     *         2 if the Gold deck is empty
     *         3 if both decks are empty
     *         4 if everything is empty
     * @throws RemoteException in case of network errors
     */
    int whereCanIDraw(int idMatch) throws RemoteException;


    /**
     * Calls the respective method on MatchController.
     * Shows which positions on the table of a specific match have cards on them.
     *
     * @param idMatch is the ID of the match the player is currently playing
     * @return a list with all the positions of the table with a card on them
     * @throws RemoteException in case of network errors
     */
    List<Integer> availablePositionsForDrawing(int idMatch) throws RemoteException;


    /**
     * Sends a message to the specified recipient by chat.
     *
     * @param idMatch is the ID of the match the player is currently playing
     * @param sender is the name of the sender client
     * @param recipient is the name of the recipient of the message
     * @param chatMessage is the message typed by the sender
     * @throws RemoteException in case of network errors
     */
    void chat(int idMatch, String sender, String recipient, String chatMessage) throws RemoteException;


    /**
     * Getter method for the clients attribute.
     *
     * @return the list with all the registered clients
     * @throws RemoteException in case of network errors
     */
    List<ClientData> getClients() throws RemoteException;


    /**
     * Checks if the requested number of players joined the specified match.
     *
     * @param idMatch is the ID related to the specified match
     * @return true if every expected player is connected
     * @throws RemoteException in case of network errors
     */
    boolean allConnected(int idMatch) throws RemoteException;


    /**
     * Checks if every player correctly placed their starter card.
     *
     * @param idMatch is the ID related to the specified match
     * @return true if every starter card has been placed, false otherwise
     * @throws RemoteException in case of network errors
     */
    boolean allStarterCardsPlaced(int idMatch) throws RemoteException;


    /**
     * If a starter card has been placed, this method increases the number of placed starter cards for the current match.
     *
     * @param idMatch is the ID related to the specified match
     * @throws RemoteException in case of network errors
     */
    void increaseNumOfStarterCardsPlaced(int idMatch) throws RemoteException;


    /**
     * Calls the respective method present in MatchController.
     * This method shuffles decks on the table and provides each player with their starter card.
     *
     * @param idMatch is the ID related to the specified match
     * @throws RemoteException in case of network errors
     */
    void shuffleDecksAndGiveStarterCards(int idMatch) throws RemoteException;


    /**
     * With this method every player draws 3 cards and common objective cards are placed
     *
     * @param idMatch is the current match
     * @throws RemoteException in case of network errors
     */
    void drawCardsAndPlaceCommonObjectives(int idMatch) throws RemoteException;


    /**
     * Calls the respective method present in MatchController.
     * Displays to the player their hand.
     *
     * @param idMatch is the ID related to the specified match
     * @param nickname is the name of the player who made the request
     * @throws RemoteException in case of network errors
     */
    void viewHand(int idMatch, String nickname) throws RemoteException;


    /**
     * Calls the respective method present in MatchController.
     * Displays to the player the starting table.
     *
     * @param idMatch is the ID related to the specified match
     * @param nickname is the name of the player who made the request
     * @throws RemoteException in case of network errors
     */
    void viewStartingTable(int idMatch, String nickname) throws RemoteException;


    /**
     * Calls the respective method present in MatchController.
     * Displays to the player the table.
     *
     * @param idMatch is the ID related to the specified match
     * @param nickname is the name of the player who made the request
     * @throws RemoteException in case of network errors
     */
    void viewTable(int idMatch, String nickname) throws RemoteException;


    /**
     * Calls the respective method present in MatchController.
     * Place the 2 common objective cards on the table.
     *
     * @param idMatch is the ID related to the specified match
     * @throws RemoteException in case of network errors
     */
    void giveObjectives(int idMatch) throws RemoteException;


    /**
     * Calls the respective method present in MatchController.
     * Displays to the player their 2 options for their secret objectives.
     *
     * @param idMatch is the ID related to the specified match
     * @param nickname is the name of the player who made the request
     * @throws RemoteException in case of network errors
     */
    void viewChoiceObjectives(int idMatch, String nickname) throws RemoteException;


    /**
     * Calls the respective method present in MatchController.
     * Puts the chosen card on the table.
     *
     * @param idMatch is the ID related to the specified match
     * @param nickname is the name of the player who made the request
     * @param obSelected indicates which option the client selected
     * @throws RemoteException in case of network errors
     */
    void putChoiceObjectives(int idMatch, String nickname, int obSelected) throws RemoteException;


    /**
     * Calls the respective getter method present in MatchController.
     *
     * @param idMatch is the ID related to the specified match
     * @return an array of all the colors the player can choose from
     * @throws RemoteException in case of network errors
     */
    Colors[] showAvailableColors(int idMatch) throws RemoteException;


    /**
     * Checks wether it is the turn of a specified player.
     *
     * @param idMatch references to the ID of the specified match
     * @param nickname is the nickname of the player who is making the request
     * @throws RemoteException in case of network errors
     */
    void itIsMyTurn(int idMatch, String nickname) throws RemoteException;


    /**
     * Calls the respective method present in MatchController.
     * Verifies if it is the turn of client who made the request to choose the color of their pawn.
     *
     * @param idMatch references to the ID of the specified match
     * @param nickname is the nickname of the player who is making the request
     * @return true if it is the turn of the player to choose the color
     * @throws RemoteException in case of network errors
     */
    boolean canIChooseTheColor(int idMatch, String nickname) throws RemoteException;


    /**
     * Calls the respective method present in MatchController.
     * Checks if the color the client selected is valid.
     *
     * @param idMatch references to the ID of the specified match
     * @param colorChosen is the color the client selected
     * @return true if the client entered a valid color
     * @throws RemoteException in case of network errors
     */
    boolean checkColor(int idMatch, Colors colorChosen) throws RemoteException;


    /**
     * Calls the respective method present in MatchController.
     * Removes the color from the array of the available colors and lets the next player choose their color.
     *
     * @param idMatch references to the ID of the specified match
     * @param nickname is the nickname of the player who is making the request
     * @param colorChosen is the color the client selected
     * @throws RemoteException in case of network errors
     */
    void removeColorAndPassTurn(int idMatch, String nickname, Colors colorChosen) throws RemoteException;


    /**
     * Calls the respective getter method present in MatchController.
     *
     * @param idMatch is the ID related to the current match
     * @return true if the black pawn has been assigned
     * @throws RemoteException in case of network errors
     */
    boolean isBlackPawnChosen(int idMatch) throws RemoteException;


    /**
     * Calls the respective setter method present in MatchController.
     *
     * @param idMatch is the ID related to the current match
     * @param value is a boolean indicating the value to pass to the setter method as a parameter
     * @throws RemoteException in case of network errors
     */
    void setBlackPawnChosen(int idMatch, boolean value) throws RemoteException;


    /**
     * Calls the respective method present in MatchController.
     * Randomly assigns the black pawn.
     *
     * @param idMatch is the ID related to the current match
     * @throws RemoteException in case of network errors
     */
    void chooseTheBlackPawn(int idMatch) throws RemoteException;


    /**
     * Calls the respective getter method present in MatchController.
     *
     * @param idMatch is the ID related to the current match
     * @return the nickname of the first player of the match, the one owning the black pawn
     * @throws RemoteException in case of network errors
     */
    String getNameOfThePlayerWithTheBlackPawn(int idMatch) throws RemoteException;


    /**
     * Calls the respective setter method present in MatchController.
     *
     * @param idMatch is the ID related to the current match
     * @param nickname is the nickname of the player whose turn will be set
     * @param value is a boolean indicating the value to pass to the setter method as a parameter
     * @throws RemoteException in case of network errors
     */
    void setYourTurn(int idMatch, String nickname, boolean value) throws RemoteException;


    /**
     * Calls the respective getter method isYourTurn of the specified player.
     *
     * @param idMatch is the ID related to the current match
     * @param nickname is the nickname of the player who is trying to perform an action
     * @return true if it is the turn of the specified player
     * @throws RemoteException in case of network errors
     */
    boolean checkTurn(int idMatch, String nickname) throws RemoteException;


    /**
     * Calls the respective method present in MatchController.
     * The player with the specified nickname receive on display the play area.
     *
     * @param idMatch is the ID relative to the specified match
     * @param nickname is the nickname of the player who is requesting to view the area
     * @param areaOwner is the nickname of the player whose area is going to be displayed
     * @throws RemoteException in case of network errors
     */
    void viewPlayArea(int idMatch, String  nickname, String areaOwner) throws RemoteException;


    /**
     * Calls the respective method present in MatchController.
     * The player with the specified nickname receive on display their secret objective card.
     *
     * @param idMatch is the ID relative to the specified match
     * @param nickname is the nickname of the player who is requesting to view the card
     * @throws RemoteException in case of network errors
     */
    void viewSecretObjective(int idMatch, String  nickname) throws RemoteException;


    /**
     * Calls the respective method present in MatchController.
     * Checks is the specified coordinates allow the player to place a card.
     *
     * @param idMatch is the ID relative to the specified match
     * @param nickname is the nickname of the player who is requesting to place the card
     * @param x is the first coordinate (row) selected by the player
     * @param y is the first coordinate (row) selected by the player
     * @return true if a card can be placed on the specified coordinates, false otherwise
     * @throws RemoteException in case of network errors
     */
    boolean areCoordinatesValid(int idMatch, String  nickname, int x, int y) throws RemoteException;


    /**
     * Calls the respective method present in MatchController.
     * Checks if the requirements to place a gold card are met.
     *
     * @param idMatch is the ID relative to the specified match
     * @param nickname is the nickname of the layer who is requesting to place the gold card
     * @param cardSelected indicates the position of the gold card in the player's hand
     * @return true if the requirements are met, false otherwise
     * @throws RemoteException in case of network errors
     */
    boolean canIPlaceTheGoldCard(int idMatch, String nickname, int cardSelected) throws RemoteException;


    /**
     * Calls the respective method present in MatchController.
     * Show to a player how his resources will change after the placement of a card.
     *
     * @param idMatch is the ID relative to the specified match
     * @param nickname is the nickname of the player who is placing the card
     * @param cardSelected indicates the position of the card in the player's hand
     * @param faceSelected indicates the face of the card that the player has chosen
     * @param pos1 is the row where the card will be placed
     * @param pos2 is the column where the card will be placed
     * @throws RemoteException in case of network errors
     */
    void viewDifferenceOfResources(int idMatch, String nickname, int cardSelected, String faceSelected, int pos1, int pos2) throws RemoteException;


    /**
     * If a player chooses their secret objective, this method updates the number of chosen secret objectives for the specified match.
     *
     * @param idMatch is the ID related to the specified match
     * @throws RemoteException in case of network errors
     */
    void increaseNumOfObjectiveCardChosen(int idMatch) throws RemoteException;


    /**
     * Checks if every player has chosen their secret objective.
     *
     * @param idMatch is the ID related to the specified match
     * @return true if every player has chosen their secret objective, false otherwise
     * @throws RemoteException in case of network errors
     */
    boolean allObjectiveCardsChosen(int idMatch) throws RemoteException;


    /**
     * Calls the respective method present in MatchController.
     * Checks if the specified match has reached the final phase of the game and wether this will be the final round.
     * If the final phase was triggered by the last player of the round, the last round begins,
     * otherwise, firstly the current round terminates, secondly the last round begins.
     *
     * @param idMatch is the ID related to the specified match
     * @param nickname is the nickname of the client/player
     * @return 0 if the conditions for the final phase are not satisfied, 1 if they are, 2 if the last round is starting
     * @throws RemoteException in case of network errors
     */
    int checkSecondToLastTurn(int idMatch, String nickname) throws RemoteException;


    /**
     * Calls the respective setter method present in MatchController.
     *
     * @param idMatch is the ID related to the current match
     * @param value is a boolean indicating the value to pass to the setter method as a parameter
     * @throws RemoteException in case of network errors
     */
    void setLastRound(int idMatch, boolean value) throws RemoteException;


    /**
     * Calls the respective getter method present in MatchController.
     *
     * @param idMatch is the ID related to the current match
     * @return true if the match is in its last round
     * @throws RemoteException in case of network errors
     */
    boolean isLastRound(int idMatch) throws RemoteException;


    /**
     * Calls the respective getter method present in MatchController.
     *
     * @param idMatch is the ID related to the current match
     * @return true if the match is in its second to last round
     * @throws RemoteException in case of network errors
     */
    boolean isSecondToLastRound(int idMatch) throws RemoteException;


    /**
     * Calls the respective setter method present in MatchController.
     *
     * @param idMatch is the ID related to the current match
     * @param b is a boolean indicating the value to pass to the setter method as a parameter
     * @throws RemoteException in case of network errors
     */
    void setSecondToLastRound(int idMatch, boolean b) throws RemoteException;


    /**
     * Calls the respective method present in MatchController.
     * Verify if the next player is the first player.
     *
     * @param idMatch is the ID related to the current match
     * @param nickname is the nickname of the client/player
     * @return true if the next player in the list Players present on the match table owns the black pawn
     * @throws RemoteException in case of network errors
     */
    boolean isNextPlayerTheBlackPawn(int idMatch, String nickname) throws RemoteException;


    /**
     * Increase the number of players who have finished to play the match.
     *
     * @param idMatch is the ID related to the current match
     * @throws RemoteException in case of network errors
     */
    void increaseNumOfPlayersThatHaveFinishedToPlay(int idMatch) throws RemoteException;


    /**
     * Calls the respective method present in MatchController.
     * Gets the number of players who have finished to play.
     *
     * @param idMatch is the ID related to the current match
     * @return the number of players who have finished to play
     * @throws RemoteException in case of network errors
     */
    int getNumOfPlayersThatHaveFinishedToPlay(int idMatch) throws RemoteException;


    /**
     * Calls the respective getter method present in MatchController.
     *
     * @param idMatch is the ID related to the current match
     * @return the number of players chosen by the creator of the match
     * @throws RemoteException in case of network errors
     */
    int getNumOfPlayers(int idMatch) throws RemoteException;


    /**
     * Calls the respective method present in MatchController.
     * Calculates the points gained by the specified player from the common and the secret objectives.
     *
     * @param idMatch is the ID related to the current match
     * @param nickname is the nickname of the client/player
     * @throws RemoteException in case of network errors
     */
    void calculateObjectives(int idMatch, String nickname) throws RemoteException;


    /**
     * Increases the number of players who have their final score calculated.
     *
     * @param idMatch is the ID related to the current match
     * @throws RemoteException in case of network errors
     */
    void increaseNumOfObjectiveCardCalculated(int idMatch) throws RemoteException;


    /**
     * Verifies if every player in the specified match had their final score calculated.
     *
     * @param idMatch is the ID related to the current match
     * @return true if the score was calculated for every player
     * @throws RemoteException in case of network errors
     */
    boolean allObjectiveCardsCalculated(int idMatch) throws RemoteException;


    /**
     * Calls the respective method present in MatchController.
     * Decides who is the winner of the specified match.
     *
     * @param idMatch is the ID related to the current match
     * @throws RemoteException in case of network errors
     */
    void calculateWinner(int idMatch) throws RemoteException;


    /**
     * Calls the respective method present in MatchController.
     * Displays a "you won" or a "you lost" message to every player.
     *
     * @param idMatch is the ID related to the current match
     * @param nickname is the nickname of the specified player
     * @throws RemoteException in case of network errors
     */
    void showEndGameMessage(int idMatch, String nickname)throws RemoteException;


    /**
     * Calls the respective method present in MatchController.
     * Method used by the GUI to know if the player is the winner.
     *
     * @param idMatch is the ID related to the current match
     * @param nickname is the nickname of the specified player
     * @return true if the player is the winner, false otherwise
     * @throws RemoteException in case of network errors
     */
    boolean endGameMessageGui(int idMatch, String nickname)throws RemoteException;


    /**
     * Calls the respective method present in MatchController.
     * This cheat provides the specified player with 10000 resources per each symbol.
     *
     * @param idMatch is the ID related to the current match
     * @param nickname is the nickname of the specified player
     * @throws RemoteException in case of network errors
     */
    void cheat1(int idMatch, String nickname) throws RemoteException;


    /**
     * Calls the respective method present in MatchController.
     * This cheat provides the specified player with 20 points.
     *
     * @param idMatch is the ID related to the current match
     * @param nickname is the nickname of the specified player
     * @throws RemoteException in case of network errors
     */
    void cheat2(int idMatch, String nickname) throws RemoteException;


    /**
     *Calls the respective method present in MatchController.
     *With this cheat both resource deck and gold deck become empty.
     * @param idMatch is the ID related to the current match
     * @throws RemoteException in case of network errors
     */
    void cheat3(int idMatch) throws RemoteException;


    /**
     * Removes the specified client from the clients list present on the server.
     *
     * @param nickname is the nickname of the specified player
     * @throws RemoteException in case of network errors
     */
    void removeClient(String nickname) throws RemoteException;
}
