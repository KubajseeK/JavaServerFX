package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;


public class LoginController {
public TextField loginField;
public PasswordField passwordField;
public Label statusMessage;
public Button btnLogin;
public AnchorPane rootPane;


    public void loginProcess(ActionEvent actionEvent) throws IOException {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("chatApp.fxml"));
            rootPane.getChildren().setAll(pane);

    }
}
