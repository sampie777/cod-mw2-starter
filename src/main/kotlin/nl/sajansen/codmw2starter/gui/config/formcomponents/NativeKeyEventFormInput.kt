package nl.sajansen.codmw2starter.gui.config.formcomponents

import keyEventToString
import nl.sajansen.codmw2starter.config.Config
import nl.sajansen.codmw2starter.globalHooks.GlobalKeyboardHook
import nl.sajansen.codmw2starter.globalHooks.NativeKeyEventJson
import org.jnativehook.keyboard.NativeKeyEvent
import org.slf4j.LoggerFactory
import java.awt.BorderLayout
import java.awt.Component
import java.awt.Dimension
import java.awt.Font
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JPanel

class NativeKeyEventFormInput(
    override val key: String,
    private val labelText: String,
    private val allowEmpty: Boolean,
    private val toolTipText: String = ""
) : FormInput {

    private val logger = LoggerFactory.getLogger(NativeKeyEventFormInput::class.java.name)

    private val button = JButton()
    private var keyEvent: NativeKeyEvent? = null

    override fun component(): Component {
        val configValue = Config.get(key) as? NativeKeyEventJson
        keyEvent = configValue?.toEvent()

        val label = JLabel(labelText)
        label.font = Font("Dialog", Font.PLAIN, 12)
        label.toolTipText = toolTipText

        setButtonText()
        button.preferredSize = Dimension(200, 30)
        button.addActionListener { calibrate() }

        val panel = JPanel()
        panel.layout = BorderLayout(10, 10)
        panel.toolTipText = toolTipText
        panel.add(label, BorderLayout.LINE_START)
        panel.add(button, BorderLayout.LINE_END)
        return panel
    }

    private fun setButtonText() {
        if (keyEvent == null) {
            button.text = "Click to Assign..."
            button.toolTipText = "Click to assign"
        } else {
            button.text = keyEventToString(keyEvent)
            button.toolTipText = "Click to clear"
        }
    }

    private fun calibrate() {
        // Clear key
        if (keyEvent != null) {
            keyEvent = null
            setButtonText()
            return
        }

        button.isEnabled = false
        button.text = "Press your keys..."
        GlobalKeyboardHook.startCalibration { keyEvent ->
            logger.info("Calibrated key event: ${keyEventToString(keyEvent)}")

            this.keyEvent = keyEvent
            setButtonText()
            button.isEnabled = true
        }
    }

    override fun validate(): List<String> {
        val errors = ArrayList<String>()

        if (!allowEmpty && keyEvent != null) {
            errors.add("Value for '$labelText' may not be empty")
        }

        return errors
    }

    override fun save() {
        if (keyEvent == null) {
            Config.set(key, null)
        } else {
            Config.set(key, NativeKeyEventJson.fromEvent(keyEvent!!))
        }
    }

    override fun value(): Any? {
        return keyEvent
    }
}