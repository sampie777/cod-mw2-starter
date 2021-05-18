package nl.sajansen.codmw2starter.gui.config.formcomponents

import nl.sajansen.codmw2starter.config.Config
import nl.sajansen.codmw2starter.gui.Theme
import org.slf4j.LoggerFactory
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Component
import java.awt.Dimension
import javax.swing.*

class SelectFormInput<T : Any>(
    override val key: String,
    private val labelText: String,
    private val values: Array<T>
) : FormInput {

    private val logger = LoggerFactory.getLogger(SelectFormInput::class.java.name)

    private val selectBox = JComboBox<Any>()

    @Suppress("UNCHECKED_CAST")
    override fun component(): Component {
        val label = JLabel(labelText)
        label.font = Theme.normalFont

        selectBox.model = DefaultComboBoxModel(values)
        selectBox.selectedItem = values.find { it == Config.get(key) }
        selectBox.preferredSize = Dimension(150, 30)
        selectBox.border = BorderFactory.createLineBorder(Color(168, 168, 168))
        selectBox.font = Theme.normalFont

        val panel = JPanel()
        panel.layout = BorderLayout(10, 10)
        panel.add(label, BorderLayout.LINE_START)
        panel.add(selectBox, BorderLayout.LINE_END)
        return panel
    }

    override fun validate(): List<String> {
        val errors = ArrayList<String>()

        if (!values.contains(value())) {
            errors.add("Selected invalid value '${value()}' for '$labelText'")
        }

        return errors
    }

    override fun save() {
        Config.set(key, value())
    }

    @Suppress("UNCHECKED_CAST")
    override fun value(): T {
        return selectBox.selectedItem as T
    }
}