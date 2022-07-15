package nl.sajansen.codmw2starter.gui.gamePlayChooser

import nl.sajansen.codmw2starter.gui.other.MapConfigButton
import org.slf4j.LoggerFactory
import javax.swing.BoxLayout
import javax.swing.JPanel
import javax.swing.border.EmptyBorder

class ButtonPanel : JPanel() {
    private val logger = LoggerFactory.getLogger(this::class.java)

    init {
        createGui()
    }

    private fun createGui() {
        layout = BoxLayout(this, BoxLayout.LINE_AXIS)
        border = EmptyBorder(0, 10, 10, 10)

        add(MapConfigButton())
    }
}