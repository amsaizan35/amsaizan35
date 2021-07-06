package model;

import java.sql.Timestamp;

/** Appointments class. */
public class Appointments {



    /** Declare Appointments variables. */
    public int apptID;
    public String title;
    public String description;
    public String location;
    public String type;
    public Timestamp start;
    public Timestamp end;
    public int custID;
    public int userID;
    public String contactName;
    public String email;
    public Timestamp createDate;
    public String createdBy;
    public Timestamp lastUpdate;
    public String lastUpdatedBy;
    public int contactID ;


    /** Constructor for appointments with appointment id. */
    public Appointments(int apptID){
        this.apptID=apptID;
    }

    /** Constructor for appointments without contact info. */
    public Appointments(int apptID, String title, String description, String location, String type, Timestamp start,
                        Timestamp end, int custID,  int userID, int contactID) {
        this.apptID=apptID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.custID = custID;
        this.userID=userID;
        this.contactID=contactID;

    }


    /** Constructor for appointments with contact info. */
    public Appointments(int apptID, String title, String description, String location, String type, Timestamp start,
                       Timestamp end, int custID,  int userID, int contactID, String contactName, String email) {
        this.apptID = apptID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.custID = custID;
        this.contactName=contactName;
        this.userID=userID;
        this.contactID=contactID;
        this.email=email;
    }



/** Appointments getters and setters. */
    public int getUserID() {
        return userID;
    }

    public int getApptID() {
        return apptID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Timestamp getStart() {
        return start;
    }

    public void setStart(Timestamp start) {
        this.start = start;
    }

    public Timestamp getEnd() {
        return end;
    }

    public void setEnd(Timestamp end) {
        this.end = end;
    }

    public int getCustID() {
        return custID;
    }

    public int getContactID() {
        return contactID;
    }

    public void setApptID(int apptID) {
        this.apptID = apptID;
    }

    public void setCustID(int custID) {
        this.custID = custID;
    }

    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    /** Method to display appointments. */
    @Override
    public String toString() {
        return ("[" + apptID + "]" + title+ " " +description + " " + location+ " " + type + " " + start +" " + end + ""+ "[" + custID +"]" +" " + "["+ contactID + "]" + " " + contactName + " " +email);
    }
}