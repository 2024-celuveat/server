package com.celuveat.notification.adapter.`in`

import com.celuveat.auth.adapter.`in`.rest.Auth
import com.celuveat.auth.adapter.`in`.rest.AuthContext
import com.celuveat.notification.adapter.`in`.response.NotificationResponse
import com.celuveat.notification.application.port.`in`.ReadNotificationUseCase
import com.celuveat.notification.application.port.`in`.ReadNotificationsUseCase
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/notifications")
@RestController
class NotificationController(
    private val readNotificationUseCase: ReadNotificationUseCase,
    private val readNotificationsUseCase: ReadNotificationsUseCase,
) : NotificationApi {
    @GetMapping
    override fun getNotifications(
        @Auth auth: AuthContext,
    ): List<NotificationResponse> {
        return readNotificationsUseCase.readNotifications(auth.memberId())
            .map { NotificationResponse.from(it) }
    }

    @PostMapping("/{notificationId}")
    override fun readNotification(
        @Auth auth: AuthContext,
        @PathVariable("notificationId") notificationId: Long,
    ) {
        readNotificationUseCase.readNotification(notificationId = notificationId, memberId = auth.memberId())
    }
}
