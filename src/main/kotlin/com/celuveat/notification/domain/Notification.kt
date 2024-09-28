package com.celuveat.notification.domain

import com.celuveat.member.domain.Member
import java.time.LocalDateTime

data class Notification(
    val id: Long = 0,
    val member: Member,
    val notificationType: NotificationType,
    // TODO 클릭시 이동할 링크같은 거 필요하면 추가
    val content: String,
    var isRead: Boolean,
    val createdAt: LocalDateTime,
) {
    fun read() {
        this.isRead = true
    }
}
