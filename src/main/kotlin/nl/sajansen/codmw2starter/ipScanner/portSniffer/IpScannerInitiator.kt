package nl.sajansen.codmw2starter.ipScanner.portSniffer

interface IpScannerInitiator {
    fun processScanStatus(status: String)
    fun addScanResult(scanResult: ScanResult)
    fun onScanFinished()
}