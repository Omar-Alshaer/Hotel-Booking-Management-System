import org.bson.Document;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public class Billing {

    private static HashMap<String, Billing> billingMap = new HashMap<>();

    private static int billingCounter = 0;

    private String billingID;
    private String guestID;
    private double roomCharges;

    private double serviceCharges;

    private double totalCharges;

    private double amountPaid;

    private double balance;

    private List<String> chargeDetails;

    private String billingDate;
    private String status;

    public Billing(String guestID) {
        this.billingID = "B" + String.format("%04d", ++billingCounter);
        this.guestID = guestID;
        this.roomCharges = 0.0;
        this.serviceCharges = 0.0;
        this.totalCharges = 0.0;
        this.amountPaid = 0.0;
        this.balance = 0.0;
        this.chargeDetails = new ArrayList<>();
        this.billingDate = java.time.LocalDate.now().toString();
        this.status = "Active";
    }

    public String getBillingID() { return billingID; }
    public String getGuestID() { return guestID; }

    public double getTotalCharges() {
        return totalCharges;
    }

    public double getBalance() { return balance; }

    public String getStatus() {
        return status;
    }

    public void generateBill(double pricePerNight, long numberOfNights) {

        double charges = pricePerNight * numberOfNights;
        this.roomCharges += charges;

        String detail = String.format(
                "Room Charge: %d nights × $%.2f = $%.2f",
                numberOfNights,
                pricePerNight,
                charges
        );
        this.chargeDetails.add(detail);

        updateTotalCharges();
    }

    public void addServiceCharge(String serviceType, double charge) {
        if (charge > 0) {
            this.serviceCharges += charge;
            String detail = String.format(
                    "Service Charge - %s: $%.2f",
                    serviceType,
                    charge
            );
            this.chargeDetails.add(detail);
            updateTotalCharges();
        }
    }

    public void addPayment(double amount) {
        if (amount > 0) {
            this.amountPaid += amount;
            String detail = String.format(
                    "Payment: -$%.2f",
                    amount
            );
            this.chargeDetails.add(detail);
            updateBalance();

            if (this.balance <= 0) {
                this.status = "Paid";
            } else {
                this.status = "Pending";
            }
        }
    }

    private void updateTotalCharges() {
        this.totalCharges = this.roomCharges + this.serviceCharges;
        updateBalance();
    }

    private void updateBalance() {
        this.balance = this.totalCharges - this.amountPaid;
        if (this.balance < 0) {
            this.balance = 0;

        }
    }

    public String getBillingDetails() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n========== BILLING DETAILS ==========\n");
        sb.append(String.format("Billing ID: %s\n", billingID));
        sb.append(String.format("Guest ID: %s\n", guestID));
        sb.append(String.format("Billing Date: %s\n", billingDate));
        sb.append("\n--- Charges ---\n");

        for (String detail : chargeDetails) {
            sb.append(detail).append("\n");
        }

        sb.append("\n--- Summary ---\n");
        sb.append(String.format("Room Charges: $%.2f\n", roomCharges));
        sb.append(String.format("Service Charges: $%.2f\n", serviceCharges));
        sb.append(String.format("Total Charges: $%.2f\n", totalCharges));
        sb.append(String.format("Amount Paid: $%.2f\n", amountPaid));
        sb.append(String.format("Balance Due: $%.2f\n", balance));
        sb.append(String.format("Status: %s\n", status));
        sb.append("=====================================\n");

        return sb.toString();
    }

    public void addToMap() {
        billingMap.put(this.guestID, this);
    }

    public static Billing getBillingByGuest(String guestID) {
        return billingMap.get(guestID);
    }

    public static Billing getOrCreateBilling(String guestID) {
        if (billingMap.containsKey(guestID)) {
            return billingMap.get(guestID);
        } else {
            Billing newBilling = new Billing(guestID);
            newBilling.addToMap();
            return newBilling;
        }
    }

    public static List<Billing> getAllBillings() {
        return new ArrayList<>(billingMap.values());
    }

    public static void clearMap() {
        billingMap.clear();
    }

    public static void resetCounter() {
        billingCounter = 0;
    }

    public Document toDocument() {
        Document doc = new Document();
        doc.append("billingID", billingID);
        doc.append("guestID", guestID);
        doc.append("roomCharges", roomCharges);
        doc.append("serviceCharges", serviceCharges);
        doc.append("totalCharges", totalCharges);
        doc.append("amountPaid", amountPaid);
        doc.append("balance", balance);
        doc.append("chargeDetails", chargeDetails);
        doc.append("billingDate", billingDate);
        doc.append("status", status);
        return doc;
    }

    public static Billing fromDocument(Document doc) {
        try {
            if (doc == null) return null;

            String guestID = doc.getString("guestID");
            if (guestID == null || guestID.isEmpty()) return null;

            Billing billing = new Billing(guestID);

            if (doc.containsKey("billingID")) {
                billing.billingID = doc.getString("billingID");
            }
            if (doc.containsKey("roomCharges")) {
                billing.roomCharges = doc.getDouble("roomCharges");
            }
            if (doc.containsKey("serviceCharges")) {
                billing.serviceCharges = doc.getDouble("serviceCharges");
            }
            if (doc.containsKey("totalCharges")) {
                billing.totalCharges = doc.getDouble("totalCharges");
            }
            if (doc.containsKey("amountPaid")) {
                billing.amountPaid = doc.getDouble("amountPaid");
            }
            if (doc.containsKey("balance")) {
                billing.balance = doc.getDouble("balance");
            }
            if (doc.containsKey("chargeDetails")) {
                @SuppressWarnings("unchecked")
                List<String> details = (List<String>) doc.get("chargeDetails");
                if (details != null) {
                    billing.chargeDetails = new ArrayList<>(details);
                }
            }
            if (doc.containsKey("billingDate")) {
                billing.billingDate = doc.getString("billingDate");
            }
            if (doc.containsKey("status")) {
                billing.status = doc.getString("status");
            }

            return billing;
        } catch (Exception e) {
            System.out.println("Error converting document to Billing: " + e.getMessage());
            return null;
        }
    }

    public boolean isPaid() {
        return balance <= 0;
    }

    @Override
    public String toString() {
        return String.format(
                "Billing[ID=%s, Guest=%s, Total=$%.2f, Paid=$%.2f, Balance=$%.2f, Status=%s]",
                billingID, guestID, totalCharges, amountPaid, balance, status
        );
    }
}

