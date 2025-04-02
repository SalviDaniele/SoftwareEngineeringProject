package Model.CalculatePoints;

import Model.Cards.Card;
import Model.Player;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * This class serves to calculate and to add the points of a just placed card to the score of the player who placed it.
 * This version of Model.CalculatePoints.CalculatePoints regards only the case in which the points granted by the card are a value
 * which depends on how many corners the placed card covers.
 */
@JsonSerialize
@JsonDeserialize
public class PointsByPosition implements CalculatePoints, Serializable {


    /**
     * The constructor of this version of CalculatePoints.
     */
    public PointsByPosition() {}


    /**
     * The method we use to update the score of the current player both in the specific player and the score track.
     * We search for the placed card in the grid and, once we find it, we count how many cards are covered by the 4 corners of the placed card.
     * The number of covered corners is then multiplied by the number on the card.
     * The result of this multiplication is the number of points which needs to be added to the player current score.
     * @param card is the card which has been placed.
     * @param currentPlayer is the player who just placed the card on their play area.
     */
    @Override
    public void calculatePoints(@NotNull Card card, @NotNull Player currentPlayer) {
        int coveredCorners = 0;
        //Explores the matrix until it finds the placed card
        for (int i = 0; i < currentPlayer.getPlayArea().getGrid().length; i++) {
            for (int j = 0; j < currentPlayer.getPlayArea().getGrid()[i].length; j++) {
                //As soon as the card is found, the cards on the corners are checked
                if (currentPlayer.getPlayArea().getGrid()[i][j] != null && currentPlayer.getPlayArea().getGrid()[i][j].equals(card)) {
                    for (int x = i-1; x <= i+1; x+=2) {
                        for (int y = j-1; y <= j+1; y+=2) {
                            if (currentPlayer.getPlayArea().getGrid()[x][y] != null) { coveredCorners++; }
                        }
                    }
                    int pointsToBeAdded = card.getPoints() * coveredCorners; //Multiplies the points reported on the card with the number of covered corners
                    currentPlayer.setPoints(currentPlayer.getPoints() + pointsToBeAdded); //Sums the result with the player's points
                    return;
                }
            }
        }
    }
}
