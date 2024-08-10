package ui.page;

import model.Log;
import model.MarkedChar;
import model.Prompt;
import model.UserResults;
import ui.GorillaTypeUi;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;

// Represents a page where the user can test their typing skills.
public class TypingTestPage extends Page implements PreLoadedContentPage {

    private boolean isTypingForTheFirstTime;

    private JTextPane typingTextPane;
    private JTextPane displayTextPane;

    private JTextField wpmField;
    private JTextField accuracyField;

    private JScrollPane typingTextScrollArea;
    private JScrollPane displayTextScrollArea;

    private JButton newTextButton;
    private JButton promptSettingsButton;
    private JButton accountSettingsButton;

    private SimpleAttributeSet wrongCharAttribute;
    private SimpleAttributeSet correctCharAttribute;
    private SimpleAttributeSet noAnswerCharAttribute;

    private UserResults currentUserResults;
    private Prompt currentPrompt;

    // Effects: Creates a TypingTestPage object
    public TypingTestPage(GorillaTypeUi controller) {
        super(controller);
    }

    // Modifies: this
    // Effects: initialize components
    @SuppressWarnings("methodlength")
    @Override
    protected void initComponents() {
        wrongCharAttribute = new SimpleAttributeSet();
        correctCharAttribute = new SimpleAttributeSet();
        noAnswerCharAttribute = new SimpleAttributeSet();

        StyleConstants.setForeground(wrongCharAttribute, Color.BLACK);
        StyleConstants.setBackground(wrongCharAttribute, Color.RED);

        StyleConstants.setForeground(correctCharAttribute, Color.BLACK);
        StyleConstants.setBackground(correctCharAttribute, Color.GREEN);

        StyleConstants.setForeground(noAnswerCharAttribute, Color.BLACK);
        StyleConstants.setBackground(noAnswerCharAttribute, Color.WHITE);

        newTextButton = new JButton("Generate New Text");
        newTextButton.addActionListener(getNewTextButtonAction());

        promptSettingsButton = new JButton("Open Prompt Settings");
        promptSettingsButton.addActionListener(getPromptSettingsButtonAction());

        accountSettingsButton = new JButton("Account Settings");
        accountSettingsButton.addActionListener(getAccountSettingsButtonAction());

        typingTextPane = new JTextPane();
        typingTextPane.setPreferredSize(new Dimension(300, 200));
        typingTextPane.setSize(300, 200);

        displayTextPane = new JTextPane();
        displayTextPane.setEditable(false);
        displayTextPane.setPreferredSize(new Dimension(300, 200));
        displayTextPane.setSize(300, 200);

        typingTextScrollArea = new JScrollPane(typingTextPane);
        typingTextScrollArea.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        typingTextScrollArea.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        typingTextPane.addKeyListener(getCustomKeyListener());
        typingTextPane.addMouseListener(getNoMouseTraversalListener());
        typingTextPane.addMouseMotionListener(getNoMouseSelectionListener());
        typingTextPane.setTransferHandler(null);
        typingTextPane.setHighlighter(null);

        displayTextScrollArea = new JScrollPane(displayTextPane);
        displayTextScrollArea.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        displayTextScrollArea.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        displayTextScrollArea.setFocusable(false);

        wpmField = new JTextField("WPM: ", 10);
        accuracyField = new JTextField("Accuracy: ", 10);
    }

    // Effects: This method is to be run before you switch to this page so a prompt is automatically generated.
    @Override
    public void runBeforeSwitch() {
        generateAndDisplayNewPrompt(false);
    }

    public ActionListener getAccountSettingsButtonAction() {
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GorillaTypeUi.getGorillaTypeUi().setPageType(Pages.ACCOUNT_SETTINGS);
            }
        };
        return actionListener;
    }

    // Effects: Returns an action listener that runs the generateAndDisplayNewPrompt method when run
    private ActionListener getNewTextButtonAction() {
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateAndDisplayNewPrompt(false);
            }
        };
        return actionListener;
    }

    // Effects: Returns an action listener that will open up a new window containing the prompt settings
    private ActionListener getPromptSettingsButtonAction() {
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GorillaTypeUi.getGorillaTypeUi().setPageType(Pages.PROMPT_SETTINGS);
            }
        };
        return actionListener;
    }

    // Effects: Logs the current prompt and the current userResults
    private void logResults() {
        Log loggedResult = new Log(currentPrompt, currentUserResults);
        GorillaTypeUi.getAccountUtils().getAccountLoggedIn().addLog(loggedResult);
    }

    // Modifies: this, currentPrompt, currentUserResults
    // Effects: Creates and returns the action listener for the new text button which will generate a new prompt
    // when called. Will set the currentPrompt to the generated prompt and initializes the currentUserResults object
    // Will log the currentPrompt and currentUserResults if the user completed the typing test to the very end.
    private void generateAndDisplayNewPrompt(boolean completedTest) {
        try {
            if (!(currentPrompt == null && currentUserResults == null) && completedTest) {
                logResults();
            }
            typingTextPane.setText("");
            currentUserResults = new UserResults();
            currentPrompt = GorillaTypeUi.getTypingTestUtils()
                    .generatePrompt(GorillaTypeUi.getAccountUtils().getAccountLoggedIn().getSettings());
            currentUserResults.updateUserResults(currentPrompt, "");
            updateDisplayTextPane(GorillaTypeUi.getTypingTestUtils().generateErrorLocations(currentUserResults,
                            currentPrompt));

            isTypingForTheFirstTime = true;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Modifies: this
    // Will update the displayTextPane to correctly display and show users correct and wrong typed characters.
    // This also includes correctly placing the caret so the user can see what text to type next
    private void updateDisplayTextPane(ArrayList<MarkedChar> markedChars) {
        clearDisplayTextPane();
        Document doc = displayTextPane.getStyledDocument();
        for (MarkedChar markedChar: markedChars) {
            try {
                switch (markedChar.getCharType()) {
                    case CORRECT:
                        doc.insertString(doc.getLength(), markedChar.getCharacter() + "", correctCharAttribute);
                        break;
                    case WRONG:
                        doc.insertString(doc.getLength(), markedChar.getCharacter() + "", wrongCharAttribute);
                        break;
                    case UNMARKED:
                        doc.insertString(doc.getLength(), markedChar.getCharacter() + "", noAnswerCharAttribute);
                        break;
                }
            } catch (BadLocationException ex) {
                throw new RuntimeException(ex);
            }
        }
        displayTextPane.setCaretPosition(typingTextPane.getCaretPosition());
    }

    // Modifies: this
    // Effects: Clears the displayTextPane
    private void clearDisplayTextPane() {
        displayTextPane.setText("");
    }

    // Modifies: this
    // Effects: Returns a custom MouseMotionListener. It makes sure that the user can never highlight and text
    private MouseMotionListener getNoMouseSelectionListener() {
        MouseMotionListener noMouseSelection = new MouseMotionListener() {
            // Modifies: this
            // Effects: Ignores user attempts to highlight text
            @Override
            public void mouseDragged(MouseEvent e) {
                e.consume();
                typingTextPane.setCaretPosition(typingTextPane.getText().length());
            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        };
        return noMouseSelection;
    }

    // Modifies: this
    // Effects: Ensures that the user can never change the caret position of the typing text pane
    private MouseListener getNoMouseTraversalListener() {
        MouseListener noMouseTraversal = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            // Modifies: this
            // Effects: Ignores any user attempts to change the carret position of the typingTextPane
            @Override
            public void mousePressed(MouseEvent e) {
                typingTextPane.setCaretPosition(typingTextPane.getText().length());
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        };
        return noMouseTraversal;
    }

    // Modifies: this
    // Effects: Updates the ui whenever the user types. Updates the words per minute, accuracy. Also ignores
    // unwanted user inputs.
    // Also updates the currentUserResults
    // so that correct statistical data can be obtained.
    @SuppressWarnings("methodlength")
    private KeyListener getCustomKeyListener() {
        KeyListener noKeyTraversal = new KeyListener() {
            // Modifies: this
            // Effects: Updates the wpm and accuracy labels.
            @Override
            public void keyTyped(KeyEvent e) {
                if (typingTextPane.getText().length() < currentPrompt.getActualLength() - 1) {
                    currentUserResults.updateUserResults(currentPrompt, typingTextPane.getText() + e.getKeyChar());
                } else {
                    e.consume();
                    currentUserResults.updateUserResults(currentPrompt, typingTextPane.getText() + e.getKeyChar());
                    generateAndDisplayNewPrompt(true);
                    return;
                }

                wpmField.setText("WPM: " + currentUserResults.getWpm());
                accuracyField.setText("Accuracy: " + currentUserResults.getAccuracy());
            }

            // Modifies: currentUserResults
            // Effects: Ignores any attempts to travers the typingTextPane.
            // This also sets the start time of the user results if its the users first time typing
            // so that the wpm remains accurate.
            @Override
            public void keyPressed(KeyEvent e) {
                if (isTypingForTheFirstTime) {
                    currentUserResults.setStartTime(new Time(System.currentTimeMillis()));
                    isTypingForTheFirstTime = false;
                }

                if (e.getKeyCode() == KeyEvent.VK_DOWN
                        || e.getKeyCode() == KeyEvent.VK_UP
                        || e.getKeyCode() == KeyEvent.VK_LEFT
                        || e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    e.consume();
                }


                // Gets a marked char array and displays it.
                ArrayList<MarkedChar> markedCharArray;

                    if (e.getKeyChar() != KeyEvent.VK_BACK_SPACE) {
                        currentUserResults.updateUserResults(currentPrompt, typingTextPane.getText() + e.getKeyChar());
                    } else {
                        if (typingTextPane.getText().length() > 0) {
                            currentUserResults.updateUserResults(currentPrompt, typingTextPane.getText().substring(0,
                                    typingTextPane.getText().length() - 1));
                        } else {
                            currentUserResults.updateUserResults(currentPrompt, typingTextPane.getText());
                        }
                    }

                markedCharArray = GorillaTypeUi.getTypingTestUtils()
                        .generateErrorLocations(currentUserResults,
                                currentPrompt);
                updateDisplayTextPane(markedCharArray);
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        };
        return noKeyTraversal;
    }

    // Modifies: this
    // Effects: Adds components and formats the ui
    @Override
    protected void addComponents() {
        groupLayout.setHorizontalGroup(
                groupLayout.createSequentialGroup()
                        .addComponent(typingTextScrollArea)
                        .addComponent(displayTextScrollArea)
                        .addComponent(newTextButton)
                        .addComponent(wpmField)
                        .addComponent(accuracyField)
                        .addComponent(promptSettingsButton)
                        .addComponent(accountSettingsButton));
    }
}
