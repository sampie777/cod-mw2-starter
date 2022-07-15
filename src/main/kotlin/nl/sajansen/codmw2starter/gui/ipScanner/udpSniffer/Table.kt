package nl.sajansen.codmw2starter.gui.ipScanner.udpSniffer

import nl.sajansen.codmw2starter.ipScanner.udpSniffer.Other
import nl.sajansen.codmw2starter.utils.ReadOnlyTableModel
import java.awt.BorderLayout
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.util.*
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTable
import javax.swing.ListSelectionModel
import javax.swing.table.DefaultTableModel

class Table(
    private val setHost: ((host: String) -> Unit),
    private val runWithHost: ((host: String) -> Unit)
) : JPanel() {

    private val tableHeader = arrayOf("Player", "Address", "PC")
    val table = JTable(ReadOnlyTableModel(tableHeader, 0))

    init {
        createGui()
    }

    private fun createGui() {
        layout = BorderLayout()

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION)
        table.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                val host = getSelectedValueAsAddress() ?: return

                if (e.clickCount >= 2) {
                    runWithHost.invoke(host)
                } else {
                    setHost.invoke(host)
                }
            }
        })

        val scrollPane = JScrollPane(table)
        add(scrollPane, BorderLayout.CENTER)
    }

    fun clearTable() {
        model().rowCount = 0
    }

    private fun model(): DefaultTableModel = table.model as DefaultTableModel

    fun addScanResult(other: Other) {
        val hostName = if (other.hostName == other.hostAddress) "" else other.hostName
        var displayNickName = other.nickname
        if (other.isHosting) {
            displayNickName = "$displayNickName (hosting)"
        }
        model().addRow(arrayOf(displayNickName, other.hostAddress, hostName))
    }

    fun getSelectedValueAsAddress(): String? {
        val row: Vector<*>?
        try {
            row = model().dataVector[table.selectedRow] as? Vector<*>
        } catch (e: ArrayIndexOutOfBoundsException) {
            return null
        }

        if (row == null || row.size < 2) {
            return null
        }

        return row[1].toString()
    }
}