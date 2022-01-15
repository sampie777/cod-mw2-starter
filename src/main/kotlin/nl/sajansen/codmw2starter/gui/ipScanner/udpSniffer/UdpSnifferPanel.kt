package nl.sajansen.codmw2starter.gui.ipScanner.udpSniffer

import nl.sajansen.codmw2starter.gui.Theme
import nl.sajansen.codmw2starter.ipScanner.udpSniffer.NetworkSniffer
import nl.sajansen.codmw2starter.ipScanner.udpSniffer.Other
import nl.sajansen.codmw2starter.utils.faSolidFont
import org.slf4j.LoggerFactory
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import javax.swing.*
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
        layout = BorderLayout(5, 5)

        val bottomPanel = JPanel()
        bottomPanel.layout = BoxLayout(bottomPanel, BoxLayout.LINE_AXIS)

        bottomPanel.add(Box.createHorizontalGlue())

        JButton("\uf2ce").also {
            bottomPanel.add(it)
            it.addActionListener { ping() }
            it.border = BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY),
                EmptyBorder(5, 10, 5, 10)
            )
            it.background = Theme.defaultPanelColor.brighter()
            it.font = faSolidFont.deriveFont(13f)
            it.toolTipText = "Re-search network without clearing current results"
        }

        bottomPanel.add(Box.createRigidArea(Dimension(5, 0)))

        JButton("\uf2f1").also {
            bottomPanel.add(it)
            it.addActionListener { search() }
            it.border = BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY),
                EmptyBorder(5, 10, 5, 10)
            )
            it.background = Theme.defaultPanelColor.brighter()
            it.font = faSolidFont.deriveFont(13f)
            it.toolTipText = "Re-search network and clear current results"
        }

        add(table, BorderLayout.CENTER)
        add(bottomPanel, BorderLayout.PAGE_END)
    }

    private fun updateTable(other: Other?) {
        table.clearTable()
        NetworkSniffer.others.values.forEach(table::addScanResult)
    }

    private fun ping() {
        NetworkSniffer.ping()
    }

    private fun search() {
        NetworkSniffer.findOthers()
        updateTable(null)
    }
}