package model;


/** This is the InHouse Part class that extends Part class. */
public class InHouse extends Part {

    //declare variables
    private static int machineID;



    //create constructor inherited from parent class part
    public InHouse(int partID, String name, double price, int inStock, int min, int max, int machineID) {
        super(partID, name, price, inStock, min, max);
        InHouse.machineID =machineID;

    }


    //getter  and setter
    public static int getMachineID() {
        return machineID;
    }

    public void setMachineID(int machineID) {
         InHouse.machineID = machineID;
    }


}
