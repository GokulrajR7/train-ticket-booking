package services;
import util.DBConnection;
import models.Train;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class TrainService {
    public List<Train> getAllTrains(){
        List<Train> trains=new ArrayList<>();
        String sql="SELECT * FROM trains";

        try(Connection conn=DBConnection.getConnection();Statement stmt=conn.createStatement();ResultSet rs=stmt.executeQuery(sql)){
            while(rs.next()){
                Train train=new Train(rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("source"),
                        rs.getString("destination"),
                        rs.getInt("seats_available"));
                trains.add(train);
            }
        }catch (SQLException e){
            System.out.println("Error fetching trains: " +e.getMessage());
        }
        return trains;
    }

}
