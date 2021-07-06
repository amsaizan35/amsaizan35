package utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/** This is the DBConnectin  class that holds the database connection information. */
public class DBConnection {

    //JDBC URL parts
    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";
    private static final String ipAddress = "//wgudb.ucertify.com/WJ06KEP";

    //JDBC URL
    private static final String jdbcUrl = protocol  + vendorName + ipAddress;

    //Driver and connection Interface reference
    private static final String MYSQLJDBCDRIVER = "com.mysql.cj.jdbc.Driver";
    private static Connection  conn = null;

    //DB Username
    private static final String username = "U06KEP";

    //DB Password
    private static final String password = "53688788136";


    /** Method to start connection. Info to get a connection to database. */
    public static Connection startConnection() {

        try {
            Class.forName(MYSQLJDBCDRIVER);
            conn = DriverManager.getConnection(jdbcUrl, username, password);
            System.out.println("Connection successful!!");

        } catch (ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        catch (SQLException e) {
            System.out.println("ERROR " + e.getMessage());
        }

        return conn;
    }

    /** Method to get the connection. Gets the database connection when needed. */
    public static Connection getConnection(){
        return conn;
    }



    /** Method to end connection. Info to end  a connection to database. */
    public static void closeConnection(){

        try {
            conn.close();
            System.out.println("Connection closed");

        } catch (SQLException ignored) {

        }
    }
}
