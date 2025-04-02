package Model.Cards;

import Model.Player;

/**
 * This class represents an objective card which provides points according to the presence
 * of certain quantities of symbols on the play area.
 */
public class ObCardSymbols extends ObjectiveCard {

    /**
     * this attribute is an array of Symbols, representing all the symbols required in order to obtain points from this card
     */
    private Symbols[] symbols;


    /**
     * constructor of an objective card based on symbols
     * @param points are the points of the card
     * @param imageFront is the front image of the card
     * @param imageBack is the back image of the card
     * @param symbols are the symbols required in order to obtain points from this card
     */
    public ObCardSymbols(int points, String imageFront, String imageBack, Symbols[] symbols) {
        super(points, imageFront, imageBack);
        this.symbols = symbols;
    }


    /**
     * generic constructor of the class
     */
    public ObCardSymbols() {
        super();
    }


    /**
     * getter method for the array of Symbols
     * @return the array of symbols linked to this card
     */
    public Symbols[] getSymbols() {
        return symbols;
    }


    /**
     * setter method for the attribute 'symbols'
     * @param symbols is the array of Symbols to be put
     */
    public void setSymbols(Symbols[] symbols) {
        this.symbols = symbols;
    }


    /**
     * with this method it is possible to calculate all the points that a player can gain from a specific objective
     * card based on Symbols
     * @param ob_card is the objective card to be used for calculating points
     * @param current_player is the player who might gain points
     */
    public void calculateObCardSymbols(ObCardSymbols ob_card, Player current_player) {
        int adding_points;
        int points_to_add;
        Symbols element1 = ob_card.getSymbols()[0];
        Symbols element2 = ob_card.getSymbols()[1];
        //Case with just 2 objects
        if (ob_card.getSymbols().length < 3) {
            //The result is an integer rounded down
            adding_points = current_player.getPlayArea().getAvailableResources().get(element1) / 2;
            //Multiplies the result and the points on the card and adds the result to the player's points
            current_player.setPoints(current_player.getPoints() + (ob_card.getPoints() * adding_points));
        }
        //Case with 3 objects
        else {
            Symbols element3 = ob_card.getSymbols()[2];
            //3 copies of the same object
            if (element1 == element2) {
                adding_points = current_player.getPlayArea().getAvailableResources().get(element1) / 3;
                points_to_add = ob_card.getPoints() * adding_points;
                current_player.setPoints(current_player.getPoints() + points_to_add);
            }
            //3 copies of different objects
            if (element1 != element2) {
                adding_points = current_player.getPlayArea().getAvailableResources().get(element1);
                if (current_player.getPlayArea().getAvailableResources().get(element2) < adding_points) {
                    adding_points = current_player.getPlayArea().getAvailableResources().get(element2);
                }
                if (current_player.getPlayArea().getAvailableResources().get(element3) < adding_points) {
                    adding_points = current_player.getPlayArea().getAvailableResources().get(element3);
                }
                current_player.setPoints(current_player.getPoints() + (ob_card.getPoints() * adding_points));
            }
        }
    }
}
