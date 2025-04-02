package View.Scene;

import Model.Cards.Card;
import View.Gui.GuiApplication;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;


import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Controller for the FXML file AnotherPlayAreaScene.fxml
 */
public class AnotherPlayAreaController extends SceneController{

    /**
     * The width of the card ImageView in the Gui
     */
    final static int CARD_WIDTH = 201;

    /**
     * The height of the card ImageView in the Gui
     */
    final static int CARD_HEIGHT = 135;

    /**
     * Label that displays the current turn information.
     */
    @FXML
    Label labelTurn;

    /**
     * Label that displays the current player's points.
     */
    @FXML
    Label playerPoints;

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
     * MenuItem for navigating to the Table of this game.
     */
    @FXML
    MenuItem tableButton;

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
     * StackPane used as a container for the cards on the PlayArea
     */
    @FXML
    StackPane paneGrid;

    /**
     * ScrollPane that provides a scrollable view for the PlayArea
     */
    @FXML
    ScrollPane scrollPane;

    /**
     * The nickname of the player who owns the current PlayArea
     */
    String currentPlayer;

    /**
     * The nickname of the user
     */
    String myNickname;

    /**
     * The points of the player who owns the current PlayArea
     */
    int points;

    /**
     * A list containing all the players' nickname in the current game
     */
    List<String> allPlayers;

    /**
     * A boolean used to exit the Turn thread
     */
    boolean exit = false;

    /**
     * An ExecutorService with a fixed thread pool size of 5, used to handle tasks related to
     * the player's turn to place a card.
     */
    final ExecutorService yourTurnToPlaceThread = Executors.newFixedThreadPool(5);

    /**
     * A Future representing the result of an asynchronous computation for managing the turn task.
     * This is used to control the task lifecycle and check if the task is completed.
     */
    private Future<?> turnTaskFuture;

    /**
     * An array containing all the cards placed by the current player.
     */
    Card[] playerCardList;


    /**
     * This method is called when the user receives a message from another player.
     * Both menuCircle and chatCircle's visibility are set to true.
     */
    public void activateCircle(){
        menuCircle.setVisible(true);
        chatCircle.setVisible(true);
    }


    /**
     * Method that displays all the card contained in playerCardList on the StackPane,
     * the cards are placed in the coordinates specified in the card instance and in the right order
     */
    public void viewPlayerPlayArea(){
        for(Card card : playerCardList){
            if (card != null) {
                ImageView cardImage;
                if (card.isFace())
                    cardImage = new ImageView(new Image(card.getImageFront()));
                else
                    cardImage = new ImageView(new Image(card.getImageBack()));
                cardImage.setFitWidth(CARD_WIDTH);
                cardImage.setFitHeight(CARD_HEIGHT);
                cardImage.setTranslateX(card.getColumn());
                cardImage.setTranslateY(card.getRow());
                paneGrid.getChildren().add(cardImage);
            }
        }
    }


    /**
     * Method used to modify the text of the Label, indicating the current player's points
     */
    public void viewPlayerPoints(){
        if(points == 1)
            playerPoints.setText("The player " + currentPlayer + " has 1 point");
        else
            playerPoints.setText("The player " + currentPlayer + " has " + points + " points");
    }


    /**
     * Sets up the menu items for other players' PlayAreas.
     * These menu items are added to the FXML attribute menuButton.
     * When a menu item is selected it shows the PlayArea of the selected player
     */
    public void setOtherPlayersMenuItem(){
        modifyStatus(target -> {
            threads.execute(() -> {
                try {
                    allPlayers = new ArrayList<>(target.getPlayersNames());
                    myNickname = target.getNickname();
                    allPlayers.remove(myNickname);
                    allPlayers.remove(currentPlayer);
                    if(!allPlayers.isEmpty()){
                        for(String name : allPlayers) {
                            Platform.runLater(() -> {
                                MenuItem anotherPlayArea = new MenuItem(name);
                                menuButton.getItems().add(anotherPlayArea);
                                anotherPlayArea.setOnAction(e -> {
                                    Objects.requireNonNull(GuiApplication.getGui()).showAnotherPlayArea(name);
                                });
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
     * This method is used to set the name of the player whose PlayArea is going to be viewed
     * and the points of the selected player, at last it calls the method to display the PlayArea,
     * the method to update the label with player's points and the method to set up the menu items for other players' PlayAreas.
     * @param nickname the nickname of the selected player
     */
    public void setNickname(String nickname){
        currentPlayer = nickname;
        modifyStatus(target -> {
            threads.execute(() -> {
                try {
                    playerCardList = target.getOrderedCardsList(currentPlayer);
                    points = target.getPlayerPoints(currentPlayer);
                    Platform.runLater(() -> {
                        viewPlayerPlayArea();
                        viewPlayerPoints();
                        setOtherPlayersMenuItem();
                    });
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            });
        });
    }


    /**
     * Initializes the controller class. This method is automatically called when the FXML file is loaded for the first time.
     * It configures the ScrollPane, calls the superclass' setTarget method and starts the waitForYourTurn thread
     */
    @FXML
    public void initialize(){
        scrollPane.layout();
        scrollPane.setHvalue(scrollPane.getHmax() / 2);
        scrollPane.setVvalue(scrollPane.getVmax() / 2);
        super.setTarget();
        startThread();
    }


    /**
     * Disable the visibility of the Circle on the menuButton.
     * This FXML method is triggered when the menuButton is Clicked
     */
    @FXML
    private void disableCircle(){
        menuCircle.setVisible(false);
    }


    /**
     * This FXML method is triggered when the player clicks on the Chat MenuItem.
     * It disables the visibility of the Circle on the ChatButton and then calls the Gui method to open the chat.
     */
    @FXML
    private void openChat(){
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
     * This FXML method is triggered when the player clicks on the Table MenuItem.
     * It calls the Gui method to view the current game's Table
     */
    @FXML
    private void tableView() {
        stopThread();
        Platform.runLater(() -> Objects.requireNonNull(GuiApplication.getGui()).showTable("draw"));
    }


    /**
     * Method that sets the exit attribute to false and start the thread responsible for managing the turn cycle
     */
    public void startThread(){
        exit = false;
        turnTaskFuture = yourTurnToPlaceThread.submit(this::waitForYourTurn);
    }


    /**
     * Method that sets the exit attribute to true and stop the thread responsible for managing the turn cycle
     */
    public void stopThread() {
        exit = true;
        if (turnTaskFuture != null) {
            turnTaskFuture.cancel(true);
        }
    }


    /**
     * This method is used to manage the turn cycle.
     * This method continuously check if it's the user's turn and updates the status label accordingly.
     * The boolean variable turn is true if it's user's turn, false otherwise.
     * The boolean variable placingPhase is true if the current game is in the PlacingPhase, false if it's in the DrawPhase.
     */
    public void waitForYourTurn(){
        modifyStatus(target -> {
            try {
                boolean turn;
                boolean placingPhase;
                while (true) {
                    if(exit){
                        return;
                    }else{
                        turn = target.checkTurn();
                        //Checks if it is the player's turn and if this is not the draw phase
                        placingPhase = turn && target.getPhaseGui().equals("PlacingPhase");
                        if (turn && placingPhase) {
                            Platform.runLater(() -> labelTurn.setText("It's your turn, place a card!"));
                        } else if (turn) {
                            Platform.runLater(() -> labelTurn.setText("It's your turn, draw a card!"));
                        } else {
                            Platform.runLater(() -> labelTurn.setText("Wait for your turn"));
                        }
                        Thread.sleep(1500);
                    }
                }
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }
}
