package View.Gui;

import View.Scene.*;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

/**
 * JavaFX manager class, used to change scene and save the Gui state
 */
public class GuiApplication extends Application {

    /**
     * The main stage of the application window
     */
    static Stage window;

    /**
     * The stage inc which the chat is displayed
     */
    static volatile Stage chatWindow;

    /**
     * A reference to the View associated with this player
     */
    static Gui gui;

    /**
     * The main scene of the application
     */
    static Scene scene;

    /**
     * The saved state for the Table Scene
     */
    static Scene tableScene;

    /**
     * The saved state for the PlayArea Scene
     */
    static Scene playAreaScene;

    /**
     * The saved state for the Chat Scene
     */
    static Scene chatScene;

    /**
     * The last opened scene of anotherPlayArea.
     */
    static Scene anotherPlayAreaScene;

    /**
     * The controller for the Table Scene
     */
    static TableSceneController tableCtrl;

    /**
     * The controller for the PlayArea Scene
     */
    static PlayAreaSceneController playAreaCtrl;

    /**
     * The controller for the Chat Scene
     */
    static ChatController chatController;

    /**
     * The controller for anotherPlayArea Scene.
     */
    static AnotherPlayAreaController anotherPlayAreaController;

    /**
     * The first scene is loaded and the main stage is initialized.
     *
     * @param stage the primary stage for this application, onto which the application scene can be set
     * */
    @Override
    public void start(Stage stage) {

        window = stage;
        gui.showFirstScene();
        window.setScene(scene);
        window.getIcons().add(new Image("PNG/logo2.png"));
        window.setTitle("Codex Naturalis");
        window.setResizable(true);
        window.setHeight(800);
        window.setMinHeight(800);
        window.setWidth(1422);
        window.setMinWidth(1422);
        window.show();
        window.setOnCloseRequest( e -> {
            e.consume();
            exit();
        });

    }

    /**
     * The method that launches the JavaFX application.
     */
    public static void guiLaunch() {
        launch();
    }

    /**
     * This method load a scene that asks if the user is sure to close the game.
     */
    public void exit(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/CloseGameScene.fxml"));
        DialogPane dialogPane;
        try {
            dialogPane = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        CloseGameController controller = loader.getController();
        if(controller.answer(dialogPane, "EXIT", "Are you sure you want to close the game?")){
            System.out.println("CLOSED");
            System.exit(0);
        }
    }

    /**
     * Setter method for gui attribute
     * @param gui the gui to associate to this player
     */
    public static void setGui(Gui gui) {
        GuiApplication.gui = gui;
    }

    /**
     * Getter method for gui attribute
     * @return a reference to the attribute gui
     */
    public static Gui getGui() {
        return gui;
    }

    /**
     * Getter method for the main stage.
     *
     * @return a reference to the main stage
     */
    public static Stage getWindow() {
        return window;
    }

    /**
     * Getter method for the chat stage.
     *
     * @return a reference to the chat stage
     */
    public static Stage getChatWindow() {
        return chatWindow;
    }

    /**
     * Method used to set the scene that let the user choose between two cards
     * @param obCard1 the image URL for the first card
     * @param obCard2 the image URL for the second card
     * @param type is the type of choice the user is going to make
     * @param title the title of the stage
     */
    public static void chooseBetweenTwoCards(String obCard1, String obCard2, String type, String title) {
        FXMLLoader loader = new FXMLLoader(GuiApplication.class.getResource("/FXML/ChooseBetweenTwoCardsScene.fxml"));
        AnchorPane root;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ChooseBetweenTwoCardsController controller = loader.getController();
        //controller.setTarget();
        controller.chooseBetweenTwoCards(root,  type, title, obCard1, obCard2);
    }

    /**
     * Method used to set the scene that let the user the color of his pawn
     */
    public static void choosePawnColor() {
        FXMLLoader loader = new FXMLLoader(GuiApplication.class.getResource("/FXML/ChoosePawnScene.fxml"));
        AnchorPane root;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ChoosePawnController controller = loader.getController();
        Scene scene = new Scene(root);
        Stage pawnStage = new Stage();
        pawnStage.setScene(scene);
        //controller.setTarget();
        controller.pawnSceneInitialization(pawnStage);
        pawnStage.setOnCloseRequest(Event::consume);
    }

    /**
     * Method used to set a generic scene for the first time
     * @param path the path of the FXML file
     */
    public static void setNewScene(String path){
        FXMLLoader loader = new FXMLLoader(GuiApplication.class.getResource(path));
        try {
            scene.setRoot(loader.load());
            window.setScene(scene);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        SceneController ctrl = loader.getController();
    }

    /**
     * Method used to set the Table scene for the first time
     *
     * @param phase it can assume three values: 'starting', 'objective', and 'draw',
     *              depending on the phase in which the table scene is asked
     */
    public static void setNewTableScene(String phase){
        FXMLLoader loader = new FXMLLoader(GuiApplication.class.getResource("/FXML/TableScene.fxml"));
        try {
            tableScene = new Scene(loader.load());
            window.setScene(tableScene);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        tableCtrl = loader.getController();
        tableCtrl.tablePhases(phase);
    }

    /**
     * Method used to set the Table scene that had already been loaded
     *
     * @param phase it can assume three values: 'starting', 'objective', and 'draw',
     *              depending on the phase in which the table scene is asked
     */
    public static void setTableScene(String phase){
        window.setScene(tableScene);
        tableCtrl.tablePhases(phase);
    }

    /**
     * Method used to set the PlayArea scene for the first time
     *
     * @param phase it can assume three values: 'starting', 'firstTurn', and 'placing',
     *              depending on the phase in which the playArea scene is asked
     */
    public static void setNewPlayAreaScene(String phase){
        FXMLLoader loader = new FXMLLoader(GuiApplication.class.getResource("/FXML/PlayAreaScene.fxml"));
        try {
            playAreaScene = new Scene(loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        window.setScene(playAreaScene);
        playAreaCtrl = loader.getController();
        playAreaCtrl.playAreaPhases(phase);
    }

    /**
     * Method used to set the PlayArea scene that had already been loaded
     *
     * @param phase it can assume three values: 'starting', 'firstTurn', and 'placing',
     *              depending on the phase in which the playArea scene is asked
     */
    public static void setPlayAreaScene(String phase){
        window.setScene(playAreaScene);
        playAreaCtrl.playAreaPhases(phase);
    }

    /**
     * Method used to set the Chat scene for the first time
     */
    public static void createNewChatScene() {
        FXMLLoader loader = new FXMLLoader(GuiApplication.class.getResource("/FXML/ChatScene.fxml"));
        chatWindow = new Stage();
        try {
            chatScene = new Scene(loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        chatWindow.setScene(chatScene);
        chatWindow.setTitle("CHAT");
        chatWindow.setResizable(false);
        chatWindow.setOnCloseRequest( e -> {
            tableCtrl.disableCircleChat();
            playAreaCtrl.disableCircleChat();
        });
        chatController = loader.getController();
    }

    /**
     * Method used to set the Chat scene that had already been loaded
     */
    public static void setChatScene(){
        chatWindow.show();
    }

    /**
     * Method used to load a scene that asks the user to wait
     *
     * @param type indicate the reason the player must wait
     */
    public static void setNewWaitingScene(String type){
        FXMLLoader loader = new FXMLLoader(GuiApplication.class.getResource("/FXML/WaitingScene.fxml"));
        try {
            scene.setRoot(loader.load());
            window.setScene(scene);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        WaitingController ctrl = loader.getController();
        switch(type){
            case "player":
                ctrl.waitForPlayerThread();
                break;
            case "pawn":
                ctrl.waitForPawnThread(null);
                break;
            case "starter":
                ctrl.waitForStarterCardThread(null);
                break;
            case "objective":
                ctrl.waitForObjectiveChoiceThread(null);
                break;
            case "endgame":
                ctrl.waitForEndgameThread();
                break;
        }
    }

    /**
     * Method used to load a scene on a different stage that asks the user to wait
     *
     * @param type indicate the reason the player must wait
     * @param stage the stage where the scene is going to be set
     */
    public static void setNewWaitingSceneOnStage(String type, Stage stage){
        FXMLLoader loader = new FXMLLoader(GuiApplication.class.getResource("/FXML/WaitingScene.fxml"));
        Scene newScene;
        try {
            newScene = new Scene(loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.setScene(newScene);
        stage.initModality(Modality.APPLICATION_MODAL);
        //stage.initStyle(StageStyle.UNDECORATED);
        stage.setOnCloseRequest(Event::consume);
        stage.setMinHeight(600);
        stage.setMinWidth(900);
        stage.setMaxHeight(600);
        stage.setMaxWidth(900);
        WaitingController ctrl = loader.getController();
        switch(type){
            case "player":
                ctrl.waitForPlayerThread();
                break;
            case "pawn":
                ctrl.waitForPawnThread(stage);
                break;
            case "starter":
                ctrl.waitForStarterCardThread(stage);
                break;
            case "objective":
                ctrl.waitForObjectiveChoiceThread(stage);
        }
        stage.show();
    }

    /**
     * Method used to set the first scene of the application
     *
     * @param path the path of the FXML file
     */
    public static void setFirstScene(String path) {
        FXMLLoader loader = new FXMLLoader(GuiApplication.class.getResource(path));
        Parent root;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        scene = new Scene(root);
        SceneController ctrl = loader.getController();
        ctrl.setTarget();
    }

    /**
     * Method used to set the last scene of the application
     *
     * @param path the path of the FXML file
     * @param message to communicate if the player is the winner or the loser
     */
    public static void setLastScene(String path, String message){
        FXMLLoader loader = new FXMLLoader(GuiApplication.class.getResource(path));
        Parent root;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        scene.setRoot(root);
        window.setScene(scene);
        WinnerOrLoserController ctrl = loader.getController();
        ctrl.generateFinalMessage(message);
        PauseTransition pause = new PauseTransition(Duration.seconds(10));
        pause.setOnFinished(event -> ctrl.closeGame());
        pause.play();
    }

    /**
     * Method used to display a chat notification to the player.
     */
    public static void chatNotification(){
        if(chatWindow == null){
            Platform.runLater(GuiApplication::createNewChatScene);
        }
        while (chatWindow == null) Thread.onSpinWait();
        if(!chatWindow.isShowing()){
            Platform.runLater(() -> playAreaCtrl.activateCircle());
            Platform.runLater(() -> tableCtrl.activateCircle());
            if(anotherPlayAreaScene != null && window.getScene().equals(anotherPlayAreaScene))
                Platform.runLater(() -> anotherPlayAreaController.activateCircle());
        }
    }

    /**
     * Method used to set the scene that shows the PlayArea of another Player.
     *
     * @param player the name of the Player selected
     */
    public static void setAnotherPlayArea(String player){
        FXMLLoader loader = new FXMLLoader(GuiApplication.class.getResource("/FXML/AnotherPlayAreaScene.fxml"));
        try {
            scene.setRoot(loader.load());
            window.setScene(scene);
            anotherPlayAreaScene = scene;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        AnotherPlayAreaController ctrl = loader.getController();
        ctrl.setNickname(player);
        anotherPlayAreaController = ctrl;
    }
}
