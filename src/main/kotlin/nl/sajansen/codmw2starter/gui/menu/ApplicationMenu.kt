package nl.sajansen.codmw2starter.gui.menu

import nl.sajansen.codmw2starter.can.CAN
import nl.sajansen.codmw2starter.exitApplication
import nl.sajansen.codmw2starter.gui.HotKeysMapping
import nl.sajansen.codmw2starter.gui.config.ConfigFrame
import nl.sajansen.codmw2starter.utils.addHotKeyMapping
import nl.sajansen.codmw2starter.utils.getMainFrameComponent
import org.slf4j.LoggerFactory
import java.awt.Color
import java.awt.event.InputEvent
import java.awt.event.KeyEvent
import javax.swing.BorderFactory
import javax.swing.JMenu
import javax.swing.JMenuItem
import javax.swing.KeyStroke

class ApplicationMenu : JMenu("Application") {
    private val logger = LoggerFactory.getLogger(ApplicationMenu::class.java.name)

    init {
        initGui()
    }

    private fun initGui() {
        popupMenu.border = BorderFactory.createLineBorder(Color(168, 168, 168))
        addHotKeyMapping(HotKeysMapping.ApplicationMenu)

        val settingsItem = JMenuItem("Settings")
        val infoItem = JMenuItem("Info")
        val quitItem = JMenuItem("Quit")
        val saveItem = JMenuItem("Save")
        val readItem = JMenuItem("Read")

        // Set alt keys
        settingsItem.addHotKeyMapping(HotKeysMapping.ShowConfig)
        infoItem.addHotKeyMapping(HotKeysMapping.ShowApplicationInfo)
        quitItem.addHotKeyMapping(HotKeysMapping.QuitApplication)
        quitItem.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK)

        add(saveItem)
        add(readItem)
        addSeparator()
        add(settingsItem)
        addSeparator()
        add(infoItem)
        add(quitItem)

        saveItem.addActionListener { CAN.saveData() }
        readItem.addActionListener { CAN.readFile() }
        settingsItem.addActionListener { ConfigFrame(getMainFrameComponent(this)) }
        infoItem.addActionListener { InfoFrame.createAndShow(getMainFrameComponent(this)) }
        quitItem.addActionListener { exitApplication() }
    }
}