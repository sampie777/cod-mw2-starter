package nl.sajansen.codmw2starter.ipScanner.udpSniffer

import org.junit.Test

class NetworkSnifferTest {
    @Test
    fun test() {
        NetworkSniffer.start()

        while(NetworkSniffer.others.size == 0) {}
    }
}