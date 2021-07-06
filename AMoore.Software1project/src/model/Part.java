package model;

/** This is the part class, which is abstract and the parent of InHouse and Outsourced classes. */
public abstract  class Part {


    //declare fields, variables
    private int partID;
    private String name;
    private double price;
    private int inStock;
    private int min;
    private int max;



    //create constructor with fields
    public Part(int partID, String name, double price, int inStock, int min, int max) {

        this.partID=partID;
        this.name = name;
        this.price = price;
        this.inStock = inStock;
        this.min = min;
        this.max = max;
    }


    //create getters

    public int getPartID() {
        return partID;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getInStock() {
        return inStock;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    //create setters


    public void setPartID(int partID) {
        this.partID = partID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setInStock(int inStock) {
        this.inStock = inStock;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public void setMax(int max) {
        this.max = max;
    }



}
