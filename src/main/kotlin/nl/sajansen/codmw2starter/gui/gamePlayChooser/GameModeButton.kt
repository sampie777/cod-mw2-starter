package nl.sajansen.codmw2starter.gui.gamePlayChooser

import org.slf4j.LoggerFactory
import java.awt.Dimension
import java.awt.event.ActionEvent
import javax.swing.JButton

class GameModeButton(text: String, private val onClick: ((e: ActionEvent) -> Unit)) : JButton(text) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    init {
        createGui()
    }

    private fun createGui() {
        preferredSize = Dimension(272, 290)
        addActionListener(onClick)
    }
}