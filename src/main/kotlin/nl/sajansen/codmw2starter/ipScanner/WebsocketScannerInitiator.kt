package nl.sajansen.codmw2starter.ipScanner

interface WebsocketScannerInitiator {
    fun processScanStatus(status: String)
    fun addScanResult(scanResult: ScanResult)
    fun onScanFinished()
}