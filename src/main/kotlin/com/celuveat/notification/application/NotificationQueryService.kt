package com.celuveat.notification.application

import com.celuveat.notification.application.port.`in`.ReadNotificationsUseCase
import com.celuveat.notification.application.port.`in`.result.NotificationResult
import com.celuveat.notification.application.port.out.ReadNotificationPort
import org.springframework.stereotype.Service

@Service
class NotificationQueryService(
    private val readNotificationPort: ReadNotificationPort,
) : ReadNotificationsUseCase {
    override fun readNotifications(memberId: Long): List<NotificationResult> {
        return readNotificationPort.readByMember(memberId)
            .map { NotificationResult.from(it) }
    }
}
