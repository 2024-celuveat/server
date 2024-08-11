package com.celuveat.review.application.port.`in`

import com.celuveat.review.application.port.`in`.result.SingleReviewResult

interface ReadSingleReviewUseCase {
    fun read(id: Long): SingleReviewResult
}
