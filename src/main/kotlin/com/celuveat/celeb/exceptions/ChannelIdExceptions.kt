package com.celuveat.celeb.exceptions

import com.celuveat.common.exception.CeluveatException
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.BAD_REQUEST

sealed class ChannelIdException(
    status: HttpStatus,
    errorMessage: String,
) : CeluveatException(status, errorMessage)

data object InvalidChannelIdException : ChannelIdException(BAD_REQUEST, "유효하지 않은 채널 ID입니다") {
    private fun readResolve(): Any = InvalidChannelIdException
}
