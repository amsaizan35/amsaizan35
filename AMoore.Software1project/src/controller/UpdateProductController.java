package controller;

import com.sun.tools.javac.Main;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import main.AlertMessages;
import model.*;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import static java.lang.Integer.parseInt;
import static model.Inventory.*;


/** This is the update product controller class which updates the product in the list and sends back to the main screen.
 LOGICAL ERROR:  with the placement of the save button onAction: if the save button action is placed within the wrong brackets either
 the button does nothing when pushed, it will close the window but not save ,  or you get an alert message stating invalid value. Finally
 adjusting the code and moving the brackets  to see where exactly each one started and ended I finally placed the save button in the
 correct place and the list was  updated with the correct information. */
public class UpdateProductController implements Initializable {

    //fxml elements
    public TextField partsSearchText;
    public TableView<Part> partsTable;
    public TableColumn<Part, Integer> partIdCol;
    public TableColumn<Part, String> partNameCol;
    public TableColumn<Part, Integer> partInvCol;
    public TableColumn<Part, Double> partPriceCol;
    public TableView<Part> associatedTable;
    public TableColumn<Part, Integer> assPartIdCol;
    public TableColumn<Part, String> assPartNameCol;
    public TableColumn<Part, Integer> assInvCol;
    public TableColumn<Part, Double> assPriceCol;
    public Button addAssociatedBut;
    public Button removeAssocBut;
    public Button saveBut;
    public Button cancelBut;
    public TextField prodIdText;
    public TextField nameText;
    public TextField invText;
    public TextField priceText;
    public TextField maxText;
    public TextField minText;



     /** The initialize method loads the tables.  All parts and products are loaded. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Update Product Initialized");

        // get info for product to be updated
        nameText.setText(MainController.prodSelected.getProdName());
        priceText.setText(String.valueOf(MainController.prodSelected.getProdPrice()));
        invText.setText(String.valueOf(MainController.prodSelected.getProdStock()));
        maxText.setText(String.valueOf(MainController.prodSelected.getProdMax()));
        minText.setText(String.valueOf(MainController.prodSelected.getProdMin()));


        // partsTable columns
        partIdCol.setCellValueFactory(new PropertyValueFactory<Part, Integer>("partID"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));
        partInvCol.setCellValueFactory(new PropertyValueFactory<Part, Integer>("InStock"));

        partsTable.setItems(getAllParts());

        //associated tables columns
        assPartIdCol.setCellValueFactory(new PropertyValueFactory<Part, Integer>("partID"));
        assPartNameCol.setCellValueFactory(new PropertyValueFactory<Part, String>("Name"));
        assPriceCol.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));
        assInvCol.setCellValueFactory(new PropertyValueFactory<Part, Integer>("inStock"));


        associatedTable.setItems(MainController.prodSelected.getAllAssociatedParts());

    }

    /** The parts search method will display a specific part.
     A specific part can be searched then displayed in the parts table.
     */
    public void partsSearchTextAction(ActionEvent actionEvent) {
         String se = partsSearchText.getText();
        ObservableList<Part> parts = searchPart(se);

        if (parts.size() == 0) {

            try {
                int partID = Integer.parseInt(se);
                Part p = searchPart(partID);
                if (p != null) {
                    parts.add(p);
                }

            } catch (NumberFormatException e) {
                AlertMessages.errorWindow(1);

            }

        }
        partsTable.setItems(parts);
        partsSearchText.setText("");
    }



        /** Method to to return to main screen. Click cancel button to go back to main screen. */
    public void cancelButAction(ActionEvent actionEvent) {
        AlertMessages.cancelWindow();
        toMain();
        Stage stage = (Stage) cancelBut.getScene().getWindow();
        stage.close();
    }

    /** Method to save updated info. Click save button to save updated information. **/

    public void saveButAction(ActionEvent actionEvent) {


        try {
            int id = MainController.prodSelected.getProdId();
            prodIdText.setText(String.valueOf(id));
            String name = nameText.getText();
            nameText.setText(name);
            double price = Double.parseDouble(priceText.getText());
            priceText.setText(String.valueOf(price));
            int inve = parseInt(invText.getText());
            invText.setText(String.valueOf(inve));
            int min = parseInt(minText.getText());
            minText.setText(String.valueOf(min));
            int max = parseInt(maxText.getText());
            maxText.setText(String.valueOf(max));

            double totalPrice = 0.0;

                for (int i = 0; i < MainController.prodSelected.getAllAssociatedParts().size(); i++) {
                    if (MainController.prodSelected.getAllAssociatedParts().get(i) != null) {
                        totalPrice += MainController.prodSelected.getAllAssociatedParts().get(i).getPrice();
                        System.out.println(MainController.prodSelected.getAllAssociatedParts().get(i).getPrice() + " price");
                        System.out.println(" total price" + totalPrice);
                    }
                }
            if (price < totalPrice) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error: Price");
                alert.setContentText("The sum of all associated parts cannot exceed that of the product itself.");
                alert.showAndWait();
                System.out.println("alert");
            }


             else if (min > max) {
                AlertMessages.errorWindow(5);

            } else if (inve < min || inve > max) {
                AlertMessages.errorWindow(6);

            } else {


                Product newProd = new Product(id, name, price, inve, min, max);

                for (int i = 0; i < MainController.prodSelected.getAllAssociatedParts().size() ; i++) {
                   if(MainController.prodSelected.getAllAssociatedParts().get(i) != null) {
                       newProd.addAssociatedPart(MainController.prodSelected.getAllAssociatedParts().get(i));
                   }

                }
               updateProduct( newProd, getAllProducts().indexOf(MainController.prodSelected));

                 Stage stage = (Stage) saveBut.getScene().getWindow();
                 stage.close();
                 toMain();

                }


        } catch (NumberFormatException e) {
            System.out.println("Invalid Input.");
            System.out.println(e.getMessage());
            AlertMessages.errorWindow(4);
        }


    }


    /** Method to return to main screen. Return to main screen. */
    public void toMain() {

        try {

            Parent root = FXMLLoader.load(getClass().getResource("/view/main.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Inventory Management System");
            stage.setScene(new Scene(root, 1000, 430));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.getStackTrace();
        }

    }

        /** Method to remove parts associated from a product and  from table. Click to remove associated part. */
    public void removeAssocButAction(ActionEvent actionEvent) {


        if (associatedTable != null) {
            Part selectedPart = associatedTable.getSelectionModel().getSelectedItem();
            if(selectedPart != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete Confirmation");
                alert.setHeaderText("Are you sure you want to delete " + selectedPart.getName());
                alert.setContentText(" Click OK to confirm ");

                Optional<ButtonType> results = alert.showAndWait();
                if(results.isPresent() && results.get() == ButtonType.OK){
                        MainController.prodSelected.deleteAssociatedPart(selectedPart);

                }

            }else
                AlertMessages.errorWindow(2);

        }

    }

    /** Method to get a  part from parts table and add to associated table. Add associated part to associated table. */
    public void addAssociatedButAction(ActionEvent actionEvent) {
        if (partsTable.getSelectionModel().getSelectedItem() != null) {
            Part selectedPart = partsTable.getSelectionModel().getSelectedItem();
            MainController.prodSelected.addAssociatedPart(selectedPart);


        }
    }




    // text fields for the basic information on product
    public void minTextAction(ActionEvent actionEvent) {

    }

    public void maxTextAction(ActionEvent actionEvent) {

    }

    public void priceTextAction(ActionEvent actionEvent) {

    }

    public void invTextAction(ActionEvent actionEvent) {

    }

    public void nameTextAction(ActionEvent actionEvent) {

    }

    public void prodIdTextAction(ActionEvent actionEvent) {

    }
}
