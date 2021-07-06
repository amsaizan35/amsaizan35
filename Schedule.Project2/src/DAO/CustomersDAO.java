package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Countries;
import model.Customers;
import model.FirstLevelDivisions;

/** Customers DAO class. */
public interface CustomersDAO {
    /** List to get store all customers. */
    ObservableList<Customers> allCust= FXCollections.observableArrayList();

    /** List to get store all countries. */
    ObservableList<Countries> allCountries = FXCollections.observableArrayList();

    /** List to get store all first level divisions. */
    ObservableList<FirstLevelDivisions> allDivisions = FXCollections.observableArrayList();

    /** List to get store all states. */
    ObservableList<FirstLevelDivisions> states = FXCollections.observableArrayList();

    /** List to get store all uk divisions. */
    ObservableList<FirstLevelDivisions> ukDiv = FXCollections.observableArrayList();

    /** List to get store all canada divisions. */
    ObservableList<FirstLevelDivisions> canadaDiv = FXCollections.observableArrayList();

}
