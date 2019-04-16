package core;

import java.io.File;
import java.sql.*;
import java.util.HashMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SQLite implements QueryBuilder {

    private static SQLite singleton = null ;
    
    private final String URL = "jdbc:sqlite:" + System.getProperty("user.home").replace("\\","/") + "/cs206.db";
    private String query;
    private Connection con = null ;
    private Statement stmt = null ;
    private ResultSet rs = null ;
    private PreparedStatement pstmt = null ;
    
    public static SQLite getInstance() {
        if (singleton == null) { 
            singleton = new SQLite(); 
        } 
        return singleton; 
    }

    private SQLite() {
        File directory = new File(System.getProperty("user.home"));
        if (!directory.exists())
            directory.mkdir();
        init();
    }
    
    public final void init () {
        try {
            con = DriverManager.getConnection(URL);
            stmt = con.createStatement();

            stmt.execute(personTable);
            stmt.execute(bookTable);
            stmt.execute(reportTable);
            stmt.execute(initPerson);   stmt.execute(initBook);   stmt.execute(initReport);
        } catch (SQLException ex) {
            ExceptionHandler.SqlHandler(ex) ;
            System.exit(1);
        }
    }
    
    public Object[] login (String email , String password) {
        Object [] person = new Object [7] ;
        query = "select id, name, email, password, phone, gender, auth from person where email = ? and password = ? " ;
        try {
            pstmt = con.prepareStatement(query) ;
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                person[0] = rs.getInt(1) ;
                person[1] = rs.getString(2) ;
                person[2] = rs.getString(3) ;
                person[3] = rs.getString(4) ;
                person[4] = rs.getString(5) ;
                person[5] = rs.getString(6) ;
                person[6] = rs.getString(7) ;
            } else {
                ExceptionHandler.showMessage("login Error", "No User/Admin Found!");
            }
        } catch (SQLException ex) {
            ExceptionHandler.showMessage("login Error", ex.getMessage());
        }
        return person ;
    }
   
    public boolean register(String[] person) {
        query = "insert or ignore into person (name, email, password, phone, gender) values (?,?,?,?,?)";
        try {
            pstmt = con.prepareStatement(query) ;
            pstmt.setString(1, person[0]);
            pstmt.setString(2, person[1]);
            pstmt.setString(3, person[2]);
            pstmt.setString(4, person[3]);
            pstmt.setString(5, person[4]);
            if (pstmt.executeUpdate() > 0) {
                Common.alert("User Added Successfully", "name: "+person[0]+"\nemail: "+
                        person[1]+"\napassword: "+person[2]+"\nphone: "+person[3]+"\ngender: "+person[4]);
                Person.getInstance(person[1], person[2]);
                return true ;
            } else {
                Common.alert("Add User", "User Already exists!");
            }
        } catch (SQLException ex) {
            ExceptionHandler.showMessage("Insert User Error", ex.getMessage());
        }
        return false;
    }
    
    public void insertBook(String isbn, String title, String author, int quantity) {
        query = "insert or ignore into book (isbn, title, author, quantity) values (?,?,?,?)" ;
        try {
            pstmt = con.prepareStatement(query) ;
            pstmt.setString(1, isbn);
            pstmt.setString(2, title);
            pstmt.setString(3, author);
            pstmt.setInt(4, quantity);
            if (pstmt.executeUpdate() > 0) {
                Common.alert("Book Added Successfully", "isbn: "+isbn+"\ntitle: "+title+"\nauthor: "+author+"\nquantity: "+quantity);
            } else {
                Common.alert("Add Book", "Book Already exists!");
            }
        } catch (SQLException ex) {
            ExceptionHandler.showMessage("Insert Book Error", ex.getMessage());
        }
    }
    
    public void UpdateBook(Book book) {
        query = "update book set isbn = ? , title = ? , author = ? , quantity = ? where id = ? " ;
        try {
            pstmt = con.prepareStatement(query) ;
            pstmt.setString(1, book.getIsbn());
            pstmt.setString(2, book.getTitle());
            pstmt.setString(3, book.getAuthor());
            pstmt.setInt(4, book.getQuantity());
            pstmt.setInt(5, book.getId());
            if (pstmt.executeUpdate() > 0) {
                Common.alert("Book Updated Successfully", "isbn: "+book.getIsbn()+"\ntitle: "
                        +book.getTitle()+"\nauthor: "+book.getAuthor()+"\nquantity: "+book.getQuantity());
            } else {
//                Common.alert("Update Book", "Book Already exists!");
            }
        } catch (SQLException ex) {
            ExceptionHandler.showMessage("Update Book Error", ex.getMessage());
        }
    }
    
    public ObservableList<Book> searchBooksByTitle (String title) {
        query = "select id, isbn, title, author, quantity from book where title like '%" + title + "%' " ;
        ObservableList<Book> b = FXCollections.observableArrayList();
        Object[] obj = new Object[5];
        try {
            rs = con.prepareStatement(query).executeQuery() ;
            if (!rs.isBeforeFirst() ) {    
                Common.alert("Search Books", "No Books at database!");
            } else {
                while(rs.next()){
                    obj[0] = rs.getInt(1) ;
                    obj[1] = rs.getString(2) ;
                    obj[2] = rs.getString(3) ;
                    obj[3] = rs.getString(4) ;
                    obj[4] = rs.getInt(5) ;
                    b.add(new Book(obj));
                }
            }
        } catch (SQLException ex) {
            ExceptionHandler.showMessage("Book search Error", ex.getMessage());
        }
        return b ;
    }
    
    public ObservableList<Report> orderBooksById (int id) {
        query = "select id, uid, bid, issueDate, dueDate from report where bid = " + id ;
        ObservableList<Report> b = FXCollections.observableArrayList();
        try {
            rs = con.prepareStatement(query).executeQuery() ;
            if (!rs.isBeforeFirst() ) {    
                Common.alert("Order Books", "No Books at database!");
            } else {
                while(rs.next()){
                    b.add(
                        new Report(
                            rs.getInt(1),
                            rs.getInt(2),
                            rs.getInt(3),
                            rs.getString(4),
                            rs.getString(5)
                       )
                    );
                }
            }
        } catch (SQLException ex) {
            ExceptionHandler.showMessage("Book Order Error", ex.getMessage());
        }
        return b ;
    }
    
 
    public HashMap<Integer,Book> allBooks () {
        query = "select id, isbn, title, author, quantity from book" ;
        HashMap<Integer,Book> b = new HashMap<>();
        Object[] obj = new Object[5] ;
        try {
            rs = con.prepareStatement(query).executeQuery() ;
            if (!rs.isBeforeFirst() ) {    
                Common.alert("List Books", "No Books at database!");
            } else {
                while(rs.next()) {
                    obj[0] = rs.getInt(1) ;
                    obj[1] = rs.getString(2) ;
                    obj[2] = rs.getString(3) ;
                    obj[3] = rs.getString(4) ;
                    obj[4] = rs.getInt(5) ;
                    
                    b.put((Integer) obj[0], new Book(obj));
                }
            }
        } catch (SQLException ex) {
            ExceptionHandler.showMessage("Book List Error", ex.getMessage());
        }
        return b ;
    }
    
    public int userFees (int id) {
        query = "select count(bid) from report where uid = " + id +" group by uid, dueDate having dueDate <= date('now')" ;
        int b = 0 ;
        try {
            rs = con.prepareStatement(query).executeQuery() ;
            if (rs.isBeforeFirst()) {
                b = rs.getInt(1);
            }
        } catch (SQLException ex) {
            ExceptionHandler.showMessage("Book Order Error", ex.getMessage());
        }
        return b ;
    }
    
    public int userborrowed (int id) {
        query = "select count(bid) from report where uid = " + id +" group by uid" ;
        int b = 0 ;
        try {
            rs = con.prepareStatement(query).executeQuery() ;
            if (rs.isBeforeFirst()) {
                b = rs.getInt(1);
            }
        } catch (SQLException ex) {
            ExceptionHandler.showMessage("Book Borrowed Error", ex.getMessage());
        }
        return b ;
    }
    
    public ObservableList<Report> userOrderBooks (int id) {
        query = "select id, uid, bid, issueDate, dueDate from report where uid = " + id ;
        ObservableList<Report> b = FXCollections.observableArrayList();
        try {
            rs = con.prepareStatement(query).executeQuery() ;
            if (!rs.isBeforeFirst() ) {    
                Common.alert("Order Books", "No Books at database!");
            } else {
                while(rs.next()){
                    b.add(
                        new Report(
                            rs.getInt(1),
                            rs.getInt(2),
                            rs.getInt(3),
                            rs.getString(4),
                            rs.getString(5)
                       )
                    );
                }
            }
        } catch (SQLException ex) {
            ExceptionHandler.showMessage("Book Order Error", ex.getMessage());
        }
        return b ;
    }
    
    public ObservableList<Integer> canBorrow (int uid) {
        query = "select id from book where id not in (select bid from report where uid = "+ uid +" ) and quantity > 0" ;
        ObservableList<Integer> b = FXCollections.observableArrayList();
        try {
            rs = con.prepareStatement(query).executeQuery() ;
            if (!rs.isBeforeFirst() ) {    
                Common.alert("Borrow Books", "No Books at database!");
            } else {
                while(rs.next()){
                    b.add(rs.getInt(1));
                }
            }
        } catch (SQLException ex) {
            ExceptionHandler.showMessage("Book Borrow Error", ex.getMessage());
        }
        return b ;
        
    }
    
    public ObservableList<Integer> canReturn (int uid) {
        query = "select bid from report where uid = "+ uid ;
        ObservableList<Integer> b = FXCollections.observableArrayList();
        try {
            rs = con.prepareStatement(query).executeQuery() ;
            if (!rs.isBeforeFirst() ) {    
                Common.alert("Return Books", "No Books at database!");
            } else {
                while(rs.next()){
                    b.add(rs.getInt(1));
                }
            }
        } catch (SQLException ex) {
            ExceptionHandler.showMessage("Book return Error", ex.getMessage());
        }
        return b ;
        
    }
    
    public ObservableList<Report> BorrowBook (int uid, int bid) {    
        ObservableList<Report> b = FXCollections.observableArrayList();
        try {
            stmt.addBatch("update book set quantity = quantity - 1 where id = "+bid+" and quantity > 0");
            stmt.addBatch("insert into report (uid, bid) values ( "+uid+" , "+bid+" )");
            int[] temp = stmt.executeBatch();
            for (int i : temp) {
                System.out.println(i);
            }
            if (temp[0] == 0 && temp[1] == 0) {
                Common.alert("Borrow Books", "Can't Borrow this book!");
            } else {
                Common.alert("Borrow Books", "Borrow Book Successfully");
            }
        } catch (SQLException ex) {
            ExceptionHandler.showMessage("Book Order Error", ex.getMessage());
        }
        return b ;
    }
    
    public ObservableList<Report> ReturnBook (int uid, int bid) {    
        ObservableList<Report> b = FXCollections.observableArrayList();
        try {
            stmt.addBatch("update book set quantity = quantity + 1 where id = "+bid);
            stmt.addBatch("delete from report where uid = "+uid+" and bid = "+bid);
            int[] temp = stmt.executeBatch();
            for (int i : temp) {
                System.out.println(i);
            }
            if (temp[0] == 0 && temp[1] == 0) {
                Common.alert("Return Book", "Can't Return this book!");
            } else {
                Common.alert("Return Book", "Return Book Successfully");
            }
        } catch (SQLException ex) {
            ExceptionHandler.showMessage("Book Return Error", ex.getMessage());
        }
        return b ;
    }
}