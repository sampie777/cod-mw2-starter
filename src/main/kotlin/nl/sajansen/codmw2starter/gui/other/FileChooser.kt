package nl.sajansen.codmw2starter.gui.other

import nl.sajansen.codmw2starter.ApplicationRuntimeSettings
import org.slf4j.LoggerFactory
import java.io.File
import javax.swing.JFileChooser
import javax.swing.UIManager

class FileChooser(
    private val file: String,
    private val title: String = "",
    private val type: Type = Type.Both,
    private val selectionMode: Int = JFileChooser.FILES_AND_DIRECTORIES
) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private var oldLookAndFeel = UIManager.getLookAndFeel()

    enum class Type {
        Both,
        Open,
        Save
    }

    fun prompt(): File? {
        if (ApplicationRuntimeSettings.testing) {
            return null
        }

        val chooser = createFileChooser(file)
        chooser.dialogTitle = title
        chooser.fileSelectionMode = selectionMode

        val result = when (type) {
            Type.Save -> chooser.showSaveDialog(null)
            Type.Open -> chooser.showOpenDialog(null)
            else -> chooser.showDialog(null, "Select")
        }

        try {
            UIManager.setLookAndFeel(oldLookAndFeel)
        } catch (t: Throwable) {
            logger.error("Failed to reset default look and feel")
            t.printStackTrace()
        }

        if (result != JFileChooser.APPROVE_OPTION) {
            return null
        }

        return chooser.selectedFile
    }

    private fun createFileChooser(file: String): JFileChooser {
        oldLookAndFeel = UIManager.getLookAndFeel()
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
        } catch (t: Throwable) {
            logger.error("Failed to get system look and feel classname")
            t.printStackTrace()
        }

        val fileChooser = JFileChooser(file)

        try {
            UIManager.setLookAndFeel(oldLookAndFeel)
        } catch (t: Throwable) {
            logger.error("Failed to reset default look and feel")
            t.printStackTrace()
        }

        return fileChooser
    }
}