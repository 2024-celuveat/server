package com.celuveat.notification.application

import com.celuveat.member.application.port.out.ReadMemberPort
import com.celuveat.notification.application.port.`in`.ReadNotificationUseCase
import com.celuveat.notification.application.port.out.ReadNotificationPort
import com.celuveat.notification.application.port.out.SaveNotificationPort
import org.springframework.stereotype.Service

@Service
class NotificationService(
    private val readNotificationPort: ReadNotificationPort,
    private val saveNotificationPort: SaveNotificationPort,
    private val readMemberPort: ReadMemberPort,
) : ReadNotificationUseCase {
    override fun readNotification(
        notificationId: Long,
        memberId: Long,
    ) {
        val member = readMemberPort.readById(memberId)
        val notification = readNotificationPort.readById(notificationId)
        notification.validateOwner(member)
        notification.read()
        saveNotificationPort.saveNotification(notification)
    }
}
