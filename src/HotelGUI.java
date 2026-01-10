import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.jdesktop.swingx.JXDatePicker;

public class HotelGUI extends JFrame {

    private HotelManagementSystem system;

    private JPanel mainPanel;
    private JPanel menuPanel;
    private JPanel contentWrapper;
    private JPanel cardPanel;
    private CardLayout cardLayout;

    private static final String CARD_WELCOME = "welcome";
    private static final String CARD_GUEST = "guest";
    private static final String CARD_ROOM = "room";
    private static final String CARD_RESERVATION = "reservation";
    private static final String CARD_SERVICE = "service";
    private static final String CARD_BILLING = "billing";
    private static final String CARD_REPORTS = "reports";

    public HotelGUI() {
        super("Hotel Booking Management System");
        system = new HotelManagementSystem();
        setupUIDefaults();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1500, 800);
        setLocationRelativeTo(null);

        mainPanel = new JPanel(new BorderLayout());
        add(mainPanel);

        createMenuPanel();
        mainPanel.add(menuPanel, BorderLayout.WEST);

        createContentPanel();
        mainPanel.add(contentWrapper, BorderLayout.CENTER);
    }

    private void setupUIDefaults() {
        UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD, 14));
        UIManager.put("TextField.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("TextArea.font", new Font("Segoe UI", Font.PLAIN, 13));
        UIManager.put("TitledBorder.font", new Font("Segoe UI", Font.BOLD, 14));
        UIManager.put("OptionPane.messageFont", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("Panel.background", Color.WHITE);
    }

    private class FormBuilder {
        private JPanel panel;
        private GridBagLayout layout;
        private GridBagConstraints gbc;
        private int row = 0;

        FormBuilder() {
            layout = new GridBagLayout();
            panel = new JPanel(layout);
            panel.setBackground(Color.WHITE);
            panel.setBorder(new EmptyBorder(10, 10, 10, 10));
            gbc = new GridBagConstraints();
            gbc.insets = new Insets(8, 8, 8, 8);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;
        }

        FormBuilder addTextFieldWithPlaceholder(String labelText, String placeholder, int columns) {
            addLabel(labelText);
            JTextField field = new JTextField(columns);
            field.setBackground(new Color(245, 245, 245));
            field.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
            field.setText(placeholder);
            field.setForeground(new Color(150, 150, 150));
            field.addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusGained(java.awt.event.FocusEvent evt) {
                    if (field.getText().equals(placeholder)) {
                        field.setText("");
                        field.setForeground(Color.BLACK);
                    }
                }

                public void focusLost(java.awt.event.FocusEvent evt) {
                    if (field.getText().isEmpty()) {
                        field.setText(placeholder);
                        field.setForeground(new Color(150, 150, 150));
                    }
                }
            });
            addField(field);
            return this;
        }

        FormBuilder addDatePicker(String labelText) {
            addLabel(labelText);
            JXDatePicker datePicker = new JXDatePicker();
            datePicker.setFormats(new SimpleDateFormat("yyyy-MM-dd"));
            datePicker.setBackground(new Color(245, 245, 245));
            addField(datePicker);
            return this;
        }

        FormBuilder addComboBox(String labelText, String[] items) {
            addLabel(labelText);
            JComboBox combo = new JComboBox<>(items);
            combo.setBackground(new Color(245, 245, 245));
            addField(combo);
            return this;
        }

        FormBuilder addNumberField(String labelText, int columns) {
            addLabel(labelText);
            JTextField field = new JTextField(columns);
            field.setBackground(new Color(245, 245, 245));
            field.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
            addField(field);
            return this;
        }

        private void addLabel(String text) {
            gbc.gridx = 0;
            gbc.gridy = row;
            gbc.weightx = 0.3;
            JLabel label = new JLabel(text);
            label.setFont(new Font("Segoe UI", Font.BOLD, 13));
            panel.add(label, gbc);
        }

        private void addField(JComponent field) {
            gbc.gridx = 1;
            gbc.gridy = row;
            gbc.weightx = 0.7;
            panel.add(field, gbc);
            row++;
        }

        JPanel getPanel() {
            return panel;
        }

        JComponent getComponent(int index) {
            return (JComponent) panel.getComponent(index * 2 + 1);
        }
    }

    private void createMenuPanel() {
        menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBorder(new EmptyBorder(15, 5, 15, 5));
        menuPanel.setPreferredSize(new Dimension(310, 0));
        menuPanel.setBackground(new Color(131, 179, 250));

        ImageIcon originalIcon = new ImageIcon("hotel.png");
        Image scaledImage = originalIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon hotelIcon = new ImageIcon(scaledImage);
        JLabel iconLabel = new JLabel(hotelIcon);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        iconLabel.setBorder(new EmptyBorder(20, 0, 20, 0));
        menuPanel.add(iconLabel);

        JLabel titleLabel = new JLabel("Hotel Booking Management System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(new EmptyBorder(8, 6, 50, 0));
        menuPanel.add(titleLabel);

        menuPanel.add(createMenuButton("Guest Management", e -> showCard(CARD_GUEST), null));
        menuPanel.add(Box.createRigidArea(new Dimension(0, 6)));
        menuPanel.add(createMenuButton("Room Management", e -> showCard(CARD_ROOM), null));
        menuPanel.add(Box.createRigidArea(new Dimension(0, 6)));
        menuPanel.add(createMenuButton("Reservations", e -> showCard(CARD_RESERVATION), null));
        menuPanel.add(Box.createRigidArea(new Dimension(0, 6)));
        menuPanel.add(createMenuButton("Service Requests", e -> showCard(CARD_SERVICE), null));
        menuPanel.add(Box.createRigidArea(new Dimension(0, 6)));
        menuPanel.add(createMenuButton("Billing", e -> showCard(CARD_BILLING), null));
        menuPanel.add(Box.createRigidArea(new Dimension(0, 6)));
        menuPanel.add(createMenuButton("Reports", e -> showCard(CARD_REPORTS), null));
        menuPanel.add(Box.createVerticalGlue());
        menuPanel.add(createMenuButton("Exit", e -> exitApplication(), new Color(220, 50, 50)));
    }

    private JButton createMenuButton(String text, ActionListener listener, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(280, 40));
        btn.setPreferredSize(new Dimension(280, 40));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(listener);
        if (bgColor != null) {
            btn.setBackground(bgColor);
            btn.setForeground(Color.WHITE);
        } else {
            btn.setBackground(new Color(255, 210, 0));
            btn.setForeground(Color.DARK_GRAY);
        }
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 210, 0)),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        return btn;
    }

    private void createContentPanel() {
        contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setBorder(new EmptyBorder(12, 12, 12, 12));
        contentWrapper.setBackground(Color.WHITE);
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(Color.WHITE);
        cardPanel.add(createWelcomePanel(), CARD_WELCOME);
        cardPanel.add(new GuestManagementPanel(), CARD_GUEST);
        cardPanel.add(new RoomManagementPanel(), CARD_ROOM);
        cardPanel.add(new ReservationPanel(), CARD_RESERVATION);
        cardPanel.add(new ServiceRequestsPanel(), CARD_SERVICE);
        cardPanel.add(new BillingPanel(), CARD_BILLING);
        cardPanel.add(new ReportsPanel(), CARD_REPORTS);
        contentWrapper.add(cardPanel, BorderLayout.CENTER);
    }

    private void showCard(String cardName) {
        cardLayout.show(cardPanel, cardName);
    }

    private JComponent createWelcomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        JLabel welcomeLabel = new JLabel("<html>" +
                "<div style='text-align: center; font-size: 24px; margin-top: 100px;'>" +
                "<b>Welcome</b><br>" +
                "Hotel Booking Management System" +
                "</div></html>");
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(welcomeLabel, BorderLayout.CENTER);
        return panel;
    }

    private void showPopup(String message, String title) {
        JTextArea textArea = new JTextArea(message);
        textArea.setEditable(false);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(520, 380));
        JOptionPane.showMessageDialog(this, scrollPane, title, JOptionPane.INFORMATION_MESSAGE);
    }

    private void exitApplication() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to exit?", "Confirm Exit",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                system.close();
            } catch (Exception ignored) {
            }
            System.exit(0);
        }
    }

    private JButton createActionButton(String text, ActionListener al) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(190, 36));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(al);
        btn.setBackground(new Color(60, 130, 200));
        btn.setForeground(Color.YELLOW);
        btn.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        return btn;
    }

    private class GuestManagementPanel extends JPanel {
        GuestManagementPanel() {
            setLayout(new BorderLayout(10, 10));
            setBorder(BorderFactory.createTitledBorder("Guest Management"));
            setBackground(Color.WHITE);
            JPanel actions = new JPanel();
            actions.setBackground(Color.WHITE);
            actions.setLayout(new FlowLayout(FlowLayout.LEFT, 12, 12));
            actions.add(createActionButton("Add Guest", e -> addGuest()));
            actions.add(createActionButton("Search by ID", e -> searchGuestByID()));
            actions.add(createActionButton("Search by Name", e -> searchGuestByName()));
            actions.add(createActionButton("Remove Guest", e -> removeGuest()));
            actions.add(createActionButton("View All Guests", e -> viewAllGuests()));
            add(actions, BorderLayout.NORTH);
        }

        private void addGuest() {
            FormBuilder form = new FormBuilder();
            form.addTextFieldWithPlaceholder("Name:", "e.g., Ahmed Hassan", 20)
                    .addTextFieldWithPlaceholder("Contact:", "e.g., +20123456789", 20)
                    .addTextFieldWithPlaceholder("Address:", "e.g., Cairo, Egypt", 20);
            int result = JOptionPane.showConfirmDialog(HotelGUI.this, form.getPanel(),
                    "Add New Guest", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String name = ((JTextField) form.getComponent(0)).getText().trim();
                String contact = ((JTextField) form.getComponent(1)).getText().trim();
                String address = ((JTextField) form.getComponent(2)).getText().trim();
                if (!name.isEmpty() && !contact.isEmpty() && !address.isEmpty()) {
                    String response = system.addGuest(name, contact, address);
                    showPopup(response, "Add Guest Result");
                } else {
                    showPopup("Please fill all fields!", "Error");
                }
            }
        }

        private void searchGuestByID() {
            String guestID = JOptionPane.showInputDialog(HotelGUI.this, "Enter Guest ID (e.g., G0001):");
            if (guestID != null && !guestID.trim().isEmpty()) {
                String result = system.searchGuestByID(guestID.trim());
                showPopup(result, "Search Result");
            }
        }

        private void searchGuestByName() {
            String name = JOptionPane.showInputDialog(HotelGUI.this, "Enter Guest Name:");
            if (name != null && !name.trim().isEmpty()) {
                String result = system.searchGuestByName(name.trim());
                showPopup(result, "Search Result");
            }
        }

        private void removeGuest() {
            String guestID = JOptionPane.showInputDialog(
                    HotelGUI.this,
                    "Enter Guest ID (e.g., G0001)"
            );

            if (guestID != null && !guestID.trim().isEmpty()) {
                String result = system.removeGuest(guestID.trim());
                showPopup(result, "Remove Guest Result");
            }
        }

        private void viewAllGuests() {
            String result = system.getAllGuests();
            showPopup(result, "All Guests");
        }
    }

    private class RoomManagementPanel extends JPanel {
        RoomManagementPanel() {
            setLayout(new BorderLayout(10, 10));
            setBorder(BorderFactory.createTitledBorder("Room Management"));
            setBackground(Color.WHITE);
            JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 12));
            actions.setBackground(Color.WHITE);
            actions.add(createActionButton("Add Room", e -> addRoom()));
            actions.add(createActionButton("View All Rooms", e -> viewAllRooms()));
            actions.add(createActionButton("View Available Rooms", e -> viewAvailableRooms()));
            add(actions, BorderLayout.NORTH);
        }

        private void addRoom() {
            FormBuilder form = new FormBuilder();
            form.addComboBox("Room Type:", new String[]{"Single", "Double", "Suite"})
                    .addNumberField("Capacity:", 10)
                    .addNumberField("Price per Night:", 10);
            int result = JOptionPane.showConfirmDialog(HotelGUI.this, form.getPanel(),
                    "Add New Room", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    JComboBox<?> typeCombo = (JComboBox<?>) form.getComponent(0);
                    String type = (String) typeCombo.getSelectedItem();
                    int capacity = Integer.parseInt(((JTextField) form.getComponent(1)).getText().trim());
                    double price = Double.parseDouble(((JTextField) form.getComponent(2)).getText().trim());
                    String response = system.addRoom(type, capacity, price);
                    showPopup(response, "Add Room Result");
                } catch (NumberFormatException ex) {
                    showPopup("Please enter valid numbers for capacity and price!", "Error");
                }
            }
        }

        private void viewAllRooms() {
            String result = system.getAllRooms();
            showPopup(result, "All Rooms");
        }

        private void viewAvailableRooms() {
            String result = system.getAvailableRooms();
            showPopup(result, "Available Rooms");
        }
    }

    private class ReservationPanel extends JPanel {
        ReservationPanel() {
            setLayout(new BorderLayout(10, 10));
            setBorder(BorderFactory.createTitledBorder("Reservation Management"));
            setBackground(Color.WHITE);
            JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 12));
            actions.setBackground(Color.WHITE);
            actions.add(createActionButton("Make Reservation", e -> makeReservation()));
            actions.add(createActionButton("Cancel Reservation", e -> cancelReservation()));
            actions.add(createActionButton("View All Reservations", e -> viewReservations()));
            add(actions, BorderLayout.NORTH);
        }

        private void makeReservation() {
            FormBuilder form = new FormBuilder();
            form.addTextFieldWithPlaceholder("Guest ID:", "e.g., G0001", 15)
                    .addTextFieldWithPlaceholder("Room ID:", "e.g., R0001", 15)
                    .addDatePicker("Check-in Date:")
                    .addDatePicker("Check-out Date:");
            int result = JOptionPane.showConfirmDialog(HotelGUI.this, form.getPanel(),
                    "Make Reservation", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    String guestID = ((JTextField) form.getComponent(0)).getText().trim();
                    String roomID = ((JTextField) form.getComponent(1)).getText().trim();
                    JXDatePicker checkInPicker = (JXDatePicker) form.getComponent(2);
                    JXDatePicker checkOutPicker = (JXDatePicker) form.getComponent(3);
                    Date checkInDate = checkInPicker.getDate();
                    Date checkOutDate = checkOutPicker.getDate();
                    if (checkInDate == null || checkOutDate == null) {
                        showPopup("Please select both check-in and check-out dates!", "Error");
                        return;
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String checkIn = sdf.format(checkInDate);
                    String checkOut = sdf.format(checkOutDate);
                    if (!guestID.isEmpty() && !roomID.isEmpty()) {
                        String response = system.makeReservation(guestID, roomID, checkIn, checkOut);
                        showPopup(response, "Reservation Result");
                    } else {
                        showPopup("Please fill all fields!", "Error");
                    }
                } catch (Exception ex) {
                    showPopup("Error processing data: " + ex.getMessage(), "Error");
                }
            }
        }

        private void cancelReservation() {
            String resID = JOptionPane.showInputDialog(HotelGUI.this,
                    "Enter Reservation ID (e.g., RES0001):");
            if (resID != null && !resID.trim().isEmpty()) {
                String result = system.cancelReservation(resID.trim());
                showPopup(result, "Cancel Result");
            }
        }

        private void viewReservations() {
            String result = system.generateReservationReport();
            showPopup(result, "All Reservations");
        }
    }

    private class ServiceRequestsPanel extends JPanel {
        ServiceRequestsPanel() {
            setLayout(new BorderLayout(10, 10));
            setBorder(BorderFactory.createTitledBorder("Service Request Management"));
            setBackground(Color.WHITE);
            JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 12));
            actions.setBackground(Color.WHITE);
            actions.add(createActionButton("Add Service Request", e -> addServiceRequest()));
            actions.add(createActionButton("Process Next Request", e -> processServiceRequest()));
            actions.add(createActionButton("View Pending Requests", e -> viewPendingRequests()));
            add(actions, BorderLayout.NORTH);
        }

        private void addServiceRequest() {
            FormBuilder form = new FormBuilder();
            form.addTextFieldWithPlaceholder("Guest ID:", "e.g., G0001", 15)
                    .addComboBox("Service Type:",
                            new String[]{"Room Service", "Laundry", "Maintenance", "Cleaning"});
            int result = JOptionPane.showConfirmDialog(HotelGUI.this, form.getPanel(),
                    "Add Service Request", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String guestID = ((JTextField) form.getComponent(0)).getText().trim();
                JComboBox<?> serviceCombo = (JComboBox<?>) form.getComponent(1);
                String serviceType = (String) serviceCombo.getSelectedItem();
                if (!guestID.isEmpty()) {
                    String response = system.addServiceRequest(guestID, serviceType);
                    showPopup(response, "Service Request Result");
                } else {
                    showPopup("Please enter Guest ID!", "Error");
                }
            }
        }

        private void processServiceRequest() {
            String result = system.processNextServiceRequest();
            showPopup(result, "Process Request");
        }

        private void viewPendingRequests() {
            String result = system.getPendingServiceRequests();
            showPopup(result, "Pending Requests");
        }
    }

    private class BillingPanel extends JPanel {
        BillingPanel() {
            setLayout(new BorderLayout(10, 10));
            setBorder(BorderFactory.createTitledBorder("Billing Management"));
            setBackground(Color.WHITE);
            JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 12));
            actions.setBackground(Color.WHITE);
            actions.add(createActionButton("View Guest Bill", e -> viewGuestBill()));
            actions.add(createActionButton("Process Payment", e -> processPayment()));
            add(actions, BorderLayout.NORTH);
        }

        private void viewGuestBill() {
            String guestID = JOptionPane.showInputDialog(HotelGUI.this, "Enter Guest ID:");
            if (guestID != null && !guestID.trim().isEmpty()) {
                String result = system.getBillingForGuest(guestID.trim());
                showPopup(result, "Guest Bill");
            }
        }

        private void processPayment() {
            FormBuilder form = new FormBuilder();
            form.addTextFieldWithPlaceholder("Guest ID:", "e.g., G0001", 15)
                    .addNumberField("Payment Amount:", 15);
            int result = JOptionPane.showConfirmDialog(HotelGUI.this, form.getPanel(),
                    "Process Payment", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    String guestID = ((JTextField) form.getComponent(0)).getText().trim();
                    double amount = Double.parseDouble(((JTextField) form.getComponent(1)).getText().trim());
                    String response = system.processPayment(guestID, amount);
                    showPopup(response, "Payment Result");
                } catch (NumberFormatException e) {
                    showPopup("Please enter a valid amount!", "Error");
                }
            }
        }
    }

    private class ReportsPanel extends JPanel {
        ReportsPanel() {
            setLayout(new BorderLayout(10, 10));
            setBorder(BorderFactory.createTitledBorder("Reports"));
            setBackground(Color.WHITE);
            JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 12));
            actions.setBackground(Color.WHITE);
            actions.add(createActionButton("Reservation Report", e -> showReservationReport()));
            actions.add(createActionButton("Revenue Report", e -> showRevenueReport()));
            actions.add(createActionButton("Occupancy Report", e -> showOccupancyReport()));
            add(actions, BorderLayout.NORTH);
        }

        private void showReservationReport() {
            String result = system.generateReservationReport();
            showPopup(result, "Reservation Report");
        }

        private void showRevenueReport() {
            String result = system.generateRevenueReport();
            showPopup(result, "Revenue Report");
        }

        private void showOccupancyReport() {
            String result = system.generateOccupancyReport();
            showPopup(result, "Occupancy Report");
        }
    }
}
