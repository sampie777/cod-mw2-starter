package nl.sajansen.codmw2starter.gui.nickname

import nl.sajansen.codmw2starter.cod.CoD
import nl.sajansen.codmw2starter.cod.CoDEventListener
import nl.sajansen.codmw2starter.cod.CoDEventListenerSubscriber
import nl.sajansen.codmw2starter.gui.Theme
import nl.sajansen.codmw2starter.gui.mainFrame.MainFrame
import nl.sajansen.codmw2starter.gui.notifications.Notifications
import nl.sajansen.codmw2starter.utils.ValidationError
import org.slf4j.LoggerFactory
import java.awt.Color
import java.awt.Cursor
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.*
import javax.swing.border.EmptyBorder

class NicknamePanel : JPanel(), CoDEventListener {
    private val logger = LoggerFactory.getLogger(NicknamePanel::class.java)

    private val nicknameLabel = JLabel("unknown nickname")

    init {
        CoDEventListenerSubscriber.register(this)

        createGui()
        onNicknameChanged()
    }

    private fun createGui() {
        layout = BoxLayout(this, BoxLayout.LINE_AXIS)
        border = BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
            EmptyBorder(10, 25, 10, 10)
        )
        background = Theme.highlightPanelColor

        val textLabel = JLabel("Hello ")
        textLabel.toolTipText = "Click to set nickname"
        textLabel.font = Theme.italicFont
        textLabel.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        textLabel.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                showNicknameModal()
            }
        })

        nicknameLabel.toolTipText = "Click to set nickname"
        nicknameLabel.font = Theme.italicBoldFont
        nicknameLabel.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        nicknameLabel.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                showNicknameModal()
            }
        })

        val instructionsLabel = JLabel(", click any IP to set it as host.")
        instructionsLabel.font = Theme.italicFont

        add(textLabel)
        add(nicknameLabel)
        add(instructionsLabel)
    }

    private fun showNicknameModal() {
        logger.info("Showing modal to change nickname")

        val current = nicknameLabel.text
        val new = JOptionPane.showInputDialog(MainFrame.getInstance(), "Enter your new nickname", current)
            ?.trim()
            ?: return

        try {
            nl.sajansen.codmw2starter.utils.validate(new.length >= 2, "Nickname must be between 2 and 16 characters.")
            nl.sajansen.codmw2starter.utils.validate(new.length <= 16, "Nickname must be between 2 and 16 characters.")
        } catch (e: ValidationError) {
            Notifications.popup("Please enter a valid nickname.\n${e.message}", "Nickname")
            return
        }

        CoD.setNickname(new)
    }

    override fun onNicknameChanged() {
        nicknameLabel.text = CoD.getNickname()
    }
}