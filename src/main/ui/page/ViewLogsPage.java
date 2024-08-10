package ui.page;

import model.Log;
import ui.GorillaTypeUi;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Represents a page where the user can view their logs
public class ViewLogsPage extends Page implements PreLoadedContentPage {

    private JTextPane logViewPane;
    private JScrollPane logViewScrollArea;
    private JButton switchToAccSettingsButton;
    private JButton updateLogViewPaneButton;
    private JTextField logAccuracyToShow;

    // Effects: Creates a ViewLogsPage object.
    public ViewLogsPage(GorillaTypeUi controller)  {
        super(controller);
    }

    // Effects: Initializes all the components
    @Override
    protected void initComponents() {
        switchToAccSettingsButton = new JButton("Back");
        switchToAccSettingsButton.addActionListener(getSwitchToAccSettingsButtonAction());

        updateLogViewPaneButton = new JButton("Update Log View");
        updateLogViewPaneButton.addActionListener(getUpdateLogViewPaneButtonAction());

        logAccuracyToShow = new JTextField();
        logAccuracyToShow.setColumns(20);
        logAccuracyToShow.setText("Enter number here to filter accuracies.");

        logViewPane = new JTextPane();
        logViewPane.setEditable(false);
        logViewPane.setPreferredSize(new Dimension(300, 200));
        logViewPane.setSize(300, 200);

        logViewScrollArea = new JScrollPane(logViewPane);
        logViewScrollArea.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        logViewScrollArea.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }

    // Effects: Returns an ActionListener that runs the filterLogsDisplay() method
    public ActionListener getUpdateLogViewPaneButtonAction() {
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    logViewPane.setText("");
                    Document doc = logViewPane.getStyledDocument();
                    SimpleAttributeSet noAnswerCharAttribute = new SimpleAttributeSet();
                    StyleConstants.setForeground(noAnswerCharAttribute, Color.BLACK);
                    StyleConstants.setBackground(noAnswerCharAttribute, Color.WHITE);
                    doc.insertString(doc.getLength(),
                            GorillaTypeUi.getAccountUtils()
                                    .filterLogsDisplay(Integer.parseInt(logAccuracyToShow.getText()),
                                            GorillaTypeUi.getAccountUtils().getAccountLoggedIn()),
                            noAnswerCharAttribute);
                } catch (BadLocationException ex) {
                    throw new RuntimeException(ex);
                }
            }
        };
        return actionListener;
    }

    // Effects: Returns an ActionListener that sets the page to ACCOUNT_SETTINGS
    public ActionListener getSwitchToAccSettingsButtonAction() {
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GorillaTypeUi.getGorillaTypeUi().setPageType(Pages.ACCOUNT_SETTINGS);
            }
        };
        return actionListener;
    }

    // Effects: adds components to the page.
    @Override
    protected void addComponents() {
        groupLayout.setHorizontalGroup(
                groupLayout.createSequentialGroup()
                        .addComponent(logViewScrollArea)
                        .addComponent(switchToAccSettingsButton)
                        .addComponent(logAccuracyToShow)
                        .addComponent(updateLogViewPaneButton));
    }

    // Effects: Sets up text when this method is run. Should be run before switching to this page.
    @Override
    public void runBeforeSwitch() {
        logViewPane.setText(GorillaTypeUi.getAccountUtils()
                .printLogs(GorillaTypeUi.getAccountUtils().getAccountLoggedIn()));
    }
}
