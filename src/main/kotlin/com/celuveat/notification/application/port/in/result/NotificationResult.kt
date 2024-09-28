package com.celuveat.notification.application.port.`in`.result

import com.celuveat.member.domain.Member
import com.celuveat.notification.domain.Notification
import com.celuveat.notification.domain.NotificationType
import java.time.LocalDateTime

data class NotificationResult(
    val id: Long = 0,
    val member: Member,
    val notificationType: NotificationType,
    val content: String,
    var isRead: Boolean,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun from(notification: Notification): NotificationResult {
            return NotificationResult(
                id = notification.id,
                member = notification.member,
                notificationType = notification.notificationType,
                content = notification.content,
                isRead = notification.isRead,
                createdAt = notification.createdAt,
            )
        }
    }
}
