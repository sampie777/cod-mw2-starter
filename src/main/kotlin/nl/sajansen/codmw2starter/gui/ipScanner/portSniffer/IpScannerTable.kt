package nl.sajansen.codmw2starter.gui.ipScanner.portSniffer

import nl.sajansen.codmw2starter.config.Config
import nl.sajansen.codmw2starter.ipScanner.portSniffer.ScanResult
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

class IpScannerTable(
    private val setHost: ((host: String) -> Unit),
    private val runWithHost: ((host: String) -> Unit)
) : JPanel() {

    private val tableHeader = arrayOf("Address", "PC")
    private val table = JTable(ReadOnlyTableModel(tableHeader, 0))

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

    fun model(): DefaultTableModel = table.model as DefaultTableModel

    fun setScanResults(scanResults: List<ScanResult>) {
        clearTable()

        scanResults.forEach {
            addScanResult(it)
        }
    }

    fun addScanResult(scanResult: ScanResult) {
        if (Config.udpSnifferFilterOutLocalIps) {
            if (scanResult.ip.startsWith("127.0.0.")
                || scanResult.ip.startsWith("0.0.0.")
            ) {
                return;
            }
        }

        model().addRow(arrayOf(scanResult.ip, scanResult.hostName))
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

        return row[0].toString()
    }
}