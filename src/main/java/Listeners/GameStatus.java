package Listeners;

import java.io.Serializable;

/**
 * Enumerations representing an event or status in the game.
 * Serves as a mean to generate the relative message.
 */
public enum GameStatus implements Serializable {

    VIEW_STARTER_CARD ("VIEW STARTER CARD"),
    STARTER_CARD_PLACED ("STARTER CARD PLACED"),
    VIEW_SECRET_OBJECTIVE ("VIEW SECRET OBJECTIVE"),
    VIEW_TABLE ("VIEW TABLE"),
    VIEW_STARTING_TABLE("VIEW STARTING TABLE"),
    VIEW_CHOICE_OBJECTIVES("VIEW CHOICE OBJECTIVES"),
    VIEW_AREA ("VIEW AREA"),
    VIEW_HAND ("VIEW HAND"),
    VIEW_HAND_AND_AREA("VIEW HAND AND AREA"),
    VIEW_DIFFERENCES_OF_RESOURCES("VIEW DIFFERENCES OF RESOURCES"),
    IT_IS_YOUR_TURN ("IT IS YOUR TURN"),
    SHOW_POINTS("SHOW POINTS"),
    I_HAVE_WON("I HAVE WON"),
    I_HAVE_LOST("I HAVE LOST"),
    ACTION_FAILED("ACTION FAILED");


    /**
     * A string related to the status.
     */
    private final String value;


    /**
     * Constructor of the GameStatus class.
     *
     * @param value represents which status needs to be generated
     */
    GameStatus(String value) {
        this.value = value;
    }


    /**
     * Getter method for the value attribute.
     *
     * @return the string that represents which status needs to be generated
     */
    public String getValue(){
        return value;
    }
}
