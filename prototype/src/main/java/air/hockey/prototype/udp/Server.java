package air.hockey.prototype.udp;

import air.hockey.prototype.model.Model;
import air.hockey.prototype.model.Pusher;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.ArrayList;

public class Server extends Thread {

    public final int SERVER_PORT = 6666;
    public final String hostname = "localhost";
    private static int totConv = 0;
    private DatagramSocket serverSocket;
    private boolean convWaiting = false;

    public Server() throws SocketException {
        serverSocket = new DatagramSocket(SERVER_PORT);
    }

    @Override
    public void run() {
        while (true) {
            System.out.println("GROS SERVEUR LANCE !!!!");
            try {
                byte[] buf = new byte[1];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                serverSocket.receive(packet);
                if(buf[0] == 1) {
                    if(!convWaiting) {
                        System.out.println("Création d'une Conversation !");
                        totConv++;
                        new Conversation(SERVER_PORT + totConv).start();
                        convWaiting = true;
                    }else convWaiting = false;
                    buf = ((SERVER_PORT + totConv) + "").getBytes();
                    packet = new DatagramPacket(buf, buf.length, packet.getAddress(), packet.getPort());
                    serverSocket.send(packet);  
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
        }
    }

    public static void main(String[] args) throws SocketException {
       new Server().start(); 
    }  

    public class Conversation extends Thread {
    
        private DatagramSocket socket;
        private int myPort;
        private int nbClient;
        private ArrayList<Integer> clientPorts;
        private ArrayList<InetAddress> clientAddress;
    
        private Model model;
        private boolean haveReceivedPusher = false;
        private int iClient = 0;
    
        public Conversation(int port) throws SocketException {
            myPort = port;
            socket = new DatagramSocket(myPort);
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
                    message(i+"", clientAddress.get(i), clientPorts.get(i));
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
            double lastT = System.nanoTime();
            double t;
            while(true){
                t = System.nanoTime();
                double dt = (t-lastT)/(1e9*1.0);
                model.update(dt);
                lastT = t;
    
            }
        }
    
        public void receivePusher() throws IOException, ClassNotFoundException {
            byte[]buf = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(packet.getData()));
            Pusher p = (Pusher)ois.readObject();
            ois.close();
            if(packet.getPort() == clientPorts.get(0)) iClient = 1;
            else iClient = 0;
            model.getPushers()[1-iClient] = p;
            haveReceivedPusher = true;
        }
    
        public void sendObjects() throws IOException {
            ByteArrayOutputStream bStream = new ByteArrayOutputStream();
            ObjectOutput oo = new ObjectOutputStream(bStream);
            oo.writeObject(model.getPushers());
            oo.writeObject(model.getPalet());
            oo.close();
            byte[] objectSerialized = bStream.toByteArray();
            int port = clientPorts.get(0);
            InetAddress address = clientAddress.get(0);
            DatagramPacket packet = new DatagramPacket(objectSerialized, objectSerialized.length, address, port);
            socket.send(packet);
    
            int port2 = clientPorts.get(1);
            InetAddress address2 = clientAddress.get(1);
            DatagramPacket packet2 = new DatagramPacket(objectSerialized, objectSerialized.length, address2, port2);
            socket.send(packet2);
        }
    
        public<T extends Serializable> void sendObject(T t) throws IOException {
            ByteArrayOutputStream bStream = new ByteArrayOutputStream();
            ObjectOutput oo = new ObjectOutputStream(bStream);
            oo.writeObject(t);
            oo.close();
            byte[] objectSerialized = bStream.toByteArray();
            int port = clientPorts.get(iClient);
            InetAddress address = clientAddress.get(iClient);
            DatagramPacket packet = new DatagramPacket(objectSerialized, objectSerialized.length, address, port);
            socket.send(packet);
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
    
    
        public class Receiver extends Thread {
            @Override
            public void run() {
                //TODO ENVOYER AU DEUX JOUEURS LA POSITION DU PALET
                //TODO ET SI LE PUSHER A BOUGE, ENVOYER LA POSITION DU PUSHER
                while(true) {
    
                    try {
                        //System.out.println("J'ATTENDS DE RECECOIR LE PUSHER");
                        receivePusher();
                        //System.out.println("J'EN AI RECU UN");
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    
        public class Sender extends Thread {
            @Override
            public void run() {
                //TODO ACTUALISER LA POSITION DU PUSHER
                while(true) {
                    try {
                        Thread.sleep(1000/40);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //System.out.print("");
                        try {
                            //System.out.println("J'ENVOIE LE PUSHER");
                            sendObjects();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                }
            }
        }
    }

}
