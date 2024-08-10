package ui.page;

import model.Account;
import model.AccountUtils;
import ui.GorillaTypeUi;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

// Represents a prompt page where the user can choose to save their account
public class SaveAccountPage extends Page {

    private JButton saveAccountButton;
    private JButton dontSaveAccountButton;
    private JLabel warningPicLabel;
    private boolean isLoggingOut;

    // Effects: Creates a SaveAccountPage object
    public SaveAccountPage(GorillaTypeUi controller) {
        super(controller);
        isLoggingOut = false;
    }

    public void setIsLoggingOut(boolean isLoggingOut) {
        this.isLoggingOut = isLoggingOut;
    }

    // Effects: Initializes all components
    @Override
    protected void initComponents() {
        saveAccountButton = new JButton("Save Account Data");
        saveAccountButton.addActionListener(getSaveAccountButtonAction());
        dontSaveAccountButton = new JButton("Don't Save Account Data");
        dontSaveAccountButton.addActionListener(getDontSaveAccountButtonAction());
        warningPicLabel = new JLabel(new ImageIcon("data/warningImage.png"));
    }

    // Effects: Closes the window, and saves the current account by not replacing it with a backup.
    private ActionListener getSaveAccountButtonAction() {
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GorillaTypeUi.getGorillaTypeUi().exitSaveAccountFrame();
                if (isLoggingOut) {
                    GorillaTypeUi.getAccountUtils().logOut();
                    GorillaTypeUi.getGorillaTypeUi().setPageType(Pages.LOGIN_SIGNUP);
                    saveAccountUtils();
                    return;
                }
                saveAccountUtils();
                System.exit(0);
            }
        };
        return actionListener;
    }

    // Effects: Saves the accountUtils as a .json file
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

    // Effects: Closes the window, and does not save account data by replacing the current account with a backup
    // account.
    private ActionListener getDontSaveAccountButtonAction() {
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Account accountLoggedIn = GorillaTypeUi.getAccountUtils().getAccountLoggedIn();
                Account backupAccount = GorillaTypeUi.getBackupAccount();
                GorillaTypeUi.getAccountUtils().getListOfAccounts().add(backupAccount);
                GorillaTypeUi.getAccountUtils().getListOfAccounts().remove(accountLoggedIn);
                GorillaTypeUi.getGorillaTypeUi().exitSaveAccountFrame();
                if (isLoggingOut) {
                    GorillaTypeUi.getAccountUtils().logOut();
                    GorillaTypeUi.getGorillaTypeUi().setPageType(Pages.LOGIN_SIGNUP);
                    saveAccountUtils();
                    return;
                }
                saveAccountUtils();
                System.exit(0);
            }
        };
        return actionListener;
    }

    // Effects: Adds all components to the page
    @Override
    protected void addComponents() {
        groupLayout.setHorizontalGroup(
                groupLayout.createSequentialGroup()
                        .addComponent(saveAccountButton)
                        .addComponent(dontSaveAccountButton)
                        .addComponent(warningPicLabel));
    }
}
