package nl.sajansen.codmw2starter

import nl.sajansen.codmw2starter.config.Config
import nl.sajansen.codmw2starter.gui.GUI
import nl.sajansen.codmw2starter.gui.mainFrame.MainFrame
import nl.sajansen.codmw2starter.gui.notifications.Notifications
import nl.sajansen.codmw2starter.utils.getCurrentJarDirectory
import org.slf4j.LoggerFactory
import java.awt.EventQueue
import kotlin.system.exitProcess

private val logger = LoggerFactory.getLogger("Application")

fun main(args: Array<String>) {
    ApplicationRuntimeSettings.testing = false

    logger.info("Starting application ${ApplicationInfo.artifactId}:${ApplicationInfo.version}")
    logger.info("Executing JAR directory: " + getCurrentJarDirectory(ApplicationInfo).absolutePath)

    Notifications.enablePopups = !ApplicationRuntimeSettings.testing

    Config.enableWriteToFile(!ApplicationRuntimeSettings.virtualConfig && !ApplicationRuntimeSettings.testing)
    Config.load()
    Config.save()

    EventQueue.invokeLater {
        MainFrame.createAndShow()
    }
}

fun exitApplication() {
    logger.info("Shutting down application")

    MainFrame.getInstance()?.saveWindowPosition()

    try {
        logger.info("Closing windows...")
        GUI.windowClosing(MainFrame.getInstance())
    } catch (t: Throwable) {
        logger.warn("Failed to correctly close windows")
        t.printStackTrace()
    }

    logger.info("Shutdown finished")
    exitProcess(0)
}
