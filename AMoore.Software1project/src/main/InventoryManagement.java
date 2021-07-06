package main;

import controller.AddPartController;
import controller.AddProductController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.*;

/** This is the Inventory Management class which starts the main controller.
 FUTURE ENHANCEMENTS: Have option to add part while while adding or updating the product.
 */
public class InventoryManagement extends Application {


    /** A method to initialize the application. Sends to the Main Controller. */
    @Override
    public void start(Stage primaryStage) {
        Inventory allInv = new Inventory();
        addSomeData(allInv);


        try {

            Parent root = FXMLLoader.load(getClass().getResource("/view/main.fxml"));
            primaryStage.setTitle("Inventory Management System");
            primaryStage.setScene(new Scene(root, 1000, 430));
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Method to add data to inventory.
     It will display  in tables.
     @param allInv  Parts and products.
     */
    void addSomeData(Inventory allInv){

        Part chain = new InHouse(AddPartController.number.incrementAndGet(), "chain", 7.99, 56, 1, 100, 50);
        Part tire = new InHouse(AddPartController.number.incrementAndGet(), "tire", 12.99, 25, 1, 100, 51);
        Part seat = new InHouse(AddPartController.number.incrementAndGet(), "seat", 6.99, 50, 1, 100, 52);
        Part brakes = new Outsourced(AddPartController.number.incrementAndGet(), "brakes", 9.59, 34, 1, 100, "MNO");
        Part motor = new Outsourced(AddPartController.number.incrementAndGet(), "motor", 39.99, 3, 1, 10, "ABC");
        Part light = new Outsourced(AddPartController.number.incrementAndGet(), "light", 3.99, 30, 1, 50, "XYZ");

        allInv.addPart(chain);
        allInv.addPart(tire);
        allInv.addPart(seat);
        allInv.addPart(brakes);
        allInv.addPart(motor);
        allInv.addPart(light);


        Product electric = new Product(AddProductController.num.incrementAndGet(), "electric bike", 150.00, 10, 5, 20);
        Product bicycle = new Product(AddProductController.num.incrementAndGet(), "bicycle", 60.00, 32, 10, 50);

        allInv.addProduct(electric);
        allInv.addProduct(bicycle);

        electric.addAssociatedPart(motor);
        electric.addAssociatedPart(brakes);
        electric.addAssociatedPart(seat);
        bicycle.addAssociatedPart(chain);
        bicycle.addAssociatedPart(tire);



    }

    /** C:\Users\amoor\OneDrive\Documents\Software1.InventorySystem - javadoc location */
    /** Method to start the Inventory Management app. This app will display parts and products in the inventory. */

    public static void main(String[] args) {

        launch(args);
    }
}
