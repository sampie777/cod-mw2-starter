package nl.sajansen.codmw2starter.gui.mainFrame

import nl.sajansen.codmw2starter.exitApplication
import nl.sajansen.codmw2starter.gui.GUI
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent


class MainFrameWindowAdapter(private val frame: MainFrame) : WindowAdapter() {
    override fun windowClosing(winEvt: WindowEvent) {
        exitApplication()
    }

    override fun windowActivated(e: WindowEvent?) {
        super.windowActivated(e)
        GUI.currentFrame = frame
    }
}