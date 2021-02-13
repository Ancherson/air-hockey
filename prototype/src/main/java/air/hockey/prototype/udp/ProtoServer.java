package air.hockey.prototype.udp;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;

public class ProtoServer extends Thread {
    public final int PORT = 6631;
    private final int BUFFER = 1024;

    private DatagramSocket socket;
    private ArrayList<InetAddress>clientAddress;

    public ProtoServer() throws SocketException {
        socket = new DatagramSocket(PORT);
        clientAddress = new ArrayList<>();
    }

    public void receiveMessage(byte[]buf, DatagramPacket packet) {
        String content = new String(buf, buf.length);

        InetAddress address = packet.getAddress();
        System.out.println(packet.getPort());

        if(!clientAddress.contains(address)) {
            clientAddress.add(address);
        }

        System.out.println(address + " : " + content);
    }

    @Override
    public void run() {
        System.out.println("Serveur Lancé");
        byte[]buf = new byte[BUFFER];
        try {
            while(true) {
                Arrays.fill(buf, (byte) 0);
                DatagramPacket packet = new DatagramPacket(buf, buf.length);

                socket.receive(packet);
                //receiveMessage(buf,packet);

                ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(packet.getData()));
                Personne p = (Personne)ois.readObject();
                ois.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[]args) throws SocketException {
        new ProtoServer().start();
    }

}
