package View.Scene;

import View.Gui.GuiApplication;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.jetbrains.annotations.NotNull;

import java.rmi.RemoteException;
import java.util.Objects;

/**
 * Controller for the FXML file PlayerNumberScene.fxml
 */
public class PlayerNumberController extends SceneController{


    /**
     * Combobox containing numbers from 2 to 4.
     */
    @FXML
    ComboBox comboBox;


    /**
     * Initializes the controller class. This method is automatically called when the FXML file is loaded for the first time.
     * It  calls the superclass' setTarget method.
     */
    @FXML
    public void initialize(){
        super.setTarget();
    }


    /**
     * This FXML method is triggered when the confirm button is pressed.
     * It notifies the Client by communicating how many players the game that is being created must have.
     */
    @FXML
    public void confirm(){
        modifyStatus(target -> {
            threads.execute(() -> {
                try {
                    target.getServer().setNumbOfPlayers(Integer.parseInt(comboBox.getSelectionModel().getSelectedItem().toString()));
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            });
        });
        Platform.runLater(() -> Objects.requireNonNull(GuiApplication.getGui()).askPlayerNickname());
    }


    /**
     * This FXML method is triggered if the Enter Key is pressed.
     * It calls the confirm method.
     *
     * @param key the key pressed.
     */
    @FXML
    public void confirmKey(@NotNull KeyEvent key){
        if(key.getCode() == KeyCode.ENTER)
            confirm();
    }
}
