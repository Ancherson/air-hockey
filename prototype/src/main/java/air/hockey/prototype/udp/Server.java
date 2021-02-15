package air.hockey.prototype.udp;

import air.hockey.prototype.model.Model;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

public class Server extends Thread {
    public final int SERVER_PORT = 6666;
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

    @Override
    public void run() {
        //TODO AVOIR DE JOUEUR CONNECTER ET STOCKER DANS LES LISTES LEUR PORT ET ADDRESSES

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
