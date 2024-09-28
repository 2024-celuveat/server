package com.celuveat.notification.adapter.`in`.response

import com.celuveat.notification.application.port.`in`.result.NotificationResult
import com.celuveat.notification.domain.NotificationType
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class NotificationResponse(
    @Schema(
        description = "알림 ID",
        example = "1",
    )
    val id: Long = 0,
    @Schema(
        description = "알림의 종류 (관심 셀럽 / 공지)",
        example = "NOTIFICATION",
    )
    val notificationType: NotificationType,
    @Schema(
        description = "본문",
        example = "공지사항입니다.",
    )
    val content: String,

    @Schema(
        description = "읽었는지 여부",
        example = "false",
    )
    var isRead: Boolean,

    @Schema(
        description = "알림 생성일",
    )
    val createdAt: LocalDateTime,
) {
    companion object {
        fun from(notificationResult: NotificationResult): NotificationResponse {
            return NotificationResponse(
                id = notificationResult.id,
                notificationType = notificationResult.notificationType,
                content = notificationResult.content,
                isRead = notificationResult.isRead,
                createdAt = notificationResult.createdAt,
            )
        }
    }
}
