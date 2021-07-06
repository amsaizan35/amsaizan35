package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.AlertMessages;
import model.Users;
import utilities.DBConnection;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.ResourceBundle;


/** Login Controller Class. */
public class LoginController implements Initializable {

    public Button loginButton;
    public TextField usernameText;
    public PasswordField passwordText;
    public Label locationLabel;
    public Label localTimeLabel;
    public Label usernamelabel;
    public Label passwordlabel;
    public Label loginformLabel;
    public Label timelabel;
    public Label locationLLAbel;

    public boolean isUser=false;
    public static Users userSelected;
    public boolean loggedin = false;


    /** Method to initialize the scene. Starts Login scene.*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        System.out.println("Logging in...");

        Locale location = Locale.getDefault();
        locationLabel.setText(location.getDisplayCountry()+ " |  "+ location.getDisplayLanguage() );
        localTimeLabel.setText(String.valueOf(ZonedDateTime.now()));

        ResourceBundle rb = ResourceBundle.getBundle("main/lang", Locale.getDefault());

           loginformLabel.setText(rb.getString("login"));
            usernamelabel.setText(rb.getString("username"));
            passwordlabel.setText(rb.getString("password"));
            locationLLAbel.setText(rb.getString("location"));
            timelabel.setText(rb.getString("localtime"));
            loginButton.setText(rb.getString("log"));
            usernameText.setPromptText(rb.getString("username"));
            passwordText.setPromptText(rb.getString("password"));


    }


    /** Method to get login info from user. Logs into the database.
     @param password password entered.
     @param username  username entered.*/
    public Users getLogin(String username, String password) {
        Users user = null;
        try {

            String sql = "SELECT  user_id, user_name, password FROM users WHERE user_name= ? and password = ?;";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                isUser = true;
                loggedin=true;
                int id = rs.getInt("user_id");
                String pass = rs.getString("password");
                String name = rs.getString("user_name");

                user = new Users(id, name, pass);

            }

        } catch (SQLException | RuntimeException  e) {
            AlertMessages.errorWindow(3);
            System.out.println(e.getMessage());
        }
        return user;
    }

        /** Method to submit username and password. Looks in database for match. */
    public void handlerLogInButt(ActionEvent actionEvent) {
        try{
            String password = passwordText.getText();
            String username =usernameText.getText();
            userSelected=getLogin(username, password);

            Stage stage = (Stage) loginButton.getScene().getWindow();
            if(isUser) {
                loggedin=true;
                userSelected=getLogin(username, password);

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/user.fxml"));
                Parent root = loader.load();

                UserController cont = loader.getController();
                cont.nameLabel.setText("    " + userSelected.getUsername());
                cont.apptTable.setItems(userSelected.getAllUserAppts(userSelected.getUserID()));
                cont.apptReminder(userSelected.getUserID());

                stage.setScene(new Scene(root, 1215, 520));
                stage.setTitle("Schedule");
                stage.setResizable(false);
                stage.show();

                String file = "login_activity.txt", user1;
                FileWriter fw = new FileWriter(file, true);
                PrintWriter pr = new PrintWriter(fw);
                user1 = "SUCCESSFUL LOGIN  -- Username: " + userSelected.getUsername() + " -- logged in: " + loggedin + " at : " +  Timestamp.valueOf(LocalDateTime.now());
                pr.println(user1 );
                pr.close();

            }else{
                AlertMessages.errorWindow(6);

                String file = "login_activity.txt", user1;
                FileWriter fw = new FileWriter(file, true);
                PrintWriter pr = new PrintWriter(fw);
                user1 = "UNSUCCESSFUL LOGIN!!! --- Username input: " + usernameText.getText() + " password input : " + passwordText.getText() + " logged in: " + loggedin + " tried logging in at : "  + Timestamp.valueOf(LocalDateTime.now());
                pr.println(user1 );
                pr.close();

            }

        }catch(IOException | RuntimeException | SQLException e){
            System.out.println(e.getMessage());
        }

    }


    /** Method to get username. Get user info from database. */
    public void handlerUsernameText(ActionEvent actionEvent) {
        String username = usernameText.getText();
        usernameText.setText(username);
    }

    /** Method to get password. Get user info from database. */
    public void handlerPasswordText(ActionEvent actionEvent) {
        String password = passwordText.getText();
        passwordText.setText(password);
    }
}
