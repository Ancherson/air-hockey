package air.hockey.prototype.udp;

import air.hockey.prototype.model.Model;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

public class Server extends Thread {
    public final int SERVER_PORT = 6666;
    public final String hostname = "localhost";
    private DatagramSocket socket;

    private int nbClient;
    private ArrayList<Integer> clientPorts;
    private ArrayList<InetAddress> clientAddress;

    private Model model;

    public Server() throws SocketException {
        socket = new DatagramSocket(SERVER_PORT);
        clientAddress = new ArrayList<>();
        clientPorts = new ArrayList<>();
        model = new Model();
    }

    public void message(String content, InetAddress address, int port) throws IOException {
        byte[]message = content.getBytes();
        DatagramPacket packet = new DatagramPacket(message, message.length, address, port);
        socket.send(packet);
    }

    @Override
    public void run() {
        //TODO AVOIR DEUX JOUEURS CONNECTES ET STOCKER DANS LES LISTES LEUR PORT ET ADDRESSES
        try {
            while(nbClient < 2) {
                byte[] buf = new byte[9];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                String message = new String(buf);
                if (message.equals("connexion")) {
                    nbClient++;
                    System.out.println("TOTAL JOUEUR CONNECTE : " + nbClient);
                    clientPorts.add(packet.getPort());
                    clientAddress.add(packet.getAddress());
                    message("oui", packet.getAddress(), packet.getPort());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        //TODO LANCER LE THREAD SENDER
        //TODO LANCER LE THREAD RECEIVER

        //TODO LANCER UN WHILE TRUE QUI APPELLE UPDATE
    }

    public static void main(String[] args) throws SocketException {
        new Server().start();
    }

    public class Sender extends Thread {
        @Override
        public void run() {
            //TODO ENVOYER AU DEUX JOUEURS LA POSITION DU PALET
            //TODO ET SI LE PUSHER A BOUGE, ENVOYER LA POSITION DU PUSHER
        }
    }

    public class Receiver extends Thread {
        @Override
        public void run() {
            //TODO ACTUALISER LA POSITION DU PUSHER
        }
    }
}
