package nl.sajansen.codmw2starter.gui.menu

import nl.sajansen.codmw2starter.ApplicationInfo
import nl.sajansen.codmw2starter.gui.ClickableLinkComponent
import nl.sajansen.codmw2starter.gui.DefaultDialogKeyDispatcher
import java.awt.Dimension
import java.awt.Font
import java.awt.KeyboardFocusManager
import java.nio.file.Paths
import javax.swing.*
import javax.swing.border.EmptyBorder

class InfoFrame(private val parentFrame: JFrame?) : JDialog(parentFrame) {

    companion object {
        fun create(parentFrame: JFrame?): InfoFrame = InfoFrame(parentFrame)

        fun createAndShow(parentFrame: JFrame?): InfoFrame {
            val frame = create(parentFrame)
            frame.isVisible = true
            return frame
        }
    }

    init {
        KeyboardFocusManager
            .getCurrentKeyboardFocusManager()
            .addKeyEventDispatcher(DefaultDialogKeyDispatcher(this))

        createGui()
    }

    private fun createGui() {
        val mainPanel = JPanel()
        mainPanel.layout = BoxLayout(mainPanel, BoxLayout.PAGE_AXIS)
        mainPanel.border = EmptyBorder(0, 20, 10, 20)
        add(mainPanel)

        val versionLabel = JLabel(
            "<html>" +
                    "<h1>${ApplicationInfo.name}</h1>" +
                    "<p>By ${ApplicationInfo.author}</p>" +
                    "<p>Version: ${ApplicationInfo.version}</p>" +
                    "</html>"
        )
        versionLabel.font = Font("Dialog", Font.PLAIN, 14)

        val sourceCodeLabel = ClickableLinkComponent("Source code (GitHub)", ApplicationInfo.url)
        sourceCodeLabel.font = Font("Dialog", Font.PLAIN, 14)

        val donationLabel = ClickableLinkComponent("Donate to ${ApplicationInfo.name}", ApplicationInfo.donationUrl)
        donationLabel.font = Font("Dialog", Font.PLAIN, 14)

        val applicationLoggingInfoLabel = JLabel("<html>Application log file location: ${Paths.get(System.getProperty("java.io.tmpdir"), "cod-mw2-starter.log")}</html>")
        applicationLoggingInfoLabel.font = Font("Dialog", Font.ITALIC, 12)

        mainPanel.add(versionLabel)
        mainPanel.add(Box.createRigidArea(Dimension(0, 10)))
        mainPanel.add(sourceCodeLabel)
        mainPanel.add(Box.createRigidArea(Dimension(0, 10)))
        mainPanel.add(donationLabel)
        mainPanel.add(Box.createRigidArea(Dimension(0, 20)))
        mainPanel.add(applicationLoggingInfoLabel)

        title = "Information"
        pack()
        setLocationRelativeTo(parentFrame)
        modalityType = ModalityType.APPLICATION_MODAL
    }
}