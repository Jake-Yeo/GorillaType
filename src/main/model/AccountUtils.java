package model;

import model.exceptions.PasswordTooShortException;
import model.exceptions.UserOrPassWrongException;
import model.exceptions.UsernameTakenException;
import model.exceptions.UsernameTooShortException;
import org.json.JSONArray;
import org.json.JSONObject;
import model.persistence.Writable;

import java.util.ArrayList;

// Represents an Account Utility object that stores a list of accounts.
public class AccountUtils implements Writable {

    private ArrayList<Account> listOfAccounts;
    private Account loggedInAccount;

    public static final int MIN_PASS_LENGTH = 5;
    public static final int MIN_USER_LENGTH = 1;

    // Effects: Creates an object that has an empty list of accounts. It will set accountLoggedIn to null
    public AccountUtils() {
        listOfAccounts = new ArrayList<>();
        loggedInAccount = null;
    }

    // Requires: password.length >= MIN_PASS_LENGTH, username.length >= 1
    // Modifies: this
    // Effects: Creates an Account object with a specified username and password. If an account in the list of accounts
    // has the same username as the given username, then nothing will happen and the method returns false. Otherwise it
    // returns true. Throws PasswordTooShortException if password.length < MIN_PASS_LENGTH. Throws
    // UsernameTakenException if username is taken. Throws UsernameTooShortException if username.length < 1
    public void signUp(String username, String password) throws UsernameTakenException, PasswordTooShortException,
            UsernameTooShortException {
        if (password.length() < MIN_PASS_LENGTH) {
            throw new PasswordTooShortException();
        }
        if (username.length() < MIN_USER_LENGTH) {
            throw new UsernameTooShortException();
        }
        for (Account account: listOfAccounts) {
            if (account.getUsername().equals(username)) {
                throw new UsernameTakenException();
            }
        }
        loggedInAccount = new Account(username, password);
        listOfAccounts.add(getAccountLoggedIn());
        EventLog.getInstance().logEvent(new Event("New account was signed up"));
    }

    // Modifies: this
    // Effects: Will login to one of the Accounts in the list of Accounts based on the given username and password.
    // If account does not exist or username or password is wrong then do nothing. Returns true if you successfully
    // login, false otherwise.
    public void login(String username, String password) throws UserOrPassWrongException {
        for (Account account: getListOfAccounts()) {
            if (account.getUsername().equals(username) && account.getPassword().equals(password)) {
                loggedInAccount = account;
                EventLog.getInstance().logEvent(new Event("Account was logged in."));
                return;
            }
        }
        throw new UserOrPassWrongException();
    }

    // Modifies: this
    // Effects: Logs the currently signed-in user out from the program, AccountLoggedIn will be set to null.
    public void logOut() {
        loggedInAccount = null;
        EventLog.getInstance().logEvent(new Event("Logged in account logged out."));
    }

    // Modifies: this
    // Effects: Deletes the account of the currently signed-in user by removing the account from list of Accounts.
    public void deleteLoggedInAccount() {
        getListOfAccounts().remove(loggedInAccount);
        loggedInAccount = null;
        EventLog.getInstance().logEvent(new Event("Logged in account deleted"));
    }

    // Effects: Prints all the logs in the logged in account.
    public String printLogs(Account loggedInAccount) {
        String logsToReturn = "";
        int logNum = 0;
        for (Log log: loggedInAccount.getLogs()) {
            logsToReturn += "LOG " + logNum + ":\n" + log.toString() + "\n\n";
            logNum++;
        }
        return logsToReturn;
    }

    public ArrayList<Account> getListOfAccounts() {
        return listOfAccounts;
    }

    public Account getAccountLoggedIn() {
        return loggedInAccount;
    }

    // Effects: Turns this object into a JSONObject
    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("listOfAccounts", listOfAccountsToJson());
        if (loggedInAccount != null) {
            jsonObject.put("loggedInAccount", loggedInAccount.toJson());
        } else {
            jsonObject.put("loggedInAccount", JSONObject.NULL);
        }
        EventLog.getInstance().logEvent(new Event("AccountUtils data was saved."));
        return jsonObject;
    }

    // EFFECTS: returns accounts in listOfAccounts as a JSONArray
    private JSONArray listOfAccountsToJson() {
        JSONArray listOfAccountsJson = new JSONArray();

        for (Account account: listOfAccounts) {
            listOfAccountsJson.put(account.toJson());
        }

        return listOfAccountsJson;
    }

    // Effects: Compares if the content of two objects are equal. So if they are different objects but have the same
    // content then this will return true
    public boolean contentEquals(AccountUtils accountUtils) {
        return accountUtils.toJson().toString().equals(this.toJson().toString());
    }

    // Effects: Returns a string containing all logged information of the given account
    public String filterLogsDisplay(int accuracy, Account account) {
        String stringToReturn = "";
        for (Log log: account.getLogs()) {
            if (log.getUserResults().getAccuracy() >= accuracy) {
                stringToReturn += log.toString();
            }
        }
        EventLog.getInstance().logEvent(new Event("Logs filtered and displayed."));
        return stringToReturn;
    }
}
