package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utilities.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/** Contacts class. */
public class Contacts {


    /** Declare Contacts variables. */
    private int contactID;
    private String contactName;
    private String email;


    /** List to hold users contacts. */
    private static ObservableList<Appointments> contAppt = FXCollections.observableArrayList();



    /** Constructor for Contacts. */
    public Contacts(int contactID, String contactName, String email) {
        this.contactID = contactID;
        this.contactName = contactName;
        this.email = email;
    }

    /** Method to get contacts appointments. Displays all appointments for contact. */
    public static ObservableList<Appointments> getContAppt(int id){

            contAppt.clear();
        try {

            String sql = "Select * from userAppt where contact_id = ? ;";
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


                Appointments contApp = new Appointments(apptID, title, description, location, type, start, end, custID, userID, contactId, contactName, email);
                contAppt.add(contApp);

            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return contAppt;
    }


    public int getContactID() {
        return contactID;
    }

    public String getContactName() {
        return contactName;
    }

    public String getEmail() {
        return email;
    }

    /** Method to display contacts. */
    @Override
    public String toString() {
        return (" ["+ contactID +"] " + " " +  contactName + " "  + email);
    }
}

