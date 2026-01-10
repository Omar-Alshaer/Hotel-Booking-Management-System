import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class ReportGenerator {

    private String reportType;

    private String data;

    public ReportGenerator() {
        this.reportType = "";
        this.data = "";
    }

    public String generateReservationReport() {
        this.reportType = "Reservation Report";
        StringBuilder report = new StringBuilder();
        report.append("========== RESERVATION REPORT ==========\n\n");

        List<Reservation> reservations = Reservation.getAllReservations();

        reservations = Reservation.sortByDate(reservations);

        int confirmed = 0, cancelled = 0, completed = 0, pending = 0;

        report.append("--- All Reservations (Sorted by Date) ---\n");
        for (Reservation res : reservations) {
            report.append(res.getReservationDetails()).append("\n");

            switch (res.getStatus()) {
                case "Confirmed":
                    confirmed++;
                    break;
                case "Cancelled":
                    cancelled++;
                    break;
                case "Completed":
                    completed++;
                    break;
                case "Pending":
                    pending++;
                    break;
            }
        }

        report.append("\n--- Summary ---\n");
        report.append("Total Reservations: ").append(reservations.size()).append("\n");
        report.append("Confirmed: ").append(confirmed).append("\n");
        report.append("Pending: ").append(pending).append("\n");
        report.append("Completed: ").append(completed).append("\n");
        report.append("Cancelled: ").append(cancelled).append("\n");

        this.data = report.toString();
        return this.data;
    }

    public String generateRevenueReport() {
        this.reportType = "Revenue Report";
        StringBuilder report = new StringBuilder();
        report.append("========== REVENUE REPORT ==========\n\n");

        List<Billing> billings = Billing.getAllBillings();

        billings = mergeSortBillingByAmount(billings);

        double totalRevenue = 0;
        double totalPending = 0;
        int paidCount = 0;
        int unpaidCount = 0;

        report.append("--- All Bills (Sorted by Amount) ---\n");
        for (Billing bill : billings) {
            report.append("Guest: ").append(bill.getGuestID());
            report.append(", Amount: $").append(String.format("%.2f", bill.getTotalCharges()));
            report.append(", Status: ").append(bill.getStatus()).append("\n");

            if (bill.isPaid()) {
                paidCount++;
                totalRevenue += bill.getTotalCharges();
            } else {
                unpaidCount++;
                totalPending += bill.getBalance();
            }
        }

        report.append("\n--- Summary ---\n");
        report.append("Total Revenue Collected: $").append(String.format("%.2f", totalRevenue)).append("\n");
        report.append("Total Pending Amount: $").append(String.format("%.2f", totalPending)).append("\n");
        report.append("Paid Bills: ").append(paidCount).append("\n");
        report.append("Unpaid Bills: ").append(unpaidCount).append("\n");

        this.data = report.toString();
        return this.data;
    }

    private List<Billing> mergeSortBillingByAmount(List<Billing> billings) {

        if (billings.size() <= 1) {
            return billings;
        }

        int mid = billings.size() / 2;

        List<Billing> left = new ArrayList<>(billings.subList(0, mid));
        List<Billing> right = new ArrayList<>(billings.subList(mid, billings.size()));

        left = mergeSortBillingByAmount(left);
        right = mergeSortBillingByAmount(right);

        return mergeBillings(left, right);
    }

    private List<Billing> mergeBillings(List<Billing> left, List<Billing> right) {
        List<Billing> result = new ArrayList<>();
        int i = 0, j = 0;

        while (i < left.size() && j < right.size()) {

            if (left.get(i).getTotalCharges() >= right.get(j).getTotalCharges()) {
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

    public String generateOccupancyReport() {
        this.reportType = "Occupancy Report";
        StringBuilder report = new StringBuilder();
        report.append("========== OCCUPANCY REPORT ==========\n\n");

        List<Room> rooms = Room.getAllRooms();

        int totalRooms = rooms.size();
        int occupied = 0;
        int available = 0;

        Map<String, int[]> typeStats = new HashMap<>();

        report.append("--- Room Status ---\n");
        for (Room room : rooms) {
            report.append(room.getRoomDetails()).append("\n");

            if (room.isAvailable()) {
                available++;
            } else {
                occupied++;
            }

            String type = room.getRoomType();
            if (!typeStats.containsKey(type)) {
                typeStats.put(type, new int[]{0, 0});

            }

            typeStats.get(type)[0]++;
            if (!room.isAvailable()) {
                typeStats.get(type)[1]++;
            }
        }

        report.append("\n--- Summary ---\n");
        report.append("Total Rooms: ").append(totalRooms).append("\n");
        report.append("Occupied: ").append(occupied).append("\n");
        report.append("Available: ").append(available).append("\n");

        if (totalRooms > 0) {
            double occupancyRate = (double) occupied / totalRooms * 100;
            report.append("Occupancy Rate: ").append(String.format("%.1f", occupancyRate)).append("%\n");
        }

        report.append("\n--- By Room Type ---\n");
        for (Map.Entry<String, int[]> entry : typeStats.entrySet()) {
            report.append(entry.getKey()).append(": ");
            report.append(entry.getValue()[1]).append("/").append(entry.getValue()[0]);
            report.append(" occupied\n");
        }

        this.data = report.toString();
        return this.data;
    }

}