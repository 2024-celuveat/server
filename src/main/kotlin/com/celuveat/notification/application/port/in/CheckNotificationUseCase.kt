package com.celuveat.notification.application.port.`in`

interface CheckNotificationUseCase {
    fun checkNotification(
        notificationId: Long,
        memberId: Long,
    )
}
