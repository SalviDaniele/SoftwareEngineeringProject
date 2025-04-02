package Model.Cards;

import Model.PlayArea;
import Model.Player;
import Model.Table;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * tests for the class ObCardPosition
 */
class ObCardPositionTest {

    private ObCardPosition obCardPosition;

    /**
     * a reference to a player
     */
    private Player player;

    private PlayArea playArea;

    /**
     * before we test the player and its playArea are initialized
     */
    @BeforeEach
    void setUp() {
        try {
            player = new Player("Carlo", 1, new Table());
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
        player.setPlayArea(new PlayArea(8,8, player));
    }

    /**
     * testing if the method 'calculateObCardPosition' of the class works properly using three different
     * objective cards, with three different patterns
     */
    @Test
    void calculateObCardPosition() {
        try {
            player = new Player("Carlo", 1, new Table());
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
        player.setPlayArea(new PlayArea(8,8, player));
        Colors[] colors_red_red_green = {Colors.RED, Colors.RED, Colors.GREEN};
        int[] positions_red_red_green = {1,1,2};
        ObCardPosition ob_1 = new ObCardPosition(3, null, null, colors_red_red_green, positions_red_red_green);
        ob_1.setObColors(colors_red_red_green);
        Symbols[] centre_card1 = {Symbols.FUNGI};
        Symbols[] symbols_card1 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card1 = new ResourceCard(symbols_card1, Colors.RED, centre_card1, true, 0, null, null, null);
        Symbols[] centre_card2 = {Symbols.FUNGI};
        Symbols[] symbols_card2 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card2 = new ResourceCard(symbols_card2, Colors.RED, centre_card2, true, 0, null, null, null);
        Symbols[] centre_card3 = {Symbols.PLANT};
        Symbols[] symbols_card3 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card3 = new ResourceCard(symbols_card3, Colors.GREEN, centre_card3, true, 0, null, null, null);
        player.getPlayArea().getGrid()[2][2] = card1;
        player.getPlayArea().getGrid()[3][2] = card2;
        player.getPlayArea().getGrid()[4][3] = card3;
        ob_1.calculateObCardPosition(ob_1, player);
        assertEquals(3, player.getPoints(), "The player points should be 3");

        Colors[] colors_red_blue_green = {Colors.RED, Colors.BLUE, Colors.GREEN};
        int[] positions_red_blue_green = {1,1,2};
        ObCardPosition ob_2 = new ObCardPosition(3, null, null, colors_red_blue_green, positions_red_blue_green);
        Symbols[] centre_card_1 = {Symbols.FUNGI};
        Symbols[] symbols_card_1 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card_1 = new ResourceCard(symbols_card_1, Colors.RED, centre_card_1, true, 0, null, null, null);
        Symbols[] centre_card_2 = {Symbols.FUNGI};
        Symbols[] symbols_card_2 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card_2 = new ResourceCard(symbols_card_2, Colors.RED, centre_card_2, true, 0, null, null, null);
        Symbols[] centre_card_3 = {Symbols.PLANT};
        Symbols[] symbols_card_3 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card_3 = new ResourceCard(symbols_card_3, Colors.GREEN, centre_card_3, true, 0, null, null, null);
        player.getPlayArea().getGrid()[2][2] = card_1;
        player.getPlayArea().getGrid()[3][2] = card_2;
        player.getPlayArea().getGrid()[4][3] = card_3;
        player.setPoints(0);
        ob_2.calculateObCardPosition(ob_2, player);
        assertEquals(0, player.getPoints(), "The player points should be 0");

        Colors[] colors_green_green_green = {Colors.GREEN, Colors.GREEN, Colors.GREEN};
        int[] positions_green_green_green = {0,1,2};
        ObCardPosition ob_3 = new ObCardPosition(3, null, null, colors_green_green_green, positions_green_green_green);
        Symbols[] centre_card__1 = {Symbols.PLANT};
        Symbols[] symbols_card__1 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card__1 = new ResourceCard(symbols_card__1, Colors.GREEN, centre_card__1, true, 0, null, null, null);
        Symbols[] centre_card__2 = {Symbols.PLANT};
        Symbols[] symbols_card__2 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card__2 = new ResourceCard(symbols_card__2, Colors.GREEN, centre_card__2, true, 0, null, null, null);
        Symbols[] centre_card__3 = {Symbols.PLANT};
        Symbols[] symbols_card__3 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card__3 = new ResourceCard(symbols_card__3, Colors.GREEN, centre_card__3, true, 0, null, null, null);
        Symbols[] centre_card__4 = {Symbols.PLANT};
        Symbols[] symbols_card__4 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card__4 = new ResourceCard(symbols_card__4, Colors.GREEN, centre_card__4, true, 0, null, null, null);
        Symbols[] centre_card__5 = {Symbols.PLANT};
        Symbols[] symbols_card__5 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card__5 = new ResourceCard(symbols_card__5, Colors.GREEN, centre_card__5, true, 0, null, null, null);
        player.getPlayArea().getGrid()[1][1] = card__1;
        player.getPlayArea().getGrid()[2][2] = card__2;
        player.getPlayArea().getGrid()[3][3] = card__3;
        player.getPlayArea().getGrid()[4][4] = card__4;
        player.getPlayArea().getGrid()[5][5] = card__5;
        player.setPoints(0);
        ob_3.calculateObCardPosition(ob_3, player);
        assertEquals(3, player.getPoints(), "The player points should be 3");
        Symbols[] centre_card__6 = {Symbols.PLANT};
        Symbols[] symbols_card__6 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card__6 = new ResourceCard(symbols_card__6, Colors.GREEN, centre_card__6, true, 0, null, null, null);
        player.getPlayArea().getGrid()[6][6] = card__6;
        player.setPoints(0);
        card__1.setConsidered(false);
        card__2.setConsidered(false);
        card__3.setConsidered(false);
        ob_3.calculateObCardPosition(ob_3, player);
        assertEquals(6, player.getPoints(), "The player points should be 6");
    }

    /**
     * testing if the method 'where_to_continue' of the class works properly using three different array
     * of positions
     */
    @Test
    void where_to_continue() {
        try {
            player = new Player("Carlo", 1, new Table());
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
        player.setPlayArea(new PlayArea(8,8, player));
        Colors[] colors_red_red_green = {Colors.RED, Colors.RED, Colors.GREEN};
        int[] positions_red_red_green = {1,1,2};
        ObCardPosition ob_1 = new ObCardPosition(3, null, null, colors_red_red_green, positions_red_red_green);
        Symbols[] centre_card1 = {Symbols.FUNGI};
        Symbols[] symbols_card1 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card1 = new ResourceCard(symbols_card1, Colors.RED, centre_card1, true, 0, null, null, null);
        Symbols[] centre_card2 = {Symbols.FUNGI};
        Symbols[] symbols_card2 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card2 = new ResourceCard(symbols_card2, Colors.RED, centre_card2, true, 0, null, null, null);
        Symbols[] centre_card3 = {Symbols.PLANT};
        Symbols[] symbols_card3 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card3 = new ResourceCard(symbols_card3, Colors.GREEN, centre_card3, true, 0, null, null, null);
        player.getPlayArea().getGrid()[2][2] = card1;
        player.getPlayArea().getGrid()[3][2] = card2;
        player.getPlayArea().getGrid()[4][3] = card3;
        int k1 = ob_1.where_to_continue(ob_1, player, 2,2, 0, 1);
        assertEquals(0, k1, "k1 should be 0");
        int p1 = ob_1.where_to_continue(ob_1, player, 3,2, 1, 2);
        assertEquals(1, p1, "p1 should be 1");
        player.getPlayArea().getGrid()[3][2] = null;
        int k_1 = ob_1.where_to_continue(ob_1, player, 2,2, 0, 1);
        assertEquals(2, k_1, "k_1 should be 2");

        Colors[] colors_red_blue_green = {Colors.RED, Colors.BLUE, Colors.GREEN};
        int[] positions_red_blue_green = {1,1,2};
        ObCardPosition ob_2 = new ObCardPosition(3, null, null, colors_red_blue_green, positions_red_blue_green);
        Symbols[] centre_card_1 = {Symbols.FUNGI};
        Symbols[] symbols_card_1 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card_1 = new ResourceCard(symbols_card_1, Colors.RED, centre_card_1, true, 0, null, null, null);
        Symbols[] centre_card_2 = {Symbols.FUNGI};
        Symbols[] symbols_card_2 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card_2 = new ResourceCard(symbols_card_2, Colors.RED, centre_card_2, true, 0, null, null, null);
        Symbols[] centre_card_3 = {Symbols.PLANT};
        Symbols[] symbols_card_3 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card_3 = new ResourceCard(symbols_card_3, Colors.GREEN, centre_card_3, true, 0, null, null, null);
        player.getPlayArea().getGrid()[2][2] = card_1;
        player.getPlayArea().getGrid()[3][2] = card_2;
        player.getPlayArea().getGrid()[4][3] = card_3;
        int k2 = ob_2.where_to_continue(ob_2, player, 2,2, 0, 1);
        assertEquals(2, k2, "k2 should be 2");
        int p2 = ob_2.where_to_continue(ob_2, player, 3,2, 1, 2);
        assertEquals(1, p2, "p2 should be 1");

        Colors[] colors_green_green_green = {Colors.GREEN, Colors.GREEN, Colors.GREEN};
        int[] positions_green_green_green = {0,1,2};
        ObCardPosition ob_3 = new ObCardPosition(3, null, null, colors_green_green_green, positions_green_green_green);
        Symbols[] centre_card__1 = {Symbols.PLANT};
        Symbols[] symbols_card__1 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card__1 = new ResourceCard(symbols_card__1, Colors.GREEN, centre_card__1, true, 0, null, null, null);
        Symbols[] centre_card__2 = {Symbols.PLANT};
        Symbols[] symbols_card__2 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card__2 = new ResourceCard(symbols_card__2, Colors.GREEN, centre_card__2, true, 0, null, null, null);
        Symbols[] centre_card__3 = {Symbols.PLANT};
        Symbols[] symbols_card__3 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card__3 = new ResourceCard(symbols_card__3, Colors.GREEN, centre_card__3, true, 0, null, null, null);
        Symbols[] centre_card__4 = {Symbols.PLANT};
        Symbols[] symbols_card__4 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card__4 = new ResourceCard(symbols_card__4, Colors.GREEN, centre_card__4, true, 0, null, null, null);
        Symbols[] centre_card__5 = {Symbols.PLANT};
        Symbols[] symbols_card__5 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card__5 = new ResourceCard(symbols_card__5, Colors.GREEN, centre_card__5, true, 0, null, null, null);
        player.getPlayArea().getGrid()[2][2] = card__1;
        player.getPlayArea().getGrid()[3][3] = card__2;
        player.getPlayArea().getGrid()[4][4] = card__3;
        player.getPlayArea().getGrid()[5][5] = card__4;
        player.getPlayArea().getGrid()[6][6] = card__5;
        int k3 = ob_3.where_to_continue(ob_3, player, 2,2, 0, 1);
        assertEquals(1, k3, "k3 should be 1");
        int p3 = ob_3.where_to_continue(ob_3, player, 3,3, 1, 2);
        assertEquals(1, p3, "p3 should be 1");
        int l3 = ob_3.where_to_continue(ob_3, player, 3,3, 1, 2);
        assertEquals(1, l3, "l3 should be 1");
        int z3 = ob_3.where_to_continue(ob_3, player, 3,3, 1, 2);
        assertEquals(1, z3, "z3 should be 1");

        Colors[] colors_blue_blue_blue = {Colors.BLUE, Colors.BLUE, Colors.BLUE};
        int[] positions_blue_blue_blue = {2,1,0};
        ObCardPosition ob_4 = new ObCardPosition(3, null, null, colors_blue_blue_blue, positions_blue_blue_blue);
        Symbols[] centre_card___1 = {Symbols.FUNGI};
        Symbols[] symbols_card___1 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card___1 = new ResourceCard(symbols_card___1, Colors.BLUE, centre_card___1, true, 0, null, null, null);
        Symbols[] centre_card___2 = {Symbols.FUNGI};
        Symbols[] symbols_card___2 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card___2 = new ResourceCard(symbols_card___2, Colors.BLUE, centre_card___2, true, 0, null, null, null);
        Symbols[] centre_card___3 = {Symbols.FUNGI};
        Symbols[] symbols_card___3 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card___3 = new ResourceCard(symbols_card___3, Colors.BLUE, centre_card___3, true, 0, null, null, null);
        player.getPlayArea().getGrid()[2][4] = card___1;
        player.getPlayArea().getGrid()[3][3] = card___2;
        player.getPlayArea().getGrid()[4][2] = card___3;
        int k4 = ob_2.where_to_continue(ob_4, player, 2,4, 0, 1);
        assertEquals(-1, k4, "k4 should be -1");
        int p4 = ob_2.where_to_continue(ob_4, player, 3,3, 1, 2);
        assertEquals(-1, p4, "p4 should be -1");
    }
}
