package utilities;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Month;

    /** Reports class. */
public class Reports {

        public int appts;
        public Month month;
        public String type;
        public String fl;
        public String country;
        public int amount;



        /** List to store first level report. */
        static ObservableList<Reports> flReport = FXCollections.observableArrayList();
        /** List to store monthly appointments report. */
        static ObservableList<Reports> monthlyReport = FXCollections.observableArrayList();


        /** Report Constructor for monthly report.  */
        public Reports(int appts,Month month, String type){
            this.appts=appts;
            this.month=month;
            this.type=type;
        }

        /** Report constructor for first level report. */
        public Reports(int amount, String fl, String country){
            this.amount=amount;
            this.fl=fl;
            this.country=country;
        }


        /** Method to monthly appointments report. Displays amount of appointments per type and month. */
        public static ObservableList<Reports> getMonthlyReport(){
            try {
                monthlyReport.clear();
                String sql = " SELECT  COUNT(Appointment_ID), type, month(start) FROM appointments GROUP BY month(start), type;";
                PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    int start = rs.getInt(3);
                    int count = rs.getInt(1);
                    Month month = Month.of(start);
                    String type = rs.getString(2);

                    Reports monthly = new Reports(count, month, type);
                    monthlyReport.add(monthly);
                }

            }catch(SQLException e){
                System.out.println(e.getMessage());
            }

            return monthlyReport;
        }

        /** Method to get amount of customers in certain areas.  Displays tally of customers in first level divisions, country.
         @return flReport  */
        public static ObservableList<Reports> getFlReport(){
            try{
                flReport.clear();
                String sql= "SELECT  COUNT(Customer_ID), division, country FROM customerAddress GROUP BY country, division;";
                PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    int amount = rs.getInt(1);
                    String fl = rs.getString(2);
                    String country = rs.getString(3);

                    Reports flD = new Reports(amount, fl, country);

                    flReport.add(flD);
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

            return  flReport;
        }


        /** Reports Getters and setters.  */

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getAppts() {
        return appts;
    }

    public void setAppts(int appts) {
        this.appts = appts;
    }

    public Month getMonth() {
        return month;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getFl() {
            return fl;
        }

        public void setFl(String fl) {
            this.fl = fl;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }


}

