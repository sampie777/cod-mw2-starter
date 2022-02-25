package nl.sajansen.codmw2starter.can

import org.slf4j.LoggerFactory
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.ByteBuffer

object CAN {
    private val logger = LoggerFactory.getLogger(this::class.java.name)

    private val outputStream = ByteArrayOutputStream()
    private var logData = false
    val outputFileName = "output.txt"

    data class Message(
        val timestamp: Int,
        val id: Int,
        val length: Int,
        val data: ByteArray,
    )

    fun handleMessage(data: ByteArray): Boolean {
        logger.info("Handling data..")

        outputStream.write(data)
        outputStream.write(";".toByteArray())

        if (logData) {
            logData(data)
        }

        return true
    }

    fun saveData() {
        logger.info("Writing data to file")
        try {
            FileOutputStream(outputFileName).also {
                outputStream.writeTo(it)
            }
            logger.info("Done writing to file")
        } catch (e: Exception) {
            logger.error("Failed to write data to file")
            e.printStackTrace()
        }
    }

    private fun logData(data: ByteArray) {
        val messages = bytesToMessages(data)

        messages.forEach { println("${it.timestamp} | ${it.id} - ${it.data.joinToString(" ") { b -> "%02x".format(b) }} (${it.length})") }
        println("")
    }

    private fun bytesToMessages(bytes: ByteArray): List<Message> {
        val messagesByteArray = arrayListOf<ArrayList<Byte>>()
        messagesByteArray.add(arrayListOf())

        bytes.forEach {
            if (it.toInt().toChar() == ';') {
                messagesByteArray.add(arrayListOf())
                return@forEach
            }

            messagesByteArray.last().add(it)
        }

        val messages = messagesByteArray
            .map { it.toByteArray() }
            .filter { it.size == 17 }
            .map {
                val buffer = ByteBuffer.wrap(it)
                return@map Message(
                    timestamp = buffer.int,
                    id = buffer.int,
                    length = buffer.get().toInt(),
                    data = byteArrayOf(
                        buffer.get(),
                        buffer.get(),
                        buffer.get(),
                        buffer.get(),
                        buffer.get(),
                        buffer.get(),
                        buffer.get(),
                        buffer.get()
                    )
                )
            }
            .filter { it.id != 0 }
        return messages
    }

    fun readFile() {
        val bytes = FileInputStream(outputFileName).readBytes()
        logData(bytes)
    }

    fun convertData() {
        val bytes = FileInputStream(outputFileName).readBytes()
        val messages = bytesToMessages(bytes)
        val outputText = StringBuilder("TIME;ID;ID (HEX);LENGTH;RAW;LONG BE;LONG LE;LONG BE REVERSED;LONG LE REVERSED;DOUBLE BE;DOUBLE LE;DOUBLE BE REVERSED;DOUBLE LE REVERSED;\n")

        messages.forEach { message ->
            outputText.append(
                "${message.timestamp};" +
                        "${message.id};" +
                        "${"0x%02x".format(message.id)};" +
                        "${message.length};" +
                        "${byteArrayToHexString(message.data, message.length)};" +
                        "${byteArrayToLongBE(message.data, message.length)};" +
                        "${byteArrayToLongLE(message.data, message.length)};" +
                        "${byteArrayToLongBEReversed(message.data, message.length)};" +
                        "${byteArrayToLongLEReversed(message.data, message.length)};"
            )

            when (message.length) {
                Float.SIZE_BYTES -> outputText.append(
                    "${byteArrayToFloatBE(message.data, message.length)};" +
                            "${byteArrayToFloatLE(message.data, message.length)};" +
                            "${byteArrayToFloatBEReversed(message.data, message.length)};" +
                            "${byteArrayToFloatLEReversed(message.data, message.length)};"
                )
                Double.SIZE_BYTES -> outputText.append(
                    "${byteArrayToDoubleBE(message.data, message.length)};" +
                            "${byteArrayToDoubleLE(message.data, message.length)};" +
                            "${byteArrayToDoubleBEReversed(message.data, message.length)};" +
                            "${byteArrayToDoubleLEReversed(message.data, message.length)};"
                )
                else -> outputText.append("0;0;0;0;")
            }
            outputText.append("\n")
        }

        logger.info("Writing data to file")
        try {
            File("output.csv").also {
                it.writeText(outputText.toString())
            }
            logger.info("Done writing to file")
        } catch (e: Exception) {
            logger.error("Failed to write data to file")
            e.printStackTrace()
        }
    }
}