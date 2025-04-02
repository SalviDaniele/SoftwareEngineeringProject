package View.Scene;

import Model.Player;
import Model.Table;
import View.Gui.GuiApplication;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Controller for the FXML file TableScene.fxml
 */
public class TableSceneController extends SceneController{

    /**
     * Enumeration representing the different phases of the game.
     */
    enum Phase{
        STARTING,
        OBJECTIVE,
        DRAW
    }

    /**
     * The current phase of the game.
     */
    Phase phase;

    /**
     * AnchorPane containing the ImageView of the top card on the resource deck.
     */
    @FXML
    private AnchorPane resourceDeckPane;

    /**
     * ImageView containing the image of the top card on the resource deck.
     */
    @FXML
    private ImageView resourceDeck;

    /**
     * The image path of the top card on the resource deck.
     */
    String resourceDeckImage;

    /**
     * AnchorPane containing the ImageView of the top card on the gold deck.
     */
    @FXML
    private AnchorPane goldDeckPane;

    /**
     * ImageView containing the image of the top card on the gold deck.
     */
    @FXML
    private ImageView goldDeck;

    /**
     * The image path of the top card on the gold deck.
     */
    String goldDeckImage;

    /**
     * AnchorPane containing the ImageView of the first resource card on the table.
     */
    @FXML
    private AnchorPane resourceCard1Pane;

    /**
     * ImageView containing the image of the first resource card on the table.
     */
    @FXML
    private ImageView resourceCard1;

    /**
     * The image path of the first resource card on the table.
     */
    String resourceCard1Image;

    /**
     * AnchorPane containing the ImageView of the second resource card on the table.
     */
    @FXML
    private AnchorPane resourceCard2Pane;

    /**
     * ImageView containing the image of the second resource card on the table.
     */
    @FXML
    private ImageView resourceCard2;

    /**
     * The image path of the second resource card on the table.
     */
    String resourceCard2Image;

    /**
     * AnchorPane containing the ImageView of the first gold card on the table.
     */
    @FXML
    private AnchorPane goldCard1Pane;

    /**
     * ImageView containing the image of the first gold card on the table.
     */
    @FXML
    private ImageView goldCard1;

    /**
     * The image path of the first gold card on the table.
     */
    String goldCard1Image;

    /**
     * AnchorPane containing the ImageView of the second gold card on the table.
     */
    @FXML
    private AnchorPane goldCard2Pane;

    /**
     * ImageView containing the image of the second gold card on the table.
     */
    @FXML
    private ImageView goldCard2;

    /**
     * The image path of the second gold card on the table.
     */
    String goldCard2Image;

    /**
     * ImageView containing the image of the first objective card on the table.
     */
    @FXML
    private ImageView objectiveCard1;

    /**
     * The image path of the first objective card on the table.
     */
    String objectiveCard1Image;

    /**
     * ImageView containing the image of the second objective card on the table.
     */
    @FXML
    private ImageView objectiveCard2;

    /**
     * The image path of the second objective card on the table.
     */
    String objectiveCard2Image;

    /**
     * MenuButton for accessing different sections of the application.
     */
    @FXML
    MenuButton menuButton;

    /**
     * MenuItem for navigating to user's PlayArea.
     */
    @FXML
    MenuItem playAreaButton;

    /**
     * MenuItem to open the chat window.
     */
    @FXML
    MenuItem chatButton;

    /**
     * Circle indicating a chat notification on the MenuButton.
     */
    @FXML
    Circle menuCircle;

    /**
     * Circle indicating a chat notification on the Chat MenuItem
     */
    @FXML
    Circle chatCircle;

    /**
     * Label that displays the current turn information.
     */
    @FXML
    Label labelTurn;

    /**
     * Boolean indicating if the objective card had already been drawn.
     */
    boolean obj;

    /**
     * Boolean indicating if it's user's turn to draw the card.
     */
    public boolean yourTurnToDraw = false;

    /**
     * Boolean condition to stop the turn thread.
     */
    boolean exit = false;

    /**
     * Int that indicates if the decks or the cards on the table are empty.
     */
    int whereCanIDraw;

    /**
     * Int which count how many positions the user cannot draw from.
     */
    int noCardsToDraw = 0;

    /**
     * Positions of the cards faced up on the table where the user is authorized to draw
     */
    List<Integer> availablePositionsForDrawing;

    /**
     * An ExecutorService with a fixed thread pool size of 5, used to handle tasks related to
     * the player's turn to draw a card.
     */
    final ExecutorService yourTurnToDrawThread = Executors.newFixedThreadPool(5);

    /**
     * A Future representing the result of an asynchronous computation for managing the turn task.
     * This is used to control the task lifecycle and check if the task is completed.
     */
    private Future<?> turnTaskFuture;

    /**
     * An int value used to check whether the current turn is the second to last or last.
     */
    int turnToEnd;


    /**
     * Initializes the controller class. This method is automatically called when the FXML file is loaded for the first time.
     * It calls the superclass' setTarget method and sets up the menu items for other players' PlayAreas.
     */
    @FXML
    private void initialize(){
        super.setTarget();
        modifyStatus(target -> {
            threads.execute(() -> {
                try {
                    for (String name : target.getPlayersNames()) {
                        if (!name.equals(target.getNickname())) {
                            MenuItem anotherPlayArea = new MenuItem(name);
                            Platform.runLater(() -> menuButton.getItems().add(anotherPlayArea));
                            anotherPlayArea.setOnAction(e -> {
                                stopThread();
                                Objects.requireNonNull(GuiApplication.getGui()).showAnotherPlayArea(name);
                            });
                        }
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            });
        });
    }


    /**
     * Method that sets the exit attribute to false and start the thread responsible for managing the turn cycle.
     */
    public void startThread(){
        exit = false;
        turnTaskFuture = yourTurnToDrawThread.submit(this::waitForYourTurn);
    }


    /**
     * Method that sets the exit attribute to true and stop the thread responsible for managing the turn cycle.
     */
    private void stopThread() {
        exit = true;
    }


    /**
     * Method called to initialize the Table based on the current phase.
     * If the current Phase is equal to STARTING the user has to pick the face of the StarterCard.
     * If the current Phase is equal to OBJECTIVE the user has to choose between two secret objective cards.
     * If the current Phase is equal to DRAW a normal turn starts.
     *
     * @param phaseValue a String representing the current Phase value
     */
    public void tablePhases(String phaseValue){
        switch (phaseValue){
            case "starting":
                phase = Phase.STARTING;
                break;
            case "objective":
                phase = Phase.OBJECTIVE;
                break;
            case "draw":
                phase = Phase.DRAW;
                break;
        }
        switch (phase){
            case STARTING:
                initializeTable();
                modifyStatus(target -> {
                    threads.execute(() -> {
                        try {
                            String cardFront = "", cardBack = "";
                            for (Player p : target.getTable(target.getIdMatch()).getPlayers()) {
                                if (p.getNickname().equals(target.getNickname())) {
                                    cardFront = p.getHand().getFirst().getImageFront();
                                    cardBack = p.getHand().getFirst().getImageBack();
                                }
                            }
                            String finalCardFront = cardFront;
                            String finalCardBack = cardBack;
                            Platform.runLater(() -> GuiApplication.chooseBetweenTwoCards(finalCardFront, finalCardBack, "starter", "STARTER CARD"));
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    });
                });
                phase = Phase.OBJECTIVE;
                break;
            case OBJECTIVE:
                modifyStatus(target -> {
                    threads.execute(() -> {
                        try {
                            if (target.isFirst()) {
                                target.getServer().drawCardsAndPlaceCommonObjectives(target.getIdMatch());
                                target.getServer().giveObjectives(target.getIdMatch());
                            }
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    });
                });
                initializeTable();
                modifyStatus(target -> {
                    threads.execute(() -> {
                        Player player = null;
                        try {
                            for (Player p : target.getTable(target.getIdMatch()).getPlayers()) {
                                if (p.getNickname().equals(target.getNickname()))
                                    player = p;
                            }
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                        assert player != null;

                        //Every player receives 2 objective cards
                        String obCard1;
                        String obCard2;
                        try {
                            if (player.getObChoice1() != null) {
                                obCard1 = player.getObChoice1().getImageFront();
                            } else {
                                obCard1 = target.getTable(target.getIdMatch()).getDeckO().get(1).getImageFront();
                                target.getTable(target.getIdMatch()).getDeckO().remove(1);
                            }
                            if (player.getObChoice2() != null) {
                                obCard2 = player.getObChoice2().getImageFront();
                            } else {
                                obCard2 = target.getTable(target.getIdMatch()).getDeckO().get(2).getImageFront();
                                target.getTable(target.getIdMatch()).getDeckO().remove(2);
                            }
                        } catch (RemoteException e) { throw new RuntimeException(e); }
                        Platform.runLater(() -> GuiApplication.chooseBetweenTwoCards(obCard1, obCard2, "objective", "OBJECTIVE CARD"));
                    });
                });
                break;
            case DRAW:
                startThread();
                noCardsToDraw = 0;
                exit = false;
                initializeTable();
                if(noCardsToDraw == 6 && yourTurnToDraw){
                    modifyStatus(target -> {
                        threads.execute(() -> {
                            try {
                                target.drawTable(5);
                                turnToEnd = target.checkEndPhaseGui();
                            } catch (RemoteException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    });
                    checkEndTurnGui();
                }
        }
    }

    /**
     * Method that initializes the table setting all the images of the relative cards on the
     * Table.
     */
    public void initializeTable () {
        obj = false;
        modifyStatus(target -> {
            try {
                Table table = target.getTable(target.getIdMatch());
                if(!table.getDeckR().isEmpty()) {
                    resourceDeckImage = table.getDeckR().getFirst().getImageBack();
                    resourceDeck.setImage(new Image(resourceDeckImage));
                }
                else {
                    resourceDeckImage = null;
                    resourceDeck.setImage(null);
                    resourceDeckPane.setDisable(true);
                    noCardsToDraw++;
                }
                if(!table.getDeckG().isEmpty()) {
                    goldDeckImage = table.getDeckG().getFirst().getImageBack();
                    goldDeck.setImage(new Image(goldDeckImage));
                }
                else {
                    goldDeckImage = null;
                    goldDeck.setImage(null);
                    goldDeckPane.setDisable(true);
                    noCardsToDraw++;
                }
                if(table.getCardsR()[0] != null) {
                    resourceCard1Image = table.getCardsR()[0].getImageFront();
                    resourceCard1.setImage(new Image(resourceCard1Image));
                }
                else {
                    resourceCard1Image = null;
                    resourceCard1.setImage(null);
                    resourceCard1Pane.setDisable(true);
                    noCardsToDraw++;
                }
                if(table.getCardsR()[1] != null) {
                    resourceCard2Image = table.getCardsR()[1].getImageFront();
                    resourceCard2.setImage(new Image(resourceCard2Image));
                }
                else {
                    resourceCard2Image = null;
                    resourceCard2.setImage(null);
                    resourceCard2Pane.setDisable(true);
                    noCardsToDraw++;
                }
                if(table.getCardsG()[0] != null) {
                    goldCard1Image = table.getCardsG()[0].getImageFront();
                    goldCard1.setImage(new Image(goldCard1Image));
                }
                else {
                    goldCard1Image = null;
                    goldCard1.setImage(null);
                    goldCard1Pane.setDisable(true);
                    noCardsToDraw++;
                }
                if(table.getCardsG()[1] != null) {
                    goldCard2Image = table.getCardsG()[1].getImageFront();
                    goldCard2.setImage(new Image(goldCard2Image));
                }
                else {
                    goldCard2Image = null;
                    goldCard2.setImage(null);
                    goldCard2Pane.setDisable(true);
                    noCardsToDraw++;
                }
                if(table.getCardsO() != null){
                    objectiveCard1Image = table.getCardsO()[0].getImageFront();
                    objectiveCard2Image = table.getCardsO()[1].getImageFront();
                    obj = true;
                } else {
                    objectiveCard1Image = null;
                    objectiveCard2Image = null;
                    obj = false;
                }
            } catch (RemoteException e) {
                System.err.println("\nError while initializing table: " + e.getMessage());
                throw new RuntimeException(e);
            }
            if(obj){
                objectiveCard1.setImage(new Image(objectiveCard1Image));
                objectiveCard2.setImage(new Image(objectiveCard2Image));
            } else {
                objectiveCard1.setImage(null);
                objectiveCard2.setImage(null);
            }
        });
    }


    /**
     * This method is called when the user receives a message from another player.
     * Both menuCircle and chatCircle's visibility are set to true.
     */
    public void activateCircle() {
        menuCircle.setVisible(true);
        chatCircle.setVisible(true);
    }


    /**
     * This method is called when the chat window has been closed. It sets to false the visibility of the chatCircle
     */
    public void disableCircleChat() {
        chatCircle.setVisible(false);
    }


    /**
     * Disable the visibility of the Circle on the menuButton.
     * This FXML method is triggered when the menuButton is Clicked.
     */
    @FXML
    private void disableCircle() {
        menuCircle.setVisible(false);
    }


    /**
     * This FXML method is triggered when the player clicks on the Chat MenuItem.
     * It disables the visibility of the Circle on the ChatButton and then calls the Gui method to open the chat.
     */
    @FXML
    private void openChat() {
        chatCircle.setVisible(false);
        Platform.runLater(() -> Objects.requireNonNull(GuiApplication.getGui()).showChatStage());
    }


    /**
     * This FXML method is triggered when the player clicks on the PlayArea MenuItem.
     * It calls the Gui method to view the user's PlayArea
     */
    @FXML
    private void playAreaView() {
        stopThread();
        Platform.runLater(() -> Objects.requireNonNull(GuiApplication.getGui()).showPlayArea("placing"));
    }


    /**
     * This method let the user know when his second to last turn has finished. It also
     * manages to set the waiting scene for the endgame.
     */
    private void checkEndTurnGui() {
        Platform.runLater(() -> {
            if(turnToEnd == 0){
                Objects.requireNonNull(GuiApplication.getGui()).showWaiting("endgame");
            } else if (turnToEnd == 1) {
                FXMLLoader loader = new FXMLLoader(GuiApplication.class.getResource("/FXML/ErrorMessage.fxml"));
                AnchorPane pane;
                try {
                    pane = loader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                ErrorMessageController error = loader.getController();
                error.generateErrorMessage(pane, "Careful! This was your second to last turn!", "red");
            }
        });
    }


    /**
     * This FXML method is triggered when the player clicks on the resource deck image.
     * It checks if the user can draw from the resource deck, notifies the Client and calls the method to
     * calculate when the last turn is going to be.
     */
    @FXML
    private synchronized void drawResDeck() {
        draw();
        if (availablePositionsForDrawing == null) {
            availablePositionsForDrawing = new ArrayList<>();
        }
        if(yourTurnToDraw && (whereCanIDraw == 0 || whereCanIDraw == 2)){
            modifyStatus(target -> {
                threads.execute(() -> {
                    try {
                        target.drawResource();
                        turnToEnd = target.checkEndPhaseGui();
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                });
            });
            checkEndTurnGui();
            Platform.runLater(this::initializeTable);
            notifyAll();
        }
    }


    /**
     * This FXML method is triggered when the player clicks on the gold deck image.
     * It checks if the user can draw from the gold deck, notifies the Client and calls the method to
     * calculate when the last turn is going to be.
     */
    @FXML
    private synchronized void drawGoldDeck() {
        draw();
        if (availablePositionsForDrawing == null) {
            availablePositionsForDrawing = new ArrayList<>();
        }
        if(yourTurnToDraw && (whereCanIDraw == 0 || whereCanIDraw == 1)) {
            modifyStatus(target -> {
                threads.execute(() -> {
                    try {
                        target.drawGold();
                        turnToEnd = target.checkEndPhaseGui();
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                });
            });
            checkEndTurnGui();
            Platform.runLater(this::initializeTable);
            notifyAll();
        }
    }


    /**
     * This FXML method is triggered when the player clicks on the first resource card image on the table.
     * It checks if the user can draw from the first resource card on the table,
     * notifies the Client and calls the method to calculate when the last turn is going to be.
     */
    @FXML
    private synchronized void drawRes1() {
        draw();
        if (availablePositionsForDrawing == null) {
            availablePositionsForDrawing = new ArrayList<>();
        }
        if(yourTurnToDraw && whereCanIDraw != 4 && availablePositionsForDrawing.contains(1)){
            modifyStatus(target -> {
                threads.execute(() -> {
                    try {
                        target.drawTable(1);
                        turnToEnd = target.checkEndPhaseGui();
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                });
            });
            checkEndTurnGui();
            Platform.runLater(this::initializeTable);
            notifyAll();
        }
    }


    /**
     * This FXML method is triggered when the player clicks on the second resource card image on the table.
     * It checks if the user can draw from the second resource card on the table,
     * notifies the Client and calls the method to calculate when the last turn is going to be.
     */
    @FXML
    private synchronized void drawRes2() {
        draw();
        if (availablePositionsForDrawing == null) {
            availablePositionsForDrawing = new ArrayList<>();
        }
        if(yourTurnToDraw && whereCanIDraw != 4 && availablePositionsForDrawing.contains(2)){
            modifyStatus(target -> {
                threads.execute(() -> {
                    try {
                        target.drawTable(2);
                        turnToEnd = target.checkEndPhaseGui();
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                });
            });
            checkEndTurnGui();
            Platform.runLater(this::initializeTable);
            notifyAll();
        }
    }


    /**
     * This FXML method is triggered when the player clicks on the first gold card image on the table.
     * It checks if the user can draw from the first gold card on the table,
     * notifies the Client and calls the method to calculate when the last turn is going to be.
     */
    @FXML
    private synchronized void drawGold1(){
        draw();
        if (availablePositionsForDrawing == null) {
            availablePositionsForDrawing = new ArrayList<>();
        }
        if(yourTurnToDraw && whereCanIDraw != 4 && availablePositionsForDrawing.contains(3)){
            modifyStatus(target -> {
                threads.execute(() -> {
                    try {
                        target.drawTable(3);
                        turnToEnd = target.checkEndPhaseGui();
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                });
            });
            checkEndTurnGui();
            Platform.runLater(this::initializeTable);
            notifyAll();
        }
    }


    /**
     * This FXML method is triggered when the player clicks on the second gold card image on the table.
     * It checks if the user can draw from the second gold card on the table,
     * notifies the Client and calls the method to calculate when the last turn is going to be.
     */
    @FXML
    private synchronized void drawGold2(){
        draw();
        if (availablePositionsForDrawing == null) {
            availablePositionsForDrawing = new ArrayList<>();
        }
        if(yourTurnToDraw && whereCanIDraw != 4 && availablePositionsForDrawing.contains(4)){
            modifyStatus(target -> {
                threads.execute(() -> {
                    try {
                        target.drawTable(4);
                        turnToEnd = target.checkEndPhaseGui();
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                });
            });
            checkEndTurnGui();
            Platform.runLater(this::initializeTable);
            notifyAll();
        }
    }


    /**
     * This method is called before drawing any card. It checks the position available for drawing.
     */
    public void draw() {
        modifyStatus(target -> {
            threads.execute(() -> {
                try {
                    whereCanIDraw = target.whereCanIDraw();
                    availablePositionsForDrawing = target.availablePositionsForDrawing();
                    if (availablePositionsForDrawing == null) {
                        availablePositionsForDrawing = new ArrayList<>();
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            });
        });
        Platform.runLater(this::initializeTable);
    }


    /**
     * This method is used to manage the turn cycle.
     * This method continuously check if it's the user's turn and updates the status label accordingly.
     * The boolean variable turn is true if it's user's turn, false otherwise.
     * If turn is true and the game is in the DrawPhase the threads wait for the player to draw a card.
     */
    public synchronized void waitForYourTurn(){
        modifyStatus(target -> {
            try {
                while (true) {
                    boolean turn;
                    do {
                        if (exit) {
                            return;
                        }else{
                            turn = target.checkTurn();
                            yourTurnToDraw = turn && target.getPhaseGui().equals("DrawPhase");
                            if (turn && yourTurnToDraw) {
                                Platform.runLater(() -> labelTurn.setText("It's your turn, draw a card!"));
                            } else if (turn) {
                                Platform.runLater(() -> labelTurn.setText("It's your turn, place a card!"));
                                Thread.sleep(1000);
                            } else {
                                Platform.runLater(() -> labelTurn.setText("Wait for your turn"));
                                Thread.sleep(1000);
                            }
                        }
                    } while (!yourTurnToDraw && !turn);
                    if(exit){
                        return;
                    }
                    wait();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
    }
}