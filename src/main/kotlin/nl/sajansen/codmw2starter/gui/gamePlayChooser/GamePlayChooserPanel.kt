package nl.sajansen.codmw2starter.gui.gamePlayChooser

import nl.sajansen.codmw2starter.gui.other.NicknamePanel
import org.slf4j.LoggerFactory
import java.awt.BorderLayout
import javax.swing.JPanel

class GamePlayChooserPanel : JPanel() {
    private val logger = LoggerFactory.getLogger(this::class.java)

    init {
        createGui()
    }

    private fun createGui() {
        layout = BorderLayout(10, 10)

        add(NicknamePanel(), BorderLayout.PAGE_START)
        add(GameModesPanel(), BorderLayout.CENTER)
        add(ButtonPanel(), BorderLayout.PAGE_END)
    }
}