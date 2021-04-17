package nl.sajansen.codmw2starter.gui

import nl.sajansen.codmw2starter.exitApplication
import nl.sajansen.codmw2starter.io.CoD
import nl.sajansen.codmw2starter.utils.addHotKeyMapping
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

        val startServerButton = JButton("Start server")
        startServerButton.addActionListener { startServer() }
        startServerButton.addHotKeyMapping(HotKeysMapping.StartServer)
        frame.rootPane.defaultButton = startServerButton

        val startClientButton = JButton("Start client")
        startClientButton.addActionListener { startClient() }
        startClientButton.addHotKeyMapping(HotKeysMapping.StartClient)

        val quitButton = JButton("Quit")
        quitButton.addActionListener { exitApplication() }
        quitButton.addHotKeyMapping(HotKeysMapping.QuitApplication)

        add(quitButton)
        add(Box.createHorizontalGlue())
        add(Box.createRigidArea(Dimension(10, 0)))
        add(startServerButton)
        add(Box.createRigidArea(Dimension(10, 0)))
        add(startClientButton)
    }

    private fun startServer() {
        if (beforeStart.invoke()) {
            CoD.startServer()
            GUI.serverStarted()
        }
    }

    private fun startClient() {
        if (beforeStart.invoke()) {
            CoD.startClient()
            GUI.clientStarted()
        }
    }
}