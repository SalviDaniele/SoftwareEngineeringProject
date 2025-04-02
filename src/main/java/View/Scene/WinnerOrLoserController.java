package View.Scene;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.rmi.RemoteException;

/**
 * Controller for the FXML file WinnerOrLoserScene.fxml
 */
public class WinnerOrLoserController extends SceneController{

    /**
     * Label that displays if the player is the winner or the loser.
     */
    @FXML
    Label text;

    /**
     * Label that displays player's final score.
     */
    @FXML
    Label finalScore;

    /**
     * Final score of the user.
     */
    int points;


    /**
     * Initializes the controller class. This method is automatically called when the FXML file is loaded for the first time.
     * It calls the superclass' setTarget method and displays the final score of the player;
     */
    @FXML
    public void initialize(){
        super.setTarget();
        modifyStatus(target -> {
            try {
                points = target.getPlayerPoints(target.getNickname());
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
        if(points == 1)
            finalScore.setText("Your final score is 1 point.");
        else
            finalScore.setText("Your final score is " + points + " points.");
    }


    /**
     * This method sets the text on the label that displays if the player is the winner or the loser.
     * @param message the message to display
     */
    public void generateFinalMessage(String message){
        text.setText(message);
    }


    /**
     * Method used to close the game.
     */
    public void closeGame(){
        modifyStatus(target -> {
                try {
                    target.getServer().removeClient(target.getNickname());
                    target.closeConnection();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
        });
    }
}
