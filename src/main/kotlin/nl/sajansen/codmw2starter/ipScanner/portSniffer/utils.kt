package nl.sajansen.codmw2starter.ipScanner.portSniffer

import org.slf4j.LoggerFactory
import java.net.NetworkInterface

private val logger = LoggerFactory.getLogger("ipScanner.utils")

fun getNetworkIpAddresses(): List<String> {
    logger.info("Getting network IP addresses from host")
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

fun getLocalNetworkIpAddresses(): List<String> {
    val networkIpAddresses = getNetworkIpAddresses()

    val localNetworkIpAddresses = networkIpAddresses
        .filter { it.startsWith("192.168.") }
        .distinct()
    return localNetworkIpAddresses
}