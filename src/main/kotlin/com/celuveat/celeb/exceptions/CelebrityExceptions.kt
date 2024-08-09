package com.celuveat.celeb.exceptions

import com.celuveat.common.exception.CeluveatException
import org.springframework.http.HttpStatus

sealed class CelebrityExceptions(
    status: HttpStatus,
    errorMessage: String,
) : CeluveatException(status, errorMessage)

data object NotFoundCelebrityException : CelebrityExceptions(HttpStatus.NOT_FOUND, "존재 하지 않는 셀럽입니다.") {
    private fun readResolve(): Any = NotFoundCelebrityException
}

data object NotFoundInterestedCelebrityException : CelebrityExceptions(HttpStatus.NOT_FOUND, "관심 셀럽을 찾을 수 없습니다.") {
    private fun readResolve(): Any = NotFoundInterestedCelebrityException
}

data object AlreadyInterestedCelebrityException : CelebrityExceptions(HttpStatus.BAD_REQUEST, "이미 관심 셀럽으로 등록된 셀럽입니다.") {
    private fun readResolve(): Any = AlreadyInterestedCelebrityException
}
