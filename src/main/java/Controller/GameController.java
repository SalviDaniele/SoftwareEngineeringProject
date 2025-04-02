package Controller;

import Listeners.Listener;
import Model.Player;
import Model.Table;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Controller which manages the creation of new matches and adds players to those matches.
 */
public class GameController implements Serializable {

    /**
     * A list of every match currently starting or already started.
     */
    private final List<MatchController> matchList = new ArrayList<>();

    /**
     * The ID of the first match created, usually 0.
     */
    private int currentMatch;

    /**
     * An integer indicating the number of players chosen by the creator of the match.
     * Every time a player has been added to the match, this number decreases until it reaches 0 anf the match starts.
     */
    private int playersQueue;

    /**
     * An attribute which is true if a player is the first one to enter the match, which means they are creating it.
     */
    private boolean first = true;


    /**
     * Constructor method for the GameController.
     * Sets also the players queue to 0.
     *
     * @param i is the id of the first match created, usually 0.
     */
    public GameController(int i) {
        currentMatch = i;
        playersQueue = 0;
    }


    /**
     * Empty constructor of the GameController class, used for Jackson serialization.
     */
    public GameController() {}


    /**
     * Getter method for the matchList attribute.
     *
     * @return the list containing all the matchControllers in the game.
     */
    public List<MatchController> getMatchList() {
        return matchList;
    }


    /**
     * Gets a match based on the provided ID.
     *
     * @param id of the searched match
     * @return the match with the provided ID
     */
    public MatchController getMatch(int id){
        if (id < 0 || id >= matchList.size()) {
            throw new IndexOutOfBoundsException("Invalid match id: " + id);
        }
        return matchList.get(id);
    }


    /**
     * Getter method for the playersQueue attribute.
     *
     * @return the value of the queue
     */
    public int getPlayersQueue(){
        return playersQueue;
    }


    /**
     * Setter method for the playersQueue attribute.
     *
     * @param playersQueue indicates how many players still need to join the match in order to start it
     */
    public void setPlayersQueue(int playersQueue) {
        this.playersQueue = playersQueue;
    }


    /**
     * Getter method for the first attribute.
     *
     * @return true if the player is the one who is creating the current match
     */
    public boolean isFirst() {
        return first;
    }


    /**
     * Setter method for the first attribute.
     *
     * @param first true if the player is the one who is creating the current match
     */
    public void setFirst(boolean first) {
        this.first = first;
    }


    /**
     * Getter method for the currentMatch attribute.
     *
     * @return the ID of the current match
     */
    public int getCurrentMatch() {
        return currentMatch;
    }


    /**
     * Setter method for the currentMatch attribute.
     *
     * @param currentMatch the ID of the current match
     */
    public void setCurrentMatch(int currentMatch) {
        this.currentMatch = currentMatch;
    }


    /**
     * This method creates a new match in the controller and adds the player who invoked this method
     * as the first player to join the match.
     *
     * @param nickname name of the player
     * @param playerListener is the listener which is going to be added to the newly created player
     * @return the ID of the created match
     */
    public int addMatch(String nickname, Listener playerListener) {
        MatchController match;
        if (first) {
            try {match = new MatchController(currentMatch, playersQueue, new Table());}
            catch (IOException | ParseException e) {throw new RuntimeException(e);}
            matchList.add(match);
            addPlayer(nickname, playerListener);
            first = false;

        } else {
            currentMatch++;
            try {match = new MatchController(currentMatch, playersQueue, new Table());}
            catch (IOException | ParseException e) {throw new RuntimeException(e);}
            matchList.add(match);
            addPlayer(nickname, playerListener);
        }
        return currentMatch;
    }


    /**
     * This method adds a player to the current match.
     *
     * @param nickname name of the player
     * @param playerListener is the listener which is going to be added to the newly created player
     * @return the ID of the current match
     * @throws NoSuchElementException if there is still no match created, whereas another player has already started
     * the software and is currently creating the first match
     */
    public int addPlayer(String nickname, Listener playerListener) throws NoSuchElementException {
        MatchController match = matchList.getLast();
        if(playersQueue > 0) {
            Player player = new Player(nickname, currentMatch, match.getTable());
            match.getTable().getPlayers().add(player);
            if (match.getTable().getPlayers().getFirst().equals(player))
                player.setYourTurn(true);
            else player.setYourTurn(false);
            player.addListener(playerListener);
            playersQueue--;
        }
        return currentMatch;
    }
}
