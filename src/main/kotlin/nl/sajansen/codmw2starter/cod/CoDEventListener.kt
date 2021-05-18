package nl.sajansen.codmw2starter.cod

import org.slf4j.LoggerFactory

interface CoDEventListener {
    fun onPauseChanged() {}
    fun onNicknameChanged() {}
}

object CoDEventListenerSubscriber {
    private val logger = LoggerFactory.getLogger(CoDEventListenerSubscriber::class.java)
    private val components: HashSet<CoDEventListener> = HashSet()

    fun onPauseChanged() {
        val componentsCopy = components.toTypedArray()
        for (component in componentsCopy) {
            component.onPauseChanged()
        }
    }

    fun onNicknameChanged() {
        val componentsCopy = components.toTypedArray()
        for (component in componentsCopy) {
            component.onNicknameChanged()
        }
    }

    fun register(component: CoDEventListener) {
        logger.info("Registering component: ${component::class.java}")
        components.add(component)
        println(components.size)
    }

    fun isRegistered(component: CoDEventListener): Boolean {
        return components.contains(component)
    }

    fun unregister(component: CoDEventListener) {
        logger.info("Unregistering component: ${component::class.java}")
        components.remove(component)
    }

    fun unregisterAll() {
        logger.info("Unregistering all (${components.size}) components")
        components.clear()
    }

    fun registeredComponents() = components
}