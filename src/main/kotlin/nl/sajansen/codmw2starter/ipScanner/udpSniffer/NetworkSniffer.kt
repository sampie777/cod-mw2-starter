package nl.sajansen.codmw2starter.ipScanner.udpSniffer

import nl.sajansen.codmw2starter.cod.CoDEventListener
import nl.sajansen.codmw2starter.cod.CoDEventListenerSubscriber
import nl.sajansen.codmw2starter.config.Config
import nl.sajansen.codmw2starter.gui.notifications.Notifications
import org.slf4j.LoggerFactory
import java.net.InetAddress
import java.util.*
import kotlin.concurrent.schedule

object NetworkSniffer : CoDEventListener {
    var onOtherAdded: (() -> Unit)? = null
    val others = hashMapOf<String, Other>()
    private val broadcast = Broadcast()
    private var timer: TimerTask? = null
    private var timerInterval: Long = Config.minNicknameBroadcastTimeout

    private val logger = LoggerFactory.getLogger(this::class.java.name)

    init {
        CoDEventListenerSubscriber.register(this)
    }

    fun start() {
        try {
            broadcast.start()
            findOthers()
            scheduleNicknameBroadcast()
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
        Thread { broadcast.send() }.start()
    }

    fun ping() {
        logger.info("Resending broadcast")
        broadcast.onNicknameReceived = ::addNickname
        Thread { broadcast.send() }.start()
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

    private fun scheduleNicknameBroadcast(timeout: Long = timerInterval) {
        if (timer != null) {
            timer!!.cancel()
        }
        timer = Timer().schedule(timeout) {
            broadcast.broadcastNickname()
            scheduleNicknameBroadcast(timerInterval)
        }
    }

    private fun setLongNicknameBroadcastTimerInterval() {
        timerInterval = Config.maxNicknameBroadcastTimeout
    }

    private fun setShortNicknameBroadcastTimerInterval() {
        if (timerInterval > Config.minNicknameBroadcastTimeout) {
            scheduleNicknameBroadcast(Config.minNicknameBroadcastTimeout) // Fire immediately instead of waiting for the long timeout
        }
        timerInterval = Config.minNicknameBroadcastTimeout
    }

    override fun onPauseChanged() = setLongNicknameBroadcastTimerInterval()
    override fun onServerStarted() = setLongNicknameBroadcastTimerInterval()
    override fun onClientStarted() = setLongNicknameBroadcastTimerInterval()
    override fun onNicknameChanged() = setShortNicknameBroadcastTimerInterval()
    override fun onHostChanged() = setShortNicknameBroadcastTimerInterval()
    override fun onUdpPingSending() = setShortNicknameBroadcastTimerInterval()
    override fun onUdpPingSent() = setShortNicknameBroadcastTimerInterval()
}