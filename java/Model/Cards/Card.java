package Model.Cards;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;

/**
 * This class represents a generic card that can be a resource card ora a gold card or a starter card
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = GoldCard.class, name = "GoldCard"),
        @JsonSubTypes.Type(value = StarterCard.class, name = "StarterCard"),
        @JsonSubTypes.Type(value = ResourceCard.class, name = "ResourceCard")
})
public class Card implements Serializable {

    /**
     * symbols on the corners of the card
     */
    private Symbols[] symbols;

    /**
     * is an array of booleans used for checking if a corner of a card is covered by another card
     */
    private boolean[] corners;

    /**
     * the color of the card
     */
    private Colors colors;

    /**
     * array of symbols on the centre of the card
     */
    private Symbols[] centre;

    /**
     * this boolean is true if the card is not folded, otherwise is false
     */
    private boolean face;

    /**
     * the points of a card
     */
    private int points;

    /**
     * symbol used if with this card gain points for the number of this object on the PlayArea
     */
    private Symbols pointsObject;

    /**
     * boolean used for checking if a card has already been considered by an objective card
     */
    private boolean considered;

    /**
     * Indicates the row of the play area where the card is placed.
     */
    private double row;

    /**
     * Indicates the column of the play area where the card is placed.
     */
    private double column;

    /**
     * front image of the card
     */
    private String imageFront;

    /**
     * back image of the card
     */
    private String imageBack;

    /**
     * constructor of the class Card, corners are set 'true' by default
     * @param symbols are the symbols on the corners of the card
     * @param colors is the color of the card
     * @param centre is the array of symbols on the centre of the card
     * @param face is true if the card is not folded, otherwise is false
     * @param points are the points of a card
     * @param pointsObject is the symbol used if with this card gain points for the number of this object on the PlayArea
     * @param imageFront is the front image of the card
     * @param imageBack is the back image of the card
     */
    public Card(Symbols[] symbols, Colors colors, Symbols[] centre, boolean face, int points, Symbols pointsObject, String imageFront, String imageBack) {
        this.symbols = symbols;
        this.corners = new boolean[4];
        for (int i=0; i<4; i++) {
            this.corners[i] = true;
        }
        this.colors = colors;
        this.centre = centre;
        this.face = face;
        this.points = points;
        this.pointsObject = pointsObject;
        this.considered = false;
        this.imageFront = imageFront;
        this.imageBack = imageBack;
        this.row = -1;
        this.column = -1;
    }

    /**
     * generic constructor for this class
     */
    public Card() {}

    /**
     * getter method for the attribute 'symbols'
     * @return the attribute 'symbols'
     */
    public Symbols[] getSymbols() {
        return symbols;
    }

    /**
     * setter method for the attribute 'symbols'
     * @param symbols is the array of Symbols to be put
     */
    public void setSymbols(Symbols[] symbols) {
        this.symbols = symbols;
    }

    /**
     * getter method for the attribute 'corners'
     * @return the attribute 'corners'
     */
    public boolean[] getCorners() {
        return corners;
    }

    /**
     * setter method for the attribute 'corners'
     * @param corners is the array of booleans to be put
     */
    public void setCorners(boolean[] corners) {
        this.corners = corners;
    }

    /**
     * getter method for the attribute 'colors'
     * @return the attribute 'colors'
     */
    public Colors getColors() {
        return colors;
    }

    /**
     * setter method for the attribute 'colors'
     * @param colors is the color to be put
     */
    public void setColors(Colors colors) {
        this.colors = colors;
    }

    /**
     * getter method for the attribute 'centre'
     * @return the attribute 'centre'
     */
    public Symbols[] getCentre() {
        return centre;
    }

    /**
     * setter method for the attribute 'centre'
     * @param centre is the array of symbols to be put
     */
    public void setCentre(Symbols[] centre) {
        this.centre = centre;
    }

    /**
     * getter method for the attribute 'face'
     * @return the attribute 'face'
     */
    public boolean isFace() {
        return face;
    }

    /**
     * setter method for the attribute 'face'
     * @param face is the boolean to be put
     */
    public void setFace(boolean face) {
        this.face = face;
    }

    /**
     * getter method for the attribute 'points'
     * @return the attribute 'points'
     */
    public int getPoints() {
        return points;
    }

    /**
     * setter method for the attribute 'points'
     * @param points is the int to be put
     */
    public void setPoints(int points) {
        this.points = points;
    }

    /**
     * getter method for the attribute 'pointsObject'
     * @return the attribute 'pointsObject'
     */
    public Symbols getPointsObject() {
        return pointsObject;
    }

    /**
     * setter method for the attribute 'pointsObject'
     * @param pointsObject is the symbol to be put
     */
    public void setPointsObject(Symbols pointsObject) {
        this.pointsObject = pointsObject;
    }

    /**
     * getter method for the attribute 'considered'
     * @return the attribute 'considered'
     */
    public boolean isConsidered() {
        return considered;
    }

    /**
     * setter method for the attribute 'considered'
     * @param considered is the boolean to be put
     */
    public void setConsidered(boolean considered) {
        this.considered = considered;
    }

    /**
     * Getter method for the row attribute.
     *
     * @return row of the play area where the card is placed
     */
    public double getRow() {
        return row;
    }

    /**
     * Setter for the column attribute.
     *
     * @param row row of the play area where the card is placed
     */
    public void setRow(double row) {
        this.row = row;
    }

    /**
     * Getter method for the column attribute.
     *
     * @return column of the play area where the card is placed
     */
    public double getColumn() {
        return column;
    }

    /**
     * Setter for the column attribute.
     *
     * @param column column of the play area where the card is placed
     */
    public void setColumn(double column) {
        this.column = column;
    }

    /**
     * getter method for the attribute 'imageFront'
     * @return the attribute 'imageFront'
     */
    public String getImageFront() {
        return imageFront;
    }

    /**
     * setter method for the attribute 'imageFront'
     * @param imageFront is the String to be put
     */
    public void setImageFront(String imageFront) {
        this.imageFront = imageFront;
    }

    /**
     * getter method for the attribute 'imageBack'
     * @return the attribute 'imageBack'
     */
    public String getImageBack() {
        return imageBack;
    }

    /**
     * setter method for the attribute 'imageBack'
     * @param imageBack is the String to be put
     */
    public void setImageBack(String imageBack) {
        this.imageBack = imageBack;
    }
}



