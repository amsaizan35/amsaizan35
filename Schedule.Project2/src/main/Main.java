package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import utilities.DBConnection;
import java.io.IOException;
import java.sql.SQLException;


/** Main class to start app. */
public class Main extends Application {


    /** Method sends to log in.  Sends users to log in screen.
     @param primaryStage stage to start.*/
    @Override
    public void start(Stage primaryStage) throws Exception {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));

            Parent root = loader.load();
            primaryStage.setTitle("Log In");
            primaryStage.setScene(new Scene(root, 470, 425));
            primaryStage.setResizable(false);
            primaryStage.show();

        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }


    /** Method to start database. Launches app and starts database.
    @param args args.
     */
    public static void main(String[] args) throws SQLException {

        DBConnection.startConnection();

            launch(args);

            DBConnection.closeConnection();
        }
    }

