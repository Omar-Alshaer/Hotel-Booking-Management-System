import org.bson.Document;

import java.util.ArrayList;

import java.util.List;

import java.text.SimpleDateFormat;

import java.util.Date;

public class ServiceRequest {

    private String requestID;

    private String guestID;

    private String serviceType;

    private String requestDate;

    private String status;

    private static ServiceRequest[] queue = new ServiceRequest[100];

    private static int front = 0;

    private static int rear = -1;

    private static int count = 0;

    private static List<ServiceRequest> allRequests = new ArrayList<>();

    private static int requestCounter = 0;

    public ServiceRequest(String guestID, String serviceType) {

        requestCounter++;

        this.requestID = String.format("SR%04d", requestCounter);

        this.guestID = guestID;

        this.serviceType = serviceType;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        this.requestDate = sdf.format(new Date());

        this.status = "Pending";
    }

    public ServiceRequest(String requestID, String guestID, String serviceType,
                          String requestDate, String status) {

        this.requestID = requestID;

        this.guestID = guestID;

        this.serviceType = serviceType;

        this.requestDate = requestDate;

        this.status = status;

        if (requestID != null && requestID.startsWith("SR") && requestID.length() > 2) {
            try {

                int idNum = Integer.parseInt(requestID.substring(2));

                if (idNum >= requestCounter) {
                    requestCounter = idNum;
                }

            } catch (NumberFormatException e) {

            }
        }
    }

    public void addRequest() {

        if (count >= queue.length) {
            System.out.println("Queue is full!");
            return;
        }

        rear = (rear + 1) % queue.length;

        queue[rear] = this;

        count++;

        allRequests.add(this);
    }

    public static ServiceRequest completeRequest() {

        if (count == 0) {
            return null;
        }

        ServiceRequest completed = queue[front];

        completed.status = "Completed";

        front = (front + 1) % queue.length;

        count--;

        return completed;
    }


    public static List<ServiceRequest> getPendingRequests() {

        List<ServiceRequest> pending = new ArrayList<>();

        int current = front;

        for (int i = 0; i < count; i++) {

            pending.add(queue[current]);

            current = (current + 1) % queue.length;
        }

        return pending;
    }

    public static void clearQueue() {

        queue = new ServiceRequest[100];

        front = 0;

        rear = -1;

        count = 0;

        allRequests.clear();
    }

    public static void resetCounter() {

        requestCounter = 0;
    }

    public Document toDocument() {

        return new Document("requestID", requestID)
                .append("guestID", guestID)
                .append("serviceType", serviceType)
                .append("requestDate", requestDate)
                .append("status", status);
    }

    public static ServiceRequest fromDocument(Document doc) {

        if (doc == null) {
            return null;
        }

        String requestID = doc.getString("requestID");

        if (requestID == null || requestID.isEmpty()) {
            return null;
        }

        return new ServiceRequest(
                requestID,
                doc.getString("guestID") != null ? doc.getString("guestID") : "Unknown",
                doc.getString("serviceType") != null ? doc.getString("serviceType") : "General",
                doc.getString("requestDate") != null ? doc.getString("requestDate") : "N/A",
                doc.getString("status") != null ? doc.getString("status") : "Pending"
        );
    }

    public String getRequestDetails() {

        return "ID: " + requestID + ", Guest: " + guestID +
                ", Service: " + serviceType + ", Date: " + requestDate +
                ", Status: " + status;
    }

    public String getRequestID() { return requestID; }
    public String getStatus() { return status; }
}
