package nl.sajansen.codmw2starter.cod

import org.slf4j.LoggerFactory

interface CoDEventListener {
    fun onPauseChanged() {}
    fun onNicknameChanged() {}
    fun onHostChanged() {}
    fun onServerStarted() {}
    fun onServerStopping() {}
    fun onServerStopped() {}
    fun onClientStarted() {}
    fun onUdpPingSending() {}
    fun onUdpPingSent() {}
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

    fun onHostChanged() {
        val componentsCopy = components.toTypedArray()
        for (component in componentsCopy) {
            component.onHostChanged()
        }
    }

    fun onServerStarted() {
        val componentsCopy = components.toTypedArray()
        for (component in componentsCopy) {
            component.onServerStarted()
        }
    }

    fun onServerStopping() {
        val componentsCopy = components.toTypedArray()
        for (component in componentsCopy) {
            component.onServerStopping()
        }
    }

    fun onServerStopped() {
        val componentsCopy = components.toTypedArray()
        for (component in componentsCopy) {
            component.onServerStopped()
        }
    }

    fun onClientStarted() {
        val componentsCopy = components.toTypedArray()
        for (component in componentsCopy) {
            component.onClientStarted()
        }
    }

    fun onUdpPingSending() {
        val componentsCopy = components.toTypedArray()
        for (component in componentsCopy) {
            component.onUdpPingSending()
        }
    }

    fun onUdpPingSent() {
        val componentsCopy = components.toTypedArray()
        for (component in componentsCopy) {
            component.onUdpPingSent()
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