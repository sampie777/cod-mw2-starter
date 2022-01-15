package nl.sajansen.codmw2starter.gui.ipScanner.portSniffer

import nl.sajansen.codmw2starter.config.Config
import nl.sajansen.codmw2starter.gui.Theme
import nl.sajansen.codmw2starter.utils.faSolidFont
import org.slf4j.LoggerFactory
import java.awt.Color
import java.awt.Dimension
import javax.swing.*
import javax.swing.border.EmptyBorder

class WebsocketScannerActionPanel(private val panel: IpScannerPanel) : JPanel() {
    private val logger = LoggerFactory.getLogger(WebsocketScannerActionPanel::class.java.name)

    private val buttonsToEnable = ArrayList<JButton>()
    private val timeoutSpinner = JSpinner()

    init {
        createGui()
        buttonsEnable(true)
    }

    private fun createGui() {
        layout = BoxLayout(this, BoxLayout.LINE_AXIS)
        border = EmptyBorder(0, 0, 0, 0)

        val timeoutLabel = JLabel("Timeout (ms): ")
        timeoutLabel.font = Theme.smallFont

        timeoutSpinner.model = SpinnerNumberModel(Config.ipScannerTimeout, 1, Int.MAX_VALUE, 100)
        timeoutSpinner.preferredSize = Dimension(60, 18)
        timeoutSpinner.toolTipText = "Adjust this value to increase the port scanning timeout if you are on a slow network"
        timeoutSpinner.font = timeoutLabel.font

        val scanButton = JButton("\uf2f1").also {
            it.border = BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY),
                EmptyBorder(5, 10, 5, 10)
            )
            it.background = Theme.defaultPanelColor.brighter()
            it.addActionListener { panel.scan(timeoutSpinner.value as Int) }
            it.font = faSolidFont.deriveFont(13f)
            it.toolTipText = "Scan network"
        }
        buttonsToEnable.add(scanButton)

        add(Box.createHorizontalGlue())
        add(timeoutLabel)
        add(timeoutSpinner)
        add(Box.createRigidArea(Dimension(10, 0)))
        add(scanButton)
    }

    fun buttonsEnable(enable: Boolean) {
        buttonsToEnable.forEach { it.isEnabled = enable }
    }
}
