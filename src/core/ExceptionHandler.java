package core;

import java.sql.SQLException;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 *
 * Handle SQLite Exceptions thrown by SQLite Class
 */
abstract public class ExceptionHandler extends Exception {
//    private String message ;

//    public ExceptionHandler(String m) {
//        message = m ;
//    }
    
    public static void showMessage (String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.getButtonTypes().setAll(ButtonType.CLOSE);
        alert.showAndWait();
         
//        if (alert.getResult() == ButtonType.CLOSE) {
//            System.exit(1);
//        }
    }
    
    public static void SqlHandler (SQLException ex) {
        System.out.println(ex.getMessage());
        showMessage("SQLite Exception", ex.getMessage()) ;
    }
}
