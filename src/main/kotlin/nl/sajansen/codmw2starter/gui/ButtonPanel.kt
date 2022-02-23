package nl.sajansen.codmw2starter.gui

import nl.sajansen.codmw2starter.cod.CoD
import nl.sajansen.codmw2starter.gui.mainFrame.MainFrame
import nl.sajansen.codmw2starter.gui.mainFrame.MainFramePanel
import nl.sajansen.codmw2starter.gui.mapConfig.MapConfigFrame
import nl.sajansen.codmw2starter.utils.Icon
import nl.sajansen.codmw2starter.utils.addHotKeyMapping
import nl.sajansen.codmw2starter.utils.getMainFrameComponent
import org.slf4j.LoggerFactory
import java.awt.Dimension
import java.awt.event.ActionEvent
import javax.swing.*
import javax.swing.border.EmptyBorder

class ButtonPanel(private val frame: JFrame, private val beforeStart: (() -> Boolean)) : JPanel() {
    private val logger = LoggerFactory.getLogger(this::class.java)

    init {
        createGui()
    }

    private fun createGui() {
        layout = BoxLayout(this, BoxLayout.LINE_AXIS)
        border = EmptyBorder(0, 10, 10, 10)

        val gamePlayChooserButton = JButton("Back").also {
            it.font = Theme.buttonFont
            it.addActionListener(::openGamePlayChooser)
        }

        val mapConfigButton = JButton().also {
            it.layout = BoxLayout(it, BoxLayout.LINE_AXIS)
            it.add(Icon("\uf013", size = 14f)
                .also { icon -> icon.border = EmptyBorder(1, 0, 0, 8) })
            it.add(JLabel("Maps")
                .also { label -> label.font = Theme.buttonFont })

            it.addActionListener { MapConfigFrame.createAndShow(getMainFrameComponent(this)) }
            it.addHotKeyMapping(HotKeysMapping.OpenMap)
        }

        val startClientButton = JButton().also {
            it.layout = BoxLayout(it, BoxLayout.LINE_AXIS)
            it.add(Icon("\uf04b", size = 18f)
                .also { icon -> icon.border = EmptyBorder(3, 0, 0, 8) })
            it.add(JLabel("Join")
                .also { label -> label.font = Theme.primaryButtonFont })

            it.addActionListener { startClient() }
            it.addHotKeyMapping(HotKeysMapping.StartClient)
            frame.rootPane.defaultButton = it
        }

        add(gamePlayChooserButton)
        add(Box.createRigidArea(Dimension(10, 0)))
        add(mapConfigButton)
        add(Box.createHorizontalGlue())
        add(Box.createRigidArea(Dimension(10, 0)))
        add(startClientButton)
    }

    fun startClient() {
        if (beforeStart.invoke()) {
            CoD.startClient()
        }
    }

    private fun openGamePlayChooser(e: ActionEvent) {
        MainFrame.getInstance()?.activatePanel(MainFramePanel.Panel.GamePlayChooser)
    }
}