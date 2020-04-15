package com.jrmplugin.ui;

import com.intellij.ide.plugins.newui.ColorButton;
import com.intellij.ui.JBColor;

import java.awt.*;

public class PluginModalWindowButton extends ColorButton {

    private static final Color BLUE_COLOR = new JBColor(0x1D73BF, 0x134D80);
    private static final Color FOREGROUND_COLOR = JBColor.namedColor("Plugins.Button.updateForeground", WhiteForeground);
    private static final Color BACKGROUND_COLOR = JBColor.namedColor("Plugins.Button.updateBackground", BLUE_COLOR);
    private static final Color BORDER_COLOR = JBColor.namedColor("Plugins.Button.updateBorderColor", BLUE_COLOR);

    public PluginModalWindowButton(String text) {
        setTextColor(FOREGROUND_COLOR);
        setBgColor(BACKGROUND_COLOR);
        setBorderColor(BORDER_COLOR);

        setText(text);
        setWidth72(this);
    }

}
