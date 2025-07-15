package models;

public class Ticket {
    private int id;
    private String passengerName;
    private int trainId;
    private String pnr; // ✅ Changed to String

    public Ticket(int id, String passengerName, int trainId, String pnr) {
        this.id = id;
        this.passengerName = passengerName;
        this.trainId = trainId;
        this.pnr = pnr;
    }

    public int getId() {
        return id;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public int getTrainId() {
        return trainId;
    }

    public String getPnr() {
        return pnr;
    }

    @Override
    public String toString() {
        return "🎫 Ticket ID: " + id +
                ", Name: " + passengerName +
                ", Train ID: " + trainId +
                ", PNR: " + pnr;
    }
}
