package controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

import static controller.MainController.prodSelected;
import static java.lang.Integer.parseInt;
import static javafx.fxml.FXMLLoader.load;
import static model.Inventory.*;


/** This is the add product controller class which adds new products to a list and the main screen . */
public class AddProductController implements Initializable {


        //fxml elements
    public TextField minText;
    public TextField maxText;
    public TextField priceText;
    public TextField invText;
    public TextField nameText;
    public TextField prodIdText;
    public Button cancelBut;
    public Button saveBut;
    public Button addAssociatedBut;
    public Button removeAssocBut;
    public TableColumn<Part, Double> assPriceCol;
    public TableColumn<Part, Integer> assInvCol;
    public TableColumn<Part, String> assPartNameCol;
    public TableColumn<Part, Integer> assPartIdCol;
    public TableView<Part> associatedTable;
    public TableColumn<Object, Object> partPriceCol;
    public TableColumn<Object, Object> partInvCol;
    public TableColumn<Object, Object> partNameCol;
    public TableColumn<Object, Object> partIdCol;
    public TextField partsSearchText;
    public TableView<Part> partsTable;

    public static List<Part> associated = new ArrayList<>();
    //create product id number
    public static final AtomicInteger num = new AtomicInteger(99) ;
    Product addedProduct = new Product();

    /** Method to initialize add product controller. Sets both partd and product tables. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Add Product Initialized");


        // Maps table columns and populates them for partsTable
        partIdCol.setCellValueFactory(new PropertyValueFactory<>("partID"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        partInvCol.setCellValueFactory(new PropertyValueFactory<>("InStock"));


        assPartIdCol.setCellValueFactory(new PropertyValueFactory<Part, Integer>("partID"));
        assPartNameCol.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        assPriceCol.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));
        assInvCol.setCellValueFactory(new PropertyValueFactory<Part, Integer>("inStock"));

        partsTable.setItems(getAllParts());


    }

    /** Method to cancel the addition of new product. Click cancel will send back to main screen. */
    public void cancelButAction(ActionEvent actionEvent) throws IOException {
        AlertMessages.cancelWindow();
        toMain();
        Stage stage = (Stage) cancelBut.getScene().getWindow();
        stage.close();
    }

    /** Method to save new product to list. Click save to add new info to list. */
    public void saveButAction(ActionEvent actionEvent) throws NumberFormatException, IOException {

        try {
            int id = num.incrementAndGet();
            prodIdText.setText(String.valueOf(id));
            String name = nameText.getText();
            nameText.setText(name);
            double price = Double.parseDouble(priceText.getText());
            priceText.setText(String.valueOf(price));
            int inve = parseInt(invText.getText());
            invText.setText(String.valueOf(inve));
            int max = parseInt(maxText.getText());
            maxText.setText(String.valueOf(max));
            int min = parseInt(minText.getText());
            minText.setText(String.valueOf(min));

            double totalPrice =0.0;

            for (int i = 0; i < associated.size(); i++) {
                if (associated.get(i) != null) {
                    totalPrice += associated.get(i).getPrice();

                    System.out.println(" total price of parts " + totalPrice);
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

                    addedProduct = new Product(id, name, price, inve, min, max);
                    for (int i = 0; i < associated.size(); i++) {
                        if(associated.get(i) !=null){
                           addedProduct.addAssociatedPart(associated.get(i));
                        }
                    }
                    addProduct(addedProduct);


                        Stage stage = (Stage) saveBut.getScene().getWindow();
                        stage.close();
                        toMain();


                    }

        }
            catch(NumberFormatException e){
            System.out.println("Invalid Input.");
            System.out.println(e.getMessage());
            AlertMessages.errorWindow(4);

            }


    }

    /** Method to return to main screen. Sends to main screen. */
   public void toMain() throws IOException {

            Parent root = load(MainController.class.getResource("/view/main.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Inventory Management System");
            stage.setScene(new Scene(root, 1000, 430));
            stage.show();

    }

    /** Method to remove associated part from table and list. Click to remove associated part.   */
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
                    associated.remove(selectedPart);
                    associatedTable.getItems().remove(selectedPart);

                }
            }else
                AlertMessages.errorWindow(2);
        }
    }

        /** Method to add associated part to table and list. Click to add associated part to list and table. */
    public void addAssociatedButAction(ActionEvent actionEvent) {

        Part selectedPart = partsTable.getSelectionModel().getSelectedItem();
        if (selectedPart == null) {
            AlertMessages.errorWindow(2);
        }
            associated.add(selectedPart);
         associatedTable.getItems().add(selectedPart);


    }



    /** Method to search part table. Search for part in table. */
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

    //text fields for gathering product information
    public void prodIdTextAction(ActionEvent actionEvent) {
    }

    public void nameTextAction(ActionEvent actionEvent) {
    }

    public void invTextAction(ActionEvent actionEvent) {
    }

    public void priceTextAction(ActionEvent actionEvent) {
    }

    public void maxTextAction(ActionEvent actionEvent) {
    }

    public void minTextAction(ActionEvent actionEvent) {
    }
}
