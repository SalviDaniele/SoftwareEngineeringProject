package Network;

import Controller.GameController;
import Network.RMI.ServerRMI;
import Network.TCP.ServerTCP;

import java.net.*;
import java.io.*;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Enumeration;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static Network.RMI.ServerRMI.reg;

/**
 * This class represents the server which then instantiates ServerRMI and ServerTCP on 2 different ports.
 * The main method serves as the connecting procedure for the server.
 */
public class ConnectionServer {

    /**
     * Method used to display the name of the network interface and the IP address.
     * Provides also the server with the assigned IP address.
     *
     * @return the IP address for this computer
     */
    public static String getIP() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                    continue;
                }
                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    if (address.isLoopbackAddress() || !address.isSiteLocalAddress()) {
                        continue;
                    }
                    System.out.println("Network interface: " + networkInterface.getDisplayName());
                    System.out.println("Your IP address: " + address.getHostAddress());
                    return address.getHostAddress();
                }
            }
        } catch (SocketException e) {
            System.err.println("\nError while getting IP address: " + e.getMessage());
        }
        return null;
    }

    public static void main(String[] args) throws IOException {
        System.setProperty("java.rmi.server.hostname", Objects.requireNonNull(getIP()));
        ServerSocket    listenSocket   = null;
        CopyOnWriteArrayList<ClientData> clients = new CopyOnWriteArrayList<>();
        GameController controller = new GameController(0);
        ServerRMI obj = new ServerRMI(listenSocket, controller, clients);
        VirtualServer stub;
        try {
            stub = (VirtualServer) UnicastRemoteObject.exportObject(obj, 1099);
            reg(stub);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        // Bind the remote object's stub in the registry
        System.out.println("\nServer ready");

        try {
            listenSocket = new ServerSocket(1235);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("\nWaiting for a client ...");
        new ServerTCP(listenSocket, controller, clients).runServer();
    }
}