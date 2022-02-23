package nl.sajansen.codmw2starter.gui.mainFrame

import nl.sajansen.codmw2starter.gui.GUI
import nl.sajansen.codmw2starter.gui.Refreshable
import nl.sajansen.codmw2starter.gui.gamePlayChooser.GamePlayChooserPanel
import nl.sajansen.codmw2starter.gui.serverBrowser.ServerBrowserPanel
import org.slf4j.LoggerFactory
import java.awt.BorderLayout
import javax.swing.JSplitPane


class MainFramePanel : JSplitPane(), Refreshable {
    enum class Panel {
        GamePlayChooser,
        ServerBrowser
    }

    private val logger = LoggerFactory.getLogger(MainFramePanel::class.java.name)
    private var activePanel = Panel.GamePlayChooser

    init {
        GUI.register(this)

        createGui()
        reloadGui()

        refreshNotifications()
    }

    private fun createGui() {
        border = null
        layout = BorderLayout(10, 10)
    }

    private fun reloadGui() {
        removeAll()

        logger.info("Reloading")
        val panel = when(activePanel) {
            Panel.GamePlayChooser -> GamePlayChooserPanel()
            Panel.ServerBrowser -> ServerBrowserPanel()
        }

        add(panel, BorderLayout.CENTER)
    }

    override fun removeNotify() {
        super.removeNotify()
        GUI.unregister(this)
    }

    fun activatePanel(panel: Panel) {
        logger.info("activatePanel: $panel")
        activePanel = panel
        reloadGui()
        invalidate()
        revalidate()
        repaint()
    }
}