package com.celuveat.notification.application.port.`in`

import com.celuveat.notification.application.port.`in`.result.NotificationResult

interface ReadNotificationsUseCase {
    fun readNotifications(memberId: Long): List<NotificationResult>
}
