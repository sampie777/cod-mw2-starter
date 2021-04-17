package nl.sajansen.codmw2starter.utils

import nl.sajansen.codmw2starter.ApplicationInfo
import nl.sajansen.codmw2starter.gui.HotKeysMapping
import nl.sajansen.codmw2starter.gui.mainFrame.MainFrame
import org.slf4j.LoggerFactory
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.InputEvent
import java.awt.event.KeyEvent
import java.awt.image.BufferedImage
import java.net.URL
import javax.swing.*
import javax.swing.plaf.basic.BasicSplitPaneDivider

private val logger = LoggerFactory.getLogger("utils.gui")


fun createGraphics(width: Int, height: Int): Pair<BufferedImage, Graphics2D> {
    val bufferedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
    val g2: Graphics2D = bufferedImage.createGraphics()
    setDefaultRenderingHints(g2)
    return Pair(bufferedImage, g2)
}

fun setDefaultRenderingHints(g2: Graphics2D) {
    g2.setRenderingHint(
        RenderingHints.KEY_TEXT_ANTIALIASING,
        RenderingHints.VALUE_TEXT_ANTIALIAS_ON
    )
    g2.setRenderingHint(
        RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON
    )
    g2.setRenderingHint(
        RenderingHints.KEY_INTERPOLATION,
        RenderingHints.VALUE_INTERPOLATION_BILINEAR
    )
}

fun drawImageInXYCenter(mainGraphics2D: Graphics2D, mainWidth: Int, mainHeight: Int, bufferedImage: BufferedImage) {
    mainGraphics2D.drawImage(
        bufferedImage,
        null,
        (mainWidth - bufferedImage.width) / 2,
        (mainHeight - bufferedImage.height) / 2
    )
}

fun drawImageInXCenter(mainGraphics2D: Graphics2D, y: Int, mainWidth: Int, bufferedImage: BufferedImage) {
    mainGraphics2D.drawImage(bufferedImage, null, (mainWidth - bufferedImage.width) / 2, y)
}

fun drawImageInYCenter(mainGraphics2D: Graphics2D, mainHeight: Int, x: Int, bufferedImage: BufferedImage) {
    mainGraphics2D.drawImage(bufferedImage, null, x, (mainHeight - bufferedImage.height) / 2)
}

fun getMainFrameComponent(childComponent: Component): JFrame? {
    return MainFrame.getInstance()
}

fun loadIcon(iconPath: String): Image? {
    val resource: URL? = ApplicationInfo::class.java.getResource(iconPath)
    if (resource == null) {
        logger.warn("Could not find icon: $iconPath")
        return null
    }

    return Toolkit.getDefaultToolkit().getImage(resource)
}

fun createImageIcon(path: String): ImageIcon? {
    val imgURL: URL? = ApplicationInfo::class.java.getResource(path)
    if (imgURL != null) {
        return ImageIcon(imgURL)
    }

    logger.error("Couldn't find imageIcon: $path")
    return null
}

fun isCtrlClick(modifiers: Int): Boolean = modifiers.and(ActionEvent.CTRL_MASK) != 0

fun getMainMenu(menu: JMenu) = (menu.popupMenu.invoker.parent as JPopupMenu).invoker

fun JSplitPane.divider(): BasicSplitPaneDivider = this.components.find { it is BasicSplitPaneDivider} as BasicSplitPaneDivider

fun JMenu.addHotKeyMapping(hotKeyMapping: HotKeysMapping) {
    addHotKey(hotKeyMapping.key)
}

fun JMenu.addHotKey(hotKey: Int?) {
    if (hotKey == null || hotKey == KeyEvent.VK_UNDEFINED) {
        return
    }

    mnemonic = hotKey
}

fun JMenuItem.addHotKeyMapping(hotKeyMapping: HotKeysMapping, ctrl: Boolean = true, alt: Boolean = true, shift: Boolean = false) {
    addHotKey(hotKeyMapping.key, ctrl, alt, shift)
}

fun JMenuItem.addHotKey(hotKey: Int?, ctrl: Boolean = true, alt: Boolean = true, shift: Boolean = false) {
    if (hotKey == null || hotKey == KeyEvent.VK_UNDEFINED) {
        return
    }

    var mask = 0
    if (ctrl) {
        mask = mask.or(InputEvent.CTRL_MASK)
    }
    if (alt) {
        mask = mask.or(InputEvent.ALT_MASK)
    }
    if (shift) {
        mask = mask.or(InputEvent.SHIFT_MASK)
    }

    mnemonic = hotKey
    accelerator = KeyStroke.getKeyStroke(hotKey, mask)
}

fun AbstractButton.addHotKeyMapping(hotKeyMapping: HotKeysMapping) {
    addHotKey(hotKeyMapping.key)
}

fun AbstractButton.addHotKey(hotKey: Int?) {
    if (hotKey == null || hotKey == KeyEvent.VK_UNDEFINED) {
        return
    }

    mnemonic = hotKey
}