package nl.sajansen.codmw2starter.gui.gamePlayChooser

import nl.sajansen.codmw2starter.cod.CoD
import nl.sajansen.codmw2starter.config.Config
import nl.sajansen.codmw2starter.gui.mainFrame.MainFrame
import nl.sajansen.codmw2starter.gui.mainFrame.MainFramePanel
import nl.sajansen.codmw2starter.ipScanner.udpSniffer.NetworkSniffer
import org.slf4j.LoggerFactory
import java.awt.Dimension
import java.awt.event.ActionEvent
import java.util.*
import javax.swing.Box
import javax.swing.JPanel
import kotlin.concurrent.schedule

class GameModesPanel : JPanel() {
    private val logger = LoggerFactory.getLogger(this::class.java)

    init {
        createGui()
    }

    private fun createGui() {
        add(GameModeButton("Host", ::playAsHost))
        add(Box.createRigidArea(Dimension(5, 0)))
        add(GameModeButton("Join", ::joinAServer))
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
}