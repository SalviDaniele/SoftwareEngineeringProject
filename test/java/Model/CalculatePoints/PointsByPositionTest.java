package Model.CalculatePoints;

import Model.Cards.*;
import Model.PlayArea;
import Model.Player;
import Model.Table;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests the calculation and the updating of one player's score in case of a card with an amount of points
 * which is variable on the number of corners the placed card covers.
 */
class PointsByPositionTest {

    /**
     * A reference to the table, where the score track is.
     */
    private Table table;

    /**
     * A reference to the player who placed the card.
     */
    private Player player;

    /**
     * A reference to the play area associated to the player who placed the card.
     */
    private PlayArea playArea;

    /**
     * The instance of the class PointsPerObject, the one we are testing.
     */
    private PointsByPosition pointsByPosition;

    /**
     * Before we test, we set up a table, a player and their play area, and pointsByPosition.
     */
    @BeforeEach
    void setUp() {
        try {
            player = new Player("Daniele", 1, new Table());
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
        player.setPoints(5);
        player.setPlayArea(new PlayArea(7, 7, player));
        playArea = player.getPlayArea();
        table = player.getTable();
        table.getScore().put(player.getNickname(), player.getPoints());
        pointsByPosition = new PointsByPosition();
    }

    /**
     * We test if the method calculatePoints correctly update the player's points and their score on the score track.
     */
    @Test
    void updateScore() {
        Symbols[] symbols = {Symbols.PLANT, Symbols.ANIMAL, Symbols.FUNGI, Symbols.INSECT };
        Symbols[] centre1 = {Symbols.INSECT};
        Symbols[] centre2 = {Symbols.ANIMAL};
        Symbols[] centre3 = {Symbols.FUNGI};
        Map<Symbols, Integer> goldCardRequirements = new HashMap<>();
        goldCardRequirements.put(Symbols.FUNGI, 2);
        Symbols[] starterCardDefaultSymbols = new Symbols[] {Symbols.FUNGI, Symbols.PLANT, Symbols.INSECT, Symbols.ANIMAL};
        Card starterCard = new StarterCard(symbols, Colors.NO_COLOR, centre1, true, 0, Symbols.EMPTY, null, null, starterCardDefaultSymbols);
        Card card1 = new ResourceCard(symbols, Colors.PURPLE, centre1, true, 1, Symbols.EMPTY, null, null);
        Card card2 = new ResourceCard(symbols, Colors.BLUE, centre2, false, 0, Symbols.EMPTY, null, null);
        Card placedCard = new GoldCard(symbols, Colors.RED, centre3, true, 2, Symbols.EMPTY, null, null, goldCardRequirements);
        playArea.getGrid()[3][3] = starterCard;
        playArea.getGrid()[2][2] = card1;
        playArea.getGrid()[2][4] = card2;
        playArea.getGrid()[1][3] = placedCard;
        starterCard.setCorners(new boolean[]{false, false, true, true});
        card1.setCorners(new boolean[]{true, false, true, true});
        card2.setCorners(new boolean[]{false, true, true, true});
        placedCard.setCorners(new boolean[]{true, true, true, true});
        player.setPlayArea(playArea);
        pointsByPosition.calculatePoints(placedCard, player);
        assertEquals(9, player.getPoints(), "The player's points should be 9.");
    }
}