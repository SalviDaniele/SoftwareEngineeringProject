package Model.Cards;

import Model.Player;

/**
 * This class represents an objective card which provides points according to the positioning of certain cards on the play area.
 */
public class ObCardPosition extends ObjectiveCard {

    /**
     * The color of the cards composing the pattern, ordered from top to bottom.
     */
    private Colors[] ob_colors;

    /**
     * The number indicating the column where a card is positioned in the pattern, ordered from top to bottom
     */
    private int[] ob_positions;


    /**
     * Constructor of an objective card based on a pattern.
     *
     * @param points are the points given by the card for every corresponding pattern
     * @param imageFront is the front image of the card
     * @param imageBack is the back image of the card
     * @param ob_colors are the colors of the cards composing the pattern, from top to bottom
     * @param ob_positions indicates the columns where cards are positioned in the pattern, from top to bottom
     */
    public ObCardPosition(int points, String imageFront, String imageBack, Colors[] ob_colors, int[] ob_positions) {
        super(points, imageFront, imageBack);
        this.ob_colors = ob_colors;
        this.ob_positions = ob_positions;
    }


    /**
     * Empty constructor for Jackson serialization.
     */
    public ObCardPosition() {
        super();
    }


    /**
     * Pattern colors' getter method.
     *
     * @return the array indicating the colors of the cards composing the pattern, from top to bottom
     */
    public Colors[] getObColors() {
        return ob_colors;
    }


    /**
     * Pattern colors' setter method.
     *
     * @param ob_colors is the array indicating the colors of the cards composing the pattern, from top to bottom
     */
    public void setObColors(Colors[] ob_colors) {
        this.ob_colors = ob_colors;
    }


    /**
     * Pattern columns' getter method.
     *
     * @return the array indicating the columns where cards are positioned in the pattern, from top to bottom
     */
    public int[] getObPositions() {
        return ob_positions;
    }


    /**
     * Pattern colors' setter method.
     *
     * @param ob_positions is the array indicating the columns where cards are positioned in the pattern, from top to bottom
     */
    public void setObPositions(int[] ob_positions) {
        this.ob_positions = ob_positions;
    }


    /**
     * with this method it is possible to calculate all the points that a player can gain from a specific objective
     * card based on a pattern of colors and positions
     * @param ob_card is the objective card to be used for calculating points
     * @param current_player is the player who might gain points
     */
    public void calculateObCardPosition(ObCardPosition ob_card, Player current_player) {
        int go_on = 2;
        int bonus = 2;
        for (int i = 0; i < current_player.getPlayArea().getGrid().length; i++) {
            for (int j = 0; j < current_player.getPlayArea().getGrid().length; j++) {
                if (current_player.getPlayArea().getGrid()[i][j]!= null){
                    if(ob_card.getObColors()[0].equals(current_player.getPlayArea().getGrid()[i][j].getColors()) &&
                        current_player.getPlayArea().getGrid()[i][j].isConsidered() == false) {
                        go_on = where_to_continue(ob_card, current_player, i, j, 0, 1);
                        if (go_on != 2) {
                            bonus = where_to_continue(ob_card, current_player, i + 1, j + go_on, 1, 2);
                            if (bonus != 2) {
                                current_player.setPoints(current_player.getPoints() + ob_card.getPoints());
                                current_player.getPlayArea().getGrid()[i][j].setConsidered(true);
                                current_player.getPlayArea().getGrid()[i + 1][j + go_on].setConsidered(true);
                                current_player.getPlayArea().getGrid()[i + 2][j + go_on + bonus].setConsidered(true);
                                go_on = 2;
                                bonus = 2;
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     *This method checks where to look in the player's grid to see if it matches the pattern of the objective card
     * @param ob_card is the objective card to be used for calculating points
     * @param current_player is the player who might gain points
     * @param i is the row in the player's grid where there is a card that may match the pattern
     * @param j is the column in the player's grid where there is a card that may match the pattern
     * @param a this number and the next one are used for checking the position in the array of positions
     * @param b this number and the previous one are used for checking the position in the array of positions
     * @return an integer used for knowing where the method 'calculateObCardPosition' is supposed to search in the
     * player's grid for checking if the pattern of the objective card can be matched
     */
    public int where_to_continue(ObCardPosition ob_card, Player current_player, int i, int j, int a, int b) {
        int k = 2;
        if (ob_card.getObPositions()[a] > ob_card.getObPositions()[b]) {
            if (current_player.getPlayArea().getGrid()[i + 1][j - 1]!= null){
                if (ob_card.getObColors()[b].equals(current_player.getPlayArea().getGrid()[i + 1][j - 1].getColors())) {
                    k = -1;
                }
            }
        }
        if (ob_card.getObPositions()[a] == ob_card.getObPositions()[b]) {
            if(current_player.getPlayArea().getGrid()[i + 1][j] != null){
                if (ob_card.getObColors()[b].equals(current_player.getPlayArea().getGrid()[i + 1][j].getColors())) {
                    k = 0;
                }
            }
        }
        if (ob_card.getObPositions()[a] < ob_card.getObPositions()[b]) {
            if(current_player.getPlayArea().getGrid()[i + 1][j + 1] != null){
                if (ob_card.getObColors()[b].equals(current_player.getPlayArea().getGrid()[i + 1][j + 1].getColors())) {
                    k = 1;
                }
            }
        }
        return k;
    }
}


