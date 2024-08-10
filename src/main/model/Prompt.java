package model;

import org.json.JSONObject;
import model.persistence.Writable;

// Represents a Prompt that has boolean settings so that a unique prompt can be generated.
public class Prompt implements Writable {

    private String prompt;
    private boolean areSentencesEnabled;
    private boolean areWordsEnabled;
    private boolean areNumbersEnabled;
    private int approxLength;
    private int actualLength;
    public static final int MIN_PROMPT_SIZE = 50;
    public static final int MAX_PROMPT_SIZE = 2500;

    // Requires: At least one or more of the inputted boolean values must be true.
    // approxCharSize >= MIN_PROMPT_SIZE
    // Effects: Creates a prompt object with boolean settings and length settings shown below.
    // The prompt is an empty string when constructed. It stores the prompt string.
    // actualLength is 0 when the object is created. Represents the prompt length.
    // approxLength is used to generate a prompt of an approximate length.
    public Prompt(boolean areSentencesEnabled, boolean areWordsEnabled, boolean areNumbersEnabled, int approxLength) {
        this.areSentencesEnabled = areSentencesEnabled;
        this.areWordsEnabled = areWordsEnabled;
        this.areNumbersEnabled = areNumbersEnabled;
        this.approxLength = approxLength;
        prompt = "";
        actualLength = 0;
    }

    public String getPrompt() {
        return prompt;
    }

    // Effects: Represents the Prompt object in a string format.
    @Override
    public String toString() {
        String promptAsString
                = "Prompt: " + getPrompt() + "\n"
                + "Are Sentences Enabled: " + getAreSentencesEnabled() + "\n"
                + "Are Words Enabled: " + getAreWordsEnabled() + "\n"
                + "Are Numbers Enabled: " + getAreNumbersEnabled() + "\n"
                + "Approx Length: " + getApproxLength() + "\n"
                + "Actual Length: " + getActualLength();
        return promptAsString;
    }

    public boolean getAreSentencesEnabled() {
        return areSentencesEnabled;
    }

    public boolean getAreWordsEnabled() {
        return areWordsEnabled;
    }

    public boolean getAreNumbersEnabled() {
        return areNumbersEnabled;
    }

    public int getApproxLength() {
        return approxLength;
    }

    public int getActualLength() {
        return actualLength;
    }

    // Effects: Compares if the content of two objects are equal. So if they are different objects but have the same
    // content then this will return true
    public boolean contentEquals(Prompt prompt) {
        return prompt.toJson().toString().equals(this.toJson().toString());
    }

    // Modifies: This
    // Effects: Changes the prompt and also updates the actual length of the prompt
    public void setPrompt(String prompt) {
        this.prompt = prompt;
        actualLength = prompt.length();
    }

    // Effects: Turns this object into a JSONObject
    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("prompt", prompt);
        jsonObject.put("areSentencesEnabled", areSentencesEnabled);
        jsonObject.put("areWordsEnabled", areWordsEnabled);
        jsonObject.put("areNumbersEnabled", areNumbersEnabled);
        jsonObject.put("approxLength", approxLength);
        jsonObject.put("actualLength", actualLength); // Don't need this but will keep it in for comparing objects
        return jsonObject;
    }
}
