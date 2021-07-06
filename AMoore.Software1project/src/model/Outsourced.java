package model;

/** This is the Outsourced class that inherits from Part class. */
public class Outsourced extends Part{
    //declare variable
    private String companyName;

    //constructor with inherited variables from part
    public Outsourced(int partID, String name, double price, int inStock, int min, int max, String companyName) {
        super(partID, name, price, inStock, min, max);
        this.companyName=companyName;
    }

    //getter and setter
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

}
