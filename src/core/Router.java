package core;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.Window;

public class Router {
    
    public void RouteOutlet (Button c, String view) {
        try {
            Scene scene = c.getScene();
            Window window = scene.getWindow();
            Stage stage = (Stage) window;
            Parent root = FXMLLoader.load(getClass().getResource("/resources/view/" + view + ".fxml")) ;
            stage.setScene(new Scene(root));
        } catch (Exception ex) {
            ExceptionHandler.showMessage("Route Error", ex.getMessage());
        }
    }
}
