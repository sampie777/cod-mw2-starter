package nl.sajansen.codmw2starter.gui.nickname

import nl.sajansen.codmw2starter.cod.CoD
import nl.sajansen.codmw2starter.gui.Theme
import org.slf4j.LoggerFactory
import java.awt.Color
import javax.swing.BorderFactory
import javax.swing.BoxLayout
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.border.EmptyBorder

class NicknamePanel : JPanel() {
    private val logger = LoggerFactory.getLogger(NicknamePanel::class.java)

    init {
        createGui()
    }

    private fun createGui() {
        layout = BoxLayout(this, BoxLayout.LINE_AXIS)
        border = BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
            EmptyBorder(10, 25, 10, 10)
        )
        background = Theme.highlightPanelColor

        val textLabel = JLabel("Hello ")
        textLabel.font = Theme.italicFont

        val nicknameLabel = JLabel(CoD.getNickname())
        nicknameLabel.font = Theme.italicBoldFont

        val instructionsLabel = JLabel(", click any IP to set it as host.")
        instructionsLabel.font = Theme.italicFont

        add(textLabel)
        add(nicknameLabel)
        add(instructionsLabel)
    }
}