package com.jrmplugin.ui

import com.intellij.ide.plugins.newui.ColorButton
import com.intellij.ui.JBColor
import java.awt.Color
import java.awt.Dimension
import java.awt.event.MouseListener

open class PluginModalWindowButton(text: String) : ColorButton() {

    @Synchronized
    override fun addMouseListener(l: MouseListener) {
        val mouseListeners = mouseListeners
        for (listener in mouseListeners) {
            if (listener.javaClass == l.javaClass) {
                return
            }
        }
        super.addMouseListener(l)
    }

    companion object {
        private val BLUE_COLOR: Color = JBColor(0x1D73BF, 0x134D80)
        private val FOREGROUND_COLOR: Color = JBColor.namedColor("Plugins.Button.updateForeground", WhiteForeground)
        private val BACKGROUND_COLOR: Color = JBColor.namedColor("Plugins.Button.updateBackground", BLUE_COLOR)
        private val BORDER_COLOR: Color = JBColor.namedColor("Plugins.Button.updateBorderColor", BLUE_COLOR)
    }

    init {
        setTextColor(FOREGROUND_COLOR)
        setBgColor(BACKGROUND_COLOR)
        setBorderColor(BORDER_COLOR)
        this.text = text
        setWidth72(this)
        preferredSize = Dimension(200, 40)
    }
}