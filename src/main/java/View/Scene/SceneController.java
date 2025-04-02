package View.Scene;

import Listeners.GuiStatusModifier;
import Network.VirtualView;
import View.Gui.Gui;
import View.Gui.GuiApplication;

/**
 * This class controls the scenes within the application and extends the ListenerGui class
 * to manage listeners of type VirtualView.
 */
public class SceneController extends GuiStatusModifier<VirtualView> {

    /**
     * Initializes the scene controller by retrieving the VirtualView from the Gui
     * linked to the GuiApplication and adding it as a target to modify.
     */
    public void setTarget() {
        Gui gui = GuiApplication.getGui();
        VirtualView target = null;
        if(gui.getClass().equals(Gui.class)){
            target = gui.getTarget();
        }
        this.setTarget(target);
    }
}
