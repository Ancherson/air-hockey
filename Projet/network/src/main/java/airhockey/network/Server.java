package airhockey.network;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

/**
 * This class is the Server, and manages all message received and all rooms
 */
public class Server extends Thread {
    /**
     * The port of the server
     */
    public final static int PORT = 6666;
    /**
     * the name of the host
     */
    private final String hostname;

    /**
     * Length of the rooms id
     */
    public static int ID_LENGTH = 10;

    /**
     * List of the rooms
     */
    private ArrayList<Room> rooms;
    /**
     * the socket of the server
     */
    private DatagramSocket socket;

    /**
     * Construtor of the Server
     * @param hostname name of the host
     * @throws SocketException
     * @throws UnknownHostException
     */
    public Server(String hostname) throws SocketException, UnknownHostException {
        this.hostname = hostname;
        rooms = new ArrayList<Room>();
        socket = new DatagramSocket(PORT, InetAddress.getByName(hostname));
    }

    @Override
    public void run() {
        System.out.println("################### SERVER RUNNING !!!! ###################");
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

    /**
     * Function that create room
     * @param port port of the client
     * @param address address of the client
     * @param isPublic boolean that says if the client wants a public room
     * @throws IOException
     */
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

    /**
     * Function to join a room
     * @param ois the rest of the client message
     * @param port the port of the client
     * @param address the addres of the client
     * @throws IOException
     */
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

    /**
     *
     * @param ois this rest of the message
     * @param id the id of the room which must receive the message
     * @param port the port of the client
     * @param address the address of the client
     * @throws IOException
     * @throws ClassNotFoundException
     */
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

    /**
     * Function to search if a room which has as id the id exists
     * @param id id the new possible room
     * @return if a room which has as id the id exist
     */
    private boolean isIdExist(String id) {
        for (Room room : rooms) {
            if (room.getId().equals(id)) return true;
        }
        return false;
    }

    /**
     * Function to join a public room
     * @param ois the rest of the message
     * @param port the port of the client
     * @param address the address of the client
     * @throws IOException
     */
    public void joinPublicRoom(ObjectInputStream ois,int port, InetAddress address) throws IOException {
        //FIRST WE SEARCH IF A PUBLIC ROOM IS WAITING A PLAYER
        Room room = getPublicRoom();
        System.out.println(room);

        //IF A PUBLIC ROOM EXISTS THE PLAYER CAN JOIN THIS ROOM
        if(room != null) {
            byte[] buf = "1".getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
            socket.send(packet);

            buf = room.getId().getBytes();
            packet = new DatagramPacket(buf, buf.length, address, port);
            socket.send(packet);

            room.join(port,address);
        }
        //IF NO ROOM EXISTS, HE CAN CREATE ONE
        else {
            byte[] buf = "0".getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
            socket.send(packet);

            createRoom(port, address, true);
        }
    }

    /**
     * Function that search a public room which is not full
     * @return the first public room which is not full
     */
    private Room getPublicRoom() {
        for (Room room : rooms) {
            if (room.isPublic() && !room.isFull()) {
                return room;
            }
        }
        return null;
    }

    /**
     * Close the room
     * @param ois the rest of the message
     * @throws IOException
     */
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
        }
        new Server(name).start();
    }
}
