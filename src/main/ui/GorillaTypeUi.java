package ui;

import model.*;
import model.exceptions.SettingsException;
import model.exceptions.UserOrPassWrongException;
import model.persistence.JsonReader;
import model.persistence.JsonWriter;
import ui.page.*;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.Iterator;

// Represents the GorillaType app
public class GorillaTypeUi extends JFrame {

    JFrame promptSettingsframe;
    JFrame saveAccountFrame;

    private static AccountUtils accountUtils;
    private static TypingTestUtils typingTestUtils;

    public static final int WIDTH = 600;
    public static final int HEIGHT = 400;
    private static GorillaTypeUi gorillaTypeUi;
    private static JPanel currentPage;

    private static LoginSignupPage loginSignupPage;
    private static TypingTestPage typingTestPage;
    private static PromptSettingsPage promptSettingsPage;
    private static AccountSettingsPage accountSettingsPage;
    private static SaveAccountPage saveAccountPage;
    private static ViewLogsPage viewLogsPage;

    private static JsonReader jsonReader = new JsonReader("data/");
    private static JsonWriter jsonWriter = new JsonWriter(jsonReader.getsavedFileLocation());

    private static Account backupAccount;

    // Modifies: this
    // Effects: Creates a new GorillaTypeUi, also adds a shutdown hook.
    // Code for shutdown hook from
    // https://stackoverflow.com/questions/2921945/useful-example-of-a-shutdown-hook-in-java
    public static void main(String[] args) throws IOException, UserOrPassWrongException, SettingsException {
        gorillaTypeUi = new GorillaTypeUi();
        initGorillaUiApp();
        gorillaTypeUi.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        gorillaTypeUi.addWindowListener(getWindowExitListener());
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                Iterator<Event> eventLogIterator = EventLog.getInstance().iterator();
                while (eventLogIterator.hasNext()) {
                    System.out.println(eventLogIterator.next().toString());
                }
            }
        });
    }

    // Effects: Creates a new gorilla type ui
    public GorillaTypeUi() {
        super.setTitle("Gorilla Type");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        loadPages();
    }

    // Effects: Returns a new window exit listener that prompts the user if they want to save their account data.
    @SuppressWarnings("methodlength")
    private static WindowListener getWindowExitListener() {
        WindowListener windowListener = new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                    gorillaTypeUi.setPageType(Pages.EXIT_SAVE_ACCOUNT);

            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        };
        return windowListener;
    }

    // Effects: Initializes the GorillaAppUi
    public static void initGorillaUiApp() throws IOException, UserOrPassWrongException, SettingsException {
        try {
            accountUtils = jsonReader.parseAccountUtilsJson();
        } catch (IOException ioException) {
            accountUtils = new AccountUtils();
        }
        typingTestUtils = new TypingTestUtils(true);
        if (accountUtils.getAccountLoggedIn() != null) {
            String username = accountUtils.getAccountLoggedIn().getUsername();
            String password = accountUtils.getAccountLoggedIn().getPassword();
            accountUtils.login(username, password);
            GorillaTypeUi.toInitAfterLoginSignup();
            gorillaTypeUi.setPageType(Pages.TYPING_TEST);
            gorillaTypeUi.setVisible(true);
            return;
        }
        gorillaTypeUi.setPageType(Pages.LOGIN_SIGNUP);
        gorillaTypeUi.setVisible(true);
    }

    public static JsonWriter getJsonWriter() {
        return jsonWriter;
    }

    // Modifies: this
    // Effects: Runs code that will set display the pageType requested
    @SuppressWarnings("methodlength")
    public void setPageType(Pages pageType) {
        switch (pageType) {
            case LOGIN_SIGNUP:
                gorillaTypeUi.setPage(loginSignupPage);
                break;
            case TYPING_TEST:
                gorillaTypeUi.setPage(typingTestPage);
                typingTestPage.runBeforeSwitch();
                break;
            case PROMPT_SETTINGS:
                if (promptSettingsframe != null && promptSettingsframe.isVisible()) {
                    promptSettingsframe.toFront();
                    return;
                }
                promptSettingsframe = new JFrame();
                promptSettingsframe.setContentPane(promptSettingsPage);
                promptSettingsPage.runBeforeSwitch();
                promptSettingsframe.setVisible(true);
                break;
            case ACCOUNT_SETTINGS:
                gorillaTypeUi.setPage(accountSettingsPage);
                accountSettingsPage.runBeforeSwitch();
                break;
            case LOGOUT_SAVE_ACCOUNT:
                if (saveAccountFrame != null && saveAccountFrame.isVisible()) {
                    saveAccountFrame.toFront();
                    return;
                }
                saveAccountFrame = new JFrame();
                saveAccountPage.setIsLoggingOut(true);
                saveAccountFrame.setContentPane(saveAccountPage);
                saveAccountFrame.setVisible(true);
                break;
            case EXIT_SAVE_ACCOUNT:
                if (accountUtils.getAccountLoggedIn() == null) {
                    System.exit(0);
                }
                if (saveAccountFrame != null && saveAccountFrame.isVisible()) {
                    saveAccountFrame.toFront();
                    return;
                }
                saveAccountFrame = new JFrame();
                saveAccountPage.setIsLoggingOut(false);
                saveAccountFrame.setContentPane(saveAccountPage);
                saveAccountFrame.setVisible(true);
                break;
            case VIEW_LOGS:
                gorillaTypeUi.setPage(viewLogsPage);
                viewLogsPage.runBeforeSwitch();
            default:
                break;
        }
    }

    // Effects: Returns the gorillaTypeUI
    public static GorillaTypeUi getGorillaTypeUi() {
        return gorillaTypeUi;
    }

    // Modifies: this
    // Effects: Sets the main frame to a given page
    private void setPage(JPanel page) {
        add(page);
        if (currentPage != null) {
            currentPage.setVisible(false);
        }
        currentPage = page;
        page.setVisible(true);
    }

    // Modifies: this
    // Effects: Loads all the pages the user may traverse through
    private void loadPages() {
        loginSignupPage = new LoginSignupPage(this);
        loginSignupPage.setVisible(true);

        typingTestPage = new TypingTestPage(this);
        typingTestPage.setVisible(true);

        promptSettingsPage = new PromptSettingsPage(this);
        promptSettingsPage.setVisible(true);

        accountSettingsPage = new AccountSettingsPage(this);
        accountSettingsPage.setVisible(true);

        saveAccountPage = new SaveAccountPage(this);
        saveAccountPage.setVisible(true);

        viewLogsPage = new ViewLogsPage(this);
        viewLogsPage.setVisible(true);
    }

    // Effects: Got code from https://stackoverflow.com/questions/1234912/how-to-programmatically-close-a-jframe to
    // Close a JFrame programatically
    public void exitSaveAccountFrame() {
        saveAccountFrame.dispatchEvent(new WindowEvent(saveAccountFrame, WindowEvent.WINDOW_CLOSING));
    }

    public static AccountUtils getAccountUtils() {
        return accountUtils;
    }

    // Modifies: this
    // Effects: Initializes the typingTestUtils
    public static void toInitAfterLoginSignup() throws IOException, UserOrPassWrongException, SettingsException {
        typingTestUtils = new TypingTestUtils(true);
        jsonWriter.open();
        jsonWriter.saveAccountUtilsObject(accountUtils);
        jsonWriter.close();
        backupAccount = jsonReader.parseAccountUtilsJson().getAccountLoggedIn();
    }

    public static TypingTestUtils getTypingTestUtils() {
        return typingTestUtils;
    }

    public static Account getBackupAccount() {
        return backupAccount;
    }


}
