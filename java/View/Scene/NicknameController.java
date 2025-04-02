package View.Scene;

import Network.VirtualView;
import View.Gui.GuiApplication;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Controller for the FXML file NicknameScene.fxml
 */
public class NicknameController extends SceneController {

    /**
     * TextField for entering the nickname
     */
    @FXML
    TextField nickname;

    /**
     * Button to confirm the nickname choice
     */
    @FXML
    Button confirm;

    /**
     * The ID of the current match
     */
    private int matchId;

    /**
     * Initializes the controller class. This method is automatically called when the FXML file is loaded for the first time.
     * It calls the superclass' setTarget method.
     */
    @FXML
    public void initialize() {
        super.setTarget();
    }

    /**
     * This method is called when the confirm button is clicked.
     * If the player creating the lobby hasn't already chosen his nickname or the nickname chosen by the user is already
     * in use an error message is displayed. If the selected name is valid a method to handle the match creation is called.
     */
    @FXML
    public void confirmButtonClicked() {
        if(!nickname.getText().isEmpty()){
            String nicknameText = nickname.getText();
            matchId = 0;

            modifyStatus(target -> {
                AtomicBoolean alreadyInUse = new AtomicBoolean(false);

                threads.execute(() -> {
                    try {
                        alreadyInUse.set(target.chooseNameGui(nicknameText));
                    } catch (RemoteException e) {
                        System.out.println("\nError while checking the nickname: " + e.getMessage());
                    }
                    if (alreadyInUse.get()) {
                        Platform.runLater(() -> showError("The username is already in use!"));
                    } else {
                        try {
                            handleMatchCreation(target, nicknameText);
                        } catch (NoSuchElementException e) {
                            Platform.runLater(() -> showError("Another user is creating the first match, please retry."));
                        }
                    }
                });
            });
        }
    }


    /**
     * Method to set the error stage
     * @param message text of the error message
     */
    private void showError(String message) {
        FXMLLoader loader = new FXMLLoader(GuiApplication.class.getResource("/FXML/ErrorMessage.fxml"));
        AnchorPane pane;
        try {
            pane = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ErrorMessageController error = loader.getController();
        error.generateErrorMessage(pane, message, "red");
    }


    /**
     * Method to handle the match creation. If the user is the first player a new match is created, otherwise the user is
     * added to an existing match.
     *
     * @param target is the user's client
     * @param nicknameText is the nickname chosen by the client
     * @throws NoSuchElementException if the match the user is trying to access does not exist
     */
    private void handleMatchCreation(VirtualView target, String nicknameText) throws NoSuchElementException{
            try {
                if (target.isFirst()) {
                    matchId = target.addMatch(nicknameText);
                } else {
                    try { matchId = target.addPlayer(nicknameText); }
                    catch (NoSuchElementException e) { throw new NoSuchElementException(); }
                }
                if (matchId < 0) throw new NoSuchElementException();

                Platform.runLater(() -> {
                    try {
                        target.setIdMatch(matchId);
                        if (!target.allConnected(matchId)) {
                            Objects.requireNonNull(GuiApplication.getGui()).showWaiting("player");
                        } else {
                            Objects.requireNonNull(GuiApplication.getGui()).showTable("starting");
                        }
                    } catch (RemoteException e) {
                        System.out.println("\nError: " + e.getMessage());
                    }
                });

            } catch (RemoteException e) {
                System.out.println("\nError: " + e.getMessage());
            }
    }


    /**
     * This method is called if the Enter Key is pressed in the nickname TextField.
     * It calls the confirmButtonClicked method.
     *
     * @param key the key pressed
     */
    @FXML
    public void enterKeyPressed(KeyEvent key){
        if(key.getCode() == KeyCode.ENTER && !nickname.getText().isEmpty())
            confirmButtonClicked();
    }
}
