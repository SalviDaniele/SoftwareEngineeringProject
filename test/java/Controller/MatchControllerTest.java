package Controller;

import Listeners.EndPoint;
import Model.Cards.*;
import Model.PlayArea;
import Model.Player;
import Model.Table;
import Network.RMI.ServerRMI;
import javafx.css.Match;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * this class test the MatchController and its methods
 */
class MatchControllerTest {

    /**
     * this is an instance of the class tested
     */
    private MatchController match;

    /**
     * a reference to a table
     */
    private Table table;

    /**
     * an instance of the class Player
     */
    private Player player;


    /**
     * before every test it is initialized a new table and a player
     */
    @BeforeEach
    void setUp() {
        try {
            table = new Table();
        } catch (IOException | ParseException e) {
            throw new RuntimeException(String.valueOf(e));
        }
        player = new Player("Carlo", 1, table);
        player.setTable(table);
        table.getPlayers().add(player);
        EndPoint listener = new EndPoint();
        player.addListener(listener);
        ServerRMI serverRMI;
        try {
            ServerSocket serverSocket = new ServerSocket();
            GameController gameController = new GameController(0);
            CopyOnWriteArrayList clients = new CopyOnWriteArrayList<>();
            serverRMI = new ServerRMI(serverSocket, gameController, clients);
            listener.setServer(serverRMI);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    /**
     * This test ensures that the method prepareTable is properly executed checking the number of resource cards, gold cards and
     * objective card on the table, every player's hand size and several booleans
     */
    @Test
    void prepareTable() {
        ServerRMI serverRMI;
        try {
            ServerSocket serverSocket = new ServerSocket();
            GameController gameController = new GameController(0);
            CopyOnWriteArrayList clients = new CopyOnWriteArrayList<>();
            serverRMI = new ServerRMI(serverSocket, gameController, clients);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        PlayArea playArea1 = new PlayArea(5, 5, null);
        Player player1 = new Player("Federico", 1, table);
        playArea1.setPlayer(player1);
        player1.setPlayArea(playArea1);
        player1.setTable(table);
        EndPoint listener1 = new EndPoint();
        player1.addListener(listener1);
        listener1.setServer(serverRMI);
        table.getPlayers().add(player1);
        PlayArea playArea2 = new PlayArea(5, 5, null);
        Player player2 = new Player("Daniele", 1, table);
        playArea2.setPlayer(player2);
        player2.setPlayArea(playArea2);
        player2.setTable(table);
        EndPoint listener2 = new EndPoint();
        player2.addListener(listener2);
        listener2.setServer(serverRMI);
        table.getPlayers().add(player2);
        PlayArea playArea3 = new PlayArea(5, 5, null);
        Player player3 = new Player("Carlo", 1, table);
        playArea3.setPlayer(player3);
        player3.setPlayArea(playArea3);
        player3.setTable(table);
        EndPoint listener3 = new EndPoint();
        player3.addListener(listener3);
        listener3.setServer(serverRMI);
        table.getPlayers().add(player3);
        PlayArea playArea4 = new PlayArea(5, 5, null);
        Player player4 = new Player("Corrado", 1, table);
        playArea4.setPlayer(player4);
        player4.setPlayArea(playArea4);
        player4.setTable(table);
        EndPoint listener4 = new EndPoint();
        player4.addListener(listener4);
        listener4.setServer(serverRMI);
        table.getPlayers().add(player4);
        MatchController match = new MatchController(1, 4, table);
        match.prepareTable();

        assertEquals(2, table.getCardsR().length, "The number of Resource Card on the table should be 2");
        assertTrue(table.getCardsR()[0].getPoints()<=1 && table.getCardsR()[0].getPointsObject()== Symbols.EMPTY && table.getCardsR()[0].getCentre()==null);
        assertTrue(table.getCardsR()[1].getPoints()<=1 && table.getCardsR()[1].getPointsObject()==Symbols.EMPTY && table.getCardsR()[1].getCentre()==null);
        assertTrue(table.getCardsR()[0].isFace() && table.getCardsR()[1].isFace(), "The ResourceCards' faces should be true");

        assertEquals(2, table.getCardsG().length, "The number of Gold Card on the table should be 2");
        assertTrue(table.getCardsG()[0].getPoints()>1 || (table.getCardsG()[0].getPointsObject()!=Symbols.EMPTY));
        assertTrue(table.getCardsG()[1].getPoints()>1 || (table.getCardsG()[1].getPointsObject()!=Symbols.EMPTY));
        assertTrue(table.getCardsG()[0].isFace() && table.getCardsG()[1].isFace(), "The GoldCards' faces should be true");

        assertNotEquals(null, player1.getPlayArea().getGrid()[2][2].getCentre(), "The card in position [2, 2] in playArea of player1 should be a StarterCard");
        assertTrue(player1.getPlayArea().getGrid()[2][2].isFace(), "The StarterCard face should be true");

        assertNotEquals(null, player2.getPlayArea().getGrid()[2][2].getCentre(), "The card in position [2, 2] in playArea of player2 should be a StarterCard");
        assertTrue(player2.getPlayArea().getGrid()[2][2].isFace(), "The StarterCard face should be true");

        assertNotEquals(null, player3.getPlayArea().getGrid()[2][2].getCentre(), "The card in position [2, 2] in playArea of player3 should be a StarterCard");
        assertTrue(player3.getPlayArea().getGrid()[2][2].isFace(), "The StarterCard face should be true");

        assertNotEquals(null, player4.getPlayArea().getGrid()[2][2].getCentre(), "The card in position [2, 2] in playArea of player4 should be a StarterCard");
        assertTrue(player4.getPlayArea().getGrid()[2][2].isFace(), "The StarterCard face should be true");

        assertEquals(3, player1.getHand().size(), "The player should have 3 cards in his hand");
        assertTrue((player1.getHand().get(0).getPoints()<=1 && player1.getHand().get(0).getPointsObject()==Symbols.EMPTY && player1.getHand().get(0).getCentre()==null), "The first card should be a ResourceCard");
        assertTrue((player1.getHand().get(1).getPoints()<=1 && player1.getHand().get(1).getPointsObject()==Symbols.EMPTY && player1.getHand().get(1).getCentre()==null), "The second card should be a ResourceCard");
        assertTrue( player1.getHand().get(2).getPoints()>1 || (player1.getHand().get(2).getPointsObject()!=Symbols.EMPTY), "The third card should be a GoldCard");

        assertEquals(3, player2.getHand().size(), "The player should have 3 cards in his hand");
        assertTrue((player2.getHand().get(0).getPoints()<=1 && player2.getHand().get(0).getPointsObject()==Symbols.EMPTY && player2.getHand().get(0).getCentre()==null), "The first card should be a ResourceCard");
        assertTrue((player2.getHand().get(1).getPoints()<=1 && player2.getHand().get(1).getPointsObject()==Symbols.EMPTY && player2.getHand().get(1).getCentre()==null), "The second card should be a ResourceCard");
        assertTrue( player2.getHand().get(2).getPoints()>1 || (player2.getHand().get(2).getPointsObject()!=Symbols.EMPTY), "The third card should be a GoldCard");

        assertEquals(3, player3.getHand().size(), "The player should have 3 cards in his hand");
        assertTrue((player3.getHand().get(0).getPoints()<=1 && player3.getHand().get(0).getPointsObject()==Symbols.EMPTY && player3.getHand().get(0).getCentre()==null), "The first card should be a ResourceCard");
        assertTrue((player3.getHand().get(1).getPoints()<=1 && player3.getHand().get(1).getPointsObject()==Symbols.EMPTY && player3.getHand().get(1).getCentre()==null), "The second card should be a ResourceCard");
        assertTrue( player3.getHand().get(2).getPoints()>1 || (player3.getHand().get(2).getPointsObject()!=Symbols.EMPTY), "The third card should be a GoldCard");

        assertEquals(3, player4.getHand().size(), "The player should have 3 cards in his hand");
        assertTrue((player4.getHand().get(0).getPoints()<=1 && player4.getHand().get(0).getPointsObject()==Symbols.EMPTY && player4.getHand().get(0).getCentre()==null), "The first card should be a ResourceCard");
        assertTrue((player4.getHand().get(1).getPoints()<=1 && player4.getHand().get(1).getPointsObject()==Symbols.EMPTY && player4.getHand().get(1).getCentre()==null), "The second card should be a ResourceCard");
        assertTrue( player4.getHand().get(2).getPoints()>1 || (player4.getHand().get(2).getPointsObject()!=Symbols.EMPTY), "The third card should be a GoldCard");

        assertEquals(2, table.getCardsO().length, "The number of ObjectiveCard on the table should be 2");
        assertFalse(table.getCardsO()[0].isSecret() && table.getCardsO()[1].isSecret(), "The ObjectiveCards on the table should not be secret");

        assertNotEquals(null, player1.getObjective(), "Player1 should have an ObjectiveCard");
        assertTrue(player1.getObjective().isSecret(), "The player1 ObjectiveCard should be secret");

        assertNotEquals(null, player2.getObjective(), "Player2 should have an ObjectiveCard");
        assertTrue(player2.getObjective().isSecret(), "The player2 ObjectiveCard should be secret");

        assertNotEquals(null, player3.getObjective(), "Player3 should have an ObjectiveCard");
        assertTrue(player3.getObjective().isSecret(), "The player3 ObjectiveCard should be secret");

        assertNotEquals(null, player4.getObjective(), "Player4 should have an ObjectiveCard");
        assertTrue(player4.getObjective().isSecret(), "The player4 ObjectiveCard should be secret");
        Card card= player1.getHand().get(1);
        match.place("Federico",2,"front",1,1);
        match.viewPlayArea("Federico", "Federico");
        //assertEquals(card,player1.getPlayArea().getGrid()[1][1]);
    }


    /**
     *This test assures that return NumOfPlayers
     */
    @Test
    void getNumOfPlayers(){
        match = new MatchController(1, 4, table);
        assertEquals(4, match.getNumOfPlayers());
    }


    /**
     *This test assures that return Table from match
     */
    @Test
    void setTable(){
        match = new MatchController(1, 4, table);
        match.setTable(table);
        assertEquals(table, match.getTable());
    }


    /**
     *Return a Player by name
     */
    @Test
    void getPlayerByName(){
        Table table;
        try {
            table = new Table();
        } catch (IOException | ParseException e) {
            throw new RuntimeException(String.valueOf(e));
        }
        Player player1 = new Player("Carlo", 1, table);
        player1.setTable(table);
        table.getPlayers().add(player1);
        Player player2 = new Player("Corrado", 1, table);
        player2.setTable(table);
        table.getPlayers().add(player2);
        match = new MatchController(1, 4, table);

        assertEquals(player1,match.getPlayerByName("Carlo"));
    }


    /**
     *This test assures that different methods from the class Player work
     */
    @Test
    void playersAction(){
        ServerRMI serverRMI;
        try {
            ServerSocket serverSocket = new ServerSocket();
            GameController gameController = new GameController(0);
            CopyOnWriteArrayList clients = new CopyOnWriteArrayList<>();
            serverRMI = new ServerRMI(serverSocket, gameController, clients);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Table table;
        try {
            table = new Table();
        } catch (IOException | ParseException e) {
            throw new RuntimeException(String.valueOf(e));
        }
        MatchController match = new MatchController(1, 4, table);
        PlayArea playArea1 = new PlayArea(5, 5, null);
        Player player1 = new Player("Federico", 1, table);
        assertEquals(1, player1.getIdMatch());
        player1.setMatch(match);
        assertEquals(match, player1.getMatch());
        player1.setNickname("Federico");
        playArea1.setPlayer(player1);
        player1.setPlayArea(playArea1);
        player1.setTable(table);
        EndPoint listener1 = new EndPoint();
        player1.addListener(listener1);
        listener1.setServer(serverRMI);
        table.getPlayers().add(player1);
        Colors[] colors_red_blue_green = {Colors.RED, Colors.BLUE, Colors.GREEN};
        int[] positions_red_blue_green = {1,1,2};
        ObCardPosition ob = new ObCardPosition(3, null, null, colors_red_blue_green, positions_red_blue_green);
        player1.setObChoice1(ob);
        player1.setObChoice2(ob);
        player1.setObCardUsed(2);
        assertEquals(ob, player1.getObChoice1());
        assertEquals(ob, player1.getObChoice2());
        assertEquals(2, player1.getObCardUsed());
        player1.setBlackPawn(true);
        assertTrue(player1.isBlackPawn());
        player1.viewHandAndArea();
        PlayArea playArea2 = new PlayArea(5, 5, null);
        Player player2 = new Player("Daniele", 1, table);
        playArea2.setPlayer(player2);
        player2.setPlayArea(playArea2);
        player2.setTable(table);
        EndPoint listener2 = new EndPoint();
        player2.addListener(listener2);
        listener2.setServer(serverRMI);
        table.getPlayers().add(player2);
        PlayArea playArea3 = new PlayArea(5, 5, null);
        Player player3 = new Player("Carlo", 1, table);
        playArea3.setPlayer(player3);
        player3.setPlayArea(playArea3);
        player3.setTable(table);
        EndPoint listener3 = new EndPoint();
        player3.addListener(listener3);
        listener3.setServer(serverRMI);
        table.getPlayers().add(player3);
        PlayArea playArea4 = new PlayArea(5, 5, null);
        Player player4 = new Player("Corrado", 1, table);
        playArea4.setPlayer(player4);
        player4.setPlayArea(playArea4);
        player4.setTable(table);
        EndPoint listener4 = new EndPoint();
        player4.addListener(listener4);
        listener4.setServer(serverRMI);
        table.getPlayers().add(player4);

        match.prepareTable();
        player4.setYourTurn(true);
        assertTrue(match.isYourTurn("Corrado"));
        match.viewHand("Corrado");
        match.viewTable("Corrado");
        match.viewStartingTable("Corrado");
        match.viewChoiceObjectives("Corrado");
        match.viewPlayArea("Corrado", "Corrado");
        match.place("Corrado",2,"front",1,1);
        match.putChoiceObjectives("Corrado",2);
        match.putChoiceObjectives("Corrado",1);
        ObjectiveCard card=player4.getObjective();
        assertEquals(card,player4.getObChoice1());
        Card card1 = player4.getPlayArea().getGrid()[2][2];
        assertEquals(card1,player4.getPlayArea().getGrid()[2][2]);
    }


    /**
     * testing if a starter card is properly placed on the PlayArea
     */
    @Test
    void placeStarterCard(){
        MatchController match = new MatchController(1, 4, table);
        PlayArea playArea = new PlayArea(5, 5, null);
        playArea.setPlayer(player);
        player.setPlayArea(playArea);
        player.setTable(table);
        table.getPlayers().add(player);
        Symbols pointsObject = Symbols.EMPTY;
        Symbols[] default_symbols = {Symbols.FUNGI, Symbols.PLANT, Symbols.INSECT, Symbols.ANIMAL};
        Symbols[] symbols = {Symbols.EMPTY, Symbols.NOCORNER, Symbols.INSECT, Symbols.FUNGI};
        Symbols[] centre = {Symbols.ANIMAL, Symbols.PLANT, Symbols.EMPTY};
        StarterCard card = new StarterCard(symbols, null, centre, true, 0, pointsObject, null, null, default_symbols);
        List<Card> hand = new ArrayList<>();
        hand.add(card);
        player.setHand(hand);
        match.placeStarterCard(player.getNickname(), "front");
        assertEquals(card, player.getPlayArea().getGrid()[2][2]);
        assertEquals(1, player.getPlayArea().getAvailableResources().get(Symbols.PLANT));
        assertEquals(1, player.getPlayArea().getAvailableResources().get(Symbols.FUNGI));
        assertEquals(1, player.getPlayArea().getAvailableResources().get(Symbols.ANIMAL));
        assertEquals(1, player.getPlayArea().getAvailableResources().get(Symbols.INSECT));
    }


    /**
     * testing if the points of a player are correctly updated with an objective card by positions and an objective card by symbols
     * on the table and an objective card by positions possessed by the player
     */
    @Test
    void calculateObjectives_case1(){
        MatchController match = new MatchController(1, 4, table);
        PlayArea playArea = new PlayArea(5, 5, null);
        playArea.setPlayer(player);
        player.setPlayArea(playArea);
        player.setTable(table);
        table.getPlayers().add(player);
        Colors[] colors_red_red_green = {Colors.RED, Colors.RED, Colors.GREEN};
        int[] positions_red_red_green = {1,1,2};
        ObCardPosition ob_1 = new ObCardPosition(3, null, null, colors_red_red_green, positions_red_red_green);
        Symbols[] symbols_3_plants = {Symbols.PLANT, Symbols.PLANT, Symbols.PLANT};
        ObCardSymbols ob_2 = new ObCardSymbols(2, null, null, symbols_3_plants);
        ObjectiveCard[] obs = {ob_1, ob_2};
        table.setCardsO(obs);
        Colors[] colors_red_blue_green = {Colors.RED, Colors.BLUE, Colors.GREEN};
        int[] positions_red_blue_green = {1,1,2};
        ObCardPosition ob_3 = new ObCardPosition(3, null, null, colors_red_blue_green, positions_red_blue_green);
        player.setObjective(ob_3);
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
        match.calculateObjectives(player.getNickname());
        assertEquals(3, player.getPoints());
        assertEquals(1, player.getObCardUsed());
    }


    /**
     * testing if the points of a player are correctly updated with an objective card by symbols and an objective card by positions
     * on the table and an objective card by symbols possessed by the player
     */
    @Test
    void calculateObjectives_case2(){
        MatchController match = new MatchController(1, 4, table);
        PlayArea playArea = new PlayArea(5, 5, null);
        playArea.setPlayer(player);
        player.setPlayArea(playArea);
        player.setTable(table);
        table.getPlayers().add(player);
        Symbols[] symbols_2_quills = {Symbols.QUILL, Symbols.QUILL};
        ObCardSymbols ob_card_2_quills = new ObCardSymbols(2, null, null, symbols_2_quills);
        Colors[] colors_green_green_green = {Colors.GREEN, Colors.GREEN, Colors.GREEN};
        int[] positions_green_green_green = {0,1,2};
        ObCardPosition ob_2 = new ObCardPosition(3, null, null, colors_green_green_green, positions_green_green_green);
        ObjectiveCard[] obs = {ob_card_2_quills, ob_2};
        table.setCardsO(obs);
        Symbols[] symbols_three_different = {Symbols.QUILL, Symbols.INKWELL, Symbols.MANUSCRIPT};
        ObCardSymbols ob_card_three_different = new ObCardSymbols(3, null, null, symbols_three_different);
        player.getPlayArea().getAvailableResources().put(Symbols.QUILL, 3);
        player.getPlayArea().getAvailableResources().put(Symbols.MANUSCRIPT, 2);
        player.getPlayArea().getAvailableResources().put(Symbols.INKWELL, 2);
        player.setObjective(ob_card_three_different);
        match.calculateObjectives(player.getNickname());
        assertEquals(8, player.getPoints());
        assertEquals(2, player.getObCardUsed());
    }


    /**
     * testing if all decks are correctly shuffled and cards are properly placed on the table
     * and every player has a starter card
     */
    @Test
    void shuffleDecksAndGiveStarterCards(){
        PlayArea playArea1 = new PlayArea(5, 5, null);
        Player player1 = new Player("Federico", 1, table);
        playArea1.setPlayer(player1);
        player1.setPlayArea(playArea1);
        player1.setTable(table);
        table.getPlayers().add(player1);
        PlayArea playArea2 = new PlayArea(5, 5, null);
        Player player2 = new Player("Daniele", 1, table);
        playArea2.setPlayer(player2);
        player2.setPlayArea(playArea2);
        player2.setTable(table);
        table.getPlayers().add(player2);
        PlayArea playArea3 = new PlayArea(5, 5, null);
        Player player3 = new Player("Carlo", 1, table);
        playArea3.setPlayer(player3);
        player3.setPlayArea(playArea3);
        player3.setTable(table);
        table.getPlayers().add(player3);
        PlayArea playArea4 = new PlayArea(5, 5, null);
        Player player4 = new Player("Corrado", 1, table);
        playArea4.setPlayer(player4);
        player4.setPlayArea(playArea4);
        player4.setTable(table);
        table.getPlayers().add(player4);
        MatchController match = new MatchController(1, 4, table);
        match.shuffleDecksAndGiveStarterCards();
        assertEquals(StarterCard.class, player1.getHand().getFirst().getClass());
        assertEquals(StarterCard.class, player2.getHand().getFirst().getClass());
        assertEquals(StarterCard.class, player3.getHand().getFirst().getClass());
        assertEquals(StarterCard.class, player4.getHand().getFirst().getClass());
        assertNotEquals(player1.getHand().getFirst(), player2.getHand().getFirst());
        assertNotEquals(table.getDeckR().getFirst(), table.getDeckR().get(1));
        assertEquals(2, table.getCardsR().length);
        assertEquals(2, table.getCardsG().length);
    }


    /**
     * testing if every player draws 2 resource cards and a gold card and if common objective
     * cards are correctly placed on the table
     */
    @Test
    void drawCardsAndPlaceCommonObjectives(){
        PlayArea playArea1 = new PlayArea(5, 5, null);
        Player player1 = new Player("Federico", 1, table);
        playArea1.setPlayer(player1);
        player1.setPlayArea(playArea1);
        player1.setTable(table);
        table.getPlayers().add(player1);
        PlayArea playArea2 = new PlayArea(5, 5, null);
        Player player2 = new Player("Daniele", 1, table);
        playArea2.setPlayer(player2);
        player2.setPlayArea(playArea2);
        player2.setTable(table);
        table.getPlayers().add(player2);
        PlayArea playArea3 = new PlayArea(5, 5, null);
        Player player3 = new Player("Carlo", 1, table);
        playArea3.setPlayer(player3);
        player3.setPlayArea(playArea3);
        player3.setTable(table);
        table.getPlayers().add(player3);
        PlayArea playArea4 = new PlayArea(5, 5, null);
        Player player4 = new Player("Corrado", 1, table);
        playArea4.setPlayer(player4);
        player4.setPlayArea(playArea4);
        player4.setTable(table);
        table.getPlayers().add(player4);
        MatchController match = new MatchController(1, 4, table);
        match.drawCardsAndPlaceCommonObjectives();
        assertEquals(3, player1.getHand().size());
        assertEquals(3, player2.getHand().size());
        assertEquals(3, player3.getHand().size());
        assertEquals(3, player4.getHand().size());
        assertEquals(ResourceCard.class, player1.getHand().getFirst().getClass());
        assertEquals(ResourceCard.class, player2.getHand().getFirst().getClass());
        assertEquals(ResourceCard.class, player3.getHand().getFirst().getClass());
        assertEquals(ResourceCard.class, player4.getHand().getFirst().getClass());
        assertEquals(ResourceCard.class, player1.getHand().get(1).getClass());
        assertEquals(ResourceCard.class, player2.getHand().get(1).getClass());
        assertEquals(ResourceCard.class, player3.getHand().get(1).getClass());
        assertEquals(ResourceCard.class, player4.getHand().get(1).getClass());
        assertEquals(GoldCard.class, player1.getHand().get(2).getClass());
        assertEquals(GoldCard.class, player2.getHand().get(2).getClass());
        assertEquals(GoldCard.class, player3.getHand().get(2).getClass());
        assertEquals(GoldCard.class, player4.getHand().get(2).getClass());
        assertEquals(2, table.getCardsO().length);
        assertFalse(table.getCardsO()[0].isSecret());
        assertFalse(table.getCardsO()[1].isSecret());
    }


    /**
     * testing if every player has taken 2 objective cards to choice
     */
    @Test
    void giveObjectives(){
        PlayArea playArea1 = new PlayArea(5, 5, null);
        Player player1 = new Player("Federico", 1, table);
        playArea1.setPlayer(player1);
        player1.setPlayArea(playArea1);
        player1.setTable(table);
        table.getPlayers().add(player1);
        PlayArea playArea2 = new PlayArea(5, 5, null);
        Player player2 = new Player("Daniele", 1, table);
        playArea2.setPlayer(player2);
        player2.setPlayArea(playArea2);
        player2.setTable(table);
        table.getPlayers().add(player2);
        PlayArea playArea3 = new PlayArea(5, 5, null);
        Player player3 = new Player("Carlo", 1, table);
        playArea3.setPlayer(player3);
        player3.setPlayArea(playArea3);
        player3.setTable(table);
        table.getPlayers().add(player3);
        PlayArea playArea4 = new PlayArea(5, 5, null);
        Player player4 = new Player("Corrado", 1, table);
        playArea4.setPlayer(player4);
        player4.setPlayArea(playArea4);
        player4.setTable(table);
        table.getPlayers().add(player4);
        MatchController match = new MatchController(1, 4, table);
        match.giveObjectives();
        assertTrue(player1.getObChoice1().isSecret());
        assertTrue(player2.getObChoice1().isSecret());
        assertTrue(player3.getObChoice1().isSecret());
        assertTrue(player3.getObChoice1().isSecret());
        assertNotEquals(null, player1.getObChoice1());
        assertNotEquals(null, player1.getObChoice2());
        assertNotEquals(null, player2.getObChoice1());
        assertNotEquals(null, player2.getObChoice2());
        assertNotEquals(null, player3.getObChoice1());
        assertNotEquals(null, player3.getObChoice2());
        assertNotEquals(null, player4.getObChoice1());
        assertNotEquals(null, player4.getObChoice2());
    }


    /**
     * testing several simple getter and setter methods
     */
    @Test
    void setterAndGetter(){
        ServerRMI serverRMI;
        try {
            ServerSocket serverSocket = new ServerSocket();
            GameController gameController = new GameController(0);
            CopyOnWriteArrayList clients = new CopyOnWriteArrayList<>();
            serverRMI = new ServerRMI(serverSocket, gameController, clients);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            table = new Table();
        } catch (IOException | ParseException e) {
            throw new RuntimeException(String.valueOf(e));
        }
        PlayArea playArea1 = new PlayArea(5, 5, null);
        Player player1 = new Player("Federico", 1, table);
        playArea1.setPlayer(player1);
        player1.setPlayArea(playArea1);
        player1.setPoints(4);
        EndPoint listener1 = new EndPoint();
        player1.addListener(listener1);
        listener1.setServer(serverRMI);
        table.getPlayers().add(player1);
        PlayArea playArea2 = new PlayArea(5, 5, null);
        Player player2 = new Player("Daniele", 1, table);
        playArea2.setPlayer(player2);
        player2.setPlayArea(playArea2);
        player2.setPoints(10);
        EndPoint listener2 = new EndPoint();
        player2.addListener(listener2);
        listener2.setServer(serverRMI);
        table.getPlayers().add(player2);
        PlayArea playArea3 = new PlayArea(5, 5, null);
        Player player3 = new Player("Carlo", 1, table);
        playArea3.setPlayer(player3);
        player3.setPlayArea(playArea3);
        player3.setPoints(30);
        EndPoint listener3 = new EndPoint();
        player3.addListener(listener3);
        listener3.setServer(serverRMI);
        table.getPlayers().add(player3);
        PlayArea playArea4 = new PlayArea(5, 5, null);
        Player player4 = new Player("Corrado", 1, table);
        playArea4.setPlayer(player4);
        player4.setPlayArea(playArea4);
        player4.setPoints(10);
        EndPoint listener4 = new EndPoint();
        player4.addListener(listener4);
        listener4.setServer(serverRMI);
        table.getPlayers().add(player4);
        MatchController match = new MatchController(1, 4, table);
        match.chooseTheBlackPawn();
        match.setNumOfStarterCardsPlaced(2);
        assertEquals(2, match.getNumOfStarterCardsPlaced());
        Colors[] colors = {Colors.GREEN, Colors.RED, Colors.PURPLE};
        match.setAvailableColors(colors);
        match.getAvailableColors();
        assertEquals(Colors.RED, match.getAvailableColors()[1]);
        match.setNumOfObjectiveCardChosen(2);
        assertEquals(2, match.getNumOfObjectiveCardChosen());
        match.setNameOfThePlayerWithTheBlackPawn(player1.getNickname());
        assertEquals(player1.getNickname(), match.getNameOfThePlayerWithTheBlackPawn());
        match.setYourTurn(player1.getNickname(), true);
        match.viewHandAndArea(player1.getNickname());
        match.setBlackPawnChosen(true);
        assertTrue(match.isBlackPawnChosen());
        match.setLastRound(true);
        assertTrue(match.isLastRound());
        match.setSecondToLastRound(true);
        assertTrue(match.isSecondToLastRound());
        match.setNumOfPlayersThatHaveFinishedToPlay(2);
        assertEquals(2, match.getNumOfPlayersThatHaveFinishedToPlay());
        match.setNumOfObjectiveCardCalculated(2);
        assertEquals(2, match.getNumOfObjectiveCardCalculated());
        match.chooseTheBlackPawn();
    }


    /**
     * testing if the method cheat1 works giving a player 10000 of each resource
     */
    @Test
    void cheat1(){
        PlayArea playArea1 = new PlayArea(5, 5, null);
        Player player1 = new Player("Federico", 1, table);
        playArea1.setPlayer(player1);
        player1.setPlayArea(playArea1);
        player1.setTable(table);
        table.getPlayers().add(player1);
        MatchController match = new MatchController(1, 4, table);
        match.cheat1(player1.getNickname());
        assertEquals(10000, player1.getPlayArea().getAvailableResources().get(Symbols.PLANT));
        assertEquals(10000, player1.getPlayArea().getAvailableResources().get(Symbols.FUNGI));
        assertEquals(10000, player1.getPlayArea().getAvailableResources().get(Symbols.ANIMAL));
        assertEquals(10000, player1.getPlayArea().getAvailableResources().get(Symbols.INSECT));
    }


    /**
     * testing if the method cheat2 works giving a player 20 points
     */
    @Test
    void cheat2(){
        PlayArea playArea1 = new PlayArea(5, 5, null);
        Player player1 = new Player("Federico", 1, table);
        playArea1.setPlayer(player1);
        player1.setPlayArea(playArea1);
        player1.setTable(table);
        table.getPlayers().add(player1);
        MatchController match = new MatchController(1, 4, table);
        match.cheat2(player1.getNickname());
        assertEquals(20, player1.getPoints());
    }


    /**
     * testing all the methods used to calculate every winner if there is a player with more points than the others
     */
    @Test
    void winner_1(){
        try {
            table = new Table();
        } catch (IOException | ParseException e) {
            throw new RuntimeException(String.valueOf(e));
        }
        PlayArea playArea1 = new PlayArea(5, 5, null);
        Player player1 = new Player("Federico", 1, table);
        playArea1.setPlayer(player1);
        player1.setPlayArea(playArea1);
        player1.setPoints(4);
        table.getPlayers().add(player1);
        PlayArea playArea2 = new PlayArea(5, 5, null);
        Player player2 = new Player("Daniele", 1, table);
        playArea2.setPlayer(player2);
        player2.setPlayArea(playArea2);
        player2.setPoints(10);
        table.getPlayers().add(player2);
        PlayArea playArea3 = new PlayArea(5, 5, null);
        Player player3 = new Player("Carlo", 1, table);
        playArea3.setPlayer(player3);
        player3.setPlayArea(playArea3);
        player3.setPoints(30);
        table.getPlayers().add(player3);
        PlayArea playArea4 = new PlayArea(5, 5, null);
        Player player4 = new Player("Corrado", 1, table);
        playArea4.setPlayer(player4);
        player4.setPlayArea(playArea4);
        player4.setPoints(10);
        table.getPlayers().add(player4);
        MatchController match = new MatchController(1, 4, table);
        match.calculateWinner();
        assertEquals(player3, match.getWinners()[0]);
    }


    /**
     * testing all the methods used to calculate every winner if there is a player with more points and more
     * objective cards resolved
     */
    @Test
    void winner_2(){
        try {
            table = new Table();
        } catch (IOException | ParseException e) {
            throw new RuntimeException(String.valueOf(e));
        }
        PlayArea playArea1 = new PlayArea(5, 5, null);
        Player player1 = new Player("Federico", 1, table);
        playArea1.setPlayer(player1);
        player1.setPlayArea(playArea1);
        player1.setPoints(4);
        table.getPlayers().add(player1);
        PlayArea playArea2 = new PlayArea(5, 5, null);
        Player player2 = new Player("Daniele", 1, table);
        playArea2.setPlayer(player2);
        player2.setPlayArea(playArea2);
        player2.setPoints(30);
        player2.setObCardUsed(2);
        table.getPlayers().add(player2);
        PlayArea playArea3 = new PlayArea(5, 5, null);
        Player player3 = new Player("Carlo", 1, table);
        playArea3.setPlayer(player3);
        player3.setPlayArea(playArea3);
        player3.setPoints(30);
        player3.setObCardUsed(3);
        table.getPlayers().add(player3);
        PlayArea playArea4 = new PlayArea(5, 5, null);
        Player player4 = new Player("Corrado", 1, table);
        playArea4.setPlayer(player4);
        player4.setPlayArea(playArea4);
        player4.setPoints(10);
        table.getPlayers().add(player4);
        MatchController match = new MatchController(1, 4, table);
        match.calculateWinner();
        assertEquals(player3, match.getWinners()[0]);
    }


    /**
     * testing all the methods used to calculate every winner if there are 2 players with more points and more
     * objective cards resolved
     */
    @Test
    void winner_3(){
        try {
            table = new Table();
        } catch (IOException | ParseException e) {
            throw new RuntimeException(String.valueOf(e));
        }
        PlayArea playArea1 = new PlayArea(5, 5, null);
        Player player1 = new Player("Federico", 1, table);
        playArea1.setPlayer(player1);
        player1.setPlayArea(playArea1);
        player1.setPoints(4);
        table.getPlayers().add(player1);
        PlayArea playArea2 = new PlayArea(5, 5, null);
        Player player2 = new Player("Daniele", 1, table);
        playArea2.setPlayer(player2);
        player2.setPlayArea(playArea2);
        player2.setPoints(30);
        player2.setObCardUsed(2);
        table.getPlayers().add(player2);
        PlayArea playArea3 = new PlayArea(5, 5, null);
        Player player3 = new Player("Carlo", 1, table);
        playArea3.setPlayer(player3);
        player3.setPlayArea(playArea3);
        player3.setPoints(30);
        player3.setObCardUsed(2);
        table.getPlayers().add(player3);
        PlayArea playArea4 = new PlayArea(5, 5, null);
        Player player4 = new Player("Corrado", 1, table);
        playArea4.setPlayer(player4);
        player4.setPlayArea(playArea4);
        player4.setPoints(10);
        table.getPlayers().add(player4);
        MatchController match = new MatchController(1, 4, table);
        match.calculateWinner();
        assertEquals(player2, match.getWinners()[0]);
        assertEquals(player3, match.getWinners()[1]);
        assertNull(match.getWinners()[2]);
    }


    /**
     * testing if the method showEndGameMessage works properly
     */
    @Test
    void showEndGameMessage(){
        ServerRMI serverRMI;
        try {
            ServerSocket serverSocket = new ServerSocket();
            GameController gameController = new GameController(0);
            CopyOnWriteArrayList clients = new CopyOnWriteArrayList<>();
            serverRMI = new ServerRMI(serverSocket, gameController, clients);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        MatchController match = new MatchController(1, 4, table);
        PlayArea playArea1 = new PlayArea(5, 5, null);
        Player player1 = new Player("Federico", 1, table);
        playArea1.setPlayer(player1);
        player1.setPlayArea(playArea1);
        player1.setPoints(4);
        EndPoint listener1 = new EndPoint();
        player1.addListener(listener1);
        listener1.setServer(serverRMI);
        table.getPlayers().add(player1);
        PlayArea playArea2 = new PlayArea(5, 5, null);
        Player player2 = new Player("Daniele", 1, table);
        playArea2.setPlayer(player2);
        player2.setPlayArea(playArea2);
        player2.setPoints(30);
        player2.setObCardUsed(2);
        EndPoint listener2 = new EndPoint();
        player2.addListener(listener2);
        listener2.setServer(serverRMI);
        table.getPlayers().add(player2);
        match.getWinners()[0] = player1;
        match.getWinners()[1] = null;
        match.showEndGameMessage(player1.getNickname());
    }


    /**
     * testing if the turn is correctly set using this method
     */
    @Test
    void itIsMyTurn(){
        try {
            table = new Table();
        } catch (IOException | ParseException e) {
            throw new RuntimeException(String.valueOf(e));
        }
        PlayArea playArea1 = new PlayArea(5, 5, null);
        Player player1 = new Player("Federico", 1, table);
        playArea1.setPlayer(player1);
        player1.setPlayArea(playArea1);
        player1.setPoints(4);
        table.getPlayers().add(player1);
        PlayArea playArea2 = new PlayArea(5, 5, null);
        Player player2 = new Player("Daniele", 1, table);
        playArea2.setPlayer(player2);
        player2.setPlayArea(playArea2);
        player2.setPoints(30);
        player2.setObCardUsed(2);
        table.getPlayers().add(player2);
        PlayArea playArea3 = new PlayArea(5, 5, null);
        Player player3 = new Player("Carlo", 1, table);
        playArea3.setPlayer(player3);
        player3.setPlayArea(playArea3);
        player3.setPoints(30);
        player3.setObCardUsed(2);
        table.getPlayers().add(player3);
        PlayArea playArea4 = new PlayArea(5, 5, null);
        Player player4 = new Player("Corrado", 1, table);
        playArea4.setPlayer(player4);
        player4.setPlayArea(playArea4);
        player4.setPoints(10);
        table.getPlayers().add(player4);
        MatchController match = new MatchController(1, 4, table);
        match.itIsMyTurn(player1.getNickname());
        assertTrue(player1.isYourTurn());
        assertFalse(player2.isYourTurn());
        assertFalse(player3.isYourTurn());
        assertFalse(player4.isYourTurn());
    }


    /**
     * testing if all the methods used in the controller for choosing the color of the pawn work properly
     */
    @Test
    void colors(){
        try {
            table = new Table();
        } catch (IOException | ParseException e) {
            throw new RuntimeException(String.valueOf(e));
        }
        PlayArea playArea1 = new PlayArea(5, 5, null);
        Player player1 = new Player("Federico", 1, table);
        playArea1.setPlayer(player1);
        player1.setPlayArea(playArea1);
        player1.setPoints(4);
        table.getPlayers().add(player1);
        PlayArea playArea2 = new PlayArea(5, 5, null);
        Player player2 = new Player("Daniele", 1, table);
        playArea2.setPlayer(player2);
        player2.setPlayArea(playArea2);
        player2.setPoints(30);
        player2.setObCardUsed(2);
        table.getPlayers().add(player2);
        PlayArea playArea3 = new PlayArea(5, 5, null);
        Player player3 = new Player("Carlo", 1, table);
        playArea3.setPlayer(player3);
        player3.setPlayArea(playArea3);
        player3.setPoints(30);
        player3.setObCardUsed(2);
        table.getPlayers().add(player3);
        PlayArea playArea4 = new PlayArea(5, 5, null);
        Player player4 = new Player("Corrado", 1, table);
        playArea4.setPlayer(player4);
        player4.setPlayArea(playArea4);
        player4.setPoints(10);
        table.getPlayers().add(player4);
        MatchController match = new MatchController(1, 4, table);
        Colors[] colors = {Colors.GREEN, Colors.RED, Colors.PURPLE};
        match.setAvailableColors(colors);
        player1.setYourTurn(true);
        player2.setYourTurn(false);
        player3.setYourTurn(false);
        player4.setYourTurn(false);
        assertTrue(match.canIChooseTheColor(player1.getNickname()));
        assertFalse(match.canIChooseTheColor(player2.getNickname()));
        assertFalse(match.canIChooseTheColor(player3.getNickname()));
        assertFalse(match.canIChooseTheColor(player4.getNickname()));
        assertTrue(match.checkColor(Colors.RED));
        assertFalse(match.checkColor(Colors.BLUE));
        match.removeColorAndPassTurn(player1.getNickname(), Colors.RED);
        assertNull(match.getAvailableColors()[1]);
        assertFalse(player1.isYourTurn());
        assertTrue(player2.isYourTurn());
    }


    /**
     * testing if all the methods used for checking if a card can be placed in a specific position
     * or a gold card can be placed satisfying its requirements
     */
    @Test
    void cardPlaceable(){
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
        MatchController match = new MatchController(1, 4, table);
        match.areCoordinatesValid(player.getNickname(), 2, 2);
        assertTrue(match.checkCoordinates(player.getPlayArea().getGrid(), 2, 2));

        player.setPlayArea(new PlayArea(8,8, player));
        Symbols[] symbols_card6 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card6 = new ResourceCard(symbols_card6, Colors.RED, null, true, 0, null, null, null);
        Symbols[] symbols_card7 = {Symbols.FUNGI, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card7 = new ResourceCard(symbols_card7, Colors.GREEN, null, true, 0, null, null, null);
        player.getPlayArea().getGrid()[1][1]=card6;
        match.areCoordinatesValid(player.getNickname(), 2, 2);
        assertTrue(match.checkCoordinates(player.getPlayArea().getGrid(), 2, 2));

        player.setPlayArea(new PlayArea(8,8, player));
        player.getPlayArea().getAvailableResources().put(Symbols.ANIMAL, 0);
        player.getPlayArea().getAvailableResources().put(Symbols.FUNGI, 0);
        player.getPlayArea().getAvailableResources().put(Symbols.PLANT, 2);
        player.getPlayArea().getAvailableResources().put(Symbols.INSECT, 2);
        player.getPlayArea().getAvailableResources().put(Symbols.QUILL, 2);
        Map<Symbols, Integer> requirements3 = new HashMap<>();
        requirements3.put(Symbols.PLANT, 2);
        requirements3.put(Symbols.FUNGI, 0);
        requirements3.put(Symbols.ANIMAL, 0);
        requirements3.put(Symbols.INSECT, 0);
        Symbols points_object_card3 = Symbols.QUILL;
        Symbols[] symbols_card10 = {Symbols.NOCORNER, Symbols.INSECT, Symbols.QUILL, Symbols.PLANT};
        Card card10 = new GoldCard(symbols_card3, Colors.GREEN, null, true, 2, points_object_card3, null, null, requirements3);
        player.getHand().add(card10);
        assertTrue(match.canIPlaceTheGoldCard(player.getNickname(), 1));
        player.getHand().add(card1);
        assertTrue(match.canIPlaceTheGoldCard(player.getNickname(), 2));
    }


    /**
     * testing if the method isNextPlayerTheBlackPawn run correctly
     */
    @Test
    void isNextPlayerTheBlackPawn(){
        try {
            table = new Table();
        } catch (IOException | ParseException e) {
            throw new RuntimeException(String.valueOf(e));
        }
        PlayArea playArea1 = new PlayArea(5, 5, null);
        Player player1 = new Player("Federico", 1, table);
        playArea1.setPlayer(player1);
        player1.setPlayArea(playArea1);
        player1.setPoints(4);
        table.getPlayers().add(player1);
        PlayArea playArea2 = new PlayArea(5, 5, null);
        Player player2 = new Player("Daniele", 1, table);
        playArea2.setPlayer(player2);
        player2.setPlayArea(playArea2);
        player2.setPoints(30);
        player2.setObCardUsed(2);
        table.getPlayers().add(player2);
        player2.setBlackPawn(true);
        MatchController match = new MatchController(1, 4, table);
        assertTrue(match.isNextPlayerTheBlackPawn(player1.getNickname()));
        player1.setPoints(20);
        assertEquals(2, match.checkSecondToLastTurn(player1.getNickname()));
    }


    /**
     * assures that the method cheat3 works correctly clearing both decks
     */
    @Test
    void cheat3(){
        MatchController match = new MatchController(1, 4, table);
        match.cheat3();
        assertEquals(0, match.getTable().getDeckR().size());
        assertEquals(0, match.getTable().getDeckG().size());
    }


    /**
     * testing that the method whereCanIDraw return the right integer
     */
    @Test
    void whereCanIDraw(){
        MatchController match = new MatchController(1, 4, table);
        assertEquals(0, match.whereCanIDraw());
        match.prepareTable();
        match.getTable().getDeckR().clear();
        assertEquals(1, match.whereCanIDraw());
        match.getTable().getDeckG().clear();
        assertEquals(3, match.whereCanIDraw());
        match.getTable().getCardsR()[0] = null;
        match.getTable().getCardsR()[1] = null;
        match.getTable().getCardsG()[0] = null;
        match.getTable().getCardsG()[1] = null;
        assertEquals(4, match.whereCanIDraw());
    }


    /**
     * testing that the method availablePositionsForDrawing return a list that contains the right integer
     */
    @Test
    void availablePositionsForDrawing(){
        MatchController match = new MatchController(1, 4, table);
        match.prepareTable();
        assertTrue(match.availablePositionsForDrawing().contains(1));
        assertTrue(match.availablePositionsForDrawing().contains(2));
        assertTrue(match.availablePositionsForDrawing().contains(3));
        assertTrue(match.availablePositionsForDrawing().contains(4));
        match.getTable().getCardsG()[1] = null;
        assertFalse(match.availablePositionsForDrawing().contains(4));
        match.getTable().getCardsR()[1] = null;
        assertFalse(match.availablePositionsForDrawing().contains(2));
        match.getTable().getCardsG()[0] = null;
        assertFalse(match.availablePositionsForDrawing().contains(3));
        match.getTable().getCardsR()[0] = null;
        assertFalse(match.availablePositionsForDrawing().contains(1));
        assertTrue(match.availablePositionsForDrawing().isEmpty());
    }
}