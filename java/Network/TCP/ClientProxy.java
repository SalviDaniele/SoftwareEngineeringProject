package Network.TCP;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedWriter;
import java.io.PrintWriter;

/**
 * A proxy which, in accordance to the method the ClientHandler called, writes strings in output
 * to the client, which will then process the information.
 */
public class ClientProxy {

    /**
     * The output where to write the strings for the ClientTCP.
     */
    final PrintWriter output;

    /**
     * Mapper which serializes objects as strings for the client.
     */
    private ObjectMapper objectMapper;


    /**
     * Constructor for the ClientProxy class.
     *
     * @param output where to write the strings for the ClientTCP
     */
    public ClientProxy(BufferedWriter output) {
        this.output = new PrintWriter(output);
        this.objectMapper = new ObjectMapper();
    }


    /**
     * Notifies the client about an update from the model.
     *
     * @param message containing the update information from the model
     */
    public void notifyUpdate(MessageTCP message) {
        try {
            output.println(message.getHeader().getValue());
            output.println(objectMapper.writeValueAsString(message.getUpdate()));
            output.flush();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Notifies the client about the answer (in boolean form) to a request.
     *
     * @param message containing the boolean
     */
    public void notifyOutcome(MessageTCP message) {
        output.println(message.getHeader().getValue());
        output.println(message.isOutcome());
        output.flush();
    }


    /**
     * Notifies the client about the answer (in integer form) to a request.
     *
     * @param message containing the integer
     */
    public void notifyValue(MessageTCP message) {
        output.println(message.getHeader().getValue());
        output.println(message.getValue());
        output.flush();
    }


    /**
     * Notifies the client with a list of integers.
     *
     * @param message containing the list showing the positions on the table where there are cards
     */
    public void notifyPositionList(MessageTCP message) {
        output.println(message.getHeader().getValue());
        output.println(message.getPositions());
        output.flush();
    }


    /**
     * Notifies the client with a list of strings.
     *
     * @param message containing the list showing the names of the players
     */
    public void notifyNamesList(MessageTCP message) {
        output.println(message.getHeader().getValue());
        output.println(message.getNames());
        output.flush();
    }


    /**
     * Notifies the client with an array of cards and their positions.
     *
     * @param message containing the array showing the cards and their coordinates on the play area
     */
    public void notifyCardsList(MessageTCP message) {
        try {
            output.println(message.getHeader().getValue());
            output.println(objectMapper.writeValueAsString(message.getCards()));
            output.flush();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Notifies the client with an array of colors.
     *
     * @param message containing the array showing the available colors for the client
     */
    public void notifyColors(MessageTCP message) {
        try {
            output.println(message.getHeader().getValue());
            output.println(objectMapper.writeValueAsString(message.getColors()));
            output.flush();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Notifies the client with a serialized Table.
     *
     * @param message contains a reference to a table
     */
    public void notifyTable(MessageTCP message) {
        try {
            output.println(message.getHeader().getValue());
            output.println(objectMapper.writeValueAsString(message.getTable()));
            output.flush();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Notifies the client with a reference to the GameController.
     *
     * @param message contains a reference to the GameController
     */
    public void notifyController(MessageTCP message) {
        try {
            output.println(message.getHeader().getValue());
            output.println(objectMapper.writeValueAsString(message.getController()));
            output.flush();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Notifies the client with a string.
     *
     * @param message containing the string for the client
     */
    public void notifyString(MessageTCP message) {
        output.println(message.getHeader().getValue());
        output.println(message.getString());
        output.flush();
    }


    /**
     * Notifies the client, informing them that it is their turn.
     *
     * @param message to inform the client
     */
    public void notifyTurn(MessageTCP message) {
        output.println(message.getHeader().getValue());
        output.flush();
    }


    /**
     * Notifies the client that they received a chat message.
     *
     * @param message containing the sender and the chat message
     */
    public void notifyChat(MessageTCP message) {
        output.println(message.getHeader().getValue());
        output.println(message.getSender());
        output.println(message.getChatMessage());
        output.flush();
    }


    /**
     * Notifies the client to call a specific method, for example the pong method.
     *
     * @param message to inform the client
     */
    public void notifyMethodCall(MessageTCP message) {
        output.println(message.getHeader().getValue());
        output.flush();
    }
}
