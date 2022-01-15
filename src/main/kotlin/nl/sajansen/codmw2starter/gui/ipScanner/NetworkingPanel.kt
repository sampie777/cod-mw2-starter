package nl.sajansen.codmw2starter.gui.ipScanner

import nl.sajansen.codmw2starter.gui.Theme
import nl.sajansen.codmw2starter.gui.ipScanner.portSniffer.IpScannerPanel
import nl.sajansen.codmw2starter.gui.ipScanner.udpSniffer.UdpSnifferPanel
import org.slf4j.LoggerFactory
import javax.swing.JTabbedPane
import javax.swing.border.EmptyBorder

class NetworkingPanel(
    onHostClick: ((host: String) -> Unit),
    onHostDoubleClick: ((host: String) -> Unit)
) : JTabbedPane() {

    private val logger = LoggerFactory.getLogger(this::class.java.name)
    private val ipScannerPanel = IpScannerPanel(onHostClick, onHostDoubleClick)
    private val udpSnifferPanel = UdpSnifferPanel(onHostClick, onHostDoubleClick)

    init {
        createGui()
    }

    private fun createGui() {
        border = EmptyBorder(0, 0, 0, 0)
        font = Theme.normalFont

        addTab("Client scanner", udpSnifferPanel)
        addTab("PC scanner", ipScannerPanel)
    }

}