package Listeners;

import Model.Cards.StarterCard;
import Model.PlayArea;
import Model.Player;
import Network.VirtualServer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.rmi.RemoteException;

/**
 * Representation of an endpoint implementing the Listener interface.
 * An endpoint generates a message and sends the necessary data to the server.
 */
@JsonSerialize
@JsonDeserialize
public class EndPoint implements Listener, Serializable {

    /**
     * A reference to the virtual server that will receive the data.
     */
    private VirtualServer myServer;

    /**
     * Mapper which serializes the data.
     */
    private static final ObjectMapper objectMapper = new ObjectMapper();


    /**
     * Constructor for the EndPoint class.
     */
    public EndPoint() {}


    /**
     * Getter method for the server attribute.
     *
     * @return the virtual server linked to the listener
     */
    @Override
    public VirtualServer getServer() {
        return myServer;
    }


    /**
     * Setter method for the server attribute.
     *
     * @param server is the virtual server linked to the listener
     */
    @Override
    public void setServer(VirtualServer server) {
        this.myServer = server;
    }


    /**
     * Send model data to clients.
     *
     * @param status indicates the status or event of the game
     * @param player is the player who performed the action
     */
    @Override
    public void onNotify(GameStatus status, Player player) {
        try {
            String args = createArgs(player);
            Message message = switch (status) {
                case VIEW_STARTER_CARD -> new Message(args, status, player.getNickname(), (StarterCard) player.getHand().getFirst());
                case STARTER_CARD_PLACED -> new Message(args, status, player.getNickname(), (StarterCard) player.getPlayArea().getGrid()[2][2]);
                case VIEW_SECRET_OBJECTIVE -> new Message(args, status, player.getNickname(), player.getObjective());
                case VIEW_TABLE ->
                        new Message(args, status, player.getNickname(), player.getTable().getDeckR(), player.getTable().getDeckG(),
                                player.getTable().getCardsR(), player.getTable().getCardsG(), player.getTable().getCardsO());
                case VIEW_STARTING_TABLE ->
                        new Message(args, status, player.getNickname(), player.getTable().getDeckR().getFirst().getColors(),
                                player.getTable().getDeckG().getFirst().getColors(), player.getTable().getCardsR(), player.getTable().getCardsG());
                case VIEW_CHOICE_OBJECTIVES -> new Message(args, status, player.getNickname(), player.getObChoice1(), player.getObChoice2());
                case VIEW_DIFFERENCES_OF_RESOURCES -> new Message(args, status, player.getNickname(), player.getPlayArea());
                case IT_IS_YOUR_TURN, I_HAVE_WON, I_HAVE_LOST -> new Message(args, status, player.getNickname());
                case VIEW_AREA -> null;
                case VIEW_HAND -> new Message(args, status, player.getNickname(), player.getHand());
                case VIEW_HAND_AND_AREA -> new Message(args, status, player.getNickname(), player.getHand(), player.getPlayArea(), player.getPoints());
                case SHOW_POINTS -> new Message(args, status, player.getNickname(), player.getPoints());
                case ACTION_FAILED -> new Message(status, player.getNickname());
            };
            myServer.sendUpdate(message);
        } catch (RemoteException e) { throw new RuntimeException(e); }
    }


    /**
     * Send a play area data to clients.
     *
     * @param status indicates the status or event of the game
     * @param player is the player who requested to see the area
     * @param areaOwner is the nickname of the player who owns the play area on display
     * @param playArea is the area the player wants to see
     * @param points the points of the player who owns the play area
     */
    @Override
    public void onNotify(GameStatus status, Player player, String areaOwner, PlayArea playArea, int points) {
        String args = createArgs(player);
        Message message = new Message(args, status, player.getNickname(), areaOwner, playArea, points);
        try { myServer.sendUpdate(message); }
        catch (RemoteException e) { throw new RuntimeException(e); }
    }


    /**
     * Serializes the player data as a string using the mapper.
     *
     * @param player is the player to serialize
     * @return the player serialized as a string
     */
    private String createArgs(Player player) {
        try {
            return objectMapper.writeValueAsString(player);
        } catch (JsonProcessingException e) { throw new RuntimeException(e); }
    }
}
