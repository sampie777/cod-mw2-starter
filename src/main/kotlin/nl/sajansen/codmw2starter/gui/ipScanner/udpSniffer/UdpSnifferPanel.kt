package nl.sajansen.codmw2starter.gui.ipScanner.udpSniffer

import nl.sajansen.codmw2starter.gui.Theme
import nl.sajansen.codmw2starter.ipScanner.udpSniffer.NetworkSniffer
import nl.sajansen.codmw2starter.ipScanner.udpSniffer.Other
import org.slf4j.LoggerFactory
import java.awt.BorderLayout
import java.awt.Color
import javax.swing.BorderFactory
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.border.EmptyBorder

class UdpSnifferPanel(
    onHostClick: ((host: String) -> Unit),
    onHostDoubleClick: ((host: String) -> Unit)
) : JPanel() {
    private val logger = LoggerFactory.getLogger(this::class.java.name)

    private val table = Table(onHostClick, onHostDoubleClick)

    init {
        createGui()

        updateTable(null)
        NetworkSniffer.onOtherAdded = ::updateTable
    }

    private fun createGui() {
        layout = BorderLayout(10, 10)

        val bottomPanel = JPanel(BorderLayout(10, 10))
        JButton("Reset search").also {
            bottomPanel.add(it, BorderLayout.LINE_END)
            it.addActionListener { search() }
            it.border = BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY),
                EmptyBorder(2, 5, 2, 5)
            )
            it.font = Theme.buttonFont
            it.background = Theme.defaultPanelColor.brighter()
        }

        add(table, BorderLayout.CENTER)
        add(bottomPanel, BorderLayout.PAGE_END)
    }

    private fun updateTable(other: Other?) {
        table.clearTable()
        NetworkSniffer.others.values.forEach(table::addScanResult)
    }

    private fun search() {
        NetworkSniffer.findOthers()
        updateTable(null)
    }
}