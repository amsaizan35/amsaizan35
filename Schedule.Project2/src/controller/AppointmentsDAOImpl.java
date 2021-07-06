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
import model.Contacts;
import model.Customers;
import model.Users;
import utilities.DBConnection;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Optional;
import java.util.ResourceBundle;


/** AppointmentsDAOImpl class. */
public class AppointmentsDAOImpl implements Initializable, AppointmentsDAO {

    public ComboBox<model.Customers> custCombo;
    public TableView<Appointments> appointmentTable;
    public TableColumn<Object, Object> apptIDCol;
    public TableColumn<Object, Object> titleCol;
    public TableColumn<Object, Object> descriptionCol;
    public TableColumn<Object, Object> locationCol;
    public TableColumn<Object, Object> typeCol;
    public TableColumn<Object, Object> startCol;
    public TableColumn<Object, Object> customerCol;
    public TableColumn<Object, Object> endCol;
    public TableColumn<Object, Object> userIdCol;
    public TableColumn<Object, Object> contactIDCol;
    public RadioButton weekRB;
    public ToggleGroup apptView;
    public RadioButton monthRB;
    public RadioButton allRB;
    public ComboBox<Contacts> contactCombo;
    public DatePicker startDatePicker;
    public DatePicker endDatePicker;
    public TextField apptIdText;
    public TextField titleText;
    public TextField descriptinText;
    public TextField typeText;
    public TextField locationText;
    public TextField userIdText;
    public Button contApptButton;
    public Label userLabel;
    public Button allApptButton;
    public Button addButton;
    public Button exitButton;
    public Button toCustButton;
    public Button updateButton;
    public Button submitButton;
    public ComboBox<LocalTime> endTimeComco;
    public ComboBox<LocalTime> startTimeCombo;
    public Button cancelButton;
    public Button deleteButton;
    public Label welcomeLabel;
    public Button userAppt;
    public Button reportBut;
    public Button custApptBut;

    Appointments toUpdate;
    int chosenContact;
    int chosenCustomer;
    int chosenUser;
    static boolean isAvailable = true;
    static boolean isDate = true;
    boolean isTime=true;
    boolean isSame=true;


    /**
     * Method to initialize scene. Starts appointments scene.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Appointments loading...");

        welcomeLabel.setText("User: " + LoginController.userSelected.getUsername());

        //set table columns
        apptIDCol.setCellValueFactory(new PropertyValueFactory<>("apptID"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        customerCol.setCellValueFactory(new PropertyValueFactory<>("custID"));
        userIdCol.setCellValueFactory(new PropertyValueFactory<>("userID"));
        contactIDCol.setCellValueFactory(new PropertyValueFactory<>("contactID"));

        custCombo.setItems(CustomersDAO.allCust);
        contactCombo.setItems(allContacts);

        allRB.setSelected(true);

        LocalTime start = LocalTime.of(8, 0);
        LocalTime end = LocalTime.of(21, 45);


        startTimeCombo.getSelectionModel().select(start);
        endTimeComco.setValue(start);

        while (start.isBefore(end.plusSeconds(1))) {
            start = start.plusMinutes(15);
            startTimeCombo.getItems().add(start);
            endTimeComco.getItems().add(start);
        }


    }


    /**
     * Method to check for overlapping appointments. Checks database for start times.
     *
     * @param start start time to be checked.
     * @param custid  customer id.
     * @param end end time to be checked.
     */
    public void checkOverlapTimes(Timestamp start, Timestamp end, int custid) throws SQLException {

        try {

            String sql = "select start, end from appointments where customer_id =? ;";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ps.setInt(1, custid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                Timestamp startTS = rs.getTimestamp("start");
                Timestamp endTS = rs.getTimestamp("end");

                if (start.equals(startTS)) {
                    isAvailable=false;
                }
                if (start.equals(startTS) && end.equals(endTS)) {
                    isAvailable=false;
                }

                if (start.after(startTS) && end.before(endTS)) {
                    isAvailable=false;
                }

                 if (start.before(startTS) && end.after(endTS)) {
                     isAvailable=false;
                }
                 if(start.equals(startTS) && end.after(endTS)){
                     isAvailable=false;
                 }
                 if(start.before(startTS) && end.equals(endTS)) {
                     isAvailable = false;
                 }
                 if(start.after(startTS) && end.equals(endTS)){
                     isAvailable=false;
                 }
                 if(start.after(startTS) && end.after(endTS)){
                     isAvailable=false;
                 }
                if(start.before(startTS) && end.before(endTS) ){
                    isAvailable=false;
                }


                if(start.before(startTS) && end.before(startTS)){
                    isAvailable=true;
                }
                if(start.after(endTS) && end.after(start)){
                    isAvailable=true;
                }


            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    /**
     * Method to check for overlapping appointments. Checks database for start times.
     *
     * @param start start time to be checked.
     * @param custid  customer id.
     * @param end end time to be checked.
     * @param apptid appointment id.
     */
    public void checkOverlapUpdate(Timestamp start, Timestamp end, int custid, int apptid) throws SQLException {
        
        try {

            String sql = "select start, end from appointments where customer_id =? and appointment_id <>?;";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ps.setInt(1, custid);
            ps.setInt(2, apptid);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                Timestamp startTS = rs.getTimestamp("start");
                Timestamp endTS = rs.getTimestamp("end");


                    if (start.equals(startTS)) {
                        isAvailable = false;
                    }
                    if (start.equals(startTS) && end.equals(endTS)) {
                        isAvailable = false;
                    }

                    if (start.after(startTS) && end.before(endTS)) {
                        isAvailable = false;
                    }

                    if (start.before(startTS) && end.after(endTS)) {
                        isAvailable = false;
                    }
                    if (start.equals(startTS) && end.after(endTS)) {
                        isAvailable = false;
                    }
                    if (start.before(startTS) && end.equals(endTS)) {
                        isAvailable = false;
                    }
                    if (start.after(startTS) && end.equals(endTS)) {
                        isAvailable = false;
                    }
                    if (start.after(startTS) && end.after(endTS)) {
                        isAvailable = false;
                    }
                    if (start.before(startTS) && end.before(endTS)) {
                        isAvailable = false;
                    }


                    if (start.before(startTS) && end.before(startTS)) {
                        isAvailable = true;
                    }
                    if (start.after(endTS) && end.after(start)) {
                        isAvailable = true;
                    }
                }


                } catch(SQLException e){
                    System.out.println(e.getMessage());
                }
            }


    /**
     * Method to get all appointments. Retrieves all appointments from database.
     *
     * @return All users.
     */
    public static ObservableList<Appointments> getAllAppts() throws SQLException {

        try {
            allAppts.clear();
            String sql = "Select * from userAppt ;";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int apptID = rs.getInt("appointment_id");
                String title = rs.getString("title");
                String description = rs.getString("description");
                String location = rs.getString("location");
                String type = rs.getString("type");
                Timestamp start = rs.getTimestamp("start");
                Timestamp end = rs.getTimestamp("end");
                int custID = rs.getInt("customer_id");
                int userID = rs.getInt("user_id");
                int contactId = rs.getInt("contact_id");
                String contactName = rs.getString("contact_name");
                String email = rs.getString("email");

                Appointments appt = new Appointments(apptID, title, description, location, type, start, end, custID, userID, contactId, contactName, email);
                allAppts.add(appt);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return allAppts;
    }

    /**
     * Method to get all contacts. Looks in database and returns all contacts.
     *
     * @return All contacts.
     */
    public static ObservableList<Contacts> getAllContacts() {

        try {
            allContacts.clear();
            String sql = "SELECT * FROM contacts";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery(sql);

            while (rs.next()) {
                int contactID = rs.getInt("contact_id");
                String name = rs.getString("contact_name");
                String email = rs.getString("email");

                Contacts contact = new Contacts(contactID, name, email);
                allContacts.add(contact);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return allContacts;
    }


    /**
     * Method to delete appointments. Deletes appointment from database.
     *
     * @param s appointment to delete.
     */
    public  void deleteAppt(Appointments s) throws SQLException {

        try {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle( "Delete Appointment");
                alert.setHeaderText( "Cancelling appointment" );
                alert.setContentText("Appointment ID:   " + s.getApptID() + " will be cancelled.");

                Optional<ButtonType> results = alert.showAndWait();
                if (results.isPresent() && results.get() == ButtonType.OK) {

                    AlertMessages.confirmDelete(s.getApptID(), s.getType());

                    if(LoginController.userSelected.getAllUserAppts(LoginController.userSelected.getUserID()) !=null) {


                            String sql = " DELETE from appointments where appointment_id = ?";
                            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

                            ps.setInt(1, s.getApptID());
                            ps.execute();

                        allAppts.remove(s);
                        LoginController.userSelected.deleteUserAppt(s);
                    }
                }

            appointmentTable.setItems(LoginController.userSelected.getAllUserAppts(LoginController.userSelected.getUserID()));

                if (results.isPresent() && results.get() == ButtonType.CANCEL) {
                  appointmentTable.getItems().setAll(LoginController.userSelected.getAllUserAppts(LoginController.userSelected.getUserID()));
                }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }


    /**
     * Method to get all users. Get all users form list.
     *
     * @return AllUsers.
     */
    public static ObservableList<Users> getAllUsers() {

        try {
            allUsers.clear();
            String sql = "SELECT user_id, user_name  FROM users ";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int userID = rs.getInt("user_id");
                String username = rs.getString("user_name");

                Users myUser = new Users(userID, username);
                allUsers.add(myUser);

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return allUsers;
    }


    /**
     * Method to auto populate appointment table. Fills in the info for selected appointment to update.
     */
    public void generateUpdatedFields(Appointments toUpdate) {

        chosenContact = appointmentTable.getSelectionModel().getSelectedItem().getContactID();
        chosenCustomer = appointmentTable.getSelectionModel().getSelectedItem().getCustID();

        for (Contacts a : allContacts) {
            if (a.getContactID() == chosenContact) {
                contactCombo.setValue(a);
            }
        }
        for (Customers s : CustomersDAO.allCust) {
            if (s.getCustID() == chosenCustomer) {
                custCombo.setValue(s);
            }
        }
        try {
            apptIdText.setText(String.valueOf(toUpdate.getApptID()));
            titleText.setText(toUpdate.getTitle());
            descriptinText.setText(toUpdate.getDescription());
            locationText.setText(toUpdate.getLocation());
            typeText.setText(toUpdate.getType());
            startDatePicker.setValue(toUpdate.getStart().toLocalDateTime().toLocalDate());
            startTimeCombo.setValue(toUpdate.getStart().toLocalDateTime().toLocalTime());
            endDatePicker.setValue(toUpdate.getEnd().toLocalDateTime().toLocalDate());
            endTimeComco.setValue(toUpdate.getEnd().toLocalDateTime().toLocalTime());
            userIdText.setText(String.valueOf(toUpdate.getUserID()));

        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
    }


    /**
     * Method to get contact name. Pick contact form list.
     */
    public void onActionContactCombo(ActionEvent actionEvent) {

        contactCombo.getSelectionModel().getSelectedItem();
    }

    /**
     * Method to make appointment. Users can make appointments.
     */
    public void onActionAddButton(ActionEvent actionEvent) throws NullPointerException {
         Stage stage = (Stage) addButton.getScene().getWindow();
        try {
            String title = titleText.getText();
            String desc = descriptinText.getText();
            String loca = locationText.getText();
            String type = typeText.getText();
            LocalDate sDate = startDatePicker.getValue();
            LocalTime sTime = startTimeCombo.getSelectionModel().getSelectedItem();
            LocalDateTime start = LocalDateTime.of(sDate, sTime);
            Timestamp tsstart = Timestamp.valueOf(start);
            LocalDate eDate = endDatePicker.getValue();
            LocalTime eTime = endTimeComco.getSelectionModel().getSelectedItem();
            LocalDateTime end = LocalDateTime.of(eDate, eTime);
            Timestamp tsend = Timestamp.valueOf(end);

            LocalDateTime create = LocalDateTime.from(ZonedDateTime.now());
            Timestamp tsCreate = Timestamp.valueOf(create);
            String createdBy = LoginController.userSelected.getUsername();
            Timestamp lastUpdate = Timestamp.valueOf(LocalDateTime.now());
            String lastUpdatedBy = LoginController.userSelected.getUsername();

            int custid = custCombo.getSelectionModel().getSelectedItem().getCustID();
            int userid = Integer.parseInt(userIdText.getText());
            int contid = contactCombo.getSelectionModel().getSelectedItem().getContactID();

           checkDates(sDate, eDate);
            if(isDate) {
                startDatePicker.setValue(sDate);
                endDatePicker.setValue(eDate);

                checkTimes(sTime, eTime);
                if(isTime) {
                    startTimeCombo.setValue(sTime);
                    endTimeComco.setValue(eTime);

                    checkOverlapTimes(tsstart, tsend, custid);
                    if (!isAvailable ) {
                        AlertMessages.errorWindow(5);
                    }
                    if (isAvailable) {
                        String sql = "INSERT into appointments (title, description, location, type, start, end, create_date, created_by, last_update," +
                                "last_updated_by, customer_id, user_id, contact_id ) Values( ?,?,?,?,?,?,?,?,?,?,?,?,?); ";
                        PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                        ps.setString(1, title);
                        ps.setString(2, desc);
                        ps.setString(3, loca);
                        ps.setString(4, type);
                        ps.setTimestamp(5, tsstart);
                        ps.setTimestamp(6, tsend);
                        ps.setTimestamp(7, tsCreate);
                        ps.setString(8, createdBy);
                        ps.setTimestamp(9, lastUpdate);
                        ps.setString(10, lastUpdatedBy);
                        ps.setInt(11, custid);
                        ps.setInt(12, userid);
                        ps.setInt(13, contid);

                        ps.execute();
                        ResultSet rs = ps.getGeneratedKeys();
                        while (rs.next()) {
                            int apptID = rs.getInt(1);

                            Appointments a = new Appointments(apptID, title, desc, loca, type, tsstart, tsend, custid, userid, contid);
                            allAppts.add(a);
                            LoginController.userSelected.addUserAppt(a);
                            Customers.addCustAppts(a);

                            stage.close();
                            toAppts();
                        }

                    }
                }

            }
            

        } catch (SQLException | RuntimeException | IOException se) {

            System.out.println(se.getMessage());
        }

    }

    /**
     * Method to delete appointment. Users can delete appointments.
     */
    public void onActionDeleteButton(ActionEvent actionEvent) throws SQLException {
        toUpdate = appointmentTable.getSelectionModel().getSelectedItem();
        try {
            if (toUpdate == null) {
                AlertMessages.errorWindow(2);
            }
            deleteAppt(toUpdate);

        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }


    /**
     * Method to update appointment. Users can update appointments.
     */
    public void onActionUpdateButtton(ActionEvent actionEvent) throws SQLException {
        toUpdate = appointmentTable.getSelectionModel().getSelectedItem();
        try {
            if (toUpdate == null) {
                AlertMessages.errorWindow(2);
            }

            generateUpdatedFields(toUpdate);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    /** Method to cancel. Cancels the update, add or delete options. */
    public void onActionCancelButton(ActionEvent actionEvent) {

        apptIdText.clear();
        titleText.clear();
        descriptinText.clear();
        locationText.clear();
        typeText.clear();
        custCombo.setItems(CustomersDAOImpl.getAllCustomers());
        userIdText.clear();
        contactCombo.setItems(getAllContacts());
        startDatePicker.setValue(LocalDate.now());
        endDatePicker.setValue(LocalDate.now());
        startTimeCombo.getSelectionModel().clearSelection();
        endTimeComco.getSelectionModel().clearSelection();

    }


    /**
     * Method to toggle radio button. Choose from all, monthly or weekly appointments.
     */
    public void onActionRadioToggle(ActionEvent actionEvent) throws SQLException {

        if (allRB.isSelected()) {
            appointmentTable.setVisible(true);
            weekRB.setVisible(false);
            monthRB.setVisible(false);
        }
        if (weekRB.isSelected()) {
            monthRB.setVisible(false);
            allRB.setVisible(false);
            weekAppts.clear();
            String sql = "SELECT * FROM userAppt WHERE YEARWEEK(start) = YEARWEEK(NOW());";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet set = ps.executeQuery();
            while (set.next()) {

                int apptID = set.getInt("appointment_id");
                String title = set.getString("title");
                String location = set.getString("location");
                String description = set.getString("description");
                int customerId = set.getInt("customer_id");
                String type = set.getString("type");
                Timestamp start = set.getTimestamp("start");
                Timestamp end = set.getTimestamp("end");
                String contactName = set.getString("contact_name");
                int userID = set.getInt("user_id");
                int contactID = set.getInt("contact_id");
                String email = set.getString("email");

                Appointments weeks = new Appointments(apptID, title, description, location, type, start, end, customerId, userID, contactID, contactName, email);
                weekAppts.add(weeks);
                appointmentTable.getItems().setAll(weekAppts);
            }
        }
        if (monthRB.isSelected()) {
            weekRB.setVisible(false);
            allRB.setVisible(false);
            monthAppts.clear();
            String sql = "SELECT * from userAppt WHERE MONTH(start) = MONTH(CURRENT_DATE()) " +
                    "AND YEAR(start) = YEAR(CURRENT_DATE()) ;";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet set = ps.executeQuery();

            while (set.next()) {
                int apptID = set.getInt("appointment_id");
                String title = set.getString("title");
                String location = set.getString("location");
                String description = set.getString("description");
                int customerId = set.getInt("customer_id");
                String type = set.getString("type");
                Timestamp start = set.getTimestamp("start");
                Timestamp end = set.getTimestamp("end");
                int userID = set.getInt("user_id");
                int contactID = set.getInt("contact_id");
                String contactName = set.getString("contact_name");
                String email = set.getString("email");

                Appointments months = new Appointments(apptID, title, description, location, type, start, end, customerId, userID, contactID, contactName, email);
                monthAppts.add(months);
                appointmentTable.getItems().setAll(monthAppts);
            }
        }

    }

    /**
     * Method to pick date. Pick date for appointment to start.
     */
    public void onActionStartDatePicker(ActionEvent actionEvent) {

        LocalDate start = startDatePicker.getValue();
        startDatePicker.setValue(start);
    }

    /**
     * Method to end appointment. Sets date to end appointment.
     */
    public void onActionEndPicker(ActionEvent actionEvent) {

            LocalDate end = endDatePicker.getValue();
            endDatePicker.setValue(end);
    }


    /**
     * Method to start time for appointment. Choose time to start appointment.
     */
    public void onActionStartTimeCombo(ActionEvent actionEvent) {
       LocalTime start =  startTimeCombo.getSelectionModel().getSelectedItem();
       startTimeCombo.setValue(start);
    }


    /**
     * Method to end time. Sets time to end appointment.
     */
    public void onActionEndTimeComco(ActionEvent actionEvent) {

            LocalTime end = endTimeComco.getSelectionModel().getSelectedItem();
            endTimeComco.setValue(end);

    }


    /**
     * Method to submit updates. Submits appointments.
     */
    public void onActionSubmit(ActionEvent actionEvent) throws SQLException, IOException {

        Stage stage = (Stage) submitButton.getScene().getWindow();
        try {

            int id = Integer.parseInt(apptIdText.getText());
            String title = titleText.getText();
            String description = descriptinText.getText();
            String location = locationText.getText();
            String type = typeText.getText();

            LocalDate sDate = startDatePicker.getValue();
            LocalTime sTime = startTimeCombo.getSelectionModel().getSelectedItem();
            LocalDateTime start = LocalDateTime.of(sDate, sTime);
            Timestamp tsstart = Timestamp.valueOf(start);
            LocalDate eDate = endDatePicker.getValue();
            LocalTime eTime = endTimeComco.getSelectionModel().getSelectedItem();
            LocalDateTime end = LocalDateTime.of(eDate, eTime);
            Timestamp tsend = Timestamp.valueOf(end);
            LocalDateTime create = LocalDateTime.from(ZonedDateTime.now());
            Timestamp tsCreate = Timestamp.valueOf(create);
            String lastUpdatedBy = LoginController.userSelected.getUsername();
            int custid = custCombo.getSelectionModel().getSelectedItem().getCustID();
            int userid = Integer.parseInt(userIdText.getText());
            int contid = contactCombo.getSelectionModel().getSelectedItem().getContactID();

            checkDates(sDate, eDate);
              if(isDate) {
                  startDatePicker.setValue(sDate);
                  endDatePicker.setValue(eDate);

                  checkTimes(sTime, eTime);
                  if (isTime) {
                      startTimeCombo.setValue(sTime);
                      endTimeComco.setValue(eTime);


                      checkOverlapUpdate(tsstart, tsend, custid, id);

                          if (!isAvailable) {
                              AlertMessages.errorWindow(5);
                          }
                          if (isAvailable) {
                              String sql = " UPDATE appointments set title=? , description=?, location=?, type=?, start=?, end=?, last_update=?, last_updated_by=?," +
                                      " customer_id=?, user_id=?, contact_id=? where appointment_id = ?";
                              PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

                              ps.setInt(12, id);
                              ps.setString(1, title);
                              ps.setString(2, description);
                              ps.setString(3, location);
                              ps.setString(4, type);
                              ps.setTimestamp(5, tsstart);
                              ps.setTimestamp(6, tsend);
                              ps.setTimestamp(7, tsCreate);
                              ps.setString(8, lastUpdatedBy);
                              ps.setInt(9, custid);
                              ps.setInt(10, userid);
                              ps.setInt(11, contid);

                              ps.execute();
                              Appointments a = new Appointments(id, title, description, location, type, tsstart, tsend, custid, userid, contid);
                              allAppts.add(a);
                              LoginController.userSelected.addUserAppt(a);

                              chosenUser = LoginController.userSelected.getUserID();
                              for (Users user : getAllUsers()) {

                                  if (user.getUserID() == chosenUser) {
                                      appointmentTable.setItems(user.getAllUserAppts(user.getUserID()));
                                      userLabel.setText("Appointments for [" + user.getUserID() + "]" + " " + user.getUsername());
                                  }
                              }

                              stage.close();
                              toAppts();
                          }
                      }
                  }

        } catch (SQLException | NullPointerException | IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /** Method to check dates. Check start is before end dates.
     @param end  check end date
     @param  start check start date. */
    public void checkDates(LocalDate start, LocalDate end){

        ArrayList<LocalDate> dates = new ArrayList<>();
        try {
             if (end.isBefore(start)) {
                 dates.add(start);
            }


             if(!dates.isEmpty()){
                 isDate=false;
                 AlertMessages.errorWindow(4);
             }
             if(dates.isEmpty()) {
                 isDate = true;
             }

        }catch(NullPointerException e){
            System.out.println(e.getMessage());
        }

    }

    /** Method to check times. Check start is before end times
     * @param end  check end time.
     * @param start check start time.
     * */
    public void checkTimes(LocalTime start, LocalTime end) {

        ArrayList<LocalTime> invalid = new ArrayList<>();
        try{
            if(end.isBefore(start)){
                invalid.add(start);
            }


            if(!invalid.isEmpty()){
                isTime=false;
                AlertMessages.errorWindow(8);
            }
            if(invalid.isEmpty()){
               isTime=true;
            }

        }catch(NullPointerException e){
            System.out.println(e.getMessage());
        }

    }



    /**
     * Method to return to user appointments. Displays users appointments.
     */
    public void toAppts() throws IOException, SQLException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/appointments.fxml"));
        Parent root = loader.load();

        AppointmentsDAOImpl dao = loader.getController();
        dao.appointmentTable.setItems(LoginController.userSelected.getAllUserAppts(LoginController.userSelected.getUserID()));
        dao.userLabel.setText("Appointments for [" + LoginController.userSelected.getUserID() + "]" + " " + LoginController.userSelected.getUsername());

        Stage stage = new Stage();
        stage.setScene(new Scene(root, 1215, 750));
        stage.setTitle("Schedule");
        stage.setResizable(false);
        stage.show();

    }


    /**
     * Method to change to customers. Change to customer window.
     */
    public void ActionToCustButton(ActionEvent actionEvent) throws IOException {

        Stage stage = (Stage) toCustButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/customers.fxml"));
        Parent root = loader.load();

        stage.setScene(new Scene(root, 1100, 400));
        stage.setTitle("Customers");
        stage.setResizable(false);
        stage.show();
    }


    /**
     * Method to exit program.
     */
    public void ActionExit(ActionEvent actionEvent) {
        Platform.exit();
    }


    /**
     * Method to get appointment id. Gets id from database.
     */
    public void onActionApptID(ActionEvent actionEvent) {

    }


    /**
     * Method to get title. Fills in title.
     */
    public void onActionTitleText(ActionEvent actionEvent) {
        try {
            String title = titleText.getText();
            titleText.setText(title);
        } catch (NumberFormatException e) {
            AlertMessages.errorWindow(3);
            System.out.println(e.getMessage());
        }
    }


    /**
     * Method to get description. Fills in description.
     */
    public void onActionDescriptionText(ActionEvent actionEvent) {
        try {
            String description = descriptinText.getText();
            descriptinText.setText(description);
        } catch (NumberFormatException e) {
            AlertMessages.errorWindow(3);
            System.out.println(e.getMessage());
        }
    }


    /**
     * Method to get type. Fills in type field.
     */
    public void onActionTypeText(ActionEvent actionEvent) {
        try {
            String type = typeText.getText();
            typeText.setText(type);
        } catch (NumberFormatException e) {
            AlertMessages.errorWindow(3);
            System.out.println(e.getMessage());
        }
    }


    /**
     * Method to get location. Fills in location field.
     */
    public void onActionLocationText(ActionEvent actionEvent) {
        try {
            String location = locationText.getText();
            locationText.setText(location);
        } catch (NumberFormatException e) {
            AlertMessages.errorWindow(3);
            System.out.println(e.getMessage());
        }
    }


    /**
     * Method to get customer.  Displays Customers.
     */
    public void ActionCustCombo(ActionEvent actionEvent) {
        custCombo.getSelectionModel().getSelectedItem();
    }


    /**
     * Method to get user id. Fills in user id field.
     */
    public void onActionUserIDText(ActionEvent actionEvent) {
        try {
            int userID = Integer.parseInt(userIdText.getText());
            userIdText.setText(String.valueOf(userID));
        } catch (NumberFormatException e) {
            AlertMessages.errorWindow(3);
            System.out.println(e.getMessage());
        }
    }


    /**
     * Method to change scenes. Changes to appointment scene and displays all appointments.
     */
    public void ActionApptButton(ActionEvent actionEvent) throws SQLException, IOException {
        appointmentTable.getItems().setAll(getAllAppts());
        userLabel.setText("All Appointments");
        allRB.setVisible(true);
        weekRB.setVisible(true);
        monthRB.setVisible(true);
    }


    /**
     * Method to get appointments for contacts. Gets contact appointments from database.
     */
    public void actionContApptBut(ActionEvent actionEvent) throws IOException, RuntimeException {

        toUpdate = appointmentTable.getSelectionModel().getSelectedItem();

        if (toUpdate == null) {
            AlertMessages.errorWindow(2);
        }
        try {
            chosenContact = appointmentTable.getSelectionModel().getSelectedItem().getContactID();
            for (Contacts contact : getAllContacts()) {
                if (contact.getContactID() == chosenContact) {


                    appointmentTable.setItems(contact.getContAppt(contact.getContactID()));
                    userLabel.setText("Appointments for [" + contact.getContactID() + "]" + " " + contact.getContactName());
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    /** method to send to to get report on users appointments. */
    public void handlerUserAppt(ActionEvent actionEvent) {

        toUpdate = appointmentTable.getSelectionModel().getSelectedItem();

        if (toUpdate == null) {
            AlertMessages.errorWindow(2);
        }
        try {
            chosenUser = appointmentTable.getSelectionModel().getSelectedItem().getUserID();
            for (Users user : getAllUsers()) {
                if (user.getUserID() == chosenUser) {

                    appointmentTable.setItems(user.getAllUserAppts(user.getUserID()));
                    userLabel.setText("Appointments for [" + user.getUserID() + "]" + " " + user.getUsername());
                }
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }

    }

    /** Method to send to report scene. */
    public void hadlerReports(ActionEvent actionEvent) {
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

    /** method to get customer appointments. */
    public void handlerCustAppt(ActionEvent actionEvent) throws IOException {

        toUpdate = appointmentTable.getSelectionModel().getSelectedItem();
        if (toUpdate == null) {
            AlertMessages.errorWindow(2);
        }
        try {
            chosenCustomer = appointmentTable.getSelectionModel().getSelectedItem().getCustID();
            for (Customers a : CustomersDAOImpl.getAllCustomers()) {
                if (a.getCustID() == chosenCustomer) {
                    appointmentTable.getItems().setAll(a.getCustAppts(a.getCustID()));
                    userLabel.setText("Appointments for " + "[" + a.getCustID() + "] " + a.getCustName());
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());

        }
    }


}




