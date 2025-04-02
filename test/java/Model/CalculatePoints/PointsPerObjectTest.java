package Model.CalculatePoints;

import Model.Cards.Card;
import Model.Cards.Colors;
import Model.Cards.GoldCard;
import Model.Cards.Symbols;
import Model.PlayArea;
import Model.Player;
import Model.Table;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the calculation and the updating of one player's score in case of a card with an amount of points
 * which is variable on the number of times a particular object is present on the player's play area.
 */
class PointsPerObjectTest {

    /**
     * A reference to the placed card.
     */
    private Card card;

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
    private PointsPerObject pointsPerObject;

    /**
     * Before we test, we set up a new gold card, a table, a player and their play area, and pointsPerObject.
     */
    @BeforeEach
    void setUp() {
        Symbols[] symbols = {Symbols.PLANT, Symbols.ANIMAL, Symbols.FUNGI, Symbols.INSECT };
        Symbols[] centre = {Symbols.FUNGI};
        Map<Symbols, Integer> goldCardRequirements = new HashMap<>();
        goldCardRequirements.put(Symbols.FUNGI, 4);
        card = new GoldCard(symbols, Colors.RED, centre, true, 2, Symbols.INKWELL, null, null, goldCardRequirements);
        try {
            player = new Player("Federico", 1, new Table());
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
        playArea = new PlayArea(5, 5, player);
        playArea.getAvailableResources().replace(Symbols.INKWELL, 0, 3);
        playArea.getAvailableResources().replace(Symbols.QUILL, 0, 2);
        player.setPlayArea(playArea);
        table = player.getTable();
        table.getScore().put(player.getNickname(), player.getPoints());
        pointsPerObject = new PointsPerObject();
    }

    /**
     * We test if the method calculatePoints correctly update the player's points and their score on the score track.
     */
    @Test
    void updateScore() {
        pointsPerObject.calculatePoints(card, player);
        assertEquals(16, player.getPoints(), "The player's points should be 16.");
    }
}