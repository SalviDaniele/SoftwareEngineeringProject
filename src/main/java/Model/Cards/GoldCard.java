package Model.Cards;

import java.util.Map;

/**
 * Represents a gold card, which needs satisfaction of certain requirements to be placed.
 */
public class GoldCard extends Card{

    /**
     * A map representing the minimum quantity per symbol which is needed to place the card.
     */
    private Map<Symbols, Integer> requirements;

    /**
     * The gold card constructor.
     *
     * @param symbols are the symbols on the corners
     * @param colors is the color of the card
     * @param centre are the symbols on the centre
     * @param face indicates if the card needs to be placed on the front or on the back
     * @param points are the points the card provides to the player who places it on their play area
     * @param pointsObject is the eventual object which provides the points
     * @param requirements is the minimum quantity per symbol which is needed to place the card
     */
    public GoldCard(Symbols[] symbols, Colors colors, Symbols[] centre, boolean face, int points, Symbols pointsObject, String imageFront, String imageBack, Map<Symbols, Integer> requirements) {
        super(symbols, colors, centre, face, points, pointsObject, imageFront, imageBack);
        this.requirements = requirements;
        this.requirements.putIfAbsent(Symbols.PLANT, 0);
        this.requirements.putIfAbsent(Symbols.ANIMAL, 0);
        this.requirements.putIfAbsent(Symbols.FUNGI, 0);
        this.requirements.putIfAbsent(Symbols.INSECT, 0);
        setRow(-1);
        setColumn(-1);
    }

    /**
     * generic constructor for this class
     */
    public GoldCard() {
        super();
    }

    /**
     * getter method for the attribute 'requirements'
     * @return the attribute 'requirements'
     */
    public Map<Symbols, Integer> getRequirements() {
        return requirements;
    }

    /**
     * setter method for the attribute 'requirements'
     * @param requirements is the map to be put
     */
    public void setRequirements(Map<Symbols, Integer> requirements) {
        this.requirements = requirements;
    }
}
