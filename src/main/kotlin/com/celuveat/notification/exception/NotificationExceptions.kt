package com.celuveat.notification.exception

import com.celuveat.common.exception.CeluveatException
import org.springframework.http.HttpStatus

sealed class NotificationException(
    status: HttpStatus,
    errorMessage: String,
) : CeluveatException(status, errorMessage)

data object NotFoundNotificationException : NotificationException(HttpStatus.NOT_FOUND, "존재하지 않는 알림입니다") {
    private fun readResolve(): Any = NotFoundNotificationException
}
