package nl.sajansen.codmw2starter.ipScanner.broadcast

import org.junit.Test
import java.net.InetAddress
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.schedule
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BroadcastTest {
    @Test
    fun test() {
        val isRunning = AtomicBoolean(true)
        val broadcast = Broadcast()
        broadcast.start()

        Timer().schedule(500) {
            broadcast.send()
        }

        Timer().schedule(800) {
            broadcast.stop()
            isRunning.set(false)
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