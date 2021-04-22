package nl.sajansen.codmw2starter.gui.ipScanner

import org.slf4j.LoggerFactory
import java.awt.Dimension
import java.awt.Font
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
        border = EmptyBorder(0, 10, 10, 10)

        val timeoutLabel = JLabel("Timeout (ms): ")
        timeoutLabel.font = Font("Dialog", Font.PLAIN, 10)

        timeoutSpinner.model = SpinnerNumberModel(500, 1, Int.MAX_VALUE, 100)
        timeoutSpinner.preferredSize = Dimension(60, 18)
        timeoutSpinner.toolTipText = "Adjust this value to increase the port scanning timeout if you are on a slow network"
        timeoutSpinner.font = timeoutLabel.font

        val scanButton = JButton("Scan")
        buttonsToEnable.add(scanButton)
        scanButton.addActionListener { panel.scan(timeoutSpinner.value as Int) }

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
