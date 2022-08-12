package nl.sajansen.codmw2starter.config

import nl.sajansen.codmw2starter.cod.CoD
import nl.sajansen.codmw2starter.cod.GameType
import nl.sajansen.codmw2starter.cod.MapName
import nl.sajansen.codmw2starter.cod.Spectate
import nl.sajansen.codmw2starter.globalHooks.NativeKeyEventJson
import org.slf4j.LoggerFactory
import java.lang.reflect.Field
import java.lang.reflect.Modifier

object Config {
    private val logger = LoggerFactory.getLogger(Config.toString())

    /* ********************************** */

    // Main window
    var mainWindowAlwaysOnTop: Boolean = false

    // File paths
    var gameDirectory: String = "path/to/game/directory"

    // Execution
    val availableExecutioners: String = CoD.Executioner.values().joinToString(";")
    var executioner: CoD.Executioner = CoD.Executioner.Auto
    var delayBetweenServerStartAndClientStart = 2000L
    var useWineOnUnix = true

    // Map config
    var consoleTitle: String = "IW4 Console"
    var map: MapName = MapName.Afghan
    var gameType: GameType = GameType.Free_For_All
    var timeLimit: Int = 10
    var scoreLimit: Int = 750
    var minPlayers: Int = 2
    var maxPlayers: Int = 16
    var killcam: Boolean = true
    var spectate: Spectate = Spectate.Free
    var codeTemplate: String = "party_hostmigration 0; " +
            "party_connecttimeout 1; " +
            "ui_mapname {{map}}; " +
            "ui_gametype {{gameType}}; " +
            "scr_{{gameType}}_timelimit {{timeLimit}}; " +
            "scr_{{gameType}}_scorelimit {{scoreLimit}}; " +
            "party_maxplayers {{maxPlayers}}; " +
            "party_minplayers {{minPlayers}}; " +
            "scr_game_spectatetype {{spectate}}; " +
            "scr_game_allowkillcam {{killcam}}"
    var sendPasteDelayMs: Int = 100
    var pauseSetPlayers: Int = 18

    // Key bindings
    var globalKeyEventPauseLobby: NativeKeyEventJson? = null

    // Player detection
    var ipScannerTimeout: Int = 300
    var udpSnifferPort = 2302
    var udpSnifferFilterOutLocalIps = true
    var useIpv4Only = true
    var minNicknameBroadcastTimeout: Long = 3000
    var maxNicknameBroadcastTimeout: Long = 30000

    // Other
    var updatesCheckForUpdates: Boolean = true

    fun load() {
        try {
            PropertyLoader.load()
            PropertyLoader.loadConfig(this::class.java)
        } catch (e: Exception) {
            logger.error("Failed to load Config")
            e.printStackTrace()
        }
    }

    fun save() {
        try {
            if (PropertyLoader.saveConfig(this::class.java)) {
                PropertyLoader.save()
            }
        } catch (e: Exception) {
            logger.error("Failed to save Config")
            e.printStackTrace()
        }
    }

    fun get(key: String): Any? {
        try {
            return javaClass.getDeclaredField(key).get(this)
        } catch (e: Exception) {
            logger.error("Could not get config key $key")
            e.printStackTrace()
        }
        return null
    }

    fun set(key: String, value: Any?) {
        try {
            javaClass.getDeclaredField(key).set(this, value)
        } catch (e: Exception) {
            logger.error("Could not set config key $key")
            e.printStackTrace()
        }
    }

    fun enableWriteToFile(value: Boolean) {
        PropertyLoader.writeToFile = value
    }

    fun fields(): List<Field> {
        val fields = javaClass.declaredFields.filter {
            it.name != "INSTANCE" && it.name != "logger"
                    && Modifier.isStatic(it.modifiers)
        }
        fields.forEach { it.isAccessible = true }
        return fields
    }
}