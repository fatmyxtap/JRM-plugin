package com.jrmplugin.ui;

import javax.swing.*;
import java.awt.*;

public class PluginModalWindowProcessableButton extends PluginModalWindowButton {

    private final JProgressBar progressBar;

    public PluginModalWindowProcessableButton(String text) {
        super(text);

        this.progressBar = new JProgressBar();
        this.progressBar.setIndeterminate(true);
        this.progressBar.setToolTipText("Wait please.");
        this.progressBar.setPreferredSize(new Dimension(200, 15));

        this.progressBar.setVisible(false);
    }

    public JProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgress(boolean inProgress) {
        if (inProgress) {
            progressBar.setVisible(true);
            this.setVisible(false);
        } else {
            progressBar.setVisible(false);
            this.setVisible(true);
        }
    }

}
