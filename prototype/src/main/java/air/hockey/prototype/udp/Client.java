package air.hockey.prototype.udp;

import air.hockey.prototype.javafx.ProtoPhysicJavaFx;
import air.hockey.prototype.model.Model;
import air.hockey.prototype.model.Palet;
import air.hockey.prototype.model.Pusher;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Client {
    private String hostname = "localhost";
    public final int SERVER_PORT = 6666;
    private int convPort;
    private DatagramSocket socket;
    private Model model;
    private int id;

    public Client(Model m) throws IOException {
        socket = new DatagramSocket();
        model = m;

        byte[]msgFirstCo = {(byte)1};
        DatagramPacket packet = new DatagramPacket(msgFirstCo, msgFirstCo.length, InetAddress.getByName(hostname),SERVER_PORT);
        socket.send(packet);
        
        byte[]rep = new byte[4];
        packet = new DatagramPacket(rep, rep.length);
        socket.receive(packet);
        convPort = Integer.parseInt(new String(rep));

        String connect = "connexion";
        byte[] msgco = connect.getBytes();
        DatagramPacket msg = new DatagramPacket(msgco,msgco.length, InetAddress.getByName(hostname),convPort);
        socket.send(msg);

        byte[] buf = new byte[1];
        packet = new DatagramPacket(buf, buf.length);
        System.out.println("ATTEND REPONSE DU SERVEUR");
        socket.receive(packet);
        System.out.println("J'AI RECU");
        String res = new String(buf);
        if(res.equals("1")){
            id = 1;
            model.swapPushers();
        } else {
            id = 0;
        }
        System.out.println(res);
        new Sender().start();
        new Receiver().start();
    }

    public void stopConnexion() {
        this.socket.close();

    }

    public void sendPusher() throws IOException {
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        ObjectOutput oo = new ObjectOutputStream(bStream);
        oo.writeObject(model.getPushers()[0]);
        oo.close();
        byte[] pusherSerialized = bStream.toByteArray();
        DatagramPacket packet = new DatagramPacket(pusherSerialized, pusherSerialized.length, InetAddress.getByName(hostname), convPort);
        socket.send(packet);
    }

    public void receivePusher() throws IOException, ClassNotFoundException {
        byte[]buf = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(packet.getData()));
        Pusher p = (Pusher)ois.readObject();
        ois.close();
        model.getPushers()[1] = p;
    }

    public void receiveModel() throws IOException, ClassNotFoundException {
        //System.out.println("J'AI RECU MODELE");
        byte[]buf = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(packet.getData()));
        Pusher p = ((Pusher[])ois.readObject())[1-id];
        Palet pa = (Palet)ois.readObject();
        ois.close();
        model.getPushers()[1] = p;
        model.setPalet(pa);
    }

    public static void main(String[] args) throws IOException {
        ProtoPhysicJavaFx.launch(ProtoPhysicJavaFx.class, args);
    }

    public class Sender extends Thread {

        @Override
        public void run() {
            while (!socket.isClosed()) {
                try {
                    Thread.sleep(1000/40);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized(model) {
                        if (model.hasPusherMoved()) {
                            //System.out.println("J'ENVOIE LA POSITION DU PUSHER");
                            try {
                                sendPusher();
                            } catch (IOException e) {
                                //e.printStackTrace();
                            }
                        }
                }
            }
        }
    }


    public class Receiver extends Thread {

        @Override
        public void run() {
            while(!socket.isClosed()) {
                //TODO RECOIT LA VRAIE POSITION DU PALET ET L'ACTUALISE
                try {
                    //System.out.println("J'ATTEND DE RECEVOIR LE PUSHER");
                    receiveModel();
                    //System.out.println("J'AI RECU LE PUSHER");
                } catch (IOException e) {
                    //e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    //e.printStackTrace();
                }
            }
        }
    }

}
