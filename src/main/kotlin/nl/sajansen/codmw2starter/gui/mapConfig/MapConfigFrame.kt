package nl.sajansen.codmw2starter.gui.mapConfig

import nl.sajansen.codmw2starter.gui.DefaultDialogKeyDispatcher
import org.slf4j.LoggerFactory
import java.awt.BorderLayout
import java.awt.KeyboardFocusManager
import javax.swing.JDialog
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.border.EmptyBorder

class MapConfigFrame(private val parentFrame: JFrame?, private val visible: Boolean = false) : JDialog(parentFrame) {
    companion object {
        private val logger = LoggerFactory.getLogger(MapConfigFrame::class.java)

        fun create(parentFrame: JFrame?): MapConfigFrame = MapConfigFrame(parentFrame, false)

        fun createAndShow(parentFrame: JFrame?): MapConfigFrame {
            val frame = create(parentFrame)
            frame.isVisible = true
            return frame
        }
    }

    private val mapConfigCreatePanel = MapConfigCreatePanel()

    init {
        KeyboardFocusManager
            .getCurrentKeyboardFocusManager()
            .addKeyEventDispatcher(DefaultDialogKeyDispatcher(this))

        createGui()
    }

    private fun createGui() {
        val mainPanel = JPanel(BorderLayout(10, 10))
        mainPanel.border = EmptyBorder(10, 10, 10, 10)
        add(mainPanel)

        mainPanel.add(mapConfigCreatePanel, BorderLayout.CENTER)
        mainPanel.add(MapConfigButtonPanel(this) { getCommand() }, BorderLayout.PAGE_END)

        title = "Game Configuration"
//        setSize(600, 520)
        pack()
        setLocationRelativeTo(parentFrame)
        modalityType = ModalityType.MODELESS
        isVisible = visible
    }

    private fun getCommand(): String? {
        return mapConfigCreatePanel.getCommand()
    }

}