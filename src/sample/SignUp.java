package sample;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class SignUp {
    public TextField firstName;
    public TextField lastName;
    public TextField userLogin;
    public TextField userPass;
    public Button signup;
    public Label errorMessage;
    public Button goBack;

    public String fname;
    public String lname;
    public String login;
    public String password;

    public void goBack(){
        closeSignUpWindow();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root1 = loader.load();

            Stage stage = new Stage();
            Scene scene = new Scene(root1, 600, 400);
            stage.resizableProperty().setValue(Boolean.FALSE);
            stage.setScene(scene);
            stage.setTitle("Log In");
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goBack(String message){
        closeSignUpWindow();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root1 = loader.load();

            LoginController controller = loader.getController();
            controller.setMsg(message);
            controller.statusMessage.setVisible(true);

            Stage stage = new Stage();
            Scene scene = new Scene(root1, 600, 400);
            stage.resizableProperty().setValue(Boolean.FALSE);
            stage.setScene(scene);
            stage.setTitle("Login");
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeSignUpWindow() {
        Stage stage = (Stage) goBack.getScene().getWindow();
        stage.close();
    }

    public void setFName(){
        fname = firstName.getText();
    }
    public void setLName(){
        lname = lastName.getText();
    }
    public void setUserLogin(){
        login = userLogin.getText();
    }
    public void setUserPass(){
        password = userPass.getText();
    }

    public void signUp() throws UnirestException {
        if (userLogin == null || userPass == null || firstName == null || lastName == null){
            errorMessage.setText("All fields mandatory.");
            return;
        }

        Unirest.setTimeouts(0, 0);
        HttpResponse<String> response = Unirest.post("http://localhost:8080/signup")
                .header("Content-Type", "application/json")
                .body("{\n\t\"fname\":\""+ fname +"\",\n\t\"lname\":\""+ lname +"\",\n\t\"login\":\""+ login +"\",\n\t\"password\":\""+ password +"\"\n}")
                .asString();

        if (response.getStatus() == 400){
           errorMessage.setText(response.getBody());
        }else {
            goBack("Successfully registered");
        }
    }
}