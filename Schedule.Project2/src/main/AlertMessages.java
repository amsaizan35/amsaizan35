package main;

import controller.CustomersDAOImpl;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.sql.Timestamp;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;


/** This is the AlertMessage class that holds the alerts used in the application. */
public class AlertMessages {

    static ResourceBundle rb = ResourceBundle.getBundle("main/lang", Locale.getDefault());

    /** Method to show alert. Deletes appointment.
     @param id Appointment ID.
     @param type and type to display. */
    public static void  confirmDelete( int id, String type) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle( "Delete Appointment");
        alert.setHeaderText( "Cancelling appointment" );
        alert.setContentText("Appointment ID:   " + id + " type: " + type + " was cancelled.");

    }

    /** Method to show alert. Deletes appointment.
     @param id Appointment ID.
     @param name Name to display. */
    public static void  confirmDeleteCust( int id, String name) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle( "Delete Customer");
        alert.setHeaderText( "Deleting Customer" );
        alert.setContentText("[" + id + "]" + name + " was deleted.");
        alert.showAndWait();
    }
    /** Method to show alerts for information.
     @param id ID to display
     @param start Start date/Time.
     */
    public static void apptReminder( int id, Timestamp start, long min) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle( rb.getString("15Min"));
            alert.setHeaderText(rb.getString( "ID")+id + " " + rb.getString("scheduled")+ start);
            alert.setContentText(rb.getString("approx") +" " + +min+ " " +  rb.getString("min"));
            alert.showAndWait();


    }


    /** Method that shows error windows. Display Error Messages.
     @param code Takes in code number to display appropriate code message. */
    public static void errorWindow(int code) {
        if (code == 1) {

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(rb.getString("NO"));
                alert.setHeaderText(rb.getString("NO"));
                alert.setContentText(rb.getString("upcoming"));
                alert.showAndWait();

        }
        if (code == 2) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(rb.getString("sel"));
            alert.setHeaderText(rb.getString("inv"));
            alert.setContentText(rb.getString("select"));
            alert.showAndWait();
        }

        if (code == 3) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("WARNING ");
            alert.setHeaderText("Invalid Values");
            alert.setContentText("Please verify that all values are valid.");
            alert.showAndWait();

        }
        if (code == 4) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning: Check Dates");
            alert.setHeaderText("Invalid Dates");
            alert.setContentText("End date can not be before start date.");
            alert.showAndWait();
        }

        if(code==5){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(rb.getString("unav"));
            alert.setHeaderText(rb.getString("noappt"));
            alert.setContentText(rb.getString("fulls"));
            alert.showAndWait();
        }
        if(code == 6){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(rb.getString("Invalid"));
            alert.setHeaderText(rb.getString("Error"));
            alert.setContentText(rb.getString( "usernpass"));
            alert.showAndWait();

        }
        if (code == 7) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(rb.getString("deletion"));
            alert.setHeaderText(rb.getString("still"));
            alert.setContentText(rb.getString("first"));
            alert.showAndWait();

        }
        if (code == 8) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning: Check Times");
            alert.setHeaderText("Invalid Time");
            alert.setContentText("End time can not be before start time.");
            alert.showAndWait();
        }


    }


}

