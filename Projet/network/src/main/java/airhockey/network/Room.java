package airhockey.network;

import airhockey.model.Model;
import airhockey.model.Palet;
import airhockey.model.Pusher;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

/**
 * This class represents a room, a place that links two players
 */
public class Room {
    /**
     * id of the room
     */
    private String id;
    /**
     * socket of the server
     */
    private DatagramSocket serverSocket;
    /**
     * list of the clients port
     */
    private ArrayList<Integer> clientPorts;
    /**
     * list of the client addresses
     */
    private ArrayList<InetAddress> clientAddresses;
    /**
     * true if the room is full (2 players)
     */
    private boolean full = false;
    /**
     * true if the room is public
     */
    private boolean isPublic;
    /**
     * model of the game
     */
    private Model model;
    /**
     * true the room is running
     */
    private boolean isRunning = true;
    private boolean isClosing = false;

    /**
     * Constructor of the rooms
     * @param serverSocket socket of the server
     * @param id id of the room
     * @param isPublic public or not
     * @throws SocketException
     */
    public Room(DatagramSocket serverSocket, String id, boolean isPublic) throws SocketException {
        this.serverSocket = serverSocket;
        this.id = id;
        this.clientPorts = new ArrayList<Integer>();
        this.clientAddresses = new ArrayList<InetAddress>();
        this.isPublic = isPublic;
        this.model = new Model();
    }

    /**
     * get the id of the room
     * @return the id of the room
     */
    public String getId(){
        return id;
    }


    public Model getModel() {
        return model;
    }

    /**
     * get the isPublic of the room
     * @return the isPublic of the oom
     */
    public boolean isPublic() {
        return isPublic;
    }

    /**
     * get the full of the room
     * @return the full of the room
     */
    public boolean isFull() {
        return full;
    }

    public boolean isClosing() {
        return isClosing;
    }

    /**
     * Function that joins a player to the this room
     * @param port port of the client
     * @param address address of the client
     * @throws IOException
     */
    public void join(int port, InetAddress address) throws IOException {
        //IF THE ROOM IS FULL, SEND FULLROOM TO THE CLIENT
        if(clientPorts.size() == 2){
            byte[] buf = "fullRoom".getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
            serverSocket.send(packet);
            return;
        }
        //ADD THE CLIENT TO THE ROOM
        clientPorts.add(port);
        clientAddresses.add(address);

        //IF THE ROOM IS FULL, WE CAN START PLAYING
        if(clientPorts.size() == 2){
            full = true;
            byte[] buf = "start".getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, clientAddresses.get(0), clientPorts.get(0));
            serverSocket.send(packet);
            new Sender().start();
        }
    }

    /**
     * Function that receive the message of the client
     * @param ois message of the client
     * @param port port of the client
     * @param address address of the client
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void receive(ObjectInputStream ois, int port, InetAddress address) throws IOException, ClassNotFoundException {
        Pusher p = (Pusher)ois.readObject();
        boolean updatePalet = (boolean) ois.readObject();
        if(updatePalet){
            Palet palet = (Palet) ois.readObject();
            model.getBoard().setPalet(palet);
        }
        ois.close();
        int iClient = (port == clientPorts.get(0) && address.equals(clientAddresses.get(0))) ? 0 : 1;
        model.getBoard().getPushers()[iClient] = p;
        if (updatePalet){
        }
    }

    /**
     * Function that send the palet and the pusher to the clients
     * @throws IOException
     */
    public void sendPaletAndPushers() throws IOException {
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        ObjectOutput oo = new ObjectOutputStream(bStream);
        oo.writeBoolean(true);
        oo.writeBoolean(model.isFinished());
        oo.writeObject(model.getBoard().getPushers());
        oo.writeObject(model.getBoard().getPalet());
        oo.writeInt(model.getScore(0));
        oo.writeInt(model.getScore(1));
        oo.writeLong(System.nanoTime());
        oo.close();
        byte[] objectSerialized = bStream.toByteArray();
        int port = clientPorts.get(0);
        InetAddress address = clientAddresses.get(0);
        DatagramPacket packet = new DatagramPacket(objectSerialized, objectSerialized.length, address, port);
        serverSocket.send(packet);

        int port2 = clientPorts.get(1);
        InetAddress address2 = clientAddresses.get(1);
        DatagramPacket packet2 = new DatagramPacket(objectSerialized, objectSerialized.length, address2, port2);
        serverSocket.send(packet2);
    }

    /**
     * This class is Thread that sends the model to the client
     */
    public class Sender extends Thread {
        @Override
        public void run() {
            double lastT = System.nanoTime();
            double t;
            while(isRunning) {
                t = System.nanoTime();
                double dt = (t-lastT)/(1e9*1.0);
                model.update(dt);
                lastT = t;
                try {
                    sendPaletAndPushers();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(1000/40);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void endGame() {
        isClosing = true;
    }

    /**
     * Function that closes this room
     */
    public void close() {
        isRunning = false;
    }

}
