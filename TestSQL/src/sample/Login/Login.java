package sample.Login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.DatabaseConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Login {
    @FXML
    private Button CancelButton;
    @FXML
    private Label loginMessage;
    @FXML
    private TextField USERNAME;
    @FXML
    private PasswordField PASSWORD;

    public void LoginAE(ActionEvent event) throws IOException {
        if (USERNAME.getText().isBlank() == false && PASSWORD.getText().isBlank() == false){
            DatabaseConnection databaseConnection = new DatabaseConnection();
            Connection connection = databaseConnection.getConnection();
            String login = "Select * from Login ";
            try {
                Statement statement = connection.createStatement();
                ResultSet que = statement.executeQuery(login);
                while (que.next()){
                    if (que.getString(1).equals(USERNAME.getText()) && que.getString(2).equals(PASSWORD.getText())){
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("/sample/EDIT/Edit.fxml"));
                        Parent homeParent = loader.load();
                        Scene scene = new Scene(homeParent);
                        stage.setScene(scene);
                        stage.centerOnScreen();
                    }else {
                        loginMessage.setText("Invalid username or password");
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                e.getCause();
            }
        }else {
            loginMessage.setText("Please enter username and password");
        }
    }

    public void CancelAE(ActionEvent event) {
        Stage stage = (Stage) CancelButton.getScene().getWindow();
        stage.close();
    }
}

