
# Hotel Booking Management System

<p align="center">
  <img src="https://github.com/Omar-Alshaer/Hotel-Booking-Management-System/blob/main/hotel.png?raw=true" alt="Hotel Management System" width="200" height="200"/>
  <br>
  <strong>Hotel Booking Management System</strong>
</p>

## 📋 Project Overview

**CSE111 - Data Structures (Fall 2025)** course project at **Alamein International University**. Full-stack Java desktop application implementing advanced data structures with MongoDB persistence and professional Swing GUI.

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
- MongoDB Atlas (5 collections: guests/rooms/reservations/serviceRequests/billing)
- Auto-ID generation (G0001/R0001/RES0001/SR0001/B0001)
- Date validation (future dates + check-out > check-in)
- Real-time room assignment + auto-charging
- Professional GUI (7 panels + FormBuilder + JXDatePicker)

---

## 🏗️ Data Structures Deep Dive

| Module | Structure | Key Features | Complexity |
|--------|-----------|--------------|------------|
| **Guests** | **AVL Tree** | Self-balancing + Name search + Rotations (LL/LR/RL/RR) | O(log n) |
| **Rooms** | **Singly Linked List** | Tail insertion + Availability tracking | O(1) insert |
| **Reservations** | **Singly Linked List** | Head insertion + Merge Sort by date | O(1) add |
| **Services** | **Circular Queue** | FIFO + Fixed 100 capacity + Overflow protection | O(1) ops |
| **Billing** | **HashMap** | Auto-create + Real-time updates | O(1) lookup |

---

## 🔌 MongoDB Schema (5 Collections)

```
guests: {guestID, name, contact, address, bookingHistory[], paymentHistory[]}
rooms: {roomID, type, capacity, isAvailable, pricePerNight}
reservations: {resID, guestID, roomID, checkIn, checkOut, status, nights}
serviceRequests: {reqID, guestID, type, date, status}
billing: {billID, guestID, roomCharges, serviceCharges, total, paid, balance}
```

---

## 🎨 GUI Highlights
```
✅ 1500x800 responsive design
✅ Custom FormBuilder w/ placeholders
✅ 7 CardLayout panels
✅ Professional Segoe UI styling
✅ Hover effects + focus animations
✅ Real-time validation + popups
✅ Logo integration (hotel.png)
```

---

## 🚀 Production-Ready Setup

```bash
# 1. Clone & Configure
git clone your-repo
# Update DataBase.java MongoDB URI

# 2. Dependencies (Maven)
mongodb-driver-sync:4.x.x
swingx-all:1.6.5-1

# 3. Run
java Main
```

---

## 📈 Verified Performance

```
Guest Search: O(log n) ✓ AVL Tree
Billing Lookup: O(1) ✓ HashMap  
Service Queue: O(1) ✓ Circular Queue
Report Sort: O(n log n) ✓ Merge Sort
MongoDB Sync: Startup only ✓
```

---

## 🎓 Academic Excellence

**CSE111 Data Structures (Fall 2025)**  
**Alamein International University**  
**Grade A Implementation:** All required structures + bonus MongoDB + GUI