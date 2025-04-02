package Controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;


/**
 * The abstract class used to implement the state design pattern for the match.
 * Every phase is a state of the current turn.
 * There are 2 phases:
 * - PlacingPhase: the player has to place the card in order to continue their turn
 * - DrawPhase: the player has to draw to pass the turn to the next player
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = PlacingPhase.class, name = "placingPhase"),
        @JsonSubTypes.Type(value = DrawPhase.class, name = "drawPhase")
})
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public abstract class Phase implements Serializable {


    /**
     * A reference to the server of the current game.
     */
    @JsonIgnore
    protected MatchController match;


    /**
     * The constructor of the Phase abstract class.
     *
     * @param match is the reference to the current match
     */
    public Phase(MatchController match) {
        this.match = match;
    }


    /**
     * Empty constructor of the Phase class, used for Jackson serialization.
     */
    public Phase() {}


    /**
     * Abstract method used to process a command and notify the client if that command is valid during this phase.
     * It is to be concretely defined in every phase.
     */
    abstract public boolean processCommand(String command);


    /**
     * Abstract method used to communicate to the Gui the current Phase.
     *
     * @return a String containing the current Phase.
     */
    abstract public String communicatePhaseToGui();


    /**
     * Method used to transition to one phase to the next.
     * @param phase indicates the phase which is currently taking place in the game.
     */
    public void transition(Phase phase) {
        match.setPhase(phase);
    }
}
