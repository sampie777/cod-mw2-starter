package nl.sajansen.codmw2starter.gui.notifications

import java.util.*

data class Notification(val message: String, val subject: String = "") {
    val timestamp = Date()
}