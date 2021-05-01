package nl.sajansen.codmw2starter.gui

import java.awt.event.KeyEvent

enum class HotKeysMapping(val key: Int) {
    QuitApplication(KeyEvent.VK_Q),
    Close(KeyEvent.VK_C),
    Save(KeyEvent.VK_S),

    StartServer(KeyEvent.VK_S),
    StartClient(KeyEvent.VK_C),

    OpenMap(KeyEvent.VK_M),
    SendMapConfig(KeyEvent.VK_S),
    PauseLobby(KeyEvent.VK_P),

    ApplicationMenu(KeyEvent.VK_A),
    ShowConfig(KeyEvent.VK_S),
    ShowApplicationInfo(KeyEvent.VK_I),
}