package com.celuveat.notification.application.port.`in`

interface ReadNotificationUseCase {
    fun readNotification(notificationId: Long, memberId: Long)
}
