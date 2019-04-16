package controllers;

import core.Book;
import core.Common;
import core.Person;
import core.Report;
import core.Router;
import core.SQLite;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class UserController implements Initializable {
    
    private final Router r ;
    Person p = Person.getInstance() ;
    private int Borrowed = 0 , Penalty = 0 ;
        
    @FXML private TabPane root ;
    @FXML private Label userId ;
    @FXML private Label name ;
    @FXML private Label email ;
    @FXML private Label password;
    @FXML private Label phone ;
    @FXML private Label gender ;
    @FXML private Label auth ;
    @FXML private Label fees ;
    @FXML private Label borrowed ;
                
    @FXML private TextField search_title ;
    @FXML private Button searchBtn ;
    @FXML private TableView<Book> search_result ;
    @FXML private TableColumn idCol ;
    @FXML private TableColumn isbnCol ;
    @FXML private TableColumn titleCol ;
    @FXML private TableColumn authorCol ;
    @FXML private TableColumn quantityCol ;
    
    @FXML private TableView<Report> order_result ;
    @FXML private TableColumn rIdCol ;
    @FXML private TableColumn uIdCol ;
    @FXML private TableColumn bIdCol ;
    @FXML private TableColumn issueCol ;
    @FXML private TableColumn dueCol ;
    
    @FXML private ComboBox<Integer> return_id ;
    @FXML private Button returnBtn ;
    
    @FXML private ComboBox<Integer> borrow_id ;
    @FXML private Button borrowBtn ;
    
        
    @FXML
    private Button signout_no ;
                                @FXML
    private Button signout_yes ;

    public UserController() {
        this.r = new Router();
    }
    
    @FXML
    private void searchBook(ActionEvent event) {
        if (search_title.getText().isEmpty()) {
            return ;
        }
        
        ObservableList<Book> result =  SQLite.getInstance().searchBooksByTitle(search_title.getText());
        search_result.getItems().setAll(result);
    }
    
    @FXML
    private void borrowBook(ActionEvent event) {
        if (Borrowed >= 3) {
            Common.alert("Borrow Book", "You have exceed maximum borrow limit (3 books)!");
        }
        if (borrow_id.getSelectionModel().isEmpty()) {
            return ;
        }
        SQLite.getInstance().BorrowBook(p.getId(), borrow_id.getValue());
        update();
    }
    
    @FXML
    private void returnBook(ActionEvent event) {
        if (return_id.getSelectionModel().isEmpty()) {
            return ;
        }
        SQLite.getInstance().ReturnBook(p.getId(), return_id.getValue());
        update();
    }
    
    
    @FXML
    private void signOutYes(ActionEvent event) {
        Person.signOut();
        r.RouteOutlet(signout_yes, "Login");
    }
    @FXML
    private void signOutNo(ActionEvent event) {
        root.getSelectionModel().select(0);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        userId.setText(p.getId()+"");
        name.setText(p.getName());
        email.setText(p.getEmail());
        password.setText(p.getPassword());
        phone.setText(p.getPhone());
        gender.setText(p.getGender());
        auth.setText(p.getAuth());
        
        idCol.setCellValueFactory((new PropertyValueFactory("id")));
        isbnCol.setCellValueFactory((new PropertyValueFactory("isbn"))) ;
        titleCol.setCellValueFactory((new PropertyValueFactory("title"))) ;
        authorCol.setCellValueFactory((new PropertyValueFactory("author"))) ;
        quantityCol.setCellValueFactory((new PropertyValueFactory("quantity"))) ;
        
        rIdCol.setCellValueFactory((new PropertyValueFactory("id")));
        uIdCol.setCellValueFactory((new PropertyValueFactory("userId"))) ;
        bIdCol.setCellValueFactory((new PropertyValueFactory("bookId"))) ;
        issueCol.setCellValueFactory((new PropertyValueFactory("issueDate"))) ;
        dueCol.setCellValueFactory((new PropertyValueFactory("dueDate"))) ;
        
        update();
       
    }
    
    private void update() {
        Penalty = SQLite.getInstance().userFees(p.getId()) ;
        Borrowed = SQLite.getInstance().userborrowed(p.getId()) ;
        fees.setText(Penalty * 3 + " $");
        borrowed.setText(Borrowed + " / 3");
        order_result.getItems().setAll(SQLite.getInstance().userOrderBooks(this.p.getId()));
        return_id.getItems().setAll(SQLite.getInstance().canReturn(p.getId()));
        borrow_id.getItems().setAll(SQLite.getInstance().canBorrow(p.getId()));
    }
    
}