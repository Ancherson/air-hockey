package air.hockey.prototype.udp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;


    class MessageSender  implements Runnable{

        public final static int PORT = 6631;
        private String hostname = "localhost";
        private DatagramSocket socket;
        MessageSender(DatagramSocket s) throws InterruptedException {
            socket = s;
        }


        public void Message(String s) throws IOException {
            byte byteMessage[] = s.getBytes();
            InetAddress address = InetAddress.getByName(hostname);
            DatagramPacket msg = new DatagramPacket(byteMessage, byteMessage.length, address, PORT);
            socket.send(msg);
        }
        public void run(){
            boolean co = false;
            do{
                try{
                    Message("y a quelqu'un ?");
                    co=true;
                } catch (IOException e) {

                }

            } while (!co);
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        while (true){
            try{
                while(!in.ready()){
                    Thread.sleep(100);
                }
            Message(in.readLine());
        }catch(Exception e){
                System.out.println("erreur");
            }
        }
        }
    }
    public class ProtoClient{

            public static void main(String[] args) throws InterruptedException, SocketException {
                DatagramSocket socket = new DatagramSocket();
                MessageSender s = new MessageSender(socket);
                Thread rt = new Thread(s);
                rt.start();
    }
}

