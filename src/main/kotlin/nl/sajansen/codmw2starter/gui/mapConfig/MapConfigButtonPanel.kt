package nl.sajansen.codmw2starter.gui.mapConfig

import nl.sajansen.codmw2starter.cod.CoD
import nl.sajansen.codmw2starter.cod.CoDEventListener
import nl.sajansen.codmw2starter.cod.CoDEventListenerSubscriber
import nl.sajansen.codmw2starter.config.Config
import nl.sajansen.codmw2starter.gui.HotKeysMapping
import nl.sajansen.codmw2starter.gui.Theme
import nl.sajansen.codmw2starter.utils.Icon
import nl.sajansen.codmw2starter.utils.addHotKeyMapping
import org.slf4j.LoggerFactory
import java.awt.Dimension
import javax.swing.*
import javax.swing.border.EmptyBorder

class MapConfigButtonPanel(private val frame: JDialog, private val getCommand: (() -> String?)) : JPanel(), CoDEventListener {
    private val logger = LoggerFactory.getLogger(MapConfigButtonPanel::class.java)

    private val pauseLobbyButton = JButton()

    init {
        createGui()

        CoDEventListenerSubscriber.register(this)
    }

    private fun createGui() {
        layout = BoxLayout(this, BoxLayout.LINE_AXIS)
        border = EmptyBorder(0, 10, 10, 10)

        pauseLobbyButton.also {
            it.layout = BoxLayout(it, BoxLayout.LINE_AXIS)
            it.add(
                Icon("\uf04c", size = 10f)
                    .also { icon -> icon.border = EmptyBorder(1, 0, 0, 8) })
            it.add(JLabel("Pause lobby")
                .also { label -> label.font = Theme.buttonFont })

            it.font = Theme.buttonFont
            it.addHotKeyMapping(HotKeysMapping.PauseLobby)
            it.addActionListener { CoD.pauseLobby() }
        }

        val cancelButton = JButton().also {
            it.layout = BoxLayout(it, BoxLayout.LINE_AXIS)
            it.add(
                Icon("\uf00d", size = 10f)
                    .also { icon -> icon.border = EmptyBorder(1, 0, 0, 8) })
            it.add(JLabel("Close")
                .also { label -> label.font = Theme.buttonFont })

            it.font = Theme.buttonFont
            it.addHotKeyMapping(HotKeysMapping.Close)
            it.addActionListener { frame.dispose() }
        }

        val sendButton = JButton().also {
            it.layout = BoxLayout(it, BoxLayout.LINE_AXIS)
            it.add(
                Icon("\uf064", size = 16f)
                    .also { icon -> icon.border = EmptyBorder(1, 0, 0, 8) })
            it.add(JLabel("Apply")
                .also { label -> label.font = Theme.buttonFont })

            it.font = Theme.primaryButtonFont
            it.addActionListener { sendConfig() }
            it.addHotKeyMapping(HotKeysMapping.SendMapConfig)
        }
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