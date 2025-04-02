package View.Gui;

import Listeners.GuiStatusModifier;
import Network.Chat;
import Network.VirtualView;
import javafx.application.Platform;

import java.rmi.RemoteException;

/**
 * Class that manages the transition of GUI scenes.
 */
public class Gui extends GuiStatusModifier<VirtualView>{

    /**
     * Thread used to run the Gui.
     */
    Thread guiThread = new Thread(GuiApplication::guiLaunch);

    /**
     * A reference to the chat of this player.
     */
    Chat chat;

    /**
     * A boolean that is set to true if the player is the winner,
     * false otherwise.
     */
    boolean winner;

    /**
     * A boolean that indicates if the chat has been already
     * opened once.
     */
    boolean chatAlreadyOpened = false;

    /**
     * A boolean that indicates if the PlayArea scene has been already
     * opened once.
     */
    boolean playAreaScene = false;

    /**
     * A boolean that indicates if the Table scene has been already
     * opened once.
     */
    boolean tableScene = false;

    /**
     * A boolean that is set to true if the user is the first player
     * in the current game.
     */
    boolean first = false;

    /**
     * A boolean that is set to true if the thread to check if
     * the user is the first player in the current game is over.
     */
    volatile boolean threadOver = false;

    /**
     * Constructor for Gui class.
     */
    public Gui() {
        guiThread.start();
        GuiApplication.setGui(this);
    }

    /**
     * Sets the first Scene of the game. If the user is the first to connect
     * is displayed a scene that asks the user many players [2-4] will play,
     * otherwise is displayed a scene that asks the user to choose a unique nickname.
     */
    public void showFirstScene() {
        System.out.println(threadOver);
        modifyStatus(target -> threads.execute(() -> {
            try {
                if (target.firstPlayer()) {
                    target.setFirst(true);
                    first = true;
                }
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            threadOver = true;
        }));
        while (!threadOver) {
            Thread.onSpinWait();
        }
        if (first)
            GuiApplication.setFirstScene("/FXML/PlayerNumberScene.fxml");
        else
            GuiApplication.setFirstScene("/FXML/NicknameScene.fxml");
    }


    /**
     * Asks the user to choose a unique nickname
     */
    public void askPlayerNickname() {
        GuiApplication.setNewScene("/FXML/NicknameScene.fxml");
    }


    /**
     * Shows a scene that asks the user to wait
     *
     * @param type indicate the reason the player must wait
     */
    public void showWaiting(String type){ GuiApplication.setNewWaitingScene(type); }


    /**
     * Shows the Table of the game
     *
     * @param phase it can assume three values: 'starting', 'objective', and 'draw',
     *              depending on the phase in which the table scene is asked
     */
    public void showTable(String phase) {
        if(!tableScene) {
            GuiApplication.setNewTableScene(phase);
            tableScene = true;
        }
        else
            GuiApplication.setTableScene(phase);
    }


    /**
     * Shows the PlayArea of the Player
     *
     * @param phase it can assume three values: 'starting', 'firstTurn', and 'placing',
     *              depending on the phase in which the playArea scene is asked
     */
    public void showPlayArea(String phase){
        if(!playAreaScene) {
            GuiApplication.setNewPlayAreaScene(phase);
            playAreaScene = true;
        }
        else
            GuiApplication.setPlayAreaScene(phase);
    }


    /**
     * Shows the Winner scene if the user is the winner,
     * shows the Loser scene otherwise
     */
    public void showWinnerOrLoser() {
        modifyStatus(target -> threads.execute(() -> {
            try {
                winner = target.endGameMessageGui();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            if(winner){
                Platform.runLater(() -> GuiApplication.setLastScene("/FXML/WinnerOrLoserScene.fxml", "You won, congratulations!"));
            }
            else{
                Platform.runLater(() -> GuiApplication.setLastScene("/FXML/WinnerOrLoserScene.fxml", "Sorry, you lost"));
            }
        }));
    }


    /**
     * Shows the playArea of another player.
     *
     * @param playerName is the name of the player.
     */
    public void showAnotherPlayArea(String playerName) {
        GuiApplication.setAnotherPlayArea(playerName);
    }


    /**
     * Shows the Chat stage of the player.
     */
    public void showChatStage() {
        if (!chatAlreadyOpened) {
            if(GuiApplication.getChatWindow() == null) {
                GuiApplication.createNewChatScene();
            }
            GuiApplication.setChatScene();
            chatAlreadyOpened = true;
        }
        else
            GuiApplication.setChatScene();
    }


    /**
     * Sets the chat for the relative Client
     */
    public void initializeChat() {
        chat = new Chat(true);
        modifyStatus(target -> threads.execute(() -> {
            try {
                target.setChat(chat);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }));
        chat.setTarget(this);
    }


    /**
     * Notify the player he has received a message.
     */
    public void chatNotification() {
        GuiApplication.chatNotification();
    }
}
