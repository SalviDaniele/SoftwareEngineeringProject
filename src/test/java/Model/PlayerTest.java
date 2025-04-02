package Model;

import Controller.GameController;
import Controller.MatchController;
import Listeners.EndPoint;
import Model.Cards.*;
import Network.RMI.ServerRMI;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.ServerSocket;
import java.util.*;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains all the necessary methods in order to test the Player class methods.
 */
class PlayerTest {

    /**
     * The table where we can access the gold deck and draw the card.
     */
    private Table table;

    /**
     * The current player who is playing the action of drawing.
     */
    private Player player1;

    /**
     * The player next in the list.
     */
    private Player player2;
    /**
     * a generic player
     */
    private Player player;
    /**
     * the match currently running
     */
    private MatchController match;

    /**
     * Before we test, we set up a new table, a player, and the decks on the table.
     */
    @BeforeEach
    void setUp() {
        ServerRMI serverRMI;
        try {
            ServerSocket serverSocket = new ServerSocket();
            GameController gameController = new GameController(0);
            CopyOnWriteArrayList clients = new CopyOnWriteArrayList<>();
            serverRMI = new ServerRMI(serverSocket, gameController, clients);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        player1 = new Player("Carlo", 1, null);
        player1.setPoints(0);
        player2 = new Player("Daniele", 1, null);
        player2.setPoints(10);
        try {
            table = new Table();
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
        player1.setTable(table);
        player2.setTable(table);
        player1.setYourTurn(true);
        player2.setYourTurn(false);
        EndPoint listener1 = new EndPoint();
        player1.addListener(listener1);
        listener1.setServer(serverRMI);
        EndPoint listener2 = new EndPoint();
        player2.addListener(listener2);
        listener2.setServer(serverRMI);
        table.getPlayers().add(player1);
        table.getPlayers().add(player2);
        player = new Player("Carlo", 1, table);
        player.setTable(table);
        EndPoint listener = new EndPoint();
        player.addListener(listener);
        listener.setServer(serverRMI);
        table.getPlayers().add(player);
    }

    /**
     * We test if the first card in the resource deck (list of resource cards) moves to the player's hand.
     * We test also if the turn correctly changes.
     */
    @Test
    void drawFromDeckR() {
        ResourceCard card1 = table.getDeckR().getFirst();
        player1.drawFromDeckR();
        assertFalse(table.getDeckR().contains(card1), "The card should have been removed from the deck.");
        assertTrue(player1.getHand().contains(card1), "The card should have been added to the player's hand.");
        assertFalse(player1.isYourTurn(), "The turn of the current player should be finished.");
        assertTrue(player2.isYourTurn(), "The turn of the next player in the list should be started.");
    }

    /**
     * We test if the first card in the gold deck (list of gold cards) moves to the player's hand.
     * We test also if the turn correctly changes.
     */
    @Test
    void drawFromDeckG() {
        GoldCard card1 = table.getDeckG().getFirst();
        player1.drawFromDeckG();
        assertFalse(table.getDeckG().contains(card1), "The card should have been removed from the deck.");
        assertTrue(player1.getHand().contains(card1), "The card should have been added to the player's hand.");
        assertFalse(player1.isYourTurn(), "The turn of the current player should be finished.");
        assertTrue(player2.isYourTurn(), "The turn of the next player in the list should be started.");
    }

    /**
     * We test if the first not folded resource card on the table is added to the player's hand, removed from the table,
     * and replaced by the first card of the respective deck. This card must also be removed from the top of the deck.
     * We test also if the turn correctly changes.
     */
    @Test
    void takeFromTable_0() {
        ResourceCard resourceCardTaken = null;
        GoldCard goldCardTaken = null;
        ResourceCard[] resourceCardsOnTable = new ResourceCard[]{table.getDeckR().getFirst(), table.getDeckR().get(1)};
        table.setCardsR(resourceCardsOnTable);
        table.getDeckR().removeFirst();
        table.getDeckR().removeFirst();
        GoldCard[] goldCardsOnTable = new GoldCard[]{table.getDeckG().getFirst(), table.getDeckG().get(1)};
        table.setCardsG(goldCardsOnTable);
        table.getDeckG().removeFirst();
        table.getDeckG().removeFirst();
        ResourceCard resourceCardPlaced = table.getDeckR().getFirst();
        GoldCard goldCardPlaced = table.getDeckG().getFirst();
        Random random = new Random(); // Creiamo un oggetto Random
        int selection = 0; // Generiamo un numero casuale compreso tra 0 e 3
        resourceCardTaken = table.getCardsR()[selection];
        player1.takeFromTable(selection);
        assertTrue(player1.getHand().contains(resourceCardTaken), "The card should have been added to the player's hand.");
        assertFalse(table.getDeckR().contains(resourceCardPlaced), "The card should have been removed from the deck.");
        assertEquals(table.getCardsR()[selection], resourceCardPlaced, "The card once on the top of the resource deck should be on the table");
        assertFalse(player1.isYourTurn(), "The turn of the current player should be finished.");
        assertTrue(player2.isYourTurn(), "The turn of the next player in the list should be started.");
    }
    /**
     * We test if the second not folded resource card on the table is added to the player's hand, removed from the table,
     * and replaced by the first card of the respective deck. This card must also be removed from the top of the deck.
     * We test also if the turn correctly changes.
     */
    @Test
    void takeFromTable_1() {
        ResourceCard resourceCardTaken = null;
        GoldCard goldCardTaken = null;
        ResourceCard[] resourceCardsOnTable = new ResourceCard[]{table.getDeckR().getFirst(), table.getDeckR().get(1)};
        table.setCardsR(resourceCardsOnTable);
        table.getDeckR().removeFirst();
        table.getDeckR().removeFirst();
        GoldCard[] goldCardsOnTable = new GoldCard[]{table.getDeckG().getFirst(), table.getDeckG().get(1)};
        table.setCardsG(goldCardsOnTable);
        table.getDeckG().removeFirst();
        table.getDeckG().removeFirst();
        ResourceCard resourceCardPlaced = table.getDeckR().getFirst();
        GoldCard goldCardPlaced = table.getDeckG().getFirst();
        Random random = new Random(); // Creiamo un oggetto Random
        int selection = 1; // Generiamo un numero casuale compreso tra 0 e 3
        resourceCardTaken = table.getCardsR()[selection];
        player1.takeFromTable(selection);
        assertTrue(player1.getHand().contains(resourceCardTaken), "The card should have been added to the player's hand.");
        assertFalse(table.getDeckR().contains(resourceCardPlaced), "The card should have been removed from the deck.");
        assertEquals(table.getCardsR()[selection], resourceCardPlaced, "The card once on the top of the resource deck should be on the table");
        assertFalse(player1.isYourTurn(), "The turn of the current player should be finished.");
        assertTrue(player2.isYourTurn(), "The turn of the next player in the list should be started.");
    }
    /**
     * We test if the first not folded gold card on the table is added to the player's hand, removed from the table,
     * and replaced by the first card of the respective deck. This card must also be removed from the top of the deck.
     * We test also if the turn correctly changes.
     */
    @Test
    void takeFromTable_2() {
        ResourceCard resourceCardTaken = null;
        GoldCard goldCardTaken = null;
        ResourceCard[] resourceCardsOnTable = new ResourceCard[]{table.getDeckR().getFirst(), table.getDeckR().get(1)};
        table.setCardsR(resourceCardsOnTable);
        table.getDeckR().removeFirst();
        table.getDeckR().removeFirst();
        GoldCard[] goldCardsOnTable = new GoldCard[]{table.getDeckG().getFirst(), table.getDeckG().get(1)};
        table.setCardsG(goldCardsOnTable);
        table.getDeckG().removeFirst();
        table.getDeckG().removeFirst();
        ResourceCard resourceCardPlaced = table.getDeckR().getFirst();
        GoldCard goldCardPlaced = table.getDeckG().getFirst();
        Random random = new Random(); // Creiamo un oggetto Random
        int selection = 2; // Generiamo un numero casuale compreso tra 0 e 3
        goldCardTaken = table.getCardsG()[selection-2];
        player1.takeFromTable(selection);
        assertTrue(player1.getHand().contains(goldCardTaken), "The card should have been added to the player's hand.");
        assertFalse(table.getDeckG().contains(goldCardPlaced), "The card should have been removed from the deck.");
        assertEquals(table.getCardsG()[selection-2], goldCardPlaced, "The card once on the top of the resource deck should be on the table");
        assertFalse(player1.isYourTurn(), "The turn of the current player should be finished.");
        assertTrue(player2.isYourTurn(), "The turn of the next player in the list should be started.");
    }
    /**
     * We test if the second not folded gold card on the table is added to the player's hand, removed from the table,
     * and replaced by the first card of the respective deck. This card must also be removed from the top of the deck.
     * We test also if the turn correctly changes.
     */
    @Test
    void takeFromTable_3() {
        ResourceCard resourceCardTaken = null;
        GoldCard goldCardTaken = null;
        ResourceCard[] resourceCardsOnTable = new ResourceCard[]{table.getDeckR().getFirst(), table.getDeckR().get(1)};
        table.setCardsR(resourceCardsOnTable);
        table.getDeckR().removeFirst();
        table.getDeckR().removeFirst();
        GoldCard[] goldCardsOnTable = new GoldCard[]{table.getDeckG().getFirst(), table.getDeckG().get(1)};
        table.setCardsG(goldCardsOnTable);
        table.getDeckG().removeFirst();
        table.getDeckG().removeFirst();
        ResourceCard resourceCardPlaced = table.getDeckR().getFirst();
        GoldCard goldCardPlaced = table.getDeckG().getFirst();
        Random random = new Random(); // Creiamo un oggetto Random
        int selection = 3; // Generiamo un numero casuale compreso tra 0 e 3
        goldCardTaken = table.getCardsG()[selection-2];
        player1.takeFromTable(selection);
        assertTrue(player1.getHand().contains(goldCardTaken), "The card should have been added to the player's hand.");
        assertFalse(table.getDeckG().contains(goldCardPlaced), "The card should have been removed from the deck.");
        assertEquals(table.getCardsG()[selection-2], goldCardPlaced, "The card once on the top of the resource deck should be on the table");
        assertFalse(player1.isYourTurn(), "The turn of the current player should be finished.");
        assertTrue(player2.isYourTurn(), "The turn of the next player in the list should be started.");
    }
    /**
     * We test the default case of the 'switch-case' of the method 'takeFromTable'.
     * We test also if the turn correctly changes.
     */
    @Test
    void takeFromTable_default() {
        ResourceCard resourceCardTaken = null;
        GoldCard goldCardTaken = null;
        ResourceCard[] resourceCardsOnTable = new ResourceCard[]{table.getDeckR().getFirst(), table.getDeckR().get(1)};
        table.setCardsR(resourceCardsOnTable);
        table.getDeckR().removeFirst();
        table.getDeckR().removeFirst();
        GoldCard[] goldCardsOnTable = new GoldCard[]{table.getDeckG().getFirst(), table.getDeckG().get(1)};
        table.setCardsG(goldCardsOnTable);
        table.getDeckG().removeFirst();
        table.getDeckG().removeFirst();
        ResourceCard resourceCardPlaced = table.getDeckR().getFirst();
        GoldCard goldCardPlaced = table.getDeckG().getFirst();
        Random random = new Random(); // Creiamo un oggetto Random
        int selection = 20; // Generiamo un numero casuale compreso tra 0 e 3
        resourceCardTaken = table.getCardsR()[0];
        player1.takeFromTable(selection);
        assertTrue(player1.getHand().contains(resourceCardTaken), "The card should have been added to the player's hand.");
        assertFalse(table.getDeckR().contains(resourceCardPlaced), "The card should have been removed from the deck.");
        assertEquals(table.getCardsR()[0], resourceCardPlaced, "The card once on the top of the resource deck should be on the table");
        assertFalse(player1.isYourTurn(), "The turn of the current player should be finished.");
        assertTrue(player2.isYourTurn(), "The turn of the next player in the list should be started.");
    }
    /**
     * This test assures that a new resource card not folded is correctly placed with 2 resource card already
     * on the grid, applying correctly all the methods called by placeCard
     */
    @Test
    void placeCard_resource_card() {
        match = new MatchController(1, 4, table);
        player.setPlayArea(new PlayArea(8,8, player));
        player.getPlayArea().getAvailableResources().put(Symbols.PLANT, 2);
        player.getPlayArea().getAvailableResources().put(Symbols.INSECT, 2);
        player.getPlayArea().getAvailableResources().put(Symbols.QUILL, 2);
        Symbols[] symbols_card1 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card1 = new ResourceCard(symbols_card1, Colors.RED, null, true, 0, null, null, null);
        Symbols[] symbols_card2 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card2 = new ResourceCard(symbols_card2, Colors.RED, null, true, 0, null, null, null);
        Symbols points_object_card3 = Symbols.EMPTY;
        Symbols[] symbols_card3 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card3 = new ResourceCard(symbols_card3, Colors.GREEN, null, true, 1, points_object_card3, null, null);
        player.getPlayArea().getGrid()[1][1]=card1;
        player.getPlayArea().getGrid()[1][3]=card2;
        player.placeCard(card3, true, 2, 2);
        assertEquals(card3, player.getPlayArea().getGrid()[2][2]);
        assertEquals(2, player.getPlayArea().getAvailableResources().get(Symbols.QUILL));
        assertEquals(2, player.getPlayArea().getAvailableResources().get(Symbols.PLANT));
        assertEquals(3, player.getPlayArea().getAvailableResources().get(Symbols.INSECT));
        assertEquals(0, player.getPlayArea().getAvailableResources().get(Symbols.FUNGI));
        assertEquals(false, player.getPlayArea().getGrid()[1][1].getCorners()[3]);
        assertEquals(false, player.getPlayArea().getGrid()[1][3].getCorners()[2]);
        assertEquals(1, player.getPoints());
    }
    /**
     * This test assures that a new resource card folded is correctly placed with 2 resource card already
     * on the grid, applying correctly all the methods called by placeCard
     */
    @Test
    void placeCard_resource_card_folded() {
        match = new MatchController(1, 4, table);
        player.setPlayArea(new PlayArea(8,8, player));
        player.getPlayArea().getAvailableResources().put(Symbols.PLANT, 2);
        player.getPlayArea().getAvailableResources().put(Symbols.INSECT, 2);
        player.getPlayArea().getAvailableResources().put(Symbols.QUILL, 2);
        Symbols[] symbols_card1 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card1 = new ResourceCard(symbols_card1, Colors.RED, null, true, 0, null, null, null);
        Symbols[] symbols_card2 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card2 = new ResourceCard(symbols_card2, Colors.RED, null, true, 0, null, null, null);
        Symbols points_object_card3 = Symbols.EMPTY;
        Symbols[] symbols_card3 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card3 = new ResourceCard(symbols_card3, Colors.GREEN, null, true, 1, points_object_card3, null, null);
        player.getPlayArea().getGrid()[1][1]=card1;
        player.getPlayArea().getGrid()[1][3]=card2;
        player.placeCard(card3, false, 2, 2);
        assertEquals(card3, player.getPlayArea().getGrid()[2][2]);
        assertEquals(1, player.getPlayArea().getAvailableResources().get(Symbols.QUILL));
        assertEquals(2, player.getPlayArea().getAvailableResources().get(Symbols.PLANT));
        assertEquals(2, player.getPlayArea().getAvailableResources().get(Symbols.INSECT));
        assertEquals(0, player.getPlayArea().getAvailableResources().get(Symbols.FUNGI));
        assertEquals(false, player.getPlayArea().getGrid()[1][1].getCorners()[3]);
        assertEquals(false, player.getPlayArea().getGrid()[1][3].getCorners()[2]);
        assertEquals(0, player.getPoints());
    }

    /**
     * This test assures that a new gold card not folded with 3 points is correctly placed with 2 resource card already
     * on the grid, applying correctly all the methods called by placeCard
     */@Test
    void placeCard_gold_card_with_3_points() {
        match = new MatchController(1, 4, table);
        player.setPlayArea(new PlayArea(8,8, player));
        player.getPlayArea().getAvailableResources().put(Symbols.ANIMAL, 0);
        player.getPlayArea().getAvailableResources().put(Symbols.FUNGI, 0);
        player.getPlayArea().getAvailableResources().put(Symbols.PLANT, 2);
        player.getPlayArea().getAvailableResources().put(Symbols.INSECT, 2);
        player.getPlayArea().getAvailableResources().put(Symbols.QUILL, 2);
        Symbols[] symbols_card1 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card1 = new ResourceCard(symbols_card1, Colors.RED, null, true, 0, null, null, null);
        Symbols[] symbols_card2 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card2 = new ResourceCard(symbols_card2, Colors.RED, null, true, 0, null, null, null);
        Map<Symbols, Integer> requirements3 = new HashMap<>();
        requirements3.put(Symbols.PLANT, 2);
        requirements3.put(Symbols.FUNGI, 0);
        requirements3.put(Symbols.ANIMAL, 0);
        requirements3.put(Symbols.INSECT, 0);
        Symbols points_object_card3 = Symbols.EMPTY;
        Symbols[] symbols_card3 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card3 = new GoldCard(symbols_card3, Colors.GREEN, null, true, 3, points_object_card3, null, null, requirements3);
        player.getPlayArea().getGrid()[1][1]=card1;
        player.getPlayArea().getGrid()[1][3]=card2;
        player.placeCard(card3, true, 2, 2);
        assertEquals(card3, player.getPlayArea().getGrid()[2][2]);
        assertEquals(2, player.getPlayArea().getAvailableResources().get(Symbols.QUILL));
        assertEquals(2, player.getPlayArea().getAvailableResources().get(Symbols.PLANT));
        assertEquals(3, player.getPlayArea().getAvailableResources().get(Symbols.INSECT));
        assertEquals(0, player.getPlayArea().getAvailableResources().get(Symbols.FUNGI));
        assertEquals(false, player.getPlayArea().getGrid()[1][1].getCorners()[3]);
        assertEquals(false, player.getPlayArea().getGrid()[1][3].getCorners()[2]);
        assertEquals(3, player.getPoints());
    }
    /**
     * This test assures that a new gold card not folded with 5 points is correctly placed with 2 resource card already
     * on the grid, applying correctly all the methods called by placeCard
     */@Test
    void placeCard_gold_card_with_5_points() {
        match = new MatchController(1, 4, table);
        player.setPlayArea(new PlayArea(8,8, player));
        player.getPlayArea().getAvailableResources().put(Symbols.ANIMAL, 0);
        player.getPlayArea().getAvailableResources().put(Symbols.FUNGI, 0);
        player.getPlayArea().getAvailableResources().put(Symbols.PLANT, 2);
        player.getPlayArea().getAvailableResources().put(Symbols.INSECT, 2);
        player.getPlayArea().getAvailableResources().put(Symbols.QUILL, 2);
        Symbols[] symbols_card1 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card1 = new ResourceCard(symbols_card1, Colors.RED, null, true, 0, null, null, null);
        Symbols[] symbols_card2 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card2 = new ResourceCard(symbols_card2, Colors.RED, null, true, 0, null, null, null);
        Map<Symbols, Integer> requirements3 = new HashMap<>();
        requirements3.put(Symbols.PLANT, 2);
        requirements3.put(Symbols.FUNGI, 0);
        requirements3.put(Symbols.ANIMAL, 0);
        requirements3.put(Symbols.INSECT, 0);
        Symbols points_object_card3 = Symbols.EMPTY;
        Symbols[] symbols_card3 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card3 = new GoldCard(symbols_card3, Colors.GREEN, null, true, 5, points_object_card3, null, null, requirements3);
        player.getPlayArea().getGrid()[1][1]=card1;
        player.getPlayArea().getGrid()[1][3]=card2;
        player.placeCard(card3, true, 2, 2);
        assertEquals(card3, player.getPlayArea().getGrid()[2][2]);
        assertEquals(2, player.getPlayArea().getAvailableResources().get(Symbols.QUILL));
        assertEquals(2, player.getPlayArea().getAvailableResources().get(Symbols.PLANT));
        assertEquals(3, player.getPlayArea().getAvailableResources().get(Symbols.INSECT));
        assertEquals(0, player.getPlayArea().getAvailableResources().get(Symbols.FUNGI));
        assertEquals(false, player.getPlayArea().getGrid()[1][1].getCorners()[3]);
        assertEquals(false, player.getPlayArea().getGrid()[1][3].getCorners()[2]);
        assertEquals(5, player.getPoints());
    }
    /**
     * This test assures that a new gold card not folded with 2 points per quill is correctly placed with 2 resource card already
     * on the grid, applying correctly all the methods called by placeCard
     */@Test
    void placeCard_gold_card_with_2_points_per_quill() {
        match = new MatchController(1, 4, table);
        player.setPlayArea(new PlayArea(8,8, player));
        player.getPlayArea().getAvailableResources().put(Symbols.ANIMAL, 0);
        player.getPlayArea().getAvailableResources().put(Symbols.FUNGI, 0);
        player.getPlayArea().getAvailableResources().put(Symbols.PLANT, 2);
        player.getPlayArea().getAvailableResources().put(Symbols.INSECT, 2);
        player.getPlayArea().getAvailableResources().put(Symbols.QUILL, 2);
        Symbols[] symbols_card1 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card1 = new ResourceCard(symbols_card1, Colors.RED, null, true, 0, null, null, null);
        Symbols[] symbols_card2 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card2 = new ResourceCard(symbols_card2, Colors.RED, null, true, 0, null, null, null);
        Map<Symbols, Integer> requirements3 = new HashMap<>();
        requirements3.put(Symbols.PLANT, 2);
        requirements3.put(Symbols.FUNGI, 0);
        requirements3.put(Symbols.ANIMAL, 0);
        requirements3.put(Symbols.INSECT, 0);
        Symbols points_object_card3 = Symbols.QUILL;
        Symbols[] symbols_card3 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card3 = new GoldCard(symbols_card3, Colors.GREEN, null, true, 2, points_object_card3, null, null, requirements3);
        player.getPlayArea().getGrid()[1][1]=card1;
        player.getPlayArea().getGrid()[1][3]=card2;
        player.placeCard(card3, true, 2, 2);
        assertEquals(card3, player.getPlayArea().getGrid()[2][2]);
        assertEquals(2, player.getPlayArea().getAvailableResources().get(Symbols.QUILL));
        assertEquals(2, player.getPlayArea().getAvailableResources().get(Symbols.PLANT));
        assertEquals(3, player.getPlayArea().getAvailableResources().get(Symbols.INSECT));
        assertEquals(0, player.getPlayArea().getAvailableResources().get(Symbols.FUNGI));
        assertEquals(false, player.getPlayArea().getGrid()[1][1].getCorners()[3]);
        assertEquals(false, player.getPlayArea().getGrid()[1][3].getCorners()[2]);
        match.getTable().getScore().put(player.getNickname(), 0);
        assertEquals(4, player.getPoints());
    }
    /**
     * This test assures that a new gold card not folded with 2 points per corner covered is correctly placed with 2 resource card already
     * on the grid, applying correctly all the methods called by placeCard
     */@Test
    void placeCard_gold_card_with_2_points_per_corner_covered() {
        match = new MatchController(1, 4, table);
        player.setPlayArea(new PlayArea(8,8, player));
        player.getPlayArea().getAvailableResources().put(Symbols.ANIMAL, 0);
        player.getPlayArea().getAvailableResources().put(Symbols.FUNGI, 0);
        player.getPlayArea().getAvailableResources().put(Symbols.PLANT, 2);
        player.getPlayArea().getAvailableResources().put(Symbols.INSECT, 2);
        player.getPlayArea().getAvailableResources().put(Symbols.QUILL, 2);
        Symbols[] symbols_card1 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card1 = new ResourceCard(symbols_card1, Colors.RED, null, true, 0, null, null, null);
        Symbols[] symbols_card2 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card2 = new ResourceCard(symbols_card2, Colors.RED, null, true, 0, null, null, null);
        Map<Symbols, Integer> requirements3 = new HashMap<>();
        requirements3.put(Symbols.PLANT, 2);
        requirements3.put(Symbols.FUNGI, 0);
        requirements3.put(Symbols.ANIMAL, 0);
        requirements3.put(Symbols.INSECT, 0);
        Symbols points_object_card3 = Symbols.EMPTY;
        Symbols[] symbols_card3 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card3 = new GoldCard(symbols_card3, Colors.GREEN, null, true, 2, points_object_card3, null, null, requirements3);
        player.getPlayArea().getGrid()[1][1]=card1;
        player.getPlayArea().getGrid()[1][3]=card2;
        player.placeCard(card3, true, 2, 2);
        assertEquals(card3, player.getPlayArea().getGrid()[2][2]);
        assertEquals(2, player.getPlayArea().getAvailableResources().get(Symbols.QUILL));
        assertEquals(2, player.getPlayArea().getAvailableResources().get(Symbols.PLANT));
        assertEquals(3, player.getPlayArea().getAvailableResources().get(Symbols.INSECT));
        assertEquals(0, player.getPlayArea().getAvailableResources().get(Symbols.FUNGI));
        assertEquals(false, player.getPlayArea().getGrid()[1][1].getCorners()[3]);
        assertEquals(false, player.getPlayArea().getGrid()[1][3].getCorners()[2]);
        assertEquals(4, player.getPoints());
    }

    /**
     * This test assures that a new gold card folded is correctly placed with 2 resource card already
     * on the grid, applying correctly all the methods called by placeCard
     */
    @Test
    void placeCard_gold_card_folded() {
        match = new MatchController(1, 4, table);
        player.setPlayArea(new PlayArea(8,8, player));
        player.getPlayArea().getAvailableResources().put(Symbols.ANIMAL, 0);
        player.getPlayArea().getAvailableResources().put(Symbols.FUNGI, 0);
        player.getPlayArea().getAvailableResources().put(Symbols.PLANT, 2);
        player.getPlayArea().getAvailableResources().put(Symbols.INSECT, 2);
        player.getPlayArea().getAvailableResources().put(Symbols.QUILL, 2);
        Symbols[] symbols_card1 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card1 = new ResourceCard(symbols_card1, Colors.RED, null, true, 0, null, null, null);
        Symbols[] symbols_card2 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card2 = new ResourceCard(symbols_card2, Colors.RED, null, true, 0, null, null, null);
        Map<Symbols, Integer> requirements3 = new HashMap<>();
        requirements3.put(Symbols.PLANT, 2);
        requirements3.put(Symbols.FUNGI, 0);
        requirements3.put(Symbols.ANIMAL, 0);
        requirements3.put(Symbols.INSECT, 0);
        Symbols points_object_card3 = Symbols.EMPTY;
        Symbols[] symbols_card3 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card3 = new GoldCard(symbols_card3, Colors.GREEN, null, true, 3, points_object_card3, null, null, requirements3);
        player.getPlayArea().getGrid()[1][1]=card1;
        player.getPlayArea().getGrid()[1][3]=card2;
        player.placeCard(card3, false, 2, 2);
        assertEquals(card3, player.getPlayArea().getGrid()[2][2]);
        assertEquals(1, player.getPlayArea().getAvailableResources().get(Symbols.QUILL));
        assertEquals(2, player.getPlayArea().getAvailableResources().get(Symbols.PLANT));
        assertEquals(2, player.getPlayArea().getAvailableResources().get(Symbols.INSECT));
        assertEquals(0, player.getPlayArea().getAvailableResources().get(Symbols.FUNGI));
        assertEquals(false, player.getPlayArea().getGrid()[1][1].getCorners()[3]);
        assertEquals(false, player.getPlayArea().getGrid()[1][3].getCorners()[2]);
        assertEquals(0, player.getPoints());
    }
    /**
     * This test assures that a new starter card not folded is correctly placed,
     * applying correctly all the methods called by placeCard
     */
    @Test
    void placeCard_starter_card() {
        match = new MatchController(1, 4, table);
        player.setPlayArea(new PlayArea(8,8, player));
        Symbols pointsObject = Symbols.EMPTY;
        Symbols[] default_symbols = {Symbols.FUNGI, Symbols.PLANT, Symbols.INSECT, Symbols.ANIMAL};
        Symbols[] symbols = {Symbols.EMPTY, Symbols.NOCORNER, Symbols.INSECT, Symbols.FUNGI};
        Symbols[] centre = {Symbols.ANIMAL, Symbols.PLANT, Symbols.EMPTY};
        Card card = new StarterCard(symbols, null, centre, true, 0, pointsObject, null, null, default_symbols);
        player.placeCard(card, true, 2, 2);
        assertEquals(card, player.getPlayArea().getGrid()[2][2]);
        assertEquals(1, player.getPlayArea().getAvailableResources().get(Symbols.PLANT));
        assertEquals(1, player.getPlayArea().getAvailableResources().get(Symbols.FUNGI));
        assertEquals(1, player.getPlayArea().getAvailableResources().get(Symbols.ANIMAL));
        assertEquals(1, player.getPlayArea().getAvailableResources().get(Symbols.INSECT));
    }
    /**
     * This test assures that a new starter card folded is correctly placed,
     * applying correctly all the methods called by placeCard
     */
    @Test
    void placeCard_starter_card_folded() {
        match = new MatchController(1, 4, table);
        player.setPlayArea(new PlayArea(8,8, player));
        Symbols[] default_symbols = {Symbols.FUNGI, Symbols.PLANT, Symbols.INSECT, Symbols.ANIMAL};
        Symbols[] symbols = {Symbols.EMPTY, Symbols.NOCORNER, Symbols.INSECT, Symbols.FUNGI};
        Symbols[] centre = {Symbols.ANIMAL, Symbols.PLANT, Symbols.EMPTY};
        Symbols pointsObject = Symbols.EMPTY;
        Card card = new StarterCard(symbols, null, centre, true, 0, pointsObject, null, null, default_symbols);
        player.placeCard(card, false, 2, 2);
        assertEquals(card, player.getPlayArea().getGrid()[2][2]);
        assertEquals(1, player.getPlayArea().getAvailableResources().get(Symbols.PLANT));
        assertEquals(1, player.getPlayArea().getAvailableResources().get(Symbols.FUNGI));
        assertEquals(1, player.getPlayArea().getAvailableResources().get(Symbols.ANIMAL));
        assertEquals(1, player.getPlayArea().getAvailableResources().get(Symbols.INSECT));
    }

    /**
     * This test assures that a new resource card not folded can be placed with 2 resource card already on the grid
     */
    @Test
    void check_and_insert_2_cards_around() {
        match = new MatchController(1, 4, table);
        player.setPlayArea(new PlayArea(8,8, player));
        Symbols[] symbols_card1 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card1 = new ResourceCard(symbols_card1, Colors.RED, null, true, 0, null, null, null);
        Symbols[] symbols_card2 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card2 = new ResourceCard(symbols_card2, Colors.RED, null, true, 0, null, null, null);
        Symbols[] symbols_card3 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card3 = new ResourceCard(symbols_card3, Colors.GREEN, null, true, 0, null, null, null);
        player.getPlayArea().getGrid()[1][1]=card1;
        player.getPlayArea().getGrid()[1][3]=card2;
        player.getPlayArea().check_and_insert(card3, true, 2, 2);
        assertEquals(card3, player.getPlayArea().getGrid()[2][2]);
    }
    /**
     * This test assures that a new resource card not folded can be placed with 4 resource card already on the grid
     */
    @Test
    void check_and_insert_4_cards_around() {
        match = new MatchController(1, 4, table);
        player.setPlayArea(new PlayArea(8,8, player));
        Symbols[] symbols_card1 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card1 = new ResourceCard(symbols_card1, Colors.RED, null, true, 0, null, null, null);
        Symbols[] symbols_card2 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card2 = new ResourceCard(symbols_card2, Colors.RED, null, true, 0, null, null, null);
        Symbols[] symbols_card3 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card3 = new ResourceCard(symbols_card3, Colors.GREEN, null, true, 0, null, null, null);
        Symbols[] symbols_card4 = {Symbols.FUNGI, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card4 = new ResourceCard(symbols_card4, Colors.GREEN, null, true, 0, null, null, null);
        Symbols[] symbols_card5 = {Symbols.FUNGI, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card5 = new ResourceCard(symbols_card5, Colors.GREEN, null, true, 0, null, null, null);
        player.getPlayArea().getGrid()[1][1]=card1;
        player.getPlayArea().getGrid()[1][3]=card2;
        player.getPlayArea().getGrid()[3][1]=card3;
        player.getPlayArea().getGrid()[3][3]=card4;
        player.getPlayArea().check_and_insert(card5, true, 2, 2);
        assertEquals(card5, player.getPlayArea().getGrid()[2][2]);
    }
    /**
     * This test assures that a new resource card folded can be placed with 4 resource card already on the grid
     */
    @Test
    void check_and_insert_4_cards_around_card_to_place_folded() {
        match = new MatchController(1, 4, table);
        player.setPlayArea(new PlayArea(8,8, player));
        Symbols[] symbols_card1 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card1 = new ResourceCard(symbols_card1, Colors.RED, null, true, 0, null, null, null);
        Symbols[] symbols_card2 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card2 = new ResourceCard(symbols_card2, Colors.RED, null, true, 0, null, null, null);
        Symbols[] symbols_card3 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card3 = new ResourceCard(symbols_card3, Colors.GREEN, null, true, 0, null, null, null);
        Symbols[] symbols_card4 = {Symbols.FUNGI, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card4 = new ResourceCard(symbols_card4, Colors.GREEN, null, true, 0, null, null, null);
        Symbols[] symbols_card5 = {Symbols.FUNGI, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card5 = new ResourceCard(symbols_card5, Colors.GREEN, null, true, 0, null, null, null);
        player.getPlayArea().getGrid()[1][1]=card1;
        player.getPlayArea().getGrid()[1][3]=card2;
        player.getPlayArea().getGrid()[3][1]=card3;
        player.getPlayArea().getGrid()[3][3]=card4;
        player.getPlayArea().check_and_insert(card5, false, 2, 2);
        assertEquals(card5, player.getPlayArea().getGrid()[2][2]);
        assertEquals(false, player.getPlayArea().getGrid()[2][2].isFace());
        for(int i=0; i<4; i++){
            assertEquals(Symbols.EMPTY, player.getPlayArea().getGrid()[2][2].getSymbols()[i]);
        }
    }
    /**
     * This test assures that a new resource card not folded can not be placed with 4 resource card already on the grid,
     * because the corner of card4 is NOCORNER
     */@Test
    void check_and_insert_NOCORNER() {
        match = new MatchController(1, 4, table);
        player.setPlayArea(new PlayArea(8,8, player));
        Symbols[] symbols_card1 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card1 = new ResourceCard(symbols_card1, Colors.RED, null, true, 0, null, null, null);
        Symbols[] symbols_card2 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card2 = new ResourceCard(symbols_card2, Colors.RED, null, true, 0, null, null, null);
        Symbols[] symbols_card3 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card3 = new ResourceCard(symbols_card3, Colors.GREEN, null, true, 0, null, null, null);
        Symbols[] symbols_card4 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card4 = new ResourceCard(symbols_card4, Colors.GREEN, null, true, 0, null, null, null);
        Symbols[] symbols_card5 = {Symbols.FUNGI, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card5 = new ResourceCard(symbols_card5, Colors.GREEN, null, true, 0, null, null, null);
        player.getPlayArea().getGrid()[1][1]=card1;
        player.getPlayArea().getGrid()[1][3]=card2;
        player.getPlayArea().getGrid()[3][1]=card3;
        player.getPlayArea().getGrid()[3][3]=card4;
        player.getPlayArea().check_and_insert(card5, true, 2, 2);
        assertEquals(null, player.getPlayArea().getGrid()[2][2]);
    }
    /**
     * This test assures that a new resource card not folded can not be placed with 4 resource card already on the grid,
     * because the corner of card2 is false
     */@Test
    void check_and_insert_false() {
        match = new MatchController(1, 4, table);
        player.setPlayArea(new PlayArea(8,8, player));
        Symbols[] symbols_card1 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card1 = new ResourceCard(symbols_card1, Colors.RED, null, true, 0, null, null, null);
        Symbols[] symbols_card2 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card2 = new ResourceCard(symbols_card2, Colors.RED, null, true, 0, null, null, null);
        card2.getCorners()[2] = false;
        Symbols[] symbols_card3 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card3 = new ResourceCard(symbols_card3, Colors.GREEN, null, true, 0, null, null, null);
        Symbols[] symbols_card4 = {Symbols.FUNGI, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card4 = new ResourceCard(symbols_card4, Colors.GREEN, null, true, 0, null, null, null);
        Symbols[] symbols_card5 = {Symbols.FUNGI, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card5 = new ResourceCard(symbols_card5, Colors.GREEN, null, true, 0, null, null, null);
        player.getPlayArea().getGrid()[1][1]=card1;
        player.getPlayArea().getGrid()[1][3]=card2;
        player.getPlayArea().getGrid()[3][1]=card3;
        player.getPlayArea().getGrid()[3][3]=card4;
        player.getPlayArea().check_and_insert(card5, true, 2, 2);
        assertEquals(null, player.getPlayArea().getGrid()[2][2]);
    }
    /**
     * This test assures that a new resource card not folded can not be placed because there are not near cards
     */@Test
    void check_and_insert_no_true() {
        match = new MatchController(1, 4, table);
        player.setPlayArea(new PlayArea(8,8, player));
        Symbols[] symbols_card1 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card1 = new ResourceCard(symbols_card1, Colors.RED, null, true, 0, null, null, null);
        player.getPlayArea().check_and_insert(card1, true, 2, 2);
        assertEquals(null, player.getPlayArea().getGrid()[2][2]);
    }

    /**
     * This test controls that the grid of a player is correctly extended when a card is placed
     * near the border of the matrix
     */
    @Test
    void extend_matrix() {
        match = new MatchController(1, 4, table);
        player.setPlayArea(new PlayArea(5,5, player));
        player.extend_matrix(1, 3);
        assertEquals(7, player.getPlayArea().getGrid().length);
        player.extend_matrix(4, 4);
        assertEquals(7, player.getPlayArea().getGrid().length);
        player.extend_matrix(3, 1);
        assertEquals(9, player.getPlayArea().getGrid().length);
        player.extend_matrix(1, 1);
        assertEquals(11, player.getPlayArea().getGrid().length);
        player.extend_matrix(9, 3);
        assertEquals(13, player.getPlayArea().getGrid().length);
        player.extend_matrix(3, 11);
        assertEquals(15, player.getPlayArea().getGrid().length);
    }


    /**
     * We are checking if the map of resources of a player is correctly updated with 2 resource card already
     * on the grid and a new resource card folded and its color is green
     */
    @Test
    void update_resources_folded_card_1() {
        match = new MatchController(1, 4, table);
        player.setPlayArea(new PlayArea(8,8, player));
        player.getPlayArea().getAvailableResources().put(Symbols.PLANT, 2);
        player.getPlayArea().getAvailableResources().put(Symbols.INSECT, 2);
        player.getPlayArea().getAvailableResources().put(Symbols.QUILL, 2);
        Symbols[] symbols_card1 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card1 = new ResourceCard(symbols_card1, Colors.RED, null, true, 0, null, null, null);
        Symbols[] symbols_card2 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card2 = new ResourceCard(symbols_card2, Colors.RED, null, true, 0, null, null, null);
        Symbols[] symbols_card3 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card3 = new ResourceCard(symbols_card3, Colors.GREEN, null, true, 0, null, null, null);
        player.getPlayArea().getGrid()[1][1]=card1;
        player.getPlayArea().getGrid()[1][3]=card2;
        player.getPlayArea().getGrid()[2][2]=card3;
        player.getPlayArea().update_resources(card3, false, 2, 2);
        assertEquals(1, player.getPlayArea().getAvailableResources().get(Symbols.QUILL));
        assertEquals(2, player.getPlayArea().getAvailableResources().get(Symbols.PLANT));
        assertEquals(2, player.getPlayArea().getAvailableResources().get(Symbols.INSECT));
    }

    /**
     * We are checking if the map of resources of a player is correctly updated with 2 resource card already
     * on the grid and a new resource card folded and its color is red
     */@Test
    void update_resources_folded_card_2() {
        match = new MatchController(1, 4, table);
        player.setPlayArea(new PlayArea(8,8, player));
        player.getPlayArea().getAvailableResources().put(Symbols.PLANT, 2);
        player.getPlayArea().getAvailableResources().put(Symbols.INSECT, 2);
        player.getPlayArea().getAvailableResources().put(Symbols.QUILL, 2);
        player.getPlayArea().getAvailableResources().put(Symbols.FUNGI, 0);
        Symbols[] symbols_card1 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card1 = new ResourceCard(symbols_card1, Colors.RED, null, true, 0, null, null, null);
        Symbols[] symbols_card2 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card2 = new ResourceCard(symbols_card2, Colors.RED, null, true, 0, null, null, null);
        Symbols[] symbols_card3 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card3 = new ResourceCard(symbols_card3, Colors.RED, null, true, 0, null, null, null);
        player.getPlayArea().getGrid()[1][1]=card1;
        player.getPlayArea().getGrid()[1][3]=card2;
        player.getPlayArea().getGrid()[2][2]=card3;
        player.getPlayArea().update_resources(card3, false, 2, 2);
        assertEquals(1, player.getPlayArea().getAvailableResources().get(Symbols.QUILL));
        assertEquals(1, player.getPlayArea().getAvailableResources().get(Symbols.PLANT));
        assertEquals(2, player.getPlayArea().getAvailableResources().get(Symbols.INSECT));
        assertEquals(1, player.getPlayArea().getAvailableResources().get(Symbols.FUNGI));
    }

    /**
     * We are checking if the map of resources of a player is correctly updated with 2 resource card already
     * on the grid and a new resource card folded and its color is blue
     */@Test
    void update_resources_folded_card_3() {
        match = new MatchController(1, 4, table);
        player.setPlayArea(new PlayArea(8,8, player));
        player.getPlayArea().getAvailableResources().put(Symbols.PLANT, 2);
        player.getPlayArea().getAvailableResources().put(Symbols.INSECT, 2);
        player.getPlayArea().getAvailableResources().put(Symbols.QUILL, 2);
        player.getPlayArea().getAvailableResources().put(Symbols.ANIMAL, 0);
        Symbols[] symbols_card1 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card1 = new ResourceCard(symbols_card1, Colors.RED, null, true, 0, null, null, null);
        Symbols[] symbols_card2 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card2 = new ResourceCard(symbols_card2, Colors.RED, null, true, 0, null, null, null);
        Symbols[] symbols_card3 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card3 = new ResourceCard(symbols_card3, Colors.BLUE, null, true, 0, null, null, null);
        player.getPlayArea().getGrid()[1][1]=card1;
        player.getPlayArea().getGrid()[1][3]=card2;
        player.getPlayArea().getGrid()[2][2]=card3;
        player.getPlayArea().update_resources(card3, false, 2, 2);
        assertEquals(1, player.getPlayArea().getAvailableResources().get(Symbols.QUILL));
        assertEquals(1, player.getPlayArea().getAvailableResources().get(Symbols.PLANT));
        assertEquals(2, player.getPlayArea().getAvailableResources().get(Symbols.INSECT));
        assertEquals(1, player.getPlayArea().getAvailableResources().get(Symbols.ANIMAL));
    }
    /**
     * We are checking if the map of resources of a player is correctly updated with 2 resource card already
     * on the grid and a new resource card folded and its color is purple
     */@Test
    void update_resources_folded_card_4() {
        match = new MatchController(1, 4, table);
        player.setPlayArea(new PlayArea(8,8, player));
        player.getPlayArea().getAvailableResources().put(Symbols.PLANT, 2);
        player.getPlayArea().getAvailableResources().put(Symbols.INSECT, 2);
        player.getPlayArea().getAvailableResources().put(Symbols.QUILL, 2);
        Symbols[] symbols_card1 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card1 = new ResourceCard(symbols_card1, Colors.RED, null, true, 0, null, null, null);
        Symbols[] symbols_card2 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card2 = new ResourceCard(symbols_card2, Colors.RED, null, true, 0, null, null, null);
        Symbols[] symbols_card3 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card3 = new ResourceCard(symbols_card3, Colors.PURPLE, null, true, 0, null, null, null);
        player.getPlayArea().getGrid()[1][1]=card1;
        player.getPlayArea().getGrid()[1][3]=card2;
        player.getPlayArea().getGrid()[2][2]=card3;
        player.getPlayArea().update_resources(card3, false, 2, 2);
        assertEquals(1, player.getPlayArea().getAvailableResources().get(Symbols.QUILL));
        assertEquals(1, player.getPlayArea().getAvailableResources().get(Symbols.PLANT));
        assertEquals(3, player.getPlayArea().getAvailableResources().get(Symbols.INSECT));
    }

    /**
     * We are checking if the map of resources of a player is correctly updated with 2 resource card already
     * on the grid and a new resource card not folded
     */@Test
    void update_resources_not_folded_card_with_3_cards() {
        match = new MatchController(1, 4, table);
        player.setPlayArea(new PlayArea(8,8, player));
        player.getPlayArea().getAvailableResources().put(Symbols.PLANT, 2);
        player.getPlayArea().getAvailableResources().put(Symbols.INSECT, 2);
        player.getPlayArea().getAvailableResources().put(Symbols.QUILL, 2);
        Symbols[] symbols_card1 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card1 = new ResourceCard(symbols_card1, Colors.RED, null, true, 0, null, null, null);
        Symbols[] symbols_card2 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card2 = new ResourceCard(symbols_card2, Colors.RED, null, true, 0, null, null, null);
        Symbols[] symbols_card3 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card3 = new ResourceCard(symbols_card3, Colors.GREEN, null, true, 0, null, null, null);
        player.getPlayArea().getGrid()[1][1]=card1;
        player.getPlayArea().getGrid()[1][3]=card2;
        player.getPlayArea().getGrid()[2][2]=card3;
        player.getPlayArea().update_resources(card3, true, 2, 2);
        assertEquals(2, player.getPlayArea().getAvailableResources().get(Symbols.QUILL));
        assertEquals(2, player.getPlayArea().getAvailableResources().get(Symbols.PLANT));
        assertEquals(3, player.getPlayArea().getAvailableResources().get(Symbols.INSECT));
        assertEquals(0, player.getPlayArea().getAvailableResources().get(Symbols.FUNGI));
    }
    /**
     * We are checking if the map of resources of a player is correctly updated with 4 resource card already
     * on the grid and a new resource card not folded
     */@Test
    void update_resources_not_folded_card_with_5_cards() {
        match = new MatchController(1, 4, table);
        player.setPlayArea(new PlayArea(8,8, player));
        player.getPlayArea().getAvailableResources().put(Symbols.PLANT, 2);
        player.getPlayArea().getAvailableResources().put(Symbols.INSECT, 2);
        player.getPlayArea().getAvailableResources().put(Symbols.QUILL, 2);
        Symbols[] symbols_card1 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card1 = new ResourceCard(symbols_card1, Colors.RED, null, true, 0, null, null, null);
        Symbols[] symbols_card2 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card2 = new ResourceCard(symbols_card2, Colors.RED, null, true, 0, null, null, null);
        Symbols[] symbols_card3 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card3 = new ResourceCard(symbols_card3, Colors.GREEN, null, true, 0, null, null, null);
        Symbols[] symbols_card4 = {Symbols.EMPTY, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card4 = new ResourceCard(symbols_card4, Colors.RED, null, true, 0, null, null, null);
        Symbols[] symbols_card5 = {Symbols.QUILL, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card5 = new ResourceCard(symbols_card5, Colors.RED, null, true, 0, null, null, null);
        player.getPlayArea().getGrid()[1][1]=card1;
        player.getPlayArea().getGrid()[1][3]=card2;
        player.getPlayArea().getGrid()[2][2]=card3;
        player.getPlayArea().getGrid()[3][1]=card4;
        player.getPlayArea().getGrid()[3][3]=card5;
        player.getPlayArea().update_resources(card3, true, 2, 2);
        assertEquals(1, player.getPlayArea().getAvailableResources().get(Symbols.QUILL));
        assertEquals(2, player.getPlayArea().getAvailableResources().get(Symbols.PLANT));
        assertEquals(2, player.getPlayArea().getAvailableResources().get(Symbols.INSECT));
        assertEquals(0, player.getPlayArea().getAvailableResources().get(Symbols.FUNGI));
    }

    /**
     * We are checking if the map of resources of a player is correctly updated  a new starter card not folded
     */@Test
    void update_resources_starter_card_not_folded() {
        match = new MatchController(1, 4, table);
        player.setPlayArea(new PlayArea(8,8, player));
        Symbols[] default_symbols = {Symbols.FUNGI, Symbols.PLANT, Symbols.INSECT, Symbols.ANIMAL};
        Symbols[] symbols = {Symbols.EMPTY, Symbols.NOCORNER, Symbols.INSECT, Symbols.FUNGI};
        Symbols[] centre = {Symbols.ANIMAL, Symbols.PLANT, Symbols.EMPTY};
        Card card = new StarterCard(symbols, null, centre, true, 0, null, null, null, default_symbols);
        player.getPlayArea().getGrid()[2][2] = card;
        player.getPlayArea().update_resources(card, true, 2, 2);
        assertEquals(1, player.getPlayArea().getAvailableResources().get(Symbols.ANIMAL));
        assertEquals(1, player.getPlayArea().getAvailableResources().get(Symbols.PLANT));
        assertEquals(1, player.getPlayArea().getAvailableResources().get(Symbols.INSECT));
        assertEquals(1, player.getPlayArea().getAvailableResources().get(Symbols.FUNGI));
        assertEquals(null,player.getPlayArea().getAvailableResources().get(Symbols.EMPTY));
    }

    /**
     * We are checking if the map of resources of a player is correctly updated  a new starter card folded
     */@Test
    void update_resources_starter_card_folded() {
        match = new MatchController(1, 4, table);
        player.setPlayArea(new PlayArea(8,8, player));
        Symbols[] default_symbols = {Symbols.FUNGI, Symbols.PLANT, Symbols.INSECT, Symbols.ANIMAL};
        Symbols[] symbols = {Symbols.EMPTY, Symbols.NOCORNER, Symbols.INSECT, Symbols.FUNGI};
        Symbols[] centre = {Symbols.ANIMAL, Symbols.PLANT, Symbols.EMPTY};
        Card card = new StarterCard(symbols, null, centre, false, 0, null, null, null, default_symbols);
        //faccio questa modifica preliminare che viene fatta all'inizio di place_card
        if(card.getCentre() != null && card.isFace() == false){
            for(int i=0; i<card.getSymbols().length; i++){
                card.getSymbols()[i] = ((StarterCard) card).getDefaultSymbols()[i];
                card.getCorners()[i] = true;
            }
        }
        player.getPlayArea().getGrid()[2][2] = card;
        player.getPlayArea().update_resources(card, false, 2, 2);
        assertEquals(1, player.getPlayArea().getAvailableResources().get(Symbols.ANIMAL));
        assertEquals(1, player.getPlayArea().getAvailableResources().get(Symbols.PLANT));
        assertEquals(1, player.getPlayArea().getAvailableResources().get(Symbols.INSECT));
        assertEquals(1, player.getPlayArea().getAvailableResources().get(Symbols.FUNGI));
        assertEquals(null,player.getPlayArea().getAvailableResources().get(Symbols.EMPTY));
    }

    /**
     * We are checking if card1 and card2 corners are correctly set to false
     */
    @Test
    void make_covered_corners_false() {
        match = new MatchController(1, 4, table);
        player.setPlayArea(new PlayArea(8,8, player));
        Symbols[] symbols_card1 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card1 = new ResourceCard(symbols_card1, Colors.RED, null, true, 0, null, null, null);
        Symbols[] symbols_card2 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card2 = new ResourceCard(symbols_card2, Colors.RED, null, true, 0, null, null, null);
        Symbols[] symbols_card3 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card3 = new ResourceCard(symbols_card3, Colors.GREEN, null, true, 0, null, null, null);
        player.getPlayArea().getGrid()[1][1]=card1;
        player.getPlayArea().getGrid()[1][3]=card2;
        player.getPlayArea().make_covered_corners_false(2, 2);
        assertEquals(false, player.getPlayArea().getGrid()[1][1].getCorners()[3]);
        assertEquals(false, player.getPlayArea().getGrid()[1][3].getCorners()[2]);
    }

    /**
     * testing if a starter card is correctly placed with the method 'placeStarterCard' and without using
     * the method 'place'
     */
    @Test
    void placeStarterCard(){
        match = new MatchController(1, 4, table);
        player.setPlayArea(new PlayArea(8,8, player));
        Symbols pointsObject = Symbols.EMPTY;
        Symbols[] default_symbols = {Symbols.FUNGI, Symbols.PLANT, Symbols.INSECT, Symbols.ANIMAL};
        Symbols[] symbols = {Symbols.EMPTY, Symbols.NOCORNER, Symbols.INSECT, Symbols.FUNGI};
        Symbols[] centre = {Symbols.ANIMAL, Symbols.PLANT, Symbols.EMPTY};
        StarterCard card = new StarterCard(symbols, null, centre, true, 0, pointsObject, null, null, default_symbols);
        List<Card> hand = new ArrayList<>();
        hand.add(card);
        player.setHand(hand);
        player.placeStarterCard(card, true);
        assertEquals(card, player.getPlayArea().getGrid()[2][2]);
        assertEquals(1, player.getPlayArea().getAvailableResources().get(Symbols.PLANT));
        assertEquals(1, player.getPlayArea().getAvailableResources().get(Symbols.FUNGI));
        assertEquals(1, player.getPlayArea().getAvailableResources().get(Symbols.ANIMAL));
        assertEquals(1, player.getPlayArea().getAvailableResources().get(Symbols.INSECT));
    }
    /**
     * testing if the method viewDifferenceOfResources works with 3 cards on the PlayArea
     */@Test
    void viewDifferenceOfResources1() {
        match = new MatchController(1, 4, table);
        player.setPlayArea(new PlayArea(8,8, player));
        player.getPlayArea().getAvailableResources().put(Symbols.PLANT, 2);
        player.getPlayArea().getAvailableResources().put(Symbols.INSECT, 2);
        player.getPlayArea().getAvailableResources().put(Symbols.QUILL, 2);
        player.getPlayArea().getAvailableResources().put(Symbols.FUNGI, 0);
        Symbols[] symbols_card1 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card1 = new ResourceCard(symbols_card1, Colors.GREEN, null, true, 0, null, null, null);
        Symbols[] symbols_card2 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card2 = new ResourceCard(symbols_card2, Colors.BLUE, null, true, 0, null, null, null);
        Symbols[] symbols_card3 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card3 = new ResourceCard(symbols_card3, Colors.RED, null, true, 0, null, null, null);
        player.getPlayArea().getGrid()[1][1]=card1;
        player.getPlayArea().getGrid()[1][3]=card2;
        player.getPlayArea().getGrid()[2][2]=card3;
        player.viewDifferenceOfResources(card3, "front", 2, 2);
        player.viewDifferenceOfResources(card3, "back", 2, 2);
        player.viewDifferenceOfResources(card1, "front", 3, 1);
        player.viewDifferenceOfResources(card2, "back", 1, 3);
    }

    /**
     * testing if the method viewDifferenceOfResources works with 5 cards on the PlayArea
     */@Test
    void viewDifferenceOfResources2() {
        match = new MatchController(1, 4, table);
        player.setPlayArea(new PlayArea(8,8, player));
        player.getPlayArea().getAvailableResources().put(Symbols.PLANT, 2);
        player.getPlayArea().getAvailableResources().put(Symbols.INSECT, 2);
        player.getPlayArea().getAvailableResources().put(Symbols.QUILL, 2);
        Symbols[] symbols_card1 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card1 = new ResourceCard(symbols_card1, Colors.RED, null, true, 0, null, null, null);
        Symbols[] symbols_card2 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card2 = new ResourceCard(symbols_card2, Colors.RED, null, true, 0, null, null, null);
        Symbols[] symbols_card3 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card3 = new ResourceCard(symbols_card3, Colors.GREEN, null, true, 0, null, null, null);
        Symbols[] symbols_card4 = {Symbols.EMPTY, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card4 = new ResourceCard(symbols_card4, Colors.RED, null, true, 0, null, null, null);
        Symbols[] symbols_card5 = {Symbols.QUILL, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card5 = new ResourceCard(symbols_card5, Colors.PURPLE, null, true, 0, null, null, null);
        player.getPlayArea().getGrid()[1][1]=card1;
        player.getPlayArea().getGrid()[1][3]=card2;
        player.getPlayArea().getGrid()[2][2]=card3;
        player.getPlayArea().getGrid()[3][1]=card4;
        player.getPlayArea().getGrid()[3][3]=card5;
        player.viewDifferenceOfResources(card5, "back", 2, 2);

    }

}