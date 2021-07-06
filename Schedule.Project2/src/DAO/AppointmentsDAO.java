package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointments;
import model.Contacts;
import model.Users;


/** Appointments Dao class. */
public interface AppointmentsDAO {

    /** List to get store all appointments. */
    ObservableList<Appointments> allAppts = FXCollections.observableArrayList();

    /** List to get store all contacts. */
    ObservableList<Contacts> allContacts = FXCollections.observableArrayList();

    /** List to get store all users. */
    ObservableList<Users> allUsers = FXCollections.observableArrayList();

    /** List to get store weekly appointments. */
    ObservableList<Appointments> weekAppts = FXCollections.observableArrayList();

    /** List to get store monthly appointments. */
    ObservableList<Appointments> monthAppts = FXCollections.observableArrayList();

}
