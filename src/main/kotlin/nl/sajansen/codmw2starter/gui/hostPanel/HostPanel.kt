package nl.sajansen.codmw2starter.gui.hostPanel

import nl.sajansen.codmw2starter.gui.GUI
import nl.sajansen.codmw2starter.gui.Refreshable
import nl.sajansen.codmw2starter.gui.ipScanner.WebsocketScannerPanel
import nl.sajansen.codmw2starter.io.CoD
import org.slf4j.LoggerFactory
import java.awt.Dimension
import javax.swing.*
import javax.swing.border.EmptyBorder

class HostPanel : JPanel(), Refreshable {
    private val logger = LoggerFactory.getLogger(HostPanel::class.java)

    private val currentHostLabel = JLabel()
    private val ipScannerPanel = WebsocketScannerPanel { onHostClick(it) }
    private val customHostField = JTextField()

    init {
        GUI.register(this)
        createGui()
        refreshCurrentHost()
    }

    private fun createGui() {
        layout = BoxLayout(this, BoxLayout.PAGE_AXIS)
        border = EmptyBorder(10, 10, 10, 10)

        add(currentHostLabel)
        add(Box.createRigidArea(Dimension(0, 10)))
        add(ipScannerPanel)
        add(Box.createRigidArea(Dimension(0, 10)))
        add(customHostField)
    }

    private fun refreshCurrentHost() {
        val host = CoD.getHost()
        currentHostLabel.text = "Current host: ${host ?: "unknown"}"
        customHostField.text = host
    }

    override fun serverStarted() = refreshCurrentHost()
    override fun clientStarted() = refreshCurrentHost()

    fun onHostClick(host: String) {
        customHostField.text = host
    }

    fun getHost(): String {
        return customHostField.text.trim()
    }
}