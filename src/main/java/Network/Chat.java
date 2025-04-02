package Network;

import Listeners.GuiStatusModifier;
import View.Gui.Gui;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static Network.RMI.ClientRMI.RESET;

/**
 * This class represents the chat service.
 * It includes a blocking queue containing messages and adds incoming messages to the said queue.
 */
public class Chat extends GuiStatusModifier<Gui> implements Runnable{

    /**
     * A blocking queue containing all the incoming chat messages for a user playing via Cli.
     */
    private final BlockingQueue<String> messageQueueCli;

    /**
     * A blocking queue containing all the incoming chat messages for a user playing via Gui.
     */
    private final BlockingQueue<String> messageQueueGui;

    /**
     * A blocking queue containing all the incoming chat messages displayed as a notification for a user playing via Gui.
     */
    private final BlockingQueue<String> notificationsQueueGui;

    /**
     * True if the user is playing via Gui, false otherwise
     */
    boolean viewType = false;


    /**
     * Constructor for the Chat class.
     * Creates a new blocking queue where to add the incoming messages for each blocking queues attributes.
     */
    public Chat() {
        this.messageQueueCli = new LinkedBlockingQueue<>();
        this.messageQueueGui = new LinkedBlockingQueue<>();
        this.notificationsQueueGui = new LinkedBlockingQueue<>();
    }


    /**
     * Constructor for the Chat class.
     * Creates a new blocking queue where to add the incoming messages for each blocking queues attributes.
     *
     * @param viewType sets the viewType attribute to true if the user is playing via Gui, false otherwise.
     */
    public Chat(boolean viewType) {
        this.viewType = viewType;
        this.messageQueueCli = new LinkedBlockingQueue<>();
        this.messageQueueGui = new LinkedBlockingQueue<>();
        this.notificationsQueueGui = new LinkedBlockingQueue<>();
    }


    /**
     * Getter method for the messageQueueGui attribute.
     *
     * @return all the incoming chat messages for a user playing via Gui.
     */
    public BlockingQueue<String> getMessageQueueGui() {
        return messageQueueGui;
    }


    /**
     * Adds an incoming chat message to the blocking queue.
     *
     * @param chatMessage is the arrived message
     */
    public void addMessage(String chatMessage) {
        if(viewType) {
            notificationsQueueGui.add(handleMessage(chatMessage));
        }
        else
            messageQueueCli.add(chatMessage);
    }


    /**
     * Method to handle an incoming message.
     * If a message is private it calls the method to handle a private message.
     * If a message is public it calls the method to handle a public message.
     *
     * @param msg is the incoming message.
     * @return the new message handled.
     */
    public String handleMessage(String msg){
        if(isPrivateMessage(msg))
            return handlePrivateMessage(msg);
        else
            return handlePublicMessage(msg);
    }


    /**
     * A method that checks if an incoming message is private
     *
     * @param msg is the incoming message
     * @return true if the message is private, false otherwise
     */
    public boolean isPrivateMessage(String msg){
        return msg.contains("(PRIVATE)");
    }


    /**
     * Method to handle a private message. The sender and the body of the message are extracted from the message,
     * they are added to a new string easier to read for the Gui.
     *
     * @param msg is the incoming message.
     * @return return the new message handled.
     */
    public String handlePrivateMessage(String msg){
        String sender = extractSenderFromPrivateMessage(msg);
        String chatMessage = extractChatMessageFromPrivateMessage(msg);

        return "Private message from " + sender + ": " + chatMessage;
    }


    /**
     * A method that extracts the body of the message from a private message
     *
     * @param msg is the complete message
     * @return the body of the message
     */
    private String extractChatMessageFromPrivateMessage(String msg) {
        int start = msg.indexOf(" to you:") + " to you:".length();
        return msg.substring(start).trim();
    }


    /**
     * A method that extract the name of the sender from a private message
     *
     * @param msg is the incoming message
     * @return the name of the sender
     */
    private String extractSenderFromPrivateMessage(String msg) {
        int start = msg.indexOf(RESET) + RESET.length();
        int end = msg.indexOf(" to you:");
        return msg.substring(start, end).trim();
    }


    /**
     * Method to handle a public message. The sender and the body of the message are extracted from the message,
     * they are added to a new string easier to read for the Gui.
     *
     * @param msg is the incoming message.
     * @return return the new message handled.
     */
    public String handlePublicMessage(String msg){
        String sender = extractSenderFromPublicMessage(msg);
        String chatMessage = extractChatMessageFromPublicMessage(msg);

        return "Public message from " + sender + " to everybody: " + chatMessage;
    }


    /**
     * A method that extracts the body of the message from a public message
     *
     * @param msg is the complete message
     * @return the body of the message
     */
    private String extractChatMessageFromPublicMessage(String msg) {
        int start = msg.indexOf(" to everybody:") + " to everybody:".length();
        return msg.substring(start).trim();
    }


    /**
     * A method that extract the name of the sender from a public message
     *
     * @param msg is the incoming message
     * @return the name of the sender
     */
    private String extractSenderFromPublicMessage(String msg) {
        int start = msg.indexOf("\n\n") + 2;
        int end = msg.indexOf(" to everybody:");
        return msg.substring(start, end).trim();
    }


    /**
     * Overrides the run method to listen for incoming chat messages.
     * If viewType is false, when a message arrives it is displayed on the Cli for the client to see it.
     * If viewType is true, it notifies the Gui that a message has arrived,
     * then the message is added to a BlockingQueue for the Gui.
     */
    @Override
    public void run() {
        while (true) {
            if(!viewType){
                try {
                    String chatMessage = messageQueueCli.take();
                    System.out.println(chatMessage);
                    System.out.print("\nInput command: ");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            else{
                try {
                    String chatMessage = notificationsQueueGui.take();
                    modifyStatus(target -> target.chatNotification());
                    messageQueueGui.add(chatMessage);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }
}
