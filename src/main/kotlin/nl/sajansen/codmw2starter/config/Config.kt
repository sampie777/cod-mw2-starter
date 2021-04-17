package nl.sajansen.codmw2starter.config

import nl.sajansen.codmw2starter.ApplicationInfo
import nl.sajansen.codmw2starter.utils.getCurrentJarDirectory
import org.slf4j.LoggerFactory
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.nio.file.Paths

object Config {
    private val logger = LoggerFactory.getLogger(Config.toString())

    /* ********************************** */

    // Main window
    var mainWindowAlwaysOnTop: Boolean = false

    // File paths
    var serverPropertiesFile: String = Paths.get(getCurrentJarDirectory(ApplicationInfo).absolutePath, "alterIWnet.ini").toString()
    var serverExeFile: String = "IWNetServer.exe"
    var serverExePath: String = getCurrentJarDirectory(ApplicationInfo).absolutePath
    var clientExeFile: String = "iw4mp.exe"
    var clientExePath: String = getCurrentJarDirectory(ApplicationInfo).absolutePath


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

    fun set(key: String, value: String?) {
        try {
            val field = javaClass.getDeclaredField(key)
            field.isAccessible = true

            if (value == null) {
                field.set(null, null)
            } else {
                field.set(null, PropertyLoader.stringToTypedValue(value, field.name, field.type))
            }
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