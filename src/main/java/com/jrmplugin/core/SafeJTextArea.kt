package com.jrmplugin.core

import javax.swing.JTextArea

class SafeJTextArea(rows: Int, columns: Int) : JTextArea(rows, columns) {
    override fun setText(t: String) {
        synchronized(OBJECT) { super.setText(t) }
    }

    companion object {
        private val OBJECT = Any()
    }
}