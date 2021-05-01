package nl.sajansen.codmw2starter.globalHooks


import nl.sajansen.codmw2starter.config.Config
import nl.sajansen.codmw2starter.io.CoD
import org.jnativehook.GlobalScreen
import org.jnativehook.NativeHookException
import org.jnativehook.keyboard.NativeKeyEvent
import org.jnativehook.keyboard.NativeKeyListener
import org.slf4j.LoggerFactory
import java.util.logging.Level
import java.util.logging.Logger


object GlobalKeyboardHook : NativeKeyListener {
    private val logger = LoggerFactory.getLogger(GlobalKeyboardHook::class.java.name)

    private var isCalibrating: Boolean = false
    private var calibrationCallback: ((NativeKeyEvent) -> Unit)? = null

    fun register() {
        // Unfortunately this must be done here and for some reason can't be done in LogService at boot
        setupLoggingForGlobalScreen()

        try {
            GlobalScreen.registerNativeHook()
        } catch (e: NativeHookException) {
            logger.warn("There was a problem registering the native hook")
            e.printStackTrace()
            return
        }

        GlobalScreen.addNativeKeyListener(GlobalKeyboardHook)
    }

    fun unregister() {
        try {
            logger.info("Unregistering native hook")
            GlobalScreen.unregisterNativeHook()
        } catch (t: Throwable) {
            logger.warn("There was a problem unregistering the native hook")
            t.printStackTrace()
        }
    }

    override fun nativeKeyTyped(e: NativeKeyEvent) {
    }

    override fun nativeKeyPressed(e: NativeKeyEvent) {
    }

    override fun nativeKeyReleased(e: NativeKeyEvent) {
        processKeyEvent(e)
    }

    fun processKeyEvent(e: NativeKeyEvent) {
        // Remove Num lock, Scroll lock, and Caps lock
        e.modifiers = e.modifiers.and(NativeKeyEvent.NUM_LOCK_MASK - 1)

        if (isCalibrating) {
            processCalibration(e)
            return
        }

        if (Config.globalKeyEventPauseLobby?.isEqualTo(e) == true) {
            logger.info("[NativeKeyEvent] Activate next Queue Item")
            CoD.pauseLobby()
        }
    }

    private fun processCalibration(e: NativeKeyEvent) {
        isCalibrating = false

        if (calibrationCallback == null) {
            logger.warn("Cannot invoke calibrationCallback because callback is null")
            return
        }

        calibrationCallback?.invoke(e)
    }

    fun startCalibration(callback: (keyEvent: NativeKeyEvent) -> Unit) {
        calibrationCallback = callback
        isCalibrating = true
    }

    private fun setupLoggingForGlobalScreen() {
        // Get the logger for "com.github.kwhat.jnativehook" and set the level to warning.
        val logger = Logger.getLogger(GlobalScreen::class.java.getPackage().name)

        logger.level = Level.WARNING
        logger.useParentHandlers = false
    }
}