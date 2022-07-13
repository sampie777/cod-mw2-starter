import com.sun.jna.Native
import com.sun.jna.Pointer
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinUser
import nl.sajansen.codmw2starter.config.Config
import nl.sajansen.codmw2starter.gui.notifications.Notifications
import nl.sajansen.codmw2starter.utils.OS
import nl.sajansen.codmw2starter.utils.getOS
import org.slf4j.LoggerFactory
import java.awt.Desktop
import java.awt.Robot
import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.StringSelection
import java.awt.event.KeyEvent
import java.net.URI


private val logger = LoggerFactory.getLogger("utils.jna")

class WindowFinder(private val windowTitle: String) : WinUser.WNDENUMPROC {
    private val logger = LoggerFactory.getLogger(WindowFinder::class.java.name)

    var windowHandle: WinDef.HWND? = null
    var count: Int = 0

    override fun callback(hWnd: WinDef.HWND, arg1: Pointer?): Boolean {
        val windowText = CharArray(512)
        User32.INSTANCE.GetWindowText(hWnd, windowText, 512)
        val wText = Native.toString(windowText)

        if (wText.isEmpty()) {
            return true
        }

        if (wText.toLowerCase().contains(windowTitle)) {
            windowHandle = hWnd
            return false
        }
        return true
    }
}

fun findWindowHandle(windowTitle: String): WinDef.HWND? {
    if (getOS() != OS.Windows) {
        logger.warn("Must run on Windows to find the window")
        Notifications.popup("Must run on Windows operating system", "KeyStroke")
        return null
    }

    val windowFinder = WindowFinder(windowTitle.toLowerCase())
    User32.INSTANCE.EnumWindows(windowFinder, null)

    if (windowFinder.windowHandle != null) {
        return windowFinder.windowHandle
    }

    logger.warn("Failed to find window: $windowTitle")
    Notifications.popup("Could not find '$windowTitle' window", "KeyStroke")
    return null
}

fun focusWindow(windowTitle: String): Boolean {
    val windowHandle = findWindowHandle(windowTitle) ?: return false

    logger.info("Set focus to window $windowTitle")
    User32.INSTANCE.SetForegroundWindow(windowHandle)
    User32.INSTANCE.SetFocus(windowHandle)
    return true
}

fun copyString(string: String) {
    logger.info("Copying text to clipboard")
    val stringSelection = StringSelection(string)
    val clipboard: Clipboard = Toolkit.getDefaultToolkit().systemClipboard
    clipboard.setContents(stringSelection, stringSelection)
}

fun pasteText(string: String) {
    logger.info("Pasting text from clipboard")
    val robot = Robot()
    robot.delay(Config.sendPasteDelayMs)
    robot.keyPress(KeyEvent.VK_CONTROL)
    robot.keyPress(KeyEvent.VK_V)
    robot.keyRelease(KeyEvent.VK_V)
    robot.keyRelease(KeyEvent.VK_CONTROL)
    robot.delay(100)
    robot.keyPress(KeyEvent.VK_ENTER)
    robot.keyRelease(KeyEvent.VK_ENTER)
}

fun openWebURL(url: String, subject: String = "Webbrowser"): Boolean {
    if (!Desktop.isDesktopSupported()) {
        logger.warn("Cannot open link '$url': not supported by host")
        return false
    }
    try {
        Desktop.getDesktop().browse(URI(url))
        return true
    } catch (t: Throwable) {
        logger.error("Error during opening link '$url'")
        t.printStackTrace()
        Notifications.popup("Failed to open link: $url", subject)
    }
    return false
}