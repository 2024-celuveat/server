package com.celuveat.notification.application.port.out

import com.celuveat.notification.domain.Notification

interface SaveNotificationPort {
    fun saveNotification(notification: Notification)
}
