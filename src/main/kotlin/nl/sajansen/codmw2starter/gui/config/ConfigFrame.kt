package nl.sajansen.codmw2starter.gui.config

import nl.sajansen.codmw2starter.gui.DefaultDialogKeyDispatcher
import org.slf4j.LoggerFactory
import java.awt.BorderLayout
import java.awt.KeyboardFocusManager
import javax.swing.JDialog
import javax.swing.JFrame
import javax.swing.JPanel

class ConfigFrame(private val parentFrame: JFrame?) : JDialog(parentFrame) {
    private val logger = LoggerFactory.getLogger(ConfigFrame::class.java.name)

    private val configEditPanel: ConfigEditPanel = ConfigEditPanel()

    init {
        KeyboardFocusManager
            .getCurrentKeyboardFocusManager()
            .addKeyEventDispatcher(DefaultDialogKeyDispatcher(this))

        createGui()
    }

    private fun createGui() {
        val mainPanel = JPanel(BorderLayout(10, 0))
        add(mainPanel)

        val configActionPanel = ConfigActionPanel(this)

        mainPanel.add(configEditPanel, BorderLayout.CENTER)
        mainPanel.add(configActionPanel, BorderLayout.PAGE_END)

        pack()  // Realize components so the focus request will work
        configActionPanel.saveButton.requestFocusInWindow()

        title = "Settings"
        setSize(560, 525)
        setLocationRelativeTo(parentFrame)
        modalityType = ModalityType.APPLICATION_MODAL
        isVisible = true
    }

    fun saveAll(): Boolean {
        return configEditPanel.saveAll()
    }
}