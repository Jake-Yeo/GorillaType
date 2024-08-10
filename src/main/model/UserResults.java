package model;

import org.json.JSONObject;
import model.persistence.Writable;

import java.io.FileNotFoundException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

// Represents the results of a user after typing the prompt. Stores words per minute, errors, accuracy, etc.
public class UserResults implements Writable {

    private String userInput;
    private Time startTime;
    private Time endTime;
    private int accuracy;
    private int length;
    private TypingTestUtils typingTestUtils;
    private static final int MILLI_IN_A_SEC = 1000;

    // Effect: Creates an object that automatically logs its creation time. Also intializes typingTestUtils for use.
    public UserResults() throws FileNotFoundException {
        startTime = new Time(System.currentTimeMillis());
        typingTestUtils = new TypingTestUtils(false);
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public String getUserInput() {
        return userInput;
    }

    // Modifies: this
    // Effects: Will update the UserResults object by setting the accuracy, userInput, and endTime
    public void updateUserResults(Prompt prompt, String userInput) {
        setUserInput(userInput);
        setAccuracy(prompt);
        endTime = new Time(System.currentTimeMillis());
    }

    // Modifies: this
    // Effects: Will set length to the length of userInput and update the new userInput with the given string.
    private void setUserInput(String userInput) {
        this.userInput = userInput;
        length = userInput.length();
    }

    public int getWpm() {
        return typingTestUtils.calculateWpm(this);
    }

    public Time getStartTime() {
        return startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public int getAccuracy() {
        return accuracy;
    }

    private void setAccuracy(Prompt prompt) {
        accuracy = typingTestUtils.calculateAccuracy(prompt, this);
    }

    public int getLength() {
        return length;
    }

    // Effects: Returns how long the user took to type the prompt in milliseconds
    public int getTestDuration() {
        return (int) (getEndTime().getTime() - getStartTime().getTime());
    }

    // Effects: Compares if the content of two objects are equal. So if they are different objects but have the same
    // content then this will return true
    public boolean contentEquals(UserResults userResults) {
        return userResults.toJson().toString().equals(this.toJson().toString());
    }

    // Effects: Represents the UserResults object in a string format.
    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
        sdf.setTimeZone(TimeZone.getTimeZone("PST"));
        String userResultsString = "User Input: " + getUserInput() + "\n"
                + "Accuracy: " + getAccuracy() + "%\n"
                + "Words Per Minute: " + getWpm() + "\n"
                + "Start Time: " + sdf.format(getStartTime().getTime()) + " PST\n"
                + "End Time: " + sdf.format(getEndTime().getTime()) + " PST\n"
                + "Duration (seconds): " + getTestDuration() / MILLI_IN_A_SEC;
        return userResultsString;
    }

    // Effects: Turns this object into a JSONObject
    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userInput", userInput);
        jsonObject.put("startTime", startTime.getTime());
        jsonObject.put("endTime", endTime.getTime());
        jsonObject.put("accuracy", accuracy);
        jsonObject.put("length", length);
        return jsonObject;
    }
}
