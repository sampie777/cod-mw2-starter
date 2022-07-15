package nl.sajansen.codmw2starter.ipScanner.udpSniffer

import nl.sajansen.codmw2starter.ApplicationRuntimeSettings
import nl.sajansen.codmw2starter.config.Config
import org.junit.Ignore
import org.junit.Test
import java.net.InetAddress
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.schedule
import kotlin.test.BeforeTest
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BroadcastTest {
    @BeforeTest
    fun before() {
        ApplicationRuntimeSettings.testing = true
        Config.gameDirectory = javaClass.classLoader.getResource("nl/sajansen/codmw2starter")!!.file
    }

    @Ignore
    @Test
    fun test() {
        return  // for maven test not ignoring tests
        val isRunning = AtomicBoolean(true)
        val broadcast = Broadcast()
        val stopTimer = Timer()
        broadcast.start()
        broadcast.onRequestReceived = { _, _ ->
            broadcast.stop()
            isRunning.set(false)
            stopTimer.cancel()
            stopTimer.purge()
        }

        stopTimer.schedule(800) {
            broadcast.stop()
            isRunning.set(false)
        }

        // When
        Timer().schedule(500) {
            broadcast.send()
        }

        while (isRunning.get()) {
        }
    }

    @Test
    fun `test handle receiving message with type but no payload gives no error`() {
        val broadcast = Broadcast()
        assertTrue(
            broadcast.handleReceivedMessage(
                InetAddress.getLoopbackAddress(),
                broadcast.createMessage(Broadcast.MessageType.Request)
            )
        )
    }

    @Test
    fun `test handle receiving message with type and payload gives no error`() {
        val broadcast = Broadcast()
        assertTrue(
            broadcast.handleReceivedMessage(
                InetAddress.getLoopbackAddress(),
                broadcast.createMessage(Broadcast.MessageType.Request, "123")
            )
        )
    }

    @Test
    fun `test handle receiving message with type and dangerous payload gives no error`() {
        val broadcast = Broadcast()
        assertTrue(
            broadcast.handleReceivedMessage(
                InetAddress.getLoopbackAddress(),
                broadcast.createMessage(Broadcast.MessageType.Request, "12=3")
            )
        )
    }

    @Test
    fun `test creating messages`() {
        val broadcast = Broadcast()
        assertEquals("Request", broadcast.createMessage(Broadcast.MessageType.Request))
        assertEquals("Request=123", broadcast.createMessage(Broadcast.MessageType.Request, "123"))
    }
}