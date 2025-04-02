package Model.Cards;

/**
 * represents a starter card, which is the first card that each player is supposed to place
 */
public class StarterCard extends Card {

    /**
     * this kind of card is particular because it has 2 sets of four symbols, one for each side, so this array
     * represents the set of symbols on the back of a starter card
     */
    private Symbols[] default_symbols;

    /**
     * constructor of the class StarterCard
     * @param symbols are the symbols on the corners
     * @param colors is the color of the card
     * @param centre are the symbols on the centre
     * @param face indicates if the card needs to be placed on the front or on the back
     * @param points are the points the card provides to the player who places it on their play area
     * @param pointsObject is the eventual object which provides the points
     * @param imageFront is the front image of the card
     * @param imageBack is the back image of the card
     * @param default_symbols are the symbols on the corners of the back side of the card
     */
    public StarterCard(Symbols[] symbols, Colors colors, Symbols[] centre, boolean face, int points, Symbols pointsObject, String imageFront, String imageBack, Symbols[] default_symbols) {
        super(symbols, colors, centre, face, points, pointsObject, imageFront, imageBack);
        this.default_symbols = default_symbols;
        setRow(-1);
        setColumn(-1);
    }

    /**
     * this is the generic constructor of the class
     */
    public StarterCard() {
        super();
    }

    /**
     * getter method for the 'attribute' default_symbols
     * @return the 'attribute' default_symbols
     */
    public Symbols[] getDefaultSymbols() {
        return default_symbols;
    }

    /**
     * setter method for the 'attribute' default_symbols
     * @param default_symbols is the array of symbols to be put
     */
    public void setDefaultSymbols(Symbols[] default_symbols) {
        this.default_symbols = default_symbols;
    }
}
