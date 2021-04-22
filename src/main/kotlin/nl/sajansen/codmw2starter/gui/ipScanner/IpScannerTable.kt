package nl.sajansen.codmw2starter.gui.ipScanner

import nl.sajansen.codmw2starter.gui.mainFrame.MainFrame
import nl.sajansen.codmw2starter.ipScanner.ScanResult
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.util.*
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTable
import javax.swing.ListSelectionModel
import javax.swing.table.DefaultTableModel

class IpScannerTable(private val onHostClick: ((host: String) -> Unit)) : JPanel() {

    private val tableHeader = arrayOf("Name", "Address")
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
                onHostClick.invoke(host)
            }
        })

        val scrollPane = JScrollPane(table)
        scrollPane.preferredSize = Dimension(MainFrame.getInstance()!!.width - 20, 140)

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
        model().addRow(arrayOf(scanResult.hostName, scanResult.ip))
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