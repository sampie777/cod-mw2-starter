package nl.sajansen.codmw2starter.gui.ipScanner

import nl.sajansen.codmw2starter.ipScanner.IpScannerInitiator
import nl.sajansen.codmw2starter.ipScanner.ScanResult
import nl.sajansen.codmw2starter.ipScanner.WebsocketScannerSwingWorker
import org.slf4j.LoggerFactory
import java.awt.BorderLayout
import javax.swing.JPanel

open class IpScannerPanel(onHostClick: ((host: String) -> Unit),
                          onHostDoubleClick: ((host: String) -> Unit)) : JPanel(), IpScannerInitiator {
    private val logger = LoggerFactory.getLogger(IpScannerPanel::class.java.name)

    private val ipScannerTable = IpScannerTable(onHostClick, onHostDoubleClick)
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