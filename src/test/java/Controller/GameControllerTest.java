package Controller;

import Listeners.EndPoint;
import Listeners.Listener;
import Model.Table;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * tester for the class GameController and its methods
 */
class GameControllerTest {
    /**
     * this is the instance of the class tested
     */
    private GameController game;
    /**
     * a reference to a table
     */
    private Table table;

    /**
     * for every test the instance of the class tested is initialized
     */
    @BeforeEach
    void setUp() {
        game = new GameController(1);
        GameController game1 = new GameController();
    }

    /**
     * testing if all setter and getter methods of the class work
     */
    @Test
    void setterAndGetter(){
        game.setPlayersQueue(2);
        assertEquals(2, game.getPlayersQueue());
        MatchController match1 = new MatchController(1, 4, null);
        game.getMatchList().add(match1);
        assertEquals(match1, game.getMatch(0));
        assertTrue(game.isFirst());
        game.setFirst(true);
        assertTrue(game.isFirst());
    }

    /**
     * testing the method addMatch of GameController
     */
    @Test
    void addMatch(){
        game.setPlayersQueue(2);
        Listener listenerTest = new EndPoint();
        game.addMatch("Corrado", listenerTest);
        assertFalse(game.isFirst());
        assertEquals(1, game.getMatchList().size());
        Listener listenerTest2 = new EndPoint();
        game.addMatch("Federico", listenerTest2);
        assertEquals(2, game.getMatchList().size());
    }

    /**
     * testing the method addPlayer of GameController
     */
    @Test
    void addPlayer(){
        game.setPlayersQueue(2);
        try {
            table = new Table();
        } catch (IOException | ParseException e) {
            throw new RuntimeException(String.valueOf(e));
        }
        MatchController match1 = new MatchController(1, 4, table);
        game.getMatchList().add(match1);
        Listener listenerTest = new EndPoint();
        game.addPlayer("Carlo", listenerTest);
        assertEquals(1, game.getPlayersQueue());
        assertEquals("Carlo", game.getMatch(0).getTable().getPlayers().getFirst().getNickname());
        Listener listenerTest2 = new EndPoint();
        game.addPlayer("Daniele", listenerTest2);
        assertEquals(0, game.getPlayersQueue());
        assertEquals("Daniele", game.getMatch(0).getTable().getPlayers().get(1).getNickname());
    }
}