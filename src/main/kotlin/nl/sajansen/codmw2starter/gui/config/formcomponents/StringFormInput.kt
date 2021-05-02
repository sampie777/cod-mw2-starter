package nl.sajansen.codmw2starter.gui.config.formcomponents

import nl.sajansen.codmw2starter.config.Config
import nl.sajansen.codmw2starter.gui.Theme
import org.slf4j.LoggerFactory
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Component
import java.awt.Dimension
import javax.swing.*

class StringFormInput(
    override val key: String,
    private val labelText: String,
    private val allowEmpty: Boolean,
    private val toolTipText: String = ""
) : FormInput {
    private val logger = LoggerFactory.getLogger(StringFormInput::class.java.name)

    private val input = JTextField()

    override fun component(): Component {
        val configValue: String? = Config.get(key) as? String

        val label = JLabel(labelText)
        label.font = Theme.normalFont
        label.toolTipText = toolTipText

        input.text = configValue
        input.preferredSize = Dimension(100, 20)
        input.border = BorderFactory.createLineBorder(Color(168, 168, 168))
        input.toolTipText = toolTipText
        input.horizontalAlignment = SwingConstants.RIGHT

        val panel = JPanel()
        panel.layout = BorderLayout(10, 10)
        panel.toolTipText = toolTipText
        panel.add(label, BorderLayout.LINE_START)
        panel.add(input, BorderLayout.CENTER)
        return panel
    }

    override fun validate(): List<String> {
        val errors = ArrayList<String>()

        if (!allowEmpty && value().isEmpty()) {
            errors.add("Value for '$labelText' may not be empty")
        }

        return errors
    }

    override fun save() {
        Config.set(key, value())
    }

    override fun value(): String {
        return input.text as String
    }
}