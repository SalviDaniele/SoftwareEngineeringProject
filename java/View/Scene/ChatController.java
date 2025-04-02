package View.Scene;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

/**
 * Controller for the FXML file ChatScene.fxml
 */
public class ChatController extends SceneController{

    /**
     * ListView that contains all players' names.
     */
    @FXML
    private ListView<String> playerListView;

    /**
     * VBox that contains all the messages of a private or global chat
     */
    @FXML
    private VBox vboxMessages;

    /**
     * AnchorPane that contains vboxMessages
     */
    @FXML
    private AnchorPane anchorPaneMessages;

    /**
     * ScrollPane that provides a scrollable view for the Chat
     */
    @FXML
    private ScrollPane scrollPane;

    /**
     * TextField that allow the player to write a message
     */
    @FXML
    private TextField inputTextField;

    /**
     * Button that allow a player to send a message
     */
    @FXML
    private Button sendButton;

    /**
     * Observable list of player names
     */
    ObservableList<String> players;

    /**
     * Blocking queue for chat messages
     */
    private BlockingQueue<String> messageQueue;

    /**
     * A map associating the player names with an observable list of their chat messages
     */
    private final Map<String, ObservableList<String>> playerChats = new HashMap<>();

    /**
     * Nickname of the currently selected player
     */
    private String currentPlayer = "";


    /**
     * Initializes the controller class. This method is automatically called when the FXML file is loaded for the first time.
     * It initializes the observable list containing all the players' names, the blocking queue containing all the unread messages,
     * the list view containing all the players' names and adds a listener, when an item is selected it displays that player's chat.
     * Starts a thread that processes incoming messages.
     */
    @FXML
    private void initialize() {
        super.setTarget();
        modifyStatus(target -> {
            try {
                players = FXCollections.observableArrayList(target.getPlayersNames());
                players.remove(target.getNickname());
                players.addFirst("Global");
                messageQueue = target.getMessagesQueueGui();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
        playerListView.setItems(players);
        for(String name : players){
            playerChats.put(name, FXCollections.observableArrayList());
        }

        // Change chat when a player is selected
        playerListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            currentPlayer = newValue;
            updateChatView();
        });

        // Setup send button
        sendButton.setOnAction(event -> sendMessage());

        // Setup input text field
        inputTextField.setOnAction(event -> sendMessage());

        //Dynamically extends the size of the vbox based on the number of messages in the chat
        vboxMessages.heightProperty().addListener((observable, oldValue, newValue) -> {
            anchorPaneMessages.setPrefHeight((Double) newValue);
        });

        // Thread for processing messages
        new Thread(this::processMessages).start();
    }


    /**
     * A method that checks if an incoming message is private
     *
     * @param msg is the incoming message
     * @return true if the message is private, false otherwise
     */
    private boolean isPrivateMessage(String msg){
        return msg.contains("Private message from");
    }


    /**
     * A method that extracts the body of the message from a private message
     *
     * @param msg is the complete message
     * @return the body of the message
     */
    private String extractChatMessageFromPrivateMessage(String msg) {
        int start = msg.indexOf(": ") + ": ".length();
        return msg.substring(start).trim();
    }


    /**
     * A method that extract the name of the sender from a private message
     *
     * @param msg is the incoming message
     * @return the name of the sender
     */
    private String extractSenderFromPrivateMessage(String msg) {
        int start = msg.indexOf("Private message from ") + "Private message from ".length();
        int end = msg.indexOf(": ");
        return msg.substring(start, end).trim();
    }


    /**
     * A method that extracts the body of the message from a public message
     *
     * @param msg is the complete message
     * @return the body of the message
     */
    private String extractChatMessageFromPublicMessage(String msg) {
        int start = msg.indexOf(" to everybody: ") + " to everybody: ".length();
        return msg.substring(start).trim();
    }


    /**
     * A method that extract the name of the sender from a public message
     *
     * @param msg is the incoming message
     * @return the name of the sender
     */
    private String extractSenderFromPublicMessage(String msg) {
        int start = msg.indexOf("Public message from ") + "Public message from ".length();
        int end = msg.indexOf(" to everybody: ");
        return msg.substring(start, end).trim();
    }


    /**
     * A method that extracts the body of the message from a message sent by the user
     *
     * @param msg is the complete message
     * @return the body of the message
     */
    private String extractMessageISent(String msg){
        int start = msg.indexOf("You: ") + "You: ".length();
        return msg.substring(start).trim();
    }


    /**
     * Method that sends the message to all the client connected to the current match.
     * At last, it calls a method to add the message to the map containing all the players and relative messages.
     */
    private void sendMessage() {
        String message = inputTextField.getText();
        if (!message.isEmpty() && currentPlayer != null && !currentPlayer.isEmpty()) {
            String recipient;
            if(!currentPlayer.equals("Global"))
                recipient = currentPlayer;
            else
                recipient = "all";
            modifyStatus(target -> {
                try {
                    target.sendMessageChatGui(recipient, message);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            });
            addMessage(currentPlayer, "You: " + message);
            inputTextField.clear();
        }
    }


    /**
     * Method to add a message to the map containing all the players and relative messages.
     * At last, it calls a method to display the message.
     *
     * @param player the name of the player the user is chatting with
     * @param message the message to add to the chat with the player
     */
    private void addMessage(String player, String message) {
        playerChats.computeIfAbsent(player, k -> FXCollections.observableArrayList()).add(message);
        if (player.equals(currentPlayer)) {
            displayMessage(message, message.startsWith("You: "));
        }
    }


    /**
     * This method displays a message in the current chat.
     * A message is encapsulated into a TextFlow, then added to the current chat's VBox.
     *
     * @param message is the message to display
     * @param isSentByUser is true if the message is sent by the user, is false if is a message received from another player.
     */
    private void displayMessage(String message, boolean isSentByUser) {
        Text text;
        if(isSentByUser)
            text = new Text(extractMessageISent(message));
        else
            text = new Text(message);
        TextFlow textFlow = new TextFlow(text);

        HBox messageBox = new HBox();
        if (isSentByUser) {
            messageBox.setStyle("-fx-alignment: center-right;");
            textFlow.setStyle("-fx-background-color: #84a1dd; -fx-background-radius: 10; -fx-padding: 10;");
        } else {
            messageBox.setStyle("-fx-alignment: center-left;");
            textFlow.setStyle("-fx-background-color: #f8d7da; -fx-background-radius: 10; -fx-padding: 10;");
        }

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        if (isSentByUser) {
            messageBox.getChildren().addAll(spacer, textFlow);
        } else {
            messageBox.getChildren().addAll(textFlow, spacer);
        }

        Platform.runLater(() -> {
            vboxMessages.getChildren().add(messageBox);
            scrollPane.layout();
            scrollPane.setVvalue(1.0);
        });
    }


    /**
     * This method continuously retrieves messages from the message queue and process theme
     * based on whether they are private or public messages.
     * If a message is received privately the body of the message and the sender are extracted and the message
     * is added to the respective player's chat.
     * Public messages are formatted with sender information and added to the global chat.
     */
    private void processMessages() {
        try {
            while (true) {
                String message = messageQueue.take();
                if(isPrivateMessage(message)){
                    String player = extractSenderFromPrivateMessage(message);
                    String msg = extractChatMessageFromPrivateMessage(message);
                    addMessage(player, msg);
                }else{
                    String msg = extractSenderFromPublicMessage(message) + ": " + extractChatMessageFromPublicMessage(message);
                    addMessage("Global", msg);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


    /**
     * This method is called when a different player is picked in the ListView.
     * It updates the VBox displaying the messages contained in the chat selected.
     */
    private void updateChatView() {
        vboxMessages.getChildren().clear();
        if(currentPlayer != null){
            List<String> messages = playerChats.get(currentPlayer);
            if (messages != null) {
                for (String message : messages) {
                    displayMessage(message, message.startsWith("You: "));
                }
            }
        }
    }
}
