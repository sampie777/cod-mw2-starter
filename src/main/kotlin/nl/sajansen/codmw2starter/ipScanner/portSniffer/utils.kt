package nl.sajansen.codmw2starter.ipScanner.portSniffer

import nl.sajansen.codmw2starter.config.Config
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

    return networkIpAddresses
        .filter { !Config.useIpv4Only || isIpv4(it) }
        .distinct()
}

fun isIpv4(it: String): Boolean = it.isNotEmpty() && it[0].isDigit() && !it.contains(":")