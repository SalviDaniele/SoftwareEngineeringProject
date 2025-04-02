package View.Scene;

import Model.Cards.Card;
import Model.Cards.Colors;
import Model.PlayArea;
import Model.Player;
import View.Gui.GuiApplication;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Controller for the FXML file PlayAreaScene.fxml
 */
public class PlayAreaSceneController extends SceneController{

    /**
     * The width of the card ImageView in the Gui.
     */
    final static int CARD_WIDTH = 201;

    /**
     * The height of the card ImageView in the Gui.
     */
    final static int CARD_HEIGHT = 135;

    /**
     * Two constants to correctly overlay cards in the Gui.
     */
    final static double translateX = -50, translateY = -55;

    /**
     * Enumeration representing the different phases of the game.
     */
    enum Phase{
        STARTING,
        FIRSTTURN,
        PLACING
    }

    /**
     * The current phase of the game.
     */
    Phase phase;

    /**
     * The root layout pane of the scene.
     */
    @FXML
    BorderPane borderPane;

    /**
     * ScrollPane that provides a scrollable view for the PlayArea
     */
    @FXML
    ScrollPane scrollPane;

    /**
     * StackPane containing the cards placed in the PlayArea .
     */
    @FXML
    StackPane paneGrid;

    /**
     * Label that displays the player's points.
     */
    @FXML
    Label numPoints;

    /**
     * The number of points the player has.
     */
    int points;

    /**
     * ImageView containing the image of the player's pawn.
     */
    @FXML
    ImageView pawn;

    /**
     * The color of the player's pawn.
     */
    Colors color;

    /**
     * Boolean indicating if the pawn is black.
     */
    boolean blackPawn;

    /**
     * ImageView containing the image of the player's secret objective card.
     */
    @FXML
    ImageView secretObCard;

    /**
     * The image path of the player's secret objective card.
     */
    String secretObCardImagePath;


    /**
     * ImageView containing the image of the first card in the player's hand.
     */
    @FXML
    ImageView handCard1Image;

    /**
     * The front image path of the first card in the player's hand.
     */
    String handCard1Front;

    /**
     * The back image path of the first card in the player's hand.
     */
    String handCard1Back;

    /**
     * Boolean indicating if the first card in the player's hand is face up.
     */
    boolean handCard1Face;


    /**
     * ImageView containing the image of the second card in the player's hand.
     */
    @FXML
    ImageView handCard2Image;

    /**
     * The front image path of the second card in the player's hand.
     */
    String handCard2Front;

    /**
     * The back image path of the second card in the player's hand.
     */
    String handCard2Back;

    /**
     * Boolean indicating if the second card in the player's hand is face up.
     */
    boolean handCard2Face;

    /**
     * ImageView containing the image of the third card in the player's hand.
     */
    @FXML
    ImageView handCard3Image;

    /**
     * The front image path of the third card in the player's hand.
     */
    String handCard3Front;

    /**
     * The back image path of the third card in the player's hand.
     */
    String handCard3Back;

    /**
     * Boolean indicating if the third card in the player's hand is face up.
     */
    boolean handCard3Face;

    /**
     * ImageView containing the image of the cart to be placed.
     */
    ImageView cardToPlace;

    /**
     * Label that displays the current turn information.
     */
    @FXML
    Label labelTurn;

    /**
     * MenuButton for accessing different sections of the application.
     */
    @FXML
    MenuButton menuButton;

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
     * The length of the PlayArea grid.
     */
    int playAreaMatrixLength;

    /**
     * Boolean indicating if it's user's turn to place the card.
     */
    public boolean yourTurnToPlace = false;

    /**
     * Boolean condition to stop the turn thread.
     */
    boolean exit = false;

    /**
     * Boolean indicating if the chosen coordinates are valid.
     */
    public boolean placeValidCoordinates;

    /**
     * Boolean indicating if the user has enough resources to place a gold card.
     */
    public boolean requirementsRespected;

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
     * Initializes the controller class. This method is automatically called when the FXML file is loaded for the first time.
     * It calls the superclass' setTarget method and sets up the menu items for other players' PlayAreas.
     */
    @FXML
    private void initialize(){
        scrollPane.layout();
        scrollPane.setHvalue(scrollPane.getHmax() / 2);
        scrollPane.setVvalue(scrollPane.getVmax() / 2);
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
        turnTaskFuture = yourTurnToPlaceThread.submit(this::waitForYourTurn);
    }


    /**
     * Method that sets the exit attribute to true and stop the thread responsible for managing the turn cycle.
     */
    public void stopThread() {
        exit = true;
        if (turnTaskFuture != null)
            turnTaskFuture.cancel(true);
    }


    /**
     * Method called to initialize the PlayArea based on the current phase.
     * If the current Phase is equal to STARTING the user has to place the StarterCard.
     * If the current Phase is equal to FIRSTTURN the black pawn is assigned by the client and
     * the first turn starts.
     * If the current Phase is equal to PLACING a normal turn starts.
     *
     * @param phaseValue a String representing the current Phase value
     */
    public void playAreaPhases(String phaseValue){
        switch (phaseValue){
            case "starting":
                phase = Phase.STARTING;
                break;
            case "firstTurn":
                phase = Phase.FIRSTTURN;
                break;
            case "placing":
                phase = Phase.PLACING;
                break;
        }
        switch(phase){
            case STARTING:
                placeStarterCard();
                break;
            case FIRSTTURN:
                assignBlackPawn();
                modifyStatus(target -> {
                    threads.execute(() -> {
                        String nickname;
                        Player player = null;
                        try {
                            nickname = target.getNickname();
                            if(nickname.equals(target.getNameOfThePlayerWithTheBlackPawn())){
                                target.setBlackPawn(true);
                                blackPawn = true;
                                color = target.getColor();
                                target.itIsMyTurn();
                                yourTurnToPlace = true;
                                startThread();
                            }
                            else if(!target.isBlackPawn()) {
                                blackPawn = false;
                                color = target.getColor();
                                target.setYourTurn(false);
                                yourTurnToPlace = false;
                                startThread();
                            }

                            for (Player p : target.getTable(target.getIdMatch()).getPlayers()) {
                                if (p.getNickname().equals(target.getNickname()))
                                    player = p;
                            }
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                        assert player != null;
                        secretObCardImagePath = player.getObjective().getImageFront();
                        initializeHand(player.getHand());
                        switch (color) {
                            case RED:
                                pawn.setImage(new Image("pedine/redPawn.png"));
                                break;
                            case BLUE:
                                pawn.setImage(new Image("pedine/bluePawn.png"));
                                break;
                            case GREEN:
                                pawn.setImage(new Image("pedine/greenPawn.png"));
                                break;
                            case YELLOW:
                                pawn.setImage(new Image("pedine/yellowPawn.png"));
                                break;
                            }
                        Platform.runLater(() -> {
                            pawn.setFitWidth(50);
                            pawn.setFitHeight(50);
                            secretObCard.setImage(new Image(secretObCardImagePath));
                        });
                    });
                });
                break;
            case PLACING:
                scrollPane.layout();
                scrollPane.setHvalue(scrollPane.getHmax() / 2);
                scrollPane.setVvalue(scrollPane.getVmax() / 2);
                startThread();
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
                        points = player.getPoints();
                        initializeHand(player.getHand());
                    });
                });
                break;
        }

    }


    /**
     * Method that assigns the black pawn of the current game.
     */
    public void assignBlackPawn(){
        modifyStatus(target -> {
            threads.execute(() -> {
                try {
                    if(target.isFirst()){
                        target.chooseTheBlackPawn();
                        target.setBlackPawnChosen(true);
                    }
                    while(!target.isBlackPawnChosen());
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            });
        });
    }


    /**
     * Method that initializes the card images on the player's hand. It also sets the text for the Label
     * that displays the player's points.
     *
     * @param hand a list containing all the cards on player's hand.
     */
    public void initializeHand(List<Card> hand){
        if(!hand.isEmpty()){
            if (hand.getFirst() != null) {
                handCard1Front = hand.getFirst().getImageFront();
                handCard1Back = hand.getFirst().getImageBack();
                handCard1Image.setImage(new Image(handCard1Front));
                handCard1Face = true;
            }
        }
        if(hand.size() > 1){
            if (hand.get(1) != null) {
                handCard2Front = hand.get(1).getImageFront();
                handCard2Back = hand.get(1).getImageBack();
                handCard2Image.setImage(new Image(handCard2Front));
                handCard2Face = true;
            }
        }
        else{
            handCard2Image.setImage(null);
        }
        if(hand.size() > 2){
            if (hand.get(2) != null) {
                handCard3Front = hand.get(2).getImageFront();
                handCard3Back = hand.get(2).getImageBack();
                handCard3Image.setImage(new Image(handCard3Front));
                handCard3Face = true;
            }
        }else{
            handCard3Image.setImage(null);
        }
        if(points != 1)
            Platform.runLater(() -> numPoints.setText("You have " + points + " points!"));
        else
            Platform.runLater(() -> numPoints.setText("You have " + points + " point!"));
    }


    /**
     * Method to place the starter card on the PlayArea. At last, if the player can choose the color of his pawn
     * the relevant scene is shown, otherwise a scene that asks the user to wait is displayed.
     */
    public void placeStarterCard(){
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
                PlayArea playArea = player.getPlayArea();
                if (player.getHand().isEmpty()) {
                    Card starterCard = playArea.getGrid()[2][2];
                    playAreaMatrixLength = playArea.getGrid().length;
                    String imageCard;
                    if (starterCard.isFace()) {
                        imageCard = starterCard.getImageFront();
                    } else {
                        imageCard = starterCard.getImageBack();
                    }
                    Platform.runLater(() -> {
                        AnchorPane pane = new AnchorPane();
                        ImageView image = new ImageView(new Image(imageCard));
                        image.setFitWidth(CARD_WIDTH);
                        image.setFitHeight(CARD_HEIGHT);
                        pane.setMaxWidth(CARD_WIDTH);
                        pane.setMaxHeight(CARD_HEIGHT);
                        pane.getChildren().add(image);
                        paneGrid.getChildren().add(pane);
                        image.setTranslateX(((2 - (double) playAreaMatrixLength / 2) + 0.5) * (CARD_WIDTH + translateX));
                        image.setTranslateY(((2 - (double) playAreaMatrixLength / 2) + 0.5) * (CARD_HEIGHT + translateY));
                    });
                    try {
                        if(target.isFirst())
                            target.itIsMyTurn();
                        if (!target.canIChooseTheColor(target.getIdMatch(), target.getNickname())) {
                            Platform.runLater(() -> GuiApplication.setNewWaitingSceneOnStage("pawn", new Stage()));
                        } else
                            Platform.runLater(GuiApplication::choosePawnColor);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        });
    }


    /**
     * Method to place a generic card in the PlayArea. When the card is placed the relevant ImageView
     * is removed by the player's hand, boolean indicating if it's user's turn is set to false and the
     * text for the Label that displays the player's points is updated.
     *
     * @param cardSelected is the card selected from the hand of the player.
     * @param face true if the card is placed in front, false otherwise.
     * @param row the row of the grid in which the card is placed.
     * @param col the column of the grid in which the card is placed.
     */
    public synchronized void placeCard (int cardSelected, boolean face, int row, int col){
        modifyStatus(target -> {
            try {
                String faceSelected;
                if(face)
                    faceSelected = "front";
                else
                    faceSelected = "back";
                target.placeGui(cardSelected, faceSelected, row, col);
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
                playAreaMatrixLength = player.getPlayArea().getGrid().length;
                points = target.getPlayerPoints(target.getNickname());
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
        switch(cardSelected){
            case 1:
                handCard1Front = null;
                handCard1Back = null;
                handCard1Image.setImage(null);
                break;
            case 2:
                handCard2Front = null;
                handCard2Back = null;
                handCard2Image.setImage(null);
                break;
            case 3:
                handCard3Front = null;
                handCard3Back = null;
                handCard3Image.setImage(null);
                break;
        }
        yourTurnToPlace = false;
        if(points != 1)
            Platform.runLater(() -> numPoints.setText("You have " + points + " points!"));
        else
            Platform.runLater(() -> numPoints.setText("You have " + points + " point!"));
        this.notifyAll();
    }


    /**
     * This method is called when the user receives a message from another player.
     * Both menuCircle and chatCircle's visibility are set to true.
     */
    public void activateCircle(){
        menuCircle.setVisible(true);
        chatCircle.setVisible(true);
    }


    /**
     * This method is called when the chat window has been closed. It sets to false the visibility of the chatCircle
     */
    public void disableCircleChat(){
        chatCircle.setVisible(false);
    }


    /**
     * Disable the visibility of the Circle on the menuButton.
     * This FXML method is triggered when the menuButton is Clicked.
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
     * This FXML method is triggered when the player clicks on the Table MenuItem.
     * It calls the Gui method to view the current game's Table
     */
    @FXML
    private void tableView() {
        stopThread();
        Platform.runLater(() -> Objects.requireNonNull(GuiApplication.getGui()).showTable("draw"));
    }


    /**
     * This FXML method is triggered when the player clicks on the first card in his hand.
     * The card is turned over and the boolean indicating if the card is face up is updated.
     */
    @FXML
    private void clickOnHandCard1(){
        if(handCard1Image.getImage() != null){
            if (handCard1Face) {
                handCard1Image.setImage(new Image(handCard1Back));
                handCard1Face = false;
            } else {
                handCard1Image.setImage(new Image(handCard1Front));
                handCard1Face = true;
            }
        }
    }


    /**
     * This FXML method is triggered when the player clicks on the second card in his hand.
     * The card is turned over and the boolean indicating if the card is face up is updated.
     */
    @FXML
    private void clickOnHandCard2(){
        if(handCard2Image.getImage() != null){
            if (handCard2Face) {
                handCard2Image.setImage(new Image(handCard2Back));
                handCard2Face = false;
            } else {
                handCard2Image.setImage(new Image(handCard2Front));
                handCard2Face = true;
            }
        }
    }


    /**
     * This FXML method is triggered when the player clicks on the third card in his hand.
     * The card is turned over and the boolean indicating if the card is face up is updated.
     */
    @FXML
    private void clickOnHandCard3(){
        if(handCard3Image.getImage() != null){
            if (handCard3Face) {
                handCard3Image.setImage(new Image(handCard3Back));
                handCard3Face = false;
            } else {
                handCard3Image.setImage(new Image(handCard3Front));
                handCard3Face = true;
            }
        }
    }


    /**
     * Handles the event when a dragged item is dropped.
     * Validates the drop location and checks if the card can be placed.
     * If valid, it places the card on the grid and notifies the Client.
     *
     * @param event the drag event.
     */
    @FXML
    public void dragDropped(DragEvent event){
        Dragboard db = event.getDragboard();
        boolean success = false;

        Node node = event.getPickResult().getIntersectedNode();
        if(node == paneGrid && db.hasImage() ){
            ImageView image = new ImageView(db.getImage());
            AnchorPane pane = new AnchorPane();
            image.setFitHeight(CARD_HEIGHT);
            image.setFitWidth(CARD_WIDTH);
            pane.setMaxWidth(CARD_WIDTH);
            pane.setMaxHeight(CARD_HEIGHT);
            double x = (((event.getX() - (paneGrid.getWidth() / 2)) + 50) / (CARD_WIDTH + translateX));
            double y = (((event.getY() - (paneGrid.getHeight() / 2)) + 12) / (CARD_HEIGHT + translateY));
            int row = (int) ((y + ((double) playAreaMatrixLength / 2)) - 0.5);
            int col = (int) ((x + ((double) playAreaMatrixLength / 2)) - 0.5);
            double new_X = ((col - ((double) playAreaMatrixLength / 2)) + 0.5) * (CARD_WIDTH + translateX);
            double new_Y = ((row - ((double) playAreaMatrixLength / 2)) + 0.5) * (CARD_HEIGHT + translateY);
            boolean face;
            int cardSelected;
            if(cardToPlace.equals(handCard1Image)) {
                cardSelected = 1;
                face = handCard1Face;
            } else if (cardToPlace.equals(handCard2Image)) {
                cardSelected = 2;
                face = handCard2Face;
            }
            else{
                cardSelected = 3;
                face = handCard3Face;
            }
            modifyStatus(target -> {
                try {
                    placeValidCoordinates = target.areCoordinatesValid(target.getIdMatch(), target.getNickname(), row, col);
                    requirementsRespected = target.canIPlaceTheGoldCard(target.getIdMatch(), target.getNickname(), cardSelected);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            });
            if(placeValidCoordinates && (requirementsRespected || !face)){
                pane.getChildren().add(image);
                paneGrid.getChildren().add(pane);
                pane.setTranslateX(new_X);
                pane.setTranslateY(new_Y);
                placeCard(cardSelected, face, row, col);
                success = true;
            }
        }
        event.setDropCompleted(success);
        event.consume();
    }


    /**
     * Handles the drag over event to accept the drag if it contains an image.
     *
     * @param event the drag event.
     */
    @FXML
    public void dragOver(DragEvent event){
        if(event.getGestureSource() != paneGrid && event.getDragboard().hasImage()){
            event.acceptTransferModes(TransferMode.MOVE);
        }
        event.consume();
    }


    /**
     * Handles the drag done event.
     *
     * @param event the drag event.
     */
    @FXML
    public void dragDone(DragEvent event){
        event.consume();
    }


    /**
     * Handles the drag detected event to initiate a drag and drop gesture.
     * It puts the card selected into a ClipBoard content.
     *
     * @param event the mouse event.
     */
    @FXML
    public void dragDetected(MouseEvent event){
        if(yourTurnToPlace){
            AnchorPane pane = (AnchorPane) event.getSource();
            cardToPlace = (ImageView) pane.getChildren().getFirst();
            Dragboard db = cardToPlace.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent cbContent = new ClipboardContent();
            cbContent.putImage(cardToPlace.getImage());
            db.setContent(cbContent);
            event.consume();
        }
    }


    /**
     * This FXML method is triggered when the user clicks on the image of his pawn.
     * Calls the respective method present in VirtualServer.
     * This cheat provides the specified player with 20 points.
     */
    @FXML
    public void cheat2() {
        modifyStatus(target -> {
            threads.execute(() -> {
                try {
                    target.getServer().cheat2(target.getIdMatch(), target.getNickname());
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            });
        });
    }


    /**
     * This method is used to manage the turn cycle.
     * This method continuously check if it's the user's turn and updates the status label accordingly.
     * The boolean variable turn is true if it's user's turn, false otherwise.
     * If turn is true and the game is in the PlacingPhase the threads wait for the player to place his card.
     */
    public synchronized void waitForYourTurn(){
        modifyStatus(target ->{
            try {
                while (true) {
                    boolean turn;
                    do {
                        if(exit){
                            return;
                        }else{
                            turn = target.checkTurn();
                            //Checks if it is the player's turn and if this is not the draw phase
                            yourTurnToPlace = turn && target.getPhaseGui().equals("PlacingPhase");
                            if (turn && yourTurnToPlace) {
                                Platform.runLater(() -> labelTurn.setText("It's your turn, place a card!"));
                            } else if (turn) {
                                Platform.runLater(() -> labelTurn.setText("It's your turn, draw a card!"));
                                Thread.sleep(1000);
                            } else {
                                Platform.runLater(() -> labelTurn.setText("Wait for your turn"));
                                Thread.sleep(1000);
                            }
                        }
                    } while (!yourTurnToPlace && !turn);
                    if(exit){
                        return;
                    }
                    this.wait();
                }
            } catch (InterruptedException | RemoteException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        });
    }
}