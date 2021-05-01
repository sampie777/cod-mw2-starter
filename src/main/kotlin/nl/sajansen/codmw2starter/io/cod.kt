package nl.sajansen.codmw2starter.io

import copyString
import focusWindow
import nl.sajansen.codmw2starter.config.Config
import nl.sajansen.codmw2starter.exitApplication
import nl.sajansen.codmw2starter.gui.notifications.Notifications
import org.ini4j.Ini
import org.slf4j.LoggerFactory
import pasteText
import java.awt.Desktop
import java.io.File


object CoD {
    enum class Executioner {
        Desktop,
        Runtime,
        ProcessBuilder1,
        ProcessBuilder2,
    }

    private val logger = LoggerFactory.getLogger(CoD::class.java)
    var isLobbyPaused = false

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
        execute("server", Config.serverExeFile)
    }

    fun startClient() {
        logger.info("Starting client...")
        execute("client", Config.clientExeFile)
    }

    fun pauseLobby() {
        if (isLobbyPaused) {
            logger.info("Unpausing lobby")
            sendCommand(
                "party_maxplayers ${Config.maxPlayers}; " +
                        "party_minplayers ${Config.minPlayers}; "
            )
        } else {
            logger.info("Pausing lobby")
            sendCommand(
                "party_maxplayers 64; " +
                        "party_minplayers 64; "
            )
        }
        isLobbyPaused = !isLobbyPaused
    }

    fun sendCommand(string: String) {
        copyString(string)
        if (focusWindow(Config.consoleTitle)) {
            pasteText(string)
        }
    }

    private fun execute(type: String, exeFile: String) {
        try {
            val file = File(exeFile)
            val directory = file.parentFile

            logger.info("Using executioner: ${Config.executioner} for file '${file.name}' in '${directory.absolutePath}'")
            when (Config.executioner) {
                Executioner.Runtime -> Runtime.getRuntime().exec(file.toString(), null, directory)
                Executioner.Desktop -> Desktop.getDesktop().open(file)
                Executioner.ProcessBuilder1 -> {
                    val processBuilder = ProcessBuilder(exeFile)
                    processBuilder.directory(directory)
                    processBuilder.start()
                }
                Executioner.ProcessBuilder2 -> ProcessBuilder(file.toString()).start()
                else -> throw IllegalArgumentException("Invalid executioner defined: ${Config.executioner}")
            }
        } catch (t: Throwable) {
            logger.error("Failed to start $type")
            t.printStackTrace()
            Notifications.popup("Failed to start $type: ${t.localizedMessage}", "CoD")
        }
    }
}