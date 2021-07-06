package model;

import controller.CustomersDAOImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utilities.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;


/** Customers class. */
public class Customers {

    private int custID;
    private String custName;
    private String address;
    private String firstLevel;
    private String country;
    private String postalCode;
    private String phone;
    private int divID;
    private int countryID;

/** List of ll customer appointments. */
    private static ObservableList<Appointments> custAppts = FXCollections.observableArrayList();


    /** Customers constructor. Gets customer info. */
    public Customers(int custID, String custName, String address, String firstLevel, String country, String postalCode, String phone, int divID) {
        this.custID = custID;
        this.custName = custName;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.divID=divID;
        this.country=country;
        this.firstLevel=firstLevel;

    }


    /** Customers Constructor  for including country. */
    public Customers(int custID, String custName, String address, String firstLevel, String country, String postalCode, String phone, int divID, int countryID) {
        this.custID = custID;
        this.custName = custName;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.divID=divID;
        this.country=country;
        this.firstLevel=firstLevel;
        this.countryID=countryID;

    }

    /** Method to get customer appointments. Gets appointments from database. */
    public static ObservableList<Appointments> getCustAppts(int id){

        try{
            custAppts.clear();
            String sql ="SELECT * from userAppt where customer_id =?;";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
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


                Appointments app = new Appointments(apptID, title, description, location, type, start, end, custID, userID, contactId, contactName, email);
                custAppts.add(app);

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return custAppts;
    }

    /** Method to add customer appointments. Adds to list. */
    public static void addCustAppts(Appointments a){
        custAppts.add(a);
    }

    /** Method to delete customer appts. Deletes from list. */
    public static  void deleteCustAppts(Appointments a){
        custAppts.remove(a);
    }


    /** Getter and setters for Customers class. */


    public int getCountryID() {
        return countryID;
    }

    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    public int getCustID() {
        return custID;
    }


    public String getCustName() {
        return custName;
    }


    public String getAddress() {
        return address;
    }


    public String getPostalCode() {
        return postalCode;
    }


    public String getPhone() {
        return phone;
    }

    public void setCustID(int custID) {
        this.custID = custID;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFirstLevel() {
        return firstLevel;
    }

    public void setFirstLevel(String firstLevel) {
        this.firstLevel = firstLevel;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getDivID() {
        return divID;
    }

    public void setDivID(int divID) {
        this.divID = divID;
    }

    /** Method to display customer info. */
    @Override
    public String toString() {
        return ("[" + custID + "]"  + " " + custName + " " + address + " " + postalCode + " "+ phone);
    }
}

