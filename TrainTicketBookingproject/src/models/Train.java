package models;

public class Train {
    private int id;
    private String name;
    private String source;
    private String destination;

    private int seatsAvailable;


    public Train(int id, String name, String source, String destination, int seatsAvailable) {
        this.id = id;
        this.name = name;
        this.source = source;
        this.destination = destination;
        this.seatsAvailable = seatsAvailable;
    }
    public int getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public int getSeatsAvailable() {
        return seatsAvailable;
    }
    @Override
    public String toString(){
        return "Train Id: " +id+
                ", Train Name: " +name+
                ", Train Route: " +source+ "-->" +destination+
                ", Seat Available: " +seatsAvailable;
    }

}
