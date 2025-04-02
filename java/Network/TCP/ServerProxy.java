package Network.TCP;

import Controller.GameController;
import Listeners.Message;
import Model.Cards.Card;
import Model.Cards.Colors;
import Model.Table;
import Network.ClientData;
import Network.VirtualServer;
import javafx.util.Pair;

import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * A virtual server which serves, in accordance to the method a client called, writes strings in output
 * to the client handler, which will then call the right method on ServerTCP.
 */
public class ServerProxy implements VirtualServer {

    /**
     * The output where the strings are printed in order for ClientHandler to read them.
     */
    final PrintWriter out;


    /**
     * Constructor of the ServerTCP class.
     *
     * @param out is the buffered writer to wrap in the printer writer.
     */
    public ServerProxy(BufferedWriter out) {
        this.out = new PrintWriter(out);
    }


    /**
     * We register a client to a list of clients (VirtualView) the server keeps.
     *
     * @param client is the client that needs to be added
     */
    @Override
    public void connect(ClientData client) {}


    /**
     * Notifies the ClientHandler to call the respective method on ServerTCP.
     * Checks if the nickname chosen by the client is already in use (check is case-sensitive).
     *
     * @param nameToCheck is the name the client would like to use
     * @return true if the name is already in use, false otherwise
     */
    @Override
    public boolean checkName(String nameToCheck) {
        out.println("check name");
        out.println(nameToCheck);
        out.flush();
        return false;
    }


    /**
     * Notifies the ClientHandler to call the respective method on ServerTCP.
     * Checks if a command selected by the client is legal during this phase of their turn.
     *
     * @param idMatch  is the id which identifies the match the clint is playing
     * @param nickname is the name of the player who launched the command
     * @param command  is the command indicating the action the client is trying to perform
     * @return true if the command is legal, false otherwise
     */
    @Override
    public boolean checkCommand(int idMatch, String nickname, String command) {
        out.println("check command");
        out.println(command);
        out.flush();
        return false;
    }


    /**
     * Notifies the ClientHandler to call the respective method on ServerTCP.
     * Checks if the player with the specified nickname is present in the specified match.
     *
     * @param idMatch  indicates the match
     * @param nickname indicates the player to search
     * @return true if the player is present in the match
     */
    @Override
    public boolean checkPlayer(int idMatch, String nickname) {
        out.println("check player");
        out.println(nickname);
        out.flush();
        return false;
    }


    /**
     * Notifies the ClientHandler to call the respective method on ServerTCP.
     * Checks whether the client  inserted a valid recipient name for chatting.
     *
     * @param idMatch is the id which identifies the match the clint is playing
     * @param sender the nickname of the sender
     * @param recipient is the name the client typed
     * @return true if recipient is a valid name, false otherwise
     */
    @Override
    public boolean checkRecipient(int idMatch, String sender, String recipient) {
        out.println("check recipient");
        out.println(recipient);
        out.flush();
        return false;
    }


    /**
     * Notifies the ClientHandler to call the respective method on ServerTCP.
     * Certifies if there is an open match.
     *
     * @return true if there isn't an open match
     */
    @Override
    public boolean firstPlayer() {
        out.println("first");
        out.flush();
        return false;
    }


    /**
     * Notifies the ClientHandler to call the respective method on ServerTCP.
     * Initializes the number of players in the game.
     *
     * @param n The number of players declared by the client
     */
    @Override
    public void setNumbOfPlayers(int n) {
        out.println("set number of players");
        out.println(n);
        out.flush();
    }


    /**
     * Notifies the ClientHandler to call the respective method on ServerTCP.
     * Adds a new match to the game.
     *
     * @param name is the name of the player who created the new match
     * @return the ID of the newly created match
     */
    @Override
    public int addMatch(String name) {
        out.println("add match");
        out.println(name);
        out.flush();
        return 0;
    }


    /**
     * Notifies the ClientHandler to call the respective method on ServerTCP.
     * The method adds a Player in a match which has not yet started.
     *
     * @param name of the player
     * @return the ID related to the match
     */
    @Override
    public int addPlayer(String name) {
        out.println("add player");
        out.println(name);
        out.flush();
        return 0;
    }


    /**
     * Calls the respective method present in Table.
     * This method search for the nickname of every player in the current game.
     *
     * @param idMatch is the ID related to the match
     * @return a list containing all players' nicknames
     */
    @Override
    public List<String> getPlayersNames(int idMatch) {
        out.println("get names");
        out.flush();
        return null;
    }


    /**
     * Sends the update contained in a message to the respective client, by calling their method showUpdate.
     *
     * @param message is the message containing the necessary information to display
     */
    @Override
    public void sendUpdate(Message message) {}


    /**
     * Gets the table of a specified match.
     *
     * @param idMatch is the ID related to the match
     * @return the table related to the specified match
     */
    @Override
    public Table getTable(int idMatch) {
        out.println("get table");
        out.flush();
        return null;
    }


    /**
     * Getter method for the server's controller attribute.
     *
     * @return the instance of the server's controller
     */
    @Override
    public GameController getController() {
        out.println("get controller");
        out.flush();
        return null;
    }


    /**
     * Notifies the ClientHandler to call the respective method on ServerTCP.
     * Lets the player place a card on their play area.
     *
     * @param idMatch is the ID related to the match the player is currently playing
     * @param nickname is the name of the player who launched the command
     * @param cardSelected references to the index of the card which has been selected
     * @param faceSelected indicates of the card needs to be placed by the front or by the back
     * @param x is the first coordinate of the position where the player wants to place the card
     * @param y is the second coordinate of the position where the player wants to place the card
     */
    @Override
    public void place(int idMatch, String nickname, int cardSelected, String faceSelected, int x, int y) {
        out.println("place");
        out.println(cardSelected);
        out.println(faceSelected);
        out.println(x);
        out.println(y);
        out.flush();
    }


    /**
     * Notifies the ClientHandler to call the respective method on ServerTCP.
     * Gets the turn phase to a player playing via GUI.
     *
     * @param idMatch is the ID related to the match the player is currently playing
     * @return the phase to the GUI
     */
    @Override
    public String getPhaseGui(int idMatch) {
        out.println("get phase");
        out.flush();
        return "";
    }


    /**
     * Notifies the ClientHandler to call the respective method on ServerTCP.
     * Lets the player draw a card from the resource deck.
     *
     * @param idMatch  is the ID of the match the player is currently playing
     * @param nickname is the name of the player who wants to play the action
     */
    @Override
    public void drawResource(int idMatch, String nickname) {
        out.println("draw resource");
        out.flush();
    }


    /**
     * Notifies the ClientHandler to call the respective method on ServerTCP.
     * Lets the player draw a card from the gold deck.
     *
     * @param idMatch  is the ID of the match the player is currently playing
     * @param nickname is the name of the player who wants to play the action
     */
    @Override
    public void drawGold(int idMatch, String nickname) {
        out.println("draw gold");
        out.flush();
    }


    /**
     * Notifies the ClientHandler to call the respective method on ServerTCP.
     * Lets the player draw a card from the table.
     *
     * @param idMatch is the ID of the match the player is currently playing
     * @param nickname is the name of the player who wants to play the action
     * @param cardSelected references to the index of the card which has been selected from the table
     */
    @Override
    public void drawTable(int idMatch, String nickname, int cardSelected) {
        out.println("draw table");
        out.println(cardSelected);
        out.flush();
    }


    /**
     * Calls the respective method in VirtualServer.
     * Method used by a user playing via Gui to receive placed cards with their coordinates,
     * the cards in the array are sorted by placement order.
     *
     * @param idMatch  is the ID of the match the player is currently playing
     * @param nickname is the name of the player who placed the cards contained in the list
     * @return an array containing the cards placed with their coordinates in a system where
     * the starter card is in position (0, 0)
     */
    @Override
    public Card[] getOrderedCardsList(int idMatch, String nickname) {
        out.println("get card list");
        out.println(nickname);
        out.flush();
        return new Card[0];
    }


    /**
     * This method is called to get the points of a player by his nickname.
     *
     * @param idMatch  is the ID of the match the player is currently playing
     * @param nickname the nickname of the player
     * @return the points of the player
     */
    @Override
    public int getPlayerPoints(int idMatch, String nickname) {
        out.println("get points");
        out.println(nickname);
        out.flush();
        return 0;
    }


    /**
     * Notifies the ClientHandler to call the respective method on ServerTCP.
     * Notifies if some of the decks are empty.
     *
     * @param idMatch is the ID of the match the player is currently playing
     * @return 0 if everything is fine
     *         1 if the Resource deck is empty
     *         2 if the Gold deck is empty
     *         3 if both decks are empty
     *         4 if everything is empty
     */
    @Override
    public int whereCanIDraw(int idMatch) {
        out.println("where can draw");
        out.flush();
        return 0;
    }


    /**
     * Notifies the ClientHandler to call the respective method on ServerTCP.
     * Shows which positions on the table of a specific match have cards on them.
     *
     * @param idMatch is the ID of the match the player is currently playing
     * @return a list with all the positions of the table with a card on them
     */
    @Override
    public List<Integer> availablePositionsForDrawing(int idMatch) {
        out.println("available for drawing");
        out.flush();
        return new ArrayList<>();
    }


    /**
     * Notifies the ClientHandler to call the respective method on ServerTCP.
     * Sends a message to the specified recipient by chat.
     *
     * @param idMatch is the ID of the match the player is currently playing
     * @param sender is the name of the sender client
     * @param recipient is the name of the recipient of the message
     * @param chatMessage is the message typed by the sender
     */
    @Override
    public void chat(int idMatch, String sender, String recipient, String chatMessage) {
        out.println("chat");
        out.println(sender);
        out.println(recipient);
        out.println(chatMessage);
        out.flush();
    }


    /**
     * Getter method for the clients attribute.
     *
     * @return the list with all the registered clients
     */
    @Override
    public List<ClientData> getClients() {
        return null;
    }


    /**
     * Notifies the ClientHandler to call the respective method on ServerTCP.
     * Checks if the requested number of players joined the specified match.
     *
     * @param idMatch is the ID related to the specified match
     * @return true if every expected player is connected
     */
    @Override
    public boolean allConnected(int idMatch) {
        out.println("all connected");
        out.flush();
        return false;
    }


    /**
     * Notifies the ClientHandler to call the respective method on ServerTCP.
     * Checks if every player correctly placed their starter card.
     *
     * @param idMatch is the ID related to the specified match
     * @return true if every starter card has been placed, false otherwise
     */
    @Override
    public boolean allStarterCardsPlaced(int idMatch) {
        out.println("all starter cards placed");
        out.flush();
        return false;
    }


    /**
     * Notifies the ClientHandler to call the respective method on ServerTCP.
     * If a starter card has been placed, this method increases the number of placed starter cards for the current match.
     *
     * @param idMatch is the ID related to the specified match
     */
    @Override
    public void increaseNumOfStarterCardsPlaced(int idMatch) {
        out.println("increase starter cards");
        out.flush();
    }


    /**
     * Notifies the ClientHandler to call the respective method on ServerTCP.
     * This method shuffles decks on the table and provides each player with their starter card.
     *
     * @param idMatch is the ID related to the specified match
     */
    @Override
    public void shuffleDecksAndGiveStarterCards(int idMatch) {
        out.println("shuffle");
        out.flush();
    }


    /**
     * Notifies the ClientHandler to call the respective method on ServerTCP.
     * With this method every player draws 3 cards and common objective cards are placed
     *
     * @param idMatch is the current match
     */
    @Override
    public void drawCardsAndPlaceCommonObjectives(int idMatch) {
        out.println("draw cards and place objectives");
        out.flush();
    }


    /**
     * Notifies the ClientHandler to call the respective method on ServerTCP.
     * Show to a player how his resources will change after the placement of a card.
     *
     * @param idMatch is the ID relative to the specified match
     * @param nickname is the nickname of the player who is placing the card
     * @param cardSelected indicates the position of the card in the player's hand
     * @param faceSelected indicates the face of the card that the player has chosen
     * @param pos1 is the row where the card will be placed
     * @param pos2 is the column where the card will be placed
     */
    @Override
    public void viewDifferenceOfResources(int idMatch, String nickname, int cardSelected, String faceSelected, int pos1, int pos2) {
        out.println("view difference");
        out.println(cardSelected);
        out.println(faceSelected);
        out.println(pos1);
        out.println(pos2);
        out.flush();
    }


    /**
     * Notifies the ClientHandler to call the respective method on ServerTCP.
     * Displays to the player their hand.
     *
     * @param idMatch is the ID related to the specified match
     * @param nickname is the name of the player who made the request
     */
    @Override
    public void viewHand(int idMatch, String nickname) {
        out.println("view hand");
        out.flush();
    }


    /**
     * Notifies the ClientHandler to call the respective method on ServerTCP.
     * Displays to the player their hand and area.
     *
     * @param idMatch is the ID related to the specified match
     * @param nickname is the name of the player who made the request
     */
    public void viewHandAndArea(int idMatch, String nickname) {
        out.println("view hand and area");
        out.flush();
    }


    /**
     * Notifies the ClientHandler to call the respective method on ServerTCP.
     * Displays to the player the starting table.
     *
     * @param idMatch is the ID related to the specified match
     * @param nickname is the name of the player who made the request
     */
    @Override
    public void viewStartingTable(int idMatch, String nickname) {
        out.println("view starting table");
        out.flush();
    }


    /**
     * Notifies the ClientHandler to call the respective method on ServerTCP.
     * Displays to the player the table.
     *
     * @param idMatch is the ID related to the specified match
     * @param nickname is the name of the player who made the request
     */
    @Override
    public void viewTable(int idMatch, String nickname) {
        out.println("view table");
        out.flush();
    }


    /**
     * Notifies the ClientHandler to call the respective method on ServerTCP.
     * Place the 2 common objective cards on the table.
     *
     * @param idMatch is the ID related to the specified match
     */
    @Override
    public void giveObjectives(int idMatch) {
        out.println("give objectives");
        out.flush();
    }


    /**
     * Notifies the ClientHandler to call the respective method on ServerTCP.
     * Displays to the player their 2 options for their secret objectives.
     *
     * @param idMatch is the ID related to the specified match
     * @param nickname is the name of the player who made the request
     */
    @Override
    public void viewChoiceObjectives(int idMatch, String nickname) {
        out.println("view choice objectives");
        out.flush();
    }


    /**
     * Notifies the ClientHandler to call the respective method on ServerTCP.
     * Puts the chosen card on the table.
     *
     * @param idMatch is the ID related to the specified match
     * @param nickname is the name of the player who made the request
     * @param obSelected indicates which option the client selected
     */
    @Override
    public void putChoiceObjectives(int idMatch, String nickname, int obSelected) {
        out.println("put choice objectives");
        out.println(obSelected);
        out.flush();
    }


    /**
     * Notifies the ClientHandler to call the respective method on ServerTCP.
     * Calls the respective getter method present in MatchController.
     *
     * @param idMatch is the ID related to the specified match
     * @return an array of all the colors the player can choose from
     */
    @Override
    public Colors[] showAvailableColors(int idMatch) {
        out.println("show colors");
        out.flush();
        return new Colors[0];
    }


    /**
     * Notifies the ClientHandler to call the respective method on ServerTCP.
     * Checks wether it is the turn of a specified player.
     *
     * @param idMatch references to the ID of the specified match
     * @param nickname is the nickname of the player who is making the request
     */
    @Override
    public void itIsMyTurn(int idMatch, String nickname) {
        out.println("my turn");
        out.flush();
    }


    /**
     * Notifies the ClientHandler to call the respective method on ServerTCP.
     * Verifies if it is the turn of client who made the request to choose the color of their pawn.
     *
     * @param idMatch references to the ID of the specified match
     * @param nickname is the nickname of the player who is making the request
     * @return true if it is the turn of the player to choose the color
     */
    @Override
    public boolean canIChooseTheColor(int idMatch, String nickname) {
        out.println("can I choose the color");
        out.flush();
        return false;
    }


    /**
     * Notifies the ClientHandler to call the respective method on ServerTCP.
     * Checks if the color the client selected is valid.
     *
     * @param idMatch references to the ID of the specified match
     * @param colorChosen is the color the client selected
     * @return true if the client entered a valid color
     */
    @Override
    public boolean checkColor(int idMatch, Colors colorChosen) {
        out.println("check color");
        out.println(colorChosen.getValue());
        out.flush();
        return false;
    }


    /**
     * Notifies the ClientHandler to call the respective method on ServerTCP.
     * Removes the color from the array of the available colors and lets the next player choose their color.
     *
     * @param idMatch references to the ID of the specified match
     * @param nickname is the nickname of the player who is making the request
     * @param colorChosen is the color the client selected
     */
    @Override
    public void removeColorAndPassTurn(int idMatch, String nickname, Colors colorChosen) {
        out.println("remove color");
        out.println(colorChosen.getValue());
        out.flush();
    }


    /**
     * Notifies the ClientHandler to call the respective method on ServerTCP.
     * Calls the respective getter method present in MatchController.
     *
     * @param idMatch is the ID related to the current match
     */
    @Override
    public boolean isBlackPawnChosen(int idMatch) {
        out.println("is black pawn chosen");
        out.flush();
        return false;
    }


    /**
     * Notifies the ClientHandler to call the respective method on ServerTCP.
     * Calls the respective setter method present in MatchController.
     *
     * @param idMatch is the ID related to the current match
     * @param value is a boolean indicating the value to pass to the setter method as a parameter
     */
    @Override
    public void setBlackPawnChosen(int idMatch, boolean value) {
        out.println("set black pawn");
        out.println(value);
        out.flush();
    }


    /**
     * Notifies the ClientHandler to call the respective method on ServerTCP.
     * Randomly assigns the black pawn.
     *
     * @param idMatch is the ID related to the current match
     */
    @Override
    public void chooseTheBlackPawn(int idMatch) {
        out.println("choose black pawn");
        out.flush();
    }


    /**
     * Notifies the ClientHandler to call the respective method on ServerTCP.
     * Calls the respective getter method present in MatchController.
     *
     * @param idMatch is the ID related to the current match
     * @return the nickname of the first player of the match, the one owning the black pawn
     */
    @Override
    public String getNameOfThePlayerWithTheBlackPawn(int idMatch) {
        out.println("player with black pawn");
        out.flush();
        return null;
    }


    /**
     * Notifies the ClientHandler to call the respective method on ServerTCP.
     * Calls the respective setter method present in MatchController.
     *
     * @param idMatch is the ID related to the current match
     * @param nickname is the nickname of the player whose turn will be set
     * @param value is a boolean indicating the value to pass to the setter method as a parameter
     */
    @Override
    public void setYourTurn(int idMatch, String nickname, boolean value) {
        out.println("set turn");
        out.println(value);
        out.flush();
    }


    /**
     * Notifies the ClientHandler to call the respective method on ServerTCP.
     * Calls the respective getter method isYourTurn of the specified player.
     *
     * @param idMatch is the ID related to the current match
     * @param nickname is the nickname of the player who is trying to perform an action
     * @return true if it is the turn of the specified player
     */
    @Override
    public boolean checkTurn(int idMatch, String nickname) {
        out.println("check turn");
        out.flush();
        return false;
    }


    /**
     * Notifies the ClientHandler to call the respective method on ServerTCP.
     * The player with the specified nickname receives on display the play area.
     *
     * @param idMatch is the ID relative to the specified match
     * @param nickname is the nickname of the player who is requesting to view the area
     * @param areaOwner is the nickname of the player whose area is going to be displayed
     */
    @Override
    public void viewPlayArea(int idMatch, String nickname, String areaOwner) {
        out.println("view play area");
        out.println(areaOwner);
        out.flush();
    }


    /**
     * Notifies the ClientHandler to call the respective method on ServerTCP.
     * The player with the specified nickname receive on display their secret objective card.
     *
     * @param idMatch  is the ID relative to the specified match
     * @param nickname is the nickname of the player who is requesting to view the card
     */
    @Override
    public void viewSecretObjective(int idMatch, String nickname) {
        out.println("view secret");
        out.flush();
    }


    /**
     * Notifies the ClientHandler to call the respective method on ServerTCP.
     * Checks is the specified coordinates allow the player to place a card.
     *
     * @param idMatch is the ID relative to the specified match
     * @param nickname is the nickname of the layer who is requesting to place the card
     * @param x is the first coordinate (row) selected by the player
     * @param y is the first coordinate (row) selected by the player
     * @return true if a card can be placed on the specified coordinates, false otherwise
     */
    @Override
    public boolean areCoordinatesValid(int idMatch, String nickname, int x, int y) {
        out.println("are coordinates valid");
        out.println(x);
        out.println(y);
        out.flush();
        return false;
    }


    /**
     * Notifies the ClientHandler to call the respective method on ServerTCP.
     * Checks wether the requirements to place a gold card are met.
     *
     * @param idMatch is the ID relative to the specified match
     * @param nickname is the nickname of the layer who is requesting to place the gold card
     * @param cardSelected indicates the position of the gold card in the player's hand
     * @return true if the requirements are met, false otherwise
     */
    @Override
    public boolean canIPlaceTheGoldCard(int idMatch, String nickname, int cardSelected) {
        out.println("can place gold card");
        out.println(cardSelected);
        out.flush();
        return false;
    }


    /**
     * Notifies the ClientHandler to call the respective method on ServerTCP.
     * If a player chooses their secret objective, this method updates the number of chosen secret objectives for the specified match.
     *
     * @param idMatch is the ID related to the specified match
     */
    @Override
    public void increaseNumOfObjectiveCardChosen(int idMatch) {
        out.println("increase objective cards");
        out.flush();
    }


    /**
     * Notifies the ClientHandler to call the respective method on ServerTCP.
     * Checks if every player has chosen their secret objective.
     *
     * @param idMatch is the ID related to the specified match
     * @return true if every player has chosen their secret objective, false otherwise
     */
    @Override
    public boolean allObjectiveCardsChosen(int idMatch) {
        out.println("all objectives chosen");
        out.flush();
        return false;
    }


    /**
     * Notifies the ClientHandler to call the respective method on ServerTCP.
     * Checks if the specified match has reached the final phase of the game and wether this will be the final round.
     * If the final phase was triggered by the last player of the round, the last round begins,
     * otherwise, firstly the current round terminates, secondly the last round begins.
     *
     * @param idMatch is the ID related to the specified match
     * @param nickname is the nickname of the client/player
     * @return 0 if the conditions for the final phase are not satisfied, 1 if they are, 2 if the last round is starting
     */
    @Override
    public int checkSecondToLastTurn(int idMatch, String nickname) {
        out.println("check round");
        out.flush();
        return 0;
    }


    /**
     * Notifies the ClientHandler to call the respective method on ServerTCP.
     * Calls the respective setter method present in MatchController.
     *
     * @param idMatch is the ID related to the current match
     * @param value is a boolean indicating the value to pass to the setter method as a parameter
     */
    @Override
    public void setLastRound(int idMatch, boolean value) {
        out.println("set last round");
        out.println(value);
        out.flush();
    }


    /**
     * Notifies the ClientHandler to call the respective method on ServerTCP.
     * Calls the respective getter method present in MatchController.
     *
     * @param idMatch is the ID related to the current match
     * @return true if the match is in its last round
     */
    @Override
    public boolean isLastRound(int idMatch) {
        out.println("is last round");
        out.flush();
        return false;
    }


    /**
     * Notifies the ClientHandler to call the respective method on ServerTCP.
     * Calls the respective getter method present in MatchController.
     *
     * @param idMatch is the ID related to the current match
     * @return true if the match is in its second to last round
     */
    @Override
    public boolean isSecondToLastRound(int idMatch) {
        out.println("is second to last round");
        out.flush();
        return false;
    }


    /**
     * Notifies the ClientHandler to call the respective method on ServerTCP.
     * Calls the respective setter method present in MatchController.
     *
     * @param idMatch is the ID related to the current match
     * @param b is a boolean indicating the value to pass to the setter method as a parameter
     */
    @Override
    public void setSecondToLastRound(int idMatch, boolean b) {
        out.println("set second to last round");
        out.println(b);
        out.flush();
    }


    /**
     * Notifies the ClientHandler to call the respective method on ServerTCP.
     * Verify if the next player is the first player.
     *
     * @param idMatch is the ID related to the current match
     * @param nickname is the nickname of the client/player
     * @return true if the next player in the list Players present on the match table owns the black pawn
     */
    @Override
    public boolean isNextPlayerTheBlackPawn(int idMatch, String nickname) {
        out.println("is next player black pawn");
        out.flush();
        return false;
    }


    /**
     * Notifies the ClientHandler to call the respective method on ServerTCP.
     * Increase the number of players who have finished to play the match.
     *
     * @param idMatch is the ID related to the current match
     */
    @Override
    public void increaseNumOfPlayersThatHaveFinishedToPlay(int idMatch) {
        out.println("increase players that have finished");
        out.flush();
    }


    /**
     * Notifies the ClientHandler to call the respective method on ServerTCP.
     * Gets the number of players who have finished to play.
     *
     * @param idMatch is the ID related to the current match
     * @return the number of players who have finished to play
     */
    @Override
    public int getNumOfPlayersThatHaveFinishedToPlay(int idMatch) {
        out.println("get players that have finished");
        out.flush();
        return 0;
    }


    /**
     * Notifies the ClientHandler to call the respective method on ServerTCP.
     * Calls the respective getter method present in MatchController.
     *
     * @param idMatch is the ID related to the current match
     * @return the number of players chosen by the creator of the match
     */
    @Override
    public int getNumOfPlayers(int idMatch) {
        out.println("get number of players");
        out.flush();
        return 0;
    }


    /**
     * Notifies the ClientHandler to call the respective method on ServerTCP.
     * Calculates the points gained by the specified player from the common and the secret objectives.
     *
     * @param idMatch is the ID related to the current match
     * @param nickname is the nickname of the client/player
     */
    @Override
    public void calculateObjectives(int idMatch, String nickname) {
        out.println("calculate objectives");
        out.flush();
    }


    /**
     * Notifies the ClientHandler to call the respective method on ServerTCP.
     * Increases the number of players who have their final score calculated.
     *
     * @param idMatch is the ID related to the current match
     */
    @Override
    public void increaseNumOfObjectiveCardCalculated(int idMatch) {
        out.println("increase calculated objectives");
        out.flush();
    }


    /**
     * Notifies the ClientHandler to call the respective method on ServerTCP.
     * Verifies if every player in the specified match had their final score calculated.
     *
     * @param idMatch is the ID related to the current match
     * @return true if the score was calculated for every player
     */
    @Override
    public boolean allObjectiveCardsCalculated(int idMatch) {
        out.println("all objectives calculated");
        out.flush();
        return false;
    }


    /**
     * Notifies the ClientHandler to call the respective method on ServerTCP.
     * Decides who is the winner of the specified match.
     *
     * @param idMatch is the ID related to the current match
     */
    @Override
    public void calculateWinner(int idMatch) {
        out.println("calculate winner");
        out.flush();
    }


    /**
     * Notifies the ClientHandler to call the respective method on ServerTCP.
     * Displays a "you won" or a "you lost" message to every player.
     *
     * @param idMatch is the ID related to the current match
     * @param nickname is the nickname of the specified player
     */
    @Override
    public void showEndGameMessage(int idMatch, String nickname) {
        out.println("endgame message");
        out.flush();
    }


    /**
     * Calls the respective method present in MatchController.
     * Method used by the GUI to know if the player is the winner.
     *
     * @param idMatch  is the ID related to the current match
     * @param nickname is the nickname of the specified player
     * @return true if the player is the winner, false otherwise
     */
    @Override
    public boolean endGameMessageGui(int idMatch, String nickname) {
        out.println("endgame message gui");
        out.flush();
        return false;
    }


    /**
     * Notifies the ClientHandler to call the respective method on ServerTCP.
     * This cheat provides the specified player with 10000 resources per each symbol.
     *
     * @param idMatch is the ID related to the current match
     * @param nickname is the nickname of the specified player
     */
    @Override
    public void cheat1(int idMatch, String nickname) {
        out.println("cheat 1");
        out.flush();
    }


    /**
     * Notifies the ClientHandler to call the respective method on ServerTCP.
     * This cheat provides the specified player with 20 points.
     *
     * @param idMatch is the ID related to the current match
     * @param nickname is the nickname of the specified player
     */
    @Override
    public void cheat2(int idMatch, String nickname) {
        out.println("cheat 2");
        out.flush();
    }


    /**
     * Notifies the ClientHandler to call the respective method on ServerTCP.
     * With this cheat both resource deck and gold deck become empty.
     *
     * @param idMatch is the ID related to the current match
     */
    @Override
    public void cheat3(int idMatch) {
        out.println("cheat 3");
        out.flush();
    }


    /**
     * Notifies the ClientHandler to call the respective method on ServerTCP.
     * Receives a pong answer from a client and registers the time.
     *
     * @param nickname is the nickname of the specified client
     */
    public void receivePong(String nickname) {
        out.println("receive pong");
        out.println(nickname);
        out.flush();
    }


    /**
     * Notifies the ClientHandler to call the respective method on ServerTCP.
     * Removes the specified client from the clients list present on the server.
     *
     * @param nickname is the nickname of the specified player
     */
    @Override
    public void removeClient(String nickname) {
        out.println("remove client");
        out.flush();
    }
}
