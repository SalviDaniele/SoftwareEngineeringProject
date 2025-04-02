package View.Scene;

import View.Gui.GuiApplication;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.rmi.RemoteException;
import java.util.Objects;

/**
 * Controller for the FXML file ChooseBetweenTwoCardsScene.fxml
 */
public class ChooseBetweenTwoCardsController extends SceneController{

    /**
     * Is the Stage that contains this scene
     */
    Stage container;

    /**
     * Label that asks a player to choose between two different cards
     */
    @FXML
    Label text;

    /**
     * AnchorPane that contains the image of the first card
     */
    @FXML
    AnchorPane paneCard1;

    /**
     * AnchorPane that contains the image of the second card
     */
    @FXML
    AnchorPane paneCard2;

    /**
     * Button to choose the first card
     */
    @FXML
    Button button1;

    /**
     * Button to choose the second card
     */
    @FXML
    Button button2;

    /**
     * The URL of the image for the first card
     */
    String obChoice1;

    /**
     * The URL of the image for the second card
     */
    String obChoice2;


    /**
     * Initializes the controller class. This method is automatically called when the FXML file is loaded for the first time.
     * It calls the superclass' setTarget method.
     */
    @FXML
    public void initialize(){
        super.setTarget();
    }


    /**
     * This method is called when the user has to choose between two objective cards and the button1 is pressed.
     * The user has chosen the first card, the pick method is called and the Stage is closed.
     */
    public void choose1() {
        pick(1);
        container.close();
    }


    /**
     * This method is called when the user has to choose between two objective cards and the button2 is pressed.
     * The user has chosen the second card, the pick method is called and the Stage is closed.
     */
    public void choose2() {
        pick(2);
        container.close();
    }


    /**
     * The method communicates the user's choice to the client. If all the players in the current game have selected their
     * objective cards the game officially begins. Otherwise, the user must wait for all the other players to choose their
     * secret objective cards.
     *
     * @param obSelected is the objective card selected by the user
     */
    public void pick(int obSelected){
        modifyStatus(target -> {
            threads.execute(() -> {
                try {
                    target.getServer().putChoiceObjectives(target.getIdMatch(), target.getNickname(), obSelected);
                    target.getServer().increaseNumOfObjectiveCardChosen(target.getIdMatch());
                    if (!target.allObjectiveCardsChosen(target.getIdMatch())) {
                        Platform.runLater(() -> GuiApplication.setNewWaitingSceneOnStage("objective", new Stage()));
                    } else {
                        Platform.runLater(() -> Objects.requireNonNull(GuiApplication.getGui()).showPlayArea("firstTurn"));
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            });
        });
    }


    /**
     * This method is called when the user has to choose between front or back of the starter card and the button1 is pressed.
     * The user has chosen the front, the place method is called and the Stage is closed.
     */
    public void chooseFront() {
        place("front");
        container.close();
    }


    /**
     * This method is called when the user has to choose between front or back of the starter card and the button2 is pressed.
     * The user has chosen the back, the place method is called and the Stage is closed.
     */
    public void chooseBack() {
        place("back");
        container.close();
    }


    /**
     * The method communicates the user's choice to the client. If all the players in the current game have selected their
     * starter cards the initialization of the game continues. Otherwise, the user must wait for all the other players to choose their
     * starter cards.
     *
     * @param choice can assume two values, 'front' or 'back', depending on the user's choice
     */
    public void place(String choice){
        modifyStatus(target -> {
            threads.execute(() -> {
                try {
                    target.getServer().place(target.getIdMatch(), target.getNickname(), 1, choice, 2, 2);
                    target.getServer().increaseNumOfStarterCardsPlaced(target.getIdMatch());
                    if(!target.allStarterCardsPlaced(target.getIdMatch())) {
                        Platform.runLater(() -> GuiApplication.setNewWaitingSceneOnStage("starter", new Stage()));
                    }
                    else{
                        Platform.runLater(() -> Objects.requireNonNull(GuiApplication.getGui()).showPlayArea("starting"));
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            });
        });
    }


    /**
     * This method initialise the stage that asks the user to choose between two objective cards.
     *
     * @param root is the AnchorPane loaded by the FXML file
     * @param type is the type of choice the user is going to make
     * @param title the title of the stage
     * @param choiceCard1 is the URL of the image for the first choice card
     * @param choiceCard2 is the URL of the image for the second choice card
     */
    public void chooseBetweenTwoCards(AnchorPane root, String type, String title, String choiceCard1, String choiceCard2){
        if(type.equals("starter")) {
            text.setText("Choose the face you prefer");
            button1.setOnAction(e -> chooseFront());
            button2.setOnAction(e -> chooseBack());
            button1.setText("Front");
            button2.setText("Back");
        }
        else if (type.equals("objective")) {
            text.setText("Choose the objective card that you prefer:");
            button1.setOnAction(e -> choose1());
            button2.setOnAction(e -> choose2());
            button1.setText("Card 1");
            button2.setText("Card 2");
        }
        obChoice1 = choiceCard1;
        obChoice2 = choiceCard2;
        container = new Stage();
        container.initModality(Modality.APPLICATION_MODAL);
        container.setOnCloseRequest(Event::consume);
        ImageView imageCard1 = new ImageView(new Image(obChoice1));
        ImageView imageCard2 = new ImageView(new Image(obChoice2));
        imageCard1.setFitWidth(201);
        imageCard1.setFitHeight(135);
        imageCard2.setFitWidth(201);
        imageCard2.setFitHeight(135);
        paneCard1.getChildren().add(imageCard1);
        paneCard2.getChildren().add(imageCard2);
        Scene scene = new Scene(root);
        container.setScene(scene);
        container.setMinHeight(600);
        container.setMinWidth(900);
        container.setMaxHeight(600);
        container.setMaxWidth(900);
        container.setTitle(title);
        container.show();
    }
}
