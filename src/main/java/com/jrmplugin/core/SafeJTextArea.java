package com.jrmplugin.core;

import javax.swing.*;

public class SafeJTextArea extends JTextArea {

    private static final Object OBJECT = new Object();

    public SafeJTextArea(int rows, int columns) {
        super(rows, columns);
    }

    @Override
    public void setText(String t) {
        synchronized (OBJECT) {
            super.setText(t);
        }
    }
}
