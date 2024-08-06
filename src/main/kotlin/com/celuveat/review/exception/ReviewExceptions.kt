package com.celuveat.review.exception

import com.celuveat.common.exception.CeluveatException
import org.springframework.http.HttpStatus


sealed class ReviewExceptions(
    status: HttpStatus,
    errorMessage: String,
) : CeluveatException(status, errorMessage)

data object NotFoundReviewException : ReviewExceptions(HttpStatus.NOT_FOUND, "존재 하지 않는 리뷰입니다.") {
    private fun readResolve(): Any = NotFoundReviewException
}

data object NoAuthorityReviewException : ReviewExceptions(HttpStatus.FORBIDDEN, "해당 리뷰에 대한 권한이 없습니다.") {
    private fun readResolve(): Any = NoAuthorityReviewException
}
