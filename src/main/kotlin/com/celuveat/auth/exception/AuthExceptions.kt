package com.celuveat.auth.exception

import com.celuveat.common.exception.CeluveatException
import org.springframework.http.HttpStatus

sealed class AuthExceptions(
    status: HttpStatus,
    errorMessage: String,
) : CeluveatException(status, errorMessage)

data object UnAuthorizationException : AuthExceptions(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다") {
    private fun readResolve(): Any = UnAuthorizationException
}
