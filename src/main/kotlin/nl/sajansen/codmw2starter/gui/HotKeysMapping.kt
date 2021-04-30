package nl.sajansen.codmw2starter.gui

import java.awt.event.KeyEvent

enum class HotKeysMapping(val key: Int) {
    QuitApplication(KeyEvent.VK_Q),
    CloseButton(KeyEvent.VK_C),
    SaveButton(KeyEvent.VK_S),

    StartServer(KeyEvent.VK_S),
    StartClient(KeyEvent.VK_C),

    OpenMapConfig(KeyEvent.VK_M),
    SendMapConfig(KeyEvent.VK_S),
}