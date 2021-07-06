package model;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;



/** This is the product class. */
public class Product {

    //declare variables

    private int prodId;
    private String prodName;
    private double prodPrice;
    private int prodStock;
    private int prodMax;
    private int prodMin;

    //create list for associated parts
    private final ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    public Product() {}


    //create constructor with parameters
    public Product(int prodID, String prodName, double prodPrice, int prodStock, int prodMin, int prodMax) {
        this.prodId = prodID;
        this.prodName = prodName;
        this.prodPrice = prodPrice;
        this.prodStock = prodStock;
        this.prodMax = prodMax;
        this.prodMin = prodMin;
    }


    /** Method to add associated parts to list. Parts associated to a product are added.
     @param part Associated part. */
    public void addAssociatedPart(Part part) {

        associatedParts.add(part);
    }


    /** Method to delete associated parts from list. Deletes an associated part from list and removes the association from the product.
     @param selectedAssociatedPart Associated part.
     @return Returns associated Part. */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart) {

        if (selectedAssociatedPart != null)
                associatedParts.remove(selectedAssociatedPart);
                return true;
    }


  /** Method to retrieve all associated parts. Access the list of associated parts.
   @return Returns all associated parts.  */
    public  ObservableList<Part> getAllAssociatedParts () {
            return associatedParts;
    }


    //create getters and setters for fields
    public  int getProdId() {
        return prodId;
    }

    public void setProdId() {
        this.prodId = prodId;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public double getProdPrice() {
        return prodPrice;
    }

    public void setProdPrice(double prodPrice) {
        this.prodPrice = prodPrice;
    }

    public int getProdStock() {
        return prodStock;
    }

    public void setProdStock(int prodStock) {
        this.prodStock = prodStock;
    }

    public int getProdMax() {
        return prodMax;
    }

    public void setProdMax(int prodMax) {
        this.prodMax = prodMax;
    }

    public int getProdMin() {
        return prodMin;
    }

    public void setProdMin(int prodMin) {
        this.prodMin = prodMin;
    }


}

