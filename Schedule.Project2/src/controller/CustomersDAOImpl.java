package controller;

import DAO.AppointmentsDAO;
import DAO.CustomersDAO;
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
import utilities.DBConnection;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;


/** CustomersDAOImpl class. */
public class CustomersDAOImpl implements Initializable, CustomersDAO {
    public TableView<Customers> customerTable;
    public TableColumn<Object, Object> custIDCol;
    public TableColumn<Object, Object> nameCol;
    public TableColumn<Object, Object> addressCol;
    public TableColumn<Object, Object> firstLevelCol;
    public TableColumn<Object, Object> countryCol;
    public TableColumn<Object, Object> phoneCol;
    public TableColumn<Object, Object> postalCol;
    public TextField custIDText;
    public TextField nameText;
    public TextField addressText;
    public ComboBox<Countries> countryCombo;
    public ComboBox<FirstLevelDivisions> firstLevelCombo;
    public TextField postalText;
    public TextField phoneText;
    public Button addButton;
    public Button updateButton;
    public Button cancelButton;
    public Button custApptButton;
    public Button submitButton;
    public Button deleteButton;
    public Button exitButton;
    public Button allCustButton;
    public Button allApptBut;

    public static Customers selectedCust;
    public static Countries selectedCountry;

    ResourceBundle rb = ResourceBundle.getBundle("main/lang", Locale.getDefault());



    /** Method to initialize scene. Starts Customers scene. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Customers view loading.....");

        customerTable.getItems().setAll(getAllCustomers());

        //set table
        custIDCol.setCellValueFactory(new PropertyValueFactory<>("custID"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("custName"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        firstLevelCol.setCellValueFactory(new PropertyValueFactory<>("firstLevel"));
        countryCol.setCellValueFactory(new PropertyValueFactory<>("country"));
        postalCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));

        countryCombo.setItems(getAllCountries());
        selectedCountry = countryCombo.getSelectionModel().getSelectedItem();
        firstLevelCombo.setOnAction(this::ActionFirstLevelCombo);
    }


    /**
     * Method to get all countries. Search database for countries.
     *
     * @return Countries.
     */
    public ObservableList<Countries> getAllCountries() {

        try {
            allCountries.clear();
            String sql = "SELECT country_id, country FROM countries ";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int countryId = rs.getInt("country_id");
                String country = rs.getString("country");
                Countries newCountry = new Countries(countryId, country);
                allCountries.add(newCountry);
            }

        } catch (SQLException t) {
            System.out.println(t.getMessage());
        }
        return allCountries;
    }



    /**
     * Method to get all states in US. Lists all states.
     *
     * @return US states.
     */
    public ObservableList<FirstLevelDivisions> getStates() {

        try {
            states.clear();
            String sql = "SELECT division, division_id FROM first_level_divisions where country_id = 1";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                String division = rs.getString("division");
                int divID = rs.getInt("division_id");

                FirstLevelDivisions state = new FirstLevelDivisions(divID, division);
                states.add(state);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return states;
    }


    /**
     * Method to get all divisions in UK. Lists all divisions.
     *
     * @return UK divisions.
     */
    public ObservableList<FirstLevelDivisions> getUK() {

        try {
            ukDiv.clear();
            String sql = "SELECT division, division_id FROM first_level_divisions where country_id = 2";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                String division = rs.getString("division");
                int divID = rs.getInt("division_id");

                FirstLevelDivisions uk = new FirstLevelDivisions(divID, division);
                ukDiv.add(uk);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return ukDiv;
    }


    /**
     * Method to get all division inCanada. Lists all divisions.
     *
     * @return Canada divisions.
     */
    public ObservableList<FirstLevelDivisions> getCanada() {

        try {
            canadaDiv.clear();
            String sql = "SELECT division, division_id FROM first_level_divisions where country_id = 3";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();


            while (rs.next()) {

                String division = rs.getString("division");
                int divID = rs.getInt("division_id");

                FirstLevelDivisions canada = new FirstLevelDivisions(divID, division);
                canadaDiv.add(canada);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return canadaDiv;
    }

    /**
     * Method to get all division . Lists all divisions.
     *
     * @return divisions.
     */
    public ObservableList<FirstLevelDivisions> getDivisions() {

        try {

            allDivisions.clear();
            String sql = "SELECT division, division_id FROM first_level_divisions;";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                String division = rs.getString("division");
                int divID = rs.getInt("division_id");

                FirstLevelDivisions div = new FirstLevelDivisions(divID, division);
                allDivisions.add(div);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return allDivisions;
    }


    /**
     * Method to get all customers. Get customers from database.
     *
     * @return Customers.
     */
    public static ObservableList<Customers> getAllCustomers() {

        try {

            allCust.clear();
            String sql = "select * from customerAddress;";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int custID = rs.getInt("customer_id");
                String name = rs.getString("customer_name");
                String address = rs.getString("address");
                String city = rs.getString("division");
                String country = rs.getString("country");
                String postal = rs.getString("postal_code");
                String phone = rs.getString("phone");
                int divID = rs.getInt("division_id");
                int countryId = rs.getInt("country_id");


                Customers c = new Customers(custID, name, address, city, country, postal, phone, divID, countryId);
                allCust.add(c);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return allCust;
    }

    /**
     * Method to add new customer. Adds customer info to database.
     */
    public void addCustomers() throws SQLException {

        try {

            String custName = nameText.getText();
            String address = addressText.getText();
            int divID = firstLevelCombo.getSelectionModel().getSelectedItem().getDivID();
            String postalCode = postalText.getText();
            String phone = phoneText.getText();
            String lastUpdatedBy = LoginController.userSelected.getUsername();
            String createdBy = LoginController.userSelected.getUsername();
            String firstLevel = String.valueOf(firstLevelCombo.getSelectionModel().getSelectedItem());
            String country = String.valueOf(countryCombo.getSelectionModel().getSelectedItem());

            Timestamp create = Timestamp.valueOf(LocalDateTime.now());
            Timestamp lastUpdate = Timestamp.valueOf(LocalDateTime.now());

            String sql = " insert into customers values(NULL,?,?,?,?,?,?,?,?,?) ";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, custName);
            ps.setString(2, address);
            ps.setString(3, postalCode);
            ps.setString(4, phone);
            ps.setTimestamp(5, create);
            ps.setString(6, createdBy);
            ps.setTimestamp(7, lastUpdate);
            ps.setString(8, lastUpdatedBy);
            ps.setInt(9, divID);
            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            int custID = rs.getInt(1);

            Customers a = new Customers(custID, custName, address, firstLevel, country, postalCode, phone, divID);
            allCust.add(a);

            customerTable.setItems(getAllCustomers());

            Stage stage = (Stage) addButton.getScene().getWindow();
            stage.close();
            toCustomers();

        } catch (SQLException | NullPointerException | IOException e) {
            System.out.println(e.getMessage());
        }
    }



    /**
     * Method to delete customer. Deletes customer from database.
     */
    public void deleteCustomers(Customers selectedCust) throws SQLException {

        try{

            String sql = " delete from customers where customer_id=?; ";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ps.setInt(1, selectedCust.getCustID());
            ps.execute();

            allCust.remove(selectedCust);

            customerTable.getItems().setAll(getAllCustomers());

        } catch (SQLException | NullPointerException e) {
            System.out.println(e.getMessage());
        }
    }


    /**
     * Method to add new customer. Adds customers to database.
     */
    public void onActionAddButton(ActionEvent actionEvent) throws SQLException {
        addCustomers();
    }


    /**
     * Method to update customer. Updates customer in database.
     */

    public void onActionUpdateButton(ActionEvent actionEvent)  {
        selectedCust = customerTable.getSelectionModel().getSelectedItem();

        try {
            if (selectedCust == null) {
                AlertMessages.errorWindow(2);
            }
        }catch(RuntimeException e){
            System.out.println(e.getMessage());
        }
        generateTextFields(selectedCust);

    }



    /**
     * Method to delete customer. Deletes customer from database.
     */
    public void onActionDeleteButton(ActionEvent actionEvent) throws SQLException {
        selectedCust = customerTable.getSelectionModel().getSelectedItem();
        if (selectedCust == null) {
            AlertMessages.errorWindow(2);
        }
        try {
            if (selectedCust.getCustAppts(selectedCust.getCustID()) != null) {
                AlertMessages.errorWindow(7);

                for (Appointments a : AppointmentsDAOImpl.getAllAppts()) {
                    if (a.getCustID() == selectedCust.getCustID()) {
                        if (a.getCustID() == selectedCust.getCustID()) {
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle(rb.getString("Confirmation"));
                            alert.setHeaderText(rb.getString("Customer" )+ " " + selectedCust.getCustName());
                            alert.setContentText(rb.getString("sure" )+  " " + a.getApptID());
                            alert.setContentText(rb.getString("OK"));

                            Optional<ButtonType> results = alert.showAndWait();
                           if (results.isPresent() && results.get() == ButtonType.OK) {
                               if(selectedCust.getCustAppts(selectedCust.getCustID()) !=null) {
                                   deleteAppt(a);
                                   selectedCust.deleteCustAppts(a);
                               }
                                deleteCustomers(selectedCust);

                                AlertMessages.confirmDelete(a.getApptID(), a.getType());
                                AlertMessages.confirmDeleteCust(selectedCust.getCustID(), selectedCust.getCustName());
                            }
                            if (results.isPresent() && results.get() == ButtonType.CANCEL) {
                                customerTable.getItems().setAll(getAllCustomers());
                            }
                        }
                    }

                    }

            }


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    /** Method to delete appointments. Deletes appointment from database. */
    public  void deleteAppt(Appointments s) throws SQLException {

        try {

            String sql = " DELETE from appointments where appointment_id = ?";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            assert s != null;
            ps.setInt(1, s.getApptID());
            ps.execute();

            AppointmentsDAO.allAppts.remove(s);
            LoginController.userSelected.deleteUserAppt(s);


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }



    /**
     * Method to cancel. Cancels adding info.
     */
    public void onActionCancelButton(ActionEvent actionEvent) {

        custIDText.clear();
        nameText.clear();
        addressText.clear();
        postalText.clear();
        firstLevelCombo.getSelectionModel().clearSelection();
        countryCombo.getSelectionModel().clearSelection();
        phoneText.clear();

    }


    /**
     * Method to get country to add to customer. Adds country to address for customer in database.
     */
    public void ActionCountryCombo(ActionEvent actionEvent) {

        selectedCountry = countryCombo.getSelectionModel().getSelectedItem();

        try {
            if (selectedCountry.getCountryID() == 1) {
                firstLevelCombo.getItems().setAll(getStates());
            }
            if (selectedCountry.getCountryID() == 2) {
                firstLevelCombo.getItems().setAll(getUK());
            }
            if (selectedCountry.getCountryID() == 3) {
                firstLevelCombo.getItems().setAll(getCanada());
            }

        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
        countryCombo.getSelectionModel().getSelectedItem();
    }


    /**
     * Method to get division. Gets division to add to address.
     */
    public void ActionFirstLevelCombo(ActionEvent actionEvent) {

        firstLevelCombo.getSelectionModel().getSelectedItem();

    }


    /**
     * Method to get user input. Gets input from user for postal code.
     */
    public void ActionPostalText(ActionEvent actionEvent) {
    }


    /**
     * Method to get user input. Gets input from user for phone.
     */
    public void ActionPhoneText(ActionEvent actionEvent) {
    }


    /**
     * Method  to get user input. Gets customer id that was generated.
     */
    public void ActionCustIDText(ActionEvent actionEvent) {

    }


    /**
     * method to get user input. Gets input from user for name.
     */
    public void ActionNameText(ActionEvent actionEvent) {
    }


    /**
     * method to get user input. Gets input from user for address.
     */
    public void ActionAddressText(ActionEvent actionEvent) {
    }


    /**
     * method to submit new and updated customers. Sends to database.
     */
    public void ActionSubmitButton(ActionEvent actionEvent) throws SQLException, IOException {

        try {
            int custID = Integer.parseInt(custIDText.getText());
            String custName = nameText.getText();
            String address = addressText.getText();
            int divID = firstLevelCombo.getSelectionModel().getSelectedItem().getDivID();
            int countryId= countryCombo.getSelectionModel().getSelectedItem().getCountryID();
            String postalCode = postalText.getText();
            String phone = phoneText.getText();
            String lastUpdatedBy = LoginController.userSelected.getUsername();

            String firstLevel = String.valueOf(firstLevelCombo.getSelectionModel().getSelectedItem());
            String country = String.valueOf(countryCombo.getSelectionModel().getSelectedItem());

            String sql = " update customers set customer_name=?, address=?, postal_code=?, phone=?, division_id=?, " +
                    " last_update=?, last_updated_by=? where customer_id = ?";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ps.setInt(8, custID);

            ps.setString(1, custName);
            ps.setString(2, address);
            ps.setString(3, postalCode);
            ps.setString(4, phone);
            ps.setInt(5, divID);
            ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(7, lastUpdatedBy);

            ps.execute();

            Customers m = new Customers(custID, custName, address, firstLevel, country, postalCode, phone, divID, countryId);
            allCust.add(m);

            Stage stage = (Stage) submitButton.getScene().getWindow();
            stage.close();
            toCustomers();
        } catch (SQLException  e) {
            System.out.println(e.getMessage());
        }

    }


    /** Method to return to user appointments. Displays users appointments. */
    public  void toCustomers() throws IOException, SQLException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/customers.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root, 1100, 375));
        stage.setTitle("Customers");
        stage.setResizable(false);
        stage.show();

    }


    /**
     * Method to generate fields to update customer. Auto fills in fields on update.
     */
    public void generateTextFields(Customers selectedCust) {

        for (Countries a : allCountries) {
            if (selectedCust.getCountryID() == a.getCountryID()){
                countryCombo.setValue(a);
            }
        }
        for(FirstLevelDivisions d: getDivisions()) {
            if (selectedCust.getDivID() == d.getDivID()) {
                firstLevelCombo.setValue(d);
            }
        }

        try {

            custIDText.setText(String.valueOf(selectedCust.getCustID()));
            nameText.setText(selectedCust.getCustName());
            addressText.setText(selectedCust.getAddress());
            postalText.setText(selectedCust.getPostalCode());
            phoneText.setText(selectedCust.getPhone());

        } catch (NullPointerException  e) {
            System.out.println(e.getMessage());
        }
    }


    /**
     * Method to exit program.
     */
    public void actionExit(ActionEvent actionEvent) {
        Platform.exit();
    }


    /**
     * Method to get customers appointments. Sends to appointments scene.
     */
    public void ActioncustApptButton(ActionEvent actionEvent) throws IOException {
        selectedCust = customerTable.getSelectionModel().getSelectedItem();

        try {
            Stage stage = (Stage) custApptButton.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/appointments.fxml"));
            Parent root = loader.load();

            AppointmentsDAOImpl  cont= loader.getController();

            cont.appointmentTable.getItems().setAll(selectedCust.getCustAppts(selectedCust.getCustID()));
            cont.userLabel.setText("Appointments for [" + selectedCust.getCustID()+  "]"  + selectedCust.getCustName() );

            stage.setScene(new Scene(root, 1215, 750));
            stage.setTitle("Customer Appointments");
            stage.setResizable(false);
            stage.show();
        } catch (IOException | NumberFormatException e) {
            System.out.println(e.getMessage());

        }
    }


    /** Method to change scenes to customers. Displays Customers info. */
    public void actionAllCustButt(ActionEvent actionEvent) {
        customerTable.getItems().setAll(getAllCustomers());
    }

    /** Method to get all appointments.  */
    public void handlerAllAppts(ActionEvent actionEvent) {
        try {
            Stage stage = (Stage) allApptBut.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/appointments.fxml"));
            Parent root = loader.load();

            AppointmentsDAOImpl  cont= loader.getController();
            AppointmentsDAO.allAppts.clear();
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
}
