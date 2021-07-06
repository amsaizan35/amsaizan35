package main;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.util.Optional;

/** This is the AlertMessage class that holds the alerts used in the application. */
public class AlertMessages {

    /** Method to show alert. Displays a message to return to main screen.  */
    public static void cancelWindow(){

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Cancel");
            alert.setHeaderText("Canceled");
            alert.setContentText(" returning to main screen");
            alert.showAndWait();

    }

    /** Method to show alert. Displays a message that product has associated parts.
     @param name Name to display.*/
    public static void  confirmDelete(String name) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle( " Delete associated parts");
        alert.setHeaderText( name + "  has parts assigned to it!");
        alert.setContentText("Delete parts FIRST then delete product");
        alert.showAndWait();
    }

    /** Method to show alerts for information. Displays an item was deleted or an error occurred.
     @param code Code.
     @param name Name to display.
     */
    public static void infoWindow(int code, String name) {
        if (code == 1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Confirmed");
            alert.setHeaderText("Confirm deletion");
            alert.setContentText(name + " was deleted from table and list ");
            alert.showAndWait();

        }
        if(code == 2){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("An Error occurred");
            alert.show();
        }

    }


    /** Method that shows error windows. Display Error Messages.
     @param code Takes in code number to display appropriate code. */
    public static void errorWindow(int code) {
        if (code == 1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Inventory is empty");
            alert.setContentText("No inventory to select");
            alert.showAndWait();
        }
        if (code == 2) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Selection");
            alert.setContentText("Select an item");
            alert.showAndWait();
        }
        if(code==3){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No Item Found ");
            alert.setContentText("Item not in inventory");
            alert.showAndWait();
        }
        if(code==4){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Values");
            alert.setContentText("Please verify that all values are valid.");
            alert.showAndWait();

        }
        if(code==5){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error: Minimum");
            alert.setContentText("Min amount cannot be greater than the max allowed.");
            alert.showAndWait();
        }
        if (code == 6) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error: Inventory");
            alert.setContentText("Inventory must be in between min and max allowed.");
            alert.showAndWait();

        }



    }
}
