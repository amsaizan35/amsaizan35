package model;

/** First Level Divisions class. */
public class FirstLevelDivisions {

    private String division;
    private int divID;
    private int countID;



    /** Constructor for First Level Divisions. */
    public FirstLevelDivisions(int divID, String division) {
        this.divID=divID;
        this.division = division;

    }

/** Getter for division id. */
    public int getDivID() {
        return divID;
    }



    /** Method to display first level division. */
    @Override
    public String toString() {
        return ( "["+divID+"]" +  division );
    }
}


