package nl.sajansen.codmw2starter.ipScanner

import java.net.InetAddress
import java.net.NetworkInterface
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.logging.Logger


class ScanResult(
    val ip: String,
    val port: Int,
    val open: Boolean,
    val hostName: String? = null
) {
    override fun toString(): String = "${ip}:${port} (${hostName})"
}


class WebsocketScanner(private val processStatus: WebsocketScannerProcessStatus, private val timeout: Int = 500, private val port: Int = 4444) {
    private val logger = Logger.getLogger(WebsocketScanner::class.java.name)

    private val threadPoolSize = 20

    fun scan(): List<ScanResult> {
        logger.info("Creating websocket scanner with timeout: $timeout ms for ports: $port")
        val networkIpAddresses = getNetworkIpAddresses()

        val localNetworkIpAddresses = networkIpAddresses
            .filter { it.startsWith("192.168.") }
            .distinct()

        val scanResultFutures: ArrayList<Future<ScanResult>> = scanIpAddresses(localNetworkIpAddresses)

        processStatus.setState("Filtering scan results")
        val addressesFound = scanResultFutures
            .map { it.get() }
            .filter { it.open }

        if (addressesFound.isEmpty()) {
            logger.info("No open websockets found")
            return addressesFound
        }

        return addressesFound
    }

    private fun getNetworkIpAddresses(): List<String> {
        logger.info("Getting network IP addresses from host")
        processStatus.setState("Getting network IP addresses")
        val networkIpAddresses = ArrayList<String>()

        NetworkInterface.getNetworkInterfaces()
            .iterator()
            .forEach { networkInterface ->
                networkInterface.inetAddresses
                    .iterator()
                    .forEach { networkIpAddresses.add(it.hostAddress) }
            }

        return networkIpAddresses
    }

    private fun scanIpAddresses(localNetworkIpAddresses: List<String>): ArrayList<Future<ScanResult>> {
        val es: ExecutorService = Executors.newFixedThreadPool(threadPoolSize)
        val scanResultFutures: ArrayList<Future<ScanResult>> = ArrayList()

        // Try IP addresses
        localNetworkIpAddresses
            .map { it.substringBeforeLast(".") }
            .distinct()
            .forEach {
                logger.info("Scanning IP addresses for IP range: $it.*")
                for (ip in 1..255) {
                    scanResultFutures.add(scanAddressPort(es, "$it.$ip", port, timeout))
                }
            }
        es.shutdown()
        return scanResultFutures
    }

    private fun scanAddressPort(es: ExecutorService, ip: String, port: Int, timeout: Int): Future<ScanResult> {
        return es.submit(Callable {
            try {
                logger.fine("Probing: $ip:$port")
                processStatus.setState("Probing: $ip:$port")

                val ipArray = ip.split(".")
                    .map { it.toInt().toByte() }
                    .toByteArray()
                val inetAddress = InetAddress.getByAddress(ipArray)

                if (!inetAddress.isReachable(timeout)) {
                    return@Callable ScanResult(ip, port, false)
                }

                val scanResult = ScanResult(ip, port, true, inetAddress.hostName)
                logger.info("Found host: $scanResult")
                processStatus.addScanResult(scanResult)

                return@Callable scanResult
            } catch (ex: Exception) {
                return@Callable ScanResult(ip, port, false)
            }
        })
    }
}