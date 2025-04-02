package View.Scene;

import View.Gui.GuiApplication;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * Controller for the FXML file ErrorMessage.fxml
 */
public class ErrorMessageController extends SceneController{
    /**
     * Label that informs the player of the reason for the error being displayed
     */
    @FXML
    Label text;

    /**
     * The stage that contains this scene
     */
    Stage container;


    /**
     * Initializes the controller class. This method is automatically called when the FXML file is loaded for the first time.
     * It calls the superclass' setTarget method.
     */
    @FXML
    public void initialize(){
        super.setTarget();
    }


    /**
     * Method that initialized the stage where the error message is displayed.
     * This stage displays for 3.5 seconds, then closes automatically
     *
     * @param pane the root of the scene
     * @param msg the message to be displayed
     * @param color the color of the message text
     */
    public void generateErrorMessage(AnchorPane pane, String msg, String color) {
        text.setText(msg);
        text.setStyle("-fx-text-fill: " + color + ";");
        container = new Stage();
        container.initOwner(GuiApplication.getWindow());
        container.initStyle(StageStyle.TRANSPARENT);
        Scene scene = new Scene(pane);
        scene.setFill(Color.TRANSPARENT);
        container.setScene(scene);
        container.setTitle("ERROR");
        container.show();

        PauseTransition pause = new PauseTransition(Duration.millis(2500));
        pause.setOnFinished(event -> container.close());
        pause.play();
    }
}
