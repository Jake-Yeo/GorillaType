package ui.page;

import ui.GorillaTypeUi;

import javax.swing.*;

// Represents a generic page
abstract class Page extends JPanel {

    private final GorillaTypeUi controller;
    protected GroupLayout groupLayout;

    //REQUIRES: GorillaTypeUi controller that holds this tab
    public Page(GorillaTypeUi controller) {
        this.controller = controller;
        this.setSize(GorillaTypeUi.WIDTH, GorillaTypeUi.HEIGHT);
        groupLayout = new GroupLayout(this);
        initComponents();
        addComponents();
    }

    //EFFECTS: returns the GorillaTypeUi controller for this tab
    public GorillaTypeUi getController() {
        return controller;
    }

    protected abstract void initComponents();

    protected abstract void addComponents();
}
