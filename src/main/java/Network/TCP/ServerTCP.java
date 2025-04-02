package Network.TCP;

import Controller.GameController;
import Controller.MatchController;
import Listeners.EndPoint;
import Listeners.GameStatus;
import Listeners.Message;
import Model.Cards.Card;
import Model.Cards.Colors;
import Model.Player;
import Model.Table;
import Network.ClientData;
import Network.VirtualServer;
import javafx.util.Pair;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.*;

/**
 * the server for every client that use a TCP protocol
 */
public class ServerTCP implements VirtualServer {

    final ServerSocket listenSocket;

    /**
     * a reference to the controller of all matches
     */
    private final GameController controller;

    /**
     * a list of all clients connected using the TCP protocol
     */
    private final CopyOnWriteArrayList<ClientData> clients;

    /**
     *a map used to allow access to the map by multiple threads
     */
    private final ConcurrentHashMap<String, Long> lastResponses;

    /**
     *an extension of the interface 'ExecutorService'
     */
    private final ScheduledExecutorService scheduler;


    /**
     * the constructor of the class
     * @param listenSocket is the socket configured to listen for incoming connections on a specific port
     * @param controller is the reference to the controller
     * @param clients is the list of all clients connected using the TCP protocol
     */
    public ServerTCP(ServerSocket listenSocket, GameController controller, CopyOnWriteArrayList<ClientData> clients) {
        this.listenSocket = listenSocket;
        this.controller = controller;
        this.clients = clients;
        scheduler = Executors.newScheduledThreadPool(1);
        lastResponses = new ConcurrentHashMap<>();
        scheduledPing();
    }


    /**
     * with this method the server for the TCP protocol can start
     * @throws IOException if there is a generic input or output exception
     */
    public void runServer() throws IOException {
        Socket clientSocket;
        while ((clientSocket = this.listenSocket.accept()) != null) {
            InputStreamReader socketRx = new InputStreamReader(clientSocket.getInputStream());
            OutputStreamWriter socketTx = new OutputStreamWriter(clientSocket.getOutputStream());

            ClientHandler handler = new ClientHandler(this, new BufferedReader(socketRx), new BufferedWriter(socketTx));

            synchronized (this.clients) {
                clients.add(new ClientData(handler, "", -1));
            }
            new Thread(() -> {
                try {
                    handler.runVirtualView();
                } catch (IOException e) {
                    System.err.println("\nError for handler: " + e.getMessage());
                }
            }).start();
        }
    }


    /**
     * We register a client to a list of clients (VirtualView) the server keeps.
     *
     * @param client is the client that needs to be added
     */
    @Override
    public void connect(ClientData client) {}


    /**
     * Checks if the nickname chosen by the client is already in use (check is case-sensitive).
     *
     * @param nameToCheck is the name the client would like to use
     * @return true if the name is already in use, false otherwise
     */
    @Override
    public boolean checkName(String nameToCheck) {
        boolean alreadyInUse = false;
        if (!clients.isEmpty()) {
            for (ClientData c : clients) {
                synchronized (this.clients) {
                    if (c.getNickname().equals(nameToCheck))    alreadyInUse = true;
                }
            }
        }
        return alreadyInUse;
    }


    /**
     * Checks if a command selected by the client is legal during this phase of their turn.
     *
     * @param idMatch  is the id which identifies the match the clint is playing
     * @param nickname is the name of the player who launched the command
     * @param command  is the command indicating the action the client is trying to perform
     * @return true if the command is legal, false otherwise
     */
    @Override
    public boolean checkCommand(int idMatch, String nickname, String command) {
        boolean isLegal = controller.getMatch(idMatch).getPhase().processCommand(command);
        return isLegal;
    }


    /**
     * Checks if the player with the specified nickname is present in the specified match.
     *
     * @param idMatch  indicates the match
     * @param nickname indicates the player to search
     * @return true if the player is present in the match
     */
    @Override
    public boolean checkPlayer(int idMatch, String nickname) {
        Player player = controller.getMatch(idMatch).getPlayerByName(nickname);
        if (player == null) return false;
        return true;
    }


    /**
     * Checks whether the client  inserted a valid recipient name for chatting.
     *
     * @param idMatch   is the id which identifies the match the clint is playing
     * @param recipient is the name the client typed
     * @return true if recipient is a valid name, false otherwise
     */
    @Override
    public boolean checkRecipient(int idMatch, String sender, String recipient) {
        if (recipient.equalsIgnoreCase("all") || recipient.equalsIgnoreCase("everybody"))
            return true;
        boolean isValid = false;
        synchronized (this.controller) {
            MatchController currentMatch = controller.getMatch(idMatch);
            for (Player player : currentMatch.getTable().getPlayers()) {
                if (player.getNickname().equals(recipient) && !player.getNickname().equals(sender))
                    isValid = true;
            }
        }
        return isValid;
    }


    /**
     * Getter method for the server's controller attribute.
     *
     * @return the instance of the server's controller
     */
    @Override
    public GameController getController() {
        return this.controller;
    }


    /**
     * Certifies if there is an open match.
     *
     * @return true if there isn't an open match
     */
    @Override
    public boolean firstPlayer() {
        if (controller.getPlayersQueue() == 0) return true;
        return false;
    }


    /**
     * Initializes the number of players in the game.
     *
     * @param n The number of players declared by the client
     */
    @Override
    public void setNumbOfPlayers(int n) {
        controller.setPlayersQueue(n);
    }


    /**
     * Calls the respective method present in GameController.
     * Adds a new match to the game.
     *
     * @param name is the name of the player who created the new match
     * @return the ID of the newly created match
     */
    @Override
    public int addMatch(String name) {
        EndPoint playerListener = new EndPoint();
        int idMatch;
        idMatch = controller.addMatch(name, playerListener);
        playerListener.setServer(this);
        System.err.println("\nNew client connected: " + name);
        for (ClientData c : clients) {
            synchronized (this.clients) {
                try {
                    if (c.getClient().getNickname().equals(name)) {
                        c.setNickname(name);
                        c.setIdMatch(idMatch);
                    }
                } catch (RemoteException | NullPointerException e) {
                    try { c.getClient().setNickname(name); }
                    catch (RemoteException ex) { throw new RuntimeException(ex); }
                    c.setNickname(name);
                    c.setIdMatch(idMatch);
                }
            }
        }
        return idMatch;
    }


    /**
     * Calls the respective method present in GameController.
     * The method adds a Player in a match which has not yet started.
     *
     * @param name of the player
     * @return the ID related to the match
     */
    @Override
    public int addPlayer(String name) {
        EndPoint playerListener = new EndPoint();
        int idMatch;
        try {
            idMatch = controller.addPlayer(name, playerListener);
        } catch (NoSuchElementException e) {
            System.err.println("\nError: client trying to connect to a non existing match");
            return -1;
        }
        playerListener.setServer(this);
        System.err.println("\nNew client connected: " + name);
        for (ClientData c : clients) {
            synchronized (this.clients) {
                try {
                    if (c.getClient().getNickname().equals(name)) {
                        c.setNickname(name);
                        c.setIdMatch(idMatch);
                    }
                } catch (NullPointerException | RemoteException e) {
                    try { c.getClient().setNickname(name); }
                    catch (RemoteException ex) { throw new RuntimeException(ex); }
                    c.setNickname(name);
                    c.setIdMatch(idMatch);
                }
            }
        }
        return idMatch;
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
        return controller.getMatch(idMatch).getPlayersNames();
    }


    /**
     * Sends the update contained in a message to the respective client, by calling their method showUpdate.
     *
     * @param message is the message containing the necessary information to display
     */
    @Override
    public void sendUpdate(Message message) {
        for (ClientData c : clients) {
            synchronized (this.clients) {
                try {
                    if (message.getNickname().equals(c.getNickname())) {
                        if (message.getStatus().equals(GameStatus.IT_IS_YOUR_TURN)) {
                            c.getClient().notifyTurn(message);
                        }
                        else c.getClient().showUpdate(message);
                    }
                } catch (RemoteException e) { forceClientDisconnection(c); }
            }
        }
    }


    /**
     * Gets the table of a specified match.
     *
     * @param idMatch is the ID related to the match
     * @return the table related to the specified match
     */
    @Override
    public Table getTable(int idMatch) {
        try {
            return controller.getMatch(idMatch).getTable();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        return null;
    }


    /**
     * Calls the respective place method present on the controller.
     * Lets the player place a card on their play area.
     *
     * @param idMatch      is the ID related to the match the player is currently playing
     * @param nickname     is the name of the player who launched the command
     * @param cardSelected references to the index of the card which has been selected
     * @param faceSelected indicates of the card needs to be placed by the front or by the back
     * @param x            is the first coordinate of the position where the player wants to place the card
     * @param y            is the second coordinate of the position where the player wants to place the card
     */
    @Override
    public void place(int idMatch, String nickname, int cardSelected, String faceSelected, int x, int y) {
        controller.getMatch(idMatch).place(nickname, cardSelected, faceSelected, x, y);
    }


    @Override
    public String getPhaseGui(int idMatch) {
        return controller.getMatch(idMatch).getPhase().communicatePhaseToGui();
    }


    /**
     * Calls the respective drawResource method present on the controller.
     * Lets the player draw a card from the resource deck.
     *
     * @param idMatch  is the ID of the match the player is currently playing
     * @param nickname is the name of the player who wants to play the action
     */
    @Override
    public void drawResource(int idMatch, String nickname) {
        controller.getMatch(idMatch).drawResource(nickname);
    }


    /**
     * Calls the respective drawGold method present on the controller.
     * Lets the player draw a card from the gold deck.
     *
     * @param idMatch  is the ID of the match the player is currently playing
     * @param nickname is the name of the player who wants to play the action
     */
    @Override
    public void drawGold(int idMatch, String nickname) {
        controller.getMatch(idMatch).drawGold(nickname);
    }


    /**
     * Calls the respective drawTable method present on the controller.
     * Lets the player draw a card from the table.
     *
     * @param idMatch      is the ID of the match the player is currently playing
     * @param nickname     is the name of the player who wants to play the action
     * @param cardSelected references to the index of the card which has been selected from the table
     */
    @Override
    public void drawTable(int idMatch, String nickname, int cardSelected) {
        controller.getMatch(idMatch).drawTable(nickname, cardSelected);
    }

    /**
     * Calls the respective method in VirtualServer.
     * Method used by a user playing via Gui to receive placed cards and their coordinates,
     * the cards in the array are sorted by placement order.
     *
     * @param idMatch  is the ID of the match the player is currently playing
     * @param nickname is the name of the player who placed the cards contained in the list
     * @return an array containing the cards placed and their coordinates in a system where
     * the starter card is in position (0, 0)
     */
    @Override
    public Card[] getOrderedCardsList(int idMatch, String nickname) {
        return controller.getMatch(idMatch).getPlayerByName(nickname).getPlayArea().getOrderedCardList();
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
        return controller.getMatch(idMatch).getPlayerByName(nickname).getPoints();
    }


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
     */
    @Override
    public int whereCanIDraw(int idMatch) {
        return controller.getMatch(idMatch).whereCanIDraw();
    }


    /**
     * Calls the respective method on MatchController.
     * Shows which positions on the table of a specific match have cards on them.
     *
     * @param idMatch is the ID of the match the player is currently playing
     * @return a list with all the positions of the table with a card on them
     */
    @Override
    public List<Integer> availablePositionsForDrawing(int idMatch) {
        return controller.getMatch(idMatch).availablePositionsForDrawing();
    }


    /**
     * Sends a message to the specified recipient by chat.
     *
     * @param idMatch     is the ID of the match the player is currently playing
     * @param sender    is the name of the sender client
     * @param recipient   is the name of the recipient of the message
     * @param chatMessage is the message typed by the sender
     */
    @Override
    public void chat(int idMatch, String sender, String recipient, String chatMessage) {
        try {
            if (recipient.equalsIgnoreCase("all") || recipient.equalsIgnoreCase("everybody")) {
                for (ClientData c : clients) {
                    synchronized (this.clients) {
                        if (c.getIdMatch() == idMatch && !c.getNickname().equals(sender))
                            c.getClient().onChatMessage(sender, chatMessage);
                    }
                }
            }
            else {
                for (ClientData c : clients) {
                    synchronized (this.clients) {
                        if (c.getIdMatch() == idMatch && c.getNickname().equals(recipient))
                            c.getClient().onPrivateChatMessage(sender, chatMessage);
                    }
                }
            }
        } catch (RemoteException e) { System.err.println("Can not send chat message to client, network error"); }
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
     * Checks if the requested number of players joined the specified match.
     *
     * @param idMatch is the ID related to the specified match
     * @return true if every expected player is connected
     */
    @Override
    public boolean allConnected(int idMatch) {
        if(controller.getMatch(idMatch).getNumOfPlayers() == controller.getMatch(idMatch).getTable().getPlayers().size()) {
            return true;
        }
        return false;
    }


    /**
     * Checks if every player correctly placed their starter card.
     *
     * @param idMatch is the ID related to the specified match
     * @return true if every starter card has been placed, false otherwise
     */
    @Override
    public boolean allStarterCardsPlaced(int idMatch) {
        boolean allPlaced = false;
        if (controller.getMatch(idMatch).getNumOfStarterCardsPlaced() == controller.getMatch(idMatch).getTable().getPlayers().size()){
            allPlaced = true;
        }
        return allPlaced;
    }


    /**
     * If a starter card has been placed, this method increases the number of placed starter cards for the current match.
     *
     * @param idMatch is the ID related to the specified match
     */
    @Override
    public void increaseNumOfStarterCardsPlaced(int idMatch) {
        MatchController currentMatch = controller.getMatch(idMatch);
        currentMatch.setNumOfStarterCardsPlaced(currentMatch.getNumOfStarterCardsPlaced() + 1);
    }


    /**
     * Calls the respective method present in MatchController.
     * This method shuffles decks on the table and provides each player with their starter card.
     *
     * @param idMatch is the ID related to the specified match
     */
    @Override
    public void shuffleDecksAndGiveStarterCards(int idMatch) {
        controller.getMatch(idMatch).shuffleDecksAndGiveStarterCards();
    }


    /**
     * With this method every player draws 3 cards and common objective cards are placed
     *
     * @param idMatch is the current match
     */
    @Override
    public void drawCardsAndPlaceCommonObjectives(int idMatch) {
        controller.getMatch(idMatch).drawCardsAndPlaceCommonObjectives();
    }


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
     */
    @Override
    public void viewDifferenceOfResources(int idMatch, String nickname, int cardSelected, String faceSelected, int pos1, int pos2) {
        controller.getMatch(idMatch).viewDifferenceOfResources(nickname, cardSelected, faceSelected, pos1, pos2);
    }


    /**
     * Calls the respective method present in MatchController.
     * Displays to the player their hand.
     *
     * @param idMatch is the ID related to the specified match
     * @param nickname is the name of the player who made the request
     */
    @Override
    public void viewHand(int idMatch, String nickname) {
        controller.getMatch(idMatch).viewHand(nickname);
    }


    /**
     * Calls the respective method present in MatchController.
     * Displays to the player their hand and area.
     *
     * @param idMatch is the ID related to the specified match
     * @param nickname is the name of the player who made the request
     */
    public void viewHandAndArea(int idMatch, String nickname) {
        controller.getMatch(idMatch).viewHandAndArea(nickname);
    }


    /**
     * Calls the respective method present in MatchController.
     * Displays to the player the starting table.
     *
     * @param idMatch is the ID related to the specified match
     * @param nickname is the name of the player who made the request
     */
    @Override
    public void viewStartingTable(int idMatch, String nickname) {
        controller.getMatch(idMatch).viewStartingTable(nickname);
    }


    /**
     * Calls the respective method present in MatchController.
     * Displays to the player the table.
     *
     * @param idMatch is the ID related to the specified match
     * @param nickname is the name of the player who made the request
     */
    @Override
    public void viewTable(int idMatch, String nickname) {
        controller.getMatch(idMatch).viewTable(nickname);
    }


    /**
     * Calls the respective method present in MatchController.
     * Place the 2 common objective cards on the table.
     *
     * @param idMatch is the ID related to the specified match
     */
    @Override
    public void giveObjectives(int idMatch) {
        controller.getMatch(idMatch).giveObjectives();
    }


    /**
     * Calls the respective method present in MatchController.
     * Displays to the player their 2 options for their secret objectives.
     *
     * @param idMatch is the ID related to the specified match
     * @param nickname is the name of the player who made the request
     */
    @Override
    public void viewChoiceObjectives(int idMatch, String nickname) {
        controller.getMatch(idMatch).viewChoiceObjectives(nickname);
    }


    /**
     * Calls the respective method present in MatchController.
     * Puts the chosen card on the table.
     *
     * @param idMatch is the ID related to the specified match
     * @param nickname is the name of the player who made the request
     * @param obSelected indicates which option the client selected
     */
    @Override
    public void putChoiceObjectives(int idMatch, String nickname, int obSelected) {
        controller.getMatch(idMatch).putChoiceObjectives(nickname, obSelected);
    }


    /**
     * Calls the respective getter method present in MatchController.
     *
     * @param idMatch is the ID related to the specified match
     * @return an array of all the colors the player can choose from
     */
    @Override
    public Colors[] showAvailableColors(int idMatch) {
        return controller.getMatch(idMatch).getAvailableColors();
    }


    /**
     * Checks wether it is the turn of a specified player.
     *
     * @param idMatch references to the ID of the specified match
     * @param nickname is the nickname of the player who is making the request
     */
    @Override
    public void itIsMyTurn(int idMatch, String nickname) {
        controller.getMatch(idMatch).itIsMyTurn(nickname);
    }


    /**
     * Calls the respective method present in MatchController.
     * Verifies if it is the turn of client who made the request to choose the color of their pawn.
     *
     * @param idMatch references to the ID of the specified match
     * @param nickname is the nickname of the player who is making the request
     * @return true if it is the turn of the player to choose the color
     */
    @Override
    public boolean canIChooseTheColor(int idMatch, String nickname) {
        return controller.getMatch(idMatch).canIChooseTheColor(nickname);
    }


    /**
     * Calls the respective method present in MatchController.
     * Checks if the color the client selected is valid.
     *
     * @param idMatch references to the ID of the specified match
     * @param colorChosen is the color the client selected
     * @return true if the client entered a valid color
     */
    @Override
    public boolean checkColor(int idMatch, Colors colorChosen) {
        return controller.getMatch(idMatch).checkColor(colorChosen);
    }


    /**
     * Calls the respective method present in MatchController.
     * Removes the color from the array of the available colors and lets the next player choose their color.
     *
     * @param idMatch references to the ID of the specified match
     * @param nickname is the nickname of the player who is making the request
     * @param colorChosen is the color the client selected
     */
    @Override
    public void removeColorAndPassTurn(int idMatch, String nickname, Colors colorChosen) {
        controller.getMatch(idMatch).removeColorAndPassTurn(nickname, colorChosen);
    }


    /**
     * Calls the respective getter method present in MatchController.
     *
     * @param idMatch is the ID related to the current match
     */
    @Override
    public boolean isBlackPawnChosen(int idMatch) {
        return controller.getMatch(idMatch).isBlackPawnChosen();
    }


    /**
     * Calls the respective setter method present in MatchController.
     *
     * @param idMatch is the ID related to the current match
     * @param value is a boolean indicating the value to pass to the setter method as a parameter
     */
    @Override
    public void setBlackPawnChosen(int idMatch, boolean value) {
        controller.getMatch(idMatch).setBlackPawnChosen(value);
    }


    /**
     * Calls the respective method present in MatchController.
     * Randomly assigns the black pawn.
     *
     * @param idMatch is the ID related to the current match
     */
    @Override
    public void chooseTheBlackPawn(int idMatch) {
        controller.getMatch(idMatch).chooseTheBlackPawn();
    }


    /**
     * Calls the respective getter method present in MatchController.
     *
     * @param idMatch is the ID related to the current match
     * @return the nickname of the first player of the match, the one owning the black pawn
     */
    @Override
    public String getNameOfThePlayerWithTheBlackPawn(int idMatch) {
        return controller.getMatch(idMatch).getNameOfThePlayerWithTheBlackPawn();
    }


    /**
     * Calls the respective setter method present in MatchController.
     *
     * @param idMatch is the ID related to the current match
     * @param nickname is the nickname of the player whose turn will be set
     * @param value is a boolean indicating the value to pass to the setter method as a parameter
     */
    @Override
    public void setYourTurn(int idMatch, String nickname, boolean value) {
        controller.getMatch(idMatch).setYourTurn(nickname, value);
    }


    /**
     * Calls the respective getter method isYourTurn of the specified player.
     *
     * @param idMatch is the ID related to the current match
     * @param nickname is the nickname of the player who is trying to perform an action
     * @return true if it is the turn of the specified player
     */
    @Override
    public boolean checkTurn(int idMatch, String nickname) {
        return controller.getMatch(idMatch).isYourTurn(nickname);
    }


    /**
     * Calls the respective method present in MatchController.
     * The player with the specified nickname receives on display the play area.
     *
     * @param idMatch is the ID relative to the specified match
     * @param nickname is the nickname of the player who is requesting to view the area
     * @param areaOwner is the nickname of the player whose area is going to be displayed
     */
    @Override
    public void viewPlayArea(int idMatch, String nickname, String areaOwner) {
        controller.getMatch(idMatch).viewPlayArea(nickname, areaOwner);
    }


    /**
     * Calls the respective method present in MatchController.
     * The player with the specified nickname receive on display their secret objective card.
     *
     * @param idMatch  is the ID relative to the specified match
     * @param nickname is the nickname of the player who is requesting to view the card
     */
    @Override
    public void viewSecretObjective(int idMatch, String nickname) {
        controller.getMatch(idMatch).viewSecretObjective(nickname);
    }


    /**
     * Calls the respective method present in MatchController.
     * Checks is the specified coordinates allow the player to place a card.
     *
     * @param idMatch  is the ID relative to the specified match
     * @param nickname is the nickname of the layer who is requesting to place the card
     * @param x        is the first coordinate (row) selected by the player
     * @param y        is the first coordinate (row) selected by the player
     * @return true if a card can be placed on the specified coordinates, false otherwise
     */
    @Override
    public boolean areCoordinatesValid(int idMatch, String nickname, int x, int y) {
        return controller.getMatch(idMatch).areCoordinatesValid(nickname, x, y);
    }


    /**
     * Calls the respective method present in MatchController.
     * Checks wether the requirements to place a gold card are met.
     *
     * @param idMatch      is the ID relative to the specified match
     * @param nickname     is the nickname of the layer who is requesting to place the gold card
     * @param cardSelected indicates the position of the gold card in the player's hand
     * @return true if the requirements are met, false otherwise
     */
    @Override
    public boolean canIPlaceTheGoldCard(int idMatch, String nickname, int cardSelected) {
        return controller.getMatch(idMatch).canIPlaceTheGoldCard(nickname, cardSelected);
    }


    /**
     * If a player chooses their secret objective, this method updates the number of chosen secret objectives for the specified match.
     *
     * @param idMatch is the ID related to the specified match
     */
    @Override
    public void increaseNumOfObjectiveCardChosen(int idMatch) {
        MatchController currentMatch = controller.getMatch(idMatch);
        currentMatch.setNumOfObjectiveCardChosen(currentMatch.getNumOfObjectiveCardChosen()+1);
    }


    /**
     * Checks if every player has chosen their secret objective.
     *
     * @param idMatch is the ID related to the specified match
     * @return true if every player has chosen their secret objective, false otherwise
     */
    @Override
    public boolean allObjectiveCardsChosen(int idMatch) {
        boolean allChosen = false;
        if (controller.getMatch(idMatch).getNumOfObjectiveCardChosen() == controller.getMatch(idMatch).getNumOfPlayers()) {
            allChosen = true;
        }
        return allChosen;
    }


    /**
     * Calls the respective method present in MatchController.
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
        return controller.getMatch(idMatch).checkSecondToLastTurn(nickname);
    }


    /**
     * Calls the respective setter method present in MatchController.
     *
     * @param idMatch is the ID related to the current match
     * @param value is a boolean indicating the value to pass to the setter method as a parameter
     */
    @Override
    public void setLastRound(int idMatch, boolean value) {
        controller.getMatch(idMatch).setLastRound(value);
    }


    /**
     * Calls the respective getter method present in MatchController.
     *
     * @param idMatch is the ID related to the current match
     * @return true if the match is in its last round
     */
    @Override
    public boolean isLastRound(int idMatch) {
        return controller.getMatch(idMatch).isLastRound();
    }


    /**
     * Calls the respective getter method present in MatchController.
     *
     * @param idMatch is the ID related to the current match
     * @return true if the match is in its second to last round
     */
    @Override
    public boolean isSecondToLastRound(int idMatch) {
        return controller.getMatch(idMatch).isSecondToLastRound();
    }


    /**
     * Calls the respective setter method present in MatchController.
     *
     * @param idMatch is the ID related to the current match
     * @param b is a boolean indicating the value to pass to the setter method as a parameter
     */
    @Override
    public void setSecondToLastRound(int idMatch, boolean b) {
        controller.getMatch(idMatch).setSecondToLastRound(b);
    }


    /**
     * Calls the respective method present in MatchController.
     * Verify if the next player is the first player.
     *
     * @param idMatch is the ID related to the current match
     * @param nickname is the nickname of the client/player
     * @return true if the next player in the list Players present on the match table owns the black pawn
     */
    @Override
    public boolean isNextPlayerTheBlackPawn(int idMatch, String nickname) {
        return controller.getMatch(idMatch).isNextPlayerTheBlackPawn(nickname);
    }


    /**
     * Increase the number of players who have finished to play the match.
     *
     * @param idMatch is the ID related to the current match
     */
    @Override
    public void increaseNumOfPlayersThatHaveFinishedToPlay(int idMatch) {
        MatchController currentMatch = controller.getMatch(idMatch);
        currentMatch.setNumOfPlayersThatHaveFinishedToPlay(currentMatch.getNumOfPlayersThatHaveFinishedToPlay() +1);
    }


    /**
     * Calls the respective method present in MatchController.
     * Gets the number of players who have finished to play.
     *
     * @param idMatch is the ID related to the current match
     * @return the number of players who have finished to play
     */
    @Override
    public int getNumOfPlayersThatHaveFinishedToPlay(int idMatch) {
        return controller.getMatch(idMatch).getNumOfPlayersThatHaveFinishedToPlay();
    }


    /**
     * Calls the respective getter method present in MatchController.
     *
     * @param idMatch is the ID related to the current match
     * @return the number of players chosen by the creator of the match
     */
    @Override
    public int getNumOfPlayers(int idMatch) {
        return controller.getMatch(idMatch).getNumOfPlayers();
    }


    /**
     * Calls the respective method present in MatchController.
     * Calculates the points gained by the specified player from the common and the secret objectives.
     *
     * @param idMatch is the ID related to the current match
     * @param nickname is the nickname of the client/player
     */
    @Override
    public void calculateObjectives(int idMatch, String nickname) {
        controller.getMatch(idMatch).calculateObjectives(nickname);
    }


    /**
     * Increases the number of players who have their final score calculated.
     *
     * @param idMatch is the ID related to the current match
     */
    @Override
    public void increaseNumOfObjectiveCardCalculated(int idMatch) {
        MatchController currentMatch = controller.getMatch(idMatch);
        currentMatch.setNumOfObjectiveCardCalculated(currentMatch.getNumOfObjectiveCardCalculated() + 1);
    }


    /**
     * Verifies if every player in the specified match had their final score calculated.
     *
     * @param idMatch is the ID related to the current match
     * @return true if the score was calculated for every player
     */
    @Override
    public boolean allObjectiveCardsCalculated(int idMatch) {
        boolean allCalculated = false;
        MatchController currentMatch = controller.getMatch(idMatch);
        if (currentMatch.getNumOfObjectiveCardCalculated() == currentMatch.getNumOfPlayers()) allCalculated = true;
        return allCalculated;
    }


    /**
     * Calls the respective method present in MatchController.
     * Decides who is the winner of the specified match.
     *
     * @param idMatch is the ID related to the current match
     */
    @Override
    public void calculateWinner(int idMatch) {
        controller.getMatch(idMatch).calculateWinner();
    }


    /**
     * Calls the respective method present in MatchController.
     * Displays a "you won" or a "you lost" message to every player.
     *
     * @param idMatch is the ID related to the current match
     * @param nickname is the nickname of the specified player
     */
    @Override
    public void showEndGameMessage(int idMatch, String nickname) {
        controller.getMatch(idMatch).showEndGameMessage(nickname);
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
        return controller.getMatch(idMatch).endGameMessageGui(nickname);
    }


    /**
     * Calls the respective method present in MatchController.
     * This cheat provides the specified player with 10000 resources per each symbol.
     *
     * @param idMatch  is the ID related to the current match
     * @param nickname is the nickname of the specified player
     */
    @Override
    public void cheat1(int idMatch, String nickname) {
        controller.getMatch(idMatch).cheat1(nickname);
    }


    /**
     * Calls the respective method present in MatchController.
     * This cheat provides the specified player with 20 points.
     *
     * @param idMatch is the ID related to the current match
     * @param nickname is the nickname of the specified player
     */
    @Override
    public void cheat2(int idMatch, String nickname) {
        controller.getMatch(idMatch).cheat2(nickname);
    }


    /**
     * Calls the respective method present in MatchController.
     * With this cheat both resource deck and gold deck become empty.
     *
     * @param idMatch is the ID related to the current match
     */
    @Override
    public void cheat3(int idMatch) {
        controller.getMatch(idMatch).cheat3();
    }


    /**
     * Sends a ping request to the client every 3 minutes.
     * If a pong answer from the client is not received in 2 minutes, the client
     * is removed from the clients list and forced to quit.
     */
    public void scheduledPing() {
        scheduler.scheduleAtFixedRate(() -> {
            synchronized (this.clients) {
                for (ClientData c : clients) {
                    try {
                        String nickname = c.getNickname();
                        if (lastResponses.containsKey(nickname)) {
                            long lastResponseTime = lastResponses.get(nickname);
                            long currentTime = System.currentTimeMillis();
                            //If a client does not respond within 2 minutes, it is considered as disconnected
                            if (currentTime - lastResponseTime > 120000)  forceClientDisconnection(c);
                        }
                        c.getClient().pong();
                    } catch (IOException e) {
                        System.err.println("\nError while pinging client " + c.getNickname());
                    }
                }
            }
        }, 0, 180, TimeUnit.SECONDS); //Ping every 3 minutes
    }


    /**
     * Receives a pong answer from a client and registers the time.
     *
     * @param nickname is the nickname of the specified client
     */
    public void receivePong(String nickname) {
        lastResponses.put(nickname, System.currentTimeMillis());
    }


    /**
     * Removes the specified client from the clients list present on the server.
     *
     * @param nickname is the nickname of the specified player
     */
    @Override
    public void removeClient(String nickname) {
        System.err.println("\nClient " + nickname + " disconnected");
        for (ClientData c : clients) {
            synchronized (this.clients) {
                if (c.getNickname().equals(nickname))
                    clients.remove(c);
            }
        }
    }


    /**
     * Removes a client from the clients list and forces they to disconnect and quit.
     *
     * @param client is a reference to the specific client
     */
    public void forceClientDisconnection(ClientData client) {
        System.err.println("\nClient " + client.getNickname() + " not responding, match " + client.getIdMatch() + " aborted");
        int idMatch = client.getIdMatch();
        clients.remove(client);
        for (ClientData c : clients) {
            if (c.getIdMatch() == idMatch) {
                String details = "\n\nA client in your match left, match aborted";
                try { c.getClient().forceExit(details); }
                catch (RemoteException e) { throw  new RuntimeException(e); }
            }
        }
    }
}
