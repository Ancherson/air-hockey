package airhockey.network;

import airhockey.model.Model;
import airhockey.model.Palet;
import airhockey.model.Pusher;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.function.Consumer;

/**
 * This class represents all the network part of the client's side
 * This is where the client is linked to the server
 */

public class Client{
    /**
     * id of the room
     */
    private String id;
    /**
     * socket of the client
     */
    private DatagramSocket socket;
    /**
     * model of the game
     */
    private Model model;
    /**
     * if you are player 0 or player 1 (left 0/right 1 on the board)
     */
    private int numPlayer;

    /**
     * Functions of the gui
     */
    private Consumer<Runnable> runLater;
    private Consumer<String> setID;
    private Runnable connect;
    private Runnable lostConnexion;

    /**
     * Constuctor of a Client
     * @param m the model of the gamr
     * @param runlater function Platform.runLater(Runnable) of javafx
     * @param setID function to change the ID on the CreateMenu
     * @param connect function to change the message on the PublicWait menu
     * @param lostConnexion function to print that the connexion is lost (the other player is gone)
     * @throws SocketException
     */
    public Client(Model m, Consumer<Runnable> runlater, Consumer<String> setID, Runnable connect, Runnable lostConnexion) throws SocketException {
        socket = new DatagramSocket();
        model = m;
        this.runLater = runlater;
        this.setID = setID;
        this.connect = connect;
        this.lostConnexion = lostConnexion;
    }

    /**
     * Function to create a room
     * @throws IOException
     */
    public void createRoom() throws IOException {
        //SEND THAT WE WANT TO CREATE A ROMM
        numPlayer = 0;
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        ObjectOutput oo = new ObjectOutputStream(bStream);
        oo.writeUTF("creer");
        oo.close();
        byte[] message = bStream.toByteArray();
        DatagramPacket packet = new DatagramPacket(message, message.length, InetAddress.getByName(Server.HOSTNAME), Server.PORT);
        socket.send(packet);

        //WE RECUPE TE ID OF THE ROOM
        byte[] idBuff = new byte[Server.ID_LENGTH];
        packet = new DatagramPacket(idBuff, idBuff.length);
        socket.receive(packet);
        id = new String(idBuff);

        //WE CHANGE THE ID ON THE CREATE-MENU
        runLater.accept(() -> {
            setID.accept(id);
        });
        System.out.println(id);

        //WE WAIT THAT AN OTHER PLAYER
        byte[] buff = new byte[5];
        packet = new DatagramPacket(buff, buff.length);
        socket.receive(packet);
        String msg = new String(buff);
        System.out.println(msg);
        if(msg.equals("start")){
            startGame();
        }
    }

    /**
     * Function to join a room
     * @param id the id of the room
     * @throws IOException
     */
    public void joinRoom(String id) throws IOException {
        //WE SEND A MESSAGE THAT SAYS WE WANT TO JOIN A ROOM
        numPlayer = 1;
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        ObjectOutput oo = new ObjectOutputStream(bStream);
        oo.writeUTF("rejoindre");
        oo.writeUTF(id);
        oo.close();
        byte[] message = bStream.toByteArray();
        DatagramPacket packet = new DatagramPacket(message, message.length, InetAddress.getByName(Server.HOSTNAME), Server.PORT);
        socket.send(packet);

        //WE WAIT A RESPONSE OF THE SERVER
        //IF yesRoom WE START THE GAME
        //ELSE WE PRINT DIFFERENT MESSAGE
        byte[] buff = new byte[8];
        packet = new DatagramPacket(buff, buff.length);
        socket.receive(packet);
        String msg = new String(buff);
        switch(msg){
            case "yesRoom ":
                this.id = id;
                startGame();
                break;

            //TODO HANDLE noRoom AND fullRoom
        }
    }

    /**
     * Function to join a public room
     * @throws IOException
     */
    public void joinRoomPublic() throws IOException {
        //WE SEND THAT WE WANT TO JOIN A PUBLIC ROOM
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        ObjectOutput oo = new ObjectOutputStream(bStream);
        oo.writeUTF("public");
        oo.close();
        byte[] message = bStream.toByteArray();
        DatagramPacket packet = new DatagramPacket(message, message.length, InetAddress.getByName(Server.HOSTNAME), Server.PORT);
        socket.send(packet);

        //WE RECUPE A MESSAGE THAT SAYS IF WE ARE THE FIRST OR THE SECOND PLAYER OF THE ROOM
        byte[] msg = new byte[1];
        packet = new DatagramPacket(msg, msg.length);
        socket.receive(packet);
        id = new String(msg);
        if(id.equals("0")) numPlayer = 0;
        if(id.equals("1")) numPlayer = 1;

        runLater.accept(connect);


        byte[] idBuff = new byte[Server.ID_LENGTH];
        packet = new DatagramPacket(idBuff, idBuff.length);
        socket.receive(packet);
        id = new String(idBuff);

        //IF WE ARE THE SECOND PLAYER, WE CAN START
        if(numPlayer == 1) {
            startGame();
            return;
        }

        //WE WAIT THE SECOND PLAYER TO START
        byte[] buff = new byte[5];
        packet = new DatagramPacket(buff, buff.length);
        socket.receive(packet);
        String res = new String(buff);
        System.out.println(res);
        if(res.equals("start")){
            startGame();
        }

    }

    /**
     * Function that start the two threads (Sender and Receiver)
     */
    public void startGame(){
        new Sender().start();
        new Receiver().start();
    }

    /**
     * Function that send our pusher to the server
     * @throws IOException
     */
    public void sendPusher() throws IOException {
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        ObjectOutput oo = new ObjectOutputStream(bStream);
        oo.writeUTF(id);
        oo.writeObject(model.getBoard().getPushers()[numPlayer]);
        oo.writeObject(false);
        oo.close();
        byte[] pusherSerialized = bStream.toByteArray();
        DatagramPacket packet = new DatagramPacket(pusherSerialized, pusherSerialized.length, InetAddress.getByName(Server.HOSTNAME), Server.PORT);
        socket.send(packet);
    }

    /**
     * Function that send the pusher and the the palet to the server
     * @throws IOException
     */
    public void sendPusherAndPalet() throws IOException{
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        ObjectOutput oo = new ObjectOutputStream(bStream);
        oo.writeUTF(id);
        oo.writeObject(model.getBoard().getPushers()[numPlayer]);
        oo.writeObject(true);
        oo.writeObject(model.getBoard().getPalet());
        oo.close();
        byte[] pusherSerialized = bStream.toByteArray();
        DatagramPacket packet = new DatagramPacket(pusherSerialized, pusherSerialized.length, InetAddress.getByName(Server.HOSTNAME), Server.PORT);
        socket.send(packet);
    }

    /**
     * Function that receive the model of the server
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void receiveModel() throws IOException, ClassNotFoundException {
        byte[]buf = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(packet.getData()));
        boolean haveRoom = ois.readBoolean();
        if(!haveRoom) {
            runLater.accept(lostConnexion);
            return;
        }
        Pusher p = ((Pusher[])ois.readObject())[1-numPlayer];
        Palet pa = (Palet)ois.readObject();
        int score1 = ois.readInt();
        int score2 = ois.readInt();
        ois.close();
        model.getBoard().getPushers()[1-numPlayer] = p;
        model.getBoard().setPalet(pa);
        model.setScore(0, score1);
        model.setScore(1, score2);
    }

    /**
     * This class is a thread that send our model to the server
     */
    public class Sender extends Thread{
        @Override
        public void run() {
            while(!socket.isClosed()){
                try {
                    Thread.sleep(1000 / 40);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (model) {
                    if (model.hasPusherMoved()) {
                        if(model.hasPalletCollided()){
                            try{
                                sendPusherAndPalet();
                            } catch (IOException e) {
                                System.out.println("Socket Closed");
                            }
                        }
                        else {
                            try {
                                sendPusher();
                            } catch (IOException e) {
                                System.out.println("Socket Closed");
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * This class is a thread tha receive the model of the server
     */
    public class Receiver extends Thread{
        @Override
        public void run() {
            while(!socket.isClosed()){
                try {
                    receiveModel();
                } catch (IOException e) {
                    System.out.println("Socket Closed !");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Function that close the socket and the two threads (Sender and Receiver)
     * @throws IOException
     */
    public void close() throws IOException {
        if (!socket.isClosed()) {
            if (id != null) {
                ByteArrayOutputStream bStream = new ByteArrayOutputStream();
                ObjectOutput oo = new ObjectOutputStream(bStream);
                oo.writeUTF("close");
                oo.writeUTF(id);
                oo.close();
                byte[] message = bStream.toByteArray();
                DatagramPacket packet = new DatagramPacket(message, message.length, InetAddress.getByName(Server.HOSTNAME), Server.PORT);
                socket.send(packet);
            }
            socket.close();
        }
    }

    /**
     * Function to get the numPlayer
     * @return
     */
    public int getNumPlayer() {
        return numPlayer;
    }
}
