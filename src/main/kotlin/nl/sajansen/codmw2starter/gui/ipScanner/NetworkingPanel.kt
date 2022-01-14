package nl.sajansen.codmw2starter.gui.ipScanner

import nl.sajansen.codmw2starter.gui.Theme
import nl.sajansen.codmw2starter.gui.ipScanner.portSniffer.IpScannerPanel
import nl.sajansen.codmw2starter.gui.ipScanner.udpSniffer.UdpSnifferPanel
import org.slf4j.LoggerFactory
import java.awt.BorderLayout
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.border.EmptyBorder

class NetworkingPanel(
    onHostClick: ((host: String) -> Unit),
    onHostDoubleClick: ((host: String) -> Unit)
) : JPanel() {

    private val logger = LoggerFactory.getLogger(this::class.java.name)
    private val ipScannerPanel = IpScannerPanel(onHostClick, onHostDoubleClick)
    private val udpSnifferPanel = UdpSnifferPanel(onHostClick, onHostDoubleClick)
    private var panelToShow: JPanel = ipScannerPanel
    private var centerPanel = JPanel()

    init {
        createGui()
        refreshGui()
    }

    private fun createGui() {
        layout = BorderLayout(10, 10)
        border = EmptyBorder(0, 0, 0, 0)

        centerPanel.also {
            it.layout = BorderLayout(0, 0)
        }

        val bottomPanel = JPanel(BorderLayout(10, 10))
        JButton("Change search method").also {
            bottomPanel.add(it, BorderLayout.LINE_END)
            it.addActionListener { switchPanel() }
            it.font = Theme.buttonFont
        }

        add(centerPanel, BorderLayout.CENTER)
        add(bottomPanel, BorderLayout.PAGE_END)
    }

    private fun refreshGui() {
        centerPanel.removeAll()
        centerPanel.add(panelToShow, BorderLayout.CENTER)
        centerPanel.revalidate()
        centerPanel.repaint()
    }

    private fun switchPanel() {
        panelToShow = if (panelToShow == ipScannerPanel) {
            udpSnifferPanel
        } else {
            ipScannerPanel
        }

        refreshGui()
    }
}