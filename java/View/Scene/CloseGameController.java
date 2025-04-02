package View.Scene;

import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;

import java.util.Optional;

/**
 * Controller for the FXML file CloseGameScene.fxml
 */
public class CloseGameController extends SceneController{

    /**
     * Label that asks the user if he wants to close the game
     */
    @FXML
    public Label yesNoText;


    /**
     * Initializes the controller class. This method is automatically called when the FXML file is loaded for the first time.
     * It calls the superclass' setTarget method.
     */
    @FXML
    public void initialize(){
        super.setTarget();
    }


    /**
     * Method that process the answer of the player.
     *
     * @param dialogPane is the root layout pane of the scene.
     * @param title is the title of the scene.
     * @param msg is the message to display.
     * @return true if the user pressed the button Yes, false if the user pressed the button No
     */
    public boolean answer(DialogPane dialogPane, String title, String msg){
        boolean answer;
        yesNoText.setText(msg);
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setDialogPane(dialogPane);
        Optional<ButtonType> opt = dialog.showAndWait();
        ButtonType buttonType = opt.orElse(ButtonType.CANCEL);
        answer = buttonType.equals(ButtonType.YES);
        return answer;
    }
}
