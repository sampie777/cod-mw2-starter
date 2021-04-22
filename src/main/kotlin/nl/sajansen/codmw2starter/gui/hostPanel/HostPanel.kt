package nl.sajansen.codmw2starter.gui.hostPanel

import getLocalNetworkIpAddresses
import nl.sajansen.codmw2starter.gui.GUI
import nl.sajansen.codmw2starter.gui.Refreshable
import nl.sajansen.codmw2starter.gui.ipScanner.IpScannerPanel
import nl.sajansen.codmw2starter.io.CoD
import org.slf4j.LoggerFactory
import java.awt.Component
import java.awt.Cursor
import java.awt.Dimension
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.*
import javax.swing.border.EmptyBorder

class HostPanel : JPanel(), Refreshable {
    private val logger = LoggerFactory.getLogger(HostPanel::class.java)

    private val localHostLabel = JLabel()
    private val currentHostLabel = JLabel()
    private val ipScannerPanel = IpScannerPanel { onHostClick(it) }
    private val customHostField = JTextField()

    init {
        GUI.register(this)
        createGui()
        refreshHosts()
    }

    private fun createGui() {
        layout = BoxLayout(this, BoxLayout.PAGE_AXIS)
        border = EmptyBorder(10, 10, 10, 10)

        currentHostLabel.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        currentHostLabel.toolTipText = "Click to set host address"
        localHostLabel.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        localHostLabel.toolTipText = "Click to set host address"

        val labelPanel = JPanel()
        labelPanel.alignmentX = Component.LEFT_ALIGNMENT
        labelPanel.layout = BoxLayout(labelPanel, BoxLayout.PAGE_AXIS)
        labelPanel.add(currentHostLabel)
        labelPanel.add(Box.createRigidArea(Dimension(0, 5)))
        labelPanel.add(localHostLabel)

        ipScannerPanel.alignmentX = Component.LEFT_ALIGNMENT
        customHostField.alignmentX = Component.LEFT_ALIGNMENT

        add(labelPanel)
        add(Box.createRigidArea(Dimension(0, 10)))
        add(ipScannerPanel)
        add(Box.createRigidArea(Dimension(0, 10)))
        add(customHostField)
    }

    private fun refreshHosts() {
        refreshCurrentHost()
        refreshLocalHost()
    }

    private fun refreshCurrentHost() {
        val host = CoD.getHost()
        currentHostLabel.text = "Current host: ${host ?: "unknown"}"
        customHostField.text = host

        currentHostLabel.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                customHostField.text = host ?: "127.0.0.1"
            }
        })
    }

    private fun refreshLocalHost() {
        val localNetworkIpAddresses = getLocalNetworkIpAddresses()
        localHostLabel.text = "Local IP: ${localNetworkIpAddresses.joinToString(", ")}"

        localHostLabel.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                customHostField.text = localNetworkIpAddresses.firstOrNull() ?: "127.0.0.1"
            }
        })
    }

    override fun serverStarted() = refreshHosts()
    override fun clientStarted() = refreshHosts()

    private fun onHostClick(host: String) {
        customHostField.text = host
    }

    fun getHost(): String {
        return customHostField.text.trim()
    }
}