package Network.TCP;

import Controller.GameController;
import Listeners.Message;
import Model.Cards.Card;
import Model.Cards.Colors;
import Model.Table;

import java.util.List;

/**
 * Represents a message the ClintProxy sends to the ClientTCP when using the TCP connection protocol.
 * The message could answer to client's request or could update they about changes in the Model or about a chat message.
 */
public class MessageTCP {

    /**
     * The header represents the type of message we have generated.
     */
    private MessageType header;

    /**
     * A boolean the message may contain, representing the answer to the client's request.
     */
    private boolean outcome;

    /**
     * An integer the message may contain, representing the answer to the client's request.
     */
    private int value;

    /**
     * A list of integers the message may contain, representing the answer to the client's request.
     */
    private List<Integer> positions;

    /**
     * An update message from the model the message may contain, representing the changes from
     * the Model that need to be sent to the client.
     */
    private Message update;

    /**
     * An array of colors the message may contain, representing the answer to the client's request.
     */
    private Colors[] colors;

    /**
     * A string the message may contain, representing the answer to the client's request.
     */
    private String string;

    /**
     * A list of strings the message may contain, representing the answer to the client's request.
     */
    private List<String> names;

    /**
     * An array of cards and their positions the message may contain, representing the answer to the client's request.
     */
    private Card[] cards;

    /**
     * A table the message may contain, representing the answer to the client's request.
     */
    private Table table;

    /**
     * A controller the message may contain, representing the answer to the client's request.
     */
    private GameController controller;

    /**
     * The name of a chat message sender the message may contain.
     */
    private String sender;

    /**
     * The chat message the message may contain
     */
    private String chatMessage;


    /**
     * Constructor for the MessageTCP class for the BOOLEAN type of message.
     *
     * @param header represents the message type
     * @param outcome is the boolean, representing the answer to the request
     */
    public MessageTCP(MessageType header, boolean outcome) {
        this.header = header;
        this.outcome = outcome;
    }


    /**
     * Constructor for the MessageTCP class for the INTEGER type of message.
     *
     * @param header represents the message type
     * @param value is the integer, representing the answer to the request
     */
    public MessageTCP(MessageType header, int value) {
        this.header = header;
        this.value = value;
    }


    /**
     * Constructor for the MessageTCP class for the UPDATE type of message.
     *
     * @param header represents the message type
     * @param update is the update message, containing data about the changes occurred on the Model
     */
    public MessageTCP(MessageType header, Message update) {
        this.header = header;
        this.update = update;
    }


    /**
     * Constructor for the MessageTCP class for the COLORS type of message.
     *
     * @param header represents the message type
     * @param colors available colors for the client
     */
    public MessageTCP(MessageType header, Colors[] colors) {
        this.header = header;
        this.colors = colors;
    }


    /**
     * Constructor for the MessageTCP class for the STRING and the EXIT types of message.
     *
     * @param header represents the message type
     * @param string is the string representing the answer to the request
     */
    public MessageTCP(MessageType header, String string) {
        this.header = header;
        this.string = string;
    }


    /**
     * Constructor for the MessageTCP class for the CHAT and the PRIVATE CHAT types of message.
     *
     * @param header represents the message type
     * @param sender of the chat message
     * @param chatMessage to send to the client
     */
    public MessageTCP(MessageType header, String sender, String chatMessage) {
        this.header = header;
        this.sender = sender;
        this.chatMessage = chatMessage;
    }


    /**
     * Constructor for the MessageTCP class for the TABLE type of message.
     *
     * @param header represents the message type
     * @param table reference to a table
     */
    public MessageTCP(MessageType header, Table table) {
        this.header = header;
        this.table = table;
    }


    /**
     * Constructor for the MessageTCP class for the CONTROLLER type of message.
     *
     * @param header represents the message type
     * @param controller reference to the GameController
     */
    public MessageTCP(MessageType header, GameController controller) {
        this.header = header;
        this.controller = controller;
    }


    /**
     * Constructor for the MessageTCP class for the TURN and the PING types of message.
     *
     * @param header represents the message type
     */
    public MessageTCP(MessageType header) {
        this.header = header;
    }


    /**
     * Getter method for the header attribute.
     *
     * @return the message type
     */
    public MessageType getHeader() {
        return header;
    }


    /**
     * Getter method for the outcome attribute.
     *
     * @return the boolean indicating the answer
     */
    public boolean isOutcome() {
        return outcome;
    }


    /**
     * Getter method for the value attribute.
     *
     * @return the integer indicating the answer
     */
    public int getValue() {
        return value;
    }


    /**
     * Getter method for the positions attribute.
     *
     * @return the list of integers indicating the answer
     */
    public List<Integer> getPositions() {
        return positions;
    }


    /**
     * Setter method for the positions attribute.
     *
     * @param positions the list of integers indicating the answer
     */
    public void setPositions(List<Integer> positions) {
        this.positions = positions;
    }


    /**
     * Getter method for the names attribute.
     *
     * @return the list of strings indicating the answer
     */
    public List<String> getNames() {
        return names;
    }


    /**
     * Setter method for the names attribute.
     *
     * @param names the list of strings indicating the answer
     */
    public void setNames(List<String> names) {
        this.names = names;
    }


    /**
     * Getter method for the cards attribute.
     *
     * @return the array of cards and their coordinates on the play area, indicating the answer
     */
    public Card[] getCards() {
        return cards;
    }


    /**
     * Setter method for the cards attribute.
     *
     * @param cards the array of cards indicating the answer
     */
    public void setCards(Card[] cards) {
        this.cards = cards;
    }


    /**
     * Getter method for the update attribute.
     *
     * @return the message containing the data from the Model
     */
    public Message getUpdate() {
        return update;
    }


    /**
     * Getter method for the colors attribute.
     *
     * @return the array of colors indicating the answer
     */
    public Colors[] getColors() {
        return colors;
    }


    /**
     * Getter method for the string attribute.
     *
     * @return the string indicating the answer
     */
    public String getString() {
        return string;
    }


    /**
     * Getter method for the table attribute.
     *
     * @return a reference to a table
     */
    public Table getTable() {
        return table;
    }


    /**
     * Getter method for the controller attribute.
     *
     * @return a reference to the GameController
     */
    public GameController getController() {
        return controller;
    }


    /**
     * Getter method for the sender attribute.
     *
     * @return the sender of the incoming chat message
     */
    public String getSender() {
        return sender;
    }


    /**
     * Getter method for the chatMessage attribute.
     *
     * @return the incoming chat message
     */
    public String getChatMessage() {
        return chatMessage;
    }
}
