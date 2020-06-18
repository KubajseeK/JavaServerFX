package sample;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import org.json.JSONArray;


public class Logs {
    public ListView<String> logList;
    public Button logins;
    public Button logouts;

    public String token;

    public void setToken(String token) {
        this.token = token;
    }

    public void getLogInLogs() throws UnirestException {
        logList.getItems().clear();
        Unirest.setTimeouts(0, 0);
        HttpResponse<String> response = Unirest.post("http://localhost:8080/log")
                .header("Authorization", token)
                .header("Content-Type", "application/json")
                .body("{\n\t\"type\": \"login\"\n}")
                .asString();

        JSONArray array = new JSONArray(response.getBody());
        for (int i=0; i<array.length(); i++){
            logList.getItems().addAll("time: "+array.getJSONObject(i).getString("time")+", "+"type: "+array.getJSONObject(i).getString("type"));
        }
    }

    public void getLogOutLogs() throws UnirestException {
        logList.getItems().clear();
        Unirest.setTimeouts(0, 0);
        HttpResponse<String> response = Unirest.post("http://localhost:8080/log")
                .header("Authorization", token)
                .header("Content-Type", "application/json")
                .body("{\n\t\"type\": \"logout\"\n}")
                .asString();

        JSONArray array = new JSONArray(response.getBody());
        for (int i=0; i<array.length(); i++){
            logList.getItems().addAll("time: "+array.getJSONObject(i).getString("time")+", "+"type: "+array.getJSONObject(i).getString("type"));
        }
    }
}
