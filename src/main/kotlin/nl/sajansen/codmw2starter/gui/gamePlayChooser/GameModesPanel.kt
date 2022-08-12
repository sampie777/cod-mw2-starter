package nl.sajansen.codmw2starter.gui.gamePlayChooser

import nl.sajansen.codmw2starter.cod.CoD
import nl.sajansen.codmw2starter.cod.CoDEventListener
import nl.sajansen.codmw2starter.cod.CoDEventListenerSubscriber
import nl.sajansen.codmw2starter.config.Config
import nl.sajansen.codmw2starter.gui.mainFrame.MainFrame
import nl.sajansen.codmw2starter.gui.mainFrame.MainFramePanel
import nl.sajansen.codmw2starter.ipScanner.udpSniffer.NetworkSniffer
import org.slf4j.LoggerFactory
import java.awt.Dimension
import java.awt.Font
import java.awt.event.ActionEvent
import java.util.*
import javax.swing.Box
import javax.swing.JPanel
import kotlin.concurrent.schedule

class GameModesPanel : JPanel(), CoDEventListener {
    private val logger = LoggerFactory.getLogger(this::class.java)

    init {
        CoDEventListenerSubscriber.register(this)
        createGui()
    }

    override fun removeNotify() {
        super.removeNotify()
        CoDEventListenerSubscriber.unregister(this)
    }

    private fun createGui() {
        reloadGui()
    }

    private fun reloadGui() {
        removeAll()

        if (CoD.isServerRunning()) {
            add(GameModeButton("Stop hosting", ::stopHosting).also {
                it.font =Font("Dialog", Font.PLAIN, 28)
                it.toolTipText = "Stop the current running server"
            })
        } else {
            add(GameModeButton("Host", ::playAsHost).also {
                it.font =Font("Dialog", Font.PLAIN, 30)
                it.toolTipText = "Start a server and join it"
            })
        }
        add(Box.createRigidArea(Dimension(5, 0)))
        add(GameModeButton("Join", ::joinAServer).also {
            it.font = Font("Dialog", Font.BOLD, 30)
            it.toolTipText = "Search for other servers to join"
        })

        invalidate()
        revalidate()
        repaint()
    }

    private fun playAsHost(e: ActionEvent) {
        Thread {
            CoD.setHost("127.0.0.1")
            CoD.startServer()
            Timer().schedule(Config.delayBetweenServerStartAndClientStart) {
                CoD.startClient()
            }
        }.start()
    }

    private fun joinAServer(e: ActionEvent) {
        NetworkSniffer.sendImHostingPing(false)
        MainFrame.getInstance()?.activatePanel(MainFramePanel.Panel.ServerBrowser)
    }

    private fun stopHosting(e: ActionEvent) {
        Thread { CoD.stopServer() }.start()
    }

    override fun onServerStarted() {
        reloadGui()
    }

    override fun onServerStopped() {
        reloadGui()
    }
}