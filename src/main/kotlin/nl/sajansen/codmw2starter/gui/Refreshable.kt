package nl.sajansen.codmw2starter.gui

import java.awt.Component

interface Refreshable {
    fun refreshNotifications() {}
    fun onConfigSettingsSaved() {}

    fun windowClosing(window: Component?) {}
}