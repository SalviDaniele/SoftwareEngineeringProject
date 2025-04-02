package Network;

import Network.RMI.ClientRMI;
import Network.TCP.ClientTCP;
import Network.TCP.ServerProxy;

import java.net.*;
import java.io.*;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Enumeration;
import java.util.Objects;
import java.util.Scanner;

/**
 * Class with a main method which serves as the connecting procedure for the client.
 */
public class ConnectionClient {

    /**
     * Method used to display the name of the network interface and the IP address.
     * Provides also the client with the assigned IP address.
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


    public static void main(String[] args) throws RemoteException {
        System.setProperty("java.rmi.server.hostname", Objects.requireNonNull(getIP()));
        Scanner in = new Scanner(System.in);
        String line = "";
        String viewType;
        System.out.print("\nPlease, choose a view type by typing 'cli' or 'gui': ");
        while (!line.equalsIgnoreCase("cli") && !line.equalsIgnoreCase("gui")) {
            line = in.nextLine();
            if (!line.equalsIgnoreCase("cli") && !line.equalsIgnoreCase("gui"))
                System.err.print("\nSorry, you need to choose a view type by typing 'cli' or 'gui': ");
        }
        viewType = line;

        Socket socket;
        VirtualServer stub;
        Registry registry;
        System.out.print("\nPlease, choose a connection protocol by typing 'tcp' (or 'socket') or 'rmi': ");
        while (!line.equalsIgnoreCase("socket") && !line.equalsIgnoreCase("tcp") && !line.equalsIgnoreCase("rmi")) {
            line = in.nextLine();
            if (!line.equalsIgnoreCase("socket") && !line.equalsIgnoreCase("tcp") && !line.equalsIgnoreCase("rmi"))
                System.err.print("\nSorry, you need to choose a connection protocol by typing 'tcp', 'socket' or 'rmi': ");
        }
        if(line.equalsIgnoreCase("socket") || line.equalsIgnoreCase("tcp")){
            try {
                String host;
                System.out.print("\nEnter an ip address: ");
                host = in.nextLine();
                socket = new Socket(host, 1235);
                System.out.println("\nYou have chosen to play using the TCP connection protocol.");
                InputStreamReader socketRx = new InputStreamReader(socket.getInputStream());
                OutputStreamWriter socketTx = new OutputStreamWriter(socket.getOutputStream());
                ClientTCP clientTCP = new ClientTCP(viewType);
                clientTCP.setSocket(socket);
                clientTCP.setInput(new BufferedReader(socketRx));
                clientTCP.setServer(new ServerProxy(new BufferedWriter(socketTx)));
                clientTCP.run();
            }
            catch (IOException e) {
                System.err.println("\nError: " + e.getMessage());
            }
        }
        else if(line.equalsIgnoreCase("rmi")){
            String host;
            System.out.print("\nEnter an ip address: ");
            host = in.nextLine();
            System.out.println("\nYou have chosen to play using the RMI connection protocol.");
            try {
                registry = LocateRegistry.getRegistry(host, 1099);
                stub = (VirtualServer) registry.lookup("VirtualServer");
            } catch (RemoteException | NotBoundException e) {
                throw new RuntimeException(e);
            }
            ClientRMI c = new ClientRMI(viewType);
            c.setServer(stub);
            c.run();
        }
    }
}