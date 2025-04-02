package Model;

import Model.Cards.Card;
import Model.Cards.Colors;
import Model.Cards.Symbols;
import com.fasterxml.jackson.annotation.JsonBackReference;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a player's play area.
 */
public class PlayArea implements Serializable {

    /**
     * The width of the card ImageView in the Gui
     */
    final static int CARD_WIDTH = 201;

    /**
     * The height of the card ImageView in the Gui
     */
    final static int CARD_HEIGHT = 135;

    /**
     * Two constants to correctly overlay cards in the gui
     */
    final static double translateX = -50, translateY = -55;

    /**
     * The matrix representing the cards which have been placed onto the play area.
     */
    private Card[][] grid;

    /**
     * The map representing the corresponding quantity on the play area for every symbol.
     */
    private Map<Symbols, Integer> available_resources;

    /**
     * The map that may represent the corresponding quantity on the play area for every symbol if a player place a card
     */
    private Map<Symbols, Integer> possibleFutureAvailableResources;

    /**
     * This attribute is used by the Gui to know the order of the cards placed and the coordinates,
     * in a system where the starter card is in position (0, 0)
     */
    private Card[] orderedCardList = new Card[50];

    /**
     * Indicates the current index in the cards array.
     */
    private int cardsIndex;

    /**
     * A reference to the player controlling  the play area.
     */
    @JsonBackReference
    private Player player;


    /**
     * The play area constructor.
     *
     * @param dim1 is the number of rows of the matrix
     * @param dim2 is the number of columns of the matrix
     * @param player is the play area's player
     */
    public PlayArea(int dim1, int dim2, Player player) {
        grid = new Card[dim1][dim2];
        available_resources = new HashMap<>();
        available_resources.put(Symbols.PLANT, 0);
        available_resources.put(Symbols.ANIMAL, 0);
        available_resources.put(Symbols.FUNGI, 0);
        available_resources.put(Symbols.INSECT, 0);
        available_resources.put(Symbols.QUILL, 0);
        available_resources.put(Symbols.INKWELL, 0);
        available_resources.put(Symbols.MANUSCRIPT, 0);
        this.player = player;
        this.cardsIndex = 0;
    }


    /**
     * Empty constructor for the class PLayArea, for Jackson serialization.
     */
    public PlayArea() {}


    /**
     * Grid's getter method.
     *
     * @return the play area's grid.
     */
    public Card[][] getGrid() {
        return grid;
    }


    /**
     * Grid's setter method.
     *
     * @param grid is the play area's grid
     */
    public void setGrid(Card[][] grid) {
        this.grid = grid;
    }


    /**
     * Getter method for the symbols' map.
     *
     * @return the map representing the quantity for each symbol on the play area
     */
    public Map<Symbols, Integer> getAvailableResources() {
        return available_resources;
    }


    /**
     * Setter methods for the symbols' map.
     *
     * @param available_resources is the map representing the available quantity for each symbol on the play area
     */
    public void setAvailableResources(Map<Symbols, Integer> available_resources) {
        this.available_resources = available_resources;
    }


    /**
     * getter method for the attribute 'possibleFutureAvailableResources'
     * @return the attribute 'possibleFutureAvailableResources'
     */
    public Map<Symbols, Integer> getPossibleFutureAvailableResources() {
        return possibleFutureAvailableResources;

    }


    /**
     * setter method for the attribute 'possibleFutureAvailableResources'
     * @param possibleFutureAvailableResources is the map of symbols to be put
     */
    public void setPossibleFutureAvailableResources(Map<Symbols, Integer> possibleFutureAvailableResources) {
        this.possibleFutureAvailableResources = possibleFutureAvailableResources;
    }


    /**
     * Player's getter method.
     *
     * @return the player linked to the play area
     */
    public Player getPlayer() {
        return player;
    }


    /**
     * Player's setter method.
     *
     * @param player represents the player linked to the play area
     */
    public void setPlayer(Player player) {
        this.player = player;
    }


    /**
     * Setter method for orderedCardList.
     *
     * @param orderedCardList a new array containing the starter card
     */
    public void setOrderedCardList(Card[] orderedCardList) {
        this.orderedCardList = orderedCardList;
    }


    /**
     * Getter method for orderedCardList.
     *
     * @return a reference of the parameter orderedCardList
     */
    public Card[] getOrderedCardList() {
        return orderedCardList;
    }


    /**
     * Getter method for cardsIndex attribute.
     *
     * @return the current index in the orderedCardList
     */
    public int getCardsIndex() {
        return cardsIndex;
    }


    /**
     * Setter method for cardsIndex attribute.
     *
     * @param cardsIndex the current index in the orderedCardList
     */
    public void setCardsIndex(int cardsIndex) {
        this.cardsIndex = cardsIndex;
    }


    /**
     * The method places the card in the grid if there is at least one corner around the card which is not covered (true)
     * and, if this is the case, if there are not covered corners (belonging to other cards) in that position.
     * The card is then placed on the chosen position and turned on the chosen face.
     *
     * @param card_to_place is a reference to the card the player is lacing in their play area.
     * @param face indicates if the card is going to be placed by the front (true) or by the back (false).
     * @param pos1 is a number indicating the first coordinate where the card will be placed.
     * @param pos2 is a number indicating the second coordinate where the card will be placed.
     */
    public boolean check_and_insert(Card card_to_place, boolean face, int pos1, int pos2){
        boolean can_I_continue;
        if( (getGrid()[pos1 + 1][pos2 + 1] != null &&
                getGrid()[pos1 + 1][pos2 + 1].getCorners()[0] == true)
                || (getGrid()[pos1+1][pos2-1] != null &&
                getGrid()[pos1+1][pos2-1].getCorners()[1] == true)
                || (getGrid()[pos1-1][pos2+1] != null &&
                getGrid()[pos1-1][pos2+1].getCorners()[2] == true)
                || (getGrid()[pos1-1][pos2-1] != null &&
                getGrid()[pos1-1][pos2-1].getCorners()[3] == true)){
            can_I_continue = true;

        }else{
            //We are trying to place a card without covering the corner of another card
            return false;
        }
        if(can_I_continue){
            if(  (getGrid()[pos1 + 1][pos2 + 1] != null &&
                    (getGrid()[pos1 + 1][pos2 + 1].getCorners()[0] == false ||
                            getGrid()[pos1+1][pos2+1].getSymbols()[0] == Symbols.NOCORNER))
                    || (getGrid()[pos1+1][pos2-1] != null &&
                    (getGrid()[pos1+1][pos2-1].getCorners()[1] == false ||
                            getGrid()[pos1+1][pos2-1].getSymbols()[1] == Symbols.NOCORNER))
                    || (getGrid()[pos1-1][pos2+1] != null &&
                    (getGrid()[pos1-1][pos2+1].getCorners()[2] == false ||
                            getGrid()[pos1-1][pos2+1].getSymbols()[2] == Symbols.NOCORNER))
                    || (getGrid()[pos1-1][pos2-1] != null &&
                    (getGrid()[pos1-1][pos2-1].getCorners()[3] == false ||
                            getGrid()[pos1-1][pos2-1].getSymbols()[3] == Symbols.NOCORNER)) ){
                //We are trying to place the card near an already covered corner or on a non-existent corner
                return false;
            }else{
                card_to_place.setFace(face);
                getGrid()[pos1][pos2] = card_to_place;
                double x = ((pos2 - ((double) getGrid().length / 2)) + 0.5) * (CARD_WIDTH + translateX);
                double y = ((pos1 - ((double) getGrid().length / 2)) + 0.5) * (CARD_HEIGHT + translateY);
                card_to_place.setColumn(x);
                card_to_place.setRow(y);
                //The card was placed correctly
                //If a card was placed on the back, its corners are set to EMPTY
                if(can_I_continue){
                    if(card_to_place.isFace() == false){
                        for(int i=0; i<4; i++){
                            card_to_place.getSymbols()[i] = Symbols.EMPTY;
                        }
                    }
                }
                return true;
            }
        }
        return true;
    }


    /**
     * This method updates the map on the play area which stores information about the available quantity
     * of all objects and resources present on the current player's play area.
     *
     * @param card_to_place is a reference to the placed card.
     * @param face indicates if the card is going to be placed by the front (true) or by the back (false).
     * @param pos1 is a number indicating the first coordinate where the card has been placed.
     * @param pos2 is a number indicating the second coordinate where the card has been placed.
     */
    public void update_resources(Card card_to_place, boolean face, int pos1, int pos2){
        Map<Symbols, Integer> a = getAvailableResources();
        //If the card is placed on the back, and it is not a starter card, there is only a resource in the center;
        //so only one resource (the same color/kingdom) of the card is added
        if(face == false && card_to_place.getCentre()==null){
            if(card_to_place.getColors() == Colors.BLUE){
                getAvailableResources().put(Symbols.ANIMAL, getAvailableResources().get(Symbols.ANIMAL) + 1);
            }
            if(card_to_place.getColors() == Colors.RED){
                getAvailableResources().put(Symbols.FUNGI, getAvailableResources().get(Symbols.FUNGI) + 1);
            }
            if(card_to_place.getColors() == Colors.PURPLE){
                getAvailableResources().put(Symbols.INSECT, getAvailableResources().get(Symbols.INSECT) + 1);
            }
            if(card_to_place.getColors() == Colors.GREEN){
                getAvailableResources().put(Symbols.PLANT, getAvailableResources().get(Symbols.PLANT) + 1);
            }
        }
        else{
            //If the card is a starter card, and it is placed on the face with only resources in the center, they are added
            if(card_to_place.getCentre() != null && face == true){
                for(int i=0; i<card_to_place.getCentre().length; i++){
                    if (card_to_place.getCentre()[i] != Symbols.NOCORNER && card_to_place.getCentre()[i] != Symbols.EMPTY) {
                        getAvailableResources().put(card_to_place.getCentre()[i], getAvailableResources().get(card_to_place.getCentre()[i]) + 1);
                    }
                }
            }
            //If the card has no resources in the center, then the ones on corners are added
            for (int i = 0; i < 4; i++) {
                if (card_to_place.getSymbols()[i] != Symbols.NOCORNER && card_to_place.getSymbols()[i] != Symbols.EMPTY) {
                    getAvailableResources().put(card_to_place.getSymbols()[i], getAvailableResources().get(card_to_place.getSymbols()[i]) + 1);
                }
            }
        }
        //Resources/objects on covered corners are subtracted
        if(getGrid()[pos1+1][pos2+1] != null &&
                getGrid()[pos1+1][pos2+1].getCorners()[0] == true &&
                getGrid()[pos1+1][pos2+1].getSymbols()[0] != Symbols.EMPTY){
            getAvailableResources().put(getGrid()[pos1+1][pos2+1].getSymbols()[0], getAvailableResources().get(getGrid()[pos1+1][pos2+1].getSymbols()[0]) - 1);
        }
        if(getGrid()[pos1+1][pos2-1] != null &&
                getGrid()[pos1+1][pos2-1].getCorners()[1] == true &&
                getGrid()[pos1+1][pos2-1].getSymbols()[1] != Symbols.EMPTY){
            getAvailableResources().put(getGrid()[pos1+1][pos2-1].getSymbols()[1], getAvailableResources().get(getGrid()[pos1+1][pos2-1].getSymbols()[1]) - 1);
        }
        if(getGrid()[pos1-1][pos2+1] != null &&
                getGrid()[pos1-1][pos2+1].getCorners()[2] == true &&
                getGrid()[pos1-1][pos2+1].getSymbols()[2] != Symbols.EMPTY){
            getAvailableResources().put(getGrid()[pos1-1][pos2+1].getSymbols()[2], getAvailableResources().get(getGrid()[pos1-1][pos2+1].getSymbols()[2]) - 1);
        }
        if(getGrid()[pos1-1][pos2-1] != null &&
                getGrid()[pos1-1][pos2-1].getCorners()[3] == true &&
                getGrid()[pos1-1][pos2-1].getSymbols()[3] != Symbols.EMPTY){
            getAvailableResources().put(getGrid()[pos1-1][pos2-1].getSymbols()[3],
                    getAvailableResources().get(getGrid()[pos1-1][pos2-1].getSymbols()[3]) - 1);
        }
    }


    /**
     * This method set as covered (false) every other card corner if it has been covered by the placed card.
     *
     * @param pos1 is a number indicating the first coordinate where the card has been placed.
     * @param pos2 is a number indicating the second coordinate where the card has been placed.
     */
    public void make_covered_corners_false(int pos1, int pos2) {
        if(getGrid()[pos1+1][pos2+1] != null){
            if(getGrid()[pos1+1][pos2+1].getCorners()[0] == true){
                getGrid()[pos1+1][pos2+1].getCorners()[0] = false;
            }
        }
        if(getGrid()[pos1+1][pos2-1] != null){
            if(getGrid()[pos1+1][pos2-1].getCorners()[1] == true){
                getGrid()[pos1+1][pos2-1].getCorners()[1] = false;
            }
        }
        if(getGrid()[pos1-1][pos2+1] != null){
            if(getGrid()[pos1-1][pos2+1].getCorners()[2] == true){
                getGrid()[pos1-1][pos2+1].getCorners()[2] = false;
            }
        }
        if(getGrid()[pos1-1][pos2-1] != null){
            if(getGrid()[pos1-1][pos2-1].getCorners()[3] == true){
                getGrid()[pos1-1][pos2-1].getCorners()[3] = false;
            }
        }
    }


    /**
     * This method is similar to 'update_resources' but it updates the attribute 'possibleFutureAvailableResources' instead
     * @param card_to_place is a reference to the placed card.
     * @param faceSelected indicates if the card is going to be placed by the front (true) or by the back (false).
     * @param pos1 is a number indicating the first coordinate where the card has been placed.
     * @param pos2 is a number indicating the second coordinate where the card has been placed.
     */
    public void viewDifferenceOfResources(Card card_to_place, String faceSelected, int pos1, int pos2){
        boolean face = faceSelected.equalsIgnoreCase("front");
        possibleFutureAvailableResources = new HashMap<>();
        getPossibleFutureAvailableResources().put(Symbols.ANIMAL, getAvailableResources().get(Symbols.ANIMAL));
        getPossibleFutureAvailableResources().put(Symbols.INSECT, getAvailableResources().get(Symbols.INSECT));
        getPossibleFutureAvailableResources().put(Symbols.PLANT, getAvailableResources().get(Symbols.PLANT));
        getPossibleFutureAvailableResources().put(Symbols.FUNGI, getAvailableResources().get(Symbols.FUNGI));
        getPossibleFutureAvailableResources().put(Symbols.INKWELL, getAvailableResources().get(Symbols.INKWELL));
        getPossibleFutureAvailableResources().put(Symbols.MANUSCRIPT, getAvailableResources().get(Symbols.MANUSCRIPT));
        getPossibleFutureAvailableResources().put(Symbols.QUILL, getAvailableResources().get(Symbols.QUILL));
        if(!face && card_to_place.getCentre()==null){
            if(card_to_place.getColors() == Colors.BLUE){
                getPossibleFutureAvailableResources().put(Symbols.ANIMAL, getPossibleFutureAvailableResources().get(Symbols.ANIMAL) + 1);
            }
            if(card_to_place.getColors() == Colors.RED){
                getPossibleFutureAvailableResources().put(Symbols.FUNGI, getPossibleFutureAvailableResources().get(Symbols.FUNGI) + 1);
            }
            if(card_to_place.getColors() == Colors.PURPLE){
                getPossibleFutureAvailableResources().put(Symbols.INSECT, getPossibleFutureAvailableResources().get(Symbols.INSECT) + 1);
            }
            if(card_to_place.getColors() == Colors.GREEN){
                getPossibleFutureAvailableResources().put(Symbols.PLANT, getPossibleFutureAvailableResources().get(Symbols.PLANT) + 1);
            }
        }else{
            if(card_to_place.getCentre() != null && face){
                for(int i=0; i<card_to_place.getCentre().length; i++){
                    if (card_to_place.getCentre()[i] != Symbols.NOCORNER && card_to_place.getCentre()[i] != Symbols.EMPTY) {
                        getPossibleFutureAvailableResources().put(card_to_place.getCentre()[i], getPossibleFutureAvailableResources().get(card_to_place.getCentre()[i]) + 1);
                    }
                }
            }
            for (int i = 0; i < 4; i++) {
                if (card_to_place.getSymbols()[i] != Symbols.NOCORNER && card_to_place.getSymbols()[i] != Symbols.EMPTY) {
                    getPossibleFutureAvailableResources().put(card_to_place.getSymbols()[i], getPossibleFutureAvailableResources().get(card_to_place.getSymbols()[i]) + 1);
                }
            }
        }
        if(getGrid()[pos1+1][pos2+1] != null &&
                getGrid()[pos1 + 1][pos2 + 1].getCorners()[0] &&
                getGrid()[pos1+1][pos2+1].getSymbols()[0] != Symbols.EMPTY){
            getPossibleFutureAvailableResources().put(getGrid()[pos1+1][pos2+1].getSymbols()[0], getPossibleFutureAvailableResources().get(getGrid()[pos1+1][pos2+1].getSymbols()[0]) - 1);
        }
        if(getGrid()[pos1+1][pos2-1] != null &&
                getGrid()[pos1 + 1][pos2 - 1].getCorners()[1] &&
                getGrid()[pos1+1][pos2-1].getSymbols()[1] != Symbols.EMPTY){
            getPossibleFutureAvailableResources().put(getGrid()[pos1+1][pos2-1].getSymbols()[1], getPossibleFutureAvailableResources().get(getGrid()[pos1+1][pos2-1].getSymbols()[1]) - 1);
        }
        if(getGrid()[pos1-1][pos2+1] != null &&
                getGrid()[pos1 - 1][pos2 + 1].getCorners()[2] &&
                getGrid()[pos1-1][pos2+1].getSymbols()[2] != Symbols.EMPTY){
            getPossibleFutureAvailableResources().put(getGrid()[pos1-1][pos2+1].getSymbols()[2], getPossibleFutureAvailableResources().get(getGrid()[pos1-1][pos2+1].getSymbols()[2]) - 1);
        }
        if(getGrid()[pos1-1][pos2-1] != null &&
                getGrid()[pos1 - 1][pos2 - 1].getCorners()[3] &&
                getGrid()[pos1-1][pos2-1].getSymbols()[3] != Symbols.EMPTY){
            getPossibleFutureAvailableResources().put(getGrid()[pos1-1][pos2-1].getSymbols()[3], getPossibleFutureAvailableResources().get(getGrid()[pos1-1][pos2-1].getSymbols()[3]) - 1);
        }
    }
}

