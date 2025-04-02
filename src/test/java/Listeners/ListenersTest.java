package Listeners;

import Controller.GameController;
import Controller.MatchController;
import Model.Cards.*;
import Model.PlayArea;
import Model.Player;
import Model.Table;
import Network.RMI.ServerRMI;
import Network.VirtualServer;
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
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

class ListenersTest {

    /**
     * before each test these actions are performed
     */
    @BeforeEach
    void setUp() {
        Player player;
        Table table;
        try {
            table = new Table();
            player = new Player("Carlo", 1, table);
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * tests for the class EndPoint
     */
    @Test
    void endPoint() {
        Player player;
        Table table;
        try {
            table = new Table();
            player = new Player("Carlo", 1, table);
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
        List<Card> hand = new ArrayList<>();
        hand.add(table.getDeckS().getFirst());
        player.setHand(hand);
        EndPoint endPoint = new EndPoint();
        ServerRMI serverRMI;
        try {
            ServerSocket serverSocket = new ServerSocket();
            GameController gameController = new GameController(0);
            CopyOnWriteArrayList clients = new CopyOnWriteArrayList<>();
            serverRMI = new ServerRMI(serverSocket, gameController, clients);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        endPoint.setServer(serverRMI);
        assertEquals(serverRMI, endPoint.getServer());
        endPoint.onNotify(GameStatus.VIEW_STARTER_CARD, player);
        endPoint.onNotify(GameStatus.STARTER_CARD_PLACED, player);
        endPoint.onNotify(GameStatus.VIEW_SECRET_OBJECTIVE, player);
        endPoint.onNotify(GameStatus.VIEW_AREA, player);
        endPoint.onNotify(GameStatus.ACTION_FAILED, player);
    }

    /**
     * tests for the class ListenerGui
     */
    @Test
    void listenerGui(){
        MatchController controller = new MatchController();
        GuiStatusModifier<MatchController> guiStatusModifier = new GuiStatusModifier<>();
        guiStatusModifier.setTarget(controller);
        assertEquals(controller, guiStatusModifier.getTarget());
        Consumer<MatchController> action = new Consumer() {
            @Override
            public void accept(Object o) {}};
        guiStatusModifier.modifyStatus(action);
    }

    /**
     * tests for the class Message
     */
    @Test
    void message(){
        Table table;
        try {
            table = new Table();
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
        Message message = new Message();
        message.setArgs("test");
        assertEquals("test", message.getArgs());
        message.setNickname("Carlo");
        assertEquals("Carlo", message.getNickname());
        message.setStarterCard(table.getDeckS().getFirst());
        assertEquals(table.getDeckS().getFirst(), message.getStarterCard());
        ObjectiveCard[] cardsO = {table.getDeckO().getFirst()};
        message.setCardsO(cardsO);
        assertEquals(cardsO, message.getCardsO());
        GoldCard[] cardsG = {table.getDeckG().getFirst()};
        message.setCardsG(cardsG);
        assertEquals(cardsG, message.getCardsG());
        ResourceCard[] cardsR = {table.getDeckR().getFirst()};
        message.setCardsR(cardsR);
        assertEquals(cardsR, message.getCardsR());
        message.setColorsG(Colors.RED);
        assertEquals(Colors.RED, message.getColorsG());
        message.setColorsR(Colors.RED);
        assertEquals(Colors.RED, message.getColorsR());
        message.setDeckR(table.getDeckR());
        assertEquals(table.getDeckR(), message.getDeckR());
        message.setDeckG(table.getDeckG());
        assertEquals(table.getDeckG(), message.getDeckG());
        PlayArea playArea = new PlayArea(5, 5, new Player());
        message.setPlayArea(playArea);
        assertEquals(playArea, message.getPlayArea());
        message.setAreaOwner("Corrado");
        assertEquals("Corrado", message.getAreaOwner());
        message.setPoints(10);
        assertEquals(10, message.getPoints());
        List<Card> hand = new ArrayList<>();
        message.setHand(hand);
        assertEquals(hand, message.getHand());
        message.setTable(table);
        assertEquals(table, message.getTable());
        message.setObchoice1(table.getDeckO().getFirst());
        message.setObchoice2(table.getDeckO().getFirst());
        assertEquals(table.getDeckO().getFirst(), message.getObchoice1());
        assertEquals(table.getDeckO().getFirst(), message.getObchoice2());
        Map<Symbols, Integer> availableResources = new HashMap<>();
        message.setAvailableResources(availableResources);
        assertEquals(availableResources, message.getAvailableResources());
        message.setStatus(GameStatus.IT_IS_YOUR_TURN);
        assertEquals(GameStatus.IT_IS_YOUR_TURN, message.getStatus());
    }
}