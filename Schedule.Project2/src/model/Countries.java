package model;

/** Countries class. */
public class Countries {


    /** Declare variables in countries. */
    private String countryName;
    private int countryID;


    /** Countries  Constructor  for country id and name. */
    public Countries(int countryID, String countryName) {
        this.countryID= countryID;
        this.countryName = countryName;
    }

    /** Countries getters and setters. */
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    public String getCountryName() {
        return countryName;
    }

    public int getCountryID() {
        return countryID;
    }

    /** Method to display countries. */
    @Override
    public String toString() {
        return ( "["+countryID+"] " +countryName );
    }
}
