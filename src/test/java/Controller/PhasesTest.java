package Controller;

import Model.Table;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * tester for the class Phase, PlacingPhase and DrawPhase and their methods
 */
class PhasesTest {

    /**
     * this is an instance of the class MatchController
     */
    private MatchController match;
    /**
     * a reference to a table
     */
    private Table table;

    /**
     * before every test the match and the table are initialized
     */
    @BeforeEach
    void setUp() {
        match = new MatchController(1, 4, table);
        try {
            table = new Table();
        } catch (IOException | ParseException e) {
            throw new RuntimeException(String.valueOf(e));
        }
    }

    /**
     * testing set and get phase in the match
     */
    @Test
    void phase(){
        Phase ph=new Phase(match) {
            @Override
            public boolean processCommand(String command) {
                return false;
            }

            @Override
            public String communicatePhaseToGui() {
                return "";
            }
        };
        match.setPhase(ph);
        assertEquals(ph,match.getPhase());
    }

    /**
     *testing if all the class placingPhase works properly
     */
    @Test
    void placingPhase(){
        Phase test1 = new PlacingPhase(match);
        Phase test4 = new PlacingPhase();
        assertTrue(test1.processCommand("place"));
        assertTrue(test1.processCommand("chat"));
        assertTrue(test1.processCommand("cheat1"));
        assertTrue(test1.processCommand("cheat2"));
        assertFalse(test1.processCommand("draw"));
        assertEquals("PlacingPhase", test1.communicatePhaseToGui());
    }

    /**
     *testing if all the class drawPhase works properly
     */
    @Test
    void drawPhase(){
        Phase test2 = new DrawPhase(match);
        Phase test3 = new DrawPhase();
        assertTrue(test2.processCommand("draw"));
        assertTrue(test2.processCommand("chat"));
        assertTrue(test2.processCommand("cheat1"));
        assertTrue(test2.processCommand("cheat2"));
        assertFalse(test2.processCommand("place"));
        assertEquals("DrawPhase", test2.communicatePhaseToGui());
    }
}