package ui.page;

import model.AccountUtils;
import model.Settings;
import ui.GorillaTypeUi;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

// Represents a page that displays account manipulation options
public class AccountSettingsPage extends Page implements PreLoadedContentPage {

    private JButton logoutButton;
    private JButton deleteAccountButton;
    private JButton saveDataButton;
    private JButton switchToTypingPageButton;
    private JButton viewLogsButton;
    private JCheckBox isAutomaticSaveEnabledBox;

    // Effects: Creates an AccountSettingsPage object
    public AccountSettingsPage(GorillaTypeUi controller) {
        super(controller);
    }

    // Effects: Sets up the loggedInSettings and check and unchecks the components correctly
    @Override
    public void runBeforeSwitch() {
        Settings loggedInSettings = GorillaTypeUi.getAccountUtils().getAccountLoggedIn().getSettings();

        isAutomaticSaveEnabledBox.setSelected(loggedInSettings.getIsAutomaticSaveEnabled());
    }

    // Modifies: this
    // Effects: Initializes the components
    @Override
    protected void initComponents() {
        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(getLogoutButtonAction());
        deleteAccountButton = new JButton("Delete Account");
        deleteAccountButton.addActionListener(getDeleteAccountButtonAction());
        saveDataButton = new JButton("Save Data");
        saveDataButton.addActionListener(getSaveDataButtonAction());
        switchToTypingPageButton = new JButton("Back");
        switchToTypingPageButton.addActionListener(getSwitchToTypingPageButtonAction());
        viewLogsButton = new JButton("View Logs");
        viewLogsButton.addActionListener(getViewLogsButtonAction());

        isAutomaticSaveEnabledBox = new JCheckBox("Enable Automatic Save");
        isAutomaticSaveEnabledBox.addChangeListener(getAutomaticSaveChangeListener());
    }

    // Effects: Returns an action listener that sets the page to VIEW_LOGS
    public ActionListener getViewLogsButtonAction() {
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GorillaTypeUi.getGorillaTypeUi().setPageType(Pages.VIEW_LOGS);
            }
        };
        return actionListener;
    }

    // Effects: Returns an action listener asks the user if they want to save their data
    public ActionListener getLogoutButtonAction() {
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GorillaTypeUi.getGorillaTypeUi().setPageType(Pages.LOGOUT_SAVE_ACCOUNT);
            }
        };
        return actionListener;
    }

    // Effects: Returns an action listener that deletes the account and redirect the user to the LOGIN_SIGNUP page
    public ActionListener getDeleteAccountButtonAction() {
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GorillaTypeUi.getAccountUtils().deleteLoggedInAccount();
                GorillaTypeUi.getGorillaTypeUi().setPageType(Pages.LOGIN_SIGNUP);
                saveAccountUtils();
            }
        };
        return actionListener;
    }

    // Effects: Saves the GorillaTypeUi.getAccountUtils() object as a .json file
    private void saveAccountUtils() {
        AccountUtils accountUtilsToSave = GorillaTypeUi.getAccountUtils();
        try {
            GorillaTypeUi.getJsonWriter().open();
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        }
        GorillaTypeUi.getJsonWriter().saveAccountUtilsObject(accountUtilsToSave);
        GorillaTypeUi.getJsonWriter().close();
    }

    // Effects: Returns an action listener that runs saveAccountUtils()
    public ActionListener getSaveDataButtonAction() {
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveAccountUtils();
            }
        };
        return actionListener;
    }

    // Modifies: this
    // Effects: Returns an action listener that sets the page to TYPING_TEST
    public ActionListener getSwitchToTypingPageButtonAction() {
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GorillaTypeUi.getGorillaTypeUi().setPageType(Pages.TYPING_TEST);
            }
        };
        return actionListener;
    }

    // Modifies: GorillaTypeUi.getAccountUtils().getAccountLoggedIn().getSettings()
    // Effects: Updates the automatic settings.
    public ChangeListener getAutomaticSaveChangeListener() {
        ChangeListener changeListener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                Settings loggedInSettings = GorillaTypeUi.getAccountUtils().getAccountLoggedIn().getSettings();
                loggedInSettings.setIsAutomaticSaveEnabled(isAutomaticSaveEnabledBox.isSelected());
            }
        };
        return changeListener;
    }

    // Modifies: this
    // Effects: Adds components and formats the ui
    @Override
    protected void addComponents() {
        groupLayout.setHorizontalGroup(
                groupLayout.createSequentialGroup()
                        .addComponent(logoutButton)
                        .addComponent(deleteAccountButton)
                        .addComponent(saveDataButton)
                        .addComponent(switchToTypingPageButton)
                        .addComponent(viewLogsButton));
    }
}
