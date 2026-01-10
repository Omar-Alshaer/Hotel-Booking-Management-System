import javax.swing.*;

public class Main {
    
    /**
       Team:
    Omar Reda Mansour Elshaer, ID:24100592
    Rohima Ahmed Ibrahim Abdelrahman, ID:24100546
    Shaimaa Mohamed Mostafa Abdelhamid Kotit, ID:24100570
        **/

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                HotelGUI frame = new HotelGUI();
                frame.setVisible(true);
                System.out.println("Hotel Booking Management System");
            } catch (Exception e) {
                System.err.println("Error starting application: " + e.getMessage());
            }
        });
    }
}
