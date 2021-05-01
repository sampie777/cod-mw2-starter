package nl.sajansen.codmw2starter

import java.util.*

object ApplicationInfo {
    private val properties = Properties()
    val version: String
    val artifactId: String
    val name: String
    val author: String
    val url: String
    val latestVersionsUrl: String
    val updatesInfoUrl: String
    val downloadsUrl: String
    val donationUrl: String

    init {
        properties.load(ApplicationInfo::class.java.getResourceAsStream("/nl/sajansen/codmw2starter/application.properties"))
        version = properties.getProperty("version")
        artifactId = properties.getProperty("artifactId")
        name = properties.getProperty("name")
        author = properties.getProperty("author")
        url = properties.getProperty("url")
        latestVersionsUrl = properties.getProperty("latestVersionsUrl")
        updatesInfoUrl = properties.getProperty("updatesInfoUrl")
        downloadsUrl = properties.getProperty("downloadsUrl")
        donationUrl = properties.getProperty("donationUrl")
    }
}