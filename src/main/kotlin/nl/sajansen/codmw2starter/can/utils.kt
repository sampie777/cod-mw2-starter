package nl.sajansen.codmw2starter.can

import java.nio.ByteBuffer
import java.nio.ByteOrder


fun byteArrayToHexString(bytes: ByteArray, length: Int): String {
    return bytes
        .slice(0 until length)
        .joinToString(" ") { b -> "%02x".format(b) }
}


fun byteArrayToLongBE(bytes: ByteArray, length: Int): Long {
    val buffer = ByteBuffer.allocate(Long.SIZE_BYTES)
    for (i in length until buffer.limit()) {
        buffer.put(0)
    }
    for (i in 0 until length) {
        buffer.put(bytes[i])
    }

    return buffer.getLong(0)
}

fun byteArrayToLongLE(bytes: ByteArray, length: Int): Long {
    val buffer = ByteBuffer.allocate(Long.SIZE_BYTES)
    for (i in length until buffer.limit()) {
        buffer.put(0)
    }
    for (i in 0 until length) {
        buffer.put(bytes[i])
    }

    buffer.order(ByteOrder.LITTLE_ENDIAN)
    return buffer.getLong(0)
}

fun byteArrayToLongBEReversed(bytes: ByteArray, length: Int): Long {
    val buffer = ByteBuffer.allocate(Long.SIZE_BYTES)
    for (i in length until buffer.limit()) {
        buffer.put(0)
    }
    for (i in 0 until length) {
        buffer.put(bytes[length - i - 1])
    }

    return buffer.getLong(0)
}

fun byteArrayToLongLEReversed(bytes: ByteArray, length: Int): Long {
    val buffer = ByteBuffer.allocate(Long.SIZE_BYTES)
    for (i in length until buffer.limit()) {
        buffer.put(0)
    }
    for (i in 0 until length) {
        buffer.put(bytes[length - i - 1])
    }

    buffer.order(ByteOrder.LITTLE_ENDIAN)
    return buffer.getLong(0)
}

fun byteArrayToFloatBE(bytes: ByteArray, length: Int): Float {
    if (length != Float.SIZE_BYTES) {
        return 0f
    }

    val buffer = ByteBuffer.allocate(Float.SIZE_BYTES)
    for (i in length until buffer.limit()) {
        buffer.put(0)
    }
    for (i in 0 until length) {
        buffer.put(bytes[i])
    }

    return buffer.getFloat(0)
}

fun byteArrayToFloatLE(bytes: ByteArray, length: Int): Float {
    if (length != Float.SIZE_BYTES) {
        return 0f
    }

    val buffer = ByteBuffer.allocate(Float.SIZE_BYTES)
    for (i in length until buffer.limit()) {
        buffer.put(0)
    }
    for (i in 0 until length) {
        buffer.put(bytes[i])
    }

    buffer.order(ByteOrder.LITTLE_ENDIAN)
    return buffer.getFloat(0)
}

fun byteArrayToFloatBEReversed(bytes: ByteArray, length: Int): Float {
    if (length != Float.SIZE_BYTES) {
        return 0f
    }

    val buffer = ByteBuffer.allocate(Float.SIZE_BYTES)
    for (i in length until buffer.limit()) {
        buffer.put(0)
    }
    for (i in 0 until length) {
        buffer.put(bytes[length - i - 1])
    }

    return buffer.getFloat(0)
}

fun byteArrayToFloatLEReversed(bytes: ByteArray, length: Int): Float {
    if (length != Float.SIZE_BYTES) {
        return 0f
    }

    val buffer = ByteBuffer.allocate(Float.SIZE_BYTES)
    for (i in length until buffer.limit()) {
        buffer.put(0)
    }
    for (i in 0 until length) {
        buffer.put(bytes[length - i - 1])
    }

    buffer.order(ByteOrder.LITTLE_ENDIAN)
    return buffer.getFloat(0)
}

fun byteArrayToDoubleBE(bytes: ByteArray, length: Int): Double {
    if (length != Double.SIZE_BYTES) {
        return 0.0
    }

    val buffer = ByteBuffer.allocate(Double.SIZE_BYTES)
    for (i in length until buffer.limit()) {
        buffer.put(0)
    }
    for (i in 0 until length) {
        buffer.put(bytes[i])
    }

    return buffer.getDouble(0)
}

fun byteArrayToDoubleLE(bytes: ByteArray, length: Int): Double {
    if (length != Double.SIZE_BYTES) {
        return 0.0
    }

    val buffer = ByteBuffer.allocate(Double.SIZE_BYTES)
    for (i in length until buffer.limit()) {
        buffer.put(0)
    }
    for (i in 0 until length) {
        buffer.put(bytes[i])
    }

    buffer.order(ByteOrder.LITTLE_ENDIAN)
    return buffer.getDouble(0)
}

fun byteArrayToDoubleBEReversed(bytes: ByteArray, length: Int): Double {
    if (length != Double.SIZE_BYTES) {
        return 0.0
    }

    val buffer = ByteBuffer.allocate(Double.SIZE_BYTES)
    for (i in length until buffer.limit()) {
        buffer.put(0)
    }
    for (i in 0 until length) {
        buffer.put(bytes[length - i - 1])
    }

    return buffer.getDouble(0)
}

fun byteArrayToDoubleLEReversed(bytes: ByteArray, length: Int): Double {
    if (length != Double.SIZE_BYTES) {
        return 0.0
    }

    val buffer = ByteBuffer.allocate(Double.SIZE_BYTES)
    for (i in length until buffer.limit()) {
        buffer.put(0)
    }
    for (i in 0 until length) {
        buffer.put(bytes[length - i - 1])
    }

    buffer.order(ByteOrder.LITTLE_ENDIAN)
    return buffer.getDouble(0)
}