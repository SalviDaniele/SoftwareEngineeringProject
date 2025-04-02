package Listeners;

import Model.PlayArea;
import Model.Player;
import Network.VirtualServer;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Interface for listeners, which listen to every action or event on the listened classes.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = EndPoint.class, name = "EndPoint")
})
public interface Listener {


    /**
     * Send model data to clients.
     *
     * @param status indicates the status or event of the game
     * @param player is the player who performed the action
     */
    void onNotify(GameStatus status, Player player);


    /**
     * Send a play area data to clients.
     *
     * @param status indicates the status or event of the game
     * @param player is the player who requested to see the area
     * @param areaOwner is the name of the player who owns the area on display
     * @param playArea is the area the player wants to see
     * @param points the points of the player who owns the play area
     */
    void onNotify(GameStatus status, Player player, String areaOwner, PlayArea playArea, int points);


    /**
     * Getter method for the server attribute.
     *
     * @return the virtual server linked to the listener
     */
    VirtualServer getServer();


    /**
     * Setter method for the server attribute.
     *
     * @param server is the virtual server linked to the listener
     */
    void setServer(VirtualServer server);
}
