package View.Scene;

import Model.Cards.Colors;
import View.Gui.GuiApplication;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Objects;

/**
 * Controller for the FXML file ChoosePawnScene.fxml
 */
public class ChoosePawnController extends SceneController {

    /**
     * Label that asks the player to choose the pawn
     */
    @FXML
    Label text;

    /**
     * ImageView for the red pawn
     */
    @FXML
    ImageView redPawn;

    /**
     * ImageView for the blue pawn
     */
    @FXML
    ImageView bluePawn;

    /**
     * ImageView for the green pawn
     */
    @FXML
    ImageView greenPawn;

    /**
     * ImageView for the yellow pawn
     */
    @FXML
    ImageView yellowPawn;

    /**
     * The stage that contains this scene
     */
    Stage pawnStage;

    /**
     * Initializes the controller class. This method is automatically called when the FXML file is loaded for the first time.
     * It calls the superclass' setTarget method.
     */
    @FXML
    public void initialize(){
        super.setTarget();
    }

    /**
     * This method initializes the stage that asks the user to choose a pawn.
     * It sets the text of the Label and the ImageViews of the available pawns.
     *
     * @param stage the stage that contains this scene
     */
    public void pawnSceneInitialization(Stage stage){
        pawnStage = stage;
        pawnStage.initModality(Modality.APPLICATION_MODAL);
        pawnStage.setOnCloseRequest(Event::consume);
        pawnStage.setMinHeight(600);
        pawnStage.setMinWidth(900);
        pawnStage.setMaxHeight(600);
        pawnStage.setMaxWidth(900);
        pawnStage.setTitle("Pawn choice");
        pawnStage.show();
        text.setText("These are the available colors:");
        HBox box = new HBox();
        box.setSpacing(50);
        box.setAlignment(Pos.CENTER);
        modifyStatus(target -> {
            threads.execute(() -> {
                try {
                    for(int i = 0; i < target.showAvailableColors(target.getIdMatch()).length; i++){
                        if(target.showAvailableColors(target.getIdMatch())[i] != null){
                            Colors color = target.showAvailableColors(target.getIdMatch())[i];
                            Platform.runLater(() -> {
                                switch (color) {
                                    case RED:
                                        redPawn.setImage(new Image("pedine/redPawn.png"));
                                        redPawn.setFitWidth(50);
                                        redPawn.setFitHeight(50);
                                        break;
                                    case BLUE:
                                        bluePawn.setImage(new Image("pedine/bluePawn.png"));
                                        bluePawn.setFitWidth(50);
                                        bluePawn.setFitHeight(50);
                                        break;
                                    case GREEN:
                                        greenPawn.setImage(new Image("pedine/greenPawn.png"));
                                        greenPawn.setFitWidth(50);
                                        greenPawn.setFitHeight(50);
                                        break;
                                    case YELLOW:
                                        yellowPawn.setImage(new Image("pedine/yellowPawn.png"));
                                        yellowPawn.setFitWidth(50);
                                        yellowPawn.setFitHeight(50);
                                        break;
                                }
                            });
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        });
    }


    /**
     * This method is called when the ImageView containing the red pawn image is clicked.
     * Notifies the client of the user's choice.
     */
    @FXML
    public void chosenRedPawn(){
        modifyStatus(target -> {
            threads.execute(() -> {
                try {
                    target.setColor(Colors.RED);
                    target.getServer().removeColorAndPassTurn(target.getIdMatch(), target.getNickname(), Colors.RED);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            });
        });
        System.out.println("Hai scelto il colore: " + Colors.RED.getValue());
        pawnStage.close();
        Platform.runLater(()-> Objects.requireNonNull(GuiApplication.getGui()).showTable("objective"));
    }


    /**
     * This method is called when the ImageView containing the blue pawn image is clicked.
     * Notifies the client of the user's choice.
     */
    @FXML
    public void chosenBluePawn(){
        modifyStatus(target -> {
            threads.execute(() -> {
                try {
                    target.setColor(Colors.BLUE);
                    target.getServer().removeColorAndPassTurn(target.getIdMatch(), target.getNickname(), Colors.BLUE);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            });
        });
        System.out.println("Hai scelto il colore: " + Colors.BLUE.getValue());
        pawnStage.close();
        Platform.runLater(() -> Objects.requireNonNull(GuiApplication.getGui()).showTable("objective"));
    }


    /**
     * This method is called when the ImageView containing the green pawn image is clicked.
     * Notifies the client of the user's choice.
     */
    @FXML
    public void chosenGreenPawn(){
        modifyStatus(target -> {
            threads.execute(() -> {
                try {
                    target.setColor(Colors.GREEN);
                    target.getServer().removeColorAndPassTurn(target.getIdMatch(), target.getNickname(), Colors.GREEN);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            });
        });
        System.out.println("Hai scelto il colore: " + Colors.GREEN.getValue());
        pawnStage.close();
        Platform.runLater(() -> Objects.requireNonNull(GuiApplication.getGui()).showTable("objective"));
    }


    /**
     * This method is called when the ImageView containing the yellow pawn image is clicked.
     * Notifies the client of the user's choice.
     */
    @FXML
    public void chosenYellowPawn(){
        modifyStatus(target -> {
            threads.execute(() -> {
                try {
                    target.setColor(Colors.YELLOW);
                    target.getServer().removeColorAndPassTurn(target.getIdMatch(), target.getNickname(), Colors.YELLOW);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            });
        });
        System.out.println("Hai scelto il colore: " + Colors.YELLOW.getValue());
        pawnStage.close();
        Platform.runLater(() -> Objects.requireNonNull(GuiApplication.getGui()).showTable("objective"));
    }
}
