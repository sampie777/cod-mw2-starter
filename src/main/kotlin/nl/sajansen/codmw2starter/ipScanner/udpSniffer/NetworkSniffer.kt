package nl.sajansen.codmw2starter.ipScanner.udpSniffer

import nl.sajansen.codmw2starter.gui.notifications.Notifications
import org.slf4j.LoggerFactory
import java.net.InetAddress

object NetworkSniffer {
    var onOtherAdded: (() -> Unit)? = null
    val others = hashMapOf<String, Other>()
    private val broadcast = Broadcast()

    private val logger = LoggerFactory.getLogger(this::class.java.name)

    fun start() {
        try {
            broadcast.start()
            findOthers()
        } catch (t: Throwable) {
            logger.error("Failed to start network sniffing")
            t.printStackTrace()
            Notifications.popup("Failed to start network sniffing: ${t.localizedMessage}")
        }
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

    fun sendImHostingPing() {
        logger.info("Sending Iam Hosting broadcast")
        broadcast.onNicknameReceived = ::addNickname
        broadcast.sendIamHostingPing()
    }

    private fun addNickname(address: InetAddress, name: String, isHosting: Boolean) {
        logger.debug("Received nickname: $name from ${address.hostAddress}")
        val other = Other(address.hostAddress, address.hostName, name, isHosting)
        others[address.hostAddress] = other
        onOtherAdded?.invoke()
    }
}