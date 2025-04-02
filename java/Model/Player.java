package Model;

import Listeners.GameStatus;
import Listeners.Listened;
import Listeners.Listener;
import Controller.MatchController;
import Model.CalculatePoints.CalculatePoints;
import Model.Cards.*;
import Model.CalculatePoints.PointsByPosition;
import Model.CalculatePoints.PointsPerObject;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * class representing a player of the game
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "nickname")
public class Player extends Listened implements Serializable {

    /**
     * This is the name of the player
     */
    private String nickname;

    /**
     * This is a list of cards representing the hand of a player
     */
    private List<Card> hand;

    /**
     * This attribute is the objective card that each player has
     */
    private ObjectiveCard objective;

    /**
     * At the start of the match each player must choose an objective card, this is the first choice
     */
    private ObjectiveCard obChoice1;

    /**
     * At the start of the match each player must choose an objective card, this is the second choice
     */
    private ObjectiveCard obChoice2;

    /**
     * This integer indicates how many points a player has
     */
    private int points;

    /**
     * This is a reference to the id of the match disputed by the player
     */
    private int idMatch;

    /**
     * Boolean showing if a player has the black pawn or not
     */
    private boolean blackPawn = false;
    
    /**
     * A reference to the playArea of the player
     */
    @JsonManagedReference
    private PlayArea playArea;

    /**
     * A reference to the table of the match
     */
    @JsonBackReference
    private Table table;

    /**
     * A reference to the class MatchController
     */
    private MatchController match;

    /**
     * The card that the player has chosen
     */
    private Card chosenCard;

    /**
     * This attribute is true if it is currently the turn of this player
     */
    private boolean yourTurn;

    /**
     * An integer indicating how many objective cards the player has resolved at the end of a match
     */
    private int ObCardUsed = 0;

    /**
     * This attribute serves to implement the strategy pattern for calculatePoints.
     * We declare the attribute drawCard of type Model.CalculatePoints.CalculatePoints.
     */
    private CalculatePoints calculate;


    /**
     * Constructor for the class Player.
     * 
     * @param nickname is the name of the player
     * @param currentMatch is a reference to the id of the match disputed by the player
     * @param table is the table of the match
     */
    public Player(String nickname, int currentMatch, Table table) {
        this.nickname = nickname;
        this.hand = new ArrayList<>();
        this.objective = null;
        idMatch=currentMatch;
        this.points = 0; //A player has initially 0 points
        this.playArea = new PlayArea(5, 5, this);
        this.table = table;
    }


    /**
     * Empty constructor of the Player class, used for Jackson serialization.
     */
    public Player() {}


    /**
     * Getter method for the attribute nickname.
     * 
     * @return the attribute 'nickname'
     */
    public String getNickname() {
        return nickname;
    }


    /**
     * Setter method for the attribute 'nickname'
     * 
     * @param nickname is the String to be put
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }


    /**
     * Getter method for the attribute 'idMatch'
     * 
     * @return the ID uniquely identifying the match
     */
    public int getIdMatch() {
        return idMatch;
    }


    /**
     * Setter method for the attribute 'idMatch'
     *
     * @param idMatch ID uniquely identifying the match
     */
    public void setIdMatch(int idMatch) {
        this.idMatch = idMatch;
    }


    /**
     * Getter method for the hand attribute.
     * 
     * @return player's hand
     */
    public List<Card> getHand() {
        return hand;
    }


    /**
     * Setter method for the attribute 'hand'.
     * 
     * @param hand is the list of cards to be put
     */
    public void setHand(List<Card> hand) {
        this.hand = hand;
    }


    /**
     * Getter method for the attribute 'objective'
     * 
     * @return the attribute 'objective'
     */
    public ObjectiveCard getObjective() {
        return objective;
    }


    /**
     * Setter method for the attribute 'objective'
     * 
     * @param objective is the objective card to be put
     */
    public void setObjective(ObjectiveCard objective) {
        this.objective = objective;
    }


    /**
     * Getter method for the attribute 'points'
     * 
     * @return the attribute 'points'
     */
    public int getPoints() {
        return points;
    }


    /**
     * Setter method for the attribute 'points'
     * 
     * @param points is the integer to be put
     */
    public void setPoints(int points) {
        this.points = points;
    }


    /**
     * Getter method for the attribute 'playArea'
     * 
     * @return the attribute 'playArea'
     */
    public PlayArea getPlayArea() { return playArea; }


    /**
     * Setter method for the attribute 'playArea'
     * 
     * @param playArea is the playArea to be put
     */
    public void setPlayArea(PlayArea playArea) {
        this.playArea = playArea;
    }


    /**
     * getter method for the attribute 'table'
     * @return the attribute 'table'
     */
    public Table getTable() {
        return table;
    }


    /**
     * setter method for the attribute 'table'
     * @param table is the table to be put
     */
    public void setTable(Table table) {
        this.table = table;
    }


    /**
     * getter method for the attribute 'match'
     * @return the attribute 'match'
     */
    public MatchController getMatch() {
        return match;
    }


    /**
     * setter method for the attribute 'match'
     * @param match is the match to be put
     */
    public void setMatch(MatchController match) {
        this.match = match;
    }


    /**
     * getter method for the attribute 'chosenCard'
     * @return the attribute 'chosenCard'
     */
    public Card getChosenCard() {
        return chosenCard;
    }


    /**
     * setter method for the attribute 'chosenCard'
     * @param chosenCard is the card to be put
     */
    public void setChosenCard(Card chosenCard) {
        this.chosenCard = chosenCard;
    }


    /**
     * getter method for the attribute 'yourTurn'
     * @return the attribute 'yourTurn'
     */
    public boolean isYourTurn() {
        return yourTurn;
    }


    /**
     * setter method for the attribute 'yourTurn'
     * @param yourTurn is the boolean to be put
     */
    public void setYourTurn(boolean yourTurn) {
        this.yourTurn = yourTurn;
    }


    /**
     * getter method for the attribute 'obCardUsed'
     * @return the attribute 'obCardUsed'
     */
    public int getObCardUsed() {
        return ObCardUsed;
    }


    /**
     * setter method for the attribute 'obCardUsed'
     * @param obCardUsed is the integer to be put
     */
    public void setObCardUsed(int obCardUsed) {
        ObCardUsed = obCardUsed;
    }


    /**
     * getter method for the attribute 'obChoice2'
     * @return the attribute 'obChoice2'
     */
    public ObjectiveCard getObChoice2() {
        return obChoice2;
    }


    /**
     * setter method for the attribute 'obChoice2'
     * @param obChoice2 is the objective card to be put
     */
    public void setObChoice2(ObjectiveCard obChoice2) {
        this.obChoice2 = obChoice2;
    }


    /**
     * getter method for the attribute 'obChoice1'
     * @return the attribute 'obChoice1'
     */
    public ObjectiveCard getObChoice1() {
        return obChoice1;
    }


    /**
     * setter method for the attribute 'obChoice1'
     * @param obChoice1 is the objective card to be put
     */
    public void setObChoice1(ObjectiveCard obChoice1) {
        this.obChoice1 = obChoice1;
    }


    /**
     * getter method for the attribute 'blackPawn'
     * @return the attribute 'blackPawn'
     */
    public boolean isBlackPawn() {
        return blackPawn;
    }


    /**
     * setter method for the attribute 'blackPawn'
     * @param blackPawn is the boolean to be put
     */
    public void setBlackPawn(boolean blackPawn) {
        this.blackPawn = blackPawn;
    }


    /**
     * getter method for the attribute 'calculate'
     * @return the attribute 'calculate'
     */
    public CalculatePoints getCalculate() { return calculate; }


    /**
     * We set the value of the attribute calculate in order to use the strategy design pattern.
     * @param calculate is the value we assign to calculate to later choose the correct algorithm to calculate the points to add.
     */
    public void setCalculate(CalculatePoints calculate) { this.calculate = calculate; }


    /**
     * The method we use to place the card in the play area.
     * If the card is a resource card or if it is positioned by the back, and it is not a starter card, we place it and, eventually, extend the grid.
     * If the card is a gold card, we do the same, but only if the card's requirements are satisfied.
     * If the card is a started card, it will be placed in the centre of a 5x5 initial matrix.
     * In the last scenario, there is no need to extend the grid, nor to call make_covered_corners_false.
     * Finally, we also run the method calculatePoints to update the score of the current player.
     *
     * @param card_to_place is the card (chosen in chooseCard) which needs to be placed.
     * @param face indicated if the card needs to be placed by the front (true) or the back (false).
     * @param pos1 is a number indicating the first coordinate where the card will be placed.
     * @param pos2 is a number indicating the second coordinate where the card will be placed.
     */
    public void placeCard(Card card_to_place, boolean face, int pos1, int pos2) {
        //Preliminary action only for starter cards
        if(card_to_place.getCentre() != null && face == false){
            for(int i=0; i<card_to_place.getSymbols().length; i++){
                card_to_place.getSymbols()[i] = ((StarterCard) card_to_place).getDefaultSymbols()[i];
                card_to_place.getCorners()[i] = true;
            }
        }
        //If the card to be placed is a resource card or is turned upside down, but it is not a starting card,
        //then I place it normally, then see if I need to extend the matrix
        if((face == false || card_to_place.getPoints()<=1 && card_to_place.getPointsObject()==Symbols.EMPTY) && (card_to_place.getCentre()==null) ){
            getPlayArea().check_and_insert(card_to_place, face, pos1, pos2);
            getPlayArea().update_resources(card_to_place, face, pos1, pos2);
            getPlayArea().make_covered_corners_false(pos1, pos2);
            extend_matrix(pos1, pos2);
            getHand().remove(card_to_place);
            if(face == true){
                setPoints(getPoints() + card_to_place.getPoints());
                getTable().getScore().put(nickname, this.getPoints());
            }
        }
        //Same for the gold card if the requirements are met
        if(card_to_place.isFace() == true && (card_to_place.getPoints()>1 || (card_to_place.getPointsObject()!=Symbols.EMPTY))){
            if(getPlayArea().getAvailableResources().get(Symbols.ANIMAL) >= ((GoldCard) card_to_place).getRequirements().get(Symbols.ANIMAL) &&
                    getPlayArea().getAvailableResources().get(Symbols.FUNGI) >= ((GoldCard) card_to_place).getRequirements().get(Symbols.FUNGI) &&
                    getPlayArea().getAvailableResources().get(Symbols.INSECT) >= ((GoldCard) card_to_place).getRequirements().get(Symbols.INSECT) &&
                    getPlayArea().getAvailableResources().get(Symbols.PLANT) >= ((GoldCard) card_to_place).getRequirements().get(Symbols.PLANT)){
                getPlayArea().check_and_insert(card_to_place, face, pos1, pos2);
                getPlayArea().update_resources(card_to_place, face, pos1, pos2);
                getPlayArea().make_covered_corners_false(pos1, pos2);
                extend_matrix(pos1, pos2);
                getHand().remove(card_to_place);
                //Case if the gold card is not turned
                if(face == true && (card_to_place.getPoints() == 5 || card_to_place.getPoints() == 3)){
					setPoints(getPoints() + card_to_place.getPoints()); //Updates player's points
                    getTable().getScore().put(nickname, this.getPoints());
                }
                if(face == true && card_to_place.getPointsObject()!=Symbols.EMPTY){
                    setCalculate(new PointsPerObject());
                    calculate.calculatePoints(card_to_place, this); //Updates player's points
                    getTable().getScore().put(nickname, this.getPoints());
                }
                if(face == true && card_to_place.getPointsObject()==Symbols.EMPTY && card_to_place.getPoints() == 2){
                    setCalculate(new PointsByPosition());
                    calculate.calculatePoints(card_to_place, this); //Updates player's points
                    getTable().getScore().put(nickname, this.getPoints());
                }
            }
        }
        //If the card to be inserted is the initial one then the player's PlayArea matrix is still empty,
        //so it will be a 5x5 matrix; then I place the starting card in the center of the matrix;
        //obviously there is no need to extend the matrix and there is no need to make the surrounding angles false
        if(card_to_place.getCentre() != null){
            getPlayArea().getGrid()[2][2] = card_to_place;
            card_to_place.setRow(0);
            card_to_place.setColumn(0);
            card_to_place.setFace(face);
            getPlayArea().update_resources(card_to_place, face, 2, 2);
            getHand().remove(card_to_place);
        }
        //Adds the card to the array indicating cards placed on the area
        playArea.getOrderedCardList()[playArea.getCardsIndex()] = card_to_place;
        playArea.setCardsIndex(playArea.getCardsIndex()+1);
        notifyListener(GameStatus.VIEW_AREA, nickname, playArea, points);
    }


    /**
     * This method creates a new matrix (grid) if a card has been placed on a border of the current grid.
     * The old matrix is then strategically copied on the new one.
     *
     * @param pos1 is a number indicating the first coordinate where the card has been placed.
     * @param pos2 is a number indicating the second coordinate where the card has been placed.
     */
    public void extend_matrix(int pos1, int pos2){
        if(pos1 == playArea.getGrid().length - 2 ||
                pos1 == 1 ||
                pos2 == playArea.getGrid().length - 2 ||
                pos2 == 1){
            PlayArea new_matrix = new PlayArea(playArea.getGrid().length + 2, playArea.getGrid().length + 2, this);
            for(int i=0; i < playArea.getGrid().length; i++){
                for(int j=0; j < playArea.getGrid().length; j++){
                    new_matrix.getGrid()[i+1][j+1] = playArea.getGrid()[i][j];
                }
            }
            new_matrix.setOrderedCardList(playArea.getOrderedCardList());
            new_matrix.setCardsIndex(playArea.getCardsIndex());
            new_matrix.setAvailableResources(playArea.getAvailableResources());
            playArea = new_matrix;
        }
    }


    /**
     * The method we use to draw a card from the resource deck.
     * After the action, using passTurn, the yourTurn of the currently playing player is set to false,
     * whereas the yourTurn of the player next in the list is set to true.
     * Finally, we update all subscribed listeners.
     */
    public void drawFromDeckR() {
        if (!table.getDeckR().isEmpty()) {
            ResourceCard drawnCard = getTable().getDeckR().getFirst();
            table.getDeckR().removeFirst(); //If the deck is not empty, remove the card on top
            hand.add(drawnCard); //Add the card to the player's hand
            notifyListener(GameStatus.VIEW_HAND);
            passTurn();
        } else notifyListener(GameStatus.ACTION_FAILED);
    }


    /**
     * The method we use to draw a card from the gold deck.
     * After the action, using passTurn, the yourTurn of the currently playing player is set to false,
     * whereas the yourTurn of the player next in the list is set to true.
     * Finally, we update the subscribed listener.
     */
    public void drawFromDeckG() {
        if (!table.getDeckG().isEmpty()) {
            GoldCard drawnCard = table.getDeckG().getFirst();
            table.getDeckG().removeFirst(); //If the deck is not empty, remove the card on top
            hand.add(drawnCard); //Add the card to the player's hand
            notifyListener(GameStatus.VIEW_HAND);
            passTurn();
        } else notifyListener(GameStatus.ACTION_FAILED);
    }


    /**
     * The method we use to take a card from the table.
     * After the action, using passTurn, the yourTurn of the currently playing player is set to false,
     * whereas the yourTurn of the player next in the list is set to true.
     * Finally, we update the subscribed listener.
     *
     * @param selection indicates the card the player chose to take
     */
    public void takeFromTable(int selection) {
        Card drawnCard;
        switch (selection) {

            case 0:
                if (table.getCardsR()[0] == null) {
                    notifyListener(GameStatus.ACTION_FAILED);
                } else {
                    drawnCard = table.getCardsR()[0];
                    table.getCardsR()[0] = null;
                    if (!table.getDeckR().isEmpty()) { //If there are still cards in the deck...
                        table.getCardsR()[0] = table.getDeckR().getFirst(); //... replaces the card on the table
                        table.getDeckR().removeFirst();
                    }
                    hand.add(drawnCard); //Adds the card to the player's hand
                    notifyListener(GameStatus.VIEW_HAND);
                    passTurn();
                } break;

            case 1:
                if (table.getCardsR()[1] == null) {
                    notifyListener(GameStatus.ACTION_FAILED);
                } else {
                    drawnCard = table.getCardsR()[1];
                    table.getCardsR()[1] = null;
                    if (!table.getDeckR().isEmpty()) { //If there are still cards in the deck...
                        table.getCardsR()[1] = table.getDeckR().getFirst(); //... replaces the card on the table
                        table.getDeckR().removeFirst();
                    }
                    hand.add(drawnCard); //Adds the card to the player's hand
                    notifyListener(GameStatus.VIEW_HAND);
                    passTurn();
                } break;

            case 2:
                if (table.getCardsG()[0] == null) {
                    notifyListener(GameStatus.ACTION_FAILED);
                } else {
                    drawnCard = table.getCardsG()[0];
                    table.getCardsG()[0] = null;
                    if (!table.getDeckG().isEmpty()) { //If there are still cards in the deck...
                        table.getCardsG()[0] = table.getDeckG().getFirst(); //... replaces the card on the table
                        table.getDeckG().removeFirst();
                    }
                    hand.add(drawnCard); //Adds the card to the player's hand
                    notifyListener(GameStatus.VIEW_HAND);
                    passTurn();
                } break;

            case 3:
                if (table.getCardsG()[1] == null) {
                    notifyListener(GameStatus.ACTION_FAILED);
                } else {
                    drawnCard = table.getCardsG()[1];
                    table.getCardsG()[1] = null;
                    if (!table.getDeckG().isEmpty()) { //If there are still cards in the deck...
                        table.getCardsG()[1] = table.getDeckG().getFirst(); //... replaces the card on the table
                        table.getDeckG().removeFirst();
                    }
                    hand.add(drawnCard); //Adds the card to the player's hand
                    notifyListener(GameStatus.VIEW_HAND);
                    passTurn();
                } break;

            case 4: passTurn(); break;

            default:
                if (table.getCardsR()[0] == null) {
                    notifyListener(GameStatus.ACTION_FAILED);
                } else {
                    drawnCard = table.getCardsR()[0];
                    table.getCardsR()[0] = null;
                    if (!table.getDeckR().isEmpty()) { //If there are still cards in the deck...
                        table.getCardsR()[0] = table.getDeckR().getFirst(); //... replaces the card on the table
                        table.getDeckR().removeFirst();
                    }
                    hand.add(drawnCard); //Adds the card to the player's hand
                    notifyListener(GameStatus.VIEW_HAND);
                    passTurn();
                }
        }
    }


    /**
     * this method simply call the same method in the class PlayArea and notify the listener
     * @param cardSelected is a reference to the placed card.
     * @param faceSelected indicates if the card is going to be placed by the front (true) or by the back (false).
     * @param pos1 is a number indicating the first coordinate where the card has been placed.
     * @param pos2 is a number indicating the second coordinate where the card has been placed.
     */
    public void viewDifferenceOfResources(Card cardSelected, String faceSelected, int pos1, int pos2){
        getPlayArea().viewDifferenceOfResources(cardSelected, faceSelected, pos1, pos2);
        notifyListener(GameStatus.VIEW_DIFFERENCES_OF_RESOURCES);
    }


    /**
     * We set the current player's boolean yourTurn to false while setting the next player's yourTurn to true.
     */
    public void passTurn() {
        int currentPlayerIndex = -1;
        for (int i = 0; i < table.getPlayers().size(); i++) {
            if (table.getPlayers().get(i).isYourTurn()) {
                currentPlayerIndex = i;
                break;
            }
        }
        table.getPlayers().get(currentPlayerIndex).setYourTurn(false); //It is not this player's turn anymore
        int nextPlayerIndex = (currentPlayerIndex + 1) % getTable().getPlayers().size();
        table.getPlayers().get(nextPlayerIndex).setYourTurn(true); //The turn passes to the next player
        table.getPlayers().get(nextPlayerIndex).notifyListener(GameStatus.IT_IS_YOUR_TURN);
    }


    /**
     * The player receives on display the hand
     */
    public void viewHand() {
        if (hand.size() == 1)
            notifyListener(GameStatus.VIEW_STARTER_CARD);
        else notifyListener(GameStatus.VIEW_HAND);
    }


    /**
     * The player receives on display the play area and the hand
     */
    public void viewHandAndArea(){
        notifyListener(GameStatus.VIEW_HAND_AND_AREA);
    }


    /**
     * The player receives on display the play area.
     *
     * @param areaOwner is the nickname of the player whose area is going to be displayed
     */
    public void viewPlayArea(String areaOwner) {
        for (Player p : table.getPlayers())
            if (p.getNickname().equals(areaOwner))
                notifyListener(GameStatus.VIEW_AREA, p.getNickname(), p.getPlayArea(), p.getPoints());
    }


    /**
     * * The player receives on display his secret objective
     */
    public void viewSecretObjective() {
        notifyListener(GameStatus.VIEW_SECRET_OBJECTIVE);
    }


    /**
     * * The player receives on display the table during the first moments of the match
     */
    public void viewStartingTable() {
        notifyListener(GameStatus.VIEW_STARTING_TABLE);
    }


    /**
     * * The player receives on display the table during the first moments of the match
     */
    public void viewTable() {
        notifyListener(GameStatus.VIEW_TABLE);
    }


    /**
     * * The player receives on display his two options of objective cards
     */
    public void viewChoiceObjectives() {
        notifyListener(GameStatus.VIEW_CHOICE_OBJECTIVES);
    }


    /**
     * the attribute 'objective' is set based on the choice of the player
     * @param obSelected is the integer indicating the choice of the player
     */
    public void putChoiceObjectives(int obSelected){
        if(obSelected == 1){
            this.setObjective(this.getObChoice1());
        }
        if(obSelected == 2){
            this.setObjective(this.getObChoice2());
        }
    }


    /**
     * an easier method for placing of the starter card
     * @param card is the starter card to be placed
     * @param face is the face (front or back) of the card that will be placed
     */
    public void placeStarterCard(StarterCard card, boolean face) {
        playArea.getGrid()[2][2] = card;
        card.setFace(face);
        playArea.update_resources(card, face, 2, 2);
        hand.removeFirst();
        notifyListener(GameStatus.VIEW_AREA, nickname, playArea, points);
    }


    /**
     * the method that notify the listener only with the GameStatus
     * @param status indicates what to notify
     */
    @Override
    public void notifyListener(GameStatus status) {
            listener.onNotify(status, this);
    }

    /**
     * the method that notify the listener with the GameStatus, the areaOwner,
     * a reference to the playArea and the points of a player
     * @param status indicates what to notify
     * @param areaOwner is a reference to the player
     * @param playArea is a reference to the player's playArea
     * @param points is a reference to the player's points
     */
    @Override
    public void notifyListener(GameStatus status, String areaOwner, PlayArea playArea, int points) {
        listener.onNotify(status, this, areaOwner, playArea, points);
    }


    /**
     * Add a subscribed listener.
     *
     * @param listener the listener that just subscribed themselves
     */
    @Override
    public void addListener(Listener listener) {
        this.listener = listener;
    }
}
