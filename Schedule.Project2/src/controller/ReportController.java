package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Contacts;
import utilities.Reports;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/** Report Controller Class. */
public class ReportController implements Initializable {

    public TableView<Reports> monthlyTable;
    public TableView<Reports> divTable;
    public Button monthlyButton;
    public Button firstButton;
    public TableColumn<Object, Object> amountColMon;
    public TableColumn<Object, Object> typeColMon;
    public TableColumn<Object, Object> monthColMon;
    public TableColumn<Object, Object> amtColFirst;
    public TableColumn<Object, Object> firstColFirst;
    public TableColumn<Object, Object> countryColMon;
    public Button apptBut;
    public Button exitBut;
    public Button contactApptButton;

    Contacts selected;
    int chosenCont;

    /**
     * Method to initialize the report scene.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        System.out.println("Reports loading ...");

        //set table
        amountColMon.setCellValueFactory(new PropertyValueFactory<>("appts"));
        monthColMon.setCellValueFactory(new PropertyValueFactory<>("month"));
        typeColMon.setCellValueFactory(new PropertyValueFactory<>("type"));


        //set table
        firstColFirst.setCellValueFactory(new PropertyValueFactory<>("fl"));
        countryColMon.setCellValueFactory(new PropertyValueFactory<>("country"));
        amtColFirst.setCellValueFactory(new PropertyValueFactory<>("amount"));

    }


    /**
     * Method to get a report for appointments per month. Gets total monthly appointments.
     */
    public void handlerMonthly(ActionEvent actionEvent) {
        monthlyTable.getItems().clear();
        monthlyTable.getItems().setAll(Reports.getMonthlyReport());
    }

    /**
     * Method to get a report showing first level division and countries per customer.
     * Displays amount of customers with address in first level divisions.
     */
    public void handlerFirst(ActionEvent actionEvent) {
        divTable.getItems().clear();
        divTable.getItems().setAll(Reports.getFlReport());

    }

    /**
     * Method to go to appointments scene. Displays the user appointments.
     */
    public void handlerAppts(ActionEvent actionEvent) {
        try {
            Stage stage = (Stage) apptBut.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/appointments.fxml"));
            Parent root = loader.load();

            AppointmentsDAOImpl cont = loader.getController();
            cont.appointmentTable.getItems().setAll(cont.getAllAppts());
            cont.userLabel.setText(" All Users Appointments ");

            stage.setScene(new Scene(root, 1215, 750));
            stage.setTitle("Appointments");
            stage.setResizable(false);
            stage.show();
        } catch (IOException | NumberFormatException | SQLException e) {
            System.out.println(e.getMessage());

        }
    }

    /**
     * Method to close application. Exists application.
     */
    public void handlerExit(ActionEvent actionEvent) {
        Platform.exit();
    }

}
