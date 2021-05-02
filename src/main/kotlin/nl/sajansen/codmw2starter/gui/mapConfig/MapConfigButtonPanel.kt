package nl.sajansen.codmw2starter.gui.mapConfig

import nl.sajansen.codmw2starter.cod.CoD
import nl.sajansen.codmw2starter.cod.CoDEventListener
import nl.sajansen.codmw2starter.cod.CoDEventListenerSubscriber
import nl.sajansen.codmw2starter.config.Config
import nl.sajansen.codmw2starter.gui.HotKeysMapping
import nl.sajansen.codmw2starter.gui.Theme
import nl.sajansen.codmw2starter.utils.addHotKeyMapping
import org.slf4j.LoggerFactory
import java.awt.Dimension
import javax.swing.*
import javax.swing.border.EmptyBorder

class MapConfigButtonPanel(private val frame: JDialog, private val getCommand: (() -> String?)) : JPanel(), CoDEventListener {
    private val logger = LoggerFactory.getLogger(MapConfigButtonPanel::class.java)

    private val pauseLobbyButton = JButton("Pause")

    init {
        createGui()

        CoDEventListenerSubscriber.register(this)
    }

    private fun createGui() {
        layout = BoxLayout(this, BoxLayout.LINE_AXIS)
        border = EmptyBorder(0, 10, 10, 10)

        pauseLobbyButton.addHotKeyMapping(HotKeysMapping.PauseLobby)
        pauseLobbyButton.addActionListener { CoD.pauseLobby() }
        pauseLobbyButton.font = Theme.buttonFont

        val cancelButton = JButton("Cancel")
        cancelButton.addHotKeyMapping(HotKeysMapping.Close)
        cancelButton.addActionListener { frame.dispose() }
        cancelButton.font = Theme.buttonFont

        val sendButton = JButton("Send")
        sendButton.addActionListener { sendConfig() }
        sendButton.addHotKeyMapping(HotKeysMapping.SendMapConfig)
        sendButton.font = Theme.primaryButtonFont
        frame.rootPane.defaultButton = sendButton

        add(cancelButton)
        add(Box.createHorizontalGlue())
        add(Box.createRigidArea(Dimension(10, 0)))
        add(pauseLobbyButton)
        add(Box.createRigidArea(Dimension(10, 0)))
        add(sendButton)
    }

    private fun sendConfig() {
        val command = getCommand.invoke() ?: return
        Config.save()
        logger.info("Sending commands: $command")
        CoD.sendCommand(command)
        CoD.isLobbyPaused = false
    }

    override fun onPauseChanged() {
        pauseLobbyButton.text = if (CoD.isLobbyPaused) "Unpause" else "Pause"
    }
}