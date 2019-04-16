package controllers;

import core.Router;
import core.SQLite;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController implements Initializable {
    
    Router r = new Router() ;
    
    @FXML
    private Button loginBtn ;
    @FXML
    private Button registerBtn ;
    @FXML
    private TextField name ;
            @FXML
    private TextField email ;
    @FXML
    private PasswordField password;
                @FXML
    private TextField phone ;
                @FXML
    private ComboBox gender ;
    
    @FXML
    private void signIn(ActionEvent event) {
        r.RouteOutlet(loginBtn, "Login");   
    }
    
    @FXML
    private void signUp(ActionEvent event) {
        if ( name.getText().isEmpty() ||
                email.getText().isEmpty() ||
                    password.getText().isEmpty() ||
                        phone.getText().isEmpty() ||
                            gender.getValue().toString().isEmpty() ) {
            return;
        }
        String[] person = new String[5] ;
        person[0] = name.getText() ;
        person[1] = email.getText() ;
        person[2] = password.getText() ;
        person[3] = phone.getText() ;
        person[4] = gender.getValue().toString() ;
//        System.out.println(person[0] + " " + person[1] + " " + person[2] + " " + person[3] + " " + person[4] + " ");
        if (SQLite.getInstance().register(person)) {
            r.RouteOutlet(registerBtn, "User");
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        gender.getItems().addAll("","male", "female");
        gender.getSelectionModel().select("");
    }    
    
}