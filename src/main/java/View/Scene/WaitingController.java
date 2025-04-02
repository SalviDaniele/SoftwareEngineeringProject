package View.Scene;

import View.Gui.GuiApplication;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.rmi.RemoteException;
import java.util.Objects;

/**
 * Controller for the FXML file WaitingScene.fxml
 */
public class WaitingController extends SceneController {

    /**
     * The stage that contains this scene
     */
    Stage thisStage;

    /**
     * Label that displays why the player has to wait.
     */
    @FXML
    Label text;

    /**
     * The root layout pane of the scene.
     */
    @FXML
    AnchorPane root;


    /**
     * Initializes the controller class. This method is automatically called when the FXML file is loaded for the first time.
     * It calls the superclass' setTarget method.
     */
    @FXML
    public void initialize() {
        super.setTarget();
    }


    /**
     * This method starts a new thread to wait for all the player to join.
     */
    public void waitForPlayerThread() {
        threads.execute(this::waitForPlayer);
    }


    /**
     * Starts a new thread to wait for the player's pawn selection.
     * Sets the current stage.
     *
     * @param stage the stage for this scene
     */
    public void waitForPawnThread(Stage stage) {
        threads.execute(this::waitForPawn);
        this.thisStage = stage;
    }


    /**
     * Starts a new thread to wait for the player's starter card selection.
     * Sets the current stage.
     *
     * @param stage the stage for this scene
     */
    public void waitForStarterCardThread(Stage stage){
        threads.execute(this::waitForStarterCard);
        this.thisStage = stage;
    }


    /**
     * Starts a new thread to wait for the player's objective card choice.
     * Sets the current stage.
     *
     * @param stage the stage for this scene
     */
    public void waitForObjectiveChoiceThread(Stage stage){
        threads.execute(this::waitForObjectiveChoice);
        this.thisStage = stage;
    }


    /**
     * This method starts a new thread to wait for the endgame condition.
     */
    public void waitForEndgameThread(){
        threads.execute(this::waitForEndgame);
    }


    /**
     * Method that initialize the scene that asks the user to wait for the other players to join.
     * When all the players joined the match the scene changes.
     */
    public void waitForPlayer() {
        text.setText("Wait for all other clients to connect");
        modifyStatus(target -> {
                boolean connected;
                do {
                    try {
                        connected = target.allConnected(target.getIdMatch());
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                } while (!connected);
                try {
                    if (target.isFirst())
                        target.getServer().shuffleDecksAndGiveStarterCards(target.getIdMatch());
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                Platform.runLater(() -> Objects.requireNonNull(GuiApplication.getGui()).showTable("starting"));
        });
    }


    /**
     * Method that initialize the scene that asks the user to wait for the other players to choose for their pawn.
     * When all the players have chosen the pawn the scene changes.
     */
    public void waitForPawn() {
        Platform.runLater(() -> text.setText("Wait for other Clients to choose their color"));
        modifyStatus(target -> {
                boolean stop;
                try {
                    do {
                        stop = target.canIChooseTheColor(target.getIdMatch(), target.getNickname());
                    } while (!stop);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                Platform.runLater(() -> {
                    GuiApplication.choosePawnColor();
                    thisStage.close();
                });
        });
    }


    /**
     * Method that initialize the scene that asks the user to wait for the other players to choose the starter card face.
     * When all the players have chosen the face of the starter card the scene changes.
     */
    public void waitForStarterCard() {
        text.setText("Wait for all other clients to place their starter card");
        modifyStatus(target -> {
                boolean stop;
                try {
                    do {
                        stop = target.allStarterCardsPlaced(target.getIdMatch());
                    } while (!stop);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                Platform.runLater(() -> {
                    Objects.requireNonNull(GuiApplication.getGui()).showPlayArea("starting");
                    thisStage.close();
                });
        });
    }


    /**
     * Method that initialize the scene that asks the user to wait for the other players to choose their objective card.
     * When all the players have chosen the objective card the scene changes.
     */
    public void waitForObjectiveChoice() {
        text.setText("Wait for all other clients to choose their objective card");
        modifyStatus(target -> {
                boolean stop;
                try {
                    do {
                        stop = target.allObjectiveCardsChosen(target.getIdMatch());
                    } while (!stop);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                Platform.runLater(() -> {
                    Objects.requireNonNull(GuiApplication.getGui()).showPlayArea("firstTurn");
                    thisStage.close();
                });
        });
    }


    /**
     * Method that initialize the scene that asks the user to wait until all the players have finished their turn.
     * When the final score is calculated the last scene of the game is set.
     */
    public void waitForEndgame() {
        MenuButton menuButton = new MenuButton("Menu");
        MenuItem chatButton = new MenuItem("Chat");
        menuButton.getItems().add(chatButton);
        chatButton.setOnAction(e -> Platform.runLater(() -> Objects.requireNonNull(GuiApplication.getGui()).showChatStage()));
        Platform.runLater(() -> {
            text.setText("This was your last turn, wait for the end of the match, now you can only use the chat");
            root.getChildren().add(menuButton);
        });
        modifyStatus(target -> {
                boolean end;
                try {
                    do {
                        end = target.getNumOfPlayersThatHaveFinishedToPlay(target.getIdMatch()) == target.getNumOfPlayers(target.getIdMatch());
                    } while (!end);
                    target.getServer().calculateObjectives(target.getIdMatch(), target.getNickname());
                    target.getServer().increaseNumOfObjectiveCardCalculated(target.getIdMatch());
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                Platform.runLater(() -> text.setText("Wait for other players to finish their last turn"));
        });

        modifyStatus(target -> {
                boolean calculated;
                try {
                    do {
                        calculated = target.allObjectiveCardsCalculated(target.getIdMatch());
                    } while (!calculated);
                    if(target.isFirst())
                        target.getServer().calculateWinner(target.getIdMatch());
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                Objects.requireNonNull(GuiApplication.getGui()).showWinnerOrLoser();
        });
    }
}
