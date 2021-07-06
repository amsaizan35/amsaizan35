package controller;

import javafx.application.Platform;
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
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static model.Inventory.*;

/** This is the main controller  class which displays the inventory on the main screen. RUNTIME ERROR:  java.lang.NullPointerException
 when clicking the modify button on main screen you get a null pointer exception because there was
 nothing selected to modify, if you  add an if statement that product selected to  modify is null then
 error  message runs correctly. */
public class MainController implements Initializable {


    //fxml elements
    public TableView<Part> partsTable = new TableView<>();
    public TableView<Product> productsTable;
    public TableColumn<Part, Integer> partIdCol;
    public TableColumn<Part, String> partNameCol;
    public TableColumn<Part, Integer> partInvCol;
    public TableColumn<Part, Integer> partPriceCol;
    public TextField searchPartText;
    public Button addPartBut;
    public Button deletePartBut;
    public Button modifyPartBut;
    public TableColumn<Product, Integer> prodIdCol;
    public TableColumn<Product, String> prodNameCol;
    public TableColumn<Product, Integer> prodInvCol;
    public TableColumn<Product, Integer> prodPriceCol;
    public TextField searchProdText;
    public Button addProdBut;
    public Button deleteProdBut;
    public Button modifyProdBut;
    public Button exitBut;

    //declare objects
    public static Part partSelected;
    public static Product prodSelected;
    public static Part deletedPart;

    /**
     * Method to initialize main controller. Loads tables.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Inventory System  Initialized");

        partsTable.setItems(getAllParts());
        productsTable.setItems(getAllProducts());


        partIdCol.setCellValueFactory(new PropertyValueFactory<>("partID"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        partInvCol.setCellValueFactory(new PropertyValueFactory<>("inStock"));


        prodIdCol.setCellValueFactory(new PropertyValueFactory<>("prodId"));
        prodNameCol.setCellValueFactory(new PropertyValueFactory<>("prodName"));
        prodPriceCol.setCellValueFactory(new PropertyValueFactory<>("prodPrice"));
        prodInvCol.setCellValueFactory(new PropertyValueFactory<>("ProdStock"));

    }

    /**
     * Method to go to add part screen. Click to take to add part screen.
     */
    public void AddPartButAction(ActionEvent actionEvent) {

        try {

            Parent root = FXMLLoader.load(getClass().getResource("/view/addPart.fxml"));
            Stage stage;
            stage = (Stage) addPartBut.getScene().getWindow();
            stage.setTitle("Add Part");
            stage.setScene(new Scene(root, 500, 400));
            stage.show();

        } catch (Exception e) {

            System.out.print(e.toString());
        }
    }

    /**
     * Method to go to delete part from table and list. Click to remove part.
     */
    public void deletePartButAction(ActionEvent actionEvent) {

        partSelected = partsTable.getSelectionModel().getSelectedItem();

        if (partSelected == null) {
            AlertMessages.errorWindow(2);
        }

        else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Confirmation");
            alert.setHeaderText("Are you sure you want to delete " + partSelected.getName());
            alert.setContentText(" Click OK to confirm ");

            Optional<ButtonType> results = alert.showAndWait();
            if(results.isPresent() && results.get() == ButtonType.OK){
                deletePart(partSelected);
            }
        }
        partsTable.refresh();
    }

    /**
     * Method to go to update part screen. Click to take to update part screen.
     */
    public void modifyPartButAction(ActionEvent actionEvent) {


        partSelected = partsTable.getSelectionModel().getSelectedItem();
        if (partSelected == null) {
            AlertMessages.errorWindow(2);
        } else
            try {
                //initialize new controller
                Parent root = FXMLLoader.load(getClass().getResource("/view/updatePart.fxml"));
                Stage stage;
                stage = (Stage) modifyPartBut.getScene().getWindow();
                stage.setTitle("Update Part");
                stage.setScene(new Scene(root, 500, 400));
                stage.show();

            } catch (Exception e) {

                System.out.print(e.toString());
            }

    }


    /**
     * Method to go to add product screen. Click to take to add product screen.
     */
    public void addProdButAction(ActionEvent actionEvent) {
        partSelected = partsTable.getSelectionModel().getSelectedItem();

        try {

            Parent root = FXMLLoader.load(getClass().getResource("/view/addProduct.fxml"));
            Stage stage;
            stage = (Stage) addProdBut.getScene().getWindow();
            stage.setTitle("Add Part");
            stage.setScene(new Scene(root, 1000, 600));
            stage.show();

        } catch (Exception e) {

            System.out.print(e.toString());

        }
    }

    /**
     * Method to go to delete product. Click to delete product.
     */
    public void deleteProdButAction(ActionEvent actionEvent) {
        prodSelected = productsTable.getSelectionModel().getSelectedItem();

        if (prodSelected == null) {
            AlertMessages.errorWindow(2);
        } else if (!prodSelected.getAllAssociatedParts().isEmpty()) {
            AlertMessages.confirmDelete(prodSelected.getProdName());

        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Confirmation");
            alert.setHeaderText("Are you sure you want to delete " + prodSelected.getProdName());
            alert.setContentText(" Click OK to confirm ");

            Optional<ButtonType> results = alert.showAndWait();
            if(results.isPresent() && results.get() == ButtonType.OK){
                deleteProduct(prodSelected);
            }

        }

        productsTable.setItems(getAllProducts());
        partsTable.refresh();


    }

    /**
     * Method to update product. Click to goto update product screen.
     */
    //method to go to update product screen
    public void modifyProdButAction(ActionEvent actionEvent) {

        prodSelected = productsTable.getSelectionModel().getSelectedItem();

        if (prodSelected == null) {
            AlertMessages.errorWindow(2);
        } else
            try {
                //initialize new controller
                Parent root = FXMLLoader.load(getClass().getResource("/view/updateProduct.fxml"));
                Stage stage;
                stage = (Stage) modifyPartBut.getScene().getWindow();
                stage.setTitle("Update Product");
                stage.setScene(new Scene(root, 900, 600));
                stage.show();
            } catch (Exception e) {

                System.out.print(e.toString());

            }
    }


    /**
     * Method to go to search for product. Click to go to search product in inventory.
     */
    public void searchProdTextAction(ActionEvent actionEvent) {
        String sa = searchProdText.getText();
        ObservableList<Product> products = searchProduct(sa);

        if (products.size() == 0) {
            try {
                int product = Integer.parseInt(sa);
                Product pr = searchProduct(product);
                if (pr != null)
                    products.add(pr);
            } catch (NumberFormatException e) {
                AlertMessages.errorWindow(3);
            }

        }
            productsTable.setItems(products);
            searchProdText.setText("");

        }


    /**
     * Method to search part table. Click to go to search part method in inventory.
     */
    public void searchPartTextAction(ActionEvent actionEvent) {
        String se = searchPartText.getText();
        ObservableList<Part> parts = searchPart(se);

        if (parts.size() == 0) {

            try {
                int partID = Integer.parseInt(se);
                Part p = searchPart(partID);
                if (p != null) {
                    parts.add(p);
                }

            } catch (NumberFormatException e) {
                AlertMessages.errorWindow(3);

            }

        }
         partsTable.setItems(parts);
        searchPartText.setText("");
        }


        /** Method to exit the app. Click to exit the application. */
        public void exitButAction (ActionEvent actionEvent){

            System.out.println("Closed Inventory System");
            Platform.exit();

        }



}
