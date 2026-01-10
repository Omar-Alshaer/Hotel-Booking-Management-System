import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Reservation {

    private String reservationID;
    private String guestID;
    private String roomID;
    private String checkInDate;
    private String checkOutDate;
    private String status;

    private long numberOfNights;

    public static class ReservationNode {

        Reservation reservation;

        ReservationNode next;

        public ReservationNode(Reservation reservation) {
            this.reservation = reservation;
            this.next = null;
        }
    }

    private static ReservationNode head = null;

    private static int size = 0;

    private static int reservationCounter = 0;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Reservation(String guestID, String roomID,
                       String checkInDate, String checkOutDate) {

        if (!isValidDateRange(checkInDate, checkOutDate)) {
            throw new IllegalArgumentException(
                    "Check-out date must be after the check-in date, and both dates must be today or in the future");
        }

        reservationCounter++;
        this.reservationID = String.format("RES%04d", reservationCounter);
        this.guestID = guestID;
        this.roomID = roomID;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.status = "Confirmed";

        this.numberOfNights = calculateNights(checkInDate, checkOutDate);
    }

    public Reservation(String reservationID, String guestID, String roomID,
                       String checkInDate, String checkOutDate, String status) {
        this.reservationID = reservationID;
        this.guestID = guestID;
        this.roomID = roomID;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.status = status;

        this.numberOfNights = calculateNights(checkInDate, checkOutDate);

        if (reservationID != null && reservationID.startsWith("RES") && reservationID.length() > 3) {
            try {
                int idNum = Integer.parseInt(reservationID.substring(3));
                if (idNum >= reservationCounter) {
                    reservationCounter = idNum;
                }
            } catch (NumberFormatException e) {

            }
        }
    }

    private static long calculateNights(String checkInDate, String checkOutDate) {
        try {
            LocalDate startDate = LocalDate.parse(checkInDate, DATE_FORMATTER);
            LocalDate endDate = LocalDate.parse(checkOutDate, DATE_FORMATTER);

            long nights = ChronoUnit.DAYS.between(startDate, endDate);
            return nights > 0 ? nights : 1;

        } catch (Exception e) {
            System.out.println("Error calculating nights: " + e.getMessage());
            return 1;

        }
    }

    private static boolean isValidDateRange(String checkInDate, String checkOutDate) {
        try {

            LocalDate startDate = LocalDate.parse(checkInDate, DATE_FORMATTER);
            LocalDate endDate = LocalDate.parse(checkOutDate, DATE_FORMATTER);
            LocalDate today = LocalDate.now();

            if (startDate.isBefore(today)) {
                throw new IllegalArgumentException("Check-in date must be today or in the future");
            }

            if (!endDate.isAfter(startDate)) {
                throw new IllegalArgumentException("Check-out date must be after the check-in date");
            }

            return true;
        } catch (Exception e) {
            System.out.println("Date validation error: " + e.getMessage());
            return false;
        }
    }

    public static boolean isValidDateFormat(String date) {
        try {
            LocalDate.parse(date, DATE_FORMATTER);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void makeReservation() {

        ReservationNode newNode = new ReservationNode(this);

        newNode.next = head;
        head = newNode;
        size++;
    }

    public void cancelReservation() {
        this.status = "Cancelled";
    }

    public static Reservation searchByID(String reservationID) {
        ReservationNode current = head;
        while (current != null) {
            if (current.reservation.getReservationID() != null &&
                    current.reservation.getReservationID().equals(reservationID)) {
                return current.reservation;
            }
            current = current.next;
        }
        return null;
    }

    public static List<Reservation> getAllReservations() {
        List<Reservation> reservations = new ArrayList<>();
        ReservationNode current = head;
        while (current != null) {
            reservations.add(current.reservation);
            current = current.next;
        }
        return reservations;
    }

    public static List<Reservation> sortByDate(List<Reservation> reservations) {

        if (reservations.size() <= 1) {
            return reservations;
        }

        int mid = reservations.size() / 2;

        List<Reservation> left = new ArrayList<>(reservations.subList(0, mid));
        List<Reservation> right = new ArrayList<>(reservations.subList(mid, reservations.size()));

        left = sortByDate(left);
        right = sortByDate(right);

        return merge(left, right);
    }

    private static List<Reservation> merge(List<Reservation> left, List<Reservation> right) {
        List<Reservation> result = new ArrayList<>();
        int i = 0, j = 0;

        while (i < left.size() && j < right.size()) {

            String date1 = left.get(i).getCheckInDate();
            String date2 = right.get(j).getCheckInDate();

            if (date1 == null) date1 = "";
            if (date2 == null) date2 = "";

            if (date1.compareTo(date2) <= 0) {
                result.add(left.get(i));
                i++;
            } else {
                result.add(right.get(j));
                j++;
            }
        }

        while (i < left.size()) {
            result.add(left.get(i));
            i++;
        }

        while (j < right.size()) {
            result.add(right.get(j));
            j++;
        }

        return result;
    }

    public static void clearList() {
        head = null;
        size = 0;
    }

    public static void resetCounter() {
        reservationCounter = 0;
    }

    public Document toDocument() {
        return new Document("reservationID", reservationID)
                .append("guestID", guestID)
                .append("roomID", roomID)
                .append("checkInDate", checkInDate)
                .append("checkOutDate", checkOutDate)
                .append("status", status)
                .append("numberOfNights", numberOfNights);
    }

    public static Reservation fromDocument(Document doc) {

        if (doc == null) {
            return null;
        }

        String reservationID = doc.getString("reservationID");

        if (reservationID == null || reservationID.isEmpty()) {
            return null;
        }

        Reservation res = new Reservation(
                reservationID,
                doc.getString("guestID") != null ? doc.getString("guestID") : "Unknown",
                doc.getString("roomID") != null ? doc.getString("roomID") : "Unknown",
                doc.getString("checkInDate") != null ? doc.getString("checkInDate") : "N/A",
                doc.getString("checkOutDate") != null ? doc.getString("checkOutDate") : "N/A",
                doc.getString("status") != null ? doc.getString("status") : "Pending"
        );

        return res;
    }

    public String getReservationDetails() {
        return "ID: " + reservationID + ", Guest: " + guestID +
                ", Room: " + roomID + ", Check-in: " + checkInDate +
                ", Check-out: " + checkOutDate + ", Nights: " + numberOfNights +
                ", Status: " + status;
    }

    public String getReservationID() { return reservationID; }
    public String getGuestID() { return guestID; }
    public String getRoomID() { return roomID; }
    public String getCheckInDate() { return checkInDate; }
    public String getCheckOutDate() { return checkOutDate; }
    public String getStatus() { return status; }
    public long getNumberOfNights() { return numberOfNights; }
}