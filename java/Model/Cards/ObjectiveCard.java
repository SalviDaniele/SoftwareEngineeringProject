package Model.Cards;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;


/**
 * this abstract class represents a generic objective card, this class is extended by the class ObCardPosition and the class ObCardSymbols
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ObCardSymbols.class, name = "obCardSymbols"),
        @JsonSubTypes.Type(value = ObCardPosition.class, name = "obCardPosition")
})
public abstract class ObjectiveCard implements Serializable {

    /**
     * this boolean indicates if an objective card is common to each player or if it belongs to a specific player
     */
    public boolean secret;

    /**
     * this attribute represents the points that may be given to a player that satisfy the requirements of an objective card
     */
    public int points;

    /**
     * if a card belongs only to a player and it is their secret objective this attribute is set to false
     */
    public boolean ob_face;

    /**
     * this is the front image of an objective card
     */
    protected String imageFront;

    /**
     * this is the back image of an objective card
     */
    protected String imageBack;

    /**
     * constructor of the class ObjectiveCard
     * @param points the points that may be given to a player that satisfy the requirements of an objective card
     * @param imageFront is the front image of the objective card
     * @param imageBack is the back image of the objective card
     */
    public ObjectiveCard(int points, String imageFront, String imageBack) {
        this.ob_face = true;
        this.secret = true;
        this.points = points;
        this.imageFront = imageFront;
        this.imageBack = imageBack;
    }

    /**
     * generic constructor of the class ObjectiveCard
     */
    protected ObjectiveCard() {}

    /**
     * getter method for the attribute 'secret'
     * @return the attribute 'secret'
     */
    public boolean isSecret() {
        return secret;
    }

    /**
     * setter method for the attribute 'secret'
     * @param secret is the boolean to be put
     */
    public void setSecret(boolean secret) {
        this.secret = secret;
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
     * @param points is the integer to be put
     */
    public void setPoints(int points) {
        this.points = points;
    }

    /**
     * getter method for the attribute 'ob_face'
     * @return the attribute 'ob_face'
     */
    public boolean isOb_face() {
        return ob_face;
    }

    /**
     * setter method for the attribute 'ob_face'
     * @param ob_face is the boolean to be put
     */
    public void setOb_face(boolean ob_face) {
        this.ob_face = ob_face;
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

    /**
     * method used for printing an objective card in a String
     * @return a String that represent an instance of this class
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Objective Card {")
                .append(", Face: ").append(ob_face)
                .append(", Points: ").append(points)
                .append(", Secret: ").append(secret)
                .append("}");
        return sb.toString();
    }
}
