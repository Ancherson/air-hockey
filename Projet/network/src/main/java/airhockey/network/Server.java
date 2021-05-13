package airhockey.network;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Server extends Thread {
    public final static int PORT = 6666;
    private final String hostname;
    public static int ID_LENGTH = 10;

    private ArrayList<Room> rooms;
    private DatagramSocket socket;

    public Server(String hostname) throws SocketException, UnknownHostException {
        this.hostname = hostname;
        rooms = new ArrayList<Room>();
        socket = new DatagramSocket(PORT, InetAddress.getByName(hostname));
    }

    @Override
    public void run() {
        System.out.println("################### SERVEUR LANCE !!!! ###################");
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
                    case "creer": createRoom(port, address, false);break;
                    case "rejoindre" : joinRoom(ois, port, address);break;
                    case "public" : joinPublicRoom(ois, port, address);break;
                    case "close" : close(ois);break;
                    default: sendToRoom(ois, part1, port, address);
                }
                ois.close();
            } catch (IOException | ClassNotFoundException e) {
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
        for (Room room : rooms) {
            if (room.getId().equals(id)) {
                room.join(port, address);

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
        for (Room room : rooms) {
            if (room.getId().equals(id)) {
                room.receive(ois, port, address);
                return;
            }
        }
        ois.close();

        //SEND NO ROOM
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        ObjectOutput oo = new ObjectOutputStream(bStream);
        oo.writeBoolean(false);
        oo.close();
        byte[] objectSerialized = bStream.toByteArray();
        DatagramPacket packet = new DatagramPacket(objectSerialized, objectSerialized.length, address, port);
        socket.send(packet);
    }

    private boolean isIdExist(String id) {
        for (Room room : rooms) {
            if (room.getId().equals(id)) return true;
        }
        return false;
    }

    public void joinPublicRoom(ObjectInputStream ois,int port, InetAddress address) throws IOException {
        Room room = getPublicRoom();
        System.out.println(room);
        if(room != null) {
            byte[] buf = "1".getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
            socket.send(packet);

            buf = room.getId().getBytes();
            packet = new DatagramPacket(buf, buf.length, address, port);
            socket.send(packet);

            room.join(port,address);
        } else {
            byte[] buf = "0".getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
            socket.send(packet);

            createRoom(port, address, true);
        }
    }

    private Room getPublicRoom() {
        for (Room room : rooms) {
            if (room.isPublic() && !room.isFull()) {
                return room;
            }
        }
        return null;
    }

    private void close(ObjectInputStream ois) throws IOException {
        String id = ois.readUTF();
        ois.close();
        for(int i = 0; i < rooms.size(); i++) {
            Room room = rooms.get(i);
            if(room.getId().equals(id)) {
                if(room.getModel().isFinished() && !room.isClosing()) {
                    room.endGame();
                }else {
                    room.close();
                    rooms.remove(i);
                }
                return;
            }
        }
    }

    public static void main(String[]args) throws SocketException, UnknownHostException {
        if(args.length > 1) {
            System.out.println("Too much arguments");
            System.exit(0);
        }
        String name = "localhost";
        if(args.length == 1) {
            name = args[0];
            try {
                InetAddress add = InetAddress.getByName(args[0]);
                try {
                    DatagramSocket test = new DatagramSocket(6666,add);
                    test.close();
                } catch (SocketException e) {
                    System.out.println("Can not connect with this hostname");
                    System.exit(0);
                }
            }catch(UnknownHostException e) {
                System.out.println("This hostname doesn't exist !");
                System.exit(0);
            }
        }
        new Server(name).start();
    }
}
