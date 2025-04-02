package Controller;

import Listeners.GameStatus;
import Listeners.Listener;
import Model.Cards.*;
import Model.Player;
import Model.Table;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Controller managing a single match.
 */
public class MatchController implements Serializable {

	/**
	 * A number which uniquely identifies the match.
	 */
	private int matchID;

	/**
	 * Indicates the number of players chosen by the player who created the match.
	 */
	private int numOfPlayers;

	/**
	 * Indicates the number of starter cards placed during the preparatory phase of the match.
	 */
	private int numOfStarterCardsPlaced = 0;

	/**
	 * Indicates if the match is still starting.
	 */
	private boolean starting;

	/**
	 * A reference to a player currently performing an action.
	 */
	private Player currentPlayer;

	/**
	 * A reference to the table where a match is currently taking place.
	 */
	private Table table;

	/**
	 * Indicates the phase currently playing in the match.
	 */
	private Phase phase;

	/**
	 * Indicates the colors currently available for a player to choose.
	 */
	private Colors[] availableColors = new Colors[4];

	/**
	 * Indicates if the black pawn has already been assigned.
	 */
	private boolean blackPawnChosen = false;
	/**
	 * Indicates the nickname of the player owning the black pawn.
	 */
	private String nameOfThePlayerWithTheBlackPawn;

	/**
	 * Indicates the number of objective cards already chosen during the preparatory phase of the match.
	 */
	private int numOfObjectiveCardChosen = 0;

	/**
	 * Indicates how many players have finished to play in the final stage of the match.
	 */
	private int numOfPlayersThatHaveFinishedToPlay = 0;

	/**
	 * Indicates how many players received their final score.
	 */
	private int numOfObjectiveCardCalculated = 0;

	/**
	 * Indicates if the match is in the last round.
	 */
	private boolean lastRound = false;

	/**
	 * Indicates if the match is in the second to last round.
	 */
	private boolean secondToLastRound = false;

	/**
	 * An array of players who won the match.
	 */
	private Player[] winners = new Player[4];


	/**
	 * Constructor for the match controller.
	 *
	 * @param matchID is the ID uniquely identifying the match
	 * @param numOfPlayers is the number of players chosen by the creator of the match
	 * @param table is a reference to the table
	 */
	public MatchController(int matchID, int numOfPlayers, Table table) {
		this.matchID = matchID;
		this.numOfPlayers = numOfPlayers;
		this. starting = true;
		this.table = table;
		availableColors[0] = Colors.GREEN;
		availableColors[1] = Colors.YELLOW;
		availableColors[2] = Colors.RED;
		availableColors[3] = Colors.BLUE;
		this.phase = new PlacingPhase(this);
	}


	/**
	 * Empty constructor of the MatchController class, used for Jackson serialization.
	 */
	public MatchController() {}


	/**
	 * Getter for the matchID attribute.
	 *
	 * @return the ID uniquely identifying the match
	 */
	public int getMatchID() {
		return matchID;
	}


	/**
	 * Setter method for the matchID attribute.
	 *
	 * @param matchID the ID uniquely identifying the match
	 */
	public void setMatchID(int matchID) {
		this.matchID = matchID;
	}


	/**
	 * Getter method for the numOfPlayers attribute.
	 *
	 * @return the number of players chosen by the creator of the match
	 */
	public int getNumOfPlayers() {
		return numOfPlayers;
	}


	/**
	 * Setter method for the starting attribute.
	 *
	 * @param numOfPlayers the chosen number of players for this match
	 */
	public void setNumOfPlayers(int numOfPlayers) {
		this.numOfPlayers = numOfPlayers;
	}


	/**
	 * Getter method for the starting attribute.
	 *
	 * @return true if the match is still starting
	 */
	public boolean isStarting() {
		return starting;
	}


	/**
	 * Setter method for the starting attribute.
	 *
	 * @param starting is true if the match is still starting
	 */
	public void setStarting(boolean starting) {
		this.starting = starting;
	}


	/**
	 * Getter method for the currentPlayer attribute.
	 *
	 * @return the player currently in exam
	 */
	public Player getCurrentPlayer() {
		return currentPlayer;
	}


	/**
	 * Setter method for the currentPlayer attribute.
	 *
	 * @param currentPlayer is the player currently in exam
	 */
	public void setCurrentPlayer(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
	}


	/**
	 * Getter method for the blackPawnChosen attribute.
	 *
	 * @return true if the black pawn has already been assigned
	 */
	public boolean isBlackPawnChosen() {
		return blackPawnChosen;
	}


	/**
	 * Setter method for the blackPawnChosen attribute.
	 *
	 * @param blackPawnChosen is true if the black pawn has already been assigned
	 */
	public void setBlackPawnChosen(boolean blackPawnChosen) {
		this.blackPawnChosen = blackPawnChosen;
	}


	/**
	 * Getter method for the table attribute.
	 *
	 * @return the table where the match is taking place
	 */
	public Table getTable() {
		return table;
	}


	/**
	 * Setter method for the table attribute.
	 *
	 * @param table is the table where the match is taking place
	 */
	public void setTable(Table table) {
		this.table = table;
	}


	/**
	 * Getter method for the numOfStarterCardsPlaced attribute.
	 *
	 * @return how many starter cards have been placed
	 */
	public int getNumOfStarterCardsPlaced() {
		return numOfStarterCardsPlaced;
	}


	/**
	 * Setter method for the numOfStarterCardsPlaced attribute.
	 *
	 * @param numOfStarterCardsPlaced indicated how many players placed their starter cards
	 */
	public void setNumOfStarterCardsPlaced(int numOfStarterCardsPlaced) {
		this.numOfStarterCardsPlaced = numOfStarterCardsPlaced;
	}


	/**
	 * Getter method for the availableColors attribute.
	 *
	 * @return an array of all the colors the player can choose from
	 */
	public Colors[] getAvailableColors() {
		return availableColors;
	}


	/**
	 * Setter method for the availableColors attribute.
	 *
	 * @param availbleColors are the available colors, which have not yet been chosen
	 */
	public void setAvailableColors(Colors[] availbleColors) {
		this.availableColors = availbleColors;
	}


	/**
	 * Getter method for the numOfObjectiveCardChosen attribute.
	 *
	 * @return how many players in this match have chosen their secret objective
	 */
	public int getNumOfObjectiveCardChosen() {
		return numOfObjectiveCardChosen;
	}


	/**
	 * Setter method for the numOfObjectiveCardChosen attribute.
	 *
	 * @param numOfObjectiveCardChosen indicates how many players in this match have chosen their secret objective
	 */
	public void setNumOfObjectiveCardChosen(int numOfObjectiveCardChosen) {
		this.numOfObjectiveCardChosen = numOfObjectiveCardChosen;
	}


	/**
	 * Setter method for the lastRound attribute.
	 *
	 * @param value is true if the match is in its last round
	 */
	public void setLastRound(boolean value){
		this.lastRound = value;
	}


	/**
	 * Getter method for the lastRound attribute.
	 *
	 * @return true if the match is in its last round
	 */
	public boolean isLastRound() {
		return lastRound;
	}


	/**
	 * Getter method for the secondToLastRound attribute.
	 *
	 * @return true if the match is in its second to last round
	 */
	public boolean isSecondToLastRound() {
		return secondToLastRound;
	}


	/**
	 * Setter method for the secondToLastRound attribute.
	 *
	 * @param secondToLastRound is true if the match is in its second to last round
	 */
	public void setSecondToLastRound(boolean secondToLastRound) {
		this.secondToLastRound = secondToLastRound;
	}


	/**
	 * Getter method for the numOfPlayersThatHaveFinishedToPlay attribute.
	 *
	 * @return how many players have finished to play
	 */
	public int getNumOfPlayersThatHaveFinishedToPlay() {
		return numOfPlayersThatHaveFinishedToPlay;
	}


	/**
	 * Setter method for the numOfPlayersThatHaveFinishedToPlay attribute.
	 *
	 * @param numOfPlayersThatHaveFinishedToPlay is true if the match is in its second to last round
	 */
	public void setNumOfPlayersThatHaveFinishedToPlay(int numOfPlayersThatHaveFinishedToPlay) {
		this.numOfPlayersThatHaveFinishedToPlay = numOfPlayersThatHaveFinishedToPlay;
	}


	/**
	 * Getter method for the numOfObjectiveCardCalculated attribute.
	 *
	 * @return how many players have their final scores calculated
	 */
	public int getNumOfObjectiveCardCalculated() {
		return numOfObjectiveCardCalculated;
	}


	/**
	 * Setter method for the numOfPlayersThatHaveFinishedToPlay attribute.
	 *
	 * @param numOfObjectiveCardCalculated indicates how many players have their final scores calculated
	 */
	public void setNumOfObjectiveCardCalculated(int numOfObjectiveCardCalculated) {
		this.numOfObjectiveCardCalculated = numOfObjectiveCardCalculated;
	}


	/**
	 * Sets the phase of the game.
	 *
	 * @return returns the current phase
	 */
	public Phase getPhase() {
		return phase;
	}


	/**
	 * Sets the phase of the game.
	 *
	 * @param newPhase is the phase the game changes to
	 */
	public void setPhase(Phase newPhase) {
		phase = newPhase;
	}


	/**
	 * Getter method for the winners attribute.
	 *
	 * @return the players who won this match
	 */
	public Player[] getWinners() {
		return winners;
	}


	/**
	 * Search for a player in this match with a specified nickname.
	 *
	 * @param name refers to the nickname we have to search for
	 * @return the player bearing the specified nickname
	 */
	public Player getPlayerByName(String name) {
		Player player = null;
		for (Player p : table.getPlayers())
			if (p.getNickname().equals(name))
				player = p;
		return player;
	}


	/**
	 * This method search for the nickname of every player in the current game
	 * @return a list containing all players' nicknames
	 */
	public List<String> getPlayersNames(){
		List<String> nicknames = new ArrayList<>();
		for(Player player : table.getPlayers()){
			nicknames.add(player.getNickname());
		}
		return nicknames;
	}


	/**
	 * Method used for the tests in MatchControllerTest.
	 * Shuffles the decks on the table and gives 2 resource cards and 1 gold card to each player.
	 */
	public void prepareTable() {
		table.shuffle_resource_deck();
		ResourceCard [] cardsR = new ResourceCard[2];
		cardsR[0] = table.getDeckR().getFirst();
		table.getDeckR().removeFirst();
		cardsR[1] = table.getDeckR().getFirst();
		table.getDeckR().removeFirst();
		table.setCardsR(cardsR);
		//faccio la stessa cosa per le carte oro
		table.shuffle_golden_deck();
		GoldCard [] cardsG = new GoldCard[2];
		cardsG[0] = table.getDeckG().getFirst();
		table.getDeckG().removeFirst();
		cardsG[1] = table.getDeckG().getFirst();
		table.getDeckG().removeFirst();
		table.setCardsG(cardsG);
		//mescolo il mazzo di carte iniziali e ne distribuisco una a ciascuno giocatore; ciascuno poi sceglie
		//il verso in cui posizionare la carta iniziale e la mette al centro della sua matrice
		table.shuffle_starter_deck();
		int i = 0;
		do{
			boolean chosen_face = true;
			setCurrentPlayer(table.getPlayers().get(i++));
			currentPlayer.placeCard(table.getDeckS().getFirst(), chosen_face, 0, 0); //la carta iniziale viene inserita sempre in posizione 2 2 quindi metto di default 2 zeri
			table.getDeckS().removeFirst();
		}while (!this.getCurrentPlayer().equals(table.getPlayers().getLast()));
		//adesso ogni giocatore pesca 2 carte risorsa e 1 carta oro
		i = 0;
		setCurrentPlayer(table.getPlayers().getFirst());
		do{
			setCurrentPlayer(table.getPlayers().get(i++));
			getCurrentPlayer().getHand().add(table.getDeckR().getFirst());
			table.getDeckR().removeFirst();
			getCurrentPlayer().getHand().add(table.getDeckR().getFirst());
			table.getDeckR().removeFirst();
			getCurrentPlayer().getHand().add(table.getDeckG().getFirst());
			table.getDeckG().removeFirst();
		}while (!getCurrentPlayer().equals(table.getPlayers().getLast()));
		//adesso mescolo il mazzo di carte obiettivo e ne posiziono 2 scoperte sul tavolo
		table.shuffle_starter_deck();
		ObjectiveCard [] cardsO = new ObjectiveCard[2];
		cardsO[0] = table.getDeckO().getFirst();
		table.getDeckO().removeFirst();
		cardsO[1] = table.getDeckO().getFirst();
		table.getDeckO().removeFirst();
		table.setCardsO(cardsO);
		cardsO[0].setSecret(false);
		cardsO[1].setSecret(false);
		//adesso ogni giocatore riceve 2 carte obiettivo e ne sceglie una che diventa
		//la sua carta obiettivo segreta; per farlo creo due oggetti (Card1 e Card2) tra
		//i quali il giocatore può scegliere
		i = 0;
		this.setCurrentPlayer(table.getPlayers().getFirst());
		do {
			setCurrentPlayer(table.getPlayers().get(i++));
			ObjectiveCard Card1 = table.getDeckO().getFirst();
			table.getDeckO().removeFirst();
			ObjectiveCard Card2 = table.getDeckO().getFirst();
			table.getDeckO().removeFirst();
			ObjectiveCard selected_one = Card1;
			getCurrentPlayer().setObjective(selected_one);
		} while (!getCurrentPlayer().equals(table.getPlayers().getLast()));
	}


	/**
	 * Calls the respective method present in Player.
	 * Verifies if it is the turn of a specified player.
	 *
	 * @param playerName is the nickname of the player
	 * @return true if it is the player's turn
	 */
	public boolean isYourTurn(String playerName) {
		return getPlayerByName(playerName).isYourTurn();
	}


	/**
	 * Calls the respective method present in Player.
	 * Lets the player place a card on their play area.
	 *
	 * @param playerName is the name of the player who launched the command
	 * @param cardNumber references to the index of the card which has been selected
	 * @param cardFace indicates of the card needs to be placed by the front or by the back
	 * @param row is the first coordinate of the position where the player wants to place the card
	 * @param column is the second coordinate of the position where the player wants to place the card
	 */
	public void place(String playerName, int cardNumber, String cardFace, int row, int column) {
		boolean face;
		if (cardFace.equalsIgnoreCase("front")) face = true;
		else face = false;
		Player p = getPlayerByName(playerName);
		p.placeCard(p.getHand().get(cardNumber-1), face, row, column);
	}


	/**
	 * Lets the player draw a card from the resource deck.
	 *
	 * @param playerName is the name of the player who wants to play the action
	 */
	public void drawResource(String playerName) {
		getPlayerByName(playerName).drawFromDeckR();
	}


	/**
	 * Calls the respective method present in Player.
	 * Lets the player draw a card from the gold deck.
	 *
	 * @param playerName is the name of the player who wants to play the action
	 */
	public void drawGold(String playerName) {
		getPlayerByName(playerName).drawFromDeckG();
	}


	/**
	 * Lets the player draw a card from the table.
	 *
	 * @param playerName is the name of the player who wants to play the action
	 */
	public void drawTable(String playerName, int selection) {
		getPlayerByName(playerName).takeFromTable(selection-1);
	}


	/**
	 * Notifies if some of the decks are empty.
	 *
	 * @return 0 if everything is fine
	 *         1 if the Resource deck is empty
	 *         2 if the Gold deck is empty
	 *         3 if both decks are empty
	 *         4 if everything is empty
	 */
	public int whereCanIDraw() {
		int value = 0;
		if(getTable().getDeckR().isEmpty()){
			value = 1;
		}
		if(getTable().getDeckG().isEmpty()){
			value = 2;
		}
		if(getTable().getDeckR().isEmpty() && getTable().getDeckG().isEmpty()){
			value = 3;
			if(getTable().getCardsR()[0] == null && getTable().getCardsR()[1] == null && getTable().getCardsG()[0] == null && getTable().getCardsG()[1] == null){
				value = 4;
			}
		}
		return value;
	}


	/**
	 * Shows which positions on the table of this match have cards on them.
	 *
	 * @return a list with all the positions of the table with a card on them
	 */
	public List<Integer> availablePositionsForDrawing() {
		List<Integer> result = new ArrayList<>();
		if(getTable().getCardsR()[0] != null)	result.add(1);
		if(getTable().getCardsR()[1] != null)	result.add(2);
		if(getTable().getCardsG()[0] != null)	result.add(3);
		if(getTable().getCardsG()[1] != null)	result.add(4);
		return result;
	}


	/**
	 * Calls the respective method present in Player.
	 * Displays to the player their hand.
	 *
	 * @param playerName is the name of the player who made the request
	 */
	public void viewHand(String playerName) {
		getPlayerByName(playerName).viewHand();
	}


	/**
	 * Calls the respective method present in Player.
	 * Displays to the player their hand and play area.
	 *
	 * @param playerName is the name of the player who made the request
	 */
	public void viewHandAndArea(String playerName) {
		getPlayerByName(playerName).viewHandAndArea();
	}


	/**
	 * Calls the respective method present in Player.
	 * Displays to the player the starting table.
	 *
	 * @param playerName is the name of the player who made the request
     */
	public void viewStartingTable(String playerName) {
		getPlayerByName(playerName).viewStartingTable();
	}


	/**
	 * Calls the respective method present in Player.
	 * Displays to the player the table.
	 *
	 * @param playerName is the name of the player who made the request
     */
	public void viewTable(String playerName) {
		getPlayerByName(playerName).viewTable();
	}


	/**
	 * Calls the respective method present in Player.
	 * Displays to the player their 2 options for their secret objectives.
	 *
	 * @param playerName is the name of the player who made the request
	 */
	public void viewChoiceObjectives(String playerName) {
		getPlayerByName(playerName).viewChoiceObjectives();
	}


	/**
	 * Calls the respective method present in Player.
	 * Puts the chosen card on the table.
	 *
	 * @param playerName is the name of the player who made the request
	 * @param obSelected indicates which option the client selected
	 */
	public void putChoiceObjectives(String playerName, int obSelected) {
		getPlayerByName(playerName).putChoiceObjectives(obSelected);
	}


	/**
	 * Calls the respective method present in Player.
	 * Place the starter card in the center of the play area.
	 *
	 * @param playerName is the name of the player who made the request
	 * @param starterFace indicates the selected face of the card
	 */
	public void placeStarterCard(String playerName, String starterFace) {
		boolean face;
		if (starterFace.equalsIgnoreCase("front")) face = true;
		else face = false;
		Player p = getPlayerByName(playerName);
		StarterCard card = (StarterCard) p.getHand().getFirst();
		p.placeStarterCard(card, face);
	}


	/**
	 * Calculates the points gained by the specified player from the common and the secret objectives.
	 *
	 * @param playerName is the nickname of the client/player
	 */
	public void calculateObjectives(String playerName) {
		Player p = getPlayerByName(playerName);
		int actualPoints = p.getPoints();
		if (getTable().getCardsO()[0].getClass() == ObCardSymbols.class) {
			((ObCardSymbols) getTable().getCardsO()[0]).calculateObCardSymbols(((ObCardSymbols) getTable().getCardsO()[0]), p);
			if (actualPoints != p.getPoints()) {
				p.setObCardUsed(p.getObCardUsed() + 1);
			}
			actualPoints = p.getPoints();
		}
		if (getTable().getCardsO()[0].getClass() == ObCardPosition.class) {
			((ObCardPosition) getTable().getCardsO()[0]).calculateObCardPosition(((ObCardPosition) getTable().getCardsO()[0]), p);
			if (actualPoints != p.getPoints()) {
				p.setObCardUsed(p.getObCardUsed() + 1);
			}
			actualPoints = p.getPoints();
		}
		if (getTable().getCardsO()[1].getClass() == ObCardSymbols.class) {
			((ObCardSymbols) getTable().getCardsO()[1]).calculateObCardSymbols(((ObCardSymbols) getTable().getCardsO()[1]), p);
			if (actualPoints != p.getPoints()) {
				p.setObCardUsed(p.getObCardUsed() + 1);
			}
			actualPoints = p.getPoints();
		}
		if (getTable().getCardsO()[1].getClass() == ObCardPosition.class) {
			((ObCardPosition) getTable().getCardsO()[1]).calculateObCardPosition(((ObCardPosition) getTable().getCardsO()[1]), p);
			if (actualPoints != p.getPoints()) {
				p.setObCardUsed(p.getObCardUsed() + 1);
			}
			actualPoints = p.getPoints();
		}
		if (p.getObjective().getClass() == ObCardSymbols.class) {
			((ObCardSymbols) p.getObjective()).calculateObCardSymbols(((ObCardSymbols) p.getObjective()), p);
			if (actualPoints != p.getPoints()) {
				p.setObCardUsed(p.getObCardUsed() + 1);
			}
			actualPoints = p.getPoints();
		}
		if (p.getObjective().getClass() == ObCardPosition.class) {
			((ObCardPosition) p.getObjective()).calculateObCardPosition(((ObCardPosition) p.getObjective()), p);
			if (actualPoints != p.getPoints()) {
				p.setObCardUsed(p.getObCardUsed() + 1);
			}
		}
		p.notifyListener(GameStatus.SHOW_POINTS);
	}


	/**
	 * This method shuffles the decks on the table and gives a starter card to each player
	 */
	public void shuffleDecksAndGiveStarterCards(){
		//Shuffles the resource deck and places the 2 first resource cards on the table
		table.shuffle_resource_deck();
		ResourceCard [] cardsR = new ResourceCard[2];
		cardsR[0] = table.getDeckR().getFirst();
		table.getDeckR().removeFirst();
		cardsR[1] = table.getDeckR().getFirst();
		table.getDeckR().removeFirst();
		table.setCardsR(cardsR);

		//Shuffles the gold deck and places the 2 first gold cards on the table
		table.shuffle_golden_deck();
		GoldCard [] cardsG = new GoldCard[2];
		cardsG[0] = table.getDeckG().getFirst();
		table.getDeckG().removeFirst();
		cardsG[1] = table.getDeckG().getFirst();
		table.getDeckG().removeFirst();
		table.setCardsG(cardsG);

		//Shuffles the starter deck and gives a starter card to every player
		table.shuffle_starter_deck();
		int i = 0;
		do {
			setCurrentPlayer(table.getPlayers().get(i++));
			currentPlayer.getHand().add(table.getDeckS().getFirst());
			table.getDeckS().removeFirst();
		} while (!this.getCurrentPlayer().equals(table.getPlayers().getLast()));
	}


	/**
	 * With this method every player draws 3 cards and the 2 common objective cards are placed.
	 */
	public void drawCardsAndPlaceCommonObjectives() {
		int i = 0;
		setCurrentPlayer(table.getPlayers().getFirst());
		do {
			setCurrentPlayer(table.getPlayers().get(i++));
			getCurrentPlayer().getHand().add(table.getDeckR().getFirst());
			table.getDeckR().removeFirst();
			getCurrentPlayer().getHand().add(table.getDeckR().getFirst());
			table.getDeckR().removeFirst();
			getCurrentPlayer().getHand().add(table.getDeckG().getFirst());
			table.getDeckG().removeFirst();
		} while (!getCurrentPlayer().equals(table.getPlayers().getLast()));

		//Shuffles the objective deck and places 2 objective cards on the table
		table.shuffle_objective_deck();
		ObjectiveCard [] cardsO = new ObjectiveCard[2];
		cardsO[0] = table.getDeckO().getFirst();
		table.getDeckO().removeFirst();
		cardsO[1] = table.getDeckO().getFirst();
		table.getDeckO().removeFirst();
		table.setCardsO(cardsO);
		cardsO[0].setSecret(false);
		cardsO[1].setSecret(false);
	}


	/**
	 * Place the 2 common objective cards on the table.
	 */
	public void giveObjectives() {
		int i = 0;
		currentPlayer = table.getPlayers().getFirst();
		do {
			currentPlayer = table.getPlayers().get(i++);
			currentPlayer.setObChoice1(table.getDeckO().getFirst());
			currentPlayer.getObChoice1().setSecret(true);
			table.getDeckO().removeFirst();
			currentPlayer.setObChoice2(table.getDeckO().getFirst());
			currentPlayer.getObChoice2().setSecret(true);
			table.getDeckO().removeFirst();
		} while (!getCurrentPlayer().equals(table.getPlayers().getLast()));
	}


	/**
	 * Sets the specified player's turn as true and every other player's turn to false.
	 *
	 * @param playerName is the nickname of the specified player
	 */
	public void itIsMyTurn(String playerName){
		for (Player p : getTable().getPlayers()) {
			if (p.getNickname().equals(playerName)) p.setYourTurn(true);
			else p.setYourTurn(false);
		}
	}


	/**
	 * Calls the respective method present in MatchController.
	 * Verifies if it is the turn of client who made the request to choose the color of their pawn.
	 *
	 * @param playerName is the nickname of the player who is making the request
	 * @return true if it is the turn of the player to choose the color
	 */
	public boolean canIChooseTheColor(String playerName){
		boolean k = false;
		for (Player p : getTable().getPlayers()) {
			if (p.getNickname().equals(playerName))
				k = p.isYourTurn();
		}
		return k;
	}


	/**
	 * Checks if the color the client selected is valid.
	 *
	 * @param colorChosen is the color the client selected
	 * @return true if the client entered a valid color
	 */
	public boolean checkColor(Colors colorChosen){
		boolean colorValid = false;
        for (Colors availableColor : availableColors) {
            if (availableColor != null && availableColor.equals(colorChosen)) {
                    colorValid = true;
            }
        }
		return colorValid;
	}


	/**
	 * Removes the color from the array of the available colors and lets the next player choose their color.
	 *
	 * @param playerName is the nickname of the player who is making the request
	 * @param colorChosen is the color the client selected
	 */
	public void removeColorAndPassTurn(String playerName, Colors colorChosen){
		for(int i=0; i<availableColors.length; i++){
			if(availableColors[i] != null){
				if(availableColors[i].equals(colorChosen)){
					availableColors[i] = null;
				}
			}
		}
		for (Player p : getTable().getPlayers()) {
			if (p.getNickname().equals(playerName)){
				int currentPlayerIndex = -1;
				for (int i = 0; i < table.getPlayers().size(); i++) {
					if (table.getPlayers().get(i).isYourTurn()) {
						currentPlayerIndex = i;
						break;
					}
				}
				table.getPlayers().get(currentPlayerIndex).setYourTurn(false); //Non è più il turno di questo giocatore
				int nextPlayerIndex = (currentPlayerIndex + 1) % getTable().getPlayers().size();
				table.getPlayers().get(nextPlayerIndex).setYourTurn(true); //Il turno passa al giocatore successivo
			}
		}
	}


	/**
	 * Randomly assigns the black pawn.
	 */
	public void chooseTheBlackPawn(){
		int k = new Random().nextInt(numOfPlayers);
		this.getTable().getPlayers().get(k).setBlackPawn(true);
		this.setNameOfThePlayerWithTheBlackPawn(this.getTable().getPlayers().get(k).getNickname());
	}


	/**
	 * Getter for the nameOfThePlayerWithTheBlackPawn attribute.
	 *
	 * @return the nickname of the first player of the match, the one owning the black pawn
	 */
	public String getNameOfThePlayerWithTheBlackPawn() {
		return nameOfThePlayerWithTheBlackPawn;
	}


	/**
	 * Setter for the nameOfThePlayerWithTheBlackPawn attribute.
	 *
	 * @param nameOfThePlayerWithTheBlackPawn indicates which player has been assigned the black pawn
	 */
	public void setNameOfThePlayerWithTheBlackPawn(String nameOfThePlayerWithTheBlackPawn) {
		this.nameOfThePlayerWithTheBlackPawn = nameOfThePlayerWithTheBlackPawn;
	}


	/**
	 * Calls the respective setter method for the specified player.
	 *
	 * @param playerName is the nickname of the specified player
	 * @param value is the boolean the yourTurn attribute is set to
	 */
	public void setYourTurn(String playerName, boolean value){
		getPlayerByName(playerName).setYourTurn(value);
	}


	/**
	 * Calls the respective method on Player.
	 * The player with the specified nickname receives on display the play area.
	 *
	 * @param playerName is the nickname of the player who is requesting to view the area
	 * @param areaOwner is the nickname of the player whose area is going to be displayed
	 */
	public void viewPlayArea(String playerName, String areaOwner) {
		getPlayerByName(playerName).viewPlayArea(areaOwner);
	}


	/**
	 * Calls the respective method on Player.
	 * The player with the specified nickname receive on display their secret objective card.
	 *
	 * @param playerName is the nickname of the player who is requesting to view the card
	 */
	public void viewSecretObjective(String playerName) {
		getPlayerByName(playerName).viewSecretObjective();
	}


	/**
	 * Verifies if the specified coordinates allow the player to place a card.
	 *
	 * @param playerName is the nickname of the layer who is requesting to place the card
	 * @param x        is the first coordinate (row) selected by the player
	 * @param y        is the first coordinate (row) selected by the player
	 * @return true if a card can be placed on the specified coordinates, false otherwise
	 */
	public boolean areCoordinatesValid(String playerName, int x, int y){
		boolean valid;
		Player p = getPlayerByName(playerName);
		try {valid = checkCoordinates(p.getPlayArea().getGrid(), x, y);}
		catch (IndexOutOfBoundsException | NullPointerException e) {valid = false;}
		return valid;
	}


	/**
	 * Checks the validity of the coordinates.
	 *
	 * @param grid is the grid of cards present on the play area
	 * @param pos1 is the first coordinate (row) selected by the player
	 * @param pos2 is the second coordinate (column) selected by the player
	 * @return true if the coordinates are valid
	 * @throws IndexOutOfBoundsException if the coordinates exceed the boundaries of the grid
	 * @throws NullPointerException if the coordinates point to a null cell
	 */
	public boolean checkCoordinates(Card[][] grid, int pos1, int pos2) throws IndexOutOfBoundsException, NullPointerException {
		boolean can_I_continue = false;
		if ((grid[pos1 + 1][pos2 + 1] != null &&
				grid[pos1 + 1][pos2 + 1].getCorners()[0] == true)
				|| (grid[pos1 + 1][pos2 - 1] != null &&
				grid[pos1 + 1][pos2 - 1].getCorners()[1] == true)
				|| (grid[pos1 - 1][pos2 + 1] != null &&
				grid[pos1 - 1][pos2 + 1].getCorners()[2] == true)
				|| (grid[pos1 - 1][pos2 - 1] != null &&
				grid[pos1 - 1][pos2 - 1].getCorners()[3] == true)) {
			can_I_continue = true;
		}
		if (can_I_continue) {
			if ((grid[pos1 + 1][pos2 + 1] != null &&
					(grid[pos1 + 1][pos2 + 1].getCorners()[0] == false ||
							grid[pos1 + 1][pos2 + 1].getSymbols()[0] == Symbols.NOCORNER))
					|| (grid[pos1 + 1][pos2 - 1] != null &&
					(grid[pos1 + 1][pos2 - 1].getCorners()[1] == false ||
							grid[pos1 + 1][pos2 - 1].getSymbols()[1] == Symbols.NOCORNER))
					|| (grid[pos1 - 1][pos2 + 1] != null &&
					(grid[pos1 - 1][pos2 + 1].getCorners()[2] == false ||
							grid[pos1 - 1][pos2 + 1].getSymbols()[2] == Symbols.NOCORNER))
					|| (grid[pos1 - 1][pos2 - 1] != null &&
					(grid[pos1 - 1][pos2 - 1].getCorners()[3] == false ||
							grid[pos1 - 1][pos2 - 1].getSymbols()[3] == Symbols.NOCORNER))) {
				can_I_continue = false;
			}
		}
		return can_I_continue;
	}


	/**
	 * Checks if the requirements to place a gold card are met.
	 *
	 * @param playerName is the nickname of the layer who is requesting to place the gold card
	 * @param cardSelected indicates the position of the gold card in the player's hand
	 * @return true if the requirements are met, false otherwise
	 */
	public boolean canIPlaceTheGoldCard(String playerName, int cardSelected){
		boolean k = false;
		Player p = getPlayerByName(playerName);
		Map<Symbols, Integer> a = p.getPlayArea().getAvailableResources();
		if (p.getHand().get(cardSelected-1).getClass() == ResourceCard.class) {
			k= true;
		} else {
			if (p.getPlayArea().getAvailableResources().get(Symbols.ANIMAL) >= ((GoldCard) p.getHand().get(cardSelected-1)).getRequirements().get(Symbols.ANIMAL) &&
					p.getPlayArea().getAvailableResources().get(Symbols.FUNGI) >= ((GoldCard) p.getHand().get(cardSelected-1)).getRequirements().get(Symbols.FUNGI) &&
					p.getPlayArea().getAvailableResources().get(Symbols.INSECT) >= ((GoldCard) p.getHand().get(cardSelected-1)).getRequirements().get(Symbols.INSECT) &&
					p.getPlayArea().getAvailableResources().get(Symbols.PLANT) >= ((GoldCard) p.getHand().get(cardSelected-1)).getRequirements().get(Symbols.PLANT)) {
				k = true;
			}
		}
		return k;
	}


	/**
	 * Calls the respective method present in Player.
	 * Show to a player how his resources will change after the placement of a card.
	 *
	 * @param playerName is the nickname of the player who is placing the card
	 * @param cardSelected indicates the position of the card in the player's hand
	 * @param faceSelected indicates the face of the card that the player has chosen
	 * @param pos1 is the row where the card will be placed
	 * @param pos2 is the column where the card will be placed
	 */
	public void viewDifferenceOfResources(String playerName, int cardSelected, String faceSelected, int pos1, int pos2){
		Player p = getPlayerByName(playerName);
		p.viewDifferenceOfResources(p.getHand().get(cardSelected-1), faceSelected, pos1, pos2);
	}


	/**
	 * Checks wether this is the last or the second to last turn of the specified player.
	 *
	 * @param playerName is the nickname of the specified player
	 * @return 0 if conditions for the ending phase are not met
	 * 		   1 if conditions for the ending phase are met and the next player does not have the black pawn
	 * 		   2 if conditions for the ending phase are met and the next player does have the black pawn
	 */
	public int checkSecondToLastTurn(String playerName){
		int k = 0;
		Player p = getPlayerByName(playerName);
		if (p.getPoints() >= 20 || (getTable().getDeckR().isEmpty() && getTable().getDeckG().isEmpty())){
			if(getTable().getPlayers().get((getTable().getPlayers().indexOf(p) + 1) % getTable().getPlayers().size()).isBlackPawn() == false)
				k=1;
			else k=2;
		}
		return k;
	}


	/**
	 * Verify if the next player is the first player.
	 *
	 * @param playerName is the nickname of the client/player
	 * @return true if the next player in the list Players present on the match table owns the black pawn
	 */
	public boolean isNextPlayerTheBlackPawn(String playerName) {
		boolean k = false;
		Player p = getPlayerByName(playerName);
		if (getTable().getPlayers().get((getTable().getPlayers().indexOf(p) + 1) % getTable().getPlayers().size()).isBlackPawn() == true) {
			k = true;
		}
		return k;
	}


	/**
	 * Decides who is the winner of the specified match.
     */
	public void calculateWinner(){
		getWinners()[0] = getTable().getPlayers().getFirst();
		int maxScore = getWinners()[0].getPoints();
		int index=1;
		for(int i=1; i<getNumOfPlayers(); i++){
			Player current = getTable().getPlayers().get(i);
			if(current.getPoints() > maxScore){
				getWinners()[0] = current;
				maxScore = current.getPoints();
				getWinners()[1] = null;
				getWinners()[2] = null;
				getWinners()[3] = null;
			} else if (current.getPoints() == maxScore && current.getObCardUsed() > getWinners()[0].getObCardUsed()) {
				getWinners()[0] = current;
				getWinners()[1] = null;
				getWinners()[2] = null;
				getWinners()[3] = null;
			} else if (current.getPoints() == maxScore && current.getObCardUsed() == getWinners()[0].getObCardUsed()) {
				getWinners()[index] = current;
				index++;
			}
		}
	}


	/**
	 * Displays a "you won" or a "you lost" message to every player.
	 *
	 * @param playerName is the nickname of the specified player
	 */
	public void showEndGameMessage(String playerName) {
		boolean win = false;
		Player p = getPlayerByName(playerName);
		for (int i=0; i<getWinners().length; i++) {
			if (getWinners()[i] != null) {
				if (p.getNickname().equals(getWinners()[i].getNickname())) {
					p.notifyListener(GameStatus.I_HAVE_WON);
					win = true;
				}
			}
		}
		if (!win)	p.notifyListener(GameStatus.I_HAVE_LOST);
	}


	/**
	 * Method used by the GUI to know if the player is the winner.
	 *
	 * @param playerName playerName is the nickname of the specified player
	 * @return true if the player is the winner, false otherwise
	 */
	public boolean endGameMessageGui(String playerName){
		boolean win = false;
		Player p = getPlayerByName(playerName);
		for (int i=0; i<getWinners().length; i++) {
			if (getWinners()[i] != null) {
				if (p.getNickname().equals(getWinners()[i].getNickname())) {
					win = true;
				}
			}
		}
		return win;
	}


	/**
	 * This cheat provides the specified player with 10000 resources per each symbol.
	 *
	 * @param playerName is the nickname of the specified player
	 */
	public void cheat1(String playerName) {
		Player p = getPlayerByName(playerName);
		p.getPlayArea().getAvailableResources().put(Symbols.PLANT, 10000);
		p.getPlayArea().getAvailableResources().put(Symbols.ANIMAL, 10000);
		p.getPlayArea().getAvailableResources().put(Symbols.INSECT, 10000);
		p.getPlayArea().getAvailableResources().put(Symbols.FUNGI, 10000);
	}


	/**
	 * This cheat provides the specified player with 20 points.
	 *
	 * @param playerName is the nickname of the specified player
	 */
	public void cheat2(String playerName){
		Player p = getPlayerByName(playerName);
		p.setPoints(20);
		getTable().getScore().put(p.getNickname(), 20);
	}


	/**
	 * With this cheat both resource deck and gold deck become empty.
	 */
	public void cheat3(){
		getTable().getDeckR().clear();
		getTable().getDeckG().clear();
	}
}