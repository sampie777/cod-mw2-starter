package nl.sajansen.codmw2starter.gui

import nl.sajansen.codmw2starter.cod.CoD
import nl.sajansen.codmw2starter.exitApplication
import nl.sajansen.codmw2starter.gui.mapConfig.MapConfigFrame
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

        val mapConfigButton = JButton("Maps")
        mapConfigButton.addActionListener { MapConfigFrame.createAndShow(getMainFrameComponent(this)) }
        mapConfigButton.addHotKeyMapping(HotKeysMapping.OpenMap)
        mapConfigButton.font = Theme.buttonFont

        val startServerButton = JButton("Start server")
        startServerButton.addActionListener { startServer() }
        startServerButton.addHotKeyMapping(HotKeysMapping.StartServer)
        startServerButton.font = Theme.buttonFont

        val startClientButton = JButton("Start client")
        startClientButton.addActionListener { startClient() }
        startClientButton.addHotKeyMapping(HotKeysMapping.StartClient)
        startClientButton.font = Theme.primaryButtonFont
        frame.rootPane.defaultButton = startClientButton

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