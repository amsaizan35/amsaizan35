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
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Integer.*;

/** This is the add part  class which adds part to the main screen . */
public class AddPartController implements Initializable {


    public TextField idText;
    public TextField nameText;
    public TextField invTextField ;
    public TextField priceTextField;
    public TextField maxText;
    public TextField minText;
    public HBox machineHBox;
    public TextField machineIDText;
    public Button saveBtn;
    public Button cancelBtn;
    public HBox compHBox;
    public TextField compNameTextField;
    public RadioButton inHouseRadio;
    public RadioButton outsourcedRadio;
    public ToggleGroup partType;


    //create part id number
    public static final AtomicInteger number = new AtomicInteger(0);

    /** Method to initialize the add part screen. Sets either InHouse or Outsourced parts. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Add Part Initialized");

        inHouseRadio.setSelected(true);
        machineHBox.setVisible(true);
        compHBox.setVisible(false);

    }


    /** Method to distinguish between inhouse or outsourced buttons. Toggle button to pick InHouse or Outsourced. */
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

    /** Method to cancel adding part and return to main screen. Returns to main screen when cacel button clicked. */
    public void cancelBtnAction(ActionEvent actionEvent) {
        AlertMessages.cancelWindow();
        toMain();
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();

    }
    /** Method to return to main screen. Returns to Main screen. */
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

    /** Method to save the new part information to list. Saves all parts info to list. */
    public void saveBtnAction(ActionEvent actionEvent) throws NumberFormatException{

        try {

                int id = number.incrementAndGet();
                idText.setText(String.valueOf(id));

                String name = nameText.getText();
                nameText.setText(name);

                double price = Double.parseDouble(priceTextField.getText());
                priceTextField.setText(String.valueOf(price));

                int inv = Integer.parseInt(invTextField.getText());
                invTextField.setText(String.valueOf(inv));

                int max = parseInt(maxText.getText());
                maxText.setText(String.valueOf(max));

                int min = parseInt(minText.getText());
                minText.setText(String.valueOf(min));


            if (min > max) {

             AlertMessages.errorWindow(5);

            } else if (inv < min || inv > max) {
                AlertMessages.errorWindow(6);

            } else {
                if (inHouseRadio.isSelected()) {
                    int machineID = parseInt(machineIDText.getText());
                    machineIDText.setText(String.valueOf(machineID));
                    InHouse newInHouse = new InHouse(id, name, price, inv, min, max, machineID);
                    Inventory.addPart(newInHouse);
                }
                if (outsourcedRadio.isSelected()) {

                        String compName = compNameTextField.getText();
                        compNameTextField.setText(compName);
                        Outsourced newOutsourced = new Outsourced(id, name, price, inv, min, max, compName);
                        Inventory.addPart(newOutsourced);

                }

                Stage stage = (Stage) saveBtn.getScene().getWindow();
                stage.close();
                    toMain();
            }




        } catch (NumberFormatException e) {

            System.out.println("Invalid Input.");
            System.out.println(e.getMessage());
            AlertMessages.errorWindow(4);

        }


    }

    //text fields to gather information for new part
    public void machineTextAction(ActionEvent actionEvent) {}

    public void minTextAction(ActionEvent actionEvent) { }


    public void idTextAction(ActionEvent actionEvent) { }

    public void nameTextAction(ActionEvent actionEvent) {}

    public void invTextAction(ActionEvent actionEvent)  { }


    public void priceTextAction (ActionEvent actionEvent){ }


    public void maxTextAction(ActionEvent actionEvent){}


    public void compNameTextAction(ActionEvent actionEvent) {}


}
