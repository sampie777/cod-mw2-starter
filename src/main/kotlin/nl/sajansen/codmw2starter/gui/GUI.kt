package nl.sajansen.codmw2starter.gui

import org.slf4j.LoggerFactory
import java.awt.Component
import javax.swing.JFrame

object GUI {
    private val logger = LoggerFactory.getLogger(GUI::class.java.name)

    var currentFrame: JFrame? = null

    private val components: HashSet<Refreshable> = HashSet()

    fun refreshNotifications() {
        val componentsCopy = components.toTypedArray()
        for (component in componentsCopy) {
            component.refreshNotifications()
        }
    }

    fun serverStarted() {
        val componentsCopy = components.toTypedArray()
        for (component in componentsCopy) {
            component.serverStarted()
        }
    }

    fun clientStarted() {
        val componentsCopy = components.toTypedArray()
        for (component in componentsCopy) {
            component.clientStarted()
        }
    }

    fun windowClosing(window: Component?) {
        val componentsCopy = components.toTypedArray()
        for (component in componentsCopy) {
            component.windowClosing(window)
        }
    }


    fun register(component: Refreshable) {
        logger.info("Registering component: ${component::class.java}")
        components.add(component)
    }

    fun isRegistered(component: Refreshable): Boolean {
        return components.contains(component)
    }

    fun unregister(component: Refreshable) {
        logger.info("Unregistering component: ${component::class.java}")
        components.remove(component)
    }

    fun unregisterAll() {
        logger.info("Unregistering all (${components.size}) components")
        components.clear()
    }

    fun registeredComponents() = components
}