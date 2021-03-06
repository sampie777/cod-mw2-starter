package nl.sajansen.codmw2starter.gui.mapConfig.mapList

import nl.sajansen.codmw2starter.cod.MapName
import nl.sajansen.codmw2starter.config.Config
import nl.sajansen.codmw2starter.gui.Theme
import nl.sajansen.codmw2starter.gui.config.formcomponents.FormInput
import nl.sajansen.codmw2starter.gui.config.formcomponents.SelectFormInput
import org.slf4j.LoggerFactory
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Component
import java.awt.Dimension
import javax.swing.*

class MapListFormInput(
    override val key: String,
    private val labelText: String,
    private val values: List<MapName>
) : FormInput {
    private val logger = LoggerFactory.getLogger(SelectFormInput::class.java.name)

    private val selectBox = JComboBox<MapName>()

    @Suppress("UNCHECKED_CAST")
    override fun component(): Component {
        val label = JLabel(labelText)
        label.font = Theme.normalFont

        selectBox.model = DefaultComboBoxModel(values.toTypedArray())
        selectBox.renderer = MapListRenderer()
        selectBox.selectedItem = values.find { it == Config.get(key) }
        selectBox.preferredSize = Dimension(250, 30)
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
    override fun value(): MapName {
        return selectBox.selectedItem as MapName
    }
}