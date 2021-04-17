package nl.sajansen.codmw2starter.gui

import java.awt.event.KeyEvent

enum class HotKeysMapping(val key: Int) {
    QuitApplication(KeyEvent.VK_Q),
    StartServer(KeyEvent.VK_S),
    StartClient(KeyEvent.VK_C),
}