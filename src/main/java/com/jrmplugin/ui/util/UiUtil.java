package com.jrmplugin.ui.util;

import javax.swing.*;
import java.awt.*;

public class UiUtil {

    public static JPanel createJPanel() {
        return UiUtil.createJPanel(defaultLayout());
    }

    public static JPanel createJPanel(LayoutManager layout) {
        return new JPanel(layout);
    }

    public static LayoutManager defaultLayout() {
        return new BorderLayout();
    }

}
