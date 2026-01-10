import org.bson.Document;
import java.util.ArrayList;
import java.util.List;

public class Guest {
    private String guestID;
    private String name;
    private String contactInfo;
    private String address;
    private List<String> bookingHistory;
    private List<String> paymentHistory;

    public static class GuestNode {
        Guest guest;
        GuestNode left;
        GuestNode right;
        int height;

        public GuestNode(Guest guest) {
            this.guest = guest;
            this.left = null;
            this.right = null;
            this.height = 1;
        }
    }

    private static GuestNode root = null;
    private static int guestCounter = 0;

    public Guest(String name, String contactInfo, String address) {
        guestCounter++;
        this.guestID = String.format("G%04d", guestCounter);
        this.name = name;
        this.contactInfo = contactInfo;
        this.address = address;
        this.bookingHistory = new ArrayList<>();
        this.paymentHistory = new ArrayList<>();
    }

    public Guest(String guestID, String name, String contactInfo, String address,
                 List<String> bookingHistory, List<String> paymentHistory) {
        this.guestID = guestID;
        this.name = name;
        this.contactInfo = contactInfo;
        this.address = address;
        this.bookingHistory = bookingHistory != null ? bookingHistory : new ArrayList<>();
        this.paymentHistory = paymentHistory != null ? paymentHistory : new ArrayList<>();

        if (guestID != null && guestID.startsWith("G") && guestID.length() > 1) {
            try {
                int idNum = Integer.parseInt(guestID.substring(1));
                if (idNum >= guestCounter) guestCounter = idNum;
            } catch (NumberFormatException e) {}
        }
    }

    public void registerGuest() {
        root = insert(root, this);
    }

    private GuestNode insert(GuestNode node, Guest guest) {

        if (node == null) return new GuestNode(guest);

        int comparison = guest.getGuestID().compareTo(node.guest.getGuestID());
        if (comparison < 0) {
            node.left = insert(node.left, guest);
        } else if (comparison > 0) {
            node.right = insert(node.right, guest);
        }

        node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));

        int balance = getBalance(node);

        if (balance > 1) {
            if (guest.getGuestID().compareTo(node.left.guest.getGuestID()) < 0) {
                return rightRotate(node);  // LL
            } else {
                node.left = leftRotate(node.left);  // LR
                return rightRotate(node);
            }
        }

        if (balance < -1) {
            if (guest.getGuestID().compareTo(node.right.guest.getGuestID()) > 0) {
                return leftRotate(node);  // RR
            } else {
                node.right = rightRotate(node.right);  // RL
                return leftRotate(node);
            }
        }

        return node;
    }

    private GuestNode rightRotate(GuestNode y) {
        GuestNode x = y.left;
        GuestNode T2 = x.right;
        x.right = y;
        y.left = T2;
        y.height = 1 + Math.max(getHeight(y.left), getHeight(y.right));
        x.height = 1 + Math.max(getHeight(x.left), getHeight(x.right));
        return x;
    }

    private GuestNode leftRotate(GuestNode x) {
        GuestNode y = x.right;
        GuestNode T2 = y.left;
        y.left = x;
        x.right = T2;
        x.height = 1 + Math.max(getHeight(x.left), getHeight(x.right));
        y.height = 1 + Math.max(getHeight(y.left), getHeight(y.right));
        return y;
    }

    private int getHeight(GuestNode node) {
        return (node == null) ? 0 : node.height;
    }

    private int getBalance(GuestNode node) {
        return (node == null) ? 0 : getHeight(node.left) - getHeight(node.right);
    }

    public static Guest searchByID(String guestID) {
        return searchBSTByID(root, guestID);
    }

    private static Guest searchBSTByID(GuestNode node, String guestID) {
        if (node == null) return null;
        int comparison = guestID.compareTo(node.guest.getGuestID());
        if (comparison == 0) return node.guest;
        else if (comparison < 0) return searchBSTByID(node.left, guestID);
        else return searchBSTByID(node.right, guestID);
    }

    public static Guest searchByName(String name) {
        if (name == null || name.trim().isEmpty()) return null;
        String query = name.trim().toLowerCase();
        return searchBSTByName(root, query);
    }

    private static Guest searchBSTByName(GuestNode node, String query) {
        if (node == null) return null;
        String guestName = node.guest.getName();
        if (guestName != null && guestName.toLowerCase().contains(query)) return node.guest;
        Guest found = searchBSTByName(node.left, query);
        if (found != null) return found;
        return searchBSTByName(node.right, query);
    }

    public static List<Guest> getAllGuests() {
        List<Guest> guests = new ArrayList<>();
        inOrderTraversal(root, guests);
        return guests;
    }

    private static void inOrderTraversal(GuestNode node, List<Guest> guests) {
        if (node != null) {
            inOrderTraversal(node.left, guests);
            guests.add(node.guest);
            inOrderTraversal(node.right, guests);
        }
    }

    public String getGuestDetails() {
        return "ID: " + guestID + ", Name: " + name +
                ", Contact: " + contactInfo + ", Address: " + address;
    }

    public void addBooking(String reservationID) {
        bookingHistory.add(reservationID);
    }

    public Document toDocument() {
        return new Document("guestID", guestID)
                .append("name", name)
                .append("contactInfo", contactInfo)
                .append("address", address)
                .append("bookingHistory", bookingHistory)
                .append("paymentHistory", paymentHistory);
    }

    public static Guest fromDocument(Document doc) {
        if (doc == null) return null;
        String guestID = doc.getString("guestID");
        if (guestID == null || guestID.isEmpty()) return null;
        String name = doc.getString("name");
        String contactInfo = doc.getString("contactInfo");
        String address = doc.getString("address");
        List<String> bookingHistory = doc.getList("bookingHistory", String.class);
        List<String> paymentHistory = doc.getList("paymentHistory", String.class);
        return new Guest(guestID, name != null ? name : "Unknown",
                contactInfo != null ? contactInfo : "N/A",
                address != null ? address : "N/A", bookingHistory, paymentHistory);
    }

    public static void clearBST() {
        root = null;
    }

    public static void resetCounter() {
        guestCounter = 0;
    }

    public String getGuestID() { return guestID; }
    public String getName() { return name; }
}