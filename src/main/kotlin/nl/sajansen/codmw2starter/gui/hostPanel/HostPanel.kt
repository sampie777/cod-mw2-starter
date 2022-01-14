package nl.sajansen.codmw2starter.gui.hostPanel

import nl.sajansen.codmw2starter.cod.CoD
import nl.sajansen.codmw2starter.cod.CoDEventListener
import nl.sajansen.codmw2starter.cod.CoDEventListenerSubscriber
import nl.sajansen.codmw2starter.gui.Theme
import nl.sajansen.codmw2starter.gui.ipScanner.NetworkingPanel
import nl.sajansen.codmw2starter.ipScanner.portSniffer.getLocalNetworkIpAddresses
import org.slf4j.LoggerFactory
import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.*
import javax.swing.border.EmptyBorder

class HostPanel(private val runClient: (() -> Unit)) : JPanel(), CoDEventListener {
    private val logger = LoggerFactory.getLogger(HostPanel::class.java)

    private val currentHostLabel = JLabel()
    private val localHostsPanel = JPanel()
    private val localHostLabel = JLabel("Local host(s): ")
    private val networkingPanel = NetworkingPanel(::onHostClick, ::onHostDoubleClick)
    private val customHostField = JTextField()

    init {
        CoDEventListenerSubscriber.register(this)
        createGui()
        refreshHosts()
    }

    private fun createGui() {
        layout = BorderLayout(10, 10)
        border = EmptyBorder(0, 0, 10, 0)

        val currentHostTextLabel = JLabel("Last used: ")
        currentHostTextLabel.toolTipText = "Click to use as server"
        currentHostTextLabel.font = Theme.normalFont

        currentHostLabel.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        currentHostLabel.toolTipText = "Click to use as server"
        currentHostLabel.font = Theme.normalFont

        val currentHostsPanel = JPanel()
        currentHostsPanel.layout = FlowLayout(FlowLayout.LEFT)
        currentHostsPanel.add(currentHostTextLabel)
        currentHostsPanel.add(currentHostLabel)

        localHostLabel.toolTipText = "Click to use as server"
        localHostLabel.font = Theme.normalFont

        localHostsPanel.layout = FlowLayout(FlowLayout.LEFT)
        localHostsPanel.add(localHostLabel)

        val customHostLabel = JLabel("Play on server:")
        customHostLabel.font = Theme.boldFont

        customHostField.font = Theme.boldFont
        customHostField.border = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color(168, 168, 168)),
            EmptyBorder(5, 10, 5, 7)
        )

        val labelPanel = JPanel()
        labelPanel.alignmentX = Component.LEFT_ALIGNMENT
        labelPanel.layout = BoxLayout(labelPanel, BoxLayout.PAGE_AXIS)
        labelPanel.border = EmptyBorder(0, 10, 0, 10)
        labelPanel.add(currentHostsPanel)
        labelPanel.add(Box.createRigidArea(Dimension(0, 5)))
        labelPanel.add(localHostsPanel)

        networkingPanel.border = EmptyBorder(0, 10, 0, 10)

        val customHostPanel = JPanel()
        customHostPanel.layout = BorderLayout(10, 10)
        customHostPanel.border = EmptyBorder(15, 10, 15, 10)
        customHostPanel.background = Theme.highlightPanelColor
        customHostPanel.add(customHostLabel, BorderLayout.LINE_START)
        customHostPanel.add(customHostField, BorderLayout.CENTER)

        add(labelPanel, BorderLayout.PAGE_START)
        add(networkingPanel, BorderLayout.CENTER)
        add(customHostPanel, BorderLayout.PAGE_END)
    }

    private fun refreshHosts() {
        onHostChanged()
        refreshLocalHost()
    }

    override fun onHostChanged() {
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
        logger.info("Refreshing local hosts")
        localHostsPanel.removeAll()
        localHostsPanel.add(localHostLabel)

        val localNetworkIpAddresses = getLocalNetworkIpAddresses()
        localNetworkIpAddresses.forEach {
            val hostLabel = JLabel(it)
            if (localNetworkIpAddresses.size > 1 && localNetworkIpAddresses.last() != it) {
                hostLabel.text = hostLabel.text + ","
            }
            hostLabel.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
            hostLabel.font = Theme.normalFont
            hostLabel.toolTipText = "Click to use as server"
            hostLabel.addMouseListener(object : MouseAdapter() {
                override fun mouseClicked(e: MouseEvent) {
                    customHostField.text = it
                }
            })

            localHostsPanel.add(hostLabel)
        }
    }

    override fun onServerStarted() = refreshLocalHost()
    override fun onClientStarted() = refreshLocalHost()

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