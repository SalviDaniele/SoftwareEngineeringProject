package Listeners;

import Model.PlayArea;

/**
 * Every class extending this one provides data to all subscribed listeners.
 */
public abstract class Listened {


    /**
     * The subscribed listener.
     */
    protected Listener listener;


    /**
     * Notify the subscribed listener that the model has been updated.
     *
     * @param status indicates which kind of event occurred
     */
    abstract public void notifyListener(GameStatus status);


    /**
     * Notify the subscribed listener of the changes in a player's play area.
     *
     * @param status indicates which kind of event occurred
     * @param areaOwner is the nickname of the player who owns the play area on display
     * @param playArea is the area the player wants to see
     * @param points the points of the player who owns the play area
     */
    abstract public void notifyListener(GameStatus status, String areaOwner, PlayArea playArea, int points);


    /**
     * Add a subscribed listener.
     *
     * @param listener the listener that just subscribed themselves
     */
    abstract public void addListener(Listener listener);
}
