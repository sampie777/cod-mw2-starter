package nl.sajansen.codmw2starter.gui.ipScanner.manualIp

import nl.sajansen.codmw2starter.cod.CoD
import org.slf4j.LoggerFactory
import java.awt.BorderLayout
import java.awt.Color
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import javax.swing.BorderFactory
import javax.swing.JPanel
import javax.swing.JTextField
import javax.swing.border.EmptyBorder

class ManualIpPanel(
    private val setHost: ((host: String) -> Unit),
    private val runWithHost: ((host: String) -> Unit),
) : JPanel() {
    private val logger = LoggerFactory.getLogger(this::class.java.name)
    private val input = JTextField(CoD.getHost() ?: "")

    init {
        createGui()
    }

    private fun createGui() {
        layout = BorderLayout(5, 5)
        border = BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 1, 1, 1, Color.LIGHT_GRAY),
            EmptyBorder(10, 10, 10, 10)
        )

        input.border = BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GRAY),
            EmptyBorder(10, 10, 10, 10)
        )
        add(input, BorderLayout.PAGE_START)

        input.addKeyListener(object : KeyListener {
            override fun keyTyped(e: KeyEvent) {
            }

            override fun keyPressed(e: KeyEvent) {
            }

            override fun keyReleased(e: KeyEvent) {
                if (e.keyCode == 13) {
                    runWithHost(input.text)
                    return
                }
                setHost(input.text)
            }
        })
    }
}