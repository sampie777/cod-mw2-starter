package nl.sajansen.codmw2starter.gui.ipScanner.portSniffer

import nl.sajansen.codmw2starter.gui.Theme
import javax.swing.JLabel
import javax.swing.JPanel

class WebsocketScannerStatusPanel : JPanel() {

    private val statusLabel = JLabel()

    init {
        createGui()
    }

    private fun createGui() {
        statusLabel.font = Theme.normalFont
        add(statusLabel)
    }

    fun updateStatus(status: String) {
        statusLabel.text = status
    }
}
