package air.hockey.prototype.udp;

import air.hockey.prototype.model.Model;
import air.hockey.prototype.model.Pusher;

import java.io.*;
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
    private boolean haveReceivedPusher = false;
    private int iClient = 0;

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
            System.out.println("########################## SERVEUR LANCE ##########################");
            while(nbClient < 2) {
                byte[] buf = new byte[9];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                String message = new String(buf);
                if (message.equals("connexion")) {
                    nbClient++;
                    clientAddress.add(packet.getAddress());
                    clientPorts.add(packet.getPort());
                    System.out.println("TOTAL JOUEUR CONNECTE : " + nbClient);
                }
            }

            for(int i = 0; i < clientAddress.size(); i++) {
                message("oui", clientAddress.get(i), clientPorts.get(i));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("FIN INITIALISATION !");

        new Sender().start();
        new Receiver().start();

        //TODO LANCER LE THREAD SENDER
        //TODO LANCER LE THREAD RECEIVER

        //TODO LANCER UN WHILE TRUE QUI APPELLE UPDATE
    }

    public static void main(String[] args) throws SocketException {
        new Server().start();
    }

    public void receivePusher() throws IOException, ClassNotFoundException {
        byte[]buf = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(packet.getData()));
        Pusher p = (Pusher)ois.readObject();
        ois.close();
        model.getPushers()[0] = p;
        if(packet.getPort() == clientPorts.get(0)) iClient = 0;
        else iClient = 1;
        haveReceivedPusher = true;
    }

    public void sendPusher() throws IOException {
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        ObjectOutput oo = new ObjectOutputStream(bStream);
        oo.writeObject(model.getPushers()[0]);
        oo.close();
        byte[] pusherSerialized = bStream.toByteArray();
        int port = clientPorts.get(iClient);
        InetAddress address = clientAddress.get(iClient);
        DatagramPacket packet = new DatagramPacket(pusherSerialized, pusherSerialized.length, address, port);
        socket.send(packet);
    }

    public class Sender extends Thread {
        @Override
        public void run() {
            //TODO ENVOYER AU DEUX JOUEURS LA POSITION DU PALET
            //TODO ET SI LE PUSHER A BOUGE, ENVOYER LA POSITION DU PUSHER
            while(true) {
                try {
                    System.out.println("J'ATTENDS DE RECECOIR LE PUSHER");
                    receivePusher();
                    System.out.println("J'EN AI RECU UN");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class Receiver extends Thread {
        @Override
        public void run() {
            //TODO ACTUALISER LA POSITION DU PUSHER
            while(true) {
                if(haveReceivedPusher) {
                    haveReceivedPusher = false;
                    try {
                        System.out.println("J'ENVOIE LE PUSHER");
                        sendPusher();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
