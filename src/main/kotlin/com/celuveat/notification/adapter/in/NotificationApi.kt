package com.celuveat.notification.adapter.`in`

import com.celuveat.auth.adapter.`in`.rest.Auth
import com.celuveat.auth.adapter.`in`.rest.AuthContext
import com.celuveat.notification.adapter.`in`.response.NotificationResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping

@SecurityRequirement(name = "JWT")
@Tag(name = "알림 API")
interface NotificationApi {

    @Operation(summary = "회원의 알림 목록 조회")
    @GetMapping
    fun getNotifications(
        @Auth auth: AuthContext,
    ): List<NotificationResponse>

    @Operation(summary = "알림 읽음 처리")
    @PostMapping("/{notificationId}")
    fun readNotification(
        @Auth auth: AuthContext,
        @Parameter(
            `in` = ParameterIn.PATH,
            description = "알림 ID",
            example = "1",
            required = true,
        )
        @PathVariable notificationId: Long,
    )
}
