package nl.sajansen.codmw2starter.io

import nl.sajansen.codmw2starter.config.Config
import nl.sajansen.codmw2starter.exitApplication
import nl.sajansen.codmw2starter.gui.notifications.Notifications
import org.ini4j.Ini
import org.slf4j.LoggerFactory
import java.io.File
import java.nio.file.Paths


object CoD {
    private val logger = LoggerFactory.getLogger(CoD::class.java)

    private fun loadProperties(): Ini {
        logger.info("Loading properties file")
        val file = File(Config.serverPropertiesFile)

        if (!file.exists()) {
            Notifications.popup("No server configuration file found (alterIWnet.ini) in current directory")
            exitApplication()
        }

        return Ini(file)
    }

    fun getHost(): String? {
        val properties = loadProperties()
        return properties["Configuration"]?.get("Server")
    }

    fun setHost(host: String): Boolean {
        logger.info("Saving new host to file: $host")

        val properties = loadProperties()
        properties["Configuration"]?.set("Server", host)

        val file = File(Config.serverPropertiesFile)
        try {
            properties.store(file)
        } catch (e: Exception) {
            logger.error("Failed to save host to file")
            e.printStackTrace()
            Notifications.popup("Failed to save host to file: ${e.localizedMessage}", "CoD")
            return false
        }
        return true
    }

    fun startServer() {
        logger.info("Starting server...")
        try {
            val directory = File(Config.serverExePath)
            val file = Paths.get(directory.absolutePath, Config.serverExeFile)
            Runtime.getRuntime().exec(file.toString(), null, directory)
        } catch (t: Throwable) {
            logger.error("Failed to start server")
            t.printStackTrace()
            Notifications.popup("Failed to start server: ${t.localizedMessage}", "CoD")
        }
    }

    fun startClient() {
        logger.info("Starting client...")
        try {
            val directory = File(Config.clientExePath)
            val file = Paths.get(directory.absolutePath, Config.clientExeFile)
            Runtime.getRuntime().exec(file.toString(), null, directory)
        } catch (t: Throwable) {
            logger.error("Failed to start client")
            t.printStackTrace()
            Notifications.popup("Failed to start client: ${t.localizedMessage}", "CoD")
        }
    }
}