import org.bson.Document;
import java.util.ArrayList;
import java.util.List;

public class Room {

    private String roomID;
    private String roomType;

    private int capacity;
    private boolean isAvailable;
    private double pricePerNight;

    public static class RoomNode {

        Room room;

        RoomNode next;

        public RoomNode(Room room) {
            this.room = room;
            this.next = null;
        }
    }

    private static RoomNode head = null;

    private static RoomNode tail = null;

    private static int size = 0;

    private static int roomCounter = 0;


    public Room(String roomType, int capacity, double pricePerNight) {

        roomCounter++;
        this.roomID = String.format("R%04d", roomCounter);
        this.roomType = roomType;
        this.capacity = capacity;
        this.isAvailable = true;
        this.pricePerNight = pricePerNight;
    }

    public Room(String roomID, String roomType, int capacity,
                boolean isAvailable, double pricePerNight) {
        this.roomID = roomID;
        this.roomType = roomType;
        this.capacity = capacity;
        this.isAvailable = isAvailable;
        this.pricePerNight = pricePerNight;

        if (roomID != null && roomID.startsWith("R") && roomID.length() > 1) {
            try {
                int idNum = Integer.parseInt(roomID.substring(1));
                if (idNum >= roomCounter) {
                    roomCounter = idNum;
                }
            } catch (NumberFormatException e) {

            }
        }
    }

    public void addToList() {

        RoomNode newNode = new RoomNode(this);

        if (head == null) {
            head = newNode;
            tail = newNode;
        }

        else {
            tail.next = newNode;
            tail = newNode;
        }

        size++;
    }

    public void assignRoom() {
        this.isAvailable = false;
    }

    public void releaseRoom() {
        this.isAvailable = true;
    }

    public static Room searchByID(String roomID) {

        RoomNode current = head;

        while (current != null) {

            if (current.room.getRoomID() != null &&
                    current.room.getRoomID().equals(roomID)) {
                return current.room;
            }

            current = current.next;
        }

        return null;
    }

    public static List<Room> getAvailableRooms() {
        List<Room> available = new ArrayList<>();

        RoomNode current = head;

        while (current != null) {

            if (current.room.isAvailable()) {
                available.add(current.room);
            }

            current = current.next;
        }

        return available;
    }

    public static List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();

        RoomNode current = head;

        while (current != null) {
            rooms.add(current.room);

            current = current.next;
        }

        return rooms;
    }

    public static void clearList() {
        head = null;
        tail = null;
        size = 0;
    }

    public static void resetCounter() {
        roomCounter = 0;
    }

    public Document toDocument() {
        return new Document("roomID", roomID)
                .append("roomType", roomType)
                .append("capacity", capacity)
                .append("isAvailable", isAvailable)
                .append("pricePerNight", pricePerNight);
    }

    public static Room fromDocument(Document doc) {

        if (doc == null) {
            return null;
        }

        String roomID = doc.getString("roomID");

        if (roomID == null || roomID.isEmpty()) {
            return null;
        }

        String roomType = doc.getString("roomType");
        Integer capacity = doc.getInteger("capacity");
        Boolean isAvailable = doc.getBoolean("isAvailable");
        Double pricePerNight = doc.getDouble("pricePerNight");

        return new Room(
                roomID,
                roomType != null ? roomType : "Standard",
                capacity != null ? capacity : 1,
                isAvailable != null ? isAvailable : true,
                pricePerNight != null ? pricePerNight : 100.0
        );
    }

    public String getRoomDetails() {
        String status = isAvailable ? "Available" : "Occupied";
        return "ID: " + roomID + ", Type: " + roomType +
                ", Capacity: " + capacity + ", Price: $" + pricePerNight +
                "/night, Status: " + status;
    }

    public String getRoomID() { return roomID; }
    public String getRoomType() { return roomType; }
    public boolean isAvailable() { return isAvailable; }
    public double getPricePerNight() { return pricePerNight; }

}