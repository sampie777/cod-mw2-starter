package nl.sajansen.codmw2starter.gui.mainFrame

import nl.sajansen.codmw2starter.cod.CoD
import nl.sajansen.codmw2starter.gui.ButtonPanel
import nl.sajansen.codmw2starter.gui.GUI
import nl.sajansen.codmw2starter.gui.Refreshable
import nl.sajansen.codmw2starter.gui.hostPanel.HostPanel
import nl.sajansen.codmw2starter.gui.nickname.NicknamePanel
import org.slf4j.LoggerFactory
import java.awt.BorderLayout
import javax.swing.JSplitPane


class MainFramePanel : JSplitPane(), Refreshable {
    private val logger = LoggerFactory.getLogger(MainFramePanel::class.java.name)

    private val hostPanel = HostPanel { startClient() }
    private val buttonPanel = ButtonPanel(MainFrame.getInstance()!!) { beforeStart() }

    init {
        GUI.register(this)

        createGui()

        refreshNotifications()
    }

    private fun createGui() {
        border = null
        layout = BorderLayout(10, 10)

        add(NicknamePanel(), BorderLayout.PAGE_START)
        add(hostPanel, BorderLayout.CENTER)
        add(buttonPanel, BorderLayout.PAGE_END)
    }

    override fun removeNotify() {
        super.removeNotify()
        GUI.unregister(this)
    }

    private fun beforeStart(): Boolean {
        return CoD.setHost(hostPanel.getHost())
    }

    private fun startClient() {
        buttonPanel.startClient()
    }
}