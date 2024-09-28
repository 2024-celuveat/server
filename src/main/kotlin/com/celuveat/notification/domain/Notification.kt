package com.celuveat.notification.domain

import java.time.LocalDateTime

data class Notification(
    val id: Long = 0,
    val notificationType: NotificationType,
    val content: String,
    var isRead: Boolean,
    val createdAt: LocalDateTime,
) {
    fun read() {
        this.isRead = true
    }
}
