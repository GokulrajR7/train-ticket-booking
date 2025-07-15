//import util.DBConnection;
//import java.sql.Connection;
//import java.sql.SQLException;
//
//public class Main {
//    public static void main(String[] args) {
//        try (Connection conn = DBConnection.getConnection()) {
//            if (conn != null) {
//                System.out.println("✅ Database connection successful!");
//            } else {
//                System.out.println("❌ Connection failed.");
//            }
//        } catch (SQLException e) {
//            System.out.println("❌ Database error: " + e.getMessage());
//        }
//    }
//}






import services.TrainService;
import services.BookingService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        TrainService trainService = new TrainService();
        BookingService bookingService = new BookingService();

        while (true) {
            System.out.println("\n--- Train Ticket Booking ---");
            System.out.println("1. View All Trains");
            System.out.println("2. Book Ticket");
            System.out.println("3. View All Tickets");
            System.out.println("4. Search Ticket by PNR");
            System.out.println("5. Cancel Ticket");
            System.out.println("6. Exit");
            System.out.print("Choose option: ");
            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    trainService.getAllTrains();
                    break;
                case 2:
                    System.out.print("Enter your name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter Train ID to book: ");
                    int trainId = sc.nextInt();
                    bookingService.bookTicket(name, trainId);
                    break;
                case 3:
                    bookingService.viewAllTickets();
                    break;
                case 4:
                    System.out.print("Enter PNR to search: ");
                    String pnr = sc.nextLine();
                    bookingService.searchTicketByPNR(pnr);
                    break;
                case 5:
                    System.out.print("Enter PNR to cancel: ");
                    String cancelPnr = sc.nextLine();
                    bookingService.cancelTicket(cancelPnr);
                    break;
                case 6:
                    System.out.println("👋 Exiting... Thank you!");
                    return;
                default:
                    System.out.println("❌ Invalid option. Try again.");
            }
        }
    }
}
