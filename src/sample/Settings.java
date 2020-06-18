package sample;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Settings {
    public TextField password;
    public Button changePasswordButton;
    public Label changeStatus;

    public String pwd;
    public String oldPwd;
    public String login;
    public String token;

    public void setPass(KeyEvent keyEvent) {
        pwd = password.getText();
    }

    public void setOldPwd(String oldPwd) {
        this.oldPwd = oldPwd;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void changePassword() throws UnirestException {
        Unirest.setTimeouts(0, 0);
        HttpResponse<String> response = Unirest.post("http://localhost:8080/changepassword")
                .header("Authorization", token)
                .header("Content-Type", "application/json")
                .body("{\r\n\t\"login\":\"" + login + "\",\r\n\t\"oldpassword\":\"" + oldPwd + "\",\r\n\t\"newpassword\":\"" + pwd + "\"\r\n}")
                .asString();

        if (response.getStatus() == 400) {
            changeStatus.setText(response.getBody());
        } else {
            changeStatus.setText("Successfully Changed!");
        }
    }
}
