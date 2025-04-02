package Network;

import java.io.Serializable;

/**
 * This class represents data of a specific client.
 * Contains a reference to a client as well as essential information such as their idMatch and their nickname.
 */
public class ClientData implements Serializable {

    /**
     * A reference to the client implementing the VirtualView interface.
     */
    private final VirtualView client;

    /**
     * Client's name.
     */
    private String nickname;

    /**
     * The ID of the match the client joined.
     */
    private int idMatch;


    /**
     * Constructor for the ClientData class.
     *
     * @param client refers to a specific client implementing the VirtualView interface
     * @param nickname refers to the client's name
     * @param idMatch refers to the ID of the match the client joined
     */
    public ClientData(VirtualView client, String nickname, int idMatch) {
        this.client = client;
        this.nickname = nickname;
        this.idMatch = idMatch;
    }


    /**
     * Getter method for the client attribute.
     *
     * @return a reference to the client
     */
    public VirtualView getClient() {
        return client;
    }


    /**
     * Getter method for the nickname attribute.
     *
     * @return the client's nickname
     */
    public String getNickname() {
        return nickname;
    }


    /**
     * Setter method for the nickname attribute.
     *
     * @param nickname of the client
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }


    /**
     * Getter method for the idMatch attribute.
     *
     * @return the idMatch for the match the client joined
     */
    public int getIdMatch() {
        return idMatch;
    }


    /**
     * Setter method for the idMatch attribute.
     *
     * @param idMatch of the match the client joined
     */
    public void setIdMatch(int idMatch) {
        this.idMatch = idMatch;
    }
}
