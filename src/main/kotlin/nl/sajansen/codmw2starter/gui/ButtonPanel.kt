package nl.sajansen.codmw2starter.gui

import nl.sajansen.codmw2starter.cod.CoD
import nl.sajansen.codmw2starter.exitApplication
import nl.sajansen.codmw2starter.gui.mapConfig.MapConfigFrame
import nl.sajansen.codmw2starter.utils.Icon
import nl.sajansen.codmw2starter.utils.addHotKeyMapping
import nl.sajansen.codmw2starter.utils.getMainFrameComponent
import org.slf4j.LoggerFactory
import java.awt.Dimension
import javax.swing.*
import javax.swing.border.EmptyBorder

class ButtonPanel(private val frame: JFrame, private val beforeStart: (() -> Boolean)) : JPanel() {
    private val logger = LoggerFactory.getLogger(ButtonPanel::class.java)

    init {
        createGui()
    }

    private fun createGui() {
        layout = BoxLayout(this, BoxLayout.LINE_AXIS)
        border = EmptyBorder(0, 10, 10, 10)

        val mapConfigButton = JButton().also {
            it.layout = BoxLayout(it, BoxLayout.LINE_AXIS)
            it.add(Icon("\uf013", size = 14f)
                .also { icon -> icon.border = EmptyBorder(1, 0, 0, 8) })
            it.add(JLabel("Maps")
                .also { label -> label.font = Theme.buttonFont })

            it.addActionListener { MapConfigFrame.createAndShow(getMainFrameComponent(this)) }
            it.addHotKeyMapping(HotKeysMapping.OpenMap)
        }

        val startServerButton = JButton().also {
            it.layout = BoxLayout(it, BoxLayout.LINE_AXIS)
            it.add(Icon("\uf5fc")
                .also { icon -> icon.border = EmptyBorder(3, 0, 0, 8) })
            it.add(JLabel("Start server")
                .also { label -> label.font = Theme.buttonFont })

            it.addActionListener { startServer() }
            it.addHotKeyMapping(HotKeysMapping.StartServer)
        }

        val startClientButton = JButton().also {
            it.layout = BoxLayout(it, BoxLayout.LINE_AXIS)
            it.add(Icon("\uf04b", size = 18f)
                .also { icon -> icon.border = EmptyBorder(3, 0, 0, 8) })
            it.add(JLabel("Play game")
                .also { label -> label.font = Theme.primaryButtonFont })

            it.addActionListener { startClient() }
            it.addHotKeyMapping(HotKeysMapping.StartClient)
            frame.rootPane.defaultButton = it
        }

        val quitButton = JButton("Quit")
        quitButton.addActionListener { exitApplication() }
        quitButton.addHotKeyMapping(HotKeysMapping.QuitApplication)

//        add(quitButton)
        add(mapConfigButton)
        add(Box.createHorizontalGlue())
        add(Box.createRigidArea(Dimension(50, 0)))
        add(startServerButton)
        add(Box.createRigidArea(Dimension(10, 0)))
        add(startClientButton)
    }

    private fun startServer() {
        if (beforeStart.invoke()) {
            CoD.startServer()
        }
    }

    fun startClient() {
        if (beforeStart.invoke()) {
            CoD.startClient()
        }
    }
}