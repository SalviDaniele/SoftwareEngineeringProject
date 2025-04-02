package Network.TCP;

import Controller.GameController;
import Listeners.Message;
import Model.Cards.Card;
import Model.Cards.Colors;
import Network.Chat;
import Model.Table;
import Network.VirtualServer;
import Network.VirtualView;

import java.io.*;
import java.rmi.RemoteException;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * This class firstly reads the output of the ServerProxy and calls the respective method on ServerTCP.
 * Secondly, the ClientHandler calls methods on ClientProxy in order to send messages back to the ClientTCP.
 */
public class ClientHandler implements VirtualView {

    /**
     * The nickname of the client related to this handler.
     */
    private String nickname;

    /**
     * The match ID of the match the client related to this handler is playing.
     */
    private int idMatch;

    /**
     * A reference to the server.
     */
    private final ServerTCP server;

    /**
     * Reader where to read the incoming strings from the ServerProxy.
     */
    private final BufferedReader input;

    /**
     * A reference to the ClientProxy, which sends messages and updates back to the ClientTCP.
     */
    private final ClientProxy clientProxy;


    /**
     * Constructor for the ClientHandler class.
     *
     * @param server is a reference to the ServerTCP
     * @param input where to read the incoming strings from the ServerProxy.
     * @param output wrapped in ClientProxy, to send updates back to the client
     */
    public ClientHandler(ServerTCP server, BufferedReader input, BufferedWriter output) {
        this.server = server;
        this.input = input;
        this.clientProxy = new ClientProxy(output);
    }

    /**
     * check is a player has the blackPawn
     * @return always false
     * @throws RemoteException if a network related error occurs
     */
    @Override
    public boolean isBlackPawn() throws RemoteException{
        return false;
    }

    /**
     * setter method for te attribute 'blackPawn'
     * @param blackPawn is the boolean to be put
     * @throws RemoteException if a network related error occurs
     */
    @Override
    public void setBlackPawn(boolean blackPawn) throws RemoteException{}


    /**
     * Calls the respective getter method present in VirtualServer.
     *
     * @return true if the black pawn has been assigned
     */
    @Override
    public boolean isBlackPawnChosen() {
        return server.isBlackPawnChosen(idMatch);
    }


    /**
     * Getter method for the server attribute.
     *
     * @return a reference to the ServerTCP, which implements the VirtualServer interface
     */
    @Override
    public VirtualServer getServer() {
        return this.server;
    }


    /**
     * Getter method for the nickname attribute.
     *
     * @return the nickname of the client related to this handler
     */
    public String getNickname() {
        return this.nickname;
    }


    /**
     * Setter method for the nickname attribute
     *
     * @param nickname the player's name
     */
    @Override
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }


    /**
     * Getter method for the first attribute.
     *
     * @return if the client is the first to join a match
     */
    @Override
    public boolean isFirst() {
        return false;
    }


    /**
     * setter method for the attribute 'first'
     * @param first is true if the client is the first to join a match, false otherwise
     */
    @Override
    public void setFirst(boolean first) {}


    /**
     * setter method for the attribute 'idMatch'
     * @param i is the match ID
     */
    @Override
    public void setIdMatch(int i) {
        this.idMatch = i;
    }


    /**
     * getter method for the attribute 'idMatch'
     * @return the attribute 'idMatch'
     */
    public int getIdMatch() {
        return idMatch;
    }

    /**
     * Setter method for the color attribute.
     *
     * @param colors the client's color
     */
    @Override
    public void setColor(Colors colors) {}


    /**
     * Getter method for the color attribute.
     *
     * @return the client's color
     */
    @Override
    public Colors getColor() {
        return null;
    }

    /**
     * Getter method for the chat attribute.
     *
     * @return chat instance for that player
     * @throws RemoteException in case of network errors
     */
    @Override
    public Chat getChat() throws RemoteException {
        return null;
    }

    /**
     * Setter method for the chat attribute.
     *
     * @param chat of the player
     * @throws RemoteException in case of network errors
     */
    @Override
    public void setChat(Chat chat) throws RemoteException {

    }

    /**
     * Getter method for the viewChoice attribute.
     *
     * @return true if the user chose to play via Gui, false otherwise
     * @throws RemoteException in case of network errors
     */
    @Override
    public boolean getViewChoice() throws RemoteException {
        return false;
    }


    /**
     * a method used for connecting a client that is using the GUI protocol
     * @param ip the client is going to connect to
     * @param i the client is going to connect to
     */
    @Override
    public void connectGui(String ip, int i) {}


    /**
     * Calls the respective method on VirtualServer.
     * The server adds the client to a list.
     */
    @Override
    public void connect() {}


    /**
     * Calls the respective method in VirtualServer.
     * Sends a message to the specified recipient by chat.
     * This message is sent by a player playing via Gui.
     *
     * @param recipient   is the name of the recipient of the message
     * @param chatMessage is the message typed by the sender
     * @throws RemoteException in case of network errors
     */
    @Override
    public void sendMessageChatGui(String recipient, String chatMessage) throws RemoteException {

    }


    /**
     * Call the method on the server that places the card in the play area, the parameter values are selected by the gui
     *
     * @param cardSelected references to the index of the card which has been selected
     * @param faceSelected indicates of the card needs to be placed by the front or by the back
     * @param x is the first coordinate of the position where the player wants to place the card
     * @param y is the second coordinate of the position where the player wants to place the card
     */
    @Override
    public void placeGui(int cardSelected, String faceSelected, int x, int y) {}


    /**
     * Calls the respective method present on VirtualServer.
     * Gets the turn phase to a player playing via GUI.
     *
     * @return the phase to the GUI
     */
    @Override
    public String getPhaseGui() {
        return server.getPhaseGui(idMatch);
    }


    /**
     * Calls the respective method on VirtualServer.
     * Calls the respective getter method isYourTurn of the specified player.
     *
     * @return true if it is the turn of the specified player
     */
    @Override
    public boolean checkTurn() {
        return server.checkTurn(idMatch, nickname);
    }


    /**
     * with this method a client using the GUI protocol can choose his name
     * @param name is the entered name
     * @return always false
     */
    @Override
    public boolean chooseNameGui(String name) {
        return false;
    }


    /**
     * Checks if the conditions to proceed to the last phase of the game are met, this method is used to let the GUI know when the last turn will be.
     * @return 0 if the completed round was the last, 1 if it was the second to last, 2 otherwise
     * @throws RemoteException in case of network errors
     */
    @Override
    public int checkEndPhaseGui() throws RemoteException {
        return 0;
    }


    /**
     * Calls the method checkTurn from VirtualServer.
     * Sets the specified player's turn as true and every other player's turn to false.
     */
    @Override
    public void itIsMyTurn() throws RemoteException {

    }


    /**
     * Calls the method checkTurn from VirtualServer.
     * Calls the setter method for the attribute 'yourTurn'.
     *
     * @param value is the boolean to be put
     */
    @Override
    public void setYourTurn(boolean value) {

    }


    /**
     * Calls the respective getter method present in ServerTCP.
     *
     * @return the nickname of the first player of the match, the one owning the black pawn
     */
    @Override
    public String getNameOfThePlayerWithTheBlackPawn() {
        return server.getNameOfThePlayerWithTheBlackPawn(idMatch);
    }


    /**
     * Calls the respective getter method present in VirtualServer.
     * Randomly assigns the black pawn.
     */
    @Override
    public void chooseTheBlackPawn() {}


    /**
     * Calls the respective setter method present in VirtualServer.
     *
     * @param value true if the player is assigned with the black pawn
     */
    @Override
    public void setBlackPawnChosen(boolean value) {}


    /**
     * Calls the respective drawResource method present on VirtualServer.
     * Lets the player draw a card from the resource deck.
     */
    @Override
    public void drawResource() {}


    /**
     * Calls the respective drawGold method present on VirtualServer.
     * Lets the player draw a card from the gold deck.
     */
    @Override
    public void drawGold() {}


    /**
     * Calls the respective drawTable method present on VirtualServer.
     * Lets the player draw a card from the table.
     *
     * @param cardSelected references to the index of the card which has been selected from the table
     */
    @Override
    public void drawTable(int cardSelected) {}


    /**
     * Calls the respective method in VirtualServer.
     * Method used by a user playing via Gui to receive placed cards and their coordinates,
     * the cards in the array are sorted by placement order.
     *
     * @param nickname is the name of the player who placed the cards contained in the array
     * @return an array containing the cards placed and their coordinates in a system where
     * the starter card is in position (0, 0)
     */
    @Override
    public Card[] getOrderedCardsList(String nickname) {
        return server.getOrderedCardsList(idMatch, nickname);
    }


    /**
     * Calls the respective method in VirtualServer.
     * This method is called to get the points of a player by his nickname.
     *
     * @param nickname the nickname of the player
     * @return the points of the player
     */
    @Override
    public int getPlayerPoints(String nickname) {
        return server.getPlayerPoints(idMatch, nickname);
    }


    /**
     * Calls the respective method present in VirtualServer.
     * This method search for the nickname of every player in the current game.
     *
     * @return a list containing all players' nicknames
     */
    @Override
    public List<String> getPlayersNames() {
        return server.getPlayersNames(idMatch);
    }


    /**
     * Runs a loop which listens for everything written on the input and calls the respective method on ServerTCP.
     *
     * @throws IOException in case of input/output errors
     */
    public void runVirtualView() throws IOException {
        String line;
        // Read message type
        while ((line = input.readLine()) != null) {
            // Read message and perform action
            try {
                switch (line) {
                    case "first" -> {
                        boolean isFirstPlayer = firstPlayer();
                        notifyOutcome(MessageType.BOOLEAN, isFirstPlayer);
                    }
                    case "set number of players" -> server.setNumbOfPlayers(Integer.parseInt(input.readLine()));
                    case "get number of players" -> {
                        int numOfPlayers = getNumOfPlayers(idMatch);
                        notifyValue(MessageType.INTEGER, numOfPlayers);
                    }
                    case "check name" -> {
                        boolean alreadyInUse = checkName(input.readLine());
                        notifyOutcome(MessageType.BOOLEAN, alreadyInUse);
                    }
                    case "add match" -> {
                        nickname = input.readLine();
                        idMatch = addMatch(nickname);
                        notifyValue(MessageType.INTEGER, idMatch);
                    }
                    case "add player" -> {
                        nickname = input.readLine();
                        idMatch = addPlayer(nickname);
                        notifyValue(MessageType.INTEGER, idMatch);
                    }
                    case "get names" -> {
                        List<String> names = getPlayersNames();
                        notifyNamesList(MessageType.NAMES, names);
                    }
                    case "get points" -> {
                        int points = getPlayerPoints(input.readLine());
                        notifyValue(MessageType.INTEGER, points);
                    }
                    case "get card list" -> {
                        Card[] cardList = getOrderedCardsList(input.readLine());
                        notifyCardsList(MessageType.CARDS, cardList);
                    }
                    case "all connected" -> {
                        boolean allConnected = allConnected(idMatch);
                        notifyOutcome(MessageType.BOOLEAN, allConnected);
                    }
                    case "my turn" -> server.itIsMyTurn(idMatch, nickname);
                    case "set turn" -> {
                        boolean yourTurn = Boolean.parseBoolean(input.readLine());
                        server.setYourTurn(idMatch, nickname, yourTurn);
                    }
                    case "check turn" -> {
                        boolean isMyTurn = checkTurn();
                        notifyOutcome(MessageType.BOOLEAN, isMyTurn);
                    }
                    case "can I choose the color" -> {
                        boolean canChoose = canIChooseTheColor(idMatch, nickname);
                        notifyOutcome(MessageType.BOOLEAN, canChoose);
                    }
                    case "show colors" -> {
                        Colors[] availableColors = showAvailableColors(idMatch);
                        notifyColors(MessageType.COLORS, availableColors);
                    }
                    case "check color" -> {
                        Colors colorToCheck = Colors.valueOf(input.readLine());
                        boolean colorValid = checkColor(idMatch, colorToCheck);
                        notifyOutcome(MessageType.BOOLEAN, colorValid);
                    }
                    case "remove color" -> {
                        Colors color = Colors.valueOf(input.readLine());
                        server.removeColorAndPassTurn(idMatch, nickname, color);
                    }
                    case "draw cards and place objectives" -> server.drawCardsAndPlaceCommonObjectives(idMatch);
                    case "give objectives" -> server.giveObjectives(idMatch);
                    case "shuffle" -> server.shuffleDecksAndGiveStarterCards(idMatch);
                    case "choose black pawn" -> server.chooseTheBlackPawn(idMatch);
                    case "set black pawn" -> {
                        boolean value = Boolean.parseBoolean(input.readLine());
                        server.setBlackPawnChosen(idMatch, value);
                    }
                    case "is black pawn chosen" -> {
                        boolean blackPawnAssigned = isBlackPawnChosen();
                        notifyOutcome(MessageType.BOOLEAN, blackPawnAssigned);
                    }
                    case "player with black pawn" -> {
                        String playerWithBlackPawn = getNameOfThePlayerWithTheBlackPawn();
                        notifyString(MessageType.STRING, playerWithBlackPawn);
                    }
                    case "get phase" -> {
                        String phase = getPhaseGui();
                        notifyString(MessageType.STRING, phase);
                    }
                    case "view starting table" -> server.viewStartingTable(idMatch, nickname);
                    case "view table" -> server.viewTable(idMatch, nickname);
                    case "view hand" -> server.viewHand(idMatch, nickname);
                    case "view hand and area" -> server.viewHandAndArea(idMatch, nickname);
                    case "view play area" -> {
                        String areaOwner = input.readLine();
                        server.viewPlayArea(idMatch, nickname, areaOwner);
                    }
                    case "view difference" -> {
                        int cardSelected = Integer.parseInt(input.readLine());
                        String faceSelected = input.readLine();
                        int pos1 = Integer.parseInt(input.readLine());
                        int pos2 = Integer.parseInt(input.readLine());
                        server.viewDifferenceOfResources(idMatch, nickname, cardSelected, faceSelected, pos1, pos2);
                    }
                    case "view secret" -> server.viewSecretObjective(idMatch, nickname);
                    case "view choice objectives" -> server.viewChoiceObjectives(idMatch, nickname);
                    case "put choice objectives" -> {
                        int obSelected = Integer.parseInt(input.readLine());
                        server.putChoiceObjectives(idMatch, nickname, obSelected);
                    }
                    case "increase objective cards" -> server.increaseNumOfObjectiveCardChosen(idMatch);
                    case "increase starter cards" -> server.increaseNumOfStarterCardsPlaced(idMatch);
                    case "all objectives chosen" -> {
                        boolean allChosen = allObjectiveCardsChosen(idMatch);
                        notifyOutcome(MessageType.BOOLEAN, allChosen);
                    }
                    case "all starter cards placed" -> {
                        boolean allPlaced = allStarterCardsPlaced(idMatch);
                        notifyOutcome(MessageType.BOOLEAN, allPlaced);
                    }
                    case "check command" -> {
                        String command = input.readLine();
                        boolean isValid = checkCommand(idMatch, nickname, command);
                        notifyOutcome(MessageType.BOOLEAN, isValid);
                    }
                    case "check player" -> {
                        boolean isPresent = checkPlayer(idMatch, input.readLine());
                        notifyOutcome(MessageType.BOOLEAN, isPresent);
                    }
                    case "get table" -> {
                        Table table = getTable(idMatch);
                        notifyTable(MessageType.TABLE, table);
                    }
                    case "get controller" -> {
                        GameController controller = getController();
                        notifyController(MessageType.CONTROLLER, controller);
                    }
                    case "place" -> {
                        int cardSelected = Integer.parseInt(input.readLine());
                        String faceSelected = input.readLine();
                        int x = Integer.parseInt(input.readLine());
                        int y = Integer.parseInt(input.readLine());
                        server.place(idMatch, nickname, cardSelected, faceSelected, x, y);
                    }
                    case "are coordinates valid" -> {
                        int x = Integer.parseInt(input.readLine());
                        int y = Integer.parseInt(input.readLine());
                        boolean valid = areCoordinatesValid(idMatch, nickname, x, y);
                        notifyOutcome(MessageType.BOOLEAN, valid);
                    }
                    case "can place gold card" -> {
                        int cardSelected = Integer.parseInt(input.readLine());
                        boolean requirementsOK = canIPlaceTheGoldCard(idMatch, nickname, cardSelected);
                        notifyOutcome(MessageType.BOOLEAN, requirementsOK);
                    }
                    case "draw resource" -> server.drawResource(idMatch, nickname);
                    case "draw gold" -> server.drawGold(idMatch, nickname);
                    case "draw table" -> {
                        int cardSelected = Integer.parseInt(input.readLine());
                        server.drawTable(idMatch, nickname, cardSelected);
                    }
                    case "where can draw" -> {
                        int result = whereCanIDraw();
                        notifyValue(MessageType.INTEGER, result);
                    }
                    case "available for drawing" -> {
                        List<Integer> positions = availablePositionsForDrawing();
                        notifyPositionList(MessageType.POSITIONS, positions);
                    }
                    case "chat" -> {
                        String sender = input.readLine();
                        String recipient = input.readLine();
                        String chatMessage = input.readLine();
                        server.chat(idMatch, sender, recipient, chatMessage);
                    }
                    case "check recipient" -> {
                        String recipient = input.readLine();
                        boolean recipientValid = checkRecipient(idMatch, recipient);
                        notifyOutcome(MessageType.BOOLEAN, recipientValid);
                    }
                    case "cheat 1" -> server.cheat1(idMatch, nickname);
                    case "cheat 2" -> server.cheat2(idMatch, nickname);
                    case "cheat 3" -> server.cheat3(idMatch);
                    case "is second to last round" -> {
                        boolean isSecondToLastRound = isSecondToLastRound(idMatch);
                        notifyOutcome(MessageType.BOOLEAN, isSecondToLastRound);
                    }
                    case "set second to last round" -> {
                        boolean value = Boolean.parseBoolean(input.readLine());
                        server.setSecondToLastRound(idMatch, value);
                    }
                    case "is last round" -> {
                        boolean isLastRound = isLastRound(idMatch);
                        notifyOutcome(MessageType.BOOLEAN, isLastRound);
                    }
                    case "set last round" -> {
                        boolean value = Boolean.parseBoolean(input.readLine());
                        server.setLastRound(idMatch, value);
                    }
                    case "check round" -> {
                        int roundIndicator = checkSecondToLastTurn(idMatch, nickname);
                        notifyValue(MessageType.INTEGER, roundIndicator);
                    }
                    case "is next player black pawn" -> {
                        boolean isBlackPawnNext = isNextPlayerTheBlackPawn(idMatch, nickname);
                        notifyOutcome(MessageType.BOOLEAN, isBlackPawnNext);
                    }
                    case "increase players that have finished" -> server.increaseNumOfPlayersThatHaveFinishedToPlay(idMatch);
                    case "get players that have finished" -> {
                        int players = getNumOfPlayersThatHaveFinishedToPlay(idMatch);
                        notifyValue(MessageType.INTEGER, players);
                    }
                    case "calculate objectives" -> server.calculateObjectives(idMatch, nickname);
                    case "increase calculated objectives" -> server.increaseNumOfObjectiveCardCalculated(idMatch);
                    case "all objectives calculated" -> {
                        boolean allCalculated = allObjectiveCardsCalculated(idMatch);
                        notifyOutcome(MessageType.BOOLEAN, allCalculated);
                    }
                    case "calculate winner" -> server.calculateWinner(idMatch);
                    case "endgame message" -> server.showEndGameMessage(idMatch, nickname);
                    case "endgame message gui" -> {
                        boolean isWinner = endGameMessageGui();
                        notifyOutcome(MessageType.BOOLEAN, isWinner);
                    }
                    case "receive pong" -> server.receivePong(input.readLine());
                    case "remove client" -> server.removeClient(nickname);
                    default -> System.err.println("[INVALID MESSAGE]: " + line);
                }
            } catch (IOException | IllegalArgumentException e) {
                System.err.println("\nError while reading from input: " + e.getMessage());
            }
        }
    }


    /**
     * Calls the respective method on VirtualServer.
     * Checks if the nickname chosen by the client is already in use (check is case-sensitive).
     *
     * @param nameToCheck is the name the client would like to use
     * @return true if the name is already in use, false otherwise
     */
    @Override
    public boolean checkName(String nameToCheck) {
        return server.checkName(nameToCheck);
    }


    /**
     * Calls the respective method on VirtualServer.
     * Checks if a command selected by the client is legal during this phase of their turn.
     *
     * @param idMatch  is the id which identifies the match the clint is playing
     * @param nickname is the name of the player who launched the command
     * @param command  is the command indicating the action the client is trying to perform
     * @return true if the command is legal, false otherwise
     */
    @Override
    public boolean checkCommand(int idMatch, String nickname, String command) {
        return server.checkCommand(idMatch, nickname, command);
    }


    /**
     * Calls the respective method on VirtualServer.
     * Checks if the player with the specified nickname is present in the specified match.
     *
     * @param idMatch  indicates the match
     * @param nickname indicates the player to search
     * @return true if the player is present in the match
     */
    @Override
    public boolean checkPlayer(int idMatch, String nickname) {
        return server.checkPlayer(idMatch, nickname);
    }


    /**
     * Calls the respective method on VirtualServer.
     * Checks whether the client  inserted a valid recipient name for chatting.
     *
     * @param idMatch   is the id which identifies the match the clint is playing
     * @param recipient is the name the client typed
     * @return true if recipient is a valid name, false otherwise
     */
    @Override
    public boolean checkRecipient(int idMatch, String recipient) {
        return server.checkRecipient(idMatch, nickname, recipient);
    }


    /**
     * Calls the respective method on VirtualServer.
     * Getter method for the server's controller attribute.
     *
     * @return the instance of the server's controller
     */
    @Override
    public GameController getController() {
        return server.getController();
    }


    /**
     * Calls the respective method on VirtualServer.
     * Certifies if there is an open match.
     *
     * @return true if there isn't an open match
     */
    @Override
    public boolean firstPlayer() {
        return server.firstPlayer();
    }


    /**
     * Calls the respective method on VirtualServer.
     * Adds a new match to the game.
     *
     * @param name is the name of the player who created the new match
     * @return the ID of the newly created match
     */
    @Override
    public int addMatch(String name) {
        return server.addMatch(name);
    }


    /**
     * Calls the respective method on VirtualServer.
     * The method adds a Player in a match which has not yet started.
     *
     * @param name of the player
     * @return the ID related to the match
     */
    @Override
    public int addPlayer(String name) {
        return server.addPlayer(name);
    }


    /**
     * Calls the respective method on VirtualServer.
     * Gets the table of a specified match.
     *
     * @param idMatch is the ID related to the match
     * @return the table related to the specified match
     */
    @Override
    public Table getTable(int idMatch) {
        return server.getTable(idMatch);
    }


    /**
     * Calls the respective method on VirtualServer.
     * Notifies if some of the decks are empty.
     *
     * @return 0 if everything is fine
     * 1 if the Resource deck is empty
     * 2 if the Gold deck is empty
     * 3 if both decks are empty
     * 4 if everything is empty
     */
    @Override
    public int whereCanIDraw() {
        return server.whereCanIDraw(idMatch);
    }


    /**
     * Calls the respective method on VirtualServer.
     * Shows which positions on the table of a specific match have cards on them.
     *
     * @return a list with all the positions of the table with a card on them
     */
    @Override
    public List<Integer> availablePositionsForDrawing() {
        return server.availablePositionsForDrawing(idMatch);
    }


    /**
     * Calls the respective method on VirtualServer.
     * Checks if the requested number of players joined the specified match.
     *
     * @param idMatch is the ID related to the specified match
     * @return true if every expected player is connected
     */
    @Override
    public boolean allConnected(int idMatch) {
        return server.allConnected(idMatch);
    }


    /**
     * Calls the respective method on VirtualServer.
     * Checks if every player correctly placed their starter card.
     *
     * @param idMatch is the ID related to the specified match
     * @return true if every starter card has been placed, false otherwise
     */
    @Override
    public boolean allStarterCardsPlaced(int idMatch) {
        return server.allStarterCardsPlaced(idMatch);
    }


    /**
     * Calls the respective getter method on VirtualServer.
     *
     * @param idMatch is the ID related to the specified match
     * @return an array of all the colors the player can choose from
     */
    @Override
    public Colors[] showAvailableColors(int idMatch) {
        return server.showAvailableColors(idMatch);
    }


    /**
     * Calls the respective method on VirtualServer.
     * Verifies if it is the turn of client who made the request to choose the color of their pawn.
     *
     * @param idMatch  references to the ID of the specified match
     * @param nickname is the nickname of the player who is making the request
     * @return true if it is the turn of the player to choose the color
     */
    @Override
    public boolean canIChooseTheColor(int idMatch, String nickname) {
        return server.canIChooseTheColor(idMatch, nickname);
    }


    /**
     * Calls the respective method on VirtualServer.
     * Checks if the color the client selected is valid.
     *
     * @param idMatch     references to the ID of the specified match
     * @param colorChosen is the color the client selected
     * @return true if the client entered a valid color
     */
    @Override
    public boolean checkColor(int idMatch, Colors colorChosen) {
        return server.checkColor(idMatch, colorChosen);
    }


    /**
     * Calls the respective method on VirtualServer.
     * Checks is the specified coordinates allow the player to place a card.
     *
     * @param idMatch  is the ID relative to the specified match
     * @param nickname is the nickname of the player who is requesting to place the card
     * @param x        is the first coordinate (row) selected by the player
     * @param y        is the first coordinate (row) selected by the player
     * @return true if a card can be placed on the specified coordinates, false otherwise
     */
    @Override
    public boolean areCoordinatesValid(int idMatch, String nickname, int x, int y) {
        return server.areCoordinatesValid(idMatch, nickname, x, y);
    }


    /**
     * Calls the respective method on VirtualServer.
     * Checks if the requirements to place a gold card are met.
     *
     * @param idMatch      is the ID relative to the specified match
     * @param nickname     is the nickname of the layer who is requesting to place the gold card
     * @param cardSelected indicates the position of the gold card in the player's hand
     * @return true if the requirements are met, false otherwise
     */
    @Override
    public boolean canIPlaceTheGoldCard(int idMatch, String nickname, int cardSelected) {
        return server.canIPlaceTheGoldCard(idMatch, nickname, cardSelected);
    }


    /**
     * Calls the respective method on VirtualServer.
     * Checks if every player has chosen their secret objective.
     *
     * @param idMatch is the ID related to the specified match
     * @return true if every player has chosen their secret objective, false otherwise
     */
    @Override
    public boolean allObjectiveCardsChosen(int idMatch) {
        return server.allObjectiveCardsChosen(idMatch);
    }


    /**
     * Calls the respective method on VirtualServer.
     * Checks if the specified match has reached the final phase of the game and wether this will be the final round.
     * If the final phase was triggered by the last player of the round, the last round begins,
     * otherwise, firstly the current round terminates, secondly the last round begins.
     *
     * @param idMatch  is the ID related to the specified match
     * @param nickname is the nickname of the client/player
     * @return 0 if the conditions for the final phase are not satisfied, 1 if they are, 2 if the last round is starting
     */
    @Override
    public int checkSecondToLastTurn(int idMatch, String nickname) {
        return server.checkSecondToLastTurn(idMatch, nickname);
    }


    /**
     * Calls the respective getter method on VirtualServer.
     * Calls the respective getter method present in MatchController.
     *
     * @param idMatch is the ID related to the current match
     * @return true if the match is in its last round
     */
    @Override
    public boolean isLastRound(int idMatch) {
        return server.isLastRound(idMatch);
    }


    /**
     * Calls the respective getter method on VirtualServer.
     *
     * @param idMatch is the ID related to the current match
     * @return true if the match is in its second to last round
     */
    @Override
    public boolean isSecondToLastRound(int idMatch) {
        return server.isSecondToLastRound(idMatch);
    }


    /**
     * Calls the respective method on VirtualServer.
     * Verify if the next player is the first player.
     *
     * @param idMatch  is the ID related to the current match
     * @param nickname is the nickname of the client/player
     * @return true if the next player in the list Players present on the match table owns the black pawn
     */
    @Override
    public boolean isNextPlayerTheBlackPawn(int idMatch, String nickname) {
        return server.isNextPlayerTheBlackPawn(idMatch, nickname);
    }


    /**
     * Calls the respective method on VirtualServer.
     * Gets the number of players who have finished to play.
     *
     * @param idMatch is the ID related to the current match
     * @return the number of players who have finished to play
     */
    @Override
    public int getNumOfPlayersThatHaveFinishedToPlay(int idMatch) {
        return server.getNumOfPlayersThatHaveFinishedToPlay(idMatch);
    }


    /**
     * Calls the respective getter method on VirtualServer.
     *
     * @param idMatch is the ID related to the current match
     * @return the number of players chosen by the creator of the match
     */
    @Override
    public int getNumOfPlayers(int idMatch) {
        return server.getNumOfPlayers(idMatch);
    }


    /**
     * Calls the respective method on VirtualServer.
     * Verifies if every player in the specified match had their final score calculated.
     *
     * @param idMatch is the ID related to the current match
     * @return true if the score was calculated for every player
     */
    @Override
    public boolean allObjectiveCardsCalculated(int idMatch) {
        return server.allObjectiveCardsCalculated(idMatch);
    }


    /**
     * Calls the respective method present in VirtualServer.
     * Method used by the GUI to know if the player is the winner.
     *
     * @return true if the player is the winner, false otherwise
     */
    @Override
    public boolean endGameMessageGui() {
        return server.endGameMessageGui(idMatch, nickname);
    }


    /**
     * Generates an update message and notifies the client through the client proxy.
     *
     * @param update contains the necessary data to display the update
     */
    @Override
    public void showUpdate(Message update) {
        MessageTCP message = new MessageTCP(MessageType.UPDATE, update);
        synchronized (this.clientProxy) {
            this.clientProxy.notifyUpdate(message);
        }
    }


    /**
     * Generates a message containing a boolean and notifies the client through the client proxy.
     *
     * @param header represent the message specific type
     * @param outcome is the boolean signaling the answer to the client's request
     */
    public void notifyOutcome(MessageType header, boolean outcome) {
        MessageTCP message = new MessageTCP(header, outcome);
        synchronized (this.clientProxy) {
            this.clientProxy.notifyOutcome(message);
        }
    }


    /**
     * Generates a message containing an integer and notifies the client through the client proxy.
     *
     * @param header represent the message specific type
     * @param value is the integer signaling the answer to the client's request
     */
    public void notifyValue(MessageType header, int value) {
        MessageTCP message = new MessageTCP(header, value);
        synchronized (this.clientProxy) {
            this.clientProxy.notifyValue(message);
        }
    }


    /**
     * Generates a message containing a list of integers and notifies the client through the client proxy.
     *
     * @param header represent the message specific type
     * @param positions is the list showing the positions on the table where it is possible to take a card
     */
    public void notifyPositionList(MessageType header, List<Integer> positions) {
        MessageTCP message = createWithIntegerList(header, positions);
        synchronized (this.clientProxy) {
            this.clientProxy.notifyPositionList(message);
        }
    }


    /**
     * Creates a MessageTCP for the POSITIONS type of message.
     *
     * @param header represents the message type
     * @param positions is the list of integers, representing the answer to the request
     */
    public MessageTCP createWithIntegerList(MessageType header, List<Integer> positions) {
        MessageTCP message = new MessageTCP(header);
        message.setPositions(positions);
        return message;
    }


    /**
     * Generates a message containing a list of strings and notifies the client through the client proxy.
     *
     * @param header represent the message specific type
     * @param names is the list showing the names of the players in the current match
     */
    public void notifyNamesList(MessageType header, List<String> names) {
        MessageTCP message = createWithStringList(header, names);
        synchronized (this.clientProxy) {
            this.clientProxy.notifyNamesList(message);
        }
    }


    /**
     * Creates a MessageTCP for the NAMES type of message.
     *
     * @param header represents the message type
     * @param names is the list of strings, representing the answer to the request
     */
    public MessageTCP createWithStringList(MessageType header, List<String> names) {
        MessageTCP message = new MessageTCP(header);
        message.setNames(names);
        return message;
    }


    /**
     * Generates a message containing an array of cards and their positions and notifies the client through the client proxy.
     *
     * @param header represent the message specific type
     * @param cards is the array showing the cards on the play area of the players in the current match
     */
    public void notifyCardsList(MessageType header, Card[] cards) {
        MessageTCP message = createWithCardList(header, cards);
        synchronized (this.clientProxy) {
            this.clientProxy.notifyCardsList(message);
        }
    }


    /**
     * Creates a MessageTCP for the CARDS type of message.
     *
     * @param header represents the message type
     * @param cards is the list of cards and their positions, representing the answer to the request
     */
    public MessageTCP createWithCardList(MessageType header, Card[] cards) {
        MessageTCP message = new MessageTCP(header);
        message.setCards(cards);
        return message;
    }


    /**
     * Generates a message containing an array of colors and notifies the client through the client proxy.
     *
     * @param header represent the message specific type
     * @param colors is the list showing the available colors for the client
     */
    public void notifyColors(MessageType header, Colors[] colors) {
        MessageTCP message = new MessageTCP(header, colors);
        synchronized (this.clientProxy) {
            this.clientProxy.notifyColors(message);
        }
    }


    /**
     * Generates a message containing a string and notifies the client through the client proxy.
     *
     * @param header represent the message specific type
     * @param string is the string to send back to the client
     */
    public void notifyString(MessageType header, String string) {
        MessageTCP message = new MessageTCP(header, string);
        synchronized (this.clientProxy) {
            this.clientProxy.notifyString(message);
        }
    }


    /**
     * Generates a message containing a table and notifies the client through the client proxy.
     *
     * @param header represent the message specific type
     * @param table reference to the table of the match
     */
    public void notifyTable(MessageType header, Table table) {
        MessageTCP message = new MessageTCP(header, table);
        synchronized (this.clientProxy) {
            this.clientProxy.notifyTable(message);
        }
    }


    /**
     * Generates a message containing a serialized GameController and notifies the client through the client proxy.
     *
     * @param header represent the message specific type
     * @param controller reference to the controller
     */
    public void notifyController(MessageType header, GameController controller) {
        MessageTCP message = new MessageTCP(header, controller);
        synchronized (this.clientProxy) {
            this.clientProxy.notifyController(message);
        }
    }


    /**
     * Generates a turn update message and notifies the client through the client proxy.
     *
     * @param turnUpdate is a message notifying the client that it is their turn
     */
    @Override
    public void notifyTurn(Message turnUpdate) {
        MessageTCP message = new MessageTCP(MessageType.TURN);
        synchronized (this.clientProxy) {
            this.clientProxy.notifyTurn(message);
        }
    }


    /**
     * Generates a message containing the sender and the chat message,
     * then notifies the client through the client proxy.
     *
     * @param sender      is the name of the sender
     * @param chatMessage is the message received
     */
    @Override
    public void onChatMessage(String sender, String chatMessage) {
        MessageTCP message = new MessageTCP(MessageType.CHAT, sender, chatMessage);
        synchronized (this.clientProxy) {
            this.clientProxy.notifyChat(message);
        }
    }


    /**
     * Generates a message containing the sender and the private chat message,
     * then notifies the client through the client proxy.
     *
     * @param sender      is the name of the sender
     * @param chatMessage is the message received
     */
    @Override
    public void onPrivateChatMessage(String sender, String chatMessage) {
        MessageTCP message = new MessageTCP(MessageType.PRIVATE_CHAT, sender, chatMessage);
        synchronized (this.clientProxy) {
            this.clientProxy.notifyChat(message);
        }
    }


    /**
     * Method called to get the messages received by the player
     *
     * @return the BlockingQueue containing all the messages
     * @throws RemoteException in case of network errors
     */
    @Override
    public BlockingQueue<String> getMessagesQueueGui() throws RemoteException {
        return null;
    }


    /**
     * Generates a pong message and notifies the client though the client proxy.
     */
    @Override
    public void pong() {
        MessageTCP message = new MessageTCP(MessageType.PING);
        synchronized (this.clientProxy) {
            this.clientProxy.notifyMethodCall(message);
        }
    }


    /**
     * Closes the connection for this client after a countdown of 3 seconds.
     */
    @Override
    public void closeConnection() throws RemoteException {}


    /**
     * Generates an exit message and notifies the client through the client proxy.
     *
     * @param details contains details about the occurred error
     */
    @Override
    public void forceExit(String details) {
        MessageTCP message = new MessageTCP(MessageType.EXIT, details);
        synchronized (this.clientProxy) {
            this.clientProxy.notifyString(message);
        }
    }
}
