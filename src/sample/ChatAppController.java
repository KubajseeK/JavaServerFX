package sample;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class ChatAppController {
    public Pane optionPane;
    public Label userName;
    public Pane usersPane;
    public ListView<String> userView;
    public ListView<String> msgList;
    public ListView<String> userList;
    public Button logBtn;
    public Label sendingTo;
    public TextField msgField;
    public Button sendBtn;
    public Button chooseUserBtn;
    public Button logOutBtn;

    public String user;
    public String token;
    public String login;
    public String fieldMsg;
    public String pass;

    public void initialize() {
        chooseUserBtn.setVisible(false);
    }

    public void setUserName(String fName, String lName){
        userName.setText(fName +" "+ lName);
    }

    public void getUsers(String token) throws UnirestException {
        Unirest.setTimeouts(0, 0);
        HttpResponse<String> response = Unirest.get("http://localhost:8080/users")
                .header("token", token)
                .asString();
        JSONObject object = new JSONObject(response.getBody());

        for (int i=0; i<object.length(); i++){
            if (login.equals(object.getJSONObject(String.valueOf(i)).getString("login"))){
                continue;
            }
            userView.getItems().addAll(object.getJSONObject(String.valueOf(i)).getString("login"));
        }
    }

    public void getUser(){
        user = userView.getSelectionModel().getSelectedItem();
        chooseUserBtn.fire();
        sendingTo.setText(userView.getSelectionModel().getSelectedItem());
    }

    public void getMessages() throws UnirestException {
        msgList.getItems().clear();
        userList.getItems().clear();
        Unirest.setTimeouts(0, 0);
        HttpResponse<String> response = Unirest.post("http://localhost:8080/messages")
                .header("Authorization", token)
                .header("Content-Type", "application/json")
                .body("{\n\t\"login\": \""+login+"\",\n\t\"from\": \""+user+"\"\n}")
                .asString();

        JSONArray objects = new JSONArray(response.getBody());
        for (int i=0; i<objects.length(); i++){
            msgList.getItems().addAll(objects.getJSONObject(i).getString("message"));
            userList.getItems().addAll(objects.getJSONObject(i).getString("from"));
        }
    }

    public void setToken(String token){
        this.token = token;
    }
    public void setLogin(String login){
        this.login = login;
    }

    public void getFieldMsg(){
        fieldMsg = msgField.getText();
    }

    public void sendMessage() throws UnirestException {
        Unirest.setTimeouts(0, 0);
        HttpResponse<String> response = Unirest.post("http://localhost:8080/message/new")
                .header("Authorization", token)
                .header("Content-Type", "application/json")
                .body("{\r\n\t\"from\":\""+login+"\",\r\n\t\"to\":\""+user+"\",\r\n\t\"message\":\""+fieldMsg+"\"\r\n}")
                .asString();

        msgField.setText("");
        chooseUserBtn.fire();
    }

    public void openLogWindow(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("logs.fxml"));
            Parent root1 = loader.load();

            Logs logWindow = loader.getController();
            logWindow.setToken(token);
            logWindow.getLogins();

            Stage stage = new Stage();
            Scene scene = new Scene(root1, 280, 480);
            stage.resizableProperty().setValue(Boolean.FALSE);
            stage.setScene(scene);
            stage.setTitle("User Logs");
            stage.show();
        }
        catch (IOException | UnirestException e) {
            e.printStackTrace();
        }
    }

    /** Done **/
    public void logOut() throws UnirestException {
        Unirest.setTimeouts(0, 0);
        HttpResponse<String> response = Unirest.post("http://localhost:8080/logout")
                .header("Authorization", token)
                .header("Content-Type", "application/json")
                .body("{\n\t\"login\":\""+login+"\"\n}")
                .asString();

        openLoginWindow();
        closeMainWindow();
    }

    private void closeMainWindow() {
        Stage stage = (Stage) logOutBtn.getScene().getWindow();
        stage.close();
    }

    private void openLoginWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root1 = loader.load();

            Stage stage = new Stage();
            Scene scene = new Scene(root1, 600, 400);
            stage.resizableProperty().setValue(Boolean.FALSE);
            stage.setScene(scene);
            stage.setTitle("Chat App");
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showOptions(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("settings.fxml"));
            Parent root1 = loader.load();

            Settings settings = loader.getController();
            settings.setOldPwd(pass);
            settings.setLogin(login);
            settings.setToken(token);

            Stage stage = new Stage();
            Scene scene = new Scene(root1, 600, 260);
            stage.resizableProperty().setValue(Boolean.FALSE);
            stage.setScene(scene);
            stage.setTitle("User Logs");
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPass(String password) {
        pass = password;
    }
}
