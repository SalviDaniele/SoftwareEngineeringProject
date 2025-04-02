package Model.Cards;

import java.io.Serializable;

/**
 * this enumeration represents all the colors that a card or a symbol or a pawn can have
 */
public enum Colors implements Serializable {

    RED ("RED"),
    GREEN ("GREEN"),
    PURPLE ("PURPLE"),
    BLUE ("BLUE"),
    YELLOW("YELLOW"),
    NO_COLOR ("NO_COLOR");

    /**
     * is the main attribute of this enumeration
     */
    private final String value;

    /**
     * constructor of the class Colors
     * @param value is the color to be put
     */
    Colors(String value) {
        this.value = value;
    }

    /**
     * getter method for the attribute 'value'
     * @return the attribute 'value'
     */
    public String getValue(){
        return value;
    }
}