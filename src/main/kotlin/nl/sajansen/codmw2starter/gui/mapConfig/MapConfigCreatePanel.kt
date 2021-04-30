package nl.sajansen.codmw2starter.gui.mapConfig

import nl.sajansen.codmw2starter.config.Config
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

class MapConfigCreatePanel : JPanel() {
    private val logger = LoggerFactory.getLogger(MapConfigCreatePanel::class.java)

    private val formComponents: ArrayList<FormComponent> = ArrayList()

    init {
        createFormInputs()
        createGui()
    }

    private fun createFormInputs() {
        formComponents.add(HeaderFormComponent("Main"))
        formComponents.add(SelectFormInput("map", "Map", MapName.values().toList()))
        formComponents.add(SelectFormInput("gameType", "Game type", GameType.values().toList()))
        formComponents.add(HeaderFormComponent("Extra"))
        formComponents.add(NumberFormInput("timeLimit", "Time limit", 1, 999, 1))
        formComponents.add(NumberFormInput("scoreLimit", "Score limit", 1, 999999, 50))
        formComponents.add(NumberFormInput("minPlayers", "Min. players", 1, 999, 1))
        formComponents.add(NumberFormInput("maxPlayers", "Max. players", 1, 999, 1))
        formComponents.add(BooleanFormInput("killcam", "Kill cam"))
        formComponents.add(SelectFormInput("spectate", "Spectate", Spectate.values().toList()))
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

    fun getCommand(): String? {
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
            JOptionPane.showMessageDialog(
                this, validationErrors.joinToString(",\n"),
                "Invalid data",
                JOptionPane.ERROR_MESSAGE
            )
            return null
        }

        formInputComponents.forEach { it.save() }

        return Config.codeTemplate
            .replace("{{map}}", Config.map.codeName)
            .replace("{{gameType}}", Config.gameType.codeName)
            .replace("{{timeLimit}}", Config.timeLimit.toString())
            .replace("{{scoreLimit}}", Config.scoreLimit.toString())
            .replace("{{maxPlayers}}", Config.maxPlayers.toString())
            .replace("{{minPlayers}}", Config.minPlayers.toString())
            .replace("{{spectate}}", Config.spectate.codeName)
            .replace("{{killcam}}", if (Config.killcam) "1" else "0")
    }
}