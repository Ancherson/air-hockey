package airhockey.network;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.*;
import java.util.ArrayList;

public class Server extends Thread {
    public final static int PORT = 6666;
    public final static String HOSTNAME = "localhost";
    public static int ID_LENGTH = 10;

    private ArrayList<Room> rooms;
    private DatagramSocket socket;

    public Server() throws SocketException, UnknownHostException {
        rooms = new ArrayList<Room>();
        socket = new DatagramSocket(PORT, InetAddress.getByName(HOSTNAME));
    }

    @Override
    public void run() {
        while(!socket.isClosed()) {
            try {
                //RECEIVE PACKET
                byte[]content = new byte[1024];
                DatagramPacket packet = new DatagramPacket(content, content.length);
                socket.receive(packet);
                int port = packet.getPort();
                InetAddress address = packet.getAddress();
                ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(content));

                //READ THE FIRST PART OF THE MESSAGE
                String part1 = ois.readUTF();

                //EXECUTE THE RIGHT THING
                switch (part1) {
                    case "creer":
                        new Thread(() -> {
                            try {
                                createRoom(port, address, false);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }).start();
                        break;
                    case "rejoindre" :
                        new Thread(() -> {
                            try {
                                joinRoom(ois, port, address);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }).start();
                        break;

                    case "public" :

                        break;
                    default: new Thread(() -> {
                        try {
                            sendToRoom(ois, part1, port, address);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void createRoom(int port, InetAddress address, boolean isPublic) throws IOException {
        //CREATE A RANDOM ID AND VERIFY THIS ID DOES NOT EXIST;
        String id = "";
        do {
            id = "";
            for(int i = 0; i < ID_LENGTH; i++) {
                id += (char)((int)(Math.random() * 26) + 65);
            }
        }while(isIdExist(id));

        //CREATE A ROOM AND ADD TO THE LIST
        Room room = new Room(socket, id, isPublic);
        room.join(port, address);
        rooms.add(room);

        //SEND THE ID TO THE CLIENT
        byte[] buf = id.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
        socket.send(packet);
    }

    public void joinRoom(ObjectInputStream ois, int port, InetAddress address) throws IOException {
        String id = ois.readUTF();
        //IF THE ID EXIST THE CLIENT JOIN THE ROOM THAT HAS THIS ID AS ID
        for(int i = 0; i < rooms.size(); i++) {
            if(rooms.get(i).getId().equals(id)) {
                rooms.get(i).join(port, address);

                //SEND HE JOINED THE ROOM
                byte[] buf = "yesRoom ".getBytes();
                DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
                socket.send(packet);
                return;
            }
        }

        //IF THE ID ROOM IS NOT CORRECT, SEND HE DOES NOT JOIN A ROOM
        byte[] buf = "noRoom  ".getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
        socket.send(packet);
    }

    public void sendToRoom(ObjectInputStream ois, String id, int port, InetAddress address) throws IOException, ClassNotFoundException {
        for(int i = 0; i < rooms.size(); i++) {
            if(rooms.get(i).getId().equals(id)) {
                rooms.get(i).receive(ois, port, address);
                return;
            }
        }
    }

    private boolean isIdExist(String id) {
        for(int i = 0; i < rooms.size(); i++) {
            if(rooms.get(i).getId().equals(id)) return true;
        }
        return false;
    }

    public void joinPublicRoom(ObjectInputStream ois,int port, InetAddress address) throws IOException {
        Room room = getPublicRoom();
        if(room != null) {
            joinRoom(ois, port, address);
        } else {

        }
    }

    private Room getPublicRoom() {
        for(int i = 0; i < rooms.size(); i++) {
            if(rooms.get(i).isPublic() && !rooms.get(i).isFull()) {
                return rooms.get(i);
            }
        }
        return null;
    }

    public static void main(String[]args) throws SocketException, UnknownHostException {
        new Server().start();
    }
}
