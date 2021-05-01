package nl.sajansen.codmw2starter.gui.mapConfig.mapList

import nl.sajansen.codmw2starter.cod.MapName
import nl.sajansen.codmw2starter.utils.createImageIcon
import java.awt.Component
import java.awt.Dimension
import java.awt.Image
import javax.swing.ImageIcon
import javax.swing.JLabel
import javax.swing.JList
import javax.swing.ListCellRenderer


class MapListRenderer : JLabel(), ListCellRenderer<MapName> {
    init {
        isOpaque = true
        horizontalAlignment = LEFT
        verticalAlignment = CENTER
        preferredSize = Dimension(270, 80)
    }

    override fun getListCellRendererComponent(list: JList<out MapName>, value: MapName, index: Int, isSelected: Boolean, hasFocus: Boolean): Component {
        if (isSelected) {
            background = list.selectionBackground
            foreground = list.selectionForeground
        } else {
            background = list.background
            foreground = list.foreground
        }

        text = value.name
        icon = createImageIcon("images/maps/${value.codeName}.jpg")?.let {
            val scaledImage = it.image.getScaledInstance(142, 80, Image.SCALE_SMOOTH)
            ImageIcon(scaledImage)
        }
        return this
    }
}