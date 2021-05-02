package nl.sajansen.codmw2starter.gui.hostPanel

import getLocalNetworkIpAddresses
import nl.sajansen.codmw2starter.cod.CoD
import nl.sajansen.codmw2starter.gui.GUI
import nl.sajansen.codmw2starter.gui.Refreshable
import nl.sajansen.codmw2starter.gui.ipScanner.IpScannerPanel
import org.slf4j.LoggerFactory
import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.*
import javax.swing.border.EmptyBorder

class HostPanel(private val runClient: (() -> Unit)) : JPanel(), Refreshable {
    private val logger = LoggerFactory.getLogger(HostPanel::class.java)

    private val currentHostLabel = JLabel()
    private val localHostsPanel = JPanel()
    private val localHostLabel = JLabel("Local host(s): ")
    private val ipScannerPanel = IpScannerPanel({ onHostClick(it) }, { onHostDoubleClick(it) })
    private val customHostField = JTextField()

    init {
        GUI.register(this)
        createGui()
        refreshHosts()
    }

    private fun createGui() {
        layout = BorderLayout(10, 10)
        border = EmptyBorder(10, 10, 10, 10)

        val currentHostTextLabel = JLabel("Current host: ")
        currentHostTextLabel.toolTipText = "Click to set host address"
        currentHostTextLabel.font = Font("Dialog", Font.PLAIN, 12)

        currentHostLabel.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        currentHostLabel.toolTipText = "Click to set host address"
        currentHostLabel.font = Font("Dialog", Font.PLAIN, 12)

        val currentHostsPanel = JPanel()
        currentHostsPanel.layout = FlowLayout(FlowLayout.LEFT)
        currentHostsPanel.add(currentHostTextLabel)
        currentHostsPanel.add(currentHostLabel)

        localHostLabel.toolTipText = "Click to set host address"
        localHostLabel.font = Font("Dialog", Font.PLAIN, 12)

        localHostsPanel.layout = FlowLayout(FlowLayout.LEFT)
        localHostsPanel.add(localHostLabel)

        val customHostLabel = JLabel("Use host:")

        val labelPanel = JPanel()
        labelPanel.alignmentX = Component.LEFT_ALIGNMENT
        labelPanel.layout = BoxLayout(labelPanel, BoxLayout.PAGE_AXIS)
        labelPanel.add(currentHostsPanel)
        labelPanel.add(Box.createRigidArea(Dimension(0, 5)))
        labelPanel.add(localHostsPanel)

        val customHostPanel = JPanel()
        customHostPanel.layout = BorderLayout(10, 10)
        customHostPanel.add(customHostLabel, BorderLayout.LINE_START)
        customHostPanel.add(customHostField, BorderLayout.CENTER)

        add(labelPanel, BorderLayout.PAGE_START)
        add(ipScannerPanel, BorderLayout.CENTER)
        add(customHostPanel, BorderLayout.PAGE_END)
    }

    private fun refreshHosts() {
        refreshCurrentHost()
        refreshLocalHost()
    }

    private fun refreshCurrentHost() {
        val host = CoD.getHost()
        customHostField.text = host

        currentHostLabel.text = host ?: "unknown"
        currentHostLabel.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                customHostField.text = host ?: "127.0.0.1"
            }
        })
    }

    private fun refreshLocalHost() {
        localHostsPanel.removeAll()
        localHostsPanel.add(localHostLabel)

        val localNetworkIpAddresses = getLocalNetworkIpAddresses()
        localNetworkIpAddresses.forEach {
            val hostLabel = JLabel(it)
            if (localNetworkIpAddresses.size > 1 && localNetworkIpAddresses.last() != it) {
                hostLabel.text = hostLabel.text + ","
            }
            hostLabel.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
            hostLabel.font = Font("Dialog", Font.PLAIN, 12)
            hostLabel.toolTipText = "Click to set host address"
            hostLabel.addMouseListener(object : MouseAdapter() {
                override fun mouseClicked(e: MouseEvent) {
                    customHostField.text = it
                }
            })

            localHostsPanel.add(hostLabel)
        }
    }

    override fun serverStarted() = refreshHosts()
    override fun clientStarted() = refreshHosts()

    private fun onHostClick(host: String) {
        customHostField.text = host
    }

    private fun onHostDoubleClick(host: String) {
        customHostField.text = host
        runClient.invoke()
    }

    fun getHost(): String {
        return customHostField.text.trim()
    }
}