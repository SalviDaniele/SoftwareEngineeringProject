package Controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.io.Serializable;

/**
 * Indicates the first phase of the turn, when the player has to place a card on their play area.
 * The phase validates the command entered by the player.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class PlacingPhase extends Phase implements Serializable {


    /**
     * The constructor of the placing phase class.
     *
     * @param match is the reference to the current match
     */
    public PlacingPhase(MatchController match) {
        super(match);
    }


    /**
     * Empty constructor of the PlacingPhase class, used for Jackson serialization.
     */
    public PlacingPhase() {}


    /**
     * Abstract method used to process commands in a specific phase.
     *
     * @return true if the command is legal during this phase, false otherwise
     */
    @Override
    public boolean processCommand(String command) {
        if (command.equalsIgnoreCase("place")) {
            this.transition(new DrawPhase(match));
            return true;
        } else if (command.equalsIgnoreCase("chat") || command.equalsIgnoreCase("hand")
                || command.equalsIgnoreCase("table") || command.equalsIgnoreCase("area")
                || command.equalsIgnoreCase("cheat1") || command.equalsIgnoreCase("cheat2")
                || command.equalsIgnoreCase("cheat3") || command.equalsIgnoreCase("secret")
                || command.equalsIgnoreCase("help")) {
            return true;
        }
        return false;
    }


    /**
     * Method used to return to the Gui the current phase.
     *
     * @return a string containing the current phase.
     */
    @Override
    public String communicatePhaseToGui() {
        return "PlacingPhase";
    }
}
