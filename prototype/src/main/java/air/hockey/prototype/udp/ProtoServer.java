package air.hockey.prototype.udp;

import java.io.IOException;
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

    @Override
    public void run() {
        byte[]buf = new byte[BUFFER];
        try {
            while(true) {
                Arrays.fill(buf, (byte) 0);
                DatagramPacket packet = new DatagramPacket(buf, buf.length);

                socket.receive(packet);

                String content = new String(buf, buf.length);

                InetAddress address = packet.getAddress();
                System.out.println(packet.getPort());

                if(!clientAddress.contains(address)) {
                    clientAddress.add(address);
                }

                System.out.println(address + " : " + content);

//                byte[]data = (address + " : " + content).getBytes();

//                for(int i = 0; i < clientAddress.size(); i++) {
//                    InetAddress ci = clientAddress.get(i);
//
//                }


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[]args) throws SocketException {
        new ProtoServer().start();
    }

}
