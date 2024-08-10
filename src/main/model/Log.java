package model;

import org.json.JSONObject;
import model.persistence.Writable;

// Represents a Log which stores the history of on singular typing test. It stores the prompt, and the users results.
public class Log implements Writable {

    private Prompt prompt;
    private UserResults userResults;

    // Effects: Creates a log object storing both Prompt and UserResults Objects
    public Log(Prompt prompt, UserResults userResults) {
        this.prompt = prompt;
        this.userResults = userResults;
    }

    public Prompt getPrompt() {
        return prompt;
    }

    public UserResults getUserResults() {
        return userResults;
    }

    // Effects: Compares if the content of two objects are equal. So if they are different objects but have the same
    // content then this will return true
    public boolean contentEquals(Log log) {
        return log.toJson().toString().equals(this.toJson().toString());
    }

    // Effects: Represents the Log object in a string format.
    @Override
    public String toString() {
        String logString = getPrompt().toString()
                + "\n\n"
                + getUserResults().toString();
        return logString;
    }

    // Effects: Turns this object into a JSONObject
    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("prompt", prompt.toJson());
        jsonObject.put("userResults", userResults.toJson());
        return jsonObject;
    }
}
