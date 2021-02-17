package air.hockey.prototype.udp;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ProtoClient{

    public static class MessageSender  implements Runnable{

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

    public static class PersonneSender implements Runnable {
        private final int SERVER_PORT = 6631;
        private String hostname = "localhost";
        private DatagramSocket socket;

        public PersonneSender(DatagramSocket d) {
            socket = d;
        }

        public void message(Personne p) throws IOException {
            ByteArrayOutputStream bStream = new ByteArrayOutputStream();
            ObjectOutput oo = new ObjectOutputStream(bStream);
            oo.writeObject(p);
            oo.close();
            byte[] personneSerialized = bStream.toByteArray();
            DatagramPacket packet = new DatagramPacket(personneSerialized, personneSerialized.length, InetAddress.getByName(hostname), SERVER_PORT);
            socket.send(packet);
        }

        public void wait(BufferedReader in) throws IOException, InterruptedException {
            while(!in.ready()){
                Thread.sleep(100);
            }
        }

        @Override
        public void run() {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            while(true) {
                try {
                    System.out.print("nom : ");
                    wait(in);
                    String nom = in.readLine();
                    System.out.print("prénom : ");
                    wait(in);
                    String prenom = in.readLine();
                    System.out.print("age : ");
                    int age = 0;
                    String a = "";
                    boolean pbm;
                    do {
                        pbm = false;
                        try {
                            wait(in);
                            a = in.readLine();
                            age = Integer.parseInt(a);
                        }catch (RuntimeException e) {
                            System.out.println("Un AGE SVP !!!");
                            pbm = true;
                        }
                    }while(pbm);
                    Personne p = new Personne(nom, prenom, age);
                    message(p);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException, SocketException {
        DatagramSocket socket = new DatagramSocket();
       // MessageSender s = new MessageSender(socket);
        PersonneSender ps = new PersonneSender(socket);
        Thread pst = new Thread(ps);
        pst.start();
//        Thread rt = new Thread(s);
//        rt.start();
    }
}

