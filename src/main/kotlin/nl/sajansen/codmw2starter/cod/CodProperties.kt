package nl.sajansen.codmw2starter.cod

import nl.sajansen.codmw2starter.ApplicationInfo
import nl.sajansen.codmw2starter.config.Config
import nl.sajansen.codmw2starter.exitApplication
import nl.sajansen.codmw2starter.gui.notifications.Notifications
import nl.sajansen.codmw2starter.gui.other.FileChooser
import nl.sajansen.codmw2starter.utils.OS
import nl.sajansen.codmw2starter.utils.getOS
import nl.sajansen.codmw2starter.utils.tryOrLog
import org.ini4j.Ini
import org.slf4j.LoggerFactory
import java.awt.EventQueue
import java.io.File
import java.nio.file.Paths
import javax.swing.JFileChooser

object CodProperties {
    private val logger = LoggerFactory.getLogger(CodProperties::class.java)

    private fun fetch(): Ini? {
        val file = File(CoD.getServerPropertiesFile())
        logger.debug("Loading CoD properties file: '${file.absolutePath}'")

        if (file.exists()) {
            return Ini(file)
        }

        logger.info("CoD properties file '${file.absolutePath}' does not exist")

        // Create a properties file if it doesn't exist and load the new file
        if (File(Config.gameDirectory).exists()) {
            try {
                createPropertiesFile()
            } catch (e: PropertiesFileError) {
                logger.error(e.localizedMessage)
                e.printStackTrace()
                Notifications.popup(
                    "Failed to initialize alterIWnet.ini properties file.\n\n${e.localizedMessage}",
                    "File not found"
                )
                exitApplication()
                return null
            }
            return fetch()
        }

        logger.info("Configured game directory '${Config.gameDirectory}' does not exist")

        // Search for the game directory
        val possibleGameLocations = getPossibleGameLocations()
        possibleGameLocations.forEach {
            val directory = File(it)

            if (!directoryContainsGameFiles(directory)) {
                return@forEach
            }

            logger.info("Found possible game directory: '${directory.absolutePath}'")
            Config.gameDirectory = directory.absolutePath
            Config.save()
            return fetch()
        }

        logger.info("No possible game directory found")

        val promptedLocation = promptForGameLocation()
        if (promptedLocation != null && promptedLocation.exists()) {
            Config.gameDirectory = promptedLocation.absolutePath
            Config.save()
            logger.debug("Saved config gameDirectory: ${Config.gameDirectory}")
            return fetch()
        }

        logger.warn("Prompted game directory does not exist")

        Notifications.popup("Failed to find game directory", "File not found")
        exitApplication()
        return null
    }

    private fun createPropertiesFile() {
        logger.info("Creating new CoD properties file")

        val exampleFile = ApplicationInfo::class.java.getResource("/nl/sajansen/codmw2starter/alterIWnet.ini.example")
            ?: throw PropertiesFileError("Could not open example CoD properties file.")

        val content = try {
            exampleFile.readText(Charsets.UTF_8)
                .replace("{name}", generateRandomName())
        } catch (t: Throwable) {
            throw PropertiesFileError("Failed to read example CoD properties file", t)
        }

        val file = File(CoD.getServerPropertiesFile())

        try {
            file.createNewFile()
        } catch (t: Throwable) {
            throw PropertiesFileError("Failed to create new CoD properties file in '${file.absolutePath}'", t)
        }

        try {
            file.writeText(content)
        } catch (t: Throwable) {
            throw PropertiesFileError("Failed to write new CoD properties to new file '${file.absolutePath}'", t)
        }

        if (!file.exists()) {
            throw PropertiesFileError("Failed to create and write to new CoD properties file in '${file.absolutePath}'")
        }

        logger.info("Created new CoD properties file in '${file.absolutePath}'")
    }

    private fun generateRandomName(): String {
        val randomNames = listOf(
            "ItIsYeElephant",
            "The Brave Gamer",
            "IHasFeet",
            "DrCourageous",
            "BraveFeetLMAO",
            "IHasInsides",
            "StupidElbows",
            "IamKevin",
            "Gamerdonkey",
            "DrBrunette",
            "TotalDonkey",
            "Flapoor",
        )
        return randomNames.random()
    }

    private fun getPossibleGameLocations(): List<String> {
        val list = arrayListOf<String>()

        val possibleGameDirectoryNames = listOf(
            "Modern Warfare 2",
            "Call of Duty - Modern Warfare 2",
            "Call of Duty Modern Warfare 2",
            "CoD - Modern Warfare 2",
            "CoD Modern Warfare 2",
            Paths.get("Modern Warfare 2", "Multiplayer").toString(),
            Paths.get("Call of Duty - Modern Warfare 2", "Multiplayer").toString(),
            Paths.get("Call of Duty Modern Warfare 2", "Multiplayer").toString(),
            Paths.get("CoD - Modern Warfare 2", "Multiplayer").toString(),
            Paths.get("CoD Modern Warfare 2", "Multiplayer").toString(),
            "Multiplayer",
        )

        val homeDirectory = System.getProperty("user.home")
        val currentDirectory = System.getProperty("user.dir")
        val configuredDirectory = Config.gameDirectory
        list.add(currentDirectory)
        tryOrLog { list.add(Paths.get(File(currentDirectory).parentFile.absolutePath).toString()) }
        tryOrLog { list.add(Paths.get(File(configuredDirectory).parentFile.absolutePath).toString()) }

        val os = getOS()
        possibleGameDirectoryNames.forEach {
            tryOrLog { list.add(Paths.get(currentDirectory, it).toString()) }
            tryOrLog { list.add(Paths.get(File(currentDirectory).parentFile.absolutePath, it).toString()) }
            tryOrLog { list.add(Paths.get(File(configuredDirectory).parentFile.absolutePath, it).toString()) }
            tryOrLog { list.add(Paths.get(homeDirectory, it).toString()) }
            tryOrLog { list.add(Paths.get(homeDirectory, "Documents", it).toString()) }
            tryOrLog { list.add(Paths.get(homeDirectory, "Downloads", it).toString()) }
            tryOrLog { list.add(Paths.get(homeDirectory, "Saved Games", it).toString()) }

            if (os == OS.Windows) {
                tryOrLog { list.add(Paths.get(System.getenv("ProgramFiles"), it).toString()) }
                tryOrLog { list.add(Paths.get(System.getenv("ProgramFiles(X86)"), it).toString()) }
            }
        }
        return list
    }

    private fun promptForGameLocation(): File? {
        logger.info("Prompting user for game location")
        var file: File? = null
        EventQueue.invokeAndWait {
            file = FileChooser(
                file = System.getProperty("user.home"),
                title = "Select game directory",
                type = FileChooser.Type.Open,
                selectionMode = JFileChooser.DIRECTORIES_ONLY,
            ).prompt()
        }
        return file
    }

    private fun directoryContainsGameFiles(directory: File): Boolean {
        if (!directory.exists() || !directory.isDirectory) {
            logger.info("File '${directory.absolutePath}' is not an existing directory")
            return false
        }

        val gameFilesNames = listOf(
            "iw4mp.exe",
            "iw4sp.exe",
            "IWNetServer.exe",
            "alterIWnet.ini",
            "Alter IWNET App.exe",
            "alterIWnet_configure.exe",
            "Alter IWNET Map Launcher.exe",
        )

        var matches = 0
        directory.listFiles()?.forEach {
            if (gameFilesNames.contains(it.name)) {
                matches++
            }
        }

        val matchPercentage = matches.toFloat() / gameFilesNames.size
        logger.info("Directory '${directory.absolutePath}' matches $matchPercentage %")
        return matchPercentage > 0.6
    }

    private fun save(properties: Ini) {
        val file = File(CoD.getServerPropertiesFile())
        try {
            properties.store(file)
        } catch (e: Exception) {
            logger.error("Failed to save properties to file")
            e.printStackTrace()
            throw e
        }
    }

    fun getHost(): String? {
        val properties = fetch() ?: return null
        return properties["Configuration"]?.get("Server")
    }

    fun setHost(host: String): Boolean {
        logger.info("Saving new host to file: $host")

        val properties = fetch() ?: return false
        properties["Configuration"]?.set("Server", host)

        try {
            save(properties)
        } catch (e: Exception) {
            Notifications.popup("Failed to save host to file: ${e.localizedMessage}", "CoD")
            return false
        }

        CoDEventListenerSubscriber.onHostChanged()
        return true
    }

    fun getNickname(): String? {
        val properties = fetch() ?: return null
        return properties["Configuration"]?.get("Nickname")
    }

    fun setNickname(Nickname: String): Boolean {
        logger.info("Saving new nickname to file: $Nickname")

        val properties = fetch() ?: return false
        properties["Configuration"]?.set("Nickname", Nickname)

        try {
            save(properties)
        } catch (e: Exception) {
            Notifications.popup("Failed to save nickname to file: ${e.localizedMessage}", "CoD")
            return false
        }

        CoDEventListenerSubscriber.onNicknameChanged()
        return true
    }

    fun initialize() {
        try {
            fetch()
        } catch (e: PropertiesFileError) {
            Notifications.popup(e.localizedMessage, "File not found")
            exitApplication()
        } catch (e: Exception) {
            logger.error("Could not load properties from file")
            e.printStackTrace()
            Notifications.popup(e.localizedMessage ?: e.message ?: e.toString(), "Could not load properties")
            exitApplication()
        }
    }
}