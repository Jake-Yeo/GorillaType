package model;

import model.exceptions.InvalidApproxLengthException;
import model.exceptions.InvalidBooleanSelectionException;
import org.json.JSONObject;
import model.persistence.Writable;

// Represents a settings object that will store all the settings of this program
public class Settings implements Writable {

    private boolean areSentencesEnabled;
    private boolean areWordsEnabled;
    private boolean areNumbersEnabled;
    private boolean isAutomaticSaveEnabled;
    private int approxLength;

    // Effects: Creates a settings object with multiple default settings where:
    // areSentencesEnabled is false,
    // areWordsEnabled is true,
    // areNumbersEnabled is false,
    // approxLength is 50
    // isAutomaticSaveEnabled is false
    public Settings() {
        areSentencesEnabled = false;
        areNumbersEnabled = false;
        areWordsEnabled = true;
        approxLength = 50;
        isAutomaticSaveEnabled = false;
    }

    public void setAreSentencesEnabled(boolean areSentencesEnabled) {
        this.areSentencesEnabled = areSentencesEnabled;
    }

    public void setAreWordsEnabled(boolean areWordsEnabled) {
        this.areWordsEnabled = areWordsEnabled;
    }

    public void setAreNumbersEnabled(boolean areNumbersEnabled) {
        this.areNumbersEnabled = areNumbersEnabled;
    }

    // Effects: Sets the ApproxLength of the prompt. Will throw a InvalidApproxLengthException if
    // approxLength < Prompt.MIN_PROMPT_SIZE
    public void setApproxLength(int approxLength) throws InvalidApproxLengthException {
        if (approxLength < Prompt.MIN_PROMPT_SIZE) {
            throw new InvalidApproxLengthException();
        }
        this.approxLength = approxLength;
    }

    public boolean getIsAutomaticSaveEnabled() {
        return isAutomaticSaveEnabled;
    }

    public void setIsAutomaticSaveEnabled(boolean isAutomaticSaveEnabled) {
        this.isAutomaticSaveEnabled = isAutomaticSaveEnabled;
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

    // Effects: Compares if the content of two objects are equal. So if they are different objects but have the same
    // content then this will return true
    public boolean contentEquals(Settings settings) {
        return settings.toJson().toString().equals(this.toJson().toString());
    }

    // Effects: Checks to see if the final selection of settings is valid.
    public void checkForInvalidBooleanSelection() throws InvalidBooleanSelectionException {
        if (!(areSentencesEnabled || areNumbersEnabled || areWordsEnabled)) {
            throw new InvalidBooleanSelectionException();
        }
    }

    @Override
    public String toString() {
        String settingsString = "Are sentences enabled: " + areSentencesEnabled + "\n"
                + "Are words enabled: " + areWordsEnabled + "\n"
                + "Are numbers enabled: " + areNumbersEnabled + "\n"
                + "Is automatic saving enabled: " + isAutomaticSaveEnabled + "\n"
                + "Approx length to generate: " + approxLength + "\n";
        return settingsString;
    }

    // Effects: Turns this object into a JSONObject
    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("areSentencesEnabled", areSentencesEnabled);
        jsonObject.put("areWordsEnabled", areWordsEnabled);
        jsonObject.put("areNumbersEnabled", areNumbersEnabled);
        jsonObject.put("approxLength", approxLength);
        jsonObject.put("isAutomaticSaveEnabled", isAutomaticSaveEnabled);
        return jsonObject;
    }
}
