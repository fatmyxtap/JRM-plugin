package com.jrmplugin.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonShortcuts;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.keymap.KeymapUtil;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.ui.popup.ComponentPopupBuilder;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.EditorTextField;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class MainAction extends AnAction {

    private static final Logger LOG = Logger.getInstance(MainAction.class);

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        LOG.info(String.format("Action (%s) has been started.", this.getClass().getName()));

        // all logic here
        preparePopup().showInBestPositionFor(e.getDataContext());
    }

    @NotNull
    private JBPopup preparePopup() {
        EditorTextField myTextField = new EditorTextField("Some text");
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(myTextField, BorderLayout.CENTER);

        ComponentPopupBuilder builder = JBPopupFactory.getInstance().createComponentPopupBuilder(panel, myTextField)
                .setCancelOnClickOutside(true)
                .setAdText(KeymapUtil.getShortcutsText(CommonShortcuts.CTRL_ENTER.getShortcuts()) + " to finish")
                .setRequestFocus(true)
                .setResizable(true)
                .setMayBeParent(true);

        final JBPopup popup = builder.createPopup();
        popup.setMinimumSize(new Dimension(500, 500));
        AnAction okAction = new DumbAwareAction() {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                unregisterCustomShortcutSet(popup.getContent());
                popup.closeOk(e.getInputEvent());
            }
        };
        okAction.registerCustomShortcutSet(CommonShortcuts.CTRL_ENTER, popup.getContent());
        return popup;
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        getTemplatePresentation().setEnabled(true);
    }

    @Override
    public boolean displayTextInToolbar() {
        return true;
    }

    @Override
    public boolean useSmallerFontForTextInToolbar() {
        return true;
    }
}
