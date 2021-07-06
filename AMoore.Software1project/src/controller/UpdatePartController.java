package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import main.AlertMessages;
import model.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import static java.lang.Integer.parseInt;
import static model.Inventory.*;

/** This is the update part  class which updates the part in the list and the sends back to the main screen. */

public class UpdatePartController implements Initializable {

        //fxml elements
    public RadioButton inHouseRadio;
    public ToggleGroup partType;
    public RadioButton outsourcedRadio;
    public TextField idText;
    public TextField nameText;
    public TextField invTextField;
    public TextField priceTextField;
    public TextField maxTextField;
    public TextField minTextField;
    public Button saveBtn;
    public Button cancelBtn;
    public HBox compHBox;
    public TextField compNameTextField;
    public HBox machineHBox;
    public TextField machineIDTextField;

    //declare Inventory object
    Inventory allInv;

        /** Method to initialize the update part controller. Loads info of selected part. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Update Part Initialized");
        generateTextFields();
    }

    /** Method to show if part is inhouse or outsourced. Display inhouse or oursourced. */
    public void partRadioToggle(ActionEvent actionEvent) {

        if(inHouseRadio.isSelected()){
            machineHBox.setVisible(true);
            compHBox.setVisible(false);
        }
        if(outsourcedRadio.isSelected()){
            compHBox.setVisible(true);
            machineHBox.setVisible(false);
        }
    }

    /** Method to cancel updating and return to main screen. Click to return to main screen. */
    public void cancelBtnAction(ActionEvent actionEvent) {
        AlertMessages.cancelWindow();
        toMain();
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();

    }
        /** Method to save updated information on selected part. Click to save updated info to list. */
    public void saveBtnAction(ActionEvent actionEvent) {

        try {
            int id = MainController.partSelected.getPartID();
            idText.setText(String.valueOf(id));
            String name = nameText.getText();
            nameText.setText(name);
            double price = Double.parseDouble(priceTextField.getText());
            priceTextField.setText(String.valueOf(price));
            int inve = parseInt(invTextField.getText());
            invTextField.setText(String.valueOf(inve));
            int min = parseInt(minTextField.getText());
            minTextField.setText(String.valueOf(min));
            int max = parseInt(maxTextField.getText());
            maxTextField.setText(String.valueOf(max));



            if (min > max) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error: Minimum");
                alert.setContentText("Min amount cannot be greater than the max allowed.");
                alert.showAndWait();

                } else if (inve < min || inve > max) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error: Inventory");
                alert.setContentText("Inventory must be in between min and max allowed.");
                alert.showAndWait();

            } else {
                if (inHouseRadio.isSelected()) {
                    int machineID = parseInt(maxTextField.getText());
                    maxTextField.setText(String.valueOf(machineID));
                    InHouse newInHousePart = new InHouse(id, name, price, inve, min, max, machineID);

                    updatePart(getAllParts().indexOf(MainController.partSelected), newInHousePart);


                }
                if (outsourcedRadio.isSelected()) {
                    String compName = compNameTextField.getText();
                    compNameTextField.setText(compName);

                    Outsourced newOutsourcedPart = new Outsourced(id, name, price, inve, min, max, compName);
                    updatePart(getAllParts().indexOf(MainController.partSelected), newOutsourcedPart);


                    Stage stage = (Stage) saveBtn.getScene().getWindow();
                    stage.close();
                    toMain();
                }


            }




        } catch (NumberFormatException e) {
            System.out.println("Invalid Input.");
            System.out.println(e.getMessage());
            AlertMessages.errorWindow(4);
        }

    }
    /** Method to return to main screen. Returns to main screen. */
    public  void toMain() {


        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/main.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Inventory Management System");
            stage.setScene(new Scene(root, 1000, 430));
            stage.setResizable(false);
            stage.show();
        }catch (IOException e){
            e.getStackTrace();
        }

    }


        /** Method to distinguish between part types.  Get information for the selected part to be updated. */
    private void generateTextFields() {

        // Displays TextFields of Part that was selected to be updated
        if (MainController.partSelected instanceof InHouse){
            InHouse inPart = (InHouse) MainController.partSelected;
            nameText.setText(inPart.getName());
            priceTextField.setText(String.valueOf(inPart.getPrice()));
            invTextField.setText(String.valueOf(inPart.getInStock()));
            minTextField.setText(String.valueOf(inPart.getMin()));
            maxTextField.setText(String.valueOf(inPart.getMax()));
            machineIDTextField.setText(String.valueOf(InHouse.getMachineID())); // InHouse Parts have a machineID

            // Only shows elements related to InHouse
            inHouseRadio.setSelected(true);
            machineHBox.setVisible(true);
            compHBox.setVisible(false);

            updatePart(getAllParts().indexOf(inPart), inPart);
        }

        if (MainController.partSelected instanceof Outsourced) {
            Outsourced outPart = (Outsourced) MainController.partSelected;

            nameText.setText(outPart.getName());
            priceTextField.setText(String.valueOf(outPart.getPrice()));
            invTextField.setText(String.valueOf(outPart.getInStock()));
            minTextField.setText(String.valueOf(outPart.getMin()));
            maxTextField.setText(String.valueOf(outPart.getMax()));
            compNameTextField.setText(outPart.getCompanyName()); // Outsourced Parts have companyName

            // Only shows elements related to Outsourced
            outsourcedRadio.setSelected(true);
            compHBox.setVisible(true);
            machineHBox.setVisible(false);


           updatePart(getAllParts().indexOf(outPart), outPart);

        }

    }

        //text fields to gather updated information for selected part
    public void idText(ActionEvent actionEvent)  {}

    public void nameTextAction(ActionEvent actionEvent)  { }

    public void invTextAction(ActionEvent actionEvent)  {}

    public void priceTextAction(ActionEvent actionEvent)  {}

    public void maxTextAction(ActionEvent actionEvent) {}

    public void minTextAction(ActionEvent actionEvent)  {}

    public void machineTextAction(ActionEvent actionEvent)  {}
}
