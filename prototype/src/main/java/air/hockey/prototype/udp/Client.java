package air.hockey.prototype.udp;

import air.hockey.prototype.javafx.ProtoPhysicJavaFx;
import air.hockey.prototype.model.Model;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Client {
    private String hostname = "localhost";
    public final int SERVER_PORT = 6666;
    private ProtoPhysicJavaFx game;
    public Client(String[]args) throws IOException {
        DatagramSocket socket = new DatagramSocket();
        game = new ProtoPhysicJavaFx();

        String connect = "connexion";
        byte[] msgco = connect.getBytes();
        DatagramPacket msg = new DatagramPacket(msgco,msgco.length, InetAddress.getByName(hostname),SERVER_PORT);
        socket.send(msg);

        byte[] buf = new byte[3];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
        String res = new String(buf);
        System.out.println(res);
        //new Sender(socket).start();
        //new Receiver(socket).start();
        //ProtoPhysicJavaFx.launch(ProtoPhysicJavaFx.class, args);
    }

    public static void main(String[] args) throws IOException {
        new Client(args);
    }

    public class Sender extends Thread {
        private DatagramSocket socket;
        public Sender(DatagramSocket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            while (true) {
                Model model = game.getModel();
                if(model.hasPusherMoved()) {
                    //TODO ENVOYER AU SERVEUR LA NOUVELLE POSITION DU PUSHER
                }
            }
        }
    }

    public class Receiver extends Thread {
        private DatagramSocket socket;
        public Receiver(DatagramSocket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            while(true) {
                //TODO RECOIT LA VRAIE POSITION DU PALET ET L'ACTUALISE, PEUT RECEVOIR LA POSITION DU PUSHER
            }
        }
    }

}
