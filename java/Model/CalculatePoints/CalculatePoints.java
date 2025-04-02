package Model.CalculatePoints;

import Model.Cards.Card;
import Model.Player;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * this interface is implemented by the class PointsByPosition and PointsPerObject for calculating points
 * obtained by a player who has placed a card
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = PointsPerObject.class, name = "PointsPerObject"),
        @JsonSubTypes.Type(value = PointsByPosition.class, name = "PointsByPosition")
})
public interface CalculatePoints {

    /**
     * this is the main method that will be rewritten by subclasses
     * @param card is the card which has been placed.
     * @param currentPlayer is the player who just placed the card on their play area.
     */
    void calculatePoints(Card card, Player currentPlayer);
}
