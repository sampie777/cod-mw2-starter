package nl.sajansen.codmw2starter.gui.mainFrame

import nl.sajansen.codmw2starter.cod.CoD
import nl.sajansen.codmw2starter.gui.ButtonPanel
import nl.sajansen.codmw2starter.gui.GUI
import nl.sajansen.codmw2starter.gui.Refreshable
import nl.sajansen.codmw2starter.gui.hostPanel.HostPanel
import org.slf4j.LoggerFactory
import java.awt.BorderLayout
import javax.swing.JSplitPane


class MainFramePanel : JSplitPane(), Refreshable {
    private val logger = LoggerFactory.getLogger(MainFramePanel::class.java.name)

    private val hostPanel = HostPanel()

    init {
        GUI.register(this)

        createGui()

        refreshNotifications()
    }

    private fun createGui() {
        border = null
        layout = BorderLayout(10, 10)

        add(hostPanel, BorderLayout.CENTER)
        add(ButtonPanel(MainFrame.getInstance()!!) { beforeStart() }, BorderLayout.PAGE_END)
    }

    override fun removeNotify() {
        super.removeNotify()
        GUI.unregister(this)
    }

    private fun beforeStart(): Boolean {
        return CoD.setHost(hostPanel.getHost())
    }
}