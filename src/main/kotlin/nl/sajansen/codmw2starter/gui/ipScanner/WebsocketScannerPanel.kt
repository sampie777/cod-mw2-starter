package nl.sajansen.codmw2starter.gui.ipScanner

import nl.sajansen.codmw2starter.ipScanner.ScanResult
import nl.sajansen.codmw2starter.ipScanner.WebsocketScannerInitiator
import nl.sajansen.codmw2starter.ipScanner.WebsocketScannerSwingWorker
import java.awt.BorderLayout
import java.util.logging.Logger
import javax.swing.JPanel
import javax.swing.border.EmptyBorder

open class WebsocketScannerPanel(private val onHostClick: ((host: String) -> Unit)) : JPanel(), WebsocketScannerInitiator {
    private val logger = Logger.getLogger(WebsocketScannerPanel::class.java.name)

    val websocketScannerTable = WebsocketScannerTable(onHostClick)
    private val websocketScannerStatusPanel = WebsocketScannerStatusPanel()
    private lateinit var websocketScannerActionPanel: WebsocketScannerActionPanel
    private var worker: WebsocketScannerSwingWorker? = null

    init {
        createGui()
    }

    private fun createGui() {
        websocketScannerActionPanel = WebsocketScannerActionPanel(this)

        val mainPanel = JPanel(BorderLayout(10, 10))
        mainPanel.border = EmptyBorder(10, 10, 10, 10)
        add(mainPanel)

        val bottomPanel = JPanel(BorderLayout())
        bottomPanel.add(websocketScannerStatusPanel, BorderLayout.LINE_START)
        bottomPanel.add(websocketScannerActionPanel, BorderLayout.LINE_END)

        mainPanel.add(websocketScannerTable, BorderLayout.CENTER)
        mainPanel.add(bottomPanel, BorderLayout.PAGE_END)
    }

    fun scan(timeout: Int) {
        websocketScannerActionPanel.buttonsEnable(false)

        websocketScannerTable.clearTable()

        worker = WebsocketScannerSwingWorker(this, timeout)
        worker!!.execute()
    }

    override fun processScanStatus(status: String) {
        websocketScannerStatusPanel.updateStatus(status)
    }

    fun processScanResults(scanResults: List<ScanResult>) {
        websocketScannerTable.setScanResults(scanResults)
    }

    override fun addScanResult(scanResult: ScanResult) {
        websocketScannerTable.addScanResult(scanResult)
    }

    override fun onScanFinished() {
        websocketScannerActionPanel.buttonsEnable(true)
    }
}