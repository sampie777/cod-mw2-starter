package nl.sajansen.codmw2starter.cod

import copyString
import focusWindow
import nl.sajansen.codmw2starter.config.Config
import nl.sajansen.codmw2starter.gui.notifications.Notifications
import nl.sajansen.codmw2starter.ipScanner.udpSniffer.NetworkSniffer
import org.slf4j.LoggerFactory
import pasteText
import java.awt.Desktop
import java.io.File
import java.nio.file.Paths


object CoD {
    enum class Executioner {
        Desktop,
        Runtime,
        ProcessBuilder1,
        ProcessBuilder2,
    }

    private val logger = LoggerFactory.getLogger(CoD::class.java)
    var isLobbyPaused = false
        set(value) {
            field = value
            CoDEventListenerSubscriber.onPauseChanged()
        }

    const val serverPropertiesFileName = "alterIWnet.ini"
    const val serverExeFileName = "IWNetServer.exe"
    const val clientExeFileName = "iw4mp.exe"
    fun getServerPropertiesFile() = Paths.get(Config.gameDirectory, serverPropertiesFileName).toString()
    fun getServerExeFile() = Paths.get(Config.gameDirectory, serverExeFileName).toString()
    fun getClientExeFile() = Paths.get(Config.gameDirectory, clientExeFileName).toString()

    fun getHost(): String? = CodProperties.getHost()
    fun setHost(host: String) = CodProperties.setHost(host)

    fun getNickname() = CodProperties.getNickname()
    fun setNickname(name: String) = CodProperties.setNickname(name)

    fun startServer() {
        logger.info("Starting server...")
        NetworkSniffer.sendImHostingPing(true)
        execute("server", getServerExeFile())
        CoDEventListenerSubscriber.onServerStarted()
    }

    fun startClient() {
        logger.info("Starting client...")
        execute("client", getClientExeFile())
        CoDEventListenerSubscriber.onClientStarted()
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
                "party_maxplayers ${Config.pauseSetPlayers}; " +
                        "party_minplayers ${Config.pauseSetPlayers}; "
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