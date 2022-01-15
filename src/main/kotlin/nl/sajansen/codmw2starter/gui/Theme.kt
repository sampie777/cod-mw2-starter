package nl.sajansen.codmw2starter.gui

import java.awt.Color
import java.awt.Font
import java.awt.Insets
import javax.swing.UIManager

object Theme {
    val buttonFont = Font("Dialog", Font.PLAIN, 12)
    val primaryButtonFont = Font("Dialog", Font.BOLD, 13)

    val smallFont = Font("Dialog", Font.PLAIN, 10)
    val normalFont = Font("Dialog", Font.PLAIN, 12)
    val boldFont = Font("Dialog", Font.BOLD, 12)
    val italicFont = Font("Dialog", Font.ITALIC, 12)
    val italicBoldFont = Font("Dialog", Font.ITALIC + Font.BOLD, 12)
    val mediumFont = Font("Dialog", Font.PLAIN, 14)

    val defaultPanelColor = Color(237, 237, 237)
    val highlightPanelColor = Color(210, 210, 210)

    init {
        UIManager.put("TabbedPane.selected", Color(250, 250, 250))
        UIManager.put("TabbedPane.contentBorderInsets", Insets(0, 0, 0, 0))
        UIManager.put("TabbedPane.contentOpaque", false)
    }
}