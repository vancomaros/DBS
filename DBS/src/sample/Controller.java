package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;


public class Controller{

    public Controller() {

    }

    @FXML
    public void login(ActionEvent event) {

    }

    @FXML
    private void Register(ActionEvent event) throws SQLException, IOException, Exception {
        Parent root = FXMLLoader.load(getClass().getResource("registration.fxml"));
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(new Scene(root, 635, 400));
        window.show();
    }


}
