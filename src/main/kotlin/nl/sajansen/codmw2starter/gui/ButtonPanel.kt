package nl.sajansen.codmw2starter.gui

import nl.sajansen.codmw2starter.config.Config
import nl.sajansen.codmw2starter.exitApplication
import nl.sajansen.codmw2starter.gui.mapConfig.MapConfigFrame
import nl.sajansen.codmw2starter.io.CoD
import nl.sajansen.codmw2starter.utils.addHotKeyMapping
import nl.sajansen.codmw2starter.utils.getMainFrameComponent
import org.slf4j.LoggerFactory
import java.awt.Dimension
import java.awt.event.ActionEvent
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

        val startServerButton = JButton("Start server")
        startServerButton.addActionListener { startServer() }
        startServerButton.addHotKeyMapping(HotKeysMapping.StartServer)

        val executionerSelector = JComboBox(CoD.Executioner.values())
        executionerSelector.selectedItem = Config.executioner
        executionerSelector.toolTipText = "Select preferred executioner"
        executionerSelector.addActionListener { setExecutioner(it) }

        val startClientButton = JButton("Start client")
        startClientButton.addActionListener { startClient() }
        startClientButton.addHotKeyMapping(HotKeysMapping.StartClient)
        frame.rootPane.defaultButton = startClientButton

        val quitButton = JButton("Quit")
        quitButton.addActionListener { exitApplication() }
        quitButton.addHotKeyMapping(HotKeysMapping.QuitApplication)

//        add(quitButton)
        add(mapConfigButton)
        add(Box.createHorizontalGlue())
        add(Box.createRigidArea(Dimension(50, 0)))
        add(executionerSelector)
        add(Box.createRigidArea(Dimension(10, 0)))
        add(startServerButton)
        add(Box.createRigidArea(Dimension(10, 0)))
        add(startClientButton)
    }

    private fun setExecutioner(e: ActionEvent) {
        val value = (e.source as JComboBox<*>).selectedItem
        if (value !is CoD.Executioner) {
            throw IllegalArgumentException("Selected value of JComboBox is not a Executioner")
        }

        logger.info("Changing executioner to: $value")
        Config.executioner = value
        Config.save()
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