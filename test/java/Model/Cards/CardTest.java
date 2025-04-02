package Model.Cards;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * with this class we are testing all classes about cards in the game
 */
class CardTest {

    /**
     * nothing is required here
     */
    @BeforeEach
    void setUp() {
    }

    /**
     * testing the initialization of a starter card
     */
    @Test
    void StarterCard(){
        StarterCard card0 = new StarterCard();
        System.out.println(card0);
        Symbols[] default_symbols = {Symbols.FUNGI, Symbols.PLANT, Symbols.INSECT, Symbols.ANIMAL};
        card0.setDefaultSymbols(default_symbols);
        assertEquals(default_symbols, card0.getDefaultSymbols());
    }
    /**
     * testing the initialization of a resource card
     */
    @Test
    void ResourceCard(){
        ResourceCard card1 = new ResourceCard();
        System.out.println(card1);
    }
    /**
     * testing the initialization of a gold card
     */
    @Test
    void GoldCard(){
        GoldCard card2 = new GoldCard();
        System.out.println(card2);
        Map<Symbols, Integer> requirements3 = new HashMap<>();
        requirements3.put(Symbols.PLANT, 2);
        requirements3.put(Symbols.FUNGI, 0);
        requirements3.put(Symbols.ANIMAL, 0);
        requirements3.put(Symbols.INSECT, 0);
        card2.setRequirements(requirements3);
        assertEquals(requirements3, card2.getRequirements());
    }

    /**
     * testing the initialization of an objective card
     */
    @Test
    void ObjectiveCard(){
        Colors[] colors_red_red_green = {Colors.RED, Colors.RED, Colors.GREEN};
        int[] positions_red_red_green = {1,1,2};
        ObCardPosition card3 = new ObCardPosition(3, null, null, colors_red_red_green, positions_red_red_green);
        System.out.println(card3);
        card3.setPoints(50);
        assertEquals(50, card3.getPoints());
        card3.setOb_face(true);
        assertTrue(card3.isOb_face());
        card3.setImageFront("FRONTE");
        assertEquals("FRONTE", card3.getImageFront());
        card3.setImageBack("RETRO");
        assertEquals("RETRO", card3.getImageBack());
    }
    /**
     * testing the initialization of a generic card
     */
    @Test
    void GenericCard(){
        Symbols[] symbols_card4 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card4 = new Card(null, Colors.RED, null, true, 0, null, null, null);
        card4.setSymbols(symbols_card4);
        assertEquals(symbols_card4, card4.getSymbols());
        card4.setColors(Colors.GREEN);
        assertEquals(Colors.GREEN, card4.getColors());
        Symbols[] centre = {Symbols.INSECT, Symbols.ANIMAL, Symbols.QUILL, Symbols.PLANT};
        card4.setCentre(centre);
        assertEquals(centre, card4.getCentre());
        card4.setPoints(20);
        assertEquals(20, card4.getPoints());
        card4.setPointsObject(Symbols.EMPTY);
        assertEquals(Symbols.EMPTY, card4.getPointsObject());
        card4.setImageFront("FRONTE");
        assertEquals("FRONTE", card4.getImageFront());
        card4.setImageBack("RETRO");
        assertEquals("RETRO", card4.getImageBack());
    }
}