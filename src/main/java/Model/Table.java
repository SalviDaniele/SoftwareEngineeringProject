package Model;

import Controller.MatchController;
import Model.Cards.*;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.*;

/**
 * this class represents the table of a match, containing mainly all decks and cards on the table
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "match")
public class Table implements Serializable{

    /**
     * Attribute used for serialization.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * A reference to the current match.
     */
    private MatchController match;

    /**
     * the deck of resource card
     */
    private List<ResourceCard> deckR;

    /**
     * the deck of gold card
     */
    private List<GoldCard> deckG;

    /**
     * the deck of starter card
     */
    private List<StarterCard> deckS;

    /**
     * the deck of objective card
     */
    private List<ObjectiveCard> deckO;

    /**
     * this array represents the 2 face up resource card on the table
     */
    private ResourceCard[] cardsR;

    /**
     * this array represents the 2 face up gold card on the table
     */
    private GoldCard[] cardsG;

    /**
     * this array represents the face up starter card on the table
     */
    private StarterCard[] cardsS;

    /**
     * this array represents the common objective card on the table
     */
    private ObjectiveCard[] cardsO;

    /**
     * the list of players currently disputing the match associated to this table
     */
    @JsonManagedReference
    private List<Player> players;

    /**
     * a map associating a player to his points
     */
    private Map<String, Integer> score;

    /**
     * a boolean indicating if the match is during its final phase
     */
    private boolean isEndPhase;


    /**
     * constructor of the class
     * @throws IOException if there is an input/output problem
     * @throws ParseException if there is an error during the parsing of text input
     */
    public Table() throws IOException, ParseException {
        this.deckR = deckRInit("Cards.json");
        this.deckG = deckGInit("Cards.json");
        this.deckS = deckSInit("Cards.json");
        this.deckO = deckOInit("Cards.json");
        this.players = new ArrayList<>();
        this. score = new HashMap<>();
        this.isEndPhase = false;
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
     * getter method for the attribute 'deckR'
     * @return the attribute 'deckR'
     */
    public List<ResourceCard> getDeckR() {
        return deckR;
    }


    /**
     * setter method for the attribute 'deckR'
     * @param deckR is the list of resource cards to be put
     */
    public void setDeckR(List<ResourceCard> deckR) {
        this.deckR = deckR;
    }


    /**
     * getter method for the attribute 'deckG'
     * @return the attribute 'deckG'
     */
    public List<GoldCard> getDeckG() {
        return deckG;
    }


    /**
     * setter method for the attribute 'deckG'
     * @param deckG is the list of gold cards to be put
     */
    public void setDeckG(List<GoldCard> deckG) {
        this.deckG = deckG;
    }


    /**
     * getter method for the attribute 'deckS'
     * @return the attribute 'deckS'
     */
    public List<StarterCard> getDeckS() {
        return deckS;
    }


    /**
     * setter method for the attribute 'deckS'
     * @param deckS is the list of starter cards to be put
     */
    public void setDeckS(List<StarterCard> deckS) {
        this.deckS = deckS;
    }


    /**
     * getter method for the attribute 'deckO'
     * @return the attribute 'deckO'
     */
    public List<ObjectiveCard> getDeckO() {
        return deckO;
    }


    /**
     * setter method for the attribute 'deckO'
     * @param deckO is the list of objective cards to be put
     */
    public void setDeckO(List<ObjectiveCard> deckO) {
        this.deckO = deckO;
    }


    /**
     * getter method for the attribute 'cardsR'
     * @return the attribute 'cardsR'
     */
    public ResourceCard[] getCardsR() {
        return cardsR;
    }


    /**
     * setter method for the attribute 'cardsR'
     * @param cardsR is the array of resource cards to be put
     */
    public void setCardsR(ResourceCard[] cardsR) {
        this.cardsR = cardsR;
    }


    /**
     * getter method for the attribute 'cardsG'
     * @return the attribute 'cardsG'
     */
    public GoldCard[] getCardsG() {
        return cardsG;
    }


    /**
     * setter method for the attribute 'cardsG'
     * @param cardsG is the array of gold cards to be put
     */
    public void setCardsG(GoldCard[] cardsG) {
        this.cardsG = cardsG;
    }


    /**
     * getter method for the attribute 'cardsS'
     * @return the attribute 'cardsS'
     */
    public StarterCard[] getCardsS() {
        return cardsS;
    }


    /**
     * setter method for the attribute 'cardsS'
     * @param cardsS is the array of starter cards to be put
     */
    public void setCardsS(StarterCard[] cardsS) {
        this.cardsS = cardsS;
    }


    /**
     * getter method for the attribute 'cardsO'
     * @return the attribute 'cardsO'
     */
    public ObjectiveCard[] getCardsO() {
        return cardsO;
    }


    /**
     * setter method for the attribute 'cardsO'
     * @param cardsO is the array of objective cards to be put
     */
    public void setCardsO(ObjectiveCard[] cardsO) {
        this.cardsO = cardsO;
    }


    /**
     * getter method for the attribute 'players'
     * @return the attribute 'players'
     */
    public List<Player> getPlayers() {
        return players;
    }


    /**
     * setter method for the attribute 'players'
     * @param players is the list of players to be put
     */
    public void setPlayers(List<Player> players) {
        this.players = players;
    }


    /**
     * getter method for the attribute 'score'
     * @return the attribute 'score'
     */
    public Map<String, Integer> getScore() {
        return score;
    }


    /**
     * setter method for the attribute 'score'
     * @param score is the map to be put
     */
    public void setScore(Map<String, Integer> score) {
        this.score = score;
    }


    /**
     * getter method for the attribute 'isEndPhase'
     * @return the attribute 'isEndPhase'
     */
    public boolean isEndPhase() {
        return isEndPhase;
    }


    /**
     * setter method for the attribute 'isEndPhase'
     * @param endPhase is the boolean to be put
     */
    public void setEndPhase(boolean endPhase) {
        isEndPhase = endPhase;
    }


    /**
     * with this method the deck of resource cards is shuffled
     */
    public void shuffle_resource_deck(){
        Collections.shuffle(deckR);
    }

    /**
     * with this method the deck of gold cards is shuffled
     */
    public void shuffle_golden_deck(){
        Collections.shuffle(deckG);
    }


    /**
     * with this method the deck of starter cards is shuffled
     */
    public void shuffle_starter_deck(){
        Collections.shuffle(deckS);
    }


    /**
     * with this method the deck of objective cards is shuffled
     */
    public void shuffle_objective_deck(){
        Collections.shuffle(deckO);
    }


    /**
     * method for creating the deck of resource cards using the json file
     * @param jsonFileName is the reference to the json file
     * @return the deck of resource cards
     * @throws ParseException if there is an error during the parsing of text input
     * @throws IOException if there is an input/output problem
     */
    public List<ResourceCard> deckRInit(String jsonFileName) throws ParseException, IOException {

        ResourceCard [] cardsR = new ResourceCard[40];
        JSONParser parser = new JSONParser();
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream(jsonFileName);
        assert is != null;
        InputStreamReader readerCard = new InputStreamReader(is);
        Object objCard = parser.parse(readerCard);
        JSONObject cardjsonobject = (JSONObject) objCard;
        JSONArray arrayResources = (JSONArray) cardjsonobject.get("ResourceCard");

        for(int i=0;i<arrayResources.size();i++) {
            Symbols[] symbols = new Symbols[4];
            Colors color = null;
            Symbols[] centre;
            JSONObject resourceCards = (JSONObject) arrayResources.get(i);
            for (Symbols t : Symbols.values()) {
                if (t.getValue().equals(resourceCards.get("fCorner1")))
                    symbols[0] = t;
                if (t.getValue().equals(resourceCards.get("fCorner2")))
                    symbols[1] = t;
                if (t.getValue().equals(resourceCards.get("fCorner3")))
                    symbols[2] = t;
                if (t.getValue().equals(resourceCards.get("fCorner4")))
                    symbols[3] = t;
            }
            for (Colors t : Colors.values()) {
                if (t.getValue().equals(resourceCards.get("color")))
                    color = t;
            }

            JSONArray centralSymbols = (JSONArray) resourceCards.get("centralSymbols");
            centre = new Symbols[centralSymbols.size()];
            for (int k = 0; k < centralSymbols.size(); k++) {
                for (Symbols t : Symbols.values()) {
                    if (t.getValue().equals(centralSymbols.get(k)))
                        centre[k] = t;
                }
            }

            Long points = (Long) resourceCards.get("points");
            String imageFront = (String) resourceCards.get("imageFront");
            String imageBack = (String) resourceCards.get("imageBack");

            cardsR[i] = new ResourceCard(symbols, color, null, true, points.intValue(), Symbols.EMPTY, imageFront, imageBack);
        }
        return new ArrayList<>(Arrays.asList(cardsR));
    }


    /**
     * method for creating the deck of gold cards using the json file
     * @param jsonFileName is the reference to the json file
     * @return the deck of gold cards
     * @throws ParseException if there is an error during the parsing of text input
     * @throws IOException if there is an input/output problem
     */
    public List<GoldCard> deckGInit(String jsonFileName) throws IOException, ParseException {
        GoldCard [] cardsG = new GoldCard[40];
        JSONParser parser = new JSONParser();
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream(jsonFileName);
        assert is != null;
        InputStreamReader readerCard = new InputStreamReader(is);
        Object objCard = parser.parse(readerCard);
        JSONObject cardjsonobject = (JSONObject) objCard;
        ObjectMapper mapper = new ObjectMapper();

        JSONArray arrayGold = (JSONArray) cardjsonobject.get("GoldCard");

        for(int i=0;i<arrayGold.size();i++) {
            JSONObject goldCards = (JSONObject) arrayGold.get(i);
            Symbols[] symbols = new Symbols[4];
            Colors color = null;
            Symbols[] centre;
            Symbols pointSymbol = null;

            for (Symbols t : Symbols.values()) {
                if (t.getValue().equals(goldCards.get("fCorner1")))
                    symbols[0] = t;
                if (t.getValue().equals(goldCards.get("fCorner2")))
                    symbols[1] = t;
                if (t.getValue().equals(goldCards.get("fCorner3")))
                    symbols[2] = t;
                if (t.getValue().equals(goldCards.get("fCorner4")))
                    symbols[3] = t;
            }
            for (Colors t : Colors.values()) {
                if (t.getValue().equals(goldCards.get("color")))
                    color = t;
            }

            JSONArray centralSymbols = (JSONArray) goldCards.get("centralSymbols");
            centre = new Symbols[centralSymbols.size()];
            for (int k = 0; k < centralSymbols.size(); k++) {
                for (Symbols t : Symbols.values()) {
                    if (t.getValue().equals(centralSymbols.get(k)))
                        centre[k] = t;
                }
            }

            Long points = (Long) goldCards.get("points");

            for (Symbols prova : Symbols.values()) {
                if (prova.getValue().equals(goldCards.get("pointSymbol")))
                    pointSymbol = prova;
            }

            String imageFront = (String) goldCards.get("imageFront");
            String imageBack = (String) goldCards.get("imageBack");
            Map<Symbols, Integer> requirements;
            JSONArray arrayRequirements = (JSONArray) goldCards.get("requirements");
            requirements = mapper.readValue(arrayRequirements.getFirst().toString(), new TypeReference<HashMap<Symbols, Integer>>() {});

            cardsG[i] = new GoldCard(symbols, color, null, true, points.intValue(), pointSymbol, imageFront, imageBack, requirements);

        }
        return new ArrayList<>(Arrays.asList(cardsG));
    }


    /**
     * method for creating the deck of starter cards using the json file
     * @param jsonFileName is the reference to the json file
     * @return the deck of starter cards
     * @throws ParseException if there is an error during the parsing of text input
     * @throws IOException if there is an input/output problem
     */
    public List<StarterCard> deckSInit(String jsonFileName) throws ParseException, IOException {
        StarterCard [] cardsS = new StarterCard[6];
        JSONParser parser = new JSONParser();
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream(jsonFileName);
        assert is != null;
        InputStreamReader readerCard = new InputStreamReader(is);
        Object objCard = parser.parse(readerCard);
        JSONObject cardjsonobject = (JSONObject) objCard;

        JSONArray arrayStarter = (JSONArray) cardjsonobject.get("StarterCard");

        for(int i=0;i<arrayStarter.size();i++) {
            Symbols[] symbolsFront = new Symbols[4];
            Symbols[] symbolsBack = new Symbols[4];
            Colors color = null;
            Symbols[] centre;
            JSONObject starterCards = (JSONObject) arrayStarter.get(i);

            for (Symbols t : Symbols.values()) {
                if (t.getValue().equals(starterCards.get("fCorner1")))
                    symbolsFront[0] = t;
                if (t.getValue().equals(starterCards.get("fCorner2")))
                    symbolsFront[1] = t;
                if (t.getValue().equals(starterCards.get("fCorner3")))
                    symbolsFront[2] = t;
                if (t.getValue().equals(starterCards.get("fCorner4")))
                    symbolsFront[3] = t;
                if (t.getValue().equals(starterCards.get("bCorner1")))
                    symbolsBack[0] = t;
                if (t.getValue().equals(starterCards.get("bCorner2")))
                    symbolsBack[1] = t;
                if (t.getValue().equals(starterCards.get("bCorner3")))
                    symbolsBack[2] = t;
                if (t.getValue().equals(starterCards.get("bCorner4")))
                    symbolsBack[3] = t;
            }
            for (Colors t : Colors.values()) {
                if (t.getValue().equals(starterCards.get("color")))
                    color = t;
            }

            String imageFront = (String) starterCards.get("imageFront");
            String imageBack = (String) starterCards.get("imageBack");

            JSONArray centralSymbols = (JSONArray) starterCards.get("centralSymbols");
            centre = new Symbols[centralSymbols.size()];
            for (int k = 0; k < centralSymbols.size(); k++) {
                for (Symbols t : Symbols.values()) {
                    if (t.getValue().equals(centralSymbols.get(k)))
                        centre[k] = t;
                }
            }

            cardsS[i] = new StarterCard(symbolsFront, color, centre, true, 0, Symbols.EMPTY, imageFront, imageBack, symbolsBack);
        }
        return new ArrayList<>(Arrays.asList(cardsS));
    }


    /**
     * method for creating the deck of objective cards using the json file
     * @param jsonFileName is the reference to the json file
     * @return the deck of objective cards
     * @throws ParseException if there is an error during the parsing of text input
     * @throws IOException if there is an input/output problem
     */
    public List<ObjectiveCard> deckOInit(String jsonFileName) throws ParseException, IOException {
        ObjectiveCard [] cardsO = new ObjectiveCard[16];
        JSONParser parser = new JSONParser();
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream(jsonFileName);
        assert is != null;
        InputStreamReader readerCard = new InputStreamReader(is);
        Object objCard = parser.parse(readerCard);
        JSONObject cardjsonobject = (JSONObject) objCard;

        JSONArray arrayObjective = (JSONArray) cardjsonobject.get("ObjectiveCard");

        for(int i=0;i<arrayObjective.size();i++) {
            JSONObject objectiveCards = (JSONObject) arrayObjective.get(i);
            Colors[] colors;
            int[] positions;
            Symbols[] symbols;
            Long points = (Long) objectiveCards.get("points");
            String type = (String) objectiveCards.get("type");
            String imageFront = (String) objectiveCards.get("imageFront");
            String imageBack = (String) objectiveCards.get("imageBack");

            switch (type) {
                case "ObCardPosition":
                    JSONArray arrayColors = (JSONArray) objectiveCards.get("colors");
                    colors = new Colors[arrayColors.size()];
                    for (int k = 0; k < arrayColors.size(); k++) {
                        for (Colors t : Colors.values()) {
                            if (t.getValue().equals(arrayColors.get(k)))
                                colors[k] = t;
                        }
                    }
                    JSONArray arrayPositions = (JSONArray) objectiveCards.get("positions");
                    positions = new int[arrayPositions.size()];
                    for (int k = 0; k < arrayPositions.size(); k++) {
                        positions[k] = ((Long) arrayPositions.get(k)).intValue();
                    }
                    cardsO[i] = new ObCardPosition(points.intValue(), imageFront, imageBack, colors, positions);
                    break;
                    case "ObCardSymbols":
                    JSONArray arraySymbols = (JSONArray) objectiveCards.get("symbols");
                    symbols = new Symbols[arraySymbols.size()];
                    for (int k = 0; k < arraySymbols.size(); k++) {
                        for (Symbols t : Symbols.values()) {
                            if (t.getValue().equals(arraySymbols.get(k)))
                                symbols[k] = t;
                        }
                    }
                    cardsO[i] = new ObCardSymbols(points.intValue(), imageFront, imageBack, symbols);
                    break;
            }
        }
        return new ArrayList<>(Arrays.asList(cardsO));
    }
}