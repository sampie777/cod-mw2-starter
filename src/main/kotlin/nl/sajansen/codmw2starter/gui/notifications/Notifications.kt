package nl.sajansen.codmw2starter.gui.notifications

import nl.sajansen.codmw2starter.gui.GUI
import nl.sajansen.codmw2starter.gui.mainFrame.MainFrame
import org.slf4j.LoggerFactory
import javax.swing.JOptionPane

object Notifications {
    private val logger = LoggerFactory.getLogger(Notifications.toString())

    val list: ArrayList<Notification> = ArrayList()
    var unreadNotifications: Int = 0
    var enablePopups: Boolean = false

    fun add(notification: Notification, markAsRead: Boolean = false) {
        logger.info("Adding new notification: $notification")
        list.add(notification)

        if (!markAsRead) {
            unreadNotifications++
        }

        GUI.refreshNotifications()
    }

    fun add(message: String, subject: String = "", markAsRead: Boolean = false) {
        add(Notification(message, subject), markAsRead)
    }

    fun markAllAsRead() {
        logger.info("Marking all notifications as read")
        unreadNotifications = 0
        GUI.refreshNotifications()
    }

    fun clear() {
        list.clear()
        markAllAsRead()
    }

    fun popup(notification: Notification, markAsRead: Boolean = enablePopups) {
        add(notification, markAsRead)

        if (!enablePopups) {
            return
        }

        JOptionPane.showMessageDialog(
            MainFrame.getInstance(),
            "<html>${notification.message}</html>",
            notification.subject,
            JOptionPane.PLAIN_MESSAGE
        )
    }

    fun popup(message: String, subject: String = "", markAsRead: Boolean = enablePopups) {
        popup(Notification(message, subject), markAsRead)
    }
}