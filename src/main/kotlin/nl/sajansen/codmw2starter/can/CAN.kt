package nl.sajansen.codmw2starter.can

import org.slf4j.LoggerFactory
import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.ByteBuffer

object CAN {
    private val logger = LoggerFactory.getLogger(this::class.java.name)

    private val outputStream = ByteArrayOutputStream()
    private var logData = false
    val outputFileName = "output.txt"

    data class Message(
        val timestamp: Number,
        val id: Number,
        val length: Number,
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
        val messagesByteArray = arrayListOf<ArrayList<Byte>>()
        messagesByteArray.add(arrayListOf())

        data.forEach {
            if (it.toInt().toChar() == ';') {
                messagesByteArray.add(arrayListOf())
                return@forEach
            }

            messagesByteArray.last().add(it)
        }

        val messages = messagesByteArray
            .map { it.toByteArray() }
            .map {
                if (it.size < 17) {
                    return@map null
                }
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
            .filterNotNull()

        messages.forEach { println("${it.timestamp} | ${it.id} - ${it.data.joinToString(" ") { b -> "%02x".format(b) }} (${it.length})") }
        println("")
    }

    fun readFile() {
        val bytes = FileInputStream(outputFileName).readBytes()
        logData(bytes)
    }
}