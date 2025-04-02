package Network.TCP;

/**
 * Enumerations indicating the type of MessageTCP the ClientProxy sends to the ClientTCP.
 */
public enum MessageType {

    BOOLEAN("BOOLEAN"),
    INTEGER("INTEGER"),
    POSITIONS("POSITIONS"),
    UPDATE("UPDATE"),
    COLORS("COLORS"),
    STRING("STRING"),
    TABLE("TABLE"),
    CONTROLLER("CONTROLLER"),
    NAMES("NAMES"),
    CARDS("CARDS"),
    TURN("TURN"),
    CHAT("CHAT"),
    PRIVATE_CHAT("PRIVATE CHAT"),
    PING("PING"),
    EXIT("EXIT");


    /**
     * A string related to the type of message.
     */
    private final String value;


    /**
     * Constructor of the MessageType class.
     *
     * @param value represents which message needs to be generated
     */
    MessageType(String value) {
        this.value = value;
    }


    /**
     * Getter method for the value attribute.
     *
     * @return the string that represents which type of message will be generated
     */
    public String getValue() {
        return value;
    }
}
