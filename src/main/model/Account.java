package model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import model.persistence.Writable;

// Represents an Account object which stores the username, list of logs, password, settings, etc.
public class Account implements Writable {

    private String username;
    private String password;
    private ArrayList<Log> logs;
    private Settings settings;

    // Requires: password.length >= AccountUtils.MIN_PASS_LENGTH, username.length >= 1
    // Effects: Creates an account object with a default settings object, an empty list of logs, and a username and
    // password
    public Account(String username, String password) {
        this.username = username;
        this.password = password;
        logs = new ArrayList<>();
        settings = new Settings();
    }

    public Settings getSettings() {
        return settings;
    }

    public ArrayList<Log> getLogs() {
        return logs;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    // Requires: username must not be the same as any of the account usernames in the list of accounts.
    // username.length >= 1
    // Modifies: this
    // Effects: Changes the username to the given string.
    public void changeUsername(String username) {
        this.username = username;
        EventLog.getInstance().logEvent(new Event("Logged in account username changed."));
    }

    // Requires: password.length >= 5
    // Modifies: this
    // Effects: Changes the password to the given string.
    public void changePassword(String password) {
        this.password = password;
        EventLog.getInstance().logEvent(new Event("Logged in account password changed"));
    }

    // Modifies: this
    // Effects: Adds the given Log object to the list of logs.
    public void addLog(Log log) {
        getLogs().add(log);
        EventLog.getInstance().logEvent(new Event("Log added to logged in account"));
    }

    // Modifies: this
    // Effects: Removes all logs from the list of logs
    public void clearLogs() {
        getLogs().clear();
        EventLog.getInstance().logEvent(new Event("Logged in account logs cleared"));
    }

    // Effects: Turns this object into a JSONObject
    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", username);
        jsonObject.put("password", password);
        jsonObject.put("logs", logsToJson());
        jsonObject.put("settings", settings.toJson());
        return jsonObject;
    }

    // EFFECTS: returns logs in logs as a JSON array
    private JSONArray logsToJson() {
        JSONArray logsJson = new JSONArray();

        for (Log log: logs) {
            logsJson.put(log.toJson());
        }

        return logsJson;
    }

    // Effects: Compares if the content of two objects are equal. So if they are different objects but have the same
    // content then this will return true
    public boolean contentEquals(Account account) {
        return account.toJson().toString().equals(this.toJson().toString());
    }
}
