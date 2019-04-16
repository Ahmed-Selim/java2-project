package controllers;

import core.ExceptionHandler;
import core.Person;
import core.Router;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController implements Initializable {
    
    Router r = new Router() ;
        
    @FXML
    private TextField email ;
    @FXML
    private PasswordField password;
    @FXML
    private Button loginBtn ;
    @FXML
    private Button registerBtn ;
    
    @FXML
    private void signIn(ActionEvent event) {
        try {
            Person p = Person.getInstance(email.getText(), password.getText()) ;
            if (p.getAuth().equals("admin")) { 
                r.RouteOutlet(loginBtn, "Admin"); 
            } else { 
                r.RouteOutlet(loginBtn, "User"); 
            }
        } catch (Exception e) {} 
    }
    
    @FXML
    private void signUp(ActionEvent event) {
        r.RouteOutlet(registerBtn, "Register");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
}