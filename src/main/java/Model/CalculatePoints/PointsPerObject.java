package Model.CalculatePoints;

import Model.Cards.Card;
import Model.Cards.Symbols;
import Model.Player;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * This class serves to calculate and to add the points of a just placed card to the score of the player who placed it.
 * This version of Model.CalculatePoints.CalculatePoints regards only the case in which the points granted by the card are a value
 * which depends on the quantity of objects, the same type as indicated on the card, are currently present on the player's play area.
 */
@JsonSerialize
@JsonDeserialize
public class PointsPerObject implements CalculatePoints, Serializable {


    /**
     * The constructor of this version of CalculatePoints.
     */
    public PointsPerObject() {}


    /**
     * The method we use to update the score of the current player both in the specific player and the score track.
     * We search for the integer value associated with the object on the card. This indicates the quantity of that object
     * present on the play area at that moment and can be found in availableResources, a map present in the player's play area.
     * Next, we multiply that value by the number indicated by the card.
     * The result of the multiplication is the number of points which needs to be added to the player current score.
     * @param card is the card which has been placed.
     * @param currentPlayer is the player who just placed the card on their play area.
     */
    @Override
    public void calculatePoints(@NotNull Card card, @NotNull Player currentPlayer) {
        Symbols object = card.getPointsObject();
        int objectQuantity = currentPlayer.getPlayArea().getAvailableResources().get(object); //Finds the quantity for the searched object
        int pointsToBeAdded = card.getPoints() * objectQuantity; //Multiplies the points reported on the card with the object's quantity
        currentPlayer.setPoints(currentPlayer.getPoints() + pointsToBeAdded); //Sums the result with the player's points
    }

}
