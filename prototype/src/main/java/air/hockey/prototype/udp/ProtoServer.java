package air.hockey.prototype.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class ProtoServer extends Thread {
    public final int PORT = 6631;
    private final int BUFFER = 1024;

    private DatagramSocket socket;

    public ProtoServer() throws SocketException {
        socket = new DatagramSocket(PORT);
    }

    @Override
    public void run() {
        byte[]buf = new byte[BUFFER];
        while(true) {
            
        }
    }

    public static void main(String[]args) throws SocketException {
        new ProtoServer().start();
    }

}
