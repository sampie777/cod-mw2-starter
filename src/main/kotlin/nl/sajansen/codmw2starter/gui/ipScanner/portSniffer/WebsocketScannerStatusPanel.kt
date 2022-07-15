package nl.sajansen.codmw2starter.gui.ipScanner.portSniffer

import nl.sajansen.codmw2starter.gui.Theme
import org.slf4j.LoggerFactory
import java.util.*
import javax.swing.JLabel
import javax.swing.JPanel

class WebsocketScannerStatusPanel : JPanel() {
    private val logger = LoggerFactory.getLogger(this::class.java.name)

    private val statusLabel = JLabel()
    private var timer: Timer? = null

    init {
        createGui()
    }

    private fun createGui() {
        statusLabel.font = Theme.normalFont
        add(statusLabel)
    }

    fun updateStatus(status: String, timeout: Long? = null) {
        statusLabel.text = status

        if (timeout != null) {
            startClearTimer(timeout)
        }
    }

    private fun startClearTimer(timeout: Long) {
        try {
            if (timer != null) {
                timer?.cancel()
            }

            timer = Timer()
            timer!!.schedule(object : TimerTask() {
                override fun run() {
                    updateStatus("")
                }
            }, timeout)
        } catch (e: Exception) {
            logger.warn("Failed to schedule new timer task for clearing status message.")
            e.printStackTrace()
        }
    }
}
