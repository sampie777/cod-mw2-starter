package nl.sajansen.codmw2starter.gui.mapConfig

import nl.sajansen.codmw2starter.config.Config
import nl.sajansen.codmw2starter.gui.HotKeysMapping
import nl.sajansen.codmw2starter.io.CoD
import nl.sajansen.codmw2starter.utils.addHotKeyMapping
import org.slf4j.LoggerFactory
import java.awt.Dimension
import javax.swing.*
import javax.swing.border.EmptyBorder

class MapConfigButtonPanel(private val frame: JDialog, private val getCommand: (() -> String?)) : JPanel() {
    private val logger = LoggerFactory.getLogger(MapConfigButtonPanel::class.java)

    init {
        createGui()
    }

    private fun createGui() {
        layout = BoxLayout(this, BoxLayout.LINE_AXIS)
        border = EmptyBorder(0, 10, 10, 10)

        val sendButton = JButton("Send")
        sendButton.addActionListener { sendConfig() }
        sendButton.addHotKeyMapping(HotKeysMapping.SendMapConfig)
        frame.rootPane.defaultButton = sendButton

        val cancelButton = JButton("Cancel")
        cancelButton.addHotKeyMapping(HotKeysMapping.CloseButton)
        cancelButton.addActionListener { frame.dispose() }

        add(cancelButton)
        add(Box.createRigidArea(Dimension(10, 0)))
        add(sendButton)
    }

    private fun sendConfig() {
        val command = getCommand.invoke() ?: return
        Config.save()
        logger.info("Sending commands: $command")
        CoD.sendCommand(command)
    }
}