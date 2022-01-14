package nl.sajansen.codmw2starter.ipScanner.portSniffer

import org.slf4j.LoggerFactory
import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener
import javax.swing.SwingWorker

class WebsocketScannerSwingWorker(
    private val initiator: IpScannerInitiator,
    private val timeout: Int = 500
) : SwingWorker<Boolean, Void>(),
    PropertyChangeListener {
    private val logger = LoggerFactory.getLogger(WebsocketScannerSwingWorker::class.java.name)

    override fun doInBackground(): Boolean {
        val processStatus = WebsocketScannerProcessStatus(this)
        val websocketScanner = WebsocketScanner(processStatus, timeout)
        websocketScanner.scan()

        initiator.processScanStatus("Scan finished")
        initiator.onScanFinished()
        return true
    }

    override fun propertyChange(event: PropertyChangeEvent?) {
        if (event == null) {
            return
        }

        if (WebsocketScannerProcessStatus.STATUS_PROGRESS == event.propertyName) {
            initiator.processScanStatus(event.newValue as String)
        }

        if (WebsocketScannerProcessStatus.VALUE_PROGRESS == event.propertyName) {
            initiator.addScanResult(event.newValue as ScanResult)
        }
    }
}