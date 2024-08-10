package ui.page;

import model.exceptions.*;
import ui.GorillaTypeUi;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

// Represents a page where the user can login or signup
public class LoginSignupPage extends Page {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton signupButton;

    // Effects: Creates a LoginSignupPage object
    public LoginSignupPage(GorillaTypeUi controller) {
        super(controller);
    }


    // Modifies: this
    // Effects: initialize components
    @Override
    protected void initComponents() {
        usernameField = new JTextField("",20);

        passwordField = new JPasswordField("",20);

        loginButton = new JButton("Login");
        loginButton.addActionListener(getLoginButtonAction());

        signupButton = new JButton("Signup");
        signupButton.addActionListener(getSignupButton());
    }

    // Effects: Shows a dialog pane that displays the given message.
    private void showMessage(String message) {
        JOptionPane.showMessageDialog(null,message);
    }

    // Effects: Creates and returns the action listener for the signup button.
    private ActionListener getSignupButton() {
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    try {
                        GorillaTypeUi.getAccountUtils().signUp(usernameField.getText(),
                                new String(passwordField.getPassword()));
                        GorillaTypeUi.toInitAfterLoginSignup();
                        GorillaTypeUi.getGorillaTypeUi().setPageType(Pages.TYPING_TEST);
                    } catch (UsernameTakenException ex) {
                        showMessage("Username taken!");
                    } catch (PasswordTooShortException ex) {
                        showMessage("Password is too short!");
                    } catch (UsernameTooShortException ex) {
                        showMessage("Username is too short!");
                    } catch (IOException | UserOrPassWrongException | SettingsException ex) {
                        ex.printStackTrace();
                    }
            }
        };
        return actionListener;
    }

    // Effects: Creates and returns the action listener for the login button.
    private ActionListener getLoginButtonAction() {
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    GorillaTypeUi.getAccountUtils().login(usernameField.getText(),
                            new String(passwordField.getPassword()));
                    GorillaTypeUi.toInitAfterLoginSignup();
                    GorillaTypeUi.getGorillaTypeUi().setPageType(Pages.TYPING_TEST);
                } catch (UserOrPassWrongException ex) {
                    showMessage("Wrong password or username!");
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (SettingsException ex) {
                    throw new RuntimeException(ex);
                }
            }
        };
        return actionListener;
    }

    // Modifies: this
    // Effects: Adds components and formats the ui
    @Override
    protected void addComponents() {
        groupLayout.setHorizontalGroup(
                groupLayout.createSequentialGroup()
                        .addComponent(usernameField)
                        .addComponent(passwordField)
                        .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(signupButton)
                                .addComponent(loginButton)
        ));

        groupLayout.setVerticalGroup(
                groupLayout.createSequentialGroup()
                        .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(usernameField)
                                .addComponent(passwordField)
                                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(signupButton)
                                        .addComponent(loginButton)))
        );
    }
}