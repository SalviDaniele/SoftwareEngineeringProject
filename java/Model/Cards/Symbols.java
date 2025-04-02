package Model.Cards;

import java.io.Serializable;

/**
 * this class is an enumeration of symbols that are inside each corner of a card and inside each card itself
 */
public enum Symbols implements Serializable{
    PLANT ("PLANT"),
    ANIMAL ("ANIMAL"),
    FUNGI ("FUNGI"),
    INSECT ("INSECT"),
    QUILL ("QUILL"),
    INKWELL ("INKWELL"),
    MANUSCRIPT ("MANUSCRIPT"),
    EMPTY ("EMPTY"),
    NOCORNER ("NOCORNER");

    /**
     * is the main attribute of this enumeration
     */
    private final String value;

    /**
     * constructor of the class Symbols
     * @param value is the symbol to be put
     */
    Symbols(String value) {
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