package core;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class Common {
    public static void alert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.getButtonTypes().setAll(ButtonType.OK);
        alert.showAndWait();
    }
}
