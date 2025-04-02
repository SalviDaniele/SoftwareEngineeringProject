package Listeners;

import Model.Cards.*;
import Model.PlayArea;
import Model.Table;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Represents a message carrying information about a player or table to a server.
 */
public class Message implements Serializable {

    /**
     * Name of the player.
     */
    private String nickname;

    /**
     * Player's starter card.
     */
    private StarterCard starterCard;

    /**
     * Objective cards on the table.
     */
    private ObjectiveCard[] cardsO;

    /**
     * Gold cards on the table.
     */
    private GoldCard[] cardsG;

    /**
     * Resource cards on the table.
     */
    private ResourceCard[] cardsR;

    /**
     * Color of the card on top of the gold deck.
     */
    private Colors colorsG;

    /**
     * Color of the card on top of the resource deck.
     */
    private Colors colorsR;

    /**
     * a reference to the deck of resource cards on the table
     */
    private List<ResourceCard> deckR;

    /**
     * a reference to the deck of gold cards on the table
     */
    private List<GoldCard> deckG;

    /**
     * Player's play area.
     */
    private PlayArea playArea;

    /**
     * Name of the owner of the play area the client wants to see.
     */
    private String areaOwner;

    /**
     * Player's points.
     */
    private int points;

    /**
     * Player's hand.
     */
    private List<Card> hand;

    /**
     * A reference to the table.
     */
    private Table table;

    /**
     * Player's secret objective.
     */
    private ObjectiveCard objective;

    /**
     * First option for the secret objective choice.
     */
    private ObjectiveCard obchoice1;

    /**
     * Second option for the secret objective choice.
     */
    private ObjectiveCard obchoice2;

    /**
     * Player's available resources.
     */
    private Map<Symbols, Integer> availableResources;

    /**
     * Represents an event or status in the game.
     * Serves as a mean to generate the relative message.
     */
    private GameStatus status;

    /**
     * Player's information serialized as a string.
     */
    private String args;


    /**
     * Constructor for the Message class for the VIEW STARTER CARD, STARTER CARD PLACED status.
     *
     * @param args represents the serialized player
     * @param status represents an event or status in the game
     * @param nickname of the player
     * @param starterCard the starter card of the player
     */
    public Message(String args, GameStatus status, String nickname, StarterCard starterCard) {
        this.args = args;
        this.status = status;
        this.nickname = nickname;
        this.starterCard = starterCard;
    }


    /**
     * Constructor for the Message class for IT IS YOUR TURN, I HAVE WON and I HAVE LOST statuses.
     *
     * @param args represents the serialized player
     * @param status represents an event or status in the game
     * @param nickname of the player
     */
    public Message(String args, GameStatus status, String nickname) {
        this.args = args;
        this.status = status;
        this.nickname = nickname;
    }


    /**
     * Constructor for the Message class for the VIEW SECRET OBJECTIVE status.
     *
     * @param args represents the serialized player
     * @param status represents an event or status in the game
     * @param nickname of the player
     * @param objective player's objective card
     */
    public Message(String args, GameStatus status, String nickname, ObjectiveCard objective) {
        this.args = args;
        this.status = status;
        this.nickname = nickname;
        this.objective = objective;
    }


    /**
     * Constructor for the Message class for the VIEW DIFFERENCES OF RESOURCES status.
     *
     * @param args represents the serialized player
     * @param status represents an event or status in the game
     * @param nickname of the player
     * @param playArea the player's play area
     */
    public Message(String args, GameStatus status, String nickname, PlayArea playArea){
        this.args = args;
        this.status = status;
        this.nickname = nickname;
        this.playArea = playArea;
    }


    /**
     * Constructor for the Message class for the VIEW TABLE status.
     *
     * @param args represents the serialized player
     * @param status represents an event or status in the game
     * @param nickname of the player
     * @param deckR the resource deck on the table
     * @param deckG the gold deck on the table
     * @param cardsR resource cards on the table
     * @param cardsG gold cards on the table
     * @param cardsO objective cards on the table
     */
    public Message(String args, GameStatus status, String nickname, List<ResourceCard> deckR, List<GoldCard> deckG, ResourceCard[] cardsR, GoldCard[] cardsG, ObjectiveCard[] cardsO) {
        this.args = args;
        this.status = status;
        this.nickname = nickname;
        this.deckR = deckR;
        this.deckG = deckG;
        this.cardsR = cardsR;
        this.cardsG = cardsG;
        this.cardsO = cardsO;
    }


    /**
     * Constructor for the Message class for the VIEW STARTING TABLE status.
     *
     * @param args represents the serialized player
     * @param status represents an event or status in the game
     * @param nickname of the player
     * @param colorsR the color of the card on top of the resource deck
     * @param colorsG the color of the card on top of the gold deck
     * @param cardsR resource cards on the table
     * @param cardsG gold cards on the table
     */
    public Message(String args, GameStatus status, String nickname, Colors colorsR, Colors colorsG, ResourceCard[] cardsR, GoldCard[] cardsG) {
        this.args = args;
        this.status = status;
        this.nickname = nickname;
        this.colorsR = colorsR;
        this.colorsG = colorsG;
        this.cardsR = cardsR;
        this.cardsG = cardsG;
    }


    /**
     * Constructor for the Message class for the VIEW CHOICE OBJECTIVES status.
     *
     * @param args represents the serialized player
     * @param status represents an event or status in the game
     * @param nickname of the player
     * @param obchoice1 the first option for the secret objective choice
     * @param obchoice2 the second option for the secret objective choice
     */
    public Message(String args, GameStatus status, String nickname, ObjectiveCard obchoice1, ObjectiveCard obchoice2) {
        this.args = args;
        this.status = status;
        this.nickname = nickname;
        this.obchoice1 = obchoice1;
        this.obchoice2 = obchoice2;
    }


    /**
     * Constructor for the Message class for the SHOW POINTS status.
     *
     * @param args represents the serialized player
     * @param status represents an event or status in the game
     * @param nickname of the player
     * @param points the player's points
     */
    public Message(String args, GameStatus status, String nickname, int points) {
        this.args = args;
        this.status = status;
        this.nickname = nickname;
        this.points = points;
    }


    /**
     * Constructor for the Message class for the VIEW HAND status.
     *
     * @param args represents the serialized player
     * @param status represents an event or status in the game
     * @param nickname of the player
     * @param hand the player's hand
     */
    public Message(String args, GameStatus status, String nickname, List<Card> hand) {
        this.args = args;
        this.status = status;
        this.nickname = nickname;
        this.hand = hand;
    }


    /**
     * Constructor for the Message class for the VIEW PLAY AREA, VIEW DIFFERENCES OF RESOURCES statuses.
     *
     * @param args represents the serialized player
     * @param status represents an event or status in the game
     * @param nickname of the player
     * @param hand the player's hand
     * @param playArea the player's play area
     * @param points the player's points
     */
    public Message(String args, GameStatus status, String nickname, List<Card> hand, PlayArea playArea, int points) {
        this.args = args;
        this.status = status;
        this.nickname = nickname;
        this.hand = hand;
        this.playArea = playArea;
        this.points = points;
        this.availableResources = playArea.getAvailableResources();
    }


    /**
     * Constructor for the Message class for the VIEW PLAY AREA status.
     *
     * @param args represents the serialized player
     * @param status represents an event or status in the game
     * @param nickname of the player
     * @param areaOwner the name of the player owning the play area the client wants to see
     * @param playArea the play area the client wants to see
     * @param points the points of the player owning the play area the client wants to see
     */
    public Message(String args, GameStatus status, String nickname, String areaOwner, PlayArea playArea, int points) {
        this.args = args;
        this.status = status;
        this.nickname = nickname;
        this.areaOwner = areaOwner;
        this.playArea = playArea;
        this.points = points;
        this.availableResources = playArea.getAvailableResources();
    }


    /**
     * Constructor for the Message class for the ACTION FAILED status.
     *
     * @param status represents an event or status in the game
     * @param nickname of the player
     */
    public Message(GameStatus status, String nickname) {
        this.status = status;
        this.nickname = nickname;
    }


    /**
     * Empty constructor for the Message class for serialization.
     */
    public Message() {}


    /**
     * Getter method for the nickname attribute.
     *
     * @return the player's name
     */
    public String getNickname() {
        return nickname;
    }


    /**
     * Getter method for the starterCard attribute.
     *
     * @return the player's starter card
     */
    public StarterCard getStarterCard() {
        return starterCard;
    }


    /**
     * Getter method for the cardsO attribute.
     *
     * @return the objective cards on the table
     */
    public ObjectiveCard[] getCardsO() {
        return cardsO;
    }


    /**
     * Getter method for the cardsG attribute.
     *
     * @return the gold cards on the table
     */
    public GoldCard[] getCardsG() {
        return cardsG;
    }


    /**
     * Getter method for the cardsR attribute.
     *
     * @return the resource cards on the table
     */
    public ResourceCard[] getCardsR() {
        return cardsR;
    }


    /**
     * Getter method for the deckR attribute.
     *
     * @return the resource deck on the table
     */
    public List<ResourceCard> getDeckR() {
        return deckR;
    }


    /**
     * Getter method for the deckG attribute.
     *
     * @return the gold deck on the table
     */
    public List<GoldCard> getDeckG() {
        return deckG;
    }


    /**
     * Getter method for the colorsG attribute.
     *
     * @return the color of the card on top of the gold deck
     */
    public Colors getColorsG() {
        return colorsG;
    }


    /**
     * Getter method for the colorsR attribute.
     *
     * @return the color of the card on top of the resource deck
     */
    public Colors getColorsR() {
        return colorsR;
    }


    /**
     * Getter method for the playArea attribute.
     *
     * @return the player's play area
     */
    public PlayArea getPlayArea() {
        return playArea;
    }


    /**
     * Getter method for the areaOwner attribute.
     *
     * @return the name of the player owning the area the client wants to see
     */
    public String getAreaOwner() {
        return areaOwner;
    }


    /**
     * Getter method for the points attribute.
     *
     * @return the player's points
     */
    public int getPoints() {
        return points;
    }


    /**
     * Getter method for the hand attribute.
     *
     * @return the player's hand
     */
    public List<Card> getHand() {
        return hand;
    }


    /**
     * Getter method for the table attribute.
     *
     * @return a reference to the table
     */
    public Table getTable() {
        return table;
    }


    /**
     * Getter method for the objective attribute.
     *
     * @return the player's secret objective
     */
    public ObjectiveCard getObjective() {
        return objective;
    }


    /**
     * Getter method for the status attribute.
     *
     * @return the status of the game
     */
    public GameStatus getStatus() {
        return status;
    }


    /**
     * Getter method for the args attribute.
     *
     * @return a serialization of the player as string
     */
    public String getArgs() {
        return args;
    }


    /**
     * Getter method for the obchoice1 attribute.
     *
     * @return the first option for the secret objective choice
     */
    public ObjectiveCard getObchoice1() {
        return obchoice1;
    }


    /**
     * Getter method for the obchoice2 attribute.
     *
     * @return the second option for the secret objective choice
     */
    public ObjectiveCard getObchoice2() {
        return obchoice2;
    }


    /**
     * Getter method for the availableResources attribute.
     *
     * @return the player's available resources
     */
    public Map<Symbols, Integer> getAvailableResources() {
        return availableResources;
    }


    /**
     * Setter method for the nickname attribute.
     *
     * @param nickname player's name
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }


    /**
     * Setter method for the starterCard attribute.
     *
     * @param starterCard player's starter card
     */
    public void setStarterCard(StarterCard starterCard) {
        this.starterCard = starterCard;
    }


    /**
     * Setter method for the cardsO attribute.
     *
     * @param cardsO objective cards on the table
     */
    public void setCardsO(ObjectiveCard[] cardsO) {
        this.cardsO = cardsO;
    }


    /**
     * Setter method for the cardsG attribute.
     *
     * @param cardsG gold cards on the table
     */
    public void setCardsG(GoldCard[] cardsG) {
        this.cardsG = cardsG;
    }


    /**
     * Setter method for the cardsR attribute.
     *
     * @param cardsR resource cards on the table
     */
    public void setCardsR(ResourceCard[] cardsR) {
        this.cardsR = cardsR;
    }


    /**
     * Setter method for the colorsG attribute.
     *
     * @param colorsG the color of the card on top of the gold deck
     */
    public void setColorsG(Colors colorsG) {
        this.colorsG = colorsG;
    }


    /**
     * Setter method for the colorsR attribute.
     *
     * @param colorsR the color of the card on top of the resource deck
     */
    public void setColorsR(Colors colorsR) {
        this.colorsR = colorsR;
    }


    /**
     * Setter method for the deckR attribute.
     *
     * @param deckR the resource deck on the table
     */
    public void setDeckR(List<ResourceCard> deckR) {
        this.deckR = deckR;
    }


    /**
     * Setter method for the deckG attribute.
     *
     * @param deckG the gold deck on the table
     */
    public void setDeckG(List<GoldCard> deckG) {
        this.deckG = deckG;
    }


    /**
     * Setter method for the playArea attribute.
     *
     * @param playArea player's play area
     */
    public void setPlayArea(PlayArea playArea) {
        this.playArea = playArea;
    }


    /**
     * Setter method for the areaOwner attribute.
     *
     * @param areaOwner is the name of the owner of the area the client wants to see
     */
    public void setAreaOwner(String areaOwner) {
        this.areaOwner = areaOwner;
    }


    /**
     * Setter method for the points attribute.
     *
     * @param points player's points
     */
    public void setPoints(int points) {
        this.points = points;
    }


    /**
     * Setter method for the hand attribute.
     *
     * @param hand player's hand
     */
    public void setHand(List<Card> hand) {
        this.hand = hand;
    }


    /**
     * Setter method for the table attribute.
     *
     * @param table reference to table
     */
    public void setTable(Table table) {
        this.table = table;
    }


    /**
     * Setter method for the objective attribute.
     *
     * @param objective player's secret objective
     */
    public void setObjective(ObjectiveCard objective) {
        this.objective = objective;
    }


    /**
     * Setter method for the obchoice1 attribute.
     *
     * @param obchoice1 player's first option for the secret objective
     */
    public void setObchoice1(ObjectiveCard obchoice1) {
        this.obchoice1 = obchoice1;
    }


    /**
     * Setter method for the obchoice2 attribute.
     *
     * @param obchoice2 player's second option for the secret objective
     */
    public void setObchoice2(ObjectiveCard obchoice2) {
        this.obchoice2 = obchoice2;
    }


    /**
     * Setter method for the availableResources attribute.
     *
     * @param availableResources player's available resources
     */
    public void setAvailableResources(Map<Symbols, Integer> availableResources) {
        this.availableResources = availableResources;
    }


    /**
     * Setter method for the status attribute.
     *
     * @param status of the game
     */
    public void setStatus(GameStatus status) {
        this.status = status;
    }


    /**
     * Setter method for the args attribute.
     *
     * @param args is the serialized player
     */
    public void setArgs(String args) {
        this.args = args;
    }
}
