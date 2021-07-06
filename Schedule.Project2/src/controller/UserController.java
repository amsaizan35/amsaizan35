package controller;


import DAO.AppointmentsDAO;
import DAO.CustomersDAO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
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
import model.Appointments;
import utilities.DBConnection;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.ResourceBundle;


/** UserController Class. Has Lambda expressions in this class.  */
public class UserController implements Initializable {
    public Button allCustomersButton;
    public Label userlabel;
    public TableView<model.Appointments> apptTable;
    public TableColumn<Object, Object> apptIDCol;
    public TableColumn<Object, Object> titleCol;
    public TableColumn<Object, Object> descCol;
    public TableColumn<Object, Object> locatCol;
    public TableColumn<Object, Object> typeCol;
    public TableColumn<Object, Object> startCol;
    public TableColumn<Object, Object> endCol;
    public TableColumn<Object, Object> custidcol;
    public TableColumn<Object, Object> contidcol;
    public TableView<model.Customers> custTable;
    public TableColumn<Object, Object> custIDCol;
    public TableColumn<Object, Object> custNameCol;
    public Button addApptBut;
    public Button updateApptBut;
    public Button deleteApptButt;
    public TableView<model.Contacts> contTable;
    public TableColumn<Object, Object> contIDCol;
    public TableColumn<Object, Object> contNameCol;
    public TableColumn<Object, Object> emailCol;
    public Button exitButton;
    public Button apptButt;
    public Label nameLabel;
    public Button reportBut;

    public static Appointments update;
    public  boolean isAppt=false;
    public static int chosenContact;



    /** METHOD TO INITIALIZE THE SCENE. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Users logging in...");

         custTable.setItems(CustomersDAOImpl.getAllCustomers());
         contTable.setItems(AppointmentsDAOImpl.getAllContacts());

        //set tables
        apptIDCol.setCellValueFactory(new PropertyValueFactory<>("apptID"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        locatCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        custidcol.setCellValueFactory(new PropertyValueFactory<>("custID"));
        contidcol.setCellValueFactory(new PropertyValueFactory<>("contactID"));

        //set table
        custIDCol.setCellValueFactory(new PropertyValueFactory<>("custID"));
        custNameCol.setCellValueFactory(new PropertyValueFactory<>("custName"));

        //set table
        contIDCol.setCellValueFactory(new PropertyValueFactory<>("contactID"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        contNameCol.setCellValueFactory(new PropertyValueFactory<>("contactName"));

    }



    /** Method to get start time. Gets start time to send reminder. */
    public  void apptReminder ( int id) throws SQLException {

        ObservableList<Appointments> noappts= FXCollections.observableArrayList();
        ObservableList<Integer> appts= FXCollections.observableArrayList();
        try {
            String sql = " Select start, appointment_id from appointments where user_id =?;";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                isAppt = true;
                Timestamp tsstart = rs.getTimestamp("start");
                int apptid = rs.getInt("appointment_id");

                LocalDateTime start = tsstart.toLocalDateTime();
                LocalDateTime ldtnow = LocalDateTime.now();
                long between = ChronoUnit.MINUTES.between(ldtnow, start);

                Appointments a = new Appointments(apptid);

                if (between >= 1 && between <= 15) {
                    appts.add(apptid);
                    isAppt = true;
                }else {
                    noappts.add(a);
                }

                if (appts.contains(apptid)) {
                    AlertMessages.apptReminder(apptid, tsstart, between);

                }
            }
            if(appts.isEmpty()) {
                AlertMessages.errorWindow(1);
            }
        }catch (SQLException  e){
            System.out.println(e.getMessage());
        }
    }



    /** Method/Action event to go to customers screen. */
    public void ActionAllCustomersButton(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) allCustomersButton.getScene().getWindow();
        stage.close();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/customers.fxml"));
            Parent root = loader.load();

            CustomersDAOImpl dao = loader.getController();
            CustomersDAO.allCust.clear();
            dao.customerTable.getItems().setAll(CustomersDAOImpl.getAllCustomers());
            stage.setScene(new Scene(root, 1100, 400));
            stage.setTitle("Customers");
            stage.setResizable(false);
            stage.show();

        } catch (IOException | RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }


    /** Method to add appointment. Sends to appointment screen. */
    public void actionAddApptBut(ActionEvent actionEvent) {

        Stage stage = (Stage) addApptBut.getScene().getWindow();
        stage.close();

        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/appointments.fxml"));
            Parent root = loader.load();

            AppointmentsDAOImpl dao = loader.getController();
            LoginController.userSelected.getAllUserAppts(LoginController.userSelected.getUserID()).clear();
            dao.appointmentTable.getItems().setAll(LoginController.userSelected.getAllUserAppts(LoginController.userSelected.getUserID()));
            dao.userLabel.setText("Appointments for [" + LoginController.userSelected.getUserID()+ "] " + LoginController.userSelected.getUsername());
            stage.setScene(new Scene(root, 1215, 750));
            stage.setTitle("Schedule");
            stage.setResizable(false);
            stage.show();

        } catch (IOException | RuntimeException  e) {
            System.out.println(e.getMessage());
        }

    }


    /** LAMBDA Expressions(two). Method to update appointments. Sends info to appointment screen.
     *  Lambda used on both for each for customer and contacts to
     * set the value of the combo box for all customers and combo box for all contacts.  */
    public void actionUpdateButt(ActionEvent actionEvent) throws IOException {

        Stage stage = (Stage) updateApptBut.getScene().getWindow();
        update = apptTable.getSelectionModel().getSelectedItem();

        if (update == null) {
            AlertMessages.errorWindow(2);
        } else {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/appointments.fxml"));
                Parent root = loader.load();

                AppointmentsDAOImpl dao = loader.getController();

                dao.chosenContact = apptTable.getSelectionModel().getSelectedItem().getContactID();
                dao.chosenCustomer = apptTable.getSelectionModel().getSelectedItem().getCustID();
                dao.appointmentTable.getItems().setAll(LoginController.userSelected.getAllUserAppts(LoginController.userSelected.getUserID()));

                dao.allContacts.forEach(a -> {
                    dao.contactCombo.setValue(a);
                });

                CustomersDAO.allCust.forEach(s ->{
                    if (s.getCustID() == dao.chosenCustomer) {
                        dao.custCombo.setValue(s);
                    }
                });



                dao.apptIdText.setText(String.valueOf(update.getApptID()));
                dao.titleText.setText(update.getTitle());
                dao.descriptinText.setText(update.getDescription());
                dao.locationText.setText(update.getLocation());
                dao.typeText.setText(update.getType());
                dao.startDatePicker.setValue(update.getStart().toLocalDateTime().toLocalDate());
                dao.startTimeCombo.setValue(update.getStart().toLocalDateTime().toLocalTime());
                dao.endDatePicker.setValue(update.getEnd().toLocalDateTime().toLocalDate());
                dao.endTimeComco.setValue(update.getEnd().toLocalDateTime().toLocalTime());
                dao.userIdText.setText(String.valueOf(update.getUserID()));
                dao.appointmentTable.setItems(LoginController.userSelected.getAllUserAppts(LoginController.userSelected.getUserID()));
                dao.userLabel.setText("Appointments for "  + " [" + LoginController.userSelected.getUserID() +"] " + LoginController.userSelected.getUsername());

                stage.setScene(new Scene(root, 1215, 750));
                stage.setTitle("Schedule");
                stage.setResizable(false);
                stage.show();

            } catch (RuntimeException  e) {
                System.out.println(e.getMessage());
            }
        }
    }



    /** Method to delete appt. Sends to appointment screen. */
    public void actionDeleteApptBut(ActionEvent actionEvent) throws IOException, SQLException {

        update = apptTable.getSelectionModel().getSelectedItem();
        if (update == null) {
            AlertMessages.errorWindow(2);
        }
        try {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle( "Delete Appointment");
            alert.setHeaderText( "Cancelling appointment" );
            alert.setContentText("Appointment ID:   " + update.getApptID() + " will be cancelled.");

            Optional<ButtonType> results = alert.showAndWait();
            if (results.isPresent() && results.get() == ButtonType.OK) {

                if(LoginController.userSelected.getAllUserAppts(LoginController.userSelected.getUserID()) !=null) {

                    AlertMessages.confirmDelete(update.getApptID(), update.getType());
                    
                    String sql = " DELETE from appointments where appointment_id = ?";
                    PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

                    ps.setInt(1, update.getApptID());
                    ps.execute();

                    LoginController.userSelected.deleteUserAppt(update);
                    AppointmentsDAO.allAppts.remove(update);

                }

            }

            apptTable.setItems(LoginController.userSelected.getAllUserAppts(LoginController.userSelected.getUserID()));

            if (results.isPresent() && results.get() == ButtonType.CANCEL) {
                apptTable.getItems().setAll(LoginController.userSelected.getAllUserAppts(LoginController.userSelected.getUserID()));
            }


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
}


    /** Method to exit the program. */
    public void handlerExit(ActionEvent actionEvent) {
        Platform.exit();
    }

    /** Method to change scenes. Send appointment scene. */
    public void handlerApptButt(ActionEvent actionEvent) {

        Stage stage = (Stage) apptButt.getScene().getWindow();
        stage.close();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/appointments.fxml"));
            Parent root = loader.load();

            AppointmentsDAOImpl dao = loader.getController();
            dao.appointmentTable.setItems(AppointmentsDAOImpl.getAllAppts());
            dao.userLabel.setText("All Appointments");

            stage.setScene(new Scene(root, 1215, 750));
            stage.setTitle("Schedule");
            stage.setResizable(false);
            stage.show();
        } catch (IOException | RuntimeException | SQLException e) {
            System.out.println(e.getMessage());

        }
    }

    /** Method to go to report scene. */
    public void handlerReport(ActionEvent actionEvent) {
        Stage stage = (Stage) reportBut.getScene().getWindow();
        stage.close();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/reports.fxml"));
            Parent root = loader.load();

            stage.setScene(new Scene(root, 560, 400));
            stage.setTitle("Reports");
            stage.setResizable(false);
            stage.show();
        } catch (IOException | RuntimeException e) {
            System.out.println(e.getMessage());

        }
    }


    }

