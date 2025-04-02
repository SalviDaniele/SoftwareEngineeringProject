package Network;

import Controller.GameController;
import Listeners.Message;
import Model.Cards.Card;
import Model.Cards.Colors;
import Model.Table;
import Network.TCP.ClientTCP;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * An interface implemented by clients, containing many useful methods that the server can call in order
 * to communicate with a client.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ClientTCP.class, name = "ClientTCP")
})
public interface VirtualView extends Remote, Serializable {


    /**
     * Getter for the server attribute.
     *
     * @return a reference to the stub
     * @throws RemoteException in case of network errors
     */
    VirtualServer getServer() throws RemoteException;


    /**
     * Displays an update of the model to this client, based on the update message received from the server.
     *
     * @param message contains the necessary data to display the update
     */
    void showUpdate(Message message) throws RemoteException;


    /**
     * Getter for the nickname attribute.
     *
     * @return a reference to the nickname
     * @throws RemoteException in case of network errors
     */
    String getNickname() throws RemoteException;


    /**
     * Setter method for the nickname attribute
     *
     * @param nickname the player's name
     * @throws RemoteException in case of network errors
     */
    void setNickname(String nickname) throws RemoteException;


    /**
     * Getter for the first attribute.
     *
     * @return true if the player is the first to join a match
     * @throws RemoteException in case of network errors
     */
    boolean isFirst() throws RemoteException;


    /**
     *
     * @param i is the match ID
     * @throws RemoteException in case of network errors
     */
    void setIdMatch(int i) throws RemoteException;


    /**
     * Getter for the idMatch attribute.
     *
     * @return the ID of the match the client has joined
     * @throws RemoteException in case of network errors
     */
    int getIdMatch() throws  RemoteException;


    /**
     * Setter method for the color attribute.
     *
     * @param first is true if the client is the first to join a match, false otherwise
     * @throws RemoteException in case of network errors
     */
    void setFirst(boolean first) throws RemoteException;


    /**
     * Setter method for the color attribute.
     *
     * @param color of the player's pawn
     * @throws RemoteException in case of network errors
     */
    void setColor(Colors color) throws RemoteException;


    /**
     * Getter method for the color attribute.
     *
     * @return color of player's pawn
     * @throws RemoteException in case of network errors
     */
    Colors getColor() throws RemoteException;


    /**
     * Getter method for the chat attribute.
     *
     * @return chat of that player
     * @throws RemoteException in case of network errors
     */
    Chat getChat() throws RemoteException;


    /**
     * Setter method for the chat attribute.
     *
     * @param chat of the player
     * @throws RemoteException in case of network errors
     */
    void setChat(Chat chat) throws RemoteException;


    /**
     * Getter method for the viewChoice attribute.
     *
     * @return true if the user chose to play via Gui, false otherwise
     * @throws RemoteException in case of network errors
     */
    boolean getViewChoice() throws RemoteException;


    /**
     * Getter method for the blackPawn attribute.
     *
     * @return true if the player is selected as black pawn
     * @throws RemoteException in case of network errors
     */
    boolean isBlackPawn() throws RemoteException;


    /**
     * Setter method for the blackPawn attribute.
     *
     * @param blackPawn true if the player is selected as the black pawn, false otherwise
     * @throws RemoteException in case of network errors
     */
    void setBlackPawn(boolean blackPawn) throws RemoteException;


    /**
     * Connects the client to a specific IP address and port in order to start the GUI.
     *
     * @param ipAddress the client is going to connect to
     * @param port the client is going to connect to
     * @throws RemoteException in case of network errors
     */
    void connectGui(String ipAddress, int port) throws RemoteException;


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
     * @param recipient is the name the client typed
     * @return true if recipient is a valid name, false otherwise
     * @throws RemoteException in case of network errors
     */
    boolean checkRecipient(int idMatch, String recipient) throws RemoteException;


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
     * Gets the table of a specified match.
     *
     * @param idMatch is the ID related to the match
     * @return the table related to the specified match
     * @throws RemoteException in case of network errors
     */
    Table getTable(int idMatch) throws RemoteException;


    /**
     * Calls the respective method on VirtualServer.
     * The server adds the client to a list.
     *
     * @throws RemoteException in case of network errors
     */
    void connect() throws RemoteException;


    /**
     * Verifies the validity of a client's nickname from the GUI.
     *
     * @param name is the entered name
     * @return true if the name is already in use, false otherwise
     * @throws RemoteException in case of network errors
     */
    boolean chooseNameGui(String name) throws RemoteException;


    /**
     * Calls the respective method in VirtualServer.
     * Sends a message to the specified recipient by chat.
     * This message is sent by a player playing via Gui.
     *
     * @param recipient is the name of the recipient of the message
     * @param chatMessage is the message typed by the sender
     * @throws RemoteException in case of network errors
     */
    void sendMessageChatGui(String recipient, String chatMessage) throws RemoteException;


    /**
     * Call the method on the server that places the card in the play area, the parameter values are selected by the gui
     *
     * @param cardSelected references to the index of the card which has been selected
     * @param faceSelected indicates of the card needs to be placed by the front or by the back
     * @param x is the first coordinate of the position where the player wants to place the card
     * @param y is the second coordinate of the position where the player wants to place the card
     * @throws RemoteException in case of network errors
     */
    void placeGui(int cardSelected, String faceSelected, int x, int y) throws RemoteException;


    /**
     * Calls the respective method present on VirtualServer.
     * Gets the turn phase to a player playing via GUI.
     *
     * @return the phase to the GUI
     * @throws RemoteException in case of network errors
     */
    String getPhaseGui() throws RemoteException;


    /**
     /**
     * Calls the method checkTurn from VirtualServer.
     *
     * @return true if it is the turn of the specified player
     * @throws RemoteException in case of network errors
     */
    boolean checkTurn() throws RemoteException;


    /**
     * Checks if the conditions to proceed to the last phase of the game are met, this method is used to let the GUI know when the last turn will be.
     * @return 0 if the completed round was the last, 1 if it was the second to last, 2 otherwise
     * @throws RemoteException in case of network errors
     */
    int checkEndPhaseGui() throws RemoteException;


    /**
     * Calls the method checkTurn from VirtualServer.
     * Sets the specified player's turn as true and every other player's turn to false.
     *
     * @throws RemoteException in case of network errors
     */
    void itIsMyTurn() throws RemoteException;


    /**
     * Calls the method checkTurn from VirtualServer.
     * Calls the setter method for the attribute 'yourTurn'.
     *
     * @param value is the boolean to be put
     * @throws RemoteException in case of network errors
     */
    void setYourTurn(boolean value) throws RemoteException;


    /**
     * Calls the respective getter method present in MatchController.
     *
     * @return the nickname of the first player of the match, the one owning the black pawn
     * @throws RemoteException in case of network errors
     */
    String getNameOfThePlayerWithTheBlackPawn() throws RemoteException;


    /**
     * Calls the respective getter method present in VirtualServer.
     * Randomly assigns the black pawn.
     *
     * @throws RemoteException in case of network errors
     */
    void chooseTheBlackPawn() throws RemoteException;


    /**
     * Calls the respective setter method present in VirtualServer.
     *
     * @param value true if the player is assigned with the black pawn
     * @throws RemoteException in case of network errors
     */
    void setBlackPawnChosen(boolean value) throws RemoteException;


    /**
     * Calls the respective getter method present in VirtualServer.
     *
     * @return true if the black pawn has been assigned
     * @throws RemoteException in case of network errors
     */
    boolean isBlackPawnChosen() throws RemoteException;


    /**
     * Calls the respective drawResource method present on VirtualServer.
     * Lets the player draw a card from the resource deck.
     *
     * @throws RemoteException in case of network errors
     */
    void drawResource() throws RemoteException;


    /**
     * Calls the respective drawGold method present on VirtualServer.
     * Lets the player draw a card from the gold deck.
     *
     * @throws RemoteException in case of network errors
     */
    void drawGold() throws RemoteException;


    /**
     * Calls the respective drawTable method present on VirtualServer.
     * Lets the player draw a card from the table.
     *
     * @param cardSelected references to the index of the card which has been selected from the table
     * @throws RemoteException in case of network errors
     */
    void drawTable(int cardSelected) throws RemoteException;


    /**
     * Calls the respective method in VirtualServer.
     * Method used by a user playing via Gui to receive placed cards and their coordinates,
     * the cards in the array are sorted by placement order.
     *
     * @param nickname is the name of the player who placed the cards contained in the array
     * @return an array containing the cards placed with their coordinates in a system where
     *         the starter card is in position (0, 0)
     * @throws RemoteException in case of network errors
     */
    Card[] getOrderedCardsList(String nickname) throws RemoteException;


    /**
     * Calls the respective method in VirtualServer.
     * This method is called to get the points of a player by his nickname.
     *
     * @param nickname the nickname of the player
     * @return the points of the player
     * @throws RemoteException in case of network errors
     */
    int getPlayerPoints(String nickname) throws RemoteException;


    /**
     * Calls the respective method present in VirtualServer.
     * This method search for the nickname of every player in the current game.
     *
     * @return a list containing all players' nicknames
     * @throws RemoteException in case of network errors
     */
    List<String> getPlayersNames() throws RemoteException;


    /**
     * Notifies a client that their turn just started.
     *
     * @param message brings the notification
     * @throws RemoteException in case of network errors
     */
    void notifyTurn(Message message) throws RemoteException;


    /**
     * Calls the respective method on MatchController.
     * Notifies if some of the decks are empty.
     *
     * @return 0 if everything is fine
     *         1 if the Resource deck is empty
     *         2 if the Gold deck is empty
     *         3 if both decks are empty
     *         4 if everything is empty
     * @throws RemoteException in case of network errors
     */
    int whereCanIDraw() throws RemoteException;


    /**
     * Calls the respective method on MatchController.
     * Shows which positions on the table of a specific match have cards on them.
     *
     * @return a list with all the positions of the table with a card on them
     * @throws RemoteException in case of network errors
     */
    List<Integer> availablePositionsForDrawing() throws RemoteException;


    /**
     * Adds a received public chat message to the blocking queue.
     *
     * @param sender is the name of the sender
     * @param chatMessage is the message received
     * @throws RemoteException in case of network errors
     */
    void onChatMessage(String sender, String chatMessage) throws RemoteException;


    /**
     * Adds a received private chat message to the blocking queue.
     *
     * @param sender is the name of the sender
     * @param chatMessage is the message received
     * @throws RemoteException in case of network errors
     */
    void onPrivateChatMessage(String sender, String chatMessage) throws RemoteException;


    /**
     * Method called to get the messages received by the player
     * @return the BlockingQueue containing all the messages
     * @throws RemoteException in case of network errors
     */
    BlockingQueue<String> getMessagesQueueGui() throws RemoteException;


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
     * Calls the respective getter method present in MatchController.
     *
     * @param idMatch is the ID related to the specified match
     * @return an array of all the colors the player can choose from
     * @throws RemoteException in case of network errors
     */
    Colors[] showAvailableColors(int idMatch) throws RemoteException;


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
     * Verifies if every player in the specified match had their final score calculated.
     *
     * @param idMatch is the ID related to the current match
     * @return true if the score was calculated for every player
     * @throws RemoteException in case of network errors
     */
    boolean allObjectiveCardsCalculated(int idMatch) throws RemoteException;


    /**
     * Calls the respective method present in VirtualServer.
     * Method used by the GUI to know if the player is the winner.
     *
     * @return true if the player is the winner, false otherwise
     * @throws RemoteException in case of network errors
     */
    boolean endGameMessageGui() throws RemoteException;


    /**
     * Answers to a ping request from the server.
     *
     * @throws RemoteException in case of network errors
     */
    void pong() throws RemoteException;


    /**
     * Closes the connection for this client after a countdown of 3 seconds.
     */
    void closeConnection() throws RemoteException;


    /**
     * Forces the client to disconnect and to exit the application in case the match is aborted.
     *
     * @param details contains details about the occurred error
     * @throws RemoteException in case of network errors
     */
    void forceExit(String details) throws RemoteException;
}
