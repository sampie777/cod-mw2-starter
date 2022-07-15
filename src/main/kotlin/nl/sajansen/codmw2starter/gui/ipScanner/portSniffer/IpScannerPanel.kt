package nl.sajansen.codmw2starter.gui.ipScanner.portSniffer

import nl.sajansen.codmw2starter.config.Config
import nl.sajansen.codmw2starter.ipScanner.portSniffer.IpScannerInitiator
import nl.sajansen.codmw2starter.ipScanner.portSniffer.ScanResult
import nl.sajansen.codmw2starter.ipScanner.portSniffer.WebsocketScannerSwingWorker
import org.slf4j.LoggerFactory
import java.awt.BorderLayout
import javax.swing.JPanel

open class IpScannerPanel(
    setHost: ((host: String) -> Unit),
    runWithHost: ((host: String) -> Unit)
) : JPanel(), IpScannerInitiator {
    private val logger = LoggerFactory.getLogger(this::class.java.name)

    private val ipScannerTable = IpScannerTable(setHost, runWithHost)
    private val websocketScannerStatusPanel = WebsocketScannerStatusPanel()
    private lateinit var websocketScannerActionPanel: WebsocketScannerActionPanel
    private var worker: WebsocketScannerSwingWorker? = null

    init {
        createGui()
    }

    private fun createGui() {
        layout = BorderLayout(10, 10)

        websocketScannerActionPanel = WebsocketScannerActionPanel(this)

        val bottomPanel = JPanel(BorderLayout(10, 10))
        bottomPanel.add(websocketScannerStatusPanel, BorderLayout.LINE_START)
        bottomPanel.add(websocketScannerActionPanel, BorderLayout.LINE_END)

        add(ipScannerTable, BorderLayout.CENTER)
        add(bottomPanel, BorderLayout.PAGE_END)
    }

    fun scan(timeout: Int) {
        websocketScannerActionPanel.buttonsEnable(false)

        ipScannerTable.clearTable()
        Config.ipScannerTimeout = timeout
        Config.save()

        worker = WebsocketScannerSwingWorker(this, timeout)
        worker!!.execute()
    }

    override fun processScanStatus(status: String) {
        websocketScannerStatusPanel.updateStatus(status)
    }

    fun processScanResults(scanResults: List<ScanResult>) {
        ipScannerTable.setScanResults(scanResults)
    }

    override fun addScanResult(scanResult: ScanResult) {
        ipScannerTable.addScanResult(scanResult)
    }

    override fun onScanFinished() {
        websocketScannerActionPanel.buttonsEnable(true)
    }
}