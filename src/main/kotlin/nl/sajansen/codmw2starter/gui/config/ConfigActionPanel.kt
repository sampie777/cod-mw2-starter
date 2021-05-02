package nl.sajansen.codmw2starter.gui.config

import nl.sajansen.codmw2starter.config.Config
import nl.sajansen.codmw2starter.gui.HotKeysMapping
import nl.sajansen.codmw2starter.gui.Theme
import nl.sajansen.codmw2starter.utils.addHotKeyMapping
import org.slf4j.LoggerFactory
import java.awt.Dimension
import javax.swing.Box
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.border.EmptyBorder

class ConfigActionPanel(private val frame: ConfigFrame) : JPanel() {
    private val logger = LoggerFactory.getLogger(ConfigActionPanel::class.java.name)

    val saveButton = JButton("Save")

    init {
        createGui()
    }

    private fun createGui() {
        layout = BoxLayout(this, BoxLayout.LINE_AXIS)
        border = EmptyBorder(0, 10, 10, 10)

        saveButton.addActionListener { saveConfigAndClose() }
        saveButton.addHotKeyMapping(HotKeysMapping.Save)
        saveButton.font = Theme.primaryButtonFont
        frame.rootPane.defaultButton = saveButton

        val cancelButton = JButton("Cancel")
        cancelButton.addActionListener { cancelWindow() }
        cancelButton.addHotKeyMapping(HotKeysMapping.Close)
        cancelButton.font = Theme.buttonFont

        add(Box.createHorizontalGlue())
        add(cancelButton)
        add(Box.createRigidArea(Dimension(10, 0)))
        add(saveButton)
    }

    private fun cancelWindow() {
        logger.debug("Exiting configuration window")
        frame.dispose()
    }

    private fun saveConfigAndClose() {
        logger.info("Saving configuration changes")
        if (!frame.saveAll()) {
            return
        }

        Config.save()
        frame.dispose()
    }
}
