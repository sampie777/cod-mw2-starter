package nl.sajansen.codmw2starter.gui.config

import nl.sajansen.codmw2starter.config.PropertyLoader
import nl.sajansen.codmw2starter.gui.config.formcomponents.*
import nl.sajansen.codmw2starter.gui.notifications.Notifications
import org.slf4j.LoggerFactory
import java.awt.BorderLayout
import java.awt.GridLayout
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.border.EmptyBorder

class ConfigEditPanel : JPanel() {
    private val logger = LoggerFactory.getLogger(ConfigEditPanel::class.java.name)

    private val formComponents: ArrayList<FormComponent> = ArrayList()

    init {
        createFormInputs()
        createGui()
    }

    private fun createFormInputs() {
        formComponents.add(HeaderFormComponent("Game Execution"))
        formComponents.add(StringFormInput("serverPropertiesFile", "alterIWnet properties file", false))
        formComponents.add(StringFormInput("serverExeFile", "IWNetServer execution file", false))
        formComponents.add(StringFormInput("serverExePath", "Directory of IWNetServer execution file", false))
        formComponents.add(StringFormInput("clientExeFile", "MW2 multiplayer execution file", false))
        formComponents.add(StringFormInput("clientExePath", "Directory MW2 multiplayer execution file", false))

        formComponents.add(HeaderFormComponent("In-game Server"))
        formComponents.add(NumberFormInput("sendPasteDelayMs", "Command paste delay", 0, 10000, 100))
        formComponents.add(
            NativeKeyEventFormInput(
                "globalKeyEventPauseLobby",
                "Global key for (un)pausing the lobby",
                allowEmpty = true,
                toolTipText = "Click the Assign button and hit your keys. Click again to clear."
            )
        )
        formComponents.add(StringFormInput("codeTemplate", "Command template", true))
    }

    private fun createGui() {
        layout = BorderLayout()

        val mainPanel = JPanel()
        mainPanel.layout = GridLayout(0, 1)
        mainPanel.border = EmptyBorder(10, 10, 10, 10)

        addConfigItems(mainPanel)

        val scrollPanelInnerPanel = JPanel(BorderLayout())
        scrollPanelInnerPanel.add(mainPanel, BorderLayout.PAGE_START)
        val scrollPanel = JScrollPane(scrollPanelInnerPanel)
        scrollPanel.border = null
        add(scrollPanel, BorderLayout.CENTER)
    }

    private fun addConfigItems(panel: JPanel) {
        formComponents.forEach {
            try {
                panel.add(it.component())
            } catch (e: Exception) {
                logger.error("Failed to create Config Edit GUI component: ${it::class.java}")
                e.printStackTrace()

                if (it !is FormInput) {
                    return@forEach
                }

                logger.error("Failed to create Config Edit GUI component: ${it.key}")
                Notifications.popup(
                    "Failed to load GUI input for config key: <strong>${it.key}</strong>. Delete your <i>${PropertyLoader.getPropertiesFile().name}</i> file and try again. (Error: ${e.localizedMessage})",
                    "Configuration"
                )
                panel.add(TextFormComponent("Failed to load component. See Notifications.").component())
            }
        }
    }

    fun saveAll(): Boolean {
        val formInputComponents = formComponents.filterIsInstance<FormInput>()
        val validationErrors = ArrayList<String>()

        formInputComponents.forEach {
            val validation = it.validate()
            if (validation.isEmpty()) {
                return@forEach
            }

            logger.warn(validation.toString())
            validationErrors += validation
        }

        if (validationErrors.isNotEmpty()) {
            if (this.parent == null) {
                logger.warn("Panel is not a visible GUI component")
                return false
            }

            JOptionPane.showMessageDialog(
                this, validationErrors.joinToString(",\n"),
                "Invalid data",
                JOptionPane.ERROR_MESSAGE
            )
            return false
        }

        formInputComponents.forEach { it.save() }
        return true
    }
}