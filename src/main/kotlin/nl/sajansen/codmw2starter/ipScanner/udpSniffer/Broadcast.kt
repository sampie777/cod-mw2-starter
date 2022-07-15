package nl.sajansen.codmw2starter.ipScanner.udpSniffer

import nl.sajansen.codmw2starter.cod.CoD
import nl.sajansen.codmw2starter.cod.CoDEventListenerSubscriber
import nl.sajansen.codmw2starter.config.Config
import nl.sajansen.codmw2starter.gui.notifications.Notifications
import nl.sajansen.codmw2starter.ipScanner.portSniffer.getLocalNetworkIpAddresses
import org.slf4j.LoggerFactory
import java.net.*
import java.util.*

class Broadcast {
    enum class MessageType {
        Request,
        Nickname,
        Hosting,
        NotHosting,
    }

    companion object {
        const val receiveAddress = "0.0.0.0"

        private fun getBroadCastAddresses(): Set<String> {
            val addresses = hashSetOf<String>()

            getLocalNetworkIpAddresses()
                .flatMap { localAddress ->
                    val networkInterface = NetworkInterface.getByInetAddress(InetAddress.getByName(localAddress))
                        ?: return@flatMap listOf("255.255.255.255")

                    networkInterface.interfaceAddresses
                        .filter { it.broadcast != null }
                        .map { it.broadcast.hostAddress }
                }
                .filter { it != null && it.isNotBlank() }
                .forEach { addresses.add(it) }

            return addresses
        }
    }

    var onRequestReceived: ((address: InetAddress, requestId: String?) -> Unit)? = null
    var onNicknameReceived: ((address: InetAddress, name: String, isHosting: Boolean?) -> Unit)? = null
    private val logger = LoggerFactory.getLogger(this::class.java.name)
    private var requestId: String? = null
    private var stopListening = false

    fun start() {
        Thread { receive() }.start()
    }

    fun stop() {
        stopListening = true
    }

    fun send() {
        CoDEventListenerSubscriber.onUdpPingSending()
        requestId = generateRequestId()
        getBroadCastAddresses().forEach { broadcastAddress ->
            val address = InetAddress.getByName(broadcastAddress)
            sendClientInfo(address, broadcast = true)

            val message = createMessage(MessageType.Request, requestId)
            sendTo(address, message, broadcast = true)
        }
        CoDEventListenerSubscriber.onUdpPingSent()
    }

    fun sendIamHostingPing(value: Boolean) {
        CoDEventListenerSubscriber.onUdpPingSending()
        getBroadCastAddresses().forEach { broadcastAddress ->
            val address = InetAddress.getByName(broadcastAddress)
            val message = createMessage(if (value) MessageType.Hosting else MessageType.NotHosting, CoD.getNickname() ?: "unknown")
            sendTo(address, message, broadcast = true)
        }
        CoDEventListenerSubscriber.onUdpPingSent()
    }

    fun broadcastNickname() {
        getBroadCastAddresses().forEach { broadcastAddress ->
            val address = InetAddress.getByName(broadcastAddress)
            sendClientInfo(address, broadcast = true)
        }
    }

    private fun receive() {
        val address = InetAddress.getByName(receiveAddress)

        val socket = try {
            DatagramSocket(Config.udpSnifferPort, address).also {
                it.broadcast = true
                it.soTimeout = 2000
            }
        } catch (e: BindException) {
            logger.error("Failed to open socket for listening: $receiveAddress:${Config.udpSnifferPort}")
            e.printStackTrace()
            Notifications.popup("Port ${Config.udpSnifferPort} already in use")
            return
        } catch (e: Exception) {
            logger.error("Failed to open socket for listening: $receiveAddress:${Config.udpSnifferPort}")
            e.printStackTrace()
            return
        }

        listenOnSocket(socket)

        logger.info("Listener has stopped")
        try {
            socket.close()
        } catch (e: Exception) {
            logger.error("Error during closing of socket")
            e.printStackTrace()
        }
    }

    private fun listenOnSocket(socket: DatagramSocket) {
        logger.info("Listening on $receiveAddress:${Config.udpSnifferPort}...")
        while (!socket.isClosed && !stopListening) {
            val packet = try {
                val buffer = ByteArray(255)
                DatagramPacket(buffer, buffer.size).also {
                    socket.receive(it)
                }
            } catch (e: SocketTimeoutException) {
                continue
            } catch (e: Exception) {
                logger.error("Failed to receive packets")
                e.printStackTrace()
                break
            }

            if (stopListening) {
                break
            }

            val data = bufferToString(packet.data).trim()
            logger.debug("Packet received: from=${packet.address.hostAddress}; data='$data'")

            try {
                handleReceivedMessage(packet.address, data)
            } catch (e: Exception) {
                logger.error("Failed to process received message: '$data'")
                e.printStackTrace()
            }
        }
    }

    fun handleReceivedMessage(address: InetAddress, message: String): Boolean {
        val typeString = message.substringBefore("=")
        val data = if (message.contains("=")) message.substringAfter("=") else null

        val type = try {
            MessageType.valueOf(typeString)
        } catch (e: Exception) {
            logger.error("Invalid type in message: $message")
            e.printStackTrace()
            return false
        }

        when (type) {
            MessageType.Request -> handleRequestMessage(address, data)
            MessageType.Nickname -> processNickname(address, data)
            MessageType.Hosting -> processIamHosting(address, data)
            MessageType.NotHosting -> processIamNotHosting(address, data)
            else -> return false
        }
        return true
    }

    private fun handleRequestMessage(address: InetAddress, data: String?) {
        if (requestId != null && data == requestId) {
            logger.debug("Ignoring own request")
            return
        } else {
            sendClientInfo(address)
        }

        onRequestReceived?.let { it(address, data) }
    }

    private fun processNickname(address: InetAddress, data: String?) {
        if (data == null) {
            logger.info("Cannot process empty nickname")
            return
        }

        onNicknameReceived?.let { it(address, data, null) }
    }

    private fun processIamHosting(address: InetAddress, data: String?) {
        if (data == null) {
            logger.info("Cannot process empty nickname for IamHosting message")
            return
        }

        onNicknameReceived?.let { it(address, data, true) }
    }

    private fun processIamNotHosting(address: InetAddress, data: String?) {
        if (data == null) {
            logger.info("Cannot process empty nickname for IamNotHosting message")
            return
        }

        onNicknameReceived?.let { it(address, data, false) }
    }

    private fun sendClientInfo(address: InetAddress, broadcast: Boolean = false) {
        val message = createMessage(MessageType.Nickname, CoD.getNickname() ?: "unknown")
        sendTo(address, message, broadcast = broadcast)
    }

    private fun sendTo(address: InetAddress, message: String, broadcast: Boolean = false) {
        try {
            val socket = DatagramSocket()
            socket.broadcast = broadcast
            val data = message.toByteArray()
            val packet = DatagramPacket(data, data.size, address, Config.udpSnifferPort)

            logger.debug("Sending packet '$message' to ${address.hostAddress}:${Config.udpSnifferPort}")
            socket.send(packet)
        } catch (e: Exception) {
            logger.error("Failed to send message: '$message' to address: ${address.hostAddress} with broadcast=$broadcast")
            e.printStackTrace()
        }
    }

    fun createMessage(type: MessageType, text: String? = null): String {
        if (text == null) {
            return type.toString()
        }
        return "$type=$text"
    }

    private fun bufferToString(buffer: ByteArray): String {
        val builder = StringBuilder()
        buffer.forEach {
            if (it == 0.toByte()) {
                return builder.toString()
            }
            builder.append(it.toInt().toChar())
        }
        return builder.toString()
    }

    private fun generateRequestId(): String {
        return UUID.randomUUID().toString()
    }
}