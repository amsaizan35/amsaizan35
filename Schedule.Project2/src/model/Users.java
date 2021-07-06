package model;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utilities.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;


/** This is the Users class that holds the users information. */
public class Users {

    private int userID;
    private  String username;
    private String password;


    /** User Constructor  for username and id. */
    public Users(int userID, String username ){
        this.userID = userID;
        this.username = username;
    }
    /** User Constructor  for username, password and id. */
    public Users(int userID, String username, String password){
        this.userID=userID;
        this.username=username;
        this.password=password;

    }



    /** List to store all users appointments. */
    private static ObservableList<Appointments> allUserAppts = FXCollections.observableArrayList();



    /** Method to get all appointments. Displays users appointments.
     @return all user appointments. */
    public static ObservableList<Appointments> getAllUserAppts(int id){

        try {
            allUserAppts.clear();
            String sql = "Select * from userAppt where user_id = ? ;";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int apptID = rs.getInt("appointment_id");
                String title = rs.getString("title");
                String description = rs.getString("description");
                String location = rs.getString("location");
                String type = rs.getString("type");
                Timestamp start = rs.getTimestamp("start");
                Timestamp end = rs.getTimestamp("end");
                int custID = rs.getInt("customer_id");
                int userID = rs.getInt("user_id");
                int contactId = rs.getInt("contact_id");
                String contactName = rs.getString("contact_name");
                String email = rs.getString("email");

                Appointments userApp = new Appointments(apptID, title, description, location, type, start, end, custID, userID, contactId, contactName, email);
                allUserAppts.add(userApp);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return allUserAppts;
    }


    /** Method to add users appointments. Adds appointments to user appointment list.
     @param selected appointment to add. */
    public void addUserAppt(Appointments selected){
        allUserAppts.add(selected);

    }

    /** Method to delete user appointments. Deletes users appointments from list.
     @param appt appointment to delete.  */
    public void  deleteUserAppt(Appointments appt){
        if(appt != null)
            allUserAppts.remove(appt);
    }



    /** Getters and setters for users class. */
    public int getUserID() {
        return userID;
    }

    public  String getUsername() {
        return username;
    }


    /** Method to display users info. */
    @Override
    public String toString() {
        return ("[" + userID + "]" + "  username is " +  username + " | password is  " + password);
    }
}
