import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class HotelManagementSystem {

    private DataBase dbConnection;

    private ReportGenerator reportGenerator;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public HotelManagementSystem() {

        dbConnection = new DataBase();

        reportGenerator = new ReportGenerator();

        loadAllData();
    }

    public void loadAllData() {

        Guest.clearBST();
        Guest.resetCounter();
        Room.clearList();
        Room.resetCounter();
        Reservation.clearList();
        Reservation.resetCounter();
        ServiceRequest.clearQueue();
        ServiceRequest.resetCounter();
        Billing.clearMap();
        Billing.resetCounter();

        try {

            MongoCollection<Document> guestsCollection = dbConnection.getGuestsCollection();
            MongoCursor<Document> guestCursor = guestsCollection.find().iterator();

            while (guestCursor.hasNext()) {
                Document doc = guestCursor.next();
                Guest guest = Guest.fromDocument(doc);

                if (guest != null && guest.getGuestID() != null) {
                    guest.registerGuest();
                }
            }
            guestCursor.close();

            MongoCollection<Document> roomsCollection = dbConnection.getRoomsCollection();
            MongoCursor<Document> roomCursor = roomsCollection.find().iterator();

            while (roomCursor.hasNext()) {
                Document doc = roomCursor.next();
                Room room = Room.fromDocument(doc);

                if (room != null && room.getRoomID() != null) {
                    room.addToList();
                }
            }
            roomCursor.close();

            MongoCollection<Document> reservationsCollection = dbConnection.getReservationsCollection();
            MongoCursor<Document> resCursor = reservationsCollection.find().iterator();

            while (resCursor.hasNext()) {
                Document doc = resCursor.next();
                Reservation res = Reservation.fromDocument(doc);

                if (res != null && res.getReservationID() != null) {
                    res.makeReservation();
                }
            }
            resCursor.close();

            MongoCollection<Document> serviceCollection = dbConnection.getServiceRequestsCollection();
            MongoCursor<Document> serviceCursor = serviceCollection.find().iterator();

            while (serviceCursor.hasNext()) {
                Document doc = serviceCursor.next();
                ServiceRequest req = ServiceRequest.fromDocument(doc);

                if (req != null && req.getRequestID() != null) {
                    if ("Pending".equals(req.getStatus())) {
                        req.addRequest();
                    }
                }
            }
            serviceCursor.close();

            MongoCollection<Document> billingCollection = dbConnection.getBillingCollection();
            MongoCursor<Document> billCursor = billingCollection.find().iterator();

            while (billCursor.hasNext()) {
                Document doc = billCursor.next();
                Billing bill = Billing.fromDocument(doc);

                if (bill != null && bill.getBillingID() != null) {
                    bill.addToMap();
                }
            }
            billCursor.close();

        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public String addGuest(String name, String contact, String address) {
        try {

            if (name == null || name.trim().isEmpty() ||
                    contact == null || contact.trim().isEmpty() ||
                    address == null || address.trim().isEmpty()) {
                return "Error: All fields are required!";
            }

            Guest guest = new Guest(name.trim(), contact.trim(), address.trim());

            guest.registerGuest();

            dbConnection.getGuestsCollection().insertOne(guest.toDocument());

            Billing billing = new Billing(guest.getGuestID());
            billing.addToMap();
            dbConnection.getBillingCollection().insertOne(billing.toDocument());

            return "Guest registered successfully!\n" + guest.getGuestDetails();
        } catch (Exception e) {
            return "Error adding guest: " + e.getMessage();
        }
    }

    public String searchGuestByID(String guestID) {
        Guest guest = Guest.searchByID(guestID);
        if (guest != null) {
            return guest.getGuestDetails();
        }
        return "Guest not found!";
    }

    public String searchGuestByName(String name) {
        Guest guest = Guest.searchByName(name);
        if (guest != null) {
            return guest.getGuestDetails();
        }
        return "Guest not found!";
    }

    public String removeGuest(String guestID) {
        try {
            if (guestID == null || guestID.trim().isEmpty()) {
                return "Error: Guest ID is required!";
            }

            Guest guest = Guest.searchByID(guestID.trim());
            if (guest == null) {
                return "Error: Guest not found!";
            }
            dbConnection.getGuestsCollection()
                    .deleteOne(new Document("guestID", guestID.trim()));

            dbConnection.getBillingCollection()
                    .deleteOne(new Document("guestID", guestID.trim()));

            loadAllData();

            return "Guest removed successfully! ID: " + guestID.trim();
        } catch (Exception e) {
            return "Error removing guest: " + e.getMessage();
        }
    }


    public String addRoom(String roomType, int capacity, double price) {
        try {

            if (roomType == null || roomType.trim().isEmpty() || capacity <= 0 || price <= 0) {
                return "Error: Invalid room data!";
            }

            Room room = new Room(roomType.trim(), capacity, price);

            room.addToList();

            dbConnection.getRoomsCollection().insertOne(room.toDocument());

            return "Room added successfully!\n" + room.getRoomDetails();
        } catch (Exception e) {
            return "Error adding room: " + e.getMessage();
        }
    }

    public String getAllRooms() {
        List<Room> rooms = Room.getAllRooms();
        if (rooms.isEmpty()) {
            return "No rooms found!";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("=== All Rooms ===\n");
        for (Room room : rooms) {
            sb.append(room.getRoomDetails()).append("\n");
        }

        return sb.toString();
    }

    public String getAvailableRooms() {
        List<Room> rooms = Room.getAvailableRooms();
        if (rooms.isEmpty()) {
            return "No available rooms!";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("=== Available Rooms ===\n");
        for (Room room : rooms) {
            sb.append(room.getRoomDetails()).append("\n");
        }

        return sb.toString();
    }

    public String makeReservation(String guestID, String roomID,
                                  String checkIn, String checkOut) {
        try {

            if (guestID == null || guestID.trim().isEmpty()) {
                return "Error: Guest ID is required!";
            }
            if (roomID == null || roomID.trim().isEmpty()) {
                return "Error: Room ID is required!";
            }
            if (checkIn == null || checkIn.trim().isEmpty()) {
                return "Error: Check-in date is required!";
            }
            if (checkOut == null || checkOut.trim().isEmpty()) {
                return "Error: Check-out date is required!";
            }

            if (!Reservation.isValidDateFormat(checkIn.trim())) {
                return "Error: Invalid check-in date format! Use format (yyyy-MM-dd)";
            }
            if (!Reservation.isValidDateFormat(checkOut.trim())) {
                return "Error: Invalid check-out date format! Use format (yyyy-MM-dd)";
            }

            LocalDate startDate = LocalDate.parse(checkIn.trim(), DATE_FORMATTER);
            LocalDate endDate = LocalDate.parse(checkOut.trim(), DATE_FORMATTER);
            LocalDate today = LocalDate.now();

            if (startDate.isBefore(today)) {
                return "Error: Check-in date must be today or in the future!";
            }

            if (!endDate.isAfter(startDate)) {
                return "Error: Check-out date must be after check-in date!";
            }

            Guest guest = Guest.searchByID(guestID.trim());
            if (guest == null) {
                return "Error: Guest not found!";
            }

            Room room = Room.searchByID(roomID.trim());
            if (room == null) {
                return "Error: Room not found!";
            }

            if (!room.isAvailable()) {
                return "Error: Room is not available for this period!";
            }

            Reservation res = new Reservation(guestID.trim(), roomID.trim(),
                    checkIn.trim(), checkOut.trim());
            res.makeReservation();

            room.assignRoom();

            guest.addBooking(res.getReservationID());

            long numberOfNights = res.getNumberOfNights();
            Billing billing = Billing.getOrCreateBilling(guestID.trim());
            billing.generateBill(room.getPricePerNight(), numberOfNights);

            dbConnection.getReservationsCollection().insertOne(res.toDocument());

            dbConnection.getRoomsCollection().updateOne(
                    new Document("roomID", roomID.trim()),
                    new Document("$set", new Document("isAvailable", false))
            );

            dbConnection.getBillingCollection().updateOne(
                    new Document("guestID", guestID.trim()),
                    new Document("$set", billing.toDocument())
            );

            String message = String.format(
                    "Reservation created successfully!\n%s\nNumber of Nights: %d nights\nTotal Charges: $%.2f",
                    res.getReservationDetails(),
                    numberOfNights,
                    room.getPricePerNight() * numberOfNights
            );

            return message;
        } catch (IllegalArgumentException e) {
            return "Error in date: " + e.getMessage();
        } catch (Exception e) {
            return "Error creating reservation: " + e.getMessage();
        }
    }

    public String cancelReservation(String reservationID) {
        try {
            if (reservationID == null || reservationID.trim().isEmpty()) {
                return "Error: Reservation ID is required!";
            }

            Reservation res = Reservation.searchByID(reservationID.trim());
            if (res == null) {
                return "Error: Reservation not found!";
            }

            res.cancelReservation();

            Room room = Room.searchByID(res.getRoomID());
            if (room != null) {
                room.releaseRoom();

                dbConnection.getRoomsCollection().updateOne(
                        new Document("roomID", room.getRoomID()),
                        new Document("$set", new Document("isAvailable", true))
                );
            }

            dbConnection.getReservationsCollection().updateOne(
                    new Document("reservationID", reservationID.trim()),
                    new Document("$set", new Document("status", "Cancelled"))
            );

            return "Reservation cancelled successfully!";
        } catch (Exception e) {
            return "Error cancelling reservation: " + e.getMessage();
        }
    }

    public String addServiceRequest(String guestID, String serviceType) {
        try {
            if (guestID == null || guestID.trim().isEmpty()) {
                return "Error: Guest ID is required!";
            }
            if (serviceType == null || serviceType.trim().isEmpty()) {
                return "Error: Service type is required!";
            }

            Guest guest = Guest.searchByID(guestID.trim());
            if (guest == null) {
                return "Error: Guest not found!";
            }

            ServiceRequest req = new ServiceRequest(guestID.trim(), serviceType.trim());
            req.addRequest();

            double serviceCharge = 25.0;
            Billing billing = Billing.getOrCreateBilling(guestID.trim());
            billing.addServiceCharge(serviceType.trim(), serviceCharge);

            dbConnection.getServiceRequestsCollection().insertOne(req.toDocument());

            dbConnection.getBillingCollection().updateOne(
                    new Document("guestID", guestID.trim()),
                    new Document("$set", billing.toDocument())
            );

            return "Service request added to queue!\n" + req.getRequestDetails();
        } catch (Exception e) {
            return "Error adding service request: " + e.getMessage();
        }
    }

    public String processNextServiceRequest() {
        try {
            ServiceRequest completed = ServiceRequest.completeRequest();
            if (completed == null) {
                return "No pending service requests!";
            }

            dbConnection.getServiceRequestsCollection().updateOne(
                    new Document("requestID", completed.getRequestID()),
                    new Document("$set", new Document("status", "Completed"))
            );

            return "Service request completed!\n" + completed.getRequestDetails();
        } catch (Exception e) {
            return "Error processing service request: " + e.getMessage();
        }
    }

    public String getPendingServiceRequests() {
        try {
            List<ServiceRequest> pending = ServiceRequest.getPendingRequests();
            if (pending.isEmpty()) {
                return "No pending service requests!";
            }

            StringBuilder sb = new StringBuilder();
            sb.append("=== Pending Service Requests (Queue Order) ===\n");
            int position = 1;
            for (ServiceRequest req : pending) {
                sb.append(position).append(". ").append(req.getRequestDetails()).append("\n");
                position++;
            }

            return sb.toString();
        } catch (Exception e) {
            return "Error getting service requests: " + e.getMessage();
        }
    }

    public String processPayment(String guestID, double amount) {
        try {
            if (guestID == null || guestID.trim().isEmpty()) {
                return "Error: Guest ID is required!";
            }
            if (amount <= 0) {
                return "Error: Amount must be greater than zero!";
            }

            Billing billing = Billing.getBillingByGuest(guestID.trim());
            if (billing == null) {
                return "Error: No billing record found for this guest!";
            }

            billing.addPayment(amount);

            dbConnection.getBillingCollection().updateOne(
                    new Document("guestID", guestID.trim()),
                    new Document("$set", billing.toDocument())
            );

            return "Payment processed!\n" + billing.getBillingDetails();
        } catch (Exception e) {
            return "Error processing payment: " + e.getMessage();
        }
    }

    public String getBillingForGuest(String guestID) {
        try {
            if (guestID == null || guestID.trim().isEmpty()) {
                return "Error: Guest ID is required!";
            }

            Billing billing = Billing.getBillingByGuest(guestID.trim());
            if (billing == null) {
                return "Error: No billing record found for this guest!";
            }

            return billing.getBillingDetails();
        } catch (Exception e) {
            return "Error getting billing: " + e.getMessage();
        }
    }

    public String generateReservationReport() {
        try {
            return reportGenerator.generateReservationReport();
        } catch (Exception e) {
            return "Error generating reservation report: " + e.getMessage();
        }
    }

    public String generateRevenueReport() {
        try {
            return reportGenerator.generateRevenueReport();
        } catch (Exception e) {
            return "Error generating revenue report: " + e.getMessage();
        }
    }

    public String generateOccupancyReport() {
        try {
            return reportGenerator.generateOccupancyReport();
        } catch (Exception e) {
            return "Error generating occupancy report: " + e.getMessage();
        }
    }

    public String getAllGuests() {
        try {
            List<Guest> guests = Guest.getAllGuests();
            if (guests.isEmpty()) {
                return "No guests found!";
            }

            StringBuilder sb = new StringBuilder();
            sb.append("=== All Guests (BST In-Order) ===\n");
            for (Guest guest : guests) {
                sb.append(guest.getGuestDetails()).append("\n");
            }

            return sb.toString();
        } catch (Exception e) {
            return "Error getting guests: " + e.getMessage();
        }
    }

    public void close() {
        if (dbConnection != null) {
            dbConnection.close();
        }
    }

}

