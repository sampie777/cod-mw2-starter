package nl.sajansen.codmw2starter.gui.ipScanner.manualIp

import nl.sajansen.codmw2starter.cod.CoD
import org.slf4j.LoggerFactory
import java.awt.BorderLayout
import java.awt.event.FocusEvent
import java.awt.event.FocusListener
import javax.swing.JPanel
import javax.swing.JTextField

class ManualIpPanel(
    private val onHostClick: ((host: String) -> Unit),
    private val onHostDoubleClick: ((host: String) -> Unit)
) : JPanel() {
    private val logger = LoggerFactory.getLogger(this::class.java.name)
    private val input = JTextField(CoD.getHost() ?: "")

    init {
        createGui()
    }

    private fun createGui() {
        layout = BorderLayout(5, 5)

        input.addFocusListener(object : FocusListener {
            override fun focusGained(e: FocusEvent) {
            }

            override fun focusLost(e: FocusEvent) {
                onHostClick(input.text)
            }
        })
    }
}