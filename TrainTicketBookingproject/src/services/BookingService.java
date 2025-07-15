package services;

import models.Ticket;
import util.DBConnection;

import java.sql.*;
import java.util.UUID;

public class BookingService {

    public void bookTicket(String passengerName, int trainId) {
        String pnr = generatePNR();
        String insertSQL = "INSERT INTO tickets (passenger_name, train_id, pnr) VALUES (?, ?, ?)";
        String updateTrainSQL = "UPDATE trains SET seats_available = seats_available - 1 WHERE id = ? AND seats_available > 0";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement insertStmt = conn.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement updateStmt = conn.prepareStatement(updateTrainSQL)) {

            // First, reduce available seat
            updateStmt.setInt(1, trainId);
            int rowsUpdated = updateStmt.executeUpdate();

            if (rowsUpdated == 0) {
                System.out.println("❌ Booking failed: No seats available.");
                return;
            }

            // Insert ticket into DB
            insertStmt.setString(1, passengerName);
            insertStmt.setInt(2, trainId);
            insertStmt.setString(3, pnr);

            int rows = insertStmt.executeUpdate();

            if (rows > 0) {
                ResultSet generatedKeys = insertStmt.getGeneratedKeys();
                int generatedId = 0;
                if (generatedKeys.next()) {
                    generatedId = generatedKeys.getInt(1);
                }

                Ticket ticket = new Ticket(generatedId, passengerName, trainId, pnr);
                System.out.println("✅ Ticket booked successfully!");
                System.out.println(ticket);
            } else {
                System.out.println("❌ Ticket booking failed.");
            }

        } catch (SQLException e) {
            System.out.println("❌ Error booking ticket: " + e.getMessage());
        }
    }


    private String generatePNR() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public void viewAllTickets() {
        String sql = "SELECT * FROM tickets";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n🎟️ All Tickets:");
            while (rs.next()) {
                Ticket ticket = new Ticket(
                        rs.getInt("id"),
                        rs.getString("passenger_name"),
                        rs.getInt("train_id"),
                        rs.getString("pnr")
                );
                System.out.println(ticket);
            }

        } catch (SQLException e) {
            System.out.println("❌ Error viewing tickets: " + e.getMessage());
        }
    }

    public void searchTicketByPNR(String pnr) {
        String sql = "SELECT * FROM tickets WHERE pnr = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, pnr);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Ticket ticket = new Ticket(
                        rs.getInt("id"),
                        rs.getString("passenger_name"),
                        rs.getInt("train_id"),
                        rs.getString("pnr")
                );
                System.out.println("✅ Ticket Found:");
                System.out.println(ticket);
            } else {
                System.out.println("❌ No ticket found with PNR: " + pnr);
            }

        } catch (SQLException e) {
            System.out.println("❌ Error searching ticket: " + e.getMessage());
        }
    }

    public void cancelTicket(String pnr) {
        String getTrainIdSQL = "SELECT train_id FROM tickets WHERE pnr = ?";
        String deleteSQL = "DELETE FROM tickets WHERE pnr = ?";
        String updateSeatsSQL = "UPDATE trains SET seats_available = seats_available + 1 WHERE id = ?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false); // transaction start

            int trainId = -1;
            try (PreparedStatement getStmt = conn.prepareStatement(getTrainIdSQL)) {
                getStmt.setString(1, pnr.trim());
                ResultSet rs = getStmt.executeQuery();
                if (rs.next()) {
                    trainId = rs.getInt("train_id");
                } else {
                    System.out.println("❌ No ticket found with PNR: " + pnr);
                    return;
                }
            }

            try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSQL);
                 PreparedStatement updateStmt = conn.prepareStatement(updateSeatsSQL)) {

                deleteStmt.setString(1, pnr.trim());
                updateStmt.setInt(1, trainId);

                deleteStmt.executeUpdate();
                updateStmt.executeUpdate();

                conn.commit();
                System.out.println("✅ Ticket cancelled successfully.");

            } catch (SQLException e) {
                conn.rollback();
                System.out.println("❌ Error cancelling ticket: " + e.getMessage());
            }

        } catch (SQLException e) {
            System.out.println("❌ DB Error: " + e.getMessage());
        }
    }
}
