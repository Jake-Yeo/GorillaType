package model.persistence;

import model.*;
import model.exceptions.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class JsonReader {

    private String savedFileLocation;
    public static final String FILE_TO_READ_NAME = "GorillaType.json";

    // Effects: Sets the savedFileLocation by getting the savedFileFolderLocation and concatenating it with
    // the FILE_TO_READ_NAME
    public JsonReader(String savedFileFolderLocation) {
        this.savedFileLocation = savedFileFolderLocation + "/" + FILE_TO_READ_NAME;
    }

    // Effects: Sets the savedFileLocation by getting the savedFileFolderLocation and concatenating it with
    // the given fileName. This is to be used for tests only
    public JsonReader(String savedFileFolderLocation, String fileName) {
        this.savedFileLocation = savedFileFolderLocation + "/" + fileName;
    }

    public String getsavedFileLocation() {
        return savedFileLocation;
    }

    // Effects: Sets the savedFileLocation by getting the savedFileFolderLocation and concatenating it with
    // the FILE_TO_READ_NAME
    public void setSavedFileLocation(String savedFileFolderLocation) {
        this.savedFileLocation = savedFileFolderLocation + FILE_TO_READ_NAME;
    }

    // Effects: Reads the json file and reconstructs the AccountUtils object. Got code from JSONSerializationDemo
    public AccountUtils parseAccountUtilsJson() throws IOException, UserOrPassWrongException,
            SettingsException {
        JSONObject jsonAccountUtils = getAccountUtilsJson();
        AccountUtils accountUtils = new AccountUtils();

        List<Account> listOfAccount = parseListOfAccounts(jsonAccountUtils.getJSONArray("listOfAccounts"));
        accountUtils.getListOfAccounts().addAll(listOfAccount);

        if (!jsonAccountUtils.isNull("loggedInAccount")) {
            Account loggedInAccount;
            loggedInAccount = parseJsonAccount(jsonAccountUtils.getJSONObject("loggedInAccount"));
            String loggedInAccUsername = loggedInAccount.getUsername();
            String loggedInAccPassword = loggedInAccount.getPassword();
            accountUtils.login(loggedInAccUsername, loggedInAccPassword);
        }

        return accountUtils;

    }

    private JSONObject getAccountUtilsJson() throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(savedFileLocation), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        } catch (IOException e) {
            throw new IOException(e);
        }

        return new JSONObject(contentBuilder.toString());
    }

    // Effects: Reads the json file and reconstructs the ListOfAccounts object in the AccountUtils object
    private ArrayList<Account> parseListOfAccounts(JSONArray jsonAccountArray) throws FileNotFoundException,
            SettingsException {
        ArrayList<Account> listOfAccounts = new ArrayList<>();
        for (Object jsonObject: jsonAccountArray) {
            listOfAccounts.add(parseJsonAccount((JSONObject) jsonObject));
        }
        return listOfAccounts;
    }

    // Effects: Reads the given Json string and reconstructs the Account object.
    private Account parseJsonAccount(JSONObject jsonAccount) throws FileNotFoundException,
            InvalidApproxLengthException, InvalidBooleanSelectionException {
        Account account = new Account((String) jsonAccount.get("username"), (String) jsonAccount.get("password"));

        ArrayList<Log> listOfLogs = parseJsonlogArray((JSONArray) jsonAccount.getJSONArray("logs"));
        account.getLogs().addAll(listOfLogs);

        Settings settings = account.getSettings();
        Settings jsonSettings = parseJsonSettings((JSONObject) jsonAccount.get("settings"));
        settings.setApproxLength(jsonSettings.getApproxLength());
        settings.setAreWordsEnabled(jsonSettings.getAreWordsEnabled());
        settings.setAreSentencesEnabled(jsonSettings.getAreSentencesEnabled());
        settings.setAreNumbersEnabled(jsonSettings.getAreNumbersEnabled());
        settings.setIsAutomaticSaveEnabled(jsonSettings.getIsAutomaticSaveEnabled());

        return account;
    }

    private ArrayList<Log> parseJsonlogArray(JSONArray jsonLogArray) throws FileNotFoundException {
        ArrayList<Log> listOfLogs = new ArrayList<>();
        for (Object jsonObject: jsonLogArray) {
            listOfLogs.add(parseJsonLog((JSONObject) jsonObject));
        }
        return listOfLogs;
    }

    // Effects: Reads the json file and reconstructs the Settings object.
    private Settings parseJsonSettings(JSONObject jsonSettings) throws InvalidApproxLengthException,
            InvalidBooleanSelectionException {
        Settings settings = new Settings();

        settings.setAreSentencesEnabled(jsonSettings.getBoolean("areSentencesEnabled"));
        settings.setAreWordsEnabled(jsonSettings.getBoolean("areWordsEnabled"));
        settings.setAreNumbersEnabled(jsonSettings.getBoolean("areNumbersEnabled"));
        settings.setApproxLength(jsonSettings.getInt("approxLength"));
        settings.setIsAutomaticSaveEnabled(jsonSettings.getBoolean("isAutomaticSaveEnabled"));

        return settings;
    }

    // Effects: Reads the json file and reconstructs the Log object.
    private Log parseJsonLog(JSONObject jsonLog) throws FileNotFoundException {
        Prompt prompt = parseJsonPrompt((JSONObject) jsonLog.get("prompt"));

        UserResults userResults = parseJsonUserResults((JSONObject) jsonLog.get("userResults"), prompt);

        Log log = new Log(prompt, userResults);

        return log;
    }

    // Effects: Reads the json file and reconstructs the Prompt object.
    private Prompt parseJsonPrompt(JSONObject jsonPrompt) {

        String prompt = jsonPrompt.getString("prompt");
        boolean areSentencesEnabled = jsonPrompt.getBoolean("areSentencesEnabled");
        boolean areWordsEnabled = jsonPrompt.getBoolean("areWordsEnabled");
        boolean areNumbersEnabled = jsonPrompt.getBoolean("areNumbersEnabled");
        int approxLength = jsonPrompt.getInt("approxLength");

        Prompt promptObj = new Prompt(areSentencesEnabled, areWordsEnabled, areNumbersEnabled, approxLength);

        promptObj.setPrompt(prompt);

        return promptObj;
    }

    // Effects: Reads the json file and reconstructs the UserResults object.
    private UserResults parseJsonUserResults(JSONObject jsonUserResults, Prompt prompt) throws FileNotFoundException {
        String userInput = jsonUserResults.getString("userInput");

        UserResults userResults = new UserResults();
        userResults.updateUserResults(prompt, userInput);

        long startTimeLong = jsonUserResults.getBigDecimal("startTime").longValue();
        Time startTime = new Time(startTimeLong);

        long endTimeLong = jsonUserResults.getBigDecimal("endTime").longValue();
        Time endTime = new Time(endTimeLong);

        userResults.setEndTime(endTime);
        userResults.setStartTime(startTime);

        return userResults;
    }
}
