package nl.sajansen.codmw2starter.gui

import openWebURL
import org.slf4j.LoggerFactory
import java.awt.Color
import java.awt.Cursor
import javax.swing.BorderFactory
import javax.swing.JButton
import javax.swing.SwingConstants


class ClickableLinkComponent(linkText: String, private val url: String) : JButton() {
    private val logger = LoggerFactory.getLogger(ClickableLinkComponent::class.java.name)

    init {
        text = linkText
        border = BorderFactory.createEmptyBorder()
        isBorderPainted = false
        isContentAreaFilled = false
        isFocusPainted = false
        background = null
        foreground = Color(25, 90, 244)
        horizontalAlignment = SwingConstants.LEFT
        cursor = Cursor(Cursor.HAND_CURSOR)
        toolTipText = url

        addActionListener {
            openWebURL(url)
        }
    }
}