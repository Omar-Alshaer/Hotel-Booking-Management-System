# Hotel Booking Management System

<p align="center">
<img width="180" alt="Hotel Logo" src="https://github.com/Omar-Alshaer/Hotel-Booking-Management-System/blob/main/hotel.png?raw=true">
<br>
<em>Hotel Booking Management System</em>
</p>

## 📋 Project Overview

**CSE111 - Data Structures (Fall 2025)** course project at **Alamein International University**. Full-stack Java desktop application implementing advanced data structures with MongoDB persistence and professional Swing GUI.

---

## 👥 Project Team

This project was developed by:

- Shaimaa Mohamed Kotit
- Omar Reda Elshaer

---

## ✨ Complete Feature Set

### **Management Modules**
- 👥 **Guests** - AVL Tree (Register/Search by ID/Name/Delete/List)
- 🏠 **Rooms** - Linked List (Add/View Available/Assign/Release)
- 📅 **Reservations** - Linked List (Create/Cancel/Sort by Date)
- 🛎️ **Services** - Circular Queue (Add/Process/View Pending - FIFO)
- 💰 **Billing** - HashMap (Auto-generate/Add Charges/Process Payments)
- 📊 **Reports** - Merge Sort (Reservations/Revenue/Occupancy Rate)

### **Production Features**
- MongoDB Atlas (5 collections)
- Auto-ID generation (G0001/R0001/RES0001/SR0001/B0001)
- Date validation (future dates + check-out > check-in)
- Real-time room assignment + auto-charging ($25/service)
- Professional GUI (7 panels + FormBuilder + JXDatePicker)

---

## 🏗️ Data Structures Implementation

| Module | Structure | Key Features | Time Complexity |
|--------|-----------|--------------|-----------------|
| **Guests** | **AVL Tree** | Self-balancing + Name/ID search + Rotations | **O(log n)** |
| **Rooms** | **Linked List** | Tail insert + Availability tracking | **O(1)** insert |
| **Reservations** | **Linked List** | Head insert + Merge Sort | **O(1)** add |
| **Services** | **Circular Queue** | FIFO + 100 capacity + Overflow protection | **O(1)** ops |
| **Billing** | **HashMap** | Auto-create + Real-time balance | **O(1)** lookup |

---



## 💾 MongoDB Schema

| Collection | Key Fields | Sample Document |
|------------|------------|-----------------|
| `guests` | `guestID`, `name`, `contactInfo`, `address[]` | `{"G0001", "Ahmed Hassan", "0123456789", "Cairo"}` |
| `rooms` | `roomID`, `roomType`, `isAvailable`, `price` | `{"R0001", "Single", true, 150.0}` |
| `reservations` | `resID`, `guestID`, `checkIn`, `status` | `{"RES0001", "G0001", "2026-01-15", "Confirmed"}` |
| `serviceRequests` | `reqID`, `guestID`, `serviceType`, `status` | `{"SR0001", "G0001", "Laundry", "Pending"}` |
| `billing` | `billID`, `guestID`, `total`, `balance` | `{"B0001", "G0001", 475.0, 275.0}` |

**Features:** Auto-sync • Startup load • Real-time CRUD


---

## 🛠️ Manual Setup

### 📁 Required JARs
```
swingx-all-1.6.5-1.jar          (GUI DatePicker)
mongodb-driver-core-5.1.1.jar   (MongoDB Core)
bson-5.1.1.jar                  (BSON)
mongodb-driver-sync-5.1.1.jar   (Sync Operations)
```

### ⚡ Compile & Run Commands

**Windows:**
```cmd
javac -cp ".;lib\*" *.java
java -cp ".;lib\*" Main
```

**Linux/Mac:**
```bash
javac -cp ".:lib/*" *.java
java -cp ".:lib/*" Main
```

---
 

## 📂 Project Structure
```
Hotel-Booking-Management-System/
├── hotel.png                 (Logo)
├── README.md                 (Documentation)
├── src/                      (Source Files)
│   ├── Main.java
│   ├── Guest.java            (AVL Tree)
│   ├── Room.java             (Linked List)
│   ├── Reservation.java      (Linked List + Merge Sort)
│   ├── ServiceRequest.java   (Circular Queue)
│   ├── Billing.java          (HashMap)
│   ├── ReportGenerator.java  (Merge Sort Reports)
│   ├── HotelManagementSystem.java (Main Controller)
│   ├── HotelGUI.java         (Complete GUI)
│   └── DataBase.java         (MongoDB Connection)
└── lib/                      (JAR Libraries)
    ├── swingx-all-1.6.5-1.jar
    ├── mongodb-driver-core-5.1.1.jar
    ├── bson-5.1.1.jar
    └── mongodb-driver-sync-5.1.1.jar
```

## 🎨 Professional Swing GUI

<p align="center">
  <img src="GUI.png?raw=true" alt="Hotel Booking Management System" width="1000"/>
  <br>
  <em>• 7 Management Modules • CardLayout Navigation • Custom FormBuilder • JXDatePicker</em>
</p>


---

## 📈 Performance Summary

```
✅ Guest Search: O(log n) - AVL Tree
✅ Billing Access: O(1) - HashMap
✅ Service Queue: O(1) - Circular Queue
✅ Reports: O(n log n) - Merge Sort
✅ MongoDB: Real-time sync
```

---

## 🔮 Key Validations
- ✅ Future check-in dates only
- ✅ Check-out > check-in
- ✅ Room availability check
- ✅ Guest existence validation
- ✅ Number format handling
- ✅ Empty field prevention

---

## 🎓 Academic Context

**Course:** CSE111 - Data Structures (Fall 2025)  
**University:** Alamein International University  