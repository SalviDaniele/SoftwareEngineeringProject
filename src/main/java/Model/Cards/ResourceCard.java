package Model.Cards;

/**
 * Represents a resource card, which provides some resources and/or object when placed.
 */
public class ResourceCard extends Card {

    /**
     * The resource card constructor.
     *
     * @param symbols are the symbols on the corners
     * @param colors is the color of the card
     * @param centre are the symbols on the centre
     * @param face indicates if the card needs to be placed on the front or on the back
     * @param points are the points the card provides to the player who places it on their play area
     * @param pointsObject is the eventual object which provides the points
     * @param imageFront is the front image of the card
     * @param imageBack is the back image of the card
     */
    public ResourceCard(Symbols[] symbols, Colors colors, Symbols[] centre, boolean face, int points, Symbols pointsObject, String imageFront, String imageBack) {
        super(symbols, colors, centre, face, points, pointsObject, imageFront, imageBack);
        setRow(-1);
        setColumn(-1);
    }

    /**
     * generic constructor for the class ResourceCard
     */
    public ResourceCard() {
        super();
    }
}
