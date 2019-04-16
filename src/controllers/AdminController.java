package controllers;

import core.Book;
import core.Person;
import core.Report;
import core.Router;
import core.SQLite;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
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

public class AdminController implements Initializable {
    
    private final Router r ;
    Person p = Person.getInstance() ;
    private HashMap<Integer,Book> books ;
        
    @FXML private TabPane root ;
    @FXML private Label adminId ;
    @FXML private Label name ;
    @FXML private Label email ;
    @FXML private Label password;
    @FXML private Label phone ;
    @FXML private Label gender ;
    @FXML private Label auth ;
    
    @FXML private Button addBtn ;
    @FXML private TextField insert_isbn ;
    @FXML private TextField insert_title ;
    @FXML private TextField insert_author ;
    @FXML private TextField insert_quantity ;
                                
    @FXML private ComboBox book_update_id;
    @FXML private TextField update_isbn ;
    @FXML private TextField update_title ;
    @FXML private TextField update_author ;
    @FXML private TextField update_quantity ;
    @FXML private Button updateBtn ;
    
    @FXML private TextField search_title ;
    @FXML private Button searchBtn ;
    @FXML private TableView<Book> search_result ;
    @FXML private TableColumn idCol ;
    @FXML private TableColumn isbnCol ;
    @FXML private TableColumn titleCol ;
    @FXML private TableColumn authorCol ;
    @FXML private TableColumn quantityCol ;
    
    @FXML private TextField order_id ;
    @FXML private Button orderBtn ;
    @FXML private TableView<Report> order_result ;
    @FXML private TableColumn rIdCol ;
    @FXML private TableColumn uIdCol ;
    @FXML private TableColumn bIdCol ;
    @FXML private TableColumn issueCol ;
    @FXML private TableColumn dueCol ;
    
    @FXML private Button signout_no ;
    @FXML private Button signout_yes ;

    public AdminController() {
        this.r = new Router();
    }
    
    @FXML
    private void addBook(ActionEvent event) {
        if ( ! (
            insert_isbn.getText().isEmpty() ||
                insert_title.getText().isEmpty() ||
                    insert_author.getText().isEmpty() ||
                        insert_quantity.getText().isEmpty() 
                ) ) {
            SQLite.getInstance().insertBook(insert_isbn.getText(), insert_title.getText(),
                    insert_author.getText(), Integer.parseInt(insert_quantity.getText()));
        }
    }
    
    @FXML
    private void listBook(ActionEvent event) {
        int bookId = Integer.parseInt(book_update_id.getValue().toString());
        update_isbn.setText(books.get(bookId).getIsbn());
        update_title.setText(books.get(bookId).getTitle());
        update_author.setText(books.get(bookId).getAuthor());
        update_quantity.setText(books.get(bookId).getQuantity()+"");
    }
    
    @FXML
    private void updateBook(ActionEvent event) {
        if (update_isbn.getText().isEmpty() ||
                update_title.getText().isEmpty() ||
                    update_author.getText().isEmpty() ||
                        update_quantity.getText().isEmpty() 
            ) {
            return ;
        }
        Object[] obj = new Object[5] ;
        obj[0] = Integer.parseInt(book_update_id.getValue().toString());
        obj[1] = update_isbn.getText();
        obj[2] = update_title.getText();
        obj[3] = update_author.getText();
        obj[4] = Integer.parseInt(update_quantity.getText());
        Book b = new Book(obj);
        SQLite.getInstance().UpdateBook(b);
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
    private void listOrder(ActionEvent event) {
        if (order_id.getText().isEmpty()) {
            return ;
        }
        ObservableList<Report> result =  SQLite.getInstance().orderBooksById(Integer.parseInt(order_id.getText()));
        order_result.getItems().setAll(result);
        
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
        adminId.setText(p.getId()+"");
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
        
        books = SQLite.getInstance().allBooks() ;
        
        books.entrySet().stream().map((entry) -> entry.getValue()).forEachOrdered((book) -> {
            book_update_id.getItems().add(book.getId());
        });

        listeners();
    }
    
    
    private void listeners () {
        insert_isbn.textProperty().addListener((ObservableValue<? extends String> observable,
                String oldValue, String newValue) -> {
            if (!newValue.matches("\\d*")) {
                insert_isbn.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        
        insert_quantity.textProperty().addListener((ObservableValue<? extends String> observable,
                String oldValue, String newValue) -> {
            if (!newValue.matches("\\d*")) {
                insert_quantity.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        
        update_quantity.textProperty().addListener((ObservableValue<? extends String> observable,
                String oldValue, String newValue) -> {
            if (!newValue.matches("\\d*")) {
                update_quantity.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        
        order_id.textProperty().addListener((ObservableValue<? extends String> observable,
                String oldValue, String newValue) -> {
            if (!newValue.matches("\\d*")) {
                order_id.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }
}