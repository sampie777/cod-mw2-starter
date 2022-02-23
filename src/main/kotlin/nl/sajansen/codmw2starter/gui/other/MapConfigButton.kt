package nl.sajansen.codmw2starter.gui.other

import nl.sajansen.codmw2starter.gui.HotKeysMapping
import nl.sajansen.codmw2starter.gui.Theme
import nl.sajansen.codmw2starter.gui.mapConfig.MapConfigFrame
import nl.sajansen.codmw2starter.utils.Icon
import nl.sajansen.codmw2starter.utils.addHotKeyMapping
import nl.sajansen.codmw2starter.utils.getMainFrameComponent
import org.slf4j.LoggerFactory
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.border.EmptyBorder

class MapConfigButton : JButton() {
    private val logger = LoggerFactory.getLogger(this::class.java)

    init {
        createGui()
    }

    private fun createGui() {
        layout = BoxLayout(this, BoxLayout.LINE_AXIS)

        add(Icon("\uf013", size = 14f)
            .also { icon -> icon.border = EmptyBorder(1, 0, 0, 8) })
        add(JLabel("Maps")
            .also { label -> label.font = Theme.buttonFont })

        addActionListener { MapConfigFrame.createAndShow(getMainFrameComponent(this)) }
        addHotKeyMapping(HotKeysMapping.OpenMap)
    }
}