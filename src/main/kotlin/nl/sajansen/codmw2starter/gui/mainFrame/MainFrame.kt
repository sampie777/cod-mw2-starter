package nl.sajansen.codmw2starter.gui.mainFrame

import nl.sajansen.codmw2starter.ApplicationInfo
import nl.sajansen.codmw2starter.config.Config
import nl.sajansen.codmw2starter.gui.GUI
import nl.sajansen.codmw2starter.gui.Refreshable
import nl.sajansen.codmw2starter.utils.loadIcon
import org.slf4j.LoggerFactory
import javax.swing.JFrame


class MainFrame : JFrame(), Refreshable {
    private val logger = LoggerFactory.getLogger(MainFrame::class.java.name)

    companion object {
        private var instance: MainFrame? = null
        fun getInstance() = instance

        fun create(): MainFrame = MainFrame()

        fun createAndShow(): MainFrame {
            val frame = create()
            frame.isVisible = true
            return frame
        }
    }

    init {
        instance = this

        GUI.register(this)

        addWindowListener(MainFrameWindowAdapter(this))

        initGUI()
    }

    private fun initGUI() {
        add(MainFramePanel())

        setSize(500, 650)
        setLocationRelativeTo(null)

        isAlwaysOnTop = Config.mainWindowAlwaysOnTop

        defaultCloseOperation = EXIT_ON_CLOSE
        iconImage = loadIcon("/icon-512.png")
        title = ApplicationInfo.name
    }

    fun saveWindowPosition() {
    }
}