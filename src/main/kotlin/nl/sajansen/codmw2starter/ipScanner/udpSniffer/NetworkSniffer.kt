package nl.sajansen.codmw2starter.ipScanner.udpSniffer

import org.slf4j.LoggerFactory
import java.net.InetAddress

object NetworkSniffer {
    var onOtherAdded: ((other: Other) -> Unit)? = null
    val others = hashMapOf<String, Other>()
    private val broadcast = Broadcast()

    private val logger = LoggerFactory.getLogger(this::class.java.name)

    fun start() {
        broadcast.start()
        findOthers()
    }

    fun findOthers() {
        logger.info("Finding for others...")
        broadcast.onNicknameReceived = ::addNickname
        others.clear()
        broadcast.send()
    }

    fun ping() {
        logger.info("Resending broadcast")
        broadcast.onNicknameReceived = ::addNickname
        broadcast.send()
    }

    private fun addNickname(address: InetAddress, name: String) {
        logger.debug("Received nickname: $name from ${address.hostAddress}")
        val other = Other(address.hostAddress, address.hostName, name)
        others[address.hostAddress] = other
        onOtherAdded?.let { it(other) }
    }
}