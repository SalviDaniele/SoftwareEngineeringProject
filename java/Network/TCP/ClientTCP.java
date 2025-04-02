package Network.TCP;
import Controller.GameController;
import Listeners.Message;
import Model.Cards.*;
import Model.Table;
import Network.Chat;
import Network.VirtualServer;
import Network.VirtualView;
import View.Gui.Gui;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This class runs a client in case their choose the TCP connection protocol.
 */
public class ClientTCP implements VirtualView {

    /**
     * Resets the color
     */
    public static final String RESET = "\033[0m";

    /**
     * Red color
     */
    public static final String RED = "\033[0;31m";

    /**
     * Green color
     */
    public static final String GREEN = "\033[0;32m";

    /**
     * Yellow color
     */
    public static final String YELLOW = "\033[0;33m";

    /**
     * Blue color
     */
    public static final String BLUE = "\033[0;34m";

    /**
     * Purple color
     */
    public static final String PURPLE = "\033[0;35m";

    /**
     * Cyan color
     */
    public static final String CYAN = "\033[0;36m";

    /**
     * White color
     */
    public static final String WHITE = "\033[0;37m";

    /**
     * Chat service the client can use to write message to other clients.
     */
    private Chat chat;

    /**
     * Thread used for the chat service.
     */
    private Thread chatThread;

    /**
     * A reference to the server proxy.
     */
    private ServerProxy server;

    /**
     * This client's socket.
     */
    private Socket socket;

    /**
     * The buffered reader which takes strings in input from the client proxy.
     */
    @JsonIgnore
    private BufferedReader input;

    /**
     * The buffered writer which writes strings in output to the server proxy.
     */
    @JsonIgnore
    private BufferedWriter output;

    /**
     * This client's nickname.
     */
    private String nickname;

    /**
     * ID of the match the client is playing in.
     */
    private int idMatch;

    /**
     * Signals if the player has been assigned with the black pawn.
     */
    private boolean blackPawn = false;

    /**
     * Boolean which is true if the client is the first player to enter a match.
     */
    private boolean first = false;

    /**
     * True if the player finished to play.
     */
    private boolean finishedToPlay = false;

    /**
     * The type of view the client has chosen.
     */
    private Gui view;

    /**
     * True if the user chose to play via Gui, false otherwise
     */
    private final boolean viewChoice;

    /**
     * Signals if the match ended.
     */
    private boolean matchEnded = false;

    /**
     * The color of the player's color.
     */
    private Colors color;

    /**
     * A blocking queue for incoming messages, serialized as strings.
     */
    private final BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();

    /**
     * A blocking queue for incoming booleans, serialized as strings.
     */
    private final BlockingQueue<String> booleanQueue = new LinkedBlockingQueue<>();

    /**
     * A blocking queue for incoming integers, serialized as strings.
     */
    private final BlockingQueue<String> integerQueue = new LinkedBlockingQueue<>();

    /**
     * A blocking queue for incoming integer arrays, serialized as strings.
     */
    private final BlockingQueue<String> integersQueue = new LinkedBlockingQueue<>();

    /**
     * A blocking queue for incoming update messages, serialized as strings.
     */
    private final BlockingQueue<String> updateQueue = new LinkedBlockingQueue<>();

    /**
     * A blocking queue for incoming Colors type objects, serialized as strings.
     */
    private final BlockingQueue<String> colorsQueue = new LinkedBlockingQueue<>();

    /**
     * A blocking queue for incoming strings.
     */
    private final BlockingQueue<String> stringQueue = new LinkedBlockingQueue<>();

    /**
     * A blocking queue for incoming string arrays.
     */
    private final BlockingQueue<String> stringsQueue = new LinkedBlockingQueue<>();

    /**
     * A blocking queue for incoming Table type objects, serialized as strings.
     */
    private final BlockingQueue<String> tableQueue = new LinkedBlockingQueue<>();

    /**
     * A blocking queue for incoming GameController type objects, serialized as strings.
     */
    private final BlockingQueue<String> controllerQueue = new LinkedBlockingQueue<>();

    /**
     * A blocking queue for incoming GameController type objects, serialized as strings.
     */
    private final BlockingQueue<String> cardsQueue = new LinkedBlockingQueue<>();

    /**
     * Mapper used to deserialize incoming messages from the client proxy.
     */
    private final ObjectMapper objectMapper;


    /**
     * Constructor for the ClientTCP class.
     */
    public ClientTCP(String choice) {
        objectMapper = new ObjectMapper();
        if(choice.equals("gui")){
            viewChoice = true;
            view = new Gui();
            view.setTarget(this);
            view.initializeChat();
        } else {
            viewChoice = false;
            this.chat = new Chat(false);
        }
    }


    /**
     * Setter method for the socket attribute.
     *
     * @param socket the client's socket
     */
    public void setSocket(Socket socket) {
        this.socket = socket;
    }


    /**
     * Setter method for the input attribute.
     *
     * @param input buffered reader which takes strings in input from the client proxy
     */
    public void setInput(BufferedReader input) {
        this.input = input;
    }


    /**
     * Setter method for the input attribute.
     *
     * @param server reference to the server proxy
     */
    public void setServer(ServerProxy server) {
        this.server = server;
    }


    /**
     * Getter method for the blackPawn attribute
     *
     * @return true if the player is selected as black pawn
     */
    @Override
    public boolean isBlackPawn() {
        return blackPawn;
    }


    /**
     * Setter method for the blackPawn attribute
     * @param blackPawn true if the player is selected as the black pawn, false otherwise
     */
    @Override
    public void setBlackPawn(boolean blackPawn) {
        this.blackPawn = blackPawn;
    }


    /**
     * Getter for the first attribute.
     *
     * @return true if the player is the first to join a match
     */
    @Override
    public boolean isFirst() {
        return first;
    }


    /**
     * Setter method for the color attribute.
     *
     * @param first is true if the client is the first to join a match, false otherwise
     */
    @Override
    public void setFirst(boolean first) {
        this.first = first;
    }


    /**
     * Setter method for the color attribute.
     *
     * @param color of the player's pawn
     */
    @Override
    public void setColor(Colors color) {
        this.color = color;
    }


    /**
     * Getter method for the color attribute.
     *
     * @return color of player's pawn
     */
    @Override
    public Colors getColor() {
        return color;
    }


    /**
     * Getter method for the chat attribute.
     *
     * @return chat instance for that player
     */
    @Override
    public Chat getChat() {
        return chat;
    }


    /**
     * Setter method for the chat attribute.
     *
     * @param chat of the player
     */
    @Override
    public void setChat(Chat chat) {
        this.chat = chat;
        this.chatThread = new Thread(chat);
        chatThread.start();
    }


    /**
     * Getter method for the viewChoice attribute.
     *
     * @return true if the user chose to play via Gui, false otherwise
     */
    @Override
    public boolean getViewChoice() {
        return viewChoice;
    }


    /**
     * Getter for the nickname attribute.
     *
     * @return a reference to the nickname
     */
    @Override
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
     * Getter for the server attribute.
     *
     * @return a reference to the stub
     */
    @Override
    public VirtualServer getServer() {
        return server;
    }


    /**
     * Getter for the idMatch attribute.
     *
     * @return the ID of the match the client has joined
     */
    @Override
    public int getIdMatch() {
        return idMatch;
    }


    /**
     * Setter method for the idMatch attribute
     *
     * @param idMatch is the ID match of the match the client has joined
     */
    @Override
    public void setIdMatch(int idMatch) {
        this.idMatch = idMatch;
    }

    
    /**
     * Creates a new thread which listens for strings in input,
     * a thread which checks wether the match is ended and runs the CLI.
     */
    public void run() {
        new Thread(() -> {
            try {
                String messageValue;
                while (!matchEnded && (messageValue = input.readLine()) != null) {
                    messageQueue.put(messageValue); // Add the message to the queue
                }
            } catch (IOException | InterruptedException e) {
                if (!matchEnded) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }
            }
        }).start();
        new Thread(this::runReceiver).start();
        new Thread(this::checkMatchEnd).start();
        chatThread = new Thread(chat);
        chatThread.start();
        if (!viewChoice) runCli();
    }


    /**
     * Runs a thread which takes strings from the messageQueue and puts values in the right queue.
     */
    public void runReceiver() {
        String messageValue;
        while (!matchEnded) {
            try {
                messageValue = messageQueue.take();
                switch (messageValue) {
                    case "BOOLEAN" -> booleanQueue.put(messageQueue.take());
                    case "INTEGER" -> integerQueue.put(messageQueue.take());
                    case "POSITIONS" -> integersQueue.put(messageQueue.take());
                    case "UPDATE" -> updateQueue.put(messageQueue.take());
                    case "COLORS" -> colorsQueue.put(messageQueue.take());
                    case "STRING" -> stringQueue.put(messageQueue.take());
                    case "NAMES" -> stringsQueue.put(messageQueue.take());
                    case "TABLE" -> tableQueue.put(messageQueue.take());
                    case "CONTROLLER" -> controllerQueue.put(messageQueue.take());
                    case "CARDS" -> cardsQueue.put(messageQueue.take());
                    case "CHAT" -> {
                        String sender = messageQueue.take();
                        String chatMessage = messageQueue.take();
                        onChatMessage(sender, chatMessage);
                    }
                    case "PRIVATE CHAT" -> {
                        String sender = messageQueue.take();
                        String chatMessage = messageQueue.take();
                        onPrivateChatMessage(sender, chatMessage);
                    }
                    case "TURN" -> {
                        System.out.println(YELLOW + "\n\n---------------------------------- YOUR TURN ----------------------------------" + RESET);
                        System.out.print("\nInput command: ");
                    }
                    case "PING" -> pong();
                    case "EXIT" -> forceExit(messageQueue.take());
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    
    /**
     * Runs the CLI for this client.
     */
    private void runCli() {
        Scanner in = new Scanner(System.in);
        first = firstPlayer(); //Checks if this client is the first to join the game
        if (first) {
            chooseNumOfPlayers();
            nickname = chooseName();
            idMatch = addMatch(nickname);
        }
        else {
            boolean mustWait = true;
            while (mustWait) {
                nickname = chooseName();
                idMatch = addPlayer(nickname);
                if (idMatch >= 0)
                    mustWait = false;
                else System.out.println(RED + "\nAnother user is creating the first match, please retry." + RESET);
            }
        }

        boolean allConnected = allConnected(idMatch);
        if(!allConnected) System.out.println(BLUE + "\nWait for all other clients to connect" + RESET);
        while (!allConnected) {
            allConnected = allConnected(idMatch);
        }
        System.out.println(YELLOW + "\n\n---------------------------------- OK, LET'S START THE GAME! ----------------------------------\n" + RESET);

        placeStarterCard();
        chooseColor();
        drawCardsAndPlaceCommonObjectives();
        chooseSecretObjectives();
        assignBlackPawn();

        //The game begins, every client can now send commands to the server
        String command = "";
        boolean isMyTurn; //Checks if it is my turn
        boolean isValid; //Checks wether the entered command is valid in this phase of the game
        boolean warningDisplayed = false;

        while (!matchEnded) {
            boolean isLastRound = isLastRound(idMatch);
            if (isLastRound && !warningDisplayed && !command.equalsIgnoreCase("place")) {
                System.out.println(RED + "\nThe next is going to be your last turn." + RESET);
                warningDisplayed = true;
            }
            System.out.print("\nInput command: ");
            command = in.nextLine();
            isMyTurn = checkTurn();

            if (!isMyTurn && !finishedToPlay && !command.equalsIgnoreCase("chat")
                    && !command.equalsIgnoreCase("hand") && !command.equalsIgnoreCase("table")
                    && !command.equalsIgnoreCase("area") && !command.equalsIgnoreCase("secret")
                    && !command.equalsIgnoreCase("help")) {
                System.out.println(RED + "\nWait for your turn." + RESET);
            }
            else {

                isValid = checkCommand(idMatch, nickname, command);

                if (!isValid) {
                    System.out.println(RED + "\nSorry, this command is not valid." + RESET);
                }

                else {

                    if (command.equalsIgnoreCase("hand")) {
                        server.viewHand(idMatch, nickname);
                        waitForUpdate();

                    } else if (command.equalsIgnoreCase("table")) {
                        server.viewTable(idMatch, nickname);
                        waitForUpdate();

                    } else if (command.equalsIgnoreCase("area")) {
                        System.out.print("\nWhose play area would you like to check? Type 'me' for your own area: ");
                        String areaOwner;
                        boolean present;
                        do {
                            areaOwner = in.nextLine();
                            if (areaOwner.equalsIgnoreCase("me"))   areaOwner = this.nickname;
                            present = checkPlayer(idMatch, areaOwner);
                            if (!present)   System.out.print(RED + "\nPlayer not present in match, retry: " + RESET);
                        } while(!present);
                        server.viewPlayArea(idMatch, nickname, areaOwner);
                        waitForUpdate();

                    } else if (command.equalsIgnoreCase("secret")) {
                        server.viewSecretObjective(idMatch, nickname);
                        waitForUpdate();

                    } else if (command.equalsIgnoreCase("help")) {
                        System.out.println("""
                                \nCommands:
                                    place  -> place a card on your play area
                                    draw   -> draw a card from one of the decks or pick a card on the table
                                    chat   -> use the chat to send a message to everyone in your match or to a specific player
                                    hand   -> displays the cards in your hand
                                    table  -> displays the cards and decks on the table
                                    area   -> displays a player's current points and play area
                                    secret -> displays your secret objective card
                                    help   -> displays this message""");
                    }

                    //This cheat provides the player with 10000 resources per each symbol
                    if (command.equalsIgnoreCase("cheat1")) {
                        server.cheat1(idMatch, nickname);
                        System.out.println(YELLOW + "\nYou have obtained lots of resources!" + RESET);
                    }

                    //This cheat provides the player with 20 points
                    if (command.equalsIgnoreCase("cheat2")) {
                        server.cheat2(idMatch, nickname);
                        System.out.println(YELLOW + "\nYou now have 20 points!" + RESET);
                    }

                    //This cheat empties both the resource and the gold deck
                    if (command.equalsIgnoreCase("cheat3")) {
                        server.cheat3(idMatch);
                        System.out.println(YELLOW + "\nNow decks are empty!" + RESET);
                    }

                    if (command.equalsIgnoreCase("place") && !finishedToPlay) place();

                    else if (command.equalsIgnoreCase("draw") && !finishedToPlay) {
                        draw();
                        checkEndPhase();
                    }

                    else if (command.equalsIgnoreCase("chat")) {
                        useChat();
                    }
                }
            }

            manageEndgame(); //Manages the ending of the match
        }
    }


    /**
     * Lets the client choose how many players there will be in the match.
     */
    public void chooseNumOfPlayers() {
        int n;
        Scanner in = new Scanner(System.in);
        System.out.print(CYAN + "\nEnter the number of players for the new match: " + RESET);
        do { //Repeats until the user enters 2, 3 or 4
            try {
                n = in.nextInt();
                in.nextLine();
            } catch (InputMismatchException e) { n = -1; in.nextLine(); }
            if (n < 2 || n > 4)
                System.out.print(RED + "\nSorry, you need to type a number between 2 and 4: " + RESET);
        } while (n < 2 || n > 4);
        server.setNumbOfPlayers(n);
    }


    /**
     * Lets the client choose a nickname.
     *
     * @return the chosen nickname
     */
    public String chooseName() {
        Scanner in = new Scanner(System.in);
        boolean alreadyInUse;
        do { //Loops until a name not already in use is entered by the client
            System.out.print("\nPlease, choose a username: ");
            nickname = in.nextLine();
            alreadyInUse = checkName(nickname); //Checks if the nickname is already in use
            if (alreadyInUse)
                System.out.println(RED + "\nSorry, the username is already in use." + RESET);
        } while(alreadyInUse);
        return nickname;
    }


    /**
     * Verifies the validity of a client's nickname from the GUI.
     *
     * @param name is the entered name
     * @return true if the name is already in use, false otherwise
     */
    @Override
    public boolean chooseNameGui(String name) {
        boolean alreadyInUse;
        alreadyInUse = checkName(name); //Checks if the nickname is already in use
        if (!alreadyInUse) {
            nickname = name;
        }
        return alreadyInUse;
    }


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
        server.chat(idMatch, nickname, recipient, chatMessage);
    }


    /**
     * Connects the client to a specific IP address and port in order to start the GUI.
     *
     * @param ipAddress the client is going to connect to
     * @param port the client is going to connect to
     */
    @Override
    public void connectGui(String ipAddress, int port) {
        try {
            socket = new Socket(ipAddress, port);
            InputStreamReader socketRx = new InputStreamReader(socket.getInputStream());
            OutputStreamWriter socketTx = new OutputStreamWriter(socket.getOutputStream());
            input = new BufferedReader(socketRx);
            server = new ServerProxy(new BufferedWriter(socketTx));
        } catch (IOException e) { forceExit("\nConnection failed"); }
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
        server.checkName(nameToCheck);
        return waitForBoolean();
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
        server.checkCommand(idMatch, nickname, command);
        return waitForBoolean();
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
        server.checkPlayer(idMatch, nickname);
        return waitForBoolean();
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
        server.checkRecipient(idMatch, nickname, recipient);
        return waitForBoolean();
    }


    /**
     * Calls the respective method on VirtualServer.
     * Getter method for the server's controller attribute.
     *
     * @return the instance of the server's controller
     */
    @Override
    public GameController getController() {
        server.getController();
        return waitForController();
    }


    /**
     * Calls the respective method on VirtualServer.
     * Certifies if there is an open match.
     *
     * @return true if there isn't an open match
     */
    @Override
    public boolean firstPlayer() {
        server.firstPlayer();
        return waitForBoolean();
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
        server.addMatch(name);
        return waitForInteger();
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
        server.addPlayer(name);
        return waitForInteger();
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
        server.getTable(idMatch);
        return waitForTable();
    }


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
        server.getOrderedCardsList(idMatch, nickname);
        return waitForCards();
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
        server.getPlayerPoints(idMatch, nickname);
        return waitForInteger();
    }


    /**
     * Calls the respective method on VirtualServer.
     * The server adds the client to a list.
     */
    @Override
    public void connect() {}


    /**
     * Call the method on the server that places the card in the play area, the parameter values are selected by the gui
     *
     * @param cardSelected references to the index of the card which has been selected
     * @param faceSelected indicates of the card needs to be placed by the front or by the back
     * @param x is the first coordinate of the position where the player wants to place the card
     * @param y is the second coordinate of the position where the player wants to place the card
     */
    @Override
    public void placeGui(int cardSelected, String faceSelected, int x, int y) {
        if(checkCommand(idMatch, nickname, "place")){
            server.place(idMatch, nickname, cardSelected, faceSelected, x, y);
        }
    }


    /**
     * Calls the respective method present on VirtualServer.
     * Gets the turn phase to a player playing via GUI.
     *
     * @return the phase to the GUI
     */
    @Override
    public String getPhaseGui() {
        server.getPhaseGui(idMatch);
        return waitForString();
    }


    /**
     * Calls the respective method on VirtualServer.
     * Calls the respective getter method isYourTurn of the specified player.
     *
     * @return true if it is the turn of the specified player
     */
    @Override
    public boolean checkTurn() {
        server.checkTurn(idMatch, nickname);
        return waitForBoolean();
    }


    /**
     * Checks if the conditions to proceed to the last phase of the game are met, this method is used to let the GUI know when the last turn will be.
     * @return 0 if the completed round was the last, 1 if it was the second to last, 2 otherwise
     * @throws RemoteException in case of network errors
     */
    @Override
    public int checkEndPhaseGui() throws RemoteException {
        if (isLastRound(idMatch)) {
            finishedToPlay = true;
            //This was your last turn, wait for the end of the match, now you can only use the chat
            server.increaseNumOfPlayersThatHaveFinishedToPlay(idMatch);
            //Checks if every player concluded their last turn
            if (getNumOfPlayersThatHaveFinishedToPlay(idMatch) != getNumOfPlayers(idMatch)) {
                //Wait for other players to finish their last turn
            }
            return 0;
        }
        //Checks wether this is the last round
        if (!isLastRound(idMatch)) {
            int result = checkSecondToLastTurn(idMatch, nickname);
            //Conditions for the ending phase are met and the next player does not have the black pawn
            if (result == 1) {
                //Careful! This was your second to last turn!
                server.setSecondToLastRound(idMatch, true);
                return 1;
            }
            //Conditions for the ending phase are met and the next player does have the black pawn
            if (result == 2) {
                //Careful! This was your second to last turn!
                server.setLastRound(idMatch, true);
                return 1;
            }
        }
        if (isSecondToLastRound(idMatch) && isNextPlayerTheBlackPawn(idMatch, nickname)) {
            //Careful! This was your second to last turn!
            server.setLastRound(idMatch, true);
            return 1;
        }
        return 2;
    }


    /**
     * Calls the method checkTurn from VirtualServer.
     * Sets the specified player's turn as true and every other player's turn to false.
     */
    @Override
    public void itIsMyTurn() {
        server.itIsMyTurn(idMatch, nickname);
    }


    /**
     * Calls the method checkTurn from VirtualServer.
     * Calls the setter method for the attribute 'yourTurn'.
     *
     * @param value is the boolean to be put
     */
    @Override
    public void setYourTurn(boolean value) {
        server.setYourTurn(idMatch, nickname, value);
    }


    /**
     * Calls the respective getter method present in VirtualServer.
     *
     * @return the nickname of the first player of the match, the one owning the black pawn
     */
    @Override
    public String getNameOfThePlayerWithTheBlackPawn() {
        server.getNameOfThePlayerWithTheBlackPawn(idMatch);
        return waitForString();
    }


    /**
     * Calls the respective getter method present in VirtualServer.
     * Randomly assigns the black pawn.
     */
    @Override
    public void chooseTheBlackPawn() {
        server.chooseTheBlackPawn(idMatch);
    }


    /**
     * Calls the respective setter method present in VirtualServer.
     *
     * @param value true if the player is assigned with the black pawn
     */
    @Override
    public void setBlackPawnChosen(boolean value) {
        server.setBlackPawnChosen(idMatch, value);
    }


    /**
     * Calls the respective getter method present in VirtualServer.
     *
     * @return true if the black pawn has been assigned
     */
    @Override
    public boolean isBlackPawnChosen() {
        server.isBlackPawnChosen(idMatch);
        return waitForBoolean();
    }


    /**
     * Calls the respective drawResource method present on VirtualServer.
     * Lets the player draw a card from the resource deck.
     */
    @Override
    public void drawResource() {
        if(checkCommand(idMatch, nickname, "draw")){
            server.drawResource(idMatch, nickname);
        }
    }


    /**
     * Calls the respective drawGold method present on VirtualServer.
     * Lets the player draw a card from the gold deck.
     */
    @Override
    public void drawGold() {
        if(checkCommand(idMatch, nickname, "draw")){
            server.drawGold(idMatch, nickname);
        }
    }


    /**
     * Calls the respective drawTable method present on VirtualServer.
     * Lets the player draw a card from the table.
     *
     * @param cardSelected references to the index of the card which has been selected from the table
     */
    @Override
    public void drawTable(int cardSelected) {
        if(checkCommand(idMatch, nickname, "draw")){
            server.drawTable(idMatch, nickname, cardSelected);
        }
    }


    /**
     * Calls the respective method present in VirtualServer.
     * This method search for the nickname of every player in the current game.
     *
     * @return a list containing all players' nicknames
     */
    @Override
    public List<String> getPlayersNames() {
        server.getPlayersNames(idMatch);
        return waitForStrings();
    }


    /**
     * Lets the client choose the face of their starter card and places said card.
     */
    public void placeStarterCard() {
        Scanner in = new Scanner(System.in);
        if (first) server.shuffleDecksAndGiveStarterCards(idMatch);
        server.viewStartingTable(idMatch, nickname);
        waitForUpdate();
        server.viewHand(idMatch, nickname);
        waitForUpdate();

        //The user chooses the face of their starter card and place it on the play area
        System.out.print(CYAN + "\nChoose the face you prefer, type 'front' or 'back': " + RESET);
        String starterFace;
        do {
            starterFace = in.nextLine();
            if (!starterFace.equalsIgnoreCase("front") && !starterFace.equalsIgnoreCase("back")) {
                System.out.print(RED + "\nSorry, the selection is not valid, type again: " + RESET);
            }
        } while (!starterFace.equalsIgnoreCase("front") && !starterFace.equalsIgnoreCase("back"));
        server.place(idMatch, nickname, 1, starterFace, 2, 2);
        server.increaseNumOfStarterCardsPlaced(idMatch);
        waitForUpdate();

        //the user waits until every other player has placed their starter card
        boolean allPlaced = allStarterCardsPlaced(idMatch);
        if(!allPlaced)  System.out.println(BLUE + "\nWait for all other clients to place their starter card" + RESET );
        while(!allPlaced) {
            allPlaced = allStarterCardsPlaced(idMatch);
        }
    }


    /**
     * The client chooses the color for their pawn.
     */
    public void chooseColor() {
        Scanner in = new Scanner(System.in);
        if(first) server.itIsMyTurn(idMatch, nickname);
        boolean done = false;
        boolean canIchooseTheColor = canIChooseTheColor(idMatch, nickname);
        if(!canIchooseTheColor) System.out.println(BLUE + "\nWait for other Clients to choose their color" + RESET);
        do {
            canIchooseTheColor = canIChooseTheColor(idMatch, nickname);
            if (canIchooseTheColor) {
                Colors[] availableColors = showAvailableColors(idMatch);
                System.out.print("\nThese are the available colors: ");
                for (Colors color : availableColors) {
                    if (color != null) {
                        System.out.print(assignColorToColor(color) + color + RESET + " ");
                    }
                }
                System.out.println();
                boolean colorValid;
                do { //Continues until the chosen color is not already in use
                    Colors colorChosen;
                    do {
                        System.out.print("\nChoose a color: ");
                        String colorString = in.nextLine();
                        colorChosen = fromStringToColor(colorString);
                        if (colorChosen.equals(Colors.PURPLE)) {
                            System.out.println(RED + "\nSorry, you did not type a color, please try again" + RESET);
                        }
                    } while(colorChosen.equals(Colors.PURPLE));
                    colorValid = checkColor(idMatch, colorChosen); //Checks wether the color is already in use
                    if (!colorValid) {
                        System.out.println(RED + "\nSorry, the color chosen is already in use." + RESET);
                    }else{
                        color = colorChosen;
                        server.removeColorAndPassTurn(idMatch, nickname, colorChosen);
                        System.out.println("\nYou have chosen the color: " + colorChosen);
                        done = true;
                    }
                } while (!colorValid);
            }
        } while (!done);
    }


    /**
     * A player provides every other player with 2 resource cards, 1 gold card and places 2 objective cards on the table.
     */
    public void drawCardsAndPlaceCommonObjectives() {
        //Every player draws 2 resource cards and 1 gold card, while 2 common objective cards are placed on the table
        if(first) server.drawCardsAndPlaceCommonObjectives(idMatch);
        server.viewHand(idMatch, nickname);
        waitForUpdate();
        server.viewTable(idMatch, nickname);
        waitForUpdate();
        if(first) server.giveObjectives(idMatch); //Every player receives 2 objective cards
        server.viewChoiceObjectives(idMatch, nickname);
        waitForUpdate();
    }


    /**
     * The client chooses their secret objective.
     */
    public void chooseSecretObjectives() {
        //The player chooses which objective card will be their secret objective
        Scanner in = new Scanner(System.in);
        System.out.print(CYAN + "\nChoose the objective card that you prefer: " + RESET);
        int obSelected;
        do {
            try {
                obSelected = in.nextInt();
                in.nextLine();
            } catch (InputMismatchException e) { obSelected = -1; in.nextLine(); }
            if (obSelected < 1 || obSelected > 2) System.err.print("\nSorry, the selection is not valid, type again: ");
        } while (obSelected < 1 || obSelected > 2);
        server.putChoiceObjectives(idMatch, nickname, obSelected);
        server.increaseNumOfObjectiveCardChosen(idMatch);
        System.out.println(PURPLE + "\nYou have chosen the objective card number " + obSelected + RESET);
        boolean allChosen = allObjectiveCardsChosen(idMatch);
        if(!allChosen)
            System.out.println(BLUE + "\nWait for all other clients to choose their objective card" + RESET);
        while(true) { //Every player waits that every other player chooses their secret objective
            allChosen = allObjectiveCardsChosen(idMatch);
            if (allChosen) break;
        }
    }


    /**
     * The black pawn is randomly assigned.
     * If this client is assigned with the black pawn, they are the first to play.
     */
    public void assignBlackPawn() {
        if (first) {
            server.chooseTheBlackPawn(idMatch);
            server.setBlackPawnChosen(idMatch, true);
        }
        boolean blackPawnAssigned = isBlackPawnChosen();
        while (!blackPawnAssigned) {
            blackPawnAssigned = isBlackPawnChosen();
        }
        //The blackPawn attribute of the client bound to the player with the black pawn is set to true
        //and that client is going to start the match
        String playerWithBlackPawn = getNameOfThePlayerWithTheBlackPawn();
        if (nickname.equals(playerWithBlackPawn)) {
            blackPawn = true;
            server.itIsMyTurn(idMatch, nickname);
            System.out.println(YELLOW + "\n\n---------------------------------- YOU START ----------------------------------" + RESET);
        }
        //Every other client, whose blackPawn attribute remains false, sets their Player's yourTurn attribute to false
        if (this.blackPawn == false) server.setYourTurn(idMatch, nickname, false);
        //The nickname of the first player is displayed to every client
        if (!nickname.equals(playerWithBlackPawn)) {
            System.out.println(GREEN + "\nThe first player is: " + RESET + playerWithBlackPawn);
            System.out.println(RED + "\nWait for your turn." + RESET);
        }
    }


    /**
     * The client requests the server to place a card they choose.
     */
    public void place() {
        Scanner in = new Scanner(System.in);
        System.out.println("\nThis is your hand");
        server.viewHandAndArea(idMatch, nickname);
        waitForUpdate();
        int cardSelected;
        int x = 0, y = 0;
        boolean areCoordinatesValid;
        String faceSelected = null;
        String areYouSureToPlace;
        boolean requirementsRespected;
        boolean canIPlace;
        do {
            canIPlace = true;
            System.out.print(CYAN + "\nEnter 1, 2 or 3 to choose the respective card from your hand: " + RESET);
            try {
                cardSelected = in.nextInt();
                in.nextLine();
            } catch (InputMismatchException e) { cardSelected = -1; in.nextLine(); }
            if (cardSelected < 1 || cardSelected > 3) {
                System.out.println(RED + "\nSorry, the selection is not valid, try again." + RESET);
                canIPlace = false;
            }
            if (canIPlace) {
                try {
                    System.out.println(CYAN + "\nChoose where to place your card by typing the coordinates." + RESET);
                    System.out.print(PURPLE + "\nEnter the first coordinate (row): " + RESET);
                    x = in.nextInt();
                    in.nextLine();
                    System.out.print(YELLOW + "\nEnter the second coordinate (column): " + RESET);
                    y = in.nextInt();
                    in.nextLine();
                } catch (InputMismatchException e) { x = -1; in.nextLine(); }
                areCoordinatesValid = areCoordinatesValid(idMatch, nickname, x, y);
                if (!areCoordinatesValid) {
                    System.out.println(RED + "\nSorry, you can not place a card here, try again." + RESET);
                    canIPlace = false;
                }
                if (canIPlace) {
                    System.out.print(CYAN + "\nType 'front' or 'back' to place the card respectively by the front or the back: " + RESET);
                    faceSelected = in.nextLine().trim();
                    if (!faceSelected.equalsIgnoreCase("front") && !faceSelected.equalsIgnoreCase("back")) {
                        System.out.println(RED + "\nSorry, the selection is not valid, try again." + RESET);
                        canIPlace = false;
                    }
                    if (canIPlace) {
                        //If the player intends to place a gold card, checks if the requirements are met
                        if (faceSelected.equalsIgnoreCase("front")) {
                            requirementsRespected = canIPlaceTheGoldCard(idMatch, nickname, cardSelected);
                            if (!requirementsRespected) {
                                System.out.println(RED + "\nSorry, you do not have enough resources to place the card, retry" + RESET);
                                canIPlace = false;
                            }
                        }
                    }
                    if(canIPlace) {
                        //The player sees the impact their card positioning would have on their resources and objects
                        boolean acceptableAnswer = false;
                        server.viewDifferenceOfResources(idMatch, nickname, cardSelected, faceSelected, x, y);
                        waitForUpdate();
                        System.out.print(PURPLE + "\nType 'yes' if you are decided to place this card, otherwise type 'no': " + RESET);
                        do {
                            areYouSureToPlace = in.nextLine().trim();
                            if(areYouSureToPlace.equalsIgnoreCase("yes") || areYouSureToPlace.equalsIgnoreCase("y")) {
                                acceptableAnswer = true;
                            }
                            if(areYouSureToPlace.equalsIgnoreCase("no") || areYouSureToPlace.equalsIgnoreCase("n")) {
                                System.err.println("\nOk, choose another card, another position or another face");
                                acceptableAnswer = true;
                                canIPlace = false;
                            }
                            if(!acceptableAnswer) {
                                System.err.print("\nSorry, you have not entered yes or no, try again: ");
                            }
                        } while(!acceptableAnswer);
                    }
                }
            }
        } while (!canIPlace);
        server.place(idMatch, nickname, cardSelected, faceSelected, x, y);
        waitForUpdate();
    }


    /**
     * The client requests the server to draw the card they choose.
     */
    public void draw() {
        Scanner in = new Scanner(System.in);
        server.viewTable(idMatch, nickname);
        waitForUpdate();
        System.out.println("""

                    Enter:
                      - "resource" to draw a card from the resource deck
                      - "gold" to draw a card from the gold deck
                      - "table" to take a card present on the table""");
        String drawChoice;
        int whereCanIDraw = whereCanIDraw();
        /*
         * 0 if everything is fine
         * 1 if the Resource deck is empty
         * 2 if the Gold deck is empty
         * 3 if both decks are empty
         * 4 if everything is empty*/
        boolean drawnFromTheRightPlace;
        if (whereCanIDraw != 4) {
            do {
                drawnFromTheRightPlace = true;
                drawChoice = in.nextLine();
                if (!drawChoice.equalsIgnoreCase("resource")
                        && !drawChoice.equalsIgnoreCase("gold")
                        && !drawChoice.equalsIgnoreCase("table")) {
                    System.out.print(RED + "\nSorry, the command is not valid, type again: " + RESET);
                }
                if (drawChoice.equalsIgnoreCase("resource") && (whereCanIDraw == 1)) {
                    System.out.print(RED + "\nSorry, this deck is empty, type 'gold' or 'table': " + RESET);
                    drawnFromTheRightPlace = false;
                }
                if (drawChoice.equalsIgnoreCase("gold") && (whereCanIDraw == 2)) {
                    System.out.print(RED + "\nSorry, this deck is empty, type 'resource' or 'table': " + RESET);
                    drawnFromTheRightPlace = false;
                }
                if ((drawChoice.equalsIgnoreCase("gold") || drawChoice.equalsIgnoreCase("resource")) && (whereCanIDraw == 3)) {
                    System.out.print(RED + "\nSorry, this deck is empty, type 'table': " + RESET);
                    drawnFromTheRightPlace = false;
                }
            } while ((!drawChoice.equalsIgnoreCase("resource")
                    && !drawChoice.equalsIgnoreCase("gold")
                    && !drawChoice.equalsIgnoreCase("table"))
                    || !drawnFromTheRightPlace);
            if (drawChoice.equalsIgnoreCase("resource")) {
                server.drawResource(idMatch, nickname);
                waitForUpdate();
            }
            else if (drawChoice.equalsIgnoreCase("gold")) {
                server.drawGold(idMatch, nickname);
                waitForUpdate();
            }
            else if (drawChoice.equalsIgnoreCase("table")) {
                System.out.print("\nType 1, 2, 3 or 4 to choose the respective card from the table: ");
                int cardSelected;
                List<Integer> availablePositions = availablePositionsForDrawing();
                do {
                    try {
                        cardSelected = in.nextInt();
                        in.nextLine();
                    } catch (InputMismatchException e) {
                        cardSelected = -1;
                        in.nextLine();
                    }
                    if (cardSelected < 1 || cardSelected > 4) {
                        System.out.print(RED + "\nSorry, the selection is not valid, type again: " + RESET);
                    } else if (!availablePositions.contains(cardSelected)) {
                        System.out.print(RED + "\nSorry, there is not a card here, type again: " + RESET);
                    }
                } while (cardSelected < 1 || cardSelected > 4 || !availablePositions.contains(cardSelected));
                server.drawTable(idMatch, nickname, cardSelected);
                waitForUpdate();
            }
        }else{
            System.out.println(RED + "\nSorry, there is nothing to draw" + RESET);
            server.drawTable(idMatch, nickname, 5);
            waitForUpdate();
        }
        System.out.println(YELLOW + "\n---------------------------------- END OF YOUR TURN ----------------------------------\n" + RESET);
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
        server.whereCanIDraw(idMatch);
        return waitForInteger();
    }


    /**
     * Calls the respective method on VirtualServer.
     * Shows which positions on the table of a specific match have cards on them.
     *
     * @return a list with all the positions of the table with a card on them
     */
    @Override
    public List<Integer> availablePositionsForDrawing() {
        server.availablePositionsForDrawing(idMatch);
        return waitForIntegers();
    }


    /**
     * Checks if the conditions to proceed to the last phase of the game are met.
     */
    public void checkEndPhase() {
        //If this was the player's last turn, they are now only allowed use the chat
        boolean isLastRound = isLastRound(idMatch);
        if (isLastRound) {
            finishedToPlay = true;
            System.out.println(GREEN + "\nThis was your last turn, wait for the end of the match, now you can only use the chat" + RESET);
            server.increaseNumOfPlayersThatHaveFinishedToPlay(idMatch);
            //Checks if every player concluded their last turn
            int playersThatHaveFinished = getNumOfPlayersThatHaveFinishedToPlay(idMatch);
            int numOfPlayers = getNumOfPlayers(idMatch);
            if (playersThatHaveFinished != numOfPlayers) {
                System.out.println(PURPLE + "\nWait for other players to finish their last turn" + RESET);
            }
        }
        //Checks wether this is the last round
        else {
            int result = checkSecondToLastTurn(idMatch, nickname);
            //Conditions for the ending phase are met and the next player does not have the black pawn
            if (result == 1) {
                System.out.println(RED + "\nCareful! This was your second to last turn!" + RESET);
                server.setSecondToLastRound(idMatch, true);
            }
            //Conditions for the ending phase are met and the next player does have the black pawn
            if (result == 2) {
                System.out.println(RED + "\nCareful! This was your second to last turn!" + RESET);
                server.setLastRound(idMatch, true);
            }
        }
        boolean isSecondToLastRound = isSecondToLastRound(idMatch);
        boolean isNextPlayerBlackPawn = isNextPlayerTheBlackPawn(idMatch, nickname);
        if (isSecondToLastRound && isNextPlayerBlackPawn) {
            server.setLastRound(idMatch, true);
        }
    }


    /**
     * Lets the player write a message using the chat service.
     */
    public void useChat() {
        Scanner in = new Scanner(System.in);
        String recipient;
        System.out.print("\nSend message to?: ");
        boolean recipientValid;
        do {
            recipient = in.nextLine();
            recipientValid = checkRecipient(idMatch, recipient);
            if (!recipientValid) System.out.print(RED + "\nRecipient invalid, type again: " + RESET);
        } while (!recipientValid);
        String chatMessage;
        System.out.print("\nYour message: ");
        chatMessage = in.nextLine();
        server.chat(idMatch, nickname, recipient, chatMessage);
    }


    /**
     * Calculates the final score of the player and shows a "you won" or "you lost" message.
     * Finally, the connection for this client closes and the process terminates.
     */
    public void manageEndgame() {
        int playersThatHaveFinished = getNumOfPlayersThatHaveFinishedToPlay(idMatch);
        int numPlayers = getNumOfPlayers(idMatch);
        if (playersThatHaveFinished == numPlayers) {
            server.calculateObjectives(idMatch, nickname); //Calculates the points gained from the objectives
            waitForUpdate();
            server.increaseNumOfObjectiveCardCalculated(idMatch);
            boolean allCalculated = allObjectiveCardsCalculated(idMatch);
            while (!allCalculated) { //Waits until every player has updated their score
                allCalculated = allObjectiveCardsCalculated(idMatch);
            }
            if (first) server.calculateWinner(idMatch); //Chooses the winner
            server.showEndGameMessage(idMatch, nickname); //Displays the match final results
            waitForUpdate();
        }
    }


    /**
     * Adds a received public chat message to the blocking queue.
     *
     * @param sender      is the name of the sender
     * @param chatMessage is the message received
     */
    @Override
    public void onChatMessage(String sender, String chatMessage) {
        chat.addMessage("\n\n" + sender + " to everybody: " + chatMessage);
    }


    /**
     * Adds a received private chat message to the blocking queue.
     *
     * @param sender      is the name of the sender
     * @param chatMessage is the message received
     */
    @Override
    public void onPrivateChatMessage(String sender, String chatMessage) {
        chat.addMessage("\n\n" + PURPLE + "(PRIVATE) " + RESET + sender + " to you: " + chatMessage);
    }


    /**
     * Method called to get the messages received by the player
     *
     * @return the BlockingQueue containing all the messages
     */
    @Override
    public BlockingQueue<String> getMessagesQueueGui() {
        return chat.getMessageQueueGui();
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
        server.allConnected(idMatch);
        return waitForBoolean();
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
        server.allStarterCardsPlaced(idMatch);
        return waitForBoolean();
    }


    /**
     * Calls the respective getter method on VirtualServer.
     *
     * @param idMatch is the ID related to the specified match
     * @return an array of all the colors the player can choose from
     */
    @Override
    public Colors[] showAvailableColors(int idMatch) {
        server.showAvailableColors(idMatch);
        return waitForColors();
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
        server.canIChooseTheColor(idMatch, nickname);
        return waitForBoolean();
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
        server.checkColor(idMatch, colorChosen);
        return waitForBoolean();
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
        server.areCoordinatesValid(idMatch, nickname, x, y);
        return waitForBoolean();
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
        server.canIPlaceTheGoldCard(idMatch, nickname, cardSelected);
        return waitForBoolean();
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
        server.allObjectiveCardsChosen(idMatch);
        return waitForBoolean();
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
        server.checkSecondToLastTurn(idMatch, nickname);
        return waitForInteger();
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
        server.isLastRound(idMatch);
        return waitForBoolean();
    }


    /**
     * Calls the respective getter method on VirtualServer.
     *
     * @param idMatch is the ID related to the current match
     * @return true if the match is in its second to last round
     */
    @Override
    public boolean isSecondToLastRound(int idMatch) {
        server.isSecondToLastRound(idMatch);
        return waitForBoolean();
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
        server.isNextPlayerTheBlackPawn(idMatch, nickname);
        return waitForBoolean();
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
        server.getNumOfPlayersThatHaveFinishedToPlay(idMatch);
        return waitForInteger();
    }


    /**
     * Calls the respective getter method on VirtualServer.
     *
     * @param idMatch is the ID related to the current match
     * @return the number of players chosen by the creator of the match
     */
    @Override
    public int getNumOfPlayers(int idMatch) {
        server.getNumOfPlayers(idMatch);
        return waitForInteger();
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
        server.allObjectiveCardsCalculated(idMatch);
        return waitForBoolean();
    }


    /**
     * Calls the respective method present in VirtualServer.
     * Method used by the GUI to know if the player is the winner.
     *
     * @return true if the player is the winner, false otherwise
     */
    @Override
    public boolean endGameMessageGui() {
        server.endGameMessageGui(idMatch, nickname);
        return waitForBoolean();
    }


    /**
     * This thread checks every second if the match ended.
     * If so, it closes the connection for this client after a countdown of 3 seconds.
     * Finally, the client exits the application.
     */
    public void checkMatchEnd() {
        while (!matchEnded) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        try {
            server.removeClient(nickname);
            System.out.println("\nClosing connection in 5 seconds . . .");
            Thread.sleep(5000); //Waits for 5 seconds before closing the connection and the process
            if (socket != null) {
                socket.shutdownInput();
                socket.shutdownOutput();
            }
            if (input != null) input.close();
            if (output != null) output.close();
            if (socket != null) socket.close();
            System.out.println("\nConnection closed.");
        } catch (InterruptedException | IOException e) {
            Thread.currentThread().interrupt();
            System.err.println("Error closing client: " + e.getMessage());
        } finally {
            System.out.println(CYAN + "\n\n---------------------------------- THANK YOU FOR PLAYING! ----------------------------------" + RESET);
            System.out.println("\nExiting . . .");
            System.exit(0);  //Terminates
        }
    }


    /**
     * Answers to a ping request from the server,
     */
    @Override
    public void pong() {
        server.receivePong(nickname);
    }


    /**
     * Closes the connection for this client after a countdown of 3 seconds.
     */
    @Override
    public void closeConnection() throws RemoteException {
        matchEnded = true;
    }


    /**
     * Notifies a client that their turn just started.
     *
     * @param message brings the notification
     */
    @Override
    public void notifyTurn(Message message) {}


    /**
     * Forces the client to disconnect and to exit the application in case the match is aborted.
     *
     * @param details contains details about the occurred error
     */
    @Override
    public void forceExit(String details) {
        System.out.println(RED + "\n\nA client in your match left, match aborted" + RESET);
        matchEnded = true;
    }


    /**
     * Waits until a boolean is received from the client proxy.
     *
     * @return the received boolean
     */
    private boolean waitForBoolean() {
        try {
            String messageValue = booleanQueue.take();
            return Boolean.parseBoolean(messageValue);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }


    /**
     * Waits until an integer is received from the client proxy.
     *
     * @return the received integer
     */
    private int waitForInteger() {
        try {
            String messageValue = integerQueue.take();
            return Integer.parseInt(messageValue);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }


    /**
     * Waits until an integer list is received from the client proxy.
     *
     * @return the received integer list
     */
    private List<Integer> waitForIntegers() {
        try {
            String messageValue = integersQueue.take();
            return objectMapper.readValue(messageValue, new TypeReference<>() {});
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }


    /**
     * Waits until a MessageTCP type object is received from the client proxy.
     */
    private void waitForUpdate() {
        try {
            showUpdate(objectMapper.readValue(updateQueue.take(), Message.class));
        } catch (JsonProcessingException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }


    /**
     * Waits until a Colors array type object is received from the client proxy.
     *
     * @return the received Colors array
     */
    private Colors[] waitForColors() {
        try {
            return (objectMapper.readValue(colorsQueue.take(), Colors[].class));
        } catch (JsonProcessingException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }


    /**
     * Waits until a String type object is received from the client proxy.
     *
     * @return the received string
     */
    private String waitForString() {
        try {
            return stringQueue.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }


    /**
     * Waits until a string list is received from the client proxy.
     *
     * @return the received string list
     */
    private List<String> waitForStrings() {
        try {
            String messageValue = stringsQueue.take();
            if (!messageValue.matches("\\[\".*\"\\]")) {
                messageValue = messageValue.replaceAll("(\\w+)", "\"$1\"");
            }
            return objectMapper.readValue(messageValue, new TypeReference<>() {});
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }


    /**
     * Waits until a cards list is received from the client proxy.
     *
     * @return the received cards list
     */
    public Card[] waitForCards() {
        try {
            String messageValue = cardsQueue.take();
            return objectMapper.readValue(messageValue, new TypeReference<>() {
            });
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }


    /**
     * Waits until a Table type object is received from the client proxy.
     */
    private Table waitForTable() {
        try {
            return (objectMapper.readValue(tableQueue.take(), Table.class));
        } catch (JsonProcessingException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }


    /**
     * Waits until a Controller type object is received from the client proxy.
     */
    private GameController waitForController() {
        try {
            return (objectMapper.readValue(controllerQueue.take(), GameController.class));
        } catch (JsonProcessingException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }


    /**
     * Returns a color from a string.
     *
     * @param colorString is a string identifying a specific color
     * @return the color corresponding to the string
     */
    public Colors fromStringToColor(String colorString) {
        Colors k = Colors.PURPLE;
        if(colorString.equalsIgnoreCase("red")){
            k = Colors.RED;
        }
        if(colorString.equalsIgnoreCase("blue")){
            k = Colors.BLUE;
        }
        if(colorString.equalsIgnoreCase("yellow")){
            k = Colors.YELLOW;
        }
        if(colorString.equalsIgnoreCase("green")){
            k = Colors.GREEN;
        }
        return k;
    }


    /**
     * Displays an update of the model to this client, based on the update message received from the server.
     *
     * @param message contains the necessary data to display the update
     */
    @Override
    public void showUpdate(Message message) {
        switch (message.getStatus()) {
            case VIEW_STARTER_CARD, STARTER_CARD_PLACED -> {
                System.out.println("\nYour starter card:");
                System.out.println("\nStarter Card (no color)");
                System.out.println("\nThese are the symbols on the front face of the card:");
                System.out.printf("%-20s %-20s%n", assignColorToSymbol(message.getStarterCard().getSymbols()[0]) + message.getStarterCard().getSymbols()[0] + RESET, assignColorToSymbol(message.getStarterCard().getSymbols()[1]) + message.getStarterCard().getSymbols()[1] + RESET);
                System.out.printf("%-20s %-20s%n", assignColorToSymbol(message.getStarterCard().getSymbols()[2]) + message.getStarterCard().getSymbols()[2] + RESET, assignColorToSymbol(message.getStarterCard().getSymbols()[3]) + message.getStarterCard().getSymbols()[3] + RESET);
                System.out.println();
                System.out.println("\nThis is the list of symbols in the centre of your starter card:");
                for (int i = 0; i < message.getStarterCard().getCentre().length; i++) {
                    System.out.print(assignColorToSymbol(message.getStarterCard().getCentre()[i]) + message.getStarterCard().getCentre()[i] + RESET + " ");
                }
                System.out.print("\n");
                System.out.println("\nThese are the symbols on the back face of the card");
                System.out.printf("%-20s %-20s%n", assignColorToSymbol(message.getStarterCard().getDefaultSymbols()[0]) + message.getStarterCard().getDefaultSymbols()[0] + RESET, assignColorToSymbol(message.getStarterCard().getDefaultSymbols()[1]) + message.getStarterCard().getDefaultSymbols()[1] + RESET);
                System.out.printf("%-20s %-20s%n", assignColorToSymbol(message.getStarterCard().getDefaultSymbols()[2]) + message.getStarterCard().getDefaultSymbols()[2] + RESET, assignColorToSymbol(message.getStarterCard().getDefaultSymbols()[3]) + message.getStarterCard().getDefaultSymbols()[3] + RESET);
                System.out.println();
            }
            case VIEW_SECRET_OBJECTIVE -> {
                System.out.println("\nYour secret objective:");
                if(message.getObjective().getClass() == ObCardSymbols.class){
                    System.out.print("you gain " + YELLOW + message.getObjective().getPoints() + " points " + RESET + "for each " );
                    System.out.print(((ObCardSymbols) message.getObjective()).getSymbols().length + " ");
                    System.out.print(assignColorToSymbol(((ObCardSymbols) message.getObjective()).getSymbols()[0]) + ((ObCardSymbols) message.getObjective()).getSymbols()[0] + "S" + RESET + " on your play area");
                }
                if(message.getObjective().getClass() == ObCardPosition.class){
                    System.out.println("\nYou gain " + YELLOW + message.getObjective().getPoints() + " points " + RESET + "for each pattern on your play area like this: " );
                    printObCardPosition(((ObCardPosition) message.getObjective()).getObPositions()[0], ((ObCardPosition) message.getObjective()), 0);
                    printObCardPosition(((ObCardPosition) message.getObjective()).getObPositions()[1], ((ObCardPosition) message.getObjective()), 1);
                    printObCardPosition(((ObCardPosition) message.getObjective()).getObPositions()[2], ((ObCardPosition) message.getObjective()), 2);
                }
            }
            case VIEW_CHOICE_OBJECTIVES -> {
                System.out.println("\n\nYour secret objective choices are: ");
                if(message.getObchoice1().getClass() == ObCardSymbols.class){
                    System.out.print("\nCard 1: " + YELLOW + message.getObchoice1().getPoints() + " points " + RESET + "for each " );
                    System.out.print(((ObCardSymbols) message.getObchoice1()).getSymbols().length + " ");
                    System.out.print(assignColorToSymbol(((ObCardSymbols) message.getObchoice1()).getSymbols()[0]) + ((ObCardSymbols) message.getObchoice1()).getSymbols()[0] + "S" + RESET + " on your play area");
                    System.out.println();
                }
                if(message.getObchoice1().getClass() == ObCardPosition.class){
                    System.out.println("\nCard 1: " + YELLOW + message.getObchoice1().getPoints() + " points " + RESET + "for each pattern on your play area like this: " );
                    printObCardPosition(((ObCardPosition) message.getObchoice1()).getObPositions()[0], ((ObCardPosition) message.getObchoice1()), 0);
                    printObCardPosition(((ObCardPosition) message.getObchoice1()).getObPositions()[1], ((ObCardPosition) message.getObchoice1()), 1);
                    printObCardPosition(((ObCardPosition) message.getObchoice1()).getObPositions()[2], ((ObCardPosition) message.getObchoice1()), 2);
                }
                if(message.getObchoice2().getClass() == ObCardSymbols.class){
                    System.out.print("\nCard 2: " + YELLOW + message.getObchoice2().getPoints() + " points " + RESET + "for each " );
                    System.out.print(((ObCardSymbols) message.getObchoice2()).getSymbols().length + " ");
                    System.out.print(assignColorToSymbol(((ObCardSymbols) message.getObchoice2()).getSymbols()[0]) + ((ObCardSymbols) message.getObchoice2()).getSymbols()[0] + "S" + RESET + " on your play area");
                    System.out.println();
                }
                if(message.getObchoice2().getClass() == ObCardPosition.class){
                    System.out.println("\nCard 2: " + YELLOW + message.getObchoice2().getPoints() + " points " + RESET + "for each pattern on your play area like this: " );
                    printObCardPosition(((ObCardPosition) message.getObchoice2()).getObPositions()[0], ((ObCardPosition) message.getObchoice2()), 0);
                    printObCardPosition(((ObCardPosition) message.getObchoice2()).getObPositions()[1], ((ObCardPosition) message.getObchoice2()), 1);
                    printObCardPosition(((ObCardPosition) message.getObchoice2()).getObPositions()[2], ((ObCardPosition) message.getObchoice2()), 2);
                }
            }
            case VIEW_TABLE -> {
                if(!message.getDeckR().isEmpty()) {
                    System.out.println("\nThe top card on the Resource Deck is " + assignColorToColor(message.getDeckR().getFirst().getColors()) + message.getDeckR().getFirst().getColors() + RESET);
                }else{
                    System.out.println("\nThe resource deck is empty!");
                }
                if(!message.getDeckG().isEmpty()) {
                    System.out.println("\nThe top card on the Gold Deck is " + assignColorToColor(message.getDeckG().getFirst().getColors()) + message.getDeckG().getFirst().getColors() + RESET);
                }else{
                    System.out.println("\nThe gold deck is empty!");
                }
                System.out.println("\nThe two resource cards on the table are:");
                System.out.println();
                if(message.getCardsR()[0]!=null) {
                    System.out.println("Card 1: ");
                    System.out.printf("%-20s %-20s%n", assignColorToSymbol(message.getCardsR()[0].getSymbols()[0]) + message.getCardsR()[0].getSymbols()[0] + RESET, assignColorToSymbol(message.getCardsR()[0].getSymbols()[1]) + message.getCardsR()[0].getSymbols()[1] + RESET);
                    System.out.printf("%-20s %-20s%n", assignColorToSymbol(message.getCardsR()[0].getSymbols()[2]) + message.getCardsR()[0].getSymbols()[2] + RESET, assignColorToSymbol(message.getCardsR()[0].getSymbols()[3]) + message.getCardsR()[0].getSymbols()[3] + RESET);
                    System.out.println("Color: " + assignColorToColor(message.getCardsR()[0].getColors()) + message.getCardsR()[0].getColors() + RESET);
                    System.out.println(YELLOW + "Points: " + RESET + message.getCardsR()[0].getPoints());
                    System.out.println();
                    } else   System.out.println("Card 1: " + WHITE + "NO CARD HERE\n" + RESET);
                if(message.getCardsR()[1]!=null) {
                    System.out.println("Card 2: ");
                    System.out.printf("%-20s %-20s%n", assignColorToSymbol(message.getCardsR()[1].getSymbols()[0]) + message.getCardsR()[1].getSymbols()[0] + RESET, assignColorToSymbol(message.getCardsR()[1].getSymbols()[1]) + message.getCardsR()[1].getSymbols()[1] + RESET);
                    System.out.printf("%-20s %-20s%n", assignColorToSymbol(message.getCardsR()[1].getSymbols()[2]) + message.getCardsR()[1].getSymbols()[2] + RESET, assignColorToSymbol(message.getCardsR()[1].getSymbols()[3]) + message.getCardsR()[1].getSymbols()[3] + RESET);
                    System.out.println("Color: " + assignColorToColor(message.getCardsR()[1].getColors()) + message.getCardsR()[1].getColors() + RESET);
                    System.out.println(YELLOW + "Points: " + RESET + message.getCardsR()[1].getPoints());
                } else   System.out.println("Card 2: " + WHITE + "NO CARD HERE\n" + RESET);
                System.out.println("\n----------------------------------------------------------------------------------------------------------");

                System.out.println("\nThe two gold cards on the table are:");
                System.out.println();
                if(message.getCardsG()[0]!=null) {
                    System.out.println("Card 3: ");
                    System.out.printf("%-20s %-20s%n", assignColorToSymbol(message.getCardsG()[0].getSymbols()[0]) + message.getCardsG()[0].getSymbols()[0] + RESET, assignColorToSymbol(message.getCardsG()[0].getSymbols()[1]) + message.getCardsG()[0].getSymbols()[1] + RESET);
                    System.out.printf("%-20s %-20s%n", assignColorToSymbol(message.getCardsG()[0].getSymbols()[2]) + message.getCardsG()[0].getSymbols()[2] + RESET, assignColorToSymbol(message.getCardsG()[0].getSymbols()[3]) + message.getCardsG()[0].getSymbols()[3] + RESET);
                    System.out.println("Color: " + assignColorToColor(message.getCardsG()[0].getColors()) + message.getCardsG()[0].getColors() + RESET);
                    System.out.println(YELLOW + "Points: " + RESET + message.getCardsG()[0].getPoints());
                    String a1 = "";
                    String a2 = "";
                    if (message.getCardsG()[0].getPointsObject() == Symbols.EMPTY && message.getCardsG()[0].getPoints() == 2) {
                        a1 = "for each";
                        a2 = "corner covered";
                    }
                    if (message.getCardsG()[0].getPointsObject() != Symbols.EMPTY) {
                        a1 = "for each";
                        a2 = message.getCardsG()[0].getPointsObject().name();
                    }
                    if (!a1.isEmpty()) {
                        System.out.printf(a1);
                        System.out.printf(" " + a2);
                    }
                    System.out.print("\nRequirements for card 3: ");
                    System.out.print(GREEN + "Plants: " + message.getCardsG()[0].getRequirements().get(Symbols.PLANT) + RESET + "  ");
                    System.out.print(BLUE + "Animals: " + message.getCardsG()[0].getRequirements().get(Symbols.ANIMAL) + RESET + "  ");
                    System.out.print(PURPLE + "Insects: " + message.getCardsG()[0].getRequirements().get(Symbols.INSECT) + RESET + "  ");
                    System.out.println(RED + "Fungi: " + message.getCardsG()[0].getRequirements().get(Symbols.FUNGI) + RESET + "  ");
                } else   System.out.println("Card 3: " + WHITE + "NO CARD HERE\n" + RESET);

                if(message.getCardsG()[1]!=null) {
                    System.out.println("\nCard 4: ");
                    System.out.printf("%-20s %-20s%n", assignColorToSymbol(message.getCardsG()[1].getSymbols()[0]) + message.getCardsG()[1].getSymbols()[0] + RESET, assignColorToSymbol(message.getCardsG()[1].getSymbols()[1]) + message.getCardsG()[1].getSymbols()[1] + RESET);
                    System.out.printf("%-20s %-20s%n", assignColorToSymbol(message.getCardsG()[1].getSymbols()[2]) + message.getCardsG()[1].getSymbols()[2] + RESET, assignColorToSymbol(message.getCardsG()[1].getSymbols()[3]) + message.getCardsG()[1].getSymbols()[3] + RESET);
                    System.out.println("Color: " + assignColorToColor(message.getCardsG()[1].getColors()) + message.getCardsG()[1].getColors() + RESET);
                    System.out.println(YELLOW + "Points: " + RESET + message.getCardsG()[1].getPoints());
                    String b1 = "";
                    String b2 = "";
                    if (message.getCardsG()[1].getPointsObject() == Symbols.EMPTY && message.getCardsG()[1].getPoints() == 2) {
                        b1 = "for each";
                        b2 = "corner covered";
                    }
                    if (message.getCardsG()[1].getPointsObject() != Symbols.EMPTY) {
                        b1 = "for each";
                        b2 = message.getCardsG()[1].getPointsObject().name();
                    }
                    if (!b1.isEmpty()) {
                        System.out.printf(b1);
                        System.out.printf(" " + b2);
                    }
                    System.out.print("\nRequirements for card 4: ");
                    System.out.print(GREEN + "Plants: " + message.getCardsG()[1].getRequirements().get(Symbols.PLANT) + RESET + "  ");
                    System.out.print(BLUE + "Animals: " + message.getCardsG()[1].getRequirements().get(Symbols.ANIMAL) + RESET + "  ");
                    System.out.print(PURPLE + "Insects: " + message.getCardsG()[1].getRequirements().get(Symbols.INSECT) + RESET + "  ");
                    System.out.println(RED + "Fungi: " + message.getCardsG()[1].getRequirements().get(Symbols.FUNGI) + RESET + "  ");
                } else   System.out.println("\nCard 4: " + WHITE + "NO CARD HERE" + RESET);
                System.out.println("\n----------------------------------------------------------------------------------------------------------");

                System.out.println("\n\nThe two common objective cards on the table are:");
                for (ObjectiveCard card : message.getCardsO()) {
                    if (card.getClass() == ObCardSymbols.class) {
                        System.out.print("\nYou gain: " + YELLOW + card.getPoints() + " points " + RESET + "for each " );
                        System.out.print(((ObCardSymbols) card).getSymbols().length + " ");
                        System.out.print(((ObCardSymbols) card).getSymbols()[0] + "S on your play area");
                        System.out.println();
                    }
                    if (card.getClass() == ObCardPosition.class) {
                        System.out.println("\nYou gain: " + YELLOW + card.getPoints() + " points " + RESET + "for each pattern on your play area like this: " );
                        printObCardPosition(((ObCardPosition) card).getObPositions()[0], ((ObCardPosition) card), 0);
                        printObCardPosition(((ObCardPosition) card).getObPositions()[1], ((ObCardPosition) card), 1);
                        printObCardPosition(((ObCardPosition) card).getObPositions()[2], ((ObCardPosition) card), 2);
                    }
                }
            }
            case VIEW_STARTING_TABLE -> {
                System.out.println("\nThe top card on the Resource Deck is " + assignColorToColor(message.getColorsR()) + message.getColorsR() + RESET);
                System.out.println("\nThe top card on the Gold Deck is " + assignColorToColor(message.getColorsG()) + message.getColorsG() + RESET);

                System.out.println("\nThe two resource cards on the table are:");
                System.out.println();
                System.out.println("Card 1: ");
                System.out.printf("%-20s %-20s%n", assignColorToSymbol(message.getCardsR()[0].getSymbols()[0]) + message.getCardsR()[0].getSymbols()[0] + RESET, assignColorToSymbol(message.getCardsR()[0].getSymbols()[1]) + message.getCardsR()[0].getSymbols()[1] + RESET);
                System.out.printf("%-20s %-20s%n", assignColorToSymbol(message.getCardsR()[0].getSymbols()[2]) + message.getCardsR()[0].getSymbols()[2] + RESET, assignColorToSymbol(message.getCardsR()[0].getSymbols()[3]) + message.getCardsR()[0].getSymbols()[3] + RESET);
                System.out.println("Color: " + assignColorToColor(message.getCardsR()[0].getColors()) + message.getCardsR()[0].getColors() + RESET);
                System.out.println(YELLOW + "Points: " + RESET + message.getCardsR()[0].getPoints());
                System.out.println();
                System.out.println("Card 2: ");
                System.out.printf("%-20s %-20s%n", assignColorToSymbol(message.getCardsR()[1].getSymbols()[0]) + message.getCardsR()[1].getSymbols()[0] + RESET, assignColorToSymbol(message.getCardsR()[1].getSymbols()[1]) + message.getCardsR()[0].getSymbols()[1] + RESET);
                System.out.printf("%-20s %-20s%n", assignColorToSymbol(message.getCardsR()[1].getSymbols()[2]) + message.getCardsR()[1].getSymbols()[2] + RESET, assignColorToSymbol(message.getCardsR()[1].getSymbols()[3]) + message.getCardsR()[0].getSymbols()[3] + RESET);
                System.out.println("Color: " + assignColorToColor(message.getCardsR()[1].getColors()) + message.getCardsR()[1].getColors() + RESET);
                System.out.println(YELLOW + "Points: " + RESET + message.getCardsR()[1].getPoints());
                System.out.println("\n----------------------------------------------------------------------------------------------------------");

                System.out.println("\nThe two gold cards on the table are:");
                System.out.println();
                System.out.println("Card 3: ");
                System.out.printf("%-20s %-20s%n", assignColorToSymbol(message.getCardsG()[0].getSymbols()[0]) + message.getCardsG()[0].getSymbols()[0] + RESET, assignColorToSymbol(message.getCardsG()[0].getSymbols()[1]) + message.getCardsG()[0].getSymbols()[1] + RESET);
                System.out.printf("%-20s %-20s%n", assignColorToSymbol(message.getCardsG()[0].getSymbols()[2]) + message.getCardsG()[0].getSymbols()[2] + RESET, assignColorToSymbol(message.getCardsG()[0].getSymbols()[3]) + message.getCardsG()[0].getSymbols()[3] + RESET);
                System.out.println("Color: " + assignColorToColor(message.getCardsG()[0].getColors()) + message.getCardsG()[0].getColors() + RESET);
                System.out.println(YELLOW + "Points: " + RESET + message.getCardsG()[0].getPoints());
                String a1 = "";
                String a2 = "";
                if (message.getCardsG()[0].getPointsObject() == Symbols.EMPTY && message.getCardsG()[0].getPoints() == 2) {
                    a1 = "for each";
                    a2 = "corner covered";
                }
                if (message.getCardsG()[0].getPointsObject() != Symbols.EMPTY) {
                    a1 = "for each";
                    a2 = message.getCardsG()[0].getPointsObject().name();
                }
                if (!a1.isEmpty()) {
                    System.out.printf(a1);
                    System.out.printf(" " + a2);
                }

                System.out.print("\nRequirements for card 3: ");
                System.out.print(GREEN + "Plants: " + message.getCardsG()[0].getRequirements().get(Symbols.PLANT) + RESET + "  ");
                System.out.print(BLUE + "Animals: " + message.getCardsG()[0].getRequirements().get(Symbols.ANIMAL) + RESET + "  ");
                System.out.print(PURPLE + "Insects: " + message.getCardsG()[0].getRequirements().get(Symbols.INSECT) + RESET + "  ");
                System.out.println(RED + "Fungi: " + message.getCardsG()[0].getRequirements().get(Symbols.FUNGI) + RESET + "  ");

                System.out.println("\nCard 4: ");
                System.out.printf("%-20s %-20s%n", assignColorToSymbol(message.getCardsG()[1].getSymbols()[0]) + message.getCardsG()[1].getSymbols()[0] + RESET, assignColorToSymbol(message.getCardsG()[1].getSymbols()[1]) + message.getCardsG()[1].getSymbols()[1] + RESET);
                System.out.printf("%-20s %-20s%n", assignColorToSymbol(message.getCardsG()[1].getSymbols()[2]) + message.getCardsG()[1].getSymbols()[2] + RESET, assignColorToSymbol(message.getCardsG()[1].getSymbols()[3]) + message.getCardsG()[1].getSymbols()[3] + RESET);
                System.out.println("Color: " + assignColorToColor(message.getCardsG()[1].getColors()) + message.getCardsG()[1].getColors() + RESET);
                System.out.println(YELLOW + "Points: " + RESET + message.getCardsG()[1].getPoints());
                String b1 = "";
                String b2 = "";
                if (message.getCardsG()[1].getPointsObject() == Symbols.EMPTY && message.getCardsG()[1].getPoints() == 2) {
                    b1 = "for each";
                    b2 = "corner covered";
                }
                if (message.getCardsG()[1].getPointsObject() != Symbols.EMPTY) {
                    b1 = "for each";
                    b2 = message.getCardsG()[1].getPointsObject().name();
                }
                if (!b1.isEmpty()) {
                    System.out.printf(b1);
                    System.out.printf(" " + b2);
                }
                System.out.print("\nRequirements for card 4: ");
                System.out.print(GREEN + "Plants: " + message.getCardsG()[1].getRequirements().get(Symbols.PLANT) + RESET + "  ");
                System.out.print(BLUE + "Animals: " + message.getCardsG()[1].getRequirements().get(Symbols.ANIMAL) + RESET + "  ");
                System.out.print(PURPLE + "Insects: " + message.getCardsG()[1].getRequirements().get(Symbols.INSECT) + RESET + "  ");
                System.out.println(RED + "Fungi: " + message.getCardsG()[1].getRequirements().get(Symbols.FUNGI) + RESET + "  ");
                System.out.println("\n----------------------------------------------------------------------------------------------------------");
            }
            case IT_IS_YOUR_TURN -> System.out.println(YELLOW + "\n\n---------------------------------- YOUR TURN ----------------------------------" + RESET);
            case SHOW_POINTS -> System.out.println("\nYour final score is " + message.getPoints() + " points.");
            case I_HAVE_LOST -> {
                System.out.println(RED + "\n---------------------------------- SORRY, YOU LOST ----------------------------------" + RESET);
                matchEnded = true;
            }
            case I_HAVE_WON -> {
                System.out.println(GREEN + "\n---------------------------------- YOU WON, CONGRATULATIONS! ----------------------------------" + RESET);
                matchEnded = true;
            }
            case VIEW_AREA -> {
                System.out.println("\nPoints: " + message.getPoints());
                System.out.println("\n" + GREEN + message.getAreaOwner() + RESET + " play area:");
                for (int k = 1; k < message.getPlayArea().getGrid().length - 1; k++) {
                    System.out.print("  ");
                    System.out.print("    " + YELLOW + k + RESET + "  ");
                }
                System.out.println();
                for (int i = 1; i < message.getPlayArea().getGrid().length - 1; i++) {
                    System.out.print(YELLOW + i + RESET + " ");
                    for (int j = 1; j < message.getPlayArea().getGrid().length - 1; j++) {
                        if (message.getPlayArea().getGrid()[i][j] != null && message.getPlayArea().getGrid()[i][j].getCentre() == null) {
                            if (message.getPlayArea().getGrid()[i][j].getColors().equals(Colors.RED)) {
                                System.out.print(RED + "   RED   " + RESET);
                            }
                            if (message.getPlayArea().getGrid()[i][j].getColors().equals(Colors.BLUE)) {
                                System.out.print(BLUE + "  BLUE   " + RESET);
                            }
                            if (message.getPlayArea().getGrid()[i][j].getColors().equals(Colors.GREEN)) {
                                System.out.print(GREEN + "  GREEN  " + RESET);
                            }
                            if (message.getPlayArea().getGrid()[i][j].getColors().equals(Colors.PURPLE)) {
                                System.out.print(PURPLE + " PURPLE  " + RESET);
                            }
                        }
                        if (message.getPlayArea().getGrid()[i][j] != null && message.getPlayArea().getGrid()[i][j].getCentre() != null) {
                            System.out.print(YELLOW + " STARTER " + RESET);
                        }
                        if (message.getPlayArea().getGrid()[i][j] == null) {
                            boolean valid = areCoordinatesValid(idMatch, message.getAreaOwner(), i, j);
                            if (valid) System.out.print(CYAN + "    1    " + RESET);
                            else System.out.print(WHITE + "    0    " + RESET);
                        }
                    }
                    System.out.println();
                }
                System.out.print("\nYour available resources are: ");
                System.out.print(GREEN + "Plants: " + message.getAvailableResources().get(Symbols.PLANT) + RESET);
                System.out.print(BLUE + "  Animals: " + message.getAvailableResources().get(Symbols.ANIMAL) + RESET);
                System.out.print(PURPLE + "  Insects: " + message.getAvailableResources().get(Symbols.INSECT) + RESET);
                System.out.print(RED + "  Fungi: " + message.getAvailableResources().get(Symbols.FUNGI) + RESET);
                System.out.println();
            }
            case VIEW_HAND -> {
                System.out.println("\nYour cards:");
                for (int k = 0; k < message.getHand().size(); k++) {
                    if (message.getHand().get(k).getClass() == ResourceCard.class) {
                        System.out.println("\nResource card");
                        System.out.printf("%-20s %-20s%n", assignColorToSymbol(message.getHand().get(k).getSymbols()[0]) + message.getHand().get(k).getSymbols()[0] + RESET, assignColorToSymbol(message.getHand().get(k).getSymbols()[1]) + message.getHand().get(k).getSymbols()[1] + RESET);
                        System.out.printf("%-20s %-20s%n", assignColorToSymbol(message.getHand().get(k).getSymbols()[2]) + message.getHand().get(k).getSymbols()[2] + RESET, assignColorToSymbol(message.getHand().get(k).getSymbols()[3]) + message.getHand().get(k).getSymbols()[3] + RESET);
                        System.out.println("Color: " + assignColorToColor(message.getHand().get(k).getColors()) + message.getHand().get(k).getColors() + RESET);
                        System.out.println("Points: " + message.getHand().get(k).getPoints());
                    }
                    if (message.getHand().get(k).getClass() == GoldCard.class) {
                        System.out.println("\nGold card");
                        System.out.printf("%-20s %-20s%n", assignColorToSymbol(message.getHand().get(k).getSymbols()[0]) + message.getHand().get(k).getSymbols()[0] + RESET, assignColorToSymbol(message.getHand().get(k).getSymbols()[1]) + message.getHand().get(k).getSymbols()[1] + RESET);
                        System.out.printf("%-20s %-20s%n", assignColorToSymbol(message.getHand().get(k).getSymbols()[2]) + message.getHand().get(k).getSymbols()[2] + RESET, assignColorToSymbol(message.getHand().get(k).getSymbols()[3]) + message.getHand().get(k).getSymbols()[3] + RESET);
                        System.out.println("Color: " + assignColorToColor(message.getHand().get(k).getColors()) + message.getHand().get(k).getColors() + RESET);
                        System.out.println("Points: " + message.getHand().get(k).getPoints());
                        String a1 = "";
                        String a2 = "";
                        if (message.getHand().get(k).getPointsObject() == Symbols.EMPTY && message.getHand().get(k).getPoints() == 2) {
                            a1 = "for each";
                            a2 = "corner covered";
                        }
                        if (message.getHand().get(k).getPointsObject() != Symbols.EMPTY) {
                            a1 = "for each";
                            a2 = message.getHand().get(k).getPointsObject().name();
                        }
                        if (!a1.isEmpty()) {
                            System.out.println(a1);
                            System.out.println(a2);
                        }
                        System.out.print("\nRequirements: ");
                        System.out.print(GREEN + "Plants: " + ((GoldCard) message.getHand().get(k)).getRequirements().get(Symbols.PLANT) + RESET + "  ");
                        System.out.print(BLUE + "Animals: " + ((GoldCard) message.getHand().get(k)).getRequirements().get(Symbols.ANIMAL) + RESET + "  ");
                        System.out.print(PURPLE + "Insects: " + ((GoldCard) message.getHand().get(k)).getRequirements().get(Symbols.INSECT) + RESET + "  ");
                        System.out.println(RED + "Fungi: " + ((GoldCard) message.getHand().get(k)).getRequirements().get(Symbols.FUNGI) + RESET + "  ");
                    }
                    System.out.println("\n------------------------------------------------------------------------------");
                }
            }
            case VIEW_HAND_AND_AREA -> {
                System.out.println("\nYour cards:");
                for (int k = 0; k < message.getHand().size(); k++) {
                    if (message.getHand().get(k).getClass() == ResourceCard.class) {
                        System.out.println("\nResource card");
                        System.out.printf("%-20s %-20s%n", assignColorToSymbol(message.getHand().get(k).getSymbols()[0]) + message.getHand().get(k).getSymbols()[0] + RESET, assignColorToSymbol(message.getHand().get(k).getSymbols()[1]) + message.getHand().get(k).getSymbols()[1] + RESET);
                        System.out.printf("%-20s %-20s%n", assignColorToSymbol(message.getHand().get(k).getSymbols()[2]) + message.getHand().get(k).getSymbols()[2] + RESET, assignColorToSymbol(message.getHand().get(k).getSymbols()[3]) + message.getHand().get(k).getSymbols()[3] + RESET);
                        System.out.println("Color: " + assignColorToColor(message.getHand().get(k).getColors()) + message.getHand().get(k).getColors() + RESET);
                        System.out.println("Points: " + message.getHand().get(k).getPoints());
                    }
                    if (message.getHand().get(k).getClass() == GoldCard.class) {
                        System.out.println("\nGold card");
                        System.out.printf("%-20s %-20s%n", assignColorToSymbol(message.getHand().get(k).getSymbols()[0]) + message.getHand().get(k).getSymbols()[0] + RESET, assignColorToSymbol(message.getHand().get(k).getSymbols()[1]) + message.getHand().get(k).getSymbols()[1] + RESET);
                        System.out.printf("%-20s %-20s%n", assignColorToSymbol(message.getHand().get(k).getSymbols()[2]) + message.getHand().get(k).getSymbols()[2] + RESET, assignColorToSymbol(message.getHand().get(k).getSymbols()[3]) + message.getHand().get(k).getSymbols()[3] + RESET);
                        System.out.println("Color: " + assignColorToColor(message.getHand().get(k).getColors()) + message.getHand().get(k).getColors() + RESET);
                        System.out.println("Points: " + message.getHand().get(k).getPoints());
                        String a1 = "";
                        String a2 = "";
                        if (message.getHand().get(k).getPointsObject() == Symbols.EMPTY && message.getHand().get(k).getPoints() == 2) {
                            a1 = "for each";
                            a2 = "corner covered";
                        }
                        if (message.getHand().get(k).getPointsObject() != Symbols.EMPTY) {
                            a1 = "for each";
                            a2 = message.getHand().get(k).getPointsObject().name();
                        }
                        if (!a1.isEmpty()) {
                            System.out.println(a1);
                            System.out.println(a2);
                        }
                        System.out.print("\nRequirements: ");
                        System.out.print(GREEN + "Plants: " + ((GoldCard) message.getHand().get(k)).getRequirements().get(Symbols.PLANT) + RESET + "  ");
                        System.out.print(BLUE + "Animals: " + ((GoldCard) message.getHand().get(k)).getRequirements().get(Symbols.ANIMAL) + RESET + "  ");
                        System.out.print(PURPLE + "Insects: " + ((GoldCard) message.getHand().get(k)).getRequirements().get(Symbols.INSECT) + RESET + "  ");
                        System.out.println(RED + "Fungi: " + ((GoldCard) message.getHand().get(k)).getRequirements().get(Symbols.FUNGI) + RESET + "  ");
                    }
                    System.out.println("\n------------------------------------------------------------------------------");
                }
                System.out.println("\nPoints: " + message.getPoints());
                System.out.println("\n" + GREEN + message.getNickname() + RESET + " play area:");
                for (int k = 1; k < message.getPlayArea().getGrid().length - 1; k++) {
                    System.out.print("  ");
                    System.out.print("    " + YELLOW + k + RESET + "  ");
                }
                System.out.println();
                for (int i = 1; i < message.getPlayArea().getGrid().length - 1; i++) {
                    System.out.print(i + " ");
                    for (int j = 1; j < message.getPlayArea().getGrid().length - 1; j++) {
                        if (message.getPlayArea().getGrid()[i][j] != null && message.getPlayArea().getGrid()[i][j].getCentre() == null) {
                            if (message.getPlayArea().getGrid()[i][j].getColors().equals(Colors.RED)) {
                                System.out.print(RED + "   RED   " + RESET);
                            }
                            if (message.getPlayArea().getGrid()[i][j].getColors().equals(Colors.BLUE)) {
                                System.out.print(BLUE + "  BLUE   " + RESET);
                            }
                            if (message.getPlayArea().getGrid()[i][j].getColors().equals(Colors.GREEN)) {
                                System.out.print(GREEN + "  GREEN  " + RESET);
                            }
                            if (message.getPlayArea().getGrid()[i][j].getColors().equals(Colors.PURPLE)) {
                                System.out.print(PURPLE + " PURPLE  " + RESET);
                            }
                        }
                        if (message.getPlayArea().getGrid()[i][j] != null && message.getPlayArea().getGrid()[i][j].getCentre() != null) {
                            System.out.print(YELLOW + " STARTER " + RESET);
                        }
                        if (message.getPlayArea().getGrid()[i][j] == null) {
                            boolean valid = areCoordinatesValid(idMatch, message.getAreaOwner(), i, j);
                            if (valid) System.out.print(CYAN + "    1    " + RESET);
                            else System.out.print(WHITE + "    0    " + RESET);
                        }
                    }
                    System.out.println();
                }
                System.out.print("\nYour available resources are: ");
                System.out.print(GREEN + "Plants: " + message.getAvailableResources().get(Symbols.PLANT) + RESET);
                System.out.print(BLUE + "  Animals: " + message.getAvailableResources().get(Symbols.ANIMAL) + RESET);
                System.out.print(PURPLE + "  Insects: " + message.getAvailableResources().get(Symbols.INSECT) + RESET);
                System.out.print(RED + "  Fungi: " + message.getAvailableResources().get(Symbols.FUNGI) + RESET);
                System.out.println();
            }
            case VIEW_DIFFERENCES_OF_RESOURCES -> {
                System.out.print("\nYour available resources will transform\n · from this: ");
                System.out.print(GREEN + "Plants: " + message.getPlayArea().getAvailableResources().get(Symbols.PLANT) + RESET);
                System.out.print(BLUE + "  Animals: " + message.getPlayArea().getAvailableResources().get(Symbols.ANIMAL) + RESET);
                System.out.print(PURPLE + "  Insects: " + message.getPlayArea().getAvailableResources().get(Symbols.INSECT) + RESET);
                System.out.print(RED + "  Fungi: " + message.getPlayArea().getAvailableResources().get(Symbols.FUNGI) + RESET);
                System.out.print(YELLOW + "  Manuscript: "+ message.getPlayArea().getAvailableResources().get(Symbols.MANUSCRIPT) + RESET);
                System.out.print(YELLOW + "  Quill: "+ message.getPlayArea().getAvailableResources().get(Symbols.QUILL) + RESET);
                System.out.print(YELLOW + "  Inkwell: "+ message.getPlayArea().getAvailableResources().get(Symbols.INKWELL) + RESET);
                System.out.print("\n · to this:   ");
                System.out.print(GREEN + "Plants: " + message.getPlayArea().getPossibleFutureAvailableResources().get(Symbols.PLANT) + RESET);
                System.out.print(BLUE + "  Animals: " + message.getPlayArea().getPossibleFutureAvailableResources().get(Symbols.ANIMAL) + RESET);
                System.out.print(PURPLE + "  Insects: " + message.getPlayArea().getPossibleFutureAvailableResources().get(Symbols.INSECT) + RESET);
                System.out.print(RED + "  Fungi: " + message.getPlayArea().getPossibleFutureAvailableResources().get(Symbols.FUNGI) + RESET);
                System.out.print(YELLOW + "  Manuscript: "+ message.getPlayArea().getPossibleFutureAvailableResources().get(Symbols.MANUSCRIPT) + RESET);
                System.out.print(YELLOW + "  Quill: "+ message.getPlayArea().getPossibleFutureAvailableResources().get(Symbols.QUILL) + RESET);
                System.out.print(YELLOW + "  Inkwell: "+ message.getPlayArea().getPossibleFutureAvailableResources().get(Symbols.INKWELL) + RESET);
                System.out.println();
            }
            case ACTION_FAILED -> System.out.println("\n" + message.getArgs());
            default -> System.out.println("\nUpdate received: " + message.getArgs());
        }
    }


    /**
     * Displays an objective card pattern on the TUI.
     *
     * @param position indicates the column of the pattern matrix
     * @param card is the objective card to display
     * @param numRow indicates the row of the pattern matrix
     */
    public void printObCardPosition(int position, ObCardPosition card, int numRow){
        if(position==0){
            System.out.printf("%-7s %-7s %-7s%n",
                    assignColorToColor(card.getObColors()[numRow]) + card.getObColors()[numRow] + RESET,
                    "      0",
                    "      0"
            );
        }
        if(position==1){
            System.out.printf("%-7s %-7s %-7s%n",
                    "0",
                    assignColorToColor(card.getObColors()[numRow]) + card.getObColors()[numRow] + RESET,
                    "      0"
            );
        }
        if(position==2){
            System.out.printf("%-7s %-7s %-7s%n",
                    "0",
                    "0",
                    assignColorToColor(card.getObColors()[numRow]) + card.getObColors()[numRow] + RESET
            );
        }
    }


    /**
     * Assigns a color to every symbol.
     *
     * @param symbol is the symbol to color
     * @return the color related to the symbol
     */
    public String assignColorToSymbol(Symbols symbol){
        String color = WHITE;
        switch (symbol) {
            case PLANT ->  color = GREEN;
            case FUNGI ->  color = RED;
            case INSECT -> color = PURPLE;
            case ANIMAL -> color = BLUE;
            case QUILL, MANUSCRIPT, INKWELL -> color = YELLOW;
            case EMPTY, NOCORNER -> color = WHITE;
        }
        return color;
    }


    /**
     * Assigns a display color to every Colors enumeration.
     *
     * @param color is the color enumeration to which assign a color to display on the TUI
     * @return the resulting color
     */
    public String assignColorToColor(Colors color){
        String result = WHITE;
        switch (color) {
            case RED ->    result = RED;
            case BLUE ->   result = BLUE;
            case GREEN ->  result = GREEN;
            case PURPLE -> result = PURPLE;
            case YELLOW -> result = YELLOW;
        }
        return result;
    }
}