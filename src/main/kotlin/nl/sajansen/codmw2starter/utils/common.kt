package nl.sajansen.codmw2starter.utils

import org.slf4j.LoggerFactory
import java.io.File
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

private val logger = LoggerFactory.getLogger("utils.common")

@Throws(UnsupportedEncodingException::class)
fun getCurrentJarDirectory(caller: Any): File {
    val url = caller::class.java.protectionDomain.codeSource.location
    val jarPath = URLDecoder.decode(url.file, "UTF-8")
    return File(jarPath).parentFile
}

fun isAddressLocalhost(address: String): Boolean {
    return address.contains("localhost") || address.contains("127.0.0.1")
}

fun decodeURI(uri: String): String {
    return URLDecoder.decode(uri, StandardCharsets.UTF_8.name())
}

fun getReadableFileSize(file: File): String {
    return when {
        file.length() > 1024 * 1024 -> {
            val fileSize = file.length().toDouble() / (1024 * 1024)
            String.format("%.2f MB", fileSize)
        }
        file.length() > 1024 -> {
            val fileSize = file.length().toDouble() / 1024
            String.format("%.2f kB", fileSize)
        }
        else -> {
            String.format("%d bytes", file.length())
        }
    }
}

fun getFileNameWithoutExtension(file: File): String {
    return file.name.substring(0, file.name.lastIndexOf('.'))
}

fun getFileExtension(file: File): String {
    return file.name.substring(file.name.lastIndexOf('.') + 1)
}

fun Date.format(format: String): String? = SimpleDateFormat(format).format(this)
fun LocalTime.format(format: String): String = this.format(DateTimeFormatter.ofPattern(format))

enum class OS {
    Unknown,
    Windows,
    Linux,
    Mac,
    Solaris
}

fun getOS(): OS {
    val osName = System.getProperty("os.name").toLowerCase()
    if (osName.contains("win")) {
        return OS.Windows
    }
    if (osName.contains("nix") || osName.contains("nux")) {
        return OS.Linux
    }
    if (osName.contains("sunos")) {
        return OS.Mac
    }
    if (osName.contains("mac")) {
        return OS.Solaris
    }
    return OS.Unknown
}