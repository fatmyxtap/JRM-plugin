package com.jrmplugin.util

import java.awt.GridBagLayout
import java.awt.LayoutManager
import javax.swing.JPanel

object UiUtil {
    fun createJPanel(): JPanel = JPanel(GridBagLayout())
}