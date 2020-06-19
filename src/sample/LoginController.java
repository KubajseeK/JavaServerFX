package sample;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.event.ActionEvent;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import okhttp3.*;
import org.json.JSONObject;

import java.awt.*;
import java.io.IOException;


public class LoginController {
public TextField loginField;
public PasswordField passwordField;
public Label statusMessage;
public Button btnLogin;
public Button btnSignUp;
public AnchorPane rootPane;

public String userLogin;
public String userPassword;

public void initialize() {
    statusMessage.setVisible(false);
}

 /**DONE **/
    public void logIn() throws UnirestException {
        setLogin();
        setPass();
        JSONObject jsonObject;

        Unirest.setTimeouts(0, 0);
        HttpResponse<String> response = Unirest.post("http://localhost:8080/login")
                .header("Content-Type", "application/json")
                .body("{\n\t\"login\":\""+userLogin+"\",\n\t\"password\":\""+userPassword+"\"\n}")
                .asString();

        if (response.getStatus() == 201 || response.getStatus() == 200) {
            jsonObject = new JSONObject(response.getBody());
            openApp(jsonObject);
            closeLoginWindow();
        } else {
            jsonObject = new JSONObject(response.getBody());
            statusMessage.setText(jsonObject.getString("error"));
            statusMessage.setVisible(true);
        }
    }

    public void setLogin(){
        userLogin = loginField.getText();
    }
    public void setPass(){
        userPassword = passwordField.getText();
    }

    public void closeLoginWindow() {
        Stage stage = (Stage) btnLogin.getScene().getWindow();
        stage.close();
    }

    private void openSignUpWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("signUp.fxml"));
            Parent root1 = loader.load();

            Stage stage = new Stage();
            Scene scene = new Scene(root1, 390, 280);
            stage.resizableProperty().setValue(Boolean.FALSE);
            stage.setScene(scene);
            stage.setTitle("Sign Up");
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openApp(JSONObject jsonObject) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("chatApp.fxml"));
            Parent root1 = loader.load();

            ChatAppController mainControl = loader.getController();
            mainControl.setLogin(jsonObject.getString("login"));
            mainControl.setUserName(jsonObject.getString("fname"), jsonObject.getString("lname"));
            mainControl.setToken(jsonObject.getString("token"));
            mainControl.setPass(userPassword);
            mainControl.getUsers(jsonObject.getString("token"));

            Stage stage = new Stage();
            Scene scene = new Scene(root1, 950, 680);
            stage.resizableProperty().setValue(Boolean.FALSE);
            stage.setScene(scene);
            stage.setTitle("Chat App");
            stage.show();
        }
        catch (IOException | UnirestException e) {
            e.printStackTrace();
        }
    }

    public void signUp(ActionEvent actionEvent) {
    closeLoginWindow();
    openSignUpWindow();

    }

    public void setMsg(String message) {
        statusMessage.setText(message);
    }
}
