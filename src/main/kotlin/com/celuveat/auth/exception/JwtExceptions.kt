package com.celuveat.auth.exception

import com.celuveat.common.exception.CeluveatException
import org.springframework.http.HttpStatus

sealed class JwtExceptions(
    status: HttpStatus,
    errorMessage: String,
) : CeluveatException(status, errorMessage)

data object InvalidJwtTokenException : JwtExceptions(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다") {
    private fun readResolve(): Any = InvalidJwtTokenException
}

data class NoSuchClaimException(
    val key: String,
) : JwtExceptions(HttpStatus.BAD_REQUEST, "토큰에 $key 클레임이 없습니다")
