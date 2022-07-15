package nl.sajansen.codmw2starter.gui.mainFrame

import nl.sajansen.codmw2starter.ApplicationInfo
import nl.sajansen.codmw2starter.config.Config
import nl.sajansen.codmw2starter.globalHooks.GlobalKeyboardHook
import nl.sajansen.codmw2starter.gui.menu.MenuBar
import nl.sajansen.codmw2starter.utils.loadIcon
import org.slf4j.LoggerFactory
import javax.swing.JFrame


class MainFrame : JFrame() {
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

    private val logger = LoggerFactory.getLogger(MainFrame::class.java.name)
    private val mainFramePanel = MainFramePanel()

    init {
        instance = this

        addWindowListener(MainFrameWindowAdapter(this))

        initGUI()

        GlobalKeyboardHook.register()
    }

    private fun initGUI() {
        setSize(580, 450)
        setLocationRelativeTo(null)

        add(mainFramePanel)

        isAlwaysOnTop = Config.mainWindowAlwaysOnTop

        jMenuBar = MenuBar()
        defaultCloseOperation = EXIT_ON_CLOSE
        iconImage = loadIcon("images/icon-512.png")
        title = ApplicationInfo.name
    }

    fun saveWindowPosition() {
    }

    fun activatePanel(panel: MainFramePanel.Panel) = mainFramePanel.activatePanel(panel)
}