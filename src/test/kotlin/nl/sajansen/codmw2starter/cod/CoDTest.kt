package nl.sajansen.codmw2starter.cod

import nl.sajansen.codmw2starter.config.Config
import java.io.File
import java.nio.file.Files
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CoDTest {
    @BeforeTest
    fun before() {
        Config.gameDirectory = javaClass.classLoader.getResource("nl/sajansen/codmw2starter")!!.file
    }

    @Test
    fun `test loading nickname from ini file gives correct host`() {
        assertEquals("pussyslayer999", CoD.getNickname())
    }

    @Test
    fun `test saving nickname to ini file creates correct file`() {
        // Given
        val gameDirectoryFile = Files.createTempDirectory("gamedirectory").toFile()
        gameDirectoryFile.deleteOnExit()
        Config.gameDirectory = gameDirectoryFile.absolutePath

        val tempOutputFile = File(CoD.getServerPropertiesFile())
        tempOutputFile.deleteOnExit()
        tempOutputFile.writeText("""
            [Configuration]
            Server = 192.168.0.10
            WebHost = auto
            Nickname = none
        """.trimIndent())

        assertEquals("none", CoD.getNickname(), "Test failed to initialize correctly")

        // When
        CoD.setNickname("pussyslayer999")

        // Then
        assertEquals("""
            [Configuration]
            Server = 192.168.0.10
            WebHost = auto
            Nickname = pussyslayer999
        """.trimIndent(), tempOutputFile.readText().trim())
    }

    @Test
    fun `test loading host from ini file gives correct host`() {
        assertEquals("192.168.0.10", CoD.getHost())
    }

    @Test
    fun `test saving host to ini file creates correct file`() {
        // Given
        val gameDirectoryFile = Files.createTempDirectory("gamedirectory").toFile()
        gameDirectoryFile.deleteOnExit()
        Config.gameDirectory = gameDirectoryFile.absolutePath

        val tempOutputFile = File(CoD.getServerPropertiesFile())
        tempOutputFile.deleteOnExit()
        tempOutputFile.writeText("""
            [Configuration]
            Server = 192.168.0.20
            WebHost = auto
            Nickname = pussyslayer999
        """.trimIndent())

        assertEquals("192.168.0.20", CoD.getHost(), "Test failed to initialize correctly")

        // When
        CoD.setHost("192.168.0.10")

        // Then
        assertEquals("""
            [Configuration]
            Server = 192.168.0.10
            WebHost = auto
            Nickname = pussyslayer999
        """.trimIndent(), tempOutputFile.readText().trim())
    }
}