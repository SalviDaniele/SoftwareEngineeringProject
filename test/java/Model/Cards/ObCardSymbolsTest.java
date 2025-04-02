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
 * tests for the class ObCardSymbols
 */
class ObCardSymbolsTest {

    private ObCardSymbols obCardSymbols;

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
     * testing the method calculateObCardSymbols of the class with an objective card with
     * three equal resources
     */
    @Test
    void calculateObCardSymbols_three_equal_resources() {
        try {
            player = new Player("Carlo", 1, new Table());
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
        player.setPlayArea(new PlayArea(8,8, player));
        Symbols[] symbols_3_plants = {Symbols.PLANT, Symbols.PLANT, Symbols.PLANT};
        ObCardSymbols ob_card_3_plants = new ObCardSymbols(2, null, null, symbols_3_plants);
        ob_card_3_plants.setSymbols(symbols_3_plants);
        player.getPlayArea().getAvailableResources().put(Symbols.PLANT, 5);
        ob_card_3_plants.calculateObCardSymbols(ob_card_3_plants, player);
        assertEquals(2, player.getPoints(), "The player points should be 2");
        player.getPlayArea().getAvailableResources().put(Symbols.PLANT, 3);
        player.setPoints(0);
        ob_card_3_plants.calculateObCardSymbols(ob_card_3_plants, player);
        assertEquals(2, player.getPoints(), "The player now points should be 2");
        player.getPlayArea().getAvailableResources().put(Symbols.PLANT, 2);
        player.setPoints(0);
        ob_card_3_plants.calculateObCardSymbols(ob_card_3_plants, player);
        assertEquals(0, player.getPoints(), "The player points should remain 0");
        player.getPlayArea().getAvailableResources().put(Symbols.PLANT, 0);
        player.setPoints(0);
        ob_card_3_plants.calculateObCardSymbols(ob_card_3_plants, player);
        assertEquals(0, player.getPoints(), "The player points now should remain 0");
    }
    /**
     * testing the method calculateObCardSymbols of the class with an objective card with
     * two equal resources
     */
    @Test
    void calculateObCardSymbols_two_equal_resources() {
        try {
            player = new Player("Carlo", 1, new Table());
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
        player.setPlayArea(new PlayArea(8,8, player));
        Symbols[] symbols_2_quills = {Symbols.QUILL, Symbols.QUILL};
        ObCardSymbols ob_card_2_quills = new ObCardSymbols(2, null, null, symbols_2_quills);
        player.getPlayArea().getAvailableResources().put(Symbols.QUILL, 3);
        ob_card_2_quills.calculateObCardSymbols(ob_card_2_quills, player);
        assertEquals(2, player.getPoints(), "The player points should be 2");
        player.getPlayArea().getAvailableResources().put(Symbols.QUILL, 2);
        player.setPoints(0);
        ob_card_2_quills.calculateObCardSymbols(ob_card_2_quills, player);
        assertEquals(2, player.getPoints(), "The player points should be 2");
        player.getPlayArea().getAvailableResources().put(Symbols.QUILL, 1);
        player.setPoints(0);
        ob_card_2_quills.calculateObCardSymbols(ob_card_2_quills, player);
        assertEquals(0, player.getPoints(), "The player points should remain 0");
        player.getPlayArea().getAvailableResources().put(Symbols.QUILL, 0);
        player.setPoints(0);
        ob_card_2_quills.calculateObCardSymbols(ob_card_2_quills, player);
        assertEquals(0, player.getPoints(), "The player points should remain 0");
    }
    /**
     * testing the method calculateObCardSymbols of the class with an objective card with
     * three different resources
     */
    @Test
    void calculateObCardSymbols_three_different_resources() {
        try {
            player = new Player("Carlo", 1, new Table());
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
        player.setPlayArea(new PlayArea(8,8, player));
        Symbols[] symbols_three_different = {Symbols.QUILL, Symbols.INKWELL, Symbols.MANUSCRIPT};
        ObCardSymbols ob_card_three_different = new ObCardSymbols(3, null, null, symbols_three_different);
        player.getPlayArea().getAvailableResources().put(Symbols.QUILL, 3);
        player.getPlayArea().getAvailableResources().put(Symbols.MANUSCRIPT, 2);
        player.getPlayArea().getAvailableResources().put(Symbols.INKWELL, 2);
        ob_card_three_different.calculateObCardSymbols(ob_card_three_different, player);
        assertEquals(6, player.getPoints(), "The player points should be 6");
        player.getPlayArea().getAvailableResources().put(Symbols.QUILL, 0);
        player.getPlayArea().getAvailableResources().put(Symbols.MANUSCRIPT, 0);
        player.getPlayArea().getAvailableResources().put(Symbols.INKWELL, 0);
        player.setPoints(0);
        ob_card_three_different.calculateObCardSymbols(ob_card_three_different, player);
        assertEquals(0, player.getPoints(), "The player points should be 0");
    }
}