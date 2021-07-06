package model;

import controller.MainController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.AlertMessages;


/** This is the Inventory class. Holds all parts and products. */
public class Inventory {



    //private list with all parts
    private static final ObservableList<Part> allParts = FXCollections.observableArrayList();

    /** Method to get all parts from list. Access to all parts in inventory.
     @return Returns all parts. */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    //private list containing all products
    private static final ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /** Method to get all products from private list. Access to all products in inventory.
     @return Returns all products. */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }


    /** Method to add new part to parts list. Add part to parts list.
     @param newPart New part */
    public static void addPart(Part newPart){

    allParts.add(newPart);
    }


    /** Method to delete selected part from parts list. Delete selected part.

      @param selectedPart Part to delete.
     * @return return true.
     */
    public static boolean deletePart(Part selectedPart) {

            getAllParts().removeIf(pa -> pa == selectedPart);
            allParts.remove(selectedPart);

        return true;
    }

    /** Method to update selected part in parts list. Update part in list.

      @param index Part index.
      @param updatedPart Part to update.
     */
    public static void updatePart(int index, Part updatedPart) {
       if (getAllParts().indexOf(MainController.partSelected) == index)
            allParts.remove(MainController.partSelected);
           allParts.add(updatedPart);

    }

    /** Method to add new product to product list. Adds products to list.
     @param newProduct Product to add.
     */
    public static void addProduct(Product newProduct) {

        newProduct.addAssociatedPart(MainController.partSelected);
        allProducts.add(newProduct);

    }

    /** Method to delete selected product from product list. Delete product from list.
     @param selectedProduct Product to delete.
     * @return Deleted.
     */
    public static boolean deleteProduct(Product selectedProduct) {
                 getAllProducts().removeIf(pa -> pa == selectedProduct);
                 allProducts.remove(selectedProduct);
           return true;
    }


    /** Method to update selected product in product list. Update products.
    @param newProduct Product to update.
     @param index Index of product.
     */
    public static void updateProduct(Product newProduct, int index) {
        if (getAllProducts().indexOf(MainController.prodSelected) == index)
            allProducts.remove(MainController.prodSelected);
               allProducts.add(newProduct);

    }


    /** Mathod to search part list for specific part with id. Use part ID to search for part.
      @param partID Part ID.
     @return Part.
     */
    public static  Part searchPart(int partID) {
        for (int i = 0; i < allParts.size(); i++) {
            Part p = allParts.get(i);
            if(p.getPartID() == partID){
                return p;
            }
        }
        AlertMessages.errorWindow(3);
        return null ;

    }


    /** Method to search specific product in list by id. Search product list by id.
     @param product Product ID.
     @return Product or null.
     */
    public static Product searchProduct ( int product) {
        for (int i = 0; i < allProducts.size() ; i++) {
                Product p= allProducts.get(i);
                if(p.getProdId() == product){
                    return p;
                }

        }
        AlertMessages.errorWindow(3);
                return null;

    }


        /** Methhod to search part list by partial name. Use part of name to search for part in list.
         @param partName Partial name or null.
          */
    public static ObservableList<Part> searchPart(String partName) {

            ObservableList<Part> namedParts = FXCollections.observableArrayList();

            for (Part pa : allParts) {
                if (pa.getName().contains(partName))
                    namedParts.add(pa);
            }

          return namedParts;

        }

        /** Method to search product list by partial name. Use partila name to search for product.
         @param productName Partial Name or null. */
    public static ObservableList<Product> searchProduct(String productName) {

        ObservableList<Product> namedProducts =FXCollections.observableArrayList();


        for (Product p : allProducts) {
                    if (p.getProdName().contains(productName)) {
                        namedProducts.add(p);

                    }
        }

        return namedProducts;

    }

}
