package ui.page;

import model.Prompt;
import model.Settings;
import model.exceptions.InvalidApproxLengthException;
import ui.GorillaTypeUi;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

// Represents a settings page to alter the generation of the users prompts
public class PromptSettingsPage extends Page implements PreLoadedContentPage {

    Settings loggedInSettings;

    JCheckBox areSentencesEnabledBox;
    JCheckBox areNumbersEnabledBox;
    JCheckBox areWordsEnabledBox;

    JSlider approxLengthSlider;

    // Effects: Creates a PromptSettingsPage object
    public PromptSettingsPage(GorillaTypeUi controller) {
        super(controller);
    }

    // Modifies: this
    // Effects: Checks and unchecks the components correctly, setting their values accordingly
    @Override
    public void runBeforeSwitch() {
        loggedInSettings = GorillaTypeUi.getAccountUtils().getAccountLoggedIn().getSettings();

        areSentencesEnabledBox.setSelected(loggedInSettings.getAreSentencesEnabled());
        areSentencesEnabledBox.addChangeListener(getPromptSettingsChangeListener());

        areNumbersEnabledBox.setSelected(loggedInSettings.getAreNumbersEnabled());
        areNumbersEnabledBox.addChangeListener(getPromptSettingsChangeListener());

        areWordsEnabledBox.setSelected(loggedInSettings.getAreWordsEnabled());
        areWordsEnabledBox.addChangeListener(getPromptSettingsChangeListener());

        approxLengthSlider.setValue(loggedInSettings.getApproxLength());
        approxLengthSlider.addChangeListener(getPromptSettingsChangeListener());
    }

    // Modifies: this
    // Effects: initialize components
    @Override
    protected void initComponents() {

        areSentencesEnabledBox = new JCheckBox("Enable Sentences");

        areWordsEnabledBox = new JCheckBox("Enable Words");

        areNumbersEnabledBox = new JCheckBox("Enable Numbers");

        approxLengthSlider = new JSlider();
        approxLengthSlider.setMinimum(Prompt.MIN_PROMPT_SIZE);
        approxLengthSlider.setMaximum(Prompt.MAX_PROMPT_SIZE);
    }

    // Modifies: this
    // Effects: Saves all the settings the user changed
    private void saveSettings() throws InvalidApproxLengthException {
        loggedInSettings.setAreSentencesEnabled(areSentencesEnabledBox.isSelected());
        loggedInSettings.setAreNumbersEnabled(areNumbersEnabledBox.isSelected());
        loggedInSettings.setAreWordsEnabled(areWordsEnabledBox.isSelected());
        loggedInSettings.setApproxLength(approxLengthSlider.getValue());
    }

    // Effects: Returns a ChangeListener that runs the saveSettings method
    private ChangeListener getPromptSettingsChangeListener() {
        ChangeListener changeListener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                try {
                    saveSettings();
                } catch (InvalidApproxLengthException ex) {
                    ex.printStackTrace();
                }
            }
        };
        return changeListener;
    }

    // Effects: Adds the components to the page
    @Override
    protected void addComponents() {
        groupLayout.setHorizontalGroup(
                groupLayout.createSequentialGroup()
                        .addComponent(areSentencesEnabledBox)
                        .addComponent(areNumbersEnabledBox)
                        .addComponent(areWordsEnabledBox)
                        .addComponent(approxLengthSlider));
    }
}
