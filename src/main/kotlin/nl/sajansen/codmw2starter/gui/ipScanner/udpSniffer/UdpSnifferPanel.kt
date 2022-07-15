package nl.sajansen.codmw2starter.gui.ipScanner.udpSniffer

import nl.sajansen.codmw2starter.cod.CoDEventListener
import nl.sajansen.codmw2starter.cod.CoDEventListenerSubscriber
import nl.sajansen.codmw2starter.config.Config
import nl.sajansen.codmw2starter.gui.Theme
import nl.sajansen.codmw2starter.gui.ipScanner.portSniffer.WebsocketScannerStatusPanel
import nl.sajansen.codmw2starter.ipScanner.portSniffer.getLocalNetworkIpAddresses
import nl.sajansen.codmw2starter.ipScanner.udpSniffer.NetworkSniffer
import nl.sajansen.codmw2starter.utils.faSolidFont
import org.slf4j.LoggerFactory
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import javax.swing.*
import javax.swing.border.EmptyBorder

class UdpSnifferPanel(
    setHost: ((host: String) -> Unit),
    runWithHost: ((host: String) -> Unit)
) : JPanel(), CoDEventListener {
    private val logger = LoggerFactory.getLogger(this::class.java.name)

    private val table = Table(setHost, runWithHost)
    private val websocketScannerStatusPanel = WebsocketScannerStatusPanel()

    init {
        createGui()

        updateTable()
        NetworkSniffer.onOtherAdded = ::updateTable
        CoDEventListenerSubscriber.register(this)
    }

    private fun createGui() {
        layout = BorderLayout(5, 5)

        val bottomActionPanel = JPanel()
        bottomActionPanel.layout = BoxLayout(bottomActionPanel, BoxLayout.LINE_AXIS)

        JButton("\uf2ce").also {
            bottomActionPanel.add(it)
            it.addActionListener { ping() }
            it.border = BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY),
                EmptyBorder(5, 10, 5, 10)
            )
            it.background = Theme.defaultPanelColor.brighter()
            it.font = faSolidFont.deriveFont(13f)
            it.toolTipText = "Re-search network without clearing current results"
        }

        bottomActionPanel.add(Box.createRigidArea(Dimension(5, 0)))

        JButton("\uf2f1").also {
            bottomActionPanel.add(it)
            it.addActionListener { search() }
            it.border = BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY),
                EmptyBorder(5, 10, 5, 10)
            )
            it.background = Theme.defaultPanelColor.brighter()
            it.font = faSolidFont.deriveFont(13f)
            it.toolTipText = "Re-search network and clear current results"
        }

        val bottomPanel = JPanel(BorderLayout(10, 10))
        bottomPanel.add(websocketScannerStatusPanel, BorderLayout.LINE_START)
        bottomPanel.add(bottomActionPanel, BorderLayout.LINE_END)

        add(table, BorderLayout.CENTER)
        add(bottomPanel, BorderLayout.PAGE_END)
    }

    private fun updateTable() {
        val localIpAddresses = getLocalNetworkIpAddresses()
        table.clearTable()
        NetworkSniffer.others.values
            .filter { !Config.udpSnifferFilterOutLocalIps || !localIpAddresses.contains(it.hostAddress) }
            .sortedBy { it.isHosting }
            .forEach(table::addScanResult)
    }

    private fun ping() {
        NetworkSniffer.ping()
    }

    private fun search() {
        NetworkSniffer.findOthers()
        updateTable()
    }

    override fun onUdpPingSending() {
        websocketScannerStatusPanel.updateStatus("Sending ping...", 4000)
    }
    override fun onUdpPingSent() {
        websocketScannerStatusPanel.updateStatus("Ping sent", 4000)
    }
}